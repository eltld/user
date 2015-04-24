package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MemberInfo;
import net.ememed.user2.entity.MessageBackupFieldEntry;
import net.ememed.user2.entity.MessageSystemEntry;
import net.ememed.user2.fragment.DoctorBasicInfoFragment;
import net.ememed.user2.fragment.DoctorBasicInfoFragment.DotorListener;
import net.ememed.user2.fragment.DoctorChatFragment;
import net.ememed.user2.fragment.DoctorOrderFragment;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.exceptions.EaseMobException;
import com.viewpagerindicator.CustomViewPager;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.MessageSystemEvent;
import de.greenrobot.event.MessageSystemEvent.ServiceType;
import de.greenrobot.event.RegisterSuccessEvent;

/***
 * 聊天、订单、简介 容器
 * 
 * @author chen
 * 
 */
public class DoctorDetailInfoActivity extends BasicActivity implements
		CustomViewPager.OnPageChangeListener, DotorListener {

	private PagerAdapter mPagerAdapter = null;
	public CustomViewPager mViewPager = null;
	private Button btn;
	private String doctor_name;
	public String tochat_userId;
	public String push;
	private String doctor_avatar;
	private MessageSystemEvent event = null;
	private static DoctorDetailInfoActivity activity;

	PageReceive pageReceive;

	private boolean mIsFollow;

	public Button btn_addhealth;

	private DoctorOrderFragment mOrderFragment;
	private DoctorBasicInfoFragment basicInfoFragment;

	
	private TextView iv_red_dot;
	
	

	// private FragmentViewPageAddpter pageAddpter;

	public static DoctorDetailInfoActivity getActivity() {
		return activity;
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.doctor_person_info);
		doctor_name = getIntent().getStringExtra("doctor_name");
		doctor_avatar = getIntent().getStringExtra("doctor_avatar");
		tochat_userId = getIntent().getStringExtra("tochat_userId");
		mIsFollow = getIntent().getBooleanExtra("isFollow", false);
		push = getIntent().getStringExtra("push");
		activity = this;
		connectionIM();
		sendSystemMsgOperation();
		pageReceive = new PageReceive();
		IntentFilter filter = new IntentFilter(
				DoctorDetailInfoActivity.class.getName());
		regMSG();
		registerReceiver(pageReceive, filter);
	}

	
	
	/**
	 * 发送聊天页系统消息
	 */
	public void sendSystemMsgOperation() {
		String from_activity = getIntent().getStringExtra("from_activity");
		String orderid = getIntent().getStringExtra("orderid");
		String orderstate = getIntent().getStringExtra("orderstate");
		String serviceid = getIntent().getStringExtra("serviceid");
		if (!TextUtils.isEmpty(from_activity)) {

			if (from_activity.equals(BuyCustomActivity.class.getSimpleName())) {

				if (!TextUtils.isEmpty(orderstate) && "2".equals(orderstate)) {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("14");
					msgEntry.setOrder_state("16");// 线下业务状态，2代表已支付
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));

					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_sys_offline_payed_to_user));
					systemEntry
							.setDoctor_msg((getString(R.string.txt_sys_offline_payed_to_doctor))
									.replace("{service}",
											PublicUtil.getServiceNameByid("16")));

					event = new MessageSystemEvent(ServiceType.SERVICE_CUSTOM,
							msgEntry, systemEntry);

				} else {

					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");// 系统消息：1
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("16");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));

					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_thank_offline)
									+ doctor_name
									+ getString(R.string.chat_sys_offine_service_custom));
					systemEntry.setDoctor_msg(PublicUtil
							.getServiceContentByid("16"));
					event = new MessageSystemEvent(ServiceType.SERVICE_CUSTOM,
							msgEntry, systemEntry);
				}

			} else if (from_activity.equals(BuyJiuyiActivity.class
					.getSimpleName())) {
				if (!TextUtils.isEmpty(orderstate) && "2".equals(orderstate)) {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("3");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));
					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_sys_offline_payed_to_user));
					systemEntry
							.setDoctor_msg((getString(R.string.txt_sys_offline_payed_to_doctor))
									.replace("{service}",
											PublicUtil.getServiceNameByid("3")));
					event = new MessageSystemEvent(ServiceType.SERVICE_JIAHAO,
							msgEntry, systemEntry);
				} else {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("3");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));
					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_thank_offline)
									+ doctor_name
									+ (getString(R.string.chat_sys_offine_service))
											.replace("{service}", PublicUtil
													.getServiceNameByid("3")));
					systemEntry.setDoctor_msg(PublicUtil
							.getServiceContentByid("3"));

					event = new MessageSystemEvent(ServiceType.SERVICE_JIAHAO,
							msgEntry, systemEntry);
				}

			} else if (from_activity.equals(BuyShangmenActivity.class
					.getSimpleName())) {
				if (!TextUtils.isEmpty(orderstate) && "2".equals(orderstate)) {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("4");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));

					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_sys_offline_payed_to_user));
					systemEntry
							.setDoctor_msg((getString(R.string.txt_sys_offline_payed_to_doctor))
									.replace("{service}",
											PublicUtil.getServiceNameByid("4")));

					event = new MessageSystemEvent(
							ServiceType.SERVICE_SHANGMEN, msgEntry, systemEntry);

				} else {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("4");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));
					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_thank_offline)
									+ doctor_name
									+ (getString(R.string.chat_sys_offine_service))
											.replace("{service}", PublicUtil
													.getServiceNameByid("4")));
					systemEntry.setDoctor_msg(PublicUtil
							.getServiceContentByid("4"));

					event = new MessageSystemEvent(
							ServiceType.SERVICE_SHANGMEN, msgEntry, systemEntry);
				}

			} else if (from_activity.equals(BuyZhuyuanActivity.class
					.getSimpleName())) {
				if (!TextUtils.isEmpty(orderstate) && "2".equals(orderstate)) {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("14");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));

					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_sys_offline_payed_to_user));
					systemEntry
							.setDoctor_msg((getString(R.string.txt_sys_offline_payed_to_doctor))
									.replace("{service}",
											PublicUtil.getServiceNameByid("14")));
					event = new MessageSystemEvent(ServiceType.SERVICE_ZHUYUAN,
							msgEntry, systemEntry);

				} else {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("14");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));

					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_thank_offline)
									+ doctor_name
									+ (getString(R.string.chat_sys_offine_service))
											.replace("{service}", PublicUtil
													.getServiceNameByid("14")));
					systemEntry.setDoctor_msg(PublicUtil
							.getServiceContentByid("14"));
					event = new MessageSystemEvent(ServiceType.SERVICE_ZHUYUAN,
							msgEntry, systemEntry);
				}

			} else if (from_activity.equals(BuyTextConsultActivity.class
					.getSimpleName())) {
				MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
				msgEntry.setCHANNEL("android");
				msgEntry.setISSYSTEMMSG("1");
				msgEntry.setORDERID(orderid);
				msgEntry.setSERVICEID(serviceid);
				msgEntry.setUSERID(SharePrefUtil.getString(Conast.MEMBER_ID));
				msgEntry.setDOCTORID(tochat_userId);
				msgEntry.setORDERTYPE("1");
				msgEntry.setDoctor_avatar(doctor_avatar);
				msgEntry.setUser_avatar(SharePrefUtil.getString(Conast.AVATAR));
				msgEntry.setDoctor_name(doctor_name);
				msgEntry.setUser_name(SharePrefUtil.getString(Conast.realName));

				MessageSystemEntry systemEntry = new MessageSystemEntry();
				systemEntry.setUser_msg(getString(R.string.txt_thank)
						+ doctor_name
						+ getString(R.string.txt_sys_text_service));
				systemEntry
						.setDoctor_msg(getString(R.string.txt_sys_txt_service_to_doctor));
				event = new MessageSystemEvent(ServiceType.SERVICE_TEXT,
						msgEntry, systemEntry);

			} else if (from_activity.equals(BuyPhoneConsultActivity.class
					.getSimpleName())) {

				MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
				msgEntry.setCHANNEL("android");
				msgEntry.setISSYSTEMMSG("1");
				msgEntry.setORDERID(orderid);
				msgEntry.setSERVICEID(serviceid);
				msgEntry.setUSERID(SharePrefUtil.getString(Conast.MEMBER_ID));
				msgEntry.setDOCTORID(tochat_userId);
				msgEntry.setORDERTYPE("2");
				msgEntry.setDoctor_avatar(doctor_avatar);
				msgEntry.setUser_avatar(SharePrefUtil.getString(Conast.AVATAR));
				msgEntry.setDoctor_name(doctor_name);
				msgEntry.setUser_name(SharePrefUtil.getString(Conast.realName));
				MessageSystemEntry systemEntry = new MessageSystemEntry();
				systemEntry.setUser_msg(getString(R.string.txt_thank)
						+ doctor_name
						+ getString(R.string.txt_sys_phone_service));
				systemEntry
						.setDoctor_msg(getString(R.string.txt_sys_phone_service_to_doctor));
				event = new MessageSystemEvent(ServiceType.SERVICE_PHONE,
						msgEntry, systemEntry);

			} else if (from_activity.equals(PrivateDoctorActivity.class
					.getSimpleName())) {
				if (!TextUtils.isEmpty(orderstate) && "2".equals(orderstate)) {
					MessageBackupFieldEntry msgEntry = new MessageBackupFieldEntry();
					msgEntry.setCHANNEL("android");
					msgEntry.setISSYSTEMMSG("1");
					msgEntry.setORDERID(orderid);
					msgEntry.setSERVICEID(serviceid);
					msgEntry.setUSERID(SharePrefUtil
							.getString(Conast.MEMBER_ID));
					msgEntry.setDOCTORID(tochat_userId);
					msgEntry.setORDERTYPE("15");
					msgEntry.setDoctor_avatar(doctor_avatar);
					msgEntry.setUser_avatar(SharePrefUtil
							.getString(Conast.AVATAR));
					msgEntry.setDoctor_name(doctor_name);
					msgEntry.setUser_name(SharePrefUtil
							.getString(Conast.realName));
					MessageSystemEntry systemEntry = new MessageSystemEntry();
					systemEntry
							.setUser_msg(getString(R.string.txt_thank)
									+ doctor_name
									+ getString(R.string.txt_sys_private_doctor_service));
					systemEntry
							.setDoctor_msg(getString(R.string.txt_sys_private_service_to_doctor));

					event = new MessageSystemEvent(
							ServiceType.SERVICE_PRIVATE_DOCTOR, msgEntry,
							systemEntry);
				}
			}
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					Message msg = new Message();
					msg.what = IResult.SEND_SYS_MSG;
					msg.obj = event;
					handler.sendMessage(msg);
				}
			}, 500);
		}
	}

	public void setTitle(String title) {
		tv_title.setText(title);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onResume() {
		super.onResume();
		setRedShow();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// 点击notification bar进入聊天页面，保证只有一个聊天页面
		String username = intent.getStringExtra("tochat_userId");
		// System.out.println("onNewIntent userid : " + username);

		if (tochat_userId.equals(username)) {
			super.onNewIntent(intent);
			setIntent(intent);
			sendSystemMsgOperation();
		} else {
			finish();
			startActivity(intent);
		}
	}

	@Override
	protected void setupView() {
		super.setupView();
		// View ll_frag_title = findViewById(R.id.ll_frag_title);
		// ll_frag_title.setVisibility(View.VISIBLE);

		// rb_frag_title_chat = (RadioButton)
		// findViewById(R.id.rb_frag_title_chat);
		rb_frag_title_order = (RelativeLayout) findViewById(R.id.rb_frag_title_order);
		rb_frag_title_introduce = (LinearLayout) findViewById(R.id.rb_frag_title_introduce);

		order_form_tx = (TextView) findViewById(R.id.order_form_tx);
		order_form_number = (TextView) findViewById(R.id.order_form_number);
		summary_tx = (TextView) findViewById(R.id.summary_tx);
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundResource(R.color.transparent);
		btn_addhealth.setText("免费微聊");
		iv_red_dot = (TextView) findViewById(R.id.iv_red_dot);
		
		rb_frag_title_introduce
				.setBackgroundResource(R.drawable.doctor_navigation);
		rb_frag_title_order
				.setBackgroundResource(R.drawable.doctor_navigation_1);
		order_form_tx.setSelected(true);
		tv_title = (TextView) findViewById(R.id.top_title);
		tv_title.setText("");
		if (!TextUtils.isEmpty(doctor_name))
			tv_title.setText(doctor_name + "医生");
		mPagerAdapter = new PagerAdapter(getSupportFragmentManager(), this,
				mIsFollow);

		// Bundle bundle_order = new Bundle();
		// bundle_order.putString("fragmentKey",
		// getString(R.string.frag_title_order));
		// bundle_order.putString("tochat_userId", tochat_userId);
		// bundle_order.putString("doctor_avatar", doctor_avatar);
		// bundle_order.putString("doctor_name", doctor_name);
		// mOrderFragment = (DoctorOrderFragment) Fragment.instantiate(this,
		// DoctorOrderFragment.class.getName(), bundle_order);
		// Bundle bundle = new Bundle();
		// bundle.putString("fragmentKey",
		// getString(R.string.frag_title_introduce));
		// bundle.putString("tochat_userId", tochat_userId);
		// bundle.putString("doctor_name", doctor_name);
		// basicInfoFragment = (DoctorBasicInfoFragment)
		// Fragment.instantiate(this, DoctorBasicInfoFragment.class.getName(),
		// bundle);
		// fragments.add(mOrderFragment);
		// fragments.add(basicInfoFragment);
		// pageAddpter = new
		// FragmentViewPageAddpter(getSupportFragmentManager(), fragments);

		Bundle bundle_order = new Bundle();
		bundle_order.putString("fragmentKey",
				getString(R.string.frag_title_order));
		bundle_order.putString("tochat_userId", tochat_userId);
		bundle_order.putString("doctor_avatar", doctor_avatar);
		bundle_order.putString("doctor_name", doctor_name);
		bundle_order.putBoolean("isFollow", mIsFollow);

		mPagerAdapter.addFragment(getString(R.string.frag_title_order),
				DoctorOrderFragment.class, bundle_order);// 订单界面

		Bundle bundle = new Bundle();
		bundle.putString("fragmentKey",
				getString(R.string.frag_title_introduce));
		bundle.putString("tochat_userId", tochat_userId);
		bundle.putString("doctor_name", doctor_name);
		mPagerAdapter.addFragment(getString(R.string.frag_title_introduce),
				DoctorBasicInfoFragment.class, bundle);// 简介页面

		mViewPager = (CustomViewPager) findViewById(R.id.pager);
		mViewPager.setOffscreenPageLimit(2);// 缓存多少个页面
		mViewPager.setAdapter(mPagerAdapter);
		mViewPager.setOnPageChangeListener(this);// 设置监听
		
		getMemberInfo(false);
		if (mOrderFragment != null)
			mOrderFragment.refresh();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_addhealth) {
			// Intent i = new Intent(this, HistoryChatActivity.class);
			// i.putExtra("doctor_id", tochat_userId);
			// i.putExtra("doctor_name", doctor_name);
			// startActivity(i);
			// XXX 这里是免费微聊得入口

			if (memberInfo == null) {
				getMemberInfo(true);
			} else {
				Intent i = new Intent(this, MicroChatActivity.class);
				i.putExtra("doctor_avatar", doctor_avatar);
				i.putExtra("tochat_userId", tochat_userId);
				i.putExtra("doctorName", doctor_name);
				i.putExtra("memberInfo", memberInfo);
				startActivity(i);
			}

		}
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		// try {
		// switch (msg.what) {
		// case IResult.NET_ERROR:
		// showToast(IMessage.NET_ERROR);
		// break;
		// case IResult.DATA_ERROR:
		// break;
		// case IResult.SEND_SYS_MSG:
		// MessageSystemEvent event = (MessageSystemEvent) msg.obj;
		// mViewPager.setCurrentItem(0);
		// if (null != mChatFragment) {
		// mChatFragment.receiveEventAndSendMsg(event);
		// }
		// if (null != mOrderFragment) {
		// if (event.getType() != null) {
		// mOrderFragment.refresh();
		// }
		// }
		// break;
		// default:
		// break;
		// }
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}
	
	

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
	}

	@Override
	protected void onStop() {
		super.onStop();

		Intent intent = new Intent();
		intent.setAction(MyApplication.GET_CONTACT_LIST);
		sendBroadcast(intent);
		EventBus.getDefault().removeStickyEvent(RegisterSuccessEvent.class);
	}

	class PagerAdapter extends FragmentPagerAdapter {

		private Context mContext;
		private final ArrayList<FragmentInfo> fragments = new ArrayList<FragmentInfo>();
		private FragmentManager fm;
		private ArrayList<Fragment> fra_list;

		protected final class FragmentInfo {
			private final String tag;
			private final Class<?> clss;
			private final Bundle args;

			protected FragmentInfo(String _tag, Class<?> _class, Bundle _args) {
				tag = _tag;
				clss = _class;
				args = _args;
			}
		}

		public PagerAdapter(FragmentManager fm, Context context,
				boolean isFollow) {
			super(fm);
			this.fm = fm;
			this.mContext = context;
			fra_list = new ArrayList<Fragment>();
		}

		public void clearFragment() {
			FragmentTransaction ft = fm.beginTransaction();
			for (Fragment f : fra_list) {
				ft.remove(f);
			}
			ft.commit();
			ft = null;
			fm.executePendingTransactions();// 立刻执行以上命令（commit）
		}

		public void addFragment(String tag, Class<?> clss, Bundle args) {
			FragmentInfo fragmentInfo = new FragmentInfo(tag, clss, args);
			fragments.add(fragmentInfo);
		}

		@Override
		public Fragment getItem(int arg0) {
			FragmentInfo fragmentInfo = fragments.get(arg0);

			Fragment fra = Fragment.instantiate(mContext,
					fragmentInfo.clss.getName(), fragmentInfo.args);
			if (!fra_list.contains(fra))
				fra_list.add(fra);
			if (fragmentInfo.clss.getName().equals(
					DoctorChatFragment.class.getName())) {
				// mChatFragment = (DoctorChatFragment) fra;
			} else if (fragmentInfo.clss.getName().equals(
					DoctorOrderFragment.class.getName())) {
				mOrderFragment = (DoctorOrderFragment) fra;
			}
			return fra;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return fragments.get(position).tag;
		}

		@Override
		public int getCount() {
			return fragments.size();
		}

	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int position) {
		switch (position) {
		case 0:
			rb_frag_title_introduce
					.setBackgroundResource(R.drawable.doctor_navigation);
			rb_frag_title_order
					.setBackgroundResource(R.drawable.doctor_navigation_1);
			order_form_tx.setSelected(true);
			summary_tx.setSelected(false);
			break;
		case 1:
			rb_frag_title_order
					.setBackgroundResource(R.drawable.doctor_navigation);
			rb_frag_title_introduce
					.setBackgroundResource(R.drawable.doctor_navigation_1);
			summary_tx.setSelected(true);
			order_form_tx.setSelected(false);
			break;
		}

	}

	/** 信息提示 */
	public void showMessage(String msg) {
		Builder builder = new Builder(this);
		Dialog dialog = builder
				.setTitle(getString(R.string.system_info))
				.setMessage(msg)
				.setPositiveButton(getString(R.string.add_health_record_know),
						null).create();
		dialog.setCancelable(false);
		if (!finish) {
			dialog.show();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		finish = true;
		activity = null;
		if (pageReceive != null) {
			unregisterReceiver(pageReceive);
		}
	}

	protected static final String TAG = DoctorDetailInfoActivity.class
			.getSimpleName();

	private boolean finish = false;
	private LinearLayout rb_frag_title_introduce;
	private RelativeLayout rb_frag_title_order;
	private TextView order_form_tx;
	private TextView order_form_number;
	private TextView summary_tx;

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 2002:
				mViewPager.setCurrentItem(0);
				break;
			case 7:
				mViewPager.setCurrentItem(1);
				break;

			default:
				break;
			}
		}
	}

	public void pageChange(View v) {
		switch (v.getId()) {
		// case R.id.rb_frag_title_chat:
		// mViewPager.setCurrentItem(0);
		// break;
		case R.id.rb_frag_title_order:
			mViewPager.setCurrentItem(0);
			break;
		case R.id.rb_frag_title_introduce:
			mViewPager.setCurrentItem(1);
			break;

		}
	}

	private TextView tv_title;

	@Override
	public void onNumber(int number) {
		if (0 == number) {
			order_form_number.setVisibility(View.GONE);
		} else {
			order_form_number.setVisibility(View.VISIBLE);
			order_form_number.setText("" + number);
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	public MemberInfo memberInfo = null;

	public void getMemberInfo(final boolean isShowDialog) {

		if (NetWorkUtils.detect(activity)) {
			if (isShowDialog) {
				loading(null);
			}
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("any_user_id", tochat_userId);

			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_member_info, MemberInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.MEMBER_INFO;
							memberInfo = (MemberInfo) response;
							if (memberInfo != null) {
								EventBus.getDefault().postSticky(memberInfo);
								if (isShowDialog) {
									destroyDialog();
									Intent i = new Intent(
											DoctorDetailInfoActivity.this,
											MicroChatActivity.class);
									i.putExtra("doctor_avatar", doctor_avatar);
									i.putExtra("tochat_userId", tochat_userId);
									i.putExtra("doctorName", doctor_name);
									i.putExtra("memberInfo", memberInfo);
									startActivity(i);
								}
							}

						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
						}
					});
		}
	}

	public class PageReceive extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			int index = intent.getIntExtra("index", 0);
			mViewPager.setCurrentItem(index);
		}

	}
	
	
	EMConversation conversation;
	public int getUnreadNumber() {
		int unCount = 0;
		List<EMMessage> emData = conversation.getAllMessages();
		for (int i = 0; i < emData.size(); i++) {
			EMMessage message = emData.get(i);
			if (message.isUnread()||message.isListened()) {
				try {
					String ext = message.getStringAttribute("ext");
					JSONObject jsonObject = new JSONObject(ext);
					String  id  = jsonObject.getString("ORDERID");
					if("-1".equals(id)){
						unCount++;
					}
				} catch (EaseMobException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return unCount;
	}
	
	public void setRedShow(){
		conversation = EMChatManager.getInstance().getConversation(
				tochat_userId);
		if (conversation.getUnreadMsgCount() > 0) {
//			 显示与此用户的消息未读数
			 int n = getUnreadNumber();
			if(n>0){
				iv_red_dot.setText(String.valueOf(n));
				iv_red_dot.setVisibility(View.VISIBLE);
			}else {
				iv_red_dot.setVisibility(View.INVISIBLE);
			}
		} else {
			iv_red_dot.setVisibility(View.INVISIBLE);
		}
	}
	
	NewMessageBroadcastReceiver msgReceiver;
	public void regMSG(){
		
		try {
			// 注册一个接收消息的BroadcastReceiver
			msgReceiver = new NewMessageBroadcastReceiver();
			IntentFilter intentFilter = new IntentFilter(EMChatManager
					.getInstance().getNewMessageBroadcastAction());
			intentFilter.setPriority(4);
			registerReceiver(msgReceiver, intentFilter);
		}catch (Exception e1) {
			e1.printStackTrace();
		}
	}
	
	private class NewMessageBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 消息id
			String msgId = intent.getStringExtra("msgid");
			// 收到这个广播的时候，message已经在db和内存里了，可以通过id获取mesage对象
			// EMMessage message =
			// EMChatManager.getInstance().getMessage(msgId);
			// 刷新bottom bar消息未读数
			
			setRedShow();
			
			// 注销广播，否则在ChatActivity中会收到这个广播
			abortBroadcast();
		}
	}

}
