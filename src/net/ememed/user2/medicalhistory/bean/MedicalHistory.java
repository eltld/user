package net.ememed.user2.medicalhistory.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MedicalHistory implements Serializable  {
	private String ID;
	private String USERID;
	private String NAME;
	private String TIME;
	private String MECHANISM;
	private String CONTENT;	
	private String ATTACHMENT;
	private String UPDATETIME;
	private String DESCRIPTION;
	private List<ImageUrlBean> ATTACHMENTS;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getNAME() {
		return NAME;
	}
	public void setNAME(String nAME) {
		NAME = nAME;
	}
	public String getTIME() {
		return TIME;
	}
	public void setTIME(String tIME) {
		TIME = tIME;
	}
	public String getMECHANISM() {
		return MECHANISM;
	}
	public void setMECHANISM(String mECHANISM) {
		MECHANISM = mECHANISM;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public String getATTACHMENT() {
		return ATTACHMENT;
	}
	public void setATTACHMENT(String aTTACHMENT) {
		ATTACHMENT = aTTACHMENT;
	}
	public String getUPDATETIME() {
		return UPDATETIME;
	}
	public void setUPDATETIME(String uPDATETIME) {
		UPDATETIME = uPDATETIME;
	}
	public String getDESCRIPTION() {
		return DESCRIPTION;
	}
	public void setDESCRIPTION(String dESCRIPTION) {
		DESCRIPTION = dESCRIPTION;
	}
	public List<ImageUrlBean> getATTACHMENTS() {
		return ATTACHMENTS;
	}
	public void setATTACHMENTS(List<ImageUrlBean> aTTACHMENTS) {
		ATTACHMENTS = aTTACHMENTS;
	}
	
	
}
