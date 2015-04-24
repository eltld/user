package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.OrderListEntity;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.entity.QuestionOrderErtry;
import net.ememed.user2.medicalhistory.activity.QuestionXiangQingActivity;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

/** 历史订单 */
public class DoctorHistoryOrderActivity extends BasicActivity implements
		OnRefreshListener {
	private TextView top_title;
	private RefreshListView list_view;
	private ContactAdapter adapter;
	private List<OrderListEntry> listItems;
	// private PullToRefreshLayout mPullToRefreshLayout;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;

	private boolean refresh = true;
	private int totalpages = 1;
	private int page = 1;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_history_order);
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("我的订单");

		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		// mPullToRefreshLayout = (PullToRefreshLayout)
		// findViewById(R.id.ptr_layout);
		// ActionBarPullToRefresh.from(DoctorHistoryOrderActivity.this).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);

		list_view = (RefreshListView) findViewById(R.id.ptr_listview);
		adapter = new ContactAdapter(null, null);
		// Set the List Adapter to display the sample items
		list_view.setAdapter(adapter);
		super.setupView();
	}

	@Override
	protected void getData() {
		refresh();
		super.getData();
	}

	@Override
	protected void addListener() {
		list_view.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				int QuestionDataPosition = adapter.getQuestionDataSize();
				if (QuestionDataPosition > (position - 1)) {
					QuestionOrderErtry questionOrderErtry = (QuestionOrderErtry) adapter
							.getlelectItem(position-1);
					Intent xq_intent = new Intent(DoctorHistoryOrderActivity.this,QuestionXiangQingActivity.class);
					xq_intent.putExtra("question_id", questionOrderErtry.getQUESTIONID());
					startActivity(xq_intent);
					finish();
				} else {
					
					
					OrderListEntry orderListEntry = (OrderListEntry) adapter
							.getlelectItem(position - 1);
					if (orderListEntry.getORDERTYPE().equals("15")) {
						Intent intent = new Intent(
								DoctorHistoryOrderActivity.this,
								JiuyiConsultDetailsActivity.class);
						intent.putExtra("item", orderListEntry);
						intent.putExtra("source", 3);
						intent.putExtra("tochat_userId",
								orderListEntry.getDOCTORID());
						intent.putExtra("doctor_avatar",
								orderListEntry.getAVATAR());
					} else if (orderListEntry.getORDERTYPE().equals("17")) {
						Intent intent = new Intent(
								DoctorHistoryOrderActivity.this,
								QuestionChatActivity.class);
						intent.putExtra("item", orderListEntry);
						intent.putExtra("source", 2);
						intent.putExtra("tochat_userId",
								orderListEntry.getDOCTORID());
						intent.putExtra("doctor_avatar",
								orderListEntry.getAVATAR());
						startActivity(intent);
					} else {
						Intent intent = new Intent(
								DoctorHistoryOrderActivity.this,
								ChatActivity.class);
						intent.putExtra("item", orderListEntry);
						intent.putExtra("source", 2);
						intent.putExtra("tochat_userId",
								orderListEntry.getDOCTORID());
						intent.putExtra("doctor_avatar",
								orderListEntry.getAVATAR());
						startActivity(intent);
					}
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
		super.addListener();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		}
	}

	protected void refresh() {
		refresh = true;
		page = 1;
		getDoctorOrderList(page);
	}

	protected void loadMore() {
		refresh = false;
		page++;
		getDoctorOrderList(page);
	}

	private void getDoctorOrderList(int page) {
		if (NetWorkUtils.detect(DoctorHistoryOrderActivity.this)) {
			// mPullToRefreshLayout.setRefreshing(true);
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("any_user_id", "");
			params.put("page", page + "");
			params.put("type", "0");

			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_order_list, OrderListEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.ORDER_LIST;
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
		try {
			switch (msg.what) {
			case IResult.ORDER_LIST:
				// mPullToRefreshLayout.setRefreshComplete();
				list_view.onRefreshComplete();
				destroyDialog();
				OrderListEntity olEntity = (OrderListEntity) msg.obj;
				if (null != olEntity) {
					if (olEntity.getSuccess() == 1) {
						if (null != olEntity.getData()
								&& olEntity.getData().size() > 0) {
							if (refresh) {
								adapter.change(olEntity.getData(),
										olEntity.getQuestion_data());
							} else {
								adapter.add(olEntity.getData());
							}
						} else {
							ll_empty.setVisibility(View.VISIBLE);
							list_view.setVisibility(View.GONE);
							Toast.makeText(getApplicationContext(),
									olEntity.getErrormsg(), 0).show();
						}

						if (page < olEntity.getPages()) {
							list_view.onLoadMoreComplete(false);
						} else {
							list_view.onLoadMoreComplete(true);
						}
					} else {
						showToast(olEntity.getErrormsg());
						ll_empty.setVisibility(View.VISIBLE);
						list_view.setVisibility(View.GONE);
					}
				} else {
					showToast(IMessage.DATA_ERROR);
					ll_empty.setVisibility(View.VISIBLE);
					list_view.setVisibility(View.GONE);
				}
				break;
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				ll_net_unavailable.setVisibility(View.VISIBLE);
				list_view.setVisibility(View.GONE);
				break;
			case IResult.DATA_ERROR:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	private class ContactAdapter extends BaseAdapter {
		List<OrderListEntry> listItems;
		List<QuestionOrderErtry> question_data;

		public ContactAdapter(List<OrderListEntry> listItems,
				List<QuestionOrderErtry> question_data) {
			if (listItems == null) {
				listItems = new ArrayList<OrderListEntry>();
			}
			this.listItems = listItems;
			if (question_data == null) {
				question_data = new ArrayList<QuestionOrderErtry>();
			}
			this.question_data = question_data;
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
				convertView = LayoutInflater.from(
						DoctorHistoryOrderActivity.this).inflate(
						R.layout.fragment_doctor_order_item, null);
				holder.tv_user_name = (TextView) convertView
						.findViewById(R.id.tv_user_name);
				holder.tv_consult_info = (TextView) convertView
						.findViewById(R.id.tv_consult_info);
				holder.tv_consult_state = (TextView) convertView
						.findViewById(R.id.tv_consult_state);
				holder.tv_service_price = (TextView) convertView
						.findViewById(R.id.tv_service_price);
				holder.content_tx = (TextView) convertView
						.findViewById(R.id.content_tx);
				holder.iv_consult_pic = (ImageView) convertView
						.findViewById(R.id.iv_consult_pic);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			if (position < question_data.size()) {
				QuestionOrderErtry orderErtry = question_data.get(position);
				if (orderErtry.getSYMPTOMS() == null
						|| orderErtry.getSYMPTOMS().equals("")) {
					holder.content_tx.setVisibility(View.GONE);
				} else {
					holder.content_tx.setText(orderErtry.getSYMPTOMS());
				}
				holder.tv_service_price.setText("免费");
				if (!TextUtils.isEmpty(orderErtry.getCREATE_TIME())) {
					holder.tv_consult_info.setText(orderErtry.getCREATE_TIME());
				}
				holder.tv_user_name.setText("免费提问");
				holder.tv_consult_state.setText("待回复");
				holder.iv_consult_pic
				.setImageResource(R.drawable.question_mianfei);

			} else {
				OrderListEntry orderListEntry = listItems.get(position);

				if (orderListEntry != null) {
					holder.tv_service_price.setText("￥"
							+ orderListEntry.getORDERAMOUNT());
					if (orderListEntry.getDESC_TEXT() == null
							|| orderListEntry.getDESC_TEXT().equals("")) {
						holder.content_tx.setVisibility(View.GONE);
					} else {
						holder.content_tx
								.setText(orderListEntry.getDESC_TEXT());
					}
					if (orderListEntry.getORDERTYPE().equals("1")) {
						holder.tv_user_name
								.setText(getString(R.string.home_item_textconsult));
						holder.iv_consult_pic
								.setImageResource(R.drawable.clinic_text_consult);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							if (orderListEntry.getSTATE().equals("2")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_three));
							} else {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_four));
							}
						}
					} else if (orderListEntry.getORDERTYPE().equals("2")) {
						holder.tv_user_name
								.setText(getString(R.string.home_item_phoneconsult));
						holder.iv_consult_pic
								.setImageResource(R.drawable.clinic_phone_consult);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							if (orderListEntry.getSTATE().equals("0")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_five));
							} else if (orderListEntry.getSTATE().equals("2")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_six));
							} else {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_four));
							}
						}

					} else if (orderListEntry.getORDERTYPE().equals("3")) {
						holder.tv_user_name
								.setText(getString(R.string.home_item_jiuyi));
						holder.iv_consult_pic
								.setImageResource(R.drawable.clinic_jiahao);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							if (orderListEntry.getSTATE().equals("0")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_one));
							} else if (orderListEntry.getSTATE().equals("1")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_two));
							} else if (orderListEntry.getSTATE().equals("2")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_three));
							} else {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_four));
							}
						}
					} else if (orderListEntry.getORDERTYPE().equals("4")) {
						holder.tv_user_name
								.setText(getString(R.string.home_item_shangmen));
						holder.iv_consult_pic
								.setImageResource(R.drawable.clinic_shangmen);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							if (orderListEntry.getSTATE().equals("0")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_one));
							} else if (orderListEntry.getSTATE().equals("1")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_two));
							} else if (orderListEntry.getSTATE().equals("2")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_three));
							} else {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_four));
							}
						}

					} else if (orderListEntry.getORDERTYPE().equals("14")) {
						holder.tv_user_name
								.setText(getString(R.string.home_item_zhuyuan));
						holder.iv_consult_pic
								.setImageResource(R.drawable.clinic_zhuyuan);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							if (orderListEntry.getSTATE().equals("0")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_one));
							} else if (orderListEntry.getSTATE().equals("1")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_two));
							} else if (orderListEntry.getSTATE().equals("2")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_three));
							} else {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_four));
							}
						}
					} else if (orderListEntry.getORDERTYPE().equals("15")) {
						holder.tv_user_name
								.setText(getString(R.string.home_item_person_doctor));
						holder.iv_consult_pic
								.setImageResource(R.drawable.ic_private_doctor);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							if (orderListEntry.getSTATE().equals("2")) {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_three));
							} else {
								holder.tv_consult_state
										.setText(getString(R.string.offline_consult_title_four));
							}
						}
					} else if (orderListEntry.getORDERTYPE().equals("16")) {
						holder.tv_user_name
								.setText(getString(R.string.home_item_custom));
						holder.iv_consult_pic
								.setImageResource(R.drawable.clinic_custom);
						if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
							if (!TextUtils.isEmpty(orderListEntry.getSTATE())) {
								if (orderListEntry.getSTATE().equals("0")) {
									holder.tv_consult_state
											.setText(getString(R.string.offline_consult_title_one));
								} else if (orderListEntry.getSTATE()
										.equals("1")) {
									holder.tv_consult_state
											.setText(getString(R.string.offline_consult_title_two));
								} else if (orderListEntry.getSTATE()
										.equals("2")) {
									holder.tv_consult_state
											.setText(getString(R.string.offline_consult_title_three));
								} else {
									holder.tv_consult_state
											.setText(getString(R.string.offline_consult_title_four));
								}
							}
						}
					} else if (orderListEntry.getORDERTYPE().equals("17")) {
						holder.iv_consult_pic
						.setImageResource(R.drawable.question_mianfei);
						holder.tv_user_name.setText(orderListEntry
								.getORDERTYPENAME());
						holder.tv_consult_state.setText(orderListEntry.getSTATE_DESC());
						holder.tv_service_price.setText("免费");
					}
					holder.tv_consult_info.setText(orderListEntry.getADDTIME());
				}
				// if (orderListEntry != null) {
				// holder.tv_user_name.setText(orderListEntry.getCONTRACTOR());
				// holder.tv_consult_info.setText(orderListEntry.getADDTIME());
				// }
			}

			return convertView;
		}

		public int getQuestionDataSize() {

			return this.question_data.size();
		}

		public Object getlelectItem(int position) {
			if (position < question_data.size()) {
				return question_data.get(position);
			} else {
				return listItems.get(position);
			}
		}

		public void change(List<OrderListEntry> lists,
				List<QuestionOrderErtry> question_data) {
			// if(lists == null){
			// lists = new ArrayList<OrderListEntry>();
			// }
			// this.listItems = lists;
			// notifyDataSetChanged();

			this.listItems.clear();
			if (question_data != null) {
				for (int i = 0; i < question_data.size(); i++) {
					this.listItems.add(null);
				}
			}
			if (lists == null) {
				lists = new ArrayList<OrderListEntry>();
			}
			this.listItems.addAll(lists);
			this.question_data = question_data;
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
		TextView tv_service_price;
		TextView content_tx;
		ImageView iv_consult_pic;
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		refresh();
	}
}
