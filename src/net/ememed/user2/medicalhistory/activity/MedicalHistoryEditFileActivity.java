package net.ememed.user2.medicalhistory.activity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.medicalhistory.activity.selecttime.JudgeDate;
import net.ememed.user2.medicalhistory.activity.selecttime.ScreenInfo;
import net.ememed.user2.medicalhistory.activity.selecttime.WheelMain;
import net.ememed.user2.medicalhistory.adapter.ListViewAdapter;
import net.ememed.user2.medicalhistory.adapter.ListViewForScrollView;
import net.ememed.user2.medicalhistory.bean.ImageUrlBean;
import net.ememed.user2.medicalhistory.bean.MedicalHistory;
import net.ememed.user2.medicalhistory.bean.MedicalHistoryBean;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;

public class MedicalHistoryEditFileActivity extends BasicActivity {
	private static final int EDIT_CONTENT = 0x23;
	private int EDIT_CONTENT_ITEM = 0x24;
	private TextView top_title;
	private EditText timeEdit;
	private EditText hospitalEdit;
	private EditText keshiEdit;
	private EditText contentEdit;
	private WheelMain wheelMain;
	private Button btn_addhealth;
	DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	private ListViewForScrollView mListView;
	private ListViewAdapter listAdapter;
	private ArrayList<String> testData = new ArrayList<String>();
	private ArrayList<String> pictures1 = new ArrayList<String>();
	private ArrayList<String> pictures2 = new ArrayList<String>();
	private ArrayList<String> pictures3 = new ArrayList<String>();
	private ArrayList<String> times = new ArrayList<String>();
	private String ATTACHMENT_ID = null;
	private StringBuffer imageAdd1 = new StringBuffer();
	private StringBuffer imageAdd2 = new StringBuffer();
	private StringBuffer imageAdd3 = new StringBuffer();
	private String ID = null;
	private List<String> xiangqin = new ArrayList<String>();
	private ArrayList<String> images = new ArrayList<String>();
	private ArrayList<String> blDrawable = new ArrayList<String>();
	ArrayList<String> ids1 = new ArrayList<String>();
	ArrayList<String> ids2 = new ArrayList<String>();
	ArrayList<String> ids3 = new ArrayList<String>();
	// private StringBuffer groupkeys = new StringBuffer();
	private ArrayList<String> groupkeys = new ArrayList<String>();
	private int row = 0;
	private String page;
	private List<List<ImageUrlBean>> grpList = new ArrayList<List<ImageUrlBean>>();
	private String groups;
	private ArrayList<ArrayList<String>> imageLists = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> idLists = new ArrayList<ArrayList<String>>();
	private ArrayList<ArrayList<String>> idList = new ArrayList<ArrayList<String>>();

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.history_medical_edit);
		initView();
		initData();
	}

	private void initView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("新建病历夹");
		timeEdit = (EditText) findViewById(R.id.user_edit_time);
		hospitalEdit = (EditText) findViewById(R.id.user_edit_self_hospital);
		keshiEdit = (EditText) findViewById(R.id.user_edit_self_keshi);
		contentEdit = (EditText) findViewById(R.id.user_edit_zhengzhuang);
		mListView = (ListViewForScrollView) findViewById(R.id.add_bingli);
		listAdapter = new ListViewAdapter(null, null, null, MedicalHistoryEditFileActivity.this);
		mListView.setAdapter(listAdapter);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int point, long id) {
				Bundle data = new Bundle();
				data.putString("testData", testData.get(point));
				if (grpList.size() != 0) {
					EDIT_CONTENT_ITEM = 0x25;
					List<ImageUrlBean> grp = grpList.get(point);
					ids1.clear();
					blDrawable.clear();
					for (int i = 0; i < grp.size(); i++) {						
						groups = grp.get(i).getGROUPKEY();
						ids1.add(grp.get(i).getATTACHMENT_ID());
						blDrawable.add(grp.get(i).getATTACHMENT());
					}
					if (ids1.size() != 0) {
						for (int j = 0; j < ids1.size(); j++) {
							imageAdd3.append(ids1.get(j));
							imageAdd3.append(",");
						}
						if (imageAdd3.toString().length() != 0) {
							data.putString("ids", imageAdd3.toString().substring(0, imageAdd3.toString().length() - 1));
							data.putStringArrayList("drawable", blDrawable);
							data.putString("groups", groups);
						}
					}
				} else {
					data.putString("ids", images.get(point));
//					blDrawable.clear();
//					blDrawable.add(pictures1.get(point));
//					blDrawable.add(pictures2.get(point));
//					blDrawable.add(pictures3.get(point));
					data.putStringArrayList("drawable", imageLists.get(point));
					data.putString("groups", groupkeys.get(point));
				}				
				data.putInt("start", 1);
				data.putString("point", point + "");
				if (ID != null) {
					data.putString("userid", ID);
				}
				Utils.startActivityForResult(MedicalHistoryEditFileActivity.this, MedicalHistoryAddDetail.class, data, EDIT_CONTENT_ITEM);
			}
		});
	}

	@Override
	protected void setupView() {
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("保存");
		super.setupView();
	}

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		try {
			switch (msg.what) {
			case IResult.BASE_PROFILE:
				SetProfile sp = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp.getSuccess() + "");
				Log.e("YY", "www" + sp.getErrormsg() + "");
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						showToast("保存成功");
						Utils.startActivity(MedicalHistoryEditFileActivity.this, MedicalHistoryMainActivity.class);
//						setResult(RESULT_OK);
						finish();
					} else {
						showToast("保存失败");
					}
				}
				break;
			case IResult.USER_INFO:
				final MedicalHistoryBean response = (MedicalHistoryBean) msg.obj;
				Log.e("YY", "www" + response.getSuccess() + "");
				Log.e("YY", "www" + response.getErrormsg() + "");
				Log.e("YY", "www" + response.getCount() + "");
				if (null != response) {
					if (response.getSuccess() == 1) {

						if (response.getData() == null || response.getData().size() == 0) {
						} else {
							List<MedicalHistory> data = response.getData();
							List<ImageUrlBean> datas = data.get(0).getATTACHMENTS();
							sort(datas);
						}
					}

				}

				// if (null != sp) {
				// if (sp.getSuccess() == 1) {
				// showToast("保存成功");
				// }
				// }
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	// 分组
	private void sort(List<ImageUrlBean> datas) {
		List<ImageUrlBean> fileList = datas;
		// fileList.add(); 添加对象
//		grpList = new ArrayList<List<ImageUrlBean>>();
		if (fileList.size() == 0) {
			return;
		}

		// 创建一个组
		List<ImageUrlBean> grp = new ArrayList<ImageUrlBean>();
		grp.add(fileList.get(0));
		String groupKey = fileList.get(0).getGROUPKEY();
		grpList.add(grp);

		// 遍历
		for (int i = 1; i < fileList.size(); i++) {
			if (!fileList.get(i).getGROUPKEY().equals(groupKey)) {
				grp = new ArrayList<ImageUrlBean>(); // 创建新的组
				grpList.add(grp); // 结束一个组
				groupKey = fileList.get(i).getGROUPKEY();
				grp.add(fileList.get(i));
			} else {
				grp.add(fileList.get(i));
			}
		}
		getIds(grpList);
	}

	private void getIds(List<List<ImageUrlBean>> grpList) {
		groupkeys.clear();
		for (int i = 0; i < grpList.size(); i++) {
			List<ImageUrlBean> grp = grpList.get(i);
			groupkeys.add(grp.get(0).getGROUPKEY());
			StringBuffer sb = null;
			ArrayList<String> paths = new ArrayList<String>();
			for (int j = 0; j < grp.size(); j++) {
				sb = new StringBuffer();
				sb.append(grp.get(j).getATTACHMENT_ID());
				sb.append(",");
				if (grp.get(j).getATTACHMENT() != null && !grp.get(j).getATTACHMENT().equals("")) {
					paths.add(grp.get(j).getATTACHMENT());
				}
				
			}
			imageLists.add(paths);
			if (sb != null && sb.toString().length() != 0) {
				String str = sb.toString().substring(0, sb.toString().length() - 1);
				images.add(str);
				idLists.add(images);
			}
		}
		listAdapter.change(imageLists, testData, times, 2);
		listAdapter.notifyDataSetChanged();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.rl_zhenzhuang_add) {
			
			Bundle bundle = new Bundle();
			bundle.putString("userid", ID);
			if (ID != null) {
				EDIT_CONTENT_ITEM = 0x26;
				bundle.putInt("start", 2);
				Utils.startActivityForResult(this, MedicalHistoryAddDetail.class, bundle, EDIT_CONTENT_ITEM);
			} else {
				Utils.startActivityForResult(this, MedicalHistoryAddDetail.class, bundle, EDIT_CONTENT);
			}
			
		} else if (view.getId() == R.id.user_edit_time) {
			LayoutInflater inflater = LayoutInflater.from(MedicalHistoryEditFileActivity.this);
			final View timepickerview = inflater.inflate(R.layout.timepicker, null);
			ScreenInfo screenInfo = new ScreenInfo(MedicalHistoryEditFileActivity.this);
			wheelMain = new WheelMain(timepickerview);
			wheelMain.screenheight = screenInfo.getHeight();
			String time = timeEdit.getText().toString();
			Calendar calendar = Calendar.getInstance();
			if (JudgeDate.isDate(time, "yyyy-MM-dd")) {
				try {
					calendar.setTime(dateFormat.parse(time));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			String time2 = timeEdit.getText().toString().trim();
			Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
			Date mydate = new Date(); // 获取当前日期Date对象
			mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
			int year = mycalendar.get(Calendar.YEAR);
			int month = mycalendar.get(Calendar.MONTH) + 1;
			int day = mycalendar.get(Calendar.DAY_OF_MONTH);
			if (!TextUtils.isEmpty(time2)) {
				String[] arrs = time2.split("-");
				year = Integer.valueOf(arrs[0]);
				month = Integer.valueOf(arrs[1]);
				day = Integer.valueOf(arrs[2]);
			}
			DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {

				@Override
				public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
					timeEdit.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
				}
			}, year, month - 1, day);
			dialog.show();
//			wheelMain.initDateTimePicker(year, month, day);
//			new AlertDialog.Builder(MedicalHistoryEditFileActivity.this).setTitle("选择时间").setView(timepickerview).setPositiveButton("确定", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					timeEdit.setText(wheelMain.getTime());
//				}
//			}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//				}
//			}).show();
		} else if (view.getId() == R.id.btn_addhealth) {
			postData();
		}
	}

	private void postData() {
		String time = timeEdit.getText().toString().trim();
		String hospital = hospitalEdit.getText().toString().trim();
		String keshi = keshiEdit.getText().toString().trim();
		String content = contentEdit.getText().toString().trim();
		if (testData != null && testData.size() != 0) {
			for (int i = 0; i < testData.size(); i++) {
				imageAdd1.append(testData.get(i));
				imageAdd1.append(",");
			}
		}
		String description = null;
		if (imageAdd1.toString().length() != 0) {
			description = imageAdd1.toString().substring(0, imageAdd1.toString().length() - 1);
		}
		if (time == null || time.length() == 0) {
			showToast("请输入就诊时间");
			return;
		}
		if (hospital == null || hospital.length() == 0) {
			showToast("请输入就诊医院");
			return;
		}
		if (keshi == null || keshi.length() == 0) {
			showToast("请输入就诊科室");
			return;
		}
		if (content == null || content.length() == 0) {
			showToast("请输入症状描述");
			return;
		}
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			if (ID != null) {
				params.put("id", ID);
			}
			params.put("name", keshi);
			params.put("time", time);
			params.put("mechanism", hospital);
			params.put("content", content);
			if (images.size() != 0) {
				for (int i = 0; i < images.size(); i++) {
					imageAdd2.append(images.get(i));
					imageAdd2.append(",");
				}
			}
			if (imageAdd2.toString().length() != 0) {
				Log.e("XX", "kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
				params.put("attachment_ids", imageAdd2.toString().substring(0, imageAdd2.toString().length() - 1));
			} else {
				// params.put("attachment_ids", "");
			}
			
			Log.e("YY", "token" + SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			Log.e("YY", "userid" + SharePrefUtil.getString(Conast.MEMBER_ID));
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.add_bingli, SetProfile.class, params, new Response.Listener() {
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

	public String toUnicode(String s) {
		String s1 = "";
		for (int i = 0; i < s.length(); i++) {
			s1 += "\\u" + Integer.toHexString(s.charAt(i) & 0xffff);

		}
		return s1;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == EDIT_CONTENT && resultCode == RESULT_OK) {
			Log.e("XX", "1111111111111111111111111111111111111111111111111111");
			ID = data.getStringExtra("ID");
			testData.add(data.getStringExtra("content"));
			imageLists.add(data.getStringArrayListExtra("imagePaths"));
			ATTACHMENT_ID = data.getStringExtra("ATTACHMENT_ID");
			groupkeys.add(data.getStringExtra("groupkey"));
			Log.e("XX", "XX:" + data.getStringExtra("groupkey") + "");
			if (ATTACHMENT_ID.equals("") || ATTACHMENT_ID == null) {
				images.add("");
			} else {
				images.add(ATTACHMENT_ID);
			}
			if (ID != null) {
				Log.e("id", "id不为空");
				listAdapter.change(imageLists, testData, times, 2);
			} else {
				Log.e("id", "id为空");
				listAdapter.change(imageLists, testData);
			}

			listAdapter.notifyDataSetChanged();
		} else if (requestCode == 0x24 && resultCode == RESULT_OK){
			int point = Integer.valueOf(data.getStringExtra("point"));
			ID = data.getStringExtra("ID");
			Log.e("IDID", "ID:" + ID);
			testData.remove(point);
			testData.add(point, data.getStringExtra("content"));
			imageLists.remove(point);
			imageLists.add(point, data.getStringArrayListExtra("imagePaths"));
//			pictures1.remove(point);
//			pictures1.add(point, data.getStringExtra("blDatas1"));
//			Log.e("blDatas1", "blDatas1:" + pictures1);
//			pictures2.remove(point);
//			pictures2.add(point, data.getStringExtra("blDatas2"));
//			pictures3.remove(point);
//			pictures3.add(point, data.getStringExtra("blDatas3"));
			ATTACHMENT_ID = data.getStringExtra("ATTACHMENT_ID");
			groupkeys.remove(point);
			groupkeys.add(point, data.getStringExtra("groupkey"));
			Log.e("2222222222222groups", data.getStringExtra("groupkey") + "");
			if (ATTACHMENT_ID.equals("") || ATTACHMENT_ID == null) {
				images.add("");
			} else {
				images.remove(point);
				images.add(point, ATTACHMENT_ID);
			}
			
			if (ID != null) {
				Log.e("id", "id不为空");
				listAdapter.change(imageLists, testData, times, 1);
			} else {
				Log.e("id", "id为空");
				listAdapter.change(imageLists, testData);
			}

			listAdapter.notifyDataSetChanged();
		} else if (requestCode == 0x25 && resultCode == RESULT_OK){
			int point = Integer.valueOf(data.getStringExtra("point"));
			ID = data.getStringExtra("ID");
			Log.e("IDID", "ID:" + ID);
			testData.remove(point);
			testData.add(point, data.getStringExtra("content"));
			ArrayList<String> imageList1 = data.getStringArrayListExtra("imagePaths");
			imageLists.remove(point);
			imageLists.add(point, imageList1);
			Log.e("XX", "图片数量:" + imageList1.size());
//			pictures1.remove(point);
//			pictures1.add(point, data.getStringExtra("blDatas1"));
//			pictures2.remove(point);
//			pictures2.add(point, data.getStringExtra("blDatas2"));
//			pictures3.remove(point);
//			pictures3.add(point, data.getStringExtra("blDatas3"));
//			Log.e("", "pictures3:yyyyyyyyyyyy" + pictures3.get(point));
			ATTACHMENT_ID = data.getStringExtra("ATTACHMENT_ID");
			groupkeys.remove(point);
			groupkeys.add(point, data.getStringExtra("groupkey"));
			Log.e("2222222222222groups", data.getStringExtra("groupkey") + "");
			
			if (ATTACHMENT_ID.equals("") || ATTACHMENT_ID == null) {
				images.add("");
			} else {
				images.remove(point);
				images.add(point, ATTACHMENT_ID);
			}
			Log.e("111", "testData:" + testData.size());
			Log.e("111", "blDatas1:" + pictures1.size());
			Log.e("111", "blDatas2:" + pictures2.size());
			Log.e("111", "blDatas3:" + pictures3.size());
			Log.e("111", "times:" + times.size());
			
			if (ID != null) {
				Log.e("id", "id不为空");
				listAdapter.change(imageLists, testData, times, 2);
			} else {
				Log.e("id", "id为空");
				listAdapter.change(imageLists, testData);
			}

			listAdapter.notifyDataSetChanged();
		} else if (requestCode == 0x26 && resultCode == RESULT_OK){
			
			ID = data.getStringExtra("ID");
			
			testData.add(data.getStringExtra("content"));
			imageLists.add(data.getStringArrayListExtra("imagePaths"));
//			pictures1.add(data.getStringExtra("blDatas1"));
//			Log.e("blDatas1", "blDatas1:" + pictures1);
//			pictures2.add(data.getStringExtra("blDatas2"));
//			pictures3.add(data.getStringExtra("blDatas3"));
			ATTACHMENT_ID = data.getStringExtra("ATTACHMENT_ID");
			groupkeys.add(data.getStringExtra("groupkey"));
			Log.e("2222222222222groups", data.getStringExtra("groupkey") + "");
			if (ATTACHMENT_ID.equals("") || ATTACHMENT_ID == null) {
				images.add("");
			} else {
				images.add(ATTACHMENT_ID);
			}
			
			if (ID != null) {
				Log.e("id", "id不为空");
				listAdapter.change(imageLists, testData, times, 1);
			} else {
				Log.e("id", "id为空");
				listAdapter.change(imageLists, testData);
			}

			listAdapter.notifyDataSetChanged();
		}
	}

	private void initData() {
		Bundle data = getIntent().getExtras();
		if (data == null) {
			return;
		} else {
			String time = data.getString("time");
			String hospital = data.getString("hospital");
			String keshi = data.getString("keshi");
			String zhenzhuang = data.getString("zhenzhuang");
			ID = data.getString("ID");
			Log.e("IDID", "ID:" + ID);
			testData = data.getStringArrayList("testData");
//			pictures1 = data.getStringArrayList("pictures1");
//			pictures2 = data.getStringArrayList("pictures2");
//			pictures3 = data.getStringArrayList("pictures3");
			times = data.getStringArrayList("times");
			row = data.getInt("row");
			page = data.getString("page");
			top_title.setText("编辑病历夹");
			timeEdit.setText(time);
			hospitalEdit.setText(hospital);
			keshiEdit.setText(keshi);
			contentEdit.setText(zhenzhuang);
			
			postEditData();
		}
	}

	private void postEditData() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("page", page);
			params.put("id", ID);
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_bingli_list, MedicalHistoryBean.class, params, new Response.Listener() {
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

}
