package net.sf.uctool.common;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

import net.sf.uctool.exception.ReaderException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.execute.InputFileFinder;
import net.sf.uctool.execute.Project;
import net.sf.uctool.input.UctoolReader;
import net.sf.uctool.validate.UctoolValidator;
import net.sf.uctool.xsd.Uct;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;

public abstract class ExecutorBase {

	protected abstract Logger getLogger();

	protected final StopWatch timeAll, time;

	protected final ResourceBundle labels;
	protected final UctoolReader uctoolReader;
	protected final ExecutionContext executionContext;
	protected final UctoolValidator uctoolValidator;

	public ExecutorBase(Project project) {
		time = new StopWatch();
		timeAll = new StopWatch();

		labels = PropertyResourceBundle.getBundle("translations/Resource",
				new Locale(project.getLanguage()));
		uctoolReader = new UctoolReader(project.getEncoding()).init();
		executionContext = new ExecutionContext(labels, project);
		uctoolValidator = new UctoolValidator(executionContext);
	}

	protected List<File> findInputFiles(File inputPath) {
		getLogger().debug("Finding input files.");
		time.start();
		InputFileFinder inputFileFinder = new InputFileFinder(inputPath,
				"*.xml");
		List<File> inputFiles = inputFileFinder.getInputFiles();
		int filesCount = inputFiles.size();
		getLogger().debug("Found {} input file(s) @ {}.", filesCount,
				time.toString());
		time.reset();
		if (0 == filesCount) {
			throw new ReaderException("No input files found at path ["
					+ inputPath + "].");
		}
		return inputFiles;
	}

	protected List<Uct> readInputFiles(List<File> inputFiles) {
		getLogger().debug("Reading input files.");
		time.start();
		List<Uct> inputs = new ArrayList<Uct>();
		for (File inputFile : inputFiles) {
			Uct uct = uctoolReader.read(inputFile);
			inputs.add(uct);
		}
		getLogger().debug("Read input files @ {}.", time.toString());
		time.reset();
		return inputs;
	}

	protected void validateInputs(List<Uct> inputs) {
		getLogger().debug("Validating inputs.");
		time.start();
		uctoolValidator.validate(inputs);
		getLogger().debug("Validated inputs @ {}.", time.toString());
		getLogger()
				.debug("Loaded {} actor(s), {} use case(s), {} data(s), {} instance(s).",
						executionContext.getActors().size(),
						executionContext.getUseCases().size(),
						executionContext.getDatas().size(),
						executionContext.getInstances().size());
		time.reset();
	}

}
