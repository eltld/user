package net.ememed.user2.widget;

import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.midian.mimi.util.customview.wheel.WheelView;
import com.midian.mimi.util.customview.wheel.adapter.AbstractWheelTextAdapter;

public class AgeDialog extends Dialog{
	
	Context context;
	WheelView wheelView;

	public AgeDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	public AgeDialog(Context context,int theme) {
		super(context, theme);
		this.context = context;
	}
	
	Button ok_bnt;
	Button Cancel_bnt;
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dialog_age);
		
		wheelView = (WheelView) findViewById(R.id.age);
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
	AgeAdapter ageAdapter;
	public void setAdapter(){
		
		
		ok_bnt = (Button) findViewById(R.id.ok_bnt);
		Cancel_bnt = (Button) findViewById(R.id.Cancel_bnt);
		
		List<String> ageList = new ArrayList<String>();
		for (int i = 0; i < 12; i++) {
			ageList.add((i+1)+" 月");
		}
		for (int i = 0; i < 120; i++) {
			ageList.add((i+1)+" 岁");
		}
		ageAdapter = new AgeAdapter(context, ageList);  
		
		wheelView.setViewAdapter(ageAdapter);
		
	}
	
	
	
	public void setOnClickListener(android.view.View.OnClickListener listener){
		
		ok_bnt.setOnClickListener(listener);
		Cancel_bnt.setOnClickListener(listener);
		
	}
	
	
	public class AgeAdapter extends AbstractWheelTextAdapter{

		
		public List<String> list;

		protected AgeAdapter(Context context, List<String> list) {
			super(context, R.layout.wheel_view_layout, NO_RESOURCE);
			this.list = list;
		}

		@Override
		public View getItem(int index, View cachedView, ViewGroup parent) {
			View view = super.getItem(index, cachedView, parent);

			TextView textCity = (TextView) view.findViewById(R.id.textView);
			textCity.setText(list.get(index));
			return view;
		}

		public int getItemsCount() {
			return list.size();
		}

		protected String getItemText(int index) {
			return list.get(index);
		}
		
	}
	
	public String  getAge(){
		
		
		return ageAdapter.getItemText(wheelView.getCurrentItem());
	}

	

}
