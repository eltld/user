package net.ememed.user2.entity;

import java.io.Serializable;

public class CurdcardListItem implements Serializable{

	private String ID;
	private String CURDCARDID;
	private String HOSPITAL;
	private String MEMBERID;
	private String CREATETIME;
	private String UPDATETIME;
	private String CHANNEL;
	private String CUREDMEMBERID;
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getCURDCARDID() {
		return CURDCARDID;
	}
	public void setCURDCARDID(String cURDCARDID) {
		CURDCARDID = cURDCARDID;
	}
	public String getHOSPITAL() {
		return HOSPITAL;
	}
	public void setHOSPITAL(String hOSPITAL) {
		HOSPITAL = hOSPITAL;
	}
	public String getMEMBERID() {
		return MEMBERID;
	}
	public void setMEMBERID(String mEMBERID) {
		MEMBERID = mEMBERID;
	}
	public String getCREATETIME() {
		return CREATETIME;
	}
	public void setCREATETIME(String cREATETIME) {
		CREATETIME = cREATETIME;
	}
	public String getUPDATETIME() {
		return UPDATETIME;
	}
	public void setUPDATETIME(String uPDATETIME) {
		UPDATETIME = uPDATETIME;
	}
	public String getCHANNEL() {
		return CHANNEL;
	}
	public void setCHANNEL(String cHANNEL) {
		CHANNEL = cHANNEL;
	}
	public String getCUREDMEMBERID() {
		return CUREDMEMBERID;
	}
	public void setCUREDMEMBERID(String cUREDMEMBERID) {
		CUREDMEMBERID = cUREDMEMBERID;
	}
	
	
}
