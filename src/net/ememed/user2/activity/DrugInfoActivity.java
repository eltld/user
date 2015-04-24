package net.ememed.user2.activity;
import net.ememed.user2.R;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.network.HttpUtil;

import com.umeng.analytics.MobclickAgent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class DrugInfoActivity extends BasicActivity {
	ConnectivityManager manager;
	private WebView webview;
	private TextView tvtitle;

	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.u_drug_info);
	}

	public void setupView() {
		tvtitle = (TextView) findViewById(R.id.top_title);
		tvtitle.setText(getString(R.string.drag_consult_info));
		webview = (WebView) findViewById(R.id.webview);
		webview.setScrollBarStyle(0);
		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				cancleProgressDialog();
			}
		});

	}

	@Override
	protected void getData() {
		if (isNetworkAvailable()) {
			loadWeb();
		} else {
			showToast(IMessage.NET_ERROR);
		}
		super.getData();
	}

	/** 加载网页 */
	public void loadWeb() {
		int id = getIntent().getIntExtra("id", -1);
		if (id != -1) {
			webview.loadUrl(HttpUtil.PRODUCT_URI + id);
		}
	}
	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		}
	}
	public boolean isNetworkAvailable() {
		NetworkInfo[] info = manager.getAllNetworkInfo();
		if (info != null) {
			for (int i = 0; i < info.length; i++) {
				if (info[i].getState() == NetworkInfo.State.CONNECTED) {
					return true;
				}
			}
		}
		return false;
	}
}
