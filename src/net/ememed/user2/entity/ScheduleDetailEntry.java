package net.ememed.user2.entity;

import java.io.Serializable;

public class ScheduleDetailEntry implements Serializable {

	private String doctor_code;// => 205869
	private String doctor_name;// => 黄彩彩
	private String fee;// => 7
//	private String prepaid;// => 1
	private String schedule_Date;// => 2014-08-23
	private String am_Pm;// => 上午
	private String schedule_State;// => 1
	private String resource_Code;// =>
									// 7510416;2014-08-23;1;08:18-08:23;7;6;033204
	private String appoint_Period;// => 08:18-08:23
//	private String serial_No;// => Array
//	private String available;// => 1
	public String getDoctor_code() {
		return doctor_code;
	}
	public void setDoctor_code(String doctor_code) {
		this.doctor_code = doctor_code;
	}
	public String getDoctor_name() {
		return doctor_name;
	}
	public void setDoctor_name(String doctor_name) {
		this.doctor_name = doctor_name;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
//	public String getPrepaid() {
//		return prepaid;
//	}
//	public void setPrepaid(String prepaid) {
//		this.prepaid = prepaid;
//	}
	public String getSchedule_Date() {
		return schedule_Date;
	}
	public void setSchedule_Date(String schedule_Date) {
		this.schedule_Date = schedule_Date;
	}
	public String getAm_Pm() {
		return am_Pm;
	}
	public void setAm_Pm(String am_Pm) {
		this.am_Pm = am_Pm;
	}
	public String getSchedule_State() {
		return schedule_State;
	}
	public void setSchedule_State(String schedule_State) {
		this.schedule_State = schedule_State;
	}
	public String getResource_Code() {
		return resource_Code;
	}
	public void setResource_Code(String resource_Code) {
		this.resource_Code = resource_Code;
	}
	public String getAppoint_Period() {
		return appoint_Period;
	}
	public void setAppoint_Period(String appoint_Period) {
		this.appoint_Period = appoint_Period;
	}
//	public String getSerial_No() {
//		return serial_No;
//	}
//	public void setSerial_No(String serial_No) {
//		this.serial_No = serial_No;
//	}
//	public String getAvailable() {
//		return available;
//	}
//	public void setAvailable(String available) {
//		this.available = available;
//	}
//	
	
}
