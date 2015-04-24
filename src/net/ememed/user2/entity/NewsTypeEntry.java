package net.ememed.user2.entity;

import java.io.Serializable;

public class NewsTypeEntry implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6140650436722393559L;
	
	private String ID;//=> 82
	private String TITLE; //=> 两性
	private String STATUS;//=> 1
	private String UPDATETIME;//=> 2014-06-04 04:41:04
	private String ORDERBY;//=> 0
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getTITLE() {
		return TITLE;
	}
	public void setTITLE(String tITLE) {
		TITLE = tITLE;
	}
	public String getSTATUS() {
		return STATUS;
	}
	public void setSTATUS(String sTATUS) {
		STATUS = sTATUS;
	}
	public String getUPDATETIME() {
		return UPDATETIME;
	}
	public void setUPDATETIME(String uPDATETIME) {
		UPDATETIME = uPDATETIME;
	}
	public String getORDERBY() {
		return ORDERBY;
	}
	public void setORDERBY(String oRDERBY) {
		ORDERBY = oRDERBY;
	}
	
}
