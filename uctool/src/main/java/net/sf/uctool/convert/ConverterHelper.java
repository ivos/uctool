package net.sf.uctool.convert;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.xsd.Attachment;
import net.sf.uctool.xsd.AttachmentGroup;
import net.sf.uctool.xsd.AttachmentRef;
import net.sf.uctool.xsd.DataRef;
import net.sf.uctool.xsd.DataStructure;
import net.sf.uctool.xsd.ReqRef;
import net.sf.uctool.xsd.Requirement;
import net.sf.uctool.xsd.StepRef;
import net.sf.uctool.xsd.Success;
import net.sf.uctool.xsd.UcRef;
import net.sf.uctool.xsd.UseCase;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

public class ConverterHelper {

	private final ExecutionContext executionContext;

	public ConverterHelper(ExecutionContext executionContext) {
		this.executionContext = executionContext;
	}

	public void writeDescription(StringBuilder sb, Object content,
			String referencedFromType, String referencedFromCode) {
		if (content instanceof String) {
			sb.append(content);
			String string = (String) content;
			if (string.length() > 0) {
				char lastChar = string.charAt(string.length() - 1);
				if (!Character.isWhitespace(lastChar)) {
					sb.append(' ');
				}
			}
		}
		if (content instanceof AttachmentRef) {
			AttachmentRef attachmentRef = ((AttachmentRef) content);
			String attachmentCode = attachmentRef.getCode();
			Attachment attachment = executionContext.getAttachments().get(
					attachmentCode);
			if (null == attachment) {
				throw new ValidationException("Missing attachment with code ["
						+ attachmentCode + "] referenced from "
						+ referencedFromType + " with code ["
						+ referencedFromCode + "].");
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
								+ "] referenced from " + referencedFromType
								+ " with code [" + referencedFromCode + "].");
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
						+ code + "] referenced from " + referencedFromType
						+ " with code [" + referencedFromCode + "].");
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
		if (content instanceof UcRef) {
			UcRef ucRef = (UcRef) content;
			String code = ucRef.getCode();
			UseCase useCase = executionContext.getUseCases().get(code);
			if (null == useCase) {
				throw new ValidationException("Missing use case with code ["
						+ code + "] referenced from " + referencedFromType
						+ " with code [" + referencedFromCode + "].");
			}
			sb.append("<a href=\"../uc/");
			sb.append(code);
			sb.append(".html\" title=\"");
			sb.append(code);
			sb.append(" - ");
			sb.append(useCase.getGoal());
			sb.append("\">");
			sb.append(ucRef.getValue());
			sb.append("</a>");
			executionContext.addUcRef(code, referencedFromCode);
		}
		if (content instanceof StepRef) {
			StepRef stepRef = (StepRef) content;
			String handle = stepRef.getHandle();
			UseCase useCase = executionContext.getCurrentUseCase();
			Integer stepNo = null;
			Success success = useCase.getSuccess();
			if (null != success) {
				for (int i = 0; i < success.getStep().size(); i++) {
					if (handle.equals(success.getStep().get(i).getHandle())) {
						stepNo = i + 1;
						break;
					}
				}
			}
			if (null == stepNo) {
				throw new ValidationException("Missing step with handle ["
						+ handle + "] referenced from " + referencedFromType
						+ " with code [" + referencedFromCode + "].");
			}
			sb.append(stepNo);
		}
		if (content instanceof Element) {
			Element element = (Element) content;
			String name = element.getLocalName();
			if ("attachment-ref".equals(name)) {
				AttachmentRef ref = new AttachmentRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode);
				return;
			}
			if ("data-ref".equals(name)) {
				DataRef ref = new DataRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode);
				return;
			}
			if ("req-ref".equals(name)) {
				ReqRef ref = new ReqRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode);
				return;
			}
			if ("uc-ref".equals(name)) {
				UcRef ref = new UcRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode);
				return;
			}
			if ("step-ref".equals(name)) {
				StepRef ref = new StepRef();
				ref.setHandle(element.getAttribute("handle"));
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode);
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
			NodeList childNodes = element.getChildNodes();
			if (0 == childNodes.getLength()) {
				sb.append(" />");
			} else {
				sb.append(">");
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					writeDescription(sb, child, referencedFromType,
							referencedFromCode);
				}
				sb.append("</");
				sb.append(name);
				sb.append(">");
			}
		}
		if (content instanceof Text) {
			Text text = (Text) content;
			sb.append(text.getTextContent());
		}
	}

}
