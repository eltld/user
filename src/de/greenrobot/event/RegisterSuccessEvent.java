package de.greenrobot.event;

import net.ememed.user2.entity.MemberInfoEntity;


/**
 * Created by kevintanhongann on 11/27/13.
 */
public class RegisterSuccessEvent {

	private MemberInfoEntity data;
	
	public RegisterSuccessEvent() {
    }

	public MemberInfoEntity getData() {
		return data;
	}

	public void setData(MemberInfoEntity data) {
		this.data = data;
	}
}
