package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import net.ememed.user2.R;
import net.ememed.user2.activity.DoctorHistoryOrderActivity.ViewHolder;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class HelpEmemedsActivity extends BasicActivity implements OnRefreshListener{
	private TextView top_title;
	private ListView list_view;
	private ContactAdapter adapter;
	private List<Map<String, Object>> listItems;
	private String[] goodsNames = { "图文咨询", "预约通话", "签约私人医生", "上门会诊"};
	private String[] goodsDetails = { "已结束", "预约通话通话时间为2014-06-01 14：30", "等待回复","等待回复" };
	private PullToRefreshLayout mPullToRefreshLayout;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_help_ememeds);
	}

	@Override
	protected void setupView() {
		super.setupView();
		top_title = (TextView)findViewById(R.id.top_title);
		top_title.setText(getString(R.string.fragment_help_ememeds));
		
		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(HelpEmemedsActivity.this).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);
		listItems = getListItems();
		list_view =  (ListView) findViewById(R.id.ptr_listview);
		adapter = new ContactAdapter(listItems);
		list_view.setAdapter(adapter);
		
	}

	public void doClick(View view){
		if(view.getId() == R.id.btn_back){
			finish();
		}else if (view.getId() == R.id.btn_seek_help){
			Intent intent = new Intent(this, QuestionDescriptionActivity.class);
			startActivity(intent);
		}
//		else if (view.getId() == R.id.btn_alipay){
//			Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
//			intent.putExtra("title", "林海滨医生");
//			startActivity(intent);
//		}
	}
	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
	}
	
	private List<Map<String, Object>> getListItems() {
		List<Map<String, Object>> listItems = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < goodsNames.length; i++) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("info", goodsNames[i]);
			map.put("detail", goodsDetails[i]);
			listItems.add(map);
		}
		return listItems;
	}
	private class ContactAdapter extends BaseAdapter {
		List<Map<String, Object>> listItems;

		public ContactAdapter(List<Map<String, Object>> listItems) {
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(HelpEmemedsActivity.this).inflate(R.layout.fragment_doctor_order_item, null);
				holder.tv_user_name = (TextView) convertView.findViewById(R.id.tv_user_name);
				holder.tv_consult_info = (TextView) convertView.findViewById(R.id.tv_consult_info);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			holder.tv_user_name.setText((String) listItems.get(position).get(
					"info"));
			holder.tv_consult_info.setText((String) listItems.get(position)
					.get("detail"));
			
			return convertView;
		}
	}

	class ViewHolder {
		TextView tv_user_name;
		TextView tv_consult_info;
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		
	}
}
