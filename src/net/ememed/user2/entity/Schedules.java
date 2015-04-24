package net.ememed.user2.entity;

public class Schedules {
	private String amOrPm;
    private String doctorCd;
    private String freeNum;
    private String returnMsg;
    private String scheduleDate;
    private String scheduleState;
	public String getAmOrPm() {
		return amOrPm;
	}
	public String getDoctorCd() {
		return doctorCd;
	}
	public String getFreeNum() {
		return freeNum;
	}
	public String getReturnMsg() {
		return returnMsg;
	}
	public String getScheduleDate() {
		return scheduleDate;
	}
	public String getScheduleState() {
		return scheduleState;
	}
	public void setAmOrPm(String amOrPm) {
		this.amOrPm = amOrPm;
	}
	public void setDoctorCd(String doctorCd) {
		this.doctorCd = doctorCd;
	}
	public void setFreeNum(String freeNum) {
		this.freeNum = freeNum;
	}
	public void setReturnMsg(String returnMsg) {
		this.returnMsg = returnMsg;
	}
	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}
	public void setScheduleState(String scheduleState) {
		this.scheduleState = scheduleState;
	}
}
