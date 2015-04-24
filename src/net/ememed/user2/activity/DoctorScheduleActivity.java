package net.ememed.user2.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.HospitalRoomInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.ScheduleEntry;
import net.ememed.user2.entity.ScheduleInfo;
import net.ememed.user2.entity.ScheduleItem;
import net.ememed.user2.entity.ScheduleTime;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.NetWorkUtils;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DoctorScheduleActivity extends BasicActivity {

	private GuahaoDoctotItem doctot_Item;
	private ListView lv0;
	private ListView lv1;
	private ListView lv2;
	private ListView lv3;
	private ListView lv4;
	private ListView lv5;
	private ListView lv6;
	private PaibanAdapter pba0;
	private PaibanAdapter pba1;
	private PaibanAdapter pba2;
	private PaibanAdapter pba3;
	private PaibanAdapter pba4;
	private PaibanAdapter pba5;
	private PaibanAdapter pba6;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_doctor_schedule);
	}

	@Override
	protected void setupView() {
		TextView title = (TextView) findViewById(R.id.top_title);
		title.setText("选择就诊时间");
		doctot_Item = (GuahaoDoctotItem) getIntent().getSerializableExtra("Doctot_Item");

		TextView tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		TextView tv_doctor_hosp = (TextView) findViewById(R.id.tv_doctor_hosp);
		TextView tv_doctor_room = (TextView) findViewById(R.id.tv_doctor_room);
		tv_doctor_name.setText(doctot_Item.getDOCTORNAME());
		tv_doctor_hosp.setText(doctot_Item.getHOSPITALNAME());
		tv_doctor_room.setText(doctot_Item.getROOMNAME());

		lv0 = (ListView) findViewById(R.id.lv0);
		lv1 = (ListView) findViewById(R.id.lv1);
		lv2 = (ListView) findViewById(R.id.lv2);
		lv3 = (ListView) findViewById(R.id.lv3);
		lv4 = (ListView) findViewById(R.id.lv4);
		lv5 = (ListView) findViewById(R.id.lv5);
		lv6 = (ListView) findViewById(R.id.lv6);
		pba0 = new PaibanAdapter(null);
		pba1 = new PaibanAdapter(null);
		pba2 = new PaibanAdapter(null);
		pba3 = new PaibanAdapter(null);
		pba4 = new PaibanAdapter(null);
		pba5 = new PaibanAdapter(null);
		pba6 = new PaibanAdapter(null);
		lv0.setAdapter(pba0);
		lv1.setAdapter(pba1);
		lv2.setAdapter(pba2);
		lv3.setAdapter(pba3);
		lv4.setAdapter(pba4);
		lv5.setAdapter(pba5);
		lv6.setAdapter(pba6);

		getDoctorSchedule(doctot_Item.getDOCTORID());
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;

		default:
			break;
		}
	}

	private void getDoctorSchedule(String doctorid) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("doctorid", doctorid);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_doctor_schedule, ScheduleInfo.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_DOCTOR_SCHEDULE;
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
		case IResult.GET_DOCTOR_SCHEDULE:
			ScheduleInfo info = (ScheduleInfo) msg.obj;
			if (info != null) {
				switch (info.getSuccess()) {
				case 1:
					List<ScheduleEntry> data = info.getData();

					pba0.change(data.get(0));
					pba1.change(data.get(1));
					pba2.change(data.get(2));
					pba3.change(data.get(3));
					pba4.change(data.get(4));
					pba5.change(data.get(5));
					pba6.change(data.get(6));

					pba0.setDate(data.get(0).getDatekey());
					pba1.setDate(data.get(1).getDatekey());
					pba2.setDate(data.get(2).getDatekey());
					pba3.setDate(data.get(3).getDatekey());
					pba4.setDate(data.get(4).getDatekey());
					pba5.setDate(data.get(5).getDatekey());
					pba6.setDate(data.get(6).getDatekey());
					break;

				default:
					break;
				}
			} else {

			}
			break;
		case IResult.DATA_ERROR:

			break;

		default:
			break;
		}
	}

	class PaibanAdapter extends BaseAdapter implements OnClickListener {
		private ScheduleEntry entry;
		private String date;

		PaibanAdapter(ScheduleEntry entry) {
			this.entry = entry;

		}

		public void setDate(String date) {
			String lian = date.substring(0, 4);
			String yue = date.substring(4, 6);
			String ri = date.substring(6, date.length());
			this.date = lian + "-" + yue + "-" + ri;
		}

		public void change(ScheduleEntry entry) {
			this.entry = entry;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return 4;
		}

		@Override
		public Object getItem(int position) {
			return entry;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv_number = null;
			if (convertView == null) {
				convertView = View.inflate(DoctorScheduleActivity.this, R.layout.item_guahao_num, null);
				tv_number = (TextView) convertView.findViewById(R.id.tv_number);
				convertView.setTag(tv_number);
			} else {
				tv_number = (TextView) convertView.getTag();
			}
			switch (position) {
			case 0:
				tv_number.setBackgroundColor(Color.parseColor("#F5F5F5"));
				break;
			case 1:
				tv_number.setBackgroundColor(Color.WHITE);
				break;
			case 2:
				tv_number.setBackgroundColor(Color.WHITE);
				break;
			case 3:
				tv_number.setBackgroundColor(Color.WHITE);
				break;

			default:
				break;
			}
			if (entry != null) {
				switch (position) {
				case 0:
					String date = entry.getDate();
					String replace = date.replace(" ", "\n");
					replace = replace.replace("星期", "周");
					replace = replace.replace("-", "/");
					tv_number.setText(replace);
					tv_number.setTextColor(Color.BLACK);
					tv_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);

					break;
				case 1:
					tv_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					List<ScheduleItem> am = entry.getAm();
					if (am != null && am.size() > 0) {
						Holder holder = new Holder();
						holder.list = (LinkedList<ScheduleItem>) am;
						holder.date = this.date;
						tv_number.setText(am.size()+"");
						tv_number.setBackgroundColor(Color.parseColor("#00a9eb"));
						tv_number.setTag(holder);
						tv_number.setOnClickListener(this);

					} else {
						tv_number.setOnClickListener(null);
					}
					break;
				case 2:
					tv_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					List<ScheduleItem> pm = entry.getPm();
					if (pm != null && pm.size() > 0) {
						Holder holder = new Holder();
						holder.list = (LinkedList<ScheduleItem>) pm;
						holder.date = this.date;
						tv_number.setText(pm.size()+"");
						tv_number.setBackgroundColor(Color.parseColor("#00a9eb"));
						tv_number.setTag(holder);
						tv_number.setOnClickListener(this);

					} else {
						tv_number.setOnClickListener(null);
					}
					break;
				case 3:
					tv_number.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
					List<ScheduleItem> Night = entry.getNight();
					if (Night != null && Night.size() > 0) {
						Holder holder = new Holder();
						holder.list = (LinkedList<ScheduleItem>) Night;
						holder.date = this.date;
						tv_number.setText(Night.size()+"");
						tv_number.setBackgroundColor(Color.parseColor("#00a9eb"));
						tv_number.setTag(holder);
						tv_number.setOnClickListener(this);

					} else {
						tv_number.setOnClickListener(null);
					}
					break;

				default:
					break;
				}
			}

			return convertView;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent i = new Intent(DoctorScheduleActivity.this, YudingActivity.class);
			Holder holder = (Holder) v.getTag();
			i.putExtra("Doctot_Item", doctot_Item);
			i.putExtra("times", holder.list);
			i.putExtra("date", holder.date);
			startActivity(i);

		}

	}

	static class Holder {
		LinkedList<ScheduleItem> list;
		String date;
	}

}
