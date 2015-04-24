package net.ememed.user2.baike.entity;

import java.io.Serializable;

public class DoctorFansInfo implements Serializable{
	private String IS_MY_FANS; 
	private int FANS_NUM;
	private int ATTENTION_NUM;
	
	
	public int getATTENTION_NUM() {
		return ATTENTION_NUM;
	}
	public void setATTENTION_NUM(int aTTENTION_NUM) {
		ATTENTION_NUM = aTTENTION_NUM;
	}
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
}
