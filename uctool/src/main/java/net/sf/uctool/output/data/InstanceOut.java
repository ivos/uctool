package net.sf.uctool.output.data;

import java.util.ArrayList;
import java.util.List;

import net.sf.uctool.output.Reference;

public class InstanceOut {

	private String code;
	private String refcode;
	private String name;
	private DataOut of;
	private final List<String> descriptions = new ArrayList<String>();
	private final List<ValueOut> values = new ArrayList<ValueOut>();
	private final List<Reference> referencesData = new ArrayList<Reference>();
	private final List<Reference> referencesUcs = new ArrayList<Reference>();
	private final List<AttributeOut> unmapped = new ArrayList<AttributeOut>();

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataOut getOf() {
		return of;
	}

	public void setOf(DataOut of) {
		this.of = of;
	}

	public Reference getOfRef() {
		return new Reference("data", of.getCode(), of.getName(), false);
	}

	public List<String> getDescriptions() {
		return descriptions;
	}

	public List<ValueOut> getValues() {
		return values;
	}

	public List<Reference> getReferencesData() {
		return referencesData;
	}

	public List<Reference> getReferencesUcs() {
		return referencesUcs;
	}

	public List<AttributeOut> getUnmapped() {
		return unmapped;
	}

	@Override
	public String toString() {
		return "InstanceOut [code=" + code + ", refcode=" + refcode + ", name="
				+ name + "]";
	}

}
