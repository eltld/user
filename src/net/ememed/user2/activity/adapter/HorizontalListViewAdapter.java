package net.ememed.user2.activity.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.GuahaoDoctorInfo;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView.FindListener;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HorizontalListViewAdapter extends BaseAdapter{
	
	private Context mContext;
	private List<GuahaoDoctotItem> recommendDoctors;
	private LayoutInflater mInflater;
	Bitmap iconBitmap;
	BasicActivity activity;
	private int selectIndex = -1;

	public HorizontalListViewAdapter(BasicActivity activity, List<GuahaoDoctotItem> recommendDoctors){
		//this.mContext = context;
		this.activity = activity;
		if(recommendDoctors != null){
			this.recommendDoctors = recommendDoctors;
		} else {
			this.recommendDoctors = new ArrayList<GuahaoDoctotItem>();
		}
		mInflater=(LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);//LayoutInflater.from(mContext);
	}
	
	public void change(List<GuahaoDoctotItem> recommendDoctors) {
		if (recommendDoctors == null) {
			recommendDoctors = new ArrayList<GuahaoDoctotItem>();
		}
		this.recommendDoctors = recommendDoctors;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return recommendDoctors.size();
	}
	@Override
	public Object getItem(int position) {
		return recommendDoctors.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if(convertView==null){
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_guahao_recommend_doctor, null);
			holder.civDoctorHead=(CircleImageView) convertView.findViewById(R.id.doctor_head);
			holder.tvDoctorName=(TextView)convertView.findViewById(R.id.tv_doctor_name);
			holder.tvDoctorPosition = (TextView) convertView.findViewById(R.id.tv_doctor_position);
			holder.ll = (LinearLayout) convertView.findViewById(R.id.linearLayout_1);
			convertView.setTag(holder);
			
		}else{
			holder=(ViewHolder)convertView.getTag();
		}
		if(position == selectIndex){
			convertView.setSelected(true);
		}else{
			convertView.setSelected(false);
		}
		
		int width=MyApplication.getInstance().canvasWidth/3;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, LayoutParams.WRAP_CONTENT);
		holder.ll.setLayoutParams(params);
		GuahaoDoctotItem doctorItem = recommendDoctors.get(position);
		holder.tvDoctorName.setText(doctorItem.getDOCTORNAME());
		holder.tvDoctorPosition.setText(doctorItem.getPROFESSIONAL());
		activity.imageLoader.displayImage(doctorItem.getAVATAR(), holder.civDoctorHead, Util.getOptions_default_portrait());

		return convertView;
	}

	private static class ViewHolder {
		private CircleImageView civDoctorHead;
		private TextView tvDoctorName;
		private TextView tvDoctorPosition;
		private LinearLayout ll;
		
	}
	private Bitmap getPropThumnail(int id){
		/*Drawable d = mContext.getResources().getDrawable(id);
		Bitmap b = BitmapUtil.drawableToBitmap(d);
//		Bitmap bb = BitmapUtil.getRoundedCornerBitmap(b, 100);
		int w = mContext.getResources().getDimensionPixelOffset(R.dimen.thumnail_default_width);
		int h = mContext.getResources().getDimensionPixelSize(R.dimen.thumnail_default_height);
		
		Bitmap thumBitmap = ThumbnailUtils.extractThumbnail(b, w, h);
		
		return thumBitmap;*/
		return null;
	}
	public void setSelectIndex(int i){
		selectIndex = i;
	}
}