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
	 * Set protocol to use to load files from CDNs. When not specified, no
	 * explicit protocol is used, which effectively defaults to the same
	 * protocol through which is accessed the HTML page itself. This should be
	 * the default when hosting generated site on a web server. It will,
	 * however, fail to load the files (styles, etc.) when browsing the
	 * generated site directly from filesystem (using protocol <code>file</code>
	 * ), in such a case, use protocol <code>http</code>.
	 * 
	 * @parameter
	 */
	private String cdnProtocol;

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

	/**
	 * The directory holding sub-directories with the attachment files.
	 * 
	 * @parameter default-value="${project.basedir}/src/main/attachments/"
	 * @required
	 */
	private File attachmentsDirectory;

	public void execute() throws MojoExecutionException, MojoFailureException {
		UctoolExecutor uctoolExecutor = new UctoolExecutor(new Project(
				useCasesName, useCasesVersion, useCasesDescription,
				sourceEncoding, bundleLanguage, cdnProtocol));
		uctoolExecutor.execute(useCasesDirectory, outputDirectory);
	}

}
