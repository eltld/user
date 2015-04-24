package net.ememed.user2.activity;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.GuahaoDoctorAdapter;
import net.ememed.user2.entity.GuahaoDoctor;
import net.ememed.user2.entity.GuahaoDoctorInfo;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.HospitalEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.RoomEntry;
import net.ememed.user2.entity.ScheduleTime;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.widget.RefreshListView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GuahaoDoctorActivity extends BasicActivity implements OnItemClickListener{

	private HospitalEntry hospitalEntry;
	private RoomEntry room;
	private RefreshListView lvCustomEvas;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;
	private GuahaoDoctorAdapter adapter;
	
	private boolean isflag = false;
	private List<GuahaoDoctorInfo> data = new ArrayList<GuahaoDoctorInfo>();
	private List<GuahaoDoctorInfo> non_data = new ArrayList<GuahaoDoctorInfo>();
	
	private TextView tv_notice;

	private ImageView iv_filter_doctor, iv_filter_date, iv_filter_default;
	private RelativeLayout rl_filter_doctor, rl_filter_date, rl_filter_default;
	private TextView tv_filter_doctor, tv_filter_date, tv_filter_default;
	private PopupWindow popup;
	private SelectAdapter selectAdapter;
	private ArrayList<String> selectorList = new ArrayList<String>();
	private List<GuahaoDoctorInfo> filterData = new ArrayList<GuahaoDoctorInfo>();
	
	private int selectorFlag = 0;	//当前选择的是哪个过滤器
	
	private final int FILTER_BY_DOCTOR_ALL = 1;	//所有医生
	private final int FILTER_BY_DOCTOR_SCHEDULE = 2;	//有号医生
	private final int FILTER_BY_DATE_ALL = 3;	//所有日期
	private final int FILTER_BY_DATE_SINGLE = 4;	//指定日期
	private final int FILTER_BY_DEFAULT = 5;	//默认排序
	private final int FILTER_BY_HOT = 6;	//热门医生
	
	private int filterDoctorFlag = FILTER_BY_DOCTOR_ALL;
	private int filterDateFlag = FILTER_BY_DATE_ALL;
	private int filterHotFlag = FILTER_BY_DEFAULT;
	private String selectDate = "";
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guahao_doctor);
		hospitalEntry = (HospitalEntry) getIntent().getSerializableExtra("hosp_entry");
		room = (RoomEntry) getIntent().getSerializableExtra("hosp_room");
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
		TextView title = (TextView) findViewById(R.id.top_title);
		title.setText("选择医生");
		TextView tv_hospital_name = (TextView) findViewById(R.id.tv_hospital_name);
		tv_hospital_name.setText(hospitalEntry.getHOSPITALNAME() + ">" + room.getROOMNAME());
		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_contact_class);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		tv_notice = (TextView) findViewById(R.id.tv_notice);
		
		iv_filter_doctor = (ImageView) findViewById(R.id.iv_filter_doctor);
		iv_filter_date = (ImageView) findViewById(R.id.iv_filter_date);
		iv_filter_default = (ImageView) findViewById(R.id.iv_filter_default);
		
		rl_filter_doctor = (RelativeLayout) findViewById(R.id.rl_filter_doctor);
		rl_filter_date = (RelativeLayout) findViewById(R.id.rl_filter_date);
		rl_filter_default = (RelativeLayout) findViewById(R.id.rl_filter_default);
		
		tv_filter_doctor = (TextView) findViewById(R.id.tv_filter_doctor);
		tv_filter_date = (TextView) findViewById(R.id.tv_filter_date);
		tv_filter_default = (TextView) findViewById(R.id.tv_filter_default);

		adapter = new GuahaoDoctorAdapter(this, null, GuahaoDoctorAdapter.zhiwu_shanchang);
		lvCustomEvas.setAdapter(adapter);
		getDoctorList(room.getROOMID(), 1);
		
		initPopupWindow(selectorList);
	}

	private void getDoctorList(String roomid, int page) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("roomid", roomid);
			// params.put("page", page+"");

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_guahao_doctor, GuahaoDoctor.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_GUAHAO_DOCTOR;
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
	protected void addListener() {
		
		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				GuahaoDoctorInfo info = (GuahaoDoctorInfo) adapter.getItem(position - 1);
				Intent intent = new Intent(GuahaoDoctorActivity.this, GuahaoDoctorInfoActivity.class);
				intent.putExtra("GuahaoDoctorInfo", (Serializable)info);
				startActivity(intent);
			}
		});
		ll_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getDoctorList(room.getROOMID(), 1);
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

	@Override
	protected void onResume() {
		if (isflag) {
			isflag = false;
			getDoctorList(room.getROOMID(), 1);
		}
		super.onResume();
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.rl_filter_doctor:
			Logger.iout("测试信息","点击医生过滤");
			selectorFlag = 1;
			clearArrayList(selectorList);
			selectorList.add("全部医生");
			selectorList.add("有号医生");
			selectAdapter.notifyDataSetChanged();
			showPopupWindow(rl_filter_doctor, 0, 0);
			break;
		case R.id.rl_filter_date:
			//2014-11-18
			Logger.iout("测试信息","点击日期过滤");
			/*Date date = new Date();  
	        System.out.println(date);  
	        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");  
	        String dateNowStr = sdf.format(date);*/
			selectorFlag = 2;
			clearArrayList(selectorList);
			Calendar cal = Calendar.getInstance(); //使用默认时区和语言环境获得一个日历。
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			
			selectorList.add("全部日期");
			selectorList.add(sdf.format(cal.getTime()));
			for(int i = 0; i < 7; i++){
				cal.add(Calendar.DAY_OF_MONTH, +1);
				selectorList.add(sdf.format(cal.getTime()));
			}
			selectAdapter.notifyDataSetChanged();
			showPopupWindow(rl_filter_date, 0, 0);
			break;
		case R.id.rl_filter_default:
			Logger.iout("测试信息","点击默认/热门过滤");
			selectorFlag = 3;
			clearArrayList(selectorList);
			selectorList.add("默认");
			selectorList.add("热门");
			selectAdapter.notifyDataSetChanged();
			showPopupWindow(rl_filter_default, 0, 0);
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		switch (msg.what) {
		case IResult.DATA_ERROR:
			ll_empty.setVisibility(View.VISIBLE);
			lvCustomEvas.setVisibility(View.GONE);
			break;
		case IResult.GET_GUAHAO_DOCTOR:
			GuahaoDoctor doctor = (GuahaoDoctor) msg.obj;
			if (doctor != null) {
				switch (doctor.getSuccess()) {
				case 1:
					data  = doctor.getData();
					non_data = doctor.getNon_data();
					//copy一份完整的数据到filterData，包括data和non_data;
					for(GuahaoDoctorInfo info : data){
						filterData.add(info);
					}
					for(GuahaoDoctorInfo info : non_data){
						filterData.add(info);
					}
					
					/*if (data  != null && data .size() > 0) {
						adapter.change(data );
					} */
					if (filterData  != null && filterData .size() > 0) {
						adapter.change(filterData);
					} else {
						ll_empty.setVisibility(View.VISIBLE);
						lvCustomEvas.setVisibility(View.GONE);
					}
					break;
				default:
//					showToast(doctor.getErrormsg());
					if(!TextUtils.isEmpty(doctor.getErrormsg()))
						tv_notice.setText(doctor.getErrormsg());
					ll_net_unavailable.setVisibility(View.GONE);
					ll_empty.setVisibility(View.VISIBLE);
					lvCustomEvas.setVisibility(View.GONE);
					break;
				}
			}else{
				ll_net_unavailable.setVisibility(View.GONE);
				ll_empty.setVisibility(View.VISIBLE);
				lvCustomEvas.setVisibility(View.GONE);
				Toast.makeText(this, IMessage.DOCTOR_DATA_ERROR, 1).show();
			}
			break;
		case IResult.NET_ERROR:
			// showToast(IMessage.NET_ERROR);
			ll_net_unavailable.setVisibility(View.VISIBLE);
			ll_empty.setVisibility(View.GONE);
			lvCustomEvas.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 初始化下拉框
	 */
	private void initPopupWindow(ArrayList<String> list){
		
		ListView lv = new ListView(this);
		
		lv.setBackgroundColor(Color.LTGRAY);
		selectAdapter = new SelectAdapter(list);
		
		lv.setVerticalScrollBarEnabled(true);
		//lv.setScrollBarFadeDuration(0);
		lv.setDivider(new ColorDrawable(Color.GRAY));
		lv.setDividerHeight(1);
		lv.setAdapter(selectAdapter);
		lv.setOnItemClickListener(this);
		
		popup = new PopupWindow(lv, MyApplication.getInstance().canvasWidth/3, LayoutParams.WRAP_CONTENT /*Util.dip2px(this, 200)*/);
		popup.setOutsideTouchable(true);
		popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popup.setFocusable(true);
		popup.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() {
				iv_filter_doctor.setSelected(false);
				iv_filter_date.setSelected(false);
				iv_filter_default.setSelected(false);
				//bg_gray.setVisibility(View.GONE);
			}
		});
	}
	
	
	private class SelectAdapter extends BaseAdapter{

		ArrayList<String> list;
		
		public SelectAdapter(ArrayList<String> list){
			if(null != list){
				this.list = list;
			}else{
				list = new ArrayList<String>();
			}
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
			TextView tv = null;
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(), R.layout.item_filter, null);
				tv = (TextView) convertView.findViewById(R.id.tv_factor);
				convertView.setTag(tv);
			} else {
				tv = (TextView) convertView.getTag();
			}
			tv.setText(list.get(position));
			return convertView;
		}
		
	}

	private void clearArrayList(ArrayList<String> list){
		while(!list.isEmpty()){
			list.remove(0);
		}
	}
	
	/**
	 * 显示下拉框
	 * @param view
	 */
	private void showPopupWindow(View view, int offX, int offY){
		if(popup!=null){
			//bg_gray.setVisibility(View.VISIBLE);//设置灰色
			popup.showAsDropDown(view, offX, offY);
			view.setSelected(true);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		
		popup.dismiss();
		switch(selectorFlag){
		case 1:
			if(0 == position){
				filterDoctorFlag = FILTER_BY_DOCTOR_ALL;
				tv_filter_doctor.setText("全部医生");
				
				//当点击第一个过滤器选择全部医生时，第二和第三个过滤器都恢复默认
				filterDateFlag = FILTER_BY_DATE_ALL;
				filterHotFlag = FILTER_BY_DEFAULT;
				tv_filter_date.setText("全部日期");
				tv_filter_default.setText("默认");
			} else {
				filterDoctorFlag = FILTER_BY_DOCTOR_SCHEDULE;
				tv_filter_doctor.setText("有号医生");
			}
			updateFilterData(selectDate);
			adapter.notifyDataSetChanged();
			break;
		case 2:
			if(0 == position){
				filterDateFlag = FILTER_BY_DATE_ALL;
				tv_filter_date.setText("全部日期");
			}else{
				filterDateFlag = FILTER_BY_DATE_SINGLE;
				selectDate = selectorList.get(position);
				tv_filter_date.setText(selectDate);
			}
			updateFilterData(selectDate);
			adapter.notifyDataSetChanged();
			break;
		case 3:
			if(0 == position){
				filterHotFlag = FILTER_BY_DEFAULT;
				tv_filter_default.setText("默认");
			}else{
				filterHotFlag = FILTER_BY_HOT;
				tv_filter_default.setText("热门");
			}
			updateFilterData(selectDate);
			adapter.notifyDataSetChanged();
			break;
		default:
			break;
		}
	}
	
	
	/**
	 * 判断医生是否有号
	 * @param scheduleList
	 * @return true:有号
	 */
	private boolean judgeDoctorhasSchedule(GuahaoDoctorInfo info){
		List<ScheduleTime> schedule = info.getSchedule();
		if(null == schedule || 0 == schedule.size()){
			return false;
		}
		return true;
	}
	
	
	/**
	 * 判断医生指定日期是否有班
	 * @param scheduleList 排班信息表
	 * @return true：有班
	 */
	private boolean judgeDoctorhasScheduleByDate(List<ScheduleTime> scheduleList, String date){
		if(null == scheduleList || 0 == scheduleList.size()){
			return false;
		}
		
		for(ScheduleTime time : scheduleList){
			String ndate1 = time.getSchedule();
			if(null == ndate1 || ndate1.equals("")){
				return false;
			}
			String ndate2 = ndate1.substring(ndate1.indexOf("/")+1, ndate1.indexOf("/")+11);
			if(ndate2.equals(date)){
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * 判断当前医生是否是热门医生
	 * @param info
	 * @return true：是热门医生
	 */
	private boolean judgeDoctorIsHot(GuahaoDoctorInfo info){
		if(1 == Integer.parseInt(info.getDoctor_info().getIS_HOT()))
		{
			return true;
		}
		return false;
	}
	
	/**
	 * 更新过滤数据
	 * @param filterType 
	 * @param date
	 */
	private void updateFilterData(String date){
		
		//清空filterData
		while(!filterData.isEmpty()){
			filterData.remove(0);
		}
		
		//从本地装载所有数据(有号的)
		for(int i = 0; i < data.size(); i++){
			GuahaoDoctorInfo temp = data.get(i);
			filterData.add(temp);
		}
		
		//如果选择第一个过滤器为全部医生，从本地加载（无号的）
		if(FILTER_BY_DOCTOR_ALL == filterDoctorFlag){
		
			for(int i = 0; i < non_data.size(); i++){
				GuahaoDoctorInfo temp = non_data.get(i);
				filterData.add(temp);
			}
		}	
		
		//如果三个过滤器都为默认，则不过滤
		if(FILTER_BY_DOCTOR_ALL == filterDoctorFlag && FILTER_BY_DATE_ALL == filterDateFlag 
				&& FILTER_BY_DEFAULT == filterHotFlag){
			
		}else{
			/*	
			//过滤器1：全部医生/有号医生
			if(FILTER_BY_DOCTOR_SCHEDULE == filterDoctorFlag){
				for(int i = 0; i < filterData.size(); i++){
					if(!judgeDoctorhasSchedule(filterData.get(i))){
						filterData.remove(i);
						i--;
					}
				}
			}*/
			
			//过滤器2：全部日期/指定日期
			if(FILTER_BY_DATE_SINGLE == filterDateFlag){
				for(int i = 0; i < filterData.size(); i++){
					if(!judgeDoctorhasScheduleByDate(filterData.get(i).getSchedule(), date)){
						filterData.remove(i);
						i--;
					}
				}
			}
			
			//过滤器3：默认/热门医生
			if(FILTER_BY_HOT == filterHotFlag){
				for(int i = 0; i < filterData.size(); i++){
					if(!judgeDoctorIsHot(filterData.get(i))){
						filterData.remove(i);
						i--;
					}
				}
			}
		}
		
		if(filterData.size() > 0){
			ll_empty.setVisibility(View.GONE);
			lvCustomEvas.setVisibility(View.VISIBLE);
		}else{
			ll_empty.setVisibility(View.VISIBLE);
			lvCustomEvas.setVisibility(View.GONE);
		}
	}
}
