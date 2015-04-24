package net.ememed.user2.fragment.adapter;

import java.util.List;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.ememed.user2.R;
import net.ememed.user2.entity.SearchTxtconsult;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CheckMedicalAdapter extends BaseAdapter{
	
	private LayoutInflater inflater;
	private List<SearchTxtconsult> list;
	private ViewHolder vh;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	
	public CheckMedicalAdapter(Context context,List<SearchTxtconsult> list,ImageLoader imageLoader,DisplayImageOptions options){
		inflater=LayoutInflater.from(context);
		this.list=list;
		this.imageLoader=imageLoader;
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
		vh=new ViewHolder();
		if(null==arg1){
			arg1=inflater.inflate(R.layout.item_search_history, null);
			vh.txt_question=(TextView) arg1.findViewById(R.id.item_search_hostory_txt_question);  //问题
			vh.img_head=(ImageView) arg1.findViewById(R.id.item_search_hostory_img_head);	//医生头像
			vh.btn_name=(TextView) arg1.findViewById(R.id.item_search_hostory_name);	//医生姓名
			vh.txt_position=(TextView) arg1.findViewById(R.id.item_search_hostory_position); //医生职位
			vh.txt_edu=(TextView) arg1.findViewById(R.id.item_search_hostory_edu);	//科位
			vh.txt_date=(TextView) arg1.findViewById(R.id.item_search_hostory_date); //回答日期
			vh.txt_answer=(TextView) arg1.findViewById(R.id.item_search_hostory_txt_answer);   //回答内容
			arg1.setTag(vh);
		}else{
			vh=(ViewHolder) arg1.getTag();
		}
		vh.txt_question.setText(list.get(arg0).getFirst_consult().getCONTENT());
		imageLoader.displayImage(list.get(arg0).getLastest_consult().getEXT().getDoctor_avatar(), vh.img_head,options);
		
		vh.btn_name.setText(list.get(arg0).getLastest_consult().getDOCTOR_INFO().getREALNAME());
		vh.txt_position.setText(list.get(arg0).getLastest_consult().getDOCTOR_INFO().getPROFESSIONAL());
		vh.txt_edu.setText(list.get(arg0).getLastest_consult().getDOCTOR_INFO().getROOMNAME());
//		vh.txt_date;
		vh.txt_answer.setText(list.get(arg0).getLastest_consult().getCONTENT());
		
		return arg1;
	}
	static class ViewHolder{
		public TextView txt_question;
		public ImageView img_head;
		public TextView btn_name;
		public TextView txt_position;
		public TextView txt_edu;
		public TextView txt_date;
		public TextView txt_answer;
	}
}
