package net.ememed.user2.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.MainActivity;
import net.ememed.user2.db.NewsTypeTable;
import net.ememed.user2.entity.NewsTypeEntry;
import net.ememed.user2.entity.NewsTypeInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.CustomViewPager;
import com.viewpagerindicator.TabPageIndicator;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

/**
 * 资讯tab
 * 
 * @author taro chyaohui@gmail.com
 */
public class InfoTabFragment extends Fragment implements Callback, OnPageChangeListener {
	private MainActivity activity;
	private Handler mHandler;
	private FrameLayout mContentView;
	private PagerAdapter mPagerAdapter;
	private CustomViewPager mViewPager;
	private TabPageIndicator mIndicator;
	private View ll_empty;
	private View ll_net_unavailable;

	public InfoTabFragment() {
		activity = (MainActivity) getActivity();
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
		mHandler = new Handler(this);

	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if (isVisibleToUser) {
			// fragment可见时执行加载数据或者进度条等
		} else {
			// 不可见时不执行操作
		}
	}

	@Override
    public void onDetach() {
    	super.onDetach();
    	try {
    	    java.lang.reflect.Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
    	    childFragmentManager.setAccessible(true);
    	    childFragmentManager.set(this, null);
    	} catch (NoSuchFieldException e) {
    	    throw new RuntimeException(e);
    	} catch (IllegalAccessException e) {
    	    throw new RuntimeException(e);
    	}
    }
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.activity_viewpager_setting_pwd, null);
		ll_empty = view.findViewById(R.id.ll_empty);
		ll_net_unavailable = view.findViewById(R.id.ll_net_unavailable);
		LayoutParams lps = ll_net_unavailable.getLayoutParams();
		if (lps == null)
			lps = new LayoutParams(LayoutParams.MATCH_PARENT, ((MainActivity) getActivity()).Height);
		lps.height = ((MainActivity) getActivity()).Height;
		ll_net_unavailable.setLayoutParams(lps);
		ll_empty.setLayoutParams(lps);
		mPagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity());
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		mViewPager = (CustomViewPager) view.findViewById(R.id.pager);
		NewsTypeTable newTypeTable = new NewsTypeTable();
		//资讯页面的屏幕上方四个选项卡
		if (null != newTypeTable.getNewsTypes() && newTypeTable.getNewsTypes().size() > 0) {
			ArrayList<NewsTypeEntry> newsTypes = newTypeTable.getNewsTypes();
			for (int i = 0; i < newsTypes.size(); i++) {
				Bundle bundle = new Bundle();
				bundle.putString("news_title", newsTypes.get(i).getTITLE());
				bundle.putString("type_id", newsTypes.get(i).getID());
				mPagerAdapter.addFragment(newsTypes.get(i).getTITLE(), InfoFragment.class, bundle);
			}
			mViewPager.setOffscreenPageLimit(newsTypes.size());// 缓存多少个页面
			mViewPager.setAdapter(mPagerAdapter);
			mIndicator.setViewPager(mViewPager);
		} else {
			mViewPager.setAdapter(mPagerAdapter);
			mIndicator.setViewPager(mViewPager);
			mViewPager.setAdapter(mPagerAdapter);
			mIndicator.setViewPager(mViewPager);
			mIndicator.setVisibility(View.GONE);
			mViewPager.setVisibility(View.GONE);
			if (NetWorkUtils.detect(getActivity())) {
				ll_empty.setVisibility(View.VISIBLE);
			} else {
				ll_net_unavailable.setVisibility(View.VISIBLE);
				ll_net_unavailable.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						((MainActivity) getActivity()).setNetWork();

					}
				});
			}

		}

		return view;
	}

	@Override
	public void onResume() {
		super.onResume();
		if (ll_empty.getVisibility() == View.VISIBLE || ll_net_unavailable.getVisibility() == View.VISIBLE) {
			getNewsType();
		}
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	public boolean handleMessage(Message arg0) {
		return false;
	}

	class PagerAdapter extends FragmentPagerAdapter {

		private Context mContext;
		private final ArrayList<FragmentInfo> fragments = new ArrayList<FragmentInfo>();
		private FragmentManager fm;
		private ArrayList<Fragment> fra_list;

		protected final class FragmentInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			protected FragmentInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		public PagerAdapter(FragmentManager fm, Context context) {
			super(fm);
			this.fm = fm;
			this.mContext = context;
			fra_list = new ArrayList<Fragment>();
		}

		public void clearFragment() {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : fra_list) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();// 立刻执行以上命令（commit）
		}

		public void addFragment(String tag, Class<?> clss, Bundle args) {
			FragmentInfo fragmentInfo = new FragmentInfo(tag, clss, args);
			fragments.add(fragmentInfo);
		}

		@Override
		public Fragment getItem(int arg0) {
			FragmentInfo fragmentInfo = fragments.get(arg0);
			Fragment fra = Fragment.instantiate(mContext,
					fragmentInfo.clss.getName(), fragmentInfo.args);
			if (!fra_list.contains(fra))
				fra_list.add(fra);
			return fra;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragments.get(position).tag;
		}
		@Override  
	    public int getItemPosition(Object object) {
	        return PagerAdapter.POSITION_NONE;  
	    } 
		@Override
		public int getCount() {
			return fragments.size();
		}
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		Logger.iout("咨询页面滑动onPageScrollStateChanged", "arg0 ="+arg0);
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		Logger.iout("咨询页面滑动onPageScrolled", "arg0 ="+arg0+", arg1="+arg1+", arg2="+arg2);
	}

	@Override
	public void onPageSelected(int arg0) {
		Logger.iout("咨询页面滑动onPageSelected", "arg0 ="+arg0);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void getNewsType() {

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("type", "user");
		params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_news_type, NewsTypeInfo.class, params, new Response.Listener() {
			@Override
			public void onResponse(Object response) {
				if (null != response) {
					NewsTypeInfo respEntry = (NewsTypeInfo) response;
					if (respEntry.getSuccess() == 1) {
						List<NewsTypeEntry> typsList = respEntry.getData();
						// 保存资讯目录
						NewsTypeTable table = new NewsTypeTable();
						table.clearTable();
						for (int i = 0; i < typsList.size(); i++) {
							table.saveNewsType(typsList.get(i));
						}

						for (int i = 0; i < typsList.size(); i++) {
							Bundle bundle = new Bundle();
							bundle.putString("news_title", typsList.get(i).getTITLE());
							bundle.putString("type_id", typsList.get(i).getID());
							mPagerAdapter.addFragment(typsList.get(i).getTITLE(), InfoFragment.class, bundle);
						}
						ll_net_unavailable.setVisibility(View.GONE);
						ll_empty.setVisibility(View.GONE);
						mIndicator.setVisibility(View.VISIBLE);
						mViewPager.setVisibility(View.VISIBLE);
						mViewPager.setOffscreenPageLimit(typsList.size());// 缓存多少个页面
						mViewPager.setAdapter(mPagerAdapter);
						mIndicator.setViewPager(mViewPager);
					}
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {

			}
		});
	}

}
