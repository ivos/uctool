package net.sf.uctool.convert;

import javax.xml.bind.JAXBElement;

import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.uc.InterestOut;
import net.sf.uctool.output.uc.UseCaseOut;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.Interest;
import net.sf.uctool.xsd.ItemsType;
import net.sf.uctool.xsd.TextType;
import net.sf.uctool.xsd.UcGroup;
import net.sf.uctool.xsd.UseCase;

public class UseCaseConverter {

	private final ExecutionContext executionContext;
	private final ConverterHelper converterHelper;

	public UseCaseConverter(ExecutionContext executionContext) {
		this.executionContext = executionContext;
		this.converterHelper = new ConverterHelper(executionContext);
	}

	public UseCaseOut convert(UseCase uc) {
		executionContext.setCurrentUseCase(uc);
		UseCaseOut o = new UseCaseOut();
		String code = uc.getCode();
		UcGroup group = executionContext.getUcGroups().get(code);
		o.setCode(code);
		o.setGoal(uc.getGoal());

		Actor primaryActor = executionContext.getActors().get(
				group.getPrimaryActor());
		o.setPrimaryActor(primaryActor);

		o.setTypeImageName(group.getVisibility() + "-" + group.getType());
		o.setTypeTitle(executionContext.getLabels().getString(
				"uc.type." + group.getType()));
		o.setVisibilityTitle(executionContext.getLabels().getString(
				"visibility." + group.getVisibility()));
		o.setLevel(uc.getLevel().value());
		o.setLevelTitle(executionContext.getLabels().getString(
				"level." + uc.getLevel().value()));
		o.setScope(group.getScope());

		for (DescriptionType descriptionType : uc.getDescription()) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				converterHelper.writeDescription(sb, content, "use case", code);
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
							code);
				}
				interestOut.setContent(sb.toString().trim());
				o.getInterests().add(interestOut);
			}
		}

		ItemsType preconditions = uc.getPreconditions();
		if (null != preconditions) {
			for (JAXBElement<TextType> itemOrText : preconditions
					.getItemOrText()) {
				StringBuilder sb = new StringBuilder();
				for (Object content : itemOrText.getValue().getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code);
				}
				String out = sb.toString().trim();
				if ("item".equals(itemOrText.getName().getLocalPart())) {
					out = "<li>" + out + "</li>";
				}
				o.getPreconditions().add(out);
			}
		}

		ItemsType minimalGuarantees = uc.getMinimalGuarantees();
		if (null != minimalGuarantees) {
			for (JAXBElement<TextType> itemOrText : minimalGuarantees
					.getItemOrText()) {
				StringBuilder sb = new StringBuilder();
				for (Object content : itemOrText.getValue().getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code);
				}
				String out = sb.toString().trim();
				if ("item".equals(itemOrText.getName().getLocalPart())) {
					out = "<li>" + out + "</li>";
				}
				o.getMinimalGuarantees().add(out);
			}
		}

		ItemsType successGuarantees = uc.getSuccessGuarantees();
		if (null != successGuarantees) {
			for (JAXBElement<TextType> itemOrText : successGuarantees
					.getItemOrText()) {
				StringBuilder sb = new StringBuilder();
				for (Object content : itemOrText.getValue().getContent()) {
					converterHelper.writeDescription(sb, content, "use case",
							code);
				}
				String out = sb.toString().trim();
				if ("item".equals(itemOrText.getName().getLocalPart())) {
					out = "<li>" + out + "</li>";
				}
				o.getSuccessGuarantees().add(out);
			}
		}

		o.setTrigger(uc.getTrigger());

		executionContext.setCurrentUseCase(null);
		return o;
	}
}
