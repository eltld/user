package net.ememed.user2.entity;

public class HospitalItemEntry {

	private String HOSPITALID;
	private String HOSPITALNAME;
	private String GRADE;
	private String HAS_SCHEDULE;
	public String getGRADE() {
		return GRADE;
	}
	public void setGRADE(String gRADE) {
		GRADE = gRADE;
	}
	public String getHOSPITALID() {
		return HOSPITALID;
	}
	public void setHOSPITALID(String hOSPITALID) {
		HOSPITALID = hOSPITALID;
	}
	public String getHOSPITALNAME() {
		return HOSPITALNAME;
	}
	public void setHOSPITALNAME(String hOSPITALNAME) {
		HOSPITALNAME = hOSPITALNAME;
	}
	public String getHAS_SCHEDULE() {
		return HAS_SCHEDULE;
	}
	public void setHAS_SCHEDULE(String hAS_SCHEDULE) {
		HAS_SCHEDULE = hAS_SCHEDULE;
	}
	
}
