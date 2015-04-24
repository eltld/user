package net.ememed.user2.activity;

import java.io.Serializable;
import java.lang.reflect.ReflectPermission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;

import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.GuahaoDoctorAdapter;
import net.ememed.user2.activity.adapter.SearchRecordsAdapter;
import net.ememed.user2.db.SearchRecordsDao;
import net.ememed.user2.entity.AreaGuahaoInfo;
import net.ememed.user2.entity.GuahaoDoctor;
import net.ememed.user2.entity.GuahaoDoctorInfo;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class GuaHaoSearchActivity extends BasicActivity {
	
	private String TAG = "chenhj, GuaHaoSearchActivity";
	
	private TextView tv_top_title;
	private EditText et_keyword;
	private RefreshListView lvCustomEvas;
	private LinearLayout ll_search_keyword;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;
	private ListView lv_search_records;
	private ImageView btn_search;
	
	private GuahaoDoctorAdapter adapter;
	
	//private String keyword;
	private int page = 1;
	private int totalpages = 1;
	private boolean isRefresh = true;
	
	private SearchRecordsAdapter recordAdapter;
	private ArrayList<String> records = new ArrayList<String>();
	private SearchRecordsDao dao = null;

	private String keyword;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_guahao_search);
		
		//keyword = getIntent().getStringExtra("keyword");
		EventBus.getDefault().register(this, GuahaoSuccessEvent.class);
		
		dao = new SearchRecordsDao(this);
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
		recordAdapter = new SearchRecordsAdapter(this, records);
		
		tv_top_title = (TextView) findViewById(R.id.top_title);
		tv_top_title.setText("免费挂号");

		et_keyword = (EditText) findViewById(R.id.et_search_keyword);
		
		//et_keyword.setText(keyword);
		et_keyword.setHintTextColor(0xffafafaf);
	/*	et_keyword.setFocusable(true);   
		et_keyword.setFocusableInTouchMode(true);   
		et_keyword.requestFocus(); */
		
		lv_search_records = (ListView) findViewById(R.id.lv_search_records);
		lv_search_records.setAdapter(recordAdapter);
		lv_search_records.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String str = records.get(position);
				et_keyword.setText(str);
				saveSearchRecords(str);
				lv_search_records.setVisibility(View.GONE);
				keyword = str;
				refresh();
			}
		});
		
		
		btn_search = (ImageView) findViewById(R.id.bt_search__keyword);
		
		ll_search_keyword = (LinearLayout) findViewById(R.id.ll_search_keyword);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		
		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_guahao_doctor);
		adapter = new GuahaoDoctorAdapter(this, null, GuahaoDoctorAdapter.hosp_room);
		lvCustomEvas.setAdapter(adapter);
		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				GuahaoDoctorInfo info = (GuahaoDoctorInfo) adapter.getItem(arg2 - 1);
				Intent intent = new Intent(GuaHaoSearchActivity.this, GuahaoDoctorInfoActivity.class);
				intent.putExtra("GuahaoDoctorInfo", (Serializable)info);
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
		
	/*	et_keyword.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				int i = lv_search_records.getVisibility();
				if(lv_search_records.getVisibility() == View.GONE){
					lv_search_records.setVisibility(View.VISIBLE);
					records = (ArrayList<String>) dao.getSearchRecords();
					recordAdapter.change(records);
				}
				return true;
			}
		});*/
		
		et_keyword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(lv_search_records.getVisibility() == View.GONE){
					lv_search_records.setVisibility(View.VISIBLE);
					records = (ArrayList<String>) dao.getSearchRecords();
					recordAdapter.change(records);
				}else{
					lv_search_records.setVisibility(View.GONE);
				}
				
				ll_empty.setVisibility(View.GONE);
				ll_net_unavailable.setVisibility(View.GONE);
			}
		});
		
		et_keyword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
					
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					records = (ArrayList<String>) dao.getSearchRecords();
					recordAdapter.change(records);
					lv_search_records.setVisibility(View.VISIBLE);
				}else{
					lv_search_records.setVisibility(View.GONE);
				}
			}
		});
		
		et_keyword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count == 0 && start == 0) {
					btn_search.setPressed(false);
					lv_search_records.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				//btn_search.setPressed(true);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		//输入法回车键变搜索
		et_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
						
				if (actionId == EditorInfo.IME_ACTION_SEND || (event!=null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {      
					String tempKeyword = et_keyword.getText().toString().trim();
					if(TextUtils.isEmpty(tempKeyword)) {
						showToast("搜索关键字为空");
					}
					else {
						keyword = tempKeyword;
						refresh();
					}
					return true;             
				}
				
				return false;
			} 
		});
		
		//getDoctorList(page);
	}
	
	public void doClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back : //返回键
			finish();
			break;
			
		case R.id.bt_search__keyword : //搜索
			String tempKeyword = et_keyword.getText().toString().trim();
			if(TextUtils.isEmpty(tempKeyword)) {
				showToast("搜索关键字为空");
			}else {
				keyword = tempKeyword;
				saveSearchRecords(keyword);
				lv_search_records.setVisibility(View.GONE);
				ll_empty.setVisibility(View.GONE);
				refresh();
			}
			break;
		}
	}
	
	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		switch (msg.what) {
		case IResult.GET_GUAHAO_DOCTOR:
			GuahaoDoctor doctor = (GuahaoDoctor) msg.obj;
			if(doctor != null) {
				switch (doctor.getSuccess()) {
				case 1:
					totalpages = doctor.getPages();
					List<GuahaoDoctorInfo> data = doctor.getData();
					
					if(isRefresh) {//第一次加载或刷新操作
						lvCustomEvas.onRefreshComplete();
						if(data != null && data.size() > 0) {
							lvCustomEvas.setVisibility(View.VISIBLE);
							ll_empty.setVisibility(View.GONE);
							adapter.change(data);
						}
						else {
							ll_empty.setVisibility(View.VISIBLE);
							lvCustomEvas.setVisibility(View.GONE);
						}
					}
					else {//分页加载
						if(data != null && data.size() > 0) {
							adapter.add(data);
						}
						else {
							showToast("暂无更多数据");
						}
					}
					
					if (page < totalpages) {
						lvCustomEvas.onLoadMoreComplete(false);
					} else {
						lvCustomEvas.onLoadMoreComplete(true);
					}
						
					break;
	
				default:
					break;
				}
			}
			else {
				showToast(IMessage.DATA_ERROR);
			}
			break;
			
		case IResult.DATA_ERROR :
			showToast(IMessage.DATA_ERROR);
			break;
			
		case IResult.NET_ERROR :
			// showToast(IMessage.NET_ERROR);
			ll_net_unavailable.setVisibility(View.VISIBLE);
			ll_empty.setVisibility(View.GONE);
			lvCustomEvas.setVisibility(View.GONE);
			break;
			
		default:
			break;
		}
	}
	
	private void refresh() {
		isRefresh = true;
		page = 1;
		getDoctorList(page);
	}
	
	private void loadMore() {
		isRefresh = false;
		page++;
		getDoctorList(page);
	}
	
	private void getDoctorList(int page) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("keyword", keyword);
			params.put("page", page+"");
			
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_guahao_doctor, GuahaoDoctor.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = handler.obtainMessage();
					message.obj = response;
					message.what = IResult.GET_GUAHAO_DOCTOR;
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
	
	private void saveSearchRecords(String str) {
		//先删除，再插入，保证最新的搜索记录排在前面
		//先尝试删除
		try {
			dao.deleteSearchRecord(str);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.iout("sqlite删除搜索记录操作",e.getMessage());
		}
		
		//插入搜索记录
		try {
			dao.saveSearchRecord(str);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.iout("sqlite插入搜索记录操作",e.getMessage());
		}
	}

}
