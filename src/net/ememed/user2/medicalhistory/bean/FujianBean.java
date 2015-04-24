package net.ememed.user2.medicalhistory.bean;

import java.io.Serializable;

public class FujianBean implements Serializable {
	private String attachment_ids;
	private String description;
	
	public String getAttachment_ids() {
		return attachment_ids;
	}
	public void setAttachment_ids(String attachment_ids) {
		this.attachment_ids = attachment_ids;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	
}
