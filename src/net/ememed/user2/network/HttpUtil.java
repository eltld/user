package net.ememed.user2.network;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import net.ememed.user2.util.ByteUtil;
import net.ememed.user2.util.StreamUtil;
import net.ememed.user2.util.TextUtil;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;

import android.text.TextUtils;

public class HttpUtil {
	public static final int GET = 1;
	public static final int POST = 2;
	// public static final String URI = "http://demo.uimed.net/user/";
//	public static final String URI = "http://183.60.177.178:8002";

//	public static final String URI =  "http://183.60.177.178:9004";//测试环境
//	public static final String URI =  "http://www.ememed.net:9004";//测试环境
	public static final String URI =  "http://www.ememed.net:8004";//正式环境
	
//	public static final String DRUG_URL = "http://172.16.0.63/drug3/";//用药助手入口,内网测试服务器
	public static final String DRUG_URL = "http://plus.ememed.net/drug";//用药助手入口,正式服务器

	public static final String URI_USER = URI + "/user/";
	public static final String PRODUCT_URI = "http://121.8.142.242:6677/android/ProductDetail.aspx?id=";
//	public static final String PAY_URI = "http://www.ememed.net:8002/pay/payorder/";
//	public static final String PAY_URI_new = "http://www.ememed.net:8002/pay/payordernew/";
//	public static final String SIGN = "http://183.60.177.178:8002/sign/index/";
//	public static final String SIGN_NEW = "http://183.60.177.178:8002/sign/sign_order/";

	public static final String URI_COMMON = URI + "/common/";
	public static final String ACCOUNT_USER = URI + "/account_user/";
	public static final String GET_HOME_ADS = URI + "/normal/user/get_home_ads";//获取用户版首界面广告
	public static final String ADD_NEWS_COMMENT = "/right/common/add_news_comment";
	
	public static final String get_version_info = "/normal/common/get_version_info";
	public static final String check_mobile_code = "/normal/common/check_mobile_code";
	public static final String get_static_variable="/normal/common/get_static_variable";  //服务介绍列表
	
	public static final String search_filter_doctor = "/normal/user/search_filter_doctor";
	public static final String get_news_list = "/normal/common/get_news_list";
	public static final String get_news_type = "/normal/common/get_news_type";//获取资讯类型列表
	public static final String search_doctor_xs = "/normal/user/search_doctor_xs";
	
	public static final String set_base_profile = "/right/user/set_base_profile";
	public static final String set_guahao_profile = "/right/user/set_guahao_profile";
	public static final String set_user_avatar = "/normal/user/set_user_avatar";
	public static final String get_user_info = "/right/user/get_user_info";
	public static final String set_user_password = "/right/user/set_user_password";
	
	public static final String register = "/normal/user/register";
	public static final String bindding_other_account = "/normal/user/bindding_other_account";
	public static final String new_phone_verify_code = "/normal/common/new_phone_verify_code";
	/**html返回*/
	public static final String NO_PRESS = URI+"/html/nopress.html";
	public static final String regClause = URI+"/html/userRegClause.html";
	
	public static final String get_memberid_by_tel = "/normal/common/get_memberid_by_tel";
	public static final String get_validate_code = "/normal/common/get_validate_code";
	public static final String reset_password = "/normal/common/reset_password";
	
	public static final String get_user_balance = "/right/user/get_user_balance";
	
	public static final String get_contact_list = "/right/user/get_contact_list";
	public static final String sync_config ="/normal/user/sync_config";//同步筛选医生基本配置
	public static final String get_all_area ="/normal/user/get_all_area";
	public static final String get_hospital_by_area ="/normal/user/get_hospital_by_area";
	public static final String login ="/normal/user/login";
	public static final String reg_to_im ="/right/common/reg_to_im";
	public static final String other_login ="/normal/user/other_login";
	public static final String ALERT_MOBILE = "/right/user/set_mobile";//修改用户手机号码
	
	public static final String get_member_info ="/right/user/get_member_info";
	public static final String get_member_info2 ="/normal/user/get_member_info";
	public static final String get_order_list ="/right/user/get_order_list";
	public static final String pay_set_order ="/right/user/pay_set_order";
	public static final String pay_callback ="/right/user/pay_callback";
	public static final String get_order_info ="/right/user/get_order_info";
	
	public static final String add_single_order ="/right/user/add_single_order";
	public static final String set_seekhelp_pic ="/normal/user/set_seekhelp_pic";
	public static final String logout ="/normal/user/logout";
	public static final String set_feedback = "/right/common/set_feedback";
	public static final String get_im_list = "/right/user/get_im_list";
	
	public static final String get_faq_list = "/normal/common/get_faq_list";
	
