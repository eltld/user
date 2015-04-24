package net.ememed.user2.activity;

import net.ememed.user2.R;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class AccountRechargeWebActivity extends Activity {
	private WebView webView;
	String url = null;

	Handler handler = new Handler(){
		
	};
	
	 @Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_web_account_recharge);
		url = getIntent().getStringExtra("url");
		webView = (WebView) findViewById(R.id.webView);
		WebSettings webSetting = webView.getSettings();
		// 设置js可用
		webSetting.setJavaScriptEnabled(true);
		
		
		webView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				try {

					Uri uri = Uri.parse(url);
					
					
					System.out.println("@@@@@@@@@@@@@@@@@@@@@@@@ = "+url);
					
					if (null != uri.getScheme()) {
						String scheme = uri.getScheme();
//						if ("http".equalsIgnoreCase(scheme)) {
							view.loadUrl(url);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				return true;
			}

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {


			}

			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				// Log.d(MYTAG, " onReceivedError ");
			}
		});
		
		
		
		// 添加js调用接口
		if (null != url) {
			webView.loadUrl(url);
		}
		
		class JsObject {  
		    @JavascriptInterface
		    public void onCallback() {
		    	System.out.println("回调成功.....");
		    	handler.post(new Runnable() {
					public void run() {
						finish();
					}
				});
		    }; 
		    @JavascriptInterface
		    public void payBack(String pirsce,String msg) {
		    	System.out.println(msg + "  =msg回调成功....."+pirsce);
		    	finish();
		    	Intent mIntent = new Intent(AccountRechargeActivity.class.getName());  
		    	mIntent.putExtra("pirsce", pirsce);
		    	mIntent.putExtra("msg", msg);
                sendBroadcast(mIntent);
		    }; 
		 } 
		webView.addJavascriptInterface(new JsObject(),"EmemedAPI");

	}
	
	
}
