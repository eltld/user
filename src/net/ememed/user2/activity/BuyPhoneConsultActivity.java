package net.ememed.user2.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.ememed.user2.R;
import net.ememed.user2.entity.AddSingle;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.OrderInfoEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.BitmapUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.FileUtil;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.widget.MenuDialog;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

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
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;

import eu.janmuller.android.simplecropimage.CropImage;

public class BuyPhoneConsultActivity extends BasicActivity {
	private static final int REQUEST_CODE_TAKE_PICTURE = 1;
	private static final int REQUEST_CODE_GALLERY = 2;
	private static final int REQUEST_CODE_CROP_IMAGE = 3;
	private static final int EXCU_UPLOAD_IMG = 4;
	private TextView top_title;
	// private Button btn_addhealth;
	// private DoctorEntry entry;
	private String doctorid;
	private String doctor_name;
	private DoctorServiceEntry entry;
	private TextView tv_price;
	private float price;
	private String upload_file = "";// 图片url
	private File picFile;
	private Uri photoUri;
	private String doctor_avatar;
	private Bitmap bm;
	private ImageView tv_upload_pic;
	private Button btn_pay_money;
	private Button btn_upload_pic;
	private EditText et_disease_description;
	private String content;

	private String ORDERID;
	private String path;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_buy_phone_consult);
		// entry = (DoctorEntry) getIntent().getSerializableExtra("entry");
		doctorid = getIntent().getStringExtra("doctorid");
		entry = (DoctorServiceEntry) getIntent().getSerializableExtra("doctorServiceEntry");
		doctor_name = getIntent().getStringExtra("doctor_name");
		doctor_avatar = getIntent().getStringExtra("doctor_avatar");
		ORDERID = getIntent().getStringExtra("orderid");
	}

	@Override
	protected void setupView() {
		super.setupView();
		btn_upload_pic = (Button) findViewById(R.id.btn_upload_pic);
		top_title = (TextView) findViewById(R.id.top_title);
		TextView tv_phone_service_price = (TextView) findViewById(R.id.tv_phone_service_price);
		tv_phone_service_price.setText(doctor_name + "医生预约通话服务，费用:");
		top_title.setText("购买" + doctor_name + "预约通话");
		// btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		// btn_addhealth.setVisibility(View.VISIBLE);
		// btn_addhealth.setText("说明");
		// btn_addhealth.setBackgroundDrawable(null);
		tv_price = (TextView) findViewById(R.id.tv_price);
		if(entry!=null){
			
			price = entry.getOFFER_CALL().getOFFER_PRICE();
		}else{
			price = 0;
		}
		tv_price.setText("￥" + String.valueOf(price));
		tv_upload_pic = (ImageView) findViewById(R.id.tv_upload_pic);
		et_disease_description = (EditText) findViewById(R.id.et_disease_description);
		btn_pay_money = (Button) findViewById(R.id.btn_pay_money);
		// et_disease_description.setHint("建议您先在输入框内输入您要咨询的详细内容、或者上传能帮助医生准确、快速评判情况的病历、影像图片等资料。");
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_pay_money) {

			if (TextUtils.isEmpty(et_disease_description.getText().toString().trim())) {
				showToast(getString(R.string.condition_details));
				return;
			}
			content = et_disease_description.getText().toString().trim();
			UICore.eventTask(BuyPhoneConsultActivity.this, BuyPhoneConsultActivity.this, IResult.BUY_SERVICE, "订单提交中..", et_disease_description.getText().toString().trim());
		} else if (view.getId() == R.id.btn_upload_pic) {
			changeAvatar();// 上传头像
		} else if (view.getId() == R.id.tv_upload_pic) {
			Intent intent = new Intent(this, ShowBigImage.class);
			intent.putExtra("uri", photoUri);
			startActivity(intent);
		}
	}

	private void changeAvatar() {

		if (NetWorkUtils.detect(this)) {
			final Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Light);
			String[] choices = new String[2];
			choices[0] = getString(R.string.change_avatar_take_photo);
			choices[1] = getString(R.string.change_avatar_album);

			MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
			MenuDialog dialog = builder.setTitle(R.string.shangmen_upload_pic).setItems(choices, new DialogInterface.OnClickListener() {

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
					}
				}
			}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
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
				picFile = new File(this.getFilesDir() + FileUtil.ROOT_DIRECTORY + "/" + FileUtil.IMG_UPLOAD, UUID.randomUUID().toString() + ".jpeg");
			}

			photoUri = Uri.fromFile(picFile);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {
			// Log.d(TAG, "cannot take picture", e);
		}
	}

	private void setSeekhelpPic(byte[] imageData) {
		try {
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("ext", "jpg"));
			params.add(new BasicNameValuePair("dir", "9"));
			String json = HttpUtil.uploadFile(HttpUtil.URI + HttpUtil.set_seekhelp_pic, params, imageData);
			json = TextUtil.substring(json, "{");
			// System.out.println("json=" + json);
			HashMap<String, Object> map = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(json);
			map.put("success", obj.getInt("success"));
			map.put("errormsg", obj.getString("errormsg"));
			JSONObject data_obj = obj.optJSONObject("data");
			if (null != data_obj) {
				map.put("SEEKHELP_PIC", data_obj.getString("SEEKHELP_PIC"));
			}
			Message message = new Message();
			message.obj = map;
			message.what = IResult.SEEKHELP_PIC;
			handler.sendMessage(message);
		} catch (IOException e) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		} catch (Exception e) {
			handler.sendEmptyMessage(IResult.DATA_ERROR);
			e.printStackTrace();
		}
	}

	/**
	 * 添加服务订单
	 */
	private void addSingleOrder(String desc) {
		try {
			if (NetWorkUtils.detect(this)) {
				String money_null = "1";
				String free = "0";
				if (price > 0) {
					money_null = "0";
				} else if (price == 0) {
					free = "1";
				}
				ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
				params.add(new BasicNameValuePair("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
				params.add(new BasicNameValuePair("channel", "android"));
				params.add(new BasicNameValuePair("memberid", SharePrefUtil.getString(Conast.MEMBER_ID)));
				params.add(new BasicNameValuePair("doctorid", doctorid));
				params.add(new BasicNameValuePair("free", free));
				params.add(new BasicNameValuePair("ordertype", "2"));
				params.add(new BasicNameValuePair("price", price + ""));

				params.add(new BasicNameValuePair("money_null", money_null));
				params.add(new BasicNameValuePair("content", desc));
				params.add(new BasicNameValuePair("upload_file", upload_file));
				params.add(new BasicNameValuePair("appversion", PublicUtil.getVersionName(BuyPhoneConsultActivity.this)));
				if(ORDERID!=null){
					params.add(new BasicNameValuePair("packet_buy_orderid", ORDERID));
				}

				String content;
				try {
					content = HttpUtil.getString(HttpUtil.URI + HttpUtil.add_single_order, params, HttpUtil.POST);
					content = TextUtil.substring(content, "{");
					Gson gson = new Gson();
					AddSingle reason = gson.fromJson(content, AddSingle.class);
					Message msg = Message.obtain();
					msg.what = IResult.ADD_SINGLE_ORDER;
					msg.obj = reason;
					handler.sendMessage(msg);
				} catch (IOException e) {
					e.printStackTrace();
					Message message = new Message();
					message.obj = e.getMessage();
					message.what = IResult.DATA_ERROR;
					handler.sendMessage(message);
				}
			} else {
				handler.sendEmptyMessage(IResult.NET_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case REQUEST_CODE_TAKE_PICTURE:
			if (resultCode == RESULT_OK) {
				startCropImage();
			}
			break;
		case REQUEST_CODE_CROP_IMAGE:
			if (data == null) {
				return;
			}
			path = data.getStringExtra(CropImage.IMAGE_PATH);
			if (path == null) {
				return;
			}
			try {
				if (resultCode == RESULT_OK) {
					if (photoUri != null) {
						// TODO upload the image
						bm = BitmapUtil.getBitmapByUri(this, photoUri);
						// image_person.setImageBitmap(bm);
						byte[] imgDatas = FileUtil.getBytes(picFile);
						UICore.eventTask(this, this, EXCU_UPLOAD_IMG, "图片上传中...", imgDatas);// 加载数据入口
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
				InputStream inputStream = this.getContentResolver().openInputStream(data.getData());
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
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		if (mes == EXCU_UPLOAD_IMG) {
			setSeekhelpPic((byte[]) obj);
		} else if (mes == IResult.BUY_SERVICE) {
			String desc = (String) obj;
			// System.out.println("desc========" + desc);
			addSingleOrder(desc);
		}
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		if (msg.what == IResult.ADD_SINGLE_ORDER) {
			AddSingle entry = (AddSingle) msg.obj;
			if (null != entry) {
				if (entry.getSuccess() == 1) {
					if (price == 0) {// 直接发起会话
						Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
						intent.putExtra("doctor_name", doctor_name);// 医生姓名
						intent.putExtra("tochat_userId", entry.getData().getDOCTORID());
						intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
						intent.putExtra("orderid", entry.getData().getORDERID());// 订单id
						intent.putExtra("serviceid", entry.getData().getSERVICEID());// 服务id
						intent.putExtra("from_activity", BuyPhoneConsultActivity.class.getSimpleName());// 来源页
						Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
						sendBroadcast(receive);																	// 用于标记发送系统消息
						startActivity(intent);
						finish();
						
						String doctor = "新服务请求：预约通话。     请在4小时内约定通话时间预约通话，" +
								"是指通过电话沟通的方式进行的远程问诊服务。您需要与用户文字沟通协商该服务的具体时间，并设置服务时间反馈给用户。您可以预约最近8小时内的可服务时间！";
						String user = "感谢购买"+doctor_name+"医生的预约通话服务，请尽快和医生沟通确认通话时间。";
						
						String systemMSG = toStringJSON(doctor, user);
						
						sendEMMessage(systemMSG, entry, true, "2");
						sendEMMessage(content, entry, false, "2");
						if(path!=null){
						sendImgSMG(path, entry, "2");
						}
					} else {
						Intent intent = new Intent(this, PayingActivity.class);// 去支付
						intent.putExtra("doctor_name", doctor_name);
						intent.putExtra("title", "预约通话");
						intent.putExtra("orderId", entry.getData().getORDERID());
						intent.putExtra("serviceId", entry.getData().getSERVICEID());
						intent.putExtra("price", Double.valueOf(price));
						intent.putExtra("ordertype", "2");
						intent.putExtra("path", path);
						intent.putExtra("dext", content);
						intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
						startActivity(intent);
						finish();
					}

				} else {// 代表已经购买过，直接跳转到聊天
					Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
					intent.putExtra("doctor_name", doctor_name);// 医生姓名
					intent.putExtra("tochat_userId", doctorid);
					intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
					startActivity(intent);
					finish();
					showToast(entry.getErrormsg());
				}
			}
		} else if (msg.what == IResult.SEEKHELP_PIC) {
			destroyDialog();
			HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
			int success = (Integer) map.get("success");
			if (success == 1) {
				tv_upload_pic.setImageBitmap(bm);
				upload_file = (String) map.get("SEEKHELP_PIC");
				btn_upload_pic.setText("重新上传");
				showToast("图片上传成功");
			} else {
				showToast((String) map.get("errormsg"));
			}
		} else if (msg.what == IResult.NET_ERROR) {

		} else if (msg.what == IResult.DATA_ERROR) {

		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	
	public void sendEMMessage(String textMSG , AddSingle entry,boolean isSystemMsg,String orderType) {
		System.out.println("系统消息发送");
		String ext = generateExtMessage(entry, isSystemMsg, orderType);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		TextMessageBody txtBody = new TextMessageBody(textMSG);
		// 设置消息body
		message.addBody(txtBody);
		if (!TextUtils.isEmpty(ext))
			message.setAttribute("ext", ext);
		
		// 设置要发给谁,用户username或者群聊groupid
		message.setReceipt(entry.getData().getDOCTORID());
		// 把messgage加到conversation中
		EMConversation conversation = EMChatManager.getInstance().getConversation(entry.getData().getDOCTORID());
		conversation.addMessage(message);
		
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				System.out.println("系统发送成功");
			}

			@Override
			public void onError(int code, String error) {
				System.out.println("系统发送失败");
			}

			@Override
			public void onProgress(int progress, String status) {
			}

		});
		
	}

	/***
	 * 附加到聊天消息中的备用字段
	 * 
	 * @return
	 */
	public String generateExtMessage(AddSingle entry, boolean isSystemMsg,String orderType) {
		JsonObject obj = new JsonObject();
		// "ISSYSTEMMSG": "0", //是否系统消息（1是，0为否）
		// "CHANNEL": "android", //来源（android|ios）
		// "ORDERID": "123", //订单ID
		// "SERVICEID": "1", //服务ID
		// "PACKET_BUY_ID": "1", //套餐购买ID
		// "USERID": "6998", //用户ID
		// "DOCTORID": "5226", //医生ID
		// "ORDERTYPE": "1" //订单类型
		obj.addProperty("SERVICEID", entry.getData().getSERVICEID());
		obj.addProperty("DOCTORID", entry.getData().getDOCTORID());
		obj.addProperty("ORDERTYPE", orderType);
		obj.addProperty("user_avatar", SharePrefUtil.getString(Conast.AVATAR));
		obj.addProperty("doctor_name", doctor_name);
		obj.addProperty("user_name", SharePrefUtil.getString(Conast.realName));
		obj.addProperty("doctor_avatar", doctor_avatar);
		obj.addProperty("USERID", SharePrefUtil.getString(Conast.MEMBER_ID));
		if (isSystemMsg) {

			obj.addProperty("ISSYSTEMMSG", "1");
		} else {
			obj.addProperty("ISSYSTEMMSG", "0");

		}
		obj.addProperty("CHANNEL", "android");
		obj.addProperty("ORDERID", entry.getData().getORDERID());
		return obj.toString();

	}
	
	private String toStringJSON(String doctor,String user){
		
		JsonObject obj = new JsonObject();
		obj.addProperty("doctor_msg", doctor);
		obj.addProperty("user_msg", user);
		System.out.println("obj.toString() = "+obj.toString());
		return obj.toString();
		
	}
	public void sendImgSMG(String filePath,AddSingle addSingle,String orderType){
		String ext = generateExtMessage(addSingle, false, orderType);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		message.setReceipt(addSingle.getData().getDOCTORID());
		message.setFrom(SharePrefUtil.getString(Conast.MEMBER_ID));
		if (!TextUtils.isEmpty(ext))
			message.setAttribute("ext", ext);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		message.addBody(body);
		EMConversation conversation = EMChatManager.getInstance().getConversation(addSingle.getData().getDOCTORID());
		conversation.addMessage(message);
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				System.out.println("系统发送成功");
			}

			@Override
			public void onError(int code, String error) {
				System.out.println("系统发送失败");
			}

			@Override
			public void onProgress(int progress, String status) {
			}

		});
	}
}
