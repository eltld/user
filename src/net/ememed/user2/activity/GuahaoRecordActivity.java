package net.ememed.user2.activity;

import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import net.ememed.user2.R;
import net.ememed.user2.entity.UserGuahaoEntry;
import net.ememed.user2.entity.UserGuahaoInfo;
import net.ememed.user2.widget.RefreshListView;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.QuickContactBadge;
import android.widget.TextView;

public class GuahaoRecordActivity extends BasicActivity {

	private RefreshListView mListView;
	private QuickAdapter<UserGuahaoEntry> mAdapter;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_guahao_record);

		mListView = (RefreshListView) findViewById(R.id.listView);
		mAdapter = new QuickAdapter<UserGuahaoEntry>(this, R.layout.list_item_record) {

			@Override
			protected void convert(BaseAdapterHelper helper, UserGuahaoEntry item) {
				helper.setText(R.id.tv_name, item.getUSERNAME())
						.setText(R.id.tv_time, item.getTIME())
						.setText(R.id.tv_doctor, item.getDOCTORNAME());
			}
		};
		mListView.setAdapter(mAdapter);
	}
}
