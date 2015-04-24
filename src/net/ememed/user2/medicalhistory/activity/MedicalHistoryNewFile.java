package net.ememed.user2.medicalhistory.activity;

import android.os.Bundle;
import android.widget.TextView;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.widget.RefreshListView;

public class MedicalHistoryNewFile extends BasicActivity {
	private TextView top_title;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.history_medical_new);	
		initView();
	}

	private void initView() {
		
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("新建病历夹");	
		super.setupView();
	}
}
