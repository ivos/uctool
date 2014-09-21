package net.sf.uctool.convert;

import static net.sf.uctool.util.Escape.*;

import java.util.Set;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.Reference;
import net.sf.uctool.output.data.AttributeOut;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.xsd.Attribute;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.Instance;
import net.sf.uctool.xsd.UseCase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutionContext executionContext;
	private final ConverterHelper converterHelper;
	private final AttributeConverter attributeConverter;

	public DataConverter(ExecutionContext executionContext,
			AttributeConverter attributeConverter) {
		this.executionContext = executionContext;
		this.attributeConverter = attributeConverter;
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
			o.setCategory(executionContext.label("category."
					+ data.getCategory()));
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
			AttributeOut ao = attributeConverter.convert(code, refcode,
					attribute);
			o.getAttributes().add(ao);
		}

		return o;
	}

	public void addReferences(DataOut o) {
		logger.debug("Adding references to data {}.", o);
		String refcode = o.getRefcode();
		Set<String> references;

		references = executionContext.getDataReferencesData().get(refcode);
		if (null != references) {
			for (String referencingRefcode : references) {
				String referencingCode, referencingName, type;
				Data referencingData = executionContext.getDatas().get(
						referencingRefcode);
				if (null != referencingData) {
					referencingCode = referencingData.getCode();
					referencingName = referencingData.getName();
					type = "data";
				} else {
					Instance referencingInstance = executionContext
							.getInstances().get(referencingRefcode);
					referencingCode = referencingInstance.getCode();
					referencingName = referencingInstance.getName();
					type = "instance";
				}
				Reference reference = new Reference(type, referencingCode,
						referencingName);
				o.getReferencesData().add(reference);
				logger.debug("Added reference {}.", reference);
			}
		}

		references = executionContext.getDataReferencesUc().get(refcode);
		if (null != references) {
			for (String referencingRefcode : references) {
				UseCase referencing = executionContext.getUseCases().get(
						referencingRefcode);
				String referencingCode = referencing.getCode();
				Reference reference = new Reference("uc", referencingCode,
						referencing.getCode() + " - " + referencing.getGoal());
				o.getReferencesUcs().add(reference);
				logger.debug("Added reference {}.", reference);
			}
		}
	}

}
