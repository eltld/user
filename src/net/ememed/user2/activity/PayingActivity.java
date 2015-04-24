package net.ememed.user2.activity;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;
import com.alipay.android.app.sdk.AliPay;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.ImageMessageBody;
import com.easemob.chat.TextMessageBody;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.MessageSystemEvent;
import de.greenrobot.event.MessageSystemEvent.ServiceType;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddSingle;
import net.ememed.user2.entity.AlipayOrderInfo;
import net.ememed.user2.entity.BalanceInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MessageBackupFieldEntry;
import net.ememed.user2.entity.MessageSystemEntry;
import net.ememed.user2.entity.OrderInfoEntity;
import net.ememed.user2.entity.OrderInfoEntry;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.Keys;
import net.ememed.user2.util.MobileSecurePayHelper;
import net.ememed.user2.util.Result;
import net.ememed.user2.util.Rsa;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.UICore;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

 

/***
 * 支付页面 付款步骤： 1.先获取用户帐户余额 2.若余额不为空，则默认勾选余额支付；否则用户可选支付宝支付
 * 3.支付成功，调用查看订单状态接口，根据订单与服务的状态 判断是否跳转到聊天界面
 * 
 * @author chen
 */
public class PayingActivity extends BasicActivity {
	private static final int REQUEST_COUPON = 2;
	public static final String TAG = "alipay-sdk";
	private static final int RQF_PAY = 3;
	private static final int RQF_LOGIN = 4;

	private ConnectivityManager manager;
	private OrderInfoEntry orderInfo;
	private ProgressBar mProgressBar;
	private Button btn;
	private TextView et_name;
	private TextView et_phone;
	/** 总价 */
	private TextView et_total;
	/** 余额 */
	private TextView et_over;
	/** 还需支付 */
	private TextView et_need;
	private CheckBox checkbox_over;
	private LinearLayout layout_pay_type;
	/** 物品价格 */
	private double money_total = 0;
	/** 我的余额 */
	private double money_mine = 0;
	/** 还需要支付的金额 */
	private double money_need = 0;
	private LinearLayout lt_coupon;
	/** 订单ID */
	private String orderId;
	/** 优惠券号 */
	private String CouponNum = "";
	/** 订单价格 */
	private String OrderAmount = "";
	/** 优惠金额 */
	private String Discount = "0";
	/*** 商品原价 */
	private String GoodsAmount = "";
	private String title;
	private TextView et_title;
	private CheckBox checkbox_alipay;
	private AlipayOrderInfo alipayOrderInfo = null;
	private boolean isUseingCoupon = false;
	private volatile PAYMODE mPayType = PAYMODE.BALANCE_PAY;
	private String CouponName = "";
	private TextView et_coupon;
	private String doctor_name;
	private String ordertype;
	private String doctor_avatar;
	private String serviceId;
	private String dext;
	private Button btn_buy;
	
	private String path = null;

	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {

		manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		doctor_name = getIntent().getStringExtra("doctor_name");//
		path = getIntent().getStringExtra("path");//
		doctor_avatar = getIntent().getStringExtra("doctor_avatar");//
		title = getIntent().getStringExtra("title");// 订单标题
		orderId = getIntent().getStringExtra("orderId");//
		serviceId = getIntent().getStringExtra("serviceId");//
		dext = getIntent().getStringExtra("dext");//
		ordertype = getIntent().getStringExtra("ordertype");// 订单类型
		money_total = getIntent().getDoubleExtra("price", 0);// 商品价格
		BigDecimal bg = new BigDecimal(money_total);// 截两位小数
		money_total = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
		GoodsAmount = String.valueOf(money_total);

		super.onBeforeCreate(savedInstanceState);

	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {

		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.u_payview);
	}

