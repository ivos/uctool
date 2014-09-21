package net.sf.uctool.execute;

public class Project {

	private final String name;
	private final String version;
	private final String description;
	private final String language;

	public Project(String name, String version, String description,
			String language) {
		this.name = name;
		this.version = version;
		this.description = description;
		this.language = language;
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

}
