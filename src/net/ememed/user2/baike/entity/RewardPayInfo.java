package net.ememed.user2.baike.entity;

public class RewardPayInfo {
	  private String OrderNo;  //"2015021079235",
	  private String out_user;//": "105150",
	  private String total_fee;//": "5",
	  private String subject;//": "打赏"
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
