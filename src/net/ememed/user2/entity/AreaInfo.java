package net.ememed.user2.entity;

import java.io.Serializable;
import java.util.List;

public class AreaInfo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 5306319512189864744L;
	private int success;	
	private String errormsg;
	private List<AreaEntry> data;
	
	public AreaInfo() {
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

	public List<AreaEntry> getData() {
		return data;
	}

	public void setData(List<AreaEntry> data) {
		this.data = data;
	}

}
