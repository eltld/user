package net.ememed.user2.baike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.DoctorClininActivity;
import net.ememed.user2.baike.entity.DoctorAttentionEntry;
import net.ememed.user2.baike.entity.DoctorAttentionInfo;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MemberInfoEntity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

public class DoctorAttentionActivity extends BasicActivity{
	private RefreshListView listView;
	private String doctorId;
	private String doctor_name;
	private int page = 1;
	private boolean isRefresh = true;
	private List<DoctorAttentionInfo> attentionList;
	private MyAdapter adapter;
	private TextView top_title;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_doctor_attention);
		doctorId = getIntent().getStringExtra("doctor_id");
		doctor_name = getIntent().getStringExtra("doctor_name");
		attentionList = new ArrayList<DoctorAttentionInfo>();
	}
	
	@Override
	protected void setupView() {
		super.setupView();
		
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(doctor_name+"的关注");
		listView = (RefreshListView) findViewById(R.id.listview);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		getDoctorAttention();
	}
	
	@Override
	protected void addListener() {
		super.addListener();
		
		listView.setOnRefreshListener(new IOnRefreshListener() {
			
			@Override
			public void OnRefresh() {
				page = 1;
				isRefresh = true;
				getDoctorAttention();
			}
		});
		
		listView.setOnLoadMoreListener(new IOnLoadMoreListener() {
			
			@Override
			public void OnLoadMore() {
				page += 1;
				isRefresh = false;
				getDoctorAttention();
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent intent = new Intent(DoctorAttentionActivity.this, DoctorClininActivity.class);
				intent.putExtra("doctor_name", doctor_name);
				intent.putExtra("current_service", "1");
				DoctorEntry entry = ((DoctorAttentionInfo)arg0.getAdapter().getItem(arg2)).getDOCTORINFO();
				intent.putExtra("entry", entry);
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 
	 * 获取医生关注的医生列表
	 */
	private void getDoctorAttention(){
		if (NetWorkUtils.detect(DoctorAttentionActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("doctorid", doctorId);
			params.put("pages", ""+page);
		
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_doctor_attention, DoctorAttentionEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GET_DOCTOR_ATTENTION;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							handler.sendMessage(message);
						}
					});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}
	
	
	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		listView.onRefreshComplete();
		switch (msg.what) {
		case IResult.GET_DOCTOR_ATTENTION:
			DoctorAttentionEntry entry = (DoctorAttentionEntry) msg.obj;
			if (null != entry) {
				if (1 == entry.getSuccess()) {
					if(isRefresh){
						attentionList = entry.getData();
					} else {
						attentionList.addAll(entry.getData());
					}
					adapter.notifyDataSetChanged();
				} else {
					showToast(entry.getErrormsg());
				}
				
				if(entry.getPages() <= page){
					listView.onLoadMoreComplete(true);
				} else {
					listView.onLoadMoreComplete(false);
				}
			}
			break;
		case IResult.DATA_ERROR:
			showToast(IMessage.DATA_ERROR);
			break;
		case IResult.NET_ERROR:
			showToast(IMessage.NET_ERROR);
			break;
		default:
			break;
		}
		super.onResult(msg);
	}
	
	class MyAdapter extends BaseAdapter{
		
		@Override
		public int getCount() {
			
			return attentionList.size();
		}

		@Override
		public Object getItem(int position) {
			return attentionList.get(position);
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
				convertView = LayoutInflater.from(DoctorAttentionActivity.this).inflate(R.layout.fans_item, null);
				
				holder.iv_photo = (CircleImageView) convertView.findViewById(R.id.civ_photo);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
				holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			
			DoctorAttentionInfo info = attentionList.get(position);
			DoctorEntry entry = info.getDOCTORINFO();
			imageLoader.displayImage(info.getDOCTORINFO().getAVATAR(), holder.iv_photo, Util.getOptions_avatar());
			holder.tv_name.setText(entry.getREALNAME());
			holder.tv_position.setText(entry.getPROFESSIONAL());
			holder.tv_content.setText(entry.getHOSPITALNAME());
			
			return convertView;
		}
	}
	
	class ViewHolder{
		CircleImageView iv_photo;
		TextView tv_name;
		TextView tv_position;
		TextView tv_content;
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_doctor_attention);
	}
	
}
