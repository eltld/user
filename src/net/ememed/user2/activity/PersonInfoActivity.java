package net.ememed.user2.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.CustomViewPager;
import com.viewpagerindicator.TabPageIndicator;

import eu.janmuller.android.simplecropimage.CropImage;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.dialog.Effectstype;
import net.ememed.user2.dialog.NiftyDialogBuilder;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.entity.UserInfo;
import net.ememed.user2.entity.UserInfoEntry;
import net.ememed.user2.erh.activity.ErhPersonBaseEditActivity;
import net.ememed.user2.erh.util.SingleDmAdapter;
import net.ememed.user2.fragment.InfoSettingFragment;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.medicalhistory.activity.ImageViewZoomActivity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.BitmapUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.FileUtil;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.widget.MenuDialog;
import net.ememed.user2.widget.MyProgressDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 资料设置
 * 
 * @author pengwl
 * 
 */
public class PersonInfoActivity extends BasicActivity {

	private ImageView iv_portrait;
	private EditText et_name;
	private TextView et_sex, tv_phone, et_birthday;
	private LinearLayout person_name;
	private Button btn_addhealth;
	private String current_service;
	private String origin_ui;
	private DoctorEntry entry;
	private String timeStr;
	private NiftyDialogBuilder dialogBuilder;
	private Effectstype effect;
	private TextView tv_city;

