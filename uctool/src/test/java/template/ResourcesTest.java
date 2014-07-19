package template;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ResourcesTest extends TemplateTestBase {

	@Test
	public void fn() throws IOException {
		e.execute(new File(inputBaseDir, "empty.xml"), outputDir);
		assertTrue("File in base resources/ dir exists", new File(outputDir,
				"resources/sea.gif").exists());
		assertTrue("File in subdir of resources/ exists", new File(outputDir,
				"resources/css/styles.css").exists());
	}

}
