package net.ememed.user2.entity;

import java.util.List;

import net.ememed.user2.baike.entity.DoctorBountyListInfo;

public class DoctorPraiseEntity {

	private String success;
	private String errormsg;
	private String count;
	private String pages;
	private DoctorPraisinfo doctorinfo;
	private List<DoctorPraise> data;
	public String getSuccess() {
		return success;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getPages() {
		return pages;
	}
	public void setPages(String pages) {
		this.pages = pages;
	}
	public DoctorPraisinfo getDoctorinfo() {
		return doctorinfo;
	}
	public void setDoctorinfo(DoctorPraisinfo doctorinfo) {
		this.doctorinfo = doctorinfo;
	}
	public List<DoctorPraise> getData() {
		return data;
	}
	public void setData(List<DoctorPraise> data) {
		this.data = data;
	}
	
	
}
