package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddSingle;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.OfferServicePacketEntry;
import net.ememed.user2.entity.PacketBuyEntry;
import net.ememed.user2.entity.PacketBuyInfo;
import net.ememed.user2.entity.PacketPeriod;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import android.content.Intent;
import android.content.DialogInterface.OnShowListener;
import android.content.pm.PackageInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.renderscript.Sampler.Value;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

public class PrivateDoctorActivity extends BasicActivity {
	private TextView top_title;
	// private DoctorEntry entry;
	private String doctor_name;
	private String doctorid;
	private DoctorServiceEntry entry;
//	private float price;
	private String upload_file = "";
	private String doctor_avatar;
	private RadioGroup radioGroup;
//	private RadioButton radioButtonWeek;
//	private RadioButton radioButtonMoth;
//	private RadioButton radioButtonThreeMoth;
	private List<RadioButton> radioButtonS;
	
	private String content;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_private_doctor);
		doctorid = getIntent().getStringExtra("doctorid");
		entry = (DoctorServiceEntry) getIntent().getSerializableExtra(
				"doctorServiceEntry");
		// entry = (DoctorEntry) getIntent().getSerializableExtra("entry");
		doctor_name = getIntent().getStringExtra("doctor_name");
//		price = getIntent().getFloatExtra("price", 0);
		doctor_avatar = getIntent().getStringExtra("doctor_avatar");
	}

	@Override
	protected void setupView() {
		super.setupView();
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("购买" + doctor_name + "签约私人医生");
		if (null != entry.getOFFER_PACKET() && null != entry.getOFFER_PACKET().getPACKET_PERIOD_LIST()) {
			List<PacketPeriod> packet_PERIOD_LIST = entry.getOFFER_PACKET().getPACKET_PERIOD_LIST();
			
//			Collections.sort(packet_PERIOD_LIST,new Comparator<PacketPeriod>(){
//
//				@Override
//				public int compare(PacketPeriod arg0, PacketPeriod arg1) {
//					Integer x0 = 0;
//					Integer x1 = 0;
//					try {
//						x0 = Integer.parseInt(arg0.getPacket_period_price());
//						x1 = Integer.parseInt(arg1.getPacket_period_price());
//					} catch (NumberFormatException e) {
//						return arg0.getPacket_period_price().compareTo(arg1.getPacket_period_price());
//					}
//					return x0.compareTo(x1);
//				}
//			});
			radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
			for (Iterator iterator = packet_PERIOD_LIST.iterator(); iterator
					.hasNext();) {
				PacketPeriod packetPeriod = (PacketPeriod) iterator.next();
				if (null != packetPeriod && !TextUtils.isEmpty(packetPeriod.getPacket_period_price())) {
					RadioButton rb = new RadioButton(this);
					LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
					rb.setLayoutParams(params);
					String packet_period_price = packetPeriod.getPacket_period_price();
					String replace = packetPeriod.getPacket_period_desc().replace(packet_period_price, "");
					replace = replace.replace("/1周", "/周");
					replace = replace.replace("/1月", "/月");
					replace = replace.replace("/1年", "/年");
					rb.setTag(packetPeriod);
					rb.setText(Html.fromHtml("<font color='red'>"+Util.DecZore(Double.parseDouble(packet_period_price))+"</font>"+replace));
					rb.setTextColor(Color.BLACK);
					radioGroup.addView(rb);
					rb.setOnCheckedChangeListener(new RadioButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							if(isChecked){
								packet_period_selected = (PacketPeriod)buttonView.getTag();
							}
						}
					});					
				}
			}			
		}
	}
	PacketPeriod packet_period_selected = null;

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.btn_pay_money) {
			if (null != packet_period_selected) {
				addSingleOrder(packet_period_selected);	
			} else {
				showToast("请先选择服务周期");
			}
		} else if (view.getId() == R.id.btn_bank_card) {
			Intent intent = new Intent(this, DoctorDetailInfoActivity.class);
			intent.putExtra("title", "林海滨医生");
			intent.putExtra("tochat_userId", "3254");
			startActivity(intent);
		}
	}

	private void addSingleOrder(PacketPeriod packet_period_selected ) {

		try {
			if (NetWorkUtils.detect(this)) {
				loading(null);
				HashMap<String, String> params = new HashMap<String, String>();
				
				params.put("token",SharePrefUtil.getString(Conast.ACCESS_TOKEN));
				params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
				params.put("packet_id", entry.getOFFER_PACKET().getPACKET_ID());
				params.put("packet_period_id", packet_period_selected.getPacket_period_id());
				params.put("app_version",PublicUtil.getVersionName(PrivateDoctorActivity.this));
				
				MyApplication.volleyHttpClient.postWithParams(
						HttpUtil.packet_buy, PacketBuyInfo.class, params,
						new Response.Listener() {
							@Override
							public void onResponse(Object response) {
								Message message = new Message();
								message.obj = response;
								message.what = IResult.ADD_SINGLE_ORDER;
								handler.sendMessage(message);
							}
						}, new Response.ErrorListener() {
							@Override
							public void onErrorResponse(VolleyError error) {
								MyApplication.volleyHttpClient.cancelRequest(HttpUtil.packet_buy);
								Message message = new Message();
								message.obj = error.getMessage();
								message.what = IResult.DATA_ERROR;
								handler.sendMessage(message);
							}
						});
			} else {
				handler.sendEmptyMessage(IResult.NET_ERROR);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
	}

	@Override
	protected void onResult(Message msg) {
		super.onResult(msg);
		if (msg.what == IResult.ADD_SINGLE_ORDER) {
			destroyDialog();
			PacketBuyInfo entry = (PacketBuyInfo) msg.obj;
			if (null != entry) {
				if (entry.getSuccess() == 1) {
					showToast("添加签约私人医生服务成功");
					Intent intent = new Intent(this, PayingActivity.class);// 去支付
					intent.putExtra("doctor_name", doctor_name);
					intent.putExtra("title", "签约私人医生服务");
					intent.putExtra("orderId", entry.getData().getORDERID());
					intent.putExtra("price", Double.valueOf(entry.getData().getPRICE()));
					intent.putExtra("ordertype", "15");
					intent.putExtra("dext", content);
					intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
					startActivity(intent);
					finish();
				} else {
					Intent intent = new Intent(this,DoctorDetailInfoActivity.class);
					intent.putExtra("doctor_name", doctor_name);// 医生姓名
					intent.putExtra("tochat_userId", doctorid);
					intent.putExtra("doctor_avatar", doctor_avatar);// 医生头像
					startActivity(intent);
					finish();
					showToast(entry.getErrormsg());
				}
			}
		} else if (msg.what == IResult.NET_ERROR) {

		} else if (msg.what == IResult.DATA_ERROR) {

		}
	}

}
