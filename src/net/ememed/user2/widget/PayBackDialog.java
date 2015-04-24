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
import android.widget.TextView;

public class PayBackDialog extends Dialog{
	
	Button btnok;

	public PayBackDialog(Context context) {
		super(context);
	}
	public PayBackDialog(Context context,int theme) {
		super(context,theme);
	}
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_pay);
		btnok = (Button)findViewById(R.id.btnok);
		pay_tx1 = (TextView)findViewById(R.id.pay_tx1);
		serial_number = (TextView)findViewById(R.id.serial_number);
		pay_prisce = (TextView)findViewById(R.id.pay_prisce);
		btnok.setOnClickListener(new android.view.View.OnClickListener() {
			
			public void onClick(View v) {
				dismiss();
			}
		});
	}
	public void setParams(int width) {
		Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(  Gravity.CENTER);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        lp.width = width*4/5; // 宽度
        lp.height = LayoutParams.WRAP_CONTENT; // 高度
        dialogWindow.setAttributes(lp);
	}
	TextView pay_tx1;
	TextView pay_prisce;
	TextView serial_number;
	public void setPrisce(String pris){
		pay_prisce.setText(pris);
	}
	public void setText(String src){
		pay_tx1.setText(src);
	}
	public void setSerialSumber(String src){
		
		serial_number.setText(src);
	}
}
