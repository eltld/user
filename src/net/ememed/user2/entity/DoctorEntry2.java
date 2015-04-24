package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

public class DoctorEntry2{
	
	
	
//		"OFFER_SERVICE":{"OFFER_TEXT":{"IS_OFFER":1,"OFFER_PRICE":20},
//		"OFFER_CALL":{"IS_OFFER":1,"OFFER_PRICE":50},
//		"OFFER_ZHUYUAN":{"IS_OFFER":1,"OFFER_PRICE":-1},
//		"OFFER_JIAHAO":{"IS_OFFER":0,"OFFER_PRICE":-1},
//		"OFFER_SHANGMEN":{"IS_OFFER":0},
//		"OFFER_CUSTOM":{"IS_OFFER":1},
//		"OFFER_PACKET":{"IS_OFFER":"1","SERVICE_CONTENT":"\u56fe\u6587\u54a8\u8be2\uff0c\u7535\u8bdd\u54a8\u8be2",
//		"PACKET_ID":"2741",
//		"PACKET_PERIOD_LIST":[{"packet_daytype":"1","packet_period_id":"3541","packet_daynum":"1","packet_period_price":"300","packet_period_desc":"300\u5143\/1\u6708"}]}},
//		"HOSPITAL_INFO":null,
	
	private String SEXNAME;  //private String \u7537private String ,
	private String AVATAR;  //private String http:\/\/www.ememed.net\/uploads\/avatar\/201409\/avatar_604ab48998c76903e6490c81bfe68f33.jpgprivate String ,
	private String DOCTORID;  //private String 101568private String ,
	private String HOSPITALID;  //null,
	private String REALNAME;  //private String \u5f20\u5c0f\u6865private String ,
	private String PROFESSIONAL;  //private String \u4e3b\u4efb\u533b\u5e08private String ,
	private String HOSPITALNAME;  //private String \u6d4e\u5357\u519b\u533a\u603b\u533b\u9662private String ,
	private String ROOMNAME;  //private String \u666e\u901a\u5916\u79d1private String ,
	private String MOBILE;  //private String 13553175153private String ,
	private String SPECIALITY;  //private String \u80c3\u80a0\u5916\u79d1\uff0c\u8179\u8154\u955c\u548c\u673a\u5668\u4eba\u80c3\u80a0\u624b\u672f\uff0c\u8179\u8154\u955c\u759d\u4fee\u8865\uff0c\u7cd6\u5c3f\u75c5\u548c\u51cf\u91cd\u624b\u672f\uff0c\u80a0\u5916\u7618\u7efc\u5408\u6cbb\u7597private String ,
	private String ADDRESS;  //null,
	private String DETAILINFO;  //null,
	private String ALLOWFREECONSULT;  //private String 0private String ,
	private String DOCTORSTATUS;  //private String 1private String ,
	private String DOCTOR_DSRS;  //8
	private HospitalInfo HOSPITAL_INFO;  //null,
//	private List<PacketPeriod> PACKET_PERIOD_LIST;
	private OfferSerViceEntity OFFER_SERVICE;

	public String getSEXNAME() {
		return SEXNAME;
	}

	public void setSEXNAME(String sEXNAME) {
		SEXNAME = sEXNAME;
	}

	public String getAVATAR() {
		return AVATAR;
	}

	public void setAVATAR(String aVATAR) {
		AVATAR = aVATAR;
	}

	public String getDOCTORID() {
		return DOCTORID;
	}

	public void setDOCTORID(String dOCTORID) {
		DOCTORID = dOCTORID;
	}

	public String getHOSPITALID() {
		return HOSPITALID;
	}

	public void setHOSPITALID(String hOSPITALID) {
		HOSPITALID = hOSPITALID;
	}

	public String getREALNAME() {
		return REALNAME;
	}

	public void setREALNAME(String rEALNAME) {
		REALNAME = rEALNAME;
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

	public String getROOMNAME() {
		return ROOMNAME;
	}

	public void setROOMNAME(String rOOMNAME) {
		ROOMNAME = rOOMNAME;
	}

	public String getMOBILE() {
		return MOBILE;
	}

	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}

	public String getSPECIALITY() {
		return SPECIALITY;
	}

	public void setSPECIALITY(String sPECIALITY) {
		SPECIALITY = sPECIALITY;
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

	public String getALLOWFREECONSULT() {
		return ALLOWFREECONSULT;
	}

	public void setALLOWFREECONSULT(String aLLOWFREECONSULT) {
		ALLOWFREECONSULT = aLLOWFREECONSULT;
	}

	public String getDOCTORSTATUS() {
		return DOCTORSTATUS;
	}

	public void setDOCTORSTATUS(String dOCTORSTATUS) {
		DOCTORSTATUS = dOCTORSTATUS;
	}

	
	public HospitalInfo getHOSPITAL_INFO() {
		return HOSPITAL_INFO;
	}

	public void setHOSPITAL_INFO(HospitalInfo hOSPITAL_INFO) {
		HOSPITAL_INFO = hOSPITAL_INFO;
	}

	public String getDOCTOR_DSRS() {
		return DOCTOR_DSRS;
	}

	public void setDOCTOR_DSRS(String dOCTOR_DSRS) {
		DOCTOR_DSRS = dOCTOR_DSRS;
	}

	public OfferSerViceEntity getOFFER_SERVICE() {
		return OFFER_SERVICE;
	}

	public void setOFFER_SERVICE(OfferSerViceEntity oFFER_SERVICE) {
		OFFER_SERVICE = oFFER_SERVICE;
	}


	
	
}
