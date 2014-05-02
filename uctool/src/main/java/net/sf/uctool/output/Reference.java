package net.sf.uctool.output;

import static org.apache.commons.lang.StringEscapeUtils.*;

public class Reference {

	private final String type, code, label;

	public Reference(String type, String code, String label) {
		this.type = type;
		this.code = code;
		this.label = escapeHtml(label);
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

}
