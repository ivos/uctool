package net.sf.uctool.convert;

import java.util.Arrays;
import java.util.List;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.Reference;
import net.sf.uctool.output.data.AttributeOut;
import net.sf.uctool.xsd.Attribute;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.DescriptionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AttributeConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutionContext executionContext;
	private final ConverterHelper converterHelper;

	private static final List<String> DATA_TYPES = Arrays.asList("string",
			"integer", "float", "decimal", "boolean", "date", "time",
			"date-time", "timestamp", "binary");
	private static final List<String> VAR_DATA_TYPES = Arrays.asList("string",
			"binary");

	public AttributeConverter(ExecutionContext executionContext) {
		this.executionContext = executionContext;
		this.converterHelper = new ConverterHelper(executionContext);
	}

	public AttributeOut convert(String dataCode, String dataRefcode,
			Attribute attribute) {
		logger.debug("Converting attribute {}.", attribute);
		AttributeOut ao = new AttributeOut();
		ao.setName(attribute.getName());
		String attributeCode = attribute.getCode();
		ao.setCode(attributeCode);
		String refcode = attribute.getRefcode();
		ao.setRefcode(refcode);
		ao.setStatus(attribute.getStatus());
		if (null != attribute.getStatus()) {
			ao.setStatus(executionContext.label("status."
					+ attribute.getStatus()));
			ao.setStatusHint(executionContext.label("status.hint."
					+ attribute.getStatus()));
		}
		StringBuffer outType = new StringBuffer();
		boolean collection = (null == attribute.isCollection()) ? false
				: attribute.isCollection();
		if (collection) {
			outType.append(executionContext.label("data.collection"));
			outType.append(" [");
		}
		String type = attribute.getType();
		if (null != type) {
			if (DATA_TYPES.contains(type)) {
				outType.append(executionContext.label("data.type." + type));
			} else {
				Data referenced = executionContext.getDatas().get(type);
				if (null == referenced) {
					throw new ValidationException("Missing data with refcode ["
							+ type + "] referenced from data with code ["
							+ dataCode + "].");
				}
				outType.append(new Reference("data", referenced.getCode(),
						referenced.getName()).toHtml(executionContext
						.isSingle()));
				executionContext.addDataRef(referenced.getRefcode(),
						dataRefcode, "data");
			}
		}
		if (collection) {
			outType.append("]");
		}
		String length = attribute.getLength();
		if (null != length || VAR_DATA_TYPES.contains(type)) {
			outType.append(" [");
			if (null != length) {
				outType.append(length);
			} else {
				outType.append(" ");
			}
			outType.append("]");
		}
		ao.setType(outType.toString());

		DescriptionType descriptionType = attribute.getDescription();
		if (null != descriptionType) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				converterHelper.writeDescription(sb, content, "attribute",
						dataCode + "." + attributeCode, dataRefcode);
			}
			ao.setDescription(sb.toString().trim());
		}
		return ao;
	}

}
