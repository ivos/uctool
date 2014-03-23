package net.sf.uctool.output;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import net.sf.uctool.exception.WriterException;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.exception.ParseErrorException;
import org.apache.velocity.exception.ResourceNotFoundException;

public class TemplateWriter {

	private final VelocityEngine ve;
	private final File baseDir;

	public TemplateWriter(VelocityEngine ve, File baseDir) {
		this.ve = ve;
		this.baseDir = baseDir;
	}

	public void write(String templateName, String outputFileNameCore,
			VelocityContext context) {
		Template template = null;
		try {
			template = ve.getTemplate(templateName);
		} catch (ResourceNotFoundException e) {
			throw new WriterException("Template not found: [" + templateName
					+ "].", e);
		} catch (ParseErrorException e) {
			throw new WriterException("Error parsing template: ["
					+ templateName + "].", e);
		} catch (Exception e) {
			throw new WriterException("Error processing template: ["
					+ templateName + "].", e);
		}
		File outputFile = new File(baseDir, outputFileNameCore + ".html");
		FileWriter fw;
		try {
			fw = new FileWriter(outputFile);
		} catch (IOException e) {
			throw new WriterException("Error opening output file ["
					+ outputFile.getName() + "].", e);
		}
		template.merge(context, fw);
		try {
			fw.close();
		} catch (IOException e) {
			throw new WriterException("Error writing output file ["
					+ outputFile.getName() + "].", e);
		}
	}

}
