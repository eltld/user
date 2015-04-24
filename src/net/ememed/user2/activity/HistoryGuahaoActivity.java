package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.GuahaoEvent;
import de.greenrobot.event.RegisterSuccessEvent;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.HospitalEntry;
import net.ememed.user2.entity.HospitalsInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.UserGuahaoEntry;
import net.ememed.user2.entity.UserGuahaoInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

/**
 * 预约挂号
 * 
 * @author pengwl
 * 
 */
public class HistoryGuahaoActivity extends BasicActivity {

	private RefreshListView lvCustomEvas;
	private String user_id;
	private boolean refresh = true;
	private int page = 1;
	private int totalpages = 1;
	private boolean isflag = false;
	private GuahaosAdapter adapter;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_history_guahao);
		user_id = SharePrefUtil.getString(Conast.MEMBER_ID);
		EventBus.getDefault().registerSticky(this, GuahaoEvent.class);
	}

	@Override
	protected void setupView() {
		TextView tv_top_title = (TextView) findViewById(R.id.top_title);
		tv_top_title.setText("我的预约");
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_contact_class);
		adapter = new GuahaosAdapter(null);
		lvCustomEvas.setAdapter(adapter);
	}

	@Override
	protected void getData() {
		getUserGuahaoRecord();
		super.getData();
	}

	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		default:
			break;
		}
	}

	@Override
	protected void addListener() {
		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				UserGuahaoEntry userGuahaoEntry = (UserGuahaoEntry) adapter.getItem(position - 1);
				Intent intent = new Intent(HistoryGuahaoActivity.this, GuahaoDetailsActivity.class);
				intent.putExtra("hosp_entry", userGuahaoEntry);
				startActivity(intent);
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

		ll_net_unavailable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isflag = true;
				setNetWork();
			}
		});

	}

	protected void loadMore() {
		refresh = false;
		page++;
		getUserGuahaoRecord();
	}

	private void refresh() {
		refresh = true;
		page = 1;
		getUserGuahaoRecord();

	}

	/** 获取用户挂号的历史记录 */
	private void getUserGuahaoRecord() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("page", page + "");
			params.put("userid", user_id);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_user_guahao_record, UserGuahaoInfo.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_USER_GUAHAO_RECORD;
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
		case IResult.GET_USER_GUAHAO_RECORD:
			UserGuahaoInfo info = (UserGuahaoInfo) msg.obj;
			if (info != null) {
				switch (info.getSuccess()) {
				case 1:
					totalpages = info.getPages();
						if (info.getData() != null && info.getData().size() > 0){
							if(refresh)
								adapter.change(info.getData());// refresh listView
							else
								adapter.add(info.getData());
						}else {
							lvCustomEvas.setVisibility(View.GONE);
							ll_empty.setVisibility(View.VISIBLE);
						}
					
					break;
				default:
					showToast(info.getErrormsg());
					break;
				}
			}
			if (page < totalpages) {
				lvCustomEvas.onLoadMoreComplete(false);
			} else {
				lvCustomEvas.onLoadMoreComplete(true);
			}
			break;
		case IResult.NET_ERROR:
			ll_net_unavailable.setVisibility(View.VISIBLE);
			ll_empty.setVisibility(View.GONE);
			lvCustomEvas.setVisibility(View.GONE);
			break;
		case IResult.DATA_ERROR:
			ll_empty.setVisibility(View.VISIBLE);
			lvCustomEvas.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	public void onEvent(GuahaoEvent testEvent) {
		refresh();
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(GuahaoEvent.class);
	}

	class GuahaosAdapter extends BaseAdapter {

		List<UserGuahaoEntry> list;

		public GuahaosAdapter(List<UserGuahaoEntry> list) {
			if (list == null)
				list = new ArrayList<UserGuahaoEntry>();
			this.list = list;
		}

		public void change(List<UserGuahaoEntry> list) {
			if (list == null)
				list = new ArrayList<UserGuahaoEntry>();
			this.list = list;
			notifyDataSetChanged();

		}

		public void add(List<UserGuahaoEntry> list) {
			this.list.addAll(list);
			notifyDataSetChanged();

		}

		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			HolderView holder = null;
			if (convertView == null) {
				holder = new HolderView();
				convertView = View.inflate(HistoryGuahaoActivity.this, R.layout.item_guahao_history, null);
				holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.tv_doctor = (TextView) convertView.findViewById(R.id.tv_doctor);
				holder.tv_guahao_status = (TextView) convertView.findViewById(R.id.tv_guahao_status);
				convertView.setTag(holder);
			} else {
				holder = (HolderView) convertView.getTag();
			}
			UserGuahaoEntry userGuahaoEntry = list.get(position);
			holder.tv_name.setText(userGuahaoEntry.getUSERNAME());
			holder.tv_time.setText(userGuahaoEntry.getTIME());
			String doctorname = userGuahaoEntry.getDOCTORNAME();
			String departmen = userGuahaoEntry.getDEPARTMEN();
			String hosp = userGuahaoEntry.getHOSP();
			if (!TextUtils.isEmpty(departmen)) {
				doctorname += ("--" + departmen);
			}
			if (!TextUtils.isEmpty(hosp)) {
				doctorname += ("--" + hosp);
			}
			holder.tv_doctor.setText(doctorname);
			holder.tv_guahao_status.setText(userGuahaoEntry.getSTATE());
			if ("-1".equals(userGuahaoEntry.getSTATUS())) {
				holder.tv_guahao_status.setBackgroundResource(R.drawable.status_invalid);
			}
			return convertView;
		}

	}

	class HolderView {

		public TextView tv_guahao_status;
		public TextView tv_doctor;
		public TextView tv_time;
		public TextView tv_name;

	}
}
