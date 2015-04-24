package net.ememed.user2.baike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.DoctorClininActivity;
import net.ememed.user2.baike.entity.BaikeBountyEntry;
import net.ememed.user2.baike.entity.DoctorBountyListEntry;
import net.ememed.user2.baike.entity.DoctorBountyListInfo;
import net.ememed.user2.baike.entity.MyRelativeEntry;
import net.ememed.user2.baike.entity.MyRelativeInfo;
import net.ememed.user2.entity.CommonResponseEntity;
import net.ememed.user2.entity.GuahaoDoctor;
import net.ememed.user2.entity.GuahaoDoctorInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

public class MyRelativeActivity extends BasicActivity{
	public static final int TYPE_MY_PRAISE = 1;
	public static final int TYPE_MY_COMMENT = 2;
	public static final int TYPE_MY_SHARE = 3;
	private List<MyRelativeInfo> list;
	
	private RefreshListView listView;
	private int type;
	private boolean isRefresh = true;
	private int page = 1;
	private MyAdapter adapter;
	private LinearLayout ll_empty;
	private TextView tv_total_num;
	private String deleteCommentId = null;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_my_related);
		type = getIntent().getIntExtra("type", 1);
		list = new ArrayList<MyRelativeInfo>();
	}
	
	public static void startAction(Context context, int type){
		Intent intent = new Intent(context, MyRelativeActivity.class);
		intent.putExtra("type", type);
		context.startActivity(intent);
	}
	
	@Override
	protected void setupView() {
		super.setupView();
		TextView top_title = (TextView) findViewById(R.id.top_title);
		if(TYPE_MY_PRAISE == type){
			top_title.setText(getResources().getString(R.string.my_praise));
		} else if(TYPE_MY_COMMENT == type){
			top_title.setText(getResources().getString(R.string.my_comment));
		} else if(TYPE_MY_SHARE == type){
			top_title.setText(getResources().getString(R.string.my_share));
		}
		
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		tv_total_num = (TextView) findViewById(R.id.tv_total_num);
		
		listView = (RefreshListView)findViewById(R.id.listView_1);
		adapter = new MyAdapter();
		listView.setAdapter(adapter);
		
		getMyRelate(type);
	}
	
	@Override
	protected void addListener() {
		super.addListener();
		
		listView.setOnRefreshListener(new IOnRefreshListener() {
			
			@Override
			public void OnRefresh() {
				isRefresh = true;
				page = 1;
				getMyRelate(type);
			}
		});
		
		listView.setOnLoadMoreListener(new IOnLoadMoreListener() {
			
			@Override
			public void OnLoadMore() {
				isRefresh = false;
				page += 1;
				getMyRelate(type);
				
			}
		});
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				MyRelativeInfo info = (MyRelativeInfo) arg0.getAdapter().getItem(arg2);
				Intent intent = new Intent(MyRelativeActivity.this, SayDetailsActivity.class);
				intent.putExtra("says_id", info.getSAYSID());
				startActivity(intent);
			}
		});
	}
	
	/**
	 * 获取我的点赞，我的评论，我的分享相关信息
	 * @param type （0所有消息，1我赞的，2我评论的，3我分享的）
	 */
	public void getMyRelate(int type) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", ""+type);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_my_related, MyRelativeEntry.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_MY_RELATED;
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
	
	private void delelteComment(String commentid){
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("commentid", commentid);
		
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.delete_comment, CommonResponseEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.DELETE_COMMENT;
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
		case IResult.GET_MY_RELATED:
			MyRelativeEntry entry = (MyRelativeEntry) msg.obj;
			if (entry != null && 1 == entry.getSuccess()) {
				if(isRefresh){
					list = entry.getData();
				} else {
					list.addAll(entry.getData());
				}
				
				if(list.size() > 0){
					ll_empty.setVisibility(View.GONE);
					
					adapter.notifyDataSetChanged();
				} else {
					ll_empty.setVisibility(View.VISIBLE);
				}
				
				if(entry.getCount() > 0){
					tv_total_num.setVisibility(View.VISIBLE);
					setTextForTotalNum(entry.getCount());
				} else {
					tv_total_num.setVisibility(View.GONE);
				}
				
				if(page < entry.getPages()){
					listView.onLoadMoreComplete(false);
				} else {
					listView.onLoadMoreComplete(true);
				}
			}
			break;
		case IResult.DELETE_COMMENT:
			CommonResponseEntity response4 = (CommonResponseEntity)msg.obj;
			if (null != response4) {
				if (1 == response4.getSuccess()) {
					showToast("删除评论成功");
					//需更新评论列表
					updateCommnetList();
					UserPreferenceWrapper.subMyCommentCount();
					setTextForTotalNum(UserPreferenceWrapper.getMyCommentCount());
				} else {
					showToast(response4.getErrormsg());
				}
			}
			break;
		case IResult.DATA_ERROR:
			showToast(getString(R.string.data_error));
			break;
		case IResult.NET_ERROR:
			showToast(getString(R.string.net_error));
			break;
		default:
			break;
		}
	}
	
	/**
	 * 更新评论列表（删除评论后，本地更新列表，不用重新请求服务器）
	 */
	private void updateCommnetList() {
		
		for(int i= 0 ; i < list.size(); i++){
			if(list.get(i).getCOMMENTID().equals(deleteCommentId)){
				list.remove(i);
				break;
			}
		}
		adapter.notifyDataSetChanged();
		deleteCommentId = null;
	}
	
	private void setTextForTotalNum(int num){
		if(TYPE_MY_PRAISE == type){
			tv_total_num.setText("我赞过的总共"+num+"条");
		} else if(TYPE_MY_COMMENT == type){
			tv_total_num.setText("我评论的总共"+num+"条");
		} else if(TYPE_MY_SHARE == type){
			tv_total_num.setText("我分享的总共"+num+"条");
		}
	}
	
	class MyAdapter extends BaseAdapter{

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(null == convertView){
				convertView = LayoutInflater.from(MyRelativeActivity.this).inflate(R.layout.item_my_praise, null);
				holder = new ViewHolder();
				holder.tv_share_to = (TextView) convertView.findViewById(R.id.tv_share_to);
				holder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
				holder.tv_time_up = (TextView) convertView.findViewById(R.id.tv_time_up);
				holder.tv_says_time = (TextView) convertView.findViewById(R.id.tv_says_time);
				holder.tv_author = (TextView) convertView.findViewById(R.id.tv_author);
				holder.tv_refer_author = (TextView) convertView.findViewById(R.id.tv_refer_author);
				holder.tv_refer_content = (TextView) convertView.findViewById(R.id.tv_refer_content);
				holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
				holder.tv_comment_time = (TextView) convertView.findViewById(R.id.tv_comment_time);
				holder.iv_delete = (ImageView) convertView.findViewById(R.id.iv_delete);
				holder.tv_shuoshuo = (TextView) convertView.findViewById(R.id.tv_shuoshuo);
				holder.ll_comment_time = (LinearLayout) convertView.findViewById(R.id.ll_comment_time);
				holder.ll_refer_area = (LinearLayout) convertView.findViewById(R.id.ll_refer_area);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			MyRelativeInfo info = list.get(position);
			
			if(TYPE_MY_PRAISE == type){
				dealMyPraiseItem(holder, info);
			} else if(TYPE_MY_COMMENT == type){
				dealMyCommentItem(holder, info, position);
			} else if(TYPE_MY_SHARE == type){
				dealMyShareItem(holder, info);
			}
			return convertView;
		}
	}
	
	private void dealMyPraiseItem(ViewHolder holder, MyRelativeInfo info){
		holder.tv_share_to.setVisibility(View.GONE);
		holder.ll_refer_area.setVisibility(View.GONE);
		holder.ll_comment_time.setVisibility(View.GONE);
		holder.iv_delete.setVisibility(View.GONE);
		
		holder.tv_comment.setText("赞了一个~");
		holder.tv_time_up.setText(info.getPRAISE_TIME());
		holder.tv_says_time.setText(info.getCREATE_TIME());
		holder.tv_author.setText(info.getDOCTORNAME());
		if(!TextUtils.isEmpty(info.getTITLE_SHOW())){
			holder.tv_title.setVisibility(View.VISIBLE);
			holder.tv_title.setText(info.getTITLE_SHOW());
		} else {
			holder.tv_title.setVisibility(View.GONE);
		}
		holder.tv_shuoshuo.setText(info.getCONTENT_SHOW());
	}
	
	private void dealMyShareItem(ViewHolder holder, MyRelativeInfo info){
		holder.ll_refer_area.setVisibility(View.GONE);
		holder.ll_comment_time.setVisibility(View.GONE);
		holder.iv_delete.setVisibility(View.GONE);
		
		holder.tv_comment.setText(info.getCONTENT_COMMENT().substring(3));
		holder.tv_time_up.setText(info.getSHARE_TIME());
		holder.tv_says_time.setText(info.getCREATE_TIME());
		holder.tv_author.setText(info.getDOCTORNAME());
		if(!TextUtils.isEmpty(info.getTITLE_SHOW())){
			holder.tv_title.setVisibility(View.VISIBLE);
			holder.tv_title.setText(info.getTITLE_SHOW());
		} else {
			holder.tv_title.setVisibility(View.GONE);
		}
		
		if(TextUtils.isEmpty(info.getCONTENT_SHOW())){
			holder.tv_shuoshuo.setVisibility(View.GONE);
		} else {
			holder.tv_shuoshuo.setVisibility(View.VISIBLE);
			holder.tv_shuoshuo.setText(info.getCONTENT_SHOW());
		}
	}
	
	private void dealMyCommentItem(ViewHolder holder, MyRelativeInfo info, int position){
		if(!TextUtils.isEmpty(info.getREFER_COMMENTID())){
			holder.ll_refer_area.setVisibility(View.VISIBLE);
			holder.tv_refer_author.setText("@" + info.getREFER_REALNAME());
			holder.tv_refer_content.setText(info.getREFER_COMMENT());
			holder.tv_comment.setText("评论@"+info.getREFER_REALNAME()+": "+info.getCONTENT_COMMENT());
		} else {
			holder.ll_refer_area.setVisibility(View.GONE);
			holder.tv_comment.setText(info.getCONTENT_COMMENT());
		}
		
		holder.tv_time_up.setVisibility(View.GONE);
		holder.tv_share_to.setVisibility(View.GONE);
		holder.tv_says_time.setText(info.getCREATE_TIME());
		holder.tv_author.setText(info.getDOCTORNAME());
		if(!TextUtils.isEmpty(info.getTITLE_SHOW())){
			holder.tv_title.setVisibility(View.VISIBLE);
			holder.tv_title.setText(info.getTITLE_SHOW());
		} else {
			holder.tv_title.setVisibility(View.GONE);
		}
		holder.tv_shuoshuo.setText(info.getCONTENT_SHOW());
		holder.tv_comment_time.setText(info.getCOMMENT_TIME());
		
		final int pos = position;
		holder.iv_delete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteCommentId = list.get(pos).getCOMMENTID();
				delelteComment(deleteCommentId);
			}
		});
	}
	
	class ViewHolder{
		TextView tv_share_to;
		TextView tv_comment;
		TextView tv_time_up;
		TextView tv_says_time;
		TextView tv_author;
		TextView tv_refer_author;
		TextView tv_refer_content;
		TextView tv_title;
		TextView tv_shuoshuo;
		TextView tv_comment_time;
		ImageView iv_delete;
		LinearLayout ll_comment_time;
		LinearLayout ll_refer_area;
	}
	
}
