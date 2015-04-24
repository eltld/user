package net.ememed.user2.entity;

import java.util.List;

public class HospitalsEntry {

	private String CITYID;
	private String CITYNANME;
	private String CITY_DOCTOR_COUNT;
	private List<HospitalEntry> HOSPITALS;
	public String getCITYID() {
		return CITYID;
	}
	public void setCITYID(String cITYID) {
		CITYID = cITYID;
	}
	public String getCITYNANME() {
		return CITYNANME;
	}
	public void setCITYNANME(String cITYNANME) {
		CITYNANME = cITYNANME;
	}
	public String getCITY_DOCTOR_COUNT() {
		return CITY_DOCTOR_COUNT;
	}
	public void setCITY_DOCTOR_COUNT(String cITY_DOCTOR_COUNT) {
		CITY_DOCTOR_COUNT = cITY_DOCTOR_COUNT;
	}
	public List<HospitalEntry> getHOSPITALS() {
		return HOSPITALS;
	}
	public void setHOSPITALS(List<HospitalEntry> hOSPITALS) {
		HOSPITALS = hOSPITALS;
	}
	
	
}
