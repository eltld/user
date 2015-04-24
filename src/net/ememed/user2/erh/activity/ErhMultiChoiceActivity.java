package net.ememed.user2.erh.activity;

import java.util.ArrayList;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.erh.adapter.HyDmAdapter;

public class ErhMultiChoiceActivity extends BasicActivity {
	private TextView top_title;
	private Button btn_addhealth;
	private HyDmAdapter hyDmAdapter;
	private String[] txts = new String[] { "高血压", "糖尿病", "冠心病",
			"慢性阻塞性肺疾病", "恶性肿瘤", "脑卒中", "重性精神疾病", "结核病", "肝炎",
			"先天畸形", "其他"

	};

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.custom_view3);
		initView();
	}

	private void initView() {
		ListView listView = (ListView) findViewById(R.id.list);
		List<String> reList = new ArrayList<String>();
		hyDmAdapter = new HyDmAdapter(
				this, txts, reList);
		listView.setAdapter(hyDmAdapter);
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("疾病类型");
		
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
			Log.e("XX", "个数:" + hyDmAdapter.getListContent().size());
			Intent data = getIntent();
			data.putStringArrayListExtra("reList", hyDmAdapter.getListContent());
			setResult(RESULT_OK, data);
			finish();
		} 
	}
}
