package net.ememed.user2.entity;

import java.util.List;

public class OrderListEntity {
	private int success;
	private String errormsg;
	private List<OrderListEntry> data;
	private List<QuestionOrderErtry> question_data;
	private int count;
	private int pages;
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
	public List<OrderListEntry> getData() {
		return data;
	}
	public void setData(List<OrderListEntry> data) {
		this.data = data;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public List<QuestionOrderErtry> getQuestion_data() {
		return question_data;
	}
	public void setQuestion_data(List<QuestionOrderErtry> question_data) {
		this.question_data = question_data;
	}
	
}
 