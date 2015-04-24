package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.db.CityConfigTable;
import net.ememed.user2.db.CityTable;
import net.ememed.user2.db.InviteMessgeDao;
import net.ememed.user2.db.UserDao;
import net.ememed.user2.entity.AdsEntry;
import net.ememed.user2.entity.AppConfig;
import net.ememed.user2.entity.AreaInfo;
import net.ememed.user2.entity.ChatUser;
import net.ememed.user2.entity.CityEntry;
import net.ememed.user2.entity.Geo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.InviteMessage;
import net.ememed.user2.entity.InviteMessage.InviteMesageStatus;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.entity.VersionInfo;
import net.ememed.user2.fragment.HomeFragment;
import net.ememed.user2.fragment.InfoTabFragment;
import net.ememed.user2.fragment.MineFragment;
import net.ememed.user2.fragment.MyDoctorFragment;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.AppContext;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.TimeUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CityHintDialog;
import net.ememed.user2.widget.MenuDialog;

import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMNotifier;
import com.easemob.util.HanziToPinyin;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.umeng.update.UmengUpdateAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.LogonSuccessEvent;
import de.greenrobot.event.util.BDLocationCallback;
import de.greenrobot.event.util.BDLocationTool;
import de.greenrobot.event.util.EMLoginCallBack;
import de.greenrobot.event.util.IMManageTool;
//import android.location.Criteria;
//import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;

/***
 * 主页
 * 
 * @author chen
 */
public class MainActivity extends BasicActivity implements BasicUIEvent,
		EMLoginCallBack, OnClickListener {
	protected static final String TAG = MainActivity.class.getSimpleName();

	public static boolean unReadFlag = false;

	private ImageButton tab_home;
	private ImageButton tab_people;
	private ImageButton tab_service;
	private ImageButton tab_me;
	private TextView top_title;
	private TextView tv_tab_contact;
	private TextView tv_tab_home;
	private TextView tv_tab_service;
	private TextView tv_tab_me;
	public int Height = 0;
	private int currentTabIndex = 0;

	private float self_weidu;
	private float self_jindu;
	private LocationManager loctionManager;

	FragmentManager fm = getSupportFragmentManager();
	public AppConfig appConfig;

	/*
	 * public LocationClient mLocationClient = null; public MyLocationListenner
	 * myListener = new MyLocationListenner();
	 */
	private RelativeLayout iv_redIcon; // 有未读消息的小红点
	private IMManageTool manageTool;

	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		AppContext.setContext(this);
		// getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		// // 弹出键盘不重新布局以避免底部上移
//		EMChatManager.getInstance().loadAllConversations();
		EventBus.getDefault().registerSticky(this, LogonSuccessEvent.class);
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);
		updateVersion();

		// 获得参数（病历夹和健康档案）
		// getAffigData();
		// UICore.eventTask(this, this, IResult.GET_CITY_DATA, null, null);
		getAffigData();
