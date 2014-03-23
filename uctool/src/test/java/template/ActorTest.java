package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ActorTest extends TemplateTestBase {

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "actor/minimal.xml"), outputDir);
		performTest("unit/actor/minimal.html", "site/out/actor/minimal.html");
	}

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "actor/full.xml"), outputDir);
		performTest("unit/actor/full.html", "site/out/actor/full.html");
	}

}
