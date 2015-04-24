package net.ememed.user2.erh.adapter;

import java.util.ArrayList;
import java.util.List;

import u.aly.ac;

import net.ememed.user2.R;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.erh.bean.ErhDiseaseHistory;
import net.ememed.user2.erh.bean.ErhDiseaseHistory2;
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
import android.widget.ListView;
import android.widget.TextView;

public class ErhHistoryYaoWuAdapter extends BaseAdapter {
	private Activity activity;
	private List<String> listItems;
	private int shuzi;

	public ErhHistoryYaoWuAdapter(Activity activity, ArrayList<String> listItems, int shuzi) {
		this.activity = activity;
		if (listItems == null) {
			listItems = new ArrayList<String>();
		}
		this.listItems = listItems;
		this.shuzi = shuzi;
	}

	@Override
	public int getCount() {
		return listItems == null ? 0 : listItems.size();
	}

	@Override
	public Object getItem(int position) {

		if (this.listItems == null) {
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.erh_history_main_item4, null);
		}
		int position2 = position + 1;
		TextView hospitalName = (TextView) convertView.findViewById(R.id.title);
		TextView disease = (TextView) convertView.findViewById(R.id.yulan_jiuzhengshijian);	
		if (shuzi == 0) {
			hospitalName.setText("过敏类型" + (position + 1));
		} else if (shuzi == 1){
			hospitalName.setText("暴露类型" + (position + 1));
		} else if (shuzi == 4){
			hospitalName.setText("遗传病史" + (position + 1));
		} else if (shuzi == 2){
			hospitalName.setText("障碍史" + (position + 1));
		}
		
		disease.setText(listItems.get(position));
		return convertView;
	}

	

	public void change(List<String> lists) {
		if (lists == null) {
			lists = new ArrayList<String>();
		}
		this.listItems = lists;
		notifyDataSetChanged();
	}

	
	// public ImageUrlBean getImageUrl(){
	//
	// }
}
