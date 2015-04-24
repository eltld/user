package net.ememed.user2.activity;


import java.util.Iterator;
import java.util.List;

import com.easemob.chat.EMChatManager;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MemberInfo;
import net.ememed.user2.entity.MemberInfoEntity;
import net.ememed.user2.entity.OfferServiceEntry;
import net.ememed.user2.entity.OfferServiceEntry2;
import net.ememed.user2.entity.OfferServicePacketEntry;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.MenuDialog;
import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DescribeServiceActivity extends BasicActivity {
	
	private LinearLayout parent_dialog;
	private TextView top_title;
	private TextView tv_presentation;
	private int service_type;
	private String service_buy;
	private String doctor_name;
	private Button btn_confirm;
//	private DoctorEntry entry;
	private boolean isOpenPrivateDoctor = false;
	private MemberInfo memberInfo;
	private String PrivateDoctor_Order;
	private MemberInfoEntity data;
	private OrderListEntry orderData;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_describe_service);
		service_type = getIntent().getIntExtra("service_type", 0);
		service_buy = getIntent().getStringExtra("service_buy");
		PrivateDoctor_Order = getIntent().getStringExtra("PrivateDoctor_Order");
		doctor_name = getIntent().getStringExtra("doctor_name");
//		entry = (DoctorEntry) getIntent().getSerializableExtra("entry");
		memberInfo = (MemberInfo) getIntent().getSerializableExtra("memberInfo");
		data = memberInfo.getData();
		
		
		
		List<OrderListEntry> open_ORDER = data.getOPEN_ORDER();
		for (Iterator iterator = open_ORDER.iterator(); iterator.hasNext();) {
			OrderListEntry orderListEntry = (OrderListEntry) iterator.next();
			if ("15".equals(orderListEntry.getORDERTYPE())) {
				isOpenPrivateDoctor = true;
				PrivateDoctor_Order = orderListEntry.getORDERID();
				orderData = orderListEntry;
			}
		}
		
