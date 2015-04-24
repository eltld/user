package net.ememed.user2.entity;
/**
 * 发送系统消息
 * @author chen
 *
 */
public class MessageSystemEntry {
	private String user_msg;
	private String doctor_msg;
	private String all;
	public String getUser_msg() {
		return user_msg;
	}
	public void setUser_msg(String user_msg) {
		this.user_msg = user_msg;
	}
	public String getDoctor_msg() {
		return doctor_msg;
	}
	public void setDoctor_msg(String doctor_msg) {
		this.doctor_msg = doctor_msg;
	}
	public String getAll() {
		return all;
	}
	public void setAll(String all) {
		this.all = all;
	}
	
}