	private static final int GET_CITY = 300;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.fragment_set_base_profil);
		dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
		current_service = getIntent().getStringExtra("current_service");
		origin_ui = getIntent().getStringExtra("origin");
		entry = (DoctorEntry) getIntent().getSerializableExtra("entry");
	}

	@Override
	protected void setupView() {
		super.setupView();
		// title
		((TextView) findViewById(R.id.top_title)).setText(getString(R.string.person_info));

		// 保存
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText(getString(R.string.save));

		iv_portrait = (ImageView) findViewById(R.id.iv_portrait);
		et_name = (EditText) findViewById(R.id.et_name);
		person_name = (LinearLayout) findViewById(R.id.person_name);
		et_sex = (TextView) findViewById(R.id.et_sex);
		et_birthday = (TextView) findViewById(R.id.et_birthday);
		tv_phone = (TextView) findViewById(R.id.tv_phone);
		tv_city = (TextView) findViewById(R.id.tv_city);

		DisplayImageOptions options = new DisplayImageOptions.Builder().displayer(
				new RoundedBitmapDisplayer(10)).build();
		ImageLoader.getInstance().displayImage(SharePrefUtil.getString(Conast.AVATAR), iv_portrait,
				options);
		et_name.setText(SharePrefUtil.getString(Conast.realName));
		String sex = SharePrefUtil.getString(Conast.USER_SEX);
		et_sex.setText(sex.equals("1") ? "男" : "女");
		et_birthday.setText(SharePrefUtil.getString(Conast.USER_BIRTHDAY));
		tv_phone.setText(SharePrefUtil.getString(Conast.USER_MOBILE));
		tv_city.setText(SharePrefUtil.getString(Conast.OFTEN_CITY));

		person_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				et_name.setCursorVisible(true);
				Editable b = et_name.getText();
				et_name.setSelection(b.length());
			}

		}

		);
		et_name.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				et_name.setCursorVisible(true);
				Editable b = et_name.getText();
				et_name.setSelection(b.length());
			}
		}

		);
		et_name.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					et_name.setSelection(et_name.getText().toString().length());
				}

			}
		});

	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_save) {// 保存个人资料
			setBaseProfile();
			et_name.setCursorVisible(false);
		} else if (view.getId() == R.id.ll_birthday) {// 设置生日
			setBirthday();
			et_name.setCursorVisible(false);
		} else if (view.getId() == R.id.ll_sex) {// 设置性别
			setSex();
			et_name.setCursorVisible(false);
		} else if (view.getId() == R.id.ll_update_pwd) {
			startActivity(new Intent(this, SettingPwdActivity.class));
			et_name.setCursorVisible(false);
		} else if (view.getId() == R.id.person_ll) {
			et_name.setCursorVisible(false);
		} else if (view.getId() == R.id.ll_add_visitperson) {
			Intent i = new Intent(this, VisitPersonChoiceActivity2.class);
			i.putExtra("isflag", false);
			startActivity(i);
		} else if (view.getId() == R.id.btn_addhealth) {// 保存个人资料
			setBaseProfile();
		} else if (view.getId() == R.id.ll_alert_mobile) {// 修改手机号码
			startActivity(new Intent(this, AlertMobileActivity.class));
		} else if (view.getId() == R.id.person_portrait) {
			changeAvatar();
		} else if (view.getId() == R.id.ll_city) {
			Intent intent = new Intent(this, CityActivity.class);
			intent.putExtra("getCity", true);
			startActivityForResult(intent, GET_CITY);
		}
	}

	private void changeAvatar() {

		final Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Light);
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

	}

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
				picFile = new File(getFilesDir() + FileUtil.ROOT_DIRECTORY + "/"
						+ FileUtil.IMG_UPLOAD, UUID.randomUUID().toString() + ".jpeg");
			}
			photoUri = Uri.fromFile(picFile);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {
		}
	}

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

	protected void checkHead() {
		Bundle bundle = new Bundle();
		bundle.putString("url", SharePrefUtil.getString(Conast.AVATAR));
		Utils.startActivity(this, ImageViewZoomActivity.class, bundle);
	}

	private static final int EXCU_UPLOAD_IMG = 4;

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case GET_CITY:
			if (resultCode == RESULT_OK) {
				if (data == null) {
					return;
				}
				String city = data.getStringExtra("city");
				tv_city.setText(city);
			}
			break;
		case REQUEST_CODE_TAKE_PICTURE:
			if (resultCode == RESULT_OK) {
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
				if (resultCode == RESULT_OK) {
					if (photoUri != null) {
						// TODO upload the image
						Bitmap bm = BitmapUtil.getBitmapByUri(this, photoUri);
						iv_portrait.setImageBitmap(bm);
						byte[] imgDatas = FileUtil.getBytes(picFile);
						UICore.eventTask(mEvent, this, EXCU_UPLOAD_IMG, "头像上传", imgDatas);// 加载数据入口
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
				InputStream inputStream = getContentResolver().openInputStream(data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(picFile);
				BitmapUtil.copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
				startCropImage();
			} catch (Exception e) {
			}

			break;
		default:
			break;
		}
	}

	private void startCropImage() {
		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, picFile.getPath());
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra("outputX", 1000);
		intent.putExtra("outputY", 1000);
		intent.putExtra(CropImage.ASPECT_X, 1);
		intent.putExtra(CropImage.ASPECT_Y, 1);
		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	@Override
	protected void getData() {
		// getUserInfo();
		super.getData();
	}

	// 设置性别
	private void setSex() {
		final String[] txts = new String[] { "男", "女" };
		View view2 = LayoutInflater.from(this).inflate(R.layout.custom_view, null);
		effect = Effectstype.Slideleft;
		String defaultStr = et_sex.getText().toString().trim();

		dialogBuilder.withTitle("性别").withTitleColor("#000000").withMessage(null)
				.withMessageColor("#000000")
				.withIcon(getResources().getDrawable(R.drawable.medical_history_ememed2))
				.isCancelableOnTouchOutside(true).withDuration(700).withEffect(effect)
				.setCustomView(view2, this).show();

		ListView listView = (ListView) view2.findViewById(R.id.list);
		final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(this, txts, defaultStr);
		listView.setAdapter(singleBaseAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				singleBaseAdapter.setName(txts[arg2]);
				singleBaseAdapter.notifyDataSetChanged();
				et_sex.setText(txts[arg2]);
				dialogBuilder.dismiss();
			}
		});
	}

	// 设置生日
	private void setBirthday() {
		String time = et_birthday.getText().toString().trim();
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
		int year = mycalendar.get(Calendar.YEAR);
		int month = mycalendar.get(Calendar.MONTH) + 1;
		int day = mycalendar.get(Calendar.DAY_OF_MONTH);
		if (!TextUtils.isEmpty(time)) {
			String[] arrs = time.split("-");
			year = Integer.valueOf(arrs[0]);
			month = Integer.valueOf(arrs[1]);
			day = Integer.valueOf(arrs[2]);
		}
		DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {

			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				et_birthday.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
						+ String.format("%02d", dayOfMonth));
			}
		}, year, month - 1, day);
		dialog.show();
	}

	/** 基本资料 */
	public void setBaseProfile() {
		String etName = et_name.getText().toString().trim();
		String etSex = et_sex.getText().toString().trim();
		String etBirthday = et_birthday.getText().toString().trim();
		String etPhone = tv_phone.getText().toString().trim();
		String city = tv_city.getText().toString().trim();

		SharePrefUtil.putString(Conast.USER_SEX, etSex.equals("男") ? "1" : "0");
		SharePrefUtil.putString(Conast.OFTEN_CITY, city);
		SharePrefUtil.commit();

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("realname", etName);
			params.put("sex", etSex.equals("男") ? "1" : "0");
			params.put("mobile", etPhone);
			params.put("birthday", etBirthday);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_base_profile,
					SetProfile.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.BASE_PROFILE;
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
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	/**
	 * 获取用户信息
	 * 
	 * @deprecated v2.2.3 use SharePrefUtil instead of the request
	 */
	private void getUserInfo() {
		// TODO Auto-generated method stub
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_user_info, UserInfo.class,
					params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.USER_INFO;
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
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		try {
			switch (msg.what) {
			case IResult.UPLOAD_IMG:
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				String avater = (String) map.get("cert_url");
				SharePrefUtil.putString(Conast.AVATAR, avater);
				break;
			case IResult.BASE_PROFILE:
				// mPullToRefreshLayout.setRefreshComplete();
				SetProfile sp = (SetProfile) msg.obj;
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						SharePrefUtil.putString(Conast.realName, et_name.getText().toString());
						SharePrefUtil.putString(Conast.realName, et_name.getText().toString()
								.trim());
						SharePrefUtil.putString(Conast.USER_SEX, et_sex.getText().toString());
						SharePrefUtil.putString(Conast.USER_BIRTHDAY, et_birthday.getText()
								.toString());
						SharePrefUtil.putString(Conast.USER_MOBILE, tv_phone.getText().toString());
						SharePrefUtil.putString(Conast.USER_ADDRESS, tv_city.getText().toString());
						SharePrefUtil.commit();
						showToast("修改个人资料成功");
						if (!TextUtils.isEmpty(origin_ui)
								&& origin_ui.equals(DoctorClininActivity.class.getSimpleName())) {
							Intent intent = null;
							if (current_service.equals("1")) {
								intent = new Intent(this, BuyTextConsultActivity.class);
							} else if (current_service.equals("2")) {
								intent = new Intent(this, BuyPhoneConsultActivity.class);
							} else if (current_service.equals("3")) {
								intent = new Intent(this, BuyJiuyiActivity.class);
							} else if (current_service.equals("4")) {
								intent = new Intent(this, BuyShangmenActivity.class);
							} else if (current_service.equals("5")) {
								intent = new Intent(this, BuyZhuyuanActivity.class);
							} else if (current_service.equals("15")) {
								intent = new Intent(this, PrivateDoctorActivity.class);
								intent.putExtra("price", "100");
							} else if (current_service.equals("16")) {
								intent = new Intent(this, BuyCustomActivity.class);
							}
							intent.putExtra("doctorid", entry.getDOCTORID());
							intent.putExtra("doctorServiceEntry", entry.getOFFER_SERVICE());
							// intent.putExtra("entry", entry);
							intent.putExtra("doctor_name", entry.getREALNAME());
							startActivity(intent);
						}
						finish();
					} else {
						showToast(sp.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.USER_INFO:
				UserInfo userInfo = (UserInfo) msg.obj;
				if (null != userInfo) {
					if (userInfo.getSuccess() == 1) {
						UserInfoEntry data = userInfo.getData();
						et_name.setText(data.getREALNAME());
						et_sex.setText(data.getSEX().equals("1") ? "男" : "女");
						if (data.getBIRTHDAY() != null) {
							et_birthday.setText(data.getBIRTHDAY());
							timeStr = data.getBIRTHDAY();
						}
						tv_phone.setText(data.getMOBILE());
					} else {
						showToast(userInfo.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
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
		Dialog dialog = builder.setTitle(getString(R.string.system_info)).setMessage(msg)
				.setPositiveButton(getString(R.string.add_health_record_know), null).create();
		dialog.setCancelable(false);
		if (!finish) {
			dialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish = true;
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.set_base_profile);
	}

	private boolean finish = false;

	private BasicUIEvent mEvent = new BasicUIEvent() {

		@Override
		public void execute(int mes, Object obj) {
			if (mes == EXCU_UPLOAD_IMG) {
				requestSetUserAvater((byte[]) obj);
			}
		}
	};

	private void requestSetUserAvater(final byte[] imageData) {
		try {
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
			params.add(new BasicNameValuePair("ext", "jpg"));
			params.add(new BasicNameValuePair("dir", "1"));
			params.add(new BasicNameValuePair("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN)));

			String json = HttpUtil.uploadFile(HttpUtil.URI + HttpUtil.set_user_avatar, params,
					imageData);
			json = TextUtil.substring(json, "{");
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

}
