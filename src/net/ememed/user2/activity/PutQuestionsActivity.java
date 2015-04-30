package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.dialog.CustomAlertDialog;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.QuestionEntry;
import net.ememed.user2.entity.RoomsConfigEn;
import net.ememed.user2.medicalhistory.activity.QuestionXiangQingActivity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.AgeDialog;
import net.ememed.user2.widget.DepartmentDailog;
import net.ememed.user2.widget.MenuDialog;
import net.ememed.user2.widget.MyGridView;
import net.ememed.user2.widget.SexDialog;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

import de.greenrobot.event.util.BitmapPutUtil;
import de.greenrobot.event.util.ImageUploadTool;
import de.greenrobot.event.util.ImageUploadTool.DownloadStateListener;

public class PutQuestionsActivity extends BasicActivity implements DownloadStateListener {

	private MyGridView gridview;
	private MyAdapter mAdapter;
	private TextView hint_tx;
	private TextView department_tx;
	private TextView age_tx;
	private TextView sex_tx;
	private EditText et_content;
	private Handler mHandler;
	private TextView top_title;

	private DepartmentDailog departmentDailog;
	private SexDialog sexDialog;

	private Button btn_addhealth;
	private int mRequestFlag = 0;

	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_put_questions);
		pathUrl = new ArrayList<String>();
		initView();

		requestRoomsList();
	}

	public void initView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("免费提问");
		gridview = (MyGridView) findViewById(R.id.gridview);
		hint_tx = (TextView) findViewById(R.id.hint_tx);
		sex_tx = (TextView) findViewById(R.id.sex_tx);
		et_content = (EditText) findViewById(R.id.symptoms);
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setBackgroundResource(R.color.transparent);
		btn_addhealth.setText("提交");
		btn_addhealth.setVisibility(View.VISIBLE);
		department_tx = (TextView) findViewById(R.id.department_tx);
		age_tx = (TextView) findViewById(R.id.age_tx);
		mAdapter = new MyAdapter(null);
		gridview.setAdapter(mAdapter);
		gridview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (position == (mAdapter.getCount() - 1)) {
					setDoctorImage();
				}
			}
		});
	}

	private void setDoctorImage() {
		final Context dialogContext = new ContextThemeWrapper(this, android.R.style.Theme_Light);
		String[] choices;
		choices = new String[2];
		choices[0] = getString(R.string.change_avatar_take_photo); // 拍照
		choices[1] = getString(R.string.change_avatar_album); // 从相册中选择

		MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
		MenuDialog dialog = builder.setTitle("选择要上传的照片")
				.setItems(choices, new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							String status = Environment.getExternalStorageState();
							if (status.equals(Environment.MEDIA_MOUNTED)) {// 判断是否有SD卡
								basictakePicture();
							}
							break;
						case 1:
							basicopenGallery();
							break;
						}
					}
				}).create();
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

	List<String> pathUrl;

	public void onCameraPath(String path) {
		super.onCameraPath(path);
		mAdapter.addImgPath(path);
		hint_tx.setVisibility(View.GONE);
		pathUrl.add(path);
	}

	long first;
	long last;

	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btn_addhealth:
			first = System.currentTimeMillis();
			isThrough();
			break;
		case R.id.btn_back:
			confirmClose();
			break;

		default:
			break;
		}
	}

	private void confirmClose() {
		if (mAdapter.getCount() == 1 && TextUtils.isEmpty(et_content.getText())) {
			finish();
			return;
		}
		new AlertDialog.Builder(this).setTitle("温馨提示").setMessage("离开将丢失当前编辑的内容，是否离开？")
				.setPositiveButton("确定", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				}).setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				}).show();
	}

	AgeDialog ageDialog;

	public void doClilk(View view) {

		switch (view.getId()) {

		case R.id.department_layout:
			// requestRoomsList();
			showRoomsDialog();
			break;
		case R.id.age_layout:
			ageDialog = new AgeDialog(this, R.style.wheelDialog);
			ageDialog.show();
			ageDialog.setParams(CanvasWidth * 3 / 4);
			ageDialog.setAdapter();
			ageDialog.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					switch (v.getId()) {
					case R.id.ok_bnt:
						age_tx.setText(ageDialog.getAge());
						ageDialog.dismiss();
						break;
					case R.id.Cancel_bnt:
						ageDialog.dismiss();
						break;

					default:
						break;
					}
				}
			});
			break;
		case R.id.sexlayout:
			sexDialog = new SexDialog(this, R.style.wheelDialog);
			sexDialog.show();
			sexDialog.setParams(CanvasWidth * 3 / 4);
			sexDialog.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.Boys_tx:
						sex = "1";
						sexDialog.dismiss();
						sex_tx.setText("男");
						break;
					case R.id.girls_tx:
						sex = "0";
						sexDialog.dismiss();
						sex_tx.setText("女");
						break;
					default:
						break;
					}
				}
			});
			break;

		default:
			break;
		}

	}

	class MyAdapter extends BaseAdapter {
		List<String> list;

		public MyAdapter(List<String> list) {
			this.list = list;
		}

		public int getCount() {
			if (list == null) {
				list = new ArrayList<String>();
				list.add("");
			}
			return list.size();
		}

		public Object getItem(int position) {
			return position;
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {

			convertView = LayoutInflater.from(PutQuestionsActivity.this).inflate(
					R.layout.item_add_img, null);
			int number = dip2px(PutQuestionsActivity.this, 50);
			int number1 = (CanvasWidth - dip2px(PutQuestionsActivity.this, 60)) / 4;
			ImageView imageView = (ImageView) convertView.findViewById(R.id.imageView);
			if (number > number1) {
				LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
						(CanvasWidth - dip2px(PutQuestionsActivity.this, 60)) / 4,
						(CanvasWidth - dip2px(PutQuestionsActivity.this, 60)) / 4);
				imageView.setLayoutParams(params);
			}
			if (position == (list.size() - 1)) {
				imageView.setImageResource(R.drawable.add_img);
			} else {
				imageView.setImageBitmap(BitmapPutUtil.getPathBitmap(list.get(position),
						PutQuestionsActivity.this));
			}
			return convertView;
		}

		public void addImgPath(String path) {
			list.add(list.size() - 1, path);
			notifyDataSetChanged();
		}

	}

	@Override
	public void onFinish(Map<String, String> successURL) {
		String pics = ListTOJson(successURL);
		System.out.println(pics + "  图片上传全部成功   时间：" + (System.currentTimeMillis() - first));
		setQuestion(pics);
	}

	@Override
	public void onFailed() {
	}

	public String ListTOJson(Map<String, String> successURL) {
		StringBuffer sb = new StringBuffer();

		JSONArray array = new JSONArray();
		sb.append("[");
		for (int i = 0; i < successURL.size(); i++) {
			array.put(successURL.get(pathUrl.get(i)));
		}
		sb.append("]");
		return array.toString();
	}

	private String department;
	private String age;
	private String sex;
	private String symptom;
	private String age_unit;

	public void isThrough() {

		department = department_tx.getText().toString().trim();
		if (TextUtils.isEmpty(department)) {
			department = "";
		}
		age = age_tx.getText().toString().trim();
		if (TextUtils.isEmpty(age)) {
			age = "";
			age_unit = "1";
		} else {

			if (age.contains("月")) {
				age = age.replaceAll("月", "");
				age_unit = "2";
			} else {
				age = age.replaceAll("岁", "");
				age_unit = "1";
			}

		}
		sex = sex_tx.getText().toString().trim();
		if (TextUtils.isEmpty(sex)) {
			sex = "0";
		} else {
			if (sex.equals("男")) {
				sex = "1";
			} else {
				sex = "2";
			}
		}
		symptom = et_content.getText().toString().trim();
		if (TextUtils.isEmpty(symptom)) {
			showToast("请简单说明你的症状");
			return;
		} else if (symptom.length() < 10) {
			showToast("不可以低于10个字");
			return;
		}
		loading(null);
		// ImageDownLoader downLoader = new ImageDownLoader(this, pathUrl,
		// this);
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
		params.add(new BasicNameValuePair("dir", "14"));
		ImageUploadTool downLoader = new ImageUploadTool(this, pathUrl, params,
				HttpUtil.set_question_pic, "ext", this);
	}

	public void setQuestion(String pics) {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
		params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("roomname", department);
		params.put("age", age);
		params.put("age_unit", age_unit);
		params.put("sex", sex);
		params.put("symptoms", symptom);
		if (!pics.equals("[]")) {
			params.put("pics", pics);
		}
		params.put("channel", "Android");
		params.put("app_version", PublicUtil.getVersionName(PutQuestionsActivity.this));
		System.out.println("params = " + params);
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_question, QuestionEntry.class,
				params, new Response.Listener() {

					@Override
					public void onResponse(Object arg0) {
						QuestionEntry entry = (QuestionEntry) arg0;
						PutQuestionsActivity.this.destroyDialog();
						if (entry.getSuccess().equals("1")) {
							showToast(entry.getErrormsg());
							Intent xq_intent = new Intent(PutQuestionsActivity.this,
									QuestionXiangQingActivity.class);
							xq_intent.putExtra("question_id", entry.getData().getQuestion_id());
							startActivity(xq_intent);
							finish();
						} else {
							showFreeQuestionInvalidDialog(entry.getErrormsg());
						}

					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						PutQuestionsActivity.this.destroyDialog();
						showToast("未知错误");
					}
				});

	}

	private AlertDialog mFreeQuestionInvalidDialog;
	private TextView tv_tips;
	private void showFreeQuestionInvalidDialog(String tips) {
		if (mFreeQuestionInvalidDialog == null) {
			View dialogView = getLayoutView(R.layout.dialog_free_question_invalid);
			tv_tips = (TextView) dialogView.findViewById(R.id.tv_tips);
			View btn_last = dialogView.findViewById(R.id.btn_last);
			View btn_see = dialogView.findViewById(R.id.btn_see);
			btn_last.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mFreeQuestionInvalidDialog.hide();
				}
			});
			btn_see.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(PutQuestionsActivity.this,
							ExpertSearchActivity.class);
					intent.putExtra("current_service", "1");
					intent.putExtra("current_name", "图文咨询");
					startActivity(intent);
					finish();
				}
			});
			mFreeQuestionInvalidDialog = new AlertDialog.Builder(this).setView(dialogView).create();
		}
		tv_tips.setText(tips);
		mFreeQuestionInvalidDialog.show();
	}

	public void requestRoomsList() {
		this.loading(null);
		if (NetWorkUtils.detect(this)) {

			HashMap<String, String> params = new HashMap<String, String>();
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.sync_rooms_config,
					RoomsConfigEn.class, params, new Listener() {

						public void onResponse(Object arg0) {
							PutQuestionsActivity.this.destroyDialog();
							RoomsConfigEn rce = (RoomsConfigEn) arg0;
							if (arg0 != null) {
								setListViewData(rce);
							} else {
								showToast("获取数据失败");
							}
						}
					}, new ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							PutQuestionsActivity.this.destroyDialog();
							PutQuestionsActivity.this.showToast(error.getMessage());
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}

					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}

	}

	public void setListViewData(RoomsConfigEn configEn) {
		Map<String, List<String>> map = configEn.getData().getRooms();
		Set<String> set = map.keySet();
		final List<String> data = new ArrayList<String>();
		for (String string : set) {
			data.add(string);
		}
		departmentDailog = new DepartmentDailog(this, R.style.wheelDialog);
		departmentDailog.addData(data);
		departmentDailog.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				department_tx.setText(data.get(position));
				departmentDailog.dismiss();
			}
		});
		if (mRequestFlag != 0) {
			departmentDailog.show();
		}
	}

	private void showRoomsDialog() {
		if (departmentDailog != null) {
			departmentDailog.show();
		} else {
			requestRoomsList();
		}
	}

}
