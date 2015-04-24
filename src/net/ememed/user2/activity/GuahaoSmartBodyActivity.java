package net.ememed.user2.activity;

import net.ememed.user2.R;
import net.ememed.user2.fragment.BodyFemaleFragment;
import net.ememed.user2.fragment.BodyMaleFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioGroup;
import android.widget.TextView;


public class GuahaoSmartBodyActivity extends FragmentActivity {

    private static final int[] RADIO_BUTTONS = {R.id.rb_male, R.id.rb_female};
	private static final String TITLE = "智能导诊";
    private RadioGroup mRadioGroup;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mFragmentPagerAdapter;
    private CheckBox cb_convert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guahao_smart_body);
        ((TextView)findViewById(R.id.tv_top_title)).setText(TITLE);

        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mViewPager = (ViewPager) findViewById(R.id.pager);
        cb_convert = (CheckBox) findViewById(R.id.cb_convert);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                cb_convert.setChecked(true);
                updateConvert();
                switch (checkedId) {
                    case R.id.rb_male:
                        mViewPager.setCurrentItem(0);
                        break;
                    case R.id.rb_female:
                        mViewPager.setCurrentItem(1);
                        break;
                }
            }
        });
        mFragmentPagerAdapter = new BodyFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mFragmentPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                mRadioGroup.check(RADIO_BUTTONS[i]);
                cb_convert.setChecked(true);
                updateConvert();
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    public class BodyFragmentPagerAdapter extends FragmentPagerAdapter {
        Fragment[] fragments = {new BodyMaleFragment(), new BodyFemaleFragment()};

        public BodyFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return fragments[i];
        }

        @Override
        public int getCount() {
            return fragments.length;
        }
    }
    
    public void doClick(View v) {
    	finish();
    }

    public void convert(View v) {
        updateConvert();
    }

    private void updateConvert() {
        ((OnConvertListener) mFragmentPagerAdapter.getItem(mViewPager.getCurrentItem())).onConvert(cb_convert.isChecked());
    }

    public interface OnConvertListener {
        public void onConvert(boolean direct);
    }

}
