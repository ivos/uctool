package net.sf.uctool.execute;

public class Project {

	private final String name;
	private final String version;
	private final String description;
	private final String language;
	private final String cdnProtocol;

	public Project(String name, String version, String description,
			String language, String cdnProtocol) {
		this.name = name;
		this.version = version;
		this.description = description;
		this.language = language;
		this.cdnProtocol = cdnProtocol;
	}

	public String getName() {
		return name;
	}

	public String getVersion() {
		return version;
	}

	public String getDescription() {
		return description;
	}

	public String getLanguage() {
		return language;
	}

	public String getCdnProtocol() {
		return cdnProtocol;
	}

	@Override
	public String toString() {
		return "Project [name=" + name + ", version=" + version
				+ ", description=" + description + ", language=" + language
				+ ", cdnProtocol=" + cdnProtocol + "]";
	}

}
