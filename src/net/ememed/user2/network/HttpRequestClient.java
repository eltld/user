package net.ememed.user2.network;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

public class HttpRequestClient {

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.get(getAbsoluteUrl(url), params, responseHandler);
	}

	public static void post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrl(url), params, responseHandler);
	}
	
	public static void postGuahao(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		client.post(getAbsoluteUrlGuahao(url), params, responseHandler);
	}

	private static String getAbsoluteUrl(String relativeUrl) {
		return HttpUtil.URI + relativeUrl;
	}
	
	private static String getAbsoluteUrlGuahao(String relativeUrl) {
		return HttpUtil.GUAHAO + relativeUrl;
	}
}
