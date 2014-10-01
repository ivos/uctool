package net.sf.uctool.output.data;

import java.util.ArrayList;
import java.util.List;

import net.sf.uctool.output.Reference;

import org.apache.commons.lang.StringUtils;

public class DataOut {

	private String code;
	private String refcode;
	private String name;
	private String category;
	private final List<String> descriptions = new ArrayList<String>();
	private final List<AttributeOut> attributes = new ArrayList<AttributeOut>();
	private final List<Reference> referencesData = new ArrayList<Reference>();
	private final List<Reference> referencesUcs = new ArrayList<Reference>();

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
		return StringUtils.defaultString(category);
	}

	public boolean hasCategory() {
		return getCategory().length() > 0;
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

	public AttributeOut getAttribute(String refcode) {
		for (AttributeOut iteratedAttributeOut : attributes) {
			String attributeRefcode = iteratedAttributeOut.getRefcode();
			if (null != attributeRefcode && attributeRefcode.equals(refcode)) {
				return iteratedAttributeOut;
			}
		}
		return null;
	}

	public List<Reference> getReferencesData() {
		return referencesData;
	}

	public List<Reference> getReferencesUcs() {
		return referencesUcs;
	}

	@Override
	public String toString() {
		return "DataOut [code=" + code + ", refcode=" + refcode + ", name="
				+ name + "]";
	}

}
