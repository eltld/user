package net.ememed.user2.activity;

import net.ememed.user2.R;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

public class AccountWebActivity extends BasicActivity {
	private WebView wb;
	String userid;
	String url;
	
	TextView top_title;
	private ProgressBar progressBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_web);
		userid = getIntent().getExtras().getString("memberid");
		wb = (WebView) findViewById(R.id.webView);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("账单明细");
		wb.getSettings().setJavaScriptEnabled(true);
		progressBar = (ProgressBar) findViewById(R.id.webview_progress);
		wb.setWebViewClient(new WebViewClient() {
			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressBar.setVisibility(View.VISIBLE);
			}
		});
		
		
		wb.setWebChromeClient(new TestWebChromeClient(new WebChromeClient()) {


			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.setProgress(newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}


		});
		url = HttpUtil.URI + HttpUtil.user_account_detail + userid+"/"+SharePrefUtil.getString(Conast.ACCESS_TOKEN);
		
		System.out.println("url : "+url);
		wb.loadUrl(url);
	}
	
	public void doClick(View v){
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

}
