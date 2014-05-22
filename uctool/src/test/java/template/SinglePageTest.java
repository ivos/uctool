package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class SinglePageTest extends TemplateTestBase {

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "single/minimal.xml"), outputDir);
		performTest("unit/single/minimal.html", "site/out/single/index.html");
	}

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "single/full.xml"), outputDir);
		performTest("unit/single/full.html", "site/out/single/index.html");
	}

}
