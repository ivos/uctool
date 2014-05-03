package net.sf.uctool.output;

import static net.sf.uctool.util.Escape.*;

public class Reference {

	private final String type, code, label;

	public Reference(String type, String code, String label) {
		this.type = type;
		this.code = code;
		this.label = escape(label);
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
