package net.ememed.user2.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.DoctorSearchActivity;
import net.ememed.user2.activity.ExpertSearchActivity;
import net.ememed.user2.activity.GuahaoActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.activity.MainActivity;
import net.ememed.user2.activity.PutQuestionsActivity;
import net.ememed.user2.activity.SearchResourceActivity;
import net.ememed.user2.activity.WebViewActivity;
import net.ememed.user2.activity.WebViewThirdServiceActivity;
import net.ememed.user2.db.ConfigTable;
import net.ememed.user2.db.HomeAdTable;
import net.ememed.user2.db.NewsTypeTable;
import net.ememed.user2.db.ServiceConfigTable;
import net.ememed.user2.db.SortConfigTable;
import net.ememed.user2.entity.AdsEntry;
import net.ememed.user2.entity.HomeAdsInfo;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.NewsEntity;
import net.ememed.user2.entity.NewsTypeEntry;
import net.ememed.user2.entity.NewsTypeInfo;
import net.ememed.user2.entity.SearchDoctorAll;
import net.ememed.user2.entity.ServiceListEntry;
import net.ememed.user2.entity.ServiceListInfo;
import net.ememed.user2.entity.StaticVariableBean;
import net.ememed.user2.entity.VariableBean;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.AppUtils;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.util.Util;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * @author taro chyaohui@gmail.com
 */