//		MyApplication instance = MyApplication.getInstance();
//		LinearLayout parent_dialog = (LinearLayout) findViewById(R.id.parent_dialog);
//		LayoutParams layoutParams = parent_dialog.getLayoutParams();
//		int dip2px = Util.dip2px(this, 60);
////		 instance.canvasHeight-dip2px)
//		if(layoutParams==null)
//			layoutParams = new LayoutParams(instance.canvasWidth-dip2px, LayoutParams.WRAP_CONTENT);
//		layoutParams.width = instance.canvasWidth-dip2px;
//		layoutParams.height = LayoutParams.WRAP_CONTENT;
//		parent_dialog.setLayoutParams(layoutParams);
		
		
	}
	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		tv_presentation = (TextView) findViewById(R.id.tv_presentation);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		LinearLayout ll_text = (LinearLayout) findViewById(R.id.ll_text);
		LinearLayout ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		LinearLayout ll_private_doctor = (LinearLayout) findViewById(R.id.ll_private_doctor);
		LinearLayout ll_xianxia = (LinearLayout) findViewById(R.id.ll_xianxia);
		
		String presentation = "        ";
		switch (service_type) {
		case 1:
			top_title.setText("图文咨询");
			presentation += getResources().getString(R.string.text_presentation);
			ll_text.setVisibility(View.VISIBLE);
			break;
		case 2:
			top_title.setText("预约通话");
			presentation += getResources().getString(R.string.phone_presentation);
			ll_phone.setVisibility(View.VISIBLE);
			
			break;
		case 3:
			top_title.setText("预约加号");
			presentation += getResources().getString(R.string.jiuyi_presentation);
			ll_xianxia.setVisibility(View.VISIBLE);
			break;
		case 4:
			top_title.setText("上门会诊");
			presentation += getResources().getString(R.string.shangmen_presentation);
			ll_xianxia.setVisibility(View.VISIBLE);
			
			
			break;
		case 14:
			top_title.setText("预约住院");
			presentation += getResources().getString(R.string.zhuyuan_presentation);
			ll_xianxia.setVisibility(View.VISIBLE);
			
			break;
		case 15:
			top_title.setText("签约私人医生");
			presentation += getResources().getString(R.string.private_doctor_presentation);
			ll_private_doctor.setVisibility(View.VISIBLE);
			
			break;
		case 16:
			top_title.setText("其他服务");
			presentation += getResources().getString(R.string.other_presentation);
			ll_xianxia.setVisibility(View.VISIBLE);
			
			break;
	

		default:
			break;
		}
		tv_presentation.setText(presentation);
		if(!TextUtils.isEmpty(service_buy)){
			btn_confirm.setText(service_buy);
		}
		
	}
	public void doClick(View v){
		int id = v.getId();
		switch (id) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_confirm:
			if (NetWorkUtils.detect(this)) {
//				if (null == EMChatManager.getInstance() || !EMChatManager.getInstance().isConnected()) {
//					this.showToast(getString(R.string.connect_chat_failed));
//				} else {
					go2BuyServiceAct(service_type);
//				}	
			} else {
//				sendEmptyMessage(IResult.NET_ERROR);
				showToast("网络异常，请检测您的网络设置");
			} 
			break;
			
			
		default:
			break;
		}
	}

	
	
	private void go2BuyServiceAct(int bt_id) {
		if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)) {// 没登录
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
		} else if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_SEX))
				|| TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName))
				|| TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_BIRTHDAY))
				|| TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_MOBILE))) {
			// 资料没完善
			MenuDialog.Builder alert = new MenuDialog.Builder(this);
			MenuDialog dialog = alert.setTitle(R.string.system_info).setMessage(R.string.you_should_improve_data)
					.setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intent = new Intent(DescribeServiceActivity.this, PersonInfoActivity.class);
							startActivity(intent);
						}
					}).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}).create();
			dialog.setCanceledOnTouchOutside(true);
			dialog.show();
		} else {
			Intent intent = null;
			if (bt_id == R.id.btn_consult) {
				intent = new Intent(DescribeServiceActivity.this, BuyTextConsultActivity.class);
			} else if (bt_id == R.id.btn_private_doctor) {
				if (isOpenPrivateDoctor) {
					intent = new Intent(DescribeServiceActivity.this, JiuyiConsultDetailsActivity.class);
					intent.putExtra("item", orderData);
					intent.putExtra("doctor_name", data.getREALNAME());
					intent.putExtra("tochat_userId", data.getMEMBERID());
					intent.putExtra("doctor_avatar", data.getAVATAR());
					startActivity(intent);
					return;
				} else {
					intent = new Intent(DescribeServiceActivity.this, PrivateDoctorActivity.class);
				}
			} else if (bt_id == R.id.btn_jiuyi) {
				intent = new Intent(DescribeServiceActivity.this, BuyJiuyiActivity.class);
			} else if (bt_id == R.id.btn_zhuyuan) {
				intent = new Intent(DescribeServiceActivity.this, BuyZhuyuanActivity.class);
			} else if (bt_id == R.id.btn_shangmen) {
				intent = new Intent(DescribeServiceActivity.this, BuyShangmenActivity.class);
			} else if (bt_id == R.id.btn_phone_consult) {

				intent = new Intent(DescribeServiceActivity.this, BuyPhoneConsultActivity.class);
			} else if (bt_id == R.id.btn_custom) {
				intent = new Intent(DescribeServiceActivity.this, BuyCustomActivity.class);
			}
			if (data == null) {
				Toast.makeText(DescribeServiceActivity.this, "数据加载加载中请稍后", 0).show();
				return;
			}
			intent.putExtra("doctorid", data.getMEMBERID());
			intent.putExtra("doctorServiceEntry", data.getOFFER_SERVICE());
			intent.putExtra("doctor_name", doctor_name);
			startActivity(intent);
			finish();
		}
	}
	
