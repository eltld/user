package net.ememed.user2.medicalhistory.adapter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class ListViewAdapter2 extends BaseAdapter {
	private ArrayList<ArrayList<String>> mList;
	private ArrayList<String> listItems;
	private ArrayList<String> times;
	private Context mContext;
	// 声明时间和变量
	private int mYear;
	private int mMonth;
	private int mDay;
	private int t = 1;

	public ListViewAdapter2(ArrayList<ArrayList<String>> mList,
			ArrayList<String> listItems, ArrayList<String> times,
			Context mContext) {
		super();
		this.mList = mList;
		this.listItems = listItems;
		this.times = times;
		this.mContext = mContext;
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		} else {
			return this.mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (mList == null) {
			return null;
		} else {
			return this.mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(this.mContext).inflate(
					R.layout.listview_item, null, false);
			holder.editView = (EditText) convertView
					.findViewById(R.id.bingli_add_text);
			holder.time = (TextView) convertView
					.findViewById(R.id.bingli_add_text1);
			holder.gridView = (GridView) convertView
					.findViewById(R.id.listview_item_gridview);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		if (this.mList != null) {
			if (holder.editView != null) {
				holder.editView.setText(listItems.get(position));
			}
			if (holder.gridView != null) {
				GridViewAdapter gridViewAdapter = new GridViewAdapter(mContext,
						this.mList.get(position));
				holder.gridView.setAdapter(gridViewAdapter);
			}
		}
		
		Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		if(t == 1){
			holder.time.setText(new StringBuilder().append(mYear).append("-")
					.append(format(mMonth + 1)).append("-")
					.append(format(mDay)));
		} else {
			holder.time.setText(times.get(position));
		}
		return convertView;
	}
	
	private String format(int x){
		String s = "" + x;
		if(s.length() == 1){
			s = "0" + s;
		}
		return s;
	}

	private class ViewHolder {
		TextView time;
		EditText editView;
		GridView gridView;
	}
	
	public void change(ArrayList<ArrayList<String>> mList, ArrayList<String> listItems) {
		if (mList == null) {
			mList = new ArrayList<ArrayList<String>>();
		}
		if (listItems == null) {
			listItems = new ArrayList<String>();
		}
		
		this.listItems = listItems;
		this.mList = mList;
		notifyDataSetChanged();
	}
	
	public void change(ArrayList<ArrayList<String>> mList, ArrayList<String> listItems, ArrayList<String> times, int t) {
		this.t = t;
		if (mList == null) {
			mList = new ArrayList<ArrayList<String>>();
		}
		if (listItems == null) {
			listItems = new ArrayList<String>();
		}
		if (times == null) {
			times = new ArrayList<String>();
		}
		this.mList = mList;
		this.listItems = listItems;
		this.times = times;
		notifyDataSetChanged();
	}

	public void add(ArrayList<ArrayList<String>> mList, ArrayList<String> listItems) {
		this.listItems.addAll(listItems);
		if (!(mList == null) && !mList.equals("")) {
			this.mList.addAll(mList);
		}
		notifyDataSetChanged();
	}
	
	
}
