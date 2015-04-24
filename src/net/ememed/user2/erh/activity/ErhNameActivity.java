package net.ememed.user2.erh.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;

public class ErhNameActivity extends BasicActivity {
	private TextView top_title;
	private Button btn_addhealth;
	private String name;
	private EditText nameExt;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.erh_person_name);
		nameExt = (EditText) findViewById(R.id.name);
		name = getIntent().getExtras().getString("name");
		nameExt.setText(name);
		nameExt.setSelection(nameExt.getText().length());
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("姓名");
		
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("确定");
		super.setupView();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_addhealth) {
			name = nameExt.getText().toString().trim();
			if (name == null || name.equals("")) {
				showToast("请输入名字");
				return;
			}
			Intent intent = getIntent();
			intent.putExtra("name", name);
			setResult(RESULT_OK, intent);
			finish();
		}
	}
}
