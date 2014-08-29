package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class InstanceTest extends TemplateTestBase {

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "instance/minimal.xml"), outputDir);
		performTest("unit/instance/minimal.html",
				"site/out/instance/minimal.html");
	}

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "instance/full.xml"), outputDir);
		performTest("unit/instance/full.html", "site/out/instance/full.html");
	}

}
