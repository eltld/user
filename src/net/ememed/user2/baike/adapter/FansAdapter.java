package net.ememed.user2.baike.adapter;

import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.baike.DoctorAttentionActivity;
import net.ememed.user2.baike.DoctorFansActivity;
import net.ememed.user2.baike.entity.FansData;
import net.ememed.user2.baike.entity.FansUserInfo;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FansAdapter extends BaseAdapter{
	private Context context;
	private List<FansData> fans;
	private int fansType;
	
	public FansAdapter(Context context, List<FansData> fans, int fansType){
		this.context = context;
		this.fans = fans;
		this.fansType = fansType;
	}
	
	@Override
	public int getCount() {
		return fans.size();
	}

	@Override
	public Object getItem(int position) {
		return fans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();

			convertView = LayoutInflater.from(context).inflate(R.layout.fans_item, null);
			holder.iv_photo = (CircleImageView) convertView.findViewById(R.id.civ_photo);
			holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
			holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.iv_next = (ImageView) convertView.findViewById(R.id.iv_next);
			holder.tv_age = (TextView) convertView.findViewById(R.id.tv_age);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if(DoctorFansActivity.FANS_TYPE_DOCTOR == fansType){
			holder.iv_next.setVisibility(View.VISIBLE);
			holder.tv_age.setVisibility(View.GONE);
			holder.tv_position.setVisibility(View.VISIBLE);
			
//			DoctorEntry entry = fans.get(position).getDOCTORINFO();
			FansData data = fans.get(position);
			
			((BasicActivity)context).imageLoader.displayImage(data.getAVATAR(), holder.iv_photo, Util.getOptions_avatar());
			holder.tv_name.setText(data.getREALNAME());
			holder.tv_position.setText(data.getPROFESSIONAL());
			holder.tv_content.setText(data.getHOSPITALNAME());
		} else {
			holder.iv_next.setVisibility(View.GONE);
			holder.tv_age.setVisibility(View.VISIBLE);
			holder.tv_position.setVisibility(View.GONE);
			
			FansUserInfo info = fans.get(position).getUSERINFO();
			((BasicActivity)context).imageLoader.displayImage(info.getAVATAR(), holder.iv_photo, Util.getOptions_avatar());
			holder.tv_name.setText(info.getREALNAME());
			holder.tv_content.setText(info.getSEX().equals("1")?"男":"女");
//			holder.tv_age.setText(info.get);
		}
		return convertView;
	}
	
	public void change(List<FansData> fans){
		this.fans.clear();
		this.fans.addAll(fans);
		notifyDataSetChanged();
	}
	
	class ViewHolder{
		CircleImageView iv_photo;
		TextView tv_name;
		TextView tv_position;
		TextView tv_content;
		ImageView iv_next;
		TextView tv_age;
	}
	
}
