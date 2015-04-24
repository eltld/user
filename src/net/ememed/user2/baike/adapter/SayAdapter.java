package net.ememed.user2.baike.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.baike.SayDetailsActivity;
import net.ememed.user2.baike.entity.BaikeSaysInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.util.Util;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

public class SayAdapter extends BaseAdapter{
	private final int REQUEST_SAYS_DETAIL = 2;
	
	private BasicActivity activity;
	private List<BaikeSaysInfo> lists;
	private Handler handler;

	public SayAdapter(BasicActivity activity, Handler handler, List<BaikeSaysInfo> lists) {
		this.activity =  activity;
		this.lists = lists;
		this.handler = handler;
	}
	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if(null == convertView){
			holder = new ViewHolder();
			convertView = LayoutInflater.from(activity).inflate(R.layout.say_item1, null);
			holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
			holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
			holder.tv_read_num = (TextView) convertView.findViewById(R.id.tv_read_num);
			holder.tv_praise_num = (TextView) convertView.findViewById(R.id.tv_praise_num);
			holder.tv_comment_num = (TextView) convertView.findViewById(R.id.tv_comment_num);
			holder.tv_share_num = (TextView) convertView.findViewById(R.id.tv_share_num);
			holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
			holder.ll_pic_area = (LinearLayout) convertView.findViewById(R.id.ll_pic_area);
			holder.gridview = (GridView) convertView.findViewById(R.id.gridview);
			holder.tv_pic_count = (TextView) convertView.findViewById(R.id.tv_pic_count);
			holder.iv_new = (ImageView) convertView.findViewById(R.id.iv_new);
			holder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_praise);
			holder.ll_praise_layout = (LinearLayout) convertView.findViewById(R.id.ll_praise_layout);
			holder.ll_comment_layout = (LinearLayout) convertView.findViewById(R.id.ll_comment_layout);
			holder.iv_red_point = (ImageView) convertView.findViewById(R.id.iv_red_point);
			holder.line_title = convertView.findViewById(R.id.line_title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
			
		BaikeSaysInfo info = lists.get(position);
		holder.tv_content.setText(info.getCONTENT_SHOW());
		holder.tv_read_num.setText(info.getHITS());
		holder.tv_praise_num.setText(info.getPRAISE_NUM());
		holder.tv_comment_num.setText(info.getCOMMENT_NUM());
		holder.tv_share_num.setText(info.getSHARE_COUNT());
		holder.tv_time.setText(info.getCREATE_TIME());
		holder.tv_title.setText(info.getTITLE());
		if(!TextUtils.isEmpty(info.getTITLE())){
			holder.tv_title.setVisibility(View.VISIBLE);
			holder.line_title.setVisibility(View.VISIBLE);
			holder.tv_title.setText(info.getTITLE());
		} else {
			holder.tv_title.setVisibility(View.GONE);
			holder.line_title.setVisibility(View.GONE);
		}
		if(!TextUtils.isEmpty(info.getIS_NEW()) && "1".equals(info.getIS_NEW())){
			holder.iv_new.setVisibility(View.VISIBLE);
		} else {
			holder.iv_new.setVisibility(View.GONE);
		}
		if(info.getUNREAD_COMMENT_COUNT() > 0){
			holder.iv_red_point.setVisibility(View.VISIBLE);
		} else {
			holder.iv_red_point.setVisibility(View.GONE);
		}
		if(info.getIS_PRAISED()){
			holder.iv_praise.setBackgroundResource(R.drawable.praise_ioc_p);
		} else {
			holder.iv_praise.setBackgroundResource(R.drawable.praise_ioc_n);
		}
		
		final int says_id = Integer.parseInt(info.getSAYSID());
		final int pos = position;
		holder.ll_praise_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Message message = new Message();
				message.what = IResult.GIVE_PRAISE_INNER;
				message.arg1 = says_id;
				message.arg2 = pos;
				handler.sendMessage(message);
			}
		});
		
		final BaikeSaysInfo info2 = info;
		//点击评论图标，直接跳到详情页评论的地方
		holder.ll_comment_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(activity, SayDetailsActivity.class);
				intent.putExtra("says_id", info2.getSAYSID());
				intent.putExtra("need_adjust", true);
				activity.startActivityForResult(intent, REQUEST_SAYS_DETAIL);
			}
		});
		
		if(null != info.getPICS_THUMB() && info.getPICS_THUMB().size() > 0){
			holder.ll_pic_area.setVisibility(View.VISIBLE);
			List<Map<String, String>> list2 = new ArrayList<Map<String,String>>();
			for(int i = 0; i < info.getPICS_THUMB().size(); i++){
				if(/*i < 4 && */!TextUtils.isEmpty(info.getPICS_THUMB().get(i))){
					Map<String, String> map = new HashMap<String, String>();
					map.put("path", "");
					map.put("url", info.getPICS_THUMB().get(i));
					map.put("url_big", info.getPICS().get(i));
					list2.add(map);
				}
			}
			PutImageAdapter adapter = new PutImageAdapter(list2, activity, false, 4);
			int wid;
			if(list2.size() > 4){
				holder.gridview.setNumColumns(4);
				wid = 4 * 50 + 3 * 10;
			} else {
				wid = list2.size() * 50 + (list2.size() - 1) * 10;
				holder.gridview.setNumColumns(list2.size());
			}
			holder.gridview.setLayoutParams(new LayoutParams(Util.dip2px(activity, wid), LayoutParams.WRAP_CONTENT));
			
			holder.gridview.setColumnWidth(50);
			holder.gridview.setAdapter(adapter);
			holder.tv_pic_count.setText("共"+list2.size()+"张");
		} else {
			holder.ll_pic_area.setVisibility(View.GONE);
		}
		
		return convertView;
	}
	
	class ViewHolder{
		TextView tv_title;
		TextView tv_content;
		TextView tv_read_num;
		TextView tv_praise_num;
		TextView tv_comment_num;
		TextView tv_share_num;
		TextView tv_time;
		LinearLayout ll_pic_area;
		GridView gridview;
		TextView tv_pic_count;
		ImageView iv_new;
		ImageView iv_red_point;
		ImageView iv_praise;
		LinearLayout ll_comment_layout;
		LinearLayout ll_praise_layout;
		View line_title;
	}
}
