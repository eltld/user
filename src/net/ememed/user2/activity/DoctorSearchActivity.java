package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.DoctorInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

public class DoctorSearchActivity extends BasicActivity implements
		OnClickListener {
	// private PagerAdapter mPagerAdapter = null;
	// private CustomViewPager mViewPager = null;
	// private TabPageIndicator mIndicator = null;
	private Button btn;
	private boolean finish = false;
	public String keyword;

	private LinearLayout ll_view_search_doctor;
	private RefreshListView lvCustomEvas;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;
	private DoctorAdapter new_adapter;
	private boolean refresh = true;
	private int page = 1;
	private boolean init;
	private int totalpages;
	private TextView tv_notice;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		// setContentView(R.layout.layout_doctor_search);
		setContentView(R.layout.fragment_search_doctor);
		keyword = getIntent().getStringExtra("keyword");
		// title = getIntent().getStringExtra("title");
		// UICore.eventTask(this, this, 1, "正在努力加载中....", null);
	}

	@Override
	protected void setupView() {
		super.setupView();

		btn = (Button) findViewById(R.id.btn_addhealth);
		btn.setVisibility(View.GONE);
		TextView tv_title = (TextView) findViewById(R.id.top_title);
//		tv_title.setText(getString(R.string.search_box_hint));
		tv_title.setText("搜索结果");
		ll_view_search_doctor = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.fragment_search_doctor, null);
		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_contact_class);
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		tv_notice =  (TextView) findViewById(R.id.tv_notice);
		ll_net_unavailable.setOnClickListener(this);
		new_adapter = new DoctorAdapter(null);
		lvCustomEvas.setAdapter(new_adapter);

	}

	@Override
	protected void getData() {
		getDoctorList(keyword, page);
		super.getData();
	}

	@Override
	protected void addListener() {
		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				DoctorEntry entry = (DoctorEntry) new_adapter
						.getItem(position - 1);
				/*判断服务是否开启*/
				if (null != entry) {
//					DoctorServiceEntry oFFER_SERVICE = new DoctorServiceEntry();
//					OfferServiceEntry2 OFFER_TEXT = new OfferServiceEntry2();
//					
//					OFFER_TEXT.setIS_OFFER(entry.getTEXT_OFFER());
//					OFFER_TEXT.setOFFER_PRICE(String.valueOf(entry.getTEXT_OFFER_PRICE()));
//
//					OfferServiceEntry OFFER_CALL = new OfferServiceEntry();
//					OFFER_CALL.setIS_OFFER(entry.getCALL_OFFER());
//					OFFER_CALL.setOFFER_PRICE(entry.getCALL_OFFER_PRICE());
//
//					OfferServiceEntry OFFER_ZHUYUAN = new OfferServiceEntry();
//					OFFER_ZHUYUAN.setIS_OFFER(entry.getZHUYUAN_OFFER());
//					OFFER_ZHUYUAN.setOFFER_PRICE(entry.getZHUYUAN_OFFER_PRICE());
//
//					OfferServiceEntry OFFER_JIAHAO = new OfferServiceEntry();
//					OFFER_JIAHAO.setIS_OFFER(entry.getJIAHAO_OFFER());
//					OFFER_JIAHAO.setOFFER_PRICE(entry.getJIAHAO_OFFER_PRICE());
//
//					OfferServiceEntry OFFER_SHANGMEN = new OfferServiceEntry();
//					OFFER_SHANGMEN.setIS_OFFER(entry.getSHANGMEN_OFFER());
//					OFFER_SHANGMEN.setOFFER_PRICE(entry.getSHANGMEN_OFFER());
//
//					OfferServicePacketEntry OFFER_PACKET = new OfferServicePacketEntry();
//					OFFER_PACKET.setPACKET_ID(entry.getPACKET_ID());
//					OFFER_PACKET.setSERVICE_CONTENT(entry.getSERVICE_CONTENT());
//					OFFER_PACKET.setPACKET_PERIOD_LIST(entry.getPERIOD_LIST());
//					OFFER_PACKET.setIS_OFFER(entry.getPACKET_OFFER());
////					System.out.println("entry.getPACKET_OFFER()===" + entry.getPACKET_OFFER());
//					
//					oFFER_SERVICE.setOFFER_TEXT(OFFER_TEXT);
//					oFFER_SERVICE.setOFFER_CALL(OFFER_CALL);
//					oFFER_SERVICE.setOFFER_ZHUYUAN(OFFER_ZHUYUAN);
//					oFFER_SERVICE.setOFFER_JIAHAO(OFFER_JIAHAO);
//					oFFER_SERVICE.setOFFER_SHANGMEN(OFFER_SHANGMEN);
//					oFFER_SERVICE.setOFFER_PACKET(OFFER_PACKET);
//
//					entry.setOFFER_SERVICE(oFFER_SERVICE);

					Intent intent = new Intent(DoctorSearchActivity.this,
							DoctorClininActivity.class);
					intent.putExtra("current_service", "1");
					intent.putExtra("title", entry.getREALNAME());
					intent.putExtra("entry", entry);
					
					System.out.println("entry  = "+entry);
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
		super.addListener();
	}

	protected void refresh() {
		refresh = true;
		page = 1;
		getDoctorList(keyword, page);
	}

	protected void loadMore() {
		refresh = false;
		page++;
		getDoctorList(keyword, page);
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		}
	}
	/**
	 * 迅搜--搜索医生(搜索引擎)
	 * @param keyword
	 * @param page
	 */
	private void getDoctorList(String keyword, int page) {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			/*
			 * q 查询关键词
			 * s 排序字段名称及方式（需商议）,为空时以匹配度排序
			 * f 查询类型，可以为空，为空的时候全文搜索
			 * p 页数
			 */
			params.put("q", keyword);
			params.put("s", "");
			params.put("f", "");
			params.put("p", page + "");
			
			if(true == SharePrefUtil.getBoolean(Conast.LOGIN)){
				params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			}else {
				params.put("memberid", "");
			}
			params.put("search_app_version", PublicUtil.getVersionName(DoctorSearchActivity.this));
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.search_doctor_xs, DoctorInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.SEARCH_DOCTOR;
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
		lvCustomEvas.onRefreshComplete();
		switch (msg.what) {
		case IResult.SEARCH_DOCTOR:
			
			ll_net_unavailable.setVisibility(View.GONE);
			final DoctorInfo response = (DoctorInfo) msg.obj;
			if (null != response) {
				if (response.getSuccess() == 1) {
					init = true;
					totalpages = response.getPages();
					if (refresh) {
						if (response.getData() == null || response.getData().size() == 0) {
							ll_empty.setVisibility(View.VISIBLE);
							tv_notice.setText("搜索不到结果,换个关键词试试??");
							lvCustomEvas.setVisibility(View.GONE);
						} else {
							ll_empty.setVisibility(View.GONE);
							lvCustomEvas.setVisibility(View.VISIBLE);
							new_adapter.change(response.getData());
						}
					} else {
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
		case IResult.DATA_ERROR:
			tv_notice.setText("搜索不到结果,换个关键词试试??");
			ll_empty.setVisibility(View.VISIBLE);
			lvCustomEvas.setVisibility(View.GONE);
			ll_net_unavailable.setVisibility(View.GONE);
			break;
		case IResult.NET_ERROR:
			ll_empty.setVisibility(View.GONE);
			lvCustomEvas.setVisibility(View.GONE);
			ll_net_unavailable.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		super.onResult(msg);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish = true;
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
				convertView = LayoutInflater.from(DoctorSearchActivity.this)
						.inflate(R.layout.doctor_info_item, null);

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

				imageLoader.displayImage(entry.getAVATAR(),
						holder.doctor_head_portrait, Util.getOptions_default_portrait());
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
	public void onClick(View v) {
		if (v.getId() == R.id.ll_empty) {
			loading(null);
			refresh();
		}
	}
}
