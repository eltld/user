package net.ememed.user2.erh.util;

import android.content.Context;
import android.util.Log;
import net.ememed.user2.MyApplication;

public class Tools {
	final static float scale = MyApplication.getInstance().getResources().getDisplayMetrics().density; 
	/**
	 * 将dip或dp值转换为px值，保证尺寸大小不变
	 * @param dipValue
	 * @return
	 */
	public static int dip2px(float dipValue) {
		return (int) (dipValue * scale + 0.5f);
	}
	
	
}
