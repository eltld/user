package net.ememed.user2.entity;

import java.io.Serializable;

public class RoomEntry implements Serializable{

	private String ROOMID;
	private String ROOMNAME;
	private String HAS_SCHEDULE;
	private String sortLetters;  //显示数据拼音的首字母
	private String TREATMENT_COUNT;	//就诊量
	
	public String getTreatmentCount() {
		return TREATMENT_COUNT;
	}

	public void setTreatmentCount(String treatmentCount) {
		this.TREATMENT_COUNT = treatmentCount;
	}

	public String getROOMID() {
		return ROOMID;
	}

	public void setROOMID(String rOOMID) {
		ROOMID = rOOMID;
	}

	public String getROOMNAME() {
		return ROOMNAME;
	}

	public void setROOMNAME(String rOOMNAME) {
		ROOMNAME = rOOMNAME;
	}

	public String getHAS_SCHEDULE() {
		return HAS_SCHEDULE;
	}

	public void setHAS_SCHEDULE(String hAS_SCHEDULE) {
		HAS_SCHEDULE = hAS_SCHEDULE;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

}
