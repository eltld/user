package net.ememed.user2.wxpay;

public class Constants {

	public static final String NOTIFY_URL = "http://www.ememed.net:8004/normal/wxpay/donotify/";// 通知服务器接口

	// APP_ID 替换为你的应用从官方网站申请到的合法appId
	public static final String APP_ID = "wxb7d72c1505c92eea";

	/**
	 * 微信开放平台和商户约定的密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	 */
	public static final String APP_SECRET = "7a2ac284b9a34dbf087bf606ad932ddb"; // wxdd74dd1804ef62bc 对应的密钥

	/** 商家向财付通申请的商家id */
	public static final String PARTNER_ID = "1229244401";

	/**
	 * 微信公众平台商户模块和商户约定的密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genPackage这个过程由服务器端完成
	 */
	public static final String PARTNER_KEY = "681cedebadaf722cc5de31f9c64109ae";

	/**
	 * 微信开放平台和商户约定的支付密钥
	 * 
	 * 注意：不能hardcode在客户端，建议genSign这个过程由服务器端完成
	 */
	public static final String APP_KEY = "NPldLugbDlYUZsSMswflZslRCbMNLeDl58L9LPHfYkQwjb5lkUq8dueYeSTat78SBgdYR8pNeUd4N9Ip8rbZknJaBOCu8rVhV4SEFRdb0YnC4wIpHbVFD4UD6Au9BMcn"; // wxd930ea5d5a258f4f

}
