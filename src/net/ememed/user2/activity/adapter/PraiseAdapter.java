package net.ememed.user2.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.baike.entity.DoctorBountyListInfo;
import net.ememed.user2.entity.DoctorPraise;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class PraiseAdapter extends BaseAdapter{
	
	private BasicActivity activity;
	private List<DoctorPraise> listItems;
	
	public PraiseAdapter(List<DoctorPraise> listItems, BasicActivity activity){
		if(null == listItems){
			listItems = new ArrayList<DoctorPraise>();
		}
		
		this.listItems = listItems;
		this.activity = activity;
	}

	@Override
	public int getCount() {
		return listItems.size();
	}

	@Override
	public Object getItem(int position) {
		return listItems.get(position);
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
			convertView = LayoutInflater.from(activity).inflate(R.layout.reward_item, null);
			holder.civ_photo = (CircleImageView) convertView.findViewById(R.id.civ_photo);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		DoctorPraise info = listItems.get(position);
		activity.imageLoader.displayImage(info.getUSER_AVATAR(), holder.civ_photo, Util.getOptions_avatar());
		if(!TextUtils.isEmpty(info.getUSER_REALNAME())){
			holder.tv_name.setText(info.getUSER_REALNAME());
		}
		if(!TextUtils.isEmpty(info.getMEMO())){
			holder.tv_content.setText(info.getMEMO());
		}
		
		if(!TextUtils.isEmpty(info.getORIGINALPRICE())){
			holder.tv_money.setText(info.getORIGINALPRICE());
		}
		
		return convertView;
	}
		
	
	
	public void change(List<DoctorPraise> lists) {
		if (lists == null) {
			lists = new ArrayList<DoctorPraise>();
		}
		this.listItems.clear();
		this.listItems.addAll(lists);
		notifyDataSetChanged();
	}
	
	
	class ViewHolder{
		CircleImageView civ_photo;
		TextView tv_name;
		TextView tv_money;
		TextView tv_content;
	}

}
