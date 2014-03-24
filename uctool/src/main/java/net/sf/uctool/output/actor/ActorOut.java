package net.sf.uctool.output.actor;

import java.util.ArrayList;
import java.util.List;

import net.sf.uctool.output.Reference;

public class ActorOut {

	private String code;
	private String name;
	private final List<String> descriptions = new ArrayList<String>();
	private final List<Reference> extendsActors = new ArrayList<Reference>();
	private final List<Reference> extendedByActors = new ArrayList<Reference>();
	private final List<Reference> goals = new ArrayList<Reference>();
	private final List<Reference> inheritedActors = new ArrayList<Reference>();
	private final List<Reference> inheritedGoals = new ArrayList<Reference>();

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

	public List<Reference> getExtendsActors() {
		return extendsActors;
	}

	public List<Reference> getExtendedByActors() {
		return extendedByActors;
	}

	public List<Reference> getGoals() {
		return goals;
	}

	public List<Reference> getInheritedActors() {
		return inheritedActors;
	}

	public List<Reference> getInheritedGoals() {
		return inheritedGoals;
	}

}
