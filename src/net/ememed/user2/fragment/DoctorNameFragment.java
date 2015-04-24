package net.ememed.user2.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.DoctorClininActivity;
import net.ememed.user2.activity.DoctorSearchActivity;
import net.ememed.user2.db.DoctorInfoTable;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.DoctorInfo;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.NewsEntity;
import net.ememed.user2.entity.NewsItem;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class DoctorNameFragment extends Fragment implements Handler.Callback,
		OnClickListener, OnRefreshListener {

	private static final String TAG = "tag";
	private String fragmentKey = "";
	private DoctorSearchActivity activity = null;
	private Handler mHandler;

	private LinearLayout ll_view_search_doctor;
	private RefreshListView lvCustomEvas;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;
	// private CustomEvaluateAdapter new_adapter;
	private DoctorAdapter new_adapter;

	private boolean refresh = true;
	private int page = 1;
	private String type_id = "0";
	private boolean init = false;
	private int totalpages = 1;
	private DoctorInfoTable table;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler(this);
		activity = (DoctorSearchActivity) getActivity();
		fragmentKey = getArguments().getString("fragmentKey");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		System.out.println("TestFragment onCreateView fragmentKey : "+ fragmentKey);

		View view = inflater.inflate(R.layout.root_layout, null);
		FrameLayout mContentView = (FrameLayout) view
				.findViewById(R.id.mainView);

		// mPullToRefreshLayout = (PullToRefreshLayout)
		// view.findViewById(R.id.ptr_layout);
		// ActionBarPullToRefresh.from(activity).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);

		ll_view_search_doctor = (LinearLayout) LayoutInflater.from(activity)
				.inflate(R.layout.fragment_search_doctor, null);
		lvCustomEvas = (RefreshListView) ll_view_search_doctor
				.findViewById(R.id.lv_contact_class);
		ll_net_unavailable = (LinearLayout) ll_view_search_doctor
				.findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) ll_view_search_doctor
				.findViewById(R.id.ll_empty);
		ll_net_unavailable.setOnClickListener(this);
		table = new DoctorInfoTable();
		ArrayList<DoctorEntry> infos = (ArrayList<DoctorEntry>) table
				.findDoctorInfos();
		new_adapter = new DoctorAdapter(null);
		lvCustomEvas.setAdapter(new_adapter);
		mContentView.addView(ll_view_search_doctor);
		addListener();
		return view;
	}

	private void addListener() {
		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DoctorEntry entry = (DoctorEntry) new_adapter.getItem(position-1);
				if (null != entry) {
					Intent intent = new Intent(activity,DoctorClininActivity.class);
					intent.putExtra("current_service", "1");
					intent.putExtra("title", entry.getREALNAME());
					intent.putExtra("entry", entry);
					startActivity(intent);	
				}
			}
		});
		lvCustomEvas.setOnRefreshListener(new IOnRefreshListener() {

			@Override
			public void OnRefresh() {
				refresh();
			}
		});
		lvCustomEvas.setOnLoadMoreListener(new IOnLoadMoreListener() {

			@Override
			public void OnLoadMore() {
				loadMore();
			}
		});
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时执行加载数据或者进度条等
			Logger.dout(TAG + "isVisibleToUser");
			if (!init) {
				// getNewsList(page , type_id);
				getDoctorList(activity.keyword);
			}
		} else {
			// 不可见时不执行操作
			Logger.dout(TAG + "unVisibleToUser");
		}
	}

	protected void refresh() {
		refresh = true;
		page = 1;
		getDoctorList(activity.keyword);
	}

	protected void loadMore() {
		refresh = false;
		page++;
		getDoctorList(activity.keyword);
	}

	private void getDoctorList(String keyword) {

		if (NetWorkUtils.detect(activity)) {
			activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("q", keyword);
			params.put("s", "");
			params.put("f", "");
			params.put("p", "0");
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.search_doctor_xs, DoctorInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.SEARCH_DOCTOR;
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


	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
//		System.out.println("TestFragment onSaveInstanceState fragmentKey : "+ fragmentKey);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if (id == R.id.ll_net_unavailable) {
			refresh();
		}

	}

	protected void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

	@Override
	public boolean handleMessage(Message msg) {
//		lvCustomEvas.onRefreshComplete();
		try {
			switch (msg.what) {
			case IResult.SEARCH_DOCTOR:
				activity.destroyDialog();
				final DoctorInfo response = (DoctorInfo) msg.obj;
				if (null != response) {
					if (response.getSuccess() == 1) {
						init = true;
						totalpages = response.getPages();
						if (refresh) {
							if (response.getData() == null
									|| response.getData().size() == 0) {
								ll_empty.setVisibility(View.VISIBLE);
								lvCustomEvas.setVisibility(View.GONE);
							} else {
								ll_empty.setVisibility(View.GONE);
								lvCustomEvas.setVisibility(View.VISIBLE);
//								new Thread() {
//									public void run() {
//										table.saveDoctorInfo(response.getData());
//									};
//								}.start();
								new_adapter.change(response.getData());
							}
						} else {
							table.saveDoctorInfo(response.getData());
							new_adapter.add(response.getData());
						}
						if (page < totalpages) {
							lvCustomEvas.onLoadMoreComplete(false);
						} else {
							lvCustomEvas.onLoadMoreComplete(true);
						}
					} else {
						ll_empty.setVisibility(View.VISIBLE);
						lvCustomEvas.setVisibility(View.GONE);
					}
				} else {
					ll_empty.setVisibility(View.VISIBLE);
					lvCustomEvas.setVisibility(View.GONE);
				}
				break;
			case IResult.RESULT:
				break;
			case IResult.END:

				break;
			case IResult.NET_ERROR:
				activity.showToast(IMessage.NET_ERROR);
				ll_net_unavailable.setVisibility(View.VISIBLE);
				ll_empty.setVisibility(View.GONE);
				lvCustomEvas.setVisibility(View.GONE);
				break;
			case IResult.DATA_ERROR:
				activity.showToast(IMessage.DATA_ERROR);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	// 医生的适配器
	private class DoctorAdapter extends BaseAdapter {
		private List<DoctorEntry> listItems;

		public DoctorAdapter(ArrayList<DoctorEntry> listItems) {
			if (listItems == null) {
				listItems = new ArrayList<DoctorEntry>();
			}
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return (listItems == null || listItems.size() == 0) ? null
					: listItems.get(position);
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
				convertView = LayoutInflater.from(activity).inflate(
						R.layout.doctor_info_item, null);

				holder.tv_doctor_name = (TextView) convertView
						.findViewById(R.id.tv_doctor_name);
				holder.tv_doctor_alias = (TextView) convertView
						.findViewById(R.id.tv_doctor_alias);
				holder.tv_hos_address = (TextView) convertView
						.findViewById(R.id.tv_hos_address);
				holder.doctor_head_portrait = (ImageView) convertView
						.findViewById(R.id.doctor_head_portrait);
				holder.tv_search_speciality = (TextView) convertView
						.findViewById(R.id.tv_search_speciality);
				holder.tv_price = (TextView) convertView
						.findViewById(R.id.tv_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			DoctorEntry entry = listItems.get(position);
			if (null != entry) {

				activity.imageLoader.displayImage(entry.getAVATAR(),
						holder.doctor_head_portrait, Util.getOptions_pic());
				holder.tv_doctor_name.setText(entry.getREALNAME());
				holder.tv_doctor_alias.setText(entry.getPROFESSIONAL());
				holder.tv_hos_address.setText(entry.getHOSPITALNAME());
				holder.tv_search_speciality.setText(entry.getSPECIALITY());
				String str = entry.getSPECIALITY(); // 不能读长度？
				if (str != "" && str != null) {
					TextView shangchang = (TextView) convertView
							.findViewById(R.id.tv_search_shangchang);
					shangchang.setVisibility(View.VISIBLE);
					TextView tv_search_speciality = (TextView) convertView
							.findViewById(R.id.tv_search_speciality);
					tv_search_speciality.setVisibility(View.VISIBLE); // 这里我改
				}

				DoctorServiceEntry current_service = entry.getOFFER_SERVICE();
				
				// "service": {
				// "1": "图文咨询",
				// "2": "预约通话",
				// "3": "预约加号",
				// "4": "上门会诊",
				// "14": "预约住院",
				// "15": "签约私人医生服务"
				// },
//				 if ("1".equals(current_service)) {//图文
//				 holder.tv_price.setText("￥"+Util.DecZore(current_service.getOFFER_TEXT().getOFFER_PRICE()));
//				 if(current_service.getOFFER_TEXT().getOFFER_PRICE()==0)
//				 holder.tv_price.setText("免费");
//				 } else if ("2".equals(current_service)){//电话
//				 holder.tv_price.setText("￥"+Util.DecZore(current_service.getOFFER_CALL().getOFFER_PRICE()));
//				 if(current_service.getOFFER_CALL().getOFFER_PRICE()==0)
//				 holder.tv_price.setText("免费");
//				 } else if ("3".equals(current_service)){//预约加号
//				 holder.tv_price.setText("￥"+Util.DecZore(current_service.getOFFER_JIAHAO().getOFFER_PRICE()));
//				 if(current_service.getOFFER_JIAHAO().getOFFER_PRICE()==0)
//				 holder.tv_price.setText("免费");
//				 } else if ("4".equals(current_service)){//上门会诊
//				 holder.tv_price.setText("￥"+Util.DecZore(current_service.getOFFER_SHANGMEN().getOFFER_PRICE()));
//				 if(current_service.getOFFER_SHANGMEN().getOFFER_PRICE()==0)
//				 holder.tv_price.setText("免费");
//				 } else if ("14".equals(current_service)){//预约住院
//				 holder.tv_price.setText("￥"+Util.DecZore(current_service.getOFFER_ZHUYUAN().getOFFER_PRICE()));
//				 if(current_service.getOFFER_ZHUYUAN().getOFFER_PRICE()==0)
//				 holder.tv_price.setText("免费");
//				 } else if ("15".equals(current_service)){//签约私人医生服务
//				 //
//				 holder.tv_price.setText("￥"+current_service.getOFFER_SHANGMEN().getOFFER_PRICE());
//				 } //以上0改成免费
			}
			return convertView;
		}

		public void change(List<DoctorEntry> lists) {
			if (lists == null) {
				lists = new ArrayList<DoctorEntry>();
			}
			this.listItems = lists;
			notifyDataSetChanged();
		}

		public void add(List<DoctorEntry> list) {
			this.listItems.addAll(list);
			notifyDataSetChanged();
		}
	}

	class ViewHolder {
		public TextView tv_price;
		public TextView tv_hos_address;
		public TextView tv_doctor_alias;
		public TextView tv_doctor_name;
		ImageView doctor_head_portrait;
		TextView tv_search_speciality;
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub

	}

}
