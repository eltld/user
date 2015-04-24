package net.ememed.user2.entity;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

public class BindInfo {
	private String success;
	private String errormsg;
	private BindEntry data;
	public int getSuccess() {
		try {
			return Integer.parseInt(success);
		} catch (NumberFormatException e) {
			return 0 ;
		}
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
	public BindEntry getData() {
		return data;
	}
	public void setData(BindEntry data) {
		this.data = data;
	}


}
