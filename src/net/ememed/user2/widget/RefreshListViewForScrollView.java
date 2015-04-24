package net.ememed.user2.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View.MeasureSpec;
import android.widget.ListView;

public class RefreshListViewForScrollView extends RefreshListView {
	public RefreshListViewForScrollView(Context context) {
		super(context);
	}

	public RefreshListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	/**
	 * 重写该方法，达到使ListView适应ScrollView的效果
	 */
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
