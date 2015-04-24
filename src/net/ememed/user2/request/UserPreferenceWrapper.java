package net.ememed.user2.request;

import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;

/**
 * 封装用户SharePreference数据请求
 * 
 * @author pch
 * 
 */
public class UserPreferenceWrapper {

	private static final String FORMAT_ACCESS_KEY = "accesskey=%s#%s#%s; Path=/; HttpOnly";

	public static boolean isLogin() {
		return SharePrefUtil.getBoolean(Conast.LOGIN);
	}

	public static String getAccessKey() {
		if (!isLogin()) {
			throw new IllegalArgumentException("the user is not login");
		}
		return String.format(FORMAT_ACCESS_KEY, SharePrefUtil.getString(Conast.USER_TYPE),
				SharePrefUtil.getString(Conast.MEMBER_ID),
				SharePrefUtil.getString(Conast.ACCESS_TOKEN));
	}

	public static void setUserBalance(String balance) {
		SharePrefUtil.putString(Conast.USER_BALANCE, balance);
		SharePrefUtil.commit();
	}

	public static String getUserBalance() {
		return SharePrefUtil.getString(Conast.USER_BALANCE);
	}

	public static void clearUserBalance() {
		setUserBalance("0");
	}

	public static void setMyPraiseCount(int count) {
		SharePrefUtil.putInt(Conast.MY_PRAISE_COUNT, count);
		SharePrefUtil.commit();
	}

	public static int getMyPraiseCount() {
		return SharePrefUtil.getInt(Conast.MY_PRAISE_COUNT);
	}

	public static void clearMyPraiseCount() {
		setMyPraiseCount(0);
	}

	public static void addMyPraiseCount() {
		int count = SharePrefUtil.getInt(Conast.MY_PRAISE_COUNT) + 1;
		SharePrefUtil.putInt(Conast.MY_PRAISE_COUNT, count);
		SharePrefUtil.commit();
	}

	private static void subMyPraiseCount() {
		int count = SharePrefUtil.getInt(Conast.MY_PRAISE_COUNT) - 1;
		SharePrefUtil.putInt(Conast.MY_PRAISE_COUNT, count);
		SharePrefUtil.commit();
	}

	public static void setMyCommentCount(int count) {
		SharePrefUtil.putInt(Conast.MY_COMMENT_COUNT, count);
		SharePrefUtil.commit();
	}

	public static void addMyCommentCount() {
		int count = SharePrefUtil.getInt(Conast.MY_COMMENT_COUNT) + 1;
		SharePrefUtil.putInt(Conast.MY_COMMENT_COUNT, count);
		SharePrefUtil.commit();
	}

	public static void subMyCommentCount() {
		int count = SharePrefUtil.getInt(Conast.MY_COMMENT_COUNT) - 1;
		SharePrefUtil.putInt(Conast.MY_COMMENT_COUNT, count);
		SharePrefUtil.commit();
	}

	public static int getMyCommentCount() {
		return SharePrefUtil.getInt(Conast.MY_COMMENT_COUNT);
	}

	public static void clearMyCommentCount() {
		setMyCommentCount(0);
	}

	public static void setMyShareCount(int count) {
		SharePrefUtil.putInt(Conast.MY_SHARE_COUNT, count);
		SharePrefUtil.commit();
	}

	public static int getMyShareCount() {
		return SharePrefUtil.getInt(Conast.MY_SHARE_COUNT);
	}

	public static void clearMyShareCount() {
		setMyCommentCount(0);
	}

	public static void addMyShareCount() {
		int count = SharePrefUtil.getInt(Conast.MY_SHARE_COUNT) + 1;
		SharePrefUtil.putInt(Conast.MY_SHARE_COUNT, count);
		SharePrefUtil.commit();
	}

	private static void subMyShareCount() {
		int count = SharePrefUtil.getInt(Conast.MY_SHARE_COUNT) - 1;
		SharePrefUtil.putInt(Conast.MY_SHARE_COUNT, count);
		SharePrefUtil.commit();
	}
	
	public static void setUserType(String type) {
		SharePrefUtil.putString(Conast.USER_TYPE, type);
		SharePrefUtil.commit();
	}
	
	public static String getUserType() {
		return SharePrefUtil.getString(Conast.USER_TYPE);
	}
	
	public static void clearUserType() {
		setUserType("");
	}

}
