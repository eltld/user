package net.ememed.user2.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.GuahaoDoctor;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.ScheduleDetailEntry;
import net.ememed.user2.entity.ScheduleDetailInfo;
import net.ememed.user2.entity.ScheduleItem;
import net.ememed.user2.entity.ScheduleTime;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class YudingActivity extends BasicActivity {

	private ScheduleTime time;
	private GuahaoDoctotItem doctorinfo;
	private ListView lv_yuding_time;
	private PayAdapter adapter;
	private List<ScheduleDetailEntry> data;
	private String date;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_doctor_yuding);
		EventBus.getDefault().register(this,GuahaoSuccessEvent.class); 
	}
	public void onEvent(GuahaoSuccessEvent event) {
		 finish();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().removeStickyEvent(GuahaoSuccessEvent.class);
		super.onDestroy();
	}
	@Override
	protected void setupView() {
		time = (ScheduleTime) getIntent().getSerializableExtra("Schedule_Time");
		date = getIntent().getStringExtra("date");
		doctorinfo = (GuahaoDoctotItem) getIntent().getSerializableExtra("Doctot_Item");
		if(time==null){
			List<ScheduleItem> items = (List<ScheduleItem>) getIntent().getSerializableExtra("times");
			data = new ArrayList<ScheduleDetailEntry>();
			for (ScheduleItem item : items) {
				ScheduleDetailEntry entry = new ScheduleDetailEntry();
				entry.setFee(item.getPay_fee());
				entry.setAppoint_Period(item.getAppointmentime());
				entry.setResource_Code(item.getAppointmentID());
				data.add(entry);
			}
		}
		
		TextView tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		TextView tv_zhiwu = (TextView) findViewById(R.id.tv_zhiwu);
		TextView tv_shangchang = (TextView) findViewById(R.id.tv_shangchang);
		tv_doctor_name.setText(doctorinfo.getDOCTORNAME());
		tv_zhiwu.setText("医院："+doctorinfo.getHOSPITALNAME());
		tv_shangchang.setText("科室："+doctorinfo.getROOMNAME());
		lv_yuding_time = (ListView) findViewById(R.id.lv_yuding_time);
		adapter = new PayAdapter(data);
		lv_yuding_time.setAdapter(adapter);
		lv_yuding_time.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ScheduleDetailEntry scheduleDetailEntry = data.get(position);
				if(TextUtils.isEmpty(scheduleDetailEntry.getSchedule_Date()))
					scheduleDetailEntry.setSchedule_Date(date);
				Intent intent = null;
				if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN)) {// 已登录
					intent = new Intent(YudingActivity.this, VisitPersonChoiceActivity2.class);
				} else {
					intent = new Intent(YudingActivity.this, LoginActivity.class);
				}
				intent.putExtra("schedule_detail", scheduleDetailEntry);
				intent.putExtra("guahao_doctot", doctorinfo);
				startActivity(intent);

			}
		});
		CircleImageView doctor_head_portrait = (CircleImageView) findViewById(R.id.doctor_head_portrait);
		imageLoader.displayImage(doctorinfo.getAVATAR(), doctor_head_portrait, Util.getOptions_default_portrait());
		
		if(time!=null)
			getDoctorScheduleDetail(time.getSchedule(), doctorinfo.getDOCFROM());
		else{
			
		}
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_close:
			finish();
			break;

		default:
			break;
		}

	}

	private void getDoctorScheduleDetail(String resourcecode, String channel) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("resourcecode", resourcecode);
			params.put("guahao_channel", channel);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_doctor_schedule_detail, ScheduleDetailInfo.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_DOCTOR_SCHEDULE_DETAIL;
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
		case IResult.GET_DOCTOR_SCHEDULE_DETAIL:
			ScheduleDetailInfo info = (ScheduleDetailInfo) msg.obj;
			if (info != null) {
				switch (info.getSuccess()) {
				case 1:
					data = info.getData();
					if (data != null && data.size() > 0)
						adapter.change(data);
					else {
						showToast("该号源已被抢光");
						finish();
					}
					break;
				case 0:
					showToast(info.getErrormsg());
				default:
					break;
				}
			} else {
				showToast("该号源已被抢光");
				finish();
			}
			break;

		case IResult.DATA_ERROR:
			showToast("该号源已被抢光");
			finish();
		default:
			break;
		}
	}

	class PayAdapter extends BaseAdapter {
		List<ScheduleDetailEntry> list;

		public PayAdapter(List<ScheduleDetailEntry> list) {
			if (list == null) {
				list = new ArrayList<ScheduleDetailEntry>();
			}
			this.list = list;
		}

		public void change(List<ScheduleDetailEntry> list) {
			if (list == null) {
				list = new ArrayList<ScheduleDetailEntry>();
			}
			this.list = list;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder = null;
			if (convertView == null) {
				holder = new HolderView();
				convertView = View.inflate(YudingActivity.this, R.layout.item_guahao_yuding, null);
				holder.tv_guahao_time = (TextView) convertView.findViewById(R.id.tv_guahao_time);
				holder.tv_guahao_pay = (TextView) convertView.findViewById(R.id.tv_guahao_pay);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			ScheduleDetailEntry scheduleDetailEntry = list.get(position);
			holder.tv_guahao_pay.setText(scheduleDetailEntry.getFee() + "元");
			holder.tv_guahao_time.setText(scheduleDetailEntry.getAppoint_Period());
			return convertView;
		}

	}

	class HolderView {

		public TextView tv_guahao_pay;
		public TextView tv_guahao_time;

	}
}
