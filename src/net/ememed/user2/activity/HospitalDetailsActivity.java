package net.ememed.user2.activity;

import net.ememed.user2.R;
import net.ememed.user2.entity.HospitalEntry;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.Util;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HospitalDetailsActivity extends BasicActivity {

	private HospitalEntry entry;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hospital_details);
		entry = (HospitalEntry) getIntent().getSerializableExtra("hosp_entry");
	}

	@Override
	protected void setupView() {
		TextView title = (TextView) findViewById(R.id.top_title);
		title.setText("医院简介");

		
		TextView tv_hospital_name = (TextView) findViewById(R.id.tv_hospital_name);// 医院名称
		TextView tv_hospital_level = (TextView) findViewById(R.id.tv_hospital_level);// 医院等级
		TextView tv_hosp_desc = (TextView) findViewById(R.id.tv_hosp_desc);// 医院简介
		TextView tv_key_zhuanke = (TextView) findViewById(R.id.tv_key_zhuanke);// 重点专科
		TextView tv_yuyuequhao = (TextView) findViewById(R.id.tv_yuyuequhao);// 预约挂号
		TextView tv_hosp_notice = (TextView) findViewById(R.id.tv_hosp_notice);// 就医指引
		TextView tv_fanghao_time = (TextView) findViewById(R.id.tv_fanghao_time);// 放号时间
		TextView tv_telephone = (TextView) findViewById(R.id.tv_telephone);// 电话
		TextView tv_address = (TextView) findViewById(R.id.tv_address);// 地址
		TextView tv_guide = (TextView) findViewById(R.id.tv_guide);// 乘车路线
		ImageView iv_hospital_appearance = (ImageView) findViewById(R.id.iv_hospital_appearance);
		imageLoader.displayImage(entry.getHOSPITAL_INFO().getIMGURL_BANNER(),iv_hospital_appearance,Util.getHosp_default_portrait());
		if (entry != null) {
			tv_hospital_name.setText(entry.getHOSPITAL_INFO().getHOSP_NAME());
			String hosp_INFO = entry.getHOSPITAL_INFO().getHOSP_INFO();
			if (!TextUtils.isEmpty(hosp_INFO))
				tv_hosp_desc.setText(Html.fromHtml("&nbsp;&nbsp;&nbsp;&nbsp;"+hosp_INFO));

			String grade = entry.getHOSPITAL_INFO().getGRADE();
			if (!TextUtils.isEmpty(grade)) {
				tv_hospital_level.setVisibility(View.VISIBLE);
				tv_hospital_level.setText(Html.fromHtml(grade).toString());
			}
			String guide = entry.getHOSPITAL_INFO().getBUSS_GUIDE();
			if (!TextUtils.isEmpty(guide)) {
				tv_guide.setVisibility(View.VISIBLE);
				tv_guide.setText(Html.fromHtml(guide).toString());
			}
			String address = entry.getHOSPITAL_INFO().getADDRESS();
			if (!TextUtils.isEmpty(address)) {
				tv_address.setVisibility(View.VISIBLE);
				tv_address.setText(Html.fromHtml(address));
			}
			String telephone = entry.getHOSPITAL_INFO().getTELEPHONE();
			if (!TextUtils.isEmpty(telephone)) {
				tv_telephone.setVisibility(View.VISIBLE);
				tv_telephone.setText(Html.fromHtml(telephone));
			}
			String letout_TIME = entry.getHOSPITAL_INFO().getLETOUT_TIME();
			if (!TextUtils.isEmpty(letout_TIME)) {
				tv_fanghao_time.setVisibility(View.VISIBLE);
				tv_fanghao_time.setText(Html.fromHtml(letout_TIME));
			}

			String hosp_NOTICE = entry.getHOSPITAL_INFO().getHOSP_NOTICE();
			if (!TextUtils.isEmpty(hosp_NOTICE)) {
				tv_hosp_notice.setVisibility(View.VISIBLE);
				tv_hosp_notice.setText(Html.fromHtml(hosp_NOTICE));
			}
		}

	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.ll_go_sub:// 我要预约
			Intent intent = new Intent(this, KeshiActivity.class);
			intent.putExtra("hosp_entry", entry);
			startActivity(intent);

			break;

		default:
			break;
		}
	}
}
