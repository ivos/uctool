package net.sf.uctool.mojo;

import java.io.File;

import net.sf.uctool.xsd.import_.UctoolXsdImportXsl;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Generate UC Tool data structures from XSD files.
 * 
 * @goal import-xsd
 * @phase generate-resources
 * @requiresDependencyResolution compile
 */
public class ImportXsdMojo extends AbstractMojo {

	/**
	 * Read the source files (XSL templates and XML transformation sources)
	 * using this encoding.
	 * 
	 * @parameter default-value="${project.build.sourceEncoding}"
	 */
	private String sourceEncoding;

	/**
	 * The directory holding the XSD files to be imported.
	 * 
	 * @parameter default-value="${project.basedir}/src/main/resources/xsd/"
	 * @required
	 */
	private File xsdDirectory;

	/**
	 * The output directory where the UC Tool XML source files with data
	 * structures are generated.
	 * 
	 * @parameter default-value="${project.basedir}/src/main/ucs/xsd-import/"
	 * @required
	 */
	private File outputDataStructuresDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {
		UctoolXsdImportXsl uctoolXsdImportXsl = new UctoolXsdImportXsl(
				sourceEncoding);
		uctoolXsdImportXsl.execute(xsdDirectory, outputDataStructuresDirectory);
	}

}
