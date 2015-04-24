package net.ememed.user2.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.DoctorDetailInfoActivity;
import net.ememed.user2.activity.ExpertSearchActivity;
import net.ememed.user2.activity.ImageActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.activity.MainActivity;
import net.ememed.user2.db.ContactTable;
import net.ememed.user2.entity.ContactEntity;
import net.ememed.user2.entity.ContactEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.CircleImageView;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.LoginSuccessEvent;

/**
 * 我的医生tab
 * 
 * @author taro chyaohui@gmail.com
 */
public class MyGroupFragment extends Fragment{
	private static final String TAG = MyGroupFragment.class.getSimpleName();
	

	private Handler handler = null;
	private MainActivity activity = null;
	
	

	public MyGroupFragment() {
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
		activity = (MainActivity) getActivity();
		Logger.dout(TAG + "onCreate");
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时执行加载数据或者进度条等
			Logger.dout(TAG + "isVisibleToUser");
			if (SharePrefUtil.getBoolean(Conast.LOGIN)) {
				
			} else {
				handler.sendEmptyMessage(IResult.UN_LOGIN);
			}
		} else {
			// 不可见时不执行操作
			Logger.dout(TAG + "unVisibleToUser");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.chat_root_layout, null);
	/*	mContentView = (FrameLayout) view.findViewById(R.id.chat_mainView);
		ll_view_content = (LinearLayout) inflater.inflate(R.layout.activity_list_view, null);*/
		return view;
	}


}
