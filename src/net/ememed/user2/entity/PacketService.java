package net.ememed.user2.entity;

import java.io.Serializable;

public class PacketService implements Serializable{
	private String DESC_PIC;// "",
	private String CONTENT;// null,
	private String SERVICEID;// "8193",
	private String DSRATTITUDE;// null,
	private String RATECONTENT;// null,
	private String EVALUATION;// "0",
	private String STYPE;// "1"
	public String getDESC_PIC() {
		return DESC_PIC;
	}
	public void setDESC_PIC(String dESC_PIC) {
		DESC_PIC = dESC_PIC;
	}
	public String getCONTENT() {
		return CONTENT;
	}
	public void setCONTENT(String cONTENT) {
		CONTENT = cONTENT;
	}
	public String getSERVICEID() {
		return SERVICEID;
	}
	public void setSERVICEID(String sERVICEID) {
		SERVICEID = sERVICEID;
	}
	public String getDSRATTITUDE() {
		return DSRATTITUDE;
	}
	public void setDSRATTITUDE(String dSRATTITUDE) {
		DSRATTITUDE = dSRATTITUDE;
	}
	public String getRATECONTENT() {
		return RATECONTENT;
	}
	public void setRATECONTENT(String rATECONTENT) {
		RATECONTENT = rATECONTENT;
	}
	public String getEVALUATION() {
		return EVALUATION;
	}
	public void setEVALUATION(String eVALUATION) {
		EVALUATION = eVALUATION;
	}
	public String getSTYPE() {
		return STYPE;
	}
	public void setSTYPE(String sTYPE) {
		STYPE = sTYPE;
	}
	
}
