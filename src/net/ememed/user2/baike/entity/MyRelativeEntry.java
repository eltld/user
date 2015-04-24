package net.ememed.user2.baike.entity;

import java.util.List;

/**
 * 我赞的，我评论的，我分享的 相关参数 
 * @author Administrator
 * 
 */
public class MyRelativeEntry {
	
	private int success;
	private String errormsg;
	private int count; // => 20
	private int pages; // => 1
	private List<MyRelativeInfo> data;
	
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
	public List<MyRelativeInfo> getData() {
		return data;
	}
	public void setData(List<MyRelativeInfo> data) {
		this.data = data;
	}
}
