package net.ememed.user2.entity;

import java.io.Serializable;

import android.text.TextUtils;

public class DoctorFansInfo implements Serializable{
    private String IS_MY_FANS;
    private int FANS_NUM;
 	private int attention_total_num;
     
	public String getIS_MY_FANS() {
		return IS_MY_FANS;
	}

	public void setIS_MY_FANS(String iS_MY_FANS) {
		IS_MY_FANS = iS_MY_FANS;
	}

	public int getFANS_NUM() {
		return FANS_NUM;
	}

	public void setFANS_NUM(int fANS_NUM) {
		FANS_NUM = fANS_NUM;
	}

	public boolean isFollow() {
		return (!TextUtils.isEmpty(IS_MY_FANS) && IS_MY_FANS.equals("1")) ? true : false;
	}

	public int getAttention_total_num() {
		return attention_total_num;
	}
	public void setAttention_total_num(int attention_total_num) {
		this.attention_total_num = attention_total_num;
	}

}
