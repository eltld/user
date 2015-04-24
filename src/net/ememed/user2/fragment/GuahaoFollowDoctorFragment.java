package net.ememed.user2.fragment;

import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.GuahaoFollowActivity;
import net.ememed.user2.entity.DoctorInfo4;
import net.ememed.user2.entity.GuahaoFollow;
import net.ememed.user2.entity.GuahaoFollowDoctorList;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

public class GuahaoFollowDoctorFragment extends Fragment implements IOnRefreshListener,
		IOnLoadMoreListener {

	private BasicActivity mActivity;
	private RefreshListView mListView;
	private QuickAdapter<GuahaoFollowDoctorList> mAdapter;
	private int mPage = 1;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mActivity = (GuahaoFollowActivity) activity;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_guahao_follow_hospital, null);
		mListView = (RefreshListView) view.findViewById(R.id.ptr_listview);
		mAdapter = new QuickAdapter<GuahaoFollowDoctorList>(getActivity(),
				R.layout.list_item_follow_doctor) {

			@Override
			protected void convert(BaseAdapterHelper helper, GuahaoFollowDoctorList item) {
				DoctorInfo4 doctorInfo = item.getDoctorInfo();
				helper.setImageUrl(R.id.iv_portrait, doctorInfo.getImgUrl())
						.setText(R.id.tv_name, doctorInfo.getDoctorName())
						.setText(R.id.tv_profession, doctorInfo.getProfessional())
						.setText(R.id.tv_speciality, doctorInfo.getSpeciality())
						.setChecked(R.id.cb_schdule, item.getHasFree());

			}
		};
		mListView.setAdapter(mAdapter);
		mListView.setOnRefreshListener(this);
		mListView.setOnLoadMoreListener(this);

		OnRefresh();
		return view;
	}

	private Handler mhandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			mListView.onRefreshComplete();
			switch (msg.what) {
			case IResult.NEW_GUAHAO_GET_ATTENTION_DOCTOR:
				GuahaoFollow data = (GuahaoFollow) msg.obj;
				if (data != null && data.getData() != null
						&& data.getData().getDoctorDetail() != null
						&& data.getData().getDoctorDetail().getList() != null) {
					if (mPage == 1) {
						mAdapter.clear();
					}
					mAdapter.addAll(data.getData().getDoctorDetail().getList());
				}
				break;
			case IResult.NET_ERROR:
				mActivity.destroyDialog();
				mActivity.showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				mActivity.destroyDialog();
				break;
			default:
				break;
			}
		}
	};

	public void requestFollowList() {
		if (!NetWorkUtils.detect(getActivity())) {
			mhandler.sendEmptyMessage(IResult.NET_ERROR);
			return;
		}
		mActivity.loading(null);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("accessKey", HttpUtil.GUAHAO_ACCESS_KEY);
		params.put("attentionType", "2");// 关注医生2,关注医院1
		params.put("page", String.valueOf(mPage));
		params.put("userCode", SharePrefUtil.getString(Conast.MEMBER_ID));

		MyApplication.volleyHttpClient.postWithParams3(HttpUtil.GUAHAO_FOLLOW_LIST,
				GuahaoFollow.class, params, new Response.Listener() {

					@Override
					public void onResponse(Object data) {
						Message message = new Message();
						message.obj = data;
						message.what = IResult.NEW_GUAHAO_GET_ATTENTION_DOCTOR;
						mhandler.sendMessage(message);
					}
				}, new ErrorListener() {

					public void onErrorResponse(VolleyError error) {
						Message message = new Message();
						message.obj = error.getMessage();
						message.what = IResult.DATA_ERROR;
						mhandler.sendMessage(message);
					}
				});
	}

	@Override
	public void OnLoadMore() {
		mPage++;
		requestFollowList();
	}

	@Override
	public void OnRefresh() {
		mPage = 1;
		requestFollowList();
	}

}
