package template;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;

import net.sf.uctool.exception.ValidationException;

import org.junit.Test;

public class DataTest extends TemplateTestBase {

	@Test
	public void minimal() throws IOException {
		e.execute(new File(inputBaseDir, "data/minimal.xml"), outputDir);
		performTest("unit/data/minimal.html", "site/out/data/minimal.html");
	}

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "data/full.xml"), outputDir);
		performTest("unit/data/full.html", "site/out/data/full.html");
	}

	@Test
	public void references() throws IOException {
		e.execute(new File(inputBaseDir, "data/references.xml"), outputDir);
		performTest("unit/data/references.html",
				"site/out/data/references.html");
	}

	@Test
	public void references_refc() throws IOException {
		e.execute(new File(inputBaseDir, "data/references_refc.xml"), outputDir);
		performTest("unit/data/references_refc.html",
				"site/out/data/dt_code.html");
	}

	@Test
	public void val_DuplicateData() {
		try {
			e.execute(new File(inputBaseDir, "data/duplicateData.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Duplicate data or instance with code [d1], refcode [d1].",
					e.getMessage());
		}
	}

	@Test
	public void val_DuplicateAttribute() {
		try {
			e.execute(new File(inputBaseDir, "data/duplicateAttribute.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Duplicate attribute with code [at1] on data with code [d1].",
					e.getMessage());
		}
	}

	@Test
	public void val_DataMissing() {
		try {
			e.execute(new File(inputBaseDir, "data/dataMissing.xml"), outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing data or instance with refcode [missing] referenced from data with code [d1].",
					e.getMessage());
		}
	}

	@Test
	public void val_Attribute_DataMissing() {
		try {
			e.execute(new File(inputBaseDir, "data/attributeDataMissing.xml"),
					outputDir);
		} catch (ValidationException e) {
			assertEquals(
					"Missing data with refcode [missing] referenced from data with code [d1].",
					e.getMessage());
		}
	}

}
