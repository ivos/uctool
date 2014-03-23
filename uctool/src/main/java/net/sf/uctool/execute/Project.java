package net.sf.uctool.execute;

public class Project {

	private String name;
	private String version;
	private String description;

	public Project() {
	}

	public Project(String name, String version, String description) {
		this.name = name;
		this.version = version;
		this.description = description;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
