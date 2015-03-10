package net.sf.uctool.mojo;

import java.io.File;

import net.sf.uctool.xsd.export.UctoolXsdExport;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Generate an XSD file from data structures defined in the use cases.
 * 
 * @goal export-xsd
 * @phase generate-resources
 * @requiresDependencyResolution compile
 */
public class ExportXsdMojo extends AbstractMojo {

	/**
	 * Read the source files (XSL templates and XML transformation sources)
	 * using this encoding.
	 * 
	 * @parameter default-value="${project.build.sourceEncoding}"
	 */
	private String sourceEncoding;

	/**
	 * The directory holding the use case XML source files.
	 * 
	 * @parameter default-value="${project.basedir}/src/main/ucs/"
	 * @required
	 */
	private File useCasesDirectory;

	/**
	 * The target namespace for generated XSD elements.
	 * 
	 * @parameter default-value="http://example.com/"
	 * @required
	 */
	private String targetNamespaceUrl;

	/**
	 * The name of the XSD file to generate.
	 * 
	 * @parameter default-value="${project.build.directory}/site/xsd/data.xsd"
	 * @required
	 */
	private File outputFile;

	public void execute() throws MojoExecutionException, MojoFailureException {
		UctoolXsdExport uctoolXsdExport = new UctoolXsdExport(sourceEncoding,
				targetNamespaceUrl);
		uctoolXsdExport.execute(useCasesDirectory, outputFile);
	}

}
