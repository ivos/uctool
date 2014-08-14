package net.sf.uctool.execute;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.sf.uctool.convert.ActorConverter;
import net.sf.uctool.convert.DataConverter;
import net.sf.uctool.convert.UseCaseConverter;
import net.sf.uctool.exception.ReaderException;
import net.sf.uctool.exception.ResourcesException;
import net.sf.uctool.input.UctoolReader;
import net.sf.uctool.output.UctoolWriter;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.output.uc.UseCaseOut;
import net.sf.uctool.validate.UctoolValidator;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.Uct;
import net.sf.uctool.xsd.UseCase;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UctoolExecutor {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final StopWatch timeAll, time;

	private final ResourceBundle labels;
	private final UctoolReader uctoolReader;
	private final ExecutionContext executionContext;
	private final ActorConverter actorConverter;
	private final UseCaseConverter useCaseConverter;
	private final DataConverter dataConverter;
	private final UctoolWriter uctoolWriter;
	private final UctoolValidator uctoolValidator;

	public UctoolExecutor(Project project) {
		time = new StopWatch();
		timeAll = new StopWatch();
		labels = PropertyResourceBundle
				.getBundle("generator/uctool/translations/Resource");
		uctoolReader = new UctoolReader().init();
		executionContext = new ExecutionContext(labels, project);
		uctoolValidator = new UctoolValidator(executionContext);
		actorConverter = new ActorConverter(executionContext);
		useCaseConverter = new UseCaseConverter(executionContext);
		dataConverter = new DataConverter(executionContext);
		uctoolWriter = new UctoolWriter();
	}

	public void execute(Reader reader, File outputDir) {
		timeAll.start();
		logger.info("Executing UCTool from reader stream.");

		List<Uct> inputs = readReader(reader);
		executeInternal(inputs, outputDir);
	}

	public void execute(File inputPath, File outputDir) {
		timeAll.start();
		logger.info("Executing UCTool for {}.", inputPath);

		List<File> inputFiles = findInputFiles(inputPath);
		List<Uct> inputs = readInputFiles(inputFiles);
		executeInternal(inputs, outputDir);
	}

	private void executeInternal(List<Uct> inputs, File outputDir) {
		validateInputs(inputs);
		convertToOutputs(executionContext.getOutputs());
		executionContext.setSingle(true);
		convertToOutputs(executionContext.getOutputsSinglePage());
		executionContext.setSingle(false);
		writeOutputs(outputDir);

		extractResources(outputDir);

		timeAll.stop();
		logger.info("Executed UCTool into {} in {}.", outputDir,
				timeAll.toString());
	}

	private List<File> findInputFiles(File inputPath) {
		logger.debug("Finding input files.");
		time.start();
		InputFileFinder inputFileFinder = new InputFileFinder(inputPath,
				"*.xml");
		List<File> inputFiles = inputFileFinder.getInputFiles();
		int filesCount = inputFiles.size();
		logger.debug("Found {} input file(s) @ {}.", filesCount,
				time.toString());
		time.reset();
		if (0 == filesCount) {
			throw new ReaderException("No input files found at path ["
					+ inputPath + "].");
		}
		return inputFiles;
	}

	private List<Uct> readInputFiles(List<File> inputFiles) {
		logger.debug("Reading input files.");
		time.start();
		List<Uct> inputs = new ArrayList<Uct>();
		for (File inputFile : inputFiles) {
			Uct uct = uctoolReader.read(inputFile);
			inputs.add(uct);
		}
		logger.debug("Read input files @ {}.", time.toString());
		time.reset();
		return inputs;
	}

	private List<Uct> readReader(Reader reader) {
		logger.debug("Reading reader stream.");
		time.start();
		List<Uct> inputs = new ArrayList<Uct>();
		Uct uct = uctoolReader.read(reader, "reader stream");
		inputs.add(uct);
		logger.debug("Read reader stream @ {}.", time.toString());
		time.reset();
		return inputs;
	}

	private void validateInputs(List<Uct> inputs) {
		logger.debug("Validating inputs.");
		time.start();
		uctoolValidator.validate(inputs);
		logger.debug("Validated inputs @ {}.", time.toString());
		logger.debug("Loaded {} actor(s), {} use case(s), {} data(s).",
				executionContext.getActors().size(), executionContext
						.getUseCases().size(), executionContext.getDatas()
						.size());
		time.reset();
	}

	private void convertToOutputs(List<Object> outputs) {
		logger.debug("Converting to outputs.");
		time.start();
		for (Actor actor : executionContext.getActors().values()) {
			ActorOut actorOut = actorConverter.convert(actor);
			outputs.add(actorOut);
			executionContext.getActorOuts().put(actorOut.getCode(), actorOut);
		}
		for (UseCase useCase : executionContext.getUseCases().values()) {
			UseCaseOut useCaseOut = useCaseConverter.convert(useCase);
			outputs.add(useCaseOut);
			executionContext.getUseCaseOuts().put(useCaseOut.getCode(),
					useCaseOut);
		}
		for (Data data : executionContext.getDatas().values()) {
			DataOut dataOut = dataConverter.convert(data);
			outputs.add(dataOut);
			executionContext.getDataOuts().put(dataOut.getCode(), dataOut);
		}

		for (Object output : outputs) {
			if (output instanceof UseCaseOut) {
				useCaseConverter.addReferences((UseCaseOut) output);
			} else if (output instanceof DataOut) {
				dataConverter.addReferences((DataOut) output);
			}
		}
		logger.debug("Converted to {} outputs @ {}.", outputs.size(),
				time.toString());
		time.reset();
	}

	private void writeOutputs(File outputDir) {
		List<Object> outputs = executionContext.getOutputs();
		logger.debug("Writing {} outputs.", outputs.size());
		time.start();
		uctoolWriter.init(outputDir, executionContext);
		uctoolWriter.writeIndex();
		uctoolWriter.writeActorIndex();
		uctoolWriter.writeUseCaseIndex();
		uctoolWriter.writeSummaryIndex();
		uctoolWriter.writeEntryPointList();
		uctoolWriter.writeSinglePage();
		for (Object output : outputs) {
			uctoolWriter.write(output);
		}
		logger.debug("Wrote outputs @ {}.", time.toString());
		time.reset();
	}

	private void extractResources(File outputDir) {
		logger.debug("Extracting resources...");
		time.start();

		ClassLoader classLoader = Thread.currentThread()
				.getContextClassLoader();
		String base = "uctool-resources/";
		List<String> names = Arrays.asList("black-design.gif",
				"black-organization.gif", "black-system.gif", "clam.gif",
				"cloud.gif", "favicon.ico", "fish.gif", "kite.gif", "sea.gif",
				"white-design.gif", "white-organization.gif",
				"white-system.gif", "css/styles.css",
				"font/fontawesome-webfont.eot", "font/fontawesome-webfont.svg",
				"font/fontawesome-webfont.ttf",
				"font/fontawesome-webfont.woff", "js/scripts.js");
		File outputBaseDir = new File(outputDir, "resources");

		try {
			for (String name : names) {
				InputStream inputStream = classLoader.getResourceAsStream(base
						+ name);
				File outputFile = new File(outputBaseDir, name);
				outputFile.getParentFile().mkdirs();
				OutputStream outputStream = new FileOutputStream(outputFile);
				logger.debug("Extracting resource {} into {}.", name,
						outputFile);
				IOUtils.copy(inputStream, outputStream);
			}
		} catch (IOException e) {
			throw new ResourcesException("Cannot extract resources.", e);
		}

		logger.debug("Extracted resources @ {}.", time.toString());
		time.reset();
	}

}
