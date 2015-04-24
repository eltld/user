package net.ememed.user2.erh.util;

import net.ememed.user2.R;
import net.ememed.user2.erh.ViewHolder;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

public class SingleDmAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	// 保存勾中的状态,记录CheckBox的状态
	private String choiceTelephones = "";
	private String[] txt;
	private Activity activity;
	private String name;
	private RadioButton rbCheck;
	private int mSelectItem = 0;
	
	public SingleDmAdapter(Activity activity, String[] txt, String name){
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		this.txt = txt;
		this.name = name;
//		init();
	}
	
//	private void init() {
//		for (int i = 0; i < txt.length; i++) {
//			if(txt[i].equals(name)){
//				mSelectItem = i;
//			}
//		}
//	}

	public void setSelectItem(int selectItem) {
		if (selectItem >= 0 && selectItem < txt.length) {
			mSelectItem = selectItem;
			notifyDataSetChanged();
		}

	}

	public int getSelectItem() {
		return mSelectItem;
	}
	
	@Override
	public int getCount() {
		return txt.length;
	}

	@Override
	public Object getItem(int arg0) {
		return txt[arg0];
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.visit_hy_dm_list_singleitem, null);
        } 
		TextView contentTextView = ViewHolder.get(convertView, R.id.iv_txt);
		rbCheck = ViewHolder.get(convertView, R.id.rb_check);
		contentTextView.setText(txt[position]);
//		cbCheck.setTag(txt[position]);
//		if(position == mSelectItem){
//			rbCheck.setChecked(true);
//		} else {
//			rbCheck.setChecked(false);
//		}
		if (name.equals(txt[position])) {
			rbCheck.setChecked(true);
		} else {
			rbCheck.setChecked(false);
		}
		return convertView;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
//	@Override
//	public void onItemClick(AdapterView<?> parent, View view, int position,
//			long id) {
//		// TODO Auto-generated method stub
//		if (position != mSelectItem) {
//			mSelectItem = position;
//			notifyDataSetChanged();
//		}
//		activity.finish();
//	}
	
}
