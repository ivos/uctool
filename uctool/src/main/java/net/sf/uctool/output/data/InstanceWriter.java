package net.sf.uctool.output.data;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.TemplateWriter;

import org.apache.velocity.VelocityContext;

public class InstanceWriter {

	private final TemplateWriter templateWriter;

	public InstanceWriter(TemplateWriter templateWriter) {
		this.templateWriter = templateWriter;
	}

	public void write(InstanceOut instance, ExecutionContext executionContext) {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		context.put("instance", instance);
		templateWriter.writeFile("template/instance.vm",
				"instance/" + instance.getCode(), context);
	}

}
