package net.ememed.user2.activity;

import java.util.HashMap;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.EvaluationEntity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class EvaluateServiceActivity extends BasicActivity {

	private static final String TITLE = "服务评价";
	private RatingBar mRatingBar;
	private EditText et_comment;

	private String mDoctorId;
	private String mDoctorName;
	private String mDoctorPortrait;
	private String mQuestionId;
	private boolean mIsFollow;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_evaluate_commit);
		((TextView) findViewById(R.id.tv_top_title)).setText(TITLE);
		TextView viewRight = (TextView) findViewById(R.id.tv_top_right);
		viewRight.setVisibility(View.VISIBLE);
		viewRight.setText("提交");

		if (getIntent() != null) {
			mDoctorId = getIntent().getStringExtra("doctorId");
			mDoctorName = getIntent().getStringExtra("doctorName");
			mDoctorPortrait = getIntent().getStringExtra("portrait");
			mQuestionId = getIntent().getStringExtra("questionId");
			mIsFollow = getIntent().getBooleanExtra("isFollow", false);
		}

		mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
		et_comment = (EditText) findViewById(R.id.et_comment);
	}

	public void requestSaveEvaluation() {
		String starNum = String.valueOf(mRatingBar.getRating() * 2);
		String comment = et_comment.getText().toString();
		
		if (TextUtils.isEmpty(comment)) {
			showToast("说点什么吧!");
			return;
		}
		
		if (!NetWorkUtils.detect(this)) {
			showToast("请检查网络设置");
			return;
		}
		loading(null);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
		params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("doctorid", mDoctorId);
		params.put("evaluation", starNum);
		params.put("evaluation_content", comment);
		params.put("question_id", mQuestionId);
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_evaluation,
				EvaluationEntity.class, params, new Response.Listener() {

					@Override
					public void onResponse(Object arg0) {
						destroyDialog();
						EvaluationEntity entity = (EvaluationEntity) arg0;
						if (entity == null || !entity.getSuccess().equals("1"))
							return;
						Intent intent = new Intent(EvaluateServiceActivity.this,
								EvaluateResultActivity.class);
						intent.putExtra("portrait", mDoctorPortrait);
						intent.putExtra("doctorId", mDoctorId);
						intent.putExtra("name", mDoctorName);
						intent.putExtra("isFollow", mIsFollow);
						startActivity(intent);
						setResult(RESULT_OK);
						finish();
					}
				}, new Response.ErrorListener() {

					@Override
					public void onErrorResponse(VolleyError arg0) {
						showToast("数据异常");
						destroyDialog();
					}
				});
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_top_right:
			requestSaveEvaluation();
			break;
		default:
			break;
		}
	}
}
