package net.ememed.user2.activity.adapter;

import net.ememed.user2.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class ModePopAdapter extends BaseAdapter {
	private LayoutInflater mInflater;
	private String[] mName;

	public ModePopAdapter(Context context, String[] name) {
		mInflater = LayoutInflater.from(context);
		mName = name;
	}

	public int getCount() {
		return mName.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.mode_popupwindow_item,
					null);
			holder = new ViewHolder();
			holder.tv_popupwindow_item = (TextView)convertView.findViewById(R.id.tv_popupwindow_item);
//			holder.icon = (CheckBox) convertView
//					.findViewById(R.id.mode_pop_icon);
//			holder.checked = (ImageView) convertView
//					.findViewById(R.id.mode_pop_checked);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.tv_popupwindow_item.setText(mName[position]);
//		holder.icon.setButtonDrawable(mIcon[position]);
//		holder.icon.setText(mName[position]);
//		if (position == mChooseId) {
//			holder.icon.setChecked(true);
//			holder.checked.setVisibility(View.VISIBLE);
//		} else {
//			holder.icon.setChecked(false);
//			holder.checked.setVisibility(View.INVISIBLE);
//		}
		return convertView;
	}

	class ViewHolder {
		TextView tv_popupwindow_item;
	}
}
