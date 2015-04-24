package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

import net.ememed.user2.baike.entity.BaikeSaysInfo;

public class MemberInfoEntity implements Serializable {
	private String MEMBERID;
	private String MEMBERNAME;
	private String PASSWORD;
	private String UTYPE;
	private String EMAIL;
	private String EMAILSTATUS;
	private String STATUS;
	private String REALNAME;
	private String REGTIME;
	private String INCOMEMONEY;
	private String USERMONEY;
	private String FREEZEMONEY;
	private String NEWPM;
	private String AVATAR;
	private String PAYPASSWORD;
	private String ISTESTER;
	private String OPENID;
	private String SINAOPENID;
	private String REG_CHANNEL;
	private String LAST_CHANNEL;
	private String BOSSTEST;
	private String TOKEN;
	private String SEX;
	private String PROFESSIONAL;
	private String CARDTYPE;
	private String CARDNUMBER;
	private String CARDFILE;
	private String DOCTORCODE;
	private String SPECIALITY;
	private String HOSPITALNAME;
	private String ROOMNAME;
	private String CERTIFICATE;
	private String CERTIFICATEHOME;
	private String CERTIFICATENUM;
	private String RECOMMEND;
	private String ALLOWFREECONSULT;
	private String MOBILE;
	private String BIRTHDAY;
	private String AUDITSTATUS;
	private String AUDITREFUSE;
	private List<OrderListEntry> OPEN_ORDER;
	private String NOW;
	private DoctorServiceEntry OFFER_SERVICE;
	private String DOCTOR_DSRS;

	private String ADDRESS;
	private String DETAILINFO;
	private String ENABLE_WEITALK;
	

	private HospitalInfo HOSPITAL_INFO;
	private List<BaikeSaysInfo> SAYS_LIST;
	private DoctorFansInfo DOCTOR_FANS;

	private String fans_total_num; // 20,
	private String fans_unread_num; // 0,
	private String fans_user_total_num; // 12,
	private String fans_user_unread_num; // 0,
	private String fans_doctor_total_num; // 8,
	private String fans_doctor_unread_num; // 0
	
	public String getFans_total_num() {
		return fans_total_num;
	}

	public void setFans_total_num(String fans_total_num) {
		this.fans_total_num = fans_total_num;
	}

	public String getFans_unread_num() {
		return fans_unread_num;
	}

	public void setFans_unread_num(String fans_unread_num) {
		this.fans_unread_num = fans_unread_num;
	}

	public String getFans_user_total_num() {
		return fans_user_total_num;
	}

	public void setFans_user_total_num(String fans_user_total_num) {
		this.fans_user_total_num = fans_user_total_num;
	}

	public String getFans_user_unread_num() {
		return fans_user_unread_num;
	}

	public void setFans_user_unread_num(String fans_user_unread_num) {
		this.fans_user_unread_num = fans_user_unread_num;
	}

	public String getFans_doctor_total_num() {
		return fans_doctor_total_num;
	}

	public void setFans_doctor_total_num(String fans_doctor_total_num) {
		this.fans_doctor_total_num = fans_doctor_total_num;
	}

	public String getFans_doctor_unread_num() {
		return fans_doctor_unread_num;
	}

	public void setFans_doctor_unread_num(String fans_doctor_unread_num) {
		this.fans_doctor_unread_num = fans_doctor_unread_num;
	}

	public DoctorFansInfo getDOCTOR_FANS() {
		return DOCTOR_FANS;
	}

	public void setDOCTOR_FANS(DoctorFansInfo dOCTOR_FANS) {
		DOCTOR_FANS = dOCTOR_FANS;
	}

	public List<BaikeSaysInfo> getSAYS_LIST() {
		return SAYS_LIST;
	}

	public void setSAYS_LIST(List<BaikeSaysInfo> sAYS_LIST) {
		SAYS_LIST = sAYS_LIST;
	}

	public String getMEMBERID() {
		return MEMBERID;
	}

	public void setMEMBERID(String mEMBERID) {
		MEMBERID = mEMBERID;
	}

	public String getMEMBERNAME() {
		return MEMBERNAME;
	}

	public void setMEMBERNAME(String mEMBERNAME) {
		MEMBERNAME = mEMBERNAME;
	}

