package net.ememed.user2.activity;


import net.ememed.user2.R;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class TempActivity extends BasicActivity {
	
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_temp);
		TextView top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("用药助手");
	}
	
	public void doClick(View view) {
		int id = view.getId();
		if (id == R.id.btn_back) {
			finish();
		} 
	}
}
