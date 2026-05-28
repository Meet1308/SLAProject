package com.sla.dto;

public class ClauseRequestDTO {

	private Integer action_type;
	private String documenttype;
	private Integer documentid;
	private String applicationid;
	private String add_clause_no;
	private String add_clause_refno;
	private String add_clause_name;
	private String add_clause_start_date;
	private String add_clause_end_date;
	private String add_clause_description;

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

	public Integer getDocumentid() {
		return documentid;
	}

	public void setDocumentid(Integer documentid) {
		this.documentid = documentid;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	public String getAdd_clause_no() {
		return add_clause_no;
	}

	public void setAdd_clause_no(String add_clause_no) {
		this.add_clause_no = add_clause_no;
	}

	public String getAdd_clause_refno() {
		return add_clause_refno;
	}

	public void setAdd_clause_refno(String add_clause_refno) {
		this.add_clause_refno = add_clause_refno;
	}

	public String getAdd_clause_name() {
		return add_clause_name;
	}

	public void setAdd_clause_name(String add_clause_name) {
		this.add_clause_name = add_clause_name;
	}

	public String getAdd_clause_start_date() {
		return add_clause_start_date;
	}

	public void setAdd_clause_start_date(String add_clause_start_date) {
		this.add_clause_start_date = add_clause_start_date;
	}

	public String getAdd_clause_end_date() {
		return add_clause_end_date;
	}

	public void setAdd_clause_end_date(String add_clause_end_date) {
		this.add_clause_end_date = add_clause_end_date;
	}

	public String getAdd_clause_description() {
		return add_clause_description;
	}

	public void setAdd_clause_description(String add_clause_description) {
		this.add_clause_description = add_clause_description;
	}

	@Override
	public String toString() {
		return "ClauseRequestDTO [action_type=" + action_type + ", documenttype=" + documenttype + ", documentid="
				+ documentid + ", applicationid=" + applicationid + ", add_clause_no=" + add_clause_no
				+ ", add_clause_refno=" + add_clause_refno + ", add_clause_name=" + add_clause_name
				+ ", add_clause_start_date=" + add_clause_start_date + ", add_clause_end_date=" + add_clause_end_date
				+ ", add_clause_description=" + add_clause_description + "]";
	}

}
