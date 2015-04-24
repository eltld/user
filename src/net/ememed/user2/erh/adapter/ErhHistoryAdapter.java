package net.ememed.user2.erh.adapter;

import java.util.ArrayList;
import java.util.List;

import u.aly.ac;

import net.ememed.user2.R;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.erh.bean.ErhDiseaseHistory;
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

public class ErhHistoryAdapter extends BaseAdapter {
	private Activity activity;
	private List<ErhDiseaseHistory> listItems;
	private int shuzi;
	

	public ErhHistoryAdapter(Activity activity, ArrayList<ErhDiseaseHistory> listItems, int shuzi) {
		this.activity = activity;
		if (listItems == null) {
			listItems = new ArrayList<ErhDiseaseHistory>();
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.erh_history_main_item3, null);
		}
		int position2 = position + 1;
//		if (position2 % 3 == 1) {
//			line.setBackgroundColor(activity.getResources().getColor(R.color.line1));
//		} else if (position2 % 3 == 2) {
//			line.setBackgroundColor(activity.getResources().getColor(R.color.line2));
//		} else if (position2 % 3 == 0){
//			line.setBackgroundColor(activity.getResources().getColor(R.color.line3));
//		} 
		TextView hospitalName = (TextView) convertView.findViewById(R.id.title);
		TextView disease = (TextView) convertView.findViewById(R.id.yulan_jiuzhengshijian);
		TextView disease2 = (TextView) convertView.findViewById(R.id.add6_txt);
		TextView time = (TextView) convertView.findViewById(R.id.yulan_hospital);	
		TextView time2 = (TextView) convertView.findViewById(R.id.add6_txts);
		if (shuzi == 2) {
			disease2.setText("疾病类型");
			time2.setText("确诊时间");
			hospitalName.setText("疾病史" + (position + 1));
		} else if (shuzi == 3) {
			disease2.setText("疾病类型");
			time2.setText("确诊时间");
			hospitalName.setText("手术史" + (position + 1));
		} else if (shuzi == 4) {
			disease2.setText("疾病类型");
			time2.setText("确诊时间");
			hospitalName.setText("外伤史" + (position + 1));
		} else if (shuzi == 5) {
			disease2.setText("疾病类型");
			time2.setText("确诊时间");
			hospitalName.setText("输血史" + (position + 1));
		} else if (shuzi == 6) {
			disease2.setText("家族成员");
			time2.setText("疾病类型");
			hospitalName.setText("家族史" + (position + 1));
		} 
		
		disease.setText(listItems.get(position).getNAME());
		time.setText(listItems.get(position).getEVENTDATE());
		return convertView;
	}

	

	public void change(List<ErhDiseaseHistory> lists) {
		if (lists == null) {
			lists = new ArrayList<ErhDiseaseHistory>();
		}
		this.listItems = lists;
		
		notifyDataSetChanged();
	}

	public void add(List<ErhDiseaseHistory> list) {
		this.listItems.addAll(list);
		notifyDataSetChanged();
	}
	
	public void clear(){
		this.listItems.clear();
	}

	public void setList(List list) {
		this.listItems = list;
	}
	// public ImageUrlBean getImageUrl(){
	//
	// }
}
