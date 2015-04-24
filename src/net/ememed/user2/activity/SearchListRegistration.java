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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchListRegistration extends Activity{
	
	private ArrayList<SearchDoctor> list;
	private SearchDoctor sd;
	private ListView lv;
	private GuaHaoAdapter gh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_docterstudio);
		
		lv=(ListView) findViewById(R.id.list_search_doctor);
		gh=new GuaHaoAdapter(this);
		
		lv.setAdapter(gh);
		
	}
	
	
	//获取数据
	private ArrayList<SearchDoctor> getData(){
		list=new ArrayList<SearchDoctor>();
		sd=new SearchDoctor(R.drawable.avatar_large, "刘莉莉", "主任医生", R.drawable.yrz_icon80x24, "广州第三人民医院", "耳鼻喉科", R.drawable.avatar_small);
		list.add(sd);
		return list;
	}
	
	//挂号适配器
	private class GuaHaoAdapter extends BaseAdapter{

		public LayoutInflater inflater;
		private ViewHolder2 holder;
		public GuaHaoAdapter(Context context){
			inflater=LayoutInflater.from(context);
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
			
			holder=new ViewHolder2();
			if(null==arg1){
				arg1=inflater.inflate(R.layout.item_search_registration, null);
				kongHolder(arg1);
				arg1.setTag(holder);
			}else{
				holder=(ViewHolder2) arg1.getTag();
			}
			
//			youHolder(arg0);
			holder.img_head.setBackgroundResource(R.drawable.avatar_large);
			holder.img_authentication.setBackgroundResource(getData().get(arg0).getImg_authentication());
			holder.txt_goodat.setText(getData().get(arg0).getTxt_goodat());
			holder.txt_hospital.setText(getData().get(arg0).getTxt_hospital());
			holder.txt_name.setText(getData().get(arg0).getTxt_name());
			holder.txt_position.setText(getData().get(arg0).getTxt_position());
			holder.img_isnumber.setBackgroundResource(getData().get(arg0).getImg_isnumber());
			
			return arg1;
		}
		
		//ContentView 为空
		public void kongHolder(View arg1){
			
			holder.img_head=(ImageView) arg1.findViewById(R.id.include_item_search).findViewById(R.id.item_search_doctor_img_head);
			holder.img_authentication= (ImageView) arg1.findViewById(R.id.include_item_search).findViewById(R.id.item_search_doctor_img_authentication);
			holder.txt_goodat=(TextView) arg1.findViewById(R.id.include_item_search).findViewById(R.id.item_search_doctor_goodat);
			holder.txt_hospital=(TextView) arg1.findViewById(R.id.include_item_search).findViewById(R.id.item_search_doctor_hospital);
			holder.txt_name=(TextView) arg1.findViewById(R.id.include_item_search).findViewById(R.id.item_search_doctor_name);
			holder.txt_position=(TextView) arg1.findViewById(R.id.include_item_search).findViewById(R.id.item_search_doctor_position);
			holder.img_isnumber=(ImageView) arg1.findViewById(R.id.item_search_registration_isnumber);
			
		}
		
	}
}

class ViewHolder2 extends ViewHolder{
	public ImageView img_isnumber; 
}

//搜索免费挂号类
class SearchDoctor{
	
	public int img_head; //医生头像
	public String txt_name;  //名字
	public String txt_position;  //职位
	public int img_authentication;  //是否认证
	public String txt_hospital;  //哪家医院
	public String txt_goodat;   //擅长
	public int img_isnumber; //是否有号
	
	public int getImg_head() {
		return img_head;
	}

	public String getTxt_name() {
		return txt_name;
	}

	public String getTxt_position() {
		return txt_position;
	}

	public int getImg_authentication() {
		return img_authentication;
	}

	public String getTxt_hospital() {
		return txt_hospital;
	}

	public String getTxt_goodat() {
		return txt_goodat;
	}

	public int getImg_isnumber() {
		return img_isnumber;
	}

	
	public SearchDoctor(int img_head, String txt_name, String txt_position,
			int img_authentication, String txt_hospital, String txt_goodat,
			int img_isnumber) {
		super();
		this.img_head = img_head;
		this.txt_name = txt_name;
		this.txt_position = txt_position;
		this.img_authentication = img_authentication;
		this.txt_hospital = txt_hospital;
		this.txt_goodat = txt_goodat;
		this.img_isnumber = img_isnumber;
	}
	
}