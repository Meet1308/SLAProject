package com.sla.dto;

public class SlaDetailDTO {

	private String level;
	private String role;
	private String notify;
	private String resolutionType;
	private String resolutionTime;

	public SlaDetailDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getResolutionType() {
		return resolutionType;
	}

	public void setResolutionType(String resolutionType) {
		this.resolutionType = resolutionType;
	}

	public String getResolutionTime() {
		return resolutionTime;
	}

	public void setResolutionTime(String resolutionTime) {
		this.resolutionTime = resolutionTime;
	}
	
	
	public String getNotify() {
		return notify;
	}

	public void setNotify(String notify) {
		this.notify = notify;
	}

	@Override
	public String toString() {
		return "SlaDetailDTO [level=" + level + ", role=" + role + ", notify=" + notify + ", resolutionType="
				+ resolutionType + ", resolutionTime=" + resolutionTime + "]";
	}

	

}