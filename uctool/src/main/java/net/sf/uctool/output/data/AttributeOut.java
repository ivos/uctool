package net.sf.uctool.output.data;

import org.apache.commons.lang.StringUtils;

public class AttributeOut {

	private String name;
	private String code;
	private String refcode;
	private String status;
	private String statusHint;
	private String type;
	private String description;

	public String getName() {
		return StringUtils.defaultString(name);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return StringUtils.defaultString(code);
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNameAndCodeLabel() {
		StringBuffer sb = new StringBuffer();
		sb.append(getName());
		if (getCode().length() > 0) {
			if (getName().length() > 0) {
				sb.append(", ");
			}
			sb.append("<code>");
			sb.append(getCode());
			sb.append("</code>");
		}
		return sb.toString();
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}

	public String getStatus() {
		return StringUtils.defaultString(status);
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getStatusHint() {
		return statusHint;
	}

	public void setStatusHint(String statusHint) {
		this.statusHint = statusHint;
	}

	public String getType() {
		return StringUtils.defaultString(type);
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return StringUtils.defaultString(description);
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public String toString() {
		return "AttributeOut [name=" + name + ", code=" + code + ", refcode="
				+ refcode + "]";
	}

}
