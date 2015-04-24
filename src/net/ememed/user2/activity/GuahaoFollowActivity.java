package net.ememed.user2.activity;

import net.ememed.user2.R;
import net.ememed.user2.fragment.GuahaoFollowDoctorFragment;
import net.ememed.user2.fragment.GuahaoFollowHospitalFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

public class GuahaoFollowActivity extends BasicActivity {

	private static final int[] RADIO_BUTTONS = { R.id.rb_doctor, R.id.rb_hospital };
	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;
	private RadioGroup mRadioGroup;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guahao_follow);

		mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				mRadioGroup.check(RADIO_BUTTONS[position]);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
		mAdapter = new TabAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mAdapter);
	}

	class TabAdapter extends FragmentPagerAdapter {

		private final Fragment[] FRAGMENTS = { new GuahaoFollowDoctorFragment(),
				new GuahaoFollowHospitalFragment() };

		public TabAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return FRAGMENTS[position];
		}

		@Override
		public int getCount() {
			return FRAGMENTS.length;
		}

	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.rb_doctor:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.rb_hospital:
			mViewPager.setCurrentItem(1);
			break;
		default:
			break;
		}
	}

}
