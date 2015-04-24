package net.ememed.user2.medicalhistory.bean;


public class QuestionXQBean {
	private String success;
	private String errormsg;
	private QuestionXQDataBean data;
	public String getSuccess() {
		return success;
	}
	public String getErrormsg() {
		return errormsg;
	}
	public QuestionXQDataBean getData() {
		return data;
	}
	public void setSuccess(String success) {
		this.success = success;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public void setData(QuestionXQDataBean data) {
		this.data = data;
	}
	
}
