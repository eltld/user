package net.ememed.user2.medicalhistory.checkImage;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Gallery;
/**
 * Gallery的适配器类，主要用于加载图片
 * @author lyc
 *
 */
public class GalleryAdapter extends BaseAdapter {
	private ArrayList<Bitmap> bms = new ArrayList<Bitmap>();
	private Context context;
	private int point;

	public GalleryAdapter(Context context, ArrayList<Bitmap> bms, int point) {
		this.context = context;
		this.bms = bms;
		this.point = point;
	}

	@Override
	public int getCount() {
		if (bms.size() == 0) {
			return 0;
		} else {
			return bms.size();
		}
		
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		//每次移动获取图片并重新加载，当图片很多时可以构造函数就把bitmap引入并放入list当中，
		//然后在getview方法当中取来直接用
		Bitmap bmp = bms.get(position);
		MyImageView view = new MyImageView(context, bmp.getWidth(), bmp.getHeight());
		view.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		view.setImageBitmap(bmp);
		return view;
	}

}
