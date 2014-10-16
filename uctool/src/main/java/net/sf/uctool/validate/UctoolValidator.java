package net.sf.uctool.validate;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Attribute;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.ExtendsActor;
import net.sf.uctool.xsd.Instance;
import net.sf.uctool.xsd.Term;
import net.sf.uctool.xsd.UcGroup;
import net.sf.uctool.xsd.Uct;
import net.sf.uctool.xsd.UseCase;
import net.sf.uctool.xsd.Value;

public class UctoolValidator {

	private final ExecutionContext executionContext;

	public UctoolValidator(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public void validate(List<Uct> inputs) {
		for (Uct uct : inputs) {
			for (Object object : uct.getActorOrUcGroupOrData()) {
				if (object instanceof Actor) {
					Actor actor = (Actor) object;
					String code = actor.getCode();
					if (executionContext.getActors().containsKey(code)) {
						throw new ValidationException(
								"Duplicate actor with code [" + code + "].");
					}
					executionContext.getActors().put(code, actor);
				}
				if (object instanceof Data) {
					Data data = (Data) object;
					String code = data.getCode();
					String refcode = data.getRefcode();
					if (null == refcode) {
						refcode = code;
						data.setRefcode(code);
					}
					if (executionContext.getDataCodes().contains(refcode)
							|| executionContext.getDataCodes().contains(code)) {
						throw new ValidationException(
								"Duplicate data or instance with code [" + code
										+ "], refcode [" + refcode + "].");
					}
					executionContext.getDataCodes().add(refcode);
					executionContext.getDataCodes().add(code);

					Set<String> attributeCodes = new HashSet<String>();
					Set<String> attributeRefcodes = new HashSet<String>();
					for (Attribute attribute : data.getAttribute()) {
						String attributeCode = attribute.getCode();
						String attributeRefcode = attribute.getRefcode();
						if (null == attributeRefcode) {
							attributeRefcode = attributeCode;
							attribute.setRefcode(attributeCode);
						}
						if (attributeCodes.contains(attributeCode)
								|| attributeRefcodes.contains(attributeRefcode)) {
							throw new ValidationException(
									"Duplicate attribute with code ["
											+ attributeCode
											+ "] on data with code [" + code
											+ "].");
						}
						if (null != attributeCode) {
							attributeCodes.add(attributeCode);
						}
						if (null != attributeRefcode) {
							attributeRefcodes.add(attributeRefcode);
						}
					}

					executionContext.getDatas().put(refcode, data);
				}
				if (object instanceof Instance) {
					Instance instance = (Instance) object;
					String code = instance.getCode();
					String refcode = instance.getRefcode();
					if (null == refcode) {
						refcode = code;
						instance.setRefcode(code);
					}
					if (executionContext.getDataCodes().contains(refcode)
							|| executionContext.getDataCodes().contains(code)) {
						throw new ValidationException(
								"Duplicate data or instance with code [" + code
										+ "], refcode [" + refcode + "].");
					}
					executionContext.getDataCodes().add(refcode);
					executionContext.getDataCodes().add(code);

					Set<String> valueCodes = new HashSet<String>();
					for (Value value : instance.getValue()) {
						String valueCode = value.getOf();
						if (valueCodes.contains(valueCode)) {
							throw new ValidationException(
									"Duplicate value of [" + valueCode
											+ "] on instance with code ["
											+ code + "].");
						}
						valueCodes.add(valueCode);
					}

					executionContext.getInstances().put(refcode, instance);
				}
				if (object instanceof Term) {
					Term term = (Term) object;
					executionContext.getTerms().add(term);
				}
				if (object instanceof UcGroup) {
					UcGroup ucGroup = (UcGroup) object;
					for (UseCase useCase : ucGroup.getUseCase()) {
						String code = useCase.getCode();
						String refcode = useCase.getRefcode();
						if (null == refcode) {
							refcode = code;
							useCase.setRefcode(code);
						}
						for (UseCase previous : executionContext.getUseCases()
								.values()) {
							if (previous.getCode().equals(code)
									|| previous.getRefcode().equals(refcode)) {
								throw new ValidationException(
										"Duplicate use case with code [" + code
												+ "].");
							}
						}
						executionContext.getUseCases().put(refcode, useCase);
						executionContext.getUcGroups().put(refcode, ucGroup);
					}
				}
			}
		}
		for (Actor actor : executionContext.getActors().values()) {
			validateActorInheritanceCycles(null, actor);
		}
	}

	private void validateActorInheritanceCycles(List<String> chain, Actor actor) {
		if (null == chain) {
			chain = new ArrayList<String>();
		} else {
			chain = new ArrayList<String>(chain);
		}
		chain.add(actor.getCode());
		for (ExtendsActor extendsActor : actor.getExtendsActor()) {
			String code = extendsActor.getCode();
			if (chain.contains(code)) {
				throw new ValidationException(
						"Inheritance cycle on actor with code [" + chain.get(0)
								+ "].");
			}
			Actor extended = executionContext.getActors().get(code);
			if (null != extended) {
				validateActorInheritanceCycles(chain, extended);
			}
		}
	}

}
