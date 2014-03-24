package net.sf.uctool.output;

public class Reference {

	private final String code, label;

	public Reference(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public String getCode() {
		return code;
	}

	public String getLabel() {
		return label;
	}

}
