package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddSingle;
import net.ememed.user2.entity.FeedbackResultInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.tencent.utils.Util;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public class FeedbackActivity extends BasicActivity{
	
	private TextView title;
	private EditText feedbackEditText;
	private String feedbackContent;
	private Button btn_addhealth;
	private boolean isSend = false;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.layout_feedback);
	}

	@Override
	protected void setupView() {
		super.setupView();
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText(getString(R.string.button_send));
		
		title = (TextView) findViewById(R.id.top_title);
		title.setText("意见反馈");
		feedbackEditText = (EditText) findViewById(R.id.feedback_message);
	}
	
	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		switch(msg.what) {
		case IResult.END:
			operationsAfterReceive();
			FeedbackResultInfo info = (FeedbackResultInfo) msg.obj;
			if (null != info) {
				if(info.getSuccess() == 1) {
					FeedbackActivity.this.showToast("感谢您的反馈，工作人员将及时跟进");
					FeedbackActivity.this.finish();
				} else {
					FeedbackActivity.this.showToast(info.getErrormsg());
					goLogin();
				}
			}
			break;
		case IResult.NET_ERROR :
			operationsAfterReceive();
			this.showToast(IMessage.NET_ERROR);
			break;
		}
	}
	private void goLogin(){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("您还没登录")
		.setMessage("感谢您对我们的吐槽，但请您先登录")
		.setPositiveButton("登录", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent i  = new Intent(FeedbackActivity.this,LoginActivity.class);
				i.putExtra("src", "FeedbackActivity");
				startActivity(i);
			}
		})
		.setNegativeButton("取消", null)
		.show();
	
	}
	
	public void doClick(View v) {
		int id = v.getId();
		if(id == R.id.btn_back) {
			finish();
		} else if (id == R.id.btn_addhealth){
			checkAndSendFeedback();
		}
	}
	
	private void checkAndSendFeedback() {
		feedbackContent = feedbackEditText.getText().toString().trim();
		if(feedbackContent.length() > 200){
			showToast("反馈信息字数达到上限！");
			return;
		}
		if(null == feedbackContent || feedbackContent.equals("")) {
			this.showToast("反馈信息为空，请确认");
		} else {
			if(!isSend) {
				sendFeedbackToServer();
			}
			else {
				showToast("发送中请稍后");
			}
		}
	}
//	private void sendFeedbackToServer2(){
//
//		try {
//			if (NetWorkUtils.detect(this)) {
//				loading(null);
//				new Thread(){
//					public void run() {
//						ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
//						params.add(new BasicNameValuePair("token",SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
//						params.add(new BasicNameValuePair("channel","android"));
//						params.add(new BasicNameValuePair("memberid", SharePrefUtil.getString(Conast.MEMBER_ID)));
//						params.add(new BasicNameValuePair("utype", "user"));
//						params.add(new BasicNameValuePair("content", feedbackContent));
//						
//						String content;
//						try {
//							content = HttpUtil.getString(HttpUtil.URI + HttpUtil.set_feedback,params, HttpUtil.POST);
//							content = TextUtil.substring(content, "{");
//							Gson gson = new Gson();
//							FeedbackResultInfo reason = gson.fromJson(content, FeedbackResultInfo.class);
//							Message msg = Message.obtain();
//							msg.what = IResult.END;
//							msg.obj = reason;
//							handler.sendMessage(msg);
//						} catch (IOException e) {
//							e.printStackTrace();
//							Message message = new Message();
//							message.obj = e.getMessage();
//							message.what = IResult.DATA_ERROR;
//							handler.sendMessage(message);
//						}
//						
//					};
//					
//				}.start();
//			} else {
//				handler.sendEmptyMessage(IResult.NET_ERROR);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	
	private void sendFeedbackToServer() {
		if (NetWorkUtils.detect(this)) {
			operationsBeforeSend();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("content", feedbackContent);
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("utype", "user");
			params.put("channel", "android");
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_feedback,
					FeedbackResultInfo.class ,params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message msg = Message.obtain();
							msg.obj = response;
							msg.what = IResult.END;
							handler.sendMessage(msg);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							operationsAfterReceive();
							FeedbackActivity.this.showToast(error.toString());
						}
					});
		} else {
//			handler.sendEmptyMessage(IResult.NET_ERROR);
			this.showToast(IMessage.NET_ERROR);
		}
	}
	
	private void operationsBeforeSend() {
		isSend = true;
		btn_addhealth.setTextColor(Color.GRAY);
		loading(null);
	}

	
	private void operationsAfterReceive() {
		isSend = false;
		btn_addhealth.setTextColor(Color.WHITE);
		destroyDialog();
	}
}
