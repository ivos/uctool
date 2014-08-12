package net.sf.uctool.output;

import static net.sf.uctool.util.Escape.*;

public class Reference {

	private final String type, code, label;

	public Reference(String type, String code, String label) {
		this(type, code, label, true);
	}

	public Reference(String type, String code, String label, boolean escapeLabel) {
		this.type = type;
		this.code = code;
		this.label = escapeLabel ? escape(label) : label;
	}

	public String getType() {
		return type;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

	public String toHtml() {
		return "<a href=\"../" + type + "/" + code + ".html\" title=\"" + label
				+ "\">" + label + "</a>";
	}

	@Override
	public String toString() {
		return "Reference [type=" + type + ", code=" + code + ", label="
				+ label + "]";
	}

}
