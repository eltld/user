package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.db.CityConfigTable;
import net.ememed.user2.db.ConfigTable;
import net.ememed.user2.db.DoctorInfoTable;
import net.ememed.user2.db.HospConfigTable;
import net.ememed.user2.db.ServiceConfigTable;
import net.ememed.user2.db.SortConfigTable;
import net.ememed.user2.entity.AreaEntry;
import net.ememed.user2.entity.AreaInfo;
import net.ememed.user2.entity.CityEntry;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.DoctorEntry2;
import net.ememed.user2.entity.DoctorInfo2;
import net.ememed.user2.entity.HospEntry;
import net.ememed.user2.entity.HospInfo;
import net.ememed.user2.entity.HospitalInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.OfferSerViceEntity;
import net.ememed.user2.entity.PacketPeriod;
import net.ememed.user2.entity.ServiceEntry;
import net.ememed.user2.entity.SortEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

/**
 * 搜索医生
 * 
 * @author ASIMO
 * 
 */
public class ExpertSearchActivity extends BasicActivity implements OnRefreshListener {

	private RefreshListView lvCustomEvas;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;
	private DoctorAdapter new_adapter;
	private TextView top_title;
	private LinearLayout ll_droplist;

	private boolean refresh = true;
	private int page = 1;
	private boolean init = false;
	private int totalpages = 0;
	private int width;
	private int height;
	private int mode_position;
	private int area_position = -1;
	private int last_area_position = -1;
	private int last_city_position = -1;
	private int room_position = -1;
	private int last_room_position = -1;
	private int hosp_position = -1;
	private int navroom_position;
	private int last_navroom_position = -1;
	private int sort_position = -1;
	private int province_index = 0;
	private int province_top = 0;
	private int city_index = 0;
	private int city_top = 0;
	private int room_index = 0;
	private int room_top = 0;
	private int navroom_index = 0;
	private int navroom_top = 0;
	private LinearLayout ll_notice_custom_top;

	private PopupWindow mModePopupWindow;
	private PopupWindow mHospPopupWindow;
	private View mModeView;
	private ListView mModeListView;
	private CityConfigTable cityConfigTable = null;
	private HospConfigTable hospConfigTable = null;
	private List<AreaEntry> areaList = null;
	private CityAdapter adapter_sub = null;
	private NavRoomAdapter adapter_room_sub = null;
	private ArrayList<HospEntry> hospList = null;
	private String current_sort = "1";
	private String current_area = TextUtils.isEmpty(SharePrefUtil.getString(Conast.Areaid))?"":SharePrefUtil.getString(Conast.Areaid);
//	private String current_area = "";
	private String current_room = "";
	private String current_service = "";
	private String current_name = "";
	private String current_quanke = "";
	private String current_hospitalid = "";
	private String keyword = "";
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		current_service = getIntent().getStringExtra("current_service");
		current_name = getIntent().getStringExtra("current_name");
		current_quanke = getIntent().getStringExtra("current_quanke");
		setContentView(R.layout.activity_search_expert);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 宽度（PX）
		height = metric.heightPixels; // 高度
		cityConfigTable = new CityConfigTable();
		UICore.eventTask(this, this, IResult.GET_CITY_DATA, null, null);
		UICore.eventTask(this, this, IResult.GET_HOSP_DATA, null, null);
		UICore.eventTask(this, this, IResult.SYSN_CONFIG, null, null);
	}

	ImageView bg;
	// 服务种类
	private TextView tv_service_droplist;
	// 科室
	private TextView tv_keshi_droplist;
	// 排序
	private TextView tv_price_droplist;
	// 城市
	private TextView tv_address_droplist;
	// 医院
	private TextView tv_hosp_droplist;
	private DoctorInfoTable table;
	private View mHospView;
	private ListView mHospListView;
	private EditText et_disease_keyword;
	private ConfigTable configTable;
	private EditText et_keyword;
	private View in_seach_bar;
	private EditText et_disease_keyword2;
	
	
	String cityname;

	@Override
	protected void setupView() {
		super.setupView();
		configTable = new ConfigTable(this);

		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(current_name);
		tv_service_droplist = (TextView) findViewById(R.id.tv_service_droplist);
		tv_address_droplist = (TextView) findViewById(R.id.tv_address_droplist);
		tv_keshi_droplist = (TextView) findViewById(R.id.tv_keshi_droplist);
		tv_price_droplist = (TextView) findViewById(R.id.tv_price_droplist);
		tv_hosp_droplist =  (TextView) findViewById(R.id.tv_hosp_droplist);
		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_contact_class);
		ll_droplist = (LinearLayout) findViewById(R.id.ll_droplist);
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ImageView iv_bg_empty = (ImageView) findViewById(R.id.iv_bg_empty);
		TextView tv_notice = (TextView) findViewById(R.id.tv_notice);
		String Areaname = SharePrefUtil.getString(Conast.Areaname);
		if(!TextUtils.isEmpty(Areaname)){
			tv_address_droplist.setText(Areaname);
		}
		//tv_address_droplist.setText(MyApplication.current_city);
		
		
		
		
		
		
		
		in_seach_bar = findViewById(R.id.ll_seach_bar);
		et_keyword = (EditText) findViewById(R.id.et_disease_keyword);
		
		View item_seach_heads = View.inflate(this, R.layout.item_seach_heads, null);
		lvCustomEvas.addHeaderView(item_seach_heads);
		et_disease_keyword = (EditText) item_seach_heads.findViewById(R.id.et_disease_keyword);
		et_disease_keyword2 = (EditText) findViewById(R.id.et_disease_keyword2);

		iv_bg_empty.setImageResource(R.drawable.ic_emply_doctor);
		android.view.ViewGroup.LayoutParams params = iv_bg_empty.getLayoutParams();
		params.width = Util.dip2px(this, 90);
		params.height = Util.dip2px(this, 100);
		iv_bg_empty.setLayoutParams(params);

		tv_notice.setText("暂时没有找到你需要的医生");
