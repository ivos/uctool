package net.sf.uctool.convert;

import static net.sf.uctool.util.Escape.*;

import java.util.Arrays;
import java.util.List;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.Reference;
import net.sf.uctool.output.data.AttributeOut;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.xsd.Attribute;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.DescriptionType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutionContext executionContext;
	private final ConverterHelper converterHelper;

	private static final List<String> DATA_TYPES = Arrays.asList("string",
			"integer", "float", "decimal", "boolean", "date", "time",
			"date-time", "timestamp", "binary");
	private static final List<String> VAR_DATA_TYPES = Arrays.asList("string",
			"binary");

	public DataConverter(ExecutionContext executionContext) {
		this.executionContext = executionContext;
		this.converterHelper = new ConverterHelper(executionContext);
	}

	public DataOut convert(Data data) {
		logger.debug("Converting data {}.", data);
		DataOut o = new DataOut();
		String code = data.getCode();
		String refcode = data.getRefcode();
		o.setCode(code);
		o.setRefcode(refcode);
		o.setName(escape(data.getName()));
		if (null != data.getCategory()) {
			o.setCategory(executionContext.getLabels().getString(
					"category." + data.getCategory()));
		}

		for (DescriptionType descriptionType : data.getDescription()) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				converterHelper.writeDescription(sb, content, "data", code,
						refcode);
			}
			o.getDescriptions().add(sb.toString().trim());
		}

		for (Attribute attribute : data.getAttribute()) {
			AttributeOut ao = new AttributeOut();
			ao.setName(attribute.getName());
			String attributeCode = attribute.getCode();
			ao.setCode(attributeCode);
			ao.setStatus(attribute.getStatus());
			if (null != attribute.getStatus()) {
				ao.setStatus(executionContext.getLabels().getString(
						"status." + attribute.getStatus()));
				ao.setStatusHint(executionContext.getLabels().getString(
						"status.hint." + attribute.getStatus()));
			}
			StringBuffer outType = new StringBuffer();
			boolean collection = (null == attribute.isCollection()) ? false
					: attribute.isCollection();
			if (collection) {
				outType.append(executionContext.getLabels().getString(
						"data.collection"));
				outType.append(" [");
			}
			String type = attribute.getType();
			if (null != type) {
				if (DATA_TYPES.contains(type)) {
					outType.append(executionContext.getLabels().getString(
							"data.type." + type));
				} else {
					Data referenced = executionContext.getDatas().get(type);
					if (null == referenced) {
						throw new ValidationException(
								"Missing data with refcode [" + type
										+ "] referenced from data with code ["
										+ code + "].");
					}
					outType.append(new Reference("data", referenced.getCode(),
							referenced.getName()).toHtml());
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
							code + "." + attributeCode, null);
				}
				ao.setDescription(sb.toString().trim());
			}

			o.getAttributes().add(ao);
		}

		return o;
	}
}
