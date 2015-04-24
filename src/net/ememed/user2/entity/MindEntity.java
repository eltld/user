package net.ememed.user2.entity;

public class MindEntity {

//	{"success":1,"errormsg":"\u9001\u5fc3\u610f\u6210\u529f","data":{"ORDERID":16966}}
	
	private String success;
	private String errormsg;
	private MindInfo data;
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
	public MindInfo getData() {
		return data;
	}
	public void setData(MindInfo data) {
		this.data = data;
	}
	
}


	
