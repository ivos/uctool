package net.sf.uctool.convert;

import static net.sf.uctool.util.Escape.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.Reference;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.uc.ExtensionOut;
import net.sf.uctool.output.uc.InterestOut;
import net.sf.uctool.output.uc.StepOut;
import net.sf.uctool.output.uc.UseCaseOut;
import net.sf.uctool.xsd.Condition;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.Extensions;
import net.sf.uctool.xsd.Interest;
import net.sf.uctool.xsd.ItemsType;
import net.sf.uctool.xsd.Step;
import net.sf.uctool.xsd.StepCondition;
import net.sf.uctool.xsd.StepExtensions;
import net.sf.uctool.xsd.Success;
import net.sf.uctool.xsd.TextType;
import net.sf.uctool.xsd.UcGroup;
import net.sf.uctool.xsd.UseCase;
import net.sf.uctool.xsd.When;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UseCaseConverter {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final ExecutionContext executionContext;
	private final ConverterHelper converterHelper;

	public UseCaseConverter(ExecutionContext executionContext) {
		this.executionContext = executionContext;
		this.converterHelper = new ConverterHelper(executionContext);
	}

	public UseCaseOut convert(UseCase uc) {
		logger.debug("Converting use case {}.", uc.getCode());
		executionContext.setCurrentUseCase(uc);
		UseCaseOut o = new UseCaseOut();
		String code = uc.getCode();
		String refcode = uc.getRefcode();
		UcGroup group = executionContext.getUcGroups().get(refcode);
		o.setCode(code);
		o.setRefcode(refcode);
		o.setGoal(escape(uc.getGoal()));

		ActorOut primaryActor = executionContext.getActorOuts().get(
				group.getPrimaryActor());
		if (null == primaryActor) {
			throw new ValidationException("Missing actor with code ["
					+ group.getPrimaryActor()
					+ "] referenced from use case with code [" + code + "].");
		}
		o.setPrimaryActor(primaryActor);

		if (null != group.getVisibility() && null != group.getType()) {
			o.setTypeImageName(group.getVisibility() + "-" + group.getType());
		}
		if (null != group.getType()) {
			o.setTypeTitle(executionContext.label("uc.type." + group.getType()));
		}
		if (null != group.getVisibility()) {
			o.setVisibilityTitle(executionContext.label("visibility."
					+ group.getVisibility()));
		}
		if (null != uc.getLevel()) {
			o.setLevel(uc.getLevel().value());
			o.setLevelTitle(executionContext.label("level."
					+ uc.getLevel().value()));
		}
		o.setScope(escape(group.getScope()));

		for (DescriptionType descriptionType : uc.getDescription()) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				converterHelper.writeDescription(sb, content, "use case", code,
						refcode);
			}
			o.getDescriptions().add(sb.toString().trim());
		}

		if (null != uc.getStakeholdersInterests()
				&& null != uc.getStakeholdersInterests().getInterest()) {
			for (Interest interest : uc.getStakeholdersInterests()
					.getInterest()) {
				InterestOut interestOut = new InterestOut();
				interestOut.setStakeholder(interest.getStakeholder());
				StringBuilder sb = new StringBuilder();
				for (Object content : interest.getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code, refcode);
				}
				interestOut.setContent(sb.toString().trim());
				o.getInterests().add(interestOut);
			}
		}

		ItemsType preconditions = uc.getPreconditions();
		if (null != preconditions) {
			TextType text = preconditions.getText();
			if (null != text) {
				StringBuilder sb = new StringBuilder();
				for (Object content : text.getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code, refcode);
				}
				String out = sb.toString().trim();
				o.setPreconditionsText(out);
			} else {
				for (TextType item : preconditions.getItem()) {
					StringBuilder sb = new StringBuilder();
					for (Object content : item.getContent()) {
						converterHelper.writeDescription(sb, content,
								"use case", code, refcode);
					}
					String out = sb.toString().trim();
					out = "<li>" + out + "</li>";
					o.getPreconditions().add(out);
				}
			}
		}

		ItemsType minimalGuarantees = uc.getMinimalGuarantees();
		if (null != minimalGuarantees) {
			TextType text = minimalGuarantees.getText();
			if (null != text) {
				StringBuilder sb = new StringBuilder();
				for (Object content : text.getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code, refcode);
				}
				String out = sb.toString().trim();
				o.setMinimalGuaranteesText(out);
			} else {
				for (TextType item : minimalGuarantees.getItem()) {
					StringBuilder sb = new StringBuilder();
					for (Object content : item.getContent()) {
						converterHelper.writeDescription(sb, content,
								"use case", code, refcode);
					}
					String out = sb.toString().trim();
					out = "<li>" + out + "</li>";
					o.getMinimalGuarantees().add(out);
				}
			}
		}

		ItemsType successGuarantees = uc.getSuccessGuarantees();
		if (null != successGuarantees) {
			TextType text = successGuarantees.getText();
			if (null != text) {
				StringBuilder sb = new StringBuilder();
				for (Object content : text.getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code, refcode);
				}
				String out = sb.toString().trim();
				o.setSuccessGuaranteesText(out);
			} else {
				for (TextType item : successGuarantees.getItem()) {
					StringBuilder sb = new StringBuilder();
					for (Object content : item.getContent()) {
						converterHelper.writeDescription(sb, content,
								"use case", code, refcode);
					}
					String out = sb.toString().trim();
					out = "<li>" + out + "</li>";
					o.getSuccessGuarantees().add(out);
				}
			}
		}

		o.setTrigger(escape(uc.getTrigger()));

		Success success = uc.getSuccess();
		if (null != success) {
			List<Step> steps = success.getStep();
			for (int i = 0; i < steps.size(); i++) {
				Step step = steps.get(i);
				StringBuilder sb = new StringBuilder();
				for (Object content : step.getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code, refcode);
				}
				StepOut stepOut = new StepOut();
				stepOut.setNumber("" + (i + 1));
				stepOut.setContent(sb.toString().trim());
				o.getSteps().add(stepOut);
			}
		}

		Extensions extensions = uc.getExtensions();
		if (null != extensions) {
			Map<String, String> stepRefs = new HashMap<String, String>();
			List<Condition> conditions = extensions.getCondition();
			for (Condition condition : conditions) {
				ExtensionOut extensionOut = new ExtensionOut();
				extensionOut.setIndent("");
				String conditionNumber = expandStepRef(code,
						condition.getStepRef(), stepRefs);
				extensionOut.setNumber(conditionNumber);
				String when;
				When whenElement = condition.getWhen();
				if (null != whenElement) {
					StringBuilder sb = new StringBuilder();
					for (Object content : whenElement.getContent()) {
						converterHelper.writeDescription(sb, content,
								"use case", code, refcode);
					}
					when = sb.toString();
				} else {
					when = "";
				}
				String content = when.trim() + ":";
				if (null != condition.getInlineStep()) {
					content += " " + condition.getInlineStep().trim();
				}
				extensionOut.setContent(content);
				o.getExtensions().add(extensionOut);

				processConditionSteps(code, refcode, o, conditionNumber,
						"&nbsp;&nbsp;&nbsp;&nbsp;",
						condition.getStepOrStepExtensions(), stepRefs);
			}
		}

		DescriptionType notes = uc.getNotes();
		if (null != notes) {
			StringBuilder sb = new StringBuilder();
			for (Object content : notes.getContent()) {
				converterHelper.writeDescription(sb, content, "use case", code,
						refcode);
			}
			o.setNotes(sb.toString().trim());
		}

		executionContext.setCurrentUseCase(null);
		return o;
	}

	private void processConditionSteps(String ucCode, String ucRefcode,
			UseCaseOut o, String conditionNumber, String indent,
			List<Object> stepOrStepExtensionsList, Map<String, String> stepRefs) {
		int i = 0;
		String stepNumber = null;
		for (Object stepOrStepExtensions : stepOrStepExtensionsList) {
			if (stepOrStepExtensions instanceof Step) {
				i++;
				Step step = (Step) stepOrStepExtensions;
				ExtensionOut extensionOut = new ExtensionOut();
				extensionOut.setIndent(indent);
				stepNumber = conditionNumber + i;
				extensionOut.setNumber(stepNumber);
				StringBuilder sb = new StringBuilder();
				for (Object stepContent : step.getContent()) {
					converterHelper.writeDescription(sb, stepContent,
							"use case", ucCode, ucRefcode);
				}
				extensionOut.setContent(sb.toString().trim());
				o.getExtensions().add(extensionOut);
			}
			if (stepOrStepExtensions instanceof StepExtensions) {
				StepExtensions stepExtensions = (StepExtensions) stepOrStepExtensions;
				List<StepCondition> stepConditions = stepExtensions
						.getStepCondition();

				for (StepCondition stepCondition : stepConditions) {
					ExtensionOut extensionOut = new ExtensionOut();
					extensionOut.setIndent(indent + "&nbsp;&nbsp;&nbsp;&nbsp;");
					String stepConditionNumber = appendCaseToStepRef(
							stepNumber, stepRefs);
					extensionOut.setNumber(stepConditionNumber);
					String stepWhen;
					When stepWhenElement = stepCondition.getWhen();
					if (null != stepWhenElement) {
						StringBuilder sb = new StringBuilder();
						for (Object stepContent : stepWhenElement.getContent()) {
							converterHelper.writeDescription(sb, stepContent,
									"use case", ucCode, ucRefcode);
						}
						stepWhen = sb.toString();
					} else {
						stepWhen = "";
					}
					String stepContent = stepWhen.trim() + ":";
					if (null != stepCondition.getInlineStep()) {
						stepContent += " "
								+ stepCondition.getInlineStep().trim();
					}
					extensionOut.setContent(stepContent);
					o.getExtensions().add(extensionOut);

					processConditionSteps(ucCode, ucRefcode, o,
							stepConditionNumber, indent
									+ "&nbsp;&nbsp;&nbsp;&nbsp;"
									+ "&nbsp;&nbsp;&nbsp;&nbsp;",
							stepCondition.getStepOrStepExtensions(), stepRefs);
				}
			}
		}
	}

	private String expandStepRef(String ucCode, String stepRef,
			Map<String, String> stepRefs) {
		if ("*".equals(stepRef)) {
			return appendCaseToStepRef("*", stepRefs);
		}
		if (stepRef.contains("-")) {
			String[] split = stepRef.split("-");
			return appendCaseToStepRef(expandStepRef(ucCode, split[0], null)
					+ "‑" + expandStepRef(ucCode, split[1], null), stepRefs);
		}
		List<Step> steps = executionContext.getCurrentUseCase().getSuccess()
				.getStep();
		for (int i = 0; i < steps.size(); i++) {
			if (stepRef.equals(steps.get(i).getHandle())) {
				return appendCaseToStepRef("" + (i + 1), stepRefs);
			}
		}
		throw new ValidationException("Missing step with handle [" + stepRef
				+ "] referenced from extension in use case [" + ucCode + "].");
	}

	private String appendCaseToStepRef(String stepRef,
			Map<String, String> stepRefs) {
		if (null == stepRefs) {
			return stepRef;
		}
		String case_ = stepRefs.get(stepRef);
		if (null == case_) {
			case_ = "a";
		} else {
			case_ = "" + (char) (case_.charAt(0) + 1);
		}
		stepRefs.put(stepRef, case_);
		return stepRef + case_;
	}

	public void addReferences(UseCaseOut o) {
		logger.debug("Adding references to use case {}.", o);
		Set<String> references = executionContext.getUcReferences().get(
				o.getRefcode());
		if (null != references) {
			for (String referencingRefcode : references) {
				UseCase referencingUseCase = executionContext.getUseCases()
						.get(referencingRefcode);
				String referencingCode = referencingUseCase.getCode();
				Reference reference = new Reference("uc", referencingCode,
						referencingCode + " - " + referencingUseCase.getGoal());
				o.getReferences().add(reference);
				logger.debug("Added reference {}.", reference);
			}
		}
	}

}
