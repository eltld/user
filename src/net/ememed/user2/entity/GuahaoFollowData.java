package net.ememed.user2.entity;


public class GuahaoFollowData {

	private GuahaoFollowDataDoctorDetail doctorDetail;
	private GuahaoFollowDataHospitalInfo hospitalInfo;

	public GuahaoFollowDataDoctorDetail getDoctorDetail() {
		return doctorDetail;
	}

	public GuahaoFollowDataHospitalInfo getHospitalInfo() {
		return hospitalInfo;
	}

	public void setDoctorDetail(GuahaoFollowDataDoctorDetail doctorDetail) {
		this.doctorDetail = doctorDetail;
	}

	public void setHospitalInfo(GuahaoFollowDataHospitalInfo hospitalInfo) {
		this.hospitalInfo = hospitalInfo;
	}

}
