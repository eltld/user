package net.ememed.user2.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.ChatActivity;
import net.ememed.user2.activity.DoctorDetailInfoActivity;
import net.ememed.user2.activity.JiuyiConsultDetailsActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.activity.QuestionChatActivity;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.OrderListEntity;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.entity.QuestionOrderErtry;
import net.ememed.user2.medicalhistory.activity.QuestionXiangQingActivity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.MD5;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TimeUtil;
import net.ememed.user2.widget.MenuDialog;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

import org.apache.http.message.BasicNameValuePair;

import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.jpush.android.api.JPushInterface;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.chat.ConnectionListener;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;

/***
 * 医生详情页 订单tab
 * 
 * @author chen
 */
public class DoctorOrderFragment extends Fragment implements Handler.Callback, OnClickListener, OnRefreshListener {
	private static final String TAG = MyDoctorFragment.class.getSimpleName();
	private String fragmentKey = "";
	private DoctorDetailInfoActivity activity = null;
	private Handler mHandler;
	// private PullToRefreshLayout mPullToRefreshLayout;
	private View ll_view_content;
	private RefreshListView list_view;
	private ContactAdapter adapter;
	private List<OrderListEntry> listItems;
	private int totalpages = 1;
	private int page = 1;
	private String tochat_userId;
	private boolean init = false;// 是否首次加载
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;
	private String doctor_name;
	private TextView tv_notice;
	private ImageView iv_bg_empty;
	private String doctor_avatar;
	
