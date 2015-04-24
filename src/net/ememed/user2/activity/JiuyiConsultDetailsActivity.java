package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import org.apache.http.message.BasicNameValuePair;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddSingle;
import net.ememed.user2.entity.ContactEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MessageBackupFieldEntry;
import net.ememed.user2.entity.OrderInfoEntity;
import net.ememed.user2.entity.OrderListEntity;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.entity.ServiceEvaluateEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.util.TimeUtil;
import net.ememed.user2.util.UICore;
import net.ememed.user2.widget.ColoredRatingBar;
import net.ememed.user2.widget.MenuDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chat.EMChatManager;
import com.google.gson.Gson;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.MessageSystemEvent;
/**
 * 订单信息页
 * @author pengwl
 *
 */
public class JiuyiConsultDetailsActivity extends BasicActivity implements BasicUIEvent {
	private TextView top_title;
	private Button btn_addhealth;
	private LinearLayout ll_service_evaluate;
	private LinearLayout ll_pay_money;
	private TextView tv_new_status;
	private OrderListEntry entry;
	private String title;
	private MessageBackupFieldEntry msgEntry;
	private String content;
	private TextView tv_order_hint;
	private TextView tv_step_declare;
	private String doctor_name;
	private String ORDERID;
	private String tochat_userId;
	private String doctor_avatar;
//	public ContactEntry contactEntry;
	private LinearLayout ll_communication_2;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_phone_consult_details);
		entry = (OrderListEntry) getIntent().getSerializableExtra("item");
		doctor_name = getIntent().getStringExtra("doctor_name");//
		tochat_userId = getIntent().getStringExtra("tochat_userId");
		doctor_avatar = getIntent().getStringExtra("doctor_avatar");
		source = getIntent().getIntExtra("source", 0);
		if(TextUtils.isEmpty(doctor_name) && entry!=null){
			doctor_name = entry.getDOCTORNAME();
		}
		if(entry!=null){
			ORDERID = entry.getORDERID();
		}else{
			ORDERID = getIntent().getStringExtra("ORDERID");
		}
		EventBus.getDefault().register(this, MessageSystemEvent.class);
	}

	@Override
	protected void setupView() {
		super.setupView();
		top_title = (TextView) findViewById(R.id.top_title);
		tv_new_status = (TextView) findViewById(R.id.tv_new_status);
		tv_order_hint = (TextView) findViewById(R.id.tv_order_hint);
		tv_step_declare = (TextView) findViewById(R.id.tv_step_declare);
		tv_generated_time = (TextView) findViewById(R.id.tv_generated_time);
		tv_order_number = (TextView) findViewById(R.id.tv_order_number);
		tv_service_price = (TextView) findViewById(R.id.tv_service_price);
		ll_communication = (LinearLayout) findViewById(R.id.ll_communication);
		ll_communication_2 = (LinearLayout) findViewById(R.id.ll_communication_2);
		btn_communication = (Button) findViewById(R.id.btn_communication);

		if(entry!=null){
			initOrder();
			
		}else
			getOrderInfo();
		
//		SingleEvaluate();
				
	}

	private void initOrder() {
		
		top_title.setText(PublicUtil.getServiceNameByid(entry.getORDERTYPE()));
		if (entry.getORDERTYPE().equals("1")) {//图文咨询
			if (!TextUtils.isEmpty(entry.getSTATE())) {
				if (entry.getSTATE().equals("2")) {
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_communication.setVisibility(View.VISIBLE);
				} else if (entry.getSTATE().equals("3")) {
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
					ll_service_evaluate.setVisibility(View.VISIBLE);
					SingleEvaluate();
				}
			}
			tv_step_declare.setText(getString(R.string.chat_order_text_hint));
//			tv_order_hint.setVisibility(View.VISIBLE);
		} else if (entry.getORDERTYPE().equals("2")) {//预约通话
			if (!TextUtils.isEmpty(entry.getSTATE())) {
				if (entry.getSTATE().equals("0")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
				} else if (entry.getSTATE().equals("2")) {
					ll_communication.setVisibility(View.VISIBLE);
//					tv_new_status.setText(getString(R.string.offline_consult_title_six) + TimeUtil.parseDateTime(Long.valueOf(entry.getCALL_TIME()), "yyyy-MM-dd HH:mm"));
					tv_new_status.setText(entry.getSTATE_DESC());
				} else if (entry.getSTATE().equals("3")) {
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
					ll_service_evaluate.setVisibility(View.VISIBLE);
					SingleEvaluate();
				}
			}
			tv_step_declare.setText(getString(R.string.chat_order_phone_hint));
//			tv_order_hint.setVisibility(View.);
//			tv_order_hint.setText("请注意：远程咨询不可作为疾病诊断最终依据，若身体不适请迅速前往医院，以免耽误病情。");
		} else if (entry.getORDERTYPE().equals("3")) {//预约加号
			if (!TextUtils.isEmpty(entry.getSTATE())) {
				if (entry.getSTATE().equals("0")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
				} else if (entry.getSTATE().equals("1")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_pay_money = (LinearLayout) findViewById(R.id.ll_pay_money);
					ll_pay_money.setVisibility(View.VISIBLE);
					title = getString(R.string.home_item_jiuyi);
				} else if (entry.getSTATE().equals("2")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
					ll_service_evaluate.setVisibility(View.VISIBLE);
					SingleEvaluate();
				} else if (entry.getSTATE().equals("3")) {
					tv_new_status.setText(entry.getSTATE_DESC());
					SingleEvaluate();
					
				}
			}
			tv_step_declare.setText(getString(R.string.chat_order_jiuyi_hint));
		} else if (entry.getORDERTYPE().equals("4")) {//住院
			if (!TextUtils.isEmpty(entry.getSTATE())) {
				if (entry.getSTATE().equals("0")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
				} else if (entry.getSTATE().equals("1")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_pay_money = (LinearLayout) findViewById(R.id.ll_pay_money);
					ll_pay_money.setVisibility(View.VISIBLE);
					title = getString(R.string.home_item_shangmen);
				} else if (entry.getSTATE().equals("2")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
					ll_service_evaluate.setVisibility(View.VISIBLE);
					SingleEvaluate();
				} else if (entry.getSTATE().equals("3")) {
					tv_new_status.setText(entry.getSTATE_DESC());
					SingleEvaluate();
					
				}
			}
			tv_step_declare.setText(getString(R.string.chat_order_shangmen_hint));
		} else if (entry.getORDERTYPE().equals("14")) {//上门
			if (!TextUtils.isEmpty(entry.getSTATE())) {
				if (entry.getSTATE().equals("0")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
				} else if (entry.getSTATE().equals("1")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_pay_money = (LinearLayout) findViewById(R.id.ll_pay_money);
					ll_pay_money.setVisibility(View.VISIBLE);
					title = getString(R.string.home_item_zhuyuan);
				} else if (entry.getSTATE().equals("2")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
					ll_service_evaluate.setVisibility(View.VISIBLE);
					SingleEvaluate();
				} else if (entry.getSTATE().equals("3")) {
					tv_new_status.setText(entry.getSTATE_DESC());
					SingleEvaluate();
					
				}
			}
			tv_step_declare.setText(getString(R.string.chat_order_zhuyuan_hint));
		} else if (entry.getORDERTYPE().equals("15")) {//签约私人医生
			tv_confirm_time=(TextView)findViewById(R.id.tv_confirm_time);
			tv_confirm_name=(TextView)findViewById(R.id.tv_confirm_name);
			ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
			ll_private_doctor = (LinearLayout) findViewById(R.id.ll_private_doctor);
			tv_private_time = (TextView) findViewById(R.id.tv_private_time);
			tv_private_describe = (TextView) findViewById(R.id.tv_private_describe);
			tv_private_des = (TextView) findViewById(R.id.tv_private_des);
			if (!TextUtils.isEmpty(entry.getSTATE())) {
				if (entry.getSTATE().equals("2")) {
					ll_communication_2.setVisibility(View.VISIBLE);
					ll_private_doctor.setVisibility(View.GONE);
					tv_new_status.setText(entry.getSTATE_DESC());
					long now_time = new Date().getTime();
					try {
						int STATUS =Integer.parseInt(entry.getCALL_TIME_STATUS());
						String parseDateTime = null;
						if(TextUtils.isEmpty(entry.getPACKET_STARTTIME())&&TextUtils.isEmpty(entry.getPACKET_ENDTIME()))
						{	
						   tv_confirm_name.setVisibility(View.GONE);
						   tv_confirm_time.setVisibility(View.GONE);
//						   Log.d("1", "1");
						}
						else
						{
							tv_confirm_name.setVisibility(View.VISIBLE);
							tv_confirm_time.setVisibility(View.VISIBLE);	
							tv_confirm_time.setText(entry.getPACKET_STARTTIME()+" "+"至"+" "+entry.getPACKET_ENDTIME());
//							Log.d("4", "4");
						}
						
						
						switch (STATUS) {
						case 0://未完成
//							Log.d("2", "2");
							if(TextUtils.isEmpty(entry.getCALL_TIME())){//预约时间外
								tv_private_describe.setVisibility(View.VISIBLE);
								tv_private_time.setVisibility(View.GONE);
								tv_private_des.setVisibility(View.GONE);
							}else if(now_time>Long.parseLong(entry.getCALL_TIME()+"000")){//预约时间外
								tv_private_describe.setText(doctor_name+"医生设置了通话时间，请届时保持手机畅通");
								parseDateTime = TimeUtil.parseDateTime(Long.parseLong(entry.getCALL_TIME()),"yy/MM/dd(HH:mm)");
								tv_private_time.setVisibility(View.VISIBLE);
								tv_private_des.setVisibility(View.GONE);
								tv_private_time.setText(Html.fromHtml("通话时间：<font color='red'>"+parseDateTime+"</font>"));
								
							}else if(now_time<=Long.parseLong(entry.getCALL_TIME()+"000")){//预约时间内
								tv_private_describe.setText(doctor_name+"医生设置了通话时间，请届时保持手机畅通");
								parseDateTime = TimeUtil.parseDateTime(Long.parseLong(entry.getCALL_TIME()),"yy/MM/dd(HH:mm)");
								tv_private_time.setVisibility(View.VISIBLE);
								tv_private_des.setVisibility(View.GONE);
								tv_private_time.setText(Html.fromHtml("通话时间：<font color='red'>"+parseDateTime+"</font>"));
							}
							break;
						case 1://通话成功
							tv_private_describe.setText("通话已达成，本次通话服务结束");
							parseDateTime = TimeUtil.parseDateTime(Long.parseLong(entry.getCALL_TIME()),"yy/MM/dd(HH:mm)");
							tv_private_time.setText(Html.fromHtml("通话时间：<font color='red'>"+parseDateTime+"</font>"));
							break;
						case 2://通话失败
							tv_private_describe.setText("通话已过期，本次通话服务结束");
							parseDateTime = TimeUtil.parseDateTime(Long.parseLong(entry.getCALL_TIME()),"yy/MM/dd(HH:mm)");
							tv_private_time.setText(Html.fromHtml("通话时间：<font color='red'>"+parseDateTime+"</font>"));
							break;
							
						case -1://放弃

							break;

						default:

							break;
						}
					} catch (NumberFormatException e) {
						tv_private_describe.setVisibility(View.VISIBLE);
						tv_private_time.setVisibility(View.GONE);
						tv_private_des.setVisibility(View.GONE);

					}
					
					
					
					
					
					
					
				} else if (entry.getSTATE().equals("3")) {
					tv_new_status.setText(entry.getSTATE_DESC());
//					ll_service_evaluate.setVisibility(View.VISIBLE);
					PrvateSingleEvaluate();
					tv_confirm_name.setVisibility(View.GONE);
					tv_confirm_time.setVisibility(View.GONE);
//					Log.d("3", "3");
				}
			}
			tv_step_declare.setText(getString(R.string.chat_order_private_hint));
//			ll_service_evaluate.setVisibility(View.VISIBLE);
//			SingleEvaluate();
		} else if (entry.getORDERTYPE().equals("16")) {//其他服务
			if (!TextUtils.isEmpty(entry.getSTATE())) {
				if (entry.getSTATE().equals("0")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
				} else if (entry.getSTATE().equals("1")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_pay_money = (LinearLayout) findViewById(R.id.ll_pay_money);
					ll_pay_money.setVisibility(View.VISIBLE);
					title = getString(R.string.home_item_custom);
				} else if (entry.getSTATE().equals("2")) {
					ll_communication.setVisibility(View.VISIBLE);
					tv_new_status.setText(entry.getSTATE_DESC());
					ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
					ll_service_evaluate.setVisibility(View.VISIBLE);
					SingleEvaluate();
				} else if (entry.getSTATE().equals("3")) {
					tv_new_status.setText(entry.getSTATE_DESC());
					SingleEvaluate();
					
				}
			}
			tv_step_declare.setText(getString(R.string.chat_order_custom_hint));
		}
		
		tv_generated_time.setText(entry.getADDTIME());
		
		tv_order_number.setText(entry.getORDERSN());
		
		tv_service_price.setText(setPrice(entry.getORDERTYPE(),entry.getORDERAMOUNT(),entry.getSTATE()));
		
		
		setCommunication();
//		setServiceEvalate();
		
	}
	private void setCommunication() {
		switch (source) {
		case 0:
			ll_communication.setVisibility(View.GONE);
			break;
		case 1:
			btn_communication.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent i = new Intent(JiuyiConsultDetailsActivity.this,ChatActivity.class);
//					i.putExtra("doctor_name", doctor_name);
//					i.putExtra("doctor_avatar", doctor_avatar);
//					i.putExtra("tochat_userId", tochat_userId);
//					i.putExtra("state", entry.getSTATE());
//					i.putExtra("ordertype", entry.getORDERTYPE());
//					i.putExtra("desc_text", entry.getDESC_TEXT());
//					startActivity(i);
					finish();
				}
			});
			break;
		case 2:
			btn_communication.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent i = new Intent(JiuyiConsultDetailsActivity.this,DoctorDetailInfoActivity.class);
					i.putExtra("doctor_name", doctor_name);
