package net.sf.uctool.output.data;

import java.util.ArrayList;
import java.util.List;

public class DataOut {

	private String code;
	private String refcode;
	private String name;
	private String category;
	private final List<String> descriptions = new ArrayList<String>();
	private final List<AttributeOut> attributes = new ArrayList<AttributeOut>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public List<AttributeOut> getAttributes() {
		return attributes;
	}

	@Override
	public String toString() {
		return "DataOut [code=" + code + ", refcode=" + refcode + ", name="
				+ name + "]";
	}

}
