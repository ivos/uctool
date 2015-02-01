package net.sf.uctool.xsd.export;

public class AttributeXsdOut {

	private String name;
	private String code;
	private String status;
	private String type;
	private String description;
	private Boolean collection;
	private String length;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Boolean getCollection() {
		return collection;
	}

	public void setCollection(Boolean collection) {
		this.collection = collection;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public boolean getOptional() {
		return (null == status) || ("display-only".equals(status))
				|| ("optional".equals(status));
	}

	private boolean hasLength() {
		return (null != length) && (length.trim().length() != 0);
	}

	private boolean hasCollection() {
		return (null != collection) && (collection);
	}

	public boolean getUnbounded() {
		return hasCollection() && (!hasLength());
	}

	public boolean getBounded() {
		return hasCollection() && (hasLength());
	}

	public boolean getTypeAttr() {
		return (null != type)
				&& !(hasLength() && !hasCollection() && "xs:string"
						.equals(type));
	}

	public boolean getSimpleStringRestricted() {
		return hasLength() && !hasCollection() && "xs:string".equals(type);
	}

}
