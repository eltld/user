package net.ememed.user2.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.DoctorDetailInfoActivity;
import net.ememed.user2.activity.ExpertSearchActivity;
import net.ememed.user2.activity.ImageActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.activity.MainActivity;
import net.ememed.user2.db.ContactTable;
import net.ememed.user2.entity.ContactEntity;
import net.ememed.user2.entity.ContactEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.LoginSuccessEvent;

/**
 * 我的医生tab
 * 
 * @author taro chyaohui@gmail.com
 */
public class MyDoctorFragment extends Fragment implements Callback, OnClickListener {
	private static final String TAG = MyDoctorFragment.class.getSimpleName();
	public static final int EXEU_GET_DATA = 0;
	//public final int REQUEST_DOCTOR_DETAIL = 666;
	private Handler handler = null;
	private MainActivity activity = null;
	// private PullToRefreshLayout mPullToRefreshLayout;
	private boolean init = false;
	private boolean refresh = true;
	private int totalpages = 1;
	private int page = 1;

	private RefreshListView listview;
	private ContactAdapter adapter;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;
	private TextView tv_notice;
	private ImageView iv_bg_empty;
	private ArrayList<ContactEntry> list;
	private FrameLayout mContentView;
	
	private InnerReceiver receiver;
	private IntentFilter filter;

	public MyDoctorFragment() {
		this.activity = (MainActivity) getActivity();
	}

	@Override
	public void onAttach(Activity activity) {
		this.activity = (MainActivity) activity;
		receiver = new InnerReceiver();
		filter = new IntentFilter();
		filter.addAction(MyApplication.GET_CONTACT_LIST);
		activity.registerReceiver(receiver, filter);
		
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		handler = new Handler(this);
		activity = (MainActivity) getActivity();
//		EventBus.getDefault().registerSticky(this, LoginSuccessEvent.class);
		Logger.dout(TAG + "onCreate");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时执行加载数据或者进度条等
			Logger.dout(TAG + "isVisibleToUser");
			if (SharePrefUtil.getBoolean(Conast.LOGIN)) {
				
			} else {
				handler.sendEmptyMessage(IResult.UN_LOGIN);
			}
		} else {
			// 不可见时不执行操作
			Logger.dout(TAG + "unVisibleToUser");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View ll_view_content = (LinearLayout) inflater.inflate(R.layout.activity_list_view, null);

		// mPullToRefreshLayout = (PullToRefreshLayout) ll_view_content
		// .findViewById(R.id.ptr_layout);
		//
		// ActionBarPullToRefresh.from(getActivity()).allChildrenArePullable().setup(mPullToRefreshLayout);

		ContactTable table = new ContactTable();
		list = table.getAllContacts();
		listview = (RefreshListView) ll_view_content.findViewById(R.id.ptr_listview);
		adapter = new ContactAdapter(list);
		// Set the List Adapter to display the sample items
		listview.setAdapter(adapter);
		ll_net_unavailable = (LinearLayout) ll_view_content.findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) ll_view_content.findViewById(R.id.ll_empty);
		ll_empty.setVisibility(View.GONE);
		LayoutParams lps = ll_empty.getLayoutParams();
		if (lps == null)
			lps = new LayoutParams(LayoutParams.MATCH_PARENT, activity.Height);
		lps.height = activity.Height;
		ll_empty.setLayoutParams(lps);
		ll_net_unavailable.setLayoutParams(lps);

