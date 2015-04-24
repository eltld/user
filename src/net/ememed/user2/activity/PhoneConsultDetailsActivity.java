package net.ememed.user2.activity;

import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.ModePopAdapter;
import android.app.ActionBar.LayoutParams;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

public class PhoneConsultDetailsActivity extends BasicActivity {
	private TextView top_title;
	private Button btn_addhealth;
	private View mModeView;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_phone_consult_details);
	}

	@Override
	protected void setupView() {
		super.setupView();
		top_title = (TextView)findViewById(R.id.top_title);
		top_title.setText("预约通话详情");
	}

	public void doClick(View view){
		if(view.getId() == R.id.btn_back){
			finish();
		}else if (view.getId() == R.id.tv_free_phone_num){
			try {
            	Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:4009003333"));
            	intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);	
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}
	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
	}
}
