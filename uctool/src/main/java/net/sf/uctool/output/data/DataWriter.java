package net.sf.uctool.output.data;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.TemplateWriter;

import org.apache.velocity.VelocityContext;

public class DataWriter {

	private final TemplateWriter templateWriter;

	public DataWriter(TemplateWriter templateWriter) {
		this.templateWriter = templateWriter;
	}

	public void write(DataOut data, ExecutionContext executionContext) {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		context.put("data", data);
		templateWriter.writeFile("template/data.vm", "data/" + data.getCode(),
				context);
	}

}
