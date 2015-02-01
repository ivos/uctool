package template;

import java.io.File;
import java.io.IOException;

import net.sf.seaf.test.util.TemplatingTestBase;
import net.sf.uctool.execute.Project;
import net.sf.uctool.execute.UctoolExecutor;
import net.sf.uctool.xsd.export.UctoolXsdExport;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

public class IntegrationTest extends TemplatingTestBase implements
		TemplateTestConstants {

	public IntegrationTest() {
		super(REPLACE_TEMPLATE);
	}

	@BeforeClass
	public static void execute() {
		Project project = new Project("test-name", "test-version",
				"test-description", "UTF-8", "en");
		new UctoolExecutor(project).execute(
				new File("src/test/ucs/integration"), new File(
						"target/site/out"));
		new UctoolXsdExport(project).execute(new File("src/test/xsd-export"),
				new File("target/site/xsd"));
	}

	@Test
	public void ucStepsAndExtension() throws IOException {
		performTest("uc/code003.html");
	}

	@Test
	public void ucHeaderFieldsAndHtmlFormattedText() throws IOException {
		performTest("uc/code001.html");
	}

	@Test
	public void ucIndex() throws IOException {
		performTest("uc/index.html");
	}

	@Test
	public void actorWoutGoals() throws IOException {
		performTest("actor/no-ucs-primary-actor.html");
	}

	@Test
	public void actorWGoals() throws IOException {
		performTest("actor/primary-actor-with-goals.html");
	}

	@Test
	public void actorWGoalsLinkingToUCs() throws IOException {
		performTest("actor/primary-actor-for-use-cases.html");
	}

	@Test
	public void actorWDescription() throws IOException {
		performTest("actor/actor-description.html");
	}

	@Test
	public void actorIndex() throws IOException {
		performTest("actor/index.html");
	}

	@Test
	public void dataEmpty() throws IOException {
		performTest("data/Empty.html");
	}

	@Test
	public void dataMaster() throws IOException {
		performTest("data/Master.html");
	}

	// @Test
	// public void dataExpansionTest() throws IOException {
	// performTest("data/ExpansionTest.html");
	// }

	// @Test
	// public void dataExpansionReferencesTest() throws IOException {
	// performTest("data/ExpansionReferredStructure.html");
	// }

	@Test
	public void dataDescription() throws IOException {
		performTest("data/description-data.html");
	}

	@Test
	public void dataIndex() throws IOException {
		performTest("data/index.html");
	}

	@Test
	public void dataRefFromUc() throws IOException {
		performTest("uc/mas-003.html");
	}

	// @Test
	// public void dataAttRefE1() throws IOException {
	// performTest("data/DARE1.html");
	// }

	// @Test
	// public void dataAttRefE2() throws IOException {
	// performTest("data/DARE2.html");
	// }

	// @Test
	// public void dataAttRefE3() throws IOException {
	// performTest("data/DARE3.html");
	// }

	// @Test
	// public void dataAttRefF1() throws IOException {
	// performTest("data/DARF1.html");
	// }

	// @Test
	// public void dataAttRefExpanded() throws IOException {
	// performTest("data/expand-att-ref.html");
	// }

	// @Test
	// public void dataAttRefExpansion() throws IOException {
	// performTest("data/att-ref-expansion.html");
	// }

	// @Test
	// public void dataAttRefExpansion2ndLevel() throws IOException {
	// performTest("data/att-ref-expansion-2.html");
	// }

	@Test
	public void single() throws IOException {
		performTest("single/index.html");
	}

	@Test
	public void index() throws IOException {
		performTest("index.html");
	}

	@Test
	public void glossary() throws IOException {
		performTest("glossary/index.html");
	}

	private void performTest(String location) throws IOException {
		performTest("integration/" + location, "site/out/" + location);
	}

	@Test
	public void xsdExport() throws IOException {
		performTest("integration/xsd-export/data.xsd", "site/xsd/data.xsd");
	}

	@Ignore
	@Test
	public void xsdImportData() throws IOException {
		performTest("xsd-import/data.xml", "site/import/data.xml",
				"/xsd/[0-9\\.\\-A-Z]*/uct.xsd", "/xsd/VERSION/uct.xsd");
	}

	@Ignore
	@Test
	public void xsdImportImported() throws IOException {
		performTest("xsd-import/imported.xml", "site/import/imported.xml",
				"/xsd/[0-9\\.\\-A-Z]*/uct.xsd", "/xsd/VERSION/uct.xsd");
	}

}
