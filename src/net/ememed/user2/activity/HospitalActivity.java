package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.db.GuahaoAreaTable;
import net.ememed.user2.entity.AreaGuahaoEntry;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.HospitalEntry;
import net.ememed.user2.entity.HospitalsInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.RefreshListView;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class HospitalActivity extends BasicActivity implements OnItemClickListener {

	private RefreshListView lvCustomEvas;
	private String cityId;
	private String cityName;
	private HospAdapter adapter;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;
	private List<HospitalEntry> hospitals = new ArrayList<HospitalEntry>();
	private PopupWindow popup;

	private GuahaoAreaTable table = new GuahaoAreaTable();
	private boolean isflag = false;
	private List<AreaGuahaoEntry> areaEntry;
	private LinearLayout ll_select;
	private AreaAdapter mAdapter;
	private TextView tv_Area_name;
	private ImageView ib_down_arrow;
	private FrameLayout fl_top_title;
	private View bg_gray;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_hospital);
		cityId = getIntent().getStringExtra("cityId");
		cityName = getIntent().getStringExtra("cityName");
		EventBus.getDefault().register(this, GuahaoSuccessEvent.class);
	}

	public void onEvent(GuahaoSuccessEvent event) {
		finish();
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().removeStickyEvent(GuahaoSuccessEvent.class);
		super.onDestroy();
	}

	@Override
	protected void setupView() {
		Logger.dout("进入Hospitalactivity");
		
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ll_select = (LinearLayout) findViewById(R.id.ll_select);
		ib_down_arrow = (ImageView) findViewById(R.id.ib_down_arrow);
		fl_top_title = (FrameLayout) findViewById(R.id.fl_top_title);
		TextView title = (TextView) findViewById(R.id.top_title);
		title.setText("选择医院");
		TextView tv_notice = (TextView) findViewById(R.id.tv_notice);
		tv_notice.setText("暂无数据，点击刷新");
		tv_Area_name = (TextView) findViewById(R.id.tv_Area_name);
		bg_gray = findViewById(R.id.bg_gray);

		// TextView tv_cityName = (TextView) findViewById(R.id.tv_cityName);
		// if (!TextUtils.isEmpty(cityName))
		// tv_cityName.setText(cityName);

		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_contact_class);
		adapter = new HospAdapter(null);
		lvCustomEvas.setAdapter(adapter);
		getHospitalList(cityId);
		areaEntry = table.getAreaEntry(cityId);
		if (areaEntry != null && areaEntry.size() > 0) {
			AreaGuahaoEntry entry = new AreaGuahaoEntry();
			entry.setAREA_NAME("全部地区");
			entry.setID(cityId);
			areaEntry.add(0, entry);

			initCitys();
		}

	}

	@SuppressLint("NewApi")
	private void initCitys() {
		ListView lv = new ListView(this);
		lv.setBackgroundColor(Color.WHITE);
		
		mAdapter = new AreaAdapter();
		
		lv.setVerticalScrollBarEnabled(true);
		lv.setScrollBarFadeDuration(0);
		lv.setDivider(new ColorDrawable(Color.GRAY));
		lv.setDividerHeight(1);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(this);
		
//		int Height = Util.dip2px(this, 40)*mAdapter.getCount()+mAdapter.getCount();
//		if(Height>(MyApplication.getInstance().canvasHeight-Util.dip2px(this, 48))){
//			Height = MyApplication.getInstance().canvasHeight-Util.dip2px(this, 48);
//		}
		popup = new PopupWindow(lv, /*MyApplication.getInstance().canvasWidth,Util.dip2px(this, 150)*/MyApplication.getInstance().canvasWidth, MyApplication.getInstance().canvasHeight);
		popup.setOutsideTouchable(true);
		popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popup.setFocusable(true);
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ib_down_arrow.setSelected(false);
				bg_gray.setVisibility(View.GONE);
			}
		});
	}

	@Override
	protected void onResume() {
		if (isflag) {
			isflag = false;
			getHospitalList(cityId);
		}
		super.onResume();
	}

	@Override
	protected void addListener() {
		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (hospitals != null) {
					// HospitalEntry hospitalEntry = hospitals.get(position);
					HospitalEntry hospitalEntry = (HospitalEntry) adapter.getItem(position - 1);
					Intent intent = new Intent(HospitalActivity.this, KeshiActivity.class);
					intent.putExtra("hosp_entry", hospitalEntry);
					startActivity(intent);
				}

			}
		});
		ll_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHospitalList(cityId);
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

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.ll_select:
			if(popup!=null){
				bg_gray.setVisibility(View.VISIBLE);
				popup.showAsDropDown(fl_top_title, 0, 0);
				ib_down_arrow.setSelected(true);
			}
			break;

		default:
			break;
		}
	}

	private void getHospitalList(String areaid) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("areaid", areaid);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.guahao_get_hospital_list, HospitalsInfo.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_AREA_LIST;
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
		case IResult.GET_AREA_LIST:
			HospitalsInfo info = (HospitalsInfo) msg.obj;
			if (info != null) {
				switch (info.getSuccess()) {
				case 1:
					hospitals = info.getData().getHOSPITALS();
					if (hospitals != null && hospitals.size() > 0) {
						Logger.dout("医院数量"+hospitals.size());
						adapter.change(hospitals);
					} else {
						ll_empty.setVisibility(View.VISIBLE);
						lvCustomEvas.setVisibility(View.GONE);
					}
					break;

				default:
					break;
				}
			} else {
				showToast(IMessage.HOSPITAL_DATA_ERROR);
			}
			break;
		case IResult.NET_ERROR:
			// showToast(IMessage.NET_ERROR);
			ll_net_unavailable.setVisibility(View.VISIBLE);
			ll_empty.setVisibility(View.GONE);
			lvCustomEvas.setVisibility(View.GONE);
			break;
		case IResult.DATA_ERROR:
			// showToast(IMessage.DATA_ERROR);
			ll_empty.setVisibility(View.VISIBLE);
			lvCustomEvas.setVisibility(View.GONE);
			break;

		default:
			break;
		}
	}

	class HospAdapter extends BaseAdapter {
		private List<HospitalEntry> listItems;

		HospAdapter(List<HospitalEntry> listItems) {
			if (listItems == null) {
				listItems = new ArrayList<HospitalEntry>();
			}
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			Holder holder = null;
			if (view == null) {
				holder = new Holder();
				view = View.inflate(HospitalActivity.this, R.layout.hospital_item, null);
				holder.iv_exterior = (ImageView) view.findViewById(R.id.iv_exterior);
				holder.tv_hospital_name = (TextView) view.findViewById(R.id.tv_hospital_name);
				holder.tv_hospital_level = (TextView) view.findViewById(R.id.tv_hospital_level);
				holder.tv_hospital_address = (TextView) view.findViewById(R.id.tv_hospital_address);
				holder.ll_go_detail = (LinearLayout) view.findViewById(R.id.ll_go_detail);
				//holder.tv_hospital_schedule_yes = (TextView) view.findViewById(R.id.tv_hospital_schedule_yes);
				//holder.tv_hospital_schedule_no = (TextView) view.findViewById(R.id.tv_hospital_schedule_no);
				view.setTag(holder);
			} else {
				holder = (Holder) view.getTag();
			}
			final HospitalEntry hospitalEntry = listItems.get(position);
			HospitalActivity.this.imageLoader.displayImage(hospitalEntry.getHOSPITAL_INFO().getIMGURL(),holder.iv_exterior,Util.getHosp_default_portrait());
			holder.tv_hospital_name.setText(hospitalEntry.getHOSPITALNAME());
			holder.tv_hospital_level.setText(hospitalEntry.getGRADE());
			holder.tv_hospital_address.setText(hospitalEntry.getHOSPITAL_INFO().getADDRESS());
			int scheduleState = Integer.parseInt(hospitalEntry.getHAS_SCHEDULE());
			//根据该医院有无号，显示有无号的textview
			/*if(1 == scheduleState){
				holder.tv_hospital_schedule_yes.setVisibility(View.GONE);
				holder.tv_hospital_schedule_no.setVisibility(View.GONE);
			}else{
				holder.tv_hospital_schedule_yes.setVisibility(View.GONE);
				holder.tv_hospital_schedule_no.setVisibility(View.GONE);
			}*/
			// 暂缺缺医院icon字段，默认加载图片为 drawable/hosp_def
			
			holder.ll_go_detail.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(HospitalActivity.this, HospitalDetailsActivity.class);
					intent.putExtra("hosp_entry", hospitalEntry);
					startActivity(intent);

				}
			});
			return view;
		}

		public void change(List<HospitalEntry> lists) {
			if (lists == null) {
				lists = new ArrayList<HospitalEntry>();
			}
			this.listItems = lists;
			notifyDataSetChanged();
		}

		public void add(List<HospitalEntry> list) {
			this.listItems.addAll(list);
			notifyDataSetChanged();
		}

	}

	class Holder {

		public LinearLayout ll_go_detail;
		public TextView tv_hospital_address;// 医院地址
		public TextView tv_hospital_level;// 医院等级
		public TextView tv_hospital_name;// 医院名称
		public ImageView iv_exterior;// 医院图片
		//public TextView tv_hospital_schedule_yes; //有号
		//public TextView tv_hospital_schedule_no; //无号
	}

	class AreaAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return areaEntry.size();
		}

		@Override
		public Object getItem(int position) {
			return areaEntry.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView tv = null;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_city, null);
				tv = (TextView) convertView.findViewById(R.id.tv_address);
				convertView.setTag(tv);
			} else {
				tv = (TextView) convertView.getTag();
			}

			tv.setText(areaEntry.get(position).getAREA_NAME());
			return convertView;
		}

	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		popup.dismiss();
		if (areaEntry != null && areaEntry.size() > 0) {
			AreaGuahaoEntry areaGuahaoEntry = areaEntry.get(position);
			tv_Area_name.setText(areaGuahaoEntry.getAREA_NAME());
			if (position == 0) {
				adapter.change(hospitals);
			} else {
				List<HospitalEntry> lists = new ArrayList<HospitalEntry>();
				for (HospitalEntry hospitalEntry : hospitals) {
					String area_CODE = hospitalEntry.getHOSPITAL_INFO().getAREA_CODE();
					if (areaGuahaoEntry.getID().equals(area_CODE)) {
						lists.add(hospitalEntry);
					}
				}
				adapter.change(lists);
			}
		}
	}

}
