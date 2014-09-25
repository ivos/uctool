package net.sf.uctool.output.data;

import net.sf.uctool.output.Reference;
import net.sf.uctool.xsd.Instance;

import org.apache.commons.lang.StringUtils;

public class ValueOut {

	private AttributeOut of;
	private AttributeOut fromAttribute;
	private DataOut fromData;
	private Instance fromInstance;
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

	public Instance getFromInstance() {
		return fromInstance;
	}

	public void setFromInstance(Instance fromInstance) {
		this.fromInstance = fromInstance;
	}

	public boolean isFromDataAttribute() {
		return (null != fromData);
	}

	public Reference getFromDataRef() {
		if (null != fromData) {
			return new Reference("data", fromData.getCode(), fromData.getName());
		}
		return new Reference("instance", fromInstance.getCode(),
				fromInstance.getName());
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
				+ ", fromData=" + fromData + ", fromInstance="
				+ fromInstance.getCode() + "]";
	}

}
