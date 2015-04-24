package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.HotCityAdapter;
import net.ememed.user2.db.CityTable;
import net.ememed.user2.entity.CityEntry;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.CtiySideBar;
import net.ememed.user2.widget.CtiySideBar.OnTouchingLetterChangedListener;
import net.ememed.user2.widget.GridViewForScrollview;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.baidu.location.BDLocation;

import de.greenrobot.event.pingyin.util.CharacterParser;
import de.greenrobot.event.pingyin.util.PinyinComparator;
import de.greenrobot.event.pingyin.util.SortAdapter;
import de.greenrobot.event.pingyin.util.SortModel;
import de.greenrobot.event.util.BDLocationCallback;
import de.greenrobot.event.util.BDLocationTool;

public class CityActivity extends BasicActivity implements BDLocationCallback {

	ListView city_listview;
	GridViewForScrollview grid_hot_city;
	CtiySideBar city_bar;
	private CharacterParser characterParser;
	private PinyinComparator pinyinComparator;
	SortAdapter adapter;
	HotCityAdapter city_adapter;
	private TextView dialog;
	Button btn_addhealth;
	SortModel sortModel;
	
	TextView TheCurrentcity;
	CityTable cityConfigTable;
	TextView top_title;
	
	boolean isBaiduLocation = false;
	
	Handler handler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 1:
				destroyDialog();
				List<CityEntry> citys = (List<CityEntry>) msg.obj;
				List<SortModel> models = filledData(citys);
				
				List<SortModel> gridData = new ArrayList<SortModel>();
				
				for (int i = 0; i < models.size(); i++) {
					SortModel GsortModel = models.get(i);
					if(GsortModel.getName().contains("北京市")||
							GsortModel.getName().contains("长沙市")||
							GsortModel.getName().contains("广州市")||
							GsortModel.getName().contains("上海市")||
							GsortModel.getName().contains("深圳市")||
							GsortModel.getName().contains("武汉市")||
							GsortModel.getName().contains("厦门市")||
							GsortModel.getName().contains("重庆市")||
							GsortModel.getName().contains("惠州市")){
						gridData.add(GsortModel);
					}
				}
				
				city_adapter.setData(gridData);
				grid_hot_city.setAdapter(city_adapter);
				Collections.sort(models, pinyinComparator);
				adapter = new SortAdapter(CityActivity.this, models);
				city_listview.setAdapter(adapter);
				
				break;
			default:
				break;
			}
		}
	};

	
	//getCity only call by: @see PersonInfoActivity

	// getCity only call by: @see PersonInfoActivity
	private boolean bCity;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_city);

		if (getIntent() != null) {
			bCity = getIntent().getBooleanExtra("getCity", false);
		}

		BDLocationTool locationTool = new BDLocationTool(this);
		locationTool.startBaiduLocation(this);

		top_title=(TextView) findViewById(R.id.top_title);
		top_title.setText("切换城市");
		city_bar = (CtiySideBar) findViewById(R.id.city_bar);
		city_listview = (ListView) findViewById(R.id.city_listview);
		View headView=LayoutInflater.from(this).inflate(R.layout.hot_city, null);
		city_listview.addHeaderView(headView);
		
		//热门城市
		grid_hot_city=(GridViewForScrollview) headView.findViewById(R.id.grid_hot_city);
		city_adapter=new HotCityAdapter(this);
		
		
		dialog = (TextView) findViewById(R.id.dialog);
		TheCurrentcity = (TextView) findViewById(R.id.TheCurrentcity);
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setText("储存");
		btn_addhealth.setBackgroundResource(R.color.transparent);
		btn_addhealth.setTextSize(16);
		city_bar.setTextView(dialog);
		characterParser = CharacterParser.getInstance();
		pinyinComparator = new PinyinComparator();
		cityConfigTable = new CityTable();

		loading(null);
		new Thread() {

			public void run() {
				List<CityEntry> citys = cityConfigTable.getAllCity();
				Message msg = new Message();
				msg.what = 1;
				msg.obj = citys;
				handler.sendMessage(msg);
				
			};

		}.start();

		setListener();
		
	}

	private void setListener(){
		city_bar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0))+1;
				System.out.println("--------------->"+position);
				if (position != -1) {
					city_listview.setSelection(position);
				}

			}
		});

		city_listview.setOnItemClickListener(new OnItemClickListener() {

			@Override

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				
				sortModel = adapter.getSortModel(position-1);
				
				System.out.println("cityEntry.getID() = "+sortModel.getId());
				
				TheCurrentcity.setText(sortModel.getName());

				if (bCity) {
					Intent intent = new Intent();
					intent.putExtra("city", sortModel.getName());
					setResult(RESULT_OK, intent);
					finish();
				}
			}
		});
		
		
		grid_hot_city.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(city_adapter!=null){
					sortModel = city_adapter.getSortModel(position);
					TheCurrentcity.setText(sortModel.getName());
					if (bCity) {
						Intent intent = new Intent();
						intent.putExtra("city", sortModel.getName());
						setResult(RESULT_OK, intent);
						finish();
					}
				}
			}
		});
		
		
	}
	
//	private HashMap<String, String> getHotCity(){
////		List<HashMap<String, String>> hot_city=new ArrayList<HashMap<String,String>>();
//		HashMap<String, String> map=new HashMap<String, String>();
//		map.put("45054", "北京市");
//		map.put("275", "长沙市");
//		map.put("289", "广州市");
//		map.put("45053", "上海市");
//		map.put("291", "深圳市");
//		map.put("258", "武汉市");
//		map.put("204", "厦门市");
//		map.put("45055", "重庆市");
//		map.put("299", "惠州市");
////		hot_city.add(map);
//		return map;
//	}
	
	private List<SortModel> filledData(List<CityEntry> citys) {
		List<SortModel> mSortList = new ArrayList<SortModel>();
		for (int i = 0; i < citys.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(citys.get(i).getAREANAME());
			sortModel.setId(citys.get(i).getID());
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(citys.get(i)
					.getAREANAME());
			// 汉字转换成拼音
			String sortString = pinyin.substring(0, 1).toUpperCase();
			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}
			mSortList.add(sortModel);
		}
		return mSortList;

	}
	
	

	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.Refresh_bnt:
			TheCurrentcity.setText("GPS定位中...");
			BDLocationTool locationTool = new BDLocationTool(this);
			locationTool.startBaiduLocation(this);
			break;
		case R.id.btn_addhealth:
			if(isBaiduLocation){
				SharePrefUtil.putString(Conast.CITY_ID, sortModel.getId());
				SharePrefUtil.putString(Conast.CITY_NAME, sortModel.getName());
				SharePrefUtil.commit();
				finish();
			}else{
				showToast("定位失败请重新定位");
			}
			
			break;

		default:
			break;
		}

	}

	@Override
	public void LocationCallback(BDLocation location) {
		// TODO Auto-generated method stub
		String city = location.getCity();
		CityEntry cityEntry = cityConfigTable.findName(city);
		isBaiduLocation = true;
		sortModel = new SortModel();
		if (cityEntry != null) {
			sortModel.setName(cityEntry.getAREANAME());
			sortModel.setId(cityEntry.getID());
			TheCurrentcity.setText(cityEntry.getAREANAME());
		}
	}

}
