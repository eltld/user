package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.fragment.CheckHospitalFragment;
import net.ememed.user2.fragment.CheckMedicalFragment;
import net.ememed.user2.fragment.DotorAtelierFragment;
import net.ememed.user2.fragment.RegistrationFragment;
import net.ememed.user2.fragment.SearchAllFragment;
import net.ememed.user2.fragment.adapter.FragmentAdapter;
import net.ememed.user2.widget.AutoCompletEditText;
import net.ememed.user2.widget.AutoCompletListener;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;

public class SearchResourceActivity extends BasicActivity implements
		AutoCompletListener {

	private AutoCompletEditText completEditText;

	public ViewPager view_pager;
	public PagerTabStrip pagerTabStrip;
	public PagerTitleStrip pagerTitleStrip;
	public FragmentAdapter adapter;
	private RadioGroup radioGroup;
	private SearchAllFragment allFragment;
	private DotorAtelierFragment dotorAtelierFragment;
	private RegistrationFragment registrationFragment;
	private CheckHospitalFragment checkHospitalFragment;
	private CheckMedicalFragment checkMedicalFragment;
	public RadioButton[] buttons = new RadioButton[5];
	public String keyWord;

	boolean isSearch1 = false;
	boolean isSearch2 = false;
	boolean isSearch3 = false;
	boolean isSearch4 = false;
	boolean isSearch5 = false;

	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_search_resource);
		completEditText = (AutoCompletEditText) findViewById(R.id.autoComplet);
		completEditText.setOnAutoCompletListener(this);
		initView();
		hideSystemKeyBoard(this, completEditText.getEditText());
	}

	private void initView() {

		initViewpage();
		buttons[0] = (RadioButton) findViewById(R.id.tab1);
		buttons[1] = (RadioButton) findViewById(R.id.tab2);
		buttons[2] = (RadioButton) findViewById(R.id.tab3);
		buttons[3] = (RadioButton) findViewById(R.id.tab4);
		buttons[4] = (RadioButton) findViewById(R.id.tab5);
		radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(RadioGroup arg0, int arg1) {
				switch (arg1) {
				case R.id.tab1:
					view_pager.setCurrentItem(0, false);
					break;
				case R.id.tab2:
					view_pager.setCurrentItem(1, false);
					break;
				case R.id.tab3:
					view_pager.setCurrentItem(2, false);
					break;
				case R.id.tab4:
					view_pager.setCurrentItem(3, false);
					break;
				case R.id.tab5:
					view_pager.setCurrentItem(4, false);
					break;

				default:
					break;
				}
			}
		});

	}

	private void initViewpage() {
		allFragment = new SearchAllFragment();
		dotorAtelierFragment = new DotorAtelierFragment();
		registrationFragment = new RegistrationFragment();
		checkHospitalFragment = new CheckHospitalFragment();
		checkMedicalFragment = new CheckMedicalFragment();
		List<Fragment> list = new ArrayList<Fragment>();
		list.add(allFragment);
		list.add(dotorAtelierFragment);
		list.add(registrationFragment);
		list.add(checkHospitalFragment);
		list.add(checkMedicalFragment);
		view_pager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new FragmentAdapter(getSupportFragmentManager(), list);
		view_pager.setAdapter(adapter);
		view_pager.setOffscreenPageLimit(5);
		view_pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				buttons[arg0].setChecked(true);
				if (!TextUtils.isEmpty(keyWord)) {
					switch (arg0) {
					case 0:
						if (isSearch1) {
							allFragment.searchSoctorAll(keyWord);
							isSearch1 = false;
						}
						break;
					case 1:
						if (isSearch2) {
							dotorAtelierFragment.searchPerson(keyWord);
							isSearch2 = false;
						}
						break;
					case 2:
						if (isSearch3) {
							registrationFragment.searchRegistrtion(keyWord);
							isSearch3 = false;
						}
						break;
					case 3:
						if (isSearch4) {
							checkHospitalFragment.searchHospital(keyWord);
							isSearch4 = false;
						}
						break;
					case 4:
						if (isSearch5) {
							checkMedicalFragment.searchcheckMedical(keyWord);
							isSearch5 = false;
						}
						break;

					default:
						break;
					}
				}

			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	@Override
	public void onSearch() {
		setAllTrue();
		keyWord = completEditText.getText();
		completEditText.setText("");
		if (keyWord.equals("")) {
			showToast("请输入搜索关键字");
			return;
		}
		switch (view_pager.getCurrentItem()) {
		case 0:
			isSearch1 = false;
			allFragment.searchSoctorAll(keyWord);
			break;
		case 1:
			isSearch2 = false;
			dotorAtelierFragment.searchPerson(keyWord);
			break;
		case 2:
			isSearch3 = false;
			registrationFragment.searchRegistrtion(keyWord);
			break;
		case 3:
			isSearch4 = false;
			checkHospitalFragment.searchHospital(keyWord);
			break;
		case 4:
			isSearch5 = false;
			checkMedicalFragment.searchcheckMedical(keyWord);
			break;

		default:
			break;
		}

	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		}
	}

	public void setAllTrue() {
		isSearch1 = true;
		isSearch2 = true;
		isSearch3 = true;
		isSearch4 = true;
		isSearch5 = true;
	}

	public void hideSystemKeyBoard(Context mcontext, View v) {
		InputMethodManager imm = (InputMethodManager) mcontext.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}
}
