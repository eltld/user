package net.ememed.user2.medicalhistory.adapter;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import net.ememed.user2.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class AdapterMain extends BaseAdapter {
	private Context context;
	private ArrayList<Drawable> datas;

	public AdapterMain(Context context, ArrayList<Drawable> datas) {
		this.context = context;
		this.datas = datas;
	}

	/**
	 *  函数名称 : getCount
	 *  功能描述 :  
	 *  参数说明：
	 *  	@return
	 *  返回值：
	 * 					
	 */
	@Override
	public int getCount() {
		return datas.size();
	}

	/**
	 *  函数名称 : getItem
	 *  功能描述 :  
	 *  参数说明：
	 *  	@param position
	 *  	@return
	 *  返回值：
	 *  	
	 * 					
	 */
	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	/**
	 *  函数名称 : getItemId
	 *  功能描述 :  
	 *  参数说明：
	 *  	@param position
	 *  	@return
	 *  返回值：
	 *  	
	 * 					
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/**
	 *  函数名称 : getView
	 *  功能描述 :  
	 *  参数说明：
	 *  	@param position
	 *  	@param convertView
	 *  	@param parent
	 *  	@return
	 *  返回值：
	 * 					
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = null;
		Viewholder holder = null;
		
		if (convertView == null) {
			inflater = LayoutInflater.from(context);
			
			convertView = inflater.inflate(R.layout.medical_history_adapter_main, null);
			
			holder = new Viewholder();
			
			holder.img_upload = (ImageView) convertView.findViewById(R.id.img_icon);
			
			convertView.setTag(holder);
			
		}else {
			holder = (Viewholder) convertView.getTag();
		}
		
		Drawable drawable = datas.get(position); 
	    BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;  
	    Bitmap bitmap = bitmapDrawable.getBitmap();  
	    BitmapDrawable bbb = new BitmapDrawable(toRoundCorner(bitmap, 30)); 
		holder.img_upload.setBackgroundDrawable(bbb);
//		ImageLoader.getInstance().displayImage(this.mList.get(position), holder.imageView, options, animateFirstListener);
		return convertView;
	}
	
	public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);
		final float roundPx = pixels;
		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		return output;
	}

	class Viewholder{
		private ImageView img_upload;
	}
}
