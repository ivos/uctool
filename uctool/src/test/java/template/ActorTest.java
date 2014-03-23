package template;

import java.io.File;
import java.io.IOException;

import net.sf.seaf.test.util.TemplatingTestBase;
import net.sf.uctool.execute.UctoolExecutor;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class ActorTest extends TemplatingTestBase {

	private static final boolean REPLACE_TEMPLATE = false;

	UctoolExecutor e;
	File outputDir;
	File inputBaseDir;

	public ActorTest() {
		super(REPLACE_TEMPLATE);
	}

	@Before
	public void before() {
		e = new UctoolExecutor();
		inputBaseDir = new File("src/test/ucs/unit");
		outputDir = new File("target/site/out");
		FileUtils.deleteQuietly(outputDir);
		outputDir.mkdirs();
	}

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
