package net.sf.uctool.execute;

public class Project {

	private final String name;
	private final String version;
	private final String description;
	private final String encoding;
	private final String language;
	private final String cdnProtocol;

	public Project(String name, String version, String description,
			String encoding, String language, String cdnProtocol) {
		this.name = name;
		this.version = version;
		this.description = description;
		this.encoding = encoding;
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

	public String getEncoding() {
		return encoding;
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
				+ ", description=" + description + ", encoding=" + encoding
				+ ", language=" + language + ", cdnProtocol=" + cdnProtocol
				+ "]";
	}

}
