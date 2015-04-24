package net.ememed.user2.erh.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.medicalhistory.Utils;

public class ErhMainActivity extends BasicActivity {
	private TextView top_title;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.erh_person_main);
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("健康档案");
		super.setupView();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.person_base_ll) {
			Utils.startActivity(this, ErhPersonBaseEditActivity.class);
		} else if (view.getId() == R.id.person_history) {
			Utils.startActivity(this, ErhMainHistoryActivity.class);
		}
	}
}
