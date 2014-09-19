package template;

import java.io.File;
import java.io.IOException;

import net.sf.seaf.test.util.TemplatingTestBase;
import net.sf.uctool.execute.Project;
import net.sf.uctool.execute.UctoolExecutor;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class IntegrationTest extends TemplatingTestBase implements
		TemplateTestConstants {

	public IntegrationTest() {
		super(REPLACE_TEMPLATE);
	}

	@BeforeClass
	public static void execute() {
		Project project = new Project("test-name", "test-version",
				"test-description");
		new UctoolExecutor(project).execute(
				new File("src/test/ucs/integration"), new File(
						"target/site/out"));
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

	@Ignore
	@Test
	public void dataExpansionTest() throws IOException {
		performTest("data/ExpansionTest.html");
	}

	@Ignore
	@Test
	public void dataExpansionReferencesTest() throws IOException {
		performTest("data/ExpansionReferredStructure.html");
	}

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

	@Ignore
	@Test
	public void dataAttRefE1() throws IOException {
		performTest("data/DARE1.html");
	}

	@Ignore
	@Test
	public void dataAttRefE2() throws IOException {
		performTest("data/DARE2.html");
	}

	@Ignore
	@Test
	public void dataAttRefE3() throws IOException {
		performTest("data/DARE3.html");
	}

	@Ignore
	@Test
	public void dataAttRefF1() throws IOException {
		performTest("data/DARF1.html");
	}

	@Ignore
	@Test
	public void dataAttRefExpanded() throws IOException {
		performTest("data/expand-att-ref.html");
	}

	@Ignore
	@Test
	public void dataAttRefExpansion() throws IOException {
		performTest("data/att-ref-expansion.html");
	}

	@Ignore
	@Test
	public void dataAttRefExpansion2ndLevel() throws IOException {
		performTest("data/att-ref-expansion-2.html");
	}

	@Test
	public void actorGoalList() throws IOException {
		performTest("summary/actor-goal-list.html");
	}

	@Test
	public void entryPointList() throws IOException {
		performTest("summary/entry-point-list.html");
	}

	@Test
	public void unusedDataList() throws IOException {
		performTest("summary/unused-data-list.html");
	}

	@Test
	public void summaryIndex() throws IOException {
		performTest("summary/index.html");
	}

	@Test
	public void single() throws IOException {
		performTest("single/index.html");
	}

	@Test
	public void index() throws IOException {
		performTest("index.html");
	}

	@Test
	public void requirement() throws IOException {
		performTest("req/req01.html");
	}

	@Test
	public void requirement2() throws IOException {
		performTest("req/req02.html");
	}

	@Test
	public void requirementIndex() throws IOException {
		performTest("req/index.html");
	}

	@Test
	public void requirementUc() throws IOException {
		performTest("uc/req-ref-03.html");
	}

	@Test
	public void requirementActor() throws IOException {
		performTest("actor/req-actor-01.html");
	}

	@Test
	public void attachmentIndex() throws IOException {
		performTest("attachment/index.html");
	}

	@Test
	public void attachmentUc() throws IOException {
		performTest("uc/attachment-ref-01.html");
	}

	@Test
	public void attachmentActor() throws IOException {
		performTest("actor/attachment-actor.html");
	}

	@Test
	public void attachmentData() throws IOException {
		performTest("data/attachment-data-structure.html");
	}

	@Test
	public void attachmentRequirement() throws IOException {
		performTest("req/attachment-requirement.html");
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
		performTest("xsd-export/data.xsd", "site/xsd/data.xsd");
	}

	@Test
	public void xsdImportData() throws IOException {
		performTest("xsd-import/data.xml", "site/import/data.xml",
				"/xsd/[0-9\\.\\-A-Z]*/uct.xsd", "/xsd/VERSION/uct.xsd");
	}

	@Test
	public void xsdImportImported() throws IOException {
		performTest("xsd-import/imported.xml", "site/import/imported.xml",
				"/xsd/[0-9\\.\\-A-Z]*/uct.xsd", "/xsd/VERSION/uct.xsd");
	}

}
