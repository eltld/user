package net.ememed.user2.baike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.baike.adapter.SayAdapter;
import net.ememed.user2.baike.entity.BaikeSaysInfo;
import net.ememed.user2.baike.entity.BaikeSaysListEntry;
import net.ememed.user2.baike.entity.SaysDetailEntry;
import net.ememed.user2.entity.CommonResponseEntity;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import android.app.DownloadManager.Query;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SayActivity extends BasicActivity{
	private final int REQUEST_SAYS_DETAIL = 2;
	private RefreshListView say_list;
	private SayAdapter adapter;
	private LinearLayout ll_empty;
	//private DoctorEntry doctorEntry;
	private int page= 1;
	private boolean isRefresh = true;
	private String doctor_id;
	private String doctor_name;
	private List<BaikeSaysInfo> shuoshuoList = null;
	private int praise_position = -1;
	private String currentSaysId = null;
 
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);

		setContentView(R.layout.activity_say);
		//doctorEntry = (DoctorEntry) getIntent().getSerializableExtra("entry");
		doctor_id = getIntent().getStringExtra("doctor_id");
		doctor_name = getIntent().getStringExtra("doctor_name");
		shuoshuoList = new ArrayList<BaikeSaysInfo>();
	}

	@Override
	protected void setupView() {
		super.setupView();

		((TextView) findViewById(R.id.top_title)).setText(doctor_name+"医生的说说");
		
		say_list = (RefreshListView) findViewById(R.id.say_list);
		adapter = new SayAdapter(this, handler, shuoshuoList);
		say_list.setAdapter(adapter);
		
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		
		getSaysList();
	}

	@Override
	protected void addListener() {
		super.addListener();
		
		say_list.setOnRefreshListener(new IOnRefreshListener() {
			@Override
			public void OnRefresh() {
				page = 1;
				isRefresh = true;
				getSaysList();
			}
		});
		
		say_list.setOnLoadMoreListener(new IOnLoadMoreListener() {
			@Override
			public void OnLoadMore() {
				page += 1;
				isRefresh = false;
				getSaysList();
			}
		});
		
		say_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				BaikeSaysInfo info = (BaikeSaysInfo)parent.getAdapter().getItem(position);
				currentSaysId = info.getSAYSID();
				Intent intent = new Intent(SayActivity.this, SayDetailsActivity.class);
				intent.putExtra("says_id", info.getSAYSID());
				startActivityForResult(intent, REQUEST_SAYS_DETAIL);
			}
		});
	}
	
	/** 获取医生说说列表 */
	public void getSaysList() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("doctorid", doctor_id);
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("page", page+"");
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_says_list, BaikeSaysListEntry.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_SHUOSHUO_LIST;
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
	
	/** 获取说说详情 */
	/*public void getSayDetail(String says_id) {
		if (NetWorkUtils.detect(this)) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("saysid", says_id);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_says_detail, SaysDetailEntry.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.GET_SAYS_DETAIL;
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
	}*/

	
	private void givePraise(String says_id){
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("saysid", says_id);
		
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_praise, CommonResponseEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GIVE_PRAISE;
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
		say_list.onRefreshComplete();
		switch (msg.what) {
		case IResult.GET_SHUOSHUO_LIST:
			BaikeSaysListEntry entry= (BaikeSaysListEntry) msg.obj;
			if (1 == entry.getSuccess()) {
				if(entry.getData() != null && entry.getData().size() > 0){
					ll_empty.setVisibility(View.GONE);
					if(isRefresh){
						shuoshuoList.clear();
					}
					shuoshuoList.addAll(entry.getData());
					adapter.notifyDataSetChanged();
					
					if (page < entry.getPages()) {
						say_list.onLoadMoreComplete(false);
					} else {
						say_list.onLoadMoreComplete(true);
					}
				} else {
					ll_empty.setVisibility(View.VISIBLE);
				}
			}
			break;
		/*case IResult.GET_SAYS_DETAIL:
			SaysDetailEntry response = (SaysDetailEntry) msg.obj;
			if(null != response && 1 == response.getSuccess()){
				for(int i = 0 ; i < shuoshuoList.size(); i++){
					if(shuoshuoList.get(i).getSAYSID().equals(response.getData().getSAYSID())){
						String content_show = shuoshuoList.get(i).getCONTENT_SHOW();
						shuoshuoList.set(i, response.getData());
						shuoshuoList.get(i).setCONTENT_SHOW(content_show);
						break;
					}
				}
				adapter.notifyDataSetChanged();
			}
			break;*/
		case IResult.GIVE_PRAISE_INNER:
			int id= msg.arg1;
			praise_position  = msg.arg2;
			givePraise(""+id);
			break;
		case IResult.GIVE_PRAISE:
			destroyDialog();
			CommonResponseEntity response2 = (CommonResponseEntity)msg.obj;
			if (null != response2) {
				if (1 == response2.getSuccess()) {
					UserPreferenceWrapper.addMyPraiseCount();
					if(praise_position >= 0){
						int num = Integer.parseInt(shuoshuoList.get(praise_position).getPRAISE_NUM());
						shuoshuoList.get(praise_position).setPRAISE_NUM((num+1)+"");
						shuoshuoList.get(praise_position).setIS_PRAISED(true);
						adapter.notifyDataSetChanged();
					}
				} else {
					showToast(response2.getErrormsg());
				}
			}
			praise_position = -1;
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
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if(REQUEST_SAYS_DETAIL == requestCode){
			/*page = 1;
			isRefresh = true;
			getSaysList();*/
			
//			getSayDetail(currentSaysId);
			
			BaikeSaysInfo result = (BaikeSaysInfo) data.getSerializableExtra("result");//得到新Activity 关闭后返回的数据
			if(null != result){
				result.setHITS(Integer.parseInt(result.getHITS())+1+"");
				for(int i = 0 ; i < shuoshuoList.size(); i++){
					if(shuoshuoList.get(i).getSAYSID().equals(result.getSAYSID())){
						String content_show = shuoshuoList.get(i).getCONTENT_SHOW();
						String is_new = shuoshuoList.get(i).getIS_NEW();
						shuoshuoList.set(i, result);
						shuoshuoList.get(i).setCONTENT_SHOW(content_show);
						shuoshuoList.get(i).setIS_NEW(is_new);
						adapter.notifyDataSetChanged();
						break;
					}
				}
			}
		}
	}
	
	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}
}
