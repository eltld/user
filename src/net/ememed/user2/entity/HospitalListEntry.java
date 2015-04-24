package net.ememed.user2.entity;

import java.util.List;

public class HospitalListEntry {

	private int CITYID;
	private String CITYNANME;
	private List<HospitalItemEntry> HOSPITALS;
	public int getCITYID() {
		return CITYID;
	}
	public void setCITYID(int cITYID) {
		CITYID = cITYID;
	}
	public List<HospitalItemEntry> getHOSPITALS() {
		return HOSPITALS;
	}
	public void setHOSPITALS(List<HospitalItemEntry> hOSPITALS) {
		HOSPITALS = hOSPITALS;
	}
	public String getCITYNANME() {
		return CITYNANME;
	}
	public void setCITYNANME(String cITYNANME) {
		CITYNANME = cITYNANME;
	}
	
	
}