	private boolean mIsFollow;

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时执行加载数据或者进度条等
			Logger.dout(TAG + "isVisibleToUser");
			if (!init) {
				refresh();
			}
		} else {
			// 不可见时不执行操作
			Logger.dout(TAG + "unVisibleToUser");
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (DoctorDetailInfoActivity) activity;
		
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler(this);
		activity = (DoctorDetailInfoActivity) getActivity();

		fragmentKey = getArguments().getString("fragmentKey");
		doctor_name = getArguments().getString("doctor_name");
		tochat_userId = getArguments().getString("tochat_userId");
		doctor_avatar = getArguments().getString("doctor_avatar");
		mIsFollow = getArguments().getBoolean("isFollow", false);
		
		// System.out.println("TestFragment onCreate fragmentKey : " +
		// fragmentKey);
		// EventBus.getDefault().registerSticky(this,
		// RegisterSuccessEvent.class);
		// loginIM();
	}

	public void loginIM() {
		if (SharePrefUtil.getBoolean(Conast.LOGIN) && !TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
			try {
				EMChatManager.getInstance().addConnectionListener(new MyConnectionListener());
				EMChat.getInstance().setAppInited();
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * 新消息广播接收者
	 * 
	 * 
	 */
	

	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.root_layout, null);
		FrameLayout mContentView = (FrameLayout) view.findViewById(R.id.mainView);
		ll_view_content = (View) LayoutInflater.from(activity).inflate(R.layout.fragment_doctor_order, null);
		mContentView.addView(ll_view_content);
		list_view = (RefreshListView) ll_view_content.findViewById(R.id.ptr_listview);

		ll_empty = (LinearLayout) ll_view_content.findViewById(R.id.ll_empty);
		iv_bg_empty = (ImageView) ll_view_content.findViewById(R.id.iv_bg_empty);
		tv_notice = (TextView) ll_view_content.findViewById(R.id.tv_notice);
		tv_notice.setText(getString(R.string.reload));
		iv_bg_empty.setOnClickListener(this);
		ll_net_unavailable = (LinearLayout) ll_view_content.findViewById(R.id.ll_net_unavailable);
		adapter = new ContactAdapter(null);
		list_view.setAdapter(adapter);
		addListener();
		return view;
	}
	

	private void addListener() {
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					OrderListEntry orderListEntry = (OrderListEntry) adapter.getlelectItem(position-1);
					if(orderListEntry.getORDERTYPE().equals("15")){
						Intent intent = new Intent(activity, JiuyiConsultDetailsActivity.class);
						intent.putExtra("item", orderListEntry);
						intent.putExtra("source", 3);
						intent.putExtra("tochat_userId", tochat_userId);
						intent.putExtra("doctor_avatar", doctor_avatar);
						intent.putExtra("isFollow", mIsFollow);
						startActivity(intent);
					}else if(orderListEntry.getORDERTYPE().equals("17")){ //免费提问已产生订单
						Intent intent = new Intent(activity, QuestionChatActivity.class);
						intent.putExtra("item", orderListEntry);
						intent.putExtra("source", 3);
						intent.putExtra("tochat_userId", tochat_userId);
						intent.putExtra("doctor_avatar", doctor_avatar);
						intent.putExtra("isFollow", mIsFollow);
						getActivity().startActivityForResult(intent, 7);
					}else{
						Intent intent = new Intent(activity, ChatActivity.class);
						intent.putExtra("item", orderListEntry);
						intent.putExtra("source", 3);
						intent.putExtra("tochat_userId", tochat_userId);
						intent.putExtra("doctor_avatar", doctor_avatar);
						intent.putExtra("isFollow", mIsFollow);
						getActivity().startActivityForResult(intent, 7);
					}
				
			}
		});

		list_view.setOnRefreshListener(new IOnRefreshListener() {

			@Override
			public void OnRefresh() {
				refresh();
			}
		});
		list_view.setOnLoadMoreListener(new IOnLoadMoreListener() {

			@Override
			public void OnLoadMore() {
				loadMore();
			}
		});
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// System.out.println("TestFragment onSaveInstanceState fragmentKey : "+
		// fragmentKey);
	}

	public void refresh() {
		page = 1;
		getDoctorOrderList(page);
	}

	protected void loadMore() {
		page++;
		getDoctorOrderList(page);
	}

	private void getDoctorOrderList(int page, boolean isLoading) {
		if (NetWorkUtils.detect(getActivity())) {
			if(isLoading)
				activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("any_user_id", activity.tochat_userId);
			params.put("page", page + "");
			params.put("type", "0");
			System.out.println("订单params = "+params);

			
			
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_order_list, OrderListEntity.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.ORDER_LIST;
					mHandler.sendMessage(message);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Message message = new Message();
					message.obj = error.getMessage();
					message.what = IResult.DATA_ERROR;
					mHandler.sendMessage(message);
				}                                     
			});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}

	}

	/** 获取订单列表 */
	private void getDoctorOrderList(int page) {
		getDoctorOrderList(page, true);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btn_save_guahao) {
			// setGuahaoProfile();
		} else if (v.getId() == R.id.iv_bg_empty) {
			refresh();
		}
	}

	@Override
	public boolean handleMessage(Message msg) {
		try {
			switch (msg.what) {
			case IResult.ORDER_LIST:
				list_view.onRefreshComplete();
				activity.destroyDialog();
				
				OrderListEntity olEntity = (OrderListEntity) msg.obj;
				
				System.out.println("Question_data = " + olEntity.getQuestion_data().size());
				
				init = true;
				if (null != olEntity) {
					if (olEntity.getSuccess() == 1) {
						init = true;
						if (null != olEntity.getData() && olEntity.getData().size() > 0) {
							if (page == 1) {
								adapter.change(olEntity.getData());
							} else {
								adapter.add(olEntity.getData());
							}
						} else {
							ll_empty.setVisibility(View.VISIBLE);
							list_view.setVisibility(View.GONE);
						}
						if (page < olEntity.getPages()) {
							list_view.onLoadMoreComplete(false);
						} else {
							list_view.onLoadMoreComplete(true);
						}
					} else {
						activity.showToast(IMessage.DATA_ERROR);
						ll_empty.setVisibility(View.VISIBLE);
						list_view.setVisibility(View.GONE);
					}
				} else {
					activity.showToast(IMessage.DATA_ERROR);
					ll_empty.setVisibility(View.VISIBLE);
					list_view.setVisibility(View.GONE);
				}
				break;
			case IResult.NET_ERROR:
				activity.destroyDialog();
				activity.showToast(IMessage.NET_ERROR);
				ll_net_unavailable.setVisibility(View.VISIBLE);
				list_view.setVisibility(View.GONE);
				break;
			case IResult.DATA_ERROR:
				activity.destroyDialog();
				ll_empty.setVisibility(View.VISIBLE);
				list_view.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public void onRefreshStarted(View view) {
		refresh();
	}

	private class ContactAdapter extends BaseAdapter {
		List<OrderListEntry> listItems;
		public ContactAdapter(List<OrderListEntry> listItems ) {
			if (listItems == null) {
				listItems = new ArrayList<OrderListEntry>();
			}
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(activity).inflate(R.layout.fragment_doctor_order_item, null);
				holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
				holder.tv_consult_info = (TextView) convertView.findViewById(R.id.tv_consult_info);
				holder.tv_consult_state = (TextView) convertView.findViewById(R.id.tv_consult_state);
				holder.iv_consult_pic = (ImageView) convertView.findViewById(R.id.iv_consult_pic);
				holder.tv_service_price = (TextView) convertView.findViewById(R.id.tv_service_price);
				holder.content_tx = (TextView) convertView.findViewById(R.id.content_tx);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
				OrderListEntry orderListEntry = listItems.get(position);
				if (orderListEntry != null) {
					holder.tv_service_price.setVisibility(View.VISIBLE);
					
					if(orderListEntry.getDESC_TEXT()==null||orderListEntry.getDESC_TEXT().equals("")){
						holder.content_tx.setVisibility(View.GONE);
					}else{
						holder.content_tx.setText(orderListEntry.getDESC_TEXT());
					}
					
					if (orderListEntry.getORDERAMOUNT().equals("0")) {
						holder.tv_service_price.setText("免费");
					} else {
						holder.tv_service_price.setText("￥" + orderListEntry.getORDERAMOUNT());
					}
					// ordertype订单类型：'1'=>"图文咨询",'2'=>"预约通话",'3'=>"名医加号",'4'=>"上门会诊",'14'=>"住院直通车",
					// '11'=>"体检报告解读",'15'=>"签约私人医生",'16'=>"其他服务" '17'=>"免费提问"
					/*
					 * 返回数组中的STATE表示该订单的状态，其对应关系如下： 图文咨询： 2支付成功，可以与医生沟通 3服务结束
					 * 
					 * 预约通话： 0需要与医生沟通，确认通话时间 2支付完成，可以通话 3服务结束
					 * 
					 * 签约私人医生服务： 2已支付,可与医生沟通 3服务结束
					 * 
					 * 预约住院、预约加号、上门会诊、自定义业务： 0请和医生沟通 1等待用户支付 2订单已支付，可以跟医生沟通了 3服务结束
					 */
					if (orderListEntry.getORDERTYPE().equals("1")) {
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.clinic_text_consult);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE_DESC())) {
							holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
//							if (orderListEntry.getSTATE().equals("2")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_three));
//							} else if (orderListEntry.getSTATE().equals("3")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_four));
//							}
						}
					} else if (orderListEntry.getORDERTYPE().equals("2")) {
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.clinic_phone_consult);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
//							if (orderListEntry.getSTATE().equals("0")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_five));
//							} else if (orderListEntry.getSTATE().equals("2")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_six) + TimeUtil.parseDateTime(Long.valueOf(orderListEntry.getCALL_TIME()), "yyyy-MM-dd HH:mm"));
//							} else if (orderListEntry.getSTATE().equals("3")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_four));
//							}
						}

					} else if (orderListEntry.getORDERTYPE().equals("3")) {// 就医
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.clinic_jiahao);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
//							if (orderListEntry.getSTATE().equals("0")) {
//								holder.tv_service_price.setVisibility(View.GONE);
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_one));
//							} else if (orderListEntry.getSTATE().equals("1")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_two));
//							} else if (orderListEntry.getSTATE().equals("2")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_three));
//							} else if (orderListEntry.getSTATE().equals("3")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_four));
//							}
						}
					} else if (orderListEntry.getORDERTYPE().equals("4")) {// 上门
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.clinic_shangmen);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
//							if (orderListEntry.getSTATE().equals("0")) {
//								holder.tv_service_price.setVisibility(View.GONE);
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_one));
//							} else if (orderListEntry.getSTATE().equals("1")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_two));
//							} else if (orderListEntry.getSTATE().equals("2")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_three));
//							} else if (orderListEntry.getSTATE().equals("3")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_four));
//							}
						}

					} else if (orderListEntry.getORDERTYPE().equals("14")) {// 住院
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.clinic_zhuyuan);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
//							if (orderListEntry.getSTATE().equals("0")) {
//								holder.tv_service_price.setVisibility(View.GONE);
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_one));
//							} else if (orderListEntry.getSTATE().equals("1")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_two));
//							} else if (orderListEntry.getSTATE().equals("2")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_three));
//							} else if (orderListEntry.getSTATE().equals("3")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_four));
//							}
							holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
						}
					} else if (orderListEntry.getORDERTYPE().equals("15")) {
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.ic_private_doctor);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
//							if (orderListEntry.getSTATE().equals("2")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_three));
//							} else if (orderListEntry.getSTATE().equals("3")) {
//								holder.tv_consult_state.setText(getString(R.string.offline_consult_title_four));
//							}
							holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
						}
					} else if (orderListEntry.getORDERTYPE().equals("16")) {// 自定义
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.clinic_custom);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
//							if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
//								if (orderListEntry.getSTATE().equals("0")) {
//									holder.tv_service_price.setVisibility(View.GONE);
//									holder.tv_consult_state.setText(getString(R.string.offline_consult_title_one));
//								} else if (orderListEntry.getSTATE().equals("1")) {
//									holder.tv_consult_state.setText(getString(R.string.offline_consult_title_two));
//								} else if (orderListEntry.getSTATE().equals("2")) {
//									holder.tv_consult_state.setText(getString(R.string.offline_consult_title_three));
//								} else if (orderListEntry.getSTATE().equals("3")) {
//									holder.tv_consult_state.setText(getString(R.string.offline_consult_title_four));
//								}
//							}
							holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
						}
					}else if (orderListEntry.getORDERTYPE().equals("17")){
						holder.tv_user_name.setText(orderListEntry.getORDERTYPENAME());
						holder.iv_consult_pic.setImageResource(R.drawable.question_mianfei);
						
						holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
						
						holder.tv_service_price.setText("免费");
					}
					if (!TextUtils.isEmpty(orderListEntry.getADDTIME())) {
						holder.tv_consult_info.setText(TimeUtil.parseFullDateTime2YMD(orderListEntry.getADDTIME()));
					}

			}

			
			return convertView;
		}
		
		
		
		public Object getlelectItem(int position){
				return listItems.get(position);
		}

		public void change(List<OrderListEntry> lists) {
			
			
			this.listItems.clear();
			if (lists == null) {
				lists = new ArrayList<OrderListEntry>();
			}
			this.listItems.addAll(lists);
			notifyDataSetChanged();
		}

		public void add(List<OrderListEntry> list) {
			this.listItems.addAll(list);
			notifyDataSetChanged();
		}
	}

	class ViewHolder {
		TextView tv_user_name;
		TextView tv_consult_info;
		TextView tv_consult_state;
		ImageView iv_consult_pic;
		TextView tv_service_price;
		TextView content_tx;
	}

	private class MyConnectionListener implements ConnectionListener {

		@Override
		public void onConnected() {
			Logger.dout(TAG + " EMChatManager 连接上聊天服务器 onConnected");

			// chatHistoryFragment.errorItem.setVisibility(View.GONE);
		}

		@Override
		public void onDisConnected(String errorString) {

			if (null == EMChatManager.getInstance()) {
				// EMChatManager.getInstance().logout();
				logIM();
			}
			// if (errorString != null && errorString.contains("conflict")) {
			// // 显示帐号在其他设备登录dialog
			// // Logger.dout(TAG +
			// " EMChatManager MyConnectionListener onDisConnected");
			// // showExitTips();
			// } else {
			// // chatHistoryFragment.errorItem.setVisibility(View.VISIBLE);
			// // chatHistoryFragment.errorText.setText("连接不到聊天服务器");
			// Logger.dout(TAG + " EMChatManager 连接不到聊天服务器 onDisConnected");
			// }
			// }
		}

		public void showExitTips() {
			try {
				if (!activity.isFinishing()) {

					MenuDialog.Builder alert = new MenuDialog.Builder(activity);
					MenuDialog dialog = alert.setTitle(getString(R.string.system_title)).setMessage(getString(R.string.im_conflict)).setPositiveButton(getString(R.string.confirm_ok), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							logout();
						}
					}).create();
					dialog.setCanceledOnTouchOutside(false);
					dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

						@Override
						public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
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

		@Override
		public void onReConnected() {
			// chatHistoryFragment.errorItem.setVisibility(View.GONE);
			Logger.dout(TAG + " EMChatManager 重新连接上聊天服务器 onReConnected");
		}

		@Override
		public void onReConnecting() {
			Logger.dout(TAG + " EMChatManager 正在重新连接聊天服务器 onReConnecting");
		}

		@Override
		public void onConnecting(String progress) {
			Logger.dout(TAG + " EMChatManager 正在连接聊天服务器 onConnecting " + progress);
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
					params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
					params.add(new BasicNameValuePair("channel", "android"));
					params.add(new BasicNameValuePair("imei", PublicUtil.getDeviceUuid(activity)));
					params.add(new BasicNameValuePair("appversion", PublicUtil.getVersionName(activity)));

					try {
						HttpUtil.getString(HttpUtil.URI + HttpUtil.logout, params, HttpUtil.POST);
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

	public void logIM() {
		if (MyApplication.isLoginIMing) {
			return;
		}

		try {

			if (!TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) && !TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_Name)))
				// 调用sdk登录方法登录聊天服务器
				MyApplication.isLoginIMing = true;
			EMChatManager.getInstance().login(SharePrefUtil.getString(Conast.MEMBER_ID), MD5.getMD5(SharePrefUtil.getString(Conast.MEMBER_ID) + SharePrefUtil.getString(Conast.USER_Name) + "ememedim"), new EMCallBack() {

				@Override
				public void onSuccess() {
					MyApplication.isLoginIMing = false;
					Logger.dout(TAG + " EMChatManager onSuccess");
					// 登录成功，保存用户名密码
				}

				@Override
				public void onProgress(int progress, String status) {
					Logger.dout(TAG + " EMChatManager onProgress");
				}

				@Override
				public void onError(int code, final String message) {
					MyApplication.isLoginIMing = false;
					Logger.dout(TAG + " EMChatManager code:" + code + "onError:" + message);
					if (code == -1) {// code = -1
										// 有可能是应用的环信value出错，有可能是该帐号没注册到环信服务器，所以重新调用接口，让后台把该帐号注册到环信
										// reg_to_im();
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	// private void reg_to_im() {
	// try {
	// if (NetWorkUtils.detect(activity)) {
	// new Thread() {
	// public void run() {
	// ArrayList<BasicNameValuePair> params = new
	// ArrayList<BasicNameValuePair>();
	// params.add(new BasicNameValuePair("token",
	// SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
	// params.add(new BasicNameValuePair("channel", "android"));
	// params.add(new BasicNameValuePair("memberid",
	// SharePrefUtil.getString(Conast.MEMBER_ID)));
	// params.add(new BasicNameValuePair("utype", "user"));
	// try {
	// String result = HttpUtil.getString(HttpUtil.URI + HttpUtil.reg_to_im,
	// params, HttpUtil.POST);
	// Gson gson = new Gson();
	// ResultInfo info = gson.fromJson(result, ResultInfo.class);
	// if (null != info && info.getSuccess() == 1) {
	// loginIM();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// };
	// }.start();
	// }
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	//
	// }
}
