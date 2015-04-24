package net.ememed.user2.entity;

import java.util.List;

public class SearchGuahao {
	private String doctorCd;
	private DoctorInfo4 doctorInfo;
	private String hasFree;
	private List<Schedules> schedules;
	private List<SchedulesShow> schedules_show;
	public String getDoctorCd() {
		return doctorCd;
	}
	public DoctorInfo4 getDoctorInfo() {
		return doctorInfo;
	}
	public String getHasFree() {
		return hasFree;
	}
	public List<Schedules> getSchedules() {
		return schedules;
	}
	public List<SchedulesShow> getSchedules_show() {
		return schedules_show;
	}
	public void setDoctorCd(String doctorCd) {
		this.doctorCd = doctorCd;
	}
	public void setDoctorInfo(DoctorInfo4 doctorInfo) {
		this.doctorInfo = doctorInfo;
	}
	public void setHasFree(String hasFree) {
		this.hasFree = hasFree;
	}
	public void setSchedules(List<Schedules> schedules) {
		this.schedules = schedules;
	}
	public void setSchedules_show(List<SchedulesShow> schedules_show) {
		this.schedules_show = schedules_show;
	}
	
}
