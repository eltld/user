package net.ememed.user2.fragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.Interface.LoadInterface;
import net.ememed.user2.activity.BuyCustomActivity;
import net.ememed.user2.activity.BuyJiuyiActivity;
import net.ememed.user2.activity.BuyPhoneConsultActivity;
import net.ememed.user2.activity.BuyShangmenActivity;
import net.ememed.user2.activity.BuyTextConsultActivity;
import net.ememed.user2.activity.BuyZhuyuanActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.activity.MicroChatActivity;
import net.ememed.user2.activity.PersonInfoActivity;
import net.ememed.user2.activity.PrivateDoctorActivity;
import net.ememed.user2.activity.adapter.MicroMessageAdapter;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.IMMessageInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MemberInfo;
import net.ememed.user2.entity.MemberInfoEntity;
import net.ememed.user2.entity.OfferServiceEntry;
import net.ememed.user2.entity.OfferServiceEntry2;
import net.ememed.user2.entity.OfferServicePacketEntry;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.MD5;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CommentDialog;
import net.ememed.user2.widget.MenuDialog;
import net.ememed.user2.widget.PasteEditText;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

import org.apache.http.message.BasicNameValuePair;

import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.LocationMessageBody;
import com.easemob.chat.TextMessageBody;
import com.easemob.chat.VoiceMessageBody;
import com.easemob.util.PathUtil;
import com.easemob.util.VoiceRecorder;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import de.greenrobot.event.MessageSystemEvent;
import de.greenrobot.event.util.TransformEMMessage;

