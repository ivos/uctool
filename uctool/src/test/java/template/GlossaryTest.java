package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class GlossaryTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "glossary/full.xml"), outputDir);
		performTest("unit/glossary/full.html", "site/out/glossary/index.html");
	}

}
