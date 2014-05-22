package net.sf.uctool.output;

import java.io.File;
import java.io.FileWriter;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.uc.UseCaseOut;

import org.apache.velocity.VelocityContext;

public class SinglePageWriter {

	private final TemplateWriter templateWriter;

	public SinglePageWriter(TemplateWriter templateWriter) {
		this.templateWriter = templateWriter;
	}

	public void write(ExecutionContext executionContext) {
		executionContext.setSingle(true);
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		File outputFile = templateWriter.getOutputFile("single/index");
		FileWriter fw = templateWriter.openFile(outputFile);
		templateWriter.writeFragment("template/single-start.vm", context, fw,
				outputFile);

		if (!executionContext.getActorOuts().isEmpty()) {
			// actor index
			templateWriter.writeText("\n", fw, outputFile);
			templateWriter.writeFragment("template/actor-index.vm", context,
					fw, outputFile);

			// actors
			templateWriter.writeText("\n<h2>Actors details</h2>\n", fw,
					outputFile);
			for (ActorOut actor : executionContext.getActorOuts().values()) {
				templateWriter.writeText("\n", fw, outputFile);
				context.put("actor", actor);
				templateWriter.writeFragment("template/actor.vm", context, fw,
						outputFile);
			}
		}

		if (!executionContext.getUseCaseOuts().isEmpty()) {
			// use case index
			templateWriter.writeText("\n", fw, outputFile);
			templateWriter.writeFragment("template/uc-index.vm", context, fw,
					outputFile);

			// use cases
			templateWriter.writeText("\n<h2>Use cases details</h2>\n", fw,
					outputFile);
			for (UseCaseOut useCase : executionContext.getUseCaseOuts()
					.values()) {
				templateWriter.writeText("\n", fw, outputFile);
				context.put("uc", useCase);
				templateWriter.writeFragment("template/uc.vm", context, fw,
						outputFile);
			}
		}

		templateWriter.writeText("\n", fw, outputFile);
		templateWriter.writeFragment("template/single-end.vm", context, fw,
				outputFile);
		templateWriter.closeFile(fw, outputFile);
		executionContext.setSingle(false);
	}

}
