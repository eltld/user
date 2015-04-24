package net.ememed.user2.widget;

import net.ememed.user2.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

public class CommentDialog extends Dialog {

	public RatingBar ratingBar;
	public EditText comment_edit;
	public Button cancel_bnt;
	public Button ok_bnt;

	public CommentDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public CommentDialog(Context context, int theme) {
		super(context, theme);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comment_layout);
		ratingBar = (RatingBar) findViewById(R.id.ratingBar);
		comment_edit = (EditText) findViewById(R.id.comment_edit);
		cancel_bnt = (Button) findViewById(R.id.Cancel_bnt);
		ok_bnt = (Button) findViewById(R.id.okComment_bnt);
		cancel_bnt.setOnClickListener(new android.view.View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
		});
	}

	public void setParams(int width) {

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		lp.width = width; // 宽度
		lp.height = LayoutParams.WRAP_CONTENT; // 高度
		lp.alpha = 0.8f; // 透明度
		dialogWindow.setAttributes(lp);
	}

	public void setOnClickListenerOK(android.view.View.OnClickListener listener) {
		if (listener != null)
			ok_bnt.setOnClickListener(listener);
	}

	public String getCommentText() {

		return comment_edit.getText().toString().trim();
	}

	public int getRating() {

		return ratingBar.getProgress();
	}
}
