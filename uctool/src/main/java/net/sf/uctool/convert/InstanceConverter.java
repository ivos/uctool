package net.sf.uctool.convert;

import static net.sf.uctool.util.Escape.*;

import java.util.Set;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.Reference;
import net.sf.uctool.output.data.AttributeOut;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.output.data.InstanceOut;
import net.sf.uctool.output.data.ValueOut;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.Instance;
import net.sf.uctool.xsd.UseCase;
import net.sf.uctool.xsd.Value;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InstanceConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutionContext executionContext;
	private final ConverterHelper converterHelper;

	public InstanceConverter(ExecutionContext executionContext) {
		this.executionContext = executionContext;
		this.converterHelper = new ConverterHelper(executionContext);
	}

	public InstanceOut convert(Instance instance) {
		logger.debug("Converting instance {}.", instance);
		InstanceOut o = new InstanceOut();
		String code = instance.getCode();
		String refcode = instance.getRefcode();
		String of = instance.getOf();
		o.setCode(code);
		o.setRefcode(refcode);
		o.setName(escape(instance.getName()));

		DataOut dataOut = executionContext.getDataOuts().get(of);
		if (null == dataOut) {
			throw new ValidationException("Instance [" + code
					+ "] refers to unknown data [" + of + "].");
		}
		o.setOf(dataOut);

		for (DescriptionType descriptionType : instance.getDescription()) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				converterHelper.writeDescription(sb, content, "instance", code,
						refcode);
			}
			o.getDescriptions().add(sb.toString().trim());
		}

		for (Value value : instance.getValue()) {
			ValueOut vo = new ValueOut();
			String attributeOf = value.getOf();
			String from = value.getFrom();

			AttributeOut attributeOut = dataOut.getAttribute(attributeOf);
			if (null == attributeOut) {
				throw new ValidationException("Value on instance [" + code
						+ "] refers to unknown attribute of data [" + of
						+ "] via code [" + attributeOf + "].");
			}
			vo.setOf(attributeOut);

			if (null != from) {
				String[] parts = from.split("\\.");
				if (parts.length != 2) {
					throw new ValidationException("Invalid 'from' [" + from
							+ "] on instance [" + code + "], value of ["
							+ attributeOf + "].");
				}
				DataOut fromData = executionContext.getDataOuts().get(parts[0]);
				if (null == fromData) {
					throw new ValidationException("Value on instance [" + code
							+ "] refers to unknown data [" + parts[0]
							+ "] in from [" + from + "].");
				}
				vo.setFromData(fromData);

				AttributeOut fromAttribute = fromData.getAttribute(parts[1]);
				if (null == fromAttribute) {
					throw new ValidationException("Value on instance [" + code
							+ "] refers to unknown attribute of data ["
							+ parts[0] + "] via code [" + parts[1]
							+ "] using 'from' [" + from + "].");
				}
				vo.setFromAttribute(fromAttribute);
			} else {
				vo.setFromData(DataOut.SAFE_EMPTY);
				vo.setFromAttribute(new AttributeOut());
			}

			DescriptionType descriptionType = value.getDescription();
			if (null != descriptionType) {
				StringBuilder sb = new StringBuilder();
				for (Object content : descriptionType.getContent()) {
					converterHelper.writeDescription(sb, content, "value", code
							+ "." + attributeOf, refcode);
				}
				vo.setDescription(sb.toString().trim());
			}

			o.getValues().add(vo);
		}

		return o;
	}

	public void addReferences(InstanceOut o) {
		logger.debug("Adding references to instance {}.", o);
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
