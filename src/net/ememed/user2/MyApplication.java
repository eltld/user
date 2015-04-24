package net.ememed.user2;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.ememed.user2.activity.DoctorDetailInfoActivity;
import net.ememed.user2.cache.Cache;
import net.ememed.user2.db.UserDao;
import net.ememed.user2.entity.ChatUser;
import net.ememed.user2.entity.MessageBackupFieldEntry;
import net.ememed.user2.entity.MobileParam;
import net.ememed.user2.exception.CrashHandler;
import net.ememed.user2.network.HttpService;
import net.ememed.user2.network.VolleyHttpClient;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;
import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMChatOptions;
import com.easemob.chat.EMMessage;
import com.easemob.chat.OnMessageNotifyListener;
import com.easemob.chat.OnNotificationClickListener;
import com.easemob.exceptions.EaseMobException;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.MemoryCacheAware;
import com.nostra13.universalimageloader.cache.memory.impl.LRULimitedMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

public class MyApplication extends Application{
	public static MyApplication app;	
	public static String WeChat_ip = "wxb7d72c1505c92eea";
	public static boolean isLoginIMing =false;
	public static boolean isLoginIM =false;
	public static VolleyHttpClient volleyHttpClient;
	public static HttpService httpService;
	private static Context context;
	private static DisplayMetrics dm = new DisplayMetrics();
	private static Toast toast = null;
	private static Vibrator vibrator;
	public static String WECHAT_SHARE_SUCCESS = "wechat_share_success";
	public static String GET_CONTACT_LIST = "get_contact_list";
	public static String GET_BOUNTY_LIST = "get_bounty_list";
	
