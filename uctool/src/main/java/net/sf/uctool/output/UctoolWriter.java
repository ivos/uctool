package net.sf.uctool.output;

import java.io.File;
import java.util.Properties;

import net.sf.uctool.exception.WriterException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.actor.ActorWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class UctoolWriter {

	private VelocityEngine ve;
	private TemplateWriter templateWriter;
	private ActorWriter actorWriter;
	private ExecutionContext executionContext;

	public void init(File baseDir, ExecutionContext executionContext) {
		Properties p = new Properties();
		p.setProperty("resource.loader", "cp");
		p.setProperty("cp.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve = new VelocityEngine();
		ve.init(p);

		new File(baseDir, "actor").mkdirs();

		templateWriter = new TemplateWriter(ve, baseDir);
		actorWriter = new ActorWriter(templateWriter);
		this.executionContext = executionContext;
	}

	public void write(Object item) {
		if (null == item) {
			throw new WriterException("Item is null.");
		}
		if (item instanceof ActorOut) {
			actorWriter.write((ActorOut) item);
			return;
		}
		throw new WriterException("Unknown item class ["
				+ item.getClass().getName() + "].");
	}

	public void writeIndex() {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		templateWriter.write("template/index.vm", "index", context);
	}

}
