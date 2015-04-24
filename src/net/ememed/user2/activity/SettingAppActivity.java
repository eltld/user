package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import cn.jpush.android.api.JPushInterface;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;
import com.umeng.fb.FeedbackAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.LogonSuccessEvent;
import net.ememed.user2.activity.SettingAppActivity;
import net.ememed.user2.entity.VersionInfo;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.network.VolleyHttpClient;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.widget.MenuDialog;
import android.app.NotificationManager;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import net.ememed.user2.entity.VersionInfo;

public class SettingAppActivity extends BasicActivity implements BasicUIEvent {
	private TextView top_title;
	private Button btn_cut_user;
	private TextView tv_version;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.layout_setting_app_info);
	}

	@Override
	protected void setupView() {
		super.setupView();
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(getString(R.string.about_us));
		btn_cut_user = (Button) findViewById(R.id.btn_cut_user);
		tv_version = (TextView) findViewById(R.id.tv_version);
		tv_version.setText("薏米医生 v" + PublicUtil.getVersionName(this));
		boolean login = SharePrefUtil.getBoolean(Conast.LOGIN);
		if (!login) {
			btn_cut_user.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		if (mes == IResult.LOGOUT) {

			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
			params.add(new BasicNameValuePair("channel", "android"));
			params.add(new BasicNameValuePair("imei", PublicUtil
					.getDeviceUuid(SettingAppActivity.this)));
			params.add(new BasicNameValuePair("appversion", PublicUtil
					.getVersionName(SettingAppActivity.this)));

			String content;
			try {
				content = HttpUtil.getString(HttpUtil.URI + HttpUtil.logout, params, HttpUtil.POST);
				content = TextUtil.substring(content, "{");
				Gson gson = new Gson();
				ResultInfo reason = gson.fromJson(content, ResultInfo.class);
				Message msg = Message.obtain();
				msg.what = IResult.LOGOUT;
				msg.obj = reason;
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				Message message = new Message();
				message.obj = e.getMessage();
				message.what = IResult.DATA_ERROR;
				handler.sendMessage(message);
			}
		}
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		if (msg.what == IResult.LOGOUT) {
			ResultInfo info = (ResultInfo) msg.obj;
			if (null != info && info.getSuccess() == 1) {
				// 退出聊天服务器
				new Thread() {
					public void run() {
						if (null != EMChatManager.getInstance()
								&& EMChatManager.getInstance().isConnected()) {
							EMChatManager.getInstance().logout();
						}
					};
				}.start();

				NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
				nm.cancel(R.string.app_name);
				nm.cancel(R.drawable.ic_launcher);
				nm.cancelAll();

				Intent intent = new Intent(SettingAppActivity.this, LoginActivity.class);
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
				EventBus.getDefault().postSticky(new LogonSuccessEvent());
			}
		}
	}

	public void doClick(View view) {
		int id = view.getId();
		if (id == R.id.lt_help) {
			Intent helpIntent = new Intent(SettingAppActivity.this, ProblemActivity.class);
			startActivity(helpIntent);

		} else if (id == R.id.lt_good_opinion) {
			try {
				Uri uri = Uri.parse("market://details?id=net.ememed.user2");
				Intent it = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(it);
			} catch (Exception e) {
			}
		} else if (id == R.id.lt_share) {

			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TITLE, getResources().getText(R.string.u_share_title));
			sendIntent
					.putExtra(Intent.EXTRA_TEXT, getResources().getText(R.string.u_share_content));
			sendIntent.setType("text/plain");
			startActivity(Intent
					.createChooser(sendIntent, getResources().getText(R.string.send_to)));

		} else if (id == R.id.lt_feedback) {
			// FeedbackAgent agent = new FeedbackAgent(SettingAppActivity.this);
			// agent.sync();
			// agent.startFeedbackActivity();
			startActivity(new Intent(this, FeedbackActivity.class));

		} else if (id == R.id.lt_upgrade) {
			update();
			// checkVersionAndUpdate();

		} else if (id == R.id.lt_statement) {
			Intent intent = new Intent(SettingAppActivity.this, WebViewActivity.class);
			intent.putExtra("title", "免责声明");
			intent.putExtra("url", HttpUtil.NO_PRESS);
			startActivity(intent);
		} else if (id == R.id.lt_about_us) {
			startActivity(new Intent(SettingAppActivity.this, AboutUsActivity.class));
		} else if (id == R.id.btn_back) {
			finish();
		} else if (id == R.id.btn_cut_user) {
			logout();
		}
	}

	public void update() {
		final ProgressDialog pd = new ProgressDialog(this);
		pd.setMessage("正在检查更新");
		pd.setCanceledOnTouchOutside(false);
		pd.show();

		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.forceUpdate(this);
		UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {

			@Override
			public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
				if (isFinishing())
					return;
				pd.dismiss();
				switch (updateStatus) {
				case UpdateStatus.Yes: // has update
					UmengUpdateAgent.showUpdateDialog(SettingAppActivity.this, updateInfo);
					break;
				case UpdateStatus.No: // has no update
					Toast.makeText(SettingAppActivity.this, "已经是最新版本", Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.NoneWifi: // none wifi
					Toast.makeText(SettingAppActivity.this, "没有wifi连接， 只在wifi下更新",
							Toast.LENGTH_SHORT).show();
					break;
				case UpdateStatus.Timeout: // time out
					Toast.makeText(SettingAppActivity.this, "网络请求超时", Toast.LENGTH_SHORT).show();
					break;
				}
			}
		});
	}

	// private void checkVersionAndUpdate() {
	// try {
	// HashMap<String, String> params = new HashMap<String, String>();
	// params.put("imei", PublicUtil.getDeviceUuid(SettingAppActivity.this));
	// params.put("appversion", PublicUtil.getVersionName(SettingAppActivity.this));
	// params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
	// params.put("membertype", "user");
	// params.put("appid", "3");
	// params.put("mobileparam", MyApplication.getInstance().getMobileParam(this));
	// MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_version_info,
	// VersionInfo.class, params, new Response.Listener() {
	// @Override
	// public void onResponse(Object response) {
	// VersionInfo info = (VersionInfo) response;
	// if (info.getSuccess() == 1) {
	// int newVersionCode = Integer.valueOf(info.getData()
	// .getVERSIONCODE());
	// int oldVersionCode = PublicUtil
	// .getVersionCode(SettingAppActivity.this);
	// if (newVersionCode > oldVersionCode) {
	// alertVersionUpdate(info);
	// } else {
	// showToast("您已经是最新版本，无需升级");
	// }
	// }
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_version_info);
	// }
	// });
	//
	// } catch (Exception e) {
	// // Log.d("chenhj,setting", "check");
	// e.printStackTrace();
	// }
	// }

	// 注销
	private void logout() {
		boolean login = SharePrefUtil.getBoolean(Conast.LOGIN);
		if (login) {
			Builder builder = new Builder(SettingAppActivity.this);
			builder.setTitle("提示").setMessage("您确定注销登录吗?")
					.setPositiveButton("确定", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							boolean pushStopped = JPushInterface
									.isPushStopped(getApplicationContext());
							if (!pushStopped)
								JPushInterface.stopPush(getApplicationContext());
							String other = SharePrefUtil.getString(Conast.USER_OTHER_LOGIN);
							if (!TextUtils.isEmpty(other)) {
								ShareSDK.initSDK(SettingAppActivity.this);
								if ("1".equals(other)) {
									QZone qq = new QZone(SettingAppActivity.this);
									if (qq.isValid()) {
										qq.getDb().removeAccount();
									}
								} else if ("2".equals(other)) {
									SinaWeibo Weibo = new SinaWeibo(SettingAppActivity.this);
									if (Weibo.isValid()) {
										Weibo.getDb().removeAccount();
									}
								}
							}

							UICore.eventTask(SettingAppActivity.this, SettingAppActivity.this,
									IResult.LOGOUT, "注销中...", null);

						}
					}).setNegativeButton("取消", null).show();
		}
	}

	/**
	 * @deprecated 
	 * @param info
	 */
	private void alertVersionUpdate(final VersionInfo info) {
		View view = LayoutInflater.from(this).inflate(R.layout.version_update_info, null);
		final TextView tvVersionName = (TextView) view.findViewById(R.id.tv_version_name);
		final TextView tvTime = (TextView) view.findViewById(R.id.tv_update_time);
		final TextView tv_upgrade_title = (TextView) view.findViewById(R.id.tv_upgrade_title);
		tv_upgrade_title.setVisibility(View.GONE);
		final TextView tvSize = (TextView) view.findViewById(R.id.tv_size);
		final TextView tv_new_feature = (TextView) view.findViewById(R.id.tv_new_feature);
		tvVersionName.setText("薏米医生(Android) " + info.getData().getVERSIONNAME());
		tvTime.setText("更新时间:" + info.getData().getUPDATETIME());
		tvSize.setText("文件大小:" + info.getData().getAPPSIZE());
		if (TextUtils.isEmpty(info.getData().getCONTENT())) {
			tv_upgrade_title.setVisibility(View.GONE);
			tv_new_feature.setVisibility(View.GONE);
		} else {
			tv_upgrade_title.setVisibility(View.VISIBLE);
			tv_new_feature.setVisibility(View.VISIBLE);
			tv_new_feature.setText(info.getData().getCONTENT());
		}

		boolean not_force_update = info.getData().getForce_update() != 1 ? false : true;// 是否强制升级

		MenuDialog.Builder alert = new MenuDialog.Builder(this);
		MenuDialog dialog = null;

		if (not_force_update) {
			dialog = alert
					.setTitle(getString(R.string.main_notice))
					.setContentView(view)
					.setPositiveButton(getString(R.string.main_shenji),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										Uri uri = Uri.parse(info.getData().getAPPFILE());
										Intent intent = new Intent(Intent.ACTION_VIEW, uri);
										startActivity(intent);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							})
					.setNegativeButton(getString(R.string.main_next),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									dialog.dismiss();
								}
							}).create();
		} else {
			dialog = alert
					.setTitle(getString(R.string.main_notice))
					.setContentView(view)
					.setPositiveButton(getString(R.string.main_shenji),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									try {
										Uri uri = Uri.parse(info.getData().getAPPFILE());
										Intent intent = new Intent(Intent.ACTION_VIEW, uri);
										startActivity(intent);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
							}).create();
		}

		dialog.setCanceledOnTouchOutside(not_force_update);
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

}
