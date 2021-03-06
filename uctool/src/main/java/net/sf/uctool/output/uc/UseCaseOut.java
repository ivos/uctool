package net.sf.uctool.output.uc;

import java.util.ArrayList;
import java.util.List;

import net.sf.uctool.output.Reference;
import net.sf.uctool.output.actor.ActorOut;

public class UseCaseOut {

	private String code;
	private String refcode;
	private String goal;
	private ActorOut primaryActor;
	private String typeImageName;
	private String typeTitle;
	private String visibilityTitle;
	private String level;
	private String levelTitle;
	private String scope;
	private final List<String> descriptions = new ArrayList<String>();
	private final List<InterestOut> interests = new ArrayList<InterestOut>();
	private final List<String> preconditions = new ArrayList<String>();
	private String preconditionsText;
	private final List<String> minimalGuarantees = new ArrayList<String>();
	private String minimalGuaranteesText;
	private final List<String> successGuarantees = new ArrayList<String>();
	private String successGuaranteesText;
	private String trigger;
	private final List<StepOut> steps = new ArrayList<StepOut>();
	private final List<ExtensionOut> extensions = new ArrayList<ExtensionOut>();
	private String notes;
	private final List<Reference> references = new ArrayList<Reference>();

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRefcode() {
		return refcode;
	}

	public void setRefcode(String refcode) {
		this.refcode = refcode;
	}

	public String getGoal() {
		return goal;
	}

	public void setGoal(String goal) {
		this.goal = goal;
	}

	public ActorOut getPrimaryActor() {
		return primaryActor;
	}

	public void setPrimaryActor(ActorOut primaryActor) {
		this.primaryActor = primaryActor;
	}

	public Reference getPrimaryActorRef() {
		return new Reference("actor", primaryActor.getCode(),
				primaryActor.getName(), false);
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

	public String getPreconditionsText() {
		return preconditionsText;
	}

	public void setPreconditionsText(String preconditionsText) {
		this.preconditionsText = preconditionsText;
	}

	public List<String> getMinimalGuarantees() {
		return minimalGuarantees;
	}

	public String getMinimalGuaranteesText() {
		return minimalGuaranteesText;
	}

	public void setMinimalGuaranteesText(String minimalGuaranteesText) {
		this.minimalGuaranteesText = minimalGuaranteesText;
	}

	public List<String> getSuccessGuarantees() {
		return successGuarantees;
	}

	public String getSuccessGuaranteesText() {
		return successGuaranteesText;
	}

	public void setSuccessGuaranteesText(String successGuaranteesText) {
		this.successGuaranteesText = successGuaranteesText;
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public List<Reference> getReferences() {
		return references;
	}

	@Override
	public String toString() {
		return "UseCaseOut [code=" + code + ", refcode=" + refcode + ", goal="
				+ goal + "]";
	}

}
