package net.ememed.user2.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.HorizontalListViewAdapter;
import net.ememed.user2.db.GuahaoAreaTable;
import net.ememed.user2.entity.AreaGuahaoInfo;
import net.ememed.user2.entity.CityGuahaoEntry;
import net.ememed.user2.entity.GuahaoDoctor;
import net.ememed.user2.entity.GuahaoDoctorInfo;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.GuahaoRecommendDoctor;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.fragment.MineFragment;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.HorizontalListView;
import android.R.integer;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GuahaoActivity extends BasicActivity implements OnItemClickListener {

	private PopupWindow popup;
	private LinearLayout ll_select_city;
	// private Map<String, String> citys;
	private List<String> cityNames;
	private CityAdapter mAdapter;
	private List<String> cityids;
	private String selectedCityId = "289";// 原先是298017
	private String selectedCityName = "广州市"; //
	private TextView tv_city_name;
	private EditText et_keyword;
	private GuahaoAreaTable table = new GuahaoAreaTable();
	private ImageView ib_down_arrow;
	private View rl_top_title;
	private View bg_gray;
	private List<GuahaoDoctotItem> recommendDoctorData = new ArrayList<GuahaoDoctotItem>();
	private HorizontalListView hListView;
	private HorizontalListViewAdapter hListViewAdapter;
	private boolean isLogin = false;

	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		selectedCityName = MyApplication.current_city;
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// TODO:2.3挂号 change to activity_guahao2_3
		setContentView(R.layout.activity_guahao);
	}

	@Override
	protected void setupView() {
		TextView title = (TextView) findViewById(R.id.top_title);
		title.setText("免费挂号");
		tv_city_name = (TextView) findViewById(R.id.tv_city_name);
		ll_select_city = (LinearLayout) findViewById(R.id.ll_select_city);
		et_keyword = (EditText) findViewById(R.id.et_disease_keyword);
		ib_down_arrow = (ImageView) findViewById(R.id.ib_down_arrow);
		rl_top_title = findViewById(R.id.fl_top_title);
		bg_gray = findViewById(R.id.bg_gray);

		hListView = (HorizontalListView) findViewById(R.id.horizon_listView);
		hListViewAdapter = new HorizontalListViewAdapter(this, null);
		hListView.setAdapter(hListViewAdapter);
		hListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(GuahaoActivity.this, DoctorScheduleActivity.class);
				Serializable item = (Serializable) hListViewAdapter.getItem(position);
				intent.putExtra("Doctot_Item", item);
				startActivity(intent);
			}
		});
		// XXX
		/*
		 * //输入法回车键变搜索 et_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
		 * 
		 * @Override public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
		 * 
		 * if (actionId == EditorInfo.IME_ACTION_SEND || (event!=null&&event.getKeyCode() ==
		 * KeyEvent.KEYCODE_ENTER)) { String keyword = et_keyword.getText().toString().trim(); if
		 * (TextUtils.isEmpty(keyword)) { showToast("搜索关键字为空"); } else { Intent searchIntent = new
		 * Intent(GuahaoActivity.this, GuaHaoSearchActivity.class); searchIntent.putExtra("keyword",
		 * keyword); startActivity(searchIntent); } return true; }
		 * 
		 * return false; }
		 * 
		 * });
		 */

		et_keyword.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// int inType = et_disease_keyword.getInputType(); // backup the input type
				et_keyword.setInputType(InputType.TYPE_NULL); // disable soft input
				// et_disease_keyword.onTouchEvent(event); // call native handler
				// et_disease_keyword.setInputType(inType); // restore input type
				// et_disease_keyword.setSelection(et_disease_keyword.getText().length());

				Intent intent = new Intent(GuahaoActivity.this, GuaHaoSearchActivity.class);
				startActivity(intent);
				return true;
			}
		});

		// 本地缓存
		try {

			List<String> cities = table.getCitiesName();
			List<String> citieids = table.getCitiesId();

			if (null != cities && cities.size() > 0) {

				cityNames = cities;
				cityids = citieids;

				Logger.iout("获取城市信息", "from 本地缓存");
				getCurrentCityNameAndId();

				initCitys();
				updata();
			} else {
				loadData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		selectedCityName = MyApplication.current_city;
		tv_city_name.setText(selectedCityName);

		getHotDoctorList();
	}

	private void getCurrentCityNameAndId() {
		// cityName = MyApplication.current_city;
		if (cityNames != null && cityNames.size() > 0)
			for (int i = 0; i < cityNames.size(); i++) {
				String city = cityNames.get(i);
				if (city.equals(selectedCityName)) {
					selectedCityId = cityids.get(i);
					break;
				}
			}

		tv_city_name.setText(selectedCityName);
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.rl_findDoctor:// 按医院找医生(去挂号)
		case R.id.iv_guahao:
			Logger.iout("获取城市信息", selectedCityName);
			if (!judgeCurrentCityCanUse(selectedCityName)) {
				showToast("当前城市尚未开通，请切换其他城市");
				return;
			}

			if (NetWorkUtils.detect(this)) {
				getCurrentCityNameAndId();
				Intent i = new Intent(this, HospitalActivity.class);
				i.putExtra("cityId", selectedCityId);
				i.putExtra("cityName", selectedCityName);
				startActivity(i);
			} else {
				handler.sendEmptyMessage(IResult.NET_ERROR);
			}

			// Intent i = new Intent(this,VisitPersonChoiceActivity.class);
			// startActivity(i);
			break;
		case R.id.iv_smart:// 智能导诊
			startActivity(new Intent(this, GuahaoSmartActivity.class));
			break;
		case R.id.bt_keyword_search:// 搜索医生
			String keyword = et_keyword.getText().toString().trim();
			if (TextUtils.isEmpty(keyword)) {
				showToast("搜索关键字为空");
			} else {
				Intent searchIntent = new Intent(this, GuaHaoSearchActivity.class);
				searchIntent.putExtra("keyword", keyword);
				startActivity(searchIntent);
			}
			break;
		case R.id.ll_select_city:// 选择城市
			if (popup != null) {
				if (bg_gray != null)
					bg_gray.setVisibility(View.VISIBLE);// 设置灰色
				popup.showAsDropDown(rl_top_title, 0, 0);
				ib_down_arrow.setSelected(true);
			}
			break;
		case R.id.ll_user_guider:// 新手指引
		case R.id.iv_guide: {
			Intent intent = new Intent(GuahaoActivity.this, GuaHaoUserGuideActivity2.class);
			startActivity(intent);
		}
			break;
		case R.id.tv_follow:// 我的关注
			startActivity(new Intent(this, GuahaoFollowActivity.class));
			break;
		case R.id.ll_my_order:// 我的预约
		case R.id.tv_order:
			if (SharePrefUtil.getBoolean("savepwd") && SharePrefUtil.getBoolean(Conast.LOGIN))
				startActivity(new Intent(this, HistoryGuahaoActivity.class));
			else
				showIsLogin();
			break;
		case R.id.ll_my_patient:// 我的就诊人
		case R.id.tv_patient: {
			// Intent intent = new Intent(this, VisitPersonChoiceActivity.class);
			Intent intent = new Intent(this, VisitPersonChoiceActivity2.class);
			intent.putExtra("isflag", false);
			startActivity(intent);
		}
			break;
		default:
			break;
		}
	}

	private void showIsLogin() {
		// TODO Auto-generated method stub
		isLogin = true;
		Intent intent = new Intent(this, LoginActivity.class);
		intent.putExtra("origin", MineFragment.class.getSimpleName());
		startActivity(intent);
	}

	protected void loadData() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.guahao_get_area_list,
					AreaGuahaoInfo.class, null, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = handler.obtainMessage();
							message.obj = response;
							message.what = IResult.GET_AREA_LIST;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = handler.obtainMessage();
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
	protected void getData() {

	}

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		switch (msg.what) {
		case IResult.GET_AREA_LIST:
			AreaGuahaoInfo info = (AreaGuahaoInfo) msg.obj;
			if (info != null) {
				switch (info.getSuccess()) {
				case 1:
					// citys = info.getData();
					// Set<String> city_ids = citys.keySet();
					cityNames = new ArrayList<String>();
					cityids = new ArrayList<String>();

					List<CityGuahaoEntry> cityList = info.getData();
					for (CityGuahaoEntry cityEntry : cityList) {
						cityNames.add(cityEntry.getCITY_NAME());
						cityids.add(cityEntry.getID());
					}

					Logger.iout("获取城市信息", "from loadDate()");

					getCurrentCityNameAndId();

					initCitys();
					table.saveAreas(info.getData());

					break;

				default:
					break;
				}
			} else {
				showToast(IMessage.DATA_ERROR);
			}
			break;
		case IResult.GET_GUAHAO_RECOMMEND_DOCTOR:
			GuahaoRecommendDoctor recommendDoctor = (GuahaoRecommendDoctor) msg.obj;
			if (recommendDoctor != null) {
				switch (recommendDoctor.getSuccess()) {
				case 1:
					recommendDoctorData = recommendDoctor.getData();

					if (recommendDoctorData != null && recommendDoctorData.size() > 0) {
						// progressBar.setVisibility(View.GONE);
						// adapter.change(data );

						hListViewAdapter.change(recommendDoctorData);

					} else {
						// 暂无推荐医生
					}
					break;
				default:
					break;
				}
			}
			break;
		case IResult.NET_ERROR:
			showToast(getString(R.string.guahao_net_error));
			break;
		default:
			break;
		}
	}

	@SuppressLint("NewApi")
	private void initCitys() {
		View view = LayoutInflater.from(this).inflate(R.layout.popup_listview, null);
		// ListView lv = new ListView(this);
		ListView lv = (ListView) view.findViewById(R.id.listView);

		lv.setBackgroundColor(Color.WHITE);
		mAdapter = new CityAdapter();

		lv.setVerticalScrollBarEnabled(true);
		lv.setScrollBarFadeDuration(0);
		lv.setDivider(new ColorDrawable(Color.GRAY));
		lv.setDividerHeight(1);
		lv.setAdapter(mAdapter);
		lv.setOnItemClickListener(this);

		// int Height = Util.dip2px(this, 40)*mAdapter.getCount()+mAdapter.getCount();
		// if(Height>(MyApplication.getInstance().canvasHeight-Util.dip2px(this, 48))){
		// Height = MyApplication.getInstance().canvasHeight-Util.dip2px(this, 48);
		// }

		popup = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
		popup.setOutsideTouchable(true);
		popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popup.setFocusable(true);
		popup.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ib_down_arrow.setSelected(false);
				if (bg_gray != null)
					bg_gray.setVisibility(View.GONE);
			}
		});

	}

	private void updata() {
		if (NetWorkUtils.detect(this)) {
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.guahao_get_area_list,
					AreaGuahaoInfo.class, null, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = handler.obtainMessage();
							message.obj = response;
							message.what = IResult.GET_AREA_LIST;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = handler.obtainMessage();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							handler.sendMessage(message);
						}
					});
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		popup.dismiss();
		selectedCityId = cityids.get(position);
		selectedCityName = cityNames.get(position);
		tv_city_name.setText(selectedCityName);
		getHotDoctorList();
	}

	class CityAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cityNames.size();
		}

		@Override
		public Object getItem(int position) {
			return cityNames.get(position);
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
			tv.setText(cityNames.get(position));
			return convertView;
		}
	}

	private void getHotDoctorList() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			getCurrentCityNameAndId();
			Map<String, String> params = new HashMap<String, String>();
			params.put("cityid", selectedCityId);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_guahao_doctor_recommend,
					GuahaoRecommendDoctor.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GET_GUAHAO_RECOMMEND_DOCTOR;
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

	/**
	 * 判断当前城市是否已开通服务
	 * 
	 * @param cityName
	 * @return true 已开通
	 */
	private boolean judgeCurrentCityCanUse(String cityName) {
		for (int i = 0; i < cityNames.size(); i++) {
			if (cityName.equals(cityNames.get(i))) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.guahao_get_area_list);
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_guahao_doctor_recommend);
	}
}
