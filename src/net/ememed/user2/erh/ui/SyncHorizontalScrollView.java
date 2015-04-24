package net.ememed.user2.erh.ui;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;

public class SyncHorizontalScrollView extends HorizontalScrollView {
	private View view;
	private ImageView leftImage;
	private ImageView rightImage;
	private Activity mContext;
	private int windowWitdh = 0;
	public int tabSize = 4;

	public void setSomeParam(View view, Activity context) {
		mContext = context;
		this.view = view;
		this.leftImage = (ImageView)context.findViewById(R.id.iv_left);
		this.rightImage = (ImageView)context.findViewById(R.id.iv_right);;
		windowWitdh = MyApplication.getScreenWidth();
	}

	public SyncHorizontalScrollView(Context context) {
		super(context);
	}

	public SyncHorizontalScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void showOrHideArrow() {
		leftImage.setVisibility(View.GONE);
		rightImage.setVisibility(View.GONE);
		
		if (!mContext.isFinishing() && view != null) {
			measure(0, 0);
			if (windowWitdh >= getMeasuredWidth()) {
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			} else {
				if (getLeft() == 0) {
					leftImage.setVisibility(View.GONE);
					rightImage.setVisibility(View.VISIBLE);
				} else if (getRight() == getMeasuredWidth() - windowWitdh) {
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.GONE);
				} else {
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.VISIBLE);
				}
			}
		}
	}
	
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		if (!mContext.isFinishing() && view != null && rightImage != null
				&& leftImage != null) {
			if (view.getWidth() <= windowWitdh) {
				leftImage.setVisibility(View.GONE);
				rightImage.setVisibility(View.GONE);
			} else {
				if (l == 0) {
					leftImage.setVisibility(View.GONE);
					rightImage.setVisibility(View.VISIBLE);
				} else if (view.getWidth() - l == windowWitdh) {
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.GONE);
				} else {
					leftImage.setVisibility(View.VISIBLE);
					rightImage.setVisibility(View.VISIBLE);
				}
			}
		}
	}
}
