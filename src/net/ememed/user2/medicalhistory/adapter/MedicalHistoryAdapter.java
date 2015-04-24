package net.ememed.user2.medicalhistory.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.medicalhistory.bean.ImageUrlBean;
import net.ememed.user2.medicalhistory.bean.MedicalHistory;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MedicalHistoryAdapter extends BaseAdapter {
	private Activity activity;
	private List<MedicalHistory> listItems;
	private List<Integer> picture_ids_list = new ArrayList<Integer>();
	private ImageUrlBean imageUrlBean;
	

	public MedicalHistoryAdapter(Activity activity, ArrayList<MedicalHistory> listItems) {
		this.activity = activity;
		if (listItems == null) {
			listItems = new ArrayList<MedicalHistory>();
		}
		this.listItems = listItems;
	}

	@Override
	public int getCount() {
		return listItems == null ? 0 : listItems.size();
	}

	@Override
	public Object getItem(int position) {

		if (this.listItems == null) {
			imageUrlBean = null;
			return null;
		}
		return this.listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.medical_history_adapter, null);
		}
		TextView hospitalName = (TextView) convertView.findViewById(R.id.medical_history_title);
		TextView keshi = (TextView) convertView.findViewById(R.id.medical_history_keshi);
		TextView time = (TextView) convertView.findViewById(R.id.medical_history_time);
		TextView zhenzhuang = (TextView) convertView.findViewById(R.id.medical_history_zhenzhuang);
		
		LinearLayout ll_image = (LinearLayout) convertView.findViewById(R.id.ll_image);
		LinearLayout ll_third = (LinearLayout) convertView.findViewById(R.id.third);
		if (picture_ids_list.size() != 0) {
			if (picture_ids_list.get(position) != 0) {
				ll_image.setVisibility(View.VISIBLE);
			} else {
				ll_image.setVisibility(View.GONE);
				ll_third.setPadding(0, 0, 0, 10);
			}
		}		
		
		hospitalName.setText(listItems.get(position).getMECHANISM());
		keshi.setText(listItems.get(position).getNAME());
		time.setText(listItems.get(position).getTIME());
		zhenzhuang.setText(listItems.get(position).getCONTENT());
		return convertView;
	}

	

	public void change(List<MedicalHistory> lists) {
		if (lists == null) {
			lists = new ArrayList<MedicalHistory>();
		}
		this.listItems = lists;
		notifyDataSetChanged();
	}

	public void add(List<MedicalHistory> list, List<Integer> picture_ids_list) {
		this.listItems.addAll(list);
		this.picture_ids_list.addAll(picture_ids_list);
		notifyDataSetChanged();
	}
	
	public void clear(){
		this.listItems.clear();
		this.picture_ids_list.clear();
	}

	public void setList(List list) {
		this.listItems = list;
	}
	// public ImageUrlBean getImageUrl(){
	//
	// }
}
