package de.greenrobot.event.util;

import android.content.Context;
import android.util.Log;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;

public class BDLocationTool {
	
	public MyLocationListener myListener;
	
	LocationClient mLocClient;
	Context context;
	BDLocationCallback bdLocationCallback;
	
	private LocationMode tempMode = LocationMode.Hight_Accuracy;
	private String tempcoor="gcj02";
	
	public BDLocationTool(Context context) {
		this.context = context;
		initLocation();
	}
	
	public void initLocation(){
		
		myListener = new MyLocationListener();
		mLocClient = new LocationClient(context.getApplicationContext());
		mLocClient.registerLocationListener(myListener);
		InitLocation();
	}
	
	private void InitLocation(){
		LocationClientOption option = new LocationClientOption();
		option.setLocationMode(tempMode);//设置定位模式
		option.setCoorType(tempcoor);//返回的定位结果是百度经纬度，默认值gcj02
		int span=5000;
		option.setScanSpan(span);//设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);
		mLocClient.setLocOption(option);
	}
	
	public void startBaiduLocation(BDLocationCallback bdLocationCallback){
		this.bdLocationCallback = bdLocationCallback;
		if (mLocClient != null) {
			mLocClient.start();
		}
		
	}
	public void stopBaiduLocation(){
		if (mLocClient != null) {
			mLocClient.stop();
		}
	}
	
	
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			//Receive Location 
			bdLocationCallback.LocationCallback(location);
			stopBaiduLocation();
		}


	}
}
