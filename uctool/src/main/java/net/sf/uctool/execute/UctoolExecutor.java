package net.sf.uctool.execute;

import java.io.File;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import net.sf.uctool.convert.ActorConverter;
import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.input.UctoolReader;
import net.sf.uctool.output.UctoolWriter;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Attachment;
import net.sf.uctool.xsd.AttachmentGroup;
import net.sf.uctool.xsd.DataStructure;
import net.sf.uctool.xsd.Requirement;
import net.sf.uctool.xsd.Uct;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UctoolExecutor {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final StopWatch timeAll, time;

	private final UctoolReader uctoolReader;
	private final ExecutionContext executionContext;
	private final ActorConverter actorConverter;
	private final UctoolWriter uctoolWriter;

	public UctoolExecutor() {
		time = new StopWatch();
		timeAll = new StopWatch();
		uctoolReader = new UctoolReader().init();
		executionContext = new ExecutionContext();
		actorConverter = new ActorConverter(executionContext);
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
		logger.debug("Found {} input file(s) @ {}.", inputFiles.size(),
				time.toString());
		time.reset();
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
		for (Uct uct : inputs) {
			for (Object object : uct.getTermOrRequirementOrUcGroup()) {
				if (object instanceof Actor) {
					Actor actor = (Actor) object;
					String code = actor.getCode();
					if (executionContext.getActors().containsKey(code)) {
						throw new ValidationException(
								"Duplicate actor with code [" + code + "].");
					}
					executionContext.getActors().put(code, actor);
				}
				if (object instanceof AttachmentGroup) {
					AttachmentGroup attachmentGroup = (AttachmentGroup) object;
					for (Attachment attachment : attachmentGroup
							.getAttachment()) {
						String code = attachment.getCode();
						if (executionContext.getAttachments().containsKey(code)) {
							throw new ValidationException(
									"Duplicate attachment with code [" + code
											+ "].");
						}
						executionContext.getAttachments().put(code, attachment);
						executionContext.getAttachmentGroups().put(code,
								attachmentGroup);
					}
				}
				if (object instanceof DataStructure) {
					DataStructure dataStructure = (DataStructure) object;
					String code = dataStructure.getCode();
					if (executionContext.getAttachments().containsKey(code)) {
						throw new ValidationException(
								"Duplicate data structure with code [" + code
										+ "].");
					}
					executionContext.getDataStructures().put(code,
							dataStructure);
				}
				if (object instanceof Requirement) {
					Requirement requirement = (Requirement) object;
					String code = requirement.getCode();
					if (executionContext.getRequirements().containsKey(code)) {
						throw new ValidationException(
								"Duplicate requirement with code [" + code
										+ "].");
					}
					executionContext.getRequirements().put(code, requirement);
				}
			}
		}
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
		logger.debug("Converted to {} outputs @ {}.", outputs.size(),
				time.toString());
		time.reset();
		return outputs;
	}

	private void writeOutputs(List<Object> outputs, File outputDir) {
		logger.debug("Writing {} outputs.", outputs.size());
		time.start();
		uctoolWriter.init(outputDir);
		for (Object output : outputs) {
			uctoolWriter.write(output);
		}
		logger.debug("Wrote outputs @ {}.", time.toString());
		time.reset();
	}

}
