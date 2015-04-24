package net.ememed.user2.fragment;


import java.util.HashMap;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.SearchResourceActivity;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SearchDoctorAll;
import net.ememed.user2.entity.SearchPersonEn;
import net.ememed.user2.fragment.adapter.DotorAtelierAdapter;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
/**
 * 
 * @author FXX 医生工作室
 */
public class DotorAtelierFragment extends Fragment implements Handler.Callback{

	private View rootView;
	private Handler mHandler;

	private SearchResourceActivity activity;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;

	private ListView listView;
	private DotorAtelierAdapter adapter;
	private TextView nodata_error;
	
	private SearchPersonEn spe=null;
	private String keyword;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity=(SearchResourceActivity) activity;
	}
	

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.from(getActivity()).inflate(
				R.layout.fragment_searchall, null);
		initview();
		
		
		return rootView;
	}
	
	
	
	private void initview() {
		// TODO Auto-generated method stub
		ll_net_unavailable = (LinearLayout) rootView
				.findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) rootView.findViewById(R.id.ll_empty);
		listView = (ListView) rootView.findViewById(R.id.listView);
		
	}

	@Override
	public boolean handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {

		case IResult.NET_ERROR:
			activity.destroyDialog();
			activity.showToast(IMessage.NET_ERROR);
			ll_empty.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			break;
		case IResult.DATA_ERROR:
			activity.destroyDialog();
			ll_net_unavailable.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		
		return false;
	}
	
	/**
	 * 搜索关键字
	 */
	public void searchPerson(String keyword) {
		this.keyword=keyword;
		if (NetWorkUtils.detect(getActivity())) {

			activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			if (!TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))
					|| SharePrefUtil.getBoolean(Conast.LOGIN)) {
				params.put("memberid",
						SharePrefUtil.getString(Conast.MEMBER_ID));
			}
			params.put("keyword", keyword);
			params.put("page", "2");
			
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.search_doctor_person, SearchPersonEn.class, params,
					new Listener() {

						public void onResponse(Object object) {
							spe = (SearchPersonEn) object;
							adapter=new DotorAtelierAdapter(getActivity(),spe.getData(),activity.imageLoader,activity.options);
							if (spe.getData() != null && spe.getData().size()!=0) {
								activity.destroyDialog();
								listView.setAdapter(adapter);
								
							} else {
								activity.destroyDialog();
								ll_empty.setVisibility(View.VISIBLE);
								listView.setVisibility(View.GONE);
							}
						}
					}, new ErrorListener() {

						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});

		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}

	}
	public void doClick(View view){
		if(view.getId()==R.id.ll_net_unavailable){
			searchPerson(keyword);
		}
	}
	
	public void setkeyWord(String keyword){
		this.keyword = keyword;
	}
}
