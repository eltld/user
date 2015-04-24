package net.ememed.user2.fragment;

import java.util.ArrayList;
import com.umeng.analytics.MobclickAgent;
import com.viewpagerindicator.CustomViewPager;
import com.viewpagerindicator.TabPageIndicator;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.MainActivity;
import net.ememed.user2.util.Logger;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class ChatTabFragment extends Fragment{
	private static final String TAG = ChatTabFragment.class.getSimpleName();
	private BasicActivity activity = null;
	private PagerAdapter mPagerAdapter;
	private CustomViewPager mViewPager;
	private TabPageIndicator mIndicator;
	
	public ChatTabFragment(){
		this.activity = (BasicActivity)getActivity();
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (MainActivity) activity;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = (BasicActivity) getActivity();
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		if(isVisibleToUser){
			Logger.iout(TAG, "可见");
		}else{
			Logger.iout(TAG, "不可见");
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
		View view = inflater.inflate(R.layout.chat_viewpager_setting_pwd, null);
	
		mPagerAdapter = new PagerAdapter(getChildFragmentManager(), getActivity());
		mIndicator = (TabPageIndicator) view.findViewById(R.id.indicator);
		mViewPager = (CustomViewPager) view.findViewById(R.id.pager);
		
		Bundle bundle1 = new Bundle();
		Bundle bundle2 = new Bundle();
		bundle1.putString("chat_tab_title", "医生好友");
		bundle2.putString("chat_tab_title", "我的群组");
		mPagerAdapter.addFragment("医生好友", MyFriendFragment.class, bundle1);
		mPagerAdapter.addFragment("我的群组", MyGroupFragment.class, bundle2);
		mViewPager.setAdapter(mPagerAdapter);
		mIndicator.setViewPager(mViewPager);
		
		return view;
	}

	class PagerAdapter extends FragmentPagerAdapter {

		private Context mContext;
		private final ArrayList<FragmentInformation> fragments = new ArrayList<FragmentInformation>();
		private FragmentManager fm;
		private ArrayList<Fragment> fra_list;

		protected final class FragmentInformation {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			protected FragmentInformation(String _tag, Class<?> _class, Bundle _args) {
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
			FragmentInformation fragmentInformation = new FragmentInformation(tag, clss, args);
			fragments.add(fragmentInformation);
		}

		@Override
		public Fragment getItem(int arg0) {
			FragmentInformation fragmentInformation = fragments.get(arg0);
			Fragment fra = Fragment.instantiate(mContext,
					fragmentInformation.clss.getName(), fragmentInformation.args);
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
	
}
