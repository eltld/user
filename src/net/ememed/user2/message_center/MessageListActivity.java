package net.ememed.user2.message_center;

import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.CommonResponseEntity;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.message_center.adapter.MessageListAdapter;
import net.ememed.user2.message_center.entity.SmsDetail;
import net.ememed.user2.message_center.entity.SmsListEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MessageListActivity extends BasicActivity{
	
	private TextView tv_title;
	private RefreshListView sms_listview;
	private MessageListAdapter adapter;
	private String sms_type;
	private int page = 1;
	private List<SmsDetail> smsList;
	private String deleteMsgid;
	private AlertDialog myDialog;
	private boolean isRefresh = true;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_message_list);
		sms_type = getIntent().getStringExtra("sms_type");
	
		//registerForContextMenu(sms_listview);
	}
	
	@Override
	protected void setupView() {
		super.setupView();
		
		tv_title = (TextView) findViewById(R.id.top_title);
		tv_title.setText("消息中心");
		
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		
		adapter = new MessageListAdapter(null, this);
		sms_listview = (RefreshListView) findViewById(R.id.sms_listview);
		sms_listview.setAdapter(adapter);
		
		sms_listview.setOnLoadMoreListener(new IOnLoadMoreListener() {
			
			@Override
			public void OnLoadMore() {
				loadMore();
			}
		});
		
		sms_listview.setOnRefreshListener(new IOnRefreshListener() {

			@Override
			public void OnRefresh() {
				refresh();
			}
		});
		
		getMessageList(page);	//获取消息列表
		
	}
	
	@Override
	protected void addListener() {
		super.addListener();
		
		sms_listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
			final int pos = position - 1;  
	       
			showAlertDialogue(pos);
			
	        return true;  
		}
	});
	}
	
	/**
	 * 初始化删除信息的提示框
	 */
	private void showAlertDialogue(final int position){
		if(null == myDialog){
			myDialog = new AlertDialog.Builder(this).create();
			View view = LayoutInflater.from(this).inflate(R.layout.dialog_alert_no_title, null);
			myDialog.setCanceledOnTouchOutside(true);
			Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel);
			Button btn_confirm = (Button) view.findViewById(R.id.btn_confirm);
			btn_cancel.setOnClickListener(new OnClickListener() {
		
				@Override
				public void onClick(View v) {
					myDialog.dismiss();
				}
			});
			btn_confirm.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					deleteMsgid = smsList.get(position).getMSGID();
					deleteMessage(deleteMsgid);
					myDialog.dismiss();
				}
			});
			myDialog.setView(view);
		}
		
		myDialog.show();
	}
	
	/*@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		
		menu.clear();
		menu.setHeaderTitle("删除短信");  
	    menu.add(0, 1, 0, "删除");  
	
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		
		ContextMenuInfo menuInfo = (ContextMenuInfo) item.getMenuInfo();       
	       AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();   
	       int id = (int)info.id;//这里的info.id对应的就是数据库中_id的值  
	         
	    switch(item.getItemId())  
	    {  
	    case 0:  
	    	Log.i("测试","aaa");
	        break;  
	    case 1:  
	    	Log.i("测试","bbb");
	        break;  
	    }  
	    return super.onContextItemSelected(item);  
	}*/
	
	private void loadMore() {
		isRefresh  = false;
		page++;
		getMessageList(page);
	}
	
	private void refresh(){
		isRefresh  = true;
		page = 1;
		getMessageList(page);
	}
	
	private void getMessageList(int page){
		if (NetWorkUtils.detect(MessageListActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("type", "0");
			params.put("sms_type", sms_type);
			params.put("page", ""+page);
			params.put("utype", "user");
			System.out.println("memberid:"+SharePrefUtil.getString(Conast.MEMBER_ID)+"--token:"+SharePrefUtil.getString(Conast.ACCESS_TOKEN)
					+"---type:"+"0"+"--sms_type:"+sms_type+"--page:"+page+"--utype:"+"user");
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_msg_pool_list, SmsListEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GET_MESSGE_LIST;
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
	
	private void deleteMessage(String msgid){
		if (NetWorkUtils.detect(MessageListActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("msgid", msgid);
			
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.delete_msg, CommonResponseEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.DELETE_MESSAGE;
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
		super.onResult(msg);
		try {
			destroyDialog();
			switch (msg.what) {
			case IResult.GET_MESSGE_LIST:
				sms_listview.onRefreshComplete();
				SmsListEntry response = (SmsListEntry) msg.obj;
				if (null != response) {
					if (response.getSuccess() == 1) {
						
						if(isRefresh){
							smsList = response.getData();
						} else {
							smsList.addAll(response.getData());
						}
							
						adapter.change(smsList);
						
						page = Integer.parseInt(response.getCurpage());
						if (page < Integer.parseInt(response.getPages())) {
							sms_listview.onLoadMoreComplete(false);
						} else {
							sms_listview.onLoadMoreComplete(true);
						}
						
						if(response.getData().size() <= 0){
							ll_empty.setVisibility(View.VISIBLE);
							sms_listview.setVisibility(View.GONE);
						} else {
							ll_empty.setVisibility(View.GONE);
							sms_listview.setVisibility(View.VISIBLE);
						}
						
					} else {
						showToast(response.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.DELETE_MESSAGE:
				CommonResponseEntity response1 = (CommonResponseEntity) msg.obj;
				if (null != response1) {
					if (response1.getSuccess() == 1) {
						showToast("删除消息成功");
						updateView();
					} else {
						showToast(response1.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				showToast(getString(R.string.net_error));
				ll_net_unavailable.setVisibility(View.VISIBLE);
				sms_listview.setVisibility(View.GONE);
				break;
			case IResult.DATA_ERROR:
				showToast("获取数据出错！");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void updateView() {
		for(int i = 0; i < smsList.size(); i++){
			if(smsList.get(i).getMSGID().equals(deleteMsgid)){
				smsList.remove(i);
				break;
			}
		}
		
		adapter.change(smsList);
	}

	public void doClick(View view){
		switch(view.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.ll_net_unavailable:
			refresh();
			break;
		}
	}
	
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_msg_pool_list);
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.delete_msg);
	}
}
