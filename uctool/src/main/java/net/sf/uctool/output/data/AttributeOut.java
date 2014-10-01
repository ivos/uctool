package net.sf.uctool.output.data;

import static net.sf.uctool.util.Escape.*;

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
		sb.append(escape(getName()));
		if (getCode().length() > 0) {
			sb.append(" <span class=\"pull-right\"><code>");
			sb.append(getCode());
			sb.append("</code></span>");
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

	public String getStatusLabelType() {
		if ("PK".equals(status)) {
			return "danger";
		} else if ("NK".equals(status)) {
			return "warning";
		} else if ("U".equals(status)) {
			return "warning";
		} else if ("M".equals(status)) {
			return "primary";
		} else if ("C".equals(status)) {
			return "info";
		} else if ("O".equals(status)) {
			return "success";
		}
		return "default";
	}

	public String getStatusLabel() {
		if (getStatus().length() == 0) {
			return "";
		}
		return "<span class=\"label label-" + getStatusLabelType() + "\">"
				+ getStatus() + "</span>";
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
