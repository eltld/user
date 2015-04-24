package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.PersonInfo;
import net.ememed.user2.entity.SetPwd;
import net.ememed.user2.fragment.PwdSettingFragment;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.CharUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.MyProgressDialog;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.CustomViewPager;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
/**
 * 密码修改
 * @author pengwl
 *
 */
public class SettingPwdActivity extends BasicActivity {

	private EditText et_old_pwd;
	private EditText et_new_pwd;
	private EditText et_confirm_pwd;
	private Button btn_save;
	private String oldPwd;
	private String newPwd;
	private int type;
	private TextView top_title;
	private InputMethodManager manager;
	

//	@Override
//	protected void onCreate(Bundle arg0) {
//		super.onCreate(arg0);
//		setContentView(R.layout.fragment_pwdsetting);
//		// setContentView(R.layout.layout_basic_info);
//		// btn = (Button) findViewById(R.id.btn_addhealth);
//		// btn.setVisibility(View.GONE);
//		// TextView tv_title = (TextView)findViewById(R.id.top_title);
//		// tv_title.setText(getString(R.string.act_title_stting_pwd));
//		// String[] titles = getResources().getStringArray(R.array.pwd_type);
//		// mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this);
//		//
//		// for (int i = 0; i < titles.length; i++) {
//		// Bundle bundle = new Bundle();
//		// bundle.putString("fragmentKey", titles[i]);
//		// mPagerAdapter.addFragment(titles[i], PwdSettingFragment.class,
//		// bundle);
//		// }
//		// mViewPager = (CustomViewPager) findViewById(R.id.pager);
//		// mViewPager.setOffscreenPageLimit(2);//缓存多少个页面
//		// mViewPager.setAdapter(mPagerAdapter);
//		// mIndicator = (TabPageIndicator) findViewById(R.id.indicator);
//		// mIndicator.setViewPager(mViewPager);
//		// mIndicator.setOnPageChangeListener(this);
//
//	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.fragment_pwdsetting);
	}

	@Override
	protected void setupView() {
		manager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
		et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
		et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
		btn_save = (Button) findViewById(R.id.btn_save);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(getString(R.string.act_title_stting_pwd));
		super.setupView();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_save) {
			amendPwd();
		}
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

	// 修改密码
	private void amendPwd() {
		try {
			oldPwd = et_old_pwd.getText().toString();
			newPwd = et_new_pwd.getText().toString();
			String confirmPwd = et_confirm_pwd.getText().toString();

			if ("".equals(oldPwd)) {
				showToast("原密码不能为空");
				throw new Exception("原密码不能为空");
			}
			if ("".equals(newPwd)) {
				showToast("新密码不能为空");
				throw new Exception("新密码不能为空");
			}
			if (type == 1) {
				if (newPwd.length() < 4 || newPwd.length() > 16) {
					showToast("密码长度错误");
					throw new Exception("密码长度错误");
				}
			} else if (type == 2) {
				if (!CharUtil.isNum(newPwd)) {
					showToast("语音密码必须为6位数字");
					throw new Exception("语音密码必须为6位数字");
				}
			}
			if (!newPwd.equals(confirmPwd)) {
				showToast("确认密码与新密码不一致");
				throw new Exception("确认密码与新密码不一致");
			}

			if (NetWorkUtils.detect(this)) {
				hideKeyboard();
				loading(null);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("token",
						SharePrefUtil.getString(Conast.ACCESS_TOKEN));
				params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
				params.put("oldpwd", oldPwd);
				params.put("newpwd", newPwd);
				params.put("pwdtype", "1");
				MyApplication.volleyHttpClient.postWithParams(
						HttpUtil.set_user_password, SetPwd.class, params,
						new Response.Listener() {
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
			} else {
				handler.sendEmptyMessage(IResult.NET_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	protected void onResult(Message msg) {
		try {
			destroyDialog();
			switch (msg.what) {
			case IResult.RESULT:
				SetPwd setPwd = (SetPwd) msg.obj;
				if(null != setPwd){
					if(setPwd.getSuccess() == 1){
						showToast("密码修改成功");
						finish();
					}else {
						showToast("原密码不正确");
						et_old_pwd.setText("");
					}
				}
				break;
			case IResult.END:
				
				break;
			case IResult.NET_ERROR:
				showMessage(IMessage.NET_ERROR);
				break;

			case IResult.DATA_ERROR:
				showMessage(IMessage.DATA_ERROR);
				break;
			default:
				break;
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	/** 信息提示 */
	public void showMessage(String msg) {
		Builder builder = new Builder(this);
		Dialog dialog = builder
				.setTitle(getString(R.string.system_info))
				.setMessage(msg)
				.setPositiveButton(getString(R.string.add_health_record_know),
						null).create();
		dialog.setCancelable(false);
		if (!finish) {
			dialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish = true;
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.set_user_password);
	}

	private boolean finish = false;

}
