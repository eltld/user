package net.ememed.user2.baike.entity;

import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.MemberInfoEntity;

public class DoctorAttentionInfo {
	private String ATTENTIONID;  // 179
    private String MEMBERID;  // 97928
    private String UTYPE;  // doctor
    private String OTHER_MEMBERID;  // 7538
    private String OTHER_UTYPE;  // doctor
    private String READ_STATUS;  // 1
    private String CREATE_TIME;  // 2015-02-09 14:20:36
    private String UPDATE_TIME;  // 2015-03-16 14:43:26
    private String HAVE_NEW_MSG;  // 0
    private String RNUM;  // 1
    private String IS_ATTENTION;  // 1
    
    private DoctorEntry DOCTORINFO;
    
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
}
