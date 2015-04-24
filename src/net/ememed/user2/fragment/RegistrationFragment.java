package net.ememed.user2.fragment;

import java.util.HashMap;

import javax.net.ssl.HandshakeCompletedListener;

import com.android.volley.VolleyError;
import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.SearchResourceActivity;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SearchDoctorAll;
import net.ememed.user2.entity.SearchGuahaoEn;
import net.ememed.user2.fragment.adapter.RegistrationAdapter;
import net.ememed.user2.fragment.adapter.DotorAtelierAdapter;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.app.Activity;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.os.*;

/**
 * 
 * @author FXX
 * 免费挂号
 *
 */

public class RegistrationFragment extends Fragment implements Handler.Callback{

	private View rootView;
	private SearchResourceActivity activity;
	
	private Handler mhandler;
	private LinearLayout ll_empty;
	private LinearLayout ll_net_unavailable;
	private ListView listView;
	
	private RegistrationAdapter adapter;
	private TextView nodata_error;
	private SearchGuahaoEn sge=null;
	private String keyword;
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		this.activity=(SearchResourceActivity)activity;
	}

	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		rootView = inflater.from(getActivity()).inflate(
				R.layout.fragment_searchall, null);
		intiview();
		return rootView;
	}
	
	
	private void intiview() {
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
			ll_net_unavailable.setVisibility(View.VISIBLE);
			listView.setVisibility(View.GONE);
			break;
		case IResult.DATA_ERROR:
			activity.destroyDialog();
			activity.showToast(IMessage.DATA_ERROR);
			ll_empty.setVisibility(View.VISIBLE);
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
	public void searchRegistrtion(String keyword) {
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
					HttpUtil.search_doctor_guahao, SearchGuahaoEn.class, params,
					new Listener() {

						public void onResponse(Object object) {
							
							sge = (SearchGuahaoEn) object;
							adapter=new RegistrationAdapter(getActivity(),sge.getData(),activity.imageLoader,activity.options);
							if (sge.getData() != null && sge.getData().size()!=0) {
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
							mhandler.sendMessage(message);
						}
					});

		} else {
			mhandler.sendEmptyMessage(IResult.NET_ERROR);
		}

	}

	public void doClick(View view){
		if(view.getId()==R.id.ll_net_unavailable){
			searchRegistrtion(keyword);
		}
	}
}