//	private void go2BuyServiceAct() {
//		if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)) {// 没登录
//			Intent intent = new Intent(this, LoginActivity.class);
//			intent.putExtra("origin", DoctorClininActivity.class.getSimpleName());
//			intent.putExtra("current_service", service_type+"");
//			intent.putExtra("entry", entry);
//			startActivity(intent);
//		} else if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName)) || TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_SEX)) || TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_BIRTHDAY)) || TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_MOBILE))) {
//			// 资料没完善
//			MenuDialog.Builder alert = new MenuDialog.Builder(this);
//			MenuDialog dialog = alert.setTitle(R.string.system_info).setMessage(R.string.you_should_improve_data).setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					Intent intent = new Intent(DescribeServiceActivity.this, PersonInfoActivity.class);
//					intent.putExtra("origin", DoctorClininActivity.class.getSimpleName());
//					intent.putExtra("current_service", service_type+"");
//					intent.putExtra("entry", entry);
//					startActivity(intent);
//				}
//			}).setNegativeButton(getString(R.string.confirm_no), new DialogInterface.OnClickListener() {
//
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//				}
//			}).create();
//			dialog.setCanceledOnTouchOutside(true);
//			dialog.show();
//		} else {
//			Intent intent = null;
//			DoctorServiceEntry doctorServiceEntry = null;
//			if (service_type == 1) {
//					if (isOpenPrivateDoctor) {
//						intent = new Intent(this, DoctorDetailInfoActivity.class);
//						intent.putExtra("doctor_name", doctor_name);
//						intent.putExtra("doctor_avatar", entry.getAVATAR());
//						intent.putExtra("tochat_userId", entry.getDOCTORID());
//					} else {
//						intent = new Intent(this, BuyTextConsultActivity.class);
//						 doctorServiceEntry = new DoctorServiceEntry();
//							OfferServiceEntry2 entry2 = new OfferServiceEntry2();
//							entry2.setOFFER_PRICE(entry.getTEXT_OFFER_PRICE());
//							doctorServiceEntry.setOFFER_TEXT(entry2);
//					}
////				}
//			} else if (service_type == 15) {
//					if (isOpenPrivateDoctor) {
//						intent = new Intent(this, JiuyiConsultDetailsActivity.class);
//						intent.putExtra("ORDERID", PrivateDoctor_Order);
//						intent.putExtra("source", 1);
//					} else {
//						
//						intent = new Intent(this, PrivateDoctorActivity.class);
//						 doctorServiceEntry = new DoctorServiceEntry();
//							OfferServicePacketEntry packetEntry = new OfferServicePacketEntry();
//							packetEntry.setPACKET_ID(entry.getPACKET_ID());
//							packetEntry.setPACKET_PERIOD_LIST(entry.getPERIOD_LIST());
//							doctorServiceEntry.setOFFER_PACKET(packetEntry);
//					}
////				}/
//			} else if (service_type == 3) {
//				intent = new Intent(this, BuyJiuyiActivity.class);
//			} else if (service_type == 14) {
//				intent = new Intent(this, BuyZhuyuanActivity.class);
//			} else if (service_type == 4) {
//				intent = new Intent(this, BuyShangmenActivity.class);
//			} else if (service_type == 2) {
//					if (isOpenPrivateDoctor) {
//						goChat();
//						return;
//
//					} else {
//						doctorServiceEntry = new DoctorServiceEntry();
//						OfferServiceEntry entry2 = new OfferServiceEntry();
//						entry2.setOFFER_PRICE(Float.parseFloat(entry.getCALL_OFFER_PRICE()));
//						doctorServiceEntry.setOFFER_CALL(entry2);
//						intent = new Intent(this, BuyPhoneConsultActivity.class);
//					}
////				}
//			} else if (service_type == 16) {
//				intent = new Intent(this, BuyCustomActivity.class);
//			}
//	
//			intent.putExtra("doctorid",entry.getDOCTORID());
//			intent.putExtra("doctorServiceEntry", doctorServiceEntry);
//			intent.putExtra("doctor_name", doctor_name);
//			startActivity(intent);
//		}
//		
//		finish();
//	}
	
//	public void goChat() {
//		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//		builder.setTitle("系统提示").setMessage("请与医生沟通通话时间，并提醒医生在订单页设置通话时间").setPositiveButton("取消", null).setNegativeButton("确定", new AlertDialog.OnClickListener() {
//
//			@Override
//			public void onClick(DialogInterface dialog, int which) {
//				Intent intent = new Intent(DescribeServiceActivity.this, DoctorDetailInfoActivity.class);
//				intent.putExtra("doctor_name", doctor_name);
//				intent.putExtra("doctor_avatar", entry.getAVATAR());
//				intent.putExtra("tochat_userId", entry.getDOCTORID());
//				startActivity(intent);
//			}
//		}).show();
//	}
}
