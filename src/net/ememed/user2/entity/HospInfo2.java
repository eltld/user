package net.ememed.user2.entity;

import java.io.Serializable;

public class HospInfo2 implements Serializable{
	private String HOSPITALID;  //"71",
	private String HOSPITALCODE;  //null,
	private String HOSPITALNAME;  //"c0\",
	private String POSTCODE;  //null,
	private String HOSTEL;  //"020\u201461339399",
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
