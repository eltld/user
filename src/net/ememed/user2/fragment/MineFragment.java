package net.ememed.user2.fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.AccountRechargeActivity;
import net.ememed.user2.activity.DoctorHistoryOrderActivity;
import net.ememed.user2.activity.FinaceDetailsActivity;
import net.ememed.user2.activity.HistoryGuahaoActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.activity.MainActivity;
import net.ememed.user2.activity.PersonInfoActivity;
import net.ememed.user2.activity.SettingAppActivity;
import net.ememed.user2.baike.MyRelativeActivity;
import net.ememed.user2.baike.entity.MyJoinEntry;
import net.ememed.user2.baike.entity.MyJoinInfo;
import net.ememed.user2.entity.AppConfig;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.VersionInfo;
import net.ememed.user2.erh.activity.ErhMainActivity;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.medicalhistory.activity.ImageViewZoomActivity;
import net.ememed.user2.medicalhistory.activity.MedicalHistoryMainActivity;
import net.ememed.user2.message_center.MessageCenterActivity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.BitmapUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.FileUtil;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.MenuDialog;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import eu.janmuller.android.simplecropimage.CropImage;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * 
 * 
 */
public class MineFragment extends Fragment implements Callback, OnClickListener, BasicUIEvent {
	private static final String TAG = MineFragment.class.getSimpleName();
	public static final int EXEU_GET_DATA = 0;
	private static final int REQUEST_CODE_TAKE_PICTURE = 1;
	private static final int REQUEST_CODE_CROP_IMAGE = 2;
	private static final int REQUEST_CODE_GALLERY = 3;
	private static final int EXCU_UPLOAD_IMG = 4;
	private Button btn_login;
	private FrameLayout mContentView = null;
	private Handler handler = null;
	private MainActivity activity = null;
	private TextView tv_name;
	private CircleImageView image_person;
	private File picFile;
	private Uri photoUri;
	boolean isLogin = false;// 用于标示是否登录
	private TextView tv_balance;

	public AlertDialog.Builder builder = null;
	private LinearLayout ll_medical_history;
	private LinearLayout rl_dang_an;
	private LinearLayout sms_center;
	public AppConfig appConfig;

	private LinearLayout ll_my_praise;
	private LinearLayout ll_my_comment;
	private LinearLayout ll_my_share;

	private TextView tv_praise_num;
	private TextView tv_comment_num;
	private TextView tv_share_num;
	
	private boolean isFirst = true;

	public MineFragment() {
		this.activity = (MainActivity) getActivity();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		handler = new Handler(this);
		activity = (MainActivity) getActivity();
		Logger.dout(TAG + "onCreate");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时执行加载数据或者进度条等
			Logger.dout(TAG + "isVisibleToUser");
		} else {
			// 不可见时不执行操作
			Logger.dout(TAG + "unVisibleToUser");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		Logger.dout(TAG + "onCreateView");
		View view = inflater.inflate(R.layout.mine_layout, null);
		// getAffigData();
		mContentView = (FrameLayout) view.findViewById(R.id.mine_mainView);
		LinearLayout person_info = (LinearLayout) view.findViewById(R.id.person_info);
		LinearLayout finance_details = (LinearLayout) view.findViewById(R.id.finance_details);
		person_info.setOnClickListener(this);
		finance_details.setOnClickListener(this);
		LinearLayout apply_set = (LinearLayout) view.findViewById(R.id.apply_set);
		apply_set.setOnClickListener(this);
		LinearLayout ll_history_order = (LinearLayout) view.findViewById(R.id.ll_history_order);
		ll_history_order.setOnClickListener(this);
		LinearLayout ll_account_recharge = (LinearLayout) view
				.findViewById(R.id.ll_account_recharge);
		ll_account_recharge.setOnClickListener(this);
		LinearLayout ll_history_guahao = (LinearLayout) view.findViewById(R.id.ll_history_guahao);
		ll_history_guahao.setOnClickListener(this);
		ll_medical_history = (LinearLayout) view.findViewById(R.id.ll_medical_history);
		ll_medical_history.setOnClickListener(this);
		rl_dang_an = (LinearLayout) view.findViewById(R.id.rl_dang_an);
		rl_dang_an.setOnClickListener(this);

		ll_my_praise = (LinearLayout) view.findViewById(R.id.ll_my_praise);
		ll_my_praise.setOnClickListener(this);
		ll_my_comment = (LinearLayout) view.findViewById(R.id.ll_my_comment);
		ll_my_comment.setOnClickListener(this);
		ll_my_share = (LinearLayout) view.findViewById(R.id.ll_my_share);
		ll_my_share.setOnClickListener(this);

		sms_center = (LinearLayout) view.findViewById(R.id.sms_center);
		sms_center.setOnClickListener(this);
		image_person = (CircleImageView) view.findViewById(R.id.image_person);
		image_person.setOnClickListener(this);
		btn_login = (Button) view.findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		tv_name = (TextView) view.findViewById(R.id.doctor_name);

		tv_balance = (TextView) view.findViewById(R.id.tv_balance);

		tv_praise_num = (TextView) view.findViewById(R.id.tv_praise_num);
		tv_comment_num = (TextView) view.findViewById(R.id.tv_commemt_num);
		tv_share_num = (TextView) view.findViewById(R.id.tv_share_num);

		setTwoNew();
		// try {
		// if (!TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
		// btn_login.setVisibility(View.GONE);
		// tv_name.setVisibility(View.VISIBLE);
		// Log.e("XX", "测试来过这里:-)");
		// tv_name.setText(TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName)) ?
		// SharePrefUtil.getString(Conast.USER_Name) : SharePrefUtil.getString(Conast.realName));
		// if (null != activity && !TextUtils.isEmpty(SharePrefUtil.getString(Conast.AVATAR))) {
		// activity.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR), image_person,
		// Util.getOptions_big_avatar());
		// }
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
		
		return view;
	}
	
