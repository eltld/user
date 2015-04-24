package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.LoginSuccessEvent;
import de.greenrobot.event.RegisterSuccessEvent;
import de.greenrobot.event.VisitPersonAddEvent;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.CuredmemeberList;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.CuredmemeberList;
import net.ememed.user2.entity.CuredmemeberListEntry;
import net.ememed.user2.entity.ScheduleDetailEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

/**
 * 选择就诊人
 * 
 * @author hjk
 * 
 */
public class VisitPersonChoiceActivity extends BasicActivity implements OnClickListener {

	private static final String UPDATE = "1";
	private static final String ADD = "2";
	private TextView person_name1;
	private TextView person_name2;
	private TextView person_name3;
	private TextView person_name4;
	private TextView person_name5;
	private List<CuredmemeberListEntry> data;
	private List<ImageView> iv_list = new ArrayList<ImageView>();
	private List<TextView> list_name = new ArrayList<TextView>();
//	private List<LinearLayout> list_ll = new ArrayList<LinearLayout>();
	private GuahaoDoctotItem guahao_doctot;
	private ScheduleDetailEntry scheduleDetailEntry;
	private int[] id = { R.id.iv_visit_person_name_0, R.id.iv_visit_person_name_1, R.id.iv_visit_person_name_2, R.id.iv_visit_person_name_3, R.id.iv_visit_person_name_4 };
	private LinearLayout btn_add_person;
	private boolean isflag;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_visit_person_choice);
		EventBus.getDefault().registerSticky(this, VisitPersonAddEvent.class);
		scheduleDetailEntry = (ScheduleDetailEntry) getIntent().getSerializableExtra("schedule_detail");
		guahao_doctot = (GuahaoDoctotItem) getIntent().getSerializableExtra("guahao_doctot");
		isflag = getIntent().getBooleanExtra("isflag", true);
		EventBus.getDefault().register(this,GuahaoSuccessEvent.class); 
	}

	public void onEvent(GuahaoSuccessEvent event) {
		 finish();
	}

	
	@Override
	protected void getData() {
		getCuredmemeberList();
		super.getData();
	}

	@Override
	protected void setupView() {
		TextView title = (TextView) findViewById(R.id.top_title);
		title.setText("选择就诊人");
//		LinearLayout ll_visit_person_0 = (LinearLayout) findViewById(R.id.ll_visit_person_0);
//		LinearLayout ll_visit_person_1 = (LinearLayout) findViewById(R.id.ll_visit_person_1);
//		LinearLayout ll_visit_person_2 = (LinearLayout) findViewById(R.id.ll_visit_person_2);
//		LinearLayout ll_visit_person_3 = (LinearLayout) findViewById(R.id.ll_visit_person_3);
//		LinearLayout ll_visit_person_4 = (LinearLayout) findViewById(R.id.ll_visit_person_4);
//		list_ll.add(ll_visit_person_0);
//		list_ll.add(ll_visit_person_1);
//		list_ll.add(ll_visit_person_2);
//		list_ll.add(ll_visit_person_3);
//		list_ll.add(ll_visit_person_4);
	

		btn_add_person = (LinearLayout) findViewById(R.id.btn_add_person);
		
		person_name1 = (TextView) findViewById(R.id.tv_visit_person_name_0);
		person_name2 = (TextView) findViewById(R.id.tv_visit_person_name_1);
		person_name3 = (TextView) findViewById(R.id.tv_visit_person_name_2);
		person_name4 = (TextView) findViewById(R.id.tv_visit_person_name_3);
		person_name5 = (TextView) findViewById(R.id.tv_visit_person_name_4);
		list_name.add(person_name1);
		list_name.add(person_name2);
		list_name.add(person_name3);
		list_name.add(person_name4);
		list_name.add(person_name5);
		ImageView iv_visit_person_name_0 = (ImageView) findViewById(R.id.iv_visit_person_name_0);
		ImageView iv_visit_person_name_1 = (ImageView) findViewById(R.id.iv_visit_person_name_1);
		ImageView iv_visit_person_name_2 = (ImageView) findViewById(R.id.iv_visit_person_name_2);
		ImageView iv_visit_person_name_3 = (ImageView) findViewById(R.id.iv_visit_person_name_3);
		ImageView iv_visit_person_name_4 = (ImageView) findViewById(R.id.iv_visit_person_name_4);
		iv_list.add(iv_visit_person_name_0);
		iv_list.add(iv_visit_person_name_1);
		iv_list.add(iv_visit_person_name_2);
		iv_list.add(iv_visit_person_name_3);
		iv_list.add(iv_visit_person_name_4);
		for (ImageView iv : iv_list) {
			iv.setOnClickListener(this);
		}
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.btn_add_person:// 新增就诊人；
//			getPersonInfo(id);// 调转到修改就诊卡信息
			Intent intent = new Intent(this, VisitPersonAddActivity.class);
			intent.putExtra("add", ADD);
			intent.putExtra("guahao_doctot", guahao_doctot);
			intent.putExtra("schedule_detail", scheduleDetailEntry);
			startActivity(intent);
			break;
		default:
			break;
		}
	}

	private void getPersonInfo(int id) {
		Intent intent = new Intent(this, VisitPersonEditActivity.class);
		if (id == R.id.iv_visit_person_name_0) {
			intent.putExtra("data", data.get(0));
		} else if (id == R.id.iv_visit_person_name_1) {
			intent.putExtra("data", data.get(1));
		} else if (id == R.id.iv_visit_person_name_2) {
			intent.putExtra("data", data.get(2));
		} else if (id == R.id.iv_visit_person_name_3) {
			intent.putExtra("data", data.get(3));
		} else if (id == R.id.iv_visit_person_name_4) {
			intent.putExtra("data", data.get(4));
		} 
		intent.putExtra("guahao_doctot", guahao_doctot);
		intent.putExtra("schedule_detail", scheduleDetailEntry);
		startActivity(intent);
	}

	private void getStartActivity(int id) {
		if(!isflag)
			return;
		Intent intent = new Intent(this, ReserveInfoActivity.class);
		if (id == R.id.tv_visit_person_name_0) {
			intent.putExtra("data", data.get(0));
		} else if (id == R.id.tv_visit_person_name_1) {
			intent.putExtra("data", data.get(1));
		} else if (id == R.id.tv_visit_person_name_2) {
			intent.putExtra("data", data.get(2));
		} else if (id == R.id.tv_visit_person_name_3) {
			intent.putExtra("data", data.get(3));
		} else if (id == R.id.tv_visit_person_name_4) {
			intent.putExtra("data", data.get(4));
		}
		intent.putExtra("guahao_doctot", guahao_doctot);
		intent.putExtra("schedule_detail", scheduleDetailEntry);
		startActivity(intent);
	}

	private void getCuredmemeberList() {
		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_curedmember_lists, CuredmemeberList.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.PATIENT_LIST;
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
		try {
			switch (msg.what) {
			case IResult.PATIENT_LIST:
				CuredmemeberList curedmemeberList = (CuredmemeberList) msg.obj;
				if (null != curedmemeberList) {
					if (curedmemeberList.getSuccess() == 1) {
						data = curedmemeberList.getData();
						if (null != data && data.size() > 0) {
							for (int i = 0; i < data.size() && i < 5; i++) {
								iv_list.get(i).setVisibility(View.VISIBLE);
								list_name.get(i).setText(data.get(i).getREALNAME());
//								if(isflag)
								list_name.get(i).setOnClickListener(this);
							}
							if(data.size()==5){
								btn_add_person.setClickable(false);
							}
							
						}
					} else {
						showToast(curedmemeberList.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				break;
			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResult(msg);
	}

	public void onEvent(VisitPersonAddEvent testEvent) {
		// Logger.dout("register success");
		// finish();
		getCuredmemeberList();
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(VisitPersonAddEvent.class);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().removeStickyEvent(GuahaoSuccessEvent.class);
		super.onDestroy();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_curedmember_lists);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.tv_visit_person_name_0:
		case R.id.tv_visit_person_name_1:
		case R.id.tv_visit_person_name_2:
		case R.id.tv_visit_person_name_3:
		case R.id.tv_visit_person_name_4:
				getStartActivity(id);// 跳转到添加就诊卡信息
			break;
		case R.id.iv_visit_person_name_0:
		case R.id.iv_visit_person_name_1:
		case R.id.iv_visit_person_name_2:
		case R.id.iv_visit_person_name_3:
		case R.id.iv_visit_person_name_4:
			getPersonInfo(id);// 调转到修改就诊卡信息
			break;
		default:
			break;
		}
	}
}
