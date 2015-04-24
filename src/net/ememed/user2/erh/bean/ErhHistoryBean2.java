package net.ememed.user2.erh.bean;

public class ErhHistoryBean2 {
	private int success;
	private String errormsg;
	private ErhDiseaseHistory2 data;

	public ErhDiseaseHistory2 getData() {
		return data;
	}
	public void setData(ErhDiseaseHistory2 data) {
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
