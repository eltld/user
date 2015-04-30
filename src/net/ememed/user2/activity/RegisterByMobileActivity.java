package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.db.UserDao;
import net.ememed.user2.entity.ChatUser;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.PhoneVerifyInfo;
import net.ememed.user2.entity.RegisterInfo;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.MD5;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.util.HanziToPinyin;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.RegisterSuccessEvent;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 注册
 * 
 * @author pengwl
 * 
 */
public class RegisterByMobileActivity extends BasicActivity {
	private static final int REQUEST_REGISTER = 1;
	private CheckBox cb_service;
	private EditText et_userName, et_password;
	private TextView tv_protocol;
	private EditText et_checkCode;
	private Button btn_checkCode;
	private Handler mTimeHandler;
	private int verifyCode;
	private ImageView iv_show_pswd;
	private boolean isShowingPwd;
	private Button btn_register;
//	private EditText et_invite;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.register_by_mobile);
	}

	@Override
	protected void setupView() {
		TextView tv_title = (TextView) findViewById(R.id.top_title);
		tv_title.setText(getString(R.string.title_register));
		cb_service = (CheckBox) findViewById(R.id.ckServiceItem);
		cb_service.setChecked(true);
		et_userName = (EditText) findViewById(R.id.et_account);
		et_password = (EditText) findViewById(R.id.et_pwd);
		et_checkCode = (EditText) findViewById(R.id.et_verify_code);
//		et_invite = (EditText) findViewById(R.id.et_invite);
		btn_checkCode = (Button) findViewById(R.id.btn_second);
		btn_register = (Button) findViewById(R.id.btn_register);
		tv_protocol = (TextView) findViewById(R.id.tv_protocol);
		tv_protocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		tv_protocol.getPaint().setAntiAlias(true);// 抗锯齿
		tv_protocol.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent intent = new Intent(RegisterByMobileActivity.this, WebViewActivity.class);
				intent.putExtra("title", "薏米网服务条款");
				intent.putExtra("url", HttpUtil.regClause);
				startActivity(intent);
			}
		});

		iv_show_pswd = (ImageView) findViewById(R.id.iv_show_pwd);

		HandlerThread mHandlerThread = new HandlerThread("count", 5);
		mHandlerThread.start();// 开始计时
		mTimeHandler = new Handler(mHandlerThread.getLooper());
		// 显示软键盘
		try {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					InputMethodManager inputManager = (InputMethodManager) RegisterByMobileActivity.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					et_userName.setFocusable(true);
					et_userName.setFocusableInTouchMode(true);
					et_userName.requestFocus();
				}
			}, 200L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void doClick(View view) {
		int id = view.getId();
		if (id == R.id.btn_second) {
			if (NetWorkUtils.detect(RegisterByMobileActivity.this)) {
				if (TextUtils.isEmpty(et_userName.getText().toString())) {
					showToast(getString(R.string.register_account_length_null));
				} else {
					String accountNum = et_userName.getText().toString().trim();
					if (accountNum.length() == 11) {
						btn_checkCode.setClickable(false);
						btn_checkCode.setEnabled(false);
						btn_checkCode.setSelected(true);
						btn_checkCode.setTextColor(getResources().getColor(R.color.light_gray));
						phone_verify_code(et_userName.getText().toString().trim());
					} else {
						showToast(getString(R.string.register_account_length));
					}
				}
			} else {
				showToast(getString(R.string.net_error));
			}
		} else if (id == R.id.btn_register) {
			if (TextUtils.isEmpty(et_userName.getText().toString())
					|| et_userName.getText().toString().trim().length() == 0) {
				showToast("请输入手机号");
				return;
			}
			if (TextUtils.isEmpty(et_checkCode.getText().toString())
					|| et_checkCode.getText().toString().trim().length() == 0) {
				showToast("请输入验证码");
				return;
			}
			String verify_code_sms = et_checkCode.getText().toString();
			if (!verify_code_sms.equals(verifyCode + "")) {
				showToast("验证码不正确,请重新获取");
				btn_checkCode.setTextColor(getResources().getColor(R.color.prime_blue));
				btn_checkCode.setText("重获");
				btn_checkCode.setClickable(true);
				btn_checkCode.setEnabled(true);
				btn_checkCode.setSelected(false);
				return;
			}

			String password = et_password.getText().toString().trim();
			if (TextUtils.isEmpty(password) || password.length() == 0) {
				showToast("请输入密码");
				return;
			}

			if (et_password.length() < 6 || et_password.length() > 20) {
				showToast("请输入6-20位数字或字母");
				return;
			}

			if (!cb_service.isChecked()) {
				showToast("请阅读并同意薏米服务条款");
				return;
			}
			if (NetWorkUtils.detect(RegisterByMobileActivity.this)) {
				register();
				EventBus.getDefault().postSticky(new RegisterSuccessEvent());
				// finish();
			} else {
				showToast(getString(R.string.net_error));
			}
		} else if (id == R.id.btn_back) {
			finish();
		} else if (id == R.id.iv_show_pwd) {
			if (isShowingPwd) {
				// 隐藏密码
				isShowingPwd = false;
				et_password.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_VARIATION_PASSWORD);
				iv_show_pswd.setImageResource(R.drawable.ic_eye_close);
			} else {
				// 显示密码
				isShowingPwd = true;
				et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				iv_show_pswd.setImageResource(R.drawable.ic_eye_open);
			}
		}
	}

	/** 获取电话验证码 */
	private void phone_verify_code(final String phoneNum) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", phoneNum);
		params.put("ispass", "0");
		params.put("utype", "user");
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

	/** 注册 */
	private void register() {

		loading(null);

		String pwd = et_password.getText().toString().trim();
		String mobile = et_userName.getText().toString().trim();
//		String invite = et_invite.getText().toString().trim();

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", mobile);
		params.put("strpwd", pwd);
		params.put("invite_number", /*invite*/"");
		params.put("channel", "Android");
		params.put("appversion", PublicUtil.getVersionName(this));

		MyApplication.volleyHttpClient.postWithParams(HttpUtil.register, RegisterInfo.class,
				params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {

						Message message = new Message();
						message.obj = response;
						message.what = IResult.RESULT;
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

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_REGISTER:
			if (RESULT_OK == resultCode && null != data) {
				String account = data.getStringExtra("account");
				String pwd = data.getStringExtra("pwd");
			}
			break;
		default:
			break;
		}
	}

	// 保存密码
	private void savePwd(String userName, String pwd) {
		SharePrefUtil.putBoolean("savepwd", true);
		SharePrefUtil.putString("account", userName);
		SharePrefUtil.putString("pwd", pwd);
		SharePrefUtil.commit();
	}

	@Override
	protected void onResult(Message msg) {
		try {
			switch (msg.what) {
			case IResult.RESULT:
				destroyDialog();
				RegisterInfo register = (RegisterInfo) msg.obj;
				if (register.getSuccess() == 1) {
					savePwd(et_userName.getText().toString(), et_password.getText().toString());
					// logonSuccess
					SharePrefUtil.putBoolean(Conast.LOGIN, true);
					SharePrefUtil.putString(Conast.MEMBER_ID, register.getData().getMEMBERID());
					SharePrefUtil.putString(Conast.USER_Name, register.getData().getMEMBERNAME());
					SharePrefUtil.putString(Conast.realName, register.getData().getREALNAME());
					SharePrefUtil.putString(Conast.ACCESS_TOKEN, register.getData().getTOKEN());
					SharePrefUtil.putString(Conast.AVATAR, register.getData().getAVATAR());
					SharePrefUtil.putString(Conast.USER_SEX, register.getData().getSEX());
					SharePrefUtil.putString(Conast.USER_MOBILE, register.getData().getMOBILE());
					SharePrefUtil.putString(Conast.USER_BIRTHDAY, register.getData().getBIRTHDAY());
					SharePrefUtil.putString(Conast.USER_CARDID, register.getData()
							.getCERTIFICATENUM());
					SharePrefUtil.putString(Conast.USER_ADDRESS, register.getData().getADDRESS());
					SharePrefUtil.putString(Conast.USER_CLINICCARDNUM, register.getData()
							.getCARDNUMBER());
					SharePrefUtil.commit();

					loginIm();

					EventBus.getDefault().postSticky(new RegisterSuccessEvent());

					Intent intent = new Intent(RegisterByMobileActivity.this,
							NavigationActivity.class);
					// intent.putExtra("account",
					// etAccount.getText().toString().trim());
					// intent.putExtra("pwd",
					// etPwd.getText().toString().trim());
					startActivity(intent);
					finish();
				} else {
					showToast(register.getErrormsg());
				}
				break;
			case IResult.PHONE_VERIFY_CODE:
				PhoneVerifyInfo phoneVerify = (PhoneVerifyInfo) msg.obj;
				if (phoneVerify.getSuccess() == 1) {
					verifyCode = phoneVerify.getData().getCODE();
					btn_checkCode.setClickable(false);
					btn_checkCode.setEnabled(false);
					btn_checkCode.setSelected(true);
					btn_checkCode.setTextColor(getResources().getColor(R.color.light_gray));
					time = 120;
					terminateCount = false;
					mTimeHandler.post(oneSecondThread);
					showToast("验证码已发送,请注意查收短信");
				} else {
					if (!TextUtils.isEmpty(phoneVerify.getErrormsg())) {
						showToast(phoneVerify.getErrormsg());
						btn_checkCode.setText("重获");
						btn_checkCode.setClickable(true);
						btn_checkCode.setEnabled(true);
						btn_checkCode.setSelected(false);
					}
				}
				break;
			case IResult.FAILURE:
				Toast.makeText(this, "账号或密码不能为空", Toast.LENGTH_SHORT).show();
				break;
			case IResult.LOGON_ERROR:
				Toast.makeText(this, (String) msg.obj, Toast.LENGTH_SHORT).show();
				break;
			case IResult.NET_ERROR:
				destroyDialog();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	/** 登录聊天服务器 */
	private void loginIm() {
		try {
			// 调用sdk登录方法登录聊天服务器
			MyApplication.isLoginIMing = true;
			EMChatManager.getInstance().login(
					SharePrefUtil.getString(Conast.MEMBER_ID),
					MD5.getMD5(SharePrefUtil.getString(Conast.MEMBER_ID)
							+ SharePrefUtil.getString(Conast.USER_Name) + "ememedim"),
					new EMCallBack() {

						@Override
						public void onSuccess() {
							Logger.dout(TAG + " EMChatManager onSuccess");
							MyApplication.isLoginIMing = false;
							// 登录成功，保存用户名密码
							try {
								// demo中简单的处理成每次登录都去获取好友username，开发者自己根据情况而定
								List<String> usernames = EMChatManager.getInstance()
										.getContactUserNames();
								// Map<String, ChatUser> userlist = new HashMap<String,
								// ChatUser>();
								// for (String username : usernames) {
								// ChatUser user = new ChatUser();
								// user.setUsername(username);
								// setUserHearder(username, user);
								// userlist.put(username, user);
								// }
								// // 存入内存
								// MyApplication.getInstance().setContactList(userlist);
								// // 存入db
								// UserDao dao = new
								// UserDao(RegisterByMobileActivity.this);
								// List<ChatUser> users = new
								// ArrayList<ChatUser>(userlist.values());
								// dao.saveContactList(users);

							} catch (Exception e) {
								e.printStackTrace();
							}
						}

						@Override
						public void onProgress(int progress, String status) {
							Logger.dout(TAG + " EMChatManager onProgress");
						}

						@Override
						public void onError(int code, final String message) {
							MyApplication.isLoginIMing = false;
							Logger.dout(TAG + " EMChatManager code:" + code + "onError:" + message);
							// handler.sendEmptyMessage(IResult.DATA_ERROR);
							if (code == -1) {// code = -1
								// 有可能是应用的环信value出错，有可能是该帐号没注册到环信服务器，所以重新调用接口，让后台把该帐号注册到环信
								reg_to_im();
							}
						}
					});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void reg_to_im() {
		try {
			if (NetWorkUtils.detect(getApplicationContext())) {
				new Thread() {
					public void run() {
						ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("token", SharePrefUtil
								.getString(Conast.ACCESS_TOKEN)));
						params.add(new BasicNameValuePair("channel", "android"));
						params.add(new BasicNameValuePair("memberid", SharePrefUtil
								.getString(Conast.MEMBER_ID)));
						params.add(new BasicNameValuePair("utype", "user"));
						try {
							String result = HttpUtil.getString(HttpUtil.URI + HttpUtil.reg_to_im,
									params, HttpUtil.POST);
							Gson gson = new Gson();
							ResultInfo info = gson.fromJson(result, ResultInfo.class);
							if (null != info && info.getSuccess() == 1) {
								loginIm();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private int time = 120;
	private boolean terminateCount = false;

	Thread oneSecondThread = new Thread(new Runnable() {

		@Override
		public void run() {
			try {
				if (time > 0 && !terminateCount) {
					time--;
					Thread.sleep(1000);
					Message msg = new Message();
					msg.arg1 = time;
					uiHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	});

	Handler uiHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (time > 0 && !terminateCount) {
				btn_checkCode.setText(msg.arg1 + " 秒");
				mTimeHandler.post(oneSecondThread);
			} else {
				btn_checkCode.setTextColor(getResources().getColor(R.color.prime_blue));
				btn_checkCode.setText("重获");
				btn_checkCode.setClickable(true);
				btn_checkCode.setEnabled(true);
				btn_checkCode.setSelected(false);
				terminateCount = true;
			}
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(RegisterSuccessEvent.class);
	}

}
