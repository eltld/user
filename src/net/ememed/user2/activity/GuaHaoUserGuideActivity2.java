package net.ememed.user2.activity;

import net.ememed.user2.R;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebSettings.RenderPriority;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

public class GuaHaoUserGuideActivity2 extends BasicActivity {

	private WebView webView;
	private ImageView btn_back;
	private TextView tv_title;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_guahao_user_guider2);
	}

	@Override
	protected void setupView() {
		super.setupView();

		tv_title = (TextView) findViewById(R.id.top_title);
		tv_title.setText("挂号新手指引");
		btn_back = (ImageView) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});

		webView = (WebView) findViewById(R.id.webView1);
		webView.getSettings().setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN); 
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setRenderPriority(RenderPriority.HIGH);
		webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT); // 设置 缓存模式
		// 开启 DOM storage API 功能
		webView.getSettings().setDomStorageEnabled(true);
		// 开启 database storage API 功能
		webView.getSettings().setDatabaseEnabled(true);
		String cacheDirPath = getFilesDir().getAbsolutePath() + getCacheDir();

		// 设置数据库缓存路径
		webView.getSettings().setDatabasePath(cacheDirPath);
		// 设置 Application Caches 缓存目录
		webView.getSettings().setAppCachePath(cacheDirPath);
		// 开启 Application Caches 功能
		webView.getSettings().setAppCacheEnabled(true);
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				loading(null);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				destroyDialog();
			}
		});
		 webView.loadUrl("http://www.ememed.net:8004/normal/html5/guahao_guide");
//		webView.loadUrl("file:///android_asset/guahao_guide.html");
	}

}
