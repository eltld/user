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
import android.widget.TextView;

public class SearchListHistory extends Activity{
	ArrayList<HistoryInfo> list;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search_docterstudio);
		
		
		
	}
	
	private ArrayList<HistoryInfo> getData(){
		
		list=new ArrayList<HistoryInfo>();
		HistoryInfo hi=new HistoryInfo("txt_question内吼啊，，今天中午吃的什么啊！内吼啊，，今天中午吃的什么啊！内吼啊，，今天中午吃的什么啊！"+"(", "男"+",   ", "26"+"岁)", R.drawable.avatar_large, "名字", "主任医师", "全科", "2000-08-08");
		list.add(hi);
		
		return list;
	}
	
	private class HistoryAdapter extends BaseAdapter{
		private LayoutInflater inflater;
		private HistoryHolder vh;
		
		public HistoryAdapter(Context context){
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
			vh=new HistoryHolder();
			if(null==arg1){
				arg1=inflater.inflate(R.layout.item_search_hospital, null);
				
				
				
				arg1.setTag(vh);
				
			}else{
				vh=(HistoryHolder) arg1.getTag();
			}
			
			
			return arg1;
		}
		
		
	}
}

class HistoryHolder{
	private TextView txt_question;
	private ImageView img_head;
	private Button btn_name;
	private TextView txt_position;
	private TextView txt_edu;
	private TextView txt_date;
}



class HistoryInfo{
	private String txt_question;
	private String txt_sex;
	private String txt_age;
	private int img_head;
	private String btn_name;
	private String txt_position;
	private String txt_edu;
	private String txt_date;
	public String getTxt_question() {
		return txt_question;
	}
	public String getTxt_sex() {
		return txt_sex;
	}
	public String getTxt_age() {
		return txt_age;
	}
	public int getImg_head() {
		return img_head;
	}
	public String getBtn_name() {
		return btn_name;
	}
	public String getTxt_position() {
		return txt_position;
	}
	public String getTxt_edu() {
		return txt_edu;
	}
	public String getTxt_date() {
		return txt_date;
	}
	public void setTxt_question(String txt_question) {
		this.txt_question = txt_question;
	}
	public void setTxt_sex(String txt_sex) {
		this.txt_sex = txt_sex;
	}
	public void setTxt_age(String txt_age) {
		this.txt_age = txt_age;
	}
	public void setImg_head(int img_head) {
		this.img_head = img_head;
	}
	public void setBtn_name(String btn_name) {
		this.btn_name = btn_name;
	}
	public void setTxt_position(String txt_position) {
		this.txt_position = txt_position;
	}
	public void setTxt_edu(String txt_edu) {
		this.txt_edu = txt_edu;
	}
	public void setTxt_date(String txt_date) {
		this.txt_date = txt_date;
	}
	
	
	public HistoryInfo(String txt_question, String txt_sex, String txt_age,
			int img_head, String btn_name, String txt_position, String txt_edu,
			String txt_date) {
		super();
		this.txt_question = txt_question;
		this.txt_sex = txt_sex;
		this.txt_age = txt_age;
		this.img_head = img_head;
		this.btn_name = btn_name;
		this.txt_position = txt_position;
		this.txt_edu = txt_edu;
		this.txt_date = txt_date;
	}
	
	
}