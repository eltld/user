package net.ememed.user2.activity;

import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.PraiseAdapter;
import net.ememed.user2.baike.RewardPayActivity;
import net.ememed.user2.entity.DoctorPraiseEntity;
import net.ememed.user2.entity.DoctorPraisinfo;
import net.ememed.user2.entity.MindEntity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.RefreshListViewForScrollView;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class MindActivity extends BasicActivity {

	// private String avatar;
	private String doctor_name;
	private String doctor_id;
	private String questionid;

	private CircleImageView iv_userhead;
	private TextView tv_doctor_name;
	private TextView tv_professional;

	private TextView tv_reward_num;

	private EditText et_other_money;
	private EditText et_content;
	private String notes = "";
	private RefreshListViewForScrollView reward_list;
	private TextView title_reward_wall;
	private PraiseAdapter adapter;
	private int page = 1;
	private RadioGroup radio_layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reward);
		Bundle bundle = getIntent().getExtras();
		doctor_id = bundle.getString("doctor_id");
		questionid = bundle.getString("questionid");
		initView();
	}

	public void initView() {

		((TextView) findViewById(R.id.top_title)).setText("打赏");
		iv_userhead = (CircleImageView) findViewById(R.id.iv_userhead);
		tv_doctor_name = (TextView) findViewById(R.id.doctor_name);
		tv_professional = (TextView) findViewById(R.id.professional);
		et_other_money = (EditText) findViewById(R.id.et_other_money);
		et_content = (EditText) findViewById(R.id.et_content);
		tv_reward_num = (TextView) findViewById(R.id.tv_reward_num);
		title_reward_wall = (TextView) findViewById(R.id.title_reward_wall);
		reward_list = (RefreshListViewForScrollView) findViewById(R.id.reward_list);
		radio_layout = (RadioGroup) findViewById(R.id.radio_layout);
		radio_layout.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.circle_1:
					Money = "1";
					break;
				case R.id.circle_2:
					Money = "5";
					break;
				case R.id.circle_3:
					Money = "10";
					break;
				case R.id.circle_4:
					Money = "20";
					break;

				default:
					break;
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		getPraiseList(page + "");
	}

	public void getPraiseList(String page) {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", doctor_id);
			params.put("limit", "10");
			params.put("page", page);
			System.out.println("params = " + params);
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_doctor_praise_list, DoctorPraiseEntity.class,
					params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {

							destroyDialog();
							DoctorPraiseEntity entity = (DoctorPraiseEntity) response;

							if (entity != null) {
								setAssignment(entity);
							}

						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("数据出错！");
							destroyDialog();
						}
					});
		} else {
			showToast(getString(R.string.net_error));
		}
	}

	public void setAssignment(DoctorPraiseEntity entity) {

		DoctorPraisinfo praisinfo = entity.getDoctorinfo();
		imageLoader.displayImage(praisinfo.getAVATAR(), iv_userhead,
				Util.getOptions_avatar());
		tv_doctor_name.setText(praisinfo.getREALNAME());
		tv_professional.setText(praisinfo.getPROFESSIONAL());
		title_reward_wall.setText(praisinfo.getREALNAME() + "医生的打赏墙");
		tv_reward_num.setText(entity.getCount());// 多少人送心意
		doctor_name = praisinfo.getREALNAME();
		adapter = new PraiseAdapter(entity.getData(), this);
		reward_list.setAdapter(adapter);

	}

	String Money;

	public void getPraise() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", doctor_id);
			params.put("money", Money);
			params.put("notes", notes);
			params.put("channel", "Android");
			params.put("app_version",
					PublicUtil.getVersionName(MindActivity.this));
			params.put("questionid", questionid);
			System.out.println("params = " + params);
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.questionset_praise, MindEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							destroyDialog();
							MindEntity entity = (MindEntity) response;
							if (entity.getSuccess().equals("1")) {
								Intent intent = new Intent(MindActivity.this,
										RewardPayActivity.class);
								intent.putExtra("doctor_name", doctor_name);
								intent.putExtra("doctor_id", doctor_id);
								intent.putExtra("money", Money);
								intent.putExtra("order_id", entity.getData()
										.getORDERID());
								intent.putExtra("type", "1"); // 代表是 从送心意进去的
								startActivity(intent);
							}
							showToast(entity.getErrormsg());
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							showToast("数据出错！");
							destroyDialog();
						}
					});
		} else {
			showToast(getString(R.string.net_error));
		}
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_next:
			verification();
			break;
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	public void verification() {
		if (TextUtils.isEmpty(et_other_money.getText())) {
			if (TextUtils.isEmpty(Money)) {
				showToast("行行好，给点钱吧！");
				return;
			}
		} else {
			Money = et_other_money.getText().toString().trim();
		}
		if (TextUtils.isEmpty(et_content.getText())) {
			showToast("说点什么吧！");
			return;
		}
		notes = et_content.getText().toString().trim();
		getPraise();

	}
}
