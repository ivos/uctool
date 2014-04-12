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

	public void write(String templateName, String outputFileNameCore,
			VelocityContext context) {
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
		File outputFile = new File(baseDir, outputFileNameCore + ".html");
		FileWriter fw;
		try {
			fw = new FileWriter(outputFile);
		} catch (IOException e) {
			throw new WriterException("Error opening output file ["
					+ outputFile.getName() + "].", e);
		}
		logger.trace("Opened file writer for file [{}].", outputFile);
		template.merge(context, fw);
		logger.trace("Merged template.");
		try {
			fw.close();
		} catch (IOException e) {
			throw new WriterException("Error writing output file ["
					+ outputFile.getName() + "].", e);
		}
		logger.debug("Wrote [{}] to {}.", templateName, outputFile);
	}

}
