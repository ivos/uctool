package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class UseCaseIndexTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "uc-index/full.xml"), outputDir);
		performTest("unit/uc-index/full.html", "site/out/uc/index.html");
	}

}
