package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import net.ememed.user2.R;
import net.ememed.user2.MyApplication;
import net.ememed.user2.entity.BasicInfo;
import net.ememed.user2.entity.DoctorInfo;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Logger;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;
import cn.sharesdk.wechat.friends.Wechat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.ShareEnd;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.AssetManager;
import android.drm.DrmManagerClient.OnEventListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class WebViewActivity extends BasicActivity implements OnRefreshListener {
	protected static final int MSG_404 = 1;
	private TextView tvText;
	private WebView wb;
	private String url;
	private String title;
	private PullToRefreshLayout mPullToRefreshLayout;
	private LinearLayout ll_empty;
	private ImageView iv_right_fun;

	private ProgressBar progressBar;

	private View mModeView;
	private GridView gvShare;
	private PopupWindow mModePopupWindow;
	private ImageView bg;
	private String type;
	private String subtitle;
	private String pic;
	private View fl_top_title;
	private MyApplication application;
	private IWXAPI api;
	private String allowcomment;
	private EditText et_send_comment;
	private Button btn_send;
	private String id;
	private String tabid;
	
	
	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		url = getIntent().getStringExtra("url");
		if (url == null) {
			url = HttpUtil.URI + "/normal/html5/newsDetail/" + getIntent().getStringExtra("ID");
		}
		id = getIntent().getStringExtra("ID");
		title = getIntent().getStringExtra("title");
		type = getIntent().getStringExtra("type");
		tabid = getIntent().getStringExtra("tabid");
		subtitle = getIntent().getStringExtra("subtitle");
		pic = getIntent().getStringExtra("pic");
		allowcomment = getIntent().getStringExtra("allowcomment");
		application = (MyApplication) getApplication();
		application.registerApp();
		api = WXAPIFactory.createWXAPI(this, application.WeChat_ip, false);
		EventBus.getDefault().register(this, ShareEnd.class);
		Logger.dout("url:" + url);
	}

	public void onEvent(ShareEnd event) {
		destroyDialog();
	}
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.webview);

	}
	

	@Override
	protected void setupView() {
		fl_top_title = findViewById(R.id.fl_top_title);
		progressBar = (ProgressBar) findViewById(R.id.webview_progress_normal);
		tvText = (TextView) findViewById(R.id.top_title);
		tvText.setText("薏米资讯");
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		iv_right_fun = (ImageView) findViewById(R.id.iv_right_fun);
		
		et_send_comment = (EditText) findViewById(R.id.et_send_comment);
		
		et_send_comment.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				btn_send.setBackgroundResource(R.drawable.img_consult_send_2);
				et_send_comment.setCompoundDrawables(null, null, null, null);
				return false;
			}
		});

		
		btn_send = (Button) findViewById(R.id.btn_send);
		btn_send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))) {
					Intent i = new Intent(WebViewActivity.this,LoginActivity.class);
					i.putExtra("src", "WebViewActivity");
					startActivity(i);
				}
				
				String comment = et_send_comment.getText().toString();
				if(TextUtils.isEmpty(comment)){
					Toast.makeText(getApplicationContext(), "请输入评论", 0).show();
					return;
				}
				SendComment(comment);
				
			}

			
		});
		LinearLayout ll_comment = (LinearLayout) findViewById(R.id.ll_comment);
		if (!TextUtils.isEmpty(type) && "InfoFragment".equals(type)) {
			iv_right_fun.setVisibility(View.VISIBLE);
			if("1".equals(allowcomment)){
				ll_comment.setVisibility(View.VISIBLE);
			}else{
				ll_comment.setVisibility(View.GONE);
			}
		}

		bg = (ImageView) findViewById(R.id.overlay);
		mModeView = getLayoutInflater().inflate(R.layout.webview_share, null);
	
		wb = (WebView) findViewById(R.id.wv_oauth);
		wb.getSettings().setJavaScriptEnabled(true);
		Map<String, String> map = new HashMap<String, String>();
		map.put("user-agent", "ememed-android");
		wb.loadUrl("user-agent", map);
		
		wb.setOnTouchListener(new OnTouchListener() {  
		  
		    @Override  
		    public boolean onTouch(View v, MotionEvent event) {  

		            
		                if(wb.getScrollY()>wb.getHeight()/4)
		                	{
		                	tvText.setText(title);
		                	}
		                if(wb.getScrollY()<=wb.getHeight()/4)
	                	{
	                	tvText.setText("薏米资讯");
	                	}
		              
		        return false;  
		    }  
		});  
		wb.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {

				if (mPullToRefreshLayout.isRefreshing()) {
					mPullToRefreshLayout.setRefreshComplete();
				}
			}

			public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
				Logger.dout(TAG + " onReceivedError ");
				view.stopLoading();
				view.clearView();
				Message msg = handler.obtainMessage();// 发送通知，加入线程
				msg.what = MSG_404;// 通知加载自定义404页面
				handler.sendMessage(msg);// 通知发送！
			}
		});

		wb.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
				// Log.d(TAG, "onJsAlert");
				return super.onJsAlert(view, url, message, result);
			}

			@Override
			public boolean onJsConfirm(WebView view, String url, String message, JsResult result) {
				// Log.d(TAG, "onJsConfirm");
				return super.onJsConfirm(view, url, message, result);
			}

			@Override
			public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, JsPromptResult result) {
				// Log.d(TAG, "onJsPrompt");
				return super.onJsPrompt(view, url, message, defaultValue, result);
			}

			@Override
			public void onProgressChanged(WebView view, int newProgress) {
				progressBar.setProgress(newProgress);
				if (newProgress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}
		});

		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);

		loadUrl(url);
	}
	/**
	 * 发送评论
	 * @param comment
	 */
	private void SendComment(String comment) {
		// TODO Auto-generated method stub
		
		/**
		 *  1	token	string	是	token加密码
			2	memberid	int	是	用户ID|医生ID
			3	utype	string	是	doctor|user
			4	newsid	int	是	资讯ID
			5	content	string	是	评论内容(限3000字符)
		 */
		
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("utype", "user");
			params.put("newsid", tabid);
			params.put("content", comment);
		
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.ADD_NEWS_COMMENT, BasicInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.ADD_NEWS_COMMENT;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							handler.sendMessage(message);
						}
					});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
		
	}
	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.iv_right_fun) {
			initModePopupWindow();
			// showShare();
		}
	}
	private static final int  onError = 11;
	private static final int  onComplete = 22;
	private static final int  onCancel = 33;
	private void setGridView() {
		gvShare = (GridView) mModeView.findViewById(R.id.gv_share);
		String[] titles = new String[] { "新浪微博", "腾讯微博", "微信", "微信朋友圈", "QQ空间" };
		int[] icons = new int[] { R.drawable.pic_sina_microblog, R.drawable.pic_tencent_microblog, R.drawable.pic_weixin, R.drawable.pic_friend, R.drawable.pic_qq_space };
		String[] from = new String[] { "icon", "title" };
		int[] to = new int[] { R.id.iv_icon, R.id.tv_title };
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < titles.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("icon", icons[i]);
			map.put("title", titles[i]);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.u_gv_item, from, to);
		gvShare.setAdapter(adapter);
		gvShare.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ShareSDK.initSDK(WebViewActivity.this);
				ShareParams sp = new ShareParams();
				String title1 = "";
				loading(null);
				isflag = true;
				switch (position) {
				case 0:// 新浪微博
					sp.setShareType(Platform.SHARE_WEBPAGE);
					if (!TextUtils.isEmpty(subtitle)) {
						title1 = title + subtitle;
					} else {
						title1 = title;
					}
					sp.setText(title1 + " " + url);
//					sp.setImagePath("file://android_asset/ic_launcher.png");
					// sp.setUrl(url);
					Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
					 weibo.setPlatformActionListener(new PlatformActionListener() {
						
						@Override
						public void onError(Platform arg0, int arg1, Throwable arg2) {
							handler.sendEmptyMessage(onError);
						}
						
						@Override
						public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
							handler.sendEmptyMessage(onComplete);
						}
						
						@Override
						public void onCancel(Platform arg0, int arg1) {
							handler.sendEmptyMessage(onCancel);
						}
					}); // 设置分享事件回调
					// 执行图文分享
					weibo.share(sp);

					break;
				case 1:// 腾讯微博

					sp.setTitle(title);
					sp.setTitleUrl(url); // 标题的超链接
					if (!TextUtils.isEmpty(subtitle)) {
						sp.setText(subtitle);
					} else {
						sp.setText("");
					}
					sp.setImageUrl(pic);
					sp.setSiteUrl(url);
					sp.setShareType(Platform.SHARE_WEBPAGE);// （分享网页，既图文分享）
					Platform tencentWeibo = ShareSDK.getPlatform(WebViewActivity.this, TencentWeibo.NAME);
					tencentWeibo.setPlatformActionListener(new PlatformActionListener() {
						
						@Override
						public void onError(Platform arg0, int arg1, Throwable arg2) {
							handler.sendEmptyMessage(onError);
						}
						
						@Override
						public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
							handler.sendEmptyMessage(onComplete);
						}
						
						@Override
						public void onCancel(Platform arg0, int arg1) {
							handler.sendEmptyMessage(onCancel);
						}
					}); // 设置分享事件回调
					// 执行图文分享
					tencentWeibo.share(sp);

					break;
				case 2:// 微信
					boolean flag = api.isWXAppInstalled() && api.isWXAppSupportAPI();
					if (!flag) {
						showToast("请先安装微信");
						return;
					}
					WXWebpageObject webpage0 = new WXWebpageObject();

					WXMediaMessage msg1 = new WXMediaMessage(webpage0);
					Bitmap thumb1 = BitmapFactory.decodeResource(getResources(), R.drawable.wechat_log);
					webpage0.webpageUrl = url;
					String title3 = title;
					if(!TextUtils.isEmpty(subtitle))
						title3 +=("\n"+subtitle);
					msg1.title = title3;
					msg1.description = subtitle;

					// Bitmap bmp = Bitmap.createScaledBitmap(thumb, 150, 150,
					// true);
					// bmp.recycle();
					msg1.thumbData = Util.bmpToByteArray(thumb1, true);

					SendMessageToWX.Req req1 = new SendMessageToWX.Req();
					// req.transaction = buildTransaction("webpage");
					req1.message = msg1;
					req1.scene = SendMessageToWX.Req.WXSceneSession;// 这里设置为好友
					// req.scene = isTimelineCb.isChecked() ?
					// SendMessageToWX.Req.WXSceneTimeline :
					// SendMessageToWX.Req.WXSceneSession;
					api.sendReq(req1);
					break;
				case 3:// 微信
					boolean flag1 = api.isWXAppInstalled() && api.isWXAppSupportAPI();
					if (!flag1) {
						showToast("请先安装微信");
						return;
					}
					WXWebpageObject webpage = new WXWebpageObject();

					WXMediaMessage msg = new WXMediaMessage(webpage);
					Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.wechat_log);
	
					webpage.webpageUrl = url;
					String title2 = title;
					if(!TextUtils.isEmpty(subtitle))
						title2 +=("\n"+subtitle);
					msg.title = title2;
					msg.description = subtitle;

					msg.thumbData = Util.bmpToByteArray(thumb, true);

					SendMessageToWX.Req req = new SendMessageToWX.Req();
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;// 这里设置为朋友圈
					api.sendReq(req);
					break;
				case 4:// QQ空间
					
					sp.setTitle(title);
					sp.setTitleUrl(url); // 标题的超链接
					if (!TextUtils.isEmpty(subtitle)) {
						sp.setText(subtitle);
					} else {
						sp.setText(title);
					}
					sp.setImageUrl(pic);
					sp.setSiteUrl(url);
					sp.setSite("薏米医生");
					sp.setShareType(Platform.SHARE_WEBPAGE);// （分享网页，既图文分享）
					Platform qzone = ShareSDK.getPlatform(QZone.NAME);
					qzone.setPlatformActionListener(new PlatformActionListener() {
						
						@Override
						public void onError(Platform arg0, int arg1, Throwable arg2) {
							handler.sendEmptyMessage(onError);
						}
						
						@Override
						public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
							handler.sendEmptyMessage(onComplete);
						}
						
						@Override
						public void onCancel(Platform arg0, int arg1) {
							handler.sendEmptyMessage(onCancel);
						}
					}); // 设置分享事件回调
				
					qzone.share(sp);
					break;

				default:
					break;
				}
				mModePopupWindow.dismiss();

			}
		});
	}
	@Override
	protected void onStop() {
		if(isflag){
			isflag = false;
			destroyDialog();
		}
		
		super.onStop();
	}
	boolean isflag = false;
	private void initModePopupWindow() {
		ShareSDK.initSDK(this);
		ShareSDK.setConnTimeout(5000);
		ShareSDK.setReadTimeout(10000);
		setGridView();
		if (mModePopupWindow == null) {
			mModePopupWindow = new PopupWindow(mModeView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
			mModePopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mModePopupWindow.setOnDismissListener(new OnDismissListener() {

				@Override
				public void onDismiss() {
					bg.setVisibility(View.GONE);
				}
			});
		}
		if (mModePopupWindow.isShowing()) {
			mModePopupWindow.dismiss();
		} else {
			bg.setVisibility(View.VISIBLE);
			mModePopupWindow.showAsDropDown(fl_top_title, 0, 0);
		}
	}

	@Override
	public void onRefreshStarted(View view) {
		// Here we just reload the webview
		wb.setVisibility(View.VISIBLE);
		mPullToRefreshLayout.setRefreshComplete();
		wb.reload();
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		destroyDialog();
		if (msg.what == MSG_404) {
			ll_empty.setVisibility(View.VISIBLE);
			wb.setVisibility(View.GONE);
		}else if(msg.what==IResult.ADD_NEWS_COMMENT){
			BasicInfo obj = (BasicInfo) msg.obj;
			if(obj!=null){
				showToast(obj.getErrormsg());
				if(obj.getSuccess()==1){//评论成功
					wb.reload();//刷新
					et_send_comment.setText("");
				}
			}
		}else if(msg.what==onError){
			showToast("分享失败");
		}else if(msg.what==onCancel){
			showToast("取消分享");
		}else if(msg.what==onComplete){
			showToast("分享成功");
		
		}
		
	}

	private void loadUrl(String url) {
		if (NetWorkUtils.detect(WebViewActivity.this)) {
			mPullToRefreshLayout.setRefreshing(false);
			wb.loadUrl(url);
		} else {
			progressBar.setVisibility(View.GONE);
			Toast.makeText(this, "网络异常", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onResume() {
		if(isflag){
			isflag = false;
			destroyDialog();
		}
		super.onResume();
	}

	private void showShare() {
		ShareSDK.initSDK(this);
		OnekeyShare oks = new OnekeyShare();

		// 关闭sso授权
		oks.disableSSOWhenAuthorize();
		// 分享时Notification的图标和文字
		// oks.setNotification(R.drawable.ic_launcher,
		// getString(R.string.app_name));
		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getString(R.string.share));

		// imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
		oks.setImageUrl(pic);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// comment是我对这条分享的评论，仅在人人网和QQ空间使用
		oks.setComment("");
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));

		// title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
		oks.setTitle(getResources().getString(R.string.app_name));
		// titleUrl是标题的网络链接，仅在人人网和QQ空间使用
		oks.setTitleUrl(url);
		// text是分享文本，所有平台都需要这个字段
		String title1 = title;
		if (!TextUtils.isEmpty(subtitle))
			title1 += subtitle;
		oks.setText(title + " " + url);
		// imageUrl是图片的网络路径，新浪微博、人人网、QQ空间、
		// 微信的两个平台、Linked-In支持此字段
		oks.setImageUrl(pic);
		// url仅在微信（包括好友和朋友圈）中使用
		oks.setUrl(url);
		// site是分享此内容的网站名称，仅在QQ空间使用
		oks.setSite(getString(R.string.app_name));
		// siteUrl是分享此内容的网站地址，仅在QQ空间使用
		oks.setSiteUrl(url);
		// 是否直接分享（true则直接分享）
		oks.setSilent(true);
		// 指定分享平台，和slient一起使用可以直接分享到指定的平台
		// if (platform != null) {
		// oks.setPlatform(platform);
		// }
		// 去除注释可通过OneKeyShareCallback来捕获快捷分享的处理结果
		// oks.setCallback(new OneKeyShareCallback());
		// 通过OneKeyShareCallback来修改不同平台分享的内容
		// oks.setShareContentCustomizeCallback(new
		// ShareContentCustomizeDemo());

		oks.show(this);
	}



}
