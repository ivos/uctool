package net.sf.uctool.output.actor;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.TemplateWriter;

import org.apache.velocity.VelocityContext;

public class ActorWriter {

	private final TemplateWriter templateWriter;

	public ActorWriter(TemplateWriter templateWriter) {
		this.templateWriter = templateWriter;
	}

	public void write(ActorOut actor, ExecutionContext executionContext) {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		context.put("actor", actor);
		templateWriter.write("template/actor.vm", "actor/" + actor.getCode(),
				context);
	}

}