/** 聊天详情页 */
public class MicroChatFragment extends Fragment implements Handler.Callback,
		OnClickListener, OnRefreshListener, BasicUIEvent ,LoadInterface {

	private static final int REQUEST_CODE_EMPTY_HISTORY = 2;
	public static final int REQUEST_CODE_CONTEXT_MENU = 3;
	private static final int REQUEST_CODE_MAP = 4;
	public static final int REQUEST_CODE_TEXT = 5;
	public static final int REQUEST_CODE_VOICE = 6;
	public static final int REQUEST_CODE_PICTURE = 7;
	public static final int REQUEST_CODE_LOCATION = 8;
	public static final int REQUEST_CODE_NET_DISK = 9;
	public static final int REQUEST_CODE_RESEND_NET_DISK = 10;
	public static final int REQUEST_CODE_COPY_AND_PASTE = 11;
	public static final int REQUEST_CODE_PICK_VIDEO = 12;
	public static final int REQUEST_CODE_DOWNLOAD_VIDEO = 13;
	public static final int REQUEST_CODE_VIDEO = 14;
	public static final int REQUEST_CODE_DOWNLOAD_VOICE = 15;
	public static final int REQUEST_CODE_SELECT_USER_CARD = 16;
	public static final int REQUEST_CODE_SEND_USER_CARD = 17;
	public static final int REQUEST_CODE_CAMERA = 18;
	public static final int REQUEST_CODE_LOCAL = 19;
	public static final int REQUEST_CODE_CLICK_DESTORY_IMG = 20;
	public static final int REQUEST_CODE_GROUP_DETAIL = 21;
	public static final int RESULT_CODE_COPY = 1;
	public static final int RESULT_CODE_DELETE = 2;
	public static final int RESULT_CODE_FORWARD = 3;
	public static final int RESULT_CODE_OPEN = 4;
	public static final int RESULT_CODE_DWONLOAD = 5;
	public static final int RESULT_CODE_TO_CLOUD = 6;
	public static final int RESULT_CODE_EXIT_GROUP = 7;
	public static final int CHATTYPE_SINGLE = 1;
	public static final int CHATTYPE_GROUP = 2;
	public static final String COPY_IMAGE = "EASEMOBIMG";
	public static final String TAG = DoctorChatFragment.class.getSimpleName();
	private View recordingContainer;
	private ImageView micImage;
	private TextView recordingHint;
	public RefreshListView listView;

	private PasteEditText mEditTextContent;
	private View buttonSetModeKeyboard;
	private View buttonSetModeVoice;
	private View buttonSend;
	private View buttonPressToSpeak;
	private LinearLayout expressionContainer;
	private View more;
	private int position;
	private ClipboardManager clipboard;
	private InputMethodManager manager;
	private Drawable[] micImages;
	private int chatType;
	private EMConversation conversation;

	// 给谁发送消息
	private String toChatUsername;
	private VoiceRecorder voiceRecorder;
	public MicroMessageAdapter adapter;
	private File cameraFile;
	public static int resendPos;
	private RelativeLayout edittext_layout;
	private ProgressBar loadmorePB;
	private boolean isloading;
	private final int pagesize = 20;
	private boolean haveMoreData = true;

	private final int TEXT = 1;
	private final int CALL = 2;
	private final int JIUYI = 3;
	private final int SHANGMEN = 4;
	private final int ZHUYUAN = 14;
	private final int PRIVATE_DOCTOR = 15;
	private final int CUSTOM = 16;

	private int chatPage = 0;

	private Handler micImageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			// 切换msg切换图片
			micImage.setImageDrawable(micImages[msg.what]);
		}
	};

	private MicroChatActivity activity = null;
	private Handler mHandler;
	private LinearLayout ll_view_content;
	private Button bt_more;
	private Button btn_send;
	public TextView tv_error;
	private String doctor_avatar;
	private String doctor_name;
	private String orderid = "-1";

	// 服务订单

	private LinearLayout gridView_layout;
	private GridView order_gradView;
	private TextView system_tx;

	private MemberInfo memberInfo;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MicroChatActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Logger.dout(TAG + " onCreate");
		mHandler = new Handler(this);
		activity = (MicroChatActivity) getActivity();
		doctor_avatar = getArguments().getString("doctor_avatar");
		doctor_name = getArguments().getString("doctor_name");
		memberInfo = (MemberInfo) getArguments().getSerializable("memberInfo");

		// 判断单聊还是群聊
		chatType = activity.getIntent()
				.getIntExtra("chatType", CHATTYPE_SINGLE);
		if (chatType == CHATTYPE_SINGLE) { // 单聊
			toChatUsername = getArguments().getString("tochat_userId");
		} else {
			// 群聊
			toChatUsername = getArguments().getString("groupId");
		}
		if (EMChatManager.getInstance() != null
				&& EMChatManager.getInstance().isConnected())
			EMChatManager.getInstance().getConversation(
					SharePrefUtil.getString(Conast.MEMBER_ID));
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.dout(TAG + " onCreateView");
		View view = inflater.inflate(R.layout.root_layout, null);
		FrameLayout mContentView = (FrameLayout) view
				.findViewById(R.id.mainView);
		ll_view_content = (LinearLayout) LayoutInflater.from(activity).inflate(
				R.layout.fragment_microchat, null);
		mContentView.addView(ll_view_content);
		setUpView();
		initView(ll_view_content);
		getIMData("0");
		// if(state.equals("5")){
		// setBottomByOpenOrder(false);
		// }else{
		// setBottomByOpenOrder(true);
		// }
		return view;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	/**
	 * initView
	 */
	protected void initView(View view) {
		recordingContainer = view.findViewById(R.id.recording_container);
		micImage = (ImageView) view.findViewById(R.id.mic_image);
		recordingHint = (TextView) view.findViewById(R.id.recording_hint);
		system_tx = (TextView) view.findViewById(R.id.system_tx);
		gridView_layout = (LinearLayout) view
				.findViewById(R.id.gridView_layout);
		order_gradView = (GridView) view.findViewById(R.id.order_gradView);
		// 对话的列表
		listView = (RefreshListView) view.findViewById(R.id.list);
		// 底部发送栏
		rl_bottom = (LinearLayout) view.findViewById(R.id.rl_bottom);
		// 底部服务栏
		more = view.findViewById(R.id.more);

		ll_bottom_btn = (LinearLayout) view.findViewById(R.id.ll_bottom_btn);
		btn_open_more = (Button) view.findViewById(R.id.btn_open_more);
		// btn_open_more.setText("再次对" + doctor_name + "医生发起咨询");

		mEditTextContent = (PasteEditText) view
				.findViewById(R.id.et_sendmessage);
		buttonSetModeKeyboard = view.findViewById(R.id.btn_set_mode_keyboard);
		edittext_layout = (RelativeLayout) view
				.findViewById(R.id.edittext_layout);
		buttonSetModeVoice = view.findViewById(R.id.btn_set_mode_voice);
		buttonSend = view.findViewById(R.id.btn_send);
		buttonPressToSpeak = view.findViewById(R.id.btn_press_to_speak);
		expressionContainer = (LinearLayout) view
				.findViewById(R.id.ll_face_container);

		loadmorePB = (ProgressBar) view.findViewById(R.id.pb_load_more);
		bt_more = (Button) view.findViewById(R.id.bt_more);
		btn_send = (Button) view.findViewById(R.id.btn_send);
		btn_set_camera = (Button) view.findViewById(R.id.btn_set_camera);
		tv_error = (TextView) view.findViewById(R.id.tv_error);
		mEditTextContent.setOnClickListener(this);
		btn_set_camera.setOnClickListener(this);

		/* bt_more.setOnClickListener(this); */
		bt_more.setVisibility(View.GONE);
		buttonSend.setOnClickListener(this);
		btn_open_more.setOnClickListener(this);
		buttonSetModeVoice.setOnClickListener(this);
		buttonSetModeKeyboard.setOnClickListener(this);
		btn_send.setOnClickListener(this);

		tv_error.setOnClickListener(this);
		// 动画资源文件,用于录制语音时
		micImages = new Drawable[] {
				getResources().getDrawable(R.drawable.record_animate_01),
				getResources().getDrawable(R.drawable.record_animate_02),
				getResources().getDrawable(R.drawable.record_animate_03),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_04),
				getResources().getDrawable(R.drawable.record_animate_05),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_06),
				getResources().getDrawable(R.drawable.record_animate_07),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_08),
				getResources().getDrawable(R.drawable.record_animate_09),
				getResources().getDrawable(R.drawable.record_animate_10),
				getResources().getDrawable(R.drawable.record_animate_11) };

		voiceRecorder = new VoiceRecorder(micImageHandler);
		buttonPressToSpeak.setOnTouchListener(new PressToSpeakListen());

		listView.setOnRefreshListener(new IOnRefreshListener() {

			@Override
			public void OnRefresh() {
				chatPage++;
				getIMData(chatPage + "");

			}
		});

		int count = listView.getCount();
		if (count > 0) {
			listView.setSelection(count - 1);
		}
		listView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				hideKeyboard();
				more.setVisibility(View.GONE);
				expressionContainer.setVisibility(View.GONE);
				// btnContainer1.setVisibility(View.GONE);
				return false;
			}
		});
		
		
		// 监听文字框
		mEditTextContent.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (!TextUtils.isEmpty(s)) {
					// buttonSetModeVoice.setVisibility(View.GONE);
					buttonSend.setVisibility(View.VISIBLE);
					/* bt_more.setVisibility(View.GONE); */
				} else {
					if (buttonSetModeKeyboard.getVisibility() != View.VISIBLE) {
						// buttonSetModeVoice.setVisibility(View.VISIBLE);
						buttonSend.setVisibility(View.GONE);
						/* bt_more.setVisibility(View.VISIBLE); */
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		// 服务订单
		data = memberInfo.getData();

		String ENABLE_WEITALK = data.getENABLE_WEITALK();

		if (ENABLE_WEITALK.equals("0") || ENABLE_WEITALK.equals("-1")) {
			if (null != memberInfo.getData().getOFFER_SERVICE()) {
				gridView_layout.setVisibility(View.VISIBLE);
				initServer(memberInfo);
				setBottomByOpenOrder(false);
			}
			if(ENABLE_WEITALK.equals("0")){
				system_tx.setVisibility(View.VISIBLE);
				system_tx.setText("该医生未开通免费微聊，不能进行聊天，请通过以下服务项目与医生沟通！");
			}
			if(ENABLE_WEITALK.equals("-1")){
				system_tx.setVisibility(View.VISIBLE);
				system_tx.setText("该医生现在不方便接收您的即时聊天消息，请通过以下服务项目与医生沟通！");
			}
		}

		order_gradView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				go2BuyServiceAct(serverList.get(position));
			}
		});
	}

	private void setUpView() {
		// position = getIntent().getIntExtra("position", -1);
		clipboard = (ClipboardManager) activity
				.getSystemService(Context.CLIPBOARD_SERVICE);
		manager = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		activity.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

		conversation = EMChatManager.getInstance().getConversation(
				toChatUsername);
		// conversation.getAllMessages();
		// 把此会话的未读数置为0
		conversation.resetUnsetMsgCount();
		// adapter = new MessageAdapter(this, activity, toChatUsername,
		// chatType, doctor_avatar,orderid);
		// 显示消息
		// listView.setAdapter(adapter);
		// listView.setOnScrollListener(new ListScrollListener());


	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CODE_EXIT_GROUP) {
			activity.setResult(activity.RESULT_OK);
			activity.finish();
			return;
		}
		if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
			switch (resultCode) {
			case RESULT_CODE_COPY: // 复制消息
				EMMessage copyMsg = ((EMMessage) adapter.getItem(data
						.getIntExtra("position", -1)));
				if (copyMsg.getType() == EMMessage.Type.IMAGE) {
					ImageMessageBody imageBody = (ImageMessageBody) copyMsg
							.getBody();
					// 加上一个特定前缀，粘贴时知道这是要粘贴一个图片
					clipboard.setText(COPY_IMAGE + imageBody.getLocalUrl());
				} else {
					// clipboard.setText(SmileUtils.getSmiledText(ChatActivity.this,
					// ((TextMessageBody) copyMsg.getBody()).getMessage()));
					clipboard.setText(((TextMessageBody) copyMsg.getBody())
							.getMessage());
				}
				break;
			case RESULT_CODE_DELETE: // 删除消息f
				EMMessage deleteMsg = (EMMessage) adapter.getItem(data
						.getIntExtra("position", -1));
				conversation.removeMessage(deleteMsg.getMsgId());
				adapter.deleteEMMessage(data.getIntExtra("position", -1));
				adapter.refresh();
				listView.setSelection(data.getIntExtra("position",
						adapter.getCount()) - 1);
				break;

			case RESULT_CODE_FORWARD: // 转发消息
				// EMMessage forwardMsg = (EMMessage)
				// adapter.getItem(data.getIntExtra("position", 0));
				// Intent intent = new Intent(activity,
				// ForwardMessageActivity.class);
				// intent.putExtra("forward_msg_id", forwardMsg.getMsgId());
				// startActivity(intent);
				break;
			default:
				break;
			}
		}
		if (resultCode == activity.RESULT_OK) { // 清空消息
			if (requestCode == REQUEST_CODE_EMPTY_HISTORY) {
				// 清空会话
				EMChatManager.getInstance().clearConversation(toChatUsername);
				adapter.refresh();
			} else if (requestCode == REQUEST_CODE_CAMERA) { // 发送照片
				if (cameraFile != null && cameraFile.exists()) {
					String extString = generateExtMessage();
					sendPicture(cameraFile.getAbsolutePath(), extString);
				}
			} else if (requestCode == REQUEST_CODE_LOCAL) { // 发送本地图片
				if (data != null) {
					Uri selectedImage = data.getData();
					if (selectedImage != null)
						sendPicByUri(selectedImage);
				}
			} else if (requestCode == REQUEST_CODE_MAP) { // 地图
				double latitude = data.getDoubleExtra("latitude", 0);
				double longitude = data.getDoubleExtra("longitude", 0);
				String locationAddress = data.getStringExtra("address");
				if (locationAddress != null && !locationAddress.equals("")) {
					more(more);
					sendLocationMsg(latitude, longitude, "", locationAddress);
				} else {
					Toast.makeText(activity, "无法获取到您的位置信息！", Toast.LENGTH_LONG)
							.show();
				}
				// 重发消息
			} else if (requestCode == REQUEST_CODE_TEXT) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_VOICE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_PICTURE) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_LOCATION) {
				resendMessage();
			} else if (requestCode == REQUEST_CODE_COPY_AND_PASTE) {
				// 粘贴
				if (!TextUtils.isEmpty(clipboard.getText())) {
					String pasteText = clipboard.getText().toString();
					if (pasteText.startsWith(COPY_IMAGE)) {
						String extString = generateExtMessage();
						// 把图片前缀去掉，还原成正常的path
						sendPicture(pasteText.replace(COPY_IMAGE, ""),
								extString);
					}
				}
			} else if (conversation.getMsgCount() > 0) {
				adapter.refresh();
				activity.setResult(activity.RESULT_OK);
			} else if (requestCode == REQUEST_CODE_GROUP_DETAIL) {
				EMChatManager.getInstance().getConversation(toChatUsername);
				adapter.refresh();
			}
		}
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.btn_send) {// 点击发送按钮(发文字和表情)
			String s = mEditTextContent.getText().toString().trim();
			String obj = generateExtMessage();
			if (TextUtils.isEmpty(s)) {
				activity.showToast(getString(R.string.input_content));
				return;
			}

			try {
				if (EMChatManager.getInstance() != null
						&& EMChatManager.getInstance().isConnected()) {
					if (!TextUtils.isEmpty(EMChatManager.getInstance()
							.getCurrentUser())) {
						InputMethodManager imm = (InputMethodManager) activity
								.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.toggleSoftInput(0,
								InputMethodManager.HIDE_NOT_ALWAYS);
						sendText(s, obj);
					} else {
					}
					loginIM();
				} else {
					activity.showToast("正在登录聊天服务器，请稍后重试");
					loginIM();
				}
			} catch (Exception e) {
				Toast.makeText(activity, "发送失败，请稍后重试", 0).show();
				loginIM();
				showExitTips();
			}

		} else if (id == R.id.btn_set_camera) {
			sendPictures();

		} else if (id == R.id.et_sendmessage) {
			editClick(v);
		} else if (id == R.id.bt_more || id == R.id.btn_open_more) {
			more(v);
		} else if (id == R.id.btn_set_mode_voice) {
			setModeVoice(v);
		} else if (id == R.id.btn_set_mode_keyboard) {
			setModeKeyboard(v);
		} else if (id == R.id.tv_error) {
			// loginIM();
		}
	}

	CommentDialog dialog;

	boolean isComment;

	/***
	 * 附加到聊天消息中的备用字段
	 * 
	 * @return
	 */
	public String generateExtMessage() {
		JsonObject obj = new JsonObject();
		obj.addProperty("SERVICEID", "");
		obj.addProperty("ORDERTYPE", "");
		obj.addProperty("PACKET_BUY_ID", "");
		obj.addProperty("DOCTORID", toChatUsername);
		obj.addProperty("user_avatar", SharePrefUtil.getString(Conast.AVATAR));
		obj.addProperty("doctor_name", doctor_name);
		obj.addProperty("user_name", SharePrefUtil.getString(Conast.realName));
		obj.addProperty("doctor_avatar", doctor_avatar);
		obj.addProperty("USERID", SharePrefUtil.getString(Conast.MEMBER_ID));
		obj.addProperty("ISSYSTEMMSG", "0");
		obj.addProperty("CHANNEL", "android");
		obj.addProperty("ORDERID", orderid);

		// "ISSYSTEMMSG": "0", //是否系统消息（1是，0为否）
		// "CHANNEL": "android", //来源（android|ios）
		// "ORDERID": "123", //订单ID
		// "SERVICEID": "1", //服务ID
		// "PACKET_BUY_ID": "1", //套餐购买ID
		// "USERID": "6998", //用户ID
		// "DOCTORID": "5226", //医生ID
		// "ORDERTYPE": "1" //订单类型
		// "user_avatar": "http://xxx.jpg" //用户头像
		// "doctor_avatar": "http://xxx.jpg" //医生头像
		// "user_name":"xxx"
		// "doctor_name":"xx"

		return obj.toString();

	}
	

	@Override
	public boolean handleMessage(Message msg) {
		try {
			switch (msg.what) {
			case IResult.BASE_PROFILE:
				// mPullToRefreshLayout.setRefreshComplete();
				SetProfile sp = (SetProfile) msg.obj;
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						activity.showToast("修改个人资料成功");
					} else {
						activity.showToast(sp.getErrormsg());
					}
				} else {
					activity.showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				activity.showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				break;
			case IResult.LOGIN:
				tv_error.setVisibility(View.GONE);
				break;
			case IResult.SEND_SYS_MSG:
				Logger.dout(" handleMessage IResult.SEND_SYS_MSG!!!");
				MessageSystemEvent event = (MessageSystemEvent) msg.obj;
				sendSystemMessage(event);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// *'1'=>'文字咨询',
	// *'2'=>'预约通话',
	// *'3'=>'名医加号',
	// *'4'=>'上门会诊',
	// *'5'=>'院内陪诊',
	// *'7'=>'免费挂号',
	// *'11'=>'体检报告解读',
	// *'12'=>'个人健康季评',
	// *'14'=>'住院直通车',
	// *'15'=>'签约私人医生服务购买',
	// *'16'=>'其他服务'

	List<Integer> serverList = new ArrayList<Integer>();

	private void initServer(MemberInfo memberInfo) {
		// TODO Auto-generated method stub
		serverList.clear();
		DoctorServiceEntry offer_SERVICE = memberInfo.getData()
				.getOFFER_SERVICE();
		try {
			OfferServiceEntry2 offer_TEXT = offer_SERVICE.getOFFER_TEXT();
			if (offer_TEXT.getIS_OFFER() == 1) {// 图文
				serverList.add(TEXT);
			}
		} catch (Exception e) {
		}
		try {
			OfferServiceEntry offer_CALL = offer_SERVICE.getOFFER_CALL();
			if (offer_CALL.getIS_OFFER() == 1) {// 电话
				serverList.add(CALL);
			}
		} catch (Exception e) {
		}
		try {
			OfferServiceEntry offer_JIAHAO = offer_SERVICE.getOFFER_JIAHAO();
			if (offer_JIAHAO.getIS_OFFER() == 1) {// 就医
				serverList.add(JIUYI);
			}
		} catch (Exception e) {
		}
		try {
			OfferServiceEntry offer_ZHUYUAN = offer_SERVICE.getOFFER_ZHUYUAN();
			if (offer_ZHUYUAN.getIS_OFFER() == 1) {// 住院
				serverList.add(ZHUYUAN);
			}
		} catch (Exception e) {
		}
//		try {
//			OfferServiceEntry offer_SHANGMEN = offer_SERVICE
//					.getOFFER_SHANGMEN();
//			if (offer_SHANGMEN.getIS_OFFER() == 1) {// 上门
//				serverList.add(SHANGMEN);
//			}
//		} catch (Exception e) {
//		}
		try {
			OfferServicePacketEntry offer_PACKET = offer_SERVICE
					.getOFFER_PACKET();
			if ("1".equals(offer_PACKET.getIS_OFFER())) {// 签约私人医生
				serverList.add(PRIVATE_DOCTOR);
			}
		} catch (Exception e) {
		}
		try {
			OfferServiceEntry offer_CUSTOM = offer_SERVICE.getOFFER_CUSTOM();
			if (offer_CUSTOM.getIS_OFFER() == 1) {// 其他
				serverList.add(CUSTOM);
			}
		} catch (Exception e) {
		}

		order_gradView.setAdapter(new MyGiveAdapter());

	}

	class MyGiveAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return serverList.size();
		}

		@Override
		public Object getItem(int position) {
			return serverList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			convertView = View.inflate(getActivity(), R.layout.server_item,
					null);
			ImageView iv_icon = (ImageView) convertView
					.findViewById(R.id.iv_icon);
			TextView tv_desc = (TextView) convertView
					.findViewById(R.id.tv_desc);
			int type = serverList.get(position);
			switch (type) {
			case TEXT:
				iv_icon.setImageResource(R.drawable.more_textconsult);
				tv_desc.setText(R.string.more_textconsult);
				break;
			case CALL:
				iv_icon.setImageResource(R.drawable.more_phoneconsult);
				tv_desc.setText(R.string.more_phoneconsult);
				break;
			case JIUYI:
				iv_icon.setImageResource(R.drawable.more_jiuyi);
				tv_desc.setText(R.string.more_jiuyi);
				break;
			case ZHUYUAN:
				iv_icon.setImageResource(R.drawable.more_zhuyuan);
				tv_desc.setText(R.string.more_zhuyuan);
				break;
			case SHANGMEN:
				iv_icon.setImageResource(R.drawable.more_shangmen);
				tv_desc.setText(R.string.more_shangmen);
				break;
			case PRIVATE_DOCTOR:
				iv_icon.setImageResource(R.drawable.more_person_doctor);
				tv_desc.setText(R.string.more_person_doctor);
				break;
			case CUSTOM:
				iv_icon.setImageResource(R.drawable.more_custom);
				tv_desc.setText(R.string.more_custom);
				break;
			default:
				break;
			}

			return convertView;
		}

	}

	private void initPopupWindow() {
		// TODO Auto-generated method stub

		View v = View.inflate(activity, R.layout.popu_layout, null);
		LinearLayout ll = (LinearLayout) v.findViewById(R.id.ll_popu);
		ll.setGravity(Gravity.CENTER);
		List<OrderListEntry> open_ORDER = data.getOPEN_ORDER();

		for (Iterator iterator = open_ORDER.iterator(); iterator.hasNext();) {
			final OrderListEntry orderListEntry = (OrderListEntry) iterator
					.next();
			View v1 = View.inflate(activity, R.layout.popu_item, null);
			String ordertype = orderListEntry.getORDERTYPE();
			ImageView iv_icon = (ImageView) v1.findViewById(R.id.iv_icon);
			TextView tv_message = (TextView) v1.findViewById(R.id.tv_message);
			/**
			 * '1'=>'文字咨询', '2'=>'预约通话', '3'=>'名医加号', '4'=>'上门会诊', '5'=>'院内陪诊',
			 * '7'=>'免费挂号', '11'=>'体检报告解读', '12'=>'个人健康季评', '14'=>'住院直通车',
			 * '15'=>'签约私人医生服务购买', '16'=>'其他服务'
			 */

			String type = orderListEntry.getORDERTYPE();
			if ("1".equals(type)) {//
				iv_icon.setBackgroundResource(R.drawable.order_text);
				tv_message.setText(getString(R.string.home_item_textconsult));
			} else if ("2".equals(type)) {
				iv_icon.setBackgroundResource(R.drawable.order_phone);
				tv_message.setText(getString(R.string.home_item_textconsult));
			} else if ("7".equals(type)) {
				iv_icon.setBackgroundResource(R.drawable.order_jiahao);
				tv_message.setText(getString(R.string.home_item_jiuyi));
			} else if ("4".equals(type)) {
				iv_icon.setBackgroundResource(R.drawable.order_shangmen);
				tv_message.setText(getString(R.string.home_item_shangmen));
			} else if ("15".equals(type)) {
				iv_icon.setBackgroundResource(R.drawable.order_private_doctor);
				tv_message.setText(getString(R.string.home_item_person_doctor));
			} else if ("16".equals(type)) {
				iv_icon.setBackgroundResource(R.drawable.order_custom);
				tv_message.setText(getString(R.string.home_item_custom));
			}

			v1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String s = mEditTextContent.getText().toString();
					// sendText(s,);
				}
			});

			ll.addView(v1);

		}

		popu = new PopupWindow(v, Util.dip2px(activity, 150), open_ORDER.size()
				* Util.dip2px(activity, 50) + 10);
		ColorDrawable cd = new ColorDrawable(-0000);
		popu.setBackgroundDrawable(cd);
		popu.setFocusable(true);
	}

	/**
	 * 
	 * @param isOpen
	 *            true:有订单，false：无订单
	 */
	private void setBottomByOpenOrder(boolean isOpen) {
		if (isOpen) {
			rl_bottom.setVisibility(View.VISIBLE);
			// ll_bottom_btn.setVisibility(View.GONE);
		} else {
			rl_bottom.setVisibility(View.GONE);
			// ll_bottom_btn.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void onRefreshStarted(View view) {

	}

	@Override
	public void execute(int mes, Object obj) {
		if (mes == IResult.SEND_SYS_MSG) {
			final MessageSystemEvent event = (MessageSystemEvent) obj;
			try {
				// 调用sdk登录方法登录聊天服务器
				// EMChatManager.getInstance().logout();
				if (MyApplication.isLoginIMing) {
					return;
				}
				MyApplication.isLoginIMing = true;
				EMChatManager.getInstance().login(
						SharePrefUtil.getString(Conast.MEMBER_ID),
						MD5.getMD5(SharePrefUtil.getString(Conast.MEMBER_ID)
								+ SharePrefUtil.getString(Conast.USER_Name)
								+ "ememedim"), new EMCallBack() {

							@Override
							public void onSuccess() {
								MyApplication.isLoginIMing = false;
								Message msg = new Message();
								msg.what = IResult.SEND_SYS_MSG;
								msg.obj = event;
								mHandler.sendMessage(msg);
							}

							@Override
							public void onProgress(int progress, String status) {
								Logger.dout(TAG + " EMChatManager onProgress");
							}

							@Override
							public void onError(int code, final String message) {
								MyApplication.isLoginIMing = false;
								Logger.dout(TAG + " EMChatManager code:" + code
										+ "onError:" + message);

								if (code == -1) {// code = -1
									// 有可能是应用的环信value出错，有可能是该帐号没注册到环信服务器，所以重新调用接口，让后台把该帐号注册到环信
									reg_to_im();
								}
							}
						});
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	public void sendSystemMessage(MessageSystemEvent event) {
		Gson gson = new Gson();
		String content = gson.toJson(event.getMsgSysEntry());
		// content = content.replaceAll("\"", "\\\\\"");// "冒号
		// 要转义。不然的话textbody会解析出错
		String extStr = gson.toJson(event.getMsgEntry());
		sendText(content, extStr);
	}

	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		cameraFile = new File(PathUtil.getInstance().getImagePath(),
				SharePrefUtil.getString(Conast.MEMBER_ID)
						+ System.currentTimeMillis() + ".jpg");
		cameraFile.getParentFile().mkdirs();
		startActivityForResult(
				new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(
						MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
				REQUEST_CODE_CAMERA);
	}

	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent intent;
		if (Build.VERSION.SDK_INT < 19) {
			intent = new Intent(Intent.ACTION_GET_CONTENT);
			intent.setType("image/*");

		} else {
			intent = new Intent(
					Intent.ACTION_PICK,
					android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		}
		startActivityForResult(intent, REQUEST_CODE_LOCAL);
	}

	/**
	 * 发送文本消息
	 * 
	 * extStr属于附加字段
	 * 
	 * 当附加字段里边的is_system_msg为1时，则发送系统消息
	 * 
	 * @param content
	 *            message content
	 * @param isResend
	 *            boolean resend
	 */
	private void sendText(String content, String extStr) {

		if (content.length() > 0) {
			EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
			TextMessageBody txtBody = new TextMessageBody(content);
			// 设置消息body
			message.addBody(txtBody);
			if (!TextUtils.isEmpty(extStr))
				message.setAttribute("ext", extStr);

			// 设置要发给谁,用户username或者群聊groupid
			message.setReceipt(toChatUsername);
			message.setFrom(SharePrefUtil.getString(Conast.MEMBER_ID));
			// 把messgage加到conversation中
			conversation.addMessage(message);
			// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
			adapter.addEMMessage(message);
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			mEditTextContent.setText("");
			activity.setResult(activity.RESULT_OK);
		}
	}
	
	

	/**
	 * 发送语音
	 * 
	 * @param filePath
	 * @param fileName
	 * @param length
	 * @param isResend
	 */
	private void sendVoice(String filePath, String fileName, String length,
			boolean isResend, String extStr) {
		if (!(new File(filePath).exists())) {
			return;
		}
		try {
			final EMMessage message = EMMessage
					.createSendMessage(EMMessage.Type.VOICE);
			String to = toChatUsername;
			message.setReceipt(to);
			message.setFrom(SharePrefUtil.getString(Conast.MEMBER_ID));
			if (!TextUtils.isEmpty(extStr))
				message.setAttribute("ext", extStr);

			int len = Integer.parseInt(length);
			VoiceMessageBody body = new VoiceMessageBody(new File(filePath),
					len);
			message.addBody(body);

			adapter.addEMMessage(message);
			conversation.addMessage(message);
			adapter.refresh();
			listView.setSelection(listView.getCount() - 1);
			activity.setResult(activity.RESULT_OK);
			// send file
			// sendVoiceSub(filePath, fileName, message);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 发送图片
	 * 
	 * @param filePath
	 */
	private void sendPicture(final String filePath, String extStr) {
		int rowId = 0;
		String to = toChatUsername;
		// create and add image message in view
		final EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.IMAGE);
		message.setReceipt(to);
		message.setFrom(SharePrefUtil.getString(Conast.MEMBER_ID));
		if (!TextUtils.isEmpty(extStr))
			message.setAttribute("ext", extStr);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		message.addBody(body);
		adapter.addEMMessage(message);
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.refresh();
		listView.setSelection(listView.getCount() - 1);
		activity.setResult(activity.RESULT_OK);
		// more(more);
	}

	/**
	 * 根据图库图片uri发送图片
	 * 
	 * @param selectedImage
	 */
	private void sendPicByUri(Uri selectedImage) {
		// String[] filePathColumn = { MediaStore.Images.Media.DATA };
		Cursor cursor = activity.getContentResolver().query(selectedImage,
				null, null, null, null);
		cursor.moveToFirst();
		int columnIndex = cursor.getColumnIndex("_data");
		String picturePath = cursor.getString(columnIndex);
		cursor.close();
		if (picturePath == null || picturePath.equals("null")) {
			Toast toast = Toast.makeText(activity, "找不到图片", Toast.LENGTH_SHORT);
			toast.setGravity(Gravity.CENTER, 0, 0);
			toast.show();
			return;
		}
		String obj = generateExtMessage();
		sendPicture(picturePath, obj);
	}

	/**
	 * 发送位置信息
	 * 
	 * @param latitude
	 * @param longitude
	 * @param imagePath
	 * @param locationAddress
	 */
	private void sendLocationMsg(double latitude, double longitude,
			String imagePath, String locationAddress) {
		EMMessage message = EMMessage
				.createSendMessage(EMMessage.Type.LOCATION);
		LocationMessageBody locBody = new LocationMessageBody(locationAddress,
				latitude, longitude);
		message.addBody(locBody);
		message.setReceipt(toChatUsername);
		message.setFrom(SharePrefUtil.getString(Conast.MEMBER_ID));
		conversation.addMessage(message);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
		listView.setSelection(listView.getCount() - 1);
		activity.setResult(activity.RESULT_OK);
	}

	/**
	 * 重发消息
	 */
	private void resendMessage() {

		EMMessage msg = null;
		
		System.out.println("resendPos!!! = " +resendPos );
		
//		msg = conversation.getMessage(resendPos);
		
		if(adapter!=null){
			msg = adapter.getItem(resendPos);
			// msg.setBackSend(true);
			msg.status = EMMessage.Status.CREATE;
			adapter.refresh();
			listView.setSelection(resendPos);
		}
		

	}

	/**
	 * 显示语音图标按钮
	 * 
	 * @param view
	 */
	public void setModeVoice(View view) {
		edittext_layout.setVisibility(View.GONE);
		hideKeyboard();
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeKeyboard.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.GONE);
		// buttonSend.setVisibility(View.GONE);
		// buttonSetModeVoice.setVisibility(View.GONE);
		buttonPressToSpeak.setVisibility(View.VISIBLE);
		// btnContainer1.setVisibility(View.VISIBLE);
		expressionContainer.setVisibility(View.GONE);
	}

	/**
	 * 显示键盘图标
	 * 
	 * @param view
	 */
	public void setModeKeyboard(View view) {
		edittext_layout.setVisibility(View.VISIBLE);
		more.setVisibility(View.GONE);
		view.setVisibility(View.GONE);
		buttonSetModeVoice.setVisibility(View.VISIBLE);
		// mEditTextContent.setVisibility(View.VISIBLE);
		mEditTextContent.requestFocus();
		// buttonSend.setVisibility(View.VISIBLE);
		buttonPressToSpeak.setVisibility(View.GONE);
	}

	/**
	 * 显示或隐藏图标按钮页
	 * 
	 * @param view
	 */
	public void more(View view) {
		if (more.getVisibility() == View.GONE) {
			// System.out.println("more gone");
			hideKeyboard();
			more.setVisibility(View.VISIBLE);
			// btnContainer1.setVisibility(View.VISIBLE);
			expressionContainer.setVisibility(View.GONE);
		} else {
			if (expressionContainer.getVisibility() == View.VISIBLE) {
				expressionContainer.setVisibility(View.GONE);
				// btnContainer1.setVisibility(View.VISIBLE);
			} else {
				more.setVisibility(View.GONE);
			}
		}
	}

	/**
	 * 点击文字输入框
	 * 
	 * @param v
	 */
	public void editClick(View v) {
		listView.setSelection(listView.getCount() - 1);
		if (more.getVisibility() == View.VISIBLE) {
			more.setVisibility(View.GONE);
		}
	}

	/**
	 * 按住说话listener
	 * 
	 */
	class PressToSpeakListen implements View.OnTouchListener {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				if (!Util.isExitsSdcard()) {
					Toast.makeText(activity, "发送语音需要sdcard支持！",
							Toast.LENGTH_SHORT).show();
					return false;
				}
				try {
					v.setPressed(true);
					recordingContainer.setVisibility(View.VISIBLE);
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					voiceRecorder.startRecording(null, toChatUsername,
							activity.getApplicationContext());
				} catch (Exception e) {
					e.printStackTrace();
					v.setPressed(false);
					recordingContainer.setVisibility(View.INVISIBLE);
					Toast.makeText(activity, R.string.recoding_fail,
							Toast.LENGTH_SHORT).show();
					return false;
				}
				return true;
			case MotionEvent.ACTION_MOVE: {
				if (event.getY() < 0) {
					recordingHint
							.setText(getString(R.string.release_to_cancel));
					recordingHint
							.setBackgroundResource(R.drawable.recording_text_hint_bg);
					micImage.setImageDrawable(getResources().getDrawable(
							R.drawable.record_delete));
				} else {
					recordingHint
							.setText(getString(R.string.move_up_to_cancel));
					recordingHint.setBackgroundColor(Color.TRANSPARENT);
					micImage.setImageDrawable(micImages[0]);
				}
				return true;
			}
			case MotionEvent.ACTION_UP:
				v.setPressed(false);
				recordingContainer.setVisibility(View.INVISIBLE);
				if (event.getY() < 0) {
					// discard the recorded audio.
					voiceRecorder.discardRecording();
				} else {
					// stop recording and send voice file
					try {
						int length = voiceRecorder.stopRecoding();
						if (length > 0) {
							String obj = generateExtMessage();
							sendVoice(voiceRecorder.getVoiceFilePath(),
									voiceRecorder
											.getVoiceFileName(toChatUsername),
									Integer.toString(length), false, obj);
						}
					} catch (Exception e) {
						Toast.makeText(activity, "发送失败，请检测服务器是否连接",
								Toast.LENGTH_SHORT).show();
					}
				}
				return true;
			default:
				try {
					v.setPressed(false);
					recordingContainer.setVisibility(View.INVISIBLE);
					voiceRecorder.discardRecording();
				} catch (Exception e) {
					// TODO Auto-generated catch block
				}
				return false;
			}
		}
	}

	@Override
	public void onPause() {
		if (voiceRecorder != null) {
			recordingContainer.setVisibility(View.INVISIBLE);
			voiceRecorder.stopRecoding();
		}

		super.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (null != adapter) {
			adapter.refresh();
		}
		if (EMChatManager.getInstance() != null
				|| EMChatManager.getInstance().isConnected()) {
			if (TextUtils.isEmpty(EMChatManager.getInstance().getCurrentUser()))
				loginIM();
		} else {
			loginIM();
		}
	}

	/**
	 * 隐藏软键盘
	 */
	private void hideKeyboard() {
		if (activity.getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
			if (activity.getCurrentFocus() != null)
				manager.hideSoftInputFromWindow(activity.getCurrentFocus()
						.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	private Button btn_set_camera;
	private MemberInfoEntity data;
	private LinearLayout rl_bottom;
	private PopupWindow popu;
	private LinearLayout ll_bottom_btn;
	private Button btn_open_more;

	private void loginIM() {
		if (MyApplication.isLoginIMing) {
			return;
		}

		if (SharePrefUtil.getBoolean(Conast.LOGIN)
				&& !TextUtils
						.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
			MyApplication.isLoginIMing = true;
			if (null == EMChatManager.getInstance()
					|| !EMChatManager.getInstance().isConnected()) {
				new Thread() {
					public void run() {
						try {
							// 调用sdk登录方法登录聊天服务器
							EMChatManager
									.getInstance()
									.login(SharePrefUtil
											.getString(Conast.MEMBER_ID),
											MD5.getMD5(SharePrefUtil
													.getString(Conast.MEMBER_ID)
													+ SharePrefUtil
															.getString(Conast.USER_Name)
													+ "ememedim"),
											new EMCallBack() {

												@Override
												public void onSuccess() {
													MyApplication.isLoginIMing = false;
													mHandler.sendEmptyMessage(IResult.LOGIN);
												}

												@Override
												public void onProgress(
														int progress,
														String status) {
													Logger.dout(TAG
															+ " EMChatManager onProgress");
												}

												@Override
												public void onError(int code,
														final String message) {
													MyApplication.isLoginIMing = false;
													Toast.makeText(
															getActivity(),
															message, 1).show();
													Logger.dout(TAG
															+ " EMChatManager code:"
															+ code + "onError:"
															+ message);
												}
											});

							activity.registerIMReceiver();

						} catch (Exception e1) {
							e1.printStackTrace();
						}
					};
				}.start();
			}
		}
	}

	/**
	 * listview滑动监听listener
	 * 
	 */
	private class ListScrollListener implements OnScrollListener {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			switch (scrollState) {
			case OnScrollListener.SCROLL_STATE_IDLE:
				if (view.getFirstVisiblePosition() == 0 && !isloading
						&& haveMoreData) {
					loadmorePB.setVisibility(View.VISIBLE);
					// sdk初始化加载的聊天记录为20条，到顶时去db里获取更多
					List<EMMessage> messages;
					try {
						// 获取更多messges，调用此方法的时候从db获取的messages
						// sdk会自动存入到此conversation中
						if (chatType == CHATTYPE_SINGLE)
							messages = conversation.loadMoreMsgFromDB(adapter
									.getItem(0).getMsgId(), pagesize);
						else
							messages = conversation.loadMoreGroupMsgFromDB(
									adapter.getItem(0).getMsgId(), pagesize);
					} catch (Exception e1) {
						loadmorePB.setVisibility(View.GONE);
						return;
					}
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
					}
					if (messages.size() != 0) {
						// 刷新ui
						adapter.notifyDataSetChanged();
						listView.setSelection(messages.size() - 1);
						if (messages.size() != pagesize)
							haveMoreData = false;
					} else {
						haveMoreData = false;
					}
					loadmorePB.setVisibility(View.GONE);
					isloading = false;
				}
				break;
			}
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {

		}
	}

	// *'1'=>'文字咨询',
	// *'2'=>'预约通话',
	// *'3'=>'名医加号',
	// *'4'=>'上门会诊',
	// *'5'=>'院内陪诊',
	// *'7'=>'免费挂号',
	// *'11'=>'体检报告解读',
	// *'12'=>'个人健康季评',
	// *'14'=>'住院直通车',
	// *'15'=>'签约私人医生服务购买',
	// *'16'=>'其他服务'
	private void sendPictures() {

		if (NetWorkUtils.detect(activity)) {
			final Context dialogContext = new ContextThemeWrapper(activity,
					android.R.style.Theme_Light);
			String[] choices = new String[2];
			choices[0] = getString(R.string.change_avatar_take_photo);
			choices[1] = getString(R.string.change_avatar_album);

			MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
			MenuDialog dialog = builder.setTitle(R.string.picture_send)
					.setItems(choices, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							switch (which) {
							case 0:
								selectPicFromCamera();
								break;
							case 1:
								selectPicFromLocal(); // 点击图片图标
								break;
							}
						}
					}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	// public void getMemberInfo() {
	//
	// if (NetWorkUtils.detect(activity)) {
	// activity.loading(null);
	// HashMap<String, String> params = new HashMap<String, String>();
	// params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
	// params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
	// params.put("order_id", orderid);
	// params.put("any_user_id", toChatUsername);
	//
	// System.out.println("params " +params);
	//
	// MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_member_info,
	// MemberInfo.class, params, new Response.Listener() {
	// @Override
	// public void onResponse(Object response) {
	// Message message = new Message();
	// message.obj = response;
	// message.what = IResult.MEMBER_INFO;
	// mHandler.sendMessage(message);
	// MemberInfo memberInfo = (MemberInfo) response;
	// if (memberInfo != null)
	// EventBus.getDefault().postSticky(memberInfo);
	// }
	// }, new Response.ErrorListener() {
	// @Override
	// public void onErrorResponse(VolleyError error) {
	// Message message = new Message();
	// message.obj = error.getMessage();
	// message.what = IResult.DATA_ERROR;
	// mHandler.sendMessage(message);
	// }
	// });
	// } else {
	// mHandler.sendEmptyMessage(IResult.NET_ERROR);
	// }
	// }

	/***
	 * 下完订单 发送系统消息
	 * 
	 * @param event
	 */
	public void receiveEventAndSendMsg(MessageSystemEvent event) {
		// ordertype订单类型：'1'=>"图文咨询",'2'=>"预约通话",'3'=>"名医加号",'4'=>"上门会诊",'14'=>"住院直通车",'11'=>"体检报告解读",'15'=>"签约私人医生服务",'16'=>"自定义订单"
		Logger.dout(TAG + " onEvent  MessageSystemEvent");
		if (null == EMChatManager.getInstance()
				|| !EMChatManager.getInstance().isConnected()) {
			UICore.eventTask(this, activity, IResult.SEND_SYS_MSG, null, event);
		} else {
			// getMemberInfo();
			sendSystemMessage(event);
		}

	}

	private void reg_to_im() {
		try {
			if (NetWorkUtils.detect(getActivity())) {
				new Thread() {
					public void run() {
						ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
						params.add(new BasicNameValuePair("token",
								SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
						params.add(new BasicNameValuePair("channel", "android"));
						params.add(new BasicNameValuePair("memberid",
								SharePrefUtil.getString(Conast.MEMBER_ID)));
						params.add(new BasicNameValuePair("utype", "user"));
						try {
							String result = HttpUtil
									.getString(HttpUtil.URI
											+ HttpUtil.reg_to_im, params,
											HttpUtil.POST);
							Gson gson = new Gson();
							ResultInfo info = gson.fromJson(result,
									ResultInfo.class);
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

	public void showExitTips() {
		try {
			if (!activity.isFinishing()) {

				MenuDialog.Builder alert = new MenuDialog.Builder(activity);
				MenuDialog dialog = alert
						.setTitle(getString(R.string.system_title))
						.setMessage(getString(R.string.im_conflict))
						.setPositiveButton(getString(R.string.confirm_ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										dialog.dismiss();
										logout();
									}
								}).create();
				dialog.setCanceledOnTouchOutside(false);
				dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

					@Override
					public boolean onKey(DialogInterface dialog, int keyCode,
							KeyEvent event) {
						if (KeyEvent.KEYCODE_BACK == keyCode) {
							return true;
						}
						return false;
					}
				});
				dialog.show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void logout() {
		JPushInterface.stopPush(activity.getApplicationContext());
		try {
			new Thread() {
				public void run() {
					if (null != EMChatManager.getInstance()) {
						EMChatManager.getInstance().logout();
					}

					ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
					params.add(new BasicNameValuePair("userid", SharePrefUtil
							.getString(Conast.MEMBER_ID)));
					params.add(new BasicNameValuePair("channel", "android"));
					params.add(new BasicNameValuePair("imei", PublicUtil
							.getDeviceUuid(activity)));
					params.add(new BasicNameValuePair("appversion", PublicUtil
							.getVersionName(activity)));

					try {
						HttpUtil.getString(HttpUtil.URI + HttpUtil.logout,
								params, HttpUtil.POST);
					} catch (IOException e) {
						e.printStackTrace();

					}
				};
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		}

		Intent intent = new Intent(activity, LoginActivity.class);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		SharePrefUtil.putBoolean(Conast.LOGIN, false);

		SharePrefUtil.putString(Conast.MEMBER_ID, "");
		SharePrefUtil.putString(Conast.USER_Name, "");
		SharePrefUtil.putString(Conast.realName, "");
		SharePrefUtil.putString(Conast.ACCESS_TOKEN, "");
		SharePrefUtil.putString(Conast.AVATAR, "");
		SharePrefUtil.putString(Conast.USER_SEX, "");
		SharePrefUtil.putString(Conast.USER_MOBILE, "");
		SharePrefUtil.putString(Conast.USER_BIRTHDAY, "");
		SharePrefUtil.putString(Conast.USER_CARDID, "");
		SharePrefUtil.putString(Conast.USER_ADDRESS, "");
		SharePrefUtil.putString(Conast.USER_CLINICCARDNUM, "");
		SharePrefUtil.commit();
		startActivity(intent);
		activity.finish();
	}

	public void goChat() {
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		builder.setTitle("系统提示").setMessage("请与医生沟通通话时间，并提醒医生在订单页设置通话时间")
				.setNegativeButton("确定", null).show();
	}

	public void getIMData(String page) {

		if (NetWorkUtils.detect(activity)) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("any_user_id", toChatUsername);
			params.put("page", page);
			params.put("order_id", orderid);
			System.out.println("params:>>>>>>>>>>>>>>>>>>>" + params);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_im_list,
					IMMessageInfo.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object arg0) {
							listView.onRefreshComplete();
							IMMessageInfo imInfo = (IMMessageInfo) arg0;
							TransformEMMessage
							.getInstance().setLoadInterface(MicroChatFragment.this);
							if (chatPage > 0) {
								List<EMMessage> imData = TransformEMMessage
										.getInstance()
										.addEMMessageList(
												imInfo,
												SharePrefUtil
														.getString(Conast.MEMBER_ID));
								
								if(adapter==null){
									adapter = new MicroMessageAdapter(MicroChatFragment.this, activity,
											toChatUsername, chatType,
											doctor_avatar, orderid, imData);
									listView.setAdapter(adapter);
									listView.setSelection(listView.getCount() - 1);
								}else{
									adapter.addEMMessagelist(imData);
								}
							} else {
								// 处理分页消息，
								List<EMMessage> imData = TransformEMMessage
										.getInstance()
										.transformEM(
												imInfo,
												SharePrefUtil
														.getString(Conast.MEMBER_ID),
												toChatUsername, orderid);

								adapter = new MicroMessageAdapter(
										MicroChatFragment.this, activity,
										toChatUsername, chatType,
										doctor_avatar, orderid, imData);
								// 显示消息
								listView.setAdapter(adapter);
								listView.setSelection(listView.getCount() - 1);
							}

						}

					}

					, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError arg0) {
							listView.onRefreshComplete();
							System.out.println("获取历史聊天记录异常");
						}
					});
		} else {

			Toast.makeText(getActivity(), "无网络，请检查网络设置", Toast.LENGTH_LONG)
					.show();

		}

	}

	private void go2BuyServiceAct(int type) {
		if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))
				|| !SharePrefUtil.getBoolean(Conast.LOGIN)) {// 没登录
			Intent intent = new Intent(activity, LoginActivity.class);
			startActivity(intent);
		} else if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_SEX))
				|| TextUtils.isEmpty(SharePrefUtil
						.getString(Conast.USER_BIRTHDAY))
				|| TextUtils.isEmpty(SharePrefUtil
						.getString(Conast.USER_MOBILE))) {
			// 资料没完善
			MenuDialog.Builder alert = new MenuDialog.Builder(activity);
			MenuDialog dialog = alert
					.setTitle(R.string.system_info)
					.setMessage(R.string.you_should_improve_data)
					.setPositiveButton(getString(R.string.confirm_yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(activity,
											PersonInfoActivity.class);
									startActivity(intent);
								}
							})
					.setNegativeButton(getString(R.string.confirm_no),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			Intent intent = null;
			if (type == TEXT) {
				// if (isOpenPrivateDoctor) {
				// goChat();
				// return;
				// } else {
				intent = new Intent(activity, BuyTextConsultActivity.class);
				// }
			} else if (type == PRIVATE_DOCTOR) {
				intent = new Intent(activity, PrivateDoctorActivity.class);
			} else if (type == JIUYI) {
				intent = new Intent(activity, BuyJiuyiActivity.class);
			} else if (type == ZHUYUAN) {
				intent = new Intent(activity, BuyZhuyuanActivity.class);
			} else if (type == SHANGMEN) {
				intent = new Intent(activity, BuyShangmenActivity.class);
			} else if (type == CALL) {
				// if (isOpenPrivateDoctor) {
				// goChat();
				// return;
				// } else {
				intent = new Intent(activity, BuyPhoneConsultActivity.class);
				// }
			} else if (type == CUSTOM) {
				intent = new Intent(activity, BuyCustomActivity.class);
			}
			intent.putExtra("doctorid", data.getMEMBERID());
			intent.putExtra("doctorServiceEntry", data.getOFFER_SERVICE());
			intent.putExtra("doctor_name", doctor_name);
			startActivity(intent);
		}
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		if(conversation!=null){
			conversation.resetUnsetMsgCount();
		}
	}
	
	private String toStringJSON(String doctor,String user){
		
		JsonObject obj = new JsonObject();
		obj.addProperty("doctor_msg", doctor);
		obj.addProperty("user_msg", user);
		return obj.toString();//76935066
		
	}

	@Override
	public void loadSuccess() {
		// TODO Auto-generated method stub
		adapter.notifyDataSetChanged();
	}

}
