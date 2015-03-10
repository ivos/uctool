package net.sf.uctool.xsd.export;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sf.uctool.common.ExecutorBase;
import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.Project;
import net.sf.uctool.output.TemplateWriter;
import net.sf.uctool.xsd.Attribute;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.DataRef;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.Uct;

import org.apache.velocity.VelocityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Element;

public class UctoolXsdExport extends ExecutorBase {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final String encoding;

	private final List<DataXsdOut> outputs = new ArrayList<DataXsdOut>();

	private final String targetNamespaceUrl;

	public UctoolXsdExport(String encoding, String targetNamespaceUrl) {
		super(new Project(null, null, null, encoding, "en"));
		this.encoding = encoding;
		this.targetNamespaceUrl = targetNamespaceUrl;
	}

	@Override
	protected Logger getLogger() {
		return logger;
	}

	public void execute(File inputPath, File outputFile) {
		timeAll.start();

		File parentDir = outputFile.getParentFile();
		if (null == parentDir) {
			throw new ValidationException(
					"The output file does not have a parent directory: "
							+ outputFile);
		}
		parentDir.mkdirs();
		List<File> inputFiles = findInputFiles(inputPath);
		List<Uct> inputs = readInputFiles(inputFiles);
		validateInputs(inputs);
		convert();
		write(outputFile);

		timeAll.stop();
		logger.info("Executed UCTool data XSD export into {} in {}.",
				outputFile, timeAll.toString());
	}

	private void convert() {
		for (Data data : executionContext.getDatas().values()) {
			DataXsdOut o = new DataXsdOut();
			o.setCode(data.getCode());
			o.setName(data.getName());
			for (DescriptionType description : data.getDescription()) {
				o.getDescriptions().add(convertDescription(description));
			}
			for (Attribute attribute : data.getAttribute()) {
				AttributeXsdOut ao = new AttributeXsdOut();
				String name = attribute.getName();
				String code = attribute.getCode();
				ao.setCode((null == code) ? name : code);
				ao.setName(name);
				ao.setStatus(attribute.getStatus());
				ao.setType(convertType(attribute.getType()));
				ao.setCollection(attribute.isCollection());
				ao.setLength(attribute.getLength());
				ao.setDescription(convertDescription(attribute.getDescription()));

				o.getAttributes().add(ao);
			}
			outputs.add(o);
		}
	}

	private String convertDescription(DescriptionType description) {
		if (null == description) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Object content : description.getContent()) {
			if (content instanceof String) {
				sb.append(content);
			}
			if (content instanceof Element) {
				sb.append(((Element) content).getTextContent());
			}
			if (content instanceof DataRef) {
				sb.append(((DataRef) content).getValue());
			}
		}
		return sb.toString();
	}

	private String convertType(String type) {
		if (null == type) {
			return null;
		}
		if ("integer".equals(type) || "float".equals(type)
				|| "decimal".equals(type) || "boolean".equals(type)
				|| "date".equals(type) || "time".equals(type)
				|| "string".equals(type)) {
			return "xs:" + type;
		}
		if ("date-time".equals(type) || "timestamp".equals(type)) {
			return "xs:dateTime";
		}
		if ("binary".equals(type)) {
			return "xs:base64Binary";
		}
		return type + "Type";
	}

	private void write(File outputFile) {
		logger.debug("Writing XSD file for {} data.", outputs.size());
		time.start();

		TemplateWriter templateWriter = new TemplateWriter(
				outputFile.getParentFile(), encoding, "");
		VelocityContext context = new VelocityContext();
		context.put("url", targetNamespaceUrl);
		context.put("datas", outputs);
		templateWriter.writeFile("template/data-xsd-export.vm",
				outputFile.getName(), context);

		logger.debug("Wrote XSD file @ {}.", time.toString());
		time.reset();
	}

}
