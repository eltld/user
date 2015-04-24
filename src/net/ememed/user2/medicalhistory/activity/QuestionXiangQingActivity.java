package net.ememed.user2.medicalhistory.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.DoctorHistoryOrderActivity;
import net.ememed.user2.activity.ShowImageActivity;
import net.ememed.user2.activity.adapter.ImageGridViewAdapter;
import net.ememed.user2.activity.adapter.MyImageGridViewAdapter;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.medicalhistory.bean.QuestionXQBean;
import net.ememed.user2.medicalhistory.bean.QuestionXQDataBean;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.CircleImageView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

public class QuestionXiangQingActivity extends BasicActivity {

	public TextView question_xiangqing_txt;
	public QuestionXQBean qxqb;
	
	private TextView top_title;
	private ListView pic_list;
	private TextView question_xiangqing_date;
	private List<String> str;
	private Handler mHandler;
	
	
	CircleImageView imageView;
	List<String> list ;
	String msg;
	String question_id;
	String token;
	String userid;
	String avaurl;  //头像

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_question_xiangqing);
		setLoad();
		xiangqin();
	}

	public void setLoad() {
		question_xiangqing_txt = (TextView) findViewById(R.id.question_xiangqing_txt);
		question_id = getIntent().getExtras().getString("question_id");
		top_title = (TextView) findViewById(R.id.search_resource_back)
				.findViewById(R.id.top_title);
		top_title.setText("我的提问");
		pic_list = (ListView) findViewById(R.id.pic_list);
		imageView = (CircleImageView) findViewById(R.id.question_pool_xiangqing_img_head);
		avaurl=SharePrefUtil.getString(Conast.AVATAR);
		if(avaurl!=null){
			imageLoader.displayImage(avaurl,
				imageView);
		}else{
			imageView.setBackgroundResource(R.drawable.avatar_large);
		}
		
		question_xiangqing_date = (TextView) findViewById(R.id.question_xiangqing_date);
//		str = (List<String>) getIntent().getExtras().getSerializable("pic");
		
		
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			startActivity(new Intent(QuestionXiangQingActivity.this,
					DoctorHistoryOrderActivity.class));
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			startActivity(new Intent(QuestionXiangQingActivity.this,
					DoctorHistoryOrderActivity.class));
			finish();
			break;

		default:
			break;
		}

		return super.onKeyDown(keyCode, event);
	}

	public void xiangqin() {

		if (NetWorkUtils.detect(this)) {

			this.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("question_id", question_id);
			Log.e("","token:"+SharePrefUtil.getString(Conast.ACCESS_TOKEN)+"   userid:"+SharePrefUtil.getString(Conast.MEMBER_ID)+"   question_id:"+question_id);
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_my_question_detail, QuestionXQBean.class, params,
					new Listener() {
						public void onResponse(Object object) {
							QuestionXiangQingActivity.this.destroyDialog();
							qxqb=(QuestionXQBean) object;
							
							setData(qxqb.getData());
						}
					}, new ErrorListener() {

						public void onErrorResponse(VolleyError error) {
							QuestionXiangQingActivity.this.destroyDialog();
							QuestionXiangQingActivity.this.showToast(error
									.getMessage());
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});

		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}

	}
	public void setData(QuestionXQDataBean qxadb){
		question_xiangqing_txt.setText(qxadb.getSYMPTOMS());
		question_xiangqing_date.setText(qxadb.getCREATE_TIME());
		
		
		String pics = qxadb.getPICS();
		
		if(pics!=null){
			list = new ArrayList<String>();
			
			try {
				JSONArray array = new JSONArray(pics);
				for (int i = 0; i < array.length(); i++) {
					list.add(array.getString(i));
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		if(list!=null){
			pic_list.setAdapter(new MyImageGridViewAdapter(list, this, imageLoader));
			pic_list.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
						long arg3) {
					
					Intent intent = new Intent(QuestionXiangQingActivity.this, ShowImageActivity.class);
					intent.putExtra("position", arg2);
					List<String> pris = list;
					intent.putExtra("imageUrls", (Serializable)pris);
					QuestionXiangQingActivity.this.startActivity(intent);
				}
			});
		}else{
			pic_list.setVisibility(View.GONE);
		}
	}
}
