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

import net.sf.uctool.common.ExecutorBase;
import net.sf.uctool.convert.ActorConverter;
import net.sf.uctool.convert.AttributeConverter;
import net.sf.uctool.convert.DataConverter;
import net.sf.uctool.convert.InstanceConverter;
import net.sf.uctool.convert.TermConverter;
import net.sf.uctool.convert.UseCaseConverter;
import net.sf.uctool.exception.ResourcesException;
import net.sf.uctool.output.UctoolWriter;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.output.data.InstanceOut;
import net.sf.uctool.output.term.TermOut;
import net.sf.uctool.output.uc.UseCaseOut;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.Instance;
import net.sf.uctool.xsd.Term;
import net.sf.uctool.xsd.Uct;
import net.sf.uctool.xsd.UseCase;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UctoolExecutor extends ExecutorBase {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ActorConverter actorConverter;
	private final UseCaseConverter useCaseConverter;
	private final DataConverter dataConverter;
	private final AttributeConverter attributeConverter;
	private final InstanceConverter instanceConverter;
	private final TermConverter termConverter;
	private final UctoolWriter uctoolWriter;
	private final KeyDataManager keyDataManager;

	public UctoolExecutor(Project project) {
		super(project);
		actorConverter = new ActorConverter(executionContext);
		useCaseConverter = new UseCaseConverter(executionContext);
		attributeConverter = new AttributeConverter(executionContext);
		dataConverter = new DataConverter(executionContext, attributeConverter);
		instanceConverter = new InstanceConverter(executionContext);
		termConverter = new TermConverter(executionContext);
		uctoolWriter = new UctoolWriter(project.getEncoding());
		keyDataManager = new KeyDataManager(executionContext);
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void validate(Reader reader) {
		timeAll.start();
		logger.info("Validating from reader stream.");

		List<Uct> inputs = readReader(reader);
		validateInputs(inputs);
		convertToOutputs(executionContext.getOutputs());

		timeAll.stop();
		logger.info("Validated in {}.", timeAll.toString());
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
		writeOutputs(outputDir);
		executionContext.getTermOuts().clear();

		executionContext.setSingle(true);
		convertToOutputs(executionContext.getOutputsSinglePage());
		executionContext.setSingle(false);
		writeSinglePageOutputs(outputDir);

		extractResources(outputDir);

		timeAll.stop();
		logger.info("Executed UCTool into {} in {}.", outputDir,
				timeAll.toString());
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
			executionContext.getUseCaseOuts().put(useCaseOut.getRefcode(),
					useCaseOut);
		}
		for (Data data : executionContext.getDatas().values()) {
			DataOut dataOut = dataConverter.convert(data);
			outputs.add(dataOut);
			executionContext.getDataOuts().put(dataOut.getRefcode(), dataOut);
		}
		for (Instance instance : executionContext.getInstances().values()) {
			InstanceOut instanceOut = instanceConverter.convert(instance);
			outputs.add(instanceOut);
			executionContext.getInstanceOuts().put(instanceOut.getRefcode(),
					instanceOut);
		}
		for (Term term : executionContext.getTerms()) {
			TermOut termOut = termConverter.convert(term);
			executionContext.getTermOuts().add(termOut);
		}

		for (Object output : outputs) {
			if (output instanceof UseCaseOut) {
				useCaseConverter.addReferences((UseCaseOut) output);
			} else if (output instanceof DataOut) {
				dataConverter.addReferences((DataOut) output);
			} else if (output instanceof InstanceOut) {
				instanceConverter.addReferences((InstanceOut) output);
			}
		}

		for (UseCaseOut useCaseOut : executionContext.getUseCaseOuts().values()) {
			if (useCaseOut.getReferences().isEmpty()) {
				executionContext.getEntryPoints().add(useCaseOut);
			}
		}
		for (DataOut dataOut : executionContext.getDataOuts().values()) {
			if (dataOut.getInstances().isEmpty()
					&& dataOut.getReferencesData().isEmpty()
					&& dataOut.getReferencesInstances().isEmpty()
					&& dataOut.getReferencesUcs().isEmpty()) {
				executionContext.getUnusedData().add(dataOut);
			}
		}
		for (InstanceOut instanceOut : executionContext.getInstanceOuts()
				.values()) {
			if (instanceOut.getReferencesData().isEmpty()
					&& instanceOut.getReferencesInstances().isEmpty()
					&& instanceOut.getReferencesUcs().isEmpty()) {
				executionContext.getUnusedInstances().add(instanceOut);
			}
		}
		keyDataManager.setupKeyData();

		logger.debug("Converted to {} outputs @ {}.", outputs.size(),
				time.toString());
		time.reset();
	}

	private void writeOutputs(File outputDir) {
		List<Object> outputs = executionContext.getOutputs();
		logger.debug("Writing {} outputs.", outputs.size());
		time.start();
		uctoolWriter.init(outputDir, executionContext);
		for (Object output : outputs) {
			uctoolWriter.write(output);
		}
		uctoolWriter.writeIndex();
		uctoolWriter.writeActorIndex();
		uctoolWriter.writeUseCaseIndex();
		uctoolWriter.writeDataIndex();
		uctoolWriter.writeInstanceIndex();
		uctoolWriter.writeGlossary();
		logger.debug("Wrote outputs @ {}.", time.toString());
		time.reset();
	}

	private void writeSinglePageOutputs(File outputDir) {
		logger.debug("Writing single page outputs.");
		time.start();
		uctoolWriter.writeSinglePage();
		logger.debug("Wrote single page outputs @ {}.", time.toString());
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
				"white-system.gif");
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
