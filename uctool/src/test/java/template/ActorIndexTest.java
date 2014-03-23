package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ActorIndexTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "actor-index/full.xml"), outputDir);
		performTest("unit/actor-index/full.html", "site/out/actor/index.html");
	}

}