	/**
	 * @deprecated
	 * 
	 *             健康档案、电子病历开关请求。已在MainActivity中有此处销毁
	 */
	private void getAffigData() {
		try {
			// if (TimeUtil.checkIsNeedUpdate())
			// {//该标记代表每天第一次启动，目前每次进入应用都需要调用更新版本接口，所以先注释掉
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("imei", PublicUtil.getDeviceUuid(getActivity()));
			params.put("appversion", PublicUtil.getVersionName(getActivity()));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("membertype", "user");
			params.put("channel", "android");
			params.put("appid", "3");
			params.put("mobileparam", MyApplication.getInstance().getMobileParam(activity));
			Log.e("XX", "重要的params:" + params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_version_info,
					VersionInfo.class, params, new Response.Listener() {
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
							MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_version_info);
						}
					});

			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getMyJoin() {
		try {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_myjoin_info,
					MyJoinEntry.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response; // version类使用JSON，可传函数回调取值/
							message.what = IResult.GET_MY_JOIN;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							handler.sendMessage(message);
						}
					});

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void onResume() {
		super.onResume();
		// getAffigData();

		//刷新余额和自媒体数量
		if (UserPreferenceWrapper.isLogin()) {
			if (isFirst) {
				isFirst = false;
				getMyJoin();
			}
			tv_balance.setText(UserPreferenceWrapper.getUserBalance() + "元");
			showMyJoinNum();
		}

		try {
			if (!TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
				btn_login.setVisibility(View.GONE);
				tv_name.setVisibility(View.VISIBLE);
				tv_name.setText(TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName)) ? SharePrefUtil
						.getString(Conast.USER_Name) : SharePrefUtil.getString(Conast.realName));
				if (null != activity && !TextUtils.isEmpty(SharePrefUtil.getString(Conast.AVATAR))) {
					activity.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR),
							image_person, Util.getOptions_big_avatar());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (isLogin)
			try {
				isLogin = false;
				if (!TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
					btn_login.setVisibility(View.GONE);
					tv_name.setVisibility(View.VISIBLE);
					tv_name.setText(TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName)) ? SharePrefUtil
							.getString(Conast.USER_Name) : SharePrefUtil.getString(Conast.realName));
					if (null != activity
							&& !TextUtils.isEmpty(SharePrefUtil.getString(Conast.AVATAR))) {
						activity.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR),
								image_person, Util.getOptions_big_avatar());
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@Override
	public void onPause() {
		super.onStop();
	}

	@Override
	public boolean handleMessage(Message msg) {
		try {
			switch (msg.what) {
			case IResult.UPLOAD_IMG:
				activity.destroyDialog();
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				int success = (Integer) map.get("success");
				if (success == 1) {
					activity.showToast(activity.getString(R.string.change_avatar_success));
					SharePrefUtil.putString(Conast.AVATAR, (String) map.get("cert_url"));
					SharePrefUtil.commit();
					activity.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR),
							image_person, Util.getOptions_big_avatar(),
							Util.getImageLoadingListenerInstance());
				}
				break;
			case IResult.NET_ERROR:

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
										appConfig.getModule_ehr());
							SharePrefUtil.commit();
							if (!TextUtils.isEmpty(appConfig.getModule_emr()))
								// Util.MODULE_EMR = appConfig.getModule_emr();
								SharePrefUtil.putString(Conast.Parent_module_emr,
										appConfig.getModule_emr());
							SharePrefUtil.commit();
						}
						setTwoNew();
					}
				}
				break;
			case IResult.GET_MY_JOIN:
				MyJoinEntry entry = (MyJoinEntry) msg.obj;
				if (null != entry && 1 == entry.getSuccess()) {
					MyJoinInfo info = entry.getData();
					if (null != info) {
						UserPreferenceWrapper.setMyPraiseCount(info.getMypraise_total_num());
						UserPreferenceWrapper.setMyCommentCount(info.getMycomment_total_num());
						UserPreferenceWrapper.setMyShareCount(info.getMyshare_total_num());
						showMyJoinNum();
					}
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void showMyJoinNum() {
		tv_praise_num.setText(UserPreferenceWrapper.getMyPraiseCount() + " 个");
		tv_comment_num.setText(UserPreferenceWrapper.getMyCommentCount() + " 条");
		tv_share_num.setText(UserPreferenceWrapper.getMyShareCount() + " 次");
	}

	private void setTwoNew() {
		if (SharePrefUtil.getString(Conast.Parent_module_ehr).equals("1")) {
			rl_dang_an.setVisibility(View.GONE);
		} else {
			rl_dang_an.setVisibility(View.GONE);
		}
		if (SharePrefUtil.getString(Conast.Parent_module_emr).equals("1")) {
			ll_medical_history.setVisibility(View.GONE);
		} else {
			ll_medical_history.setVisibility(View.GONE);
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_TAKE_PICTURE:
			if (resultCode == activity.RESULT_OK) {
				startCropImage();
			}
			break;
		case REQUEST_CODE_CROP_IMAGE:
			if (data == null) {
				return;
			}
			final String path = data.getStringExtra(CropImage.IMAGE_PATH);
			if (path == null) {
				return;
			}
			try {
				if (resultCode == activity.RESULT_OK) {
					if (photoUri != null) {
						// TODO upload the image
						Bitmap bm = BitmapUtil.getBitmapByUri(activity, photoUri);
						image_person.setImageBitmap(bm);
						byte[] imgDatas = FileUtil.getBytes(picFile);
						UICore.eventTask(this, activity, EXCU_UPLOAD_IMG, "头像上传", imgDatas);// 加载数据入口
					}

				} else {
					if (null != picFile && picFile.exists()) {
						picFile.delete();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case REQUEST_CODE_GALLERY:
			try {
				InputStream inputStream = activity.getContentResolver().openInputStream(
						data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(picFile);
				BitmapUtil.copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
				startCropImage();
			} catch (Exception e) {
				// Log.e(TAG, "Error while creating temp file", e);
			}

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		try {
			int id = v.getId();

			if (id == R.id.person_info) {// 资料设置
				isLogin = true;
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					startActivity(new Intent(activity, PersonInfoActivity.class));
				else
					showIsLogin();
			} else if (id == R.id.finance_details) {// 我的余额
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					startActivity(new Intent(activity, FinaceDetailsActivity.class));
				else
					showIsLogin();
			} else if (id == R.id.ll_history_guahao) {// 历史订单
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					startActivity(new Intent(activity, HistoryGuahaoActivity.class));
				else
					showIsLogin();
			} else if (id == R.id.apply_set) {// 关于薏米医生（原先为“设置”）
				isLogin = true;
				startActivity(new Intent(activity, SettingAppActivity.class));
			} else if (id == R.id.btn_login) {// 未登录，登录按钮
				isLogin = true;
				Intent intent = new Intent(activity, LoginActivity.class);
				intent.putExtra("origin", MineFragment.class.getSimpleName());
				startActivity(intent);
			} else if (id == R.id.ll_account_recharge) { // 账户充值
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					startActivity(new Intent(activity, AccountRechargeActivity.class));
				else
					showIsLogin();
			} else if (id == R.id.ll_history_order) { // 历史订单
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					startActivity(new Intent(activity, DoctorHistoryOrderActivity.class));
				else
					showIsLogin();
			} else if (id == R.id.image_person) { // 头像上传
				if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
					// activity.showToast(activity.getString(R.string.login_first));
					showIsLogin();// 未登录跳转的loginActivity
				} else {
					changeAvatar();// 已登录显示本地相册与照相
				}
			} else if (id == R.id.ll_medical_history) {
				Intent intent = new Intent(activity, MedicalHistoryMainActivity.class);
				startActivity(intent); // 进入病历夹
			} else if (id == R.id.rl_dang_an) {
				Intent intent = new Intent(activity, ErhMainActivity.class);
				startActivity(intent); // 进入健康档案
			} else if (id == R.id.sms_center) { // 消息中心
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					startActivity(new Intent(activity, MessageCenterActivity.class));
				else
					showIsLogin();
			} else if (id == R.id.ll_my_praise) { // 我赞过的
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					MyRelativeActivity.startAction(activity, MyRelativeActivity.TYPE_MY_PRAISE);
				else
					showIsLogin();
			} else if (id == R.id.ll_my_comment) { // 我评论过的
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					MyRelativeActivity.startAction(activity, MyRelativeActivity.TYPE_MY_COMMENT);
				else
					showIsLogin();
			} else if (id == R.id.ll_my_share) { // 我分享过的
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
					MyRelativeActivity.startAction(activity, MyRelativeActivity.TYPE_MY_SHARE);
				else
					showIsLogin();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showIsLogin() {
		// TODO Auto-generated method stub
		isLogin = true;
		Intent i = new Intent(activity, LoginActivity.class);
		i.putExtra("origin", MineFragment.class.getSimpleName());
		activity.startActivity(i);

		// builder = new
		// AlertDialog.Builder(activity,AlertDialog.THEME_HOLO_LIGHT);
		// builder.setTitle("登录薏米")
		// .setIcon(R.drawable.ic_launcher)
		// .setMessage("你还没有登录到薏米医生,请先登录再使用")
		// .setPositiveButton("登录", new AlertDialog.OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// Intent i = new Intent(activity,LoginActivity.class);
		// activity.startActivity(i);
		// }
		// })
		// .setNegativeButton("取消", null)
		// .show();

	}

	private void changeAvatar() {

		if (NetWorkUtils.detect(activity)) {
			final Context dialogContext = new ContextThemeWrapper(activity,
					android.R.style.Theme_Light);
			String[] choices = new String[2];
			choices[0] = getString(R.string.change_avatar_take_photo);
			choices[1] = getString(R.string.change_avatar_album);
			// choices[2] = getString(R.string.change_check_album);

			MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
			MenuDialog dialog = builder.setTitle(R.string.change_avatar)
					.setItems(choices, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:
								String status = Environment.getExternalStorageState();
								if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
									takePicture();
								}
								break;
							case 1:
								openGallery();
								break;
							case 2:
								checkHead();
								break;
							}
						}
					}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}

	}

	protected void checkHead() {
		Bundle bundle = new Bundle();
		bundle.putString("url", SharePrefUtil.getString(Conast.AVATAR));
		Utils.startActivity(activity, ImageViewZoomActivity.class, bundle);
	}

	/** 4.0 */
	private void takePicture() {

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
				picFile = new File(activity.getFilesDir() + FileUtil.ROOT_DIRECTORY + "/"
						+ FileUtil.IMG_UPLOAD, UUID.randomUUID().toString() + ".jpeg");
			}

			photoUri = Uri.fromFile(picFile);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {
			// Log.d(TAG, "cannot take picture", e);
		}
	}

	private void startCropImage() {
		Intent intent = new Intent(activity, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, picFile.getPath());
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra("outputX", 1000);
		intent.putExtra("outputY", 1000);
		intent.putExtra(CropImage.ASPECT_X, 1);
		intent.putExtra(CropImage.ASPECT_Y, 1);
		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	/** for4.3 */
	private void openGallery() {
		try {
			File pictureFileDir = new File(FileUtil.PATH, FileUtil.IMG_UPLOAD);
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			picFile = new File(pictureFileDir, UUID.randomUUID().toString() + ".jpeg");
			if (!picFile.exists()) {
				picFile.createNewFile();
			}
			photoUri = Uri.fromFile(picFile);
			Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
			photoPickerIntent.setType("image/*");
			startActivityForResult(photoPickerIntent, REQUEST_CODE_GALLERY);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void set_user_avatar(final byte[] imageData) {
		try {
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
			params.add(new BasicNameValuePair("ext", "jpg"));
			params.add(new BasicNameValuePair("dir", "1"));
			params.add(new BasicNameValuePair("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN)));

			String json = HttpUtil.uploadFile(HttpUtil.URI + HttpUtil.set_user_avatar, params,
					imageData);
			json = TextUtil.substring(json, "{");
			// System.out.println("json=" + json);
			HashMap<String, Object> map = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(json);
			map.put("success", obj.getInt("success"));
			map.put("errormsg", obj.getString("errormsg"));
			JSONObject data_obj = obj.getJSONObject("data");
			if (null != data_obj) {
				map.put("cert_url", data_obj.getString("AVATAR"));
			}
			Message message = new Message();
			message.obj = map;
			message.what = IResult.UPLOAD_IMG;
			handler.sendMessage(message);
		} catch (IOException e) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		} catch (Exception e) {
			handler.sendEmptyMessage(IResult.DATA_ERROR);
			e.printStackTrace();
		}
	}

	@Override
	public void execute(int mes, Object obj) {
		if (mes == EXCU_UPLOAD_IMG) {
			set_user_avatar((byte[]) obj);
		}
	}

}
