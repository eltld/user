package net.ememed.user2.fragment;

import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.DoctorInfo4;
import net.ememed.user2.entity.GuahaoFollow;
import net.ememed.user2.entity.GuahaoFollowHospitalListItem;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Response.ErrorListener;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

public class GuahaoFollowHospitalFragment extends Fragment implements IOnRefreshListener,
		IOnLoadMoreListener {

	private RefreshListView mListView;
	private QuickAdapter<GuahaoFollowHospitalListItem> mAdapter;
	private int mPage = 1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_guahao_follow_hospital, null);
		mListView = (RefreshListView) view.findViewById(R.id.ptr_listview);
		mAdapter = new QuickAdapter<GuahaoFollowHospitalListItem>(getActivity(),
				R.layout.list_item_follow_hospital) {

			@Override
			protected void convert(BaseAdapterHelper helper, GuahaoFollowHospitalListItem item) {
				helper.setImageUrl(R.id.iv_portrait, item.getImageUrl())
						.setText(R.id.tv_name, item.getHospitalName())
						.setText(R.id.tv_grade, item.getGrade())
						.setText(R.id.tv_address, item.getAddress());

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
			case IResult.NEW_GUAHAO_GET_ATTENTION_HOSPITAL:
				GuahaoFollow data = (GuahaoFollow) msg.obj;
				if (data != null && data.getData() != null
						&& data.getData().getHospitalInfo() != null
						&& data.getData().getHospitalInfo().getList() != null) {
					if (mPage == 1) {
						mAdapter.clear();
					}
					mAdapter.addAll(data.getData().getHospitalInfo().getList());
				}
				break;
			case IResult.NET_ERROR:
				// activity.destroyDialog();
				// activity.showToast(IMessage.NET_ERROR);
				// ll_empty.setVisibility(View.VISIBLE);
				// listView.setVisibility(View.GONE);
				break;
			case IResult.DATA_ERROR:
				// activity.destroyDialog();
				// ll_net_unavailable.setVisibility(View.VISIBLE);
				// listView.setVisibility(View.GONE);
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
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("accessKey", HttpUtil.GUAHAO_ACCESS_KEY);
		params.put("attentionType", "1");// 关注医生2,关注医院1
		params.put("page", String.valueOf(mPage));
		params.put("userCode", SharePrefUtil.getString(Conast.MEMBER_ID));

		MyApplication.volleyHttpClient.postWithParams3(HttpUtil.GUAHAO_FOLLOW_LIST,
				GuahaoFollow.class, params, new Listener<GuahaoFollow>() {
					@Override
					public void onResponse(GuahaoFollow data) {
						Message message = new Message();
						message.obj = data;
						message.what = IResult.NEW_GUAHAO_GET_ATTENTION_HOSPITAL;
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
