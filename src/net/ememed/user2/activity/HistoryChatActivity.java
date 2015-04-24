package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.message.BasicNameValuePair;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Direct;
import com.easemob.chat.EMMessage.Type;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.MessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.util.DateUtils;
import com.google.gson.Gson;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.HistoryChatAdapter;
import net.ememed.user2.activity.adapter.MessageAdapter;
import net.ememed.user2.entity.Attribute2;
import net.ememed.user2.entity.ChatEntry;
import net.ememed.user2.entity.ChatInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.ImInfo;
import net.ememed.user2.entity.ContactEntity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.ImUserInfo;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.MD5;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.RefreshListView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.os.Parcel;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class HistoryChatActivity extends BasicActivity {
	
	private TextView top_title;
	private RefreshListView ptr_listview;
	private String userid;;
	private String doctor_id;
	private String doctor_name;
	private int page =1;
	private HistoryChatAdapter adapter;
	private EMConversation conversation;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_history_chat);
		userid = SharePrefUtil.getString(Conast.MEMBER_ID);
		doctor_id = getIntent().getStringExtra("doctor_id");
		doctor_name = getIntent().getStringExtra("doctor_name");
	}
	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("聊天记录");
		ptr_listview = (RefreshListView) findViewById(R.id.ptr_listview);
		

		if (null == EMChatManager.getInstance() || !EMChatManager.getInstance().isConnected()) {
			loginIM();
		}
		conversation = EMChatManager.getInstance().getConversation(SharePrefUtil.getString(Conast.MEMBER_ID));
	}
	public void doClick(View v){
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:
			onBackPressed();
			break;

		default:
			break;
		}
	}
	@Override
	protected void getData() {
		getImList(page);
		super.getData();
		
	}
	private void getImList(int page){
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
//			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("userid", userid);
			params.put("any_user_id", doctor_id);
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("page", page + "");
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_im_list, ImInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
//							Log.i(TAG, response.toString());
							message.what = IResult.IM_LIST;
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
		switch (msg.what) {
		case IResult.DATA_ERROR:
			break;
		case IResult.NET_ERROR:
			showToast(IMessage.NET_ERROR);
			break;
		case IResult.IM_LIST:
			ImInfo info = (ImInfo) msg.obj;
			if(info.getSuccess().equals("1")){//数据正确
				ChatInfo data = info.getData();
				List<ChatEntry> list = data.getList();
				Map<String, ImUserInfo> userinfo = data.getUserinfo();
				ImUserInfo imUserInfo = userinfo.get(doctor_id);
				List<EMMessage> msgs = transformEMMessages(list);
				EMChatManager mananger = EMChatManager.getInstance();
				for (EMMessage emMessage : msgs) {
					if(emMessage !=null)
						mananger.importMessage(emMessage, true);
				}
				HistoryChatAdapter historyChatAdapter = new HistoryChatAdapter(this,doctor_id,1, imUserInfo.getAVATAR());
				ptr_listview.setAdapter(historyChatAdapter);
				
				
				
//				adapter = new HistoryChatAdapter(this,imUserInfo.getMEMBERNAME(),imUserInfo.getAVATAR(),list);
//				ptr_listview.setAdapter(adapter);
				
				
			}else{
				showToast(info.getErrormsg());
			}
			
			break;

		default:
			break;
		}
		
	}
	private final static int MESSGE_TYPE_TXT = 1;
	private final static int MESSGE_TYPE_IMAGE = 2;
	private final static int MESSGE_TYPE_VOICE = 3;


	private List<EMMessage> transformEMMessages(List<ChatEntry> list) {
		if(list==null ||  list.size()<1){
			return null;
		}
		List<EMMessage> emList = new ArrayList<EMMessage>();
		EMMessage msg = null;
		for (ChatEntry Entry : list) {
			//消息类型
			EMMessage.Type type = null;
			
			int messge_type = 0;
			if (EMMessage.Type.TXT.equals(Entry.getTYPE())) {
				type = EMMessage.Type.TXT;
				messge_type = MESSGE_TYPE_TXT;
			}else if (EMMessage.Type.IMAGE.equals(Entry.getTYPE())) {
				type = EMMessage.Type.IMAGE;
				messge_type = MESSGE_TYPE_IMAGE;
			}else if (EMMessage.Type.VOICE.equals(Entry.getTYPE())) {
				type = EMMessage.Type.VOICE;
				messge_type = MESSGE_TYPE_VOICE;
			}
			//接收方和发送方
			if(SharePrefUtil.getString(Conast.MEMBER_ID).equals(Entry.getSENDER())){//发送
				msg = EMMessage.createSendMessage(type);
				msg.setFrom(SharePrefUtil.getString(Conast.MEMBER_ID));
				msg.setTo(doctor_id);
				msg.setReceipt(EMMessage.Direct.SEND.toString());
				msg.direct = EMMessage.Direct.SEND;
			}else{//接收
				msg = EMMessage.createReceiveMessage(type);
				msg.setFrom(doctor_id);
				msg.setTo(SharePrefUtil.getString(Conast.MEMBER_ID));
				msg.setReceipt(EMMessage.Direct.RECEIVE.toString());
				msg.direct  = EMMessage.Direct.RECEIVE;
			}
			msg.setChatType(ChatType.Chat);
			msg.setMsgTime(Long.parseLong(Entry.getSENDTIME()));
			msg.setMsgId(Entry.getMSG_ID());
//			msg.setReceipt(arg0);
			
			switch (messge_type) {
			case MESSGE_TYPE_TXT:
				TextMessageBody tbody = new TextMessageBody(Entry.getCONTENT());
				msg.addBody(tbody);
				break;
			case MESSGE_TYPE_IMAGE:
				Attribute2 ab2 = Entry.getCONTENT_EXT().get(0);
				ImageMessageBody ibody = new ImageMessageBody();
				ibody.setFileName(ab2.getFilename());
				ibody.setRemoteUrl(ab2.getUrl());
				ibody.setSecret(ab2.getSecret());
				ibody.setThumbnailSecret("");
				ibody.setThumbnailUrl(ab2.getUrl());
//				ibody.setLocalUrl(arg0)
				msg.addBody(ibody);
				break;
			case MESSGE_TYPE_VOICE:
				
				break;

			default:
				break;
			}
			
			
			
			
			emList.add(msg);
			
		}
		
		
		return emList;
	}
	private void loginIM() {
		if (SharePrefUtil.getBoolean(Conast.LOGIN) && !TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
			if (null == EMChatManager.getInstance() || !EMChatManager.getInstance().isConnected()) {
				new Thread() {
					public void run() {
						try {
							// 调用sdk登录方法登录聊天服务器
							EMChatManager.getInstance().login(SharePrefUtil.getString(Conast.MEMBER_ID), MD5.getMD5(SharePrefUtil.getString(Conast.MEMBER_ID)+SharePrefUtil.getString(Conast.USER_Name)+"ememedim"),
									new EMCallBack() {

										@Override
										public void onSuccess() {
											handler.sendEmptyMessage(IResult.LOGIN);
										}

										@Override
										public void onProgress(int progress,
												String status) {
//											Logger.dout(TAG
//													+ " EMChatManager onProgress");
										}

										@Override
										public void onError(int code,
												final String message) {
//											Logger.dout(TAG + " EMChatManager code:"
//													+ code + "onError:" + message);
											if (code == -1) {// code = -1
												// 有可能是应用的环信value出错，有可能是该帐号没注册到环信服务器，所以重新调用接口，让后台把该帐号注册到环信
								reg_to_im();
							}
										}
									});
							
//							HistoryChatActivity.this.registerIMReceiver();
							
						} catch (Exception e1) {
							e1.printStackTrace();
						}
					};
				}.start();
			}
		}
	}
	
