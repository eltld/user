package net.ememed.user2.activity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.VisitPersonAddEvent;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.SortAdapter;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.HospitalEntry;
import net.ememed.user2.entity.HospitalRoomInfo;
import net.ememed.user2.entity.HospitalsInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.RoomEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.CharacterParser;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PinyinComparator;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.SideBar;
import net.ememed.user2.widget.SideBar.OnTouchingLetterChangedListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class KeshiActivity extends BasicActivity {

	private HospitalEntry hospitalEntry;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	private SideBar sideBar;
	private RefreshListView lvCustomEvas;
	private TextView dialog;
	private SortAdapter adapter;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_content;
	private boolean isflag = false;
	//需求变更开关
	private boolean flag2 = true;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_keshi);
		hospitalEntry = (HospitalEntry) getIntent().getSerializableExtra("hosp_entry");
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		EventBus.getDefault().register(this,GuahaoSuccessEvent.class); 
		
	}
	public void onEvent(GuahaoSuccessEvent event) {
		// Logger.dout("register success");
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
		title.setText("选择科室");
		TextView tv_hospital_name = (TextView) findViewById(R.id.tv_hospital_name);
		tv_hospital_name.setText(hospitalEntry.getHOSPITALNAME());

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		if(flag2)
			sideBar.setVisibility(View.GONE);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		ll_net_unavailable = (LinearLayout) findViewById(R.id.ll_net_unavailable);
		adapter = new SortAdapter(this,null,flag2);
		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_contact_class);
		lvCustomEvas.setAdapter(adapter);
		getRoomsList(hospitalEntry.getHOSPITALID());
//		getRoomsList("9673762");
	}

	@Override
	protected void addListener() {
		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					lvCustomEvas.setSelection(position);
				}

			}
		});
		
		ll_empty.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getRoomsList(hospitalEntry.getHOSPITALID());
			}
		});
		
		ll_net_unavailable.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				isflag = true;
				setNetWork();
			}
		});
		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(KeshiActivity.this,GuahaoDoctorActivity.class);
				RoomEntry item = (RoomEntry) adapter.getItem(position-1);
				intent.putExtra("hosp_entry", hospitalEntry);
				intent.putExtra("hosp_room", item);
				startActivity(intent);
				
			}
		});

	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;

		default:
			break;
		}
	}
	@Override
	protected void onResume() {
		if(isflag){
			isflag = false;
			getRoomsList(hospitalEntry.getHOSPITALID());
		}
		super.onResume();
	}

	private void getRoomsList(String hospitalid) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			Map<String, String> params = new HashMap<String, String>();
			params.put("hospitalid", hospitalid);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.guahao_get_hospital_rooms, HospitalRoomInfo.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_ROOM_LIST;
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
		case IResult.DATA_ERROR:
			ll_empty.setVisibility(View.VISIBLE);
			ll_content.setVisibility(View.GONE);
			break;
		case IResult.GET_ROOM_LIST:
			HospitalRoomInfo info = (HospitalRoomInfo) msg.obj;
			if(info != null) {
				switch (info.getSuccess()) {
				case 1:
					List<RoomEntry> data = info.getData();
					if (data != null && data.size() > 0) {
						List<RoomEntry> filledData = filledData(data);
						adapter.updateListView(filledData);
					} else {
						ll_empty.setVisibility(View.VISIBLE);
						ll_content.setVisibility(View.GONE);
					}
					break;
				default:
					break;
				}
			}
			else {
				showToast(IMessage.ROOM_DATA_ERROR);
			}
			break;
		case IResult.NET_ERROR:
			// showToast(IMessage.NET_ERROR);
			ll_net_unavailable.setVisibility(View.VISIBLE);
			ll_empty.setVisibility(View.GONE);
			ll_content.setVisibility(View.GONE);
			break;
		
		default:
			break;
		}
	}
	

	/**
	 * 为ListView填充数据
	 * @param date
	 * @return
	 */
	private List<RoomEntry> filledData(List<RoomEntry> date){
		List<RoomEntry> data = new ArrayList<RoomEntry>();
		for (RoomEntry roomEntry : date) {
			 String pinyin = characterParser.getSelling(roomEntry.getROOMNAME());
			 if(TextUtils.isEmpty(pinyin)){
				 continue;
			 }
			 String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
				if(sortString.matches("[A-Z]")){
					roomEntry.setSortLetters(sortString.toUpperCase());
				}else{
					roomEntry.setSortLetters("#");
				}
				data.add(roomEntry);
		}
		if(!flag2)
			Collections.sort(data, pinyinComparator);
		return data;
	}
}
