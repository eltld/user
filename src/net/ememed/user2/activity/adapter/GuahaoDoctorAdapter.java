package net.ememed.user2.activity.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.DoctorScheduleActivity;
import net.ememed.user2.activity.YudingActivity;
import net.ememed.user2.entity.GuahaoDoctorInfo;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.ScheduleTime;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.Util;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class GuahaoDoctorAdapter extends BaseAdapter implements OnItemClickListener {
	public final static int hosp_room = 1;
	public final static int zhiwu_shanchang = 2;
	BasicActivity activity;
	List<GuahaoDoctorInfo> list;
	int type;

	public GuahaoDoctorAdapter(BasicActivity activity, List<GuahaoDoctorInfo> list, int type) {
		this.activity = activity;
		if (list == null) {
			list = new ArrayList<GuahaoDoctorInfo>();
		}
		this.list = list;
		this.type = type;
	}

	public void change(List<GuahaoDoctorInfo> list) {
		if (list == null) {
			list = new ArrayList<GuahaoDoctorInfo>();
		}
		this.list = list;
		notifyDataSetChanged();
	}

	public void add(List<GuahaoDoctorInfo> list) {
		if (list == null) {
			list = new ArrayList<GuahaoDoctorInfo>();
		}
		this.list.addAll(list);
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
			convertView = View.inflate(activity, R.layout.item_guahao_doctor, null);
			holder.doctor_head_portrait = (ImageView) convertView.findViewById(R.id.doctor_head_portrait);
			holder.tv_doctor_name = (TextView) convertView.findViewById(R.id.tv_doctor_name);
			holder.tv_zhiwu = (TextView) convertView.findViewById(R.id.tv_zhiwu);
			holder.tv_shangchang = (TextView) convertView.findViewById(R.id.tv_shangchang);
			holder.gv_times = (GridView) convertView.findViewById(R.id.gv_times);
			holder.gvAdapter = new GVAdapter(activity, null);
			holder.gv_times.setAdapter(holder.gvAdapter);
			holder.time = new ScheduleTime();
			holder.time.setSchedule_desc("查看排班信息");

			convertView.setTag(holder);
		} else {
			holder = (HolderView) convertView.getTag();
		}
		Logger.iout("获取位置position = "+ position);
		GuahaoDoctorInfo info = list.get(position);
		activity.imageLoader.displayImage(info.getDoctor_info().getAVATAR(), holder.doctor_head_portrait, Util.getOptions_default_portrait());
		holder.tv_doctor_name.setText(info.getDoctor_info().getDOCTORNAME());
		if (type == hosp_room) {
			String doctorname = info.getDoctor_info().getDOCTORNAME();
			if (!TextUtils.isEmpty(doctorname))
				holder.tv_shangchang.setText("医院：" + info.getDoctor_info().getHOSPITALNAME());
			String roomname = info.getDoctor_info().getROOMNAME();
			if (!TextUtils.isEmpty(roomname))
				holder.tv_zhiwu.setText("科室：" + roomname);

		} else if (type == zhiwu_shanchang) {
			String professional = info.getDoctor_info().getPROFESSIONAL();
			if (!TextUtils.isEmpty(professional))
				holder.tv_zhiwu.setText("职务：" + professional);
			String speciality = info.getDoctor_info().getSPECIALITY();
			if (!TextUtils.isEmpty(speciality))
				holder.tv_shangchang.setText("擅长：" + speciality);

		}

		List<ScheduleTime> schedules = info.getSchedule();

		if (schedules != null){
			if(!schedules.contains(holder.time)) {
				schedules.add(holder.time);
			}
		}else{
			schedules = new LinkedList<ScheduleTime>();
			schedules.add(holder.time);
		}
		
		if (schedules != null && schedules.size() > 1){
			holder.gv_times.setOnItemClickListener(this);
			schedules.get(schedules.size()-1).setSchedule_desc("查看排班信息");
		}else {
			schedules.get(0).setSchedule_desc("暂无排班信息");
			holder.gv_times.setOnItemClickListener(null);
		}
		
		holder.gvAdapter.setDoctorData(info.getDoctor_info());
		holder.gvAdapter.change(schedules);
		return convertView;
	}

	static class HolderView {

		public ScheduleTime time;
		public GridView gv_times;
		public TextView tv_shangchang;
		public TextView tv_zhiwu;
		public TextView tv_doctor_name;
		public ImageView doctor_head_portrait;
		public GVAdapter gvAdapter;

	}

	class GVAdapter extends BaseAdapter {
		List<ScheduleTime> schedules;
		BasicActivity activity;
		GuahaoDoctotItem doctor_info;

		GVAdapter(BasicActivity activity, List<ScheduleTime> schedules) {
			if (schedules == null) {
				schedules = new ArrayList<ScheduleTime>();
			}
			this.schedules = schedules;
			this.activity = activity;
		}

		public void setDoctorData(GuahaoDoctotItem doctor_info) {
			this.doctor_info = doctor_info;

		}

		public GuahaoDoctotItem getDoctorinfo() {
			return doctor_info;
		}

		@Override
		public int getCount() {
			return schedules.size();
		}

		@Override
		public Object getItem(int position) {
			return schedules.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(activity, R.layout.item_time, null);
			TextView tv_times = (TextView) convertView.findViewById(R.id.tv_times);
			ScheduleTime scheduleTime = schedules.get(position);
			if (schedules.size() == 1) {
				tv_times.setBackgroundResource(R.drawable.time_gray_bg);
			} else {
				if (position == schedules.size() - 1) {
					tv_times.setBackgroundResource(R.drawable.bg_time_blue_effect);
				} else {
					tv_times.setBackgroundResource(R.drawable.bg_time_green_effect);
				}
			}
			tv_times.setText(scheduleTime.getSchedule_desc());
			convertView.setTag(scheduleTime);
			return convertView;
		}

		public void change(List<ScheduleTime> list) {
			if (list == null) {
				list = new ArrayList<ScheduleTime>();
			}
			this.schedules = list;
			notifyDataSetChanged();
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		ScheduleTime Time = (ScheduleTime) parent.getChildAt(position).getTag();
		GVAdapter adapter = (GVAdapter) parent.getAdapter();
		GuahaoDoctotItem doctorinfo = adapter.getDoctorinfo();
		Intent intent = null;
		if (position == adapter.getCount() - 1) {
			intent = new Intent(activity, DoctorScheduleActivity.class);
		} else {
			intent = new Intent(activity, YudingActivity.class);
			intent.putExtra("Schedule_Time", Time);
		}
		intent.putExtra("Doctot_Item", doctorinfo);
		activity.startActivity(intent);
	}
}
