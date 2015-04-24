package net.ememed.user2.entity;

public class GuahaoSuccessInfo {

	
	private int success;
	private String errormsg;
	private String count;
	private GuahaoSuccessEntry data;
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
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public GuahaoSuccessEntry getData() {
		return data;
	}
	public void setData(GuahaoSuccessEntry data) {
		this.data = data;
	}
	
	
}
