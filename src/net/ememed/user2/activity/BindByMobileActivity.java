package net.ememed.user2.activity;

import java.util.HashMap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.RegisterSuccessEvent;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.BindInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.OtherLoginEntry;
import net.ememed.user2.entity.PhoneVerifyInfo;
import net.ememed.user2.entity.RegisterInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class BindByMobileActivity extends BasicActivity{
	private CheckBox ckServiceItem;
	private EditText etAccount, etPwd;
	private TextView tv_protocol;
	private EditText et_verify_code;
	private Button btn_second;
	private Handler mTimeHandler;
	private int verifyCode;
	private ImageView iv_show_pswd;
	private boolean isShowingPwd;
	private OtherLoginEntry entry;
	private String bindfrom;
	private String openid;
	private InputMethodManager manager;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.register_by_mobile);
		entry = (OtherLoginEntry) getIntent().getSerializableExtra("bind_data");
		bindfrom = getIntent().getStringExtra("bindfrom");
		openid = getIntent().getStringExtra("openid");
	}
	
	@Override
	protected void setupView() {
		TextView tv_title = (TextView) findViewById(R.id.top_title);
		tv_title.setText(getString(R.string.bind_phone));
		
		btn_register = (Button) findViewById(R.id.btn_register);
		btn_register.setText("绑定");
		
		
		ckServiceItem = (CheckBox) findViewById(R.id.ckServiceItem);
		ckServiceItem.setChecked(true);
		etAccount = (EditText) findViewById(R.id.et_account);
		etPwd = (EditText) findViewById(R.id.et_pwd);
		et_verify_code = (EditText) findViewById(R.id.et_verify_code);
		btn_second = (Button) findViewById(R.id.btn_second);
		tv_protocol = (TextView) findViewById(R.id.tv_protocol);
		tv_protocol.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); // 下划线
		tv_protocol.getPaint().setAntiAlias(true);// 抗锯齿
		manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		
		iv_show_pswd = (ImageView) findViewById(R.id.iv_show_pwd);

		HandlerThread mHandlerThread = new HandlerThread("count", 5);
		mHandlerThread.start();
		mTimeHandler = new Handler(mHandlerThread.getLooper());
		try {
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					InputMethodManager inputManager = (InputMethodManager) BindByMobileActivity.this
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					inputManager.toggleSoftInput(0,InputMethodManager.HIDE_NOT_ALWAYS);
					etAccount.setFocusable(true);
					etAccount.setFocusableInTouchMode(true);
					etAccount.requestFocus();
				}
			}, 200L);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	private void phone_verify_code(final String phoneNum) {
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("mobile", phoneNum);
		params.put("ispass", "1");
		params.put("utype", "user");
		params.put("appversion", PublicUtil.getVersionName(this));
		params.put("channel", "android");

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
	
	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
	
	private void bind() {
		if (TextUtils.isEmpty(etAccount.getText().toString())
				|| etAccount.getText().toString().trim().length() == 0) {
			showToast("请输入手机号");
			return;
		}
		if (TextUtils.isEmpty(et_verify_code.getText().toString())
				|| et_verify_code.getText().toString().trim().length() == 0) {
			showToast("请输入验证码");
			return;
		}
		String verify_code_sms = et_verify_code.getText().toString();
		if (!verify_code_sms.equals(verifyCode + "")) {
			showToast("验证码不正确,请重新获取");
			return;
		}

		if (TextUtils.isEmpty(etPwd.getText().toString())
				|| etPwd.getText().toString().trim().length() == 0) {
			showToast("请输入密码");
			return;
		}

		if (!ckServiceItem.isChecked()) {
			showToast("请阅读并同意薏米服务条款");
			return;
		}
		
		loading(null);
		String mobile = etAccount.getText().toString().trim();
		String password = etPwd.getText().toString().trim();
		
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("openid", this.openid);
		params.put("bindfrom", this.bindfrom);
		params.put("mobile", mobile);
		params.put("password", password);
		params.put("memberid", entry.getMEMBERID());

		MyApplication.volleyHttpClient.postWithParams(HttpUtil.bindding_other_account,
				BindInfo.class, params, new Response.Listener() {
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
	protected void onResult(Message msg) {
		try {
			switch (msg.what) {
			case IResult.RESULT://绑定成功
				destroyDialog();
				BindInfo binder = (BindInfo) msg.obj;
				if (binder.getSuccess() == 1) {
					// logonSuccess
					SharePrefUtil.putBoolean(Conast.LOGIN, true);
					SharePrefUtil.putBoolean(Conast.VALIDATED, true);
					SharePrefUtil.putString(Conast.MEMBER_ID, binder.getData().getMEMBERID());
					SharePrefUtil.putString(Conast.USER_Name, binder.getData().getMEMBERNAME());
					SharePrefUtil.putString(Conast.realName, binder.getData().getREALNAME());
					SharePrefUtil.putString(Conast.ACCESS_TOKEN, binder.getData().getTOKEN());
					SharePrefUtil.putString(Conast.AVATAR, binder.getData().getAVATAR());
					SharePrefUtil.putString(Conast.USER_SEX,binder.getData().getSEX());
					SharePrefUtil.putString(Conast.USER_MOBILE,binder.getData().getMOBILE());
					SharePrefUtil.putString(Conast.USER_BIRTHDAY,binder.getData().getBIRTHDAY());
//					SharePrefUtil.putString(Conast.USER_CARDID,register.getData().getCERTIFICATENUM());
					SharePrefUtil.putString(Conast.USER_ADDRESS,binder.getData().getADDRESS());
//					SharePrefUtil.putString(Conast.USER_CLINICCARDNUM,register.getData().getCARDNUMBER());
					
					SharePrefUtil.commit();

					EventBus.getDefault().postSticky(new RegisterSuccessEvent());
					
					Intent intent = new Intent(BindByMobileActivity.this,MainActivity.class);
//					intent.putExtra("account", etAccount.getText().toString().trim());
//					intent.putExtra("pwd", etPwd.getText().toString().trim());
					startActivity(intent);
					finish();
				} else {
				}
				showToast(binder.getErrormsg());
				break;
			case IResult.PHONE_VERIFY_CODE://发送验证码成功
				PhoneVerifyInfo phoneVerify = (PhoneVerifyInfo) msg.obj;
				if (phoneVerify.getSuccess() == 1) {
					verifyCode = phoneVerify.getData().getCODE();
					btn_second.setClickable(false);
					btn_second.setEnabled(false);
					btn_second.setSelected(true);
					btn_second.setTextColor(getResources().getColor(R.color.light_gray));
					time = 120;
					terminateCount = false;
					mTimeHandler.post(oneSecondThread);
					showToast("验证码已发送,请注意查收短信");
				} else {
					if (!TextUtils.isEmpty(phoneVerify.getErrormsg())) {
						showToast(phoneVerify.getErrormsg());
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
				Toast.makeText(this, "网络错误", Toast.LENGTH_SHORT).show();
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}
	//验证码倒数
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
				btn_second.setText(msg.arg1 + " 秒");
				mTimeHandler.post(oneSecondThread);
			} else {
				btn_second.setTextColor(getResources().getColor(R.color.black));
				btn_second.setText("重新获取验证码");
				btn_second.setClickable(true);
				btn_second.setEnabled(true);
				btn_second.setSelected(false);
				terminateCount = true;
			}
		}
	};
	private Button btn_register;

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(RegisterSuccessEvent.class);
	}

	//点击事件
	public void doClick(View view) {
		int id = view.getId();
		if (id == R.id.btn_second) {//发送验证码
			if (NetWorkUtils.detect(BindByMobileActivity.this)) {
				if (TextUtils.isEmpty(etAccount.getText().toString())) {
					showToast(getString(R.string.register_account_length_null));
				} else {
					String accountNum = etAccount.getText().toString().trim();
					if (accountNum.length() == 11) {
						hideKeyboard();
						btn_second.setClickable(false);
						btn_second.setEnabled(false);
						btn_second.setSelected(true);
						btn_second.setTextColor(getResources().getColor(R.color.light_gray));
						phone_verify_code(etAccount.getText().toString().trim());
					} else {
						showToast(getString(R.string.register_account_length));
					}
				}
			} else {
				showToast(getString(R.string.net_error));
			}
		} else if (id == R.id.btn_register) {//绑定手机
			if (NetWorkUtils.detect(BindByMobileActivity.this)) {
				if(!TextUtils.isEmpty(openid) && !TextUtils.isEmpty(bindfrom) && entry!=null && !TextUtils.isEmpty(entry.getMEMBERID())){
					bind();
					EventBus.getDefault().postSticky(new RegisterSuccessEvent());
				}
			} else {
				showToast(getString(R.string.net_error));
			}
		} else if (id == R.id.btn_back) {//关闭的按钮
			MyApplication.logoutThird(this);
			finish();
		} else if (id == R.id.iv_show_pwd) {//显示和隐藏密码的按钮
			if (isShowingPwd) {
				// 隐藏密码
				isShowingPwd = false;
				etPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
				iv_show_pswd.setImageResource(R.drawable.ic_eye_close);
			} else {
				// 显示密码
				isShowingPwd = true;
				etPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				iv_show_pswd.setImageResource(R.drawable.ic_eye_open);
			}
		}
	}
}
