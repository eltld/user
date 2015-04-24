package net.ememed.user2.baike;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.AccountRechargeActivity;
import net.ememed.user2.activity.AccountRechargeWebActivity;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.QuestionChatActivity;
import net.ememed.user2.baike.entity.RewardPayEntry;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.PayBackDialog;
import net.ememed.user2.wxpay.WxPay;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class RewardPayActivity extends BasicActivity {
	private static final int PAY_EMEMED = 1;
	private static final int PAY_ZHIFUBAO = 2;
	private static final int PAY_BANKCARD = 3;
	private static final int PAY_WX = 7;

	private String doctor_name;
	private String doctor_id;
	private String money;
	private String order_id;
	private String type;
	private TextView tv_doctor_name;
	private TextView tv_money;
	private TextView tv_time;
	private TextView tv_num;

	private ImageButton btn_ememed;
	private ImageButton btn_zhifubao;
	private ImageButton btn_bankcard;
	private ImageButton btn_wxpay;

	private int payStyle = -1;
	private AlertDialog myDialog;
	private boolean needRefreshRewardWall = false; // 退出是否需要更新打赏墙

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_reward_pay);
		doctor_name = getIntent().getStringExtra("doctor_name");
		doctor_id = getIntent().getStringExtra("doctor_id");
		money = getIntent().getStringExtra("money");
		order_id = getIntent().getStringExtra("order_id");
		type = getIntent().getStringExtra("type");
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(AccountRechargeActivity.class.getName());
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	protected void setupView() {
		super.setupView();

		((TextView) findViewById(R.id.top_title)).setText("打赏");

		tv_money = (TextView) findViewById(R.id.tv_money);
		tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_num = (TextView) findViewById(R.id.tv_num);
		tv_num.setText(UserPreferenceWrapper.getUserBalance() + "元");

		tv_money.setText(money + "元");
		tv_doctor_name.setText(doctor_name);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		String str = sdf.format(curDate);
		tv_time.setText(str);

		btn_ememed = (ImageButton) findViewById(R.id.btn_ememed);
		btn_zhifubao = (ImageButton) findViewById(R.id.btn_zhifubao);
		btn_bankcard = (ImageButton) findViewById(R.id.btn_bank);
		btn_wxpay = (ImageButton) findViewById(R.id.ib_wxpay);
	}

	public void doClick(View view) {
		switch (view.getId()) {
		case R.id.btn_ememed:
			selectPayStyle(PAY_EMEMED);
			break;
		case R.id.btn_zhifubao:
			selectPayStyle(PAY_ZHIFUBAO);
			break;
		case R.id.btn_bank:
			selectPayStyle(PAY_BANKCARD);
			break;
		case R.id.ib_wxpay:
			selectPayStyle(PAY_WX);
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_pay:
			if (-1 == payStyle) {
				showToast("请选择付款方式");
				return;
			} else {
				if (PAY_EMEMED == payStyle) {
					payForEmemed();
				} else if (PAY_ZHIFUBAO == payStyle) {
					String memberId = SharePrefUtil.getString(Conast.MEMBER_ID);
					String token = SharePrefUtil.getString(Conast.ACCESS_TOKEN);
					String url = "http://www.ememed.net:8004/index.php?d=right&c=alipay&m=alipay_demo&memberid="
							+ memberId + "&token=" + token + "&orderid=" + order_id;
					Intent intent = new Intent(RewardPayActivity.this,
							AccountRechargeWebActivity.class);
					intent.putExtra("url", url);
					startActivity(intent);
				} else if (PAY_WX == payStyle) {
					wxpay();
				}
			}
			break;
		}
	}

	private void wxpay() {
		WxPay wxPay = new WxPay(this);
		wxPay.setBody("打赏");
		wxPay.setPrice(money);
		wxPay.setOrderNo(order_id);
		wxPay.pay();
	}

	private void selectPayStyle(int pos) {
		btn_ememed.setBackgroundResource(R.drawable.finace_circle);
		btn_zhifubao.setBackgroundResource(R.drawable.finace_circle);
		btn_bankcard.setBackgroundResource(R.drawable.finace_circle);
		btn_wxpay.setBackgroundResource(R.drawable.finace_circle);

		if (PAY_EMEMED == pos) {
			btn_ememed.setBackgroundResource(R.drawable.finace_gou);
			payStyle = PAY_EMEMED;
		} else if (PAY_ZHIFUBAO == pos) {
			btn_zhifubao.setBackgroundResource(R.drawable.finace_gou);
			payStyle = PAY_ZHIFUBAO;
		} else if (PAY_BANKCARD == pos) {
			btn_bankcard.setBackgroundResource(R.drawable.finace_gou);
			payStyle = PAY_BANKCARD;
		} else if (PAY_WX == pos) {
			btn_wxpay.setBackgroundResource(R.drawable.finace_gou);
			payStyle = PAY_WX;
		}
	}

	/** 余额支付 */
	public void payForEmemed() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("goodsamount", money + "");
			params.put("orderamount", money + "");
			params.put("orderid", order_id);
			params.put("paytype", "1");
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.pay_set_order,
					RewardPayEntry.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.PAY;
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

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		try {
			destroyDialog();
			switch (msg.what) {
			case IResult.PAY:
				RewardPayEntry entry = (RewardPayEntry) msg.obj;
				if (null != entry) {
					if (1 == entry.getSuccess()) {
						showPayCompletedDialogue("您已通过薏米账户余额支付成功！");
						needRefreshRewardWall = true;
					} else {
						showToast(entry.getErrormsg());
					}
				}
				break;
			case IResult.NET_ERROR:
				showToast(getString(R.string.net_error));
				break;
			case IResult.DATA_ERROR:
				showToast("数据出错！");
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 显示支付完成对话框
	 */
	private void showPayCompletedDialogue(String text) {
		myDialog = new AlertDialog.Builder(this).create();
		View view = LayoutInflater.from(this).inflate(R.layout.pay_completed, null);
		TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
		tv_content.setText(text);
		LayoutParams lps = view.getLayoutParams();

		if (lps == null) {
			lps = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		}
		lps.height = LayoutParams.WRAP_CONTENT;
		lps.width = LayoutParams.WRAP_CONTENT;
		view.setLayoutParams(lps);

		// myDialog.setCanceledOnTouchOutside(true);
		Button btn_left = (Button) view.findViewById(R.id.btn_left);
		Button btn_right = (Button) view.findViewById(R.id.btn_right);
		btn_left.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RewardPayActivity.this.finish();
				myDialog.dismiss();
			}
		});

		if (type != null) {
			btn_right.setText("返回聊天界面");
		}

		btn_right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (type != null) {
					if (type.equals("1")) {
						Intent intent = new Intent(RewardPayActivity.this,
								QuestionChatActivity.class);
						startActivity(intent);
					} else {
						Intent intent = new Intent(RewardPayActivity.this, SayActivity.class);
						intent.putExtra("doctor_id", doctor_id);
						intent.putExtra("doctor_name", doctor_name);
						startActivity(intent);
					}
				} else {

					Intent intent = new Intent(RewardPayActivity.this, SayActivity.class);
					intent.putExtra("doctor_id", doctor_id);
					intent.putExtra("doctor_name", doctor_name);
					startActivity(intent);
				}
				myDialog.dismiss();
				finish();
			}
		});
		myDialog.setView(view);

		myDialog.show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(mBroadcastReceiver);
	}

	PayBackDialog backDialog;
	private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String pirsce = intent.getExtras().getString("pirsce");
			String msg = intent.getExtras().getString("msg");
			// et_recharge_num.setText("");
			/*
			 * backDialog = new PayBackDialog(context, R.style.wheelDialog); backDialog.show();
			 * backDialog.setParams(CanvasWidth); backDialog.setPrisce(pirsce);
			 * backDialog.setText("您已通过支付宝支付成功"); backDialog.setSerialSumber("");
			 */
			showPayCompletedDialogue("您已通过支付宝支付成功！");
			needRefreshRewardWall = true;
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
		Intent intent = new Intent();
		intent.setAction(MyApplication.GET_BOUNTY_LIST);
		sendBroadcast(intent);
	}
}