	@Override
	protected void setupView() {
		try {
			TextView tv_title = (TextView) findViewById(R.id.top_title);
			tv_title.setText(getString(R.string.taobao_pay));

			et_title = (TextView) findViewById(R.id.et_title);
			et_name = (TextView) findViewById(R.id.et_name);
			et_phone = (TextView) findViewById(R.id.et_phone);
			et_title.setText(title);
			et_name.setText(SharePrefUtil.getString(Conast.realName));
			et_phone.setText(SharePrefUtil.getString(Conast.USER_MOBILE));

			et_total = (TextView) findViewById(R.id.et_total);
			et_over = (TextView) findViewById(R.id.et_over);
			et_need = (TextView) findViewById(R.id.et_need);
			layout_pay_type = (LinearLayout) findViewById(R.id.layout_pay_type);
			checkbox_over = (CheckBox) findViewById(R.id.checkbox_over);
			lt_coupon = (LinearLayout) findViewById(R.id.lt_coupon);
			checkbox_alipay = (CheckBox) findViewById(R.id.checkbox_alipay);
			et_coupon = (TextView) findViewById(R.id.et_coupon);

			btn_buy = (Button) findViewById(R.id.btn_buy);
			btn_buy.setClickable(false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.setupView();
	}

	public void doClick(View view) {
		try {
			if (view.getId() == R.id.btn_back) {
				finish();
			} else if (view.getId() == R.id.lt_coupon) {
				if (isUseingCoupon) {
					showToast("您已经在使用优惠券,请勿重复使用");
				} else {
					// Intent intent = new Intent(PayingActivity.this,
					// CouponActivity.class);
					// intent.putExtra("orderId", orderId);
					// intent.putExtra("fromActivity",
					// PayingActivity.class.getSimpleName());
					// startActivityForResult(intent, REQUEST_COUPON);
				}

			} else if (view.getId() == R.id.btn_buy) {
				if (checkbox_over.isChecked()) {// 勾选了余额支付,并且余额足够支付该笔订单
					if (money_mine >= money_total) {
						mPayType = PAYMODE.BALANCE_PAY;// 余额支付

						UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.PAY, "交易中...", 1);

					} else {
						if (checkbox_alipay.isChecked()) {
							mPayType = PAYMODE.ALI_AND_BALANCE_PAY;// 余额+支付宝支付
							MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
							boolean isMobile_spExist = mspHelper.detectMobile_sp();
							if (!isMobile_spExist)
								return;
							UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.PAY, "交易中...", 3);

						} else {
							showToast("余额不足,请选择支付方式");
						}
					}
				} else {
					if (checkbox_alipay.isChecked()) {
						mPayType = PAYMODE.ALI_PAY;// 支付宝支付
						MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(this);
						boolean isMobile_spExist = mspHelper.detectMobile_sp();
						if (!isMobile_spExist)
							return;
						UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.PAY, "交易中...", 2);
					} else {
						showToast("请选择支付方式");
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public enum PAYMODE {
		/** 余额支付 */
		BALANCE_PAY,
		/** 支付宝支付 */
		ALI_PAY,
		/** 余额不够，用支付宝支付 */
		ALI_AND_BALANCE_PAY,
	}

	/**
	 * 支付接口(余额支付的全程处理，支付宝支付的支付前数据处理) CouponNum （非必须） MemberID OrderID
	 * GoodsAmount OrderAmount Discount 折扣 BalancePrice 余额支付的价钱数 PayType 支付渠道
	 * paytype（默认 1） 1：余额支付；2：支付宝支付；3：余额+支付宝支付
	 */
	private void pay_set_order(final String CouponNum, final String OrderID, final String GoodsAmount, final String OrderAmount, final String Discount, final String BalancePrice, final int PayType) {

		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
		params.add(new BasicNameValuePair("channel", "android"));
		params.add(new BasicNameValuePair("couponnum", CouponNum));
		params.add(new BasicNameValuePair("goodsamount", GoodsAmount));

		params.add(new BasicNameValuePair("orderamount", OrderAmount));
		params.add(new BasicNameValuePair("discount", Discount));
		params.add(new BasicNameValuePair("memberid", SharePrefUtil.getString(Conast.MEMBER_ID)));
		params.add(new BasicNameValuePair("orderid", OrderID));
		params.add(new BasicNameValuePair("paytype", PayType + ""));
		params.add(new BasicNameValuePair("balance_price", BalancePrice));

		String content;
		try {
			content = HttpUtil.getString(HttpUtil.URI + HttpUtil.pay_set_order, params, HttpUtil.POST);
			content = TextUtil.substring(content, "{");
			Gson gson = new Gson();
			AlipayOrderInfo reason = gson.fromJson(content, AlipayOrderInfo.class);
			Message msg = Message.obtain();
			msg.what = IResult.PAY;
			msg.obj = reason;
			handler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
			Message message = new Message();
			message.obj = e.getMessage();
			message.what = IResult.NET_ERROR;
			handler.sendMessage(message);
		}
	}

	/** 支付接口完成回调 */
	private void pay_callback(final String OrderNo, final String total_fee, final String subject) {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
		params.add(new BasicNameValuePair("channel", "android"));
		params.add(new BasicNameValuePair("out_user", SharePrefUtil.getString(Conast.MEMBER_ID)));
		params.add(new BasicNameValuePair("ordersn", OrderNo));
		params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));
		params.add(new BasicNameValuePair("total_fee", total_fee));
		params.add(new BasicNameValuePair("subject", subject));

		String content;
		try {
			content = HttpUtil.getString(HttpUtil.URI + HttpUtil.pay_callback, params, HttpUtil.POST);
			content = TextUtil.substring(content, "{");
			Gson gson = new Gson();
			ResultInfo reason = gson.fromJson(content, ResultInfo.class);
			Message msg = Message.obtain();
			msg.what = IResult.CALL_BACK;
			msg.obj = reason;
			handler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
			Message message = new Message();
			message.obj = e.getMessage();
			message.what = IResult.NET_ERROR;
			handler.sendMessage(message);
		}
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		if (mes == IResult.PAY) {
			Integer payType = (Integer) obj;// paytype（默认 1）
											// 1：余额支付；2：支付宝支付；3：余额+支付宝支付
			if (payType == 2) {
				pay_set_order(CouponNum, orderId, GoodsAmount, String.valueOf(money_total), Discount, "0", 2);
			} else {
				pay_set_order(CouponNum, orderId, GoodsAmount, String.valueOf(money_total), Discount, String.valueOf(money_need), payType);
			}
		} else if (mes == IResult.RESULT) {
			getstatus();
		} else if (mes == IResult.CALL_BACK) {
			String orderNo = alipayOrderInfo.getData().getOrderNo();
			String totelFee = alipayOrderInfo.getData().getTotal_fee();
			String subject = alipayOrderInfo.getData().getSubject();
			pay_callback(orderNo, totelFee, subject);
		}
	}

	/***
	 * 获取订单状态
	 */
	private void getstatus() {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
		params.add(new BasicNameValuePair("channel", "android"));
		params.add(new BasicNameValuePair("orderid", orderId));
		params.add(new BasicNameValuePair("userid", SharePrefUtil.getString(Conast.MEMBER_ID)));

		String content;
		try {
			content = HttpUtil.getString(HttpUtil.URI + HttpUtil.get_order_info, params, HttpUtil.POST);
			content = TextUtil.substring(content, "{");
			Gson gson = new Gson();
			OrderInfoEntity reason = gson.fromJson(content, OrderInfoEntity.class);
			Message msg = Message.obtain();
			msg.what = IResult.RESULT;
			msg.obj = reason;
			handler.sendMessage(msg);
		} catch (IOException e) {
			e.printStackTrace();
			Message message = new Message();
			message.obj = e.getMessage();
			message.what = IResult.NET_ERROR;
			handler.sendMessage(message);
		}

	}

	private void test() {
		UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.PAY, "交易中...", null);
		UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.CALL_BACK, "加载中...", null);
		UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.RESULT, "获取订单状态中...", null);
	}

	@Override
	protected void onResult(Message msg) {
		try {
			destroyDialog();
			switch (msg.what) {
			case IResult.RESULT:
				OrderInfoEntity map = (OrderInfoEntity) msg.obj;
				int success = map.getSuccess();
				if (success == 0) {
					showToast(map.getErrormsg());
				} else {
					orderInfo = (OrderInfoEntry) map.getData();
					if (!TextUtils.isEmpty(orderInfo.getSTATE())) {// 支付成功,可以沟通
						// *ordertype订单类型：'1'=>"图文咨询",'2'=>"预约通话",'3'=>"名医加号",'4'=>"上门会诊",'14'=>"住院直通车",'15'=>"签约私人医生服务",'16'=>"自定义订单"
						// *[1]图文咨询：2支付成功，可以与医生沟通 3服务结束 [2]预约通话：0需要与医生沟通，确认通话时间
						// 2支付完成，可以通话 3服务结束
						// *签约私人医生服务：2已支付,可与医生沟通 3服务结束
						// 预约住院、预约加号、上门会诊、自定义业务：0请和医生沟通 1等待用户支付 2订单已支付，可以跟医生沟通了
						// 3服务结束
						if ("1".equals(ordertype)) {
							if ("2".equals(orderInfo.getSTATE())) {
								Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
								intent.putExtra("doctor_name", doctor_name);// 医生姓名
								intent.putExtra("tochat_userId", orderInfo.getDOCTORID());
								intent.putExtra("doctor_avatar", doctor_avatar);
								intent.putExtra("orderid", orderInfo.getORDERID());// 订单id
								intent.putExtra("serviceid", serviceId);// 服务id
								intent.putExtra("from_activity", BuyTextConsultActivity.class.getSimpleName());// 来源页
								startActivity(intent);
								Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
								sendBroadcast(receive);
								finish();
							}
							
							String doctor = "感谢购买"+doctor_name+"医生的图文咨询服务，您可以在下面输入您想咨询医生的问题。温馨提示：服务有效期为购买后的48小时内，如需帮助，请拨打4009003333联系薏米助理";
							String user = "新服务请求：图文咨询 。图文咨询，是指您与用户通过发送图片、文字、语音的方式，进行的线上问诊服务。" +
									"帮助用户初步解答病情是您应尽的责任和义务 注：图文咨询不可作为疾病诊断的最终依据！单项单次图文咨询有效期为48小时";
							String systemMSG = toStringJSON(doctor, user);
							sendEMMessage(systemMSG, orderInfo, true, ordertype);
							sendEMMessage(dext, orderInfo, false, ordertype);
							if(path!=null){
								sendImgSMG(path, orderInfo, ordertype);
							}
						} else if ("2".equals(ordertype)) {
							if ("0".equals(orderInfo.getSTATE())) {
								Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
								intent.putExtra("doctor_name", doctor_name);// 医生姓名
								intent.putExtra("doctor_avatar", doctor_avatar);
								intent.putExtra("tochat_userId", orderInfo.getDOCTORID());
								intent.putExtra("orderid", orderInfo.getORDERID());// 订单id
								intent.putExtra("serviceid", serviceId);// 服务id
								intent.putExtra("from_activity", BuyPhoneConsultActivity.class.getSimpleName());// 来源页
								startActivity(intent);
								Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
								sendBroadcast(receive);
								finish();
							}
							String user = "感谢购买"+doctor_name+"医生的预约通话服务，请尽快和医生沟通确认通话时间。";
							String doctor = "新服务请求：预约通话。     请在4小时内约定通话时间预约通话，" +
									"是指通过电话沟通的方式进行的远程问诊服务。您需要与用户文字沟通协商该服务的具体时间，并设置服务时间反馈给用户。您可以预约最近8小时内的可服务时间！";
							String systemMSG = toStringJSON(doctor, user);
							sendEMMessage(systemMSG, orderInfo, true, ordertype);
							sendEMMessage(dext, orderInfo, false, ordertype);
							if(path!=null){
								sendImgSMG(path, orderInfo, ordertype);
							}
						} else if ("3".equals(ordertype)) {// 预约加号
							if ("2".equals(orderInfo.getSTATE())) {
								Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
								intent.putExtra("doctor_name", doctor_name);// 医生姓名
								intent.putExtra("doctor_avatar", doctor_avatar);
								intent.putExtra("tochat_userId", orderInfo.getDOCTORID());

								intent.putExtra("orderid", orderInfo.getORDERID());// 订单id
								intent.putExtra("orderstate", "2");// 订单状态
								intent.putExtra("serviceid", serviceId);// 服务id
								intent.putExtra("from_activity", BuyJiuyiActivity.class.getSimpleName());// 来源页

								startActivity(intent);
								
								Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
								sendBroadcast(receive);
								String doctor = "该用户已成功支付该服务订单，请在服务完成后务必要求用户进行评价（评价后方可结算服务费用）";
								String user = "您已支付该次预约加号服务"+et_total.getText().toString()+"，请保持与医生沟通，当服务达成后请给医生服务评价";
								String systemMSG = toStringJSON(doctor, user);
								sendEMMessage(systemMSG,orderInfo, true, ordertype);
								finish();
							}
							
							
						} else if ("4".equals(ordertype)) {
							if ("2".equals(orderInfo.getSTATE())) {// 上门会诊
								Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
								intent.putExtra("doctor_name", doctor_name);// 医生姓名
								intent.putExtra("doctor_avatar", doctor_avatar);
								intent.putExtra("tochat_userId", orderInfo.getDOCTORID());

								intent.putExtra("orderid", orderInfo.getORDERID());// 订单id
								intent.putExtra("orderstate", "2");// 订单状态
								intent.putExtra("serviceid", serviceId);// 服务id
								intent.putExtra("from_activity", BuyShangmenActivity.class.getSimpleName());// 来源页

								startActivity(intent);
								Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
								sendBroadcast(receive);
								
								String doctor = "该用户已成功支付该服务订单，请在服务完成后务必要求用户进行评价（评价后方可结算服务费用）";
								String user = "您已支付该次上门会诊服务"+et_total.getText().toString()+"，请保持与医生沟通，当服务达成后请给医生服务评价";
								String systemMSG = toStringJSON(doctor, user);
								sendEMMessage(systemMSG, orderInfo, true, ordertype);
								finish();

							}
							
						} else if ("14".equals(ordertype)) {
							if ("2".equals(orderInfo.getSTATE())) {// 预约住院
								Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
								intent.putExtra("doctor_name", doctor_name);// 医生姓名
								intent.putExtra("doctor_avatar", doctor_avatar);
								intent.putExtra("tochat_userId", orderInfo.getDOCTORID());

								intent.putExtra("orderid", orderInfo.getORDERID());// 订单id
								intent.putExtra("orderstate", "2");// 订单状态
								intent.putExtra("serviceid", serviceId);// 服务id
								intent.putExtra("from_activity", BuyZhuyuanActivity.class.getSimpleName());// 来源页
								startActivity(intent);
								
								String doctor = "该用户已成功支付该服务订单，请在服务完成后务必要求用户进行评价（评价后方可结算服务费用）";
								String user = "您已支付该次预约住院服务"+et_total.getText().toString()+"，请保持与医生沟通，当服务达成后请给医生服务评价";
								String systemMSG = toStringJSON(doctor, user);
								
								sendEMMessage(systemMSG ,orderInfo, true, ordertype);
								finish();
								Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
								sendBroadcast(receive);
							}
						} else if ("15".equals(ordertype)) {
							if ("2".equals(orderInfo.getSTATE())) {// 签约私人医生
								Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
								intent.putExtra("doctor_name", doctor_name);// 医生姓名
								intent.putExtra("doctor_avatar", doctor_avatar);
								intent.putExtra("tochat_userId", orderInfo.getDOCTORID());
								intent.putExtra("orderid", orderInfo.getORDERID());// 订单id
								intent.putExtra("orderstate", "2");// 订单状态
								intent.putExtra("serviceid", serviceId);// 服务id
								intent.putExtra("from_activity", PrivateDoctorActivity.class.getSimpleName());// 来源页
								startActivity(intent);
								finish();
								Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
								sendBroadcast(receive);
							}
							
						} else if ("16".equals(ordertype)) {
							if ("2".equals(orderInfo.getSTATE())) {// 自定义服务
								Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
								intent.putExtra("doctor_name", doctor_name);// 医生姓名
								intent.putExtra("doctor_avatar", doctor_avatar);
								intent.putExtra("tochat_userId", orderInfo.getDOCTORID());
								intent.putExtra("orderid", orderInfo.getORDERID());// 订单id
								intent.putExtra("orderstate", "2");// 订单状态
								intent.putExtra("serviceid", serviceId);// 服务id
								intent.putExtra("from_activity", BuyCustomActivity.class.getSimpleName());// 来源页
								startActivity(intent);
								String doctor = "该用户已成功支付该服务订单，请在服务完成后务必要求用户进行评价（评价后方可结算服务费用）";
								String user = "您已支付该次服务"+et_total.getText().toString()+"，请保持与医生沟通，当服务达成后请给医生服务评价";
								String systemMSG = toStringJSON(doctor, user);
								sendEMMessage(systemMSG,orderInfo, true, ordertype);
								finish();
								Intent receive = new Intent(DoctorDetailInfoActivity.class.getName());
								sendBroadcast(receive);
							}
						}
						showToast("支付成功");
					} else {
						showToast(getString(R.string.service_nonocall));
					}
				}
				break;
			case IResult.PAY:
				alipayOrderInfo = (AlipayOrderInfo) msg.obj;
				int success2 = alipayOrderInfo.getSuccess();
				if (success2 == 0) {
					showToast(alipayOrderInfo.getErrormsg());
				} else {
					if (mPayType == PAYMODE.BALANCE_PAY) {
						mHandler.postDelayed(new Runnable() {

							@Override
							public void run() {
								UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.RESULT, "获取订单状态中...", null);
							}
						}, 500);

					} else if (mPayType == PAYMODE.ALI_PAY) {
						doAliPay(alipayOrderInfo.getData().getOrderNo(), alipayOrderInfo.getData().getSubject(), String.valueOf(money_total));
					} else if (mPayType == PAYMODE.ALI_AND_BALANCE_PAY) {
						doAliPay(alipayOrderInfo.getData().getOrderNo(), alipayOrderInfo.getData().getSubject(), String.valueOf(money_need));
					}
				}
				break;
			case IResult.CALL_BACK:
				ResultInfo map_call_back = (ResultInfo) msg.obj;
				if (map_call_back.getSuccess() == 0) {
					showToast(map_call_back.getErrormsg());
				} else {
					// showToast(map_call_back.getErrormsg());
					showToast(IMessage.CHECKING_BACK_RESULT);

					mHandler.postDelayed(new Runnable() {

						@Override
						public void run() {
							UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.RESULT, "获取订单状态中...", null);
						}
					}, 500);

				}
				break;
			case IResult.NET_ERROR:
				destroyDialog();
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				showToast(IMessage.DATA_ERROR);
				break;
			case IResult.END:
				btn_buy.setClickable(true);
				BalanceInfo map_get_money = (BalanceInfo) msg.obj;
				String balence_money = map_get_money.getData().getMONEY();
				if (!TextUtils.isEmpty(balence_money)) {
					money_mine = Double.valueOf(balence_money);
				}
				resetMoney();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.onResult(msg);
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		return super.onKeyUp(keyCode, event);
	}

	@Override
	protected void getData() {
		if (isNetworkAvailable()) {
			getbalance();
		} else {
			Toast.makeText(this, getString(R.string.error_net), Toast.LENGTH_SHORT).show();
		}

		super.getData();
	}

	/** 获取账户余额 */
	private void getbalance() {
		loading(null);

		HashMap<String, String> params = new HashMap<String, String>();
		params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
		params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_user_balance, BalanceInfo.class, params, new Response.Listener() {
			@Override
			public void onResponse(Object response) {
				Message message = new Message();
				message.obj = response;
				message.what = IResult.END;
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

	}

	private void resetMoney() {
		if (isNetworkAvailable()) {
			et_total.setText(String.valueOf(money_total) + getString(R.string.apply_hosp_money));
			et_over.setText(String.valueOf(money_mine) + getString(R.string.apply_hosp_money));
			if (money_mine > 0) {// 我的帐户是0元
				if (money_mine > money_total) {
					money_need = 0;
				} else {
					BigDecimal bg = new BigDecimal(money_total - money_mine);
					money_need = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				}
				checkbox_over.setChecked(true);

				if (money_mine < money_total) {// 显示支付宝支付项目
					layout_pay_type.setVisibility(View.VISIBLE);
					checkbox_alipay.setChecked(true);
				} else {
					layout_pay_type.setVisibility(View.GONE);
					checkbox_alipay.setChecked(false);
				}
			} else {// 调用支付宝
				money_need = money_total;
				checkbox_over.setEnabled(false);
				layout_pay_type.setVisibility(View.VISIBLE);
				checkbox_alipay.setChecked(true);
			}
			et_need.setText(String.valueOf(money_need) + getString(R.string.apply_hosp_money));
			checkbox_over.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

					if (isChecked) {
						if (money_mine >= money_total) {
							layout_pay_type.setVisibility(View.GONE);
							checkbox_alipay.setChecked(false);
							money_need = 0;
						} else {
							BigDecimal bg = new BigDecimal(money_total - money_mine);
							money_need = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							layout_pay_type.setVisibility(View.VISIBLE);
							checkbox_alipay.setChecked(true);
						}
						et_need.setText(String.valueOf(money_need) + getString(R.string.apply_hosp_money));
					} else {
						money_need = money_total;
						layout_pay_type.setVisibility(View.VISIBLE);
						et_need.setText(String.valueOf(money_need) + getString(R.string.apply_hosp_money));
					}
				}
			});

		} else {
			Toast.makeText(this, getString(R.string.error_net), Toast.LENGTH_SHORT).show();
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (requestCode == REQUEST_COUPON) {
				if (resultCode == RESULT_OK) {
					if (null != data) {
						OrderAmount = data.getStringExtra("OrderAmount");
						Discount = data.getStringExtra("Discount");
						GoodsAmount = data.getStringExtra("GoodsAmount");
						CouponNum = data.getStringExtra("CouponNum");
						CouponName = data.getStringExtra("CouponName");
						isUseingCoupon = true;
						if (!TextUtils.isEmpty(CouponName)) {
							et_coupon.setText(getResources().getString(R.string.text_is_useing) + " " + CouponName);
							et_coupon.setEnabled(false);
						}
						// System.out.println("OrderAmount:" + OrderAmount);

						money_total = Double.valueOf(OrderAmount);
						et_total.setText(money_total + getString(R.string.apply_hosp_money));
						if (money_total == 0) {
							checkbox_over.setChecked(true);
							checkbox_alipay.setChecked(false);
							layout_pay_type.setVisibility(View.GONE);
						}
						if (checkbox_over.isChecked()) {// 勾选了余额支付
							if (money_mine >= money_total) {// 余额比物品价格高 或相等
								money_need = 0;
							} else {// 余额比物品价格低
								BigDecimal bg = new BigDecimal(money_total - money_mine);
								money_need = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							}
							et_need.setText(String.valueOf(money_need) + getString(R.string.apply_hosp_money));
						} else {
							money_need = money_total;
							et_need.setText(String.valueOf(money_need) + getString(R.string.apply_hosp_money));
						}
					}
				}
			} else {

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void doAliPay(String OutTradeNo, String subject, String total_fee) {
		try {
			String info = getNewOrderInfo(OutTradeNo, subject, subject, total_fee);
			String sign = Rsa.sign(info, Keys.PRIVATE);
			sign = URLEncoder.encode(sign);
			info += "&sign=\"" + sign + "\"&" + getSignType();
			// Log.i("ExternalPartner", "start pay");
			// start the pay.
			// Log.i(TAG, "info = " + info);

			final String orderInfo = info;
			new Thread() {
				public void run() {
					AliPay alipay = new AliPay(PayingActivity.this, mHandler);
					// 设置为沙箱模式，不设置默认为线上环境
					// alipay.setSandBox(true);
					String result = alipay.pay(orderInfo);

					// Log.i(TAG, "result = " + result);
					Message msg = new Message();
					msg.what = RQF_PAY;
					msg.obj = result;
					mHandler.sendMessage(msg);
				}
			}.start();

		} catch (Exception ex) {
			ex.printStackTrace();
			Toast.makeText(PayingActivity.this, R.string.remote_call_failed, Toast.LENGTH_SHORT).show();
		}
	}

	Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			try {
				Result result = new Result((String) msg.obj);

				switch (msg.what) {
				case RQF_PAY:
					if (null != result) {
						Toast.makeText(PayingActivity.this, result.getResult(), Toast.LENGTH_SHORT).show();
						result.parseResult();
						String resultStatus = result.resultStatus;
						// String memo = result.memo;
						// String resultStr = result.result;
						String resultCode = result.getResultCode();
						if (!TextUtils.isEmpty(resultCode)) {
							if (resultCode.equals("9000")) {// 支付宝返回成功
								UICore.eventTask(PayingActivity.this, PayingActivity.this, IResult.CALL_BACK, "加载中...", null);
							} else {
								showToast(resultStatus);
							}
						}
					}
					break;
				case RQF_LOGIN: {
					if (null != result) {
						Toast.makeText(PayingActivity.this, result.getResult(), Toast.LENGTH_SHORT).show();
					}
				}
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		};
	};

	/**
	 * 
	 * @param subject
	 *            商品名称
	 * @param body
	 *            商品描述
	 * @param price
	 *            订单价格
	 * @return
	 */
	private String getNewOrderInfo(String OutTradeNo, String subject, String body, String price) {
		StringBuilder sb = new StringBuilder();
		sb.append("partner=\"");
		sb.append(Keys.DEFAULT_PARTNER);
		sb.append("\"&out_trade_no=\"");
		sb.append(OutTradeNo);
		sb.append("\"&subject=\"");
		sb.append(subject);
		sb.append("\"&body=\"");
		sb.append(body);
		sb.append("\"&total_fee=\"");
		sb.append(price);
		sb.append("\"&notify_url=\"");

		// 网址需要做URL编码
		sb.append(URLEncoder.encode("http://www.ememed.net:8002/alipay/notify_url.php"));
		sb.append("\"&service=\"mobile.securitypay.pay");
		sb.append("\"&_input_charset=\"UTF-8");
		sb.append("\"&return_url=\"");
		sb.append(URLEncoder.encode("http://m.alipay.com"));
		sb.append("\"&payment_type=\"1");
		sb.append("\"&seller_id=\"");
		sb.append(URLEncoder.encode(Keys.DEFAULT_SELLER));

		// 如果show_url值为空，可不传
		// sb.append("\"&show_url=\"");
		sb.append("\"&it_b_pay=\"1m");
		sb.append("\"");

		return new String(sb);
	}

	private String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss");
		Date date = new Date();
		String key = format.format(date);

		java.util.Random r = new java.util.Random();
		key += r.nextInt();
		key = key.substring(0, 15);
		// Log.d(TAG, "outTradeNo: " + key);
		return key;
	}

	private String getSignType() {
		return "sign_type=\"RSA\"";
	}

	private void doLogin() {
		final String orderInfo = getUserInfo();
		new Thread() {
			public void run() {
				String result = new AliPay(PayingActivity.this, mHandler).pay(orderInfo);

				// Log.i(TAG, "result = " + result);
				Message msg = new Message();
				msg.what = RQF_LOGIN;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		}.start();
	}

	private String getUserInfo() {
		String userId = null;
		// String userId = mUserId.getText().toString();
		return trustLogin(Keys.DEFAULT_PARTNER, userId);

	}

	private String trustLogin(String partnerId, String appUserId) {
		StringBuilder sb = new StringBuilder();
		sb.append("app_name=\"mc\"&biz_type=\"trust_login\"&partner=\"");
		sb.append(partnerId);
		// Log.d("TAG", "sUserID = " + appUserId);
		if (!TextUtils.isEmpty(appUserId)) {
			appUserId = appUserId.replace("\"", "");
			sb.append("\"&app_id=\"");
			sb.append(appUserId);
		}
		sb.append("\"");

		String info = sb.toString();

		// 请求信息签名
		String sign = Rsa.sign(info, Keys.PRIVATE);
		try {
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		info += "&sign=\"" + sign + "\"&" + getSignType();

		return info;
	}
	
	
	public void sendEMMessage(String textMSG , OrderInfoEntry entry,boolean isSystemMsg,String orderType) {
		System.out.println("系统消息发送");
		String ext = generateExtMessage(entry, isSystemMsg, orderType);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
		TextMessageBody txtBody = new TextMessageBody(textMSG);
		// 设置消息body
		message.addBody(txtBody);
		if (!TextUtils.isEmpty(ext))
			message.setAttribute("ext", ext);
		
		// 设置要发给谁,用户username或者群聊groupid
		message.setReceipt(entry.getDOCTORID());
		// 把messgage加到conversation中
		EMConversation conversation = EMChatManager.getInstance().getConversation(entry.getDOCTORID());
		conversation.addMessage(message);
		
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				System.out.println("系统发送成功");
			}

			@Override
			public void onError(int code, String error) {
				System.out.println("系统发送失败");
			}

			@Override
			public void onProgress(int progress, String status) {
			}

		});
		
	}

	/***
	 * 附加到聊天消息中的备用字段
	 * 
	 * @return
	 */
	public String generateExtMessage(OrderInfoEntry entry, boolean isSystemMsg,String orderType) {
		JsonObject obj = new JsonObject();
		// "ISSYSTEMMSG": "0", //是否系统消息（1是，0为否）
		// "CHANNEL": "android", //来源（android|ios）
		// "ORDERID": "123", //订单ID
		// "SERVICEID": "1", //服务ID
		// "PACKET_BUY_ID": "1", //套餐购买ID
		// "USERID": "6998", //用户ID
		// "DOCTORID": "5226", //医生ID
		// "ORDERTYPE": "1" //订单类型
		obj.addProperty("SERVICEID", entry.getSERVICEID());
		obj.addProperty("DOCTORID", entry.getDOCTORID());
		obj.addProperty("ORDERTYPE", orderType);
		obj.addProperty("user_avatar", SharePrefUtil.getString(Conast.AVATAR));
		obj.addProperty("doctor_name", doctor_name);
		obj.addProperty("user_name", SharePrefUtil.getString(Conast.realName));
		obj.addProperty("doctor_avatar", doctor_avatar);
		obj.addProperty("USERID", SharePrefUtil.getString(Conast.MEMBER_ID));
		if (isSystemMsg) {

			obj.addProperty("ISSYSTEMMSG", "1");
		} else {
			obj.addProperty("ISSYSTEMMSG", "0");

		}
		obj.addProperty("CHANNEL", "android");
		obj.addProperty("ORDERID", entry.getORDERID());
		return obj.toString();

	}
	

	//
	/**
	 * the OnCancelListener for lephone platform. lephone系统使用到的取消dialog监听
	 */
	public static class AlixOnCancelListener implements DialogInterface.OnCancelListener {
		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}
	
	private String toStringJSON(String doctor,String user){
		
		JsonObject obj = new JsonObject();
		obj.addProperty("doctor_msg", doctor);
		obj.addProperty("user_msg", user);
		System.out.println("obj.toString() = "+obj.toString());
		return obj.toString();
		
	}
	
	
	public void sendImgSMG(String filePath,OrderInfoEntry addSingle,String orderType){
		String ext = generateExtMessage(addSingle, false, orderType);
		EMMessage message = EMMessage.createSendMessage(EMMessage.Type.IMAGE);
		message.setReceipt(addSingle.getDOCTORID());
		message.setFrom(SharePrefUtil.getString(Conast.MEMBER_ID));
		if (!TextUtils.isEmpty(ext))
			message.setAttribute("ext", ext);
		ImageMessageBody body = new ImageMessageBody(new File(filePath));
		message.addBody(body);
		EMConversation conversation = EMChatManager.getInstance().getConversation(addSingle.getDOCTORID());
		conversation.addMessage(message);
		EMChatManager.getInstance().sendMessage(message, new EMCallBack() {

			@Override
			public void onSuccess() {
				System.out.println("系统发送成功");
			}

			@Override
			public void onError(int code, String error) {
				System.out.println("系统发送失败");
			}

			@Override
			public void onProgress(int progress, String status) {
			}

		});
	}

}
