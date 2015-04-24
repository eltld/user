package net.ememed.user2.medicalhistory.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Handler.Callback;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.medicalhistory.adapter.AdapterMain;
import net.ememed.user2.medicalhistory.getImage.ObtainPicturesCall;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.FileUtil;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.widget.MenuDialog;

public class MedicalHistoryAddDetailActivity extends BasicActivity implements Callback {
	private static final int UPLOAD_BINGLI_IMAGE = 0x12;
	private BasicActivity activity;
	private TextView top_title;
	private MyGridView blGrid;
	private EditText contentEdit;
	private BaseAdapter blAdapter;
	private ArrayList<String> blDatas = new ArrayList<String>();
	private ArrayList<Drawable> blDrawable = new ArrayList<Drawable>(); //
	// private ArrayList<String> imagePaths = new ArrayList<String>();
	private Drawable drawableImage;
	private ObtainPicturesCall mPicturesCall = new ObtainPicturesCall(this);
	private Button btn_addhealth;
	private String bingliStr;
	// 图片的路径
	private String uploadFile = "";
	// 图片的名字
	private String newName = "";
	private StringBuffer imageAdd1 = new StringBuffer();
	private List<String> imageUrls = new ArrayList<String>();
	private List<String> ids = new ArrayList<String>();
	private Handler handler = null;
	private String content;

