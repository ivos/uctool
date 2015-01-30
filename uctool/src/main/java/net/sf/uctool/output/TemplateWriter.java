package net.sf.uctool.output;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import net.sf.uctool.exception.WriterException;

import org.apache.commons.lang.time.StopWatch;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TemplateWriter {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	private final Map<String, Template> templates = new HashMap<String, Template>();

	private final VelocityEngine ve;
	private final File baseDir;
	private final String encoding;

	private final StopWatch time;

	public TemplateWriter(VelocityEngine ve, File baseDir, String encoding) {
		this.ve = ve;
		this.baseDir = baseDir;
		this.encoding = encoding;
		time = new StopWatch();
	}

	public void writeFile(String templateName, String outputFileNameCore,
			VelocityContext context) {
		File outputFile = getOutputFile(outputFileNameCore);
		OutputStream os = openStream(outputFile);
		Writer w = openWriter(os, outputFile);
		writeFragment(templateName, context, w, outputFile);
		closeFile(os, w, outputFile);
	}

	public File getOutputFile(String outputFileNameCore) {
		File outputFile = new File(baseDir, outputFileNameCore + ".html");
		return outputFile;
	}

	public OutputStream openStream(File outputFile) {
		try {
			OutputStream os = new FileOutputStream(outputFile);
			logger.trace("Opened file stream for file [{}].", outputFile);
			return os;
		} catch (IOException e) {
			throw new WriterException("Error opening output file ["
					+ outputFile.getName() + "].", e);
		}
	}

	public Writer openWriter(OutputStream os, File outputFile) {
		try {
			Writer w = new OutputStreamWriter(os, encoding);
			logger.trace("Opened writer for file [{}].", outputFile);
			return w;
		} catch (IOException e) {
			throw new WriterException("Error opening output file ["
					+ outputFile.getName() + "].", e);
		}
	}

	private Template loadTemplate(String templateName) {
		Template template = templates.get(templateName);
		if (null == template) {
			logger.trace("Loading template by name [{}]...", templateName);
			try {
				template = ve.getTemplate(templateName, encoding);
			} catch (ResourceNotFoundException e) {
				throw new WriterException("Template not found: ["
						+ templateName + "].", e);
			} catch (ParseErrorException e) {
				throw new WriterException("Error parsing template: ["
						+ templateName + "].", e);
			} catch (Exception e) {
				throw new WriterException("Error processing template: ["
						+ templateName + "].", e);
			}
			templates.put(templateName, template);
			logger.trace("Loaded template by name [{}].", templateName);
		}
		return template;
	}

	public void writeFragment(String templateName, VelocityContext context,
			Writer w, File outputFile) {
		if (logger.isDebugEnabled()) {
			time.start();
		}
		Template template = loadTemplate(templateName);
		template.merge(context, w);
		if (logger.isDebugEnabled()) {
			logger.debug("Merged template [{}] to {} @ {}.",
					template.getName(), outputFile, time.toString());
			time.reset();
		}
	}

	public void writeText(String text, Writer w, File outputFile) {
		try {
			w.write(text);
		} catch (IOException e) {
			throw new WriterException("Error writing text to output file ["
					+ outputFile.getName() + "].", e);
		}
	}

	public void closeFile(OutputStream os, Writer w, File outputFile) {
		try {
			w.close();
			os.close();
			logger.debug("Closed {}.", outputFile);
		} catch (IOException e) {
			throw new WriterException("Error writing output file ["
					+ outputFile.getName() + "].", e);
		}
	}

}
