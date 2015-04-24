package net.ememed.user2.fragment;

import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.SearchResourceActivity;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SearchDoctorAll;
import net.ememed.user2.fragment.adapter.CheckHospitalAdapter;
import net.ememed.user2.fragment.adapter.CheckMedicalAdapter;
import net.ememed.user2.fragment.adapter.DotorAtelierAdapter;
import net.ememed.user2.fragment.adapter.RegistrationAdapter;
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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;

/**
 * 
 * @author FXX 搜索全部
 * 
 */

public class SearchAllFragment extends Fragment implements Handler.Callback,OnClickListener {

	private View rootView;
	private Handler mHandler;
	private SearchDoctorAll all=null;

	private SearchResourceActivity activity;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_search;

	private DotorAtelierAdapter adapter_person;
	private RegistrationAdapter	adapter_registration;
	private CheckHospitalAdapter adapter_hospital;
	private CheckMedicalAdapter adapter_hostory;
	
	private ListView listView_person;
	private ListView listView_registration;
	private ListView listView_hospital;
	private ListView listView_history;
	
	private String keyword;
	
	
	
	RelativeLayout search_all_person;
	RelativeLayout search_all_registration;
	RelativeLayout search_all_hospital;
	RelativeLayout search_all_history;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (SearchResourceActivity) activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mHandler = new Handler(this);
		rootView = inflater.from(getActivity()).inflate(
				R.layout.search_all, null);
		
		initView();
		return rootView;
	}
	

	public void initView() {
		ll_net_unavailable = (LinearLayout) rootView
				.findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout) rootView.findViewById(R.id.ll_empty);
		ll_search=(LinearLayout) rootView.findViewById(R.id.search_all_layout);
		ll_search.setVisibility(View.GONE);
		listView_person = (ListView) rootView.findViewById(R.id.list_person_info);
		listView_registration = (ListView) rootView.findViewById(R.id.list_registration_info);
		listView_hospital = (ListView) rootView.findViewById(R.id.list_hospital_info);
		listView_history = (ListView) rootView.findViewById(R.id.list_history_info);
		search_all_person = (RelativeLayout) rootView.findViewById(R.id.search_all_person);
		search_all_registration = (RelativeLayout) rootView.findViewById(R.id.search_all_registration);
		search_all_hospital = (RelativeLayout) rootView.findViewById(R.id.search_all_hospital);
		search_all_history = (RelativeLayout) rootView.findViewById(R.id.search_all_history);
		search_all_person.setOnClickListener(this);
		search_all_registration.setOnClickListener(this);
		search_all_hospital.setOnClickListener(this);
		search_all_history.setOnClickListener(this);
	}

	public boolean handleMessage(Message msg) {
		switch (msg.what) {

		case IResult.NET_ERROR:
			activity.destroyDialog();
			activity.showToast(IMessage.NET_ERROR);
			ll_net_unavailable.setVisibility(View.VISIBLE);
			ll_search.setVisibility(View.GONE);
			break;
		case IResult.DATA_ERROR:
			activity.destroyDialog();
			ll_empty.setVisibility(View.VISIBLE);
			ll_search.setVisibility(View.GONE);
			break;
		default:
			break;
		}
		return false;
	}

	/**
	 * 搜索关键字
	 */
	public void searchSoctorAll(String keyword) {
		
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
			
			System.out.println("params = "+params);
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.search_doctor_all, SearchDoctorAll.class, params,
					new Listener() {

						public void onResponse(Object object) {
							activity.destroyDialog();
							all = (SearchDoctorAll) object;
							adapter_person=new DotorAtelierAdapter(getActivity(), all.getSearch_person(),activity.imageLoader,activity.options);
							adapter_registration=new RegistrationAdapter(getActivity(),all.getSearch_guahao(),activity.imageLoader,activity.options);
							adapter_hospital=new CheckHospitalAdapter(getActivity(),all.getSearch_hospital(),activity.imageLoader,activity.options);
							adapter_hostory=new CheckMedicalAdapter(getActivity(),all.getSearch_txtconsult(),activity.imageLoader,activity.options);
							if (all != null) {
								ll_search.setVisibility(View.VISIBLE);
								ll_net_unavailable.setVisibility(View.GONE);
								if(all.getSearch_person()!=null && all.getSearch_person().size()!=0){
									listView_person.setAdapter(adapter_person);
								}
								
								if(all.getSearch_guahao()!=null && all.getSearch_guahao().size()!=0){
									listView_registration.setAdapter(adapter_registration);
								}
								
								if(all.getSearch_guahao()!=null && all.getSearch_guahao().size()!=0){
									listView_hospital.setAdapter(adapter_hospital);								
								}
								
								if(all.getSearch_guahao()!=null && all.getSearch_guahao().size()!=0){
									listView_history.setAdapter(adapter_hostory);
								}
							} else {
								ll_net_unavailable.setVisibility(View.VISIBLE);
								ll_search.setVisibility(View.GONE);
							}

						}
					}, new ErrorListener() {

						public void onErrorResponse(VolleyError error) {
							activity.showToast(error.getMessage());
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
	
	public void setkeyWord(String keyword){
		this.keyword = keyword;
	}
	
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.ll_net_unavailable:
			searchSoctorAll(keyword);
			break;
		case R.id.search_all_person:
			activity.view_pager.setCurrentItem(1);
			break;
		case R.id.search_all_registration:
			activity.view_pager.setCurrentItem(2);
			break;
		case R.id.search_all_hospital:
			activity.view_pager.setCurrentItem(3);
			break;
		case R.id.search_all_history:
			activity.view_pager.setCurrentItem(4);
			break;

		default:
			break;
		}
	}
	public void doClick(View view){
		if(view.getId()==R.id.ll_net_unavailable){
			searchSoctorAll(keyword);
		}
	}
}
