package net.ememed.user2.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.KeshiActivity;
import net.ememed.user2.entity.RoomEntry;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class SortAdapter extends BaseAdapter {
	private List<RoomEntry> list;
	private BasicActivity activity;
	private boolean flag;

	public SortAdapter(BasicActivity activity, List<RoomEntry> list,boolean flag) {
		if (list == null) {
			list = new ArrayList<RoomEntry>();
		}
		this.list = list;
		this.activity = activity;
		this.flag = flag;
	}

	/**
	 * 当ListView数据发生变化时,调用此方法来更新ListView
	 * 
	 * @param list
	 */
	public void updateListView(List<RoomEntry> list) {
		if (list == null) {
			list = new ArrayList<RoomEntry>();
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
	public View getView(final int position, View view, ViewGroup arg2) {
		ViewHolder viewHolder = null;
		final RoomEntry mContent = list.get(position);
		if (view == null) {
			viewHolder = new ViewHolder();
			view = View.inflate(activity, R.layout.room_item, null);
			viewHolder.tvTitle = (TextView) view.findViewById(R.id.title);
			viewHolder.tvLetter = (TextView) view.findViewById(R.id.catalog);
			viewHolder.tvScheduleYes = (TextView) view.findViewById(R.id.tv_schedule_yes);
			viewHolder.tvScheduleNo = (TextView) view.findViewById(R.id.tv_schedule_no);
			//viewHolder.tvPatientNumTitle = (TextView) view.findViewById(R.id.tv_patient_num_title);
			//viewHolder.tvPatientNum = (TextView) view.findViewById(R.id.tv_patient_num);
			view.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) view.getTag();
		}
		Log.i("SortAdapter", mContent.getROOMNAME());
		// 根据position获取分类的首字母的Char ascii值
		int section = getSectionForPosition(position);

		if(flag){
			viewHolder.tvLetter.setVisibility(View.GONE);
		}else{
			// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
			if (position == getPositionForSection(section)) {
				viewHolder.tvLetter.setVisibility(View.VISIBLE);
				viewHolder.tvLetter.setText(mContent.getSortLetters());
			} else {
				viewHolder.tvLetter.setVisibility(View.GONE);
			}
		}
		viewHolder.tvTitle.setText(mContent.getROOMNAME());
		//viewHolder.tvPatientNum.setText(mContent.getTreatmentCount());
		int scheduleState = Integer.parseInt(mContent.getHAS_SCHEDULE());
		if(1 == scheduleState){
			//viewHolder.tvScheduleYes.setVisibility(View.VISIBLE);
			viewHolder.tvScheduleNo.setVisibility(View.GONE);
		} else {
			//viewHolder.tvScheduleYes.setVisibility(View.GONE);
			viewHolder.tvScheduleNo.setVisibility(View.VISIBLE);
		}
		
		return view;

	}

	/**
	 * 根据ListView的当前位置获取分类的首字母的Char ascii值
	 */
	public int getSectionForPosition(int position) {
		return list.get(position).getSortLetters().charAt(0);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * 提取英文的首字母，非英文字母用#代替。
	 * 
	 * @param str
	 * @return
	 */
	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}

	final static class ViewHolder {
		TextView tvLetter;
		TextView tvTitle;
		TextView tvScheduleYes;
		TextView tvScheduleNo;
		//TextView tvPatientNumTitle;
		//TextView tvPatientNum;
	}

}
