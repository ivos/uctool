package net.sf.uctool.mojo;

import java.io.File;

import net.sf.uctool.execute.Project;
import net.sf.uctool.execute.UctoolExecutor;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;

/**
 * Generate use case documentation.
 * 
 * @goal use-cases
 * @phase generate-resources
 * @requiresDependencyResolution compile
 */
public class UseCasesMojo extends AbstractMojo {

	/**
	 * Read the source files (XSL templates and XML transformation sources)
	 * using this encoding.
	 * 
	 * @parameter default-value="${project.build.sourceEncoding}"
	 */
	private String sourceEncoding;

	/**
	 * Set name to display on the start HTML page.
	 * 
	 * @parameter default-value= "${project.name}"
	 */
	private String useCasesName;

	/**
	 * Set version to display on the start HTML page.
	 * 
	 * @parameter default-value= "${project.version}"
	 */
	private String useCasesVersion;

	/**
	 * Set project description to display on the start HTML page.
	 * 
	 * @parameter default-value= "${project.description}"
	 */
	private String useCasesDescription;

	/**
	 * Set language to load the resource bundle with fixed texts translations.
	 * When not specified, the default locale language is used.
	 * 
	 * @parameter
	 */
	private String bundleLanguage;

	/**
	 * The directory holding the use case XML source files.
	 * 
	 * @parameter default-value="${project.basedir}/src/main/ucs/"
	 * @required
	 */
	private File useCasesDirectory;

	/**
	 * The output directory. The HTML use case documentation is generated there.
	 * 
	 * @parameter default-value="${project.build.directory}/site/ucs/"
	 * @required
	 */
	private File outputDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {
		UctoolExecutor uctoolExecutor = new UctoolExecutor(new Project(
				useCasesName, useCasesVersion, useCasesDescription,
				sourceEncoding, bundleLanguage));
		uctoolExecutor.execute(useCasesDirectory, outputDirectory);
	}

}
