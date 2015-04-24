package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

import android.text.TextUtils;

public class CuredmemeberListEntry implements Serializable {
	private String ID;
	private String REALNAME;
	private String MEMBERID;
	private String SEX;
	private String BIRTHDAY;
	private String MOBILE;
	private String ADDRESS;
	private String CARDID;
	private String CREATETIME;
	private String UPDATETIME;
	private List<CurdcardListEntry> curdcard_list;

	public List<CurdcardListEntry> getCurdcard_list() {
		return curdcard_list;
	}

	public void setCurdcard_list(List<CurdcardListEntry> curdcard_list) {
		this.curdcard_list = curdcard_list;
	}

	public String getID() {
		return ID;
	}

	public void setID(String iD) {
		ID = iD;
	}

	public String getREALNAME() {
		return REALNAME;
	}

	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
	}

	public String getMEMBERID() {
		return MEMBERID;
	}

	public void setMEMBERID(String mEMBERID) {
		MEMBERID = mEMBERID;
	}

	public String getSEX() {
		return SEX;
	}

	public void setSEX(String sEX) {
		SEX = sEX;
	}

	public String getBIRTHDAY() {
		return BIRTHDAY;
	}

	public void setBIRTHDAY(String bIRTHDAY) {
		BIRTHDAY = bIRTHDAY;
	}

	public String getMOBILE() {
		return MOBILE;
	}

	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getCARDID() {
		return CARDID;
	}

	public void setCARDID(String CARDID) {
		CARDID = CARDID;
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

	public String getSex() {
		String str = "";
		if (TextUtils.isEmpty(SEX))
			str = "";
		else if (SEX.equals("1")) {
			str = "男";
		} else {
			str = "女";
		}
		return str;
	}

}
