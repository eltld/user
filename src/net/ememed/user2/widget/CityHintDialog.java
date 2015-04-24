package net.ememed.user2.widget;

import de.greenrobot.event.util.BitmapPutUtil;
import net.ememed.user2.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.TextView;

public class CityHintDialog extends Dialog{
	
	
	TextView content_tx;
	Button btn_cancel;
	Button btn_confirm;

	public CityHintDialog(Context context) {
		super(context);
	}
	
	public CityHintDialog(Context context,int theme) {
		super(context,theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_city_hint);
		content_tx = (TextView) findViewById(R.id.content_tx);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
	}
	
	public void initDialog(android.view.View.OnClickListener listener,int width,String content){
		btn_confirm.setOnClickListener(listener);
		btn_cancel.setOnClickListener(listener);
		content_tx.setText(content);
		Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        lp.width = width*3/4; // 宽度
        lp.height = LayoutParams.WRAP_CONTENT; // 高度
        dialogWindow.setAttributes(lp);
	}
}
