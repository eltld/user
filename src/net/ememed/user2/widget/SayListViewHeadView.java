package net.ememed.user2.widget;

import net.ememed.user2.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class SayListViewHeadView extends LinearLayout{

	private Context context;
	private View mainView;
	private WebView wb;
	private ProgressBar webview_progress;
	private Button give_bnt;
	private Button praise_bnt;
	private TextView discuss_tx;
	private WebSettings webSettings;
	public SayListViewHeadView(Context context) {
		super(context);
		this.context = context;
		mainView = LayoutInflater.from(context).inflate(R.layout.saylistview_head,
				this);
		initView();
	}
	public SayListViewHeadView(Context context, AttributeSet attrs){   
        super(context, attrs);   
        this.context = context;
    	mainView = LayoutInflater.from(context).inflate(R.layout.saylistview_head,
				this);
		initView();
    } 
	public void initView() {
		wb = (WebView) mainView.findViewById(R.id.wv_oauth);
		webview_progress = (ProgressBar) mainView.findViewById(R.id.webview_progress);
		give_bnt = (Button) mainView.findViewById(R.id.give_bnt);
		praise_bnt = (Button) mainView.findViewById(R.id.praise_bnt);
		discuss_tx = (TextView) mainView.findViewById(R.id.discuss_tx);
		
		
		wb.setFocusable(true);
		wb.requestFocus();
		webSettings = wb.getSettings();
		webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
		webSettings.setJavaScriptEnabled(true);
		webSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);  
		webSettings.setUseWideViewPort(true);
		//自带缩放
		webSettings.setSupportZoom(true);  
		webSettings.setBuiltInZoomControls(true);
		webSettings.setLoadWithOverviewMode(true);
		wb.setWebViewClient(new WebViewClient(){
			
			/**
			 * 在点击请求的是链接是才会调用，重写此方法返回true表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
			 */
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				super.onPageStarted(view, url, favicon);
				webview_progress.setVisibility(View.VISIBLE);
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				
				
			}
			
		});
		
		wb.setWebChromeClient(new WebChromeClient(){
			
			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				// TODO Auto-generated method stub
				super.onProgressChanged(view, newProgress);
				webview_progress.setProgress(newProgress);
				if(newProgress==100){
					webview_progress.setVisibility(View.GONE);
				}
			}
			
		});
	}
	
	public void setDiscuss(String number){
		discuss_tx.setText("共"+number+"条评论");
	}
	public void loadUrl(String url){
		wb.loadUrl(url);
	}
	
	
}
