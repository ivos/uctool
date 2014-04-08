package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class UseCaseTest extends TemplateTestBase {

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "uc/minimal.xml"), outputDir);
		performTest("unit/uc/minimal.html", "site/out/uc/minimal.html");
	}

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "uc/full.xml"), outputDir);
		performTest("unit/uc/full.html", "site/out/uc/full.html");
	}

	@Test
	public void description() throws IOException {
		e.execute(new File(inputBaseDir, "uc/description.xml"), outputDir);
		performTest("unit/uc/description.html", "site/out/uc/description.html");
	}

	@Test
	public void interests() throws IOException {
		e.execute(new File(inputBaseDir, "uc/interests.xml"), outputDir);
		performTest("unit/uc/interests.html", "site/out/uc/interests.html");
	}

	@Test
	public void preconditions() throws IOException {
		e.execute(new File(inputBaseDir, "uc/preconditions.xml"), outputDir);
		performTest("unit/uc/preconditions.html",
				"site/out/uc/preconditions.html");
	}

	@Test
	public void minGuarantees() throws IOException {
		e.execute(new File(inputBaseDir, "uc/min-guarantees.xml"), outputDir);
		performTest("unit/uc/min-guarantees.html",
				"site/out/uc/min-guarantees.html");
	}

	@Test
	public void successGuarantees() throws IOException {
		e.execute(new File(inputBaseDir, "uc/success-guarantees.xml"),
				outputDir);
		performTest("unit/uc/success-guarantees.html",
				"site/out/uc/success-guarantees.html");
	}

}
