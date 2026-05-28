package com.sla.dto;

import java.util.List;

public class SLARequestDTO {

	private String action_type;
	private String applicationid;
	private String documenttype;
	private String vendrmngt_slaname;
	private String vendrmngt_sla_descb;
	private String documentid;
	private String sruserid;

	private List<SlaDetailDTO> strdata;

	public SLARequestDTO() {
		super();
	}

	public String getAction_type() {
		return action_type;
	}

	public void setAction_type(String action_type) {
		this.action_type = action_type;
	}

	public String getApplicationid() {
		return applicationid;
	}

	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}

	public String getDocumenttype() {
		return documenttype;
	}

	public void setDocumenttype(String documenttype) {
		this.documenttype = documenttype;
	}

	public String getVendrmngt_slaname() {
		return vendrmngt_slaname;
	}

	public void setVendrmngt_slaname(String vendrmngt_slaname) {
		this.vendrmngt_slaname = vendrmngt_slaname;
	}

	public String getVendrmngt_sla_descb() {
		return vendrmngt_sla_descb;
	}

	public void setVendrmngt_sla_descb(String vendrmngt_sla_descb) {
		this.vendrmngt_sla_descb = vendrmngt_sla_descb;
	}

	public String getDocumentid() {
		return documentid;
	}

	public void setDocumentid(String documentid) {
		this.documentid = documentid;
	}

	public String getSruserid() {
		return sruserid;
	}

	public void setSruserid(String sruserid) {
		this.sruserid = sruserid;
	}

	public List<SlaDetailDTO> getStrdata() {
		return strdata;
	}

	public void setStrdata(List<SlaDetailDTO> strdata) {
		this.strdata = strdata;
	}

	@Override
	public String toString() {
		return "SLARequestDTO [action_type=" + action_type + ", applicationid=" + applicationid + ", documenttype="
				+ documenttype + ", vendrmngt_slaname=" + vendrmngt_slaname + ", vendrmngt_sla_descb="
				+ vendrmngt_sla_descb + ", documentid=" + documentid + ", sruserid=" + sruserid + ", strdata=" + strdata
				+ "]";
	}

}