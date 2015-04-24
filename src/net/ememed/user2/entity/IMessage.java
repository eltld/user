package net.ememed.user2.entity;

public interface IMessage {
	public static final String NET_ERROR="网络异常,请检查您的网络设置";
//	public static final String DATA_ERROR="暂无信息";
	public static final String DATA_ERROR="网络数据加载异常";//chenhj-2014-08-28修改
	public static final String SERVICE_ERROR="服务异常，请稍后再试";
	public static final String LOADING = "努力加载中...";
	public static final String LOGINING = "正在登录...";
	public static final String SEARCHING = "正在搜索...";
	public static final String DELETEING = "删除中...";
	public static final String ADDING = "添加中...";
	public static final String CHECKING_PAY_STATUS = "更新订单状态";
	public static final String CHECKING_BACK_RESULT = "查询结果，请稍候..";
	
	//llj 2014-11-17添加
	public static final String HOSPITAL_DATA_ERROR = "抱歉,未能找到您想预约的医院";
	public static final String ROOM_DATA_ERROR = "          亲、真抱歉,\n未能找到您想预约的科室";
	public static final String DOCTOR_DATA_ERROR = "          亲、真抱歉,\n未能找到您想预约的医生";
}
