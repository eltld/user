package net.ememed.user2.activity;

import net.ememed.user2.R;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class GuahaoSmartActivity extends Activity{

	private static final String TITLE = "智能导诊";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_guahao_smart);
		((TextView)findViewById(R.id.tv_top_title)).setText(TITLE);
		
	}
	
	public void online(View v) {
		if(SharePrefUtil.getBoolean(Conast.LOGIN)){
			Intent intent = new Intent(this, PutQuestionsActivity.class);
			startActivity(intent);
		}else{
			Intent intent = new Intent(this, LoginActivity.class);
			intent.putExtra("origin", PutQuestionsActivity.class.getSimpleName());
			intent.putExtra("current_service", "17");
			startActivity(intent);
		}
	}
	
	public void check(View v) {
		startActivity(new Intent(this, GuahaoSmartBodyActivity.class));
	}
	
	public void doClick(View v) {
		finish();
	}

}
