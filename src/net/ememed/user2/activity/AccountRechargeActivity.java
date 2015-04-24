package net.ememed.user2.activity;

import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddChargeOrderEntry;
import net.ememed.user2.entity.CardEntity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.UserFinaceEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.CardDialog;
import net.ememed.user2.widget.PayBackDialog;
import net.ememed.user2.wxpay.WxPay;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

/**
 * 财务详情
 * 
 * @author pch
 * 
 *         update by pch, 2015/3/18 1.add wxpay
 * 
 */
public class AccountRechargeActivity extends BasicActivity implements OnClickListener {
	private LinearLayout ll_zhifubao;
	private LinearLayout ll_bankcard;
	private LinearLayout ll_wxpay;
	private TextView btn_zhifubao;
	private TextView btn_bankcard;
	private TextView btn_card;
	private TextView tv_wxpay;
	private LinearLayout btn_recharge;
	private LinearLayout card_layout;
	private EditText et_recharge_num;
	private TextView tv_total_money;
	private TextView top_title;
	private ImageView btn_back;

	private View convertView;
	private PopupWindow popup;
	private LinearLayout layout_account_root;

	private Button btn_addhealth;

	private static final int PAY_WX = 7;
	private int rechageRoute = 1; // 1表示支付宝，2表示银行卡, 7微信支付

