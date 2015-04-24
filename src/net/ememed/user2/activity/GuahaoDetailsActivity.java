package net.ememed.user2.activity;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.GuahaoEvent;
import de.greenrobot.event.LoginSuccessEvent;
import de.greenrobot.event.RegisterSuccessEvent;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.CancelGuahao;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.UserGuahaoEntry;
import net.ememed.user2.entity.UserGuahaoInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
/**
 * 预约详情
 * @author pengwl
 *
 */
public class GuahaoDetailsActivity extends BasicActivity {

	private UserGuahaoEntry userGuahaoEntry;
	private TextView tv_state;
	private TextView tv_number;
	private TextView tv_hosp;
	private TextView tv_room;
	private TextView tv_doctor_name;
	private TextView tv_time;
	private TextView tv_address;
	private TextView top_title;
	private Button btn_of_guahao;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.guahao_details);
		userGuahaoEntry = (UserGuahaoEntry) getIntent().getSerializableExtra(
				"hosp_entry");
		EventBus.getDefault().registerSticky(this, GuahaoEvent.class);
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("预约详情");
		tv_state = (TextView) findViewById(R.id.tv_state);
		tv_number = (TextView) findViewById(R.id.tv_number);
		tv_hosp = (TextView) findViewById(R.id.tv_hosp);
		tv_room = (TextView) findViewById(R.id.tv_room);
		tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_address = (TextView) findViewById(R.id.tv_address);
		btn_of_guahao = (Button) findViewById(R.id.btn_of_guahao);

		tv_state.setText(userGuahaoEntry.getSTATE());
		tv_number.setText(userGuahaoEntry.getORDERNO());
		tv_hosp.setText(userGuahaoEntry.getHOSP());
		 tv_room.setText(userGuahaoEntry.getDEPARTMEN());
		tv_doctor_name.setText(userGuahaoEntry.getDOCTORNAME());
		tv_time.setText(userGuahaoEntry.getTIME());
		// tv_address.setText(userGuahaoEntry.getSTATE());
		if(userGuahaoEntry.getSTATUS().equals("-1")){
			btn_of_guahao.setText("已取消预约");
			btn_of_guahao.setClickable(false);
			btn_of_guahao.setBackgroundResource(R.drawable.time_gray_bg);
		}
		super.setupView();
	}

	@Override
	protected void addListener() {
		// TODO Auto-generated method stub
		super.addListener();
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_of_guahao:
			showMessage();
			break;
		case R.id.ll_phone:
			Uri uri = Uri.parse("tel:4009003333");   
			Intent intent = new Intent(Intent.ACTION_DIAL, uri);     
			startActivity(intent);  
			break;
		default:
			break;
		}
	}
	/**取消预约挂号*/
	private void cancelGuahao() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("orderno", userGuahaoEntry.getORDERNO());
			params.put("serviceid", userGuahaoEntry.getSERVICEID());
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.cancel_guahao, CancelGuahao.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.CANCEL_GUAHAO;
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
		destroyDialog();
		switch (msg.what) {
		case IResult.CANCEL_GUAHAO:
			CancelGuahao cancelGuahao = (CancelGuahao) msg.obj;
			if(cancelGuahao!=null){
				switch (cancelGuahao.getSuccess()) {
				case 1:
					EventBus.getDefault().postSticky(new GuahaoEvent());
					finish();
					break;
				case 0:
					showToast(cancelGuahao.getErrormsg());
					break;
				default:
					showToast(cancelGuahao.getErrormsg());
					break;
				}
			}
			break;
		case IResult.NET_ERROR:
			 showToast(IMessage.NET_ERROR);
			break;
		case IResult.DATA_ERROR:
			 showToast(IMessage.DATA_ERROR);
			break;

		default:
			break;
		}
	}
	
	/** 信息提示 */
	private boolean finish = false;

	public void showMessage() {
		Builder builder = new Builder(this);
		Dialog dialog = builder
				.setTitle("取消提醒")
				.setMessage("每周取消3次将被列入黑名单，确认取消？")
				.setNegativeButton("取消", null)
				.setPositiveButton("确定",
						new OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								// TODO Auto-generated method stub
								cancelGuahao();
							}
						}).create();
		dialog.setCancelable(false);
		if (!finish) {
			dialog.show();
		}
	}
	public void onEvent(GuahaoEvent testEvent) {
		Logger.dout("register success");
		finish();
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(GuahaoEvent.class);
	}
	
}
