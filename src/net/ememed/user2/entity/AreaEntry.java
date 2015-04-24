package net.ememed.user2.entity;

import java.util.List;

public class AreaEntry {
	private String ID;
	private String AREANAME;
	private List<CityEntry> CITY;
	private String ZHIXIA;
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getAREANAME() {
		return AREANAME;
	}
	public void setAREANAME(String aREANAME) {
		AREANAME = aREANAME;
	}
	public List<CityEntry> getCITY() {
		return CITY;
	}
	public void setCITY(List<CityEntry> cITY) {
		CITY = cITY;
	}
	public String getZHIXIA() {
		return ZHIXIA;
	}
	public void setZHIXIA(String zHIXIA) {
		ZHIXIA = zHIXIA;
	}
	
	
}
