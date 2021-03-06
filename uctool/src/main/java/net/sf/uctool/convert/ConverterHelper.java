package net.sf.uctool.convert;

import static net.sf.uctool.util.Escape.*;

import java.util.Arrays;
import java.util.List;

import net.sf.uctool.exception.ValidationException;
import net.sf.uctool.execute.ExecutionContext;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.DataRef;
import net.sf.uctool.xsd.Instance;
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

	private String getRefPrefix() {
		if (executionContext.isSingle()) {
			return "#";
		}
		return "../";
	}

	private String getRefSeparator() {
		if (executionContext.isSingle()) {
			return "_";
		}
		return "/";
	}

	private String getRefSuffix() {
		if (executionContext.isSingle()) {
			return "";
		}
		return ".html";
	}

	public void writeDescription(StringBuilder sb, Object content,
			String referencedFromType, String referencedFromCode,
			String referencedFromRefcode) {
		if (content instanceof String) {
			String string = (String) content;
			sb.append(escape(string));
			if (string.length() > 0) {
				char lastChar = string.charAt(string.length() - 1);
				if (!Character.isWhitespace(lastChar)) {
					sb.append(' ');
				}
			}
		}
		if (content instanceof DataRef) {
			DataRef dataRef = (DataRef) content;
			String refcode = dataRef.getCode();
			String referencingCode, referencingName, type;
			Data referencingData = executionContext.getDatas().get(refcode);
			if (null != referencingData) {
				referencingCode = referencingData.getCode();
				referencingName = referencingData.getName();
				type = "data";
			} else {
				Instance referencingInstance = executionContext.getInstances()
						.get(refcode);
				if (null == referencingInstance) {
					throw new ValidationException(
							"Missing data or instance with refcode [" + refcode
									+ "] referenced from " + referencedFromType
									+ " with code [" + referencedFromCode
									+ "].");
				}
				referencingCode = referencingInstance.getCode();
				referencingName = referencingInstance.getName();
				type = "instance";
			}
			sb.append("<a href=\"" + getRefPrefix() + type + getRefSeparator());
			sb.append(referencingCode);
			sb.append(getRefSuffix() + "\" title=\"");
			sb.append(referencingName);
			sb.append("\">");
			sb.append(escape(dataRef.getValue()));
			if (executionContext.isSingle()) {
				sb.append(" (");
				sb.append(referencingName);
				sb.append(")");
			}
			sb.append("</a>");
			executionContext.addDataRef(refcode, referencedFromRefcode,
					referencedFromType);
		}
		if (content instanceof UcRef) {
			UcRef ucRef = (UcRef) content;
			String refcode = ucRef.getCode();
			UseCase useCase = executionContext.getUseCases().get(refcode);
			if (null == useCase) {
				throw new ValidationException("Missing use case with refcode ["
						+ refcode + "] referenced from " + referencedFromType
						+ " with code [" + referencedFromCode + "].");
			}
			String code = useCase.getCode();
			sb.append("<a href=\"" + getRefPrefix() + "uc" + getRefSeparator());
			sb.append(code);
			sb.append(getRefSuffix() + "\" title=\"");
			sb.append(code);
			sb.append(" - ");
			sb.append(useCase.getGoal());
			sb.append("\">");
			sb.append(escape(ucRef.getValue()));
			if (executionContext.isSingle()) {
				sb.append(" (");
				sb.append(code);
				sb.append(" - ");
				sb.append(useCase.getGoal());
				sb.append(")");
			}
			sb.append("</a>");
			executionContext.addUcRef(refcode, referencedFromRefcode);
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
			if ("data-ref".equals(name)) {
				DataRef ref = new DataRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode, referencedFromRefcode);
				return;
			}
			if ("uc-ref".equals(name)) {
				UcRef ref = new UcRef();
				ref.setCode(element.getAttribute("code"));
				ref.setValue(element.getTextContent());
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode, referencedFromRefcode);
				return;
			}
			if ("step-ref".equals(name)) {
				StepRef ref = new StepRef();
				ref.setHandle(element.getAttribute("handle"));
				writeDescription(sb, ref, referencedFromType,
						referencedFromCode, referencedFromRefcode);
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
			if (0 == childNodes.getLength()
					&& COLLAPSED_ELEMENTS.contains(name.toLowerCase())) {
				sb.append(" />");
			} else {
				sb.append(">");
				for (int i = 0; i < childNodes.getLength(); i++) {
					Node child = childNodes.item(i);
					writeDescription(sb, child, referencedFromType,
							referencedFromCode, referencedFromRefcode);
				}
				sb.append("</");
				sb.append(name);
				sb.append(">");
			}
		}
		if (content instanceof Text) {
			Text text = (Text) content;
			sb.append(escape(text.getTextContent()));
		}
	}

	private static final List<String> COLLAPSED_ELEMENTS = Arrays.asList("br",
			"hr");

}
