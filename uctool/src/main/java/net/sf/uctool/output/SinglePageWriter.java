package net.sf.uctool.output;

import java.io.File;
import java.io.OutputStream;
import java.io.Writer;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.output.data.InstanceOut;
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
		OutputStream os = templateWriter.openStream(outputFile);
		Writer w = templateWriter.openWriter(os, outputFile);
		templateWriter.writeFragment("template/single-start.vm", context, w,
				outputFile);

		if (!executionContext.getActorOuts().isEmpty()) {
			// actor index
			templateWriter.writeText("\n", w, outputFile);
			templateWriter.writeFragment("template/actor-index.vm", context, w,
					outputFile);

			// actors
			templateWriter.writeText(
					"\n<h2>" + executionContext.label("actor.details.section")
							+ "</h2>\n", w, outputFile);
			for (ActorOut actor : executionContext.getActorOuts().values()) {
				templateWriter.writeText("\n", w, outputFile);
				context.put("actor", actor);
				templateWriter.writeFragment("template/actor.vm", context, w,
						outputFile);
			}
		}

		if (!executionContext.getUseCaseOuts().isEmpty()) {
			// use case index
			templateWriter.writeText("\n", w, outputFile);
			templateWriter.writeFragment("template/uc-index.vm", context, w,
					outputFile);

			// use cases
			templateWriter.writeText(
					"\n<h2>"
							+ executionContext
									.label("use.case.details.section")
							+ "</h2>\n", w, outputFile);
			for (UseCaseOut useCase : executionContext.getUseCaseOuts()
					.values()) {
				templateWriter.writeText("\n", w, outputFile);
				context.put("uc", useCase);
				templateWriter.writeFragment("template/uc.vm", context, w,
						outputFile);
			}
		}

		if (!executionContext.getDataOuts().isEmpty()) {
			// data index
			templateWriter.writeText("\n", w, outputFile);
			templateWriter.writeFragment("template/data-index.vm", context, w,
					outputFile);

			// data
			templateWriter.writeText(
					"\n<h2>"
							+ executionContext
									.label("data.structure.details.section")
							+ "</h2>\n", w, outputFile);
			for (DataOut data : executionContext.getDataOuts().values()) {
				templateWriter.writeText("\n", w, outputFile);
				context.put("data", data);
				templateWriter.writeFragment("template/data.vm", context, w,
						outputFile);
			}
		}

		if (!executionContext.getInstanceOuts().isEmpty()) {
			// instance index
			templateWriter.writeText("\n", w, outputFile);
			templateWriter.writeFragment("template/instance-index.vm", context,
					w, outputFile);

			// instances
			templateWriter.writeText(
					"\n<h2>"
							+ executionContext
									.label("instance.details.section")
							+ "</h2>\n", w, outputFile);
			for (InstanceOut instance : executionContext.getInstanceOuts()
					.values()) {
				templateWriter.writeText("\n", w, outputFile);
				context.put("instance", instance);
				templateWriter.writeFragment("template/instance.vm", context,
						w, outputFile);
			}
		}

		if (!executionContext.getTermOuts().isEmpty()) {
			templateWriter.writeText("\n", w, outputFile);
			templateWriter.writeFragment("template/glossary.vm", context, w,
					outputFile);
		}

		templateWriter.writeText("\n", w, outputFile);
		templateWriter.writeFragment("template/single-end.vm", context, w,
				outputFile);
		templateWriter.closeFile(os, w, outputFile);
		executionContext.setSingle(false);
	}

}
