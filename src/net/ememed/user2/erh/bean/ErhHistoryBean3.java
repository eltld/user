package net.ememed.user2.erh.bean;

public class ErhHistoryBean3 {
	private int success;
	private String errormsg;
	private ErhDiseaseHistory3 data;

	public ErhDiseaseHistory3 getData() {
		return data;
	}
	public void setData(ErhDiseaseHistory3 data) {
		this.data = data;
	}
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
}
