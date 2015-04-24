package net.ememed.user2.entity;

import java.io.Serializable;

public class DrugClass implements Serializable {
	private static final long serialVersionUID = -5858274512384756736L;
	private String scId;
	private String parentId;
	private String title;
	private int existSub;

	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getExistSub() {
		return existSub;
	}

	public void setExistSub(int existSub) {
		this.existSub = existSub;
	}

	public String getScId() {
		return scId;
	}

	public void setScId(String scId) {
		this.scId = scId;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

}
