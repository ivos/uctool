package net.sf.uctool.output.uc;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.TemplateWriter;

import org.apache.velocity.VelocityContext;

public class UseCaseWriter {

	private final TemplateWriter templateWriter;

	public UseCaseWriter(TemplateWriter templateWriter) {
		this.templateWriter = templateWriter;
	}

	public void write(UseCaseOut useCase, ExecutionContext executionContext) {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		context.put("uc", useCase);
		templateWriter.writeFile("template/uc.vm", "uc/" + useCase.getCode(),
				context);
	}

}
