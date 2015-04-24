package de.greenrobot.event.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.text.Html.ImageGetter;
import android.view.Display;
import android.widget.TextView;

public class URLImageGetter implements ImageGetter {

	TextView textView;
	Context context;

	public URLImageGetter(Context contxt, TextView textView) {
		this.context = contxt;
		this.textView = textView;
	}

	@Override
	public Drawable getDrawable(String paramString) {
		URLDrawable urlDrawable = new URLDrawable(context);

		ImageGetterAsyncTask getterTask = new ImageGetterAsyncTask(urlDrawable);
		getterTask.execute(paramString);
		return urlDrawable;
	}

	public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {
		URLDrawable urlDrawable;

		public ImageGetterAsyncTask(URLDrawable drawable) {
			this.urlDrawable = drawable;
		}

		@Override
		protected void onPostExecute(Drawable result) {
			if (result != null) {
				urlDrawable.drawable = result;

				URLImageGetter.this.textView.requestLayout();
			}
		}

		@Override
		protected Drawable doInBackground(String... params) {
			String source = params[0];
			return fetchDrawable(source);
		}

		public Drawable fetchDrawable(String url) {
			Drawable drawable = null;
			URL Url;
			try {
				Url = new URL(url);
				drawable = Drawable.createFromStream(Url.openStream(), "");
				// Rect bounds = getDefaultImageBounds(context);
				// BitmapFactory.decodeStream(Url.openStream(), bounds, opts)
			} catch (Exception e) {
				return null;
			}
			// 按比例缩放图片
			Rect bounds = getDefaultImageBounds(context);
			int newwidth = bounds.width();
			int newheight = bounds.height();
			// double factor = 1;
			// double fx = (double) drawable.getIntrinsicWidth()
			// / (double) newwidth;
			// double fy = (double) drawable.getIntrinsicHeight()
			// / (double) newheight;
			// factor = fx > fy ? fx : fy;
			// if (factor < 1)
			// factor = 1;
			// newwidth = (int) (drawable.getIntrinsicWidth() / factor);
			// newheight = (int) (drawable.getIntrinsicHeight() / factor);

			drawable.setBounds(0, 0, newwidth, newheight);

			return drawable;
		}
	}

	// 预定图片宽高比例为 4:3
	@SuppressWarnings("deprecation")
	public Rect getDefaultImageBounds(Context context) {
		Display display = ((Activity) context).getWindowManager()
				.getDefaultDisplay();
		int width = display.getWidth();
		// int height = (int) (width * 3 / 4);
		int height = display.getHeight();

		Rect bounds = new Rect(0, 0, width, height);
		return bounds;
	}

	public void compressImage(InputStream stream) {

	}

	public void inputstreamtofile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Bitmap createNewBitmapAndCompressByFile(String filePath, int wh[]) {
		int offset = 100;
		File file = new File(filePath);
		long fileSize = file.length();
		if (200 * 1024 < fileSize && fileSize <= 1024 * 1024)
			offset = 90;
		else if (1024 * 1024 < fileSize)
			offset = 85;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 为true里只读图片的信息，如果长宽，返回的bitmap为null
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		options.inDither = false;
		/**
		 * 计算图片尺寸 //TODO 按比例缩放尺寸
		 */
		BitmapFactory.decodeFile(filePath, options);

		int bmpheight = options.outHeight;
		int bmpWidth = options.outWidth;
		int inSampleSize = bmpheight / wh[1] > bmpWidth / wh[0] ? bmpheight
				/ wh[1] : bmpWidth / wh[0];
		// if(bmpheight / wh[1] < bmpWidth / wh[0]) inSampleSize = inSampleSize
		// * 2 / 3;//TODO 如果图片太宽而高度太小，则压缩比例太大。所以乘以2/3
		if (inSampleSize > 1)
			options.inSampleSize = inSampleSize;// 设置缩放比例
		options.inJustDecodeBounds = false;

		InputStream is = null;
		try {
			is = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			return null;
		}
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream(is, null, options);
		} catch (OutOfMemoryError e) {

			System.gc();
			bitmap = null;
		}
		if (offset == 100)
			return bitmap;// 缩小质量
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, offset, baos);
		byte[] buffer = baos.toByteArray();
		options = null;
		if (buffer.length >= fileSize)
			return bitmap;
		return BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
	}
}
