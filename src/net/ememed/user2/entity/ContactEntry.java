package net.ememed.user2.entity;

import java.io.Serializable;

import org.apache.http.entity.SerializableEntity;

public class ContactEntry implements Serializable{
	
	private String MEMBERID;
	private String HAVEORDER;
	private String MSG_ID;
	private String TYPE;
	private String CONTENT;
//	private ExtItem EXT;
	private String SENDTIME;
	private String ISSYSTEMMSG;
//	
//	private MessageContentEntry CONTENT_EXT;
	private String AVATAR;
	private String UTYPE;
	private String PROFESSIONAL;
	private String HOSPITALNAME;
	private String REALNAME;
	private String MEMBERNAME;
    private DoctorFansInfo DOCTOR_FANS;
   
	
	public DoctorFansInfo getDOCTOR_FANS() {
		return DOCTOR_FANS;
	}
	public void setDOCTOR_FANS(DoctorFansInfo dOCTOR_FANS) {
		DOCTOR_FANS = dOCTOR_FANS;
	}
	public String getMEMBERID() {
		return MEMBERID;
	}
	public void setMEMBERID(String mEMBERID) {
		MEMBERID = mEMBERID;
	}
	public String getHAVEORDER() {
		return HAVEORDER;
	}
	public void setHAVEORDER(String hAVEORDER) {
		HAVEORDER = hAVEORDER;
	}
	public String getMSG_ID() {
		return MSG_ID;
	}
	public void setMSG_ID(String mSG_ID) {
		MSG_ID = mSG_ID;
	}
	public String getTYPE() {
		return TYPE;
	}
	public void setTYPE(String tYPE) {
		TYPE = tYPE;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
//	public ExtItem getEXT() {
//		return EXT;
//	}
//	public void setEXT(ExtItem eXT) {
//		EXT = eXT;
//	}
	public String getSENDTIME() {
		return SENDTIME;
	}
	public String getISSYSTEMMSG() {
		return ISSYSTEMMSG;
	}
	public void setISSYSTEMMSG(String iSSYSTEMMSG) {
		ISSYSTEMMSG = iSSYSTEMMSG;
	}
	public void setSENDTIME(String sENDTIME) {
		SENDTIME = sENDTIME;
	}
//	public ContentExtItem getCONTENT_EXT() {
//		return CONTENT_EXT;
//	}
//	public void setCONTENT_EXT(ContentExtItem cONTENT_EXT) {
//		CONTENT_EXT = cONTENT_EXT;
//	}
	public String getAVATAR() {
		return AVATAR;
	}
	public void setAVATAR(String aVATAR) {
		AVATAR = aVATAR;
	}
	public String getUTYPE() {
		return UTYPE;
	}
	public void setUTYPE(String uTYPE) {
		UTYPE = uTYPE;
	}
	public String getPROFESSIONAL() {
		return PROFESSIONAL;
	}
	public void setPROFESSIONAL(String pROFESSIONAL) {
		PROFESSIONAL = pROFESSIONAL;
	}
	public String getHOSPITALNAME() {
		return HOSPITALNAME;
	}
	public void setHOSPITALNAME(String hOSPITALNAME) {
		HOSPITALNAME = hOSPITALNAME;
	}
	public String getREALNAME() {
		return REALNAME;
	}
	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
	}
	public String getMEMBERNAME() {
		return MEMBERNAME;
	}
	public void setMEMBERNAME(String mEMBERNAME) {
		MEMBERNAME = mEMBERNAME;
	}
//	public ExtItem getEXT() {
//		return EXT;
//	}
//	public void setEXT(ExtItem eXT) {
//		EXT = eXT;
//	}
//	public MessageContentEntry getCONTENT_EXT() {
//		return CONTENT_EXT;
//	}
//	public void setCONTENT_EXT(MessageContentEntry cONTENT_EXT) {
//		CONTENT_EXT = cONTENT_EXT;
//	}
}
