package net.ememed.user2.entity;

import java.io.Serializable;

public class HospitalInfo  implements Serializable{
/**
	 * 
	 */
	private static final long serialVersionUID = 4125241324174065260L;
//	"HOSPITALID": "2609", 
//    "HOSPITALCODE": null, 
//    "HOSPITALNAME": "天津医科大学总医院", 
//    "ADDRESS": "天津市和平区鞍山道154号", 
//    "POSTCODE": null, 
//    "HOSTEL": "022-60362255", 
//    "CONTEXT": "​天津医科大学总医院始建于1946年，是天津市最大的集医疗、教学、科研、预防为一体的综合性大学医院和天津市西医医学中心，医院为三级甲等医院、全国“百佳医院”、全国百姓放心示范医院。<BR>1950年4月中央医院改名为天津市立总医院。1956年更名为天津医学院附属医院。1994年8月改名为天津医科大学总医院。1993年被评为三级甲等医院，1998年被评选为全国“百佳医院”，2001年被天津市政府确定为天津市西医医学中心，2009年被评为全国百姓放心示范医院。", 
//    "GRADE": "三级甲等"
	private String HOSPITALID;
	private String HOSPITALCODE;
	private String HOSPITALNAME;
	private String ADDRESS;
	private String POSTCODE;
	private String HOSTEL;
	private String CONTEXT;
	private String GRADE;
	public String getHOSPITALID() {
		return HOSPITALID;
	}
	public void setHOSPITALID(String hOSPITALID) {
		HOSPITALID = hOSPITALID;
	}
	public String getHOSPITALCODE() {
		return HOSPITALCODE;
	}
	public void setHOSPITALCODE(String hOSPITALCODE) {
		HOSPITALCODE = hOSPITALCODE;
	}
	public String getHOSPITALNAME() {
		return HOSPITALNAME;
	}
	public void setHOSPITALNAME(String hOSPITALNAME) {
		HOSPITALNAME = hOSPITALNAME;
	}
	public String getADDRESS() {
		return ADDRESS;
	}
	public void setADDRESS(String aDDRESS) {
		ADDRESS = aDDRESS;
	}
	public String getPOSTCODE() {
		return POSTCODE;
	}
	public void setPOSTCODE(String pOSTCODE) {
		POSTCODE = pOSTCODE;
	}
	public String getHOSTEL() {
		return HOSTEL;
	}
	public void setHOSTEL(String hOSTEL) {
		HOSTEL = hOSTEL;
	}
	public String getCONTEXT() {
		return CONTEXT;
	}
	public void setCONTEXT(String cONTEXT) {
		CONTEXT = cONTEXT;
	}
	public String getGRADE() {
		return GRADE;
	}
	public void setGRADE(String gRADE) {
		GRADE = gRADE;
	}
	
}