		ll_net_unavailable.setOnClickListener(this);
		ll_empty.setOnClickListener(this);
		tv_notice = (TextView) ll_view_content.findViewById(R.id.tv_notice);
		iv_bg_empty = (ImageView) ll_view_content.findViewById(R.id.iv_bg_empty);
		iv_bg_empty.setOnClickListener(this);
			// fragment可见时执行加载数据或者进度条等
//		if (SharePrefUtil.getBoolean(Conast.LOGIN)) {
//		
//		} else {
//			handler.sendEmptyMessage(IResult.UN_LOGIN);
//		}
		addListener();
		return ll_view_content;
	}

	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		activity.unregisterReceiver(receiver);
	}
	private void addListener() {
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// if (null != EMChatManager.getInstance() &&
				// !EMChatManager.getInstance().isConnected()) {
				// Logger.dout(TAG + " im 没连接上！");
				// }
				if (!NetWorkUtils.detect(activity)) {
					activity.showToast(getString(R.string.network_unavailable));
				}
				ContactEntry item = (ContactEntry) adapter.getItem(position - 1);
				Intent intent = new Intent(activity, DoctorDetailInfoActivity.class);
				intent.putExtra("doctor_name", item.getREALNAME());
				intent.putExtra("tochat_userId", item.getMEMBERID());
				intent.putExtra("doctor_avatar", item.getAVATAR());
				intent.putExtra("data", item);
				intent.putExtra("isFollow", item.getDOCTOR_FANS().isFollow());
				startActivity(intent);
				//startActivityForResult(intent, REQUEST_DOCTOR_DETAIL);
			}
		});

		listview.setOnRefreshListener(new IOnRefreshListener() {

			@Override
			public void OnRefresh() {
				refresh();
			}
		});
		listview.setOnLoadMoreListener(new IOnLoadMoreListener() {

			@Override
			public void OnLoadMore() {
				loadMore();
			}
		});
	}

	public void refresh() {
		ll_empty.setVisibility(View.GONE);
		if (NetWorkUtils.detect(activity)) {// 判断是否连接网络
			// mPullToRefreshLayout.setRefreshing(true);
			if (adapter == null || adapter.getCount() == 0) {
				activity.loading(IMessage.LOADING);
			}
			refresh = true;
			page = 1;
			getContactList(page);
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	protected void loadMore() {// 加载更多
		if (NetWorkUtils.detect(activity)) {
			refresh = false;
			page++;
			getContactList(page);
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.dout(TAG + "onResume");
		if (null != adapter) {
			adapter.notifyDataSetChanged();
		}

		if (SharePrefUtil.getBoolean(Conast.LOGIN)) {
			if(adapter.getCount()==0)
				refresh();
		} else {
			handler.sendEmptyMessage(IResult.UN_LOGIN);
		}
	}

	@Override
	public void onPause() {
		super.onStop();
	}

	@Override
	public boolean handleMessage(Message msg) {
		try {
			listview.onRefreshComplete();
			switch (msg.what) {
			case IResult.CONTACT_LIST:
				activity.destroyDialog();
				ll_net_unavailable.setVisibility(View.GONE);
				final ContactEntity response = (ContactEntity) msg.obj;
				if (null != response) {
					if (response.getSuccess() == 1) {
						init = true;
						totalpages = response.getPages();
						if (refresh) {
							if (response.getData() == null || response.getData().size() == 0) {
								ll_empty.setVisibility(View.VISIBLE);
								ll_empty.setEnabled(false);
								tv_notice.setText(getString(R.string.text_get_data_none_contact));
								listview.setVisibility(View.GONE);
								ContactTable table = new ContactTable();
								table.deleteByUserId();
							} else {
								new Thread() {
									public void run() {
										ContactTable table = new ContactTable();
										table.deleteByUserId();
										table.savePositionName(response.getData());
									};
								}.start();
								ll_empty.setVisibility(View.GONE);
								listview.setVisibility(View.VISIBLE);
								ll_net_unavailable.setVisibility(View.GONE);
								adapter.change(response.getData());
							}
						} else {
							adapter.add(response.getData());
						}
						if (page < totalpages) {
							listview.onLoadMoreComplete(false);
						} else {
							listview.onLoadMoreComplete(true);
						}
					} else {
						if (adapter.getCount() == 0) {
							ll_empty.setVisibility(View.VISIBLE);
							ll_empty.setEnabled(false);
							tv_notice.setText(getString(R.string.text_get_data_none));
							listview.setVisibility(View.GONE);
						}
					}
				}
				break;
			case IResult.RESULT:
				break;
			case IResult.END:
				break;
			case IResult.NET_ERROR:
				activity.destroyDialog();
				activity.showToast(getString(R.string.network_unavailable));
				ContactTable table = new ContactTable();
				if (null != table.getAllContacts() && table.getAllContacts().size() > 0) {

				} else {
					ll_net_unavailable.setVisibility(View.VISIBLE);
					ll_empty.setVisibility(View.GONE);
				}
				break;
			case IResult.DATA_ERROR:
				activity.destroyDialog();
				listview.setVisibility(View.GONE);
				ll_net_unavailable.setVisibility(View.GONE);
				ll_empty.setVisibility(View.VISIBLE);
				activity.showToast(IMessage.DATA_ERROR);
				break;
			case IResult.UN_LOGIN:
				activity.destroyDialog();
				listview.setVisibility(View.GONE);
				ll_net_unavailable.setVisibility(View.GONE);
				ll_empty.setVisibility(View.VISIBLE);
				tv_notice.setText(getString(R.string.text_is_un_login));
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

//	public void onEvent(LoginSuccessEvent successEvent) {
//		Logger.dout("login success");
//		refresh();
//		activity.loginIM();
//	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	/** 获取联系人 */
	private void getContactList(int page) {
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
		params.put("page", page + "");
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_contact_list, ContactEntity.class, params, new Response.Listener() {
			@Override
			public void onResponse(Object response) {
				Message message = new Message();
				message.obj = response;
				message.what = IResult.CONTACT_LIST;
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
	}
	

	@Override
	public void onDestroy() {
		super.onDestroy();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_contact_list);
		EventBus.getDefault().removeStickyEvent(LoginSuccessEvent.class);
	}

	/** 联系人适配 */
	private class ContactAdapter extends BaseAdapter {
		private List<ContactEntry> listItems;

		public ContactAdapter(ArrayList<ContactEntry> listItems) {
			if (listItems == null) {
				listItems = new ArrayList<ContactEntry>();
			}
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			return (null != listItems && listItems.size() > 0) ? listItems.size() : 0;
		}

		@Override
		public Object getItem(int position) {
			return (listItems == null || listItems.size() == 0) ? null : listItems.get(position);
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
				convertView = LayoutInflater.from(activity).inflate(R.layout.contact_item, null);
				holder.image_person = (CircleImageView) convertView.findViewById(R.id.image_person);
				// holder.image_person.setImageResource(R.drawable.avatar_medium);
				holder.tv_position = (TextView) convertView.findViewById(R.id.tv_position);
				holder.tv_hos_name = (TextView) convertView.findViewById(R.id.tv_hos_name);
				holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
				holder.tv_consult_info = (TextView) convertView.findViewById(R.id.tv_consult_info);
				holder.unreadLabel = (ImageView) convertView.findViewById(R.id.unreadLabel);
				holder.iv_attention = (ImageView) convertView.findViewById(R.id.iv_attention);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final ContactEntry entry = listItems.get(position);
			if (entry != null) {
				activity.imageLoader.displayImage(entry.getAVATAR(), holder.image_person, Util.getOptions_default_portrait());
				holder.tv_user_name.setText(entry.getREALNAME());
				if (!TextUtils.isEmpty(entry.getISSYSTEMMSG()) && "1".equals(entry.getISSYSTEMMSG())) {
					holder.tv_consult_info.setText(activity.getString(R.string.system_msg));
				} else {
					holder.tv_consult_info.setText(entry.getCONTENT());
				}
				if (TextUtils.isEmpty(entry.getHOSPITALNAME())) {
					holder.tv_hos_name.setVisibility(View.GONE);
				} else {
					holder.tv_hos_name.setVisibility(View.VISIBLE);
					holder.tv_hos_name.setText(entry.getHOSPITALNAME());
				}
				if (TextUtils.isEmpty(entry.getPROFESSIONAL())) {
					holder.tv_position.setVisibility(View.GONE);
				} else {
					holder.tv_position.setVisibility(View.VISIBLE);
					holder.tv_position.setText(entry.getPROFESSIONAL());
				}

				if("1".equals(entry.getDOCTOR_FANS().getIS_MY_FANS())){
					holder.iv_attention.setVisibility(View.VISIBLE);
				} else {
					holder.iv_attention.setVisibility(View.GONE);
				}
				
				EMConversation conversation = EMChatManager.getInstance().getConversation(entry.getMEMBERID());
				//XXX
				if (conversation.getUnreadMsgCount() > 0) {//显示与此用户的消息未读数
					// holder.unreadLabel.setText(String.valueOf(conversation.getUnreadMsgCount()));
					holder.unreadLabel.setVisibility(View.VISIBLE);
				} else {
					holder.unreadLabel.setVisibility(View.INVISIBLE);
				}
			}

			holder.image_person.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(activity, ImageActivity.class);
					intent.putExtra("avatar", entry.getAVATAR());
					startActivity(intent);

				}
			});
			
			return convertView;
		}

		public void change(List<ContactEntry> lists) {
			if (lists == null) {
				lists = new ArrayList<ContactEntry>();
			}
			this.listItems = lists;
			notifyDataSetChanged();
		}

		public void add(List<ContactEntry> list) {
			this.listItems.addAll(list);
			notifyDataSetChanged();
		}
		public List<ContactEntry> getData(){
			
			return this.listItems;
		}
	}
	

	class ViewHolder {
		public TextView tv_position;
		public TextView tv_hos_name;
		public ImageView unreadLabel;
		CircleImageView image_person;
		TextView tv_user_name;
		TextView tv_consult_info;
		ImageView iv_attention;
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.ll_empty) {
			if (SharePrefUtil.getBoolean(Conast.LOGIN)) {
				refresh();
			} else {
				Intent intent = new Intent(activity, LoginActivity.class);
				intent.putExtra("origin", MyDoctorFragment.class.getSimpleName());
				startActivity(intent);
			}
		} else if (id == R.id.ll_net_unavailable) {
			refresh();
		} else if (id == R.id.iv_bg_empty) {
			if (SharePrefUtil.getBoolean(Conast.LOGIN)) {
				refresh();
			} else {
				Intent intent = new Intent(activity, LoginActivity.class);
				intent.putExtra("origin", MyDoctorFragment.class.getSimpleName());
				startActivity(intent);
			}
		}
	}
	
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(MyApplication.GET_CONTACT_LIST)){
				getContactList(page);
			}
		}
	}
}
