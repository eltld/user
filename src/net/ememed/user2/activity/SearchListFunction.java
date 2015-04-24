package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;

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

public class SearchListFunction extends Activity{
	
	private ListView view;	//
	private ArrayList<HashMap<String, Object>>  list;
	private ListAdapter listAdapter=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_docterstudio);
		view=(ListView) findViewById(R.id.list_search_doctor);
		
		listAdapter=new ListAdapter(this);
		view.setAdapter(listAdapter);
		
	}
//获取数据
	private ArrayList<HashMap<String,Object>> getData(){
		list=new ArrayList<HashMap<String,Object>>();
		HashMap<String, Object> map=new HashMap<String, Object>();
		map.put("head", R.drawable.avatar_large);
		map.put("name", "刘莉莉");
		map.put("position", "主任医师");
		map.put("authentication", R.drawable.yrz_icon80x24);
		map.put("hospital", "广州第三人民医院");
		map.put("goodat", "耳鼻喉科");
		list.add(map);
		
		return list;
	}
	
	public class ListAdapter extends BaseAdapter{

		private LayoutInflater inflater;
		private ViewHolder holder;
		public ListAdapter(Context context){
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
			holder=new ViewHolder();
			if(null==arg1){
				
				arg1=inflater.inflate(R.layout.item_search_doctor_function, null);
				kongholder(arg1);
				
				arg1.setTag(holder);
				
			}else{
				holder=(ViewHolder) arg1.getTag();
			}
			
			holder.img_head.setBackgroundResource((Integer) getData().get(arg0).get("head"));
			holder.img_authentication.setBackgroundResource((Integer) getData().get(arg0).get("authentication"));
			holder.txt_goodat.setText(getData().get(arg0).get("goodat").toString());
			holder.txt_hospital.setText(getData().get(arg0).get("hospital").toString());
			holder.txt_name.setText(getData().get(arg0).get("name").toString());
			holder.txt_position.setText(getData().get(arg0).get("position").toString());
			
			return arg1;
		}
		
		public void kongholder(View arg1){
			holder.img_head=(ImageView) arg1.findViewById(R.id.item_search_doctor_img_head);
			holder.img_authentication= (ImageView) arg1.findViewById(R.id.item_search_doctor_img_authentication);
			holder.txt_goodat=(TextView) arg1.findViewById(R.id.item_search_doctor_goodat);
			holder.txt_hospital=(TextView) arg1.findViewById(R.id.item_search_doctor_hospital);
			holder.txt_name=(TextView) arg1.findViewById(R.id.item_search_doctor_name);
			holder.txt_position=(TextView) arg1.findViewById(R.id.item_search_doctor_position);
		}
	}
}

// 属性对象
class ViewHolder{
	public ImageView img_head; //医生头像
	public TextView txt_name;  //名字
	public TextView txt_position;  //职位
	public ImageView img_authentication;  //是否认证
	public TextView txt_hospital;  //哪家医院
	public TextView txt_goodat; 
}


