package com.hjw.webService.client.dashiqiao.ResCusBean;

import java.util.ArrayList;
import java.util.List;

public class Resource {

	
	private String resourceType;//
	private String id;//
	private Meta meta = new Meta();//
	private Text text = new Text();

	private List<Identifier> identifier = new ArrayList<>();//
	private List<Extension> extension = new ArrayList<>();
	private String active;
	private List<Name> name = new ArrayList<>();
	private List<Telecom> telecom = new ArrayList<>();

	private String gender;
	private String birthDate;
	private List<Address> address = new ArrayList<>();
	private String status;
	private Code code=  new Code();
	private Subject subject=  new Subject();
	private List<Account> account = new ArrayList<>();
	private List<Participant> participant = new ArrayList<>();
	private String  created;
	private String end;
	private String start;
	
	
	public List<Participant> getParticipant() {
		return participant;
	}
	public void setParticipant(List<Participant> participant) {
		this.participant = participant;
	}
	public String getCreated() {
		return created;
	}
	public void setCreated(String created) {
		this.created = created;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
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
	public List<Identifier> getIdentifier() {
		return identifier;
	}
	public void setIdentifier(List<Identifier> identifier) {
		this.identifier = identifier;
	}
	public List<Extension> getExtension() {
		return extension;
	}
	public void setExtension(List<Extension> extension) {
		this.extension = extension;
	}
	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	public List<Name> getName() {
		return name;
	}
	public void setName(List<Name> name) {
		this.name = name;
	}
	public List<Telecom> getTelecom() {
		return telecom;
	}
	public void setTelecom(List<Telecom> telecom) {
		this.telecom = telecom;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthDate() {
		return birthDate;
	}
	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}
	public List<Address> getAddress() {
		return address;
	}
	public void setAddress(List<Address> address) {
		this.address = address;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Code getCode() {
		return code;
	}
	public void setCode(Code code) {
		this.code = code;
	}
	public Subject getSubject() {
		return subject;
	}
	public void setSubject(Subject subject) {
		this.subject = subject;
	}
	public List<Account> getAccount() {
		return account;
	}
	public void setAccount(List<Account> account) {
		this.account = account;
	}


	
}
