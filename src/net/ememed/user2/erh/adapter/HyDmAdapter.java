package net.ememed.user2.erh.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.erh.ViewHolder;
import net.ememed.user2.erh.activity.ErhMainHistoryActivity;
import net.ememed.user2.erh.activity.ErhPayAndWorkActivity;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class HyDmAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	// 保存勾中的状态,记录CheckBox的状态
	private String choiceTelephones = "";
	private String[] txt;
	private Activity activity;
	private List<String> list = new ArrayList<String>();
	
	public HyDmAdapter(Activity activity, String[] txt, List<String> reList){
		this.activity = activity;
		inflater = LayoutInflater.from(activity);
		this.txt = txt;
		this.list = reList;
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
			convertView = inflater.inflate(R.layout.visit_hy_dm_list_item, null);
        } 
		TextView contentTextView = ViewHolder.get(convertView, R.id.iv_txt);
		CheckBox cbCheck = ViewHolder.get(convertView, R.id.cb_common_check);
		contentTextView.setText(txt[position]);
		cbCheck.setTag(txt[position]);
		if(list.contains(txt[position])){
			cbCheck.setChecked(true);
		} else {
			cbCheck.setChecked(false);
		}
		cbCheck.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				changeSelect(txt[position], 
						((CheckBox) paramView).isChecked());
			}
		});
		return convertView;
	}
	
	/**
	 * 选择的item
	 * @param item
	 * @param check
	 */
	private void changeSelect(String item, Boolean check) {
		if (check == true) {
			list.add(item);
		} else {
			list.remove(item);
		}
	}
	
	public ArrayList<String> getListContent(){
		return (ArrayList<String>) list;
	}
	
}
