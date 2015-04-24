package net.ememed.user2.activity;

import java.util.List;

import de.greenrobot.event.EventBus;

import net.ememed.user2.R;
import net.ememed.user2.entity.GuahaoDoctorInfo;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.ScheduleTime;
import net.ememed.user2.util.Util;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GuahaoDoctorInfoActivity extends BasicActivity {

	private String MYTAG = "chenhj, GuahaoDoctorInfoActivity";

	private TextView tv_doctor_name, tv_doctor_domain, tv_hos_name, tv_prof_attributes, tv_keshi, tv_hos_address, tv_search_speciality, tv_doctor_person_info, top_title, tv_hos_grade, tv_hos_desc;
	private ImageView doctor_head_portrait;
	private LinearLayout ll_special, ll_person_info, ll_hosp_address;
	private LinearLayout ll_hosp_desc;
	private View line_person_info;
	private View line_hosp_address;
	private Button btn_guahao;

	private GuahaoDoctorInfo doctorInfo;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		setContentView(R.layout.activity_guahao_doctor_info);
		doctorInfo = (GuahaoDoctorInfo) getIntent().getExtras().getSerializable("GuahaoDoctorInfo");
		EventBus.getDefault().register(this, GuahaoSuccessEvent.class);
	}

	public void onEvent(GuahaoSuccessEvent event) {
		finish();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().removeStickyEvent(GuahaoSuccessEvent.class);
		super.onDestroy();
	}

	@Override
	protected void setupView() {

		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("医生信息");
		doctor_head_portrait = (ImageView) findViewById(R.id.doctor_head_portrait);
		tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		tv_doctor_domain = (TextView) findViewById(R.id.tv_doctor_domain);
		tv_hos_name = (TextView) findViewById(R.id.tv_hos_name);
		tv_hos_grade = (TextView) findViewById(R.id.tv_hos_grade);
		ll_hosp_address = (LinearLayout) findViewById(R.id.ll_hosp_address);
		line_hosp_address = (View) findViewById(R.id.line_hosp_address);
		ll_hosp_desc = (LinearLayout) findViewById(R.id.ll_hosp_desc);
		tv_hos_desc = (TextView) findViewById(R.id.tv_hos_desc);
		tv_prof_attributes = (TextView) findViewById(R.id.tv_prof_attributes);
		tv_keshi = (TextView) findViewById(R.id.tv_keshi);
		tv_search_speciality = (TextView) findViewById(R.id.tv_search_speciality);
		tv_hos_address = (TextView) findViewById(R.id.tv_hos_address);
		ll_person_info = (LinearLayout) findViewById(R.id.ll_person_info);
		line_person_info = (View) findViewById(R.id.line_person_info);
		tv_doctor_person_info = (TextView) findViewById(R.id.tv_doctor_person_info);

		ll_special = (LinearLayout) findViewById(R.id.ll_special);

		btn_guahao = (Button) findViewById(R.id.btn_guahao);

		// 头像
		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getAVATAR())) {
			imageLoader.displayImage(doctorInfo.getDoctor_info().getAVATAR(), doctor_head_portrait, Util.getOptions_pic());
		}

		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getDOCTORNAME())) {
			tv_doctor_name.setText(Html.fromHtml(doctorInfo.getDoctor_info().getDOCTORNAME()).toString());
		}

		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getPROFESSIONAL())) {
			tv_doctor_domain.setText(Html.fromHtml(doctorInfo.getDoctor_info().getPROFESSIONAL()).toString());
		}
		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getHOSPITALNAME())) {
			tv_hos_name.setText(Html.fromHtml(doctorInfo.getDoctor_info().getHOSPITALNAME()).toString());
		}
		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getROOMNAME())) {
			tv_keshi.setText(Html.fromHtml(doctorInfo.getDoctor_info().getROOMNAME()).toString());
		}

		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getALLOWFREECONSULT())) {
			if (doctorInfo.getDoctor_info().getALLOWFREECONSULT().equals("1")) {
				tv_prof_attributes.setText("全科医生");
			} else if (doctorInfo.getDoctor_info().getALLOWFREECONSULT().equals("0")) {
				tv_prof_attributes.setText("专科医生");
			}
		}

		//
		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getSPECIALITY())) {
			tv_search_speciality.setText(Html.fromHtml(doctorInfo.getDoctor_info().getSPECIALITY()).toString());
			// Log.d(MYTAG, "SPECIALITY : " +
			// doctorInfo.getDoctor_info().getSPECIALITY());
		} else {
			// Log.d(MYTAG, "SPECIALITY : is empty !" );
			// ll_special.setVisibility(View.GONE);
		}

		if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getWORKPROVINCE()) || !TextUtils.isEmpty(doctorInfo.getDoctor_info().getWORKCITY()) || !TextUtils.isEmpty(doctorInfo.getDoctor_info().getWORKDIST())) {
			String province = TextUtils.isEmpty(doctorInfo.getDoctor_info().getWORKPROVINCE()) ? "" : doctorInfo.getDoctor_info().getWORKPROVINCE();
			String city = TextUtils.isEmpty(doctorInfo.getDoctor_info().getWORKCITY()) ? "" : doctorInfo.getDoctor_info().getWORKCITY();
			String dist = TextUtils.isEmpty(doctorInfo.getDoctor_info().getWORKDIST()) ? "" : doctorInfo.getDoctor_info().getWORKDIST();
			tv_hos_address.setText(province + " " + city + " " + dist);
		} else {
			ll_hosp_address.setVisibility(View.GONE);
		}
		// if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getADDRESS())) {
		// line_hosp_address.setVisibility(View.VISIBLE);
		// ll_hosp_address.setVisibility(View.VISIBLE);
		// tv_hos_address.setText(entry.getADDRESS());
		// } else {
		// line_hosp_address.setVisibility(View.GONE);
		// ll_hosp_address.setVisibility(View.GONE);
		// }
		// if (!TextUtils.isEmpty(doctorInfo.getDoctor_info().getDETAILINFO()))
		// {
		// ll_person_info.setVisibility(View.VISIBLE);
		// line_person_info.setVisibility(View.VISIBLE);
		// tv_doctor_person_info.setText(entry.getDETAILINFO());
		// } else {
		// line_person_info.setVisibility(View.GONE);
		// ll_person_info.setVisibility(View.GONE);
		// }

		List<ScheduleTime> scheduleList = doctorInfo.getSchedule();
		boolean canGuahao = true;

		if (scheduleList == null || scheduleList.isEmpty()) {
			canGuahao = false;
		} else {

			int leftGuahao = 0;

			for (ScheduleTime time : scheduleList) {
				// Log.d(MYTAG, time.getSchedule_desc());

				try {
					leftGuahao += Integer.parseInt(time.getFree_num());
				} catch (NumberFormatException e) {
					leftGuahao += 0;
				}

			}
			if (leftGuahao <= 0) {
				canGuahao = false;
				// Log.d(MYTAG, "no left ");
			} else {
				// Log.d(MYTAG, "no left ");
				canGuahao = true;
			}

		}

		// 当前医生没有挂号位置时
		if (canGuahao == false) {
			btn_guahao.setEnabled(false);
			// btn_guahao.setTextColor(getResources().getColor(R.color.black));
			// btn_guahao.setBackgroundColor(getResources().getColor(R.color.gray));
			btn_guahao.setText("该医生暂无预约挂号信息");
		}
	}

	public void doClick(View v) {
		Intent intent;
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		case R.id.btn_guahao:
			intent = new Intent(this, DoctorScheduleActivity.class);
			intent.putExtra("Doctot_Item", doctorInfo.getDoctor_info());
			startActivity(intent);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onResult(Message msg) {

	}
}
