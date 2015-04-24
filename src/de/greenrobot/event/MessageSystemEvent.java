package de.greenrobot.event;

import net.ememed.user2.entity.MessageBackupFieldEntry;
import net.ememed.user2.entity.MessageSystemEntry;

/**
 * 用于发起服务时 ，通知聊天框自动触发系统消息 
 * @author chen
 */
public class MessageSystemEvent {
	public enum ServiceType {
		/**预约通话*/
		SERVICE_PHONE,
		/**文字咨询*/
		SERVICE_TEXT,
		/**上门*/
		SERVICE_SHANGMEN,
		/**预约加号*/
		SERVICE_JIAHAO,
		/**住院*/
		SERVICE_ZHUYUAN,
		/**自定义服务*/
		SERVICE_CUSTOM,
		/**签约私人医生*/
		SERVICE_PRIVATE_DOCTOR,
	}
	
	private ServiceType type;
	private MessageBackupFieldEntry msgEntry;
	private MessageSystemEntry msgSysEntry; 
	
	public MessageSystemEvent(ServiceType serviceType,MessageBackupFieldEntry msgEntry,MessageSystemEntry msgSysEntry){
		this.type = serviceType;
		this.msgSysEntry = msgSysEntry;
		this.setMsgEntry(msgEntry);
	}
	
	public ServiceType getType() {
		return type;
	}
	public void setType(ServiceType type) {
		this.type = type;
	}

	public MessageBackupFieldEntry getMsgEntry() {
		return msgEntry;
	}

	public void setMsgEntry(MessageBackupFieldEntry msgEntry) {
		this.msgEntry = msgEntry;
	}

	public MessageSystemEntry getMsgSysEntry() {
		return msgSysEntry;
	}

	public void setMsgSysEntry(MessageSystemEntry msgSysEntry) {
		this.msgSysEntry = msgSysEntry;
	}
}
