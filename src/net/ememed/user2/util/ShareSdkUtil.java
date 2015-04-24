package net.ememed.user2.util;

import java.util.ArrayList;
import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.PopupWindow.OnDismissListener;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.tencent.weibo.TencentWeibo;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.ShareEnd;

public class ShareSdkUtil implements PlatformActionListener{
	
	private PopupWindow mModePopupWindow;
	private View mModeView;
	private View referenceView;
	private BasicActivity activity;
	private Handler handler;
	private GridView gvShare;
	private String subtitle;
	private String title;
	private String url;
	private String pic;
	private IWXAPI api;
	private MyApplication application;
	
	public static final int  onError = 6001;
	public static final int  onComplete = 6002;
	public static final int  onCancel = 6003;
	
	public static final int share_to_where = -999;
	
		//1新浪微博，2微信，3微信朋友圈，4QQ空间，5QQ好友
	public static final int SHARE_TO_SINA = 1;
	public static final int SHARE_TO_WECHAT = 22;
	public static final int SHARE_TO_WECHAT_MOMENTS = 23;
	public static final int SHARE_TO_QZONE = 6;
	public static final int SHARE_TO_QQ = 24;
	
	public static int getShareToWhere() {
		return share_to_where;
	}
	
	public ShareSdkUtil(BasicActivity activity, Handler handler){
		this.mModeView = activity.getLayoutInflater().inflate(R.layout.webview_share, null);
		this.activity = activity;
		this.handler = handler;
		
		
		ShareSDK.initSDK(activity);
		application = (MyApplication) activity.getApplication();
		application.registerApp();
		api = WXAPIFactory.createWXAPI(activity, application.WeChat_ip, false);
		EventBus.getDefault().register(activity, ShareEnd.class);
	}
	
