package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

public class OrderListEntry implements Serializable{
	private String ORDERID;
	private String ORDERSN;
	private String ORDERTYPE;
	private String USERID;
	private String DOCTORID;
	private String ORDERSTATUS;
	private String PAYSTATUS;
	private String CONTRACTOR;
	private String TELPHONE;
	private String MOBILE;
	private String EMAIL;
	private String PAYID;
	private String PAYNAME;
	private String GOODSAMOUNT;
	private String PAYFEE;
	private String DESC_TEXT;
	private String DISCOUNT;
	private String ORDERAMOUNT;
	private String ADDTIME;
	private String CONFIRMTIME;
	private String PAYTIME;
	private String BALANCE;
	private String BALANCETIME;
	private String BANKDEDUCTTYPE;
	private String DEPOSITBACKTYPE;
	private String BACKFORORDERID;
	private String CHANNELID;
	private String CHANNELNAME;
	private String FROMTYPE;
	private String FROMOTHER;
	private String MEMO;
	private String FREE;
	private String SMSSENDED;
	private String ORIGINALPRICE;
	private String CHANNEL;
	private String COUPONNUM;
	private String BALANCEPRICE;
	private String OVERTIME;
	private String RNUM;
	private String STATE;
	private String SERVICEID;
	private String CALL_TIME;
	private String DSRATTITUDE;
	private String EVALUATION;
	private String DOCTORNAME;
	private String CALL_TIME_STATUS;
	private String PACKET_STARTTIME;
	private String PACKET_ENDTIME;
	private String AVATAR;
	private String ORDERTYPENAME;
	private String STATE_DESC;
	private String QUESTIONID;
	private List<PacketService> PACKET_SERVICE;
	
	
	public String getQUESTIONID() {
		return QUESTIONID;
	}
	public void setQUESTIONID(String qUESTIONID) {
		QUESTIONID = qUESTIONID;
	}
	public String getSERVICEID() {
		return SERVICEID;
	}
	public void setSERVICEID(String sERVICEID) {
		SERVICEID = sERVICEID;
	}
	public String getORDERID() {
		return ORDERID;
	}
	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}
	public String getORDERSN() {
		return ORDERSN;
	}
	public void setORDERSN(String oRDERSN) {
		ORDERSN = oRDERSN;
	}
	public String getORDERTYPE() {
		return ORDERTYPE;
	}
	public void setORDERTYPE(String oRDERTYPE) {
		ORDERTYPE = oRDERTYPE;
	}
	public String getUSERID() {
		return USERID;
	}
	public void setUSERID(String uSERID) {
		USERID = uSERID;
	}
	public String getDOCTORID() {
		return DOCTORID;
	}
	public void setDOCTORID(String dOCTORID) {
		DOCTORID = dOCTORID;
	}
	public String getORDERSTATUS() {
		return ORDERSTATUS;
	}
	public void setORDERSTATUS(String oRDERSTATUS) {
		ORDERSTATUS = oRDERSTATUS;
	}
	public String getPAYSTATUS() {
		return PAYSTATUS;
	}
	public void setPAYSTATUS(String pAYSTATUS) {
		PAYSTATUS = pAYSTATUS;
	}
	public String getCONTRACTOR() {
		return CONTRACTOR;
	}
	public void setCONTRACTOR(String cONTRACTOR) {
		CONTRACTOR = cONTRACTOR;
	}
	public String getTELPHONE() {
		return TELPHONE;
	}
	public void setTELPHONE(String tELPHONE) {
		TELPHONE = tELPHONE;
	}
	public String getMOBILE() {
		return MOBILE;
	}
	public void setMOBILE(String mOBILE) {
		MOBILE = mOBILE;
	}
	public String getEMAIL() {
		return EMAIL;
	}
	public void setEMAIL(String eMAIL) {
		EMAIL = eMAIL;
	}
	public String getPAYID() {
		return PAYID;
	}
	public void setPAYID(String pAYID) {
		PAYID = pAYID;
	}
	public String getPAYNAME() {
		return PAYNAME;
	}
	public void setPAYNAME(String pAYNAME) {
		PAYNAME = pAYNAME;
	}
	public String getGOODSAMOUNT() {
		return GOODSAMOUNT;
	}
	public void setGOODSAMOUNT(String gOODSAMOUNT) {
		GOODSAMOUNT = gOODSAMOUNT;
	}
	public String getPAYFEE() {
		return PAYFEE;
	}
	public void setPAYFEE(String pAYFEE) {
		PAYFEE = pAYFEE;
	}
	public String getDISCOUNT() {
		return DISCOUNT;
	}
	public void setDISCOUNT(String dISCOUNT) {
		DISCOUNT = dISCOUNT;
	}
	public String getORDERAMOUNT() {
		return ORDERAMOUNT;
	}
	public void setORDERAMOUNT(String oRDERAMOUNT) {
		ORDERAMOUNT = oRDERAMOUNT;
	}
	public String getADDTIME() {
		return ADDTIME;
	}
	public void setADDTIME(String aDDTIME) {
		ADDTIME = aDDTIME;
	}
	public String getCONFIRMTIME() {
		return CONFIRMTIME;
	}
	public void setCONFIRMTIME(String cONFIRMTIME) {
		CONFIRMTIME = cONFIRMTIME;
	}
	public String getPAYTIME() {
		return PAYTIME;
	}
	public void setPAYTIME(String pAYTIME) {
		PAYTIME = pAYTIME;
	}
	public String getBALANCE() {
		return BALANCE;
	}
	public void setBALANCE(String bALANCE) {
		BALANCE = bALANCE;
	}
	public String getBALANCETIME() {
		return BALANCETIME;
	}
	public void setBALANCETIME(String bALANCETIME) {
		BALANCETIME = bALANCETIME;
	}
	public String getBANKDEDUCTTYPE() {
		return BANKDEDUCTTYPE;
	}
	public void setBANKDEDUCTTYPE(String bANKDEDUCTTYPE) {
		BANKDEDUCTTYPE = bANKDEDUCTTYPE;
	}
	public String getDEPOSITBACKTYPE() {
		return DEPOSITBACKTYPE;
	}
	public void setDEPOSITBACKTYPE(String dEPOSITBACKTYPE) {
		DEPOSITBACKTYPE = dEPOSITBACKTYPE;
	}
	public String getBACKFORORDERID() {
		return BACKFORORDERID;
	}
	public void setBACKFORORDERID(String bACKFORORDERID) {
		BACKFORORDERID = bACKFORORDERID;
	}
	public String getCHANNELID() {
		return CHANNELID;
	}
	public void setCHANNELID(String cHANNELID) {
		CHANNELID = cHANNELID;
	}
	public String getCHANNELNAME() {
		return CHANNELNAME;
	}
	public void setCHANNELNAME(String cHANNELNAME) {
		CHANNELNAME = cHANNELNAME;
	}
	public String getFROMTYPE() {
		return FROMTYPE;
	}
	public void setFROMTYPE(String fROMTYPE) {
		FROMTYPE = fROMTYPE;
	}
	public String getFROMOTHER() {
		return FROMOTHER;
	}
	public void setFROMOTHER(String fROMOTHER) {
		FROMOTHER = fROMOTHER;
	}
	public String getMEMO() {
		return MEMO;
	}
	public void setMEMO(String mEMO) {
		MEMO = mEMO;
	}
	public String getFREE() {
		return FREE;
	}
	public void setFREE(String fREE) {
		FREE = fREE;
	}
	public String getSMSSENDED() {
		return SMSSENDED;
	}
	public void setSMSSENDED(String sMSSENDED) {
		SMSSENDED = sMSSENDED;
	}
	public String getORIGINALPRICE() {
		return ORIGINALPRICE;
	}
	public void setORIGINALPRICE(String oRIGINALPRICE) {
		ORIGINALPRICE = oRIGINALPRICE;
	}
	public String getCHANNEL() {
		return CHANNEL;
	}
	public void setCHANNEL(String cHANNEL) {
		CHANNEL = cHANNEL;
	}
	public String getCOUPONNUM() {
		return COUPONNUM;
	}
	public void setCOUPONNUM(String cOUPONNUM) {
		COUPONNUM = cOUPONNUM;
	}
	public String getBALANCEPRICE() {
		return BALANCEPRICE;
	}
	public void setBALANCEPRICE(String bALANCEPRICE) {
		BALANCEPRICE = bALANCEPRICE;
	}
	public String getOVERTIME() {
		return OVERTIME;
	}
	public void setOVERTIME(String oVERTIME) {
		OVERTIME = oVERTIME;
	}
	public String getRNUM() {
		return RNUM;
	}
	public void setRNUM(String rNUM) {
		RNUM = rNUM;
	}
	public String getSTATE() {
		return STATE;
	}
	public void setSTATE(String sTATE) {
		STATE = sTATE;
	}
	public String getCALL_TIME() {
		return CALL_TIME;
	}
	public void setCALL_TIME(String cALL_TIME) {
		CALL_TIME = cALL_TIME;
	}
	public String getDSRATTITUDE() {
		return DSRATTITUDE;
	}
	public void setDSRATTITUDE(String dSRATTITUDE) {
		DSRATTITUDE = dSRATTITUDE;
	}
	public String getEVALUATION() {
		return EVALUATION;
	}
	public void setEVALUATION(String eVALUATION) {
		EVALUATION = eVALUATION;
	}
	public String getDOCTORNAME() {
		return DOCTORNAME;
	}
	public void setDOCTORNAME(String dOCTORNAME) {
		DOCTORNAME = dOCTORNAME;
	}
	public String getCALL_TIME_STATUS() {
		return CALL_TIME_STATUS;
	}
	public void setCALL_TIME_STATUS(String cALL_TIME_STATUS) {
		CALL_TIME_STATUS = cALL_TIME_STATUS;
	}
	public String getPACKET_STARTTIME() {
		return PACKET_STARTTIME;
	}
	public void setpACKET_STARTTIME(String pACKET_STARTTIME) {
		PACKET_STARTTIME = pACKET_STARTTIME;
	}
	public String getPACKET_ENDTIME() {
		return PACKET_ENDTIME;
	}
	public void setPACKET_ENDTIME(String pACKET_ENDTIME) {
		PACKET_ENDTIME = pACKET_ENDTIME;
	}
	public List<PacketService> getPACKET_SERVICE() {
		return PACKET_SERVICE;
	}
	public void setPACKET_SERVICE(List<PacketService> pACKET_SERVICE) {
		PACKET_SERVICE = pACKET_SERVICE;
	}
	public void setPACKET_STARTTIME(String pACKET_STARTTIME) {
		PACKET_STARTTIME = pACKET_STARTTIME;
	}
	public String getDESC_TEXT() {
		return DESC_TEXT;
	}
	public void setDESC_TEXT(String dESC_TEXT) {
		DESC_TEXT = dESC_TEXT;
	}
	public String getAVATAR() {
		return AVATAR;
	}
	public void setAVATAR(String aVATAR) {
		AVATAR = aVATAR;
	}
	public String getORDERTYPENAME() {
		return ORDERTYPENAME;
	}
	public void setORDERTYPENAME(String oRDERTYPENAME) {
		ORDERTYPENAME = oRDERTYPENAME;
	}
	public String getSTATE_DESC() {
		return STATE_DESC;
	}
	public void setSTATE_DESC(String sTATE_DESC) {
		STATE_DESC = sTATE_DESC;
	}
	
	
}
