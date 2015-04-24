package net.ememed.user2.baike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.DoctorClininActivity;
import net.ememed.user2.activity.DoctorInfoActivity;
import net.ememed.user2.baike.adapter.FansAdapter;
import net.ememed.user2.baike.entity.DoctorAttentionEntry;
import net.ememed.user2.baike.entity.DoctorAttentionInfo;
import net.ememed.user2.baike.entity.FansData;
import net.ememed.user2.baike.entity.FansEntry;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

public class DoctorFansActivity extends BasicActivity{
	
	public static final int FANS_TYPE_DOCTOR = 1;
	public static final int FANS_TYPE_USER = 2;
	
	private String doctorId;
	private ViewPager viewPager;
	private View view1, view2;
	private LayoutInflater inflater;
	private ArrayList<View> views = new ArrayList<View>();
	private MyViewPagerAdapter pagerAdapter;
	private RefreshListView lv_doctor_fans;
	private RefreshListView lv_user_fans;
	private int user_page = 1;
	private int doctor_page = 1;
	private boolean isRefresh = true;
	private int fansType;
	private List<FansData> doctorFans = null;
	private List<FansData> userFans = null;
	private FansAdapter doctorAdapter;
	private FansAdapter userAdapter;
	
	private LinearLayout ll_tab_doctor_fans;
	private LinearLayout ll_tab_user_fans;
	private TextView tv_doctor_fans;
	private TextView tv_user_fans;
	private View line_doctor_fans;
	private View line_user_fans;
	private LinearLayout ll_empty_docotr;
	private LinearLayout ll_empty_user;
	
	private boolean isFirstLoadDoctorFans = true;
	private boolean isFirstLoadUserFans = true;
	
	public static void startAction(Context context, String doctorId, int fansType){
		Intent intent = new Intent(context, DoctorFansActivity.class);
		intent.putExtra("doctor_id", doctorId);
		intent.putExtra("fans_type", fansType);
		context.startActivity(intent);
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		
		setContentView(R.layout.activity_doctor_fans);
		doctorId = getIntent().getStringExtra("doctor_id");
		fansType = getIntent().getIntExtra("fans_type", FANS_TYPE_DOCTOR);
		doctorFans = new ArrayList<FansData>();
	    userFans = new ArrayList<FansData>();
	}
	
	@Override
	protected void setupView() {
		super.setupView();
		
		viewPager = (ViewPager) findViewById(R.id.vPager);
		inflater = LayoutInflater.from(this);
		view1 = inflater.inflate(R.layout.pager_dooctor_fans, null);
		view2 = inflater.inflate(R.layout.pager_dooctor_fans, null);
		
		ll_tab_doctor_fans = (LinearLayout) findViewById(R.id.ll_tab_doctor_fans);
		ll_tab_user_fans = (LinearLayout) findViewById(R.id.ll_tab_user_fans);
		tv_doctor_fans = (TextView) findViewById(R.id.tv_doctor_fans);
		tv_user_fans = (TextView) findViewById(R.id.tv_user_fans);
		line_doctor_fans = findViewById(R.id.line_doctor_fans);
		line_user_fans = findViewById(R.id.line_user_fans);
		
		lv_doctor_fans = (RefreshListView) view1.findViewById(R.id.lv_fans);
		lv_user_fans = (RefreshListView) view2.findViewById(R.id.lv_fans);
		ll_empty_docotr = (LinearLayout) view1.findViewById(R.id.ll_empty);
		ll_empty_user = (LinearLayout) view2.findViewById(R.id.ll_empty);
		doctorAdapter = new FansAdapter(this, doctorFans, FANS_TYPE_DOCTOR);
		userAdapter = new FansAdapter(this, userFans, FANS_TYPE_USER);
		lv_doctor_fans.setAdapter(doctorAdapter);
		lv_user_fans.setAdapter(userAdapter);
		
		views.add(view1);
		views.add(view2);
		pagerAdapter = new MyViewPagerAdapter(views);
		viewPager.setAdapter(pagerAdapter);
		
		if(FANS_TYPE_DOCTOR == fansType){
	    	changeTab(0);
	    } else {
	    	changeTab(1);
	    }
	}
	
