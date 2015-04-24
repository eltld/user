package net.ememed.user2.medicalhistory.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.medicalhistory.adapter.ListViewAdapter;
import net.ememed.user2.medicalhistory.adapter.ListViewForScrollView;
import net.ememed.user2.medicalhistory.bean.ImageUrlBean;
import net.ememed.user2.medicalhistory.bean.MedicalHistory;
import net.ememed.user2.medicalhistory.bean.MedicalHistoryBean;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class MedicalHistoryYuLan extends BasicActivity {
	private TextView top_title3;
	private TextView top_title;
	private TextView timeTxt, hospitalTxt, keshiTxt, zhenzhuangTxt;
	private LinearLayout ll_title;
	private TextView top_title2;
	private Button btn_addhealth;
	private ListViewForScrollView mListView;
	private RelativeLayout relativeLayout;
	private ListViewAdapter listAdapter;
	private String time;
	private String hospital;
	private String keshi;
	private String zhenzhuang;
	private String ID = null;
	private String page = null;
	ArrayList<String> contents = new ArrayList<String>();
	ArrayList<String> contents1 = new ArrayList<String>();
	ArrayList<String> contents2 = new ArrayList<String>();
	ArrayList<String> contents3 = new ArrayList<String>();
	ArrayList<String> contents4 = new ArrayList<String>();
	ArrayList<String> times = new ArrayList<String>();
	ArrayList<String> times1 = new ArrayList<String>();
	ArrayList<String> ids = new ArrayList<String>();
	ArrayList<String> ids1 = new ArrayList<String>();
	ArrayList<String> ids2 = new ArrayList<String>();
	ArrayList<String> ids3 = new ArrayList<String>();
	private int row = 0;
	public ArrayList<ArrayList<String>> imageLists = new ArrayList<ArrayList<String>>();
	private ArrayList<String> imagePaths = new ArrayList<String>();

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.medical_history_yulan);
		imageLists.clear();
		initView();
		initData();
	}

	private void initView() {
		timeTxt = (TextView) findViewById(R.id.yulan_jiuzhengshijian);
		hospitalTxt = (TextView) findViewById(R.id.yulan_hospital);
		keshiTxt = (TextView) findViewById(R.id.yulan_keshi);
		zhenzhuangTxt = (TextView) findViewById(R.id.yulan_zhenzhuang);
		relativeLayout = (RelativeLayout) findViewById(R.id.yulan_next);
		mListView = (ListViewForScrollView) findViewById(R.id.yulan_add_bingli);
		top_title3 = (TextView) findViewById(R.id.top_title3);
		top_title2 = (TextView) findViewById(R.id.top_title2);
		top_title = (TextView) findViewById(R.id.top_title);
		ll_title = (LinearLayout) findViewById(R.id.ll_title);
		ll_title.setVisibility(View.VISIBLE);
		top_title.setVisibility(View.GONE);

		Bundle data = getIntent().getExtras();
		time = data.getString("time");
		hospital = data.getString("hospital");
		keshi = data.getString("keshi");
		zhenzhuang = data.getString("zhenzhuang");
		ID = data.getString("ID");
		page = data.getString("page");
		ids = data.getStringArrayList("ids");
		top_title3.setText(time);
//		top_title2.setText(text);
		listAdapter = new ListViewAdapter(null, null, null, MedicalHistoryYuLan.this);
		mListView.setAdapter(listAdapter);

		if (time != null && !time.equals("")) {
			timeTxt.setText(time);
		}
		if (hospital != null && !hospital.equals("")) {
			hospitalTxt.setText(hospital);
		}
		if (keshi != null && !keshi.equals("")) {
			keshiTxt.setText(keshi);
		}
		if (zhenzhuang != null && !zhenzhuang.equals("")) {
			zhenzhuangTxt.setText(zhenzhuang);
		}

	}

	// 分组
	private void sort(List<ImageUrlBean> datas) {
		List<ImageUrlBean> fileList = datas;
		// fileList.add(); 添加对象
		List<List<ImageUrlBean>> grpList = new ArrayList<List<ImageUrlBean>>();

		if (fileList.size() == 0) {
			relativeLayout.setVisibility(View.GONE);
			return;
		} else {
			relativeLayout.setVisibility(View.VISIBLE);
		}

		// 创建一个组
		List<ImageUrlBean> grp = new ArrayList<ImageUrlBean>();
		grp.add(fileList.get(0));
		String groupKey = fileList.get(0).getGROUPKEY();
		grpList.add(grp);
		Log.e("XX", "第一个:" + groupKey);
		// 遍历
		for (int i = 1; i < fileList.size(); i++) {
			if (!fileList.get(i).getGROUPKEY().equals(groupKey)) {
				grp = new ArrayList<ImageUrlBean>(); // 创建新的组
				grpList.add(grp); // 结束一个组
				groupKey = fileList.get(i).getGROUPKEY();
				Log.e("XX", "第二个:" + groupKey);
				grp.add(fileList.get(i));

			} else {
				grp.add(fileList.get(i));
			}
		}
		row = grpList.size();
		Log.e("XX", "XX1111111:" + grpList.size());
		getGroup(grpList);
	}

	private void getGroup(List<List<ImageUrlBean>> grpList) {

		for (int i = 0; i < grpList.size(); i++) {
			List<ImageUrlBean> grp = grpList.get(i);
			ArrayList<String> paths = new ArrayList<String>();
			for (int j = 0; j < grp.size(); j++) {
				Log.e("grounds", "groupkey:" + grp.get(j).getGROUPKEY());
				boolean isNull = true;
				if (j == 0) {
					if (grp.get(j).getDESCRIPTION() == null || grp.get(j).getDESCRIPTION().equals("")) {
						isNull = false;
					} else {
						contents4.add(grp.get(j).getDESCRIPTION());
					}
					times1.add(grp.get(j).getUPDATETIME().split(" ")[0]);
//					if (grp.get(j).getATTACHMENT() != null && !grp.get(j).getATTACHMENT().equals("")) {
//						pictures1.add(grp.get(j).getATTACHMENT());
//					} else {
//						pictures1.add("");
//					}
					
//					ids1.add(ids.get(j));
//					if (grp.size() == 1) {
//						pictures2.add("");
//						pictures3.add("");
//						ids2.add("");
//						ids3.add("");
//					}
				}
				if (!isNull) {
					if (grp.get(j).getDESCRIPTION() != null && !grp.get(j).getDESCRIPTION().equals("")) {
						contents4.add(grp.get(j).getDESCRIPTION());
						isNull = true;
					}
				}
				if (j == (grp.size() - 1)) {
					if (!isNull) {
						contents4.add(grp.get(j).getDESCRIPTION());
					}
				}
				if (grp.get(j).getATTACHMENT() != null && !grp.get(j).getATTACHMENT().equals("")) {
					paths.add(grp.get(j).getATTACHMENT());
				}
//				else if (j == 1) {
//					ids2.add(grp.get(j).getATTACHMENT_ID());
//					pictures2.add(grp.get(j).getATTACHMENT());
//					if (grp.size() == 2) {
//						pictures3.add("");
//						ids3.add("");
//					}
//				} else if (j == 2) {
//					ids3.add(grp.get(j).getATTACHMENT_ID());
//					pictures3.add(grp.get(j).getATTACHMENT());
//				}
				Log.e("XX", "查看：" + grp.get(j).getATTACHMENT());
			}
			imageLists.add(paths);
		}
		listAdapter.change(imageLists, contents4, times1);
		listAdapter.notifyDataSetChanged();
	}

	@Override
	protected void setupView() {
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("编辑");
		super.setupView();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_addhealth) {
			Bundle data = new Bundle();
			data.putString("ID", ID);
			data.putString("time", time);
			data.putString("hospital", hospital);
			data.putString("keshi", keshi);
			data.putString("zhenzhuang", zhenzhuang);
			data.putStringArrayList("testData", contents4);
			data.putStringArrayList("times", times1);
//			if (ids1.size() != 0) {
//				data.putStringArrayList("ids1", ids1);
//			}
//			if (ids2.size() != 0) {
//				data.putStringArrayList("ids2", ids2);
//			}
//			if (ids3.size() != 0) {
//				data.putStringArrayList("ids3", ids3);
//			}
			data.putInt("row", row);
			data.putString("page", page);
//			data.putStringArrayList("times", times1);
//			data.putStringArrayList("times", times1);
			Utils.startActivity(MedicalHistoryYuLan.this, MedicalHistoryEditFileActivity.class, data);
			finish();
		}
	}

	private void initData() {
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

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		try {
			switch (msg.what) {
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
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}
}
