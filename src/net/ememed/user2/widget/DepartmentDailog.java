package net.ememed.user2.widget;

import java.util.List;

import de.greenrobot.event.util.BitmapPutUtil;

import net.ememed.user2.R;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DepartmentDailog extends Dialog {
	private Context context;

	private ListView listView;
	private ArrayAdapter<String> mAdapter;
	private OnItemClickListener mItemClickListener;

	public DepartmentDailog(Context context) {
		super(context);
	}

	public DepartmentDailog(Context context, int theme) {
		super(context, theme);
		this.context = context;
		mAdapter = new ArrayAdapter<String>(context, R.layout.department_dailog_item, R.id.textView);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_department);
		listView = (ListView) findViewById(R.id.departmentlistview);
		listView.setAdapter(mAdapter);
		if (mItemClickListener != null)
			listView.setOnItemClickListener(mItemClickListener);
		setParams();
	}

	public void addData(List<String> list) {
		if (list == null)
			return;
		mAdapter.addAll(list);
	}

	public void setOnItemClickListener(OnItemClickListener clickListener) {
		mItemClickListener = clickListener;
	}

	public void setParams() {

		Window dialogWindow = getWindow();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		dialogWindow.setGravity(Gravity.CENTER);
		lp.width = LayoutParams.WRAP_CONTENT; // 宽度
		lp.height = BitmapPutUtil.convertdipTopx(context, 400); // 高度
		lp.alpha = 0.8f; // 透明度
		dialogWindow.setAttributes(lp);
	}
}
