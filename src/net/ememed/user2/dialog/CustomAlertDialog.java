package net.ememed.user2.dialog;

import net.ememed.user2.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomAlertDialog extends Dialog implements OnClickListener {

	private Context context;
	private TextView tv_title, tv_message;
	private Button btn_left, btn_right;
	private String mTitle, mMessage;
	private OnOkClickListener mRightListener;
	private OnCancelClickListener mLeftListener;
	private String mButtonLeft, mButtonRight;

	public static void make(Context context, String title, String message,
			OnOkClickListener okListener, OnCancelClickListener cancelListener, String left,
			String right) {
		new CustomAlertDialog(context, title, message, okListener, cancelListener, left, right).show();
	}

	public CustomAlertDialog(Context context) {
		super(context, R.style.AlertDialogStyle);
		this.context = context;
		this.mMessage = "";
		this.mTitle = "";
	}

	public CustomAlertDialog(Context context, String message, OnOkClickListener okListener,
			OnCancelClickListener cancelListener) {
		this(context, "提示", message, okListener, cancelListener, "确定", "取消");
	}

	public CustomAlertDialog(Context context, String title, String message, OnOkClickListener okListener,
			OnCancelClickListener cancelListener, String left, String right) {
		super(context, R.style.AlertDialogStyle);
		this.context = context;
		this.mTitle = title;
		this.mMessage = message;
		this.mRightListener = okListener;
		this.mLeftListener = cancelListener;
		this.mButtonLeft = left;
		this.mButtonRight = right;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_alert);

		setCanceledOnTouchOutside(false);// Touch Other Dismiss

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_message = (TextView) findViewById(R.id.tv_message);
		btn_right = (Button) findViewById(R.id.btn_right);
		btn_left = (Button) findViewById(R.id.btn_left);
		btn_left.setText(mButtonLeft);
		btn_right.setText(mButtonRight);
		btn_right.setOnClickListener(this);
		btn_left.setOnClickListener(this);
		if (mLeftListener == null) {
			btn_left.setVisibility(View.GONE);
		}
		if (mRightListener == null) {
			btn_right.setVisibility(View.GONE);
		}

		// Set Dialog Gravity
		Window dialogWindow = getWindow();
		dialogWindow.setGravity(Gravity.CENTER);

		// Set Dialog Width
		WindowManager.LayoutParams p = dialogWindow.getAttributes();
		// p.width = WindowManager.LayoutParams.MATCH_PARENT;

		dialogWindow.setAttributes(p);

	}

	@Override
	public void show() {
		super.show();
		tv_title.setText(mTitle);
		tv_message.setText(mMessage);
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public void setMessage(String message) {
		this.mMessage = message;
	}

	public void setOnOkClickListener(OnOkClickListener listener) {
		this.mRightListener = listener;
	}

	public interface OnOkClickListener {
		public void onClick();
	}

	public interface OnCancelClickListener {
		public void onClick();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_right:
			if (mRightListener == null) {
				return;
			}
			mRightListener.onClick();
			break;
		case R.id.btn_left:
			if (mLeftListener == null) {
				return;
			}
			mLeftListener.onClick();
			break;

		default:
			break;
		}
		dismiss();
	}

}
