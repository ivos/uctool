package template;

import java.io.File;

import net.sf.seaf.test.util.TemplatingTestBase;
import net.sf.uctool.execute.Project;
import net.sf.uctool.execute.UctoolExecutor;

import org.apache.commons.io.FileUtils;
import org.junit.Before;

public class TemplateTestBase extends TemplatingTestBase implements
		TemplateTestConstants {

	UctoolExecutor e;
	File outputDir;
	File inputBaseDir;

	public TemplateTestBase() {
		super(REPLACE_TEMPLATE);
	}

	@Before
	public void cleanExecute() {
		Project project = new Project("test-name", "test-version",
				"test-description", "en");
		e = new UctoolExecutor(project);
		inputBaseDir = new File("src/test/ucs/unit");
		outputDir = new File("target/site/out");
		FileUtils.deleteQuietly(outputDir);
		outputDir.mkdirs();
	}

}
