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

public class CardDialog extends Dialog{
	
	Button Cancel_bnt;
	Button ok_bnt;
	EditText account_eidt;
	EditText password_eidt;
	public CardDialog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public CardDialog(Context context,int theme) {
		super(context,theme);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_card);
		Cancel_bnt = (Button) findViewById(R.id.Cancel_bnt);
		ok_bnt = (Button) findViewById(R.id.ok_bnt);
		account_eidt = (EditText) findViewById(R.id.account_eidt);
		password_eidt = (EditText) findViewById(R.id.password_eidt);
		Cancel_bnt.setOnClickListener(new android.view.View.OnClickListener() {
			
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
        dialogWindow.setGravity(  Gravity.CENTER);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        lp.width = width*3/4; // 宽度
        lp.height = LayoutParams.WRAP_CONTENT; // 高度
        dialogWindow.setAttributes(lp);
	}
	
	public void setOnClickListener(android.view.View.OnClickListener listener){
		ok_bnt.setOnClickListener(listener);
	}
	
	public String getAccount(){
		return account_eidt.getText().toString().trim();
	}
	public String getPassword(){
		return password_eidt.getText().toString().trim();
	}
	
}
