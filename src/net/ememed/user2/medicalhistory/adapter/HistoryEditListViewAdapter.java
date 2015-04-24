package net.ememed.user2.medicalhistory.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.medicalhistory.bean.MedicalHistory;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class HistoryEditListViewAdapter extends BaseAdapter {
	private Activity activity;
	private List<String> listItems;
	
	public HistoryEditListViewAdapter(Activity activity, ArrayList<String> listItems) {
		this.activity = activity;
		if (listItems == null) {
			listItems = new ArrayList<String>();
		}
		this.listItems = listItems;
	}
	@Override
	public int getCount() {
		return listItems == null ? 0 : listItems.size();
	}

	@Override
	public Object getItem(int position) {
		if(this.listItems == null ) return null;
		return this.listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.medical_history_edit_adpter, null);
		}
		TextView hospitalName = (TextView) convertView.findViewById(R.id.bingli_add_text);
		hospitalName.setText(listItems.get(position));

		return convertView;
	}
	
	public void change(List<String> lists) {
		if (lists == null) {
			lists = new ArrayList<String>();
		}
		this.listItems = lists;
		notifyDataSetChanged();
	}
	
	public void add(List<String> list) {
		this.listItems.addAll(list);
		notifyDataSetChanged();
	}

}
