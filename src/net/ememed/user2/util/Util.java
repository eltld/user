/*
 * Copyright (C) 2009 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.ememed.user2.util;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.ememed.user2.R;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.ConnectivityManager;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Collection of utility functions used in this package.
 */
public class Util {
	public static final int DIRECTION_LEFT = 0;
	public static final int DIRECTION_RIGHT = 1;
	public static final int DIRECTION_UP = 2;
	public static final int DIRECTION_DOWN = 3;
	public static String MODULE_EHR = 0 + "";
	public static String MODULE_EMR = 0 + "";

	private static OnClickListener sNullOnClickListener;

	private Util() {
	}

	public static String getVersionName(Context context) {
		String versionName = "1.0";
		try {
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			versionName = info.versionName;
		} catch (NameNotFoundException e) {
			return versionName;
		}
		return versionName;
	}

	public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		bmp.compress(CompressFormat.PNG, 100, output);
		if (needRecycle) {
			bmp.recycle();
		}

		byte[] result = output.toByteArray();
		try {
			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public static boolean isAppOnForeground(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// Returns a list of application processes that are running on the
		// device
		List<RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
		if (appProcesses == null)
			return false;
		for (RunningAppProcessInfo appProcess : appProcesses) {
			// importance:
			// The relative importance level that the system places
			// on this process.
			// May be one of IMPORTANCE_FOREGROUND, IMPORTANCE_VISIBLE,
			// IMPORTANCE_SERVICE, IMPORTANCE_BACKGROUND, or IMPORTANCE_EMPTY.
			// These constants are numbered so that "more important" values are
			// always smaller than "less important" values.
			// processName:
			// The name of the process that this object is associated with.
			if (appProcess.processName.equals("net.ememed.user2")
					&& appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
				// Logger.dout("=========> isAppOnForeground!! ");
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断程序是否在前台
	 * 
	 * @return
	 */
	public static boolean isTopActivity(Context context) {
		try {
			ActivityManager am = (ActivityManager) context
					.getSystemService(context.ACTIVITY_SERVICE);
			List<RunningTaskInfo> tasksInfo = am.getRunningTasks(1);
			String packageName = "net.ememed.user2";
			if (null != tasksInfo && tasksInfo.size() > 0) {
				// 应用程序位于堆栈的顶层
				if (packageName.equals(tasksInfo.get(0).topActivity.getPackageName())) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}

	// Rotates the bitmap by the specified degree.
	// If a new bitmap is created, the original bitmap is recycled.
	public static Bitmap rotate(Bitmap b, int degrees) {
		if (degrees != 0 && b != null) {
			Matrix m = new Matrix();
			m.setRotate(degrees, (float) b.getWidth() / 2, (float) b.getHeight() / 2);
			try {
				Bitmap b2 = Bitmap.createBitmap(b, 0, 0, b.getWidth(), b.getHeight(), m, true);
				if (b != b2) {
					b.recycle();
					b = b2;
				}
			} catch (OutOfMemoryError ex) {
				// We have no memory to rotate. Return the original bitmap.
			}
		}
		return b;
	}

	@SuppressWarnings("static-access")
	public static boolean canConnect(Context context) {
		try {
			// get connectivity manager
			ConnectivityManager manager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			// check if we have active network and that network is connected, if
			// not abort
			if (manager.getActiveNetworkInfo() == null
					|| !manager.getActiveNetworkInfo().isConnected()) {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		// we can use the internet connection
		return true;
	}

	public static Bitmap getBitmapFromSdCard(String path) {
		try {

			BitmapFactory.Options options = new BitmapFactory.Options(); // 压缩图片
			options.inSampleSize = 5;
			Bitmap bitmap = BitmapFactory.decodeFile(path, options);
			Bitmap bitmapTumb = ThumbnailUtils.extractThumbnail(bitmap, 128, 128);
			bitmap.recycle();
			return bitmapTumb;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 根据手机的分辨率从 px(像素)的单位转成为 dp
	 * 
	 * @param context
	 * @param pxValue
	 * @return
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	private static ImageLoadingListener animateFirstListener = null;

	// private static ImageLoadingListener animateFirstListener = null;
	//
	// public static ImageLoadingListener getImageLoadingListenerInstance(){
	// if (animateFirstListener == null) {
	// animateFirstListener = new Util.AnimateFirstDisplayListener();
	// }
	// return animateFirstListener;
	// }
	// public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
	//
	// static final List<String> displayedImages = Collections.synchronizedList(new
	// LinkedList<String>());
	//
	// @Override
	// public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
	// if (loadedImage != null) {
	// ImageView imageView = (ImageView) view;
	// boolean firstDisplay = !displayedImages.contains(imageUri);
	// if (firstDisplay) {
	// FadeInBitmapDisplayer.animate(imageView, 500);
	// displayedImages.add(imageUri);
	// } else {
	// imageView.setImageBitmap(loadedImage);
	// }
	// }
	// }
	// }

	public static ImageLoadingListener getImageLoadingListenerInstance() {
		if (animateFirstListener == null) {
			animateFirstListener = new Util.AnimateFirstDisplayListener();
		}
		return animateFirstListener;
	}

	public static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			String message = null;
			switch (failReason.getType()) { // 获取图片失败类型
			case IO_ERROR: // 文件I/O错误
				message = "Input/Output error";
				break;
			case DECODING_ERROR: // 解码错误
				message = "Image can't be decoded";
				break;
			case NETWORK_DENIED: // 网络延迟
				message = "Downloads are denied";
				break;
			case OUT_OF_MEMORY: // 内存不足
				message = "Out Of Memory error";
				break;
			case UNKNOWN: // 原因不明
				message = "Unknown error";
				break;
			}
		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				// 是否第一次显示
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					// 图片淡入效果
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				} else {
					imageView.setImageBitmap(loadedImage);
				}
			}
		}
	}

	static DisplayImageOptions options_avatar = null;
	static DisplayImageOptions options_big_avatar = null;
	static DisplayImageOptions options_pic = null;
	static DisplayImageOptions options_little_pic = null;
	static DisplayImageOptions options_bottletypes = null;
	static DisplayImageOptions Options_default_portrait = null;
	static DisplayImageOptions options_viewpager = null;
	static DisplayImageOptions Hosp_default_portrait = null;
	private static DisplayImageOptions options_EmptyImg;
	private static DisplayImageOptions options_cover;

	public static DisplayImageOptions getOptions_avatar() {
		if (options_avatar == null) {
			options_avatar = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.avatar_small)
					.showImageForEmptyUri(R.drawable.avatar_small)
					.showImageOnFail(R.drawable.avatar_small).cacheInMemory().cacheOnDisc()
					.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return options_avatar;
	}

	public static DisplayImageOptions getOptions_little_pic() {
		if (options_little_pic == null) {
			options_little_pic = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.little_error_pic)
					.showImageForEmptyUri(R.drawable.little_error_pic)
					.showImageOnFail(R.drawable.little_error_pic).cacheInMemory(true)
					.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return options_little_pic;
	}

	public static DisplayImageOptions getOptions_pic() {
		if (options_pic == null) {
			options_pic = new DisplayImageOptions.Builder().showStubImage(R.drawable.big_error_pic)
					.showImageForEmptyUri(R.drawable.big_error_pic)
					.showImageOnFail(R.drawable.big_error_pic).cacheInMemory(true)
					.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return options_pic;
	}

	public static DisplayImageOptions getOptions_big_avatar() {
		if (options_big_avatar == null) {
			options_big_avatar = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.avatar_large)
					.showImageForEmptyUri(R.drawable.avatar_large)
					.showImageOnFail(R.drawable.avatar_large).cacheInMemory(true).cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return options_big_avatar;
	}

	public static DisplayImageOptions getOptions_default_portrait() {
		if (Options_default_portrait == null) {
			Options_default_portrait = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.default_portrait)
					.showImageForEmptyUri(R.drawable.default_portrait)
					.showImageOnFail(R.drawable.default_portrait).cacheInMemory(true)
					.cacheOnDisc(true).bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return Options_default_portrait;
	}

	public static DisplayImageOptions getHosp_default_portrait() {
		if (Hosp_default_portrait == null) {
			Hosp_default_portrait = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.hosp_def).showImageForEmptyUri(R.drawable.hosp_def)
					.showImageOnFail(R.drawable.hosp_def).cacheInMemory(true).cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return Hosp_default_portrait;
	}

	public static DisplayImageOptions getOptions_EmptyImg() {
		if (options_EmptyImg == null) {
			options_EmptyImg = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.transparent_none)
					.showImageForEmptyUri(R.drawable.transparent_none)
					.showImageOnFail(R.drawable.transparent_none).cacheInMemory(true)
					.imageScaleType(ImageScaleType.NONE).cacheOnDisc(true)
					.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return options_EmptyImg;
	}

	public static DisplayImageOptions getOptions_for_viewPager() {

		if (options_viewpager == null) {
			options_viewpager = new DisplayImageOptions.Builder()
					.showStubImage(R.drawable.transparent_none)
					.showImageForEmptyUri(R.drawable.transparent_none)
					.showImageOnFail(R.drawable.transparent_none).cacheInMemory().cacheOnDisc()
					.bitmapConfig(Bitmap.Config.RGB_565)// 防止内存溢出的，图片太多就这这个。还有其他设置
					// .displayer(new RoundedBitmapDisplayer(0))
					.build();
		}
		return options_viewpager;
	}

	private static final String TAG = Util.class.getSimpleName();

	/**
	 * 检测Sdcard是否存在
	 * 
	 * @return
	 */
	public static boolean isExitsSdcard() {
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			return true;
		else
			return false;
	}

	public static String getTopActivity(Context context) {
		ActivityManager manager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);

		if (runningTaskInfos != null)
			return runningTaskInfos.get(0).topActivity.getClassName();
		else
			return "";
	}

	/**
	 * 去掉多余的0
	 * 
	 * @param f
	 * @return
	 */
	public static String DecZore(String f) {
		try {
			NumberFormat nf = new DecimalFormat("#.##");
			return nf.format(f);
		} catch (Exception e) {
			return f;
		}
	}

	/**
	 * 去掉多余的0
	 * 
	 * @param f
	 * @return
	 */
	public static String DecZore(Float f) {
		NumberFormat nf = new DecimalFormat("#.##");
		return nf.format(f);
	}

	/**
	 * 去掉多余的0
	 * 
	 * @param f
	 * @return
	 */
	public static String DecZore(Double f) {
		NumberFormat nf = new DecimalFormat("#.##");
		return nf.format(f);
	}

	public static String PriceFormat(String Price) {
		if (TextUtils.isEmpty(Price))
			return Price;
		if ("0".equals(Price)) {
			return "免费";
		} else {
			Price = "￥" + Price;
		}
		return Price;
	}

	public static void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}
		int totalHeight = 0;
		for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目
			View listItem = listAdapter.getView(i, null, listView);
			listItem.measure(0, 0); // 计算子项View 的宽高
			totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
		}
		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}
}
