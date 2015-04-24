package net.ememed.user2.activity.adapter;



import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.EMMessage.Type;
import com.easemob.exceptions.EaseMobException;
import com.easemob.util.DateUtils;
import com.easemob.util.LatLng;
import com.google.gson.Gson;

import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.HistoryChatActivity;
import net.ememed.user2.activity.ShowBigImage;
import net.ememed.user2.activity.adapter.MessageAdapter.ViewHolder;
import net.ememed.user2.entity.Attribute1;
import net.ememed.user2.entity.Attribute2;
import net.ememed.user2.entity.ChatEntry;
import net.ememed.user2.entity.MessageBackupFieldEntry;
import net.ememed.user2.entity.MessageSystemEntry;
import net.ememed.user2.task.LoadImageTask;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.ImageCache;
import net.ememed.user2.util.ImageUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class HistoryChatAdapter extends BaseAdapter{

	private final static String TAG = "msg";

	private static final int MESSAGE_TYPE_RECV_TXT = 0;
	private static final int MESSAGE_TYPE_SENT_TXT = 1;
	private static final int MESSAGE_TYPE_SENT_IMAGE = 2;
	private static final int MESSAGE_TYPE_SENT_LOCATION = 3;
	private static final int MESSAGE_TYPE_RECV_LOCATION = 4;
	private static final int MESSAGE_TYPE_RECV_IMAGE = 5;
	private static final int MESSAGE_TYPE_SENT_VOICE = 6;
	private static final int MESSAGE_TYPE_RECV_VOICE = 7;

	public static final String IMAGE_DIR = "chat/image/";
	public static final String VOICE_DIR = "chat/audio/";

	private String username;
	private LayoutInflater inflater;

	// reference to conversation object in chatsdk
	private EMConversation conversation;
	private BasicActivity context;
	private String user_avatar;
	private Gson gson;

	public HistoryChatAdapter(BasicActivity context, String username, int chatType,String user_avatar) {
		this.username = username;
		this.context = context;
		this.user_avatar = user_avatar;
		inflater = LayoutInflater.from(context);
		this.conversation = EMChatManager.getInstance().getConversation(username);
		gson = new Gson();
	}

	// public void setUser(String user) {
	// this.user = user;
	// }

	public int getCount() {
		return conversation.getMsgCount();
	}
	
	public void refresh(){
		notifyDataSetChanged();
	}
	
	
	public EMMessage getItem(int position) {
		return conversation.getMessage(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public int getItemViewType(int position) {
		EMMessage message = conversation.getMessage(position);
		if (message.getType() == EMMessage.Type.TXT) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_TXT : MESSAGE_TYPE_SENT_TXT;
		}
		if (message.getType() == EMMessage.Type.IMAGE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_IMAGE : MESSAGE_TYPE_SENT_IMAGE;

		}
		if (message.getType() == EMMessage.Type.LOCATION) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_LOCATION : MESSAGE_TYPE_SENT_LOCATION;
		}
		if (message.getType() == EMMessage.Type.VOICE) {
			return message.direct == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE : MESSAGE_TYPE_SENT_VOICE;

		}

		return -1;// invalid
	}

	public int getViewTypeCount() {
		return 8;
	}

	private View createViewByMessage(EMMessage message, int position) {
		switch (message.getType()) {
		case LOCATION:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_location, null) : inflater.inflate(
					R.layout.row_sent_location, null);
		case IMAGE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_picture, null) : inflater.inflate(
					R.layout.row_sent_picture, null);

		case VOICE:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_voice, null) : inflater.inflate(
					R.layout.row_sent_voice, null);

		default:
			return message.direct == EMMessage.Direct.RECEIVE ? inflater.inflate(R.layout.row_received_message, null) : inflater.inflate(
					R.layout.row_sent_message, null);
		}
	}

	@SuppressLint("NewApi")
	public View getView(final int position, View convertView, ViewGroup parent) {
		final EMMessage message = getItem(position);
		ChatType chatType = message.getChatType();
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = createViewByMessage(message, position);
			if (message.getType() == EMMessage.Type.IMAGE) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.iv_sendPicture));
					holder.head_iv = (CircleImageView) convertView.findViewById(R.id.iv_userhead);
					holder.head_iv.setVisibility(View.VISIBLE);
					holder.tv = (TextView) convertView.findViewById(R.id.percentage);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.progressBar);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
					
					if (message.direct == EMMessage.Direct.SEND) {
						context.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR),holder.head_iv, Util.getOptions_pic());
					} else {
						context.imageLoader.displayImage(user_avatar,holder.head_iv, Util.getOptions_pic());
					}
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (message.getType() == EMMessage.Type.TXT) {
				try {
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.head_iv = (CircleImageView) convertView.findViewById(R.id.iv_userhead);
					holder.head_iv.setVisibility(View.VISIBLE);
					holder.tv = (TextView) convertView.findViewById(R.id.tv_chatcontent);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
					if (message.direct == EMMessage.Direct.SEND) {
						holder.tv.setBackgroundResource(R.drawable.chatto_bg);	
						context.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR),holder.head_iv, Util.getOptions_pic());
					} else {
						holder.tv.setBackgroundResource(R.drawable.chatfrom_bg);	
						context.imageLoader.displayImage(user_avatar,holder.head_iv, Util.getOptions_pic());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (message.getType() == EMMessage.Type.VOICE) {
				try {
					holder.iv = ((ImageView) convertView.findViewById(R.id.iv_voice));
					holder.head_iv = (CircleImageView) convertView.findViewById(R.id.iv_userhead);
					holder.head_iv.setVisibility(View.VISIBLE);
					holder.tv = (TextView) convertView.findViewById(R.id.tv_length);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
					holder.iv_read_status = (ImageView) convertView.findViewById(R.id.iv_unread_voice);
					if (message.direct == EMMessage.Direct.SEND) {
						context.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR),holder.head_iv, Util.getOptions_pic());
					} else {
						context.imageLoader.displayImage(user_avatar,holder.head_iv, Util.getOptions_pic());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else if (message.getType() == EMMessage.Type.LOCATION) {
				try {
					holder.head_iv = (CircleImageView) convertView.findViewById(R.id.iv_userhead);
					holder.head_iv.setVisibility(View.VISIBLE);
					holder.tv = (TextView) convertView.findViewById(R.id.tv_location);
					holder.pb = (ProgressBar) convertView.findViewById(R.id.pb_sending);
					holder.staus_iv = (ImageView) convertView.findViewById(R.id.msg_status);
					holder.tv_userId = (TextView) convertView.findViewById(R.id.tv_userid);
					if (message.direct == EMMessage.Direct.SEND) {
						context.imageLoader.displayImage(SharePrefUtil.getString(Conast.AVATAR),holder.head_iv, Util.getOptions_pic());
					} else {
						context.imageLoader.displayImage(user_avatar,holder.head_iv, Util.getOptions_pic());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 群聊时，显示接收的消息的发送人的名称
		if (chatType == ChatType.GroupChat && message.direct == EMMessage.Direct.RECEIVE)
			holder.tv_userId.setText(message.getFrom());

		// 如果是发送的消息并且不是群聊消息，显示已读textview
		if (message.direct == EMMessage.Direct.SEND && chatType != ChatType.GroupChat) {
			holder.tv_ack = (TextView) convertView.findViewById(R.id.tv_ack);
			if (holder.tv_ack != null) {
				if (message.isAcked) {
					holder.tv_ack.setVisibility(View.VISIBLE);
				} else {
					holder.tv_ack.setVisibility(View.INVISIBLE);
				}
			}
		} else {
			// 如果是文本或者地图消息并且不是group messgae，显示的时候给对方发送已读回执
			if ((message.getType() == Type.TXT || message.getType() == Type.LOCATION) && !message.isAcked
					&& chatType != ChatType.GroupChat) {
				try {
					// 发送已读回执
					message.isAcked = true;
					EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		switch (message.getType()) {
			// 根据消息type显示item
			case IMAGE:
				handleImageMessage(message, holder, position, convertView);
				break;
			case TXT:
				handleTextMessage(message, holder, position);
				break;
			case LOCATION:
				handleLocationMessage(message, holder, position, convertView);
				break;
			case VOICE:
				handleVoiceMessage(message, holder, position, convertView);
				break;
			default:
				// not supported
		}

//		if (message.direct == EMMessage.Direct.SEND) {
//			View statusView = convertView.findViewById(R.id.msg_status);
//
//			statusView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//
//					// 显示重发消息的自定义alertdialog
//					Intent intent = new Intent(context, AlertDialog.class);
//					intent.putExtra("msg", context.getString(R.string.confirm_resend));
//					intent.putExtra("title", context.getString(R.string.resend));
//					intent.putExtra("cancel", true);
//					intent.putExtra("position", position);
//					if (message.getType() == EMMessage.Type.TXT)
//						fragment.startActivityForResult(intent, DoctorChatFragment.REQUEST_CODE_TEXT);
//					else if (message.getType() == EMMessage.Type.VOICE)
//						fragment.startActivityForResult(intent, DoctorChatFragment.REQUEST_CODE_VOICE);
//					else if (message.getType() == EMMessage.Type.IMAGE)
//						fragment.startActivityForResult(intent, DoctorChatFragment.REQUEST_CODE_PICTURE);
//					else if (message.getType() == EMMessage.Type.LOCATION)
//						fragment.startActivityForResult(intent, DoctorChatFragment.REQUEST_CODE_LOCATION);
//					else if (message.getType() == EMMessage.Type.FILE)
//						fragment.startActivityForResult(intent, DoctorChatFragment.REQUEST_CODE_RESEND_NET_DISK);
//					else if (message.getType() == EMMessage.Type.VIDEO)
//						fragment.startActivityForResult(intent, DoctorChatFragment.REQUEST_CODE_VIDEO);
//
//				}
//			});
//
//		} else {
//			holder.head_iv.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					context.mIndicator.setCurrentItem(2);
//				}
//			});
//		}

		TextView timestamp = (TextView) convertView.findViewById(R.id.timestamp);

		if (position == 0) {
			timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
			timestamp.setVisibility(View.VISIBLE);
		} else {
			if (DateUtils.isCloseEnough(message.getMsgTime(), conversation.getMessage(position - 1).getMsgTime())) {
				timestamp.setVisibility(View.GONE);
			} else {
				timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
				timestamp.setVisibility(View.VISIBLE);
			}
		}
		// convertView.setOnClickListener(null);
		return convertView;
	}

	/**
	 * 图片消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleImageMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
		holder.pb.setTag(position);

		if (message.direct == EMMessage.Direct.RECEIVE) {
			System.err.println("it is receive msg");
			if (message.status == EMMessage.Status.INPROGRESS) {
				System.err.println("!!!! back receive");
				holder.iv.setImageResource(R.drawable.album_nophoto);
				showDownloadImageProgress(message, holder);
				// downloadImage(message, holder);
			} else {
				System.err.println("!!!! not back receive, show image directly");
				holder.pb.setVisibility(View.GONE);
				holder.tv.setVisibility(View.GONE);
				holder.iv.setImageResource(R.drawable.album_nophoto);
				ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
				if (imgBody.getLocalUrl() != null) {
					String filePath = imgBody.getLocalUrl();
					String thumbnailPath = ImageUtils.getThumbnailImagePath(filePath);
					showImageView(thumbnailPath, holder.iv, filePath, imgBody.getRemoteUrl(), message);
				}
			}
			return;
		}

		// process send message
		System.err.println("!!!! it is send msg");
		// send pic, show the pic directly
		ImageMessageBody imgBody = (ImageMessageBody) message.getBody();
		String filePath = imgBody.getLocalUrl();
		if (new File(filePath).exists()){
			showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, null, message);
		} else {
			showImageView(ImageUtils.getThumbnailImagePath(filePath), holder.iv, filePath, IMAGE_DIR, message);
		}

		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.tv.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			// set a timer
			final Timer timer = new Timer();
			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					context.runOnUiThread(new Runnable() {
						public void run() {
							holder.pb.setVisibility(View.VISIBLE);
							holder.tv.setVisibility(View.VISIBLE);
							holder.tv.setText(message.progress + "%");
							if (message.status == EMMessage.Status.SUCCESS) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_SUCCESS);
								timer.cancel();
							} else if (message.status == EMMessage.Status.FAIL) {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
								// message.setProgress(0);
								holder.staus_iv.setVisibility(View.VISIBLE);
								Toast.makeText(context,context.getString(R.string.send_fail) + context.getString(R.string.connect_failuer_toast), 0).show();
								timer.cancel();
							}

						}
					});

				}
			}, 0, 500);
			break;
		default:
			sendPictureMessage(message, holder);
		}
	}

	/**
	 * 语音消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleVoiceMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
		VoiceMessageBody voiceBody = (VoiceMessageBody) message.getBody();
		holder.tv.setText(voiceBody.getLength() + "\"");
		holder.iv.setOnClickListener(new VoicePlayClickListener(message, holder.iv, holder.iv_read_status, context, context, username));

		if (message.direct == EMMessage.Direct.RECEIVE) {
			if (message.isAcked) {
				// 隐藏语音未读标志
				holder.iv_read_status.setVisibility(View.INVISIBLE);
			} else {
				holder.iv_read_status.setVisibility(View.VISIBLE);
			}
			return;
		}

		// until here, deal with send voice msg
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			break;
		default:
			sendMsgInBackground(message, holder);
		}
	}

	/**
	 * 处理位置消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 * @param convertView
	 */
	private void handleLocationMessage(final EMMessage message, final ViewHolder holder, final int position, View convertView) {
		TextView locationView = ((TextView) convertView.findViewById(R.id.tv_location));
		LocationMessageBody locBody = (LocationMessageBody) message.getBody();
		locationView.setText(locBody.getAddress());
		LatLng loc = new LatLng(locBody.getLatitude(), locBody.getLongitude());
//		locationView.setOnClickListener(new MapClickListener(loc, locBody.getAddress()));

		if (message.direct == EMMessage.Direct.RECEIVE) {
			return;
		}
		// deal with send message
		switch (message.status) {
		case SUCCESS:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.GONE);
			break;
		case FAIL:
			holder.pb.setVisibility(View.GONE);
			holder.staus_iv.setVisibility(View.VISIBLE);
			break;
		case INPROGRESS:
			break;
		default:
			sendMsgInBackground(message, holder);
		}
	}

	/**
	 * 文本消息
	 * 
	 * @param message
	 * @param holder
	 * @param position
	 */
	private void handleTextMessage(EMMessage message, ViewHolder holder, final int position) {
		TextMessageBody txtBody = (TextMessageBody) message.getBody();
		String txtContent = txtBody.getMessage();
		try {
			String extStr = message.getStringAttribute("ext");
			MessageBackupFieldEntry entry = gson.fromJson(extStr, MessageBackupFieldEntry.class);
			if (null != entry) {
				if (!TextUtils.isEmpty(entry.getISSYSTEMMSG()) && "1".equals(entry.getISSYSTEMMSG())) {
					holder.head_iv.setVisibility(View.INVISIBLE);
//					txtContent = txtBody.getMessage().replaceAll("\\\\\"", "\"");
					MessageSystemEntry systemEntry = gson.fromJson(txtContent, MessageSystemEntry.class);
					txtContent = systemEntry.getDoctor_msg();
					holder.tv.setBackgroundResource(R.drawable.button_white_bg);	
				} else {
					holder.head_iv.setVisibility(View.VISIBLE);
					if (message.direct == EMMessage.Direct.SEND) {
						holder.tv.setBackgroundResource(R.drawable.chatto_bg);
					} else {
						holder.tv.setBackgroundResource(R.drawable.chatfrom_bg);
					}
				}
			} else {
				if (message.direct == EMMessage.Direct.SEND) {
					holder.tv.setBackgroundResource(R.drawable.chatto_bg);
				} else {
					holder.tv.setBackgroundResource(R.drawable.chatfrom_bg);
				}
				holder.head_iv.setVisibility(View.VISIBLE);
			}
		} catch (EaseMobException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
		holder.tv.setText(txtContent);
		if (message.direct == EMMessage.Direct.SEND) {
			switch (message.status) {
			case SUCCESS:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.GONE);
				break;
			case FAIL:
				holder.pb.setVisibility(View.GONE);
				holder.staus_iv.setVisibility(View.VISIBLE);
				break;
			case INPROGRESS:
				break;
			default:
				sendMsgInBackground(message, holder);
			}
		}
	}

	/**
	 * 发送消息
	 * 
	 * @param message
	 * @param holder
	 */
	public void sendMsgInBackground(final EMMessage message, final ViewHolder holder) {
		holder.staus_iv.setVisibility(View.GONE);
		holder.pb.setVisibility(View.VISIBLE);
//		if (chatType == ChatActivity.CHATTYPE_SINGLE) {
			// 单聊
			EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

				@Override
				public void onSuccess() {
					updateSendedView(message, holder);
				}

				@Override
				public void onError(int code, String error) {
					updateSendedView(message, holder);
				}

				@Override
				public void onProgress(int progress, String status) {
				}

			});
//		} else {
//			// 群聊
//			EMChatManager.getInstance().sendGroupMessage(message, new EMCallBack() {
//
//				@Override
//				public void onSuccess() {
//					updateSendedView(message, holder);
//				}
//
//				@Override
//				public void onError(int code, String error) {
//					updateSendedView(message, holder);
//				}
//
//				@Override
//				public void onProgress(int progress, String status) {
//				}
//
//			});
//		}

	}

	/*
	 * chat sdk will automatic download thumbnail image for the image message we
	 * need to register callback show the download progress
	 */
	private void showDownloadImageProgress(final EMMessage message, final ViewHolder holder) {
		System.err.println("!!! show download image progress");
		final ImageMessageBody msgbody = (ImageMessageBody) message.getBody();
		msgbody.setDownloadCallback(new EMCallBack() {

			@Override
			public void onSuccess() {
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						// message.setBackReceive(false);
						holder.pb.setVisibility(View.GONE);
						holder.tv.setVisibility(View.GONE);
						notifyDataSetChanged();
					}
				});
			}

			@Override
			public void onError(int code, String message) {

			}

			@Override
			public void onProgress(final int progress, String status) {
				context.runOnUiThread(new Runnable() {
					@Override
					public void run() {
						holder.tv.setText(progress + "%");

					}
				});
			}

		});
	}

	/*
	 * send message with new sdk
	 */
	private void sendPictureMessage(final EMMessage message, final ViewHolder holder) {

		try {
			String to = message.getTo();

			// before send, update ui
			holder.staus_iv.setVisibility(View.GONE);
			holder.pb.setVisibility(View.VISIBLE);
			holder.tv.setVisibility(View.VISIBLE);
			holder.tv.setText("0%");
//			if (chatType == ChatActivity.CHATTYPE_SINGLE) {
				EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

					@Override
					public void onSuccess() {
						Log.d(TAG, "send image message successfully");
						context.runOnUiThread(new Runnable() {
							public void run() {
								// send success
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
							}
						});
					}

					@Override
					public void onError(int code, String error) {
						context.runOnUiThread(new Runnable() {
							public void run() {
								holder.pb.setVisibility(View.GONE);
								holder.tv.setVisibility(View.GONE);
								// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
								holder.staus_iv.setVisibility(View.VISIBLE);
								Toast.makeText(context,
										context.getString(R.string.send_fail) + context.getString(R.string.connect_failuer_toast), 0)
										.show();
							}
						});
					}

					@Override
					public void onProgress(final int progress, String status) {
						context.runOnUiThread(new Runnable() {
							public void run() {
								holder.tv.setText(progress + "%");
							}
						});
					}

				});
//			} else {
//				EMChatManager.getInstance().sendGroupMessage(message, new EMCallBack() {
//
//					@Override
//					public void onSuccess() {
//						Log.d(TAG, "send image message successfully");
//						activity.runOnUiThread(new Runnable() {
//							public void run() {
//								// send success
//								holder.pb.setVisibility(View.GONE);
//								holder.tv.setVisibility(View.GONE);
//							}
//						});
//					}
//
//					@Override
//					public void onError(int code, String error) {
//						activity.runOnUiThread(new Runnable() {
//							public void run() {
//								holder.pb.setVisibility(View.GONE);
//								holder.tv.setVisibility(View.GONE);
//								// message.setSendingStatus(Message.SENDING_STATUS_FAIL);
//								holder.staus_iv.setVisibility(View.VISIBLE);
//								Toast.makeText(activity,
//										activity.getString(R.string.send_fail) + activity.getString(R.string.connect_failuer_toast), 0)
//										.show();
//							}
//						});
//					}
//
//					@Override
//					public void onProgress(final int progress, String status) {
//						activity.runOnUiThread(new Runnable() {
//							public void run() {
//								holder.tv.setText(progress + "%");
//							}
//						});
//					}
//
//				});
//			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 更新ui上消息发送状态
	 * 
	 * @param message
	 * @param holder
	 */
	private void updateSendedView(final EMMessage message, final ViewHolder holder) {
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				// message.setBackSend(false);
				// send success
				if (message.status == EMMessage.Status.SUCCESS) {
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.GONE);
				} else if (message.status == EMMessage.Status.FAIL) {
					holder.pb.setVisibility(View.GONE);
					holder.staus_iv.setVisibility(View.VISIBLE);
					Toast.makeText(context, context.getString(R.string.send_fail) + context.getString(R.string.connect_failuer_toast), 0)
							.show();
				}
			}
		});
	}

	/**
	 * load image into image view
	 * 
	 * @param thumbernailPath
	 * @param iv
	 * @param position
	 * @return the image exists or not
	 */
	private boolean showImageView(final String thumbernailPath, final ImageView iv, final String localFullSizePath, String remoteDir,
			final EMMessage message) {
		String imagename = localFullSizePath.substring(localFullSizePath.lastIndexOf("/") + 1, localFullSizePath.length());
		// final String remote = remoteDir != null ? remoteDir+imagename :
		// imagename;
		final String remote = remoteDir;

		// first check if the thumbnail image already loaded into cache
		Bitmap bitmap = ImageCache.getInstance().get(thumbernailPath);
		if (bitmap != null) {
			// thumbnail image is already loaded, reuse the drawable
			iv.setImageBitmap(bitmap);
			iv.setClickable(true);
			iv.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					System.err.println("image view on click");
					Intent intent = new Intent(context, ShowBigImage.class);
					File file = new File(localFullSizePath);
					if (file.exists()) {
						Uri uri = Uri.fromFile(file);
						intent.putExtra("uri", uri);
						System.err.println("here need to check why download everytime");
					} else {
						// The local full size pic does not exist yet.
						// ShowBigImage needs to download it from the server
						// first
						// intent.putExtra("", message.get);
						ImageMessageBody body = (ImageMessageBody) message.getBody();
						intent.putExtra("secret", body.getSecret());
						intent.putExtra("remotepath", remote);
					}
					if (message != null && message.direct == EMMessage.Direct.RECEIVE && !message.isAcked && message.getChatType() != ChatType.GroupChat) {
						message.isAcked = true;
						try {
							EMChatManager.getInstance().ackMessageRead(message.getFrom(), message.getMsgId());
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					context.startActivity(intent);
				}
			});
			return true;
		} else {
			new LoadImageTask().execute(thumbernailPath, localFullSizePath, remote, message.getChatType(), iv, context, message);
			return true;
		}

	}

	public static class ViewHolder {
		ImageView iv;
		TextView tv;
		ProgressBar pb;
		ImageView staus_iv;
		CircleImageView head_iv;
		TextView tv_userId;
		ImageView playBtn;
		TextView timeLength;
		TextView size;
		LinearLayout container_status_btn;
		ImageView iv_read_status;
		TextView tv_ack;
	}

//	/*
//	 * 点击地图消息listener
//	 */
//	class MapClickListener implements View.OnClickListener {
//
//		LatLng location;
//		String address;
//
//		public MapClickListener(LatLng loc, String address) {
//			location = loc;
//			this.address = address;
//
//		}
//
//		@Override
//		public void onClick(View v) {
//			Intent intent;
//			intent = new Intent(context, BaiduMapActivity.class);
//			intent.putExtra("latitude", location.latitude);
//			intent.putExtra("longitude", location.longitude);
//			intent.putExtra("address", address);
//			context.startActivity(intent);
//		}
//
//	}

}
