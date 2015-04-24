package net.ememed.user2.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.Drug;
import net.ememed.user2.util.Util;
import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchRecordsAdapter extends BaseAdapter {
	private List<String> records;
	private BasicActivity mContext;

	public SearchRecordsAdapter() {
		super();
	}

	public SearchRecordsAdapter(BasicActivity context, ArrayList<String> records) {
		if (records == null) {
			records = new ArrayList<String>();
		}
		
		this.mContext = context;
		this.records = records;
	}

	@Override
	public int getCount() {
		return records.size();
	}

	@Override
	public Object getItem(int position) {
		return records.get(position);
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_search_record, null);
			holder.tvSearchRecord = (TextView) convertView.findViewById(R.id.tv_search_record);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
	
		holder.tvSearchRecord.setText(records.get(position));
		return convertView;
	}



	public void change(ArrayList<String> records) {
		if (records == null) {
			records = new ArrayList<String>();
		}
		this.records = records;
		notifyDataSetChanged();
	}

	private class ViewHolder {
		TextView tvSearchRecord;
	}
}
