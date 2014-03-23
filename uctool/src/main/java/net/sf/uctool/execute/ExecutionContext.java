package net.sf.uctool.execute;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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

	private final Project project;
	private final Map<String, Actor> actors;
	private final Map<String, Attachment> attachments;
	private final Map<String, AttachmentGroup> attachmentGroups;
	private final Map<String, DataStructure> dataStructures;
	private final Map<String, Requirement> requirements;
	private final Set<Term> terms;
	private final Map<String, UseCase> useCases;
	private final Map<String, UcGroup> ucGroups;

	public ExecutionContext(Project project) {
		this.project = project;
		actors = new HashMap<String, Actor>();
		attachments = new HashMap<String, Attachment>();
		attachmentGroups = new HashMap<String, AttachmentGroup>();
		dataStructures = new HashMap<String, DataStructure>();
		requirements = new HashMap<String, Requirement>();
		terms = new HashSet<Term>();
		useCases = new HashMap<String, UseCase>();
		ucGroups = new HashMap<String, UcGroup>();
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

}
