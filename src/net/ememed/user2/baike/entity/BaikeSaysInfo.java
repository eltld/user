package net.ememed.user2.baike.entity;

import java.io.Serializable;
import java.util.List;

public class BaikeSaysInfo implements Serializable{
	 private String SAYSID;  // private String 101private String ,
     private String TITLE;  // null,
     private String CONTENT;  // private String 我的说说private String ,
     private String CONTENT_SHOW;
     
     private String DOCTORID;  // private String 107936private String ,
     private String HITS;  // private String 45private String ,
     private String PRAISE_NUM;  // private String 16private String ,
     private String COMMENT_NUM;  // private String 0private String ,
     private String SHARE_COUNT;  // private String 0private String ,
     private String STATUS;  // private String 1private String ,
     private String CREATE_TIME;  // private String 2015-02-04 18:01:17private String ,
     private String UPDATE_TIME;  // private String 2015-02-04 18:01:17private String ,
     private String RNUM;  // private String 1private String ,
     private boolean IS_PRAISED;  // false,
     private String REALNAME;
     private String AVATAR;
     private String PROFESSIONAL;
     private String IS_NEW;  // 1,
     private String SAYS_UNREAD;  // 1,
     private String COMMENT_UNREAD;  // 0,
     private int UNREAD_COMMENT_COUNT;  // 0
     private String TOTAL_BOUNTY_NUM;
     
     private List<String> PICS;  // 
     private List<String> PICS_THUMB;  // 
     
     private String SHARE_URL;
     private String DETAIL_URL;
     private String SHARE_CONTENT;
     
     private SaysCommentDetai COMMENT_LIST;
     
     
     
	public String getSHARE_CONTENT() {
		return SHARE_CONTENT;
	}
	public void setSHARE_CONTENT(String sHARE_CONTENT) {
		SHARE_CONTENT = sHARE_CONTENT;
	}
	public String getSHARE_COUNT() {
		return SHARE_COUNT;
	}
	public void setSHARE_COUNT(String sHARE_COUNT) {
		SHARE_COUNT = sHARE_COUNT;
	}
	public String getCONTENT_SHOW() {
		return CONTENT_SHOW;
	}
	public void setCONTENT_SHOW(String cONTENT_SHOW) {
		CONTENT_SHOW = cONTENT_SHOW;
	}
	public String getTOTAL_BOUNTY_NUM() {
		return TOTAL_BOUNTY_NUM;
	}
	public void setTOTAL_BOUNTY_NUM(String tOTAL_BOUNTY_NUM) {
		TOTAL_BOUNTY_NUM = tOTAL_BOUNTY_NUM;
	}
	public String getPROFESSIONAL() {
		return PROFESSIONAL;
	}
	public void setPROFESSIONAL(String pROFESSIONAL) {
		PROFESSIONAL = pROFESSIONAL;
	}
	public String getREALNAME() {
		return REALNAME;
	}
	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
	}
	public String getAVATAR() {
		return AVATAR;
	}
	public void setAVATAR(String aVATAR) {
		AVATAR = aVATAR;
	}
	public String getSHARE_URL() {
		return SHARE_URL;
	}
	public void setSHARE_URL(String sHARE_URL) {
		SHARE_URL = sHARE_URL;
	}
	public String getDETAIL_URL() {
		return DETAIL_URL;
	}
	public void setDETAIL_URL(String dETAIL_URL) {
		DETAIL_URL = dETAIL_URL;
	}
	public SaysCommentDetai getCOMMENT_LIST() {
		return COMMENT_LIST;
	}
	public void setCOMMENT_LIST(SaysCommentDetai cOMMENT_LIST) {
		COMMENT_LIST = cOMMENT_LIST;
	}
	public String getSAYSID() {
		return SAYSID;
	}
	public void setSAYSID(String sAYSID) {
		SAYSID = sAYSID;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public String getDOCTORID() {
		return DOCTORID;
	}
	public void setDOCTORID(String dOCTORID) {
		DOCTORID = dOCTORID;
	}
	public String getHITS() {
		return HITS;
	}
	public void setHITS(String hITS) {
		HITS = hITS;
	}
	public String getPRAISE_NUM() {
		return PRAISE_NUM;
	}
	public void setPRAISE_NUM(String pRAISE_NUM) {
		PRAISE_NUM = pRAISE_NUM;
	}
	public String getCOMMENT_NUM() {
		return COMMENT_NUM;
	}
	public void setCOMMENT_NUM(String cOMMENT_NUM) {
		COMMENT_NUM = cOMMENT_NUM;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
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
	public String getRNUM() {
		return RNUM;
	}
	public void setRNUM(String rNUM) {
		RNUM = rNUM;
	}
	public boolean getIS_PRAISED() {
		return IS_PRAISED;
	}
	public void setIS_PRAISED(boolean iS_PRAISED) {
		IS_PRAISED = iS_PRAISED;
	}
	public String getIS_NEW() {
		return IS_NEW;
	}
	public void setIS_NEW(String iS_NEW) {
		IS_NEW = iS_NEW;
	}
	public String getSAYS_UNREAD() {
		return SAYS_UNREAD;
	}
	public void setSAYS_UNREAD(String sAYS_UNREAD) {
		SAYS_UNREAD = sAYS_UNREAD;
	}
	public String getCOMMENT_UNREAD() {
		return COMMENT_UNREAD;
	}
	public void setCOMMENT_UNREAD(String cOMMENT_UNREAD) {
		COMMENT_UNREAD = cOMMENT_UNREAD;
	}
	public int getUNREAD_COMMENT_COUNT() {
		return UNREAD_COMMENT_COUNT;
	}
	public void setUNREAD_COMMENT_COUNT(int uNREAD_COMMENT_COUNT) {
		UNREAD_COMMENT_COUNT = uNREAD_COMMENT_COUNT;
	}
	public List<String> getPICS() {
		return PICS;
	}
	public void setPICS(List<String> pICS) {
		PICS = pICS;
	}
	public List<String> getPICS_THUMB() {
		return PICS_THUMB;
	}
	public void setPICS_THUMB(List<String> pICS_THUMB) {
		PICS_THUMB = pICS_THUMB;
	}
}
