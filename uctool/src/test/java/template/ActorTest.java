package template;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.uctool.exception.ValidationException;

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

	@Test
	public void val_DuplicateActor() {
		try {
			e.execute(new File(inputBaseDir, "actor/duplicateActor.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals("Duplicate actor with code [a1].", e.getMessage());
		}
	}

	@Test
	public void val_ExtendedMissing() {
		try {
			e.execute(new File(inputBaseDir, "actor/extendedMissing.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing actor with code [missing] referenced from actor with code [a1].",
					e.getMessage());
		}
	}

	@Test
	public void val_AttachmentMissing() {
		try {
			e.execute(new File(inputBaseDir, "actor/attachmentMissing.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing attachment with code [missing] referenced from actor with code [a1].",
					e.getMessage());
		}
	}

	@Test
	public void val_DataMissing() {
		try {
			e.execute(new File(inputBaseDir, "actor/dataMissing.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing data with code [missing] referenced from actor with code [a1].",
					e.getMessage());
		}
	}

	@Test
	public void val_RequirementMissing() {
		try {
			e.execute(new File(inputBaseDir, "actor/requirementMissing.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing requirement with code [missing] referenced from actor with code [a1].",
					e.getMessage());
		}
	}

}