//		ll_notice_custom_top = (LinearLayout) findViewById(R.id.ll_notice_custom_top);
		if (!TextUtils.isEmpty(current_quanke)) {
			// 免费提问，去掉价钱排序，科室排序
			tv_price_droplist.setVisibility(View.GONE);
			// tv_keshi_droplist.setVisibility(View.GONE);
		} else {
			try {
				int current = Integer.parseInt(current_service);
				switch (current) {
				case 1:
				case 2:
					tv_price_droplist.setVisibility(View.VISIBLE);
					break;
				case 3:
				case 4:
				case 14:
				case 15:
				case 16:
					tv_price_droplist.setVisibility(View.GONE);
					break;
				}
			} catch (NumberFormatException e) {
			}
		}

		table = new DoctorInfoTable();
		hospConfigTable = new HospConfigTable();

		//ArrayList<DoctorEntry> infos = (ArrayList<DoctorEntry>) table.findDoctorInfos(current_service);
		ArrayList<DoctorEntry2> infos = new ArrayList<DoctorEntry2>();
		new_adapter = new DoctorAdapter(infos);
		lvCustomEvas.setAdapter(new_adapter);

		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				DoctorEntry2 entry = (DoctorEntry2) new_adapter.getItem(position - lvCustomEvas.getHeaderViewsCount());
				DoctorEntry entry2 = getDoctorEntry(entry);
				if (null != entry) {
					Intent intent = new Intent(ExpertSearchActivity.this, DoctorClininActivity.class);
					if (TextUtils.isEmpty(current_service)) {
						current_service = "1";
					}
					intent.putExtra("current_service", current_service);
					intent.putExtra("title", entry.getREALNAME());
					intent.putExtra("entry", entry2);
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
		
		
		mModeView = getLayoutInflater().inflate(R.layout.mode_popupwindow, null);
		mModeListView = (ListView) mModeView.findViewById(R.id.mode_pop_list);
		
		mHospView = getLayoutInflater().inflate(R.layout.mode_popupwindow, null);
		mHospListView = (ListView) mHospView.findViewById(R.id.mode_pop_list);
		
		bg = (ImageView) findViewById(R.id.overlay);
		
		
		
		cityname = SharePrefUtil.getString(Conast.CITY_NAME);
		if(!TextUtils.isEmpty(cityname)){
			tv_address_droplist.setText(cityname);
			current_area = SharePrefUtil.getString(Conast.CITY_ID);
			
			System.out.println("城市id : "+current_area);
		}
		
		if (!TextUtils.isEmpty(current_quanke)) {
			getDoctorList(page, current_service, current_area, current_room, current_sort, current_quanke,current_hospitalid,keyword);
		} else {
			getDoctorList(page, current_service, current_area, current_room, current_sort,current_hospitalid,keyword);
		}

		//XXX这里是否是读取的多余信息？
		new Thread() {
			public void run() {
				ServiceConfigTable table = new ServiceConfigTable();
				String service_name = table.getServiceName(current_service);
				tv_service_droplist.setText(current_name);
				tv_service_droplist.setText(service_name);
			};
		}.start();
	}

	/** 左边显示PopupWindow （业务列表） */
	private void initModePopupWindow() {
		ServiceConfigTable table = new ServiceConfigTable();
		ArrayList<ServiceEntry> list = table.getAllPositionNames();
		final ServiceAdapter adapter = new ServiceAdapter(this, list);
		mModeListView.setAdapter(adapter);
		mModeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				mModePopupWindow.dismiss();
				mode_position = position;
				ServiceEntry entry = (ServiceEntry) adapter.getItem(position);
				current_service = entry.getService_id();
				tv_service_droplist.setText(entry.getService_name());
				refresh();
			}
		});
		mModePopupWindow = new PopupWindow(mModeView, width / 3, LayoutParams.WRAP_CONTENT, true);
		mModePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mModePopupWindow.setOutsideTouchable(true);
		mModePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				bg.setVisibility(View.GONE);
			}
		});
		if (mModePopupWindow.isShowing()) {
			mModePopupWindow.dismiss();
		} else {
			mModePopupWindow.showAsDropDown(ll_droplist, 0, 0);
			bg.setVisibility(View.VISIBLE);
		}
	}

	/** 城市显示PopupWindow */
	private void initAreaPopupWindow() {
		try {
			area_position = last_area_position;
			View mAreaView = LayoutInflater.from(this).inflate(R.layout.u_select_area, null);

			final RefreshListView ll_conten_nav = (RefreshListView) mAreaView.findViewById(R.id.ll_conten_nav);
			ll_conten_nav.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
			// ll_conten_nav.setHeaderDividersEnabled(false);
			final RefreshListView lv_Area_sub = (RefreshListView) mAreaView.findViewById(R.id.lv_area);
			lv_Area_sub.setHeaderDividersEnabled(false);
			if (area_position == -1)
				lv_Area_sub.setBackgroundResource(R.color.list_gray);

			areaList = cityConfigTable.getProvinces();

			final ProvinceAdapter nav_adapter = new ProvinceAdapter(this, areaList);
			ll_conten_nav.setAdapter(nav_adapter);
			ll_conten_nav.setSelectionFromTop(province_index, province_top);
			List<CityEntry> city_list = null;
			if (area_position != -1 && null != areaList && areaList.size() > 0) {
				AreaEntry ememed_cityId = (AreaEntry) nav_adapter.getItem(area_position - 1);
				if (!TextUtils.isEmpty(ememed_cityId.getID())) {
					city_list = cityConfigTable.getAllCityNames(ememed_cityId.getID());
				}
			}
			if (area_position == -1 && null != areaList && areaList.size() > 0) {
				city_list = cityConfigTable.getAllCityNames(areaList.get(0).getID());
			}
			adapter_sub = new CityAdapter(this, city_list);
			lv_Area_sub.setAdapter(adapter_sub);
			lv_Area_sub.setVisibility(View.VISIBLE);
			lv_Area_sub.setSelectionFromTop(city_index, city_top);
			ll_conten_nav.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
					try {
						area_position = position;
						System.out.println(areaList.get(position - 1).getID());
						nav_adapter.setSelectItem(position - 1);
						nav_adapter.notifyDataSetInvalidated();
						AreaEntry ememed_cityId = (AreaEntry) nav_adapter.getItem(position - 1);
						if (!TextUtils.isEmpty(ememed_cityId.getID())) {
							List<CityEntry> city_list = cityConfigTable.getAllCityNames(ememed_cityId.getID());
							sendMessage(IResult.GET_CITY_DATA, city_list);
						}
						lv_Area_sub.setVisibility(View.VISIBLE);
						// }
						lv_Area_sub.setBackgroundResource(R.color.white);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			lv_Area_sub.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

					mModePopupWindow.dismiss();
					CityEntry entry = (CityEntry) adapter_sub.getItem(position - 1);
					current_area = entry.getID();
					tv_address_droplist.setText(entry.getAREANAME());
					last_area_position = area_position;
					last_city_position = position;
					city_index = lv_Area_sub.getFirstVisiblePosition();
					View v2 = lv_Area_sub.getChildAt(0);
					city_top = (v2 == null) ? 0 : v2.getTop();
					province_index = ll_conten_nav.getFirstVisiblePosition();
					View v1 = ll_conten_nav.getChildAt(0);
					province_top = (v1 == null) ? 0 : v1.getTop();
					
					tv_hosp_droplist.setText("所有医院");
					tv_keshi_droplist.setText("所有科室");
					current_hospitalid = "";
					current_room = "";
					hosp_position=-1;
					room_position=-1;
					last_room_position=-1;
					
					gethospListFromServer();
					refresh();
				}
			});
			mModePopupWindow = new PopupWindow(mAreaView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
			mModePopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mModePopupWindow.setOutsideTouchable(true);
			mModePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

				@Override
				public void onDismiss() {
					bg.setVisibility(View.GONE);
					tv_address_droplist.setSelected(false);
				}
			});
			if (mModePopupWindow.isShowing()) {
				mModePopupWindow.dismiss();
			} else {
				bg.setVisibility(View.VISIBLE);
				mModePopupWindow.showAsDropDown(ll_droplist, 0, 0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 科室显示PopupWindow */
	private void initRoomPopupWindow() {
		View mAreaView = LayoutInflater.from(this).inflate(R.layout.u_select_area, null);
		room_position = last_room_position;
		final RefreshListView ll_conten_nav = (RefreshListView) mAreaView.findViewById(R.id.ll_conten_nav);
		ll_conten_nav.setChoiceMode(ListView.CHOICE_MODE_SINGLE);// 一定要设置这个属性，否则ListView不会刷新
		// ll_conten_nav.setHeaderDividersEnabled(false);
		final RefreshListView lv_Area_sub = (RefreshListView) mAreaView.findViewById(R.id.lv_area);
		lv_Area_sub.setHeaderDividersEnabled(false);
		if (room_position == -1)
			lv_Area_sub.setBackgroundResource(R.color.list_gray);
		final ConfigTable configTable = new ConfigTable();
		ArrayList<String> departmentGroups = configTable.getDepartmentGroups();
		final RoomAdapter adapter = new RoomAdapter(this, departmentGroups);

		ArrayList<String> departmentsSubList = null;

		if (room_position != -1 && null != departmentGroups && departmentGroups.size() > 0) {
			String ememed_cityId = (String) adapter.getItem(room_position - 1);
			if (!TextUtils.isEmpty(ememed_cityId)) {
				departmentsSubList = configTable.getDepartmentNames(ememed_cityId);
			}
		}
		if (room_position == -1 && null != departmentGroups && departmentGroups.size() > 0) {
			departmentsSubList = configTable.getDepartmentNames(departmentGroups.get(0));
		}

		adapter_room_sub = new NavRoomAdapter(this, departmentsSubList);
		ll_conten_nav.setAdapter(adapter);
		ll_conten_nav.setSelectionFromTop(room_index, room_top);
		lv_Area_sub.setAdapter(adapter_room_sub);
		lv_Area_sub.setVisibility(View.VISIBLE);
		lv_Area_sub.setSelectionFromTop(navroom_index, navroom_top);
		ll_conten_nav.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				try {
					room_position = position;
					adapter.setSelectItem(position - 1);
					adapter.notifyDataSetInvalidated();
					String ememed_cityId = (String) adapter.getItem(position - 1);
					if (!TextUtils.isEmpty(ememed_cityId)) {
						ArrayList<String> departmentsSubList = configTable.getDepartmentNames(ememed_cityId);
						sendMessage(IResult.GET_DEPARTMENT_DATA, departmentsSubList);
					}
					lv_Area_sub.setBackgroundResource(R.color.white);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		lv_Area_sub.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				navroom_position = position;
				last_room_position = room_position;
				last_navroom_position = position;
				mModePopupWindow.dismiss();
				current_room = (String) adapter_room_sub.getItem(position - 1);
				tv_keshi_droplist.setText(current_room);
				navroom_index = lv_Area_sub.getFirstVisiblePosition();
				View v2 = lv_Area_sub.getChildAt(0);
				navroom_top = (v2 == null) ? 0 : v2.getTop();
				room_index = ll_conten_nav.getFirstVisiblePosition();
				View v1 = ll_conten_nav.getChildAt(0);
				room_top = (v1 == null) ? 0 : v1.getTop();
				if (current_room.equals("所有科室")) {
					current_room = "";
				}
				refresh();
			}
		});
		mModePopupWindow = new PopupWindow(mAreaView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		mModePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mModePopupWindow.setOutsideTouchable(true);
		mModePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				bg.setVisibility(View.GONE);
				tv_keshi_droplist.setSelected(false);
			}
		});
		if (mModePopupWindow.isShowing()) {
			mModePopupWindow.dismiss();
		} else {
			mModePopupWindow.showAsDropDown(ll_droplist, 10, 0);
			bg.setVisibility(View.VISIBLE);
		}
	}

	/** 右边显示PopupWindow(排序) */
	private void initRightModePopupWindow() {
		SortConfigTable sortTable = new SortConfigTable();
		ArrayList<SortEntry> mSortName = sortTable.getAllSortNames();
		final SortAdapter adapter = new SortAdapter(this, mSortName);
		mModeListView.setAdapter(adapter);
		mModeListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				sort_position = position;
				mModePopupWindow.dismiss();
				SortEntry entry = (SortEntry) adapter.getItem(position);
				current_sort = entry.getSort_id();
				tv_price_droplist.setText(entry.getSort_name());
				refresh();
			}
		});
		mModePopupWindow = new PopupWindow(mModeView, width/2, LayoutParams.WRAP_CONTENT, true);
		mModePopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mModePopupWindow.setOutsideTouchable(true);
		mModePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				bg.setVisibility(View.GONE);
				tv_price_droplist.setSelected(false);
			}
		});
		if (mModePopupWindow.isShowing()) {
			mModePopupWindow.dismiss();
		} else {
			mModePopupWindow.showAsDropDown(ll_droplist, width, 0); 
			bg.setVisibility(View.VISIBLE);
		}
	}

	protected void refresh() {
		refresh = true;
		page = 1;
		if (!TextUtils.isEmpty(current_quanke)) {
			getDoctorList(page, current_service, current_area, current_room, current_sort, current_quanke,current_hospitalid,keyword);
		} else {
			getDoctorList(page, current_service, current_area, current_room, current_sort,current_hospitalid,keyword);
		}
	}

	protected void loadMore() {
		refresh = false;
		page++;
		if (!TextUtils.isEmpty(current_quanke)) {
			getDoctorList(page, current_service, current_area, current_room, current_sort, current_quanke,current_hospitalid,keyword);
		} else {
			getDoctorList(page, current_service, current_area, current_room, current_sort,current_hospitalid,keyword);
		}
	}

	/** 通过筛选条件搜索医生 */
	private void getDoctorList(int page, String service_type, String areaid, String room, String orderby,String hospitalid,String keyword) {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			/*
			 * service_type
			 * 服务类型：'1'=>"图文咨询",'2'=>"预约通话",'3'=>"名医加号",'4'=>"上门会诊",
			 * '14'=>"住院直通车",'11'=>"体检报告解读",'15'=>"签约私人医生服务",'16'=>"自定义订单" areaid
			 * 地区ID room 科室名 orderby 排序类型 page 分页数
			 */
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("service_type", service_type);
			params.put("areaid", areaid);
			params.put("room", room);
			params.put("orderby", orderby);
			params.put("page", page + "");
			params.put("hospitalid", hospitalid);
			
			System.out.println("城市搜索医生："+params);
			if(!TextUtils.isEmpty(keyword)){
				params.put("keyword", keyword);
			}
			
			params.put("search_app_version", PublicUtil.getVersionName(ExpertSearchActivity.this));
			
			if(SharePrefUtil.getBoolean(Conast.LOGIN)){
				params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			}
			
			System.out.println("搜索参数params："+params);
			
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.search_filter_doctor, DoctorInfo2.class, params, new Response.Listener() {
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.SEARCH_DOCTOR;
					handler.sendMessage(message);
				}
			}, new Response.ErrorListener() {
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

	/**
	 * 免费提问入口 增加使用使用关键字quanke service_type不使用 ，重载需要股不删除该参数
	 */
	private void getDoctorList(int page, String service_type, String areaid, String room, String orderby, String quanke, String hospitalid,String keyword) {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			/*
			 * service_type
			 * 服务类型：'1'=>"图文咨询",'2'=>"预约通话",'3'=>"名医加号",'4'=>"上门会诊",
			 * '14'=>"住院直通车",'11'=>"体检报告解读",'15'=>"签约私人医生服务",'16'=>"自定义订单" areaid
			 * 地区ID room 科室名 orderby 排序类型 page 分页数
			 */
			HashMap<String, String> params = new HashMap<String, String>();
			// params.put("service_type", service_type);
			params.put("areaid", areaid);
			params.put("room", room);
			params.put("orderby", orderby);
			params.put("page", page + "");
			params.put("quanke", quanke);
			params.put("hospitalid", hospitalid);
			
			System.out.println("城市搜索医生："+params);
			
			if(!TextUtils.isEmpty(keyword)){
				params.put("keyword", keyword);
			}
			params.put("search_app_version", PublicUtil.getVersionName(ExpertSearchActivity.this));
			
			if(SharePrefUtil.getBoolean(Conast.LOGIN)){
				params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			}
			
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.search_filter_doctor, DoctorInfo2.class, params, new Response.Listener() {
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

	// 获取城市列表
	AreaInfo cityInfoTemp = new AreaInfo();
	private void getCityListFromServer() {
		try {
			MyApplication.volleyHttpClient.post(HttpUtil.get_all_area, AreaInfo.class, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					cityInfoTemp = (AreaInfo) response;
					if (null != cityInfoTemp && cityInfoTemp.getSuccess() == 1) {
						if (null != cityInfoTemp.getData() && cityInfoTemp.getData().size() > 0) {
							areaList =cityInfoTemp.getData();
							new Thread() {
								public void run() {
									cityConfigTable.clearTable();
									for (int i = 0; i < cityInfoTemp.getData().size(); i++) {
										cityConfigTable.saveSortName(cityInfoTemp.getData().get(i));
										//areaList = cityConfigTable.getProvinces();
									}
								};
							}.start();
						} else{
							new Thread() {
								public void run() {
									areaList = cityConfigTable.getProvinces();
								};
							}.start();
						}
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Logger.dout("area error:" + error.getMessage());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		if (mes == IResult.GET_CITY_DATA) {
			if (null == cityConfigTable || null == cityConfigTable.getProvinces() || cityConfigTable.getProvinces().size() <= 1) {
				getCityListFromServer();
			}
		}else if(mes == IResult.GET_HOSP_DATA){
			if (null == hospConfigTable || null == hospConfigTable.getProvinces(current_area) || hospConfigTable.getProvinces(current_area).size() == 0) {
				gethospListFromServer();
			}
		}else if(mes == IResult.SYSN_CONFIG){
			if(null==configTable || null == configTable.getDepartmentGroups() ||  configTable.getDepartmentGroups().size() == 0){
				sysn_config();
			}
		}
	}
	/**
	 * 从服务器获取医院的列表
	 */
	HospInfo hospitalInfoTemp = new HospInfo();
	private void gethospListFromServer() {
		// TODO Auto-generated method stub

		try {
			loading(null);
			HashMap<String, String > params = new HashMap<String, String>();
			params.put("areaid", current_area);
			
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_hospital_by_area,HospInfo.class, params, new Response.Listener() {
				

				@Override
				public void onResponse(Object response) {
					hospitalInfoTemp = (HospInfo) response;
					if (null != hospitalInfoTemp && hospitalInfoTemp.getSuccess() == 1) {
						
						if (null != hospitalInfoTemp.getData() && hospitalInfoTemp.getData().size() > 0) {
							/*cityConfigTable.clearTable();
							for (int i = 0; i < info.getData().size(); i++) {
								cityConfigTable.saveSortName(info.getData().get(i));
							}*/
							
							new Thread() {
								public void run() {
									hospConfigTable.clearTable(current_area);
									hospConfigTable.saveSortName(hospitalInfoTemp.getData(), current_area);
									hospList = hospConfigTable.getProvinces(current_area);
								};
							}.start();
							
						}else{
							new Thread() {
								public void run() {
									hospList = hospConfigTable.getProvinces(current_area);
								};
							}.start();
						}
					}
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Logger.dout("area error:" + error.getMessage());
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
	}

	public void doClick(View view) {
		try {
			if (view.getId() == R.id.btn_back) {
				finish();
			} else if (view.getId() == R.id.tv_service_droplist) {
				initModePopupWindow();
			} else if (view.getId() == R.id.tv_address_droplist) {
				tv_address_droplist.setSelected(true);
				tv_keshi_droplist.setSelected(false);
				tv_price_droplist.setSelected(false);
				tv_hosp_droplist.setSelected(false);
				initAreaPopupWindow();
			} else if (view.getId() == R.id.tv_keshi_droplist) {
				tv_address_droplist.setSelected(false);
				tv_keshi_droplist.setSelected(true);
				tv_price_droplist.setSelected(false);
				tv_hosp_droplist.setSelected(false);
				initRoomPopupWindow();
			} else if (view.getId() == R.id.tv_price_droplist) {
				tv_address_droplist.setSelected(false);
				tv_keshi_droplist.setSelected(false);
				tv_price_droplist.setSelected(true);
				tv_hosp_droplist.setSelected(false);
				initRightModePopupWindow();
			} else if (view.getId() == R.id.tv_hosp_droplist) {
				tv_address_droplist.setSelected(false);
				tv_keshi_droplist.setSelected(false);
				tv_price_droplist.setSelected(false);
				tv_hosp_droplist.setSelected(true);
				initHospPopupWindow();

			} else if (view.getId() == R.id.overlay) {
				bg.setVisibility(View.GONE);
			}else if (view.getId() == R.id.bt_keyword_search) {
				if(TextUtils.isEmpty(et_disease_keyword.getText().toString().trim())){
					showToast("请输入关键字");
					return;
				}
				keyword = et_disease_keyword.getText().toString().trim();
				et_disease_keyword2.setText(keyword);
				refresh();
			}else if (view.getId() == R.id.bt_keyword_search2) {
				if(TextUtils.isEmpty(et_disease_keyword2.getText().toString().trim())){
					showToast("请输入关键字");
					return;
				}
				
				keyword = et_disease_keyword2.getText().toString().trim();
				et_disease_keyword.setText(keyword);
				refresh();
			} else if(view.getId() == R.id.iv_net_failed){
				refresh();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 初始化医院的弹框
	 */
	private void initHospPopupWindow() {
		// TODO Auto-generated method stub
		ArrayList<HospEntry> provinces = hospConfigTable.getProvinces(current_area);
		final HospAdapter adapter = new HospAdapter(this, provinces);
		mHospListView.setAdapter(adapter);
		mHospListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
				
				hosp_position = position;
				mHospPopupWindow.dismiss();
				HospEntry entry = (HospEntry) adapter.getItem(position);
				current_hospitalid = entry.getHOSPITALID();
				tv_hosp_droplist.setText(entry.getHOSPITALNAME());
				if(position==0){
					current_hospitalid = "";
				}
				refresh();
			}
		});
		mHospPopupWindow = new PopupWindow(mHospView, width, LayoutParams.WRAP_CONTENT, true);
		mHospPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		mHospPopupWindow.setOutsideTouchable(true);
		mHospPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

			@Override
			public void onDismiss() {
				bg.setVisibility(View.GONE);
				tv_hosp_droplist.setSelected(false);
			}
		});
		if (mHospPopupWindow.isShowing()) {
			mHospPopupWindow.dismiss();
		} else {
			mHospPopupWindow.showAsDropDown(ll_droplist, width, 0);
			bg.setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		lvCustomEvas.onRefreshComplete();
		try {
			switch (msg.what) {
			case IResult.GET_CITY_DATA:
				List<CityEntry> city_list = (List<CityEntry>) msg.obj;
				adapter_sub.change(city_list);
				break;
			case IResult.GET_DEPARTMENT_DATA:
				ArrayList<String> departmentsSubList = (ArrayList<String>) msg.obj;
				adapter_room_sub.change(departmentsSubList);
				break;
			case IResult.SEARCH_DOCTOR:
				ll_net_unavailable.setVisibility(View.GONE);
				final DoctorInfo2 response = (DoctorInfo2) msg.obj;
				if (null != response) {
					if (response.getSuccess() == 1) {
						totalpages = response.getPages();
						if (refresh) {
							if (response.getData() == null || response.getData().size() == 0) {
								ll_empty.setVisibility(View.VISIBLE);
								in_seach_bar.setVisibility(View.VISIBLE);
								lvCustomEvas.setVisibility(View.GONE);
							} else {
								ll_empty.setVisibility(View.GONE);
								in_seach_bar.setVisibility(View.GONE);
								lvCustomEvas.setVisibility(View.VISIBLE);
							/*	new Thread() {
									public void run() {
										List<DoctorEntry> data = response.getData();
										table.saveDoctorInfo(new ArrayList<DoctorEntry>(data));
									};
								}.start();*/
								new_adapter.change(response.getData());
							}
						} else {
							/*new Thread() {
								public void run() {
									List<DoctorEntry> data = response.getData();
									table.saveDoctorInfo(new ArrayList<DoctorEntry>(data));
								};
							}.start();*/
							new_adapter.add(response.getData());
						}
						if (page < totalpages) {
							lvCustomEvas.onLoadMoreComplete(false);
						} else {
							lvCustomEvas.onLoadMoreComplete(true);
						}
					} else {
						in_seach_bar.setVisibility(View.VISIBLE);
						ll_empty.setVisibility(View.VISIBLE);
						lvCustomEvas.setVisibility(View.GONE);
					}
				}
				break;
			case IResult.NEWS_LIST:
				// ll_net_unavailable.setVisibility(View.GONE);
				// NewsEntity response = (NewsEntity) msg.obj;
				// if (null != response) {
				// if (response.getSuccess() == 1) {
				// init = true;
				// totalpages = response.getPages();
				// if (refresh) {
				// if (response.getData().getFocuslist() == null
				// || response.getData().getFocuslist().size() == 0) {
				// ll_empty.setVisibility(View.VISIBLE);
				// lvCustomEvas.setVisibility(View.GONE);
				// } else {
				// ll_empty.setVisibility(View.GONE);
				// lvCustomEvas.setVisibility(View.VISIBLE);
				// // setupFocusViews(response.getData().getFocuslist());
				// new_adapter.change(response.getData()
				// .getNewslist());
				// }
				// } else {
				// new_adapter.add(response.getData().getNewslist());
				// }
				// if (page < totalpages) {
				// lvCustomEvas.onLoadMoreComplete(false);
				// } else {
				// lvCustomEvas.onLoadMoreComplete(true);
				// }
				// } else {
				// ll_empty.setVisibility(View.VISIBLE);
				// lvCustomEvas.setVisibility(View.GONE);
				// }
				// }
				break;
			case IResult.RESULT:
				break;
			case IResult.END:

				break;
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				ll_net_unavailable.setVisibility(View.VISIBLE);
				ll_net_unavailable.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						setNetWork();
					}
				});
				ll_empty.setVisibility(View.GONE);
				lvCustomEvas.setVisibility(View.GONE);
				break;
			case IResult.DATA_ERROR:
				showToast(IMessage.DATA_ERROR);
				ll_empty.setVisibility(View.VISIBLE);
				lvCustomEvas.setVisibility(View.GONE);
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.search_filter_doctor);
	}

	// 医生的适配器
	private class DoctorAdapter extends BaseAdapter {
		private List<DoctorEntry2> listItems;

		public DoctorAdapter(ArrayList<DoctorEntry2> listItems) {
			if (listItems == null) {
				listItems = new ArrayList<DoctorEntry2>();
			}
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			return listItems.size();
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
				convertView = LayoutInflater.from(ExpertSearchActivity.this).inflate(R.layout.doctor_info_item, null);

				holder.tv_doctor_name = (TextView) convertView.findViewById(R.id.tv_doctor_name);
				holder.tv_search_shangchang = (TextView) convertView.findViewById(R.id.tv_search_shangchang);
				holder.tv_doctor_alias = (TextView) convertView.findViewById(R.id.tv_doctor_alias);
				holder.tv_hos_address = (TextView) convertView.findViewById(R.id.tv_hos_address);
				holder.doctor_head_portrait = (CircleImageView) convertView.findViewById(R.id.doctor_head_portrait);
				holder.tv_search_speciality = (TextView) convertView.findViewById(R.id.tv_search_speciality);
				holder.tv_price = (TextView) convertView.findViewById(R.id.tv_price);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			final DoctorEntry2 entry = listItems.get(position);
			if (null != entry) {

				imageLoader.displayImage(entry.getAVATAR(), holder.doctor_head_portrait, Util.getOptions_default_portrait());
				String name_before = entry.getREALNAME();
				if (name_before.length() > 4) {
					String name_after = name_before.substring(0, 4);
					holder.tv_doctor_name.setText(name_after + "…");
				} else
					holder.tv_doctor_name.setText(name_before);
				holder.tv_doctor_alias.setText(entry.getPROFESSIONAL());
				holder.tv_hos_address.setText(entry.getHOSPITALNAME());
				String str = entry.getSPECIALITY(); // 不能读长度？
				if (!TextUtils.isEmpty(str)) {
					holder.tv_search_speciality.setText(entry.getSPECIALITY());
					holder.tv_search_shangchang.setVisibility(View.VISIBLE);
					holder.tv_search_speciality.setVisibility(View.VISIBLE); // 这里我改
				} else {
					holder.tv_search_shangchang.setVisibility(View.GONE);
					holder.tv_search_speciality.setVisibility(View.GONE);
				}

				OfferSerViceEntity service_offer = entry.getOFFER_SERVICE();
				// "service": {
				// "1": "图文咨询",
				// "2": "预约通话",
				// "3": "预约加号",
				// "4": "上门会诊",
				// "14": "预约住院",
				// "15": "签约私人医生服务"
				// },
				if ("1".equals(current_service)) {// 图文
					holder.tv_price.setText("￥" + Util.DecZore(service_offer.getOFFER_TEXT().getOFFER_PRICE()));
					holder.tv_price.setVisibility(View.VISIBLE);
					if (Float.valueOf(service_offer.getOFFER_TEXT().getOFFER_PRICE()) == 0)
						holder.tv_price.setText("免费");
				} else if ("2".equals(current_service)) {// 电话
					holder.tv_price.setText("￥" + Util.DecZore(service_offer.getOFFER_CALL().getOFFER_PRICE()));
					holder.tv_price.setVisibility(View.VISIBLE);
					if (service_offer.getOFFER_CALL().getOFFER_PRICE() .equals("0") )
						holder.tv_price.setText("免费");
				} else if ("3".equals(current_service)) {// 预约加号
					holder.tv_price.setText("￥" + Util.DecZore(service_offer.getOFFER_JIAHAO().getOFFER_PRICE()));
					holder.tv_price.setVisibility(View.GONE);
//					if (service_offer.getOFFER_JIAHAO().getOFFER_PRICE() .equals("0"))
//						holder.tv_price.setText("免费");
				} else if ("4".equals(current_service)) {// 上门会诊
					holder.tv_price.setText("￥" + Util.DecZore(service_offer.getOFFER_SHANGMEN().getOFFER_PRICE()));
					holder.tv_price.setVisibility(View.GONE);
//					if (service_offer.getOFFER_SHANGMEN().getOFFER_PRICE() .equals("0"))
//						holder.tv_price.setText("免费");
				} else if ("14".equals(current_service)) {// 预约住院
					holder.tv_price.setText("￥" + Util.DecZore(service_offer.getOFFER_ZHUYUAN().getOFFER_PRICE()));
					holder.tv_price.setVisibility(View.GONE);
//					if (service_offer.getOFFER_ZHUYUAN().getOFFER_PRICE() .equals("0"))
//						holder.tv_price.setText("免费");
				} else if ("15".equals(current_service)) {// 签约私人医生服务
					// holder.tv_price.setText("￥"+service_offer.getOFFER_.getOFFER_PRICE());
					holder.tv_price.setVisibility(View.VISIBLE);
				} // 以上0改成免费
				holder.doctor_head_portrait.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Intent intent = new Intent(ExpertSearchActivity.this, ImageActivity.class);
						intent.putExtra("avatar", entry.getAVATAR());
						startActivity(intent);

					}
				});
			}
			return convertView;
		}

		public void change(List<DoctorEntry2> lists) {
			if (lists == null) {
				lists = new ArrayList<DoctorEntry2>();
			}
			this.listItems = lists;
			notifyDataSetChanged();
		}

		public void add(List<DoctorEntry2> list) {
			this.listItems.addAll(list);
			notifyDataSetChanged();
		}
	}

	class ViewHolder {
		public TextView tv_search_shangchang;
		public TextView tv_price;
		public TextView tv_hos_address;
		public TextView tv_doctor_alias;
		public TextView tv_doctor_name;
		CircleImageView doctor_head_portrait;
		TextView tv_search_speciality;
	}

	// 城市的适配器
	public class CityAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<CityEntry> citys;

		public CityAdapter(Context context, List<CityEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<CityEntry>();
			}
			this.citys = hosps;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return citys.size();
		}

		@Override
		public Object getItem(int position) {
			return citys.get(position);
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
				convertView = inflater.inflate(R.layout.u_room_item, null);
				holder.tvHospName = (TextView) convertView.findViewById(R.id.tv_hosp_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			CityEntry hosp = citys.get(position);
			holder.tvHospName.setText(hosp.getAREANAME());

			if (last_city_position == -1) {
				return convertView;
			} else if (area_position == last_area_position) {
				if (position == last_city_position - 1) {
					convertView.setBackgroundResource(R.color.blue);
				} else {
					convertView.setBackgroundResource(R.color.white);
				}
			} else
				convertView.setBackgroundResource(R.color.white);
			return convertView;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;

		class ViewHolder {
			public TextView tvHospName;
		}

		public void change(List<CityEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<CityEntry>();
			}
			this.citys = hosps;
			notifyDataSetChanged();
		}

		public void add(List<CityEntry> hosps) {
			this.citys.addAll(hosps);
			notifyDataSetChanged();
		}
	}

	// 地区的适配器
	public class ProvinceAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<AreaEntry> citys;

		public ProvinceAdapter(Context context, List<AreaEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<AreaEntry>();
			}
			this.citys = hosps;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return citys.size();
		}

		@Override
		public Object getItem(int position) {
			return citys.get(position);
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
				convertView = inflater.inflate(R.layout.u_room_item, null);
				holder.tvHospName = (TextView) convertView.findViewById(R.id.tv_hosp_name);
				holder.iv_right_arrow = (ImageView) convertView.findViewById(R.id.iv_right_arrow);
				holder.item_view = (View) convertView.findViewById(R.id.item_view);
				if (position == area_position - 1) {
					holder.item_view.setVisibility(View.VISIBLE);
				} else {
					holder.item_view.setVisibility(View.INVISIBLE);
				}
				holder.iv_right_arrow.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				if (position == area_position - 1) {
					holder.item_view.setVisibility(View.VISIBLE);

				} else {
					holder.item_view.setVisibility(View.INVISIBLE);
				}
			}
			AreaEntry hosp = citys.get(position);
			holder.tvHospName.setText(hosp.getAREANAME());

			if (position == area_position - 1) {
				convertView.setBackgroundResource(R.color.white);
			} else if (area_position == -1) {
				convertView.setBackgroundResource(R.color.white);
			} else {
				convertView.setBackgroundResource(R.color.list_gray);

			}
			return convertView;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;

		class ViewHolder {
			public ImageView iv_right_arrow;
			public TextView tvHospName;
			public View item_view;
		}

		public void change(List<AreaEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<AreaEntry>();
			}
			this.citys = hosps;
			notifyDataSetChanged();
		}

		public void add(List<AreaEntry> hosps) {
			this.citys.addAll(hosps);
			notifyDataSetChanged();
		}
	}

	// 科室的适配器
	public class RoomAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<String> hosps;

		public RoomAdapter(Context context, List<String> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<String>();
			}
			this.hosps = hosps;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return hosps.size();
		}

		@Override
		public Object getItem(int position) {
			return hosps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.u_room_item, null);
				holder.tvHospName = (TextView) convertView.findViewById(R.id.tv_hosp_name);
				holder.item_view = (View) convertView.findViewById(R.id.item_view);
				if (position == room_position - 1) {
					holder.item_view.setVisibility(View.VISIBLE);
				} else {
					holder.item_view.setVisibility(View.INVISIBLE);
				}
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
				if (position == room_position - 1) {
					holder.item_view.setVisibility(View.VISIBLE);
				} else {
					holder.item_view.setVisibility(View.INVISIBLE);
				}
			}
			String hosp = hosps.get(position);
			if (null != hosp) {
				holder.tvHospName.setText(hosp);
			}
			if (position == room_position - 1) {
				convertView.setBackgroundResource(R.color.white);
			} else if (room_position == -1) {
				convertView.setBackgroundResource(R.color.white);
			} else {
				convertView.setBackgroundResource(R.color.list_gray);

			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_grade;
			TextView tvHospName;
			View item_view;
		}

		public void change(List<String> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<String>();
			}
			this.hosps = hosps;
			notifyDataSetChanged();
		}

		public void add(List<String> hosps) {
			this.hosps.addAll(hosps);
			notifyDataSetChanged();
		}
	}

	public class NavRoomAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<String> hosps;

		public NavRoomAdapter(Context context, List<String> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<String>();
			}
			this.hosps = hosps;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return hosps.size();
		}

		@Override
		public Object getItem(int position) {
			return hosps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.u_room_item, null);
				holder.tvHospName = (TextView) convertView.findViewById(R.id.tv_hosp_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();

			}
			String hosp = hosps.get(position);
			if (null != hosp) {
				holder.tvHospName.setText(hosp);
			}
			if (last_navroom_position == -1) {
				return convertView;
			} else if (room_position == last_room_position) {
				if (position == last_navroom_position - 1) {
					convertView.setBackgroundResource(R.color.blue);
				} else {
					convertView.setBackgroundResource(R.color.white);
				}
			} else
				convertView.setBackgroundResource(R.color.white);
			return convertView;
		}

		class ViewHolder {
			TextView tv_grade;
			TextView tvHospName;
		}

		public void change(List<String> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<String>();
			}
			this.hosps = hosps;
			notifyDataSetChanged();
		}

		public void add(List<String> hosps) {
			this.hosps.addAll(hosps);
			notifyDataSetChanged();
		}
	}

	// 排序的适配器
	public class SortAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<SortEntry> hosps;

		public SortAdapter(Context context, List<SortEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<SortEntry>();
			}
			this.hosps = hosps;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return hosps.size();
		}

		@Override
		public Object getItem(int position) {
			return hosps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.u_room_item, null);
				holder.tvHospName = (TextView) convertView.findViewById(R.id.tv_hosp_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			SortEntry hosp = hosps.get(position);

			if (null != hosp) {
				holder.tvHospName.setText(hosp.getSort_name());
			}
			if (sort_position == -1) {
				return convertView;
			} else if (position == sort_position) {
				convertView.setBackgroundResource(R.color.blue);
			} else {
				convertView.setBackgroundResource(R.color.white);
			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_grade;
			TextView tvHospName;
		}

		public void change(List<SortEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<SortEntry>();
			}
			this.hosps = hosps;
			notifyDataSetChanged();
		}

		public void add(List<SortEntry> hosps) {
			this.hosps.addAll(hosps);
			notifyDataSetChanged();
		}
	}

	// 服务的适配器
	public class ServiceAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<ServiceEntry> citys;

		public ServiceAdapter(Context context, List<ServiceEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<ServiceEntry>();
			}
			this.citys = hosps;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return citys.size();
		}

		@Override
		public Object getItem(int position) {
			return citys.get(position);
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
				convertView = inflater.inflate(R.layout.u_room_item, null);
				holder.tvHospName = (TextView) convertView.findViewById(R.id.tv_hosp_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			ServiceEntry hosp = citys.get(position);
			holder.tvHospName.setText(hosp.getService_name());
			if (position == mode_position) {
				convertView.setBackgroundResource(R.color.blue);
			} else {
				convertView.setBackgroundResource(R.color.white);
			}
			return convertView;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;

		class ViewHolder {
			public ImageView iv_right_arrow;
			public TextView tvHospName;
		}

		public void change(List<ServiceEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<ServiceEntry>();
			}
			this.citys = hosps;
			notifyDataSetChanged();
		}

		public void add(List<ServiceEntry> hosps) {
			this.citys.addAll(hosps);
			notifyDataSetChanged();
		}
	}

	
	// 医院的适配器
	public class HospAdapter extends BaseAdapter {
		private LayoutInflater inflater;
		private List<HospEntry> hosps;

		public HospAdapter(Context context, List<HospEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<HospEntry>();
			}
			this.hosps = hosps;
			this.inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return hosps.size();
		}

		@Override
		public Object getItem(int position) {
			return hosps.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		public void setSelectItem(int selectItem) {
			this.selectItem = selectItem;
		}

		private int selectItem = 0;

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = inflater.inflate(R.layout.u_room_item, null);
				holder.tvHospName = (TextView) convertView.findViewById(R.id.tv_hosp_name);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			HospEntry hosp = hosps.get(position);

			if (null != hosp) {
				holder.tvHospName.setText(hosp.getHOSPITALNAME());
			}
			if (hosp_position == -1) {
				return convertView;
			} else if (position == hosp_position) {
				convertView.setBackgroundResource(R.color.blue);
			} else {
				convertView.setBackgroundResource(R.color.white);
			}
			return convertView;
		}

		class ViewHolder {
			TextView tv_grade;
			TextView tvHospName;
		}

		public void change(List<HospEntry> hosps) {
			if (hosps == null) {
				hosps = new ArrayList<HospEntry>();
			}
			this.hosps = hosps;
			notifyDataSetChanged();
		}

		public void add(List<HospEntry> hosps) {
			this.hosps.addAll(hosps);
			notifyDataSetChanged();
		}
	}
	public void sysn_config() {

		try {
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Channel", "android"));// 新增
			String content = HttpUtil.getString(HttpUtil.URI + HttpUtil.sync_config, params, HttpUtil.POST);
			content = TextUtil.substring(content, "{");
			if (content != null) {
				JSONObject obj = new JSONObject(content);
				JSONObject resultObj = obj.getJSONObject("data");

				/***** 无耻的分割线 获取科室 ***/
				JSONObject roomsObj = resultObj.getJSONObject("rooms");
				ConfigTable configTable = new ConfigTable();
				configTable.clearTable();
				Iterator arrays = roomsObj.keys();
				String keyName = "";
				String keyValue = "";
				
				while (arrays.hasNext()) {
					keyName = arrays.next().toString();
					Logger.dout("keyName:" + keyName);
					JSONArray roomArray = roomsObj.getJSONArray(keyName);
					for (int i = 0; i < roomArray.length(); i++) {
						Logger.dout("keyValue:" + roomArray.getString(i));
						keyValue = roomArray.getString(i);
						configTable.saveDepartment(keyName, keyValue);
					}
				}
				/***** 无耻的分割线 获取服务 ***/
				JSONObject positionObj = resultObj.getJSONObject("service");
				if (null != positionObj) {
					ServiceConfigTable serviceTable = new ServiceConfigTable();
					serviceTable.clearTable();
					Iterator serviceArrays = positionObj.keys();
					String servicekeyValue = "";
					String servicekeyName = "";
					while (serviceArrays.hasNext()) {
						Logger.dout("servicekeyValue:" + servicekeyValue);
						Logger.dout("servicekeyName:" + servicekeyName);
						servicekeyName = serviceArrays.next().toString();
						servicekeyValue = positionObj.getString(servicekeyName);
						serviceTable.savePositionName(servicekeyName, servicekeyValue);
					}
				}
				/***** 无耻的分割线 获取排序列表 ***/
				JSONObject orderObj = resultObj.getJSONObject("orderby");
				if (null != orderObj) {
					SortConfigTable sortTable = new SortConfigTable();
					sortTable.clearTable();
					Iterator sortArrays = orderObj.keys();
					String sortValue = "";
					String sortId = "";
					while (sortArrays.hasNext()) {
						Logger.dout("sortValue:" + sortValue);
						Logger.dout("sortName:" + sortId);
						sortId = sortArrays.next().toString();
						sortValue = orderObj.getString(sortId);
						sortTable.saveSortName(sortId, sortValue);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	/**
	 * 
	 * @param entry 类转化
	 * @return
	 */
	private DoctorEntry getDoctorEntry(DoctorEntry2 entry){
		
		DoctorEntry doctorEntry = new DoctorEntry();
		doctorEntry.setADDRESS(entry.getADDRESS());
		doctorEntry.setALLOWFREECONSULT(entry.getALLOWFREECONSULT());
		doctorEntry.setAVATAR(entry.getAVATAR());
		doctorEntry.setDOCTORID(entry.getDOCTORID());
		doctorEntry.setREALNAME(entry.getREALNAME());
		doctorEntry.setPROFESSIONAL(entry.getPROFESSIONAL());
		doctorEntry.setHOSPITALNAME(entry.getHOSPITALNAME());
		doctorEntry.setROOMNAME(entry.getROOMNAME());
		doctorEntry.setSPECIALITY(entry.getSPECIALITY());
		doctorEntry.setDOCTOR_DSRS(entry.getDOCTOR_DSRS());
		doctorEntry.setHOSPITAL_INFO(entry.getHOSPITAL_INFO());
		
		doctorEntry.setTEXT_OFFER(Integer.parseInt(entry.getOFFER_SERVICE().getOFFER_TEXT().getIS_OFFER()));
		doctorEntry.setCALL_OFFER(Integer.parseInt(entry.getOFFER_SERVICE().getOFFER_CALL().getIS_OFFER()));
		doctorEntry.setSHANGMEN_OFFER(Integer.parseInt(entry.getOFFER_SERVICE().getOFFER_SHANGMEN().getIS_OFFER()));
		doctorEntry.setJIAHAO_OFFER(Integer.parseInt(entry.getOFFER_SERVICE().getOFFER_JIAHAO().getIS_OFFER()));
		doctorEntry.setZHUYUAN_OFFER(Integer.parseInt(entry.getOFFER_SERVICE().getOFFER_ZHUYUAN().getIS_OFFER()));
		doctorEntry.setCUSTOM_OFFER(Integer.parseInt(entry.getOFFER_SERVICE().getOFFER_CUSTOM().getIS_OFFER()));
		doctorEntry.setPACKET_OFFER(entry.getOFFER_SERVICE().getOFFER_PACKET().getIS_OFFER());
		
		
		doctorEntry.setTEXT_OFFER_PRICE(entry.getOFFER_SERVICE().getOFFER_TEXT().getOFFER_PRICE());
		doctorEntry.setCALL_OFFER_PRICE(entry.getOFFER_SERVICE().getOFFER_CALL().getOFFER_PRICE());
		doctorEntry.setPACKET_ID(entry.getOFFER_SERVICE().getOFFER_PACKET().getPACKET_ID());
		doctorEntry.setPERIOD_LIST(entry.getOFFER_SERVICE().getOFFER_PACKET().getPACKET_PERIOD_LIST());
		
		return doctorEntry;
		
	}
}
