package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class InstanceIndexTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "instance-index/full.xml"), outputDir);
		performTest("unit/instance-index/full.html",
				"site/out/instance/index.html");
	}

}