	@Override
	protected void addListener() {
		super.addListener();
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				changeTab(arg0);
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		lv_doctor_fans.setOnRefreshListener(new IOnRefreshListener() {
			
			@Override
			public void OnRefresh() {
				isRefresh = true;
				doctor_page = 1;
				getFans(FANS_TYPE_DOCTOR);
			}
		});
		
		lv_doctor_fans.setOnLoadMoreListener(new IOnLoadMoreListener() {
			
			@Override
			public void OnLoadMore() {
				isRefresh = false;
				doctor_page += 1;
				getFans(FANS_TYPE_DOCTOR);
			}
		});
		
		lv_doctor_fans.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(DoctorFansActivity.this, DoctorClininActivity.class);
				FansData data = (FansData)arg0.getAdapter().getItem(arg2);
				DoctorEntry entry = data.getDOCTORINFO();
				entry.setREALNAME(data.getREALNAME());
				entry.setAVATAR(data.getAVATAR());
//				entry.setDOCTORID(data.getOTHER_MEMBERID());
//				entry.setALLOWFREECONSULT(data.getALLOWFREECONSULT());
				entry.setPROFESSIONAL(data.getPROFESSIONAL());
//				entry.setADDRESS(data.getADDRESS());
//				entry.setROOMNAME(data.getROOMNAME);
//				entry.setSPECIALITY(data.getSPECIALITY());
//				entry.setCUSTOM_OFFER(data.getCUSTOM_OFFER());
//				entry.setSEXNAME(data.getSEXNAME());
//				entry.setMOBILE(data.getMOBLILE());
//				entry.setDETAILINFO(data.getDETAILINFO());
//				entry.setPERCENT(data.getPERCENT());
//				entry.setAUDITSTATUS(data.getAUDITSTATUS());
//				entry.setFans_total_num(data.getFans_total_num());
//				entry.setFans_user_total_num(data.getFans_user_total_num());
//				entry.setFans_doctor_total_num(data.getFans_doctor_total_num());
				
				intent.putExtra("title", entry.getREALNAME());
				intent.putExtra("current_service", "1");
				intent.putExtra("entry", entry);
				startActivity(intent);
			}
		});
		
		lv_user_fans.setOnRefreshListener(new IOnRefreshListener() {
			
			@Override
			public void OnRefresh() {
				isRefresh = true;
				user_page = 1;
				getFans(FANS_TYPE_USER);
			}
		});
		
		lv_user_fans.setOnLoadMoreListener(new IOnLoadMoreListener() {
			
			@Override
			public void OnLoadMore() {
				isRefresh = false;
				user_page += 1;
				getFans(FANS_TYPE_USER);
			}
		});
		
		ll_tab_doctor_fans.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(viewPager.getCurrentItem() != 0)
					changeTab(0);
			}
		});
		
		ll_tab_user_fans.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(viewPager.getCurrentItem() != 1)
					changeTab(1);
			}
		});
	}
	
	/**
	 * 获取医生粉丝列表
	 */
	private void getFans(int type){
		if (NetWorkUtils.detect(DoctorFansActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("doctorid", doctorId);
			params.put("type", ""+type); //粉丝类型(0全部，1医生，2患者)
			if(FANS_TYPE_DOCTOR == type){
				params.put("pages", "" + doctor_page);
			} else if(FANS_TYPE_USER == type){
				params.put("pages", "" + user_page);
			}
		
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_doctor_fans, FansEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GET_DOCTOR_FANS;
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
		lv_doctor_fans.onRefreshComplete();
		lv_user_fans.onRefreshComplete();
		switch (msg.what) {
		case IResult.GET_DOCTOR_FANS:
			FansEntry entry = (FansEntry) msg.obj;
			if (null != entry) {
				if (1 == entry.getSuccess()) {
					if(FANS_TYPE_DOCTOR == fansType){
						isFirstLoadDoctorFans = false;
						if(isRefresh){
							doctorFans = entry.getData();
						} else {
							doctorFans.addAll(entry.getData());
						}
						
						if(doctorFans.size() > 0){
							ll_empty_docotr.setVisibility(View.GONE);
							doctorAdapter.change(doctorFans);
						} else {
							ll_empty_docotr.setVisibility(View.VISIBLE);
						}
						
						if(entry.getPages() <= doctor_page){
							lv_doctor_fans.onLoadMoreComplete(true);
						} else {
							lv_doctor_fans.onLoadMoreComplete(false);
						}
					} else {
						isFirstLoadUserFans = false;
						if(isRefresh){
							userFans = entry.getData();
						} else {
							userFans.addAll(entry.getData());
						}
						
						if(userFans.size() > 0){
							ll_empty_user.setVisibility(View.GONE);
							userAdapter.change(userFans);
						} else {
							ll_empty_user.setVisibility(View.VISIBLE);
						}
						
						if(entry.getPages() <= user_page){
							lv_user_fans.onLoadMoreComplete(true);
						} else {
							lv_user_fans.onLoadMoreComplete(false);
						}
					}
				} else {
					showToast(entry.getErrormsg());
				}
			}
			break;
		case IResult.DATA_ERROR:
			showToast(IMessage.DATA_ERROR);
			break;
		case IResult.NET_ERROR:
			showToast(IMessage.NET_ERROR);
			break;
		default:
			break;
		}
		super.onResult(msg);
	}
	
	private void changeTab(int position){
		tv_doctor_fans.setTextColor(getResources().getColor(R.color.grayness));
		tv_user_fans.setTextColor(getResources().getColor(R.color.grayness));
		line_doctor_fans.setVisibility(View.GONE);
		line_user_fans.setVisibility(View.GONE);
		
		if(0 == position){
			viewPager.setCurrentItem(0);
			tv_doctor_fans.setTextColor(getResources().getColor(R.color.blue));
			line_doctor_fans.setVisibility(View.VISIBLE);
			fansType = FANS_TYPE_DOCTOR;
			if(isFirstLoadDoctorFans){
				getFans(FANS_TYPE_DOCTOR);
			}
		} else if (1 == position){
			viewPager.setCurrentItem(1);
			tv_user_fans.setTextColor(getResources().getColor(R.color.blue));
			line_user_fans.setVisibility(View.VISIBLE);
			fansType = FANS_TYPE_USER;
			if(isFirstLoadUserFans){
				getFans(FANS_TYPE_USER);
			}
		}
	}
	
	class MyViewPagerAdapter extends PagerAdapter {

		private List<View> mListViews;
		
		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;//构造方法，参数是我们的页卡，这样比较方便。
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) 	{	
			container.removeView(mListViews.get(position));//删除页卡
		}


		@Override
		public Object instantiateItem(ViewGroup container, int position) {	//这个方法用来实例化页卡		
			 container.addView(mListViews.get(position), 0);//添加页卡
			 return mListViews.get(position);
		}

		@Override
		public int getCount() {			
			return  mListViews.size();//返回页卡的数量
		}
		
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {			
			return arg0==arg1;//官方提示这样写
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_doctor_fans);
	}
}
