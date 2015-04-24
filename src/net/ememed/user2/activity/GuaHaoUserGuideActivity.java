package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import net.ememed.user2.R;
import net.ememed.user2.activity.DoctorHistoryOrderActivity.ViewHolder;
import net.ememed.user2.task.AsyncImageLoader;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.Util;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.umeng.analytics.MobclickAgent;

public class GuaHaoUserGuideActivity extends BasicActivity{

	private String URL = "http://www.ememed.net/uploads/guahao/zhiyin_";
	private String urls[] = new String[]{
			"www.ememed.net/uploads/guahao/zhiyin_0.png",
			"www.ememed.net/uploads/guahao/zhiyin_1.png",
			"www.ememed.net/uploads/guahao/zhiyin_2.png",
			"www.ememed.net/uploads/guahao/zhiyin_3.png",
			"www.ememed.net/uploads/guahao/zhiyin_4.png",
			"www.ememed.net/uploads/guahao/zhiyin_5.png",
			"www.ememed.net/uploads/guahao/zhiyin_6.png",
			"www.ememed.net/uploads/guahao/zhiyin_7.png",
			"www.ememed.net/uploads/guahao/zhiyin_8.png",
			"www.ememed.net/uploads/guahao/zhiyin_9.png",};
	
	private ViewPager viewpager;
	//private View[] childViews;
	private PagerAdapter adapter;
	//private View[] childDots;
	private OnPageChangeListener listener;
	private ViewGroup group;
	/** 
     * 图片资源id 
     */  
    private int[] imgIdArray = new int[]{R.drawable.logo_douban, R.drawable.logo_dropbox, R.drawable.logo_email, 
    		R.drawable.logo_facebook,  R.drawable.logo_googleplus}; 
    
    /** 
     * 装ImageView数组 
     */  
    private ImageView[] mImageViews;  
    
    /** 
     * 装点点的ImageView数组 
     */  
    private ImageView[] tips;  

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_guahao_user_guider);
	}

	@Override
	protected void setupView() {
		super.setupView();
		LoadPics();
		
		// 初始化ViewPager
		viewpager = (ViewPager) findViewById(R.id.viewpager);
		group = (ViewGroup)findViewById(R.id.viewGroup);
		
		 //将图片装载到数组中 
		 mImageViews = new ImageView[imgIdArray.length];  
         for(int i=0; i<mImageViews.length; i++){  
            ImageView imageView = new ImageView(this);  
            mImageViews[i] = imageView;  
            imageView.setBackgroundResource(imgIdArray[i]);  
         }  
	        
        //将点点加入到ViewGroup中  
        tips = new ImageView[imgIdArray.length];  
        for(int i=0; i<tips.length; i++){  
            ImageView imageView = new ImageView(this);  
            imageView.setLayoutParams(new LayoutParams(10,10));  
            tips[i] = imageView;  
            if(i == 0){  
               // tips[i].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark)); 
            	tips[i].setBackgroundResource(R.drawable.logo_email);
            }else{  
                //tips[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));  
                tips[i].setBackgroundResource(R.drawable.logo_douban);
            }  
              
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(LayoutParams.WRAP_CONTENT,    
                    LayoutParams.WRAP_CONTENT));  
            layoutParams.leftMargin = 5;  
            layoutParams.rightMargin = 5;  
            group.addView(imageView, layoutParams);  
        }  
		
		// 初始化子级页面
		/*childViews = new View[3];
		childViews[0] = View.inflate(this, R.layout.item_guahao_user_guider_pager1, null);
		childViews[1] = View.inflate(this, R.layout.item_guahao_user_guider_pager2, null);
		childViews[2] = View.inflate(this, R.layout.item_guahao_user_guider_pager3, null);*/
		// 初始化标识按钮（点）
	/*	childDots = new View[3];
		childDots[0] = findViewById(R.id.dot1);
		childDots[1] = findViewById(R.id.dot2);
		childDots[2] = findViewById(R.id.dot3);*/
		// 初始化ViewPager的adapter
		adapter = new InnerPagerAdapter();
		// 为ViewPager配置adapter
		viewpager.setAdapter(adapter);
		// 初始化页面切换监听器
		listener = new InnerPageChangeListener();
		// 为ViewPager添加页面切换监听器
		viewpager.setOnPageChangeListener(listener);
		// 为第1个标识按钮（点）添加背景颜色
	//	childDots[0].setBackgroundColor(Color.GREEN);
	}
	
	private void LoadPics(){
		
		for(String url:urls){
			LoadPic(url);
		}
	}
	
	private void LoadPic(String url){
		
		imageLoader.loadImage(url, new ImageLoadingListener() {
			
			@Override
			public void onLoadingStarted(String arg0, View arg1) {
				Logger.iout("加载使用指南图片", "加载开始");
			}
			
			@Override
			public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
				Logger.iout("加载使用指南图片", "加载失败"+ arg2.toString());
			}
			
			@Override
			public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
				Logger.iout("加载使用指南图片", "加载完毕");
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				Logger.iout("加载使用指南图片", "加载取消");
			}
		});
	}
	
	
	/**
	 * ViewPager的adapter
	 * 
	 * @author tarena
	 * 
	 */
	private class InnerPagerAdapter extends PagerAdapter {

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			 ((ViewPager)container).removeView(mImageViews[position % mImageViews.length]);  
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			 ((ViewPager)container).addView(mImageViews[position % mImageViews.length], 0);  
	            return mImageViews[position % mImageViews.length]; 
		}

		@Override
		public int getCount() {
			return mImageViews.length;
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}
	
	/**
	 * 页面切换监听器
	 * 
	 * @author tarena
	 * 
	 */
	private class InnerPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int position) {
		/*	// 把所有的标识按钮（点）全部设置为默认背景颜色
			for (int i = 0; i < childDots.length; i++) {
				childDots[i].setBackgroundColor(Color.GRAY);
			}
			// 设置指定的标识按钮（点）的背景颜色
			childDots[position].setBackgroundColor(Color.GREEN);*/
			setImageBackground(position);
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			// TODO Auto-generated method stub
		}

	}
	
	 /** 
     * 设置选中的页面的tip(点点)的颜色 
     * @param selectItems 
     */  
    private void setImageBackground(int selectItems){  
        for(int i=0; i<tips.length; i++){  
            if(i == selectItems){  
            	 tips[i].setBackgroundColor(getResources().getColor(android.R.color.holo_green_dark));
            }else{  
            	tips[i].setBackgroundColor(getResources().getColor(android.R.color.darker_gray));  
            }  
        }  
    }  

}
