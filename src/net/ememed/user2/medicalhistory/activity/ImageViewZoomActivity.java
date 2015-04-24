package net.ememed.user2.medicalhistory.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.medicalhistory.activity.view.ImageZoomState;
import net.ememed.user2.medicalhistory.activity.view.ImageZoomView;
import net.ememed.user2.medicalhistory.activity.view.SimpleImageZoomListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ImageViewZoomActivity extends BasicActivity {
	// private ZoomControls zoomCtrl;// 系统自带的缩放控制组件
	private ImageZoomView zoomView;// 自定义的图片显示组件
	private ImageZoomState zoomState;// 图片缩放和移动状态类
	private SimpleImageZoomListener zoomListener;// 缩放事件监听器
	private Bitmap bitmap;// 要显示的图片位图
	private GestureDetector detector;
	private RelativeLayout rl_image;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.image_zoom_layout);
		String url1 = getIntent().getExtras().getString("url");
		zoomState = new ImageZoomState();
		zoomListener = new SimpleImageZoomListener();
		zoomListener.setZoomState(zoomState);
		this.setImageController();
		rl_image = (RelativeLayout) findViewById(R.id.rl_image);
		rl_image.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		zoomView = (ImageZoomView) findViewById(R.id.zoomView);
		zoomView.setImageZoomState(zoomState);
		zoomView.setOnTouchListener(zoomListener);
		zoomView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setFullScreen();
			}
		});
		getImage(url1);
	}
	
	private void getImage(final String path) {
		loading(null);
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					URL url = new URL(path);
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setConnectTimeout(5000);
					conn.setRequestMethod("GET");
					if (conn.getResponseCode() == 200) {
						InputStream inputStream = conn.getInputStream();
						bitmap = BitmapFactory.decodeStream(inputStream);
						handler.sendEmptyMessage(0x12);
					}
				} catch (IOException e) {

					e.printStackTrace();
				}
			}
		}).start();
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			if (msg.what == 0x12) {
				destroyDialog();
				zoomView.setImage(bitmap);
			}
		}

	};

	private void setImageController() {
		// zoomCtrl.setOnZoomInClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// float z = zoomState.getmZoom() + 0.25f;// 图像大小增加原来的0.25倍
		// if(z <2.0f)
		// {
		// zoomState.setmZoom(z);
		// zoomState.notifyObservers();
		// zoomCtrl.setIsZoomInEnabled(true);
		// }
		// else
		// {
		// zoomCtrl.setIsZoomInEnabled(false);
		// }
		//
		// }
		// });
		// zoomCtrl.setOnZoomOutClickListener(new OnClickListener()
		// {
		// @Override
		// public void onClick(View v)
		// {
		// float z = zoomState.getmZoom() - 0.25f;// 图像大小减少原来的0.25倍
		// zoomState.setmZoom(z);
		// zoomState.notifyObservers();
		// zoomCtrl.setIsZoomInEnabled(true);
		//
		// }
		// });
	}

	/**
	 * 隐藏处ImageZoomView外地其他组件，全屏显示
	 */
	private void setFullScreen() {
		// if (zoomCtrl != null)
		// {
		// if (zoomCtrl.getVisibility() == View.VISIBLE)
		// {
		// // zoomCtrl.setVisibility(View.GONE);
		// // zoomCtrl.hide(); // 有过度效果
		//
		// }
		// else if (zoomCtrl.getVisibility() == View.GONE)
		// {
		// // zoomCtrl.setVisibility(View.VISIBLE);
		// // zoomCtrl.show();// 有过渡效果
		//
		// }
		// }
	}
	
	public void doClick(View view) {
		if (view.getId() == R.id.rl_image) {
			finish();
		} 
	}

}