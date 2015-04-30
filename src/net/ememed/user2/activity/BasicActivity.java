package net.ememed.user2.activity;

import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.UUID;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.AppContext;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.CommandTaskEvent;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.FileUtil;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.MD5;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.MenuDialog;
import net.ememed.user2.widget.MyProgressDialog;

import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.easemob.EMConnectionListener;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.util.NetUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.util.IMManageTool;
import eu.janmuller.android.simplecropimage.CropImage;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

/**
 * 
 * @author chen
 * 
 */
public class BasicActivity extends FragmentActivity implements BasicUIEvent {

	public static final int DESTROYDIALOG = 0x00000101;
	private final static int cancelAlertDialog = 0x00000102;
	private final static int destroyDialogByFinish = 0x00000103;
	public MyProgressDialog dialog;
	protected Toast toast;
	public int CanvasHeight;
	public int CanvasWidth;

	public MyApplication app;

	protected Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			onResult(msg);
		};
	};
	protected static final String TAG = BasicActivity.class.getSimpleName();

	public ImageLoader imageLoader = ImageLoader.getInstance();
	public DisplayImageOptions options = new DisplayImageOptions.Builder().cacheInMemory(true)
			.cacheOnDisc(true).build();// 缓存图片内存跟磁盘
	private SmoothProgressBar sp_progress;
	private View top_bar_bottom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		exapp = new ExitappReceiver();

		registerReceiver(exapp, new IntentFilter(Conast.ACTION_EXITAPP));
		onBeforeCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		onCreateContent(savedInstanceState);
		onAfterCreate(savedInstanceState);
		AppContext.setContext(this);
		String country = getResources().getConfiguration().locale.getCountry();
		if (country.equals("CN")) {
			AppContext.setLanguage(1);
		} else if (country.equals("TW")) {
			AppContext.setLanguage(2);
		} else {
			AppContext.setLanguage(0);
		}
		TextUtil.setCtxForGetResString(this);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		CanvasWidth = displayMetrics.widthPixels;
		CanvasHeight = displayMetrics.heightPixels;

		final IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(mBatInfoReceiver, filter);
	}

	/** 画面初期化处理之前 */
	protected void onBeforeCreate(Bundle savedInstanceState) {
	}

	/** 画面初期化 */
	protected void onCreateContent(Bundle savedInstanceState) {

	}

	/** 画面初期化后 */
	protected void onAfterCreate(Bundle savedInstanceState) {
		setupView();
		addListener();
		getData();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}

	/** 获取数据 */
	protected void getData() {

	}

	public void setNetWork() {
		AlertDialog.Builder bulider = new AlertDialog.Builder(BasicActivity.this);
		bulider.setTitle("网络设置");
		bulider.setMessage("为了更好的为您服务，请检查您的网络设置");
		bulider.setPositiveButton("设置", new AlertDialog.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
				startActivity(intent);

			}
		});
		bulider.setNegativeButton("取消", null);
		bulider.show();
	}

	/** 实例化各控件 */
	protected void setupView() {
		sp_progress = (SmoothProgressBar) findViewById(R.id.loading_progress);
		top_bar_bottom = (View) findViewById(R.id.top_bar_bottom);
		
		
	}
	

	/** 显示ProgressBar */
	public void showProgressDialog() {
		try {
			if (this.isFinishing()) {
				return;
			}
			if (sp_progress == null || sp_progress.getVisibility() == View.GONE) {
				sp_progress = (SmoothProgressBar) findViewById(R.id.loading_progress);
				top_bar_bottom = (View) findViewById(R.id.top_bar_bottom);
				sp_progress.setVisibility(View.VISIBLE);
				top_bar_bottom.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (null != sp_progress && sp_progress.getVisibility() == View.VISIBLE) {
				sp_progress.setVisibility(View.GONE);
				top_bar_bottom.setVisibility(View.VISIBLE);
			}
		}
	}

	/**
	 * 关闭整个应用的广播接收者
	 */
	class ExitappReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (Conast.ACTION_EXITAPP.equals(action)) {
				BasicActivity.this.onBackPressed();
				finish();
			}
		}
	};

	/** 取消ProgressBar */
	public void cancleProgressDialog() {
		try {
			if (this.isFinishing()) {
				return;
			}
			if (null != sp_progress && sp_progress.getVisibility() == View.VISIBLE) {
				sp_progress.setVisibility(View.GONE);
				top_bar_bottom.setVisibility(View.VISIBLE);
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (null != sp_progress && sp_progress.getVisibility() == View.VISIBLE) {
				sp_progress.setVisibility(View.GONE);
				top_bar_bottom.setVisibility(View.VISIBLE);
			}
		}
	}

	/** 给相应控件添加监听 */
	protected void addListener() {
	}

	public void showToast(String msg) {
		if (!TextUtils.isEmpty(msg)) {
			Toast.makeText(BasicActivity.this, msg, Toast.LENGTH_SHORT).show();
		}
	};

	private final BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(final Context context, final Intent intent) {
			final String action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				// Log.d(TAG, "screen is on...");

			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				// Log.d(TAG, "screen is off...");
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		MobclickAgent.onResume(this);
		if (AppContext.getInstance().getLogin_uid() == 0
				|| AppContext.getInstance().getLogin_token() == null) {
			initConfig();
		}
		if (!AppContext.getInstance().isActive) {
			// app 从后台唤醒，进入前台
			AppContext.getInstance().isActive = true;
			Logger.dout(BasicActivity.class.getSimpleName() + " ===> onResume()");
		}
		if (EMChatManager.getInstance() != null) {
			EMChatManager.getInstance().activityResumed();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (!Util.isAppOnForeground(this)) {
			AppContext.getInstance().isActive = false;
			Logger.dout(BasicActivity.class.getSimpleName() + " ===> onStop()");
		}

	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_HOME) {
			Intent intent;
			if (AppContext.getContext() == null) {
				intent = new Intent(this, MainActivity.class);
			} else {
				intent = new Intent(this, AppContext.getContext().getClass());
			}
			intent.setAction("android.intent.action.MAIN");
			intent.addCategory("android.intent.category.LAUNCHER");
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		}
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void onDestroy() {
		MyApplication.isLoginIM = false;
		finish = true;
		if (toast != null) {
			toast.cancel();
		}
		if (exapp != null)
			this.unregisterReceiver(this.exapp);
		if (mBatInfoReceiver != null)
			unregisterReceiver(mBatInfoReceiver);
		super.onDestroy();
	}

	protected boolean onBackClick() {
		finish();
		return false;
	}

	public View getLayoutView(int layout) {
		return LayoutInflater.from(this).inflate(layout, null);
	}

	@Override
	public void execute(int mes, Object obj) {

	}

	public void connectionIM() {
		// if (EMChatManager.getInstance().isConnected()) {
		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
		// }

	}

	public void eventTask(int CommandID, String tips, Object obj) {
		try {
			CommandTaskEvent com = new CommandTaskEvent(this, this, tips);
			com.execute((new Object[] { CommandID + "", obj }));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public AlertDialog alert = null;
	public boolean alertIsShow = false;

	private int countDestroyDialogKeycodeBack = 0;
	
	public void showLoadingDialog() {
		loading(null);
	}

	public void loading(String msg) {
		try {
			if (this.isFinishing()) {
				return;
			}
			if (dialog == null || !dialog.isShowing()) {
				countDestroyDialogKeycodeBack = 0;
				dialog = new MyProgressDialog(BasicActivity.this, R.style.mydialog);
				dialog.show();
				dialog.setMessage(msg);
				dialog.setCancelable(false);
				dialog.setOnKeyListener(new OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialogs, int keyCode, KeyEvent event) {
						if (keyCode == KeyEvent.KEYCODE_BACK) {
							countDestroyDialogKeycodeBack++;
							if (countDestroyDialogKeycodeBack == 3) {
								dialog.dismiss();
							}
						}
						return false;
					}
				});

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroyDialog() {
		basicHandler.sendEmptyMessage(DESTROYDIALOG);
	}

	public void onToast(String msg) {
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}

	/**
	 * 系统信息 必须在handler或者控件触发 调用才有效
	 * 
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	public void setSysMes(String message) {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
		if (alert == null)
			alert = new AlertDialog.Builder(this).create();

		if (!alert.isShowing()) {
			alertIsShow = true;
			alert.setTitle(getString(R.string.system_info));
			alert.setMessage(message);
			alert.setButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					alertIsShow = false;
				}
			});
			alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					alertIsShow = false;
				}
			});
			alert.show();

		}
	}

	/**
	 * 系统信息 必须在handler或者控件触发 调用才有效
	 * 
	 * @param message
	 */
	@SuppressWarnings("deprecation")
	public void setSysMes(String message, final int what) {
		if (message == null || "出错了".equals(message)) {
			return;
		}
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
		if (alert == null)
			alert = new AlertDialog.Builder(this).create();

		if (!alertIsShow) {
			alertIsShow = true;
			alert.setTitle(getString(R.string.system_info));
			alert.setMessage(message);
			alert.setButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
					sysMesPositiveButtonEvent(what);
					alertIsShow = false;
				}
			});
			alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
				public void onCancel(DialogInterface dialog) {
					alertIsShow = false;
				}
			});
			alert.show();
		}
	}

	/**
	 * dismiss alert 系统信息提示框
	 */
	public void cancelAlertDialog() {
		if (alert != null && alert.isShowing()) {
			Message localMessage = Message.obtain(basicHandler, cancelAlertDialog);
			basicHandler.sendMessage(localMessage);
		}
	}

	/**
	 * 系统信息选择提示 必须在handler或者控件触发 调用才有效
	 * 
	 * @param message
	 * @param what
	 */
	public void showChoseMes(String message, final int what) {
		if (dialog != null && dialog.isShowing()) {
			dialog.cancel();
		}
		// Builder alert = new AlertDialog.Builder(this); // 为了避免
		// setSysMes（String
		// message,final int
		// what） 出现 “否”选择
		MenuDialog.Builder alert = new MenuDialog.Builder(this);

		if (!alertIsShow) {
			alertIsShow = true;
			MenuDialog dialog = alert
					.setTitle(getString(R.string.system_info))
					.setMessage(message)
					.setPositiveButton(getString(R.string.confirm_yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									sysMesPositiveButtonEvent(what);
									alertIsShow = false;
								}
							})
					.setNegativeButton(getString(R.string.confirm_no),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
									sysMesNegativeButtonEvent(what);
									alertIsShow = false;
								}
							}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		}
	}

	/**
	 * 系统信息提示ok按钮回调方法 是 showChoseMes(String message,final int what)，setSysMes(String message,final
	 * int what)方法回调
	 */
	public void sysMesPositiveButtonEvent(int what) {

	}

	/**
	 * 系统信息提示no按钮回调方法 是 showChoseMes(String message,final int what)方法回调
	 * 
	 * @param what
	 */
	public void sysMesNegativeButtonEvent(int what) {

	}

	/**
	 * 系统信息提示no按钮回调方法 是 showChoseMes(String message,final int what)方法回调
	 * 
	 * @param what
	 */
	public void sysMesOnCancelEvent(int what) {

	}

	public void destroyDialogByFinish() {
		Message localMessage = Message.obtain(basicHandler, destroyDialogByFinish);
		basicHandler.sendMessage(localMessage);
	}

	public void finish() {
		try {
			if (dialog != null && dialog.isShowing()) {
				destroyDialogByFinish();
			} else {
				super.finish();
			}
			// ActivityManager.removeActivity(this);
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void finishAll() {
		try {
			if (dialog != null && dialog.isShowing()) {
				destroyDialogByFinish();
			} else {
				super.finish();
			}
			System.gc();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Handler basicHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case DESTROYDIALOG:
					if (dialog != null && dialog.isShowing()) {
						dialog.cancel();
					}
					break;
				case cancelAlertDialog:
					if (alert != null && alert.isShowing()) {
						alert.cancel();
					}
					break;
				case destroyDialogByFinish:
					if (dialog != null && dialog.isShowing()) {
						dialog.cancel();
					}
					BasicActivity.this.finish();
				default:
					break;
				}
			} catch (Exception e) {
			}
		}
	};

	public void initConfig() {
		String country = getResources().getConfiguration().locale.getCountry();
		if (country.equals("CN")) {
			AppContext.setLanguage(1);
		} else if (country.equals("TW")) {
			AppContext.setLanguage(2);
		} else {
			AppContext.setLanguage(0);
		}

		SharedPreferences preferences = getSharedPreferences(AppContext.Preferences_userinfo,
				Activity.MODE_APPEND);
		int login_uid = preferences.getInt("login_uid", 0);
		String login_token = preferences.getString("login_token", null);
		AppContext.getInstance().setLogin_uid(login_uid);
		AppContext.getInstance().setLogin_token(login_token);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			// 获取打开相机的图片
			case REQUEST_CODE_TAKE_PICTURE:
				if(MyApplication.pathName!=null){
					onCameraPath(((MyApplication)getApplication()).pathName);
				}else{
					showToast("系统错误,请测试");
				}
				break;
			// 获取打开图库的数据
			case REQUEST_CODE_GALLERY:

				Uri uri = data.getData();
				Cursor cursor = getContentResolver().query(uri, null, null, null, null);
				if (cursor != null) {
					cursor.moveToNext();
					String imgPath = cursor.getString(1); // 图片文件路径
					cursor.close();
					onCameraPath(imgPath);
				} else {
					System.out.println("uri = "
							+ uri.toString().substring(uri.toString().indexOf("s")));
					onCameraPath(uri.toString().substring(uri.toString().indexOf("s")));
				}
				break;

			default:
				break;
			}

		}
	}

	protected void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		handler.sendMessage(msg);
	}

	protected void onResult(Message msg) {
		switch (msg.what) {
		case IResult.NET_ERROR:
			break;

		case IResult.DATA_ERROR:

			break;
		}
	}

	/** 信息提示 */
	private boolean finish = false;
	private ExitappReceiver exapp;

	protected void showMessage(String msg) {
		Builder builder = new Builder(this);
		Dialog dialog = builder.setTitle(getString(R.string.add_health_record_notice))
				.setMessage(msg)
				.setPositiveButton(getString(R.string.add_health_record_know), null).create();
		dialog.setCancelable(false);
		if (!finish) {
			dialog.show();
		}
	}

	private class MyConnectionListener implements EMConnectionListener {

		@Override
		public void onConnected() {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					System.out.println("环信   重新连接成功....");
				}

			});
		}

		@Override
		public void onDisconnected(final int error) {
			runOnUiThread(new Runnable() {

				public void run() {
					if (error == EMError.CONNECTION_CONFLICT) {
						// 显示帐号在其他设备登录dialog
						showExitTips("帐号在其他设备登录");
						MyApplication.isLoginIM = false;

					} else {
						if (NetUtils.hasNetwork(BasicActivity.this)) {
							// System.out.println("连接不到聊天服务器......");
							// showExitTips("连接不到聊天服务器");
							errorString(error);
						} else {
							System.out.println("当前网络不可用，请检查网络设置");
						}

					}
				}

			});
		}

	}

	public void showExitTips(String msg) {
		EMChatManager.getInstance().logout(null);
		try {
			if (!this.isFinishing()) {
				MenuDialog.Builder alert = new MenuDialog.Builder(this);
				MenuDialog dialog = alert
						.setTitle(getString(R.string.system_title))
						.setMessage(msg)
						.setPositiveButton(getString(R.string.confirm_ok),
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										dialog.dismiss();
										logoutUser();
									}
								}).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
					@Override
					public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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

	protected void logoutUser() {
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
							.getDeviceUuid(BasicActivity.this)));
					params.add(new BasicNameValuePair("appversion", PublicUtil
							.getVersionName(BasicActivity.this)));

					try {
						HttpUtil.getString(HttpUtil.URI + HttpUtil.logout, params, HttpUtil.POST);
					} catch (IOException e) {
						e.printStackTrace();

					}
				};
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Intent intent = new Intent(BasicActivity.this, LoginActivity.class);
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
		UserPreferenceWrapper.clearMyPraiseCount();
		UserPreferenceWrapper.clearMyCommentCount();
		UserPreferenceWrapper.clearMyShareCount();
		UserPreferenceWrapper.clearUserType();
		SharePrefUtil.putString("self_weidu", "");
		SharePrefUtil.putString("self_jindu", "");
		SharePrefUtil.commit();
		
		// logout third
		logoutThird(this);
		
		startActivity(intent);
		finish();

	}

	public void errorString(int code) {

		switch (code) {
		case EMError.CONNECT_TIMER_OUT:
			System.out.println("连接服务器超时");
			break;
		case EMError.CONNECTION_CLOSED:
			System.out.println("连接断开");
			break;
		case EMError.CONNECTION_INIT_FAILED:
			System.out.println("由于登录失败导致的初始化连接失败");
			break;
		case EMError.DNS_ERROR:
			System.out.println("DNS 错误");
			break;
		case EMError.ENCRYPTION_ERROR:
			System.out.println("数据加密传输错误");
			break;
		case EMError.FILE_NOT_FOUND:
			System.out.println("文件不存在异常");
			break;
		case EMError.GENERAL_ERROR:
			System.out.println("GENERAL_ERROR");
			break;
		case EMError.GROUP_ADD_MEMBERS_TOO_MUCH:
			System.out.println("要加入的用户人数超过剩余可加入的人数");
			break;
		case EMError.GROUP_MEMBERS_FULL:
			System.out.println("群组成员数已满");
			break;
		case EMError.GROUP_NO_PERMISSIONS:
			System.out.println("群组权限问题");
			break;
		case EMError.GROUP_NOT_EXIST:
			System.out.println("群组不存在");
			break;
		case EMError.GROUP_NOT_EXIST_LOCAL:
			System.out.println("本地不存在这个群组");
			break;
		case EMError.INVALID_CERTIFICATE:
			System.out.println("证书认证错误");
			break;
		case EMError.INVALID_FILE:
			System.out.println("无效文件异常，一般文件为0字节时为无效 (录制音频，在没有权限的时候会为0)");
			break;
		case EMError.INVALID_KEYSTORE:
			System.out.println("密钥认证错误");
			break;
		case EMError.INVALID_PASSWORD_USERNAME:
			System.out.println("用户名或密码错误");
			break;
		case EMError.IO_EXCEPTION:
			System.out.println("数据读取错误");
			break;
		case EMError.LOGOFFINPROGRESS_ERROR:
			System.out.println("Reserved for future usage");
			break;
		case EMError.NO_ERROR:
			System.out.println("无错误");
			break;
		case EMError.NONETWORK_ERROR:
			System.out.println("网络不可用");
			EMChatManager.getInstance().logout(null);

			try {
				IMManageTool.getInstance(this).loginIM(
						SharePrefUtil.getString(Conast.MEMBER_ID),
						MD5.getMD5(SharePrefUtil.getString(Conast.MEMBER_ID)
								+ SharePrefUtil.getString(Conast.USER_Name) + "ememedim"));
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case EMError.UNABLE_CONNECT_TO_SERVER:
			System.out.println("无法连接到服务器");
			break;
		case EMError.UNAUTHORIZED:
			System.out.println("没有权限 （在注册授权模式下，手机端调用注册会有此问题）");
			break;
		case EMError.UNKNOW_ERROR:
			System.out.println("UNKNOW_ERROR");
			break;
		case EMError.UNKNOWN_SERVER_ERROR:
			System.out.println("无法识别服务器返回值");
			break;
		case EMError.USER_ALREADY_EXISTS:
			System.out.println("当前用户已存在（注册时会出现）");
			break;

		default:
			break;
		}

	}

	public int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	public static final int REQUEST_CODE_GALLERY = 21;
	public static final int REQUEST_CODE_TAKE_PICTURE = 12;
	public static final int REQUEST_CODE_CROP_IMAGE = 4;
	public File picFile;
	public Uri photoUri;
	
	
	
	

	/**
	 * 打开图库
	 */
	/** for4.3 */
	public void basicopenGallery() {
		try {
			File pictureFileDir = new File(FileUtil.PATH, FileUtil.IMG_UPLOAD);
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			picFile = new File(pictureFileDir, UUID.randomUUID().toString() + ".jpg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		// intent.setType("image/*");
		// Intent wrapperIntent=Intent.createChooser(intent, null);
		// startActivityForResult(wrapperIntent, REQUEST_CODE_GALLERY);
	}

	/**
	 * 打开相机
	 */
	/** 4.0 */
	public void basictakePicture() {
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		try {

			File uploadFileDir = new File(FileUtil.PATH, FileUtil.IMG_UPLOAD);
			if (!uploadFileDir.exists()) {
				uploadFileDir.mkdirs();
			}

			String state = Environment.getExternalStorageState();
			if (Environment.MEDIA_MOUNTED.equals(state)) {
				picFile = new File(uploadFileDir, UUID.randomUUID().toString() + ".jpeg");
			} else {
				picFile = new File(this.getFilesDir() + FileUtil.ROOT_DIRECTORY + "/"
						+ FileUtil.IMG_UPLOAD, UUID.randomUUID().toString() + ".jpeg");
			}
			MyApplication.pathName = picFile.getAbsolutePath();
			photoUri = Uri.fromFile(picFile);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {
			// Log.d(TAG, "cannot take picture", e);
		}
	}

	/**
	 * 剪切图片
	 */
	private void basicstartCropImage() {
		if (picFile != null) {
			Intent intent = new Intent(this, CropImage.class);
			intent.putExtra(CropImage.IMAGE_PATH, picFile.getPath());
			intent.putExtra(CropImage.SCALE, true);
			intent.putExtra("outputX", 1000);
			intent.putExtra("outputY", 1000);
			intent.putExtra(CropImage.ASPECT_X, 1);
			intent.putExtra(CropImage.ASPECT_Y, 1);
			startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
		}
	}

	public void onCameraPath(String path) {

	}
	
	/**
	 * 注销第三方账号
	 * 
	 * @param context
	 */
	public void logoutThird(Context context) {
		ShareSDK.initSDK(context);
		Platform qq = new QQ(context);
		if (qq.isValid()) {
			qq.removeAccount();
		}

		Platform weibo = new SinaWeibo(context);
		if (weibo.isValid()) {
			weibo.removeAccount();
		}

		Platform wechat = new Wechat(context);
		if (wechat.isValid()) {
			wechat.removeAccount();
		}
	}

}
