package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class IndexTest extends TemplateTestBase {

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "index/minimal.xml"), outputDir);
		performTest("unit/index/minimal.html", "site/out/index.html");
	}

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "index/full.xml"), outputDir);
		performTest("unit/index/full.html", "site/out/index.html");
	}

}
