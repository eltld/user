package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.BaseEntity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.PhoneVerifyInfo;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.AppUtils;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.StringUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.LogonSuccessEvent;

public class AlertMobileActivity extends BasicActivity implements BasicUIEvent {

	private static final String TITLE = "修改手机号码";
	private EditText et_mobile;
	private EditText et_checkCode;
	private TextView tv_checkCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alert_mobile);
		((TextView) findViewById(R.id.tv_top_title)).setText(TITLE);

		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_checkCode = (EditText) findViewById(R.id.et_check_code);
		tv_checkCode = (TextView) findViewById(R.id.btn_check_code);
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_check_code:
			requestCheckCode();
			break;
		case R.id.btn_submit:
			requestAlertMobile();
			break;
		default:
			break;
		}
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		if (mes == IResult.LOGOUT) {

			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
			params.add(new BasicNameValuePair("channel", "android"));
			params.add(new BasicNameValuePair("imei", PublicUtil.getDeviceUuid(this)));
			params.add(new BasicNameValuePair("appversion", PublicUtil.getVersionName(this)));

			String content;
			try {
				content = HttpUtil.getString(HttpUtil.URI + HttpUtil.logout, params, HttpUtil.POST);
				content = TextUtil.substring(content, "{");
				Gson gson = new Gson();
				ResultInfo reason = gson.fromJson(content, ResultInfo.class);
				Message msg = Message.obtain();
				msg.what = IResult.LOGOUT;
				msg.obj = reason;
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				Message message = new Message();
				message.obj = e.getMessage();
				message.what = IResult.DATA_ERROR;
				handler.sendMessage(message);
			}
		}
	}

	@Override
	protected void onResult(Message msg) {
		try {
			switch (msg.what) {
			case IResult.PHONE_VERIFY_CODE:// 获取校验码
				destroyDialog();
				PhoneVerifyInfo phoneVerify = (PhoneVerifyInfo) msg.obj;
				if (phoneVerify.isSuccess()) {
					setEnableCheckCode(false);
					showToast("验证码已发送,请注意查收短信");
				} else {
					showToast(phoneVerify.getErrormsg());
				}
				break;
			case IResult.ALERT_MOBILE:// 修改手机号
				destroyDialog();
				BaseEntity baseEntity = (BaseEntity) msg.obj;
				showToast(baseEntity.getErrorMessage());
				if (baseEntity.isSuccess()) {
					// stop jpush
					boolean pushStopped = JPushInterface.isPushStopped(getApplicationContext());
					if (!pushStopped)
						JPushInterface.stopPush(getApplicationContext());

					// cancel third part
					String other = SharePrefUtil.getString(Conast.USER_OTHER_LOGIN);
					if (!TextUtils.isEmpty(other)) {
						ShareSDK.initSDK(this);
						if ("1".equals(other)) {
							QZone qq = new QZone(this);
							if (qq.isValid()) {
								qq.getDb().removeAccount();
							}
						} else if ("2".equals(other)) {
							SinaWeibo Weibo = new SinaWeibo(this);
							if (Weibo.isValid()) {
								Weibo.getDb().removeAccount();
							}
						}
					}
					UICore.eventTask(this, this, IResult.LOGOUT, "注销中...", null);
				}
				break;
			case IResult.LOGOUT:
				ResultInfo info = (ResultInfo) msg.obj;
				if (null != info && info.getSuccess() == 1) {
					// 退出聊天服务器
					new Thread() {
						public void run() {
							if (null != EMChatManager.getInstance()
									&& EMChatManager.getInstance().isConnected()) {
								EMChatManager.getInstance().logout();
							}
						};
					}.start();

					NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
					nm.cancel(R.string.app_name);
					nm.cancel(R.drawable.ic_launcher);
					nm.cancelAll();

					Intent intent = new Intent(this, LoginActivity.class);
					intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
					SharePrefUtil.putBoolean(Conast.LOGIN, false);

					SharePrefUtil.putString(Conast.MEMBER_ID, "");
					SharePrefUtil.putString(Conast.USER_Name, "");
					SharePrefUtil.putString(Conast.realName, "");
					SharePrefUtil.putString(Conast.ACCESS_TOKEN, "");
					SharePrefUtil.putString(Conast.AVATAR, "");
					SharePrefUtil.putString(Conast.USER_SEX, "");
					SharePrefUtil.putString(Conast.USER_MOBILE, "");
					SharePrefUtil.putString(Conast.USER_BIRTHDAY, "");
					SharePrefUtil.putString(Conast.USER_CARDID, "");
					SharePrefUtil.putString(Conast.USER_ADDRESS, "");
					SharePrefUtil.putString(Conast.USER_CLINICCARDNUM, "");
					UserPreferenceWrapper.clearUserBalance();
					SharePrefUtil.putString("self_weidu", "");
					SharePrefUtil.putString("self_jindu", "");

					SharePrefUtil.commit();
					startActivity(intent);
					EventBus.getDefault().postSticky(new LogonSuccessEvent());
					finish();
				}
				break;
			case IResult.NET_ERROR:
				destroyDialog();
				showToast(getString(R.string.net_error));
				break;
			case IResult.DATA_ERROR:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	private void requestCheckCode() {
		if (!NetWorkUtils.detect(this)) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
			return;
		}

		String mobile = et_mobile.getText().toString();
		if (!StringUtil.isMobile(mobile)) {
			showToast("请输入正确的手机号");
			return;
		}

		loading(null);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("ispass", "1");// 是否需要跳过手机号码唯一性验证 0 不跳过 1跳过
		params.put("utype", "user");
		params.put("appversion", AppUtils.getVersionName(this));
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.new_phone_verify_code,
				PhoneVerifyInfo.class, params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {
						Message message = new Message();
						message.obj = response;
						message.what = IResult.PHONE_VERIFY_CODE;
						handler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Message message = new Message();
						message.obj = error.getMessage();
						message.what = IResult.NET_ERROR;
						handler.sendMessage(message);
					}
				});
	}

	private void requestAlertMobile() {
		if (!NetWorkUtils.detect(this)) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
			return;
		}
		String mobile = et_mobile.getText().toString();
		String checkCode = et_checkCode.getText().toString();
		if (!StringUtil.isMobile(mobile)) {
			showToast("请输入正确的手机号");
			return;
		}
		if (TextUtils.isEmpty(checkCode)) {
			showToast("请输入验证码");
			return;
		}

		loading(null);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
		params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("mobile", mobile);
		params.put("verify_code", checkCode);
		params.put("app_version", AppUtils.getVersionName(this));
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.ALERT_MOBILE, BaseEntity.class,
				params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {
						Message message = new Message();
						message.obj = response;
						message.what = IResult.ALERT_MOBILE;
						handler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						Message message = new Message();
						message.obj = error.getMessage();
						message.what = IResult.NET_ERROR;
						handler.sendMessage(message);
					}
				});
	}

	private static final int TICKER_CHECK_CODE = 90;
	private int mTick;
	private final static String FORMAT_TICK = "重获（%d秒）";

	Handler mTickHandler = new Handler();
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			mTick--;
			if (mTick <= 0) {
				setEnableCheckCode(true);
				return;
			}
			tv_checkCode.setText(String.format(FORMAT_TICK, mTick));
			mTickHandler.postDelayed(runnable, 1000);
		}
	};

	private void setEnableCheckCode(boolean enable) {
		if (enable) {
			tv_checkCode.setClickable(true);
			tv_checkCode.setTextColor(getResources().getColor(R.color.prime_blue));
			tv_checkCode.setText("重获");
		} else {
			tv_checkCode.setClickable(false);
			tv_checkCode.setTextColor(Color.GRAY);
			mTick = TICKER_CHECK_CODE;
			mTickHandler.postDelayed(runnable, 1000);
		}
	}
}
