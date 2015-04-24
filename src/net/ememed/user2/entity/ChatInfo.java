package net.ememed.user2.entity;

import java.util.List;
import java.util.Map;

public class ChatInfo {
	
	private List<ChatEntry> list;
	private Map<String, ImUserInfo> userinfo;
	

	public List<ChatEntry> getList() {
		return list;
	}

	public void setList(List<ChatEntry> list) {
		this.list = list;
	}


	public Map<String, ImUserInfo> getUserinfo() {
		return userinfo;
	}

	public void setUserinfo(Map<String, ImUserInfo> userinfo) {
		this.userinfo = userinfo;
	}


	
	
	
	
}
