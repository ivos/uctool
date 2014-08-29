package net.sf.uctool.output.data;

import net.sf.uctool.output.Reference;

import org.apache.commons.lang.StringUtils;

public class ValueOut {

	private AttributeOut of;
	private AttributeOut fromAttribute;
	private DataOut fromData;
	private String description;

	public AttributeOut getOf() {
		return of;
	}

	public void setOf(AttributeOut of) {
		this.of = of;
	}

	public AttributeOut getFromAttribute() {
		return fromAttribute;
	}

	public void setFromAttribute(AttributeOut fromAttribute) {
		this.fromAttribute = fromAttribute;
	}

	public DataOut getFromData() {
		return fromData;
	}

	public void setFromData(DataOut fromData) {
		this.fromData = fromData;
	}

	public Reference getFromDataRef() {
		return new Reference("data", fromData.getCode(), fromData.getName());
	}

	public String getDescription() {
		return StringUtils.defaultString(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "ValueOut [of=" + of + ", fromAttribute=" + fromAttribute
				+ ", fromData=" + fromData + "]";
	}

}
