package net.ememed.user2.entity;

public class CancelGuahaoEntry {
	private int success;
	private String error;
	private CancelGuahaoItem data;

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public CancelGuahaoItem getData() {
		return data;
	}

	public void setData(CancelGuahaoItem data) {
		this.data = data;
	}

}
