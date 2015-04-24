package net.ememed.user2.entity;

import java.io.Serializable;

public class HospitalEntry implements Serializable{

    private String HOSPITALID;
    private String HOSPITALNAME;
    private String GRADE;
    private String HOSPITAL_DOCTOR_COUNT;
    private String CHANNEL;
    private String HAS_SCHEDULE;
    private HospitalItem HOSPITAL_INFO;
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
	public String getGRADE() {
		return GRADE;
	}
	public void setGRADE(String gRADE) {
		GRADE = gRADE;
	}
	public String getHOSPITAL_DOCTOR_COUNT() {
		return HOSPITAL_DOCTOR_COUNT;
	}
	public void setHOSPITAL_DOCTOR_COUNT(String hOSPITAL_DOCTOR_COUNT) {
		HOSPITAL_DOCTOR_COUNT = hOSPITAL_DOCTOR_COUNT;
	}
	public String getCHANNEL() {
		return CHANNEL;
	}
	public void setCHANNEL(String cHANNEL) {
		CHANNEL = cHANNEL;
	}
	public String getHAS_SCHEDULE() {
		return HAS_SCHEDULE;
	}
	public void setHAS_SCHEDULE(String hAS_SCHEDULE) {
		HAS_SCHEDULE = hAS_SCHEDULE;
	}
	public HospitalItem getHOSPITAL_INFO() {
		return HOSPITAL_INFO;
	}
	public void setHOSPITAL_INFO(HospitalItem hOSPITAL_INFO) {
		HOSPITAL_INFO = hOSPITAL_INFO;
	}
    
        
}
