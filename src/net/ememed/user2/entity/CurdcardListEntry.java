package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

public class CurdcardListEntry implements Serializable{

	private String ID;
	private String CURDCARDID;
	private String HOSPITAL;
	private String MEMBERID;
	
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

}
