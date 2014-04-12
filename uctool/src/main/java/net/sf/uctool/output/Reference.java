package net.sf.uctool.output;

public class Reference {

	private final String type, code, label;

	public Reference(String type, String code, String label) {
		this.type = type;
		this.code = code;
		this.label = label;
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
