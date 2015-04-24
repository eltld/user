package net.ememed.user2.activity;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;

//import com.alipay.android.app.util.LogUtils;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.db.HomeAdTable;
import net.ememed.user2.entity.AdsEntry;
import net.ememed.user2.entity.BasicInfo;
import net.ememed.user2.entity.HomeAdsInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.VersionInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.MenuDialog;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.provider.Settings;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/** 首页 */
public class NavigationActivity extends BasicActivity {

	private ViewPager vp_ad;
	private List<View> views = new ArrayList<View>();
	// 滑动图片源
	private HomeAdTable table = new HomeAdTable();
	boolean isFromNet = false;
	String latitude;
	String longitude;
	TextView test1;
	TextView test2;
	double self_weidu;
	double self_jindu;
	LocationManager loctionManager;
	String contextService = Context.LOCATION_SERVICE;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_navigation);

		test1 = (TextView) findViewById(R.id.test1);
		test2 = (TextView) findViewById(R.id.test2);
		vp_ad = (ViewPager) findViewById(R.id.vp_ad);
		adapterCycle = new AdapterCycle(null, null);
		vp_ad.setAdapter(adapterCycle);
		getAdsFromDB();
//		checkAppVersion();// 检测更新
		initViewPager();
		loctionManager = (LocationManager) getSystemService(contextService);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);// 高精度
		criteria.setAltitudeRequired(false);// 不要求海拔
		criteria.setBearingRequired(false);// 不要求方位
		criteria.setCostAllowed(true);// 允许有花费
		criteria.setPowerRequirement(Criteria.POWER_LOW);// 低功耗
		// 从可用的位置提供器中，匹配以上标准的最佳提供器
		String provider = loctionManager.getBestProvider(criteria, true);
		// 获得最后一次变化的位置
		Location location = loctionManager.getLastKnownLocation(provider);
		loctionManager.requestLocationUpdates(provider, 1000, 0,
				locationListener);
		gps_loc(location);
		test1.setText("" + self_jindu);
		test2.setText("" + self_weidu);
		if (!isOPen(NavigationActivity.this)) {

			new AlertDialog.Builder(this)
					.setTitle("薏米医生需要打开GPS")
					.setMessage("打开吗？")
					.setPositiveButton("是",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int which) {

									gotoGpsSystemSetting(NavigationActivity.this);
								}
							}).setNegativeButton("否", null).show();
		}
	}

	// 位置监听器
	private final LocationListener locationListener = new LocationListener() {
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		// 当位置变化时触发
		@Override
		public void onLocationChanged(Location location) {
			// 使用新的location更新TextView显示
			gps_loc(location);
			test1.setText("" + self_jindu);
			test2.setText("" + self_weidu);
		}

	};

	private void gps_loc(Location location) {
		if (location != null) {
			self_weidu = location.getLatitude();
			self_jindu = location.getLongitude();
		} else {
			self_weidu = 0;
			self_jindu = 0;
		}
	}

	public static final boolean isOPen(final Context context) {
		LocationManager locationManager = (LocationManager) context
				.getSystemService(Context.LOCATION_SERVICE);
		// 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
		boolean gps = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		// 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
		boolean network = locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		if (gps || network) {
			return true;
		}

		return false;
	}

	public static void gotoGpsSystemSetting(Activity activity) {
		Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		activity.startActivityForResult(intent, 0);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkOldVer();
	}

	private void initViewPager() {
		new Thread() {
			public void run() {
				String content = null;
				try {

					content = HttpUtil.getString(HttpUtil.GET_HOME_ADS, null,
							HttpUtil.POST);
					content = TextUtil.substring(content, "{");
					Gson gson = new Gson();
					HomeAdsInfo info = gson
							.fromJson(content, HomeAdsInfo.class);
					Message msg = Message.obtain();
					msg.what = IResult.GET_HOME_ADS;
					msg.obj = info;
					handler.sendMessage(msg);

				} catch (IOException e) {
					Message message = handler.obtainMessage();
					message.obj = e.getMessage();
					message.what = IResult.DATA_ERROR;
					handler.sendMessage(message);
				}
			};
		}.start();
	}

	Timer t;

	private void TimeSlide() {
		if (t != null)
			t.cancel();
		t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(0);
			}
		}, 3000, 3000);
	}

	protected void onResult(Message msg) {

		switch (msg.what) {
		case 0:
			vp_ad.setCurrentItem(vp_ad.getCurrentItem() + 1);
			break;
		case IResult.GET_HOME_ADS:
			HomeAdsInfo info = (HomeAdsInfo) msg.obj;
			if (info.getSuccess() == 1) {
				data = info.getData();
				if (data != null && data.size() > 0) {

					views = new ArrayList<View>();

					if (data.size() == 1) {
						initItem(0);

						vp_ad.setAdapter(adapterCycle);
					} else if (data.size() == 2 || data.size() == 3) {
						for (int i = 0; i < 2; i++) {
							for (int j = 0; j < data.size(); j++) {
								initItem(j);
							}
						}
					} else if (data.size() > 3) {
						for (int i = 0; i < data.size(); i++) {
							initItem(i);
						}
					}
					adapterCycle.setData(data, views);

					if (data.size() > 1) {
						TimeSlide();
					}

					// //缓存保存
					table.clearTable();
					table.saveAdsList(data);
				}

			} else {
				showToast(info.getErrormsg());
			}

			break;
		case IResult.CHECK_VERSION:
			VersionInfo versionInfo = (VersionInfo) msg.obj;
			if (versionInfo != null) {
				if (versionInfo.getSuccess() == 1) {
					int newVersionCode = Integer.valueOf(versionInfo.getData()
							.getVERSIONCODE());
					int oldVersionCode = PublicUtil.getVersionCode(this);
					if (newVersionCode > oldVersionCode) {
						alertVersionUpdate(versionInfo);
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

	private void getAdsFromDB() {
		data = table.getAdsList();
		if (data != null && !data.isEmpty()) {
			if (data.size() == 1) {
				initItem(0);

				vp_ad.setAdapter(adapterCycle);
			} else if (data.size() == 2 || data.size() == 3) {
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < data.size(); j++) {
						initItem(j);
					}
				}
			} else if (data.size() > 3) {
				for (int i = 0; i < data.size(); i++) {
					initItem(i);
				}
			}
			adapterCycle.setData(data, views);
			if (data.size() > 1) {
				TimeSlide();
			}
		}
	}

	private void initItem(int i) {
		final AdsEntry adsEntry = data.get(i);
		View view = View.inflate(getApplicationContext(), R.layout.item_ads,
				null);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent(NavigationActivity.this,
						WebViewActivity.class);
				i.putExtra("url", adsEntry.getURL());
				i.putExtra("title", adsEntry.getTITLE());
				startActivity(i);
			}
		});
		ImageView iv = (ImageView) view.findViewById(R.id.iv);
		imageLoader
				.displayImage(adsEntry.getTHUMB(), iv, Util.getOptions_pic());
		// imageLoader.displayImage(adsEntry.getTHUMB(), iv, arg2, arg3, arg4)
		views.add(view);
	}

	public void doClick(View v) {
		int id = v.getId();
		Intent intent = null;
		switch (id) {
		case R.id.guahao:// 预约挂号
			intent = new Intent(this, GuahaoActivity.class);
			startActivity(intent);
			// intent = new Intent(this,KeshiActivity.class);
			// startActivity(intent);
			break;
		case R.id.findPill:// 用药助手
			// 原用药助手页面，由于服务器问题临时被注释跳转到临时Activity
			// intent = new Intent(this, DrugClassActivity.class);
			// startActivity(intent);
			intent = new Intent(this, TempActivity.class);
			startActivity(intent);

			break;
		case R.id.main:
			intent = new Intent(this, MainActivity.class);
			startActivity(intent);
			break;
		case R.id.aboutus:// 关于我们
			intent = new Intent(this, AboutUsActivity.class);
			startActivity(intent);
			break;
		// case R.id.iv:
		// Intent i = new Intent(NavigationActivity.this,
		// WebViewActivity.class);
		// i.putExtra("url", "http://s524765304.onlinehome.us/");
		// i.putExtra("title", "明择美式私人顾问");
		// startActivity(i);
		// break;
		default:
			break;
		}
	}

	/**
	 * 广告位适配器
	 * 
	 * @author huangjk
	 * 
	 */
	public class AdapterCycle extends PagerAdapter {
		List<AdsEntry> data;
		List<View> views;

		public void setData(List<AdsEntry> data, List<View> views) {
			if (data == null) {
				data = new ArrayList<AdsEntry>();
			}
			if (views == null) {
				views = new ArrayList<View>();
			}
			this.data = data;
			this.views = views;
			notifyDataSetChanged();
		}

		public AdapterCycle(List<AdsEntry> data, List<View> views) {
			if (data == null) {
				data = new ArrayList<AdsEntry>();
			}
			if (views == null) {
				views = new ArrayList<View>();
			}
			this.data = data;
			this.views = views;
		}

		@Override
		public int getCount() {
			if (data.size() <= 1) {
				return views.size();
			} else {
				return Integer.MAX_VALUE;
			}

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (views == null || views.size() == 0) {
				return;
			}
			int posi = 0;
			if (data.size() <= 1) {
				posi = position;
			} else {
				posi = position % views.size();
			}

			View view = views.get(posi);
			container.removeView(view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (views == null || views.size() == 0) {
				return null;
			}
			int posi = 0;
			if (data.size() <= 1) {
				posi = position;
			} else {
				posi = position % views.size();
			}
			container.addView(views.get(posi));
			return views.get(posi);
		}

	}

	/**
	 * 检测新版本
	 */
//	private void checkAppVersion() {
//		try {
//
//			// if (TimeUtil.checkIsNeedUpdate())
//			// {//该标记代表每天第一次启动，目前每次进入应用都需要调用更新版本接口，所以先注释掉
//
//			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("imei", PublicUtil.getDeviceUuid(this));
//			params.put("appversion", PublicUtil.getVersionName(this));
//			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
//			params.put("membertype", "user");
//			params.put("channel", "android");
//			params.put("appid", "3");
//			if (self_jindu != 0 && self_weidu != 0) {
//				params.put("lng", "" + self_jindu);
//				params.put("lat", "" + self_weidu);
//			}
//			params.put("mobileparam", MyApplication.getInstance().getMobileParam(this));
//
//			MyApplication.volleyHttpClient.postWithParams(
//					HttpUtil.get_version_info, VersionInfo.class, params,
//					new Response.Listener() {
//						@Override
//						public void onResponse(Object response) {
//
//							Message message = new Message();
//							message.obj = response; // version类使用JSON，可传函数回调取值
//							message.what = IResult.CHECK_VERSION;
//							handler.sendMessage(message);
//						}
//					}, new Response.ErrorListener() {
//						@Override
//						public void onErrorResponse(VolleyError error) {
//							MyApplication.volleyHttpClient
//									.cancelRequest(HttpUtil.get_version_info);
//						}
//					});
//
//			// }
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}

	// 双击退出
	long[] mHits = new long[2];
	private AdapterCycle adapterCycle;
	private List<AdsEntry> data;

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

	/**
	 * 弹出升级对话框
	 * 
	 * @param info
	 */
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
	 * 检测1.0版本
	 */
	private final int OLD_VER = 10;

	public void checkOldVer() {
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

}
