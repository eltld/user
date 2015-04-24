package net.ememed.user2.entity;

public class AlipayEntry {
	private String OrderNo;
	private String out_user;
	private String total_fee;
	private String subject;
	public String getOrderNo() {
		return OrderNo;
	}
	public void setOrderNo(String orderNo) {
		OrderNo = orderNo;
	}
	public String getOut_user() {
		return out_user;
	}
	public void setOut_user(String out_user) {
		this.out_user = out_user;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	
}
