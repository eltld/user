package net.ememed.user2.activity;

import net.ememed.user2.R;
import android.app.AlertDialog;
import android.app.ActionBar.LayoutParams;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;

import com.umeng.analytics.MobclickAgent;

public class QuestionDescriptionActivity extends BasicActivity {
	private TextView top_title;

	private View mModeView;
	private PopupWindow mModePopupWindow;
	private ImageView bg;
	private int width;
	private int height;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_question_description);

		DisplayMetrics metric = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metric);
		width = metric.widthPixels; // 宽度（PX）
		height = metric.heightPixels; // 高度
	}

	@Override
	protected void setupView() {
		super.setupView();
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(getString(R.string.question_description));

		bg = (ImageView) findViewById(R.id.overlay);
		 mModeView =
		 getLayoutInflater().inflate(R.layout.question_put_success, null);
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_pay_money) {
			 initModePopupWindow();
//			View mModeView = getLayoutInflater().inflate(
//					R.layout.question_put_success, null);
//			AlertDialog.Builder builder = new AlertDialog.Builder(this);
//			builder.setView(mModeView).show();
		}
	}

	private void initModePopupWindow() {
		if (mModePopupWindow == null) {
			mModePopupWindow = new PopupWindow(mModeView,
					LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);

			mModePopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mModePopupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					bg.setVisibility(View.GONE);
				}
			});
		}
		if (mModePopupWindow.isShowing()) {
			mModePopupWindow.dismiss();
		} else {
			bg.setVisibility(View.VISIBLE);
			mModePopupWindow.showAsDropDown(top_title, 0, height / 3);
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
