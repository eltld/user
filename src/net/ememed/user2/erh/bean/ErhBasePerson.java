package net.ememed.user2.erh.bean;

public class ErhBasePerson {
	private int success;
	private String errormsg;
	private ErhBasePersonBean data;
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public ErhBasePersonBean getData() {
		return data;
	}
	public void setData(ErhBasePersonBean data) {
		this.data = data;
	}
	
}
