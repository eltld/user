package net.ememed.user2.activity;

import java.util.ArrayList;


import net.ememed.user2.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchListHospital extends Activity{
	private ArrayList<ListHospital> list;
	private ListView listview;
	private HospitalAdapter ha;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_docterstudio);
		
		listview=(ListView) findViewById(R.id.list_search_doctor);
		ha=new HospitalAdapter(this);
		
		listview.setAdapter(ha);
	}
	
	
	private ArrayList<ListHospital> getData(){  
		list=new ArrayList<ListHospital>();
		ListHospital lh=new ListHospital(R.drawable.avatar_large, "广州第三人民医院", "三甲医院", "免费挂号", "医生工作室", "广州市天河区");
		list.add(lh);
		return list;
	}
	
	private class HospitalAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private HospitalHolder vh;
		
		public HospitalAdapter(Context context){
			this.inflater=LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return getData().size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			vh=new HospitalHolder();
			if(null==arg1){
				arg1=inflater.inflate(R.layout.item_search_hospital, null);
				vh.img_head=(ImageView) arg1.findViewById(R.id.item_search_hospital_img_head);
				vh.txt_name=(TextView) arg1.findViewById(R.id.item_search_hospital_name);
				vh.btn_grade=(Button) arg1.findViewById(R.id.item_search_hospital_grade);
				vh.btn_registration=(Button) arg1.findViewById(R.id.item_search_hospital_registration);
				vh.btn_function=(Button) arg1.findViewById(R.id.item_search_hospital_function);
				vh.txt_address=(TextView) arg1.findViewById(R.id.item_search_hospital_address);
				arg1.setTag(vh);
				
			}else{
				vh=(HospitalHolder) arg1.getTag();
			}
			
			vh.img_head.setBackgroundResource(getData().get(arg0).getImg_head());
			vh.txt_name.setText(getData().get(arg0).getTxt_name());
			vh.btn_grade.setText(getData().get(arg0).getBtn_grade());
			vh.btn_registration.setText(getData().get(arg0).getBtn_registration());
			vh.btn_function.setText(getData().get(arg0).getBtn_function());
			vh.txt_address.setText(getData().get(arg0).getTxt_address());
			
			return arg1;
		}
		
		
	}
}



class HospitalHolder{
	public ImageView img_head;  //医院图像
	public TextView txt_name;	//医院名字
	public Button btn_grade;	//医院等级
	public Button  btn_registration;//免费挂号
	public Button btn_function;//医生工作室
	public TextView txt_address;//医院地址
}


//医院挂号属性
class ListHospital{
	public int img_head;
	private String txt_name;
	private String btn_grade;
	private String btn_registration;
	private String btn_function;
	private String txt_address;
	
	
	public int getImg_head() {
		return img_head;
	}
	public String getTxt_name() {
		return txt_name;
	}
	public String getBtn_grade() {
		return btn_grade;
	}
	public String getBtn_registration() {
		return btn_registration;
	}
	public String getBtn_function() {
		return btn_function;
	}
	public String getTxt_address() {
		return txt_address;
	}
	public void setImg_head(int img_head) {
		this.img_head = img_head;
	}
	public void setTxt_name(String txt_name) {
		this.txt_name = txt_name;
	}
	public void setBtn_grade(String btn_grade) {
		this.btn_grade = btn_grade;
	}
	public void setBtn_registration(String btn_registration) {
		this.btn_registration = btn_registration;
	}
	public void setBtn_function(String btn_function) {
		this.btn_function = btn_function;
	}
	public void setTxt_address(String txt_address) {
		this.txt_address = txt_address;
	}
	
	
	public ListHospital(int img_head, String txt_name, String btn_grade,
			String btn_registration, String btn_function, String txt_address) {
		super();
		this.img_head = img_head;
		this.txt_name = txt_name;
		this.btn_grade = btn_grade;
		this.btn_registration = btn_registration;
		this.btn_function = btn_function;
		this.txt_address = txt_address;
	}
	
}