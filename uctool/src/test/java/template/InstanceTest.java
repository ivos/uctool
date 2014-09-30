package template;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.uctool.exception.ValidationException;

import org.junit.Test;

public class InstanceTest extends TemplateTestBase {

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "instance/minimal.xml"), outputDir);
		performTest("unit/instance/minimal.html",
				"site/out/instance/minimal.html");
	}

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "instance/full.xml"), outputDir);
		performTest("unit/instance/full.html", "site/out/instance/full.html");
	}

	@Test
	public void references() throws IOException {
		e.execute(new File(inputBaseDir, "instance/references.xml"), outputDir);
		performTest("unit/instance/references.html",
				"site/out/instance/references.html");
	}

	@Test
	public void val_DuplicateInstance() {
		try {
			e.execute(new File(inputBaseDir, "instance/duplicateInstance.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Duplicate data or instance with code [in1], refcode [in1].",
					e.getMessage());
		}
	}

	@Test
	public void val_OfDataMissing() {
		try {
			e.execute(new File(inputBaseDir, "instance/ofDataMissing.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals("Instance [in1] refers to unknown data [d2].",
					e.getMessage());
		}
	}

	@Test
	public void val_OfAttributeMissing() {
		try {
			e.execute(
					new File(inputBaseDir, "instance/ofAttributeMissing.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Value on instance [in1] refers to unknown attribute of data [d1] via code [at2].",
					e.getMessage());
		}
	}

	@Test
	public void val_DuplicateValue() {
		try {
			e.execute(new File(inputBaseDir, "instance/duplicateValue.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Duplicate value of [at1] on instance with code [in1].",
					e.getMessage());
		}
	}

}
