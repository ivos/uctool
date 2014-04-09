package net.sf.uctool.output.uc;

import java.util.ArrayList;
import java.util.List;

import net.sf.uctool.xsd.Actor;

public class UseCaseOut {

	private String code;
	private String goal;
	private Actor primaryActor;
	private String typeImageName;
	private String typeTitle;
	private String visibilityTitle;
	private String level;
	private String levelTitle;
	private String scope;
	private final List<String> descriptions = new ArrayList<String>();
	private final List<InterestOut> interests = new ArrayList<InterestOut>();
	private final List<String> preconditions = new ArrayList<String>();
	private final List<String> minimalGuarantees = new ArrayList<String>();
	private final List<String> successGuarantees = new ArrayList<String>();
	private String trigger;
	private final List<StepOut> steps = new ArrayList<StepOut>();
	private final List<ExtensionOut> extensions = new ArrayList<ExtensionOut>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public Actor getPrimaryActor() {
		return primaryActor;
	}

	public void setPrimaryActor(Actor primaryActor) {
		this.primaryActor = primaryActor;
	}

	public String getTypeImageName() {
		return typeImageName;
	}

	public void setTypeImageName(String typeImageName) {
		this.typeImageName = typeImageName;
	}

	public String getTypeTitle() {
		return typeTitle;
	}

	public void setTypeTitle(String typeTitle) {
		this.typeTitle = typeTitle;
	}

	public String getVisibilityTitle() {
		return visibilityTitle;
	}

	public void setVisibilityTitle(String visibilityTitle) {
		this.visibilityTitle = visibilityTitle;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getLevelTitle() {
		return levelTitle;
	}

	public void setLevelTitle(String levelTitle) {
		this.levelTitle = levelTitle;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public List<InterestOut> getInterests() {
		return interests;
	}

	public List<String> getPreconditions() {
		return preconditions;
	}

	public List<String> getMinimalGuarantees() {
		return minimalGuarantees;
	}

	public List<String> getSuccessGuarantees() {
		return successGuarantees;
	}

	public String getTrigger() {
		return trigger;
	}

	public void setTrigger(String trigger) {
		this.trigger = trigger;
	}

	public List<StepOut> getSteps() {
		return steps;
	}

	public List<ExtensionOut> getExtensions() {
		return extensions;
	}

}
