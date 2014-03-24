package net.sf.uctool.convert;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.Reference;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Attachment;
import net.sf.uctool.xsd.AttachmentGroup;
import net.sf.uctool.xsd.AttachmentRef;
import net.sf.uctool.xsd.DataRef;
import net.sf.uctool.xsd.DataStructure;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.ExtendsActor;
import net.sf.uctool.xsd.ReqRef;
import net.sf.uctool.xsd.Requirement;
import net.sf.uctool.xsd.UseCase;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ActorConverter {

	private final ExecutionContext executionContext;

	public ActorConverter(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public ActorOut convert(Actor actor) {
		ActorOut o = new ActorOut();
		String code = actor.getCode();
		o.setCode(code);
		o.setName(actor.getName());
		for (DescriptionType descriptionType : actor.getDescription()) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				writeDescription(sb, content, code);
			}
			o.getDescriptions().add(sb.toString().trim());
		}
		Set<Actor> extendedActors = new LinkedHashSet<Actor>();
		for (ExtendsActor extendsActor : actor.getExtendsActor()) {
			String extendsCode = extendsActor.getCode();
			Actor extended = executionContext.getActors().get(extendsCode);
			if (null == extended) {
				throw new ValidationException("Missing actor with code ["
						+ extendsCode + "] referenced from actor with code ["
						+ code + "].");
			}
			o.getExtendsActors().add(
					new Reference(extendsCode, extended.getName()));
			extendedActors.add(extended);
		}
		for (Actor other : executionContext.getActors().values()) {
			for (ExtendsActor extendsActor : other.getExtendsActor()) {
				if (extendsActor.getCode().equals(code)) {
					o.getExtendedByActors().add(
							new Reference(other.getCode(), other.getName()));
				}
			}
		}
		List<UseCase> goals = getGoals(actor);
		for (UseCase useCase : goals) {
			o.getGoals().add(
					new Reference(useCase.getCode(), useCase.getCode() + " - "
							+ useCase.getGoal()));
		}
		Set<Actor> inheritedActors = new LinkedHashSet<Actor>();
		for (Actor extended : extendedActors) {
			getTransitiveExtendedActors(extended, inheritedActors);
		}
		for (Actor inherited : inheritedActors) {
			o.getInheritedActors().add(
					new Reference(inherited.getCode(), inherited.getName()));
		}
		Set<Actor> transitiveExtendedActors = new LinkedHashSet<Actor>();
		transitiveExtendedActors.addAll(extendedActors);
		transitiveExtendedActors.addAll(inheritedActors);
		Set<UseCase> inheritedGoals = new LinkedHashSet<UseCase>();
		for (Actor transitiveExtended : transitiveExtendedActors) {
			List<UseCase> actorGoals = getGoals(transitiveExtended);
			inheritedGoals.addAll(actorGoals);
		}
		for (UseCase useCase : inheritedGoals) {
			o.getInheritedGoals().add(
					new Reference(useCase.getCode(), useCase.getCode() + " - "
							+ useCase.getGoal()));
		}
		return o;
	}

	private List<UseCase> getGoals(Actor actor) {
		List<UseCase> goals = new ArrayList<UseCase>();
		for (UseCase useCase : executionContext.getUseCases().values()) {
			if (executionContext.getUcGroups().get(useCase.getCode())
					.getPrimaryActor().equals(actor.getCode())) {
				goals.add(useCase);
			}
		}
		return goals;
	}

	private void getTransitiveExtendedActors(Actor actor, Set<Actor> list) {
		for (ExtendsActor extendsActor : actor.getExtendsActor()) {
			Actor extended = executionContext.getActors().get(
					extendsActor.getCode());
			list.add(extended);
			getTransitiveExtendedActors(extended, list);
		}
	}

	public void writeDescription(StringBuilder sb, Object content,
			String actorCode) {
		if (content instanceof String) {
			sb.append(content);
			sb.append(' ');
		}
		if (content instanceof AttachmentRef) {
			AttachmentRef attachmentRef = ((AttachmentRef) content);
			String attachmentCode = attachmentRef.getCode();
			Attachment attachment = executionContext.getAttachments().get(
					attachmentCode);
			if (null == attachment) {
				throw new ValidationException("Missing attachment with code ["
						+ attachmentCode
						+ "] referenced from actor with code [" + actorCode
						+ "].");
			}
			AttachmentGroup attachmentGroup = executionContext
					.getAttachmentGroups().get(attachmentCode);
			sb.append("<a href=\"../attachment/");
			sb.append(attachmentGroup.getDirectory());
			sb.append("/");
			sb.append(attachment.getFileName());
			sb.append("\" title=\"");
			sb.append(attachment.getName());
			sb.append(" - ");
			sb.append(attachmentGroup.getDirectory());
			sb.append("/");
			sb.append(attachment.getFileName());
			sb.append("\">");
			sb.append(attachmentRef.getValue());
			sb.append("</a>");
		}
		if (content instanceof DataRef) {
			DataRef dataRef = (DataRef) content;
			String code = dataRef.getCode();
			DataStructure dataStructure = executionContext.getDataStructures()
					.get(code);
			if (null == dataStructure) {
				throw new ValidationException(
						"Missing data structure with code [" + code
								+ "] referenced from actor with code ["
								+ actorCode + "].");
			}
			sb.append("<a href=\"../data/");
			sb.append(code);
			sb.append(".html\" title=\"");
			sb.append(dataStructure.getName());
			sb.append("\">");
			sb.append(dataRef.getValue());
			sb.append("</a>");
		}
		if (content instanceof ReqRef) {
			ReqRef reqRef = (ReqRef) content;
			String code = reqRef.getCode();
			Requirement requirement = executionContext.getRequirements().get(
					code);
			if (null == requirement) {
				throw new ValidationException("Missing requirement with code ["
						+ code + "] referenced from actor with code ["
						+ actorCode + "].");
			}
			sb.append("<a href=\"../req/");
			sb.append(code);
			sb.append(".html\" title=\"");
			sb.append(code);
			sb.append(" - ");
			sb.append(requirement.getName());
			sb.append("\">");
			sb.append(reqRef.getValue());
			sb.append("</a>");
		}
		if (content instanceof Element) {
			Element element = (Element) content;
			String name = element.getLocalName();
			if ("attachment-ref".equals(name)) {
				AttachmentRef ref = new AttachmentRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, actorCode);
				return;
			}
			if ("data-ref".equals(name)) {
				DataRef ref = new DataRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, actorCode);
				return;
			}
			if ("req-ref".equals(name)) {
				ReqRef ref = new ReqRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, actorCode);
				return;
			}
			sb.append("<");
			sb.append(name);
			NamedNodeMap attributes = element.getAttributes();
			for (int i = 0; i < attributes.getLength(); i++) {
				Attr attr = (Attr) attributes.item(i);
				if (!"xmlns".equals(attr.getName())
						&& !"xmlns".equals(attr.getPrefix())) {
					sb.append(' ');
					sb.append(attr.getName());
					sb.append("=\"");
					sb.append(attr.getValue());
					sb.append('"');
				}
			}
			sb.append(">");
			NodeList childNodes = element.getChildNodes();
			for (int i = 0; i < childNodes.getLength(); i++) {
				Node child = childNodes.item(i);
				writeDescription(sb, child, actorCode);
			}
			sb.append("</");
			sb.append(name);
			sb.append(">");
		}
		if (content instanceof Text) {
			Text text = (Text) content;
			sb.append(text.getTextContent());
		}
	}

}
