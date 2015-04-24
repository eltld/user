package net.ememed.user2.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddSingle;
import net.ememed.user2.entity.IResult;
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

public class BuyJiuyiActivity extends BasicActivity {
	private static final int REQUEST_CODE_TAKE_PICTURE = 1;
	private static final int REQUEST_CODE_GALLERY = 2;
	private static final int REQUEST_CODE_CROP_IMAGE = 3;
	private static final int EXCU_UPLOAD_IMG = 4;
	private TextView top_title;
	private File picFile;
	private Uri photoUri;
	// private DoctorEntry entry;
	private String doctor_name;
	private String doctorid;

	private ImageView iv_consult;
	private String upload_file = "";
	private String doctor_avatar;
	private Bitmap bm;
	private ImageView tv_upload_pic;
	private TextView tv_consult_title;
	private Button btn_pay_money;
	private Button btn_upload_pic;
	private EditText et_disease_description;
	
	private String describe;
	
	private String path;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_buy_shangmen);
		// entry = (DoctorEntry) getIntent().getSerializableExtra("entry");
		doctor_name = getIntent().getStringExtra("doctor_name");
		doctorid = getIntent().getStringExtra("doctorid");
		doctor_avatar = getIntent().getStringExtra("doctor_avatar");
	}

	@Override
	protected void setupView() {
		super.setupView();
		btn_upload_pic = (Button) findViewById(R.id.btn_upload_pic);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("购买" + doctor_name + "预约加号");
		tv_consult_title = (TextView) findViewById(R.id.tv_consult_title);
		tv_consult_title.setText(getString(R.string.jiuyi_service_hint));
		iv_consult = (ImageView) findViewById(R.id.iv_consult);
		iv_consult.setImageResource(R.drawable.clinic_jiahao);
		tv_upload_pic = (ImageView)findViewById(R.id.tv_upload_pic);
		btn_pay_money = (Button) findViewById(R.id.btn_yuyue);
		et_disease_description = (EditText) findViewById(R.id.et_disease_description);
//		et_disease_description.setHint("建议您先在输入框内输入您要咨询的详细内容、或者上传能帮助医生准确、快速评判情况的病历、影像图片等资料。");
		}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_yuyue) {
		
			if(TextUtils.isEmpty(et_disease_description.getText().toString().trim())){
				showToast(getString(R.string.condition_details));
				return;
			}
			describe = et_disease_description.getText().toString().trim();
			btn_pay_money.setClickable(false);
			UICore.eventTask(BuyJiuyiActivity.this, BuyJiuyiActivity.this, IResult.BUY_SERVICE, "订单提交中..", et_disease_description.getText().toString().trim());
		} else if (view.getId() == R.id.btn_upload_pic) {
			changeAvatar();
		} else if (view.getId() == R.id.tv_upload_pic){
			Intent intent = new Intent(this,ShowBigImage.class);
			intent.putExtra("uri",photoUri);
			startActivity(intent);
		}
	}
	/**
	 * 添加服务订单
	 */
	private void addSingleOrder(String content) {
		
		if (NetWorkUtils.detect(this)) {
			
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("token",SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
			params.add(new BasicNameValuePair("channel","android"));
			params.add(new BasicNameValuePair("memberid", SharePrefUtil.getString(Conast.MEMBER_ID)));
			params.add(new BasicNameValuePair("doctorid", doctorid));
			params.add(new BasicNameValuePair("free", ""));
			params.add(new BasicNameValuePair("ordertype", "3"));
			params.add(new BasicNameValuePair("price", ""));
			
			params.add(new BasicNameValuePair("money_null", "1"));
			params.add(new BasicNameValuePair("content", content));
			params.add(new BasicNameValuePair("upload_file", upload_file));
			params.add(new BasicNameValuePair("appversion", PublicUtil.getVersionName(BuyJiuyiActivity.this)));
			
			String result;
			try {
				result = HttpUtil.getString(HttpUtil.URI + HttpUtil.add_single_order,params, HttpUtil.POST);
				result = TextUtil.substring(result, "{");
				Gson gson = new Gson();
				AddSingle reason = gson.fromJson(result, AddSingle.class);
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
		
	}

	private void setSeekhelpPic(byte[] imageData) {
		try {
//			System.out.println("imageData===" + imageData);
			// activity.loading(null);
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("ext", "jpg"));
			params.add(new BasicNameValuePair("dir", "9"));
			String json = HttpUtil.uploadFile(HttpUtil.URI
					+ HttpUtil.set_seekhelp_pic, params, imageData);
			json = TextUtil.substring(json, "{");
//			System.out.println("json=" + json);
			HashMap<String, Object> map = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(json);
			map.put("success", obj.getInt("success"));
			map.put("errormsg", obj.getString("errormsg"));
			JSONObject data_obj =  obj.optJSONObject("data");
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

	private void changeAvatar() {

		if (NetWorkUtils.detect(this)) {
			final Context dialogContext = new ContextThemeWrapper(this,
					android.R.style.Theme_Light);
			String[] choices = new String[2];
			choices[0] = getString(R.string.change_avatar_take_photo);
			choices[1] = getString(R.string.change_avatar_album);

			MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
			MenuDialog dialog = builder.setTitle(R.string.shangmen_upload_pic)
					.setItems(choices, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:
								String status = Environment
										.getExternalStorageState();
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
				picFile = new File(uploadFileDir, UUID.randomUUID().toString()
						+ ".jpeg");
			} else {
				picFile = new File(this.getFilesDir() + FileUtil.ROOT_DIRECTORY
						+ "/" + FileUtil.IMG_UPLOAD, UUID.randomUUID()
						.toString() + ".jpeg");
			}

			photoUri = Uri.fromFile(picFile);
			intent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, photoUri);
			intent.putExtra("return-data", true);
			startActivityForResult(intent, REQUEST_CODE_TAKE_PICTURE);
		} catch (ActivityNotFoundException e) {
//			Log.d(TAG, "cannot take picture", e);
		}
	}

	/** for4.3 */
	private void openGallery() {
		try {
			File pictureFileDir = new File(FileUtil.PATH, FileUtil.IMG_UPLOAD);
			if (!pictureFileDir.exists()) {
				pictureFileDir.mkdirs();
			}
			picFile = new File(pictureFileDir, UUID.randomUUID().toString()
					+ ".jpeg");
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
						UICore.eventTask(this, this, EXCU_UPLOAD_IMG, "图片上传中...",
								imgDatas);// 加载数据入口
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
				InputStream inputStream = this.getContentResolver()
						.openInputStream(data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(
						picFile);
				BitmapUtil.copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
				startCropImage();
			} catch (Exception e) {
//				Log.e(TAG, "Error while creating temp file", e);
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
			String content = (String)obj;
			addSingleOrder(content);
		}
	}

	@Override
	protected void onResult(Message msg) {
		try {
			btn_pay_money.setClickable(true);
			switch (msg.what) {
			case IResult.SEEKHELP_PIC:
				destroyDialog();
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				int success = (Integer) map.get("success");
				if (success == 1) {
					tv_upload_pic.setImageBitmap(bm);
					upload_file  = (String) map.get("SEEKHELP_PIC");
					btn_upload_pic.setText("重新上传");
					showToast("图片上传成功");
				} else {
					showToast((String) map.get("errormsg"));
				}
				break;
			case IResult.ADD_SINGLE_ORDER:
				AddSingle addSingle = (AddSingle) msg.obj;
				if (null != addSingle) {
					if (addSingle.getSuccess() == 1) {
						Intent intent = new Intent(this,
								DoctorDetailInfoActivity.class);
						intent.putExtra("doctor_name", doctor_name);// 医生姓名
						intent.putExtra("tochat_userId", addSingle.getData().getDOCTORID());
						intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
						intent.putExtra("orderid", addSingle.getData().getORDERID());// 订单id
						intent.putExtra("serviceid", addSingle.getData().getSERVICEID());// 服务id
						intent.putExtra("from_activity", BuyJiuyiActivity.class.getSimpleName());// 来源页
						startActivity(intent);
						Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
						sendBroadcast(receive);
						finish();
						
						String doctor = "新服务请求：预约加号，是指用户发起的针对疾病康复服务的其他服务请求。请您在看到这条订单请求之后，积极与用户沟通服务的细节，并设置合理价格供用户购买。" +
								"用户支付成功后，协助用户具体落实该项服务是您应尽的责任和义务！";
						String user = "感谢您向"+doctor_name+"医生咨询预约加号服务，鉴于该该服务为线下服务，具体服务细节请与详细沟通。再由医生为服务定价。";
						
						String systemMSG = toStringJSON(doctor, user);
						
						sendEMMessage(systemMSG, addSingle, true, "3");
						sendEMMessage(describe, addSingle, false, "3");
						if(path!=null){
							sendImgSMG(path, addSingle, "3");
						}
					} else {
						Intent intent = new Intent(this,DoctorDetailInfoActivity.class);
						intent.putExtra("doctor_name", doctor_name);// 医生姓名
						intent.putExtra("tochat_userId", doctorid);
						intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
						startActivity(intent);
						finish();
						showToast(addSingle.getErrormsg());
					}
				}
				break;
			case IResult.NET_ERROR:
				showToast(getString(R.string.net_error));
				break;
			case IResult.DATA_ERROR:
				showToast(getString(R.string.data_error));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.add_single_order);
	}
	
	
	public void sendEMMessage(String textMSG , AddSingle addSingle,boolean isSystemMsg,String orderType) {
		System.out.println("系统消息发送");
		String ext = generateExtMessage(addSingle, isSystemMsg, orderType);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		TextMessageBody txtBody = new TextMessageBody(textMSG);
		// 设置消息body
		message.addBody(txtBody);
		if (!TextUtils.isEmpty(ext))
			message.setAttribute("ext", ext);
		
		// 设置要发给谁,用户username或者群聊groupid
		message.setReceipt(addSingle.getData().getDOCTORID());
		// 把messgage加到conversation中
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

	/***
	 * 附加到聊天消息中的备用字段
	 * 
	 * @return
	 */
	public String generateExtMessage(AddSingle addSingle, boolean isSystemMsg,String orderType) {
		JsonObject obj = new JsonObject();
		// "ISSYSTEMMSG": "0", //是否系统消息（1是，0为否）
		// "CHANNEL": "android", //来源（android|ios）
		// "ORDERID": "123", //订单ID
		// "SERVICEID": "1", //服务ID
		// "PACKET_BUY_ID": "1", //套餐购买ID
		// "USERID": "6998", //用户ID
		// "DOCTORID": "5226", //医生ID
		// "ORDERTYPE": "1" //订单类型
		obj.addProperty("SERVICEID", addSingle.getData().getSERVICEID());
		obj.addProperty("DOCTORID", addSingle.getData().getDOCTORID());
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
		obj.addProperty("ORDERID", addSingle.getData().getORDERID());
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