	private String blDatas1 = "";
	private String blDatas2 = "";
	private String blDatas3 = "";
	private String ID = null;
	private String path;
	private MenuDialog alertDialog;
	private int fuid;
	private ArrayList<String> initDatas = new ArrayList<String>();
	private String groupkey;
	private String fujian_ids;
	private String point = "";

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.medical_history_add_detail);
		activity = this;
		handler = new Handler(this);
		UUID uuid = UUID.randomUUID();
		groupkey = uuid.toString();
		initView();
		initData();

	}

	@SuppressLint("NewApi")
	private void initData() {
		Bundle data = getIntent().getExtras();
		int start = data.getInt("start", 0);
		if (start == 0) {
			return;
		} else if (start == 1) {
			loading(null);
			String testData = data.getString("testData");
			content = testData;
			initDatas.clear();
			ids.clear();
			if (data.getString("userid", "").equals("")) {

			} else {
				ID = data.getString("userid");
			}
			if (data.getString("point", "").equals("")) {
				point = "";
			} else {
				point = data.getString("point");
			}
			Log.e("IDID", "ID:" + ID);
			if (ID == null || ID.equals("")) {
				if (data.getString("ids", "").equals("")) {

				} else {
					String fuid = data.getString("ids") + ",";
					String[] fuids = fuid.split(",");
					for (int i = 0; i < fuids.length; i++) {
						ids.add(fuids[i]);
					}
				}
			} else {
				if (data.getString("ids", "").equals("")) {

				} else {
					String fuid = data.getString("ids") + ",";
					String[] fuids = fuid.split(",");
					for (int i = 0; i < fuids.length; i++) {
						ids.add(fuids[i]);
					}
				}
			}

			initDatas = data.getStringArrayList("drawable");
			if (initDatas.size() != 0) {
				for (int i = 0; i < initDatas.size(); i++) {
					if (i == 0) {
						if (initDatas.get(i) != null && !initDatas.get(i).equals("")) {
							blDatas1 = initDatas.get(i);
						}
					} else if (i == 1) {
						if (initDatas.get(i) != null && !initDatas.get(i).equals("")) {
							blDatas2 = initDatas.get(i);
						}
					} else if (i == 2) {
						if (initDatas.get(i) != null && !initDatas.get(i).equals("")) {
							blDatas3 = initDatas.get(i);
						}
					}
				}
			}
			contentEdit.setText(content);
			if (data.getString("groups", "").equals("")) {

			} else {
				groupkey = data.getString("groups");
			}
			Log.e("", "groupkey:" + groupkey);
			new Thread(new Runnable() {

				@Override
				public void run() {
					for (int i = 0; i < initDatas.size(); i++) {
						Log.e("urlPath:", "urlPath:" + initDatas.get(i));
						if (initDatas.get(i) != null && !initDatas.get(i).equals("")) {
							Bitmap bitmap = returnBitMap(initDatas.get(i));
							Drawable drawable1 = new BitmapDrawable(bitmap);
							blDrawable.add(drawable1);
						}
					}
					handler2.sendEmptyMessage(0x11);
				}
			}).start();

		} else if (start == 2) {
			if (data.getString("userid", "").equals("")) {

			} else {
				ID = data.getString("userid");
			}
		}
	}

	private Handler handler2 = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x11) {
				activity.destroyDialog();
				blDrawable.remove(drawableImage);
				blDrawable.add(drawableImage);
				blAdapter.notifyDataSetChanged();
			}
		}

	};

	public Bitmap returnBitMap(String path) {
		try {
			URL url = new URL(path);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			conn.setRequestMethod("GET");
			if (conn.getResponseCode() == 200) {
				InputStream inputStream = conn.getInputStream();
				Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
				return bitmap;
			}
		} catch (IOException e) {

			e.printStackTrace();
		}
		return null;

	}

	private void initView() {
		blGrid = (MyGridView) findViewById(R.id.bingli_add_grid1);
		contentEdit = (EditText) findViewById(R.id.bingli_add_text);
		blAdapter = new AdapterMain(this, blDrawable);
		blGrid.setAdapter(blAdapter);

		blGrid.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int point, long id) {
				if (blDrawable.size() == 4) {
					activity.showToast("一次最多只能添加三张图片");
					return;
				}
				if (point == (blDrawable.size() - 1)) {
					mPicturesCall.showAlertDialog(0);
				}
			}
		});
		blGrid.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int point, long id) {
				if (point == blDrawable.size()) {
					return false;
				} else {
					final Context dialogContext = new ContextThemeWrapper(MedicalHistoryAddDetailActivity.this, android.R.style.Theme_Light);
					String[] choices = new String[2];
					choices[0] = "删除";
					choices[1] = "取消";

					MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
					alertDialog = builder.setTitle("删除图片").setItems(choices, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:
								canelCase(point);
								break;
							case 1:

								break;
							}
						}
					}).create();
					alertDialog.setCanceledOnTouchOutside(true);
					alertDialog.show();
				}

				return false;
			}
		});
		drawableImage = getResources().getDrawable(R.drawable.medical_history_add_picture);
		blDrawable.add(drawableImage);
	}

	private void canelCase(int position) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("id", ids.get(position));
			fuid = position;
			Log.e("id:", params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.cancel_fujian, SetProfile.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = 0x28;
					handler.sendMessage(message);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Message message = new Message();
					message.obj = error.getMessage();
					message.what = 0x29;
					handler.sendMessage(message);
				}
			});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("添加诊疗详细信息");
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("保存");
		super.setupView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 168) {
			if (intent == null) {
				return;
			}
			ContentResolver resolver = getContentResolver();
			// 照片的原始资源地址
			Uri uri = intent.getData();
			String[] proj = { MediaColumns.DATA };
			Cursor cursor = managedQuery(uri, proj, null, null, null);
			int column_index = cursor.getColumnIndexOrThrow(MediaColumns.DATA);
			cursor.moveToFirst();

			path = cursor.getString(column_index);
			Log.e("paths:", "path:" + path);
			// imagePaths.add(path);
			// imagePaths = datas.getStringArrayList("imagePaths");
			getRePicture();
		} else if (requestCode == ObtainPicturesCall.SELECT_CAMER && resultCode == RESULT_OK) {
			// if(mPicturesCall.mFilePictures.getAbsolutePath().equals("") || mPicturesCall.mFilePictures.getAbsolutePath() == null){
			// return;
			// }
			// imagePaths.clear();
			// imagePaths.add(mPicturesCall.mFilePictures.getAbsolutePath());
			path = mPicturesCall.mFilePictures.getAbsolutePath();
			Log.e("paths:", "path:" + path);
			getRePicture();
		}
	}

	private void getCamera() {

	}

	// 压缩后的图片
	File smallFile;

	private void getRePicture() {
		bingliStr = path;
		Log.d("XX", bingliStr);
		save(path);
		uploadFile = smallFile.getPath();
		blDatas.add(uploadFile);
		blDrawable.add(BitmapDrawable.createFromPath(uploadFile));
		byte[] imgDatas = FileUtil.getBytes(smallFile);
		UICore.eventTask(this, activity, UPLOAD_BINGLI_IMAGE, "图片上传", imgDatas);// 加载数据入口

		blDrawable.remove(drawableImage);
		blDrawable.add(drawableImage);
		blAdapter.notifyDataSetChanged();
	}

	@Override
	public void execute(int mes, Object obj) {
		if (mes == UPLOAD_BINGLI_IMAGE) {
			Log.d("XX", "11111111111111111111111111111");
			setBingli((byte[]) obj);
		}
	}

	private void setBingli(final byte[] imageData) {
		try {
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
			params.add(new BasicNameValuePair("ext", "jpg"));
			params.add(new BasicNameValuePair("type", "1"));
			params.add(new BasicNameValuePair("dir", "6"));
			String json = HttpUtil.uploadFile(HttpUtil.URI + HttpUtil.post_bingli_image, params, imageData);
			json = TextUtil.substring(json, "{");
			// System.out.println("json=" + json);
			HashMap<String, Object> map = new HashMap<String, Object>();
			JSONObject obj = new JSONObject(json);
			map.put("success", obj.getInt("success"));
			map.put("errormsg", obj.getString("errormsg"));
			JSONObject data_obj = obj.getJSONObject("data");
			if (null != data_obj) {
				map.put("ATTACHMENT_ID", data_obj.getString("ATTACHMENT_ID"));
				map.put("ATTACHMENT", data_obj.getString("ATTACHMENT"));
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
	public boolean handleMessage(Message msg) {
		destroyDialog();
		try {
			switch (msg.what) {
			case IResult.UPLOAD_IMG:
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				int success = (Integer) map.get("success");
				if (success == 1) {
					activity.showToast("图片上传成功");
					ids.add((String) map.get("ATTACHMENT_ID"));
					// imageAdd1.append(map.get("ATTACHMENT_ID"));
					// imageAdd1.append(",");
					Log.e("url", "url:" + map.get("ATTACHMENT"));
					if (!TextUtil.notEmpty(blDatas1)) {
						blDatas1 = (String) map.get("ATTACHMENT");
						Log.e("blDatas1", "blDatas1:" + map.get("ATTACHMENT"));
					} else if (!TextUtil.notEmpty(blDatas2)) {
						blDatas2 = (String) map.get("ATTACHMENT");
					} else if (!TextUtil.notEmpty(blDatas3)) {
						blDatas3 = (String) map.get("ATTACHMENT");
					}

				} else {
					activity.showToast("图片上传失败");
				}
				break;
			case IResult.BASE_PROFILE:
				SetProfile sp = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp.getSuccess() + "");
				Log.e("YY", "www" + sp.getErrormsg() + "");
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						showToast("保存成功");
						fujian_ids = sp.getData();
						Log.e("XX", "附件XX:" + fujian_ids);
						Intent data = getIntent();
						data.putExtra("ID", ID);
						data.putExtra("content", content);
						data.putExtra("blDatas1", blDatas1);
						data.putExtra("blDatas2", blDatas2);
						data.putExtra("blDatas3", blDatas3);
						data.putExtra("point", point);
						if (imageAdd1.toString().length() != 0) {
							data.putExtra("ATTACHMENT_ID", imageAdd1.toString().substring(0, imageAdd1.toString().length() - 1));
							Log.e("XX", "附件YY:" + fujian_ids);
						} else {
							data.putExtra("ATTACHMENT_ID", fujian_ids);
						}
						data.putExtra("groupkey", groupkey);
						// data.putExtra("ATTACHMENT_ID", fujian_ids);
						// Log.e("XX", "附件ids:" + fujian_ids);
						setResult(RESULT_OK, data);
						finish();
					}
				}
				break;
			case 0x28:
				SetProfile sp1 = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp1.getSuccess() + "");
				Log.e("YY", "www" + sp1.getErrormsg() + "");
				if (null != sp1) {
					if (sp1.getSuccess() == 1) {
						showToast("删除成功");
						Log.e("ids:", "ids:" + ids.size());
						ids.remove(fuid);
						if (fuid == 0) {
							blDatas1 = "";
						} else if (fuid == 1) {
							blDatas2 = "";
						} else if (fuid == 2) {
							blDatas3 = "";
						}
						Log.e("ids:", "ids:" + ids.size());
						blDrawable.remove(fuid);
						blDrawable.remove(drawableImage);
						blDrawable.add(drawableImage);
						blAdapter.notifyDataSetChanged();
					} else {
						showToast("删除失败");
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

	// 压缩图片
	private void save(String mCurrentPhotoPath) {

		if (mCurrentPhotoPath != null) {

			try {
				File f = new File(mCurrentPhotoPath);

				Bitmap bm = getSmallBitmap(mCurrentPhotoPath);
				smallFile = new File(Environment.getExternalStorageDirectory() + "/yimiyisheng/temp/", "small_" + f.getName());
				FileOutputStream fos = new FileOutputStream(smallFile);

				bm.compress(Bitmap.CompressFormat.JPEG, 30, fos);

			} catch (Exception e) {
				Log.e("BingLiActivity", "error", e);
			}

		} else {
			Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
		}
	}

	/**
	 * 根据路径获得突破并压缩返回bitmap用于显示
	 * 
	 * @param imagesrc
	 * @return
	 */
	public static Bitmap getSmallBitmap(String filePath) {
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, 480, 800);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		return BitmapFactory.decodeFile(filePath, options);
	}

	/**
	 * 计算图片的缩放值
	 * 
	 * @param options
	 * @param reqWidth
	 * @param reqHeight
	 * @return
	 */
	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and
			// width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will
			// guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_addhealth) {
			if (content == null || content.equals("")) {
				content = contentEdit.getText().toString().trim();
			}
//			if (content == null || content.equals("")) {
//				showToast("请输入详情");
//				return;
//			}
			content = contentEdit.getText().toString().trim();
			postData();
		}
	}

	private void postData() {
		if (NetWorkUtils.detect(this)) {
			Log.e("YY", "bbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbbb");
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("description", content);
			params.put("groupkey", groupkey);
			Log.e("", "上传groupkey:" + groupkey);
			if (ids.size() != 0) {
				for (int i = 0; i < ids.size(); i++) {
					imageAdd1.append(ids.get(i));
					imageAdd1.append(",");
				}
			}
			if (imageAdd1.toString().length() != 0) {
				Log.e("XX", "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				params.put("attachment_ids", imageAdd1.toString().substring(0, imageAdd1.toString().length() - 1));
			} else {
				Log.e("XX", "附件为空");
				// params.put("attachment_ids", "");
			}
			Log.e("params", params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.add_bingli_att, SetProfile.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Log.e("YY", "1111111111111111111111111111111111111111111");
					Message message = new Message();
					message.obj = response;
					message.what = IResult.BASE_PROFILE;
					handler.sendMessage(message);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Log.e("YY", "dddddddddddddddddddddddddddddddddd");
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

}