//					i.putExtra("doctor_avatar", oiEntity.getData().getAVATAR());
					i.putExtra("tochat_userId", entry.getDOCTORID());
					startActivity(i);
					finish();
				}
			});
			break;
		case 3:
			btn_communication.setOnClickListener(new Button.OnClickListener() {
				
				@Override
				public void onClick(View v) {
//					Intent i = new Intent(JiuyiConsultDetailsActivity.this,DoctorDetailInfoActivity.class);
//					setResult(2002, i);
//					finish();
					
					Intent i = new Intent(JiuyiConsultDetailsActivity.this,ChatActivity.class);
					i.putExtra("doctor_name", doctor_name);
					i.putExtra("doctor_avatar", doctor_avatar);
					i.putExtra("tochat_userId", tochat_userId);
					i.putExtra("orderid", ORDERID);
					i.putExtra("state", entry.getSTATE());
					i.putExtra("ordertype", entry.getORDERTYPE());
					i.putExtra("desc_text", entry.getDESC_TEXT());
					i.putExtra("time", entry.getCALL_TIME());
					startActivity(i);
					finish();
					
				}
			});
			break;

		default:
			break;
		}
	}

	private String setPrice(String orderType,String price,String STATE){
		try {
			int type = Integer.parseInt(orderType);
			int state = Integer.parseInt(STATE);
			switch (type) {
			case 1://图文
			case 2://电话
			case 15://签约私人医生		
				if(TextUtils.isEmpty(price) || "0".equals(price)){
					return "免费";
				}else{
					return price+"元";
				}
			case 3://就医
			case 4://住院
			case 14://上门
			case 16://其他
				switch (state) {
				case 0:
//					return "";
				case 1:
//					return "待支付...";
				case 2:
				case 3:
				case 4:
//					if(TextUtils.isEmpty(price) || "0".equals(price)){
//						return "待支付...";
//					}else{
//						return price+"元";
//					}
					if(TextUtils.isEmpty(price)){
						return "待支付...";
					}else{
						return price+"元";
					}
				}
				break;
			}
		} catch (NumberFormatException e) {
			return null;
		}
		return null;
	}
	
	

	private void SingleEvaluate() {
		ll_order_evaluate = (LinearLayout) findViewById(R.id.ll_order_evaluate);
		rtb_service_goods2 = (ColoredRatingBar) findViewById(R.id.rtb_service_goods);
		ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
		try {
			int EVALUATIO = Integer.parseInt(entry.getEVALUATION());
			int DSRATTITUDE = Integer.parseInt(entry.getDSRATTITUDE());
			if(EVALUATIO>0){
				ll_order_evaluate.setVisibility(View.VISIBLE);
				rtb_service_goods2.setRating(DSRATTITUDE/2);
				ll_service_evaluate.setVisibility(View.GONE);
			}else{
				ll_order_evaluate.setVisibility(View.GONE);
				ll_service_evaluate.setVisibility(View.VISIBLE);
				
			}
		} catch (NumberFormatException e) {
			ll_order_evaluate.setVisibility(View.GONE);
			ll_service_evaluate.setVisibility(View.VISIBLE);
		}
	}
	private void PrvateSingleEvaluate() {
		ll_order_evaluate = (LinearLayout) findViewById(R.id.ll_order_evaluate);
		rtb_service_goods2 = (ColoredRatingBar) findViewById(R.id.rtb_service_goods);
		ll_service_evaluate = (LinearLayout) findViewById(R.id.ll_service_evaluate);
		try {
			int EVALUATIO = Integer.parseInt(entry.getPACKET_SERVICE().get(0).getEVALUATION());
			int DSRATTITUDE = Integer.parseInt(entry.getPACKET_SERVICE().get(0).getDSRATTITUDE());
			if(EVALUATIO>0){
				ll_order_evaluate.setVisibility(View.VISIBLE);
				rtb_service_goods2.setRating(DSRATTITUDE/2);
				ll_service_evaluate.setVisibility(View.GONE);
			}else{
				ll_order_evaluate.setVisibility(View.GONE);
				ll_service_evaluate.setVisibility(View.VISIBLE);
				
			}
		} catch (NumberFormatException e) {
			ll_order_evaluate.setVisibility(View.GONE);
			ll_service_evaluate.setVisibility(View.VISIBLE);
		}
	}

	public void doClick(View view) {
		
		int id = view.getId();
		switch (view.getId()) {
		case R.id.more_textconsult://图文咨询
		case R.id.more_phoneconsult://预约通话
			if (NetWorkUtils.detect(this)) {
				if (null == EMChatManager.getInstance() || !EMChatManager.getInstance().isConnected()) {
					this.showToast(getString(R.string.connect_chat_failed));
				} else {
					go2BuyServiceAct(id);
				}	
			} else {
				showToast(IMessage.NET_ERROR);
			} 
			break;
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_free_phone_num:
			try {
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:4009003333"));
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case R.id.btn_service_evaluate:
			ServiceEvaluate();
			break;
		case R.id.btn_pay_money:
			Intent intent = new Intent(this, PayingActivity.class);// 去支付
			intent.putExtra("doctor_name", doctor_name);
			intent.putExtra("title", title);
			intent.putExtra("orderId", entry.getORDERID());
			// intent.putExtra("serviceId", entry.getSERVICEID());
			intent.putExtra("price", Double.valueOf(entry.getORDERAMOUNT()));
			intent.putExtra("ordertype", entry.getORDERTYPE());
			// intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
			startActivity(intent);
			break;

		default:
			break;
		}
		
		
	}

	/** 获取订单信息 */
	private void getOrderInfo() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
//			showProgressDialog();
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("orderid", ORDERID);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_order_info, OrderInfoEntity.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = IResult.ORDER_INFO;
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

	private EditText et_evaluate_hint;
	private ColoredRatingBar rtb_service_goods;
	protected float rating;
	private LinearLayout ll_order_evaluate;
	private ColoredRatingBar rtb_service_goods2;
	private TextView tv_generated_time;
	private TextView tv_confirm_time;
	private TextView tv_confirm_name;
	private TextView tv_order_number;
	private TextView tv_service_price;
	private LinearLayout ll_private_doctor;
	private TextView tv_private_time;
	private TextView tv_private_describe;
	private TextView tv_private_des;
	private LinearLayout ll_communication;
	private int source;
	private Button btn_communication;
	private OrderInfoEntity oiEntity;

	/** 服务评价 Dialog */
	private void ServiceEvaluate() {
		View mModeView = getLayoutInflater().inflate(R.layout.dialog_service_evaluate, null);
		rtb_service_goods = (ColoredRatingBar) mModeView.findViewById(R.id.rtb_service_goods);
		rtb_service_goods.setRating(4);
		et_evaluate_hint = (EditText) mModeView.findViewById(R.id.et_evaluate_hint);
		rtb_service_goods.setIndicator(false);
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(mModeView).setNegativeButton("取消", null).setPositiveButton("确定", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
//				rtb_service_goods.setNumStars(5);
				rating = rtb_service_goods.getRating() * 2;
				content = et_evaluate_hint.getText().toString().trim();
				getServiceEvaluate();
			}
		}).show();
	}

	protected void getServiceEvaluate() {
		UICore.eventTask(this, this, IResult.SERVICE_EVALUATE, "努力加载中...", null);// 加载数据入口
	}
	/**设置服务评价
	 * */
	private void setServiceEvalate() {
		if (NetWorkUtils.detect(this)) {
			
			ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
			params.add(new BasicNameValuePair("token",SharePrefUtil.getString(Conast.ACCESS_TOKEN)));
			params.add(new BasicNameValuePair("channel","android"));
			params.add(new BasicNameValuePair("memberid", SharePrefUtil.getString(Conast.MEMBER_ID)));
			params.add(new BasicNameValuePair("orderid", entry.getDOCTORID()));
			params.add(new BasicNameValuePair("evaluate_num", rating + ""));
			params.add(new BasicNameValuePair("content", content));
			String SERVICEID = null;
			if("15".equals(entry.getORDERTYPE())){
				if(entry.getPACKET_SERVICE()!=null &&entry.getPACKET_SERVICE().size()>1)
					SERVICEID = entry.getPACKET_SERVICE().get(0).getSERVICEID();
			}else{
				SERVICEID = entry.getSERVICEID();
			}
			params.add(new BasicNameValuePair("serviceid", SERVICEID));
			params.add(new BasicNameValuePair("appversion", PublicUtil.getVersionName(JiuyiConsultDetailsActivity.this)));
			
			String result;
			try {
				result = HttpUtil.getString(HttpUtil.URI + HttpUtil.user_evaluate_doctor_service,params, HttpUtil.POST);
				result = TextUtil.substring(result, "{");
				Gson gson = new Gson();
				ServiceEvaluateEntry reason = gson.fromJson(result, ServiceEvaluateEntry.class);
				Message msg = Message.obtain();
				msg.what = IResult.SERVICE_EVALUATE;
				msg.obj = reason;
				handler.sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
				Message message = new Message();
				message.obj = e.getMessage();
				message.what = IResult.DATA_ERROR;
				handler.sendMessage(message);
			}
			
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		if (mes == IResult.SERVICE_EVALUATE) {
			setServiceEvalate();
		}
		
	}

	@Override
	protected void onResult(Message msg) {
		try {
			switch (msg.what) {
			case IResult.ORDER_INFO:
				destroyDialog();
//				cancleProgressDialog()
				oiEntity = (OrderInfoEntity) msg.obj;
				if (null != oiEntity) {
					if (oiEntity.getSuccess() == 1) {
						orderInfo(oiEntity);
						setServiceEvalate();
					} else {
						showToast(oiEntity.getErrormsg());
					}
				} else {
					showToast("加载数据失败");
				}
				break;
			case IResult.SERVICE_EVALUATE:
				ServiceEvaluateEntry seEntry = (ServiceEvaluateEntry) msg.obj;
				if (null != seEntry) {
					if (seEntry.getSuccess() == 1) {
						showToast(seEntry.getErrormsg());
						ll_order_evaluate.setVisibility(View.VISIBLE);
						rtb_service_goods2.setRating(rating/2);
						ll_service_evaluate.setVisibility(View.GONE);
						ll_communication.setVisibility(View.GONE);
					} else {
						showToast(seEntry.getErrormsg());
					}
				} else {
					showToast("加载数据失败");
				}
				break;
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	public void orderInfo(OrderInfoEntity oiEntity) {
//		TextView tv_generated_time = (TextView) findViewById(R.id.tv_generated_time);
//		tv_generated_time.setText(oiEntity.getData().getADDTIEM());
//		TextView tv_order_number = (TextView) findViewById(R.id.tv_order_number);
//		tv_order_number.setText(oiEntity.getData().getORDERSN());
		doctor_name = oiEntity.getData().getANY_USER_NAME();
		entry = new OrderListEntry();
		entry.setpACKET_STARTTIME(oiEntity.getData().getPACKET_STARTTIME());
		entry.setPACKET_ENDTIME(oiEntity.getData().getPACKET_ENDTIME());
		entry.setADDTIME(oiEntity.getData().getADDTIEM());
		entry.setEVALUATION(oiEntity.getData().getEVALUATION());
		entry.setDSRATTITUDE(oiEntity.getData().getDSRATTITUDE());
		entry.setORDERID(oiEntity.getData().getORDERID());
		entry.setORDERAMOUNT(oiEntity.getData().getORDERAMOUNT());
		entry.setORDERSN(oiEntity.getData().getORDERSN());
		entry.setORDERTYPE(oiEntity.getData().getORDERTYPE());
		entry.setDOCTORID(oiEntity.getData().getDOCTORID());
		entry.setSERVICEID(oiEntity.getData().getSERVICEID());
		entry.setSTATE(oiEntity.getData().getSTATE());
		entry.setCALL_TIME(oiEntity.getData().getCALL_TIME());
		entry.setCALL_TIME_STATUS(oiEntity.getData().getCALL_TIME_STATUS());
		entry.setSTATE(oiEntity.getData().getSTATE());
		source=2;
		initOrder();
	}

	public void onEvent(MessageSystemEvent testEvent) {
		msgEntry = testEvent.getMsgEntry();
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().unregister(MessageSystemEvent.class);
	}
	
	private void go2BuyServiceAct(int bt_id) {
		if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))||!SharePrefUtil.getBoolean(Conast.LOGIN)){//没登录
			Intent intent = new Intent(this,LoginActivity.class);
			startActivity(intent);
		} else if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_SEX))||TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName))||TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_BIRTHDAY))||TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_MOBILE))){
			//资料没完善
			MenuDialog.Builder alert = new MenuDialog.Builder(this);
			MenuDialog dialog = alert.setTitle(R.string.system_info).setMessage(R.string.you_should_improve_data)
					.setPositiveButton(getString(R.string.confirm_yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,int which) {
									dialog.dismiss();
									Intent intent = new Intent(JiuyiConsultDetailsActivity.this,PersonInfoActivity.class);
									startActivity(intent);
								}
							})
					.setNegativeButton(getString(R.string.confirm_no),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			 if (bt_id == R.id.more_phoneconsult) {//预约通话s
				 goChat();
			 }else if(bt_id == R.id.more_textconsult){//图文咨询
				 //进入聊天界面
				 Intent intent  = new Intent(JiuyiConsultDetailsActivity.this, BuyTextConsultActivity.class);
				 intent.putExtra("doctor_name", doctor_name);
					intent.putExtra("doctorid", tochat_userId);
					intent.putExtra("doctor_avatar", doctor_avatar);
					intent.putExtra("orderid", ORDERID);
					startActivity(intent);
			 }
			
			
		}
	}
	 public void goChat(){
	    	AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    	builder.setTitle("系统提示")
	    	.setMessage("请与医生沟通通话时间，并提醒医生在订单页设置通话时间")
	    	.setPositiveButton("取消", null)
	    	.setNegativeButton("确定", new AlertDialog.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					//进入聊天界面
					Intent intent  = new Intent(JiuyiConsultDetailsActivity.this, BuyPhoneConsultActivity.class);
					intent.putExtra("doctor_name", doctor_name);
					intent.putExtra("doctorid", tochat_userId);
					intent.putExtra("doctor_avatar", doctor_avatar);
					intent.putExtra("orderid", ORDERID);
					startActivity(intent);
				}
			}).show();  	
	    }
	 
	
}