//	public void registerIMReceiver() {
//		// 注册接收消息广播
//		NewMessageBroadcastReceiver msgReceiver = new NewMessageBroadcastReceiver();
//		IntentFilter intentFilter = new IntentFilter(EMChatManager.getInstance().getNewMessageBroadcastAction());
//		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
//		intentFilter.setPriority(6);
//		registerReceiver(msgReceiver, intentFilter);
//
//		// 注册一个ack回执消息的BroadcastReceiver
//		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager.getInstance().getAckMessageBroadcastAction());
//		ackMessageIntentFilter.setPriority(6);
//		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
//		// 注册一个监听连接状态的listener
//		EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
//		// 通知sdk，UI 已经初始化完毕，注册了相应的receiver, 可以接受broadcast了
//		EMChat.getInstance().setAppInited();
//	}
	
	/**
	 * 消息广播接收者
	 * 
	 */
//	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String username = intent.getStringExtra("from");
//			String msgid = intent.getStringExtra("msgid");
//			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
//			EMMessage message = EMChatManager.getInstance().getMessage(msgid);
//			if(null != message){
//				if (!username.equals(tochat_userId)) {
//					// 消息不是发给当前会话，return
//					return;
//				}
//				Logger.dout("NewMessageBroadcastReceiver" + "  onReceive()___1111");
//				// conversation =
//				// EMChatManager.getInstance().getConversation(toChatUsername);
//				// 通知adapter有新消息，更新ui
//				if (null != mChatFragment && null!=mChatFragment.adapter && null!=mChatFragment.listView) {
//					Logger.dout("NewMessageBroadcastReceiver" + "  onReceive() refresh ui");
//					mChatFragment.adapter.refresh();	
//					mChatFragment.listView.setSelection(mChatFragment.listView.getCount() - 1);
//				} else {
//					if (mChatFragment == null) {
//						Logger.dout("NewMessageBroadcastReceiver" + " mChatFragment == null");
//					}
//				}
//			}
//			// 记得把广播给终结掉
//			abortBroadcast();
//		}
//	}
	
	private void reg_to_im() {
		try {
			if (NetWorkUtils.detect(getApplicationContext())) {
				new Thread() {
					public void run() {
						ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
						params.add(new BasicNameValuePair("channel", "android"));
						params.add(new BasicNameValuePair("memberid", SharePrefUtil.getString(Conast.MEMBER_ID)));
						params.add(new BasicNameValuePair("utype", "user"));
						try {
							String result = HttpUtil.getString(HttpUtil.URI + HttpUtil.reg_to_im, params, HttpUtil.POST);
							Gson gson = new Gson();
							ResultInfo info = gson.fromJson(result, ResultInfo.class);
							if (null != info && info.getSuccess() == 1) {
								loginIM();
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					};
				}.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
}
