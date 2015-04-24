package net.ememed.user2.entity;

import com.google.gson.annotations.SerializedName;

public class BaseEntity {
	private Integer success;
	@SerializedName("errormsg")
	private String errorMessage;

	public Integer getSuccess() {
		return success;
	}

	public void setSuccess(Integer success) {
		this.success = success;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public boolean isSuccess() {
		return success != null && success == 1 ? true : false;
	}
}
