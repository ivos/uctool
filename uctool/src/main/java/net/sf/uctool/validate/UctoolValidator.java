package net.sf.uctool.validate;

import java.util.List;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Attachment;
import net.sf.uctool.xsd.AttachmentGroup;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.Requirement;
import net.sf.uctool.xsd.Term;
import net.sf.uctool.xsd.UcGroup;
import net.sf.uctool.xsd.Uct;
import net.sf.uctool.xsd.UseCase;

public class UctoolValidator {

	private final ExecutionContext executionContext;

	public UctoolValidator(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public void validate(List<Uct> inputs) {
		for (Uct uct : inputs) {
			for (Object object : uct.getTermOrRequirementOrUcGroup()) {
				if (object instanceof Actor) {
					Actor actor = (Actor) object;
					String code = actor.getCode();
					if (executionContext.getActors().containsKey(code)) {
						throw new ValidationException(
								"Duplicate actor with code [" + code + "].");
					}
					executionContext.getActors().put(code, actor);
				}
				if (object instanceof AttachmentGroup) {
					AttachmentGroup attachmentGroup = (AttachmentGroup) object;
					for (Attachment attachment : attachmentGroup
							.getAttachment()) {
						String code = attachment.getCode();
						if (executionContext.getAttachments().containsKey(code)) {
							throw new ValidationException(
									"Duplicate attachment with code [" + code
											+ "].");
						}
						executionContext.getAttachments().put(code, attachment);
						executionContext.getAttachmentGroups().put(code,
								attachmentGroup);
					}
				}
				if (object instanceof Data) {
					Data data = (Data) object;
					String code = data.getCode();
					if (executionContext.getAttachments().containsKey(code)) {
						throw new ValidationException(
								"Duplicate data with code [" + code + "].");
					}
					executionContext.getDatas().put(code, data);
				}
				if (object instanceof Requirement) {
					Requirement requirement = (Requirement) object;
					String code = requirement.getCode();
					if (executionContext.getRequirements().containsKey(code)) {
						throw new ValidationException(
								"Duplicate requirement with code [" + code
										+ "].");
					}
					executionContext.getRequirements().put(code, requirement);
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
						for (UseCase previous : executionContext.getUseCases()
								.values()) {
							if (previous.getCode().equals(code)) {
								throw new ValidationException(
										"Duplicate use case with code [" + code
												+ "].");
							}
						}
						if (null == refcode) {
							refcode = code;
							useCase.setRefcode(code);
						}
						executionContext.getUseCases().put(refcode, useCase);
						executionContext.getUcGroups().put(refcode, ucGroup);
					}
				}
			}
		}
	}

}
