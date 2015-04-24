package net.ememed.user2.medicalhistory.adapter;

import java.util.ArrayList;
import java.util.List;
import com.nostra13.universalimageloader.core.ImageLoader;
import net.ememed.user2.R;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.medicalhistory.activity.ImageViewZoomActivity;
import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MedicalHistoryEditAdapter2 extends BaseAdapter {
	private Activity activity;
	private List<String> listItems;
	private List<String> times;
	private Drawable drawableImage;
	private List<String> blDatas1;
	private List<String> blDatas2;
	private List<String> blDatas3;
	
	public MedicalHistoryEditAdapter2(Activity activity, ArrayList<String> listItems, ArrayList<String> blDatas1,
			ArrayList<String> blDatas2, ArrayList<String> blDatas3, ArrayList<String> blDatas4,
			ArrayList<String> blDatas5, ArrayList<String> blDatas6, ArrayList<String> times) {
		this.activity = activity;
		if (listItems == null) {
			listItems = new ArrayList<String>();
		}
		if (times == null) {
			times = new ArrayList<String>();
		}
		if (blDatas1 == null) {
			blDatas1 = new ArrayList<String>();
		}
		if (blDatas2 == null) {
			blDatas2 = new ArrayList<String>();
		}
		if (blDatas3 == null) {
			blDatas3 = new ArrayList<String>();
		}
		if (blDatas4 == null) {
			blDatas4 = new ArrayList<String>();
		}
		if (blDatas5 == null) {
			blDatas5 = new ArrayList<String>();
		}
		if (blDatas6 == null) {
			blDatas6 = new ArrayList<String>();
		}		
		this.listItems = listItems;
		this.blDatas1 = blDatas1;
		this.blDatas2 = blDatas2;
		this.blDatas3 = blDatas3;
		drawableImage = activity.getResources().getDrawable(R.drawable.medical_history_add_picture);
	}
	@Override
	public int getCount() {
		return listItems == null ? 0 : listItems.size();
	}

	@Override
	public Object getItem(int position) {
		if(this.listItems == null ) return null;
		return this.listItems.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.medical_history_edit_adpter2, null);
		}
		TextView hospitalName = (TextView) convertView.findViewById(R.id.bingli_add_text);
		TextView time = (TextView) convertView.findViewById(R.id.bingli_add_text1);
		ImageView imageView1 = (ImageView) convertView.findViewById(R.id.image1);
		ImageView imageView2 = (ImageView) convertView.findViewById(R.id.image2);
		ImageView imageView3 = (ImageView) convertView.findViewById(R.id.image3);
		hospitalName.setText(listItems.get(position));
		time.setText(times.get(position));
		if (!blDatas1.get(position).equals("") && !(blDatas1.get(position) == null)) {
			imageView1.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(blDatas1.get(position), imageView1); 
			Log.d("附件图片：", blDatas1.get(position) + "");
			
		}else {
			imageView1.setVisibility(View.GONE);
		}
		imageView1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("url", blDatas1.get(position));
				Utils.startActivity(activity, ImageViewZoomActivity.class, bundle);
			}
		});
		if (!(blDatas2.get(position) == null) && !blDatas2.get(position).equals("")) {
			imageView2.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(blDatas2.get(position), imageView2);
		} else {
			imageView2.setVisibility(View.GONE);
		}
		imageView2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("url", blDatas2.get(position));
				Utils.startActivity(activity, ImageViewZoomActivity.class, bundle);
			}
		});
		if (!(blDatas3.get(position) == null) && !blDatas3.get(position).equals("")) {
			imageView3.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(blDatas3.get(position), imageView3);
		} else {
			imageView3.setVisibility(View.GONE);
		}
		imageView3.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Bundle bundle = new Bundle();
				bundle.putString("url", blDatas3.get(position));
				Utils.startActivity(activity, ImageViewZoomActivity.class, bundle);
			}
		});
		return convertView;
	}
	
	public void change(List<String> lists, List<String> lists1, List<String> lists2, List<String> lists3, List times) {
		if (lists == null) {
			lists = new ArrayList<String>();
		}
		if (lists1 == null) {
			lists1 = new ArrayList<String>();
		}
		if (lists2 == null) {
			lists2 = new ArrayList<String>();
		}
		if (lists3 == null) {
			lists3 = new ArrayList<String>();
		}
		if (times == null) {
			times = new ArrayList<String>();
		}

		this.listItems = lists;
		this.blDatas1 = lists1;
		this.blDatas2 = lists2;
		this.blDatas3 = lists3;
		this.times = times;
		
		notifyDataSetChanged();
	}
	
	public void add(List<String> list, List<String> lists1, List<String> lists2, List<String> lists3) {
		this.listItems.addAll(list);
		if (!(lists1 == null) && !lists1.equals("")) {
			this.blDatas1.addAll(lists1);
		}
		if (!(lists2 == null) && !lists2.equals("")) {
			this.blDatas2.addAll(lists2);
		}
		if (!(lists3 == null) && !lists3.equals("")) {
			this.blDatas3.addAll(lists3);
		}
		notifyDataSetChanged();
	}

}
