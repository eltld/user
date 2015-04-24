package net.ememed.user2.entity;

public class OrderInfo {
	private String OrderID;
	private String UserID;
	private String DoctorID;
	private int PayStatus;
	public String getOrderID() {
		return OrderID;
	}
	public void setOrderID(String orderID) {
		OrderID = orderID;
	}
	public String getUserID() {
		return UserID;
	}
	public void setUserID(String userID) {
		UserID = userID;
	}
	public String getDoctorID() {
		return DoctorID;
	}
	public void setDoctorID(String doctorID) {
		DoctorID = doctorID;
	}
	public int getPayStatus() {
		return PayStatus;
	}
	public void setPayStatus(int payStatus) {
		PayStatus = payStatus;
	}
	

}
