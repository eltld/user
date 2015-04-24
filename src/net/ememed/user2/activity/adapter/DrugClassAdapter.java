package net.ememed.user2.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.entity.DrugClass;

import android.content.Context;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DrugClassAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private List<DrugClass> drugClasses;

	public DrugClassAdapter() {
	}

	public DrugClassAdapter(Context context, List<DrugClass> drugClasses) {
		if (drugClasses == null) {
			drugClasses = new ArrayList<DrugClass>();
		}
		this.drugClasses = drugClasses;
		this.inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return drugClasses.size();
	}

	@Override
	public Object getItem(int position) {
		return drugClasses.get(position-1);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.u_drug_class_item, null);
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_drug_class_name);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DrugClass drugClass = drugClasses.get(position);
		holder.tvTitle.setText(drugClass.getTitle());
		return convertView;
	}

	class ViewHolder {
		TextView tvTitle;
	}

	public void change(List<DrugClass> drugClasses) {
		if(drugClasses==null){
			drugClasses = new ArrayList<DrugClass>();
		}
		this.drugClasses = drugClasses;
		notifyDataSetChanged();
		
	}

}
