package net.ememed.user2.entity;

import java.io.Serializable;

public class Drug implements Serializable{
	private static final long serialVersionUID = 8175400992324698667L;
	private int id;
	private String sku;
	private String cnName;
	private String price;
	private String yhPrice;
	private String picPath;
	private String function;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public String getSku() {
		return sku;
	}
	public void setSku(String sku) {
		this.sku = sku;
	}
	public String getCnName() {
		return cnName;
	}
	public void setCnName(String cnName) {
		this.cnName = cnName;
	}
	
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getYhPrice() {
		return yhPrice;
	}
	public void setYhPrice(String yhPrice) {
		this.yhPrice = yhPrice;
	}
	public String getPicPath() {
		return picPath;
	}
	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}
	public String getFunction() {
		return function;
	}
	public void setFunction(String function) {
		this.function = function;
	}
	
}
