package net.ememed.user2.entity;

import java.io.Serializable;

public class ScheduleItem implements Serializable{

	private String Appointmentime;// => 09:18-09:23
	private String appointmentID;// => 7510551;2014-08-22;1;09:18-09:23;9;8;033204
	private String pay_fee;//
	public String getAppointmentime() {
		return Appointmentime;
	}
	public void setAppointmentime(String appointmentime) {
		Appointmentime = appointmentime;
	}
	public String getAppointmentID() {
		return appointmentID;
	}
	public void setAppointmentID(String appointmentID) {
		this.appointmentID = appointmentID;
	}
	public String getPay_fee() {
		return pay_fee;
	}
	public void setPay_fee(String pay_fee) {
		this.pay_fee = pay_fee;
	}
	
}
