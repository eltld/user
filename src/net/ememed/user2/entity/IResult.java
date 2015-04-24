package net.ememed.user2.entity;

public interface IResult {
	public static final int NET_ERROR=1000;
	public static final int DATA_ERROR=1;
	public static final int RESULT = 2;
	public static final int END = 3;
	public static final int ERROR = 4;
	public static final int NEWS_LIST = 5;
	
	public static final int PHONE_VERIFY_CODE = 6;
	public static final int FAILURE = 7;
	public static final int LOGON_ERROR = 8;
	public static final int CHECK_VERSION = 9;
	public static final int CONTACT_LIST = 10;

	public static final int BASE_PROFILE = 20;
	public static final int GUAHAO_PROFILE = 21;
	public static final int GET_USER = 22;
	public static final int GET_VALI_CODE = 23;
	public static final int RESET_PWD = 24;
	public static final int SYSN_CONFIG = 25;
	public static final int USER_BALANCE = 26;
	public static final int UPLOAD_IMG = 27;
	
	public static final int TEST = 28;
	public static final int GET_CITY_DATA = 29;
	public static final int GET_DEPARTMENT_DATA = 30;
	public static final int SEARCH_DOCTOR = 31;
	public static final int LOGIN = 32;
	public static final int UPLOAD_PIC = 32;
	
	
	
	public static final int ORDER_LIST = 40;
	public static final int CALL_BACK = 41;
	public static final int PAY = 42;
	public static final int ADD_SINGLE_ORDER = 43;
	public static final int SEEKHELP_PIC = 44;
	public static final int MEMBER_INFO = 45;
	public static final int ORDER_INFO = 46;
	public static final int UN_LOGIN = 47;
	public static final int LOGOUT = 48;
	public static final int OTHER_LOGIN = 49;
	public static final int SEND_SYS_MSG = 50;
	public static final int SERVICE_EVALUATE = 51;
	public static final int USER_INFO = 52;
	public static final int BUY_SERVICE = 53;
	public static final int IM_LIST = 54;
	public static final int REGISTER_TO_IM = 55;
	public static final int PROBLEM_LIST = 56;
	public static final int PAGE_COUNT = 57;
	public static final int HOSP = 58;
	public static final int SYNC_CONFIG = 59;
	public static final int GET_AREA_LIST = 60;
	public static final int GET_ROOM_LIST = 61;
	public static final int GET_GUAHAO_DOCTOR = 65;
	public static final int GET_DOCTOR_SCHEDULE = 66;
	public static final int PATIENT_LIST = 62;
	public static final int PATIENT_ADD = 63;
	public static final int MEDICAL_CARD_ADD = 64;
	public static final int MEDICAL_CARD_LIST = 65;
	public static final int GET_DOCTOR_SCHEDULE_DETAIL = 69;
	public static final int USER_GUAHAO = 67;
	public static final int GET_USER_GUAHAO_RECORD = 68;
	public static final int EDIT_CUREDMEMEBER = 70;
	public static final int EDIT_CURDCARD = 71;
	public static final int CANCEL_GUAHAO = 72;
	public static final int GET_HOME_ADS = 73;
	public static final int ADD_NEWS_COMMENT = 74;
	public static final int GET_HOSP_DATA = 75;
	
	public static final int AFFIG_DATA = 76;
	public static final int DELETE_DATA = 77;
	
	public static final int GET_GUAHAO_RECOMMEND_DOCTOR = 78;
	
	public static final int GET_OTHER_SERVICE = 79;
	
	public static final int GET_USER_BALANCE = 80;
	public static final int ADD_CHARGE_ORDER = 81;
	
	//消息中心相关
	public static final int GET_MESSGE_CLASSSIFY = 82;	//获取短信分类
	public static final int GET_MESSGE_LIST = 83;	//获取短信列表
	public static final int DELETE_MESSAGE = 84;	//获取短信列表
	public static final int MARK_UNREAD_MSG = 85;	//标记所有消息为已读
	public static final int ERROR_MARK_UNREAD_MSG = 86;	//标记所有消息为已读失败
	public static final int GET_MESSAGE_SETTING = 87;	//获取短信开关设置
	public static final int SET_MESSAGE_SETTING = 88;	//设置短信开关
	
	//新版挂号
	public static final int NEW_GUAHAO_GET_AREA_PROVINCE = 89;	//获取省级列表
	public static final int NEW_GUAHAO_GET_AREA_CITY = 90;	//获取市级列表
	public static final int NEW_GUAHAO_GET_AREA = 91;	//获取市级列表
	public static final int NEW_GUAHAO_GET_HOT_DOCTOR = 92;	//获取市级列表
	public static final int NEW_GUAHAO_GET_HOSPITAL = 93;	//获取医院列表
	public static final int NEW_GUAHAO_GET_ROOMS = 94;	//获取科室列表
	public static final int NEW_GUAHAO_GET_IS_ATTENTION = 95;	//获取是否已关注的状态
	public static final int NEW_GUAHAO_SET_ATTENTION_OR_CANCEL = 96;	//设置关注或者取消关注
	public static final int NEW_GUAHAO_GET_DOCTOR = 97;	//获取医生列表
	public static final int NEW_GUAHAO_GET_ATTENTION_DOCTOR = 98;	//获取关注的医生
	public static final int NEW_GUAHAO_GET_ATTENTION_HOSPITAL = 99;	//获取关注的医院
	
	//百科
	public static final int BAIKE_ADD_ATTENTION = 200;	//添加关注
	public static final int BAIKE_CANCEL_ATTENTION = 201;	//取消关注
	public static final int GET_SHUOSHUO_LIST = 202;	//获取说说列表
	public static final int GIVE_PRAISE_INNER = 203;	//点赞(内部指令)
	public static final int GIVE_PRAISE = 204;	//点赞
	public static final int COMMENT_OTHER = 205;	//评论
	public static final int DELETE_COMMENT_INNER = 206;	//删除评论（内部指令）
	public static final int DELETE_COMMENT = 207;	//删除评论
	public static final int GET_SAYS_DETAIL = 208;	//获取说说详情
	public static final int COMMENT_SHUOSHUO = 209;	//发表评论
	public static final int SET_SHARE = 210;		//分享
	public static final int SET_BOUNTY = 211;		//新增打赏订单
	public static final int GET_BOUNTY_LIST = 212;		//打赏列表
	public static final int GET_DOCTOR_ATTENTION = 213;		//医生关注列表
	public static final int GET_DOCTOR_FANS = 214;		//医生粉丝列表
	public static final int GET_MY_RELATED = 215;		//我点赞的，我评论的，我分享的相关信息
	public static final int GET_MY_JOIN = 216;		//获取我参与过的
	
	//修改手机号
	public static final int ALERT_MOBILE = 300;
	
}
