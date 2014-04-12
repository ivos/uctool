package net.sf.uctool.execute;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Attachment;
import net.sf.uctool.xsd.AttachmentGroup;
import net.sf.uctool.xsd.DataStructure;
import net.sf.uctool.xsd.Requirement;
import net.sf.uctool.xsd.Term;
import net.sf.uctool.xsd.UcGroup;
import net.sf.uctool.xsd.UseCase;

public class ExecutionContext {

	private final ResourceBundle labels;
	private final Project project;
	private final Map<String, Actor> actors;
	private final Map<String, Attachment> attachments;
	private final Map<String, AttachmentGroup> attachmentGroups;
	private final Map<String, DataStructure> dataStructures;
	private final Map<String, Requirement> requirements;
	private final Set<Term> terms;
	private final Map<String, UseCase> useCases;
	private final Map<String, UcGroup> ucGroups;
	private final Map<String, Set<String>> ucReferences;

	private UseCase currentUseCase;

	public ExecutionContext(ResourceBundle labels, Project project) {
		this.labels = labels;
		this.project = project;
		actors = new LinkedHashMap<String, Actor>();
		attachments = new LinkedHashMap<String, Attachment>();
		attachmentGroups = new LinkedHashMap<String, AttachmentGroup>();
		dataStructures = new LinkedHashMap<String, DataStructure>();
		requirements = new LinkedHashMap<String, Requirement>();
		terms = new LinkedHashSet<Term>();
		useCases = new LinkedHashMap<String, UseCase>();
		ucGroups = new LinkedHashMap<String, UcGroup>();
		ucReferences = new LinkedHashMap<String, Set<String>>();
	}

	public ResourceBundle getLabels() {
		return labels;
	}

	public Project getProject() {
		return project;
	}

	public Map<String, Actor> getActors() {
		return actors;
	}

	public Map<String, Attachment> getAttachments() {
		return attachments;
	}

	public Map<String, AttachmentGroup> getAttachmentGroups() {
		return attachmentGroups;
	}

	public Map<String, DataStructure> getDataStructures() {
		return dataStructures;
	}

	public Map<String, Requirement> getRequirements() {
		return requirements;
	}

	public Set<Term> getTerms() {
		return terms;
	}

	public Map<String, UseCase> getUseCases() {
		return useCases;
	}

	public Map<String, UcGroup> getUcGroups() {
		return ucGroups;
	}

	public UseCase getCurrentUseCase() {
		return currentUseCase;
	}

	public void setCurrentUseCase(UseCase currentUseCase) {
		this.currentUseCase = currentUseCase;
	}

	public Map<String, Set<String>> getUcReferences() {
		return ucReferences;
	}

	public void addUcRef(String codeUcReferenced, String codeUcReferencing) {
		Set<String> references = ucReferences.get(codeUcReferenced);
		if (null == references) {
			references = new LinkedHashSet<String>();
			ucReferences.put(codeUcReferenced, references);
		}
		references.add(codeUcReferencing);
	}

}
