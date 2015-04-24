package net.ememed.user2.activity;

import com.umeng.analytics.MobclickAgent;
import net.ememed.user2.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AboutUsActivity extends BasicActivity {
	private TextView top_title;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.about_us);		
	}

	@Override
	protected void setupView() {
		top_title = (TextView)findViewById(R.id.top_title);
		top_title.setText(getString(R.string.about_us));
		super.setupView();	
//		TextView tv_version = (TextView)findViewById(R.id.tv_version);
//		tv_version.setText(getString(R.string.app_version)+PublicUtil.getVersionName(this));	
	}
	
	public void doClick(View view) {
		if(view.getId() == R.id.btn_back){
			finish();
		} 
	}
}

