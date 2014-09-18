package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class DataIndexTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "data-index/full.xml"), outputDir);
		performTest("unit/data-index/full.html", "site/out/data/index.html");
	}

}
