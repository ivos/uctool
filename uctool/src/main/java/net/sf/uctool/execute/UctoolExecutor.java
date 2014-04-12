package net.sf.uctool.execute;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.sf.uctool.convert.ActorConverter;
import net.sf.uctool.convert.UseCaseConverter;
import net.sf.uctool.exception.ReaderException;
import net.sf.uctool.input.UctoolReader;
import net.sf.uctool.output.UctoolWriter;
import net.sf.uctool.output.uc.UseCaseOut;
import net.sf.uctool.validate.UctoolValidator;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Uct;
import net.sf.uctool.xsd.UseCase;

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
		uctoolWriter = new UctoolWriter();
	}

	public void execute(Reader reader, File outputDir) {
		timeAll.start();
		logger.info("Executing UCTool from reader stream.");

		List<Uct> inputs = readReader(reader);
		validateInputs(inputs);
		List<Object> outputs = convertToOutputs();
		writeOutputs(outputs, outputDir);

		logger.info("Executed UCTool into {} in {}.", outputDir,
				timeAll.toString());
		timeAll.stop();
	}

	public void execute(File inputPath, File outputDir) {
		timeAll.start();
		logger.info("Executing UCTool for {}.", inputPath);

		List<File> inputFiles = findInputFiles(inputPath);
		List<Uct> inputs = readInputFiles(inputFiles);
		validateInputs(inputs);
		List<Object> outputs = convertToOutputs();
		writeOutputs(outputs, outputDir);

		logger.info("Executed UCTool into {} in {}.", outputDir,
				timeAll.toString());
		timeAll.stop();
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
		logger.debug("Loaded {} actor(s).", executionContext.getActors().size());
		time.reset();
	}

	private List<Object> convertToOutputs() {
		logger.debug("Converting to outputs.");
		time.start();
		List<Object> outputs = new ArrayList<Object>();
		for (Actor actor : executionContext.getActors().values()) {
			outputs.add(actorConverter.convert(actor));
		}
		for (UseCase useCase : executionContext.getUseCases().values()) {
			outputs.add(useCaseConverter.convert(useCase));
		}

		for (Object output : outputs) {
			if (output instanceof UseCaseOut) {
				useCaseConverter.addReferences((UseCaseOut) output);
			}
		}
		logger.debug("Converted to {} outputs @ {}.", outputs.size(),
				time.toString());
		time.reset();
		return outputs;
	}

	private void writeOutputs(List<Object> outputs, File outputDir) {
		logger.debug("Writing {} outputs.", outputs.size());
		time.start();
		uctoolWriter.init(outputDir, executionContext);
		uctoolWriter.writeIndex();
		uctoolWriter.writeActorIndex();
		for (Object output : outputs) {
			uctoolWriter.write(output);
		}
		logger.debug("Wrote outputs @ {}.", time.toString());
		time.reset();
	}

}
