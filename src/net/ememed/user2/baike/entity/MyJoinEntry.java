package net.ememed.user2.baike.entity;

import net.ememed.user2.entity.BaseResponseEntry;

public class MyJoinEntry extends BaseResponseEntry{
	private MyJoinInfo data;

	public MyJoinInfo getData() {
		return data;
	}

	public void setData(MyJoinInfo data) {
		this.data = data;
	}
	
}
