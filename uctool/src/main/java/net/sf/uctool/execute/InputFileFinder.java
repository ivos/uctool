package net.sf.uctool.execute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import net.sf.uctool.exception.ReaderException;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InputFileFinder extends DirectoryWalker<File> {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final File baseDir;
	private final String fileFilter;

	public InputFileFinder(File baseDir, String fileFilter) {
		this.baseDir = baseDir;
		this.fileFilter = fileFilter;
	}

	public List<File> getInputFiles() {
		List<File> results = new ArrayList<File>();
		if (baseDir.isFile()) {
			logger.debug(
					"The base directory is a file. Returning the file as a single input file {}.",
					baseDir);
			results.add(baseDir);
			return results;
		}
		try {
			logger.debug("Walking diretory {}.", baseDir);
			walk(baseDir, results);
			return results;
		} catch (IOException e) {
			throw new ReaderException("Error locating input files.", e);
		}
	}

	@Override
	protected void handleFile(File file, int depth, Collection<File> results)
			throws IOException {
		if (FilenameUtils.wildcardMatch(file.getName(), fileFilter,
				IOCase.INSENSITIVE)) {
			logger.debug("Adding input file {}.", file);
			results.add(file);
		} else {
			logger.debug("Skipping input file {}.", file);
		}
	}

	@Override
	protected File[] filterDirectoryContents(File directory, int depth,
			File[] files) throws IOException {
		if (null != files) {
			Arrays.sort(files);
		}
		return files;
	}

}
