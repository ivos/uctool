package net.sf.uctool.execute;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.sf.uctool.exception.ReaderException;

import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.filefilter.WildcardFileFilter;

public class InputFileFinder extends DirectoryWalker<File> {

	private final File baseDir;

	public InputFileFinder(File baseDir, String filter) {
		super(new WildcardFileFilter(filter), -1);
		this.baseDir = baseDir;
	}

	public List<File> getInputFiles() {
		List<File> results = new ArrayList<File>();
		if (baseDir.isFile()) {
			results.add(baseDir);
			return results;
		}
		try {
			walk(baseDir, results);
			return results;
		} catch (IOException e) {
			throw new ReaderException("Error locating input files.", e);
		}
	}

	@Override
	protected void handleFile(File file, int depth, Collection<File> results)
			throws IOException {
		results.add(file);
	}

}
