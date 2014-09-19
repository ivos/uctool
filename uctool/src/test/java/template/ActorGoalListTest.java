package template;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

public class ActorGoalListTest extends TemplateTestBase {

	@Test
	public void full() throws IOException {
		e.execute(new File(inputBaseDir, "actor-goal-list/full.xml"), outputDir);
		performTest("unit/actor-goal-list/full.html",
				"site/out/summary/actor-goal-list.html");
	}

}
