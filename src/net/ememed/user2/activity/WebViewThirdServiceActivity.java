package net.ememed.user2.activity;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

import net.ememed.user2.R;
import net.ememed.user2.entity.ServiceListEntry;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.FileUtil;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.location.Location;
import android.net.Uri;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomButtonsController;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.umeng.analytics.MobclickAgent;

/**
 * 根据WebViewActivity拓展成的第三方Activity
 * 
 * @author chenhj
 * 
 */
public class WebViewThirdServiceActivity extends BasicActivity implements
		OnRefreshListener,AMapLocationListener {
	protected static final int MSG_34034 = 1;
	private WebView wb;
	private String firstUrl; // 记录第一次传入时的URL
	private String currentUrl = null;
	private String accessKey;

	/* 启动WebViewActivity传入的参数，即服务列表 */
	private List<ServiceListEntry> serviceList;

	/* 用于存储及更新第三方需要的字段 */
	HashMap<String, String> map = new HashMap<String, String>();

	boolean isStop = false; // 标志是前进键还是停止键

	private ImageButton forwardOrStopBtn;

	private LinearLayout ll_empty;
	private TextView noticeTextView;

	private ProgressBar progressBar;

	private String MYTAG = "chenhj,WebViewThirdServiceActivity";

	private WebSettings webSettings;
	ValueCallback<Uri> mUploadMessage;

	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		firstUrl = getIntent().getStringExtra("url");
		serviceList = (List<ServiceListEntry>) getIntent().getExtras()
				.getSerializable("ServiceList");
		if (getIntent().hasExtra("accesskey")) 
			accessKey = getIntent().getStringExtra("accesskey");
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_PROGRESS);
		setContentView(R.layout.webview_third_service);
	}
	
	/** 
	 * 设置cookie值 
	 */  
	public void synCookies(Context context, String url, String value) {
	    CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.setAcceptCookie(true);  
	    cookieManager.removeSessionCookie();
	    cookieManager.setCookie(url, value);
	    CookieSyncManager.getInstance().sync();  
	}
	
	/**
	 * 移除Session Cookie
	 */
	public void removeSessionCookie(Context context) {
		CookieSyncManager.createInstance(context);  
	    CookieManager cookieManager = CookieManager.getInstance();  
	    cookieManager.removeSessionCookie();
	    CookieSyncManager.getInstance().sync();  
	}

	@Override
	protected void setupView() {

		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		noticeTextView = (TextView) findViewById(R.id.tv_notice);
		forwardOrStopBtn = (ImageButton) findViewById(R.id.webview_forward);
		progressBar = (ProgressBar) findViewById(R.id.webview_progress);
		wb = (WebView) findViewById(R.id.wv_oauth);

		ll_empty.setClickable(true);
		ll_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!wb.canGoBack()) {
					wb.loadUrl(currentUrl);
				} else {
					wb.reload();
				}
			}
		});

		wb.setFocusable(true);
		wb.requestFocus();
		webSettings = wb.getSettings();
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setJavaScriptEnabled(true);
		// 启用地理定位
		webSettings.setGeolocationEnabled(true);
		// 设置定位的数据库路径
		// 最重要的方法，一定要设置，这就是出不来的主要原因
		webSettings.setDomStorageEnabled(true);
		
		if (!TextUtils.isEmpty(accessKey)) {
			synCookies(WebViewThirdServiceActivity.this, firstUrl, accessKey);
		}else {
			removeSessionCookie(WebViewThirdServiceActivity.this);
		}

		// 设置webview：跳转、开始加载、加载完成、加载失败的逻辑
		wb.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				try {

					Uri uri = Uri.parse(url);
					if (null != uri.getScheme()) {
						String scheme = uri.getScheme();
						if ("http".equalsIgnoreCase(scheme)) {
							currentUrl = url;
							view.loadUrl(url);
						} else {
							Intent intent = new Intent(Intent.ACTION_VIEW, uri);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					showToast("不支持或不符合格式的URI");
				}

				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressBar.setVisibility(View.VISIBLE);
				forwardOrStopBtn
						.setBackgroundResource(R.drawable.webview_cancle);
				isStop = true;
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				if (wb.canGoForward()) {
					forwardOrStopBtn
							.setBackgroundResource(R.drawable.webview_forward);
				} else {
					forwardOrStopBtn
							.setBackgroundResource(R.drawable.webview_forward_unvalue);
				}

				isStop = false;
			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// Log.d(MYTAG, " onReceivedError ");
				view.stopLoading();
				view.clearView();
				Message msg = handler.obtainMessage();// 发送通知，加入线程
				msg.what = MSG_34034;// 通知加载自定义34034页面
				handler.sendMessage(msg);// 通知发送！
			}
		});
		wb.setWebChromeClient(new TestWebChromeClient(new WebChromeClient()) {

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url,
					String message, JsResult result) {

				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onJsPrompt(WebView view, String url, String message,
					String defaultValue, JsPromptResult result) {
				return super.onJsPrompt(view, url, message, defaultValue,
						result);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.setProgress(newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}

			@Override
			public void onGeolocationPermissionsShowPrompt(String origin,
					android.webkit.GeolocationPermissions.Callback callback) {
				callback.invoke(origin, true, false);
				super.onGeolocationPermissionsShowPrompt(origin, callback);
			}

			// The undocumented magic method override
			// Eclipse will swear at you if you try to put @Override here
			public void openFileChooser(ValueCallback<Uri> uploadMsg) {
				System.out.println("*********************1");
				mUploadMessage = uploadMsg;
				startActivityForResult(createDefaultOpenableIntent(), 4);
			}

			public void openFileChooser(ValueCallback uploadMsg,
					String acceptType) {
				System.out.println("*********************2");
				mUploadMessage = uploadMsg;
				startActivityForResult(createDefaultOpenableIntent(), 4);
			}

			// For Android 34.1
			public void openFileChooser(ValueCallback<Uri> uploadMsg,
					String acceptType, String capture) {
				System.out.println("*********************3");
				mUploadMessage = uploadMsg;
				startActivityForResult(createDefaultOpenableIntent(), 4);

			}
		});

		// 初始化，将第三方需要的信息到map中
		setMessageForThirdService();

		wb.addJavascriptInterface(new ObjectForJS(), "EmemedAPI");
		String actuallUrl = getMyFormatUrl(firstUrl);
		currentUrl = actuallUrl;

		// 去掉WebView自带的缩放按钮但不影响缩放功能
		try {
			int sysVersion = Integer.parseInt(VERSION.SDK);
			if (sysVersion >= 11) {
				setZoomControlGoneX(webSettings, new Object[] { false });
			} else {
				setZoomControlGone(wb);
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}

		System.out.println("actuallUrl = "+actuallUrl);
		
		loadUrl(actuallUrl);
//		loadUrl("file:///android_asset/index.html");
	}

	private Intent createDefaultOpenableIntent() {
		// Create and return a chooser with the default OPENABLE
		// actions including the camera, camcorder and sound
		// recorder where available.
		Intent i = new Intent(Intent.ACTION_GET_CONTENT);
		i.addCategory(Intent.CATEGORY_OPENABLE);
		i.setType("*/*");
		Intent chooser = createChooserIntent(createCameraIntent(),
				createCamcorderIntent(), createSoundRecorderIntent());
		chooser.putExtra(Intent.EXTRA_INTENT, i);
		return chooser;
	}

	private Intent createChooserIntent(Intent... intents) {
		Intent chooser = new Intent(Intent.ACTION_CHOOSER);
		chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, intents);
		chooser.putExtra(Intent.EXTRA_TITLE, "File Chooser");
		return chooser;
	}

	String mCameraFilePath;
	public File picFile;
	private Intent createCameraIntent() {
		Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		
		File uploadFileDir = new File(FileUtil.PATH, FileUtil.IMG_UPLOAD);
		if (!uploadFileDir.exists()) {
			uploadFileDir.mkdirs();
		}

		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			picFile = new File(uploadFileDir, UUID.randomUUID().toString() + ".jpeg");
		} else {
			picFile = new File(this.getFilesDir() + FileUtil.ROOT_DIRECTORY + "/" + FileUtil.IMG_UPLOAD, UUID.randomUUID().toString() + ".jpeg");
		}
		
		mCameraFilePath = picFile.getAbsolutePath();
