package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

public class GuahaoDoctorInfo implements Serializable {

	private String doctorcode;
	private GuahaoDoctotItem doctor_info;
	private List<ScheduleTime> schedule;
	
	public void setDoctorcode(String doctorcode) {
		this.doctorcode = doctorcode;
	}
	public void setDoctor_info(GuahaoDoctotItem doctor_info) {
		this.doctor_info = doctor_info;
	}
	public void setSchedule(List<ScheduleTime> schedule) {
		this.schedule = schedule;
	}
	public String getDoctorcode() {
		return doctorcode;
	}
	public GuahaoDoctotItem getDoctor_info() {
		return doctor_info;
	}
	public List<ScheduleTime> getSchedule() {
		return schedule;
	}
	
	
}