	public static final String user_evaluate_doctor_service ="/right/user/user_evaluate_doctor_service";
	public static final String packet_buy ="/right/user/packet_buy";
	public static final String guahao_sync_config ="/normal/guahao/sync_config";
	public static final String guahao_get_area_list ="/normal/guahao/get_area_list";
	public static final String guahao_get_hospital_list ="/normal/guahao/get_hospital_list";
	public static final String guahao_get_hospital_rooms ="/normal/guahao/get_hospital_rooms";
	public static final String get_guahao_doctor ="/normal/guahao/get_guahao_doctor";
	public static final String get_doctor_schedule_detail ="/normal/guahao/get_doctor_schedule_detail";
	public static final String get_doctor_schedule ="/normal/guahao/get_doctor_schedule";
	public static final String get_guahao_doctor_recommend = "/normal/guahao/get_guahao_doctor_recommend";
	
	public static final String get_curedmember_lists = "/normal/guahao/get_curedmember_lists";
	public static final String PATIENT_ADD = "/normal/guahao/add_curedmember";
	public static final String edit_curedmember = "/normal/guahao/edit_curedmember";
	public static final String MEDICAL_CARD_ADD = "/normal/guahao/add_curdcard";
	public static final String edit_curdcard = "/normal/guahao/edit_curdcard";

	public static final String get_user_guahao_record = "/normal/guahao/get_user_guahao_record";

	public static final String get_curdcard_lists = "/normal/guahao/get_curdcard_lists";
	public static final String user_guahao = "/normal/guahao/user_guahao";
	public static final String cancel_guahao = "/normal/guahao/cancel_guahao";
	
	//病历夹  
	public static final String add_bingli = "/right/medrecord/set_medrecord";
	public static final String add_bingli_att = "/right/medrecord/set_attachment_description";
	public static final String get_bingli_list = "/right/medrecord/get_medrecord_list";
	public static final String post_bingli_image = "/normal/user/set_medrecord_attachment";
	public static final String cancel_bingli = "/right/medrecord/del_medrecord";
	public static final String cancel_fujian = "/right/medrecord/del_attachment";
	//病史
	public static final String add_medical_history = "/right/medhistory/set_multiple_medhistory";
	public static final String get_medical_history = "/right/medhistory/get_multiple_medhistory";
	public static final String add_yaowu_history = "/right/medhistory/set_single_medhistory";
	public static final String get_yaowu_history = "/right/medhistory/get_single_medhistory";
	//个人基本信息
	public static final String post_base_person = "/right/user/set_base_profile";
	public static final String get_base_person = "/right/user/get_user_info";
	//删除病史
	public static final String delete_history_medical = "/right/medhistory/delete_multiple_medhistory";
	
	//获取第三方服务
	public static final String get_other_service_list = "/normal/user/get_third_service_list";//获取第3方服务
	
	//充值
	public static final String add_charge_order = "/right/user/add_charge_order";

	//搜索
	public static final String search_doctor_all = "/normal/user/search_doctor_all";
	public static final String search_doctor_person = "/normal/user/search_doctor_person";
	public static final String search_doctor_guahao = "/normal/user/search_doctor_guahao";
	public static final String search_doctor_hospital = "/normal/user/search_doctor_hospital";
	public static final String search_doctor_txtconsult = "/normal/user/search_doctor_txtconsult";

	//消息中心
	public static final String get_msg_pool_class = "/right/common/get_msg_pool_class";
	public static final String get_msg_pool_list = "/right/common/get_msg_pool_list";
	public static final String mark_unread_msg = "/right/common/edit_all_msg";
	public static final String delete_msg = "/right/common/delete_msg";
	public static final String get_message_setting = "/right/user/get_message_setting";
	public static final String set_message_setting = "/right/user/set_message_setting";
	
	//新版免费挂号
	public static final String GUAHAO =  "http://api.ememed.net:8086";//新版挂号环境
	public static final String GUAHAO_ACCESS_KEY =  "123456";
	public static final String new_guahao_get_area = "/hisCore/services/guahao/getArea";	//获取区域信息
	public static final String new_guahao_get_hot_doctor = "/hisCore/services/guahao/getHotDoctors";	//获取热门医生
	public static final String new_guahao_get_hospital = "/hisCore/services/guahao/getHospital";	//获取医院信息
	public static final String new_guahao_get_hospital_rooms = "/hisCore/services/guahao/getDepart";	//获取科室信息
	public static final String new_guahao_is_attention = "/hisCore/services/guahao/isAttention";	//判断是否已关注
	public static final String new_guahao_attention_or_cancel = "/hisCore/services/guahao/attentionOperate";	//关注或者取消关注
	public static final String new_guahao_get_doctor_schedule = "/hisCore/services/guahao/queryDoctorSchedule";	//获得医生列表（排班信息）
	public static final String GUAHAO_FOLLOW_LIST = "/hisCore/services/guahao/getAttention";	//获取关注的对象（医院或医生）
	
