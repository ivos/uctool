package net.sf.uctool.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.sf.uctool.exception.WriterException;

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

	public TemplateWriter(VelocityEngine ve, File baseDir) {
		this.ve = ve;
		this.baseDir = baseDir;
	}

	public void writeFile(String templateName, String outputFileNameCore,
			VelocityContext context) {
		File outputFile = getOutputFile(outputFileNameCore);
		FileWriter fw = openFile(outputFile);
		writeFragment(templateName, context, fw, outputFile);
		closeFile(fw, outputFile);
	}

	public File getOutputFile(String outputFileNameCore) {
		File outputFile = new File(baseDir, outputFileNameCore + ".html");
		return outputFile;
	}

	public FileWriter openFile(File outputFile) {
		try {
			FileWriter fw = new FileWriter(outputFile);
			logger.trace("Opened file writer for file [{}].", outputFile);
			return fw;
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
				template = ve.getTemplate(templateName);
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
			FileWriter fw, File outputFile) {
		Template template = loadTemplate(templateName);
		template.merge(context, fw);
		logger.debug("Merged template [{}] to {}.", template.getName(),
				outputFile);
	}

	public void writeText(String text, FileWriter fw, File outputFile) {
		try {
			fw.write(text);
		} catch (IOException e) {
			throw new WriterException("Error writing text to output file ["
					+ outputFile.getName() + "].", e);
		}
	}

	public void closeFile(FileWriter fw, File outputFile) {
		try {
			fw.close();
			logger.debug("Closed {}.", outputFile);
		} catch (IOException e) {
			throw new WriterException("Error writing output file ["
					+ outputFile.getName() + "].", e);
		}
	}

}
