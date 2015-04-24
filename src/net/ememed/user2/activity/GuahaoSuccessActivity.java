package net.ememed.user2.activity;

import java.io.Serializable;

import net.ememed.user2.R;
import net.ememed.user2.entity.CuredmemeberListEntry;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.GuahaoSuccessEntry;
import net.ememed.user2.entity.ScheduleDetailEntry;
import net.ememed.user2.entity.UserGuahaoEntry;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class GuahaoSuccessActivity extends BasicActivity {
	
	private GuahaoSuccessEntry entry;
	private String time;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_reserva_info);
		entry = (GuahaoSuccessEntry) getIntent().getSerializableExtra("data");
		time = getIntent().getStringExtra("time");
	}
	@Override
	protected void setupView() {
		TextView tv_name = (TextView) findViewById(R.id.tv_name);
		TextView tv_hosp = (TextView) findViewById(R.id.tv_hosp);
		TextView tv_room = (TextView) findViewById(R.id.tv_room);
		TextView tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		TextView tv_time = (TextView) findViewById(R.id.tv_time);
		TextView tv_number = (TextView) findViewById(R.id.tv_number);
		TextView tv_address = (TextView) findViewById(R.id.tv_address);
		tv_name.setText(entry.getUsername());
		tv_hosp.setText(entry.getHosp());
		tv_room.setText(entry.getDepartmen());
		tv_doctor_name.setText(entry.getDoctorname());
		tv_time.setText(time);
		tv_number.setText(entry.getOrderno());
//		tv_address.setText(text)
	}
	
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.btn_reservation:
			Intent i = new Intent(this,HistoryGuahaoActivity.class);
			startActivity(i);
			finish();
			
			break;

		default:
			break;
		}
		
	}

}
