package net.sf.uctool.output.data;

import org.apache.commons.lang.StringUtils;

public class AttributeOut {

	private String name;
	private String code;
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
		return "AttributeOut [name=" + name + ", code=" + code + "]";
	}

}
