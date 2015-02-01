package net.sf.uctool.xsd.export;

import java.util.ArrayList;
import java.util.List;

public class DataXsdOut {

	private String code;
	private String name;
	private final List<String> descriptions = new ArrayList<String>();
	private final List<AttributeXsdOut> attributes = new ArrayList<AttributeXsdOut>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public List<AttributeXsdOut> getAttributes() {
		return attributes;
	}

}
