package net.ememed.user2.widget;

import net.ememed.user2.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class SexDialog extends Dialog{

	
	Context context;
	
	TextView Boys_tx;
	TextView girls_tx;
	public SexDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public SexDialog(Context context,int theme) {
		super(context, theme);
		this.context = context;
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_sex);
		Boys_tx = (TextView) findViewById(R.id.Boys_tx);
		girls_tx = (TextView) findViewById(R.id.girls_tx);
	}
	
	
	public void setParams(int width) {

		Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(  Gravity.CENTER);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        lp.width = width; // 宽度
        lp.height = LayoutParams.WRAP_CONTENT; // 高度
        lp.alpha = 0.8f; // 透明度
        dialogWindow.setAttributes(lp);
	}
	
	public void setOnClickListener(android.view.View.OnClickListener listener){
		Boys_tx.setOnClickListener(listener);
		girls_tx.setOnClickListener(listener);
	}

}
