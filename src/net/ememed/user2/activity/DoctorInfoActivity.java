package net.ememed.user2.activity;

import net.ememed.user2.baike.DoctorAttentionActivity;
import net.ememed.user2.baike.DoctorFansActivity;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.MemberInfoEntity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.SpringbackScrollview;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DoctorInfoActivity extends BasicActivity {

	private DoctorEntry entry;
	private String doctor_name;
	private TextView tv_doctor_name, tv_doctor_domain, tv_hos_name, tv_prof_attributes, tv_keshi,
			tv_hos_address, tv_search_speciality, tv_doctor_person_info, top_title;
	private ImageView doctor_head_portrait;
	private MemberInfoEntity data;
	// private TextView tv_hos_grade;
	// private LinearLayout ll_hosp_desc;
	private TextView tv_hos_desc;
	private LinearLayout ll_hosp_address;
	private LinearLayout ll_person_info;
	private View line_person_info;
	private View line_hosp_address;
	private String avatar;
	public static final int RESULT_FINISH = 2001;
	private LinearLayout ll_button;
	private Button btn_goList;
	private CheckBox follow;
	private TextView tv_attention;
	private TextView tv_doctor_fans;
	private TextView tv_user_fans;
	private String doctorId;
	private Button btn_buyService;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_doctor_info);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("医生信息");

		doctor_name = getIntent().getStringExtra("doctor_name");

		doctor_head_portrait = (ImageView) findViewById(R.id.doctor_head_portrait);
		tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		tv_doctor_domain = (TextView) findViewById(R.id.tv_doctor_domain);
		tv_hos_name = (TextView) findViewById(R.id.tv_hos_name);
		// tv_hos_grade = (TextView) findViewById(R.id.tv_hos_grade);
		ll_hosp_address = (LinearLayout) findViewById(R.id.ll_hosp_address);
		line_hosp_address = (View) findViewById(R.id.line_hosp_address);
		// ll_hosp_desc = (LinearLayout) findViewById(R.id.ll_hosp_desc);
		tv_hos_desc = (TextView) findViewById(R.id.tv_hos_desc);
		tv_prof_attributes = (TextView) findViewById(R.id.tv_prof_attributes);
		tv_keshi = (TextView) findViewById(R.id.tv_keshi);
		tv_search_speciality = (TextView) findViewById(R.id.tv_search_speciality);
		tv_hos_address = (TextView) findViewById(R.id.tv_hos_address);
		ll_person_info = (LinearLayout) findViewById(R.id.ll_person_info);
		line_person_info = (View) findViewById(R.id.line_person_info);
		tv_doctor_person_info = (TextView) findViewById(R.id.tv_doctor_person_info);
		follow = (CheckBox) findViewById(R.id.follow);

		tv_attention = (TextView) findViewById(R.id.tv_attention);
		tv_doctor_fans = (TextView) findViewById(R.id.tv_doctor_fans);
		tv_user_fans = (TextView) findViewById(R.id.tv_user_fans);

		btn_buyService = (Button) findViewById(R.id.btn_goDoctor);

		if ((entry = (DoctorEntry) getIntent().getExtras().getSerializable("entry")) != null) {
			doctorId = entry.getDOCTORID();
			avatar = entry.getAVATAR();

			if (null != entry.getHOSPITAL_INFO()) {
				// if (!TextUtils.isEmpty(entry.getHOSPITAL_INFO().getGRADE())) {
				// tv_hos_grade.setText(entry.getHOSPITAL_INFO().getGRADE());
				// }
				// if (!TextUtils.isEmpty(entry.getHOSPITAL_INFO().getCONTEXT())) {
				// ll_hosp_desc.setVisibility(View.VISIBLE);
				// tv_hos_desc.setText(Html.fromHtml(entry.getHOSPITAL_INFO().getCONTEXT()));
				// } else {
				// ll_hosp_desc.setVisibility(View.GONE);
				// }

				if (!TextUtils.isEmpty(entry.getHOSPITAL_INFO().getADDRESS())) {
					line_hosp_address.setVisibility(View.VISIBLE);
					ll_hosp_address.setVisibility(View.VISIBLE);
					tv_hos_address.setText(entry.getHOSPITAL_INFO().getADDRESS());
				} else {
					line_hosp_address.setVisibility(View.VISIBLE);
					ll_hosp_address.setVisibility(View.VISIBLE);
				}
			}

			if (!TextUtils.isEmpty(entry.getAVATAR())) {
				imageLoader.displayImage(entry.getAVATAR(), doctor_head_portrait,
						Util.getOptions_pic());
			}
			tv_doctor_name.setText(doctor_name);
			if (!TextUtils.isEmpty(entry.getPROFESSIONAL())) {
				tv_doctor_domain.setText(entry.getPROFESSIONAL());
			}
			if (!TextUtils.isEmpty(entry.getHOSPITALNAME())) {
				tv_hos_name.setText(entry.getHOSPITALNAME());
			}
			if (!TextUtils.isEmpty(entry.getROOMNAME())) {
				tv_keshi.setText(entry.getROOMNAME());
			}

			if (!TextUtils.isEmpty(entry.getALLOWFREECONSULT())) {
				if (entry.getALLOWFREECONSULT().equals("1")) {
					tv_prof_attributes.setText("全科医生");
				} else if (entry.getALLOWFREECONSULT().equals("0")) {
					tv_prof_attributes.setText("专科医生");
				}
			}

			//
			if (!TextUtils.isEmpty(entry.getSPECIALITY())) {
				tv_search_speciality.setText(entry.getSPECIALITY());
			}
			if (!TextUtils.isEmpty(entry.getADDRESS())) {
				line_hosp_address.setVisibility(View.VISIBLE);
				ll_hosp_address.setVisibility(View.VISIBLE);
				tv_hos_address.setText(entry.getADDRESS());
			} else {
				line_hosp_address.setVisibility(View.VISIBLE);
				ll_hosp_address.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(entry.getDETAILINFO())) {
				ll_person_info.setVisibility(View.VISIBLE);
				line_person_info.setVisibility(View.VISIBLE);
				tv_doctor_person_info.setText(entry.getDETAILINFO());
			} else {
				line_person_info.setVisibility(View.GONE);
				ll_person_info.setVisibility(View.GONE);
			}

			// hide buy button
			if (getIntent() != null) {
				int hasService = getIntent().getIntExtra("hasService", 0);
				btn_buyService.setVisibility(hasService == 0 ? View.GONE : View.VISIBLE);
			}

			// TODO 从搜索医生界面进入该activity时，传值为entry,不含审核通过与否的参数，待添加
			if (!TextUtils.isEmpty(entry.getAUDITSTATUS()) && "1".equals(entry.getAUDITSTATUS())) {
				follow.setChecked(true);
			} else {
				follow.setChecked(false);
			}

			tv_attention.setText("关注  " + entry.getDOCTOR_FANS().getAttention_total_num());
			tv_doctor_fans.setText("医生粉丝  " + entry.getFans_doctor_total_num());
			tv_user_fans.setText("患者粉丝  " + entry.getFans_user_total_num());
		}
		if ((data = (MemberInfoEntity) getIntent().getExtras().getSerializable("data")) != null) {
			avatar = data.getAVATAR();
			doctorId = data.getMEMBERID();
			if (null != data.getHOSPITAL_INFO()) {
				// if (!TextUtils.isEmpty(data.getHOSPITAL_INFO().getGRADE())) {
				// tv_hos_grade.setText(data.getHOSPITAL_INFO().getGRADE());
				// }
				// if (!TextUtils.isEmpty(data.getHOSPITAL_INFO().getCONTEXT())) {
				// ll_hosp_desc.setVisibility(View.VISIBLE);
				// tv_hos_desc.setText(Html.fromHtml(data.getHOSPITAL_INFO().getCONTEXT()));
				// } else {
				// ll_hosp_desc.setVisibility(View.GONE);
				// }

				if (!TextUtils.isEmpty(data.getHOSPITAL_INFO().getADDRESS())) {
					line_hosp_address.setVisibility(View.VISIBLE);
					ll_hosp_address.setVisibility(View.VISIBLE);
					tv_hos_address.setText(data.getHOSPITAL_INFO().getADDRESS());
				} else {
					line_hosp_address.setVisibility(View.VISIBLE);
					ll_hosp_address.setVisibility(View.VISIBLE);
				}
			}

			if (!TextUtils.isEmpty(data.getAVATAR())) {
				imageLoader.displayImage(data.getAVATAR(), doctor_head_portrait,
						Util.getOptions_pic());
			}
			tv_doctor_name.setText(doctor_name);
			if (!TextUtils.isEmpty(data.getPROFESSIONAL())) {
				tv_doctor_domain.setText(data.getPROFESSIONAL());
			}
			if (!TextUtils.isEmpty(data.getHOSPITALNAME())) {
				tv_hos_name.setText(data.getHOSPITALNAME());
			}
			if (!TextUtils.isEmpty(data.getROOMNAME())) {
				tv_keshi.setText(data.getROOMNAME());
			}
			if (!TextUtils.isEmpty(data.getALLOWFREECONSULT())) {
				if (data.getALLOWFREECONSULT().equals("1")) {
					tv_prof_attributes.setText("全科医生");
				} else if (data.getALLOWFREECONSULT().equals("0")) {
					tv_prof_attributes.setText("专科医生");
				}
			}
			if (!TextUtils.isEmpty(data.getSPECIALITY())) {
				tv_search_speciality.setText(data.getSPECIALITY());
			}
			if (!TextUtils.isEmpty(data.getADDRESS())) {
				line_hosp_address.setVisibility(View.VISIBLE);
				ll_hosp_address.setVisibility(View.VISIBLE);
				tv_hos_address.setText(data.getADDRESS());
			} else {
				line_hosp_address.setVisibility(View.VISIBLE);
				ll_hosp_address.setVisibility(View.VISIBLE);
			}
			if (!TextUtils.isEmpty(data.getDETAILINFO())) {
				ll_person_info.setVisibility(View.VISIBLE);
				line_person_info.setVisibility(View.VISIBLE);
				tv_doctor_person_info.setText(data.getDETAILINFO());
			} else {
				line_person_info.setVisibility(View.GONE);
				ll_person_info.setVisibility(View.GONE);
			}

			if (!TextUtils.isEmpty(data.getAUDITSTATUS()) && "1".equals(data.getAUDITSTATUS())) {
				follow.setChecked(true);
			} else {
				follow.setChecked(false);
			}

			tv_attention.setText("关注  " + data.getDOCTOR_FANS().getAttention_total_num());
			tv_doctor_fans.setText("医生粉丝  " + data.getFans_doctor_total_num());
			tv_user_fans.setText("患者粉丝  " + data.getFans_user_total_num());
		}
		ll_button = (LinearLayout) findViewById(R.id.ll_button);
		btn_goList = (Button) findViewById(R.id.btn_goList);
		if (!TextUtils.isEmpty(getIntent().getStringExtra("src"))) {
			btn_goList.setVisibility(View.GONE);
		}
		SpringbackScrollview ssv = (SpringbackScrollview) findViewById(R.id.ssv);
		ssv.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				// // 触摸屏幕时刻
				// case MotionEvent.ACTION_DOWN:
				// ll_button.setVisibility(View.GONE);
				// break;
				// 终止触摸时刻
				case MotionEvent.ACTION_UP:
					ll_button.setVisibility(View.VISIBLE);
					break;
				// 触摸并移动时刻
				case MotionEvent.ACTION_MOVE:
					ll_button.setVisibility(View.GONE);
					break;
				default:
					break;
				}
				return false;
			}
		});
	}

	@Override
	protected void getData() {
		super.getData();
	}

	public void doClick(View view) {
		Intent intent;
		int id = view.getId();
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.contact_image:
			intent = new Intent(this, ImageActivity.class);
			intent.putExtra("avatar", avatar);
			startActivity(intent);
			break;
		case R.id.btn_goDoctor:
			// DoctorDetailInfoActivity.getActivity().getIntent().putExtra("type", 1);
			finish();
			break;
		case R.id.btn_goList:
			Intent i = new Intent(this, DoctorClininActivity.class);
			setResult(RESULT_FINISH, i);
			finish();
			break;
		case R.id.tv_attention:
			intent = new Intent(DoctorInfoActivity.this, DoctorAttentionActivity.class);
			intent.putExtra("doctor_id", doctorId);
			intent.putExtra("doctor_name", doctor_name);
			startActivity(intent);
			break;
		case R.id.tv_doctor_fans:
			DoctorFansActivity.startAction(this, doctorId, DoctorFansActivity.FANS_TYPE_DOCTOR);
			break;
		case R.id.tv_user_fans:
			DoctorFansActivity.startAction(this, doctorId, DoctorFansActivity.FANS_TYPE_USER);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_member_info);
	}
}
