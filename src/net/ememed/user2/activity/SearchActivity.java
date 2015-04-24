package net.ememed.user2.activity;

import java.util.ArrayList;

import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.SearchRecordsAdapter;
import net.ememed.user2.db.SearchRecordsDao;
import net.ememed.user2.util.Logger;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SearchActivity extends BasicActivity/* implements OnClickListener*/{
	private BasicActivity activity;
	private EditText et_disease_keyword;
	private ListView lv_search_records;
	private ImageView btn_search;
	private SearchRecordsAdapter adapter;
	private ArrayList<String> records = new ArrayList<String>();
	private SearchRecordsDao dao = null;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_search);
		activity = (BasicActivity)this;
		dao = new SearchRecordsDao(activity);
		init();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if(null == dao){
			dao = new SearchRecordsDao(activity);
		}
		records = (ArrayList<String>) dao.getSearchRecords();
		adapter.change(records);
		Logger.iout("生命周期","onResume()");
	}
	
	private void init(){
		dao = new SearchRecordsDao(activity);
		records = (ArrayList<String>) dao.getSearchRecords();
		lv_search_records = (ListView) findViewById(R.id.lv_search_records);
		adapter = new SearchRecordsAdapter(activity, records);
		lv_search_records.setAdapter(adapter);
		lv_search_records.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String str = records.get(position);
				saveSearchRecords(str);
				Intent intent = new Intent(SearchActivity.this, DoctorSearchActivity.class);
				intent.putExtra("keyword", str);
//				et_disease_keyword.setText("");
				startActivity(intent);
			}
		});
		
		btn_search = (ImageView) findViewById(R.id.bt_keyword_search);
		btn_search.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = null;
				if (et_disease_keyword.isFocused() && activity.getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED) {
					btn_search.setPressed(true);
				} else {
					btn_search.setPressed(false);
				}

				if (v.getId() == R.id.bt_keyword_search) {
					String str = et_disease_keyword.getText().toString().trim();
					if (TextUtils.isEmpty(et_disease_keyword.getText().toString().trim())) {
						activity.showToast("请输入关键字");
					} else {
						// 保存搜索记录
						saveSearchRecords(str);
						intent = new Intent(SearchActivity.this, GuaHaoSearchActivity.class);
						intent.putExtra("keyword", et_disease_keyword.getText().toString().trim());
//						et_disease_keyword.setText("");
						startActivity(intent);
					}
				} 
			}
		});
		
		et_disease_keyword = (EditText) findViewById(R.id.et_disease_keyword);
		et_disease_keyword.setHintTextColor(0xffafafaf);
		et_disease_keyword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(hasFocus){
					records = (ArrayList<String>) dao.getSearchRecords();
					adapter.change(records);
					lv_search_records.setVisibility(View.VISIBLE);
				}else{
					lv_search_records.setVisibility(View.GONE);
				}
			}
		});
		
		et_disease_keyword.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				if (count == 0 && start == 0) {
					btn_search.setPressed(false);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				btn_search.setPressed(true);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});
		
		et_disease_keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					if (TextUtils.isEmpty(et_disease_keyword.getText().toString().trim())) {
						activity.showToast("请输入关键字");
					} else {
						Intent intent = new Intent(activity, GuaHaoSearchActivity.class);
						intent.putExtra("keyword", et_disease_keyword.getText().toString().trim());
						et_disease_keyword.setText("");
						startActivity(intent);
					}
					return true;
				}
				return false;
			}
		});
	}
	
	private void saveSearchRecords(String str) {
		//先删除，再插入，保证最新的搜索记录排在前面
		//先尝试删除
		try {
			dao.deleteSearchRecord(str);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.iout("sqlite删除搜索记录操作",e.getMessage());
		}
		
		//插入搜索记录
		try {
			dao.saveSearchRecord(str);
		} catch (Exception e) {
			e.printStackTrace();
			Logger.iout("sqlite插入搜索记录操作",e.getMessage());
		}
	}
	
	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		}
	}
}