	public static String current_city = "广州市";//
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		if (processAppName == null || processAppName.equals("")) {
			// workaround for baidu location sdk 3.3
			// 百度定位sdk3.3，定位服务运行在一个单独的进程，每次定位服务启动的时候，都会调用application::onCreate
			// 创建新的进程。
			// 但环信的sdk只需要在主进程中初始化一次。 这个特殊处理是，如果从pid 找不到对应的processInfo
			// processName，
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		WindowManager wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metric = new DisplayMetrics();
		wm.getDefaultDisplay().getMetrics(metric);
		canvasWidth = metric.widthPixels;
		canvasHeight = metric.heightPixels;
		
//		CrashHandler.getInstance().init(this);
		
		
		app = this;
		Cache.newInstance(getApplicationContext());
		httpService = HttpService.newInstance(getApplicationContext());
		volleyHttpClient = VolleyHttpClient.newInstance(httpService);
        context = this.getApplicationContext();
		JPushInterface.setDebugMode(true); 	//设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
        JPushInterface.setAlias(this, JPushInterface.getUdid(this), new TagAliasCallback() {
			
			@Override
			public void gotResult(int returnCode, String alias, Set<String> arg2) {
		        String logs ;
		        switch (returnCode) {
		        case 0:
		            break;
		        case 6002:
		            break;
		        default: 
		        }
			}
		});
        toast = Toast.makeText(MyApplication.this, "",Toast.LENGTH_LONG);
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(MyApplication.this);
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;//设置为自动消失
        JPushInterface.setPushNotificationBuilder(1, builder);
        initImageLoader(this);
        
    	// 初始化环信SDK,一定要先调用init()
//		Log.d("EMChat Demo", "initialize EMChat SDK");
		EMChat.getInstance().init(app);
		// debugmode设为true后，就能看到sdk打印的log了
		EMChat.getInstance().setDebugMode(true);

		// 获取到EMChatOptions对象
		EMChatOptions options = EMChatManager.getInstance().getChatOptions();
		// 默认添加好友时，是不需要验证的，改成需要验证
		options.setAcceptInvitationAlways(false);
		// 设置收到消息是否有新消息通知，默认为true
		options.setNotificationEnable(true);
		// 设置收到消息是否有声音提示，默认为true
		options.setNoticeBySound(true);
		// 设置收到消息是否震动 默认为true
		options.setNoticedByVibrate(false);
		// 设置语音消息播放是否设置为扬声器播放 默认为true
		options.setUseSpeaker(true);
		//设置notification消息点击时，跳转的intent为自定义的intent
		options.setOnNotificationClickListener(new OnNotificationClickListener() {
			
			@Override
			public Intent onNotificationClick(EMMessage message) {
				String extStr;
				MessageBackupFieldEntry entry = null;
				try {
					extStr = message.getStringAttribute("ext");
					Gson gson = new Gson();
					entry = gson.fromJson(extStr,MessageBackupFieldEntry.class);
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
				String doctor_avatar = ""; 
				String doctor_name = "";
				if (null != entry) {
					if (!TextUtils.isEmpty(entry.getISSYSTEMMSG()) && "1".equals(entry.getISSYSTEMMSG())) {
						return null;	
					} else {
						doctor_avatar = entry.getDoctor_avatar();
						doctor_name = entry.getDoctor_name();
						Intent intent = new Intent(context, DoctorDetailInfoActivity.class);
						intent.putExtra("tochat_userId", message.getFrom());
						intent.putExtra("doctor_name", doctor_name);// 医生姓名
						intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
						return intent;			
					}
				} else {
					return null;	
				}
			}
		});
		//取消注释，app在后台，有新消息来时，状态栏的消息提示换成自己写的
		options.setNotifyText(new OnMessageNotifyListener() {

			@Override
			public int onSetSmallIcon(EMMessage arg0) {
				// TODO Auto-generated method stub
				return 0;
			}

			 public String onSetNotificationTitle(EMMessage arg0) {
			 // TODO Auto-generated method stub
			 return null;
			 }

			@Override
			public String onNewMessageNotify(EMMessage message) {
				// TODO Auto-generated method stub
				String extStr;
				MessageBackupFieldEntry entry = null;
				try {
					extStr = message.getStringAttribute("ext");
					Gson gson = new Gson();
					entry = gson
							.fromJson(extStr, MessageBackupFieldEntry.class);
				} catch (EaseMobException e) {
					e.printStackTrace();
				}
				if (null != entry) {
					if (!TextUtils.isEmpty(entry.getISSYSTEMMSG())
							&& "1".equals(entry.getISSYSTEMMSG())) {
						return null;
					} else {
						// 可以根据message的类型提示不同文字，demo简单的覆盖了原来的提示
						return "您有新的消息";
					}
				} else {
					// 可以根据message的类型提示不同文字，demo简单的覆盖了原来的提示
					return "您有新的消息";
				}
			}

			 public String onLatestMessageNotify(EMMessage message, int
			 fromUsersNum, int messageNum) {
			 // return fromUsersNum + "个用户，发来了" + messageNum + "条消息";
			 return "您有新的消息";
			 }
		});
		
	}
	
	public void registerApp(){
		final IWXAPI api = WXAPIFactory.createWXAPI(this,WeChat_ip, false);
		api.registerApp(WeChat_ip);
	}
	
	/**
     * toast信息提示(long)
     * @param msg
     */
    public static void toastMakeTextLong(String msg){
    	app.handlerToast.sendMessage(app.handlerToast.obtainMessage(2, msg));
    }
    
    public static int getScreenWidth() {
		WindowManager wm = (WindowManager) app.getSystemService(Context.WINDOW_SERVICE);
		return wm.getDefaultDisplay().getWidth();
	}
    
    public final Handler handlerToast = new Handler() {
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				
				case 2:
					if(msg.obj instanceof String)
						toast.setText(String.valueOf(msg.obj));
					else
						toast.setText(Integer.valueOf(""+msg.obj));
					toast.show();
					vibrate();
					break;
				default:
					if(msg.obj instanceof String)
						toast.setText(String.valueOf(msg.obj));
					else
						toast.setText(Integer.valueOf(""+msg.obj));
					toast.show();
					break;
				}
			} catch (NumberFormatException e) {
				Log.e("MyApp", e.getMessage(), e);
			}
		};
	};
	
	/**
	 * 震动  50 毫秒
	 */
    public static void vibrate() {
    	vibrate(50);
	}
    
    /**
	 * 震动 
	 * @param milliseconds
	 */
    public static void vibrate(long milliseconds) {
    	if(vibrator == null)
    		vibrator  = (Vibrator) getInstance().getSystemService(Service.VIBRATOR_SERVICE);
    	vibrator.vibrate(milliseconds);
	}

	public static Context getContext() {
	       return context;
	}
	public static MyApplication getInstance() {
		return app;
	}
	public static void initImageLoader(Context context) {
		int memoryCacheSize = (int) (Runtime.getRuntime().maxMemory() / 8);

		MemoryCacheAware<String, Bitmap> memoryCache;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			memoryCache = new LruMemoryCache(memoryCacheSize);
		} else {
			memoryCache = new LRULimitedMemoryCache(memoryCacheSize);
		}

		// This configuration tuning is custom. You can tune every option, you may tune some of them, 
		// or you can create default configuration by
		//  ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
				.threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密  
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())  // Not necessary in common
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}
	
	public static DisplayMetrics getDisplayMetrics(){
		return dm;
	}
	
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm.getApplicationInfo(info.processName, PackageManager.GET_META_DATA));
					// Log.d("Process", "Id: "+ info.pid +" ProcessName: "+
					// info.processName +"  Label: "+c.toString());
					// processName = c.toString();
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
//				 Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	
	private Map<String, ChatUser> contactList;

	public int canvasWidth;

	public int canvasHeight;
	/**
	 * 获取内存中好友user list
	 * 
	 * @return
	 */
	public Map<String, ChatUser> getContactList() {
		if(SharePrefUtil.getString(Conast.MEMBER_ID) != null &&contactList == null){
			UserDao dao = new UserDao(getContext());
			// 获取本地好友user list到内存,方便以后获取好友list
			contactList = dao.getContactList();
		}
		return contactList;
	}

	/**
	 * 设置好友user list到内存中
	 * 
	 * @param contactList
	 */
	public void setContactList(Map<String, ChatUser> contactList) {
		this.contactList = contactList;
	}
	
	/**
	 * 获取设备信息
	 * @return
	 */
	@SuppressLint("NewApi")
	static public String getMobileParam(Context context){
		TelephonyManager telephonyManager = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		MobileParam param = new MobileParam();
		Build bd = new Build();
		param.setMobile_model(bd.BRAND);		//机器型号（xiaomi）
		param.setMobile_version(bd.MODEL);		//机器版本（MI 3）
		param.setMobile_alias(bd.HOST);			//别名
		param.setMobile_sysversion(Build.VERSION.RELEASE);
		
		int type = telephonyManager.getNetworkType();
		String networkType;
		if(type == TelephonyManager.NETWORK_TYPE_GPRS){
			networkType = "GPRS";
		} else if(type == TelephonyManager.NETWORK_TYPE_EDGE){
			networkType = "EDGE";
		} else if(type == TelephonyManager.NETWORK_TYPE_UMTS){
			networkType = "UMTS";
		} else if(type == TelephonyManager.NETWORK_TYPE_CDMA){
			networkType = "CDMA";
		} else if(type == TelephonyManager.NETWORK_TYPE_EVDO_0){
			networkType = "EVDO_0";
		} else if(type == TelephonyManager.NETWORK_TYPE_EVDO_A){
			networkType = "EVDO_A";
		} else if(type == TelephonyManager.NETWORK_TYPE_1xRTT){
			networkType = "1xRTT";
		} else if(type == TelephonyManager.NETWORK_TYPE_HSDPA){
			networkType = "HSDPA";
		}else if(type == TelephonyManager.NETWORK_TYPE_HSUPA){
			networkType = "HSUPA";
		}else if(type == TelephonyManager.NETWORK_TYPE_HSPA){
			networkType = "HSPA";
		} else{
			networkType = "未知网络";
		}
		
		param.setMobile_network(networkType);	//网络制式  GPRS
		param.setMobile_operator(telephonyManager.getNetworkOperatorName());	//CHINA BOBILE
		
		Gson gson = new Gson();
		String strJson = gson.toJson(param);
		return strJson;
	}
	
	public static void logoutThird(Context context) {
		ShareSDK.initSDK(context);
		Platform qq = new QQ(context);
		if (qq.isValid()) {
			qq.removeAccount();
		}

		Platform weibo = new SinaWeibo(context);
		if (weibo.isValid()) {
			weibo.removeAccount();
		}

		Platform wechat = new Wechat(context);
		if (wechat.isValid()) {
			wechat.removeAccount();
		}
	}
	
}
