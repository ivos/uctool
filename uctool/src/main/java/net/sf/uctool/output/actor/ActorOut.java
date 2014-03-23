package net.sf.uctool.output.actor;

import java.util.ArrayList;
import java.util.List;

public class ActorOut {

	private String code;
	private String name;
	private final List<String> descriptions = new ArrayList<String>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

}
