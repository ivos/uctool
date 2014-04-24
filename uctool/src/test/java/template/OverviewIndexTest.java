package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class OverviewIndexTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "overview-index/full.xml"), outputDir);
		performTest("unit/overview-index/full.html",
				"site/out/summary/index.html");
	}

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "empty.xml"), outputDir);
		performTest("unit/overview-index/minimal.html",
				"site/out/summary/index.html");
	}

}
