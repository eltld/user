package net.ememed.user2.entity;

import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;

import com.google.gson.JsonObject;

/**
 * 发送消息的备用字段
 * 
 * @author chen
 * 
 */
public class MessageBackupFieldEntry {
	private String ISSYSTEMMSG;
	private String CHANNEL;
	private String ORDERID;
	private String SERVICEID;
	private String PACKET_BUY_ID;
	private String USERID;
	private String DOCTORID;
	private String ORDERTYPE;
	private String user_avatar;
	private String doctor_avatar;
	private String user_name;
	private String doctor_name;
	private String order_state;

	public String getISSYSTEMMSG() {
		return ISSYSTEMMSG;
	}

	public void setISSYSTEMMSG(String iSSYSTEMMSG) {
		ISSYSTEMMSG = iSSYSTEMMSG;
	}

	public String getCHANNEL() {
		return CHANNEL;
	}

	public void setCHANNEL(String cHANNEL) {
		CHANNEL = cHANNEL;
	}

	public String getORDERID() {
		return ORDERID;
	}

	public void setORDERID(String oRDERID) {
		ORDERID = oRDERID;
	}

	public String getSERVICEID() {
		return SERVICEID;
	}

	public void setSERVICEID(String sERVICEID) {
		SERVICEID = sERVICEID;
	}

	public String getPACKET_BUY_ID() {
		return PACKET_BUY_ID;
	}

	public void setPACKET_BUY_ID(String pACKET_BUY_ID) {
		PACKET_BUY_ID = pACKET_BUY_ID;
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

	public String getORDERTYPE() {
		return ORDERTYPE;
	}

	public void setORDERTYPE(String oRDERTYPE) {
		ORDERTYPE = oRDERTYPE;
	}

	public String getUser_avatar() {
		return user_avatar;
	}

	public void setUser_avatar(String user_avatar) {
		this.user_avatar = user_avatar;
	}

	public String getDoctor_avatar() {
		return doctor_avatar;
	}

	public void setDoctor_avatar(String doctor_avatar) {
		this.doctor_avatar = doctor_avatar;
	}

	public String getOrder_state() {
		return order_state;
	}

	public void setOrder_state(String order_state) {
		this.order_state = order_state;
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}

	public String getDoctor_name() {
		return doctor_name;
	}

	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}

	public String getExtToString() {

		JsonObject obj = new JsonObject();
		obj.addProperty("SERVICEID", SERVICEID);
		obj.addProperty("DOCTORID", DOCTORID);
		obj.addProperty("ORDERTYPE", ORDERTYPE);
		obj.addProperty("user_avatar", user_avatar);
		obj.addProperty("doctor_name", doctor_name);
		obj.addProperty("user_name", user_name);
		obj.addProperty("doctor_avatar", doctor_avatar);
		obj.addProperty("USERID", USERID);
		obj.addProperty("ISSYSTEMMSG", ISSYSTEMMSG);
		obj.addProperty("CHANNEL", CHANNEL);
		obj.addProperty("ORDERID", ORDERID);
		
		return obj.toString();

	}

}