	public String getPASSWORD() {
		return PASSWORD;
	}

	public void setPASSWORD(String pASSWORD) {
		PASSWORD = pASSWORD;
	}

	public String getUTYPE() {
		return UTYPE;
	}

	public void setUTYPE(String uTYPE) {
		UTYPE = uTYPE;
	}

	public String getEMAIL() {
		return EMAIL;
	}

	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}

	public String getEMAILSTATUS() {
		return EMAILSTATUS;
	}

	public void setEMAILSTATUS(String eMAILSTATUS) {
		EMAILSTATUS = eMAILSTATUS;
	}

	public String getSTATUS() {
		return STATUS;
	}

	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}

	public String getREALNAME() {
		return REALNAME;
	}

	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
	}

	public String getREGTIME() {
		return REGTIME;
	}

	public void setREGTIME(String rEGTIME) {
		REGTIME = rEGTIME;
	}

	public String getINCOMEMONEY() {
		return INCOMEMONEY;
	}

	public void setINCOMEMONEY(String iNCOMEMONEY) {
		INCOMEMONEY = iNCOMEMONEY;
	}

	public String getUSERMONEY() {
		return USERMONEY;
	}

	public void setUSERMONEY(String uSERMONEY) {
		USERMONEY = uSERMONEY;
	}

	public String getFREEZEMONEY() {
		return FREEZEMONEY;
	}

	public void setFREEZEMONEY(String fREEZEMONEY) {
		FREEZEMONEY = fREEZEMONEY;
	}

	public String getNEWPM() {
		return NEWPM;
	}

	public void setNEWPM(String nEWPM) {
		NEWPM = nEWPM;
	}

	public String getAVATAR() {
		return AVATAR;
	}

	public void setAVATAR(String aVATAR) {
		AVATAR = aVATAR;
	}

	public String getPAYPASSWORD() {
		return PAYPASSWORD;
	}

	public void setPAYPASSWORD(String pAYPASSWORD) {
		PAYPASSWORD = pAYPASSWORD;
	}

	public String getISTESTER() {
		return ISTESTER;
	}

	public void setISTESTER(String iSTESTER) {
		ISTESTER = iSTESTER;
	}

	public String getOPENID() {
		return OPENID;
	}

	public void setOPENID(String oPENID) {
		OPENID = oPENID;
	}

	public String getSINAOPENID() {
		return SINAOPENID;
	}

	public void setSINAOPENID(String sINAOPENID) {
		SINAOPENID = sINAOPENID;
	}

	public String getREG_CHANNEL() {
		return REG_CHANNEL;
	}

	public void setREG_CHANNEL(String rEG_CHANNEL) {
		REG_CHANNEL = rEG_CHANNEL;
	}

	public String getLAST_CHANNEL() {
		return LAST_CHANNEL;
	}

	public void setLAST_CHANNEL(String lAST_CHANNEL) {
		LAST_CHANNEL = lAST_CHANNEL;
	}

	public String getBOSSTEST() {
		return BOSSTEST;
	}

	public void setBOSSTEST(String bOSSTEST) {
		BOSSTEST = bOSSTEST;
	}

	public String getTOKEN() {
		return TOKEN;
	}

	public void setTOKEN(String tOKEN) {
		TOKEN = tOKEN;
	}

	public String getSEX() {
		return SEX;
	}

	public void setSEX(String sEX) {
		SEX = sEX;
	}

	public String getPROFESSIONAL() {
		return PROFESSIONAL;
	}

	public void setPROFESSIONAL(String pROFESSIONAL) {
		PROFESSIONAL = pROFESSIONAL;
	}

	public String getCARDTYPE() {
		return CARDTYPE;
	}

	public void setCARDTYPE(String cARDTYPE) {
		CARDTYPE = cARDTYPE;
	}

	public String getCARDNUMBER() {
		return CARDNUMBER;
	}

	public void setCARDNUMBER(String cARDNUMBER) {
		CARDNUMBER = cARDNUMBER;
	}

	public String getCARDFILE() {
		return CARDFILE;
	}

	public void setCARDFILE(String cARDFILE) {
		CARDFILE = cARDFILE;
	}

	public String getDOCTORCODE() {
		return DOCTORCODE;
	}

	public void setDOCTORCODE(String dOCTORCODE) {
		DOCTORCODE = dOCTORCODE;
	}

	public String getSPECIALITY() {
		return SPECIALITY;
	}

	public void setSPECIALITY(String sPECIALITY) {
		SPECIALITY = sPECIALITY;
	}

	public String getHOSPITALNAME() {
		return HOSPITALNAME;
	}

	public void setHOSPITALNAME(String hOSPITALNAME) {
		HOSPITALNAME = hOSPITALNAME;
	}

	public String getROOMNAME() {
		return ROOMNAME;
	}

	public void setROOMNAME(String rOOMNAME) {
		ROOMNAME = rOOMNAME;
	}

	public String getCERTIFICATE() {
		return CERTIFICATE;
	}

	public void setCERTIFICATE(String cERTIFICATE) {
		CERTIFICATE = cERTIFICATE;
	}

	public String getCERTIFICATEHOME() {
		return CERTIFICATEHOME;
	}

	public void setCERTIFICATEHOME(String cERTIFICATEHOME) {
		CERTIFICATEHOME = cERTIFICATEHOME;
	}

	public String getCERTIFICATENUM() {
		return CERTIFICATENUM;
	}

	public void setCERTIFICATENUM(String cERTIFICATENUM) {
		CERTIFICATENUM = cERTIFICATENUM;
	}

	public String getRECOMMEND() {
		return RECOMMEND;
	}

	public void setRECOMMEND(String rECOMMEND) {
		RECOMMEND = rECOMMEND;
	}

	public String getALLOWFREECONSULT() {
		return ALLOWFREECONSULT;
	}

	public void setALLOWFREECONSULT(String aLLOWFREECONSULT) {
		ALLOWFREECONSULT = aLLOWFREECONSULT;
	}

	public String getMOBILE() {
		return MOBILE;
	}

	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}

	public String getBIRTHDAY() {
		return BIRTHDAY;
	}

	public void setBIRTHDAY(String bIRTHDAY) {
		BIRTHDAY = bIRTHDAY;
	}

	public String getAUDITSTATUS() {
		return AUDITSTATUS;
	}

	public void setAUDITSTATUS(String aUDITSTATUS) {
		AUDITSTATUS = aUDITSTATUS;
	}

	public String getAUDITREFUSE() {
		return AUDITREFUSE;
	}

	public void setAUDITREFUSE(String aUDITREFUSE) {
		AUDITREFUSE = aUDITREFUSE;
	}

	public List<OrderListEntry> getOPEN_ORDER() {
		return OPEN_ORDER;
	}

	public void setOPEN_ORDER(List<OrderListEntry> oPEN_ORDER) {
		OPEN_ORDER = oPEN_ORDER;
	}

	public String getNOW() {
		return NOW;
	}

	public void setNOW(String nOW) {
		NOW = nOW;
	}

	public DoctorServiceEntry getOFFER_SERVICE() {
		return OFFER_SERVICE;
	}

	public void setOFFER_SERVICE(DoctorServiceEntry oFFER_SERVICE) {
		OFFER_SERVICE = oFFER_SERVICE;
	}

	public String getDOCTOR_DSRS() {
		return DOCTOR_DSRS;
	}

	public void setDOCTOR_DSRS(String dOCTOR_DSRS) {
		DOCTOR_DSRS = dOCTOR_DSRS;
	}

	public String getADDRESS() {
		return ADDRESS;
	}

	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}

	public String getDETAILINFO() {
		return DETAILINFO;
	}

	public void setDETAILINFO(String dETAILINFO) {
		DETAILINFO = dETAILINFO;
	}

	public HospitalInfo getHOSPITAL_INFO() {
		return HOSPITAL_INFO;
	}

	public void setHOSPITAL_INFO(HospitalInfo hOSPITAL_INFO) {
		HOSPITAL_INFO = hOSPITAL_INFO;
	}

	public String getENABLE_WEITALK() {
		return ENABLE_WEITALK;
	}

	public void setENABLE_WEITALK(String eNABLE_WEITALK) {
		ENABLE_WEITALK = eNABLE_WEITALK;
	}
	
	

}
