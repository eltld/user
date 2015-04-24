package net.ememed.user2.baike.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.baike.entity.DoctorBountyListInfo;
import net.ememed.user2.baike.entity.SaysCommentInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.MenuDialog;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class RewardAdapter extends BaseAdapter{

	private BasicActivity activity;
	private List<DoctorBountyListInfo> listItems;
	
	public RewardAdapter(List<DoctorBountyListInfo> listItems, BasicActivity activity){
		if(null == listItems){
			listItems = new ArrayList<DoctorBountyListInfo>();
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
//			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
			holder.tv_RMB = (TextView) convertView.findViewById(R.id.tv_RMB);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		DoctorBountyListInfo info = listItems.get(position);
		activity.imageLoader.displayImage(info.getAVATAR(), holder.civ_photo, Util.getOptions_avatar());
			
		holder.tv_name.setText(info.getUSERNAME());
		holder.tv_content.setText(info.getCONTENT());
//		holder.tv_time.setText(info.get)
//		holder.tv_money.setText(info.getMONEY());
		holder.tv_RMB.setVisibility(View.GONE);
		holder.tv_money.setVisibility(View.VISIBLE);
		return convertView;
	}
		
	
	
	public void change(List<DoctorBountyListInfo> lists) {
		if (lists == null) {
			lists = new ArrayList<DoctorBountyListInfo>();
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
//		TextView tv_time;
		TextView tv_RMB;
	}

}
