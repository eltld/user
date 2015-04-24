package net.ememed.user2.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

public class AppraiseDidlog extends Dialog{
	private Context context;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
//		setContentView(r.);
	}
	
	public AppraiseDidlog(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	public AppraiseDidlog(Context context, int theme){
        super(context, theme);
        this.context = context;
    }
}