public class HomeFragment extends Fragment implements BasicUIEvent, Callback,
		OnClickListener {
	private static final String TAG = HomeFragment.class.getSimpleName();
	public static final int EXEU_GET_DATA = 0;
	public static final int EXEU_GET_DATA_ERROR = 1;
	private static final int EXEU_SYS_DATA = 2;
	private FrameLayout mContentView = null;
	private Handler handler = null;
	private MainActivity activity = null;
	private PullToRefreshLayout ll_view_content;
	private LinearLayout ll_txt_top;
	private RelativeLayout questions_layout;
	private LinearLayout ll_phone_top;
	private LinearLayout ll_search_box;
	private LinearLayout ll_guahao_top;
	private LinearLayout ll_zhuyuan_top;
	private LinearLayout ll_shangmen_top;
	private LinearLayout ll_private_top;
	private LinearLayout ll_notice_custom_top;
	private LinearLayout ll_yongyao_top;
	private ImageView bt_keyword_search;
	private TextView et_disease_keyword;
	private LinearLayout ll_free_question;
	private GestureDetector gestureDetector;

	private LinearLayout ll_switch_top;
	private FrameLayout fl_home_ads;
	/*
	 * private LinearLayout ll_about_us; private LinearLayout ll_ememed_doctor;
	 * private ImageView ig_ememed_doctor;
	 */
	private LinearLayout ll_qingsong_guahao;
	private ViewPager vp_ad;
	private AdapterCycle adapterCycle;
	private List<AdsEntry> data;
	private List<View> views = new ArrayList<View>();
	private ArrayList<View> dots;
	private LinearLayout ll_dot_content;
	/* private ScrollView cv_content; */
	private HomeAdTable table = new HomeAdTable();
	private boolean isFromNet = false;
	private int clickTimes = 0;

	private AlphaAnimation alphaShow = null;
	private AlphaAnimation alphaDisapear = null;

	private ArrayList<ServiceListEntry> serviceContent = null;

	public HomeFragment() {
		this.activity = (MainActivity) getActivity();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
		handler = new Handler(this);
		Logger.dout(TAG + "onCreate");
		activity = (MainActivity) getActivity();
		UICore.eventTask(this, activity, EXEU_GET_DATA, null, null);// 加载数据入口
		getNewsType();
		
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时执行加载数据或者进度条等
			Logger.dout(TAG + "isVisibleToUser");
		} else {
			// 不可见时不执行操作
			Logger.dout(TAG + "unVisibleToUser");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Logger.dout(TAG + "onCreateView");
		View view = inflater.inflate(R.layout.root_layout, null);
		mContentView = (FrameLayout) view.findViewById(R.id.mainView);
		addListener();
		getIntroduceList();
		return view;
	}

	/**
	 * 下滑上面三个按钮
	 */
	public void initHome() {
		/*
		 * cv_content.scrollTo(0, 0);
		 * ig_ememed_doctor.setBackgroundResource(R.drawable
		 * .btn_ememed_doctor_show);
		 */
	}

	private void addListener() {
		ll_view_content = (PullToRefreshLayout) LayoutInflater.from(activity)
				.inflate(R.layout.fragment_home, null);
		bt_keyword_search = (ImageView) ll_view_content
				.findViewById(R.id.bt_keyword_search);
		bt_keyword_search.setOnClickListener(this);
		ll_txt_top = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_txt_top);
		ll_txt_top.setOnClickListener(this);
		// questions_layout = (RelativeLayout) ll_view_content
		// .findViewById(R.id.questions_layout);
		// questions_layout.setOnClickListener(this);
		ll_phone_top = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_phone_top);
		ll_phone_top.setOnClickListener(this);
		ll_guahao_top = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_guahao_top);
		ll_guahao_top.setOnClickListener(this);
		ll_search_box = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_search_box);
		// ll_search_box.setOnClickListener(this);
		// ll_shangmen_top = (LinearLayout) ll_view_content
		// .findViewById(R.id.ll_shangmen_top);
		// ll_shangmen_top.setOnClickListener(this);
		ll_private_top = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_private_top);
		ll_private_top.setOnClickListener(this);
		ll_zhuyuan_top = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_zhuyuan_top);
		ll_zhuyuan_top.setOnClickListener(this);
		// ll_notice_custom_top = (LinearLayout) ll_view_content
		// .findViewById(R.id.ll_notice_custom_top);
		// ll_notice_custom_top.setOnClickListener(this);
		ll_content = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_content);
		ll_content.setOnClickListener(this);
		
		// ll_notice_custom_top = (LinearLayout) ll_view_content
		// .findViewById(R.id.ll_notice_custom_top);
		// ll_notice_custom_top.setOnClickListener(this);
		ll_yongyao_top = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_yongyao_top);
		ll_yongyao_top.setOnClickListener(this);

		ll_content = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_content);
		ll_content.setOnClickListener(this);

		ll_free_question = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_free_question);
		ll_free_question.setOnClickListener(this);

		/*
		 * cv_content = (ScrollView)
		 * ll_view_content.findViewById(R.id.cv_content); ll_content =
		 * (LinearLayout) ll_view_content.findViewById(R.id.ll_content);
		 * ll_content.getViewTreeObserver().addOnGlobalLayoutListener(new
		 * OnGlobalLayoutListener() {
		 * 
		 * @Override public void onGlobalLayout() { // TODO Auto-generated
		 * method stub if (ll_content.getHeight() > 0) {
		 * ll_content.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		 * int temp = activity.Height + Util.dip2px(activity, 48); if (temp <=
		 * ll_content.getHeight()) {
		 * 
		 * } else { LayoutParams layoutParams = (LayoutParams)
		 * ll_content.getLayoutParams(); if (layoutParams == null) layoutParams
		 * = new LayoutParams(LayoutParams.MATCH_PARENT, temp);
		 * layoutParams.height = temp; //
		 * activity.showToast(layoutParams.height+"");
		 * ll_content.setLayoutParams(layoutParams); } } } });
		 */

		/* 以下是新主页顶部三个按钮的响应完成后取消注释 */
		/*
		 * ll_about_us = (LinearLayout)
		 * ll_view_content.findViewById(R.id.ll_about_us);
		 * ll_about_us.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { if
		 * (event.getAction() == MotionEvent.ACTION_DOWN) {
		 * ig_ememed_doctor.setPressed(false); } return false; } });
		 * ll_about_us.setOnClickListener(this); ll_ememed_doctor =
		 * (LinearLayout) ll_view_content.findViewById(R.id.ll_ememed_doctor);
		 * ig_ememed_doctor = (ImageView)
		 * ll_view_content.findViewById(R.id.ig_ememed_doctor);
		 * ll_ememed_doctor.setOnClickListener(this);
		 */
		ll_qingsong_guahao = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_qingsong_guahao);
		/*
		 * ll_qingsong_guahao.setOnTouchListener(new OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { if
		 * (event.getAction() == MotionEvent.ACTION_DOWN) {
		 * ig_ememed_doctor.setPressed(false); } return false; } });
		 */
		ll_qingsong_guahao.setOnClickListener(this);
		/*
		 * ll_switch_top = (LinearLayout)
		 * ll_view_content.findViewById(R.id.ll_switch_top);
		 */
		fl_home_ads = (FrameLayout) ll_view_content
				.findViewById(R.id.fl_home_ads);

		vp_ad = (ViewPager) ll_view_content.findViewById(R.id.vp_ad);
		adapterCycle = new AdapterCycle(null, null);
		vp_ad.setAdapter(adapterCycle);
		ll_dot_content = (LinearLayout) ll_view_content
				.findViewById(R.id.ll_dot_content);
		getAdsFromDB();
		initViewPager();
		initDotView();
		vp_ad.setOnPageChangeListener(new MyPageChangeListener());

		et_disease_keyword = (TextView) ll_view_content
				.findViewById(R.id.et_disease_keyword);
		et_disease_keyword.setHintTextColor(0xffafafaf);
		// et_disease_keyword.setOnClickListener(this);
		// et_disease_keyword.addTextChangedListener(new TextWatcher() {
		//
		// @Override
		// public void onTextChanged(CharSequence s, int start, int before, int
		// count) {
		// if (count == 0 && start == 0) {
		// bt_keyword_search.setPressed(false);
		// }
		// }
		//
		// @Override
		// public void beforeTextChanged(CharSequence s, int start, int count,
		// int after) {
		// bt_keyword_search.setPressed(true);
		// }
		//
		// @Override
		// public void afterTextChanged(Editable s) {
		//
		// }
		// });
		// et_disease_keyword.setOnEditorActionListener(new
		// TextView.OnEditorActionListener() {
		//
		// @Override
		// public boolean onEditorAction(TextView v, int actionId, KeyEvent
		// event) {
		//
		// if (actionId == EditorInfo.IME_ACTION_SEND || (event != null &&
		// event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
		// if
		// (TextUtils.isEmpty(et_disease_keyword.getText().toString().trim())) {
		// activity.showToast("请输入关键字");
		// } else {
		// Intent intent = new Intent(activity, DoctorSearchActivity.class);
		// intent.putExtra("keyword",
		// et_disease_keyword.getText().toString().trim());
		// et_disease_keyword.setText("");
		// startActivity(intent);
		// }
		// return true;
		// }
		//
		// return false;
		// }
		//
		// });
		et_disease_keyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (count == 0 && start == 0) {
					bt_keyword_search.setPressed(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				bt_keyword_search.setPressed(true);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		/*
		 * et_disease_keyword.setOnEditorActionListener(new
		 * TextView.OnEditorActionListener() {
		 * 
		 * @Override public boolean onEditorAction(TextView v, int actionId,
		 * KeyEvent event) {
		 * 
		 * if (actionId == EditorInfo.IME_ACTION_SEND || (event != null &&
		 * event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) { if
		 * (TextUtils.isEmpty(et_disease_keyword.getText().toString().trim())) {
		 * activity.showToast("请输入关键字"); } else { Intent intent = new
		 * Intent(activity, DoctorSearchActivity.class);
		 * intent.putExtra("keyword",
		 * et_disease_keyword.getText().toString().trim());
		 * et_disease_keyword.setText(""); startActivity(intent); } return true;
		 * }
		 * 
		 * return false; }
		 * 
		 * });
		 */

		gestureDetector = new GestureDetector(activity,
				new MainGestureListener());

		// cv_content.setOnTouchListener(new OnTouchListener() {
		//
		// @Override
		// public boolean onTouch(View v, MotionEvent event) {
		// return gestureDetector.onTouchEvent(event);
		// }
		// });

		mContentView.addView(ll_view_content);
	}

	@Override
	public void onResume() {
		super.onResume();
		Logger.dout(TAG + "onResume");
		initHome();

		if (null == serviceContent)
			getServiceListFromNet();
		/* ig_ememed_doctor.setPressed(true); */
	}

	@Override
	public void onPause() {
		/* ig_ememed_doctor.setPressed(false); */
		super.onStop();
	}

	@Override
	public boolean handleMessage(Message msg) {

		switch (msg.what) {
		case 9999:
			vp_ad.setCurrentItem(vp_ad.getCurrentItem() + 1);
			break;
		case IResult.GET_HOME_ADS:
			HomeAdsInfo info = (HomeAdsInfo) msg.obj;
			if (info == null) {
				break;
			}
			if (info.getSuccess() == 1) {
				data = new ArrayList<AdsEntry>();
				// data.add(null);//数据项首位为null，表示守卫为本地广告
				List<AdsEntry> tempList = info.getData();
				if (tempList != null && tempList.size() != 0) {
					data.addAll(tempList);
				}
				if (data != null && data.size() > 0) {
					views = new ArrayList<View>();
					if (data.size() == 1) {
						initItem(0);
						vp_ad.setAdapter(adapterCycle);
					} else if (data.size() == 2 || data.size() == 3) {
						for (int i = 0; i < 2; i++) {
							for (int j = 0; j < data.size(); j++) {
								initItem(j);
							}
						}
					} else if (data.size() > 3) {
						for (int i = 0; i < data.size(); i++) {
							initItem(i);
						}
					}
					adapterCycle.setData(data, views);
					initDotView();
					if (data.size() > 1) {
						TimeSlide();
					}

					// //缓存保存
//					new Thread() {
//						public void run() {
//							table.clearTable();
//							table.saveAdsList(data);
//						};
//					}.start();

				}
			} else {
				activity.showToast(info.getErrormsg());
			}

			break;
		case IResult.GET_OTHER_SERVICE:
			ServiceListInfo serviceListInfo = (ServiceListInfo) msg.obj;
			List<ServiceListEntry> list = serviceListInfo.getData();
			serviceContent = new ArrayList<ServiceListEntry>(list);
			break;
		case IResult.GET_STATIC_VARIABLE:
			StaticVariableBean svb=(StaticVariableBean) msg.obj;
			List<VariableBean> lvb=svb.getData();
			for (int i = 0; i < lvb.size(); i++) {
				VariableBean vb=lvb.get(i);
				
				String value=vb.getSTITLE()+","+vb.getSVALUE();
				
				if(vb.getSKEY().equals("VAR_20002")){
					SharePrefUtil.putString(Conast.ORDER_CALL,value);
				}
				else if(vb.getSKEY().equals("VAR_20004")){
					SharePrefUtil.putString(Conast.ORDER_ADD_NULBER,value);
				}
				else if(vb.getSKEY().equals("VAR_20005")){
					SharePrefUtil.putString(Conast.ORDER_HOSPITAL,value);
				}
				else if(vb.getSKEY().equals("VAR_20006")){
					SharePrefUtil.putString(Conast.FREE_CHAR,value);
				}
				else if(vb.getSKEY().equals("VAR_20001")){
					SharePrefUtil.putString(Conast.TELETEXT_CONSULT,value);
				}
				else if(vb.getSKEY().equals("VAR_20003")){
					SharePrefUtil.putString(Conast.PEOPLE_DOCTOR,value);
				}
			}
			break;
		case EXEU_GET_DATA:
			NewsEntity entity = (NewsEntity) msg.obj;
			break;
		case EXEU_GET_DATA_ERROR:
			break;

		default:
			break;
		}
		return false;
	}

	@Override
	public void execute(int mes, Object obj) {
		switch (mes) {
		case EXEU_GET_DATA:
			getHomeNews();
			break;
		case EXEU_SYS_DATA:
			sysn_config();
			break;
		default:
			break;
		}
	}

	private void getHomeNews() {

		try {
			// Thread.sleep(5000);
			Message message = new Message();
			message.obj = null;
			message.what = EXEU_GET_DATA;
			handler.sendMessage(message);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// HashMap<String, String> params = new HashMap<String, String>();
		// params.put("some_key", "some_value");
		// MyApplication.volleyHttpClient.postWithParams("url",
		// NewsEntity.class, params, new Response.Listener() {
		// @Override
		// public void onResponse(Object response) {
		//
		// Message message = new Message();
		// message.obj = response;
		// message.what = EXEU_GET_DATA;
		// handler.sendMessage(message);
		//
		// }
		// }, new Response.ErrorListener() {
		// @Override
		// public void onErrorResponse(VolleyError error) {
		//
		// Message message = new Message();
		// message.obj = error.getMessage();
		// message.what = EXEU_GET_DATA_ERROR;
		// handler.sendMessage(message);
		// }
		//
		// }
		// );
	}

	// 获取资讯类型列表
	private void getNewsType() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", "user");// type --> 用户类型 doctor|user
		params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));

		MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_news_type,
				NewsTypeInfo.class, params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {// 加载数据成功
						if (null != response) {
							NewsTypeInfo respEntry = (NewsTypeInfo) response;
							if (respEntry.getSuccess() == 1) {
								final List<NewsTypeEntry> typsList = respEntry
										.getData();
								// 保存资讯目录
								new Thread() {
									@Override
									public void run() {
										// TODO Auto-generated method stub
										super.run();
										NewsTypeTable table = new NewsTypeTable();
										table.clearTable();
										for (int i = 0; i < typsList.size(); i++) {
											table.saveNewsType(typsList.get(i));
										}
									}

								}.start();

							}
						}
					}
				}, new Response.ErrorListener() {// 加载数据失败
					@Override
					public void onErrorResponse(VolleyError error) {

					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		// if(v.getId()==R.id.et_disease_keyword){
		// intent = new Intent(activity, SearchResourceActivity.class);
		// startActivity(intent);
		// }
		if (et_disease_keyword.isFocused()
				&& activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
			bt_keyword_search.setPressed(true);
		} else {
			bt_keyword_search.setPressed(false);
		}

		if (v.getId() == R.id.bt_keyword_search) {

			if (TextUtils.isEmpty(et_disease_keyword.getText().toString()
					.trim())) {
				activity.showToast("请输入关键字");
			} else {
				intent = new Intent(activity, DoctorSearchActivity.class);
				intent.putExtra("keyword", et_disease_keyword.getText()
						.toString().trim());
				// et_disease_keyword.setText("");
				startActivity(intent);
			}

		} else if (v.getId() == R.id.ll_txt_top) {
			intent = new Intent(activity, ExpertSearchActivity.class);
			intent.putExtra("current_service", "1");
			intent.putExtra("current_name", "图文咨询");
			startActivity(intent);
		}
		// else if (v.getId() == R.id.questions_layout) {//免费提问
		// intent = new Intent(activity, PutQuestionsActivity.class);
		// startActivity(intent);
		// }
		else if (v.getId() == R.id.ll_phone_top) {
			intent = new Intent(activity, ExpertSearchActivity.class);
			intent.putExtra("current_service", "2");
			intent.putExtra("current_name", "预约通话");
			startActivity(intent);
		} else if (v.getId() == R.id.ll_guahao_top) {
			intent = new Intent(activity, ExpertSearchActivity.class);
			intent.putExtra("current_service", "3");
			intent.putExtra("current_name", "预约加号");
			startActivity(intent);
		}
		// else if (v.getId() == R.id.ll_shangmen_top) {
		// intent = new Intent(activity, ExpertSearchActivity.class);
		// intent.putExtra("current_service", "4");
		// intent.putExtra("current_name", "上门会诊");
		// startActivity(intent);
		// }
		else if (v.getId() == R.id.ll_zhuyuan_top) {
			intent = new Intent(activity, ExpertSearchActivity.class);
			intent.putExtra("current_service", "14");
			intent.putExtra("current_name", "预约住院");
			startActivity(intent);
		} else if (v.getId() == R.id.ll_private_top) {
			intent = new Intent(activity, ExpertSearchActivity.class);
			intent.putExtra("current_service", "15");
			intent.putExtra("current_name", "签约私人医生");
			startActivity(intent);
		}
		// else if (v.getId() == R.id.ll_notice_custom_top) {
		//
		// intent = new Intent(activity, ExpertSearchActivity.class);
		// intent.putExtra("current_service", "16");
		// intent.putExtra("current_name", "其他服务");
		// startActivity(intent);
		// }
		else if (v.getId() == R.id.ll_search_box) {
			intent = new Intent(activity, SearchResourceActivity.class);
			startActivity(intent);
		}

		else if (v.getId() == R.id.ll_free_question) {

			if (SharePrefUtil.getBoolean(Conast.LOGIN)) {
				intent = new Intent(activity, PutQuestionsActivity.class);
				startActivity(intent);
			} else {
				intent = new Intent(activity, LoginActivity.class);
				intent.putExtra("origin",
						PutQuestionsActivity.class.getSimpleName());
				intent.putExtra("current_service", "17");
				startActivity(intent);
			}

		}

		/*
		 * else if (v.getId() == R.id.ll_about_us) { intent = new
		 * Intent(activity, AboutUsActivity.class); startActivity(intent); }
		 */
		else if (v.getId() == R.id.ll_qingsong_guahao) {
			intent = new Intent(activity, GuahaoActivity.class);
			startActivity(intent);
		} else if (v.getId() == R.id.ll_yongyao_top) {
			intent = new Intent(activity, WebViewThirdServiceActivity.class);
			// intent.putExtra("url", "http://10.18.0.26:80/splash?r=/drug");
			// intent.putExtra("url", "http://plus.ememed.net/splash?r=/drug");
			// intent.putExtra("url", "http://plus.ememed.net/drug");
//			intent.putExtra("url", "http://172.16.0.63/drug3/");
			intent.putExtra("url", "http://plus.ememed.net/drug");//正式服务器地址
//			intent.putExtra("url", "http://plus.ememed.net/drug");//正式服务器地址
//			intent.putExtra("url", "http://172.16.0.63/drug3/");//内网测试服务器
			intent.putExtra("url", HttpUtil.DRUG_URL);
			intent.putExtra("ServiceList", serviceContent);
			if (UserPreferenceWrapper.isLogin()) 
				intent.putExtra("accesskey", UserPreferenceWrapper.getAccessKey());
			startActivity(intent);
		}

	}

	/*
	 * if (v.getId() == R.id.ll_ememed_doctor) {
	 * 
	 * cv_content.scrollBy(0, Util.dip2px(activity, 113));
	 * ig_ememed_doctor.setBackgroundResource
	 * (R.drawable.btn_ememed_doctor_show); } else {
	 * ig_ememed_doctor.setBackgroundResource
	 * (R.drawable.bg_home_frag_ememed_doctor); }
	 */

	/**
	 * 同步筛选医生基本配置
	 */
	private void sysn_config() {

		try {
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("Channel", "android"));// 新增
			String content = HttpUtil.getString(HttpUtil.URI
					+ HttpUtil.sync_config, params, HttpUtil.POST);

			final String content_1 = TextUtil.substring(content, "{");
			if (content != null) {

				new Thread() {

					public void run() {

						try {

							JSONObject obj = new JSONObject(content_1);
							JSONObject resultObj = obj.getJSONObject("data");
							JSONObject roomsObj = resultObj
									.getJSONObject("rooms");
							ConfigTable configTable = new ConfigTable();
							configTable.clearTable();
							Iterator arrays = roomsObj.keys();
							String keyName = "";
							String keyValue = "";
							while (arrays.hasNext()) {
								keyName = arrays.next().toString();
								Logger.dout("keyName:" + keyName);
								JSONArray roomArray = roomsObj
										.getJSONArray(keyName);
								for (int i = 0; i < roomArray.length(); i++) {
									Logger.dout("keyValue:"
											+ roomArray.getString(i));
									keyValue = roomArray.getString(i);
									configTable.saveDepartment(keyName,
											keyValue);
								}
							}
							/***** 无耻的分割线 获取服务 ***/
							JSONObject positionObj = resultObj
									.getJSONObject("service");
							if (null != positionObj) {
								ServiceConfigTable serviceTable = new ServiceConfigTable();
								serviceTable.clearTable();
								Iterator serviceArrays = positionObj.keys();
								String servicekeyValue = "";
								String servicekeyName = "";
								while (serviceArrays.hasNext()) {
									Logger.dout("servicekeyValue:"
											+ servicekeyValue);
									Logger.dout("servicekeyName:"
											+ servicekeyName);
									servicekeyName = serviceArrays.next()
											.toString();
									servicekeyValue = positionObj
											.getString(servicekeyName);
									serviceTable.savePositionName(
											servicekeyName, servicekeyValue);
								}
							}
							/***** 无耻的分割线 获取排序列表 ***/
							JSONObject orderObj = resultObj
									.getJSONObject("orderby");
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

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					};

				}.start();

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 广告位适配器
	 * 
	 * @author huangjk
	 * 
	 */
	public class AdapterCycle extends PagerAdapter {
		List<AdsEntry> data;
		List<View> views;

		public void setData(List<AdsEntry> data, List<View> views) {
			if (data == null) {
				data = new ArrayList<AdsEntry>();
			}
			if (views == null) {
				views = new ArrayList<View>();
			}
			this.data = data;
			this.views = views;
			notifyDataSetChanged();
		}

		public AdapterCycle(List<AdsEntry> data, List<View> views) {
			if (data == null) {
				data = new ArrayList<AdsEntry>();
			}
			if (views == null) {
				views = new ArrayList<View>();
			}
			this.data = data;
			this.views = views;
		}

		@Override
		public int getCount() {
			if (data.size() <= 1) {
				return views.size();
			} else {
				return Integer.MAX_VALUE;
			}

		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (views == null || views.size() == 0) {
				return;
			}
			int posi = 0;
			if (data.size() <= 1) {
				posi = position;
			} else {
				posi = position % views.size();
			}

			View view = views.get(posi);
			container.removeView(view);
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (views == null || views.size() == 0) {
				return null;
			}
			int posi = 0;
			if (data.size() <= 1) {
				posi = position;
			} else {
				posi = position % views.size();
			}
			container.addView(views.get(posi));
			return views.get(posi);
		}
	}

	private void initViewPager() {
		new Thread() {
			public void run() {
				String content = null;
				try {

					content = HttpUtil.getString(HttpUtil.GET_HOME_ADS, null,
							HttpUtil.POST);
					content = TextUtil.substring(content, "{");
					
					System.out.println("广告内容content = "+content);
					
					Gson gson = new Gson();
					HomeAdsInfo info = gson
							.fromJson(content, HomeAdsInfo.class);
					Message msg = Message.obtain();
					msg.what = IResult.GET_HOME_ADS;
					msg.obj = info;
					handler.sendMessage(msg);

				} catch (IOException e) {
					Message message = handler.obtainMessage();
					message.obj = e.getMessage();
					message.what = IResult.DATA_ERROR;
					handler.sendMessage(message);
				}
			};
		}.start();
	}

	private void getAdsFromDB() {
		data = new ArrayList<AdsEntry>();
		// data.add(null);//数据项首位为null，表示守卫为本地广告
		List<AdsEntry> tempList = table.getAdsList();
		if (tempList != null && tempList.size() != 0) {
			data.addAll(tempList);
		}
		if (data != null && !data.isEmpty()) {
			if (data.size() == 1) {
				initItem(0);

				vp_ad.setAdapter(adapterCycle);
			} else if (data.size() == 2 || data.size() == 3) {
				for (int i = 0; i < 2; i++) {
					for (int j = 0; j < data.size(); j++) {
						initItem(j);
					}
				}
			} else if (data.size() > 3) {
				for (int i = 0; i < data.size(); i++) {
					initItem(i);
				}
			}
			adapterCycle.setData(data, views);
			if (data.size() > 1) {
				TimeSlide();
			}
		}
	}

	private void initItem(int i) {

		final AdsEntry adsEntry = data.get(i);
		View view = View.inflate(activity.getApplicationContext(),
				R.layout.item_ads, null);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(activity, WebViewActivity.class);
				i.putExtra("url", adsEntry.getURL());
				i.putExtra("title", adsEntry.getTITLE());
				startActivity(i);
			}
		});
		ImageView iv = (ImageView) view.findViewById(R.id.iv);
		activity.imageLoader.displayImage(adsEntry.getTHUMB(), iv,
				Util.getOptions_pic(), new ImageLoadingListener() {

					@Override
					public void onLoadingStarted(String arg0, View arg1) {
					}

					@Override
					public void onLoadingFailed(String arg0, View arg1,
							FailReason arg2) {

					}

					@Override
					public void onLoadingComplete(String arg0, View arg1,
							Bitmap arg2) {
					}

					@Override
					public void onLoadingCancelled(String arg0, View arg1) {

					}
				});
		views.add(view);
		// }
	}

	Timer t;

	private void TimeSlide() {
		if (t != null)
			t.cancel();
		t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				handler.sendEmptyMessage(9999);
			}
		}, 5000, 5000);
	}

	private int oldPosition = 0;
	private LinearLayout ll_ememed_doctor2;
	private LinearLayout ll_content;

	private class MyPageChangeListener implements OnPageChangeListener {

		/**
		 * This method will be invoked when a new page becomes selected.
		 * position: Position index of the new selected page.
		 */
		public void onPageSelected(int position) {
			if (data == null || data.size() == 0) {
				return;
			}
			int posi = position;
			if (data.size() == 1) {
				posi = position;
			} else {
				posi = position % data.size();
			}
			dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
			dots.get(posi).setBackgroundResource(R.drawable.dot_focused);
			oldPosition = posi;
		}

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	private void initDotView() {
		if (data != null && data.size() > 0) {
			ll_dot_content.removeAllViews();
			dots = new ArrayList<View>();
			for (int i = 0; i < data.size(); i++) {
				View dot_item = LayoutInflater.from(activity).inflate(
						R.layout.v_dot_news_pic, null);
				LinearLayout.LayoutParams dot_params = new LinearLayout.LayoutParams(
						40, 6);// 设置
								// dots
								// 的长宽
				dot_params.setMargins(2, 0, 2, 0);// 设置 dots 左右之间的距离
				dot_item.setLayoutParams(dot_params);
				dots.add(dot_item);
				ll_dot_content.addView(dot_item);
			}
			dots.get(0).setBackgroundResource(R.drawable.dot_focused);// 默认
																		// dots
																		// 初始颜色
		}
	}

	private class MainGestureListener extends SimpleOnGestureListener {

		private LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT, Util.dip2px(activity,
						113));

		private int totalWidth = -1;
		private int totalHeight = -1;
		private int viewWidth = -1;
		private int viewHeight = -1;

		@Override
		public boolean onScroll(MotionEvent e1, MotionEvent e2,
				float distanceX, float distanceY) {
			return super.onScroll(e1, e2, distanceX, distanceY);
		}

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			// Log.d("chenhj", velocityY+"");
			// 动态计算view大小，用于设置滑动
			// if(totalWidth < 0 || totalWidth < 0) {
			// totalWidth = ll_view_content.getWidth();
			// totalHeight = ll_view_content.getHeight();
			// viewWidth = ll_switch_top.getWidth();
			// viewHeight = ll_shangmen_top.getHeight();
			// }
			// 设置下拉
			if (velocityY > 0.0f) {
				if (alphaShow == null) {
					alphaShow = new AlphaAnimation(0, 1);
					alphaShow.setDuration(750);
				}
				if (ll_switch_top.getVisibility() != View.VISIBLE) {
					ll_switch_top.clearAnimation();
					ll_switch_top.startAnimation(alphaShow);
					margin.setMargins(0, Util.dip2px(activity, 8), 0, 0);
					fl_home_ads.setLayoutParams(margin);
					ll_switch_top.setVisibility(View.VISIBLE);
				}
				return true;
			} else if (velocityY < 0.0f) { // 设置上拉
				if (alphaDisapear == null) {
					alphaDisapear = new AlphaAnimation(1, 0);
					alphaDisapear.setDuration(1000);
				}
				if (ll_switch_top.getVisibility() != View.GONE) {
					ll_switch_top.clearAnimation();
					ll_switch_top.startAnimation(alphaDisapear);
					margin.setMargins(0, 0, 0, 0);
					fl_home_ads.setLayoutParams(margin);
					ll_switch_top.setVisibility(View.GONE);
				}
				return true;
			} else {
				return false;
			}
		}
	}
	
	private void getIntroduceList(){
		if (!NetWorkUtils.detect(activity)) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
			return;
		}
		activity.loading(null);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
		params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("utype", "user");
		params.put("appversion", AppUtils.getVersionName(activity));
		params.put("channel", "android");
		System.out.println("token--"+SharePrefUtil.getString(Conast.ACCESS_TOKEN)+"--memberid--"+SharePrefUtil.getString(Conast.MEMBER_ID)+
				"--utype--"+"user"+"--appversion--"+AppUtils.getVersionName(activity)+"--channel--"+"android");
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_static_variable, StaticVariableBean.class, params,new Listener() {

			@Override
			public void onResponse(Object response) {
				// TODO Auto-generated method stub
				Message msg = handler
						.obtainMessage();
				msg.obj = response;
				msg.what = IResult.GET_STATIC_VARIABLE;
				handler.sendMessage(msg);
			}
		},new ErrorListener() {

			@Override
			public void onErrorResponse(VolleyError response) {
				// TODO Auto-generated method stub
				Message msg = handler
						.obtainMessage();
				msg.obj = response;
				msg.what = IResult.DATA_ERROR;
				handler.sendMessage(msg);
			}
		});
	}

	private void getServiceListFromNet() {
		if (NetWorkUtils.detect(activity)) {
//			activity.loading(null);

			try {
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
				params.put("token",
						SharePrefUtil.getString(Conast.ACCESS_TOKEN));
				params.put("appversion", AppUtils.getVersionName(activity));
				MyApplication.volleyHttpClient.postWithParams(
						HttpUtil.get_other_service_list, ServiceListInfo.class,
						params, new Response.Listener() {
							@Override
							public void onResponse(Object response) {
								/*
								 * if (!isRefresh) { activity.destroyDialog(); }
								 */
								activity.destroyDialog();
								if (null != response) {
									ServiceListInfo respEntry = (ServiceListInfo) response;
									if (respEntry.getSuccess() == 1) {
										List<ServiceListEntry> serList = respEntry
												.getData();

										// 本地服务为空时直接网络加载 并保存到数据库
										if (null == serviceContent
												|| serviceContent.isEmpty()) {
											/*
											 * updatePos = new
											 * boolean[serList.size()]; int i =
											 * 0; for (ServiceListEntry serEntry
											 * : serList) {
											 * table.saveServiceContent
											 * (serEntry, doctorID);
											 * updatePos[i] = true; i++; }
											 * getImage();
											 */
											Message msg = handler
													.obtainMessage();
											msg.obj = response;
											msg.what = IResult.GET_OTHER_SERVICE;
											handler.sendMessage(msg);

										} else {// 有数据项需要检查本地和服务器之间的版本号，用来设置红点提示
											/*
											 * updatePos = new
											 * boolean[serList.size()]; for (int
											 * i = 0; i < serList.size(); i++) {
											 * ServiceListEntry serEntry =
											 * serList.get(i); String
											 * versionInNet =
											 * serEntry.getVERSION() == null ?
											 * "0" : serEntry.getVERSION();
											 * String versionInDB =
											 * table.getServiceItemVersion
											 * (doctorID, serEntry.getID());
											 * boolean serviceItemOnClick =
											 * table
											 * .getServiceItemOnClick(doctorID,
											 * serEntry.getID());//
											 * true为未点击，false为已点击 // 本地有该项数据 if
											 * (serviceItemOnClick) {
											 * updatePos[i] = true; } else { if
											 * (null != versionInDB) { //
											 * version需要更新时 try { if
											 * (Double.parseDouble(versionInNet)
											 * >
											 * Double.parseDouble(versionInDB))
											 * {
											 * 
											 * updatePos[i] = true;
											 * 
											 * } else { updatePos[i] = false;
											 * 
											 * } } catch (NumberFormatException
											 * e) { updatePos[i] = false;
											 * e.printStackTrace(); } catch
											 * (Exception e) {
											 * e.printStackTrace(); } } else {
											 * // 无该项数据时 updatePos[i] = false; }
											 * }
											 * 
											 * } updatePos[0] = false;
											 * 
											 * // table.clearTable();
											 * getImage(); List<String> ids =
											 * new ArrayList<String>(); for
											 * (ServiceListEntry serEntry :
											 * serList) {
											 * table.saveServiceContent
											 * (serEntry, doctorID);
											 * ids.add(serEntry.getID()); } //
											 * 清理关闭的第三方服务 List<String>
											 * serviceIds =
											 * table.getServiceIds(); if
											 * (serviceIds != null &&
											 * serviceIds.size() > 0) { for
											 * (String id : serviceIds) {
											 * boolean contains =
											 * ids.contains(id); if (!contains)
											 * { table.clearItem(id); } } }
											 */

											// 更新UI
											Message msg = handler
													.obtainMessage();
											msg.obj = response;
											msg.what = IResult.GET_OTHER_SERVICE;
											handler.sendMessage(msg);
										}
									}// if(null != response)
								}// if
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								activity.destroyDialog();

								Message message = handler.obtainMessage();
								message.obj = error.getMessage();
								message.what = IResult.DATA_ERROR;
								handler.sendMessage(message);
							}
						});
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

}