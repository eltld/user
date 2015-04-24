package net.ememed.user2.fragment.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.ememed.user2.R;
import net.ememed.user2.entity.SearchHospital;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckHospitalAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private List<SearchHospital> list;
	private ViewHolder vh;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public CheckHospitalAdapter(Context context,List<SearchHospital> list,ImageLoader imageLoader,DisplayImageOptions options){
		inflater=LayoutInflater.from(context);
		this.list=list;
		this.imageLoader=imageLoader;
		this.options=options;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		if(list==null){
			return 0;
		}
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
			arg1=inflater.inflate(R.layout.item_search_hospital, null);
			vh.img_head=(ImageView) arg1.findViewById(R.id.item_search_hospital_img_head);
			vh.txt_name=(TextView) arg1.findViewById(R.id.item_search_hospital_name);
			vh.btn_grade=(Button) arg1.findViewById(R.id.item_search_hospital_grade);
			vh.txt_registration=(TextView) arg1.findViewById(R.id.item_search_hospital_registration);
			vh.btn_function=(Button) arg1.findViewById(R.id.item_search_hospital_function);
			vh.txt_address=(TextView) arg1.findViewById(R.id.item_search_hospital_address);
			arg1.setTag(vh);
		}else{
			vh=(ViewHolder) arg1.getTag();
		}
		
		imageLoader.displayImage(list.get(arg0).getPIC(), vh.img_head,options);
		vh.txt_name.setText(list.get(arg0).getHOSPITALNAME());
		vh.btn_grade.setText(list.get(arg0).getGRADE());
//		vh.txt_registration.setText(list.get(arg0).getWEBSITE());
		vh.txt_address.setText(list.get(arg0).getADDRESS());
		
		return arg1;
	}
	
	static class ViewHolder{
		ImageView img_head;
		TextView txt_name;
		Button btn_grade;
		TextView txt_registration;
		Button btn_function;
		TextView txt_address;
	}
}
