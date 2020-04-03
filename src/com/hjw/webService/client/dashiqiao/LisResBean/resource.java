package com.hjw.webService.client.dashiqiao.LisResBean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.dashiqiao.ResCusBean.Extension;
import com.hjw.webService.client.dashiqiao.ResCusBean.Meta;
import com.hjw.webService.client.dashiqiao.ResCusBean.Text;

public class resource {

	private String resourceType;
	private String id;
	private List<identifier> identifier = new ArrayList<identifier>();
	private String status;
	private List<category> category = new ArrayList<category>();
	private code code = new code();
	private subject subject = new subject();
	private encounter encounter = new encounter();
	private String effectiveDateTime;
	private String issued;
	private List<result> result = new ArrayList<result>();
	private valueQuantity valueQuantity = new valueQuantity();
	private List<referenceRange> referenceRange = new ArrayList<referenceRange>();
	private List<Extension> extension = new ArrayList<Extension>();
	private List<performer> performer = new ArrayList<performer>();
	private Meta meta = new Meta();
	private Text text = new Text();
	private String valueString;
	
	
	

	public String getValueString() {
		return valueString;
	}

	public void setValueString(String valueString) {
		this.valueString = valueString;
	}

	public Meta getMeta() {
		return meta;
	}

	public void setMeta(Meta meta) {
		this.meta = meta;
	}

	public Text getText() {
		return text;
	}

	public void setText(Text text) {
		this.text = text;
	}

	public List<performer> getPerformer() {
		return performer;
	}

	public void setPerformer(List<performer> performer) {
		this.performer = performer;
	}

	public List<Extension> getExtension() {
		return extension;
	}

	public void setExtension(List<Extension> extension) {
		this.extension = extension;
	}

	public String getResourceType() {
		return resourceType;
	}

	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<identifier> getIdentifier() {
		return identifier;
	}

	public void setIdentifier(List<identifier> identifier) {
		this.identifier = identifier;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<category> getCategory() {
		return category;
	}

	public void setCategory(List<category> category) {
		this.category = category;
	}

	public code getCode() {
		return code;
	}

	public void setCode(code code) {
		this.code = code;
	}

	public subject getSubject() {
		return subject;
	}

	public void setSubject(subject subject) {
		this.subject = subject;
	}

	public encounter getEncounter() {
		return encounter;
	}

	public void setEncounter(encounter encounter) {
		this.encounter = encounter;
	}

	public String getEffectiveDateTime() {
		return effectiveDateTime;
	}

	public void setEffectiveDateTime(String effectiveDateTime) {
		this.effectiveDateTime = effectiveDateTime;
	}

	public String getIssued() {
		return issued;
	}

	public void setIssued(String issued) {
		this.issued = issued;
	}

	public List<result> getResult() {
		return result;
	}

	public void setResult(List<result> result) {
		this.result = result;
	}

	public valueQuantity getValueQuantity() {
		return valueQuantity;
	}

	public void setValueQuantity(valueQuantity valueQuantity) {
		this.valueQuantity = valueQuantity;
	}

	public List<referenceRange> getReferenceRange() {
		return referenceRange;
	}

	public void setReferenceRange(List<referenceRange> referenceRange) {
		this.referenceRange = referenceRange;
	}

}
