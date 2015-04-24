package net.ememed.user2.fragment;

import java.util.HashMap;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.PersonInfoActivity;
import net.ememed.user2.activity.SettingPwdActivity;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.entity.UserInfo;
import net.ememed.user2.entity.UserInfoEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.MenuDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class InfoSettingFragment extends Fragment implements Handler.Callback,
		OnClickListener, OnRefreshListener {

	private String fragmentKey = "";
	private PersonInfoActivity activity = null;
	private Handler mHandler;
	int type = 1;
	private LinearLayout ll_birthday, ll_sex, ll_update_pwd;
	private RelativeLayout ll_view_content;
	private RelativeLayout ll_view_content_guahao;
	private FrameLayout mContentView = null;
	private TextView et_sex,tv_phone,et_birthday;
	private EditText et_identity_card, et_medical_num, et_address,et_name;
	private Button btn_save;
	private Button btn_save_guahao;

	private int sex;
//	private PullToRefreshLayout mPullToRefreshLayout;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler(this);
		activity = (PersonInfoActivity) getActivity();
		fragmentKey = getArguments().getString("fragmentKey");
//		System.out.println("TestFragment onCreate fragmentKey : " + fragmentKey);
		
	}
	

	public String getFragmentKey() {
		return fragmentKey;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		System.out.println("TestFragment onCreateView fragmentKey : " + fragmentKey);
		View view = inflater.inflate(R.layout.root_layout, null);
		mContentView = (FrameLayout) view.findViewById(R.id.mainView);
		getUserInfo();
		if ("基本资料".equals(fragmentKey)) {
			setupBaseProfile();
		} else if ("挂号资料".equals(fragmentKey)) {
			setupGuahaoProfile();
		}
		return view;
	}

	private void setupGuahaoProfile() {
		ll_view_content_guahao = (RelativeLayout) LayoutInflater.from(activity)
				.inflate(R.layout.fragment_set_guahao_profile, null);
		mContentView.addView(ll_view_content_guahao);
		et_identity_card = (EditText) ll_view_content_guahao
				.findViewById(R.id.et_identity_card);
		et_medical_num = (EditText) ll_view_content_guahao
				.findViewById(R.id.et_medical_num);
		et_address = (EditText) ll_view_content_guahao
				.findViewById(R.id.et_address);
		btn_save_guahao = (Button) ll_view_content_guahao
				.findViewById(R.id.btn_save_guahao);
		btn_save_guahao.setOnClickListener(this);

		et_identity_card.setText(SharePrefUtil.getString(Conast.USER_CARDID));
		et_medical_num.setText(SharePrefUtil
				.getString(Conast.USER_CLINICCARDNUM));
		et_address.setText(SharePrefUtil.getString(Conast.USER_ADDRESS));
		
	}



	private void getUserInfo() {
		// TODO Auto-generated method stub
		if (NetWorkUtils.detect(getActivity())) {
			activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));

			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_user_info, UserInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.USER_INFO;
							mHandler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	private void setupBaseProfile() {
		ll_view_content = (RelativeLayout) LayoutInflater.from(activity).inflate(R.layout.fragment_set_base_profil, null);
		mContentView.addView(ll_view_content);
		ll_sex = (LinearLayout) ll_view_content.findViewById(R.id.ll_sex);
		et_name = (EditText) ll_view_content.findViewById(R.id.et_name);
		et_name.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				// TODO Auto-generated method stub
				if(hasFocus){
					et_name.setSelection(et_name.getText().toString().length());	
				}
				
			}
		});
		et_sex = (TextView) ll_view_content.findViewById(R.id.et_sex);
		ll_sex.setOnClickListener(this);
		et_birthday = (TextView) ll_view_content.findViewById(R.id.et_birthday);
		tv_phone = (TextView) ll_view_content.findViewById(R.id.tv_phone);
		ll_birthday = (LinearLayout) ll_view_content.findViewById(R.id.ll_birthday);
		ll_birthday.setOnClickListener(this);
		ll_update_pwd = (LinearLayout) ll_view_content.findViewById(R.id.ll_update_pwd);
		ll_update_pwd.setOnClickListener(this);
		btn_save = (Button) ll_view_content.findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		System.out.println("TestFragment onSaveInstanceState fragmentKey : "+ fragmentKey);
		if ("基本资料".equals(fragmentKey)) {
			type = 1;
		} else if ("挂号资料".equals(fragmentKey)) {
			type = 2;
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_save) {
			setBaseProfile();
		} else if (v.getId() == R.id.btn_save_guahao) {
			setGuahaoProfile();
		} else if (v.getId() == R.id.ll_birthday) {
			setBirthday();
		} else if (v.getId() == R.id.ll_sex) {
			setSex();
		} else if (v.getId() == R.id.ll_update_pwd) {
			startActivity(new Intent(activity,SettingPwdActivity.class));
		}
	}

	// 设置性别
	private void setSex() {

		final String[] items = new String[2];
		items[0] = getString(R.string.add_userseek_girl);
		items[1] = getString(R.string.add_userseek_boy);

		MenuDialog.Builder builder = new MenuDialog.Builder(activity);
		MenuDialog dialog = builder.setTitle(R.string.getuser_setsex)
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						et_sex.setText(items[which]);
						sex = which;
						dialog.cancel();
					}
				}).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();

	}

	// 设置生日
	private void setBirthday() {
		DatePickerDialog dialog = new DatePickerDialog(activity,
				new OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						et_birthday.setText(year + "-"
								+ String.format("%02d", (monthOfYear + 1))
								+ "-" + String.format("%02d", dayOfMonth));
					}
				}, 2013, 0, 1);
		dialog.show();
	}

	/** 挂号资料 */
	public void setGuahaoProfile() {
		String etIdentityCard = et_identity_card.getText().toString().trim();
		String etMedicalNum = et_medical_num.getText().toString().trim();
		String etAddress = et_address.getText().toString().trim();
		if (NetWorkUtils.detect(getActivity())) {
//			mPullToRefreshLayout.setRefreshing(true);
			activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("cardid", etIdentityCard);
			params.put("address", etMedicalNum);
			params.put("cliniccardnum", etAddress);

			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.set_guahao_profile, SetProfile.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GUAHAO_PROFILE;
							mHandler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	/** 基本资料 */
	public void setBaseProfile() {
		String etName = et_name.getText().toString().trim();
		String etSex = et_sex.getText().toString().trim();
		String etBirthday = et_birthday.getText().toString().trim();
		String etPhone = tv_phone.getText().toString().trim();
		

		if (NetWorkUtils.detect(getActivity())) {
//			mPullToRefreshLayout.setRefreshing(true);
			activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("realname", etName);
			params.put("sex", etSex.equals("男") ? "1" : "0");
			params.put("mobile", etPhone);
			params.put("birthday", etBirthday);

			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.set_base_profile, SetProfile.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.BASE_PROFILE;
							mHandler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		activity.destroyDialog();
		try {
			switch (msg.what) {
			case IResult.BASE_PROFILE:
//				mPullToRefreshLayout.setRefreshComplete();
				SetProfile sp = (SetProfile) msg.obj;
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						SharePrefUtil.putString(Conast.realName, et_name.getText().toString().trim());
						SharePrefUtil.putString(Conast.USER_SEX, et_sex.getText().toString().trim());
						SharePrefUtil.putString(Conast.USER_BIRTHDAY,et_birthday.getText().toString().trim());
						SharePrefUtil.putString(Conast.USER_MOBILE, tv_phone.getText().toString().trim());
						SharePrefUtil.commit();
						activity.showToast("修改个人资料成功");
					} else {
						activity.showToast(sp.getErrormsg());
					}
				} else {
					activity.showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.GUAHAO_PROFILE:
//				mPullToRefreshLayout.setRefreshComplete();
				SetProfile sp2 = (SetProfile) msg.obj;
				if (null != sp2) {
					if (sp2.getSuccess() == 1) {
						SharePrefUtil.putString(Conast.USER_CARDID,
								et_identity_card.getText().toString().trim());
						SharePrefUtil.putString(Conast.USER_CLINICCARDNUM,
								et_medical_num.getText().toString().trim());
						SharePrefUtil.putString(Conast.USER_ADDRESS, et_address
								.getText().toString().trim());
						SharePrefUtil.commit();
						activity.showToast("修改挂号资料成功");
					} else {
						activity.showToast(sp2.getErrormsg());
					}
				} else {
					activity.showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.USER_INFO:
				UserInfo userInfo = (UserInfo) msg.obj;
				if (null != userInfo) {
					if (userInfo.getSuccess() == 1) {
						UserInfoEntry data = userInfo.getData();
						et_name.setText(data.getREALNAME());
						et_sex.setText(data.getSEX().equals("1") ? "男":"女");
						et_birthday.setText(data.getBIRTHDAY());
						tv_phone.setText(data.getMOBILE());
					} else {
						activity.showToast(userInfo.getErrormsg());
					}
				} else {
					activity.showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				activity.showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onRefreshStarted(View view) {

	}

}
