package net.ememed.user2.activity;

import java.util.HashMap;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.baike.entity.BaikeFansEntry;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 评价成功
 * 
 * @author pch
 * 
 */
public class EvaluateResultActivity extends BasicActivity {

	private static final String TITLE = "评价成功";
	private String mDoctorId;
	private String mDoctorName;
	private String mDoctorPortrait;
	private Button btn_follow;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_evaluate_result2);
		((TextView) findViewById(R.id.tv_top_title)).setText(TITLE);

		boolean isFollow = getIntent().getBooleanExtra("isFollow", false);
		mDoctorId = getIntent().getStringExtra("doctorId");
		mDoctorPortrait = getIntent().getStringExtra("portrait");
		mDoctorName = getIntent().getStringExtra("name");

		ImageView iv_portrait = (ImageView) findViewById(R.id.iv_portrait);
		TextView tv_name = (TextView) findViewById(R.id.tv_name);
		TextView tv_grade = (TextView) findViewById(R.id.tv_grade);
		btn_follow = (Button) findViewById(R.id.btn_follow);
		tv_name.setText(mDoctorName);
		
		ImageLoader.getInstance().displayImage(mDoctorPortrait, iv_portrait);
		if (isFollow) {
			btn_follow.setText("已关注");
			btn_follow.setEnabled(false);
		}
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_information:
			Intent intent1 = new Intent(this,DoctorBasicInfoActivity.class);
			intent1.putExtra("tochat_userId", mDoctorId);
			intent1.putExtra("doctor_name", mDoctorName);
//			sendBroadcast(intent1);
//			setResult(7);
			startActivity(intent1);
			finish();
			break;
		case R.id.btn_follow:
			followDoctor();
			break;
		default:
			break;
		}
	}

	/** 添加关注 */
	public void followDoctor() {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", mDoctorId);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_fans, BaikeFansEntry.class,
					params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;

							message.what = IResult.BAIKE_ADD_ATTENTION;
							handler.sendMessage(message);

						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							handler.sendMessage(message);
						}
					});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	protected void onResult(Message msg) {
		try {
			switch (msg.what) {
			case IResult.DATA_ERROR:

				break;
			case IResult.NET_ERROR:

				break;
			case IResult.BAIKE_ADD_ATTENTION:
				destroyDialog();
				BaikeFansEntry entry = (BaikeFansEntry) msg.obj;
				if (1 == entry.getSuccess()) {
					showToast("添加关注成功");
					btn_follow.setText("已关注");
					btn_follow.setEnabled(false);
				} else {
					showToast("添加关注失败，请稍后再试");
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
		}
		super.onResult(msg);
	}

}
