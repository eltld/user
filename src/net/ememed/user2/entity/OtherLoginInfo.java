package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class OtherLoginInfo  implements Serializable{

	private int success;	
	private String errormsg;
	private OtherLoginEntry data;
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
	public OtherLoginEntry getData() {
		return data;
	}
	public void setData(OtherLoginEntry data) {
		this.data = data;
	}

	
	
}
