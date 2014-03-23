package net.sf.uctool.execute;

import java.util.HashMap;
import java.util.Map;

import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Attachment;
import net.sf.uctool.xsd.AttachmentGroup;
import net.sf.uctool.xsd.DataStructure;
import net.sf.uctool.xsd.Requirement;

public class ExecutionContext {

	private final Map<String, Actor> actors;
	private final Map<String, Attachment> attachments;
	private final Map<String, AttachmentGroup> attachmentGroups;
	private final Map<String, DataStructure> dataStructures;
	private final Map<String, Requirement> requirements;

	public ExecutionContext() {
		actors = new HashMap<String, Actor>();
		attachments = new HashMap<String, Attachment>();
		attachmentGroups = new HashMap<String, AttachmentGroup>();
		dataStructures = new HashMap<String, DataStructure>();
		requirements = new HashMap<String, Requirement>();
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

}
