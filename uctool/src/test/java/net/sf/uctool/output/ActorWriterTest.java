package net.sf.uctool.output;

import java.io.File;

import net.sf.uctool.output.actor.ActorOut;

import org.junit.Before;
import org.junit.Test;

public class ActorWriterTest {

	UctoolWriter w;

	@Before
	public void before() {
		w = new UctoolWriter();
		w.init(new File("target/site/out"));
	}

	@Test
	public void fn() {
		ActorOut actor = new ActorOut();
		actor.setCode("code14");
		actor.setName("Name 1");
		actor.getDescriptions().add("Some description 1.");
		actor.getDescriptions().add("Some description 2.");
		w.write(actor);
	}

}
