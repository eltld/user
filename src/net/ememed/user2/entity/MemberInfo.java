package net.ememed.user2.entity;

import java.io.Serializable;

public class MemberInfo  implements Serializable{
	private int success;
	private String errormsg;
	private MemberInfoEntity data;

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

	public MemberInfoEntity getData() {
		return data;
	}

	public void setData(MemberInfoEntity data) {
		this.data = data;
	}

}