//		UICore.eventTask(this, this, IResult.GET_CITY_DATA, null, null);
		if (SharePrefUtil.getBoolean("savepwd")
				&& SharePrefUtil.getBoolean(Conast.LOGIN)) {
			JPushInterface.init(getApplicationContext());
			JPushInterface.resumePush(getApplicationContext());
		}
		cityTable = new CityTable();
		manageTool = IMManageTool.getInstance(getApplicationContext());
		manageTool.setEMLoginCallBack(this);
		connectionIM();

		String lon = SharePrefUtil.getString("lon");
		String lat = SharePrefUtil.getString("lat");

		if (lon.equals("") || lat.equals("")) {
			baiduLocation();
		} else {

			checkAppVersion(lon, lat);
		}
	}

	public void baiduLocation() {

		BDLocationTool locationTool = new BDLocationTool(this);
		locationTool.startBaiduLocation(new BDLocationCallback() {
			@Override
			public void LocationCallback(BDLocation location) {
				// TODO Auto-generated method stub
				if (location != null) {
					String lat = String.valueOf(location.getLatitude());
					String lon = String.valueOf(location.getLongitude());
					SharePrefUtil.putString("lat", lat);
					SharePrefUtil.putString("lon", lon);
					SharePrefUtil.commit();
					checkAppVersion(lon, lat);
				} else {
					showToast("定位失败，请退出重试");
				}
			}
		});
	}

	/**
	 * update by umeng
	 */
	private void updateVersion() {
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
	}

	// private void getAffigData() {
	// try {
	// // if (TimeUtil.checkIsNeedUpdate())
	// // {//该标记代表每天第一次启动，目前每次进入应用都需要调用更新版本接口，所以先注释掉
	// HashMap<String, String> params = new HashMap<String, String>();
	// params.put("imei", PublicUtil.getDeviceUuid(this));
	// params.put("appversion", PublicUtil.getVersionName(this));
	// params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
	// params.put("membertype", "user");
	// params.put("channel", "android");
	// params.put("appid", "3");
	// params.put("mobileparam",
	// MyApplication.getInstance().getMobileParam(this));
	//
	// MyApplication.volleyHttpClient.postWithParams(
	// HttpUtil.get_version_info, VersionInfo.class, params,
	// new Response.Listener() {
	// @Override
	// public void onResponse(Object response) {
	// Message message = new Message();
	// message.obj = response; // version类使用JSON，可传函数回调取值/
	// message.what = IResult.AFFIG_DATA;
	// handler.sendMessage(message);
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// MyApplication.volleyHttpClient
	// .cancelRequest(HttpUtil.get_version_info);
	// }
	// });
	//
	// // }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
	private void getAffigData() {
		try {
			// if (TimeUtil.checkIsNeedUpdate())
			// {//该标记代表每天第一次启动，目前每次进入应用都需要调用更新版本接口，所以先注释掉
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("imei", PublicUtil.getDeviceUuid(this));
			params.put("appversion", PublicUtil.getVersionName(this));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("membertype", "user");
			params.put("channel", "android");
			params.put("appid", "3");
			params.put("mobileparam", MyApplication.getInstance().getMobileParam(this));
			
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_version_info, VersionInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response; // version类使用JSON，可传函数回调取值/
							message.what = IResult.AFFIG_DATA;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							MyApplication.volleyHttpClient
									.cancelRequest(HttpUtil.get_version_info);
						}
					});

			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onEvent(LogonSuccessEvent logonEvent) {
		Logger.dout("finish success");
		finish();
	}

	@Override
	protected void setupView() {
		super.setupView();

		View view_head_layout = LayoutInflater.from(this).inflate(
				R.layout.top_bar_doctor_green, null);

		ImageView btn_back = (ImageView) view_head_layout
				.findViewById(R.id.btn_back);

		/*
		 * btn_addhealth = (Button)
		 * view_head_layout.findViewById(R.id.btn_addhealth);
		 * 
		 * btn_addhealth = (Button) view_head_layout
		 * .findViewById(R.id.btn_addhealth);
		 * 
		 * btn_addhealth.setVisibility(View.VISIBLE);
		 * btn_addhealth.setText("意见");
		 * btn_addhealth.setBackgroundColor(getResources().getColor(
		 * R.color.topbg_green)); btn_addhealth.setOnClickListener(new
		 * OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // TODO Auto-generated method
		 * stub startActivity(new Intent(MainActivity.this,
		 * FeedbackActivity.class)); } });
		 */
		btn_back.setVisibility(View.GONE);
		view_head_layout.setVerticalFadingEdgeEnabled(false);
		view_head_layout.setHorizontalFadingEdgeEnabled(false);
		view_head_layout.setFadingEdgeLength(0);
		// 得到Activity的ActionBar
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
				android.view.ViewGroup.LayoutParams.FILL_PARENT,
				android.view.ViewGroup.LayoutParams.FILL_PARENT);
		actionBar.setCustomView(view_head_layout, layoutParams);

		home_main_layout = (FrameLayout) findViewById(R.id.home_main_layout);
		home_main_layout.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {

					@Override
					public void onGlobalLayout() {
						if (home_main_layout.getHeight() != 0) {
							home_main_layout.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
							Height = home_main_layout.getHeight()
									- Util.dip2px(getApplicationContext(), 48);
							initFragment();
						}
					}
				});

		/** 顶部 和 底部 的组件 */
		top_title = (TextView) view_head_layout.findViewById(R.id.top_title);
		top_title.setText(getString(R.string.act_title_home));
		top_title.setBackgroundResource(R.drawable.top_logo);
		tab_home = (ImageButton) findViewById(R.id.home_tab_home);
		tab_people = (ImageButton) findViewById(R.id.home_tab_people);
		tab_service = (ImageButton) findViewById(R.id.home_tab_service);
		tab_me = (ImageButton) findViewById(R.id.home_tab_me);
		tab_home.setSelected(true);

		tv_tab_home = (TextView) findViewById(R.id.tv_tab_bottom_home);
		tv_tab_contact = (TextView) findViewById(R.id.tv_tab_bottom_contact);
		tv_tab_service = (TextView) findViewById(R.id.tv_tab_bottom_service);
		tv_tab_me = (TextView) findViewById(R.id.tv_tab_bottom_me);
		// iv_redIcon = (RelativeLayout)
		// findViewById(R.id.home_tab_people_count_image);
		// 登录聊天服务器
	}

	@Override
	protected void onStart() {
		super.onStart();
		loginIM();
		if (SharePrefUtil.getBoolean(Conast.LOGIN)
				&& !TextUtils
						.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
			updateUnreadLabel();
		}
		checkOldVer();
	}

	/**
	 * 初始化4个页面
	 */
	private void initFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();

		// contactFragment = new MyDoctorFragment();
		// ft.add(R.id.home_main_layout, contactFragment,
		// MyDoctorFragment.class.getSimpleName());
		// ft.hide(contactFragment);
		//
		// serviceFragment = new InfoTabFragment();
		// ft.add(R.id.home_main_layout, serviceFragment,
		// InfoTabFragment.class.getSimpleName());
		// ft.hide(serviceFragment);
		//
		// mineFragment = new MineFragment();
		// ft.add(R.id.home_main_layout, mineFragment,
		// MineFragment.class.getSimpleName());
		// ft.hide(mineFragment);
		//
		homeFragment = new HomeFragment();
		ft.add(R.id.home_main_layout, homeFragment, "homefragment");
		ft.show(homeFragment);
		ft.commit();
		lastFragment = homeFragment;
	}

	public void doClick(View view) {
		try {
			int id = view.getId();
			// changeTabAction(id);
			if (id == R.id.rl_tab_home) {
				/* btn_addhealth.setVisibility(View.VISIBLE); */
				if (homeFragment != null)
					homeFragment.initHome();
				changeTabAction(R.id.rl_tab_home);
			} else if (id == R.id.rl_tab_contact) {
				/* btn_addhealth.setVisibility(View.GONE); */
				if (homeFragment != null)
					homeFragment.initHome();
				changeTabAction(R.id.rl_tab_contact);
			} else if (id == R.id.ll_tab_service) {
				/* btn_addhealth.setVisibility(View.GONE); */
				if (homeFragment != null)
					homeFragment.initHome();
				changeTabAction(R.id.ll_tab_service);
			} else if (id == R.id.ll_tab_me) {
				/* btn_addhealth.setVisibility(View.GONE); */
				if (homeFragment != null)
					homeFragment.initHome();
				changeTabAction(R.id.ll_tab_me);
			} else if (id == R.id.btn_free_consult) {
			} else if (id == R.id.btn_back) {
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Fragment lastFragment = null;
	private HomeFragment homeFragment = null;
	private MyDoctorFragment contactFragment = null;
	// private ChatTabFragment contactFragment = null;
	private InfoTabFragment serviceFragment = null;
	private MineFragment mineFragment = null;

	/** 把fragment 添加到 activity中 */
	private void changeTabAction(int layoutId) {
		// 继承支持库类FragmentActivity获取FragmentManager对象的方法
		// 若继承Activity使用getFragmentManager()
		if (fm == null)
			fm = getSupportFragmentManager();
		FragmentTransaction ft = fm.beginTransaction();
		if (layoutId == R.id.rl_tab_home) {

			if (lastFragment != homeFragment) {
				ft.hide(lastFragment);// 隐藏
				if (homeFragment == null) {// 查看fragment事务队列中是否存在此事物
					homeFragment = new HomeFragment();
					ft.add(R.id.home_main_layout, homeFragment,
							HomeFragment.class.getSimpleName());
				}
				ft.show(homeFragment);
				ft.commit(); // 提交事务（资源ID作为唯一标识符且通知视图位置）
				lastFragment = homeFragment;

				top_title.setText(getString(R.string.act_title_home));
				top_title.setBackgroundResource(R.drawable.top_logo);
				tab_home.setSelected(true);
				tab_me.setSelected(false);
				tab_people.setSelected(false);
				tab_service.setSelected(false);

				tv_tab_home.setTextColor(getResources().getColor(
						R.color.topbg_green));
				tv_tab_contact.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_service.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_me.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				currentTabIndex = 0;
			}

		} else if (layoutId == R.id.rl_tab_contact) {
			if (lastFragment != contactFragment) {
				ft.hide(lastFragment);
				if (contactFragment == null) {
					contactFragment = new MyDoctorFragment();
					ft.add(R.id.home_main_layout, contactFragment,
							MyDoctorFragment.class.getSimpleName());
				}
				ft.show(contactFragment);
				ft.commit();
				lastFragment = contactFragment;
				top_title.setText(getString(R.string.act_title_contact));
				top_title.setBackgroundResource(android.R.color.transparent);
				tab_people.setSelected(true);
				tab_me.setSelected(false);
				tab_home.setSelected(false);
				tab_service.setSelected(false);
				tv_tab_home.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_contact.setTextColor(getResources().getColor(
						R.color.topbg_green));
				tv_tab_service.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_me.setTextColor(getResources().getColor(
						R.color.tab_light_gray));

				currentTabIndex = 1;
			}
		} else if (layoutId == R.id.ll_tab_service) {
			if (lastFragment != serviceFragment) {
				ft.hide(lastFragment);
				if (serviceFragment == null) {
					serviceFragment = new InfoTabFragment();
					ft.add(R.id.home_main_layout, serviceFragment,
							InfoTabFragment.class.getSimpleName());
					// } else {
					// ft.show(serviceFragment);
				}
				ft.show(serviceFragment);
				ft.commit();
				lastFragment = serviceFragment;

				top_title.setText(getString(R.string.act_title_service));
				top_title.setBackgroundResource(android.R.color.transparent);
				tab_service.setSelected(true);
				tab_me.setSelected(false);
				tab_home.setSelected(false);
				tab_people.setSelected(false);

				tv_tab_home.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_contact.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_service.setTextColor(getResources().getColor(
						R.color.topbg_green));
				tv_tab_me.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				currentTabIndex = 2;
			}
		} else if (layoutId == R.id.ll_tab_me) {
			if (lastFragment != mineFragment) {
				ft.hide(lastFragment);
				if (mineFragment == null) {
					mineFragment = new MineFragment();
					ft.add(R.id.home_main_layout, mineFragment,
							MineFragment.class.getSimpleName());
				}
				ft.show(mineFragment);
				ft.commit();
				lastFragment = mineFragment;

				top_title.setText(getString(R.string.tab_bottom_me));
				top_title.setBackgroundResource(android.R.color.transparent);
				tab_me.setSelected(true);
				tab_service.setSelected(false);
				tab_home.setSelected(false);
				tab_people.setSelected(false);

				tv_tab_home.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_contact.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_service.setTextColor(getResources().getColor(
						R.color.tab_light_gray));
				tv_tab_me.setTextColor(getResources().getColor(
						R.color.topbg_green));
				currentTabIndex = 3;
			}
		}
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		if (mes == IResult.SYSN_CONFIG) {

		} else if (mes == IResult.GET_CITY_DATA) {

			new Thread() {
				boolean isflag = true;

				public void run() {
					CityConfigTable cityConfigTable = new CityConfigTable();
					while (isflag) {
						if (!cityConfigTable.isLocked()) {
							if (null == cityConfigTable
									|| cityConfigTable.getProvinces() == null
									|| TimeUtil.checkIsNeedUpdate()) {
								getCityListFromServer();
							}
							isflag = false;
						} else {
							try {
								Thread.sleep(50);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
						}
					}
				};
			}.start();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(LogonSuccessEvent.class);
	}

	CityHintDialog cityHintDialog;
	CityEntry cityEntry;

	@Override
	protected void onResult(Message msg) {
		switch (msg.what) {

		case IResult.CHECK_VERSION:
			VersionInfo versionInfo = (VersionInfo) msg.obj;
			if (versionInfo != null) {
				if (versionInfo.getSuccess() == 1) {
					Geo geo = versionInfo.getData().getGeo();

					if (geo != null) {

						cityHintDialog = new CityHintDialog(MainActivity.this,
								R.style.wheelDialog);
						String cityname = SharePrefUtil
								.getString(Conast.CITY_NAME);
						if (cityname.equals("")) {
							cityHintDialog.show();
							cityHintDialog.initDialog(MainActivity.this,
									CanvasWidth,
									"检测到您的定位城市为  " + geo.getAreaname()
											+ " 是否切换？");
							cityEntry = cityTable.findId(geo.getAreaid());
						} else {
							cityEntry = cityTable.findId(geo.getAreaid());
							if (cityEntry != null) {
								if (!cityname.equals(cityEntry.getAREANAME())) {
									cityHintDialog.show();
									cityHintDialog.initDialog(
											MainActivity.this, CanvasWidth,
											"检测到您的位置发生变化, 是否切换？");
								}
							}

						}

						// if (!TextUtils.isEmpty(geo.getAreaid()))
						// SharePrefUtil.putString(Conast.Areaid,
						// geo.getAreaid());
						// if (!TextUtils.isEmpty(geo.getAreaid())){
						// SharePrefUtil.putString(Conast.Areaname,
						// geo.getAreaid());
						// MyApplication.current_city = geo.getAreaid();
						// }
						// if (!TextUtils.isEmpty(geo.getAreatype()))
						// SharePrefUtil.putString(Conast.Areatype,
						// geo.getAreatype());
						// if (!TextUtils.isEmpty(geo.getParent_areaid()))
						// SharePrefUtil.putString(Conast.Parent_areaid,
						// geo.getParent_areaid());
						// SharePrefUtil.commit();
						// Logger.iout("测试", "城市为："+geo.getAreaid());
					}

//					int newVersionCode = Integer.valueOf(versionInfo.getData()
//							.getVERSIONCODE());
//					int oldVersionCode = PublicUtil.getVersionCode(this);
//					if (newVersionCode > oldVersionCode) {
//						alertVersionUpdate(versionInfo);
//					}
				} else if (versionInfo.getSuccess() == -1) {
					showExitTips();
				}
			}
			// Log.d("chenhj", "CHECK_VERSION");
			break;

		case IResult.AFFIG_DATA:
			VersionInfo versionInfo2 = (VersionInfo) msg.obj;
			if (versionInfo2 != null) {
				if (versionInfo2.getSuccess() == 1) {
					appConfig = versionInfo2.getData().getAppconfig();

					if (appConfig != null) {
						if (!TextUtils.isEmpty(appConfig.getModule_ehr()))
							// Util.MODULE_EHR = appConfig.getModule_ehr();
							SharePrefUtil.putString(Conast.Parent_module_ehr,
									appConfig.getModule_ehr()); // 健康档案
						SharePrefUtil.commit();
						if (!TextUtils.isEmpty(appConfig.getModule_emr()))
							// Util.MODULE_EMR = appConfig.getModule_emr();
							SharePrefUtil.putString(Conast.Parent_module_emr,
									appConfig.getModule_emr()); // 病历夹
						SharePrefUtil.commit();
					}

				}
			}
			break;
		case OLD_VER:
			deleteOld();
			break;

		default:
			break;
		}
	}

	// 获取城市列表
	private void getCityListFromServer() {

		if (NetWorkUtils.detect(this)) {

			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			// params.add(new BasicNameValuePair("token", SharePrefUtil
			// .getString(Conast.ACCESS_TOKEN)));
			// params.add(new BasicNameValuePair("channel", "android"));

			System.out.println("获取城市params = " + params);

			String result;

			try {
				result = HttpUtil.getString(HttpUtil.URI
						+ HttpUtil.get_all_area, params, HttpUtil.POST);
				result = TextUtil.substring(result, "{");
				Gson gson = new Gson();

				AreaInfo info = gson.fromJson(result, AreaInfo.class);
				if (null != info && null != info.getData()
						&& info.getData().size() > 0) {
					CityConfigTable cityConfigTable = new CityConfigTable();
					CityTable cityTable = new CityTable();
					cityConfigTable.clearTable();
					cityTable.clearTable();
					for (int i = 0; i < info.getData().size(); i++) {
						cityConfigTable.saveSortName(info.getData().get(i));
						cityTable.saveSortName(info.getData().get(i));

					}
				}
			} catch (JsonSyntaxException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销广播接收者

		try {
			unregisterReceiver(msgReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (ackMessageReceiver != null)
				unregisterReceiver(ackMessageReceiver);
		} catch (Exception e) {
			e.printStackTrace();
		}
		// try {
		// unregisterReceiver(contactInviteReceiver);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		EventBus.getDefault().removeStickyEvent(LogonSuccessEvent.class);
	}

	private NewMessageBroadcastReceiver msgReceiver;

	public void loginIM() {
		try {
			// 注册一个接收消息的BroadcastReceiver
			msgReceiver = new NewMessageBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter(EMChatManager
					.getInstance().getNewMessageBroadcastAction());
			intentFilter.setPriority(3);
			registerReceiver(msgReceiver, intentFilter);

			// 注册一个ack回执消息的BroadcastReceiver
			IntentFilter ackMessageIntentFilter = new IntentFilter(
					EMChatManager.getInstance().getAckMessageBroadcastAction());
			ackMessageIntentFilter.setPriority(3);
			registerReceiver(ackMessageReceiver, ackMessageIntentFilter);

			// 注册一个好友请求同意好友请求等的BroadcastReceiver
			// IntentFilter inviteIntentFilter = new
			// IntentFilter(EMChatManager
			// .getInstance().getContactInviteEventBroadcastAction());
			// registerReceiver(contactInviteReceiver, inviteIntentFilter);
			// setContactListener监听联系人的变化等
			// EMContactManager.getInstance().setContactListener(
			// new MyContactListener());
			// 注册一个监听连接状态的listener

			// 注册群聊相关的listener
			// EMGroupManager.getInstance().addGroupChangeListener(
			// new MyGroupChangeListener());
			// 通知sdk，UI 已经初始化完毕，注册了相应的receiver, 可以接受broadcast了

			// if (EMChatManager.getInstance() == null
			// || !EMChatManager.getInstance().isConnected()) {
			// logIM();
			// } else {
			// if (TextUtils.isEmpty(EMChatManager.getInstance()
			// .getCurrentUser()))
			// logIM();
			// }
			// EMChatManager.getInstance().addConnectionListener(
			// new MyConnectionListener());
			EMChat.getInstance().setAppInited();

		} catch (Exception e1) {
			e1.printStackTrace();
		}
	}

	private void updateUnreadLabel() {

		// if (EMChatManager.getInstance() != null &&
		// !TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser())) {

		int unreadMsgsCount = EMChatManager.getInstance().getUnreadMsgsCount();
		// if (unreadMsgsCount > 0) {// 有
		// iv_redIcon.setVisibility(View.VISIBLE);
		// } else {// 没有
		// iv_redIcon.setVisibility(View.GONE);
		// }

		// XXX 待處理
		// }

	}

	/**
	 * 新消息广播接收者
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);

			// 刷新bottom bar消息未读数
			updateUnreadLabel();
			if (currentTabIndex == 1) {
				// 当前页面如果为聊天历史页面，刷新此页面
				if (contactFragment != null) {
					contactFragment.refresh();
				}
			}
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					// 获取自定义的属性，第2个参数为返回的默认值
					String receive_custom_msg = msg.getStringAttribute(
							"attribute1", null);
					if (!TextUtils.isEmpty(receive_custom_msg)) {
						// System.out.println("receive_custom_msg:"+receive_custom_msg);
					}
					msg.isAcked = true;
				}
			}
			abortBroadcast();
		}
	};

	private BroadcastReceiver contactInviteReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 请求理由
			final String reason = intent.getStringExtra("reason");
			final boolean isResponse = intent.getBooleanExtra("isResponse",
					false);
			// 消息发送方username
			final String from = intent.getStringExtra("username");
			// 接到邀请的消息，如果不处理(同意或拒绝)，掉线后，服务器会自动再发过来，所以客户端不要重复提醒
			List<InviteMessage> msgs = inviteMessgeDao.getMessagesList();
			for (InviteMessage inviteMessage : msgs) {
				if (inviteMessage.getFrom().equals(from)) {
					return;
				}
			}

			InviteMessage msg = new InviteMessage();
			msg.setFrom(from);
			msg.setTime(System.currentTimeMillis());
			msg.setReason(reason);
			// sdk暂时只提供同意好友请求方法，不同意选项可以参考微信增加一个忽略按钮。
			if (!isResponse) {
				// Log.d(TAG, from + "请求加你为好友,reason: " + reason);
				// 设成未验证
				msg.setStatus(InviteMesageStatus.NO_VALIDATION);
				msg.setInviteFromMe(false);
			} else {
				// Log.d(TAG, from + "同意了你的好友请求");
				// 对方已同意你的请求
				msg.setStatus(InviteMesageStatus.AGREED);
				msg.setInviteFromMe(true);
			}
			// 保存msg
			inviteMessgeDao.saveMessage(msg);
			// 未读数加1
			ChatUser user = MyApplication.getInstance().getContactList()
					.get(Conast.NEW_FRIENDS_USERNAME);
			user.setUnreadMsgCount(user.getUnreadMsgCount() + 1);
			// 提示有新消息
			EMNotifier.getInstance(getApplicationContext()).notifyOnNewMsg();

			// 刷新bottom bar消息未读数
			// updateUnreadAddressLable();
			// // 刷新好友页面ui
			// if (currentTabIndex == 1)
			// contactListFragment.refresh();
			abortBroadcast();
		}
	};
	private InviteMessgeDao inviteMessgeDao;
	private UserDao userDao;

	private FrameLayout home_main_layout;

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
			user.setHeader(HanziToPinyin.getInstance()
					.get(headerName.substring(0, 1)).get(0).target.substring(0,
					1).toUpperCase());
			char header = user.getHeader().toLowerCase().charAt(0);
			if (header < 'a' || header > 'z') {
				user.setHeader("#");
			}
		}
	}

	public void showExitTips() {
		try {
			if (!MainActivity.this.isFinishing()) {

				MenuDialog.Builder alert = new MenuDialog.Builder(
						MainActivity.this);
				MenuDialog dialog = alert
						.setTitle(getString(R.string.system_title))
						.setMessage(getString(R.string.im_conflict))
						.setPositiveButton(getString(R.string.confirm_ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										logout();
									}
								}).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (KeyEvent.KEYCODE_BACK == keyCode) {
							return true;
						}
						return false;
					}
				});
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void logout() {
		JPushInterface.stopPush(getApplicationContext());
		try {
			new Thread() {
				public void run() {
					if (null != EMChatManager.getInstance()) {
						EMChatManager.getInstance().logout();
					}

					ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
					params.add(new BasicNameValuePair("userid", SharePrefUtil
							.getString(Conast.MEMBER_ID)));
					params.add(new BasicNameValuePair("channel", "android"));
					params.add(new BasicNameValuePair("imei", PublicUtil
							.getDeviceUuid(MainActivity.this)));
					params.add(new BasicNameValuePair("appversion", PublicUtil
							.getVersionName(MainActivity.this)));

					try {
						HttpUtil.getString(HttpUtil.URI + HttpUtil.logout,
								params, HttpUtil.POST);
					} catch (IOException e) {
						e.printStackTrace();

					}
				};
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(MainActivity.this, LoginActivity.class);
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
		SharePrefUtil.commit();
		startActivity(intent);
		finish();

	}

	// public void logIM() {
	// System.out.println("MyApplication.isLoginIM="+MyApplication.isLoginIM);
	// if (!MyApplication.isLoginIM) {
	// return;
	// }
	// try {
	// if (!TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) &&
	// !TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_Name)))
	// MyApplication.isLoginIMing = true;
	// // 调用sdk登录方法登录聊天服务器
	//
	//
	// manageTool.loginIM(SharePrefUtil.getString(Conast.MEMBER_ID),
	// MD5.getMD5(SharePrefUtil.getString(Conast.MEMBER_ID) +
	// SharePrefUtil.getString(Conast.USER_Name) + "ememedim"));
	//
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	private void reg_to_im() {
		try {
			if (NetWorkUtils.detect(getApplicationContext())) {
				new Thread() {
					public void run() {
						ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("token",
								SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
						params.add(new BasicNameValuePair("channel", "android"));
						params.add(new BasicNameValuePair("memberid",
								SharePrefUtil.getString(Conast.MEMBER_ID)));
						params.add(new BasicNameValuePair("utype", "user"));
						try {
							String result = HttpUtil
									.getString(HttpUtil.URI
											+ HttpUtil.reg_to_im, params,
											HttpUtil.POST);
							Gson gson = new Gson();
							ResultInfo info = gson.fromJson(result,
									ResultInfo.class);
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

	/**
	 * 弹出升级对话框
	 * 
	 * @param info
	 */
	@Deprecated
	private void alertVersionUpdate(final VersionInfo info) {
		View view = LayoutInflater.from(this).inflate(
				R.layout.version_update_info, null);
		final TextView tvVersionName = (TextView) view
				.findViewById(R.id.tv_version_name);
		final TextView tvTime = (TextView) view
				.findViewById(R.id.tv_update_time);
		final TextView tv_upgrade_title = (TextView) view
				.findViewById(R.id.tv_upgrade_title);
		tv_upgrade_title.setVisibility(View.GONE);
		final TextView tvSize = (TextView) view.findViewById(R.id.tv_size);
		final TextView tv_new_feature = (TextView) view
				.findViewById(R.id.tv_new_feature);
		tvVersionName.setText("薏米医生(Android) "
				+ info.getData().getVERSIONNAME());
		tvTime.setText("更新时间:" + info.getData().getUPDATETIME());
		tvSize.setText("文件大小:" + info.getData().getAPPSIZE());
		tv_new_feature.setText(info.getData().getCONTENT());
		boolean not_force_update = "1".equals(info.getData().getUPGRADEMODE());// 是否强制升级

		MenuDialog.Builder alert = new MenuDialog.Builder(this);
		MenuDialog dialog = null;

		if (!not_force_update) {// 不强制升级
			dialog = alert
					.setTitle(getString(R.string.main_notice))
					.setContentView(view)
					.setPositiveButton(getString(R.string.main_shenji),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										Uri uri = Uri.parse(info.getData()
												.getAPPFILE());
										Intent intent = new Intent(
												Intent.ACTION_VIEW, uri);
										startActivity(intent);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton(getString(R.string.main_next),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create();
		} else {// 强制升级
			dialog = alert
					.setTitle(getString(R.string.main_notice))
					.setContentView(view)
					.setPositiveButton(getString(R.string.main_shenji),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									try {
										Uri uri = Uri.parse(info.getData()
												.getAPPFILE());
										Intent intent = new Intent(
												Intent.ACTION_VIEW, uri);
										startActivity(intent);
										finish();
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).create();
		}

		dialog.setCanceledOnTouchOutside(not_force_update);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					return true;
				}
				return false;
			}
		});
		dialog.show();
	}

	/**
	 * 检测新版本
	 */
	private void checkAppVersion(String lng, String lat) {
		try {

			// if (TimeUtil.checkIsNeedUpdate())
			// {//该标记代表每天第一次启动，目前每次进入应用都需要调用更新版本接口，所以先注释掉

			HashMap<String, String> params = new HashMap<String, String>();
			params.put("imei", PublicUtil.getDeviceUuid(this));
			params.put("appversion", PublicUtil.getVersionName(this));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("membertype", "user");
			params.put("channel", "android");
			params.put("appid", "3");
			params.put("lng", "" + lng);
			params.put("lat", "" + lat);
			params.put("mobileparam", MyApplication.getInstance()
					.getMobileParam(this));
			System.out.println("检测 params ：" + params);
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_version_info, VersionInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {

							Message message = new Message();
							message.obj = response; // version类使用JSON，可传函数回调取值/
							message.what = IResult.CHECK_VERSION;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							MyApplication.volleyHttpClient
									.cancelRequest(HttpUtil.get_version_info);
						}
					});

			// }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// private void checkAppVersion() {
	// try {
	//
	// // if (TimeUtil.checkIsNeedUpdate())
	// // {//该标记代表每天第一次启动，目前每次进入应用都需要调用更新版本接口，所以先注释掉
	//
	// HashMap<String, String> params = new HashMap<String, String>();
	// params.put("imei", PublicUtil.getDeviceUuid(this));
	// params.put("appversion", PublicUtil.getVersionName(this));
	// params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
	// params.put("membertype", "user");
	// params.put("channel", "android");
	// params.put("appid", "3");
	// // params.put("lng", "113");
	// // params.put("lat", "23" );
	// if (self_jindu != 0 && self_weidu != 0) {
	// params.put("lng", "" + self_jindu);
	// params.put("lat", "" + self_weidu);
	// } else {
	// String lng = SharePrefUtil.getFloat("self_jindu") == 0.0 ? ""
	// : (SharePrefUtil.getFloat("self_jindu") + "");
	// String lat = SharePrefUtil.getFloat("self_weidu") == 0.0 ? ""
	// : (SharePrefUtil.getFloat("self_weidu") + "");
	// params.put("lng", "" + lng);
	// params.put("lat", "" + lat);
	// }
	// params.put("mobileparam",
	// MyApplication.getInstance().getMobileParam(this));
	//
	// MyApplication.volleyHttpClient.postWithParams(
	// HttpUtil.get_version_info, VersionInfo.class, params,
	// new Response.Listener() {
	// @Override
	// public void onResponse(Object response) {
	//
	// Message message = new Message();
	// message.obj = response; // version类使用JSON，可传函数回调取值/
	// message.what = IResult.CHECK_VERSION;
	// handler.sendMessage(message);
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// MyApplication.volleyHttpClient
	// .cancelRequest(HttpUtil.get_version_info);
	// }
	// });
	//
	// // }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// }

	/**
	 * 检测1.0版本
	 */
	private final int OLD_VER = 10;

	public void checkOldVer() {
		// Log.d("chenhj", "checkOldVer");
		new Thread() {
			public void run() {
				List<PackageInfo> packages = getPackageManager()
						.getInstalledPackages(
								android.content.pm.PackageManager.GET_INSTRUMENTATION);
				for (PackageInfo packageInfo : packages) {
					if ("net.ememed.doctor".equals(packageInfo.packageName)) {
						handler.sendEmptyMessage(OLD_VER);
						break;
					}
				}
			};
		}.start();

	}

	private void deleteOld() {
		MenuDialog.Builder builder = new MenuDialog.Builder(this);
		MenuDialog dialog = new MenuDialog(this);
		dialog = builder
				.setTitle("系统提示")
				.setMessage("检测到老版本，请卸载后再继续使用")
				.setPositiveButton("立刻卸载",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								Intent intent = new Intent();
								intent.setAction(Intent.ACTION_DELETE);
								intent.setData(Uri
										.parse("package:net.ememed.doctor"));
								startActivity(intent);
							}

						}).create();
		dialog.setCanceledOnTouchOutside(false);
		dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

			@Override
			public boolean onKey(DialogInterface dialog, int keyCode,
					KeyEvent event) {
				if (KeyEvent.KEYCODE_BACK == keyCode) {
					return true;
				}
				return false;
			}
		});
		dialog.show();
	}

	// private void gps_func() {
	// // Log.d("chenhj", "gps_func");
	//
	// // if (!isOPen(MainActivity.this)) {
	// //
	// // new AlertDialog.Builder(this)
	// // .setTitle("薏米医生需要打开GPS")
	// // .setMessage("打开吗？")
	// // .setPositiveButton("是",
	// // new DialogInterface.OnClickListener() {
	// // public void onClick(DialogInterface dialog,
	// // int which) {
	// //
	// // gotoGpsSystemSetting(MainActivity.this);
	// // }
	// // }).setNegativeButton("否", null).show();
	// // } else {
	// loctionManager = (LocationManager)
	// getSystemService(Context.LOCATION_SERVICE);
	// Criteria criteria = new Criteria();
	// criteria.setAccuracy(Criteria.ACCURACY_COARSE);// 低精度
	// criteria.setAltitudeRequired(false);// 不要求海拔
	// criteria.setBearingRequired(false);// 不要求方位
	// criteria.setSpeedRequired(true); // 设置是否要求速度
	// criteria.setCostAllowed(true);// 允许有花费
	// criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
	// // 从可用的位置提供器中，匹配以上标准的最佳提供器
	// String provider = loctionManager.getBestProvider(criteria, true);
	// // 获得最后一次变化的位置
	// Location location = loctionManager.getLastKnownLocation(provider);
	// loctionManager.requestLocationUpdates(provider, 1000*60*15, 0,
	// locationListener);
	// gps_loc(location);
	//
	// }
	//
	// private final LocationListener locationListener = new LocationListener()
	// {
	// @Override
	// public void onStatusChanged(String provider, int status, Bundle extras) {
	// }
	//
	// @Override
	// public void onProviderEnabled(String provider) {
	// }
	//
	// @Override
	// public void onProviderDisabled(String provider) {
	// }
	//
	// // 当位置变化时触发
	// @Override
	// public void onLocationChanged(Location location) {
	// // 使用新的location更新TextView显示
	// gps_loc(location);
	// }
	// };
	//
	// private void gps_loc(Location location) {
	// if (location != null) {
	// self_weidu = (float) location.getLatitude();
	// self_jindu = (float) location.getLongitude();
	// SharePrefUtil.putFloat("self_weidu", self_weidu);
	// SharePrefUtil.putFloat("self_jindu", self_jindu);
	// SharePrefUtil.commit();
	// checkAppVersion(self_jindu+"", self_weidu+"");
	// // checkAppVersion();// 检测更新
	// } else {
	// self_weidu = 0;
	// self_jindu = 0;
	// }
	// }

	// public static final boolean isOPen(final Context context) {
	// LocationManager locationManager = (LocationManager) context
	// .getSystemService(Context.LOCATION_SERVICE);
	// // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
	// boolean gps = locationManager
	// .isProviderEnabled(LocationManager.GPS_PROVIDER);
	// //
	// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
	// boolean network = locationManager
	// .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	// //boolean network =
	// locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	// boolean network1 = checkNetwork(context);
	// if (gps || network1) {
	// return true;
	// }
	// return false;
	// }

	public static void gotoGpsSystemSetting(Activity activity) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		activity.startActivityForResult(intent, 2);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == 2) {
				// gps_func();
			}
		}

	}

	// 双击退出
	long[] mHits = new long[2];
	private List<AdsEntry> data;

	/* private Button btn_addhealth; */

	@Override
	public void onBackPressed() {
		System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
		mHits[mHits.length - 1] = SystemClock.uptimeMillis();
		if (mHits[0] >= (SystemClock.uptimeMillis() - 2000)) {
			// super.onBackPressed();
			sendBroadcast(new Intent(Conast.ACTION_EXITAPP));
		} else {
			Toast.makeText(getApplicationContext(), "再按一次退出程序", 0).show();
		}
	}

	@Override
	public void onSuccess() {
		// TODO Auto-generated method stub
		MyApplication.isLoginIMing = false;
		MyApplication.isLoginIM = true;
	}

	@Override
	public void onProgress(int progress, String str) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onError(int age1, String age2) {
		// TODO Auto-generated method stub
		MyApplication.isLoginIMing = false;
		MyApplication.isLoginIM = false;
		if (age1 == -1) {// code = -1
			// 有可能是应用的环信value出错，有可能是该帐号没注册到环信服务器，所以重新调用接口，让后台把该帐号注册到环信
			reg_to_im();
		} else {
			// logIM();
		}
	}

	// 设置相关参数
	/*
	 * private void setLocationOption(){ LocationClientOption option = new
	 * LocationClientOption(); option.setOpenGps(true); //打开gps
	 * option.setServiceName("com.baidu.location.service_v2.9");
	 * option.setPoiExtraInfo(true); option.setAddrType("all");
	 * //option.setPriority(LocationClientOption.NetWorkFirst);
	 * option.setPriority(LocationClientOption.GpsFirst); //gps
	 * option.setPoiNumber(10); option.disableCache(true);
	 * option.setScanSpan(1000*60*153000); mLocationClient.setLocOption(option);
	 * }
	 */

	// XXX
	/*
	 * private class MyLocationListenner implements BDLocationListener{
	 * 
	 * @Override public void onReceiveLocation(BDLocation location) {
	 * if(location.getLongitude() == 4.9E-324){ //showToast("定位失败！");
	 * Logger.iout("测试", "定位失败"); }else{ String city = location.getCity();
	 * if(null != city){ MyApplication.current_city = city;
	 * showToast("定位到当前城市为："+city); //定位成功以后，则结束定位监听器
	 * mLocationClient.unRegisterLocationListener(this); Long time =
	 * System.currentTimeMillis(); SharePrefUtil.putString(Conast.CURRENT_CITY,
	 * city); SharePrefUtil.putString(Conast.PRE_LOACTION_TIME, ""+time);
	 * self_jindu = (float) location.getLongitude(); self_weidu = (float)
	 * location.getLatitude(); SharePrefUtil.putFloat("self_jindu", self_jindu);
	 * SharePrefUtil.putFloat("self_weidu", self_weidu); SharePrefUtil.commit();
	 * 
	 * Logger.iout("测试", "定位成功"+ self_jindu+ "<<<"+self_weidu);
	 * 
	 * checkAppVersion(); } } }
	 * 
	 * @Override public void onReceivePoi(BDLocation arg0) {
	 * 
	 * }
	 * 
	 * }
	 */

	private static boolean checkNetwork(Context context) {
		ConnectivityManager manager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// 获得当前网络信息
		NetworkInfo info = manager.getActiveNetworkInfo();
		// 判断是否连接

		if (info == null || !info.isConnected()) {
			return false;
		}
		return true;
	}

	CityTable cityTable;

	// @Override
	// public void LocationCallback(BDLocation location) {

	// Intent intent = new Intent(this, CityActivity.class);
	// startActivity(intent);
	// String cityname = SharePrefUtil.getString(Conast.CITY_NAME);
	// if(cityname.equals("")){
	//
	// }else{
	// CityEntry cityEntry = cityTable.findName(location.getCity());
	// if(!cityname.equals(cityEntry.getAREANAME())){
	//
	// }
	// }
	//
	// }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cancel:
			Intent intent = new Intent(this, CityActivity.class);
			startActivity(intent);
			cityHintDialog.dismiss();
			break;
		case R.id.btn_confirm:
			if(cityEntry!=null){
				SharePrefUtil.putString(Conast.CITY_ID, cityEntry.getID());
				SharePrefUtil.putString(Conast.CITY_NAME, cityEntry.getAREANAME());
				SharePrefUtil.commit();
			}
			cityHintDialog.dismiss();
			break;

		default:
			break;
		}
	}
}