	//问题池
	public static final String set_question_pic = "/normal/question/set_question_pic";	//免费提问提交图片
	public static final String set_question = "/right/user_question/set_question";	//提交免费问题
	public static final String get_my_question_detail="/right/user_question/get_my_question_detail";   //单个问题详情
	
	public static final String sync_rooms_config="/normal/user/sync_rooms_config";
	

	
	//自媒体
	public static final String set_fans = "/right/user_baike/set_fans";	//添加关注
	public static final String cancel_fans = "/right/user_baike/cancel_fans";	//取消关注
	public static final String get_says_list = "/normal/baike/get_says_list_for_user";	//获取医生说说列表
	public static final String set_praise = "/right/user_baike/set_praise";	//说说点赞
	public static final String get_says_detail = "/normal/baike/get_says_detail_for_user";	//说说详情
	public static final String set_comment = "/right/user_baike/set_comment";	//发表评论
	public static final String delete_comment = "/right/user_baike/delete_comment";	//删除评论
	public static final String set_share = "/right/user_baike/set_share";	//分享统计
	public static final String set_bounty = "/right/user_baike/set_bounty";	//新增打赏订单
	public static final String get_doctor_bounty_list = "/normal/baike/get_doctor_bounty_list";	//获取打赏列表
	public static final String get_doctor_praise_list = "/right/user_question/get_doctor_praise_list";	//医生心意墙
	public static final String questionset_praise = "/right/user_question/set_praise";	//
	public static final String set_evaluation = "/right/user_question/set_evaluation";	//医生评论
	public static final String charge_card = "/right/user/charge_card";	//礼品卡充值
	public static final String user_account_detail = "/normal/html5/user_account_detail/";	//账号交易明细
	public static final String get_doctor_attention = "/normal/baike/get_doctor_myattention_list";	//获取医生的关注列表
	public static final String get_doctor_fans = "/normal/baike/get_doctor_attentionme_list";	//获取医生的粉丝列表
	public static final String get_my_related = "/right/user_baike/get_relatedme_list";	//获取我的点赞，我的评论，我的分享 相关信息
	public static final String get_myjoin_info = "/right/user_baike/get_myjoin_info";	//获取我参与的

	
 
	public static long getLength(String path,ArrayList<BasicNameValuePair> params, int method) throws Exception {
		long length = 0;
		HttpEntity entity = getEntity(path, params, method);
		if (entity != null) {
			length = entity.getContentLength();
		}
		return length;
	}

	public static HttpEntity getEntity(String uri,ArrayList<BasicNameValuePair> params, int method) throws IOException {
		HttpEntity entity = null;
		HttpClient client = new DefaultHttpClient();
		HttpContext httpContext = new BasicHttpContext();
		client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 60000);
		client.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 60000);
