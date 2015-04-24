package net.ememed.user2.medicalhistory.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import net.ememed.user2.R;
import net.ememed.user2.erh.MainActivity;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.medicalhistory.activity.ImageViewZoomActivity;
import net.ememed.user2.medicalhistory.checkImage.CheckImageActivity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class GridViewAdapter extends BaseAdapter {
	 private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener(); 
	private Context mContext;
	private ArrayList<String> mList;

	public GridViewAdapter(Context mContext,
			ArrayList<String> mList) {
		super();
		this.mContext = mContext;
		this.mList = mList;
	}

	@Override
	public int getCount() {
		if (mList == null) {
			return 0;
		} else {
			return this.mList.size();
		}
	}

	@Override
	public Object getItem(int position) {
		if (mList == null) {
			return null;
		} else {
			return this.mList.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(this.mContext).inflate(
					R.layout.gridview_item, null, false);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.gridview_item_image);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		DisplayImageOptions options = new DisplayImageOptions.Builder()  
        .showStubImage(R.drawable.common_defalt_bg)  // 设置图片下载期间显示的图片       
        .cacheInMemory(true)                        // 设置下载的图片是否缓存在内存中  
        .cacheOnDisc(true)                          // 设置下载的图片是否缓存在SD卡中  
        .displayer(new RoundedBitmapDisplayer(20))  // 设置成圆角图片  
        .build();                              
		if (this.mList != null) {
			if (holder.imageView != null) {
				if (mList.size() == 0) {
					holder.imageView.setVisibility(View.GONE);
				} else {
					holder.imageView.setVisibility(View.VISIBLE);
				}
				ImageLoader.getInstance().displayImage(this.mList.get(position), holder.imageView, options, animateFirstListener);
				holder.imageView.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						Bundle bundle = new Bundle();
						bundle.putStringArrayList("urlPaths", mList);
						bundle.putInt("point", position);
						Utils.startActivity(mContext, CheckImageActivity.class, bundle);
					}
				});
			}
		} else {
			holder.imageView.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	/** 
     * 图片加载第一次显示监听器 
     * @author Administrator 
     * 
     */  
    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {  
          
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<String>());  
  
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
                }  
            }  
        }  
    }  

	private class ViewHolder {
		ImageView imageView;
	}
}
