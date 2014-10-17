package net.sf.uctool.output;

import java.io.File;
import java.util.Properties;

import net.sf.uctool.exception.WriterException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.actor.ActorWriter;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.output.data.DataWriter;
import net.sf.uctool.output.data.InstanceOut;
import net.sf.uctool.output.data.InstanceWriter;
import net.sf.uctool.output.uc.UseCaseOut;
import net.sf.uctool.output.uc.UseCaseWriter;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;

public class UctoolWriter {

	private VelocityEngine ve;
	private TemplateWriter templateWriter;
	private ActorWriter actorWriter;
	private SinglePageWriter singlePageWriter;
	private UseCaseWriter useCaseWriter;
	private DataWriter dataWriter;
	private InstanceWriter instanceWriter;
	private ExecutionContext executionContext;

	public void init(File baseDir, ExecutionContext executionContext) {
		Properties p = new Properties();
		p.setProperty("resource.loader", "cp");
		p.setProperty("cp.resource.loader.class",
				ClasspathResourceLoader.class.getName());
		ve = new VelocityEngine();
		ve.init(p);

		new File(baseDir, "actor").mkdirs();
		new File(baseDir, "uc").mkdirs();
		new File(baseDir, "data").mkdirs();
		new File(baseDir, "instance").mkdirs();
		new File(baseDir, "glossary").mkdirs();
		new File(baseDir, "summary").mkdirs();
		new File(baseDir, "single").mkdirs();

		this.executionContext = executionContext;

		templateWriter = new TemplateWriter(ve, baseDir);
		actorWriter = new ActorWriter(templateWriter);
		useCaseWriter = new UseCaseWriter(templateWriter);
		dataWriter = new DataWriter(templateWriter);
		instanceWriter = new InstanceWriter(templateWriter);
		singlePageWriter = new SinglePageWriter(templateWriter);
	}

	public void write(Object item) {
		if (null == item) {
			throw new WriterException("Item is null.");
		}
		if (item instanceof ActorOut) {
			actorWriter.write((ActorOut) item, executionContext);
			return;
		}
		if (item instanceof UseCaseOut) {
			useCaseWriter.write((UseCaseOut) item, executionContext);
			return;
		}
		if (item instanceof DataOut) {
			dataWriter.write((DataOut) item, executionContext);
			return;
		}
		if (item instanceof InstanceOut) {
			instanceWriter.write((InstanceOut) item, executionContext);
			return;
		}
		throw new WriterException("Unknown item class ["
				+ item.getClass().getName() + "].");
	}

	public void writeIndex() {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		templateWriter.writeFile("template/index.vm", "index", context);
	}

	public void writeActorIndex() {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		templateWriter.writeFile("template/actor-index.vm", "actor/index",
				context);
	}

	public void writeUseCaseIndex() {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		templateWriter.writeFile("template/uc-index.vm", "uc/index", context);
	}

	public void writeDataIndex() {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		templateWriter.writeFile("template/data-index.vm", "data/index",
				context);
	}

	public void writeInstanceIndex() {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		templateWriter.writeFile("template/instance-index.vm",
				"instance/index", context);
	}

	public void writeGlossary() {
		VelocityContext context = new VelocityContext();
		context.put("ctx", executionContext);
		templateWriter.writeFile("template/glossary.vm", "glossary/index",
				context);
	}

	public void writeSinglePage() {
		singlePageWriter.write(executionContext);
	}

}
