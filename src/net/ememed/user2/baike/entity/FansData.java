package net.ememed.user2.baike.entity;

import java.io.Serializable;

import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.UserInfo;
import net.ememed.user2.entity.UserInfoEn;

public class FansData implements Serializable{
	 private String ATTENTIONID;  // 1113
     private String MEMBERID;  // 103678
     private String UTYPE;  // doctor
     private String OTHER_MEMBERID;  // 104038
     private String OTHER_UTYPE;  // doctor
     private String READ_STATUS;  // 1
     private String CREATE_TIME;  // 2015-03-16 14:12:14
     private String UPDATE_TIME;  // 2015-03-16 14:42:54
     private String HAVE_NEW_MSG;  // 0
     private String RNUM;  // 1
     private String AVATAR;  // http://www.ememed.net/uploads/avatar/201412/avatar_103678_ba576dcce549d595adc4767f9ffa5e1a.jpg
     private String REALNAME;  // 崔展铭(薏米测试)
     private String MEMBERNAME;  // emuRrB3aE9
     private String PROFESSIONAL;  // 主任
     private String HOSPITALNAME;  // 薏米医院
     private String HAVEORDER;  // 
     private String MSG_ID;  // 
     private String TYPE;  // 
     private String CONTENT;  // 

     private String SENDTIME;  // 
     private String ISSYSTEMMSG;  // 
 //    private String CONTENT_EXT;  // 
     private String IS_ATTENTION;  // 0
             
     private DoctorEntry DOCTORINFO;
     private FansUserInfo USERINFO;
     
	public String getATTENTIONID() {
		return ATTENTIONID;
	}
	public void setATTENTIONID(String aTTENTIONID) {
		ATTENTIONID = aTTENTIONID;
	}
	public String getMEMBERID() {
		return MEMBERID;
	}
	public void setMEMBERID(String mEMBERID) {
		MEMBERID = mEMBERID;
	}
	public String getUTYPE() {
		return UTYPE;
	}
	public void setUTYPE(String uTYPE) {
		UTYPE = uTYPE;
	}
	public String getOTHER_MEMBERID() {
		return OTHER_MEMBERID;
	}
	public void setOTHER_MEMBERID(String oTHER_MEMBERID) {
		OTHER_MEMBERID = oTHER_MEMBERID;
	}
	public String getOTHER_UTYPE() {
		return OTHER_UTYPE;
	}
	public void setOTHER_UTYPE(String oTHER_UTYPE) {
		OTHER_UTYPE = oTHER_UTYPE;
	}
	public String getREAD_STATUS() {
		return READ_STATUS;
	}
	public void setREAD_STATUS(String rEAD_STATUS) {
		READ_STATUS = rEAD_STATUS;
	}
	public String getCREATE_TIME() {
		return CREATE_TIME;
	}
	public void setCREATE_TIME(String cREATE_TIME) {
		CREATE_TIME = cREATE_TIME;
	}
	public String getUPDATE_TIME() {
		return UPDATE_TIME;
	}
	public void setUPDATE_TIME(String uPDATE_TIME) {
		UPDATE_TIME = uPDATE_TIME;
	}
	public String getHAVE_NEW_MSG() {
		return HAVE_NEW_MSG;
	}
	public void setHAVE_NEW_MSG(String hAVE_NEW_MSG) {
		HAVE_NEW_MSG = hAVE_NEW_MSG;
	}
	public String getRNUM() {
		return RNUM;
	}
	public void setRNUM(String rNUM) {
		RNUM = rNUM;
	}
	public String getAVATAR() {
		return AVATAR;
	}
	public void setAVATAR(String aVATAR) {
		AVATAR = aVATAR;
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
	public String getSENDTIME() {
		return SENDTIME;
	}
	public void setSENDTIME(String sENDTIME) {
		SENDTIME = sENDTIME;
	}
	public String getISSYSTEMMSG() {
		return ISSYSTEMMSG;
	}
	public void setISSYSTEMMSG(String iSSYSTEMMSG) {
		ISSYSTEMMSG = iSSYSTEMMSG;
	}
	/*public String getCONTENT_EXT() {
		return CONTENT_EXT;
	}
	public void setCONTENT_EXT(String cONTENT_EXT) {
		CONTENT_EXT = cONTENT_EXT;
	}*/
	public String getIS_ATTENTION() {
		return IS_ATTENTION;
	}
	public void setIS_ATTENTION(String iS_ATTENTION) {
		IS_ATTENTION = iS_ATTENTION;
	}
	public DoctorEntry getDOCTORINFO() {
		return DOCTORINFO;
	}
	public void setDOCTORINFO(DoctorEntry dOCTORINFO) {
		DOCTORINFO = dOCTORINFO;
	}
	public FansUserInfo getUSERINFO() {
		return USERINFO;
	}
	public void setUSERINFO(FansUserInfo uSERINFO) {
		USERINFO = uSERINFO;
	}
     
     
            	
}
