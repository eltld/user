package net.ememed.user2.activity;

import java.io.Serializable;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.MyImageGridViewAdapter;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.fragment.QuestionChatFragment;
import net.ememed.user2.util.Logger;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.nineoldandroids.view.ViewPropertyAnimator;

import de.greenrobot.event.MessageSystemEvent;

public class QuestionChatActivity extends BasicActivity implements OnClickListener {


	private String doctor_name;
	private String doctor_avatar;
	private String tochat_userId;
	private String orderid;
	private String doctorName;
	private QuestionChatFragment mChatFragment;
	private NewMessageBroadcastReceiver msgReceiver;
	
	

	private OrderListEntry entry;
	View rootView ;
	private String evaluation;
	private boolean mIsFollow;
	
	
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_questionchat_1);
		mChatFragment = (QuestionChatFragment) Fragment.instantiate(this,
				QuestionChatFragment.class.getName());
		doctor_avatar = getIntent().getStringExtra("doctor_avatar");
		tochat_userId = getIntent().getStringExtra("tochat_userId");
		entry = (OrderListEntry) getIntent().getSerializableExtra("item");
		mIsFollow = getIntent().getBooleanExtra("isFollow", false);
		evaluation = entry.getEVALUATION();
		orderid = entry.getORDERID();
		doctorName = entry.getDOCTORNAME();
		Bundle bundle_chat = new Bundle();
		bundle_chat.putString("fragmentKey",
				getString(R.string.frag_title_chat));
		bundle_chat.putString("tochat_userId", tochat_userId);
		bundle_chat.putString("doctor_avatar", doctor_avatar);
		bundle_chat.putString("orderid", orderid);
		bundle_chat.putString("doctor_name", doctorName);
		bundle_chat.putString("questionid", entry.getQUESTIONID());
		bundle_chat.putString("evaluation", evaluation);
		bundle_chat.putString("state", entry.getSTATE());
		bundle_chat.putBoolean("isFollow", mIsFollow);
		mChatFragment.setArguments(bundle_chat);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.fragment_layout, mChatFragment).commit();
		initView();
		connectionIM();
		rootView = ((ViewGroup)findViewById(android.R.id.content)).getChildAt(0);
		
		
	}


	private void initView() {
		
		registerIMReceiver();
	}

	public void registerIMReceiver() {
		// 注册接收消息广播
		msgReceiver = new NewMessageBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter(EMChatManager
				.getInstance().getNewMessageBroadcastAction());
		// 设置广播的优先级别大于Mainacitivity,这样如果消息来的时候正好在chat页面，直接显示消息，而不是提示消息未读
		intentFilter.setPriority(6);
		registerReceiver(msgReceiver, intentFilter);

		// 注册一个ack回执消息的BroadcastReceiver
		IntentFilter ackMessageIntentFilter = new IntentFilter(EMChatManager
				.getInstance().getAckMessageBroadcastAction());
		ackMessageIntentFilter.setPriority(6);
		registerReceiver(ackMessageReceiver, ackMessageIntentFilter);
		// 注册一个监听连接状态的listener
		// EMChatManager.getInstance().addConnectionListener(new
		// MyConnectionListener());
		// // 通知sdk，UI 已经初始化完毕，注册了相应的receiver, 可以接受broadcast了
		// EMChat.getInstance().setAppInited();
	}

	/**
	 * 消息广播接收者
	 * 
	 */
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			String username = intent.getStringExtra("from");
			String msgid = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			EMMessage message = EMChatManager.getInstance().getMessage(msgid);
			if (null != message) {
				if (!username.equals(tochat_userId)) {
					// 消息不是发给当前会话，return
					return;
				}
				Logger.dout("NewMessageBroadcastReceiver"
						+ "  onReceive()___1111");
				// conversation =
				// EMChatManager.getInstance().getConversation(toChatUsername);
				// 通知adapter有新消息，更新ui
				if (null != mChatFragment && null != mChatFragment.adapter
						&& null != mChatFragment.listView) {
					Logger.dout("NewMessageBroadcastReceiver"
							+ "  onReceive() refresh ui");
					mChatFragment.adapter.addEMMessage(message);
					mChatFragment.adapter.refresh();
					mChatFragment.listView.setSelection(mChatFragment.listView
							.getCount() - 1);
				} else {
					if (mChatFragment == null) {
						Logger.dout("NewMessageBroadcastReceiver"
								+ " mChatFragment == null");
					}
				}
			}
			// 记得把广播给终结掉
			abortBroadcast();
		}
	}

	/**
	 * 消息回执BroadcastReceiver
	 */
	private BroadcastReceiver ackMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String msgid = intent.getStringExtra("msgid");
			String from = intent.getStringExtra("from");
			EMConversation conversation = EMChatManager.getInstance()
					.getConversation(from);
			if (conversation != null) {
				// 把message设为已读
				EMMessage msg = conversation.getMessage(msgid);
				if (msg != null) {
					// 获取自定义的属性，第2个参数为返回的默认值
					String receive_custom_msg = msg.getStringAttribute(
							"attribute1", null);
					if (!TextUtils.isEmpty(receive_custom_msg)) {
						// System.out.println("receive_custom_msg:"+receive_custom_msg);
					}
					msg.isAcked = true;
				}
			}
			abortBroadcast();
			if (null != mChatFragment && null != mChatFragment.adapter) {
				Logger.dout("ackMessageReceiver" + "  notifyDataSetChanged()");
				mChatFragment.adapter.notifyDataSetChanged();
			}
		}
	};



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		try {
			unregisterReceiver(msgReceiver);
			msgReceiver = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			unregisterReceiver(ackMessageReceiver);
			ackMessageReceiver = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		try {
			switch (msg.what) {
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				break;
			case IResult.SEND_SYS_MSG:
				MessageSystemEvent event = (MessageSystemEvent) msg.obj;
				if (null != mChatFragment) {
					mChatFragment.receiveEventAndSendMsg(event);
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	


	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.toptitle_img_right:

			break;
		case R.id.btn_back:
			finish();
			break;
			
		case R.id.top_layout:

			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		default:
			break;
		}
	}
	
	
	
	
	
}
