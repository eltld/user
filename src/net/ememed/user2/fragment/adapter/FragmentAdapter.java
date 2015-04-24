package net.ememed.user2.fragment.adapter;

import java.util.List;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * 
 * @author FXX
 * Fragment + viewpage 适配器
 *
 */

public class FragmentAdapter extends FragmentPagerAdapter{

	
	private List<Fragment> list;
	
	public FragmentAdapter(FragmentManager fm,List<Fragment> list) {
		super(fm);
		this.list = list;
	}
	
	@Override
	public Fragment getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public int getCount() {
		return list.size();
	}

}
