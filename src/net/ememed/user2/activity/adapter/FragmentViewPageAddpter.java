package net.ememed.user2.activity.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentViewPageAddpter extends FragmentPagerAdapter{

	
	private List<Fragment> fragments;
	
	public FragmentViewPageAddpter(FragmentManager m,List<Fragment> fragments) {
		super(m);
		this.fragments = fragments;
	}
	@Override
	public Fragment getItem(int arg0) {
		return fragments.get(arg0);
	}

	@Override
	public int getCount() {
		return fragments.size();
	}

	
	
}
