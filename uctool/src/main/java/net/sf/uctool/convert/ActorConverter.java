package net.sf.uctool.convert;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Attachment;
import net.sf.uctool.xsd.AttachmentGroup;
import net.sf.uctool.xsd.AttachmentRef;
import net.sf.uctool.xsd.DataRef;
import net.sf.uctool.xsd.DataStructure;
import net.sf.uctool.xsd.DescriptionType;
import net.sf.uctool.xsd.ReqRef;
import net.sf.uctool.xsd.Requirement;

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
		o.setCode(actor.getCode());
		o.setName(actor.getName());
		for (DescriptionType descriptionType : actor.getDescription()) {
			StringBuilder sb = new StringBuilder();
			for (Object content : descriptionType.getContent()) {
				writeDescription(sb, content);
			}
			o.getDescriptions().add(sb.toString().trim());
		}
		return o;
	}

	public void writeDescription(StringBuilder sb, Object content) {
		if (content instanceof String) {
			sb.append(content);
			sb.append(' ');
		}
		if (content instanceof AttachmentRef) {
			AttachmentRef attachmentRef = ((AttachmentRef) content);
			String code = attachmentRef.getCode();
			Attachment attachment = executionContext.getAttachments().get(code);
			AttachmentGroup attachmentGroup = executionContext
					.getAttachmentGroups().get(code);
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
						"Invalid data structure reference, no data structure with code ["
								+ code + "] found.");
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
				throw new ValidationException(
						"Invalid data requirement, no requirement with code ["
								+ code + "] found.");
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
				writeDescription(sb, ref);
				return;
			}
			if ("data-ref".equals(name)) {
				DataRef ref = new DataRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref);
				return;
			}
			if ("req-ref".equals(name)) {
				ReqRef ref = new ReqRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref);
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
				writeDescription(sb, child);
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