	public void initModePopupWindow(View referenceView, String title, String subtitle, String url, String pic) {
		this.referenceView = referenceView;
		this.title = title;
		this.subtitle = subtitle;
		this.url = url;
		this.pic = pic;
		setGridView();
		if (mModePopupWindow == null) {
			mModePopupWindow = new PopupWindow(mModeView, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
			mModePopupWindow.setBackgroundDrawable(new BitmapDrawable());
			mModePopupWindow.setOnDismissListener(new OnDismissListener() {
				@Override
				public void onDismiss() {
					activity.destroyDialog();
				}
			});
		}
		if (mModePopupWindow.isShowing()) {
			mModePopupWindow.dismiss();
		} else {
			mModePopupWindow.showAsDropDown(referenceView, 0, 0);
		}
	}
	
	private void setGridView() {
		gvShare = (GridView) mModeView.findViewById(R.id.gv_share);
		String[] titles = new String[] { "新浪微博",  "微信", "微信朋友圈", "QQ空间", "QQ好友"/*,"腾讯微博"*/};
		int[] icons = new int[] { R.drawable.pic_sina_microblog, R.drawable.pic_weixin, 
				R.drawable.pic_friend, R.drawable.pic_qq_space, R.drawable.pic_qq/*, R.drawable.pic_tencent_microblog*/};
		String[] from = new String[] { "icon", "title" };
		int[] to = new int[] { R.id.iv_icon, R.id.tv_title };
		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
		for (int i = 0; i < titles.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("icon", icons[i]);
			map.put("title", titles[i]);
			list.add(map);
		}
		SimpleAdapter adapter = new SimpleAdapter(activity, list, R.layout.u_gv_item, from, to);
		gvShare.setAdapter(adapter);
		gvShare.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				ShareSDK.initSDK(activity);
				ShareParams sp = new ShareParams();
				String title1 = "";
				activity.loading(null);
				//isflag = true;
				switch (position) {
				case 0:// 新浪微博
					if(TextUtils.isEmpty(title)){
						return;
					}
					
					if (!TextUtils.isEmpty(subtitle)) {
						title1 = title + subtitle;
					} else {
						title1 = title;
					}
					sp.setText(title1 + " " + url);
//					sp.setText(title1);
					
					Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
					
					weibo.setPlatformActionListener(ShareSdkUtil.this); // 设置分享事件回调
					weibo.share(sp);

					break;
				/*case 1:// 腾讯微博
					if(TextUtils.isEmpty(title)){
						return;
					}

					sp.setTitleUrl(url); // 标题的超链接
					sp.setTitle(title);
					if (!TextUtils.isEmpty(subtitle)) {
						sp.setText(subtitle);
					} 
					
					sp.setImageUrl(pic);
					sp.setSiteUrl(url);
					sp.setSite("薏米网");
					
					sp.setShareType(Platform.SHARE_WEBPAGE);// （分享网页，既图文分享）
					Platform tencentWeibo = ShareSDK.getPlatform(activity, TencentWeibo.NAME);
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

					break;*/
				case 1:// 微信
					if(TextUtils.isEmpty(title)){
						return;
					}
					
					boolean flag = api.isWXAppInstalled() && api.isWXAppSupportAPI();
					if (!flag) {
						activity.showToast("请先安装微信");
						return;
					}
					WXWebpageObject webpage0 = new WXWebpageObject();

					WXMediaMessage msg1 = new WXMediaMessage(webpage0);
					Bitmap thumb1 = BitmapFactory.decodeResource(activity.getResources(), R.drawable.wechat_log);

					webpage0.webpageUrl = url;
					String title3 = title;
					if(!TextUtils.isEmpty(subtitle))
						title3 +=("\n"+subtitle);
					msg1.title = title3;
					msg1.description = subtitle;

					msg1.thumbData = Util.bmpToByteArray(thumb1, true);

					SendMessageToWX.Req req1 = new SendMessageToWX.Req();
					req1.message = msg1;
					req1.scene = SendMessageToWX.Req.WXSceneSession;// 这里设置为好友
					api.sendReq(req1);
					break;
				case 2:// 微信朋友圈
					if(TextUtils.isEmpty(title)){
						return;
					}
					boolean flag1 = api.isWXAppInstalled() && api.isWXAppSupportAPI();
					if (!flag1) {
						activity.showToast("请先安装微信");
						return;
					}
					WXWebpageObject webpage = new WXWebpageObject();

					WXMediaMessage msg = new WXMediaMessage(webpage);
					Bitmap thumb = BitmapFactory.decodeResource(activity.getResources(), R.drawable.wechat_log);

					webpage.webpageUrl = url;
					String title2 = title;
					if(!TextUtils.isEmpty(subtitle))
						title2 +=("\n"+subtitle);
					msg.title = title;
					msg.description = subtitle;

					msg.thumbData = Util.bmpToByteArray(thumb, true);

					SendMessageToWX.Req req = new SendMessageToWX.Req();
					// req.transaction = buildTransaction("webpage");
					req.message = msg;
					req.scene = SendMessageToWX.Req.WXSceneTimeline;// 这里设置为朋友圈
					api.sendReq(req);
					break;
				case 3:// QQ空间
					if(TextUtils.isEmpty(title)){
						return;
					}
					sp.setTitle(title);
					sp.setTitleUrl(url); // 标题的超链接
					if (!TextUtils.isEmpty(subtitle)) {
						sp.setText(subtitle);
					} else {
						sp.setText(title);
					}
					sp.setImageUrl(pic);
					sp.setSiteUrl(url);
					sp.setSite("薏米网");
					sp.setShareType(Platform.SHARE_WEBPAGE);// （分享网页，既图文分享）
					Platform qzone = ShareSDK.getPlatform(activity, QZone.NAME);
					qzone.setPlatformActionListener(ShareSdkUtil.this); // 设置分享事件回调
					// qzone. setPlatformActionListener (null); // 设置分享事件回调
					// 执行图文分享
					qzone.share(sp);
					break;
				case 4:
					if(TextUtils.isEmpty(title)){
						return;
					}
					Platform qq = ShareSDK.getPlatform(QQ.NAME);
					sp.setTitleUrl(url); // 标题的超链接
					sp.setTitle(title);
					if (!TextUtils.isEmpty(subtitle)) {
						sp.setText(subtitle);
					} 
					
					sp.setImageUrl(pic);
					sp.setSiteUrl(url);
					sp.setSite("薏米网");
					
					qq.setPlatformActionListener(ShareSdkUtil.this);
					qq.share(sp);
					
					break;
				default:
					break;
				}
				mModePopupWindow.dismiss();
			}
		});
	}

	@Override
	public void onCancel(Platform arg0, int arg1) {
		handler.sendEmptyMessage(onCancel);
	}

	@Override
	public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
		Message message = new Message();
		message.what = onComplete;
		if(arg0.getName().trim().equals("QQ")){
			message.arg1 = SHARE_TO_QQ;
		} else if(arg0.getName().trim().equals("QZone")){
			message.arg1 = SHARE_TO_QZONE;
		} else if(arg0.getName().trim().equals("SinaWeibo")){
			message.arg1 = SHARE_TO_SINA;
		} else if(arg0.getName().trim().equals("Wechat")){
			message.arg1 = SHARE_TO_WECHAT;
		} else if(arg0.getName().trim().equals("WechatMoments")){
			message.arg1 = SHARE_TO_WECHAT_MOMENTS;
		}
		
		Log.i("测试", "分享到"+ arg0.getName());
		
		handler.sendMessage(message);
	}

	@Override
	public void onError(Platform arg0, int arg1, Throwable arg2) {
		handler.sendEmptyMessage(onError);
	}
}