//		File externalDataDir = Environment
//				.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
//		File cameraDataDir = new File(externalDataDir.getAbsolutePath()
//				+ File.separator + "browser-photos");
//		cameraDataDir.mkdirs();
//		mCameraFilePath = cameraDataDir.getAbsolutePath() + File.separator
//				+ System.currentTimeMillis() + ".jpg";
		cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,
				Uri.fromFile(picFile));
		return cameraIntent;
	}

	private Intent createCamcorderIntent() {
		return new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
	}

	private Intent createSoundRecorderIntent() {
		return new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
	}

	/**
	 * 根据需求设置对应字段以及值 即初始化 HashMap<String, String> map
	 */
	private void setMessageForThirdService() {
		String appVersion = "0.0";
		try {
			PackageInfo packInfo = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			// getPackageName()是你当前类的包名，0代表是获取版本信息
			appVersion = packInfo.versionName;

		} catch (NameNotFoundException e1) {
			e1.printStackTrace();
		}

		DisplayMetrics mDisplayMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);

		// 第三要求的修改UA信息，切记修改此处！
		webSettings.setUserAgentString(webSettings.getUserAgentString()
				+ " ememeduser/" + appVersion);

		/* 输出确保信息正确 */
		// Log.d(MYTAG, "版本号   "+appVersion);
		// Log.d(MYTAG, "应用名称   "+"ememeddoctor");
		// Log.d(MYTAG, "系统号   "+android.os.Build.MODEL + "," +
		// android.os.Build.VERSION.RELEASE);
		// Log.d(MYTAG,"UA : "+webSettings.getUserAgentString());
		// Log.d(MYTAG,"W : "+mDisplayMetrics.widthPixels);
		// Log.d(MYTAG,"H : "+mDisplayMetrics.heightPixels);
		// Log.d(MYTAG,"IMEI : "+PublicUtil.getDeviceUuid(this));
		// Log.d(MYTAG,
		// "REALNAME : "+SharePrefUtil.getString(Conast.Doctor_Name));

		try {
			// map.put("MEMBERID",
			// URLEncoder.encode(SharePrefUtil.getString(Conast.Doctor_ID),
			// "UTF-8"));
			map.put("MEMBERID", URLEncoder.encode(
					SharePrefUtil.getString(Conast.MEMBER_ID), "UTF-8"));
			map.put("MEMBERNAME", URLEncoder.encode(
					SharePrefUtil.getString("account"), "UTF-8"));
			map.put("UTYPE", URLEncoder.encode("user", "UTF-8"));
			// map.put("REALNAME",
			// URLEncoder.encode(SharePrefUtil.getString(Conast.Doctor_Name)
			// ,"UTF-8"));
			map.put("REALNAME", URLEncoder.encode(
					SharePrefUtil.getString(Conast.realName), "UTF-8"));
			map.put("CLIENTNAME",
					URLEncoder.encode("ememeduserofficial", "UTF-8"));
			map.put("CLIENTVER", URLEncoder.encode(appVersion, "UTF-8"));
			map.put("OSTYPE", URLEncoder.encode("Android", "UTF-8"));
			map.put("OSVER",
					URLEncoder.encode(android.os.Build.MODEL + ", Android "
							+ android.os.Build.VERSION.RELEASE, "UTF-8"));
			map.put("UA", URLEncoder.encode(webSettings.getUserAgentString(),
					"UTF-8"));
			map.put("SCREENW", URLEncoder.encode(mDisplayMetrics.widthPixels
					+ "", "UTF-8"));
			map.put("SCREENH", URLEncoder.encode(mDisplayMetrics.heightPixels
					+ "", "UTF-8"));
			map.put("DEVICEID", "");
			map.put("IMEI",
					URLEncoder.encode(PublicUtil.getDeviceUuid(this), "UTF-8"));
			map.put("ACCESSKEY", URLEncoder.encode(
					SharePrefUtil.getString(Conast.ACCESS_TOKEN), "UTF-8"));
			map.put("REFERER", URLEncoder.encode("", "UTF-8"));
			map.put("EXT", "");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 根据URL请求字段以及本地保存的字段组装URL
	 * 
	 * @param curUrl
	 *            服务器传入的URL
	 * @return 组装好的URL
	 */
	private String getMyFormatUrl(String curUrl) {
		String[] depart = curUrl.split("#");
		String currentKey = "";
		String resultlUrl = "";
		for (int i = 0; i < depart.length; i++) {
			if (i % 2 == 0) {
				resultlUrl += depart[i];
				if (i == 0) {
					currentKey = depart[i].substring(
							depart[0].indexOf('?') + 1, depart[i].length() - 1);
				} else {
					currentKey = depart[i].substring(1, depart[i].length() - 1);
				}
			} else {
				resultlUrl += map.get(currentKey);
			}
		}
		return resultlUrl;
	}

	/**
	 * 线性查找appkey的下标
	 * 
	 * @param aimAppKey
	 *            查找的AppKey
	 * @return 没找到时返回-1, serviceList为null返回-2
	 */
	private int getTargetAppKeyIndex(String aimAppKey) {
		if (null == serviceList) {
			return -2;
		}

		/* 遍历查找，效率有待改进 */
		int index = -1;
		for (int i = 0; i < serviceList.size(); i++) {
			if (aimAppKey.equals(serviceList.get(i).getAPPKEY())) {
				index = i;
				break;
			}
		}
		return index;
	}

	/**
	 * 设置分享网址，关键是去掉URL中的用户关键字段
	 * 
	 * @param originUrl
	 *            用户所要分享的网址
	 * @return 删除用户关键字段的网址
	 */
	private String getShareUrl(String originUrl) {
		StringBuilder shareUrl = new StringBuilder(originUrl);
		String[] keywords = new String[] { "MEMBERNAME", "REALNAME",
				"ACCESSKEY" };
		for (int i = 0; i < keywords.length; i++) {
			int startIndex = shareUrl.indexOf(keywords[i]);
			if (startIndex != -1) {
				int endIndex = shareUrl.indexOf("&", startIndex);
				if (endIndex == -1) {
					endIndex = shareUrl.length();
				} else {
					endIndex += 1;
				}
				shareUrl.replace(startIndex, endIndex, "");
			}
		}
		return shareUrl.toString();
	}

	public void doClick(View view) {

		if (ll_empty.getVisibility() == View.VISIBLE
				&& view.getId() != R.id.ll_webview_share) {
			ll_empty.setVisibility(View.GONE);
			wb.setVisibility(View.VISIBLE);
		}

		switch (view.getId()) {

		case R.id.ll_webview_back:
			if (wb.canGoBack()) {
				wb.goBack();
			} else {
				finish();
			}
			break;

		case R.id.ll_webview_forward:
			if (isStop) {
				wb.stopLoading();
				if (wb.canGoForward()) {
					forwardOrStopBtn
							.setBackgroundResource(R.drawable.webview_forward);
				} else {
					forwardOrStopBtn
							.setBackgroundResource(R.drawable.webview_forward_unvalue);
				}
			} else {
				if (wb.canGoForward()) {
					wb.goForward();
				}
			}
			break;

		case R.id.ll_webview_share:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.putExtra(Intent.EXTRA_SUBJECT, "Share");
			intent.putExtra(Intent.EXTRA_TEXT, "这个链接很不错，来分享一下（来自于薏米网医生版）\n"
					+ getShareUrl(currentUrl));

			startActivity(Intent.createChooser(intent, getTitle()));
			break;

		case R.id.ll_webview_refresh:
			if (!wb.canGoBack()) {
				wb.loadUrl(firstUrl);
			} else {
				wb.reload();
			}
			break;

		case R.id.ll_webview_close:
			finish();
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// Here we just reload the webview
		wb.setVisibility(View.VISIBLE);
		wb.reload();
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		if (msg.what == MSG_34034) {
			ll_empty.setVisibility(View.VISIBLE);
			wb.setVisibility(View.GONE);
			noticeTextView.setText("载入失败,点击上方图片重新加载");
		}
	}

	private void loadUrl(String url) {
		if (NetWorkUtils.detect(WebViewThirdServiceActivity.this)) {
			wb.loadUrl(url);
		} else {
			Message msg = handler.obtainMessage();// 发送通知，加入线程
			msg.what = MSG_34034;// 通知加载自定义34034页面
			handler.sendMessage(msg);// 通知发送！
			Toast.makeText(this, "网络状态异常", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (wb.canGoBack()) {
				wb.goBack();
			} else {
				finish();
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 提供给JavaScript调用的类
	 * 
	 * @author chenhj
	 * 
	 */
	private class ObjectForJS {
		// 函数前注解@JavascriptInterface不能去掉，去掉以后API
		// 17或以上版本的手机使用时JS无法找到下列方法，向下兼容，去掉后很可能导致第三方服务不正常
		/**
		 * 用于给第三方无误调用的方法，负责寻找跳转服务并跳转
		 * 
		 * @param sAppKey
		 *            需要在服务项寻找的目标AppKey
		 * @param sRefererURL
		 *            第三方传入的用于设置 REFERER的值
		 * @return 如果不能找到匹配的AppKey返回false，能匹配时负责跳转以及返回true，不作跳转容错是为了及时发现问题
		 */
		@JavascriptInterface
		public boolean checkRight(String sAppKey, String sRefererURL) {
			int index = getTargetAppKeyIndex(sAppKey);
			if (index < 0) {
				return false;
			} else {
				if (null == serviceList.get(index).getEXT()) {
					return false;
				}

				String currentRightUrl = serviceList.get(index).getEXT()
						.getCHECK_RIGHT_URL();
				if (null != currentRightUrl) {
					map.put("REFERER", sRefererURL);
					final String newCurUrl = getMyFormatUrl(currentRightUrl);
					currentUrl = newCurUrl;
					handler.post(new Runnable() {
						@Override
						public void run() {
							wb.loadUrl(newCurUrl);
						}
					});
				}
				return true;
			}
		}
		
		@JavascriptInterface
		public void getLocation(String sJSCallback) {
			_sJSCallback = sJSCallback;
			handler.post(new Runnable() {
				public void run() {
					// wvWeb.loadUrl("javascript:onLocationChanged()");
					getGaodeLocation();
				}
			});
		}
	}

	// 以下两个函数用于去掉WebView自带的缩放按钮但不影响缩放功能
	private void setZoomControlGoneX(WebSettings view, Object[] args) {
		Class classType = view.getClass();

		try {
			Class[] argsClass = new Class[args.length];

			for (int i = 0, j = args.length; i < j; i++) {
				argsClass[i] = args[i].getClass();
			}

			Method[] ms = classType.getMethods();

			for (int i = 0; i < ms.length; i++) {
				if (ms[i].getName().equals("setDisplayZoomControls")) {
					try {
						ms[i].invoke(view, false);
					} catch (Exception e) {
						e.printStackTrace();
					}

					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// 隐藏webview的缩放按钮 适用于3.0以前
	private void setZoomControlGone(View view) {
		Class classType;
		Field field;

		try {
			classType = WebView.class;
			field = classType.getDeclaredField("mZoomButtonsController");
			field.setAccessible(true);

			ZoomButtonsController mZoomButtonsController = new ZoomButtonsController(
					view);
			mZoomButtonsController.getZoomControls().setVisibility(View.GONE);

			try {
				field.set(view, mZoomButtonsController);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 4) {
			Uri result = data == null || resultCode != RESULT_OK ? null : data
					.getData();
			if (result == null && data == null
					&& resultCode == Activity.RESULT_OK) {
				if(mCameraFilePath!=null){
					File cameraFile = new File(mCameraFilePath);
					if (cameraFile.exists()) {
						result = Uri.fromFile(cameraFile);
						// Broadcast to the media scanner that we have a new photo
						// so it will be added into the gallery for the user.
						sendBroadcast(new Intent(
								Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, result));
					}
				}
				
			}
			if(mUploadMessage!=null){
				mUploadMessage.onReceiveValue(result);
				mUploadMessage = null;
			}
		}

	}
	
	private LocationManagerProxy mLocationManagerProxy;
	/**
	 * 初始化定位
	 */
	private void getGaodeLocation() {
		// 初始化定位，只采用网络定位
		Log.i("lxq", "getGaodeLocation()1");
		if (null != mLocationManagerProxy) {
			// 移除定位请求
			mLocationManagerProxy.removeUpdates(this);
			// 销毁定位
			mLocationManagerProxy.destroy();
		}

		mLocationManagerProxy = LocationManagerProxy.getInstance(this);
		mLocationManagerProxy.setGpsEnable(false);
		// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
		// 注意设置合适的定位时间的间隔（最小间隔支持为2000ms），并且在合适时间调用removeUpdates()方法来取消定位请求
		// 在定位结束后，在合适的生命周期调用destroy()方法
		// 其中如果间隔时间为-1，则定位只定一次,
		// 在单次定位情况下，定位无论成功与否，都无需调用removeUpdates()方法移除请求，定位sdk内部会移除
		mLocationManagerProxy.requestLocationData(
				LocationProviderProxy.AMapNetwork, 60 * 1000, 15, this);
		Log.i("lxq", "getGaodeLocation()2");

	}
	String _sJSCallback = "onLocationChanged";
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(AMapLocation amapLocation) {
		// TODO Auto-generated method stub
		if (null != mLocationManagerProxy) {
			// 移除定位请求
			mLocationManagerProxy.removeUpdates(this);
			// 销毁定位
			mLocationManagerProxy.destroy();
		}
		if(amapLocation!=null){
			
			JSONObject jo = new JSONObject();
			try {
				jo.put("Lat", amapLocation.getLatitude());
				jo.put("ErrorCode", amapLocation.getAMapException().getErrorCode());
				jo.put("ErrorMessage", amapLocation.getAMapException().getErrorMessage());
				jo.put("Lon", amapLocation.getLongitude());
				jo.put("CityCode", amapLocation.getCityCode());
				jo.put("Address", amapLocation.getAddress());
				jo.put("AdCode", amapLocation.getAdCode());
				
				System.out.println("jo = "+jo);
				
				
				wb.loadUrl("javascript:" + _sJSCallback + "('"  
						+ jo.toString().replace("'","\\'") + "')");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		
		
	}
	
	

}


abstract class TestWebChromeClient extends WebChromeClient {
	private WebChromeClient mWrappedClient;

	protected TestWebChromeClient(WebChromeClient wrappedClient) {
		mWrappedClient = wrappedClient;
	}

	/** } */
	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		mWrappedClient.onProgressChanged(view, newProgress);
	}

	/** } */
	@Override
	public void onReceivedTitle(WebView view, String title) {
		mWrappedClient.onReceivedTitle(view, title);
	}

	/** } */
	@Override
	public void onReceivedIcon(WebView view, Bitmap icon) {
		mWrappedClient.onReceivedIcon(view, icon);
	}

	/** } */
	@Override
	public void onReceivedTouchIconUrl(WebView view, String url,
			boolean precomposed) {
		mWrappedClient.onReceivedTouchIconUrl(view, url, precomposed);
	}

	/** } */
	@Override
	public void onShowCustomView(View view, CustomViewCallback callback) {
		mWrappedClient.onShowCustomView(view, callback);
	}

	/** } */
	@Override
	public void onHideCustomView() {
		mWrappedClient.onHideCustomView();
	}

	/** } */
	@Override
	public boolean onCreateWindow(WebView view, boolean dialog,
			boolean userGesture, Message resultMsg) {
		return mWrappedClient.onCreateWindow(view, dialog, userGesture,
				resultMsg);
	}

	/** } */
	@Override
	public void onRequestFocus(WebView view) {
		mWrappedClient.onRequestFocus(view);
	}

	/** } */
	@Override
	public void onCloseWindow(WebView window) {
		mWrappedClient.onCloseWindow(window);
	}

	/** } */
	@Override
	public boolean onJsAlert(WebView view, String url, String message,
			JsResult result) {
		return mWrappedClient.onJsAlert(view, url, message, result);
	}

	/** } */
	@Override
	public boolean onJsConfirm(WebView view, String url, String message,
			JsResult result) {
		return mWrappedClient.onJsConfirm(view, url, message, result);
	}

	/** } */
	@Override
	public boolean onJsPrompt(WebView view, String url, String message,
			String defaultValue, JsPromptResult result) {
		return mWrappedClient.onJsPrompt(view, url, message, defaultValue,
				result);
	}

	/** } */
	@Override
	public boolean onJsBeforeUnload(WebView view, String url, String message,
			JsResult result) {
		return mWrappedClient.onJsBeforeUnload(view, url, message, result);
	}

	/** } */
	@Override
	public void onExceededDatabaseQuota(String url, String databaseIdentifier,
			long currentQuota, long estimatedSize, long totalUsedQuota,
			WebStorage.QuotaUpdater quotaUpdater) {
		mWrappedClient.onExceededDatabaseQuota(url, databaseIdentifier,
				currentQuota, estimatedSize, totalUsedQuota, quotaUpdater);
	}

	/** } */
	@Override
	public void onReachedMaxAppCacheSize(long spaceNeeded, long totalUsedQuota,
			WebStorage.QuotaUpdater quotaUpdater) {
		mWrappedClient.onReachedMaxAppCacheSize(spaceNeeded, totalUsedQuota,
				quotaUpdater);
	}

	/** } */
	@Override
	public void onGeolocationPermissionsShowPrompt(String origin,
			GeolocationPermissions.Callback callback) {
		mWrappedClient.onGeolocationPermissionsShowPrompt(origin, callback);
	}

	/** } */
	@Override
	public void onGeolocationPermissionsHidePrompt() {
		mWrappedClient.onGeolocationPermissionsHidePrompt();
	}

	/** } */
	@Override
	public boolean onJsTimeout() {
		return mWrappedClient.onJsTimeout();
	}

	/** } */
	@Override
	@Deprecated
	public void onConsoleMessage(String message, int lineNumber, String sourceID) {
		mWrappedClient.onConsoleMessage(message, lineNumber, sourceID);
	}

	/** } */
	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
		return mWrappedClient.onConsoleMessage(consoleMessage);
	}

	/** } */
	@Override
	public Bitmap getDefaultVideoPoster() {
		return mWrappedClient.getDefaultVideoPoster();
	}

	/** } */
	@Override
	public View getVideoLoadingProgressView() {
		return mWrappedClient.getVideoLoadingProgressView();
	}

	/** } */
	@Override
	public void getVisitedHistory(ValueCallback<String[]> callback) {
		mWrappedClient.getVisitedHistory(callback);
	}

	/** } */

	public void openFileChooser(ValueCallback<Uri> uploadFile) {
		((TestWebChromeClient) mWrappedClient).openFileChooser(uploadFile);
	}
	
	
	
	
	
}
