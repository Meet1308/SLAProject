package com.sla.dto;

public class ContractRequestDTO {

	private Integer action_type;
	private String documenttype;
	private String applicationid;
	private String add_contract_name;
	private String add_contract_vender_text;
	private String add_contract_venderid;
	private String add_contract_start_date;
	private String add_contract_end_date;
	private String add_contract_description;
	private String add_contract_notify;
	private String add_contract_days_id;
	private String add_contract_days_text;
	private String attachid;

	public Integer getAction_type() {
		return action_type;
	}

	public void setAction_type(Integer action_type) {
		this.action_type = action_type;
	}

	public String getDocumenttype() {
		return documenttype;
	}

	public void setDocumenttype(String documenttype) {
		this.documenttype = documenttype;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	public String getAdd_contract_name() {
		return add_contract_name;
	}

	public void setAdd_contract_name(String add_contract_name) {
		this.add_contract_name = add_contract_name;
	}

	public String getAdd_contract_vender_text() {
		return add_contract_vender_text;
	}

	public void setAdd_contract_vender_text(String add_contract_vender_text) {
		this.add_contract_vender_text = add_contract_vender_text;
	}

	public String getAdd_contract_venderid() {
		return add_contract_venderid;
	}

	public void setAdd_contract_venderid(String add_contract_venderid) {
		this.add_contract_venderid = add_contract_venderid;
	}

	public String getAdd_contract_start_date() {
		return add_contract_start_date;
	}

	public void setAdd_contract_start_date(String add_contract_start_date) {
		this.add_contract_start_date = add_contract_start_date;
	}

	public String getAdd_contract_end_date() {
		return add_contract_end_date;
	}

	public void setAdd_contract_end_date(String add_contract_end_date) {
		this.add_contract_end_date = add_contract_end_date;
	}

	public String getAdd_contract_description() {
		return add_contract_description;
	}

	public void setAdd_contract_description(String add_contract_description) {
		this.add_contract_description = add_contract_description;
	}

	public String getAdd_contract_notify() {
		return add_contract_notify;
	}

	public void setAdd_contract_notify(String add_contract_notify) {
		this.add_contract_notify = add_contract_notify;
	}

	public String getAdd_contract_days_id() {
		return add_contract_days_id;
	}

	public void setAdd_contract_days_id(String add_contract_days_id) {
		this.add_contract_days_id = add_contract_days_id;
	}

	public String getAdd_contract_days_text() {
		return add_contract_days_text;
	}

	public void setAdd_contract_days_text(String add_contract_days_text) {
		this.add_contract_days_text = add_contract_days_text;
	}

	public String getAttachid() {
		return attachid;
	}

	public void setAttachid(String attachid) {
		this.attachid = attachid;
	}

	@Override
	public String toString() {
		return "ContractRequestDTO [action_type=" + action_type + ", documenttype=" + documenttype + ", applicationid="
				+ applicationid + ", add_contract_name=" + add_contract_name + ", add_contract_vender_text="
				+ add_contract_vender_text + ", add_contract_venderid=" + add_contract_venderid
				+ ", add_contract_start_date=" + add_contract_start_date + ", add_contract_end_date="
				+ add_contract_end_date + ", add_contract_description=" + add_contract_description
				+ ", add_contract_notify=" + add_contract_notify + ", add_contract_days_id=" + add_contract_days_id
				+ ", add_contract_days_text=" + add_contract_days_text + ", attachid=" + attachid + "]";
	}

}
