package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class EntryPointListTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "entry-point-list/full.xml"),
				outputDir);
		performTest("unit/entry-point-list/full.html",
				"site/out/summary/entry-point-list.html");
	}

}
