package net.ememed.user2.entity;

import java.util.List;

public class GuahaoFollowDoctorList {

	private String doctorCd;
	private Integer hasFree;
	private DoctorInfo4 doctorInfo;
	private List<Schedules> schedules;

	public String getDoctorCd() {
		return doctorCd;
	}

	public boolean getHasFree() {
		return hasFree == null || hasFree == 0 ? false : true;
	}

	public DoctorInfo4 getDoctorInfo() {
		return doctorInfo;
	}

	public List<Schedules> getSchedules() {
		return schedules;
	}

}
