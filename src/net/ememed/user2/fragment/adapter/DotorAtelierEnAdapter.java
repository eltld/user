package net.ememed.user2.fragment.adapter;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.entity.SearchPerson;
import net.ememed.user2.fragment.adapter.DotorAtelierAdapter.ViewHolder;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DotorAtelierEnAdapter extends BaseAdapter{
	private List<SearchPerson> list;
	private LayoutInflater inflater;
	private ViewHolder vh;
	private ImageLoader imageLoader;

	public DotorAtelierEnAdapter(Context context, List<SearchPerson> list,ImageLoader imageLoader) {
		if (null == list) {
			list = new ArrayList();
		}
		this.list = list;
		inflater = LayoutInflater.from(context);
		this.imageLoader=imageLoader;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		vh = new ViewHolder();
		if (null == arg1) {
			arg1 = inflater.inflate(R.layout.item_search_doctor_function, null);

			vh.img_head = (ImageView) arg1
					.findViewById(R.id.item_search_doctor_img_head);
			vh.txt_name = (TextView) arg1
					.findViewById(R.id.item_search_doctor_name);
			vh.txt_position = (TextView) arg1
					.findViewById(R.id.item_search_doctor_position);
			vh.img_authentication = (ImageView) arg1
					.findViewById(R.id.item_search_doctor_img_authentication);
			vh.txt_hospital = (TextView) arg1
					.findViewById(R.id.item_search_doctor_hospital);
			vh.txt_goodat = (TextView) arg1
					.findViewById(R.id.item_search_doctor_goodat);

			arg1.setTag(vh);
		} else {
			vh = (ViewHolder) arg1.getTag();
		}
		
		
		imageLoader.displayImage(list.get(arg0).getAVATAR(), vh.img_head);
		vh.txt_name.setText(list.get(arg0).getREALNAME());
		vh.txt_position.setText(list.get(arg0).getPROFESSIONAL());
		if (Integer.parseInt(list.get(arg0).getAUDITSTATUS()) == 1) {
			vh.img_authentication
					.setBackgroundResource(R.drawable.yrz_icon80x24);
		} else {
			vh.img_authentication
					.setBackgroundResource(R.drawable.wrz_icon66x24);
		}
		vh.txt_hospital.setText(list.get(arg0).getHOSPITALNAME());
		vh.txt_goodat.setText(list.get(arg0).getROOMNAME());

		return arg1;
	}

	static class ViewHolder {
		ImageView img_head;
		TextView txt_name;
		TextView txt_position;
		ImageView img_authentication;
		TextView txt_hospital;
		TextView txt_goodat;
	}

}
