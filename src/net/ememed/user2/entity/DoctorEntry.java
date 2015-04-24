package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

public class DoctorEntry implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -2126563663472021325L;

	private HospitalInfo HOSPITAL_INFO;
	private String DOCTORID;// => 88946
	private String REALNAME;// => 建仁（薏米）
	private String AVATAR; // =>
							// http://www.ememed.net/uploads/avatar/20140725/avatar_88946_1406273803_EBiDyb5kga.jpg
	private String ALLOWFREECONSULT;
	private String PROFESSIONAL;// => 主任医师
	private String ADDRESS;// => 地址
	private String HOSPITALNAME;// => 薏米医院
	private String ROOMNAME;// => 中医儿科
	private String SPECIALITY;// => 薏米一生
	private String DOCTOR_DSRS;// =>
	private int TEXT_OFFER;
	private int CALL_OFFER;
	private int SHANGMEN_OFFER;
	private int JIAHAO_OFFER;
	private int ZHUYUAN_OFFER;
	private int CUSTOM_OFFER;
	private String TEXT_OFFER_PRICE;
	private String CALL_OFFER_PRICE;
	private String PACKET_OFFER;
	private String SERVICE_CONTENT;
	private String PACKET_ID;
	private List<PacketPeriod> PERIOD_LIST;

	/**
	 * 多余
	 */
	private String SEXNAME;// => 女
	private String MOBILE;// => 13800138086
	private String DETAILINFO;
	private float JIAHAO_OFFER_PRICE;
	private float ZHUYUAN_OFFER_PRICE;
	private String UPDATETIME;
	private String PERCENT;
	private DoctorServiceEntry OFFER_SERVICE;

	private String AUDITSTATUS;
	private String fans_total_num;
	private String fans_user_total_num;
	private String fans_doctor_total_num;
	private DoctorFansInfo DOCTOR_FANS;

	public DoctorFansInfo getDOCTOR_FANS() {
		return DOCTOR_FANS;
	}

	public void setDOCTOR_FANS(DoctorFansInfo dOCTOR_FANS) {
		DOCTOR_FANS = dOCTOR_FANS;
	}

	public String getAUDITSTATUS() {
		return AUDITSTATUS;
	}

	public void setAUDITSTATUS(String aUDITSTATUS) {
		AUDITSTATUS = aUDITSTATUS;
	}

	public String getFans_total_num() {
		return fans_total_num;
	}

	public void setFans_total_num(String fans_total_num) {
		this.fans_total_num = fans_total_num;
	}

	public String getFans_user_total_num() {
		return fans_user_total_num;
	}

	public void setFans_user_total_num(String fans_user_total_num) {
		this.fans_user_total_num = fans_user_total_num;
	}

	public String getFans_doctor_total_num() {
		return fans_doctor_total_num;
	}

	public void setFans_doctor_total_num(String fans_doctor_total_num) {
		this.fans_doctor_total_num = fans_doctor_total_num;
	}

	public HospitalInfo getHOSPITAL_INFO() {
		return HOSPITAL_INFO;
	}

	public void setHOSPITAL_INFO(HospitalInfo hOSPITAL_INFO) {
		HOSPITAL_INFO = hOSPITAL_INFO;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

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

	public String getALLOWFREECONSULT() {
		return ALLOWFREECONSULT;
	}

	public void setALLOWFREECONSULT(String aLLOWFREECONSULT) {
		ALLOWFREECONSULT = aLLOWFREECONSULT;
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

	/*** ＝＝＝＝ 非常重要 ＝＝＝ 一定要注意＝＝＝＝＝-以下字段为迅搜 搜索医生专用----- ****/

	public int getTEXT_OFFER() {
		return TEXT_OFFER;
	}

	public void setTEXT_OFFER(int tEXT_OFFER) {
		TEXT_OFFER = tEXT_OFFER;
	}

	public int getCALL_OFFER() {
		return CALL_OFFER;
	}

	public void setCALL_OFFER(int cALL_OFFER) {
		CALL_OFFER = cALL_OFFER;
	}

	public int getSHANGMEN_OFFER() {
		return SHANGMEN_OFFER;
	}

	public void setSHANGMEN_OFFER(int sHANGMEN_OFFER) {
		SHANGMEN_OFFER = sHANGMEN_OFFER;
	}

	public int getJIAHAO_OFFER() {
		return JIAHAO_OFFER;
	}

	public void setJIAHAO_OFFER(int jIAHAO_OFFER) {
		JIAHAO_OFFER = jIAHAO_OFFER;
	}

	public int getZHUYUAN_OFFER() {
		return ZHUYUAN_OFFER;
	}

	public void setZHUYUAN_OFFER(int zHUYUAN_OFFER) {
		ZHUYUAN_OFFER = zHUYUAN_OFFER;
	}

	public int getCUSTOM_OFFER() {
		return CUSTOM_OFFER;
	}

	public void setCUSTOM_OFFER(int cUSTOM_OFFER) {
		CUSTOM_OFFER = cUSTOM_OFFER;
	}

	public String getTEXT_OFFER_PRICE() {
		return TEXT_OFFER_PRICE;
	}

	public void setTEXT_OFFER_PRICE(String tEXT_OFFER_PRICE) {
		TEXT_OFFER_PRICE = tEXT_OFFER_PRICE;
	}

	public String getCALL_OFFER_PRICE() {
		return CALL_OFFER_PRICE;
	}

	public void setCALL_OFFER_PRICE(String cALL_OFFER_PRICE) {
		CALL_OFFER_PRICE = cALL_OFFER_PRICE;
	}

	public float getJIAHAO_OFFER_PRICE() {
		return JIAHAO_OFFER_PRICE;
	}

	public void setJIAHAO_OFFER_PRICE(float jIAHAO_OFFER_PRICE) {
		JIAHAO_OFFER_PRICE = jIAHAO_OFFER_PRICE;
	}

	public float getZHUYUAN_OFFER_PRICE() {
		return ZHUYUAN_OFFER_PRICE;
	}

	public void setZHUYUAN_OFFER_PRICE(float zHUYUAN_OFFER_PRICE) {
		ZHUYUAN_OFFER_PRICE = zHUYUAN_OFFER_PRICE;
	}

	public String getUPDATETIME() {
		return UPDATETIME;
	}

	public void setUPDATETIME(String uPDATETIME) {
		UPDATETIME = uPDATETIME;
	}

	public List<PacketPeriod> getPERIOD_LIST() {
		return PERIOD_LIST;
	}

	public void setPERIOD_LIST(List<PacketPeriod> pERIOD_LIST) {
		PERIOD_LIST = pERIOD_LIST;
	}

	public String getSERVICE_CONTENT() {
		return SERVICE_CONTENT;
	}

	public void setSERVICE_CONTENT(String sERVICE_CONTENT) {
		SERVICE_CONTENT = sERVICE_CONTENT;
	}

	public String getPACKET_ID() {
		return PACKET_ID;
	}

	public void setPACKET_ID(String pACKET_ID) {
		PACKET_ID = pACKET_ID;
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

	public String getPACKET_OFFER() {
		return PACKET_OFFER;
	}

	public void setPACKET_OFFER(String pACKET_OFFER) {
		PACKET_OFFER = pACKET_OFFER;
	}

	public String getPERCENT() {
		return PERCENT;
	}

	public void setPERCENT(String pERCENT) {
		PERCENT = pERCENT;
	}

	@Override
	public String toString() {
		return "DoctorEntry [HOSPITAL_INFO=" + HOSPITAL_INFO + ", DOCTORID=" + DOCTORID
				+ ", REALNAME=" + REALNAME + ", AVATAR=" + AVATAR + ", ALLOWFREECONSULT="
				+ ALLOWFREECONSULT + ", PROFESSIONAL=" + PROFESSIONAL + ", ADDRESS=" + ADDRESS
				+ ", HOSPITALNAME=" + HOSPITALNAME + ", ROOMNAME=" + ROOMNAME + ", SPECIALITY="
				+ SPECIALITY + ", DOCTOR_DSRS=" + DOCTOR_DSRS + ", TEXT_OFFER=" + TEXT_OFFER
				+ ", CALL_OFFER=" + CALL_OFFER + ", SHANGMEN_OFFER=" + SHANGMEN_OFFER
				+ ", JIAHAO_OFFER=" + JIAHAO_OFFER + ", ZHUYUAN_OFFER=" + ZHUYUAN_OFFER
				+ ", CUSTOM_OFFER=" + CUSTOM_OFFER + ", TEXT_OFFER_PRICE=" + TEXT_OFFER_PRICE
				+ ", CALL_OFFER_PRICE=" + CALL_OFFER_PRICE + ", PACKET_OFFER=" + PACKET_OFFER
				+ ", SERVICE_CONTENT=" + SERVICE_CONTENT + ", PACKET_ID=" + PACKET_ID
				+ ", PERIOD_LIST=" + PERIOD_LIST + ", SEXNAME=" + SEXNAME + ", MOBILE=" + MOBILE
				+ ", DETAILINFO=" + DETAILINFO + ", JIAHAO_OFFER_PRICE=" + JIAHAO_OFFER_PRICE
				+ ", ZHUYUAN_OFFER_PRICE=" + ZHUYUAN_OFFER_PRICE + ", UPDATETIME=" + UPDATETIME
				+ ", PERCENT=" + PERCENT + ", OFFER_SERVICE=" + OFFER_SERVICE + "]";
	}

}