//		System.out.println("url = " + uri);
		StringBuilder sb = new StringBuilder(uri);
		HttpUriRequest request = null;
		switch (method) {
		case GET:
			if (params != null) {
				sb.append('?');
			}
			buildParams(sb, params);
			request = new HttpGet(sb.toString());
			break;
		case POST:
			/** 设置请求实体 */
			request = new HttpPost(sb.toString());

			if (params != null && !params.isEmpty()) {
				((HttpPost) request).setEntity(new UrlEncodedFormEntity(params,
						"utf-8"));
			}

			break;
		}
		/** 执行请求响应 */
		HttpResponse response = client.execute(request, httpContext);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			entity = response.getEntity();
		}
		return entity;
	}

	public static InputStream getInputStream(String uri,
			ArrayList<BasicNameValuePair> params, int method)
			throws IOException {
		InputStream in = null;
		HttpEntity entity = getEntity(uri, params, method);
		if (entity != null) {
			in = entity.getContent();
		}
		return in;
	}

	// public static String getString(String uri,
	// ArrayList<BasicNameValuePair> params, int method)
	// throws IOException {
	// String content = null;
	// HttpEntity entity = getEntity(uri, params, method);
	// if (entity != null) {
	// content = EntityUtils.toString(entity, "utf-8");
	// }
	// if (!TextUtils.isEmpty(content)) {
	// Log.i("life", content + "");
	// }
	//
	// return content;
	// }
	public static String getString(String uri,
			ArrayList<BasicNameValuePair> params, int method)
			throws IOException {
		String content = null;
//		Log.i("url", uri);
		// **********httpclient模式***/
		// HttpEntity entity = getEntity(uri, params, method);
		// if (entity != null) {
		// content = EntityUtils.toString(entity, "utf-8");
		// }
		// ***********httpclient模式**end*/

		switch (method) {
		case GET:
			// 暂没用到，工期较紧，先不写
			break;
		case POST:
			content = post(uri, params);
			break;
		}
		if (!TextUtils.isEmpty(content)) {
//			Log.i("life", content + "");
		}
		return content;
	}

	private static final String tag = HttpUtil.class.getSimpleName();

	/**
	 * multipart/form-data
	 * 
	 * @param actionUrl
	 * @param params
	 * @param files
	 * @return
	 * @throws IOException
	 * @throws Exception
	 */
	public static String post(String actionUrl,
			ArrayList<BasicNameValuePair> params) throws IOException {
//		Log.d(tag, "request-url=" + actionUrl);
		String result = null;
		String BOUNDARY = java.util.UUID.randomUUID().toString();
		String PREFIX = "--", LINEND = "\r\n";
		String MULTIPART_FROM_DATA = "multipart/form-data";
		String CHARSET = "UTF-8";

		URL uri = new URL(actionUrl);
		HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
		conn.setReadTimeout(60 * 1000); // 缓存的最长时间
		conn.setConnectTimeout(60 * 1000);
		conn.setDoInput(true);// 允许输入
		conn.setDoOutput(true);// 允许输出
		conn.setUseCaches(false); // 不允许使用缓存
		conn.setRequestMethod("POST");
		conn.setRequestProperty("connection", "keep-alive");
		conn.setRequestProperty("Charsert", "UTF-8");
		conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA
				+ ";boundary=" + BOUNDARY);

		// 首先组拼文本类型的参数
		StringBuilder sb = new StringBuilder();
		DataOutputStream outStream = null;
		if (null != params) {
			for (BasicNameValuePair entry : params) {
				sb.append(PREFIX);
				sb.append(BOUNDARY);
				sb.append(LINEND);
				sb.append("Content-Disposition: form-data; name=\""
						+ entry.getName() + "\"" + LINEND);
				sb.append("Content-Type: text/plain; charset=" + CHARSET
						+ LINEND);
				sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
				sb.append(LINEND);
				sb.append(TextUtils.isEmpty(entry.getValue()) ? "" : entry
						.getValue());
				sb.append(LINEND);
			}
//			Log.i("httptest", "test" + sb.toString());
			outStream = new DataOutputStream(conn.getOutputStream());
			outStream.write(sb.toString().getBytes());
		}
//		Log.i("httptest", "testend");
		InputStream in = null;

		// 得到响应码
		int res = conn.getResponseCode();
//		Log.i(tag, res + "-----------------1");
		if (res == 200) {
			in = conn.getInputStream();
			result = convertStreamToString(in);
		}
		if (null != outStream) {
			outStream.close();
		}
		if (null != conn) {
			conn.disconnect();
		}
//		Log.d(tag, "result=" + result);
		return result;
	}

	static String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8 * 1024);
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			sb.delete(0, sb.length());
		} finally {
			closeStream(is);
		}
		return sb.toString();
	}

	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {

			}
		}
	}

	// 上传文件
	public static String uploadFile(String uri,
			ArrayList<BasicNameValuePair> params, byte[] bytes)
			throws IOException {
		String json = buildJosn(params);

		byte[] data = json.getBytes();

		URL url = new URL(uri);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setConnectTimeout(30000);
		// 这是请求方式为POST
		conn.setRequestMethod("POST");
		// 设置post请求必要的请求头
		conn.setRequestProperty("Content-Type", "application/octet-stream");// 请求头,
																			// 必须设置

		conn.setDoOutput(true);// 准备写出
		DataOutputStream dos = new DataOutputStream(conn.getOutputStream());

		dos.write(ByteUtil.int2byte(data.length));

		dos.writeBytes(json);
		if (bytes != null && bytes.length != 0) {
			dos.write(bytes);
		}

		dos.flush();

		InputStream in = conn.getInputStream();

		String str = StreamUtil.getStreamString(in);
//		Log.i("life", str);
		if (in != null) {
			in.close();
		}
		conn.disconnect();
		dos.close();

		return str;
	}

	public static InputStream loadFile(String uri) throws IOException {
		URL url = new URL(uri);
		URLConnection conn = url.openConnection();
		return conn.getInputStream();
	}

	public static String buildJosn(ArrayList<BasicNameValuePair> params)
			throws IOException {
		StringWriter sw = new StringWriter();
		JsonWriter jw = new JsonWriter(sw);
		if (params != null && !params.isEmpty()) {
			jw.beginObject();
			for (BasicNameValuePair param : params) {
				jw.name(param.getName()).value(param.getValue());
			}
			jw.endObject();
			jw.close();
			sw.close();

		}

		return sw.toString();
	}

	public static StringBuilder buildParams(StringBuilder sb,
			ArrayList<BasicNameValuePair> params) {
		if (params != null && params.size() > 0) {
			for (BasicNameValuePair pair : params) {
				sb.append(pair.getName()).append('=').append(pair.getValue())
						.append('&');
			}
			sb.deleteCharAt(sb.length() - 1);
		}
		return sb;
	}
	

	

}
