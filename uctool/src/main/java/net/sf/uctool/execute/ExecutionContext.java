package net.sf.uctool.execute;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import net.sf.uctool.output.actor.ActorOut;
import net.sf.uctool.output.data.DataOut;
import net.sf.uctool.output.data.InstanceOut;
import net.sf.uctool.output.term.TermOut;
import net.sf.uctool.output.uc.UseCaseOut;
import net.sf.uctool.xsd.Actor;
import net.sf.uctool.xsd.Data;
import net.sf.uctool.xsd.Instance;
import net.sf.uctool.xsd.Term;
import net.sf.uctool.xsd.UcGroup;
import net.sf.uctool.xsd.UseCase;

public class ExecutionContext {

	private final ResourceBundle labels;
	private final Project project;
	private final Map<String, Actor> actors;
	private final Map<String, Data> datas;
	private final Map<String, Instance> instances;
	private final Set<Term> terms;
	private final Map<String, UseCase> useCases;
	private final Map<String, UcGroup> ucGroups;
	private final Map<String, Set<String>> ucReferences;
	private final Map<String, Set<String>> dataReferencesData;
	private final Map<String, Set<String>> dataInstances;
	private final Map<String, Set<String>> dataReferencesUc;

	private final Set<String> dataCodes;

	private final List<Object> outputs;
	private final List<Object> outputsSinglePage;

	private final Map<String, UseCaseOut> useCaseOuts;
	private final Map<String, ActorOut> actorOuts;
	private final Map<String, DataOut> dataOuts;
	private final Map<String, InstanceOut> instanceOuts;
	private final Set<TermOut> termOuts;

	private final Set<UseCaseOut> entryPoints;
	private final Set<DataOut> unusedData;
	private final Set<InstanceOut> unusedInstances;
	private final Set<DataOut> keyData;

	private UseCase currentUseCase;
	private boolean single = false;

	public ExecutionContext(ResourceBundle labels, Project project) {
		this.labels = labels;
		this.project = project;
		actors = new LinkedHashMap<String, Actor>();
		datas = new LinkedHashMap<String, Data>();
		instances = new LinkedHashMap<String, Instance>();
		terms = new LinkedHashSet<Term>();
		useCases = new LinkedHashMap<String, UseCase>();
		ucGroups = new LinkedHashMap<String, UcGroup>();
		ucReferences = new LinkedHashMap<String, Set<String>>();
		dataReferencesData = new LinkedHashMap<String, Set<String>>();
		dataInstances = new LinkedHashMap<String, Set<String>>();
		dataReferencesUc = new LinkedHashMap<String, Set<String>>();

		dataCodes = new HashSet<String>();

		outputs = new ArrayList<Object>();
		outputsSinglePage = new ArrayList<Object>();

		useCaseOuts = new LinkedHashMap<String, UseCaseOut>();
		actorOuts = new LinkedHashMap<String, ActorOut>();
		dataOuts = new LinkedHashMap<String, DataOut>();
		instanceOuts = new LinkedHashMap<String, InstanceOut>();
		termOuts = new LinkedHashSet<TermOut>();

		entryPoints = new LinkedHashSet<UseCaseOut>();
		unusedData = new LinkedHashSet<DataOut>();
		unusedInstances = new LinkedHashSet<InstanceOut>();
		keyData = new LinkedHashSet<DataOut>();
	}

	public String label(String key) {
		return labels.getString(key);
	}

	public Project getProject() {
		return project;
	}

	public Map<String, Actor> getActors() {
		return actors;
	}

	public Map<String, Data> getDatas() {
		return datas;
	}

	public Map<String, Instance> getInstances() {
		return instances;
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

	public Map<String, Set<String>> getDataReferencesData() {
		return dataReferencesData;
	}

	public Map<String, Set<String>> getDataInstances() {
		return dataInstances;
	}

	public Map<String, Set<String>> getDataReferencesUc() {
		return dataReferencesUc;
	}

	public Set<String> getDataCodes() {
		return dataCodes;
	}

	public List<Object> getOutputs() {
		return outputs;
	}

	public List<Object> getOutputsSinglePage() {
		return outputsSinglePage;
	}

	public Map<String, UseCaseOut> getUseCaseOuts() {
		return useCaseOuts;
	}

	public Map<String, ActorOut> getActorOuts() {
		return actorOuts;
	}

	public Map<String, DataOut> getDataOuts() {
		return dataOuts;
	}

	public Map<String, InstanceOut> getInstanceOuts() {
		return instanceOuts;
	}

	public Set<TermOut> getTermOuts() {
		return termOuts;
	}

	public Set<UseCaseOut> getEntryPoints() {
		return entryPoints;
	}

	public Set<DataOut> getUnusedData() {
		return unusedData;
	}

	public Set<InstanceOut> getUnusedInstances() {
		return unusedInstances;
	}

	public Set<DataOut> getKeyData() {
		return keyData;
	}

	public void addUcRef(String refcodeReferenced, String refcodeReferencing) {
		Set<String> references = ucReferences.get(refcodeReferenced);
		if (null == references) {
			references = new LinkedHashSet<String>();
			ucReferences.put(refcodeReferenced, references);
		}
		references.add(refcodeReferencing);
	}

	public void addDataRef(String refcodeReferenced, String refcodeReferencing,
			String referencingType) {
		Map<String, Set<String>> refs;
		if ("data".equals(referencingType)
				|| "instance".equals(referencingType)
				|| "attribute".equals(referencingType)
				|| "value".equals(referencingType)) {
			refs = dataReferencesData;
		} else if ("instance of".equals(referencingType)) {
			refs = dataInstances;
		} else if ("use case".equals(referencingType)) {
			refs = dataReferencesUc;
		} else {
			return;
		}
		Set<String> references = refs.get(refcodeReferenced);
		if (null == references) {
			references = new LinkedHashSet<String>();
			refs.put(refcodeReferenced, references);
		}
		references.add(refcodeReferencing);
	}

	public boolean isSingle() {
		return single;
	}

	public void setSingle(boolean single) {
		this.single = single;
	}

}
