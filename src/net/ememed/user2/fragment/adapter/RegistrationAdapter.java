package net.ememed.user2.fragment.adapter;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.ememed.user2.R;
import net.ememed.user2.entity.SearchGuahao;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class RegistrationAdapter extends BaseAdapter{
	
	private List<SearchGuahao> list;
	private LayoutInflater inflater;
	private ViewHolder vh;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public RegistrationAdapter(Context context,List<SearchGuahao> list,ImageLoader iamgeLoader,DisplayImageOptions options){
		
		this.list=list;
		inflater=LayoutInflater.from(context);
		this.imageLoader=iamgeLoader;
		this.options=options;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list==null)
			return 0;
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
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		if(null==arg1){
			vh=new ViewHolder();
			arg1=inflater.inflate(R.layout.item_search_registration, null);
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
			vh.img_isnumber=(ImageView) arg1.findViewById(R.id.item_search_registration_isnumber);

			arg1.setTag(vh);
		}else{
			vh = (ViewHolder) arg1.getTag();
		}
			
			imageLoader.displayImage(list.get(arg0).getDoctorInfo().getImgUrl(), vh.img_head,options);
			vh.txt_name.setText(list.get(arg0).getDoctorInfo().getDoctorName());
			vh.txt_position.setText(list.get(arg0).getDoctorInfo().getProfessional());
			//是否认证
//			if (Integer.parseInt(list.get(arg0).get) == 1) {
//				vh.img_authentication
//						.setBackgroundResource(R.drawable.yrz_icon80x24);
//			} else {
//				vh.img_authentication
//						.setBackgroundResource(R.drawable.wrz_icon66x24);
//			}
			vh.txt_hospital.setText(list.get(arg0).getDoctorInfo().getHospitalName());
			vh.txt_goodat.setText(list.get(arg0).getDoctorInfo().getRoomName());
			//是否有号
			if(list.get(arg0).getSchedules().size()>0){
				vh.img_isnumber.setBackgroundResource(R.drawable.youhao_icon_66x66);
			}else{
				vh.img_isnumber.setBackgroundResource(R.drawable.wuhao_icon_66x66);
			}

			return arg1;
	}
	
	static class ViewHolder{
		ImageView img_head;
		TextView txt_name;
		TextView txt_position;
		ImageView img_authentication;
		TextView txt_hospital;
		TextView txt_goodat;
		ImageView img_isnumber;
	}
}
