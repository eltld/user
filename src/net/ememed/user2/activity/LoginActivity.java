package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.baike.SayDetailsActivity;
import net.ememed.user2.entity.ChatUser;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.LoginEntry;
import net.ememed.user2.entity.LoginInfo;
import net.ememed.user2.entity.OtherLoginEntry;
import net.ememed.user2.entity.OtherLoginInfo;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.entity.ScheduleDetailEntry;
import net.ememed.user2.fragment.DoctorBasicInfoFragment1;
import net.ememed.user2.fragment.MineFragment;
import net.ememed.user2.fragment.MyDoctorFragment;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.MD5;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PlatformBindConfig;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.util.HanziToPinyin;
import com.google.gson.Gson;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQAuth;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.LoginSuccessEvent;
import de.greenrobot.event.RegisterSuccessEvent;
import de.greenrobot.event.util.EMLoginCallBack;
import de.greenrobot.event.util.IMManageTool;

public class LoginActivity extends BasicActivity implements PlatformActionListener, Callback,
		EMLoginCallBack {
	protected static final String TAG = LoginActivity.class.getSimpleName();
	private static final int REQUEST_REGISTER = 1;
	private EditText etAccount, etPwd;
	private TextView btn_look_for_pwd;
	private QQAuth mQQAuth;
	private Tencent mTencent;
	private String openid;
	private String qq_accessToken;
	private String qq_expires_in;
	private UserInfo mInfo;
	private String origin_ui;
	private String BINDFROM;
	// private boolean isOpenOther = false;
	// private boolean isOpenOther1 = false;

	private static final int MSG_USERID_FOUND = 2;
	private static final int MSG_LOGIN = 3;
	private static final int MSG_AUTH_CANCEL = 6;
	private static final int MSG_AUTH_ERROR = 4;
	private static final int MSG_AUTH_COMPLETE = 5;

	private static final String BINDFROM_QQ = "1";
	private static final String BINDFROM_Weibo = "2";
	private String other_login;
	private TextView tv_hint;
	private String current_service;
	private DoctorEntry entry;
	private Button btn_login;
	IMManageTool tool;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		ShareSDK.initSDK(this);
		origin_ui = getIntent().getStringExtra("origin");
		current_service = getIntent().getStringExtra("current_service");
		entry = (DoctorEntry) getIntent().getSerializableExtra("entry");
		src = getIntent().getStringExtra("src");
		scheduleDetailEntry = (ScheduleDetailEntry) getIntent().getSerializableExtra(
				"schedule_detail");
		guahao_doctot = (GuahaoDoctotItem) getIntent().getSerializableExtra("guahao_doctot");
		setContentView(R.layout.login_v2);
		EventBus.getDefault().registerSticky(this, RegisterSuccessEvent.class);
		tool = IMManageTool.getInstance(getApplicationContext());
		tool.setEMLoginCallBack(this);
	}

	@Override
	protected void onDestroy() {
		ShareSDK.stopSDK(this);
		super.onDestroy();
	}

	@Override
	protected void setupView() {
		TextView tv_title = (TextView) findViewById(R.id.top_title);
		tv_title.setText(getString(R.string.title_login));
		tv_hint = (TextView) findViewById(R.id.tv_hint);
		if (!TextUtils.isEmpty(origin_ui)
				&& origin_ui.equals(DoctorClininActivity.class.getSimpleName())) {
			tv_hint.setVisibility(View.VISIBLE);
		}
		//TODO

		btn_login = (Button) findViewById(R.id.btn_login);

		etAccount = (EditText) findViewById(R.id.et_account);
		etPwd = (EditText) findViewById(R.id.et_pwd);
		btn_look_for_pwd = (TextView) findViewById(R.id.btn_look_for_pwd);
		// btn_look_for_pwd.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		// btn_look_for_pwd.getPaint().setAntiAlias(true);// 抗锯齿
		btn_look_for_pwd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				seekPwd();
			}
		});
		// try {
		// //自动弹出键盘
		// new Handler().postDelayed(new Runnable() {
		// @Override
		// public void run() {
		// InputMethodManager inputManager = (InputMethodManager)
		// LoginActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
		// inputManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
		// etAccount.setFocusable(true);
		// etAccount.setFocusableInTouchMode(true);
		// etAccount.requestFocus();
		// }
		// }, 200L);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public void doClick(View view) {
		int id = view.getId();
		if (id == R.id.btn_login) {
			login();
		} else if (id == R.id.btn_logon) {
			logon();
		} else if (id == R.id.btn_look_for_pwd) {
			seekPwd();
		} else if (id == R.id.btn_back) {
			finish();
		} else if (id == R.id.rb_qq_login) {
			loading(null);
			other_login = BINDFROM_QQ;
			authorize(new QQ(this));
		} else if (id == R.id.rb_weibo_login) {
			loading(null);
			other_login = BINDFROM_Weibo;
			authorize(new SinaWeibo(this));
		}
	}

	// 找回密码
	private void seekPwd() {
		Intent intent = new Intent(this, SeekPwdActivity.class);
		startActivity(intent);
	}

	// 登录
	private void login() {
		// isLogining = true;
		final String userName = etAccount.getText().toString().trim();
		final String pwd = etPwd.getText().toString().trim();
		if ("".equals(etAccount.getText().toString().trim())
				|| "".equals(etPwd.getText().toString().trim())) {
			showToast("账户或密码不能为空");
			return;
		}
		loading(null);
		savePwd(userName, pwd);
		if (!NetWorkUtils.detect(this)) {
			setNetWork();
			return;
		}
		loading(null);
		savePwd(userName, pwd);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("strusername", userName);
		params.put("strpwd", pwd);
		params.put("imei", PublicUtil.getDeviceUuid(LoginActivity.this));
		params.put("appversion", PublicUtil.getVersionName(LoginActivity.this));
		Log.e("XX", params.toString());
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.login, LoginInfo.class, params,
				new Response.Listener() {
					@Override
					public void onResponse(Object response) {
						// isLogining = false;
						Message message = new Message();
						message.obj = response;
						message.what = IResult.LOGIN;
						handler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						// isLogining = false;
						Message message = new Message();
						message.obj = error.getMessage();
						message.what = IResult.NET_ERROR;
						handler.sendMessage(message);
					}
				});
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
								loginIM();
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

	// 保存密码
	private void savePwd(String userName, String pwd) {
		SharePrefUtil.putBoolean("savepwd", true);
		SharePrefUtil.putString("account", userName);
		SharePrefUtil.putString("pwd", pwd);
		SharePrefUtil.commit();
	}

	/**
	 * 第三方登录保存数据
	 * @param doctor
	 */
	private void saveDoctor(OtherLoginEntry doctor) {
		SharePrefUtil.putBoolean(Conast.LOGIN, true);
		SharePrefUtil.putString(Conast.ACCESS_TOKEN, doctor.getTOKEN());
		SharePrefUtil.putString(Conast.MEMBER_ID, doctor.getMEMBERID());
		SharePrefUtil.putString(Conast.USER_Name, doctor.getMEMBERNAME());
		SharePrefUtil.putString(Conast.realName, doctor.getREALNAME());
		SharePrefUtil.putString(Conast.AVATAR, doctor.getAVATAR());
		SharePrefUtil.putString(Conast.USER_SEX, doctor.getSEX());
		SharePrefUtil.putString(Conast.USER_MOBILE, doctor.getMOBILE());
		SharePrefUtil.putString(Conast.USER_BIRTHDAY, doctor.getBIRTHDAY());
		SharePrefUtil.putString(Conast.USER_CARDID, doctor.getCARDID());
		SharePrefUtil.putString(Conast.USER_ADDRESS, doctor.getADDRESS());
		SharePrefUtil.putString(Conast.USER_CLINICCARDNUM, doctor.getCLINICCARDNUM());
		SharePrefUtil.putString(Conast.USER_TYPE, doctor.getUTYPE());
		UserPreferenceWrapper.setUserBalance(doctor.getUSERMONEY());
		SharePrefUtil.putString(Conast.USER_OTHER_LOGIN, other_login);
		SharePrefUtil.commit();

		loginIM();
	}

	public void loginIM() {
		try {
			// 调用sdk登录方法登录聊天服务器
			tool.loginIM(
					SharePrefUtil.getString(Conast.MEMBER_ID),
					MD5.getMD5(SharePrefUtil.getString(Conast.MEMBER_ID)
							+ SharePrefUtil.getString(Conast.USER_Name) + "ememedim"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 正常登录保存数据
	 * @param doctor
	 */
	private void saveDoctor(LoginEntry doctor) {
		SharePrefUtil.putBoolean(Conast.LOGIN, true);
		SharePrefUtil.putString(Conast.ACCESS_TOKEN, doctor.getTOKEN());
		SharePrefUtil.putString(Conast.MEMBER_ID, doctor.getMEMBERID());
		SharePrefUtil.putString(Conast.USER_Name, doctor.getMEMBERNAME());
		SharePrefUtil.putString(Conast.realName, doctor.getREALNAME());
		SharePrefUtil.putString(Conast.AVATAR, doctor.getAVATAR());
		SharePrefUtil.putString(Conast.USER_SEX, doctor.getSEX());
		SharePrefUtil.putString(Conast.USER_MOBILE, doctor.getMOBILE());
		SharePrefUtil.putString(Conast.USER_BIRTHDAY, doctor.getBIRTHDAY());
		SharePrefUtil.putString(Conast.USER_CARDID, doctor.getCARDID());
		SharePrefUtil.putString(Conast.USER_ADDRESS, doctor.getADDRESS());
		SharePrefUtil.putString(Conast.USER_CLINICCARDNUM, doctor.getCLINICCARDNUM());
		SharePrefUtil.putString(Conast.USER_TYPE, doctor.getUTYPE());
		UserPreferenceWrapper.setUserBalance(doctor.getUSERMONEY());
		SharePrefUtil.commit();
		
		loginIM();
	}

	/** 注册 */
	private void logon() {
		Intent intent = new Intent(this, RegisterByMobileActivity.class);
		startActivityForResult(intent, REQUEST_REGISTER);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_REGISTER:
			if (RESULT_OK == resultCode && null != data) {
				String account = data.getStringExtra("account");
				String pwd = data.getStringExtra("pwd");
				if (!TextUtils.isEmpty(account) && !TextUtils.isEmpty(pwd)) {
					etAccount.setText(account);
					etPwd.setText(pwd);

					// savePwd(account, pwd);

					// Intent intent = new
					// Intent(LoginActivity.this,RegisterSuccessActivity.class);
					// intent.putExtra("account",
					// etAccount.getText().toString().trim());
					// intent.putExtra("pwd",
					// etPwd.getText().toString().trim());
					// startActivity(intent);
					// finish();
				}
			}
			break;
		default:
			break;
		}
	}

	public void onEvent(RegisterSuccessEvent testEvent) {
		Logger.dout("register success");
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
//		destroyDialog();
		EventBus.getDefault().removeStickyEvent(RegisterSuccessEvent.class);
		EventBus.getDefault().removeStickyEvent(LoginSuccessEvent.class);
	}

	LoginInfo login;

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);

		switch (msg.what) {
		case IResult.LOGIN:// 普通登录

			login = (LoginInfo) msg.obj;

			if (null != login) {
				if (login.getSuccess() == 1) {
					JPushInterface.init(getApplicationContext());
					JPushInterface.resumePush(getApplicationContext());
					saveDoctor(login.getData());
				} else {
					showToast(login.getErrormsg());
					tv_hint.setVisibility(View.VISIBLE);
					tv_hint.setText(login.getErrormsg());
					destroyDialog();
				}
			}
			break;
		case IResult.OTHER_LOGIN:// 第三方登录
			OtherLoginInfo ologin = (OtherLoginInfo) msg.obj;
			if (ologin == null) {
				Toast.makeText(getApplicationContext(), "服务器连接失败", 0).show();
				return;
			}
			switch (ologin.getSuccess()) {
			case 0:// 注册失败
				Toast.makeText(this, "注册失败，请重试或换一种注册方式", 1).show();
				break;
			case 1:// 登录成功
				JPushInterface.init(getApplicationContext());
				JPushInterface.resumePush(getApplicationContext());
				savePwd(ologin.getData().getMEMBERNAME(), ologin.getData().getPAYPASSWORD());

				saveDoctor(ologin.getData());
				dealLoginSuccess();
				/*if (!TextUtils.isEmpty(origin_ui)
						&& origin_ui.equals(MineFragment.class.getSimpleName())) {
					finish();
					LoginSuccessEvent event = new LoginSuccessEvent();
					EventBus.getDefault().postSticky(event);
				} else if (!TextUtils.isEmpty(origin_ui)
						&& origin_ui.equals(DoctorClininActivity.class.getSimpleName())) {
					Intent intent = null;
					if (current_service.equals("1")) {
						intent = new Intent(this, BuyTextConsultActivity.class);
					} else if (current_service.equals("2")) {
						intent = new Intent(this, BuyPhoneConsultActivity.class);
					} else if (current_service.equals("3")) {
						intent = new Intent(this, BuyJiuyiActivity.class);
					} else if (current_service.equals("4")) {
						intent = new Intent(this, BuyShangmenActivity.class);
					} else if (current_service.equals("5")) {
						intent = new Intent(this, BuyZhuyuanActivity.class);
					} else if (current_service.equals("15")) {
						intent = new Intent(this, PrivateDoctorActivity.class);
					} else if (current_service.equals("16")) {
						intent = new Intent(this, BuyCustomActivity.class);
					} else if (current_service.equals("17")) {
						intent = new Intent(this, PutQuestionsActivity.class);
					}else if (current_service.equals("-1")){
						finish();
					}
					intent.putExtra("doctorid", entry.getDOCTORID());
					intent.putExtra("doctorServiceEntry", entry.getOFFER_SERVICE());
					intent.putExtra("doctor_name", entry.getREALNAME());
					startActivity(intent);
					finish();
					LoginSuccessEvent event = new LoginSuccessEvent();
					EventBus.getDefault().postSticky(event);
				} else if (!TextUtils.isEmpty(origin_ui)
						&& origin_ui.equals(MyDoctorFragment.class.getSimpleName())) {
					finish();
					LoginSuccessEvent event = new LoginSuccessEvent();
					EventBus.getDefault().postSticky(event);
				} else if(!TextUtils.isEmpty(origin_ui) && origin_ui.equals(DoctorBasicInfoFragment1.class.getSimpleName())){
					finish();
				} else if(!TextUtils.isEmpty(origin_ui) && origin_ui.equals(SayDetailsActivity.class.getSimpleName())){
					finish();
				} else if ("FeedbackActivity".equals(src) || "WebViewActivity".equals(src)) {
					finish();
				} else if (guahao_doctot != null && scheduleDetailEntry != null) {
					Intent intent = new Intent(this, VisitPersonChoiceActivity.class);
					intent.putExtra("schedule_detail", scheduleDetailEntry);
					intent.putExtra("guahao_doctot", guahao_doctot);
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(this, MainActivity.class);
					intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
					startActivity(intent);
					finish();
				}*/
				break;
			case 2:// 账号已经存在,绑定手机号码
				destroyDialog();
				Intent i = new Intent(this, BindByMobileActivity.class);
				i.putExtra("bind_data", ologin.getData());
				i.putExtra("bindfrom", BINDFROM);
				i.putExtra("openid", openid);
				startActivity(i);
				break;
			default:
				break;
			}
			break;
		case IResult.NET_ERROR:
			destroyDialog();
			showToast("登录失败");
			break;
		default:
			break;
		}
	}

	private void onClickQQLogin() {
		mQQAuth = QQAuth.createInstance(PlatformBindConfig.QQ_AppKey, LoginActivity.this);
		mTencent = Tencent.createInstance(PlatformBindConfig.QQ_AppKey, LoginActivity.this);
		if (!mQQAuth.isSessionValid()) {

			IUiListener listener = new BaseUiListener() {
				@Override
				protected void doComplete(JSONObject values) {
					getQQUserInfo((JSONObject) values);
				}
			};
			mQQAuth.login(this, "all", listener);
		} else {
			mQQAuth.logout(this);
		}
	}

	private void getQQUserInfo(JSONObject values) {

		try {
			openid = values.getString("openid");
			qq_accessToken = values.getString("access_token");
			qq_expires_in = values.getString("expires_in");

		} catch (JSONException e1) {
			e1.printStackTrace();
		}

		if (mQQAuth != null && mQQAuth.isSessionValid()) {
			IUiListener listener_ui = new IUiListener() {

				@Override
				public void onError(UiError e) {
				}

				@Override
				public void onComplete(final Object response) {
					JSONObject json = (JSONObject) response;
					if (openid != null)
						OtherLogin(BINDFROM_QQ);
					try {
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				@Override
				public void onCancel() {
				}
			};
			mInfo = new UserInfo(this, mQQAuth.getQQToken());
			mInfo.getUserInfo(listener_ui);

		}
	}

	public class BaseUiListener implements IUiListener {

		protected void doComplete(JSONObject values) {
			// System.out.println("doComplete");
		}

		@Override
		public void onError(UiError e) {
			// System.out.println("onError:" + "code:" + e.errorCode + ", msg:"
			// + e.errorMessage + ", detail:" + e.errorDetail);
		}

		@Override
		public void onCancel() {
			// System.out.println("onCancel");
		}

		@Override
		public void onComplete(Object arg0) {
			// System.out.println("onComplete");
			doComplete((JSONObject) arg0);
		}
	}

	/**
	 * 设置hearder属性，方便通讯中对联系人按header分类显示，以及通过右侧ABCD...字母栏快速定位联系人
	 * 
	 * @param username
	 * @param user
	 */
	protected void setUserHearder(String username, ChatUser user) {
		String headerName = null;
		if (!TextUtils.isEmpty(user.getNick())) {
			headerName = user.getNick();
		} else {
			headerName = user.getUsername();
		}
		if (username.equals(Conast.NEW_FRIENDS_USERNAME)) {
			user.setHeader("");
		} else if (Character.isDigit(headerName.charAt(0))) {
			user.setHeader("#");
		} else {
			user.setHeader(HanziToPinyin.getInstance().get(headerName.substring(0, 1)).get(0).target
					.substring(0, 1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	private void authorize(Platform plat) {
		if (plat.isValid()) {
			String userId = plat.getDb().getUserId();
			if (!TextUtils.isEmpty(userId)) {
				// plat.getDb().removeAccount();
				// // UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
				openid = userId;
				if (plat.getName() != null && plat.getName().endsWith("QQ")) {
					OtherLogin(BINDFROM_QQ);
				} else if (plat.getName() != null && plat.getName().endsWith("SinaWeibo")) {
					OtherLogin(BINDFROM_Weibo);
				}
				return;
			}
		}
		plat.setPlatformActionListener(this);
		plat.SSOSetting(false);
		plat.showUser(null);
	}

	private String src;
	private GuahaoDoctotItem guahao_doctot;
	private ScheduleDetailEntry scheduleDetailEntry;

	private void OtherLogin(String BINDFROM) {
		loading(null);
		Map<String, String> params = new HashMap<String, String>();
		params.put("openid", openid);
		params.put("bindfrom", BINDFROM);
		params.put("appversion", PublicUtil.getVersionName(LoginActivity.this));
		params.put("imei", PublicUtil.getDeviceUuid(LoginActivity.this));
		// isLogining = true;

		this.BINDFROM = BINDFROM;
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.other_login, OtherLoginInfo.class,
				params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {
						Message message = new Message();
						message.obj = response;
						message.what = IResult.OTHER_LOGIN;
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

	// 取消
	@Override
	public void onCancel(Platform platform, int action) {
		destroyDialog();
		if (action == Platform.ACTION_USER_INFOR) {

		}
	}

	// 成功
	@Override
	public void onComplete(Platform platform, int action, HashMap<String, Object> res) {
		// TODO Auto-generated method stub
		if (action == Platform.ACTION_USER_INFOR) {
			if (platform.isValid()) {
				String userId = platform.getDb().getUserId();
				if (!TextUtils.isEmpty(userId)) {
					// plat.getDb().removeAccount();
					// // UIHandler.sendEmptyMessage(MSG_USERID_FOUND, this);
					openid = userId;
					if (platform.getName() != null && platform.getName().endsWith("QQ")) {
						OtherLogin(BINDFROM_QQ);
					} else if (platform.getName() != null
							&& platform.getName().endsWith("SinaWeibo")) {
						OtherLogin(BINDFROM_Weibo);
					}
					return;
				}
			}
		}

	}

	// 出错
	@Override
	public void onError(Platform platform, int action, Throwable t) {

		destroyDialog();
		if (action == Platform.ACTION_USER_INFOR) {
		}
		t.printStackTrace();
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		destroyDialog();
		return false;
	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		destroyDialog();
		dealLoginSuccess();
	}

	private void dealLoginSuccess() {
		if (!TextUtils.isEmpty(origin_ui) && origin_ui.equals(MineFragment.class.getSimpleName())) {
			finish();
			LoginSuccessEvent event = new LoginSuccessEvent();
			EventBus.getDefault().postSticky(event);
		} else if (!TextUtils.isEmpty(origin_ui)
				&& origin_ui.equals(DoctorClininActivity.class.getSimpleName())) {
			Intent intent = null;
			if (current_service.equals("1")) {
				intent = new Intent(this, BuyTextConsultActivity.class);
			} else if (current_service.equals("2")) {
				intent = new Intent(this, BuyPhoneConsultActivity.class);
			} else if (current_service.equals("3")) {
				intent = new Intent(this, BuyJiuyiActivity.class);
			} else if (current_service.equals("4")) {
				intent = new Intent(this, BuyShangmenActivity.class);
			} else if (current_service.equals("5")) {
				intent = new Intent(this, BuyZhuyuanActivity.class);
			} else if (current_service.equals("15")) {
				intent = new Intent(this, PrivateDoctorActivity.class);
			} else if (current_service.equals("16")) {
				intent = new Intent(this, BuyCustomActivity.class);
			} else if (current_service.equals("17")) {
				intent = new Intent(this, PutQuestionsActivity.class);
			}else if (current_service.equals("-1")) {
				finish();
				return;
			} else {
				intent = new Intent(this, MainActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			}
			intent.putExtra("doctorid", entry.getDOCTORID());
			intent.putExtra("doctorServiceEntry", entry.getOFFER_SERVICE());
			// intent.putExtra("entry", entry);
			intent.putExtra("doctor_name", entry.getREALNAME());
			startActivity(intent);
			LoginSuccessEvent event = new LoginSuccessEvent();
			EventBus.getDefault().postSticky(event);
		} else if (!TextUtils.isEmpty(origin_ui)
				&& origin_ui.equals(MyDoctorFragment.class.getSimpleName())) {
			finish();
			LoginSuccessEvent event = new LoginSuccessEvent();
			EventBus.getDefault().postSticky(event);
		} else if (!TextUtils.isEmpty(origin_ui)
				&& origin_ui.equals(HospitalDetailsActivity.class.getSimpleName())) {
			finish();
			LoginSuccessEvent event = new LoginSuccessEvent();
			EventBus.getDefault().postSticky(event);
		} else if(!TextUtils.isEmpty(origin_ui) && origin_ui.equals(DoctorBasicInfoFragment1.class.getSimpleName())){
			finish();
		} else if(!TextUtils.isEmpty(origin_ui) && origin_ui.equals(SayDetailsActivity.class.getSimpleName())){
			finish();
		} else if ("FeedbackActivity".equals(src) || "WebViewActivity".equals(src)) {
			finish();
		} else if (guahao_doctot != null && scheduleDetailEntry != null) {
			Intent intent = new Intent(this, VisitPersonChoiceActivity.class);
			intent.putExtra("schedule_detail", scheduleDetailEntry);
			intent.putExtra("guahao_doctot", guahao_doctot);
			startActivity(intent);
			finish();
		} else {
			Intent intent = new Intent(this, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
			finish();
		}
	}

	@Override
	public void onProgress(int progress, String str) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int age1, final String age2) {
		// TODO Auto-generated method stub
		if (age1 == EMCallBack.ERROR_EXCEPTION_INVALID_PASSWORD_USERNAME) {// code = -1
			// 有可能是应用的环信value出错，有可能是该帐号没注册到环信服务器，所以重新调用接口，让后台把该帐号注册到环信
			reg_to_im();
		} else {
			// 处理错误消息
			runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					LoginActivity.this.destroyDialog();
					LoginActivity.this.showToast(age2);
				}
			});
		}
	}

}
