package net.ememed.user2.erh.activity;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.erh.util.SingleDmAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class ErhPayAndWorkActivity extends BasicActivity {
	private TextView top_title;
	private ListView mListView;
	private Button btn_addhealth;

	private String[] hys = new String[] { "国家机关", "党群组织", "企业", "事业单位负责人", "专业技术人员", "办事人员和有关人员", "商业、服务人员", "农、林、牧、渔、水利业生产人员", "生产、运输设备操作人员及有关人员", "军人", "不便的其他从业人员", "退休/离休", "无业" };
	private SingleDmAdapter singleBaseAdapter;
	private String job;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.medical_history_custom_view);
		job = getIntent().getExtras().getString("job");
		initEvent();
		initData();
	}

	private void initData() {
		singleBaseAdapter = new SingleDmAdapter(ErhPayAndWorkActivity.this, hys, job);
		mListView.setAdapter(singleBaseAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				job = hys[arg2];
				singleBaseAdapter.setName(hys[arg2]);
				singleBaseAdapter.notifyDataSetChanged();
			}
		});
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("选择职业");

		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("确定");
		super.setupView();
	}

	protected void initEvent() {
		mListView = (ListView) findViewById(R.id.list);
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_addhealth) {
			Intent intent = getIntent();
			intent.putExtra("job", job);
			setResult(RESULT_OK, intent);
			finish();
		}
	}

}
