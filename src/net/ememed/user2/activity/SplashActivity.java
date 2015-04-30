package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.db.CityConfigTable;
import net.ememed.user2.db.CityTable;
import net.ememed.user2.entity.AreaInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.TimeUtil;
import net.ememed.user2.util.UICore;

import org.apache.http.message.BasicNameValuePair;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.baidu.location.BDLocation;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import de.greenrobot.event.util.BDLocationCallback;
import de.greenrobot.event.util.BDLocationTool;

public class SplashActivity extends BasicActivity {
	private ImageView splash;
	boolean isFirstIn = false;
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	private static final int GO_HOME = 1000;
	private static final int GO_GUIDE = 1001;
	private int versionCode;

	boolean isBaidu = false;
	boolean isAnim = false;
	private String web_action, ext, other_doctorid;
	
	private boolean isFromWeb = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash);
		
		Intent i_getvalue = getIntent();  
		String action = i_getvalue.getAction();  
		  
		if(Intent.ACTION_VIEW.equals(action)){  
		    Uri uri = i_getvalue.getData();  
		    if(uri != null){  
		    	web_action = uri.getQueryParameter("action"); 
		    	ext = uri.getQueryParameter("ext");
		    	other_doctorid = uri.getQueryParameter("other_doctorid");
		    }  
		}
		getCityListFromServer();
		baiduLocation();
		splash = (ImageView) findViewById(R.id.splash);
		splash.setBackgroundResource(R.drawable.splash);
		Animation anim = AnimationUtils.loadAnimation(this, R.anim.fadein);
		splash.setAnimation(anim);
		init();
		anim.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation animation) {

			}

			@Override
			public void onAnimationRepeat(Animation animation) {
			}

			@Override
			public void onAnimationEnd(Animation animation) {
				// 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
				isAnim = true;
				
				if (isBaidu) {
					if(judgeIsFromWeb()){
						goToActivityfromWeb();
					} else {
						if (!isFirstIn) {
							// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
							goHome();
						} else {
							goGuide();
						}
					}
				}
			}

		});
	
		
	}

	long first = 0;

	public void baiduLocation() {

		first = System.currentTimeMillis();
		BDLocationTool locationTool = new BDLocationTool(this);
		locationTool.startBaiduLocation(new BDLocationCallback() {
			@Override
			public void LocationCallback(BDLocation location) {
				// TODO Auto-generated method stub
				if (location != null) {
					SharePrefUtil.putString("lat",
							String.valueOf(location.getLatitude()));

					SharePrefUtil.putString("lon",
							String.valueOf(location.getLongitude()));

					SharePrefUtil.commit();
					System.out.println((System.currentTimeMillis() - first)
							+ " : 城市定位：" + location.getAddrStr());
					isBaidu = true;
					if (isAnim) {
						if(judgeIsFromWeb()){
							goToActivityfromWeb();
						} else { 
							if (!isFirstIn) {
								// 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
								goHome();
							} else {
								goGuide();
							}
						}
					}

				} else {
					showToast("定位失败，请退出重试");
				}
			}
		});
	}
	
	private boolean judgeIsFromWeb(){
		if(web_action != null && web_action.equals("activity")){
			if(ext != null && ext.equals("baike_home")){
				if(other_doctorid != null){
					return true;
				}
			}
		}
		return false;
	}
	
	private void goToActivityfromWeb() {
		Intent intent = new Intent(SplashActivity.this, DoctorBasicInfoActivity.class);
		intent.putExtra("tochat_userId", other_doctorid);
		intent.putExtra("from", SplashActivity.class.getSimpleName());
		startActivity(intent);
		SplashActivity.this.finish();
	}

	private void init() {
		// 读取SharedPreferences中需要的数据
		// 使用SharedPreferences来记录程序的使用次数
		SharedPreferences preferences = getSharedPreferences(
				SHAREDPREFERENCES_NAME, MODE_PRIVATE);

		try {
			PackageInfo packageInfo = this.getPackageManager().getPackageInfo(
					this.getPackageName(), 0);
			versionCode = packageInfo.versionCode;
		} catch (NameNotFoundException e) {
			versionCode = 0;
		}
		// 取得相应的值，如果没有该值，说明还未写入，用true作为默认值
		isFirstIn = preferences.getBoolean("isFirstIn", true);

		int lastVersionCode = preferences.getInt("lastVersionCode", 0);
		if (versionCode > lastVersionCode) {
			isFirstIn = true;
		}
		preferences.edit().putInt("lastVersionCode", versionCode).commit();

		// 极光推送
		if (SharePrefUtil.getBoolean("savepwd")
				&& SharePrefUtil.getBoolean(Conast.LOGIN))
			JPushInterface.init(getApplicationContext());

	}

	private void goHome() {
		Intent intent = new Intent(SplashActivity.this, MainActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();
	}

	private void goGuide() {
		Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
		SplashActivity.this.startActivity(intent);
		SplashActivity.this.finish();

	}

	@Override
	public void execute(int mes, Object obj) {
		// TODO Auto-generated method stub
		super.execute(mes, obj);

//		new Thread() {
//			boolean isflag = true;
//
//			public void run() {
//				CityConfigTable cityConfigTable = new CityConfigTable();
//				while (isflag) {
//					if (!cityConfigTable.isLocked()) {
//						if (null == cityConfigTable
//								|| cityConfigTable.getProvinces() == null
//								|| TimeUtil.checkIsNeedUpdate()) {
//							getCityListFromServer();
//						}
//						isflag = false;
//					} else {
//						try {
//							Thread.sleep(50);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//					}
//				}
//			};
//		}.start();
	}

	public void getCityListFromServer() {

		HashMap<String, String> params = new HashMap<String, String>();

		MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_all_area,
				AreaInfo.class, params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {
						final AreaInfo info = (AreaInfo) response;
						new Thread() {
							public void run() {
								if (null != info && null != info.getData()
										&& info.getData().size() > 0) {
									CityConfigTable cityConfigTable = new CityConfigTable();
									CityTable cityTable = new CityTable();
									cityConfigTable.clearTable();
									cityTable.clearTable();
									cityConfigTable.areaEntryToContentValues( info.getData());
									cityTable.areaEntryToContentValues( info.getData());
//									for (int i = 0; i < info.getData().size(); i++) {
//										cityConfigTable.saveSortName(info.getData()
//												.get(i));
//										cityTable.saveSortName(info.getData().get(i));
//									}
								}
							};

						}.start();

					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
					}
				});

	}

}