	private String mPrice;
	private String money;
	private CardDialog cardDialog;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_account_recharge);
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(AccountRechargeActivity.class.getName());
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	protected void setupView() {
		super.setupView();
		layout_account_root = (LinearLayout) findViewById(R.id.layout_account_root);
		ll_zhifubao = (LinearLayout) findViewById(R.id.ll_zhifubao);
		ll_zhifubao.setOnClickListener(this);
		ll_bankcard = (LinearLayout) findViewById(R.id.ll_bankcard);
		ll_bankcard.setOnClickListener(this);
		ll_wxpay = (LinearLayout) findViewById(R.id.ll_wxpay);
		ll_wxpay.setOnClickListener(this);
		btn_recharge = (LinearLayout) findViewById(R.id.btn_recharge);
		card_layout = (LinearLayout) findViewById(R.id.card_layout);
		btn_recharge.setOnClickListener(this);
		card_layout.setOnClickListener(this);
		btn_zhifubao = (TextView) findViewById(R.id.btn_zhifubao);
		btn_bankcard = (TextView) findViewById(R.id.btn_bankcard);
		tv_wxpay = (TextView) findViewById(R.id.tv_wxpay);
		btn_card = (TextView) findViewById(R.id.btn_card);
		et_recharge_num = (EditText) findViewById(R.id.et_recharge_num);
		tv_total_money = (TextView) findViewById(R.id.tv_total_money);
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(getString(R.string.account_recharge));
		btn_back = (ImageView) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundResource(R.color.transparent);
		btn_addhealth.setText("明细");
		btn_addhealth.setOnClickListener(this);
		btn_zhifubao.setBackgroundResource(R.drawable.finace_gou);
		btn_bankcard.setBackgroundResource(R.drawable.finace_circle);
		initSharePopupWindows();
	}

	@Override
	protected void onResume() {
		getUserbalance();
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ll_zhifubao:
			btn_zhifubao.setBackgroundResource(R.drawable.finace_gou);
			btn_bankcard.setBackgroundResource(R.drawable.finace_circle);
			btn_card.setBackgroundResource(R.drawable.finace_circle);
			tv_wxpay.setBackgroundResource(R.drawable.finace_circle);
			rechageRoute = 1;
			break;
		case R.id.ll_bankcard:
			btn_zhifubao.setBackgroundResource(R.drawable.finace_circle);
			btn_bankcard.setBackgroundResource(R.drawable.finace_gou);
			btn_card.setBackgroundResource(R.drawable.finace_circle);
			tv_wxpay.setBackgroundResource(R.drawable.finace_circle);
			rechageRoute = 2;
			if (popup != null && !popup.isShowing()) {
				popup.showAtLocation(layout_account_root, Gravity.CENTER, 0, 0);
			}
			break;
		case R.id.ll_wxpay:
			btn_zhifubao.setBackgroundResource(R.drawable.finace_circle);
			btn_bankcard.setBackgroundResource(R.drawable.finace_circle);
			btn_card.setBackgroundResource(R.drawable.finace_circle);
			tv_wxpay.setBackgroundResource(R.drawable.finace_gou);
			rechageRoute = PAY_WX;
			break;
		case R.id.btn_recharge:
			if (2 == rechageRoute) {
				if (popup != null && !popup.isShowing()) {
					popup.showAtLocation(layout_account_root, Gravity.CENTER, 0, 0);
				}
				return;
			}

			if (TextUtils.isEmpty(et_recharge_num.getText().toString().trim())) {
				showToast("请输入要充值的金额");
				return;
			}

			mPrice = et_recharge_num.getText().toString().trim();
			addChargeOrder(mPrice);

			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_addhealth:

			Intent intent = new Intent(this, AccountWebActivity.class);
			intent.putExtra("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			startActivity(intent);
			break;
		case R.id.card_layout:// 礼品卡
			btn_card.setBackgroundResource(R.drawable.finace_gou);
			btn_zhifubao.setBackgroundResource(R.drawable.finace_circle);
			btn_bankcard.setBackgroundResource(R.drawable.finace_circle);
			rechageRoute = 3;

			cardDialog = new CardDialog(this, R.style.wheelDialog);
			cardDialog.show();
			cardDialog.setParams(CanvasWidth);
			cardDialog.setOnClickListener(this);
			break;
		case R.id.ok_bnt:
			chargeCard();
			break;
		default:
			break;
		}
	}

	public void chargeCard() {

		String card = cardDialog.getAccount();
		if (TextUtils.isEmpty(card)) {
			showToast("请输入礼品账号");
		}
		String card_pwd = cardDialog.getPassword();
		if (TextUtils.isEmpty(card_pwd)) {
			showToast("请输入礼品密码");
		}

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("card", card);
			params.put("card_pwd", card_pwd);
			params.put("channel", "Android");
			params.put("app_version", PublicUtil.getVersionName(this));
			params.put("payid", String.valueOf(rechageRoute));

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.charge_card, CardEntity.class,
					params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {

							destroyDialog();
							CardEntity cardEntity = (CardEntity) response;

							if (cardEntity != null) {
								showToast(cardEntity.getErrormsg());
								if (cardEntity.getSuccess().equals("1")) {
									backDialog = new PayBackDialog(AccountRechargeActivity.this,
											R.style.wheelDialog);
									backDialog.show();
									backDialog.setParams(CanvasWidth);
									backDialog.setPrisce(cardEntity.getData().getMoney());
									backDialog.setText("你已通过礼品卡充值成功");
									backDialog.setSerialSumber("礼品卡账号："
											+ cardEntity.getData().getCard_num());
									getUserbalance();
								}

							}
							cardDialog.dismiss();
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							destroyDialog();
							cardDialog.dismiss();
						}
					});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	protected void getUserbalance() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_user_balance,
					UserFinaceEntry.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = handler.obtainMessage();
							message.obj = response;
							message.what = IResult.GET_USER_BALANCE;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = handler.obtainMessage();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							handler.sendMessage(message);
						}
					});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	protected void addChargeOrder(String price) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("channel", "Android");
			params.put("price", price);
			params.put("app_version", PublicUtil.getVersionName(AccountRechargeActivity.this));
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.add_charge_order,
					AddChargeOrderEntry.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = handler.obtainMessage();
							message.obj = response;
							message.what = IResult.ADD_CHARGE_ORDER;
							handler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = handler.obtainMessage();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							handler.sendMessage(message);
						}
					});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		destroyDialog();
		switch (msg.what) {
		case IResult.GET_USER_BALANCE:
			UserFinaceEntry userFinaceEntry = (UserFinaceEntry) msg.obj;
			if (userFinaceEntry != null) {
				if (userFinaceEntry.getSuccess() == 1) {
					if (!TextUtils.isEmpty(userFinaceEntry.getData().getMONEY())) {
						String money = userFinaceEntry.getData().getMONEY();
						UserPreferenceWrapper.setUserBalance(money);
						if (!money.contains(".")) {
							money = money + ".00";
						} else {
							if (money.indexOf(".") == (money.length() - 2)) {
								money = money + "0";
							}
						}
						tv_total_money.setText("¥ " + money);
					} else {
						tv_total_money.setText("0.00");
					}
				} else {
					showToast(userFinaceEntry.getErrormsg());
				}
			}
			break;
		case IResult.ADD_CHARGE_ORDER:
			destroyDialog();
			AddChargeOrderEntry addChargeOrderEntry = (AddChargeOrderEntry) msg.obj;
			if (addChargeOrderEntry != null) {
				if (addChargeOrderEntry.getSuccess() == 1) {
					if (!TextUtils.isEmpty(addChargeOrderEntry.getData().getOrderid())) {
						String orderId = addChargeOrderEntry.getData().getOrderid();
						if (rechageRoute == PAY_WX) {
							wxpay(orderId);
						} else if (rechageRoute == 1) {
							String memberId = SharePrefUtil.getString(Conast.MEMBER_ID);
							String token = SharePrefUtil.getString(Conast.ACCESS_TOKEN);
							String url = "http://www.ememed.net:8004/index.php?d=right&c=alipay&m=alipay_demo&memberid="
									+ memberId + "&token=" + token + "&orderid=" + orderId;
							Intent intent = new Intent(AccountRechargeActivity.this,
									AccountRechargeWebActivity.class);
							intent.putExtra("url", url);
							startActivity(intent);
						}
					} else {
						showToast("获取订单号错误");
					}
				} else {
					showToast(addChargeOrderEntry.getErrormsg());
				}
			}
			break;
		case IResult.NET_ERROR:
			showToast(getString(R.string.net_error));
			break;
		case IResult.DATA_ERROR:
			break;
		}
	}

	private void wxpay(String orderId) {
		WxPay wxPay = new WxPay(this);
		wxPay.setBody("充值");
		wxPay.setPrice(mPrice);
		wxPay.setOrderNo(orderId);
		wxPay.pay();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_user_balance);
		unregisterReceiver(mBroadcastReceiver);
	}

	private void initSharePopupWindows() {
		convertView = View.inflate(this, R.layout.account_recharge_tip, null);
		Button btn = (Button) convertView.findViewById(R.id.button);
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (popup != null && popup.isShowing())
					popup.dismiss();
			}
		});

		popup = new PopupWindow(convertView, MyApplication.getInstance().canvasWidth * 3 / 4,
				LayoutParams.WRAP_CONTENT);
		popup.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popup.setFocusable(true);
	}

	PayBackDialog backDialog;

	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String pirsce = intent.getExtras().getString("pirsce");
			String msg = intent.getExtras().getString("msg");
			et_recharge_num.setText("");
			backDialog = new PayBackDialog(context, R.style.wheelDialog);
			backDialog.show();
			backDialog.setParams(CanvasWidth);
			backDialog.setPrisce(pirsce);
			backDialog.setText("你已通过支付宝充值成功");
			backDialog.setSerialSumber("流水号：" + msg);
		}

	};

}
