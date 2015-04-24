package net.ememed.user2.entity;

import java.util.List;

public class CityGuahaoEntry {
	
	private String ID;
	private String CITY_NAME;
	private List<AreaGuahaoEntry> AREA;
	
	public String getID() {
		return ID;
	}
	public void setID(String ID_) {
		ID = ID_;
	}
	
	public String getCITY_NAME() {
		return CITY_NAME;
	}
	public void setCITY_NAME(String CITY_NAME_) {
		CITY_NAME = CITY_NAME_;
	}
	
	public List<AreaGuahaoEntry> getAREA() {
		return AREA;
	}
	public void setAREA(List<AreaGuahaoEntry> AREA_) {
		AREA = AREA_;
	}
	
}
