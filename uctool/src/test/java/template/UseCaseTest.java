package template;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.uctool.exception.ValidationException;

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

	@Test
	public void steps() throws IOException {
		e.execute(new File(inputBaseDir, "uc/steps.xml"), outputDir);
		performTest("unit/uc/steps.html", "site/out/uc/steps.html");
	}

	@Test
	public void notes() throws IOException {
		e.execute(new File(inputBaseDir, "uc/notes.xml"), outputDir);
		performTest("unit/uc/notes.html", "site/out/uc/notes.html");
	}

	@Test
	public void references() throws IOException {
		e.execute(new File(inputBaseDir, "uc/references.xml"), outputDir);
		performTest("unit/uc/references.html", "site/out/uc/references.html");
	}

	@Test
	public void refcodeReferences() throws IOException {
		// references to UC with refcode
		e.execute(new File(inputBaseDir, "uc/refcode-references.xml"),
				outputDir);
		performTest("unit/uc/refcode-references.html",
				"site/out/uc/references.html");
	}

	@Test
	public void actorMissing() throws IOException {
		try {
			e.execute(new File(inputBaseDir, "uc/actorMissing.xml"), outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing actor with code [missing] referenced from use case with code [UC1].",
					e.getMessage());
		}
	}

	@Test
	public void ucMissing() throws IOException {
		try {
			e.execute(new File(inputBaseDir, "uc/ucMissing.xml"), outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing use case with refcode [missing] referenced from use case with code [UC1].",
					e.getMessage());
		}
	}

	@Test
	public void stepMissingInStep() throws IOException {
		try {
			e.execute(new File(inputBaseDir, "uc/stepMissingInStep.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing step with handle [missing] referenced from use case with code [UC1].",
					e.getMessage());
		}
	}

	@Test
	public void stepMissingInCondition() throws IOException {
		try {
			e.execute(new File(inputBaseDir, "uc/stepMissingInCondition.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing step with handle [missing] referenced from extension in use case [UC1].",
					e.getMessage());
		}
	}

	@Test
	public void stepMissingInRange() throws IOException {
		try {
			e.execute(new File(inputBaseDir, "uc/stepMissingInRange.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing step with handle [missing] referenced from extension in use case [UC1].",
					e.getMessage());
		}
	}

}
