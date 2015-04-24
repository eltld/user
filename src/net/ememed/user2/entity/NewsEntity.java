package net.ememed.user2.entity;

import java.io.Serializable;

/**
 */
public class NewsEntity implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 2995097411714860280L;
	private String errormsg; // => 1
    private int success; //=> 成功
    private int count;//=> 13
    private int pages; //=> 1
    private NewsContentEntry data;
    
	public String getErrormsg() {
		return errormsg;
	}
	public void setErrormsg(String errormsg) {
		this.errormsg = errormsg;
	}
	public int getSuccess() {
		return success;
	}
	public void setSuccess(int success) {
		this.success = success;
	}
	public NewsContentEntry getData() {
		return data;
	}
	public void setData(NewsContentEntry data) {
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
    
}
