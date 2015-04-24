package net.ememed.user2.baike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.RadioGroup.OnCheckedChangeListener;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.baike.adapter.RewardAdapter;
import net.ememed.user2.baike.entity.BaikeBountyEntry;
import net.ememed.user2.baike.entity.BaikeSaysListEntry;
import net.ememed.user2.baike.entity.DoctorBountyListEntry;
import net.ememed.user2.baike.entity.DoctorBountyListInfo;
import net.ememed.user2.baike.entity.SaysDetailEntry;
import net.ememed.user2.entity.CommonResponseEntity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListViewForScrollView;

public class RewardActivity extends BasicActivity{
	private String professional;
	private String avatar;
	private String doctor_name;
	private String doctor_id;
	private String says_id;
	private String bounty_num;
	
	private CircleImageView iv_userhead;
	private TextView tv_doctor_name;
	private TextView tv_professional;
	private RadioButton tv_circle1;
	private RadioButton tv_circle2;
	private RadioButton tv_circle3;
	private RadioButton tv_circle4;
	private RadioGroup radio_layout;
	
	private TextView tv_reward_num;
	
	private EditText et_other_money;
	private EditText et_content;
	private int money = 0;
	private String notes = "";
	private RefreshListViewForScrollView reward_list;
	private TextView title_reward_wall;
	private RewardAdapter adapter;
	private int page = 1;
	private List<DoctorBountyListInfo> bountys = null;
	private InnerReceiver receiver;
	private IntentFilter filter;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		
		setContentView(R.layout.activity_reward);
		avatar = getIntent().getStringExtra("avatar");
		doctor_name = getIntent().getStringExtra("real_name");
		professional = getIntent().getStringExtra("professional");
		doctor_id = getIntent().getStringExtra("doctor_id");
		
		says_id = getIntent().getStringExtra("says_id");
		bounty_num = getIntent().getStringExtra("bounty_num");
		bountys = new ArrayList<DoctorBountyListInfo>();
		
		receiver = new InnerReceiver();
		filter = new IntentFilter();
		filter.addAction(MyApplication.GET_BOUNTY_LIST);
		registerReceiver(receiver, filter);
	}
	
	@Override
	protected void setupView() {
		super.setupView();
		
		((TextView)findViewById(R.id.top_title)).setText("打赏");
		iv_userhead = (CircleImageView) findViewById(R.id.iv_userhead);
		tv_doctor_name = (TextView) findViewById(R.id.doctor_name);
		tv_professional = (TextView) findViewById(R.id.professional);
		
		imageLoader.displayImage(avatar, iv_userhead, Util.getOptions_avatar());
		tv_doctor_name.setText(doctor_name);
		tv_professional.setText(professional);
		
		tv_circle1 = (RadioButton) findViewById(R.id.circle_1);
		tv_circle2 = (RadioButton) findViewById(R.id.circle_2);
		tv_circle3 = (RadioButton) findViewById(R.id.circle_3);
		tv_circle4 = (RadioButton) findViewById(R.id.circle_4);
		
		et_other_money = (EditText) findViewById(R.id.et_other_money);
		et_content = (EditText) findViewById(R.id.et_content);
		
		tv_reward_num = (TextView) findViewById(R.id.tv_reward_num);
		tv_reward_num.setText(bounty_num);
		
		title_reward_wall = (TextView) findViewById(R.id.title_reward_wall);
		title_reward_wall.setText(doctor_name+"医生的打赏墙");
		
		reward_list = (RefreshListViewForScrollView) findViewById(R.id.reward_list);
		adapter = new RewardAdapter(null, this);
		reward_list.setAdapter(adapter);
		
		radio_layout = (RadioGroup) findViewById(R.id.radio_layout);
		radio_layout.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				switch (checkedId) {
				case R.id.circle_1:
					money  = 1;
					break;
				case R.id.circle_2:
					money  = 5;
					break;
				case R.id.circle_3:
					money  = 10;
					break;
				case R.id.circle_4:
					money  = 20;
					break;
				default:
					break;
				}
			}
		});
		
		getBountyList();
	}
	
	@Override
	protected void addListener() {
		super.addListener();
		
		reward_list.setOnLoadMoreListener(new IOnLoadMoreListener() {
			@Override
			public void OnLoadMore() {
				page += 1;
				getBountyList();
			}
		});
	}
	
	/** 新增打赏订单 */
	public void set_bounty() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", doctor_id);
			params.put("money", money+"");
			params.put("channel", "Android");
			params.put("app_version", PublicUtil.getVersionName(this));
			params.put("notes", notes);
			params.put("saysid", says_id);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_bounty, BaikeBountyEntry.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.SET_BOUNTY;
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
	 * 获取打赏列表
	 */
	public void getBountyList() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("doctorid", doctor_id);
			params.put("page", page+"");
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_doctor_bounty_list, DoctorBountyListEntry.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_BOUNTY_LIST;
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
		super.onResult(msg);
		try {
			destroyDialog();
			switch (msg.what) {
			case IResult.SET_BOUNTY:
				BaikeBountyEntry entry = (BaikeBountyEntry) msg.obj;
				if (null != entry) {
					if (1 == entry.getSuccess()) {
						if(!TextUtils.isEmpty(entry.getData().getORDERID())){
							Intent intent = new Intent(this, RewardPayActivity.class);
							intent.putExtra("doctor_name", doctor_name);
							intent.putExtra("doctor_id", doctor_id);
							intent.putExtra("money", money+"");
							intent.putExtra("order_id", entry.getData().getORDERID());
							startActivity(intent);
						} else {
							
						}
					} else {
						showToast(entry.getErrormsg());
					}
				}
				break;
			case IResult.GET_BOUNTY_LIST:
				DoctorBountyListEntry entry2 = (DoctorBountyListEntry) msg.obj;
				if(null != entry2){
					if(1 == entry2.getSuccess()){
						List<DoctorBountyListInfo> list = entry2.getData();
						if(1 == page){
							bountys.clear();
						}
						bountys.addAll(list);
						tv_reward_num.setText(entry2.getTotal_bounty_num()+"");
						adapter.change(bountys);
					}
					
					if(page < entry2.getPages()){
						reward_list.onLoadMoreComplete(false);
					} else {
						reward_list.onLoadMoreComplete(true);
					}
				}
				break;
			case IResult.NET_ERROR:
				showToast(getString(R.string.net_error));
				break;
			case IResult.DATA_ERROR:
				showToast("数据出错！");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void doClick(View view){
		
		switch(view.getId()){
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_next:
			goToNext();
			break;
		default:
			break;
		}
	}
	
	private void goToNext(){
		String num = et_other_money.getText().toString().trim();
		if(!TextUtils.isEmpty(num)){	
			if(Integer.parseInt(num) < 1){
				showToast("打赏金额必须为大于0的整数");
				return;
			} else {
				money = Integer.parseInt(num);
			}
		} else {
			if(0 == money){
				showToast("请选择或者输入要打赏的金额");
				return;
			}
		}
		
		notes = et_content.getText().toString().trim();
		
		set_bounty();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
	
	private class InnerReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent.getAction().equals(MyApplication.GET_BOUNTY_LIST)){
				getBountyList();
			}
		}
	}
}
