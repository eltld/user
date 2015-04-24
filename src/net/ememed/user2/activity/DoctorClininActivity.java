package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.baike.SayActivity;
import net.ememed.user2.baike.SayDetailsActivity;
import net.ememed.user2.baike.entity.BaikeFansEntry;
import net.ememed.user2.baike.entity.BaikeSaysInfo;
import net.ememed.user2.entity.CommonResponseEntity;
import net.ememed.user2.entity.DoctorEntry;
import net.ememed.user2.entity.DoctorFansInfo;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MemberInfo;
import net.ememed.user2.entity.MemberInfoEntity;
import net.ememed.user2.entity.OfferServiceEntry;
import net.ememed.user2.entity.OfferServiceEntry2;
import net.ememed.user2.entity.OfferServicePacketEntry;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.entity.PacketPeriod;
import net.ememed.user2.fragment.MineFragment;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import net.ememed.user2.widget.ColoredRatingBar;
import net.ememed.user2.widget.DescribeServiceDialog;
import net.ememed.user2.widget.ListViewForScrollView;
import net.ememed.user2.widget.MenuDialog;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;

/***
 * 医生详情界面
 * 
 * @author pwl
 */
public class DoctorClininActivity extends BasicActivity implements OnClickListener{

	private final int REQUEST_SAYS_DETAIL = 2;
	private final int REQUEST_SAYS_LIST = 3;
	private String doctor_name;
	private String current_service;
	private TextView top_title;
	private TextView tv_doctor_name;
	private DoctorEntry entry;
	private TextView tv_doctor_alias;
	private TextView tv_doctor_quanke;
	private TextView tv_doctor_zhuanke;
	private TextView tv_hos_address;
	private ImageView doctor_head_portrait;
	private TextView tv_search_speciality;
	private ColoredRatingBar rtb_service_goods;
	private LinearLayout ll_special;

	private TextView tv_consult_price;
	private LinearLayout lt_text_consult_below;
	private LinearLayout lt_phone_consult_below;
	private LinearLayout lt_jiuyi_below;
	private LinearLayout lt_shangmen_below;
	private LinearLayout lt_private_doctor_below;
	private LinearLayout lt_zhuyuan_below;
	private LinearLayout lt_custom_below;
	ScrollView doctor_scroll_view;
	private ImageView iv_text_arrows;
	private ImageView iv_phone_arrows;
//	private ImageView iv_shangmen_arrows;
	private ImageView iv_jiuyi_arrows;
	private ImageView iv_zhuyuan_arrows;
	private ImageView iv_private_doctor_arrows;
	private ImageView iv_custom_arrows;
	private List<ImageView> arrowslist = new ArrayList<ImageView>();
	private List<LinearLayout> belowlist = new ArrayList<LinearLayout>();
	private Uri photoUri;
	private static final int REQUEST_DOCTOR_INFO = 100;

	LinearLayout lt_all_text_consult;
	LinearLayout lt_all_phone_consult;
	LinearLayout lt_all_shangmen_consult;
	LinearLayout lt_all_jiuyi_consult;
	LinearLayout lt_all_zhuyuan_consult;
	LinearLayout lt_all_private_doctor_consult;
	LinearLayout lt_all_custom;

//	private String text_offer;
//	private String call_offer;
//	private String shangmen_offer;
//	private String jiahao_offer;
//	private String zhuyuan_offer;
//	private String custom_offer;
//	private String text_offer_price;
//	private String call_offer_price;
//	private String packet_offer;
	
	private MyAdapter adapter;
	private ListViewForScrollView say_list;
	private List<BaikeSaysInfo> shuoshuoList = new ArrayList<BaikeSaysInfo>();
	private int praise_position = -1;
	private TextView tv_attention_num;
	private LinearLayout ll_attention;
	private String doctorId;
	private TextView tv_all_shuoshuo;
	
	private TextView tv_attention; 
	private TextView tv_fans; 
	private TextView tv_noshuoshuo;
	
	
	
	private Button btn_addhealth;//免费微聊
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.doctor_clinic);
		doctor_name = getIntent().getStringExtra("title");
		current_service = getIntent().getStringExtra("current_service");
		entry = (DoctorEntry) getIntent().getSerializableExtra("entry");
		
//		doctorId = entry.getDOCTORID();
//		
//		text_offer = entry.getTEXT_OFFER() + "";
//		call_offer = entry.getCALL_OFFER() + "";
//		shangmen_offer = entry.getSHANGMEN_OFFER() + "";
//		jiahao_offer = entry.getJIAHAO_OFFER() + "";
//		zhuyuan_offer = entry.getZHUYUAN_OFFER() + "";
//		custom_offer = entry.getCUSTOM_OFFER() + "";
//		text_offer_price = entry.getTEXT_OFFER_PRICE() + "";
//		call_offer_price = entry.getCALL_OFFER_PRICE() + "";
//		packet_offer = entry.getPACKET_OFFER();
//
//		System.out.println("call_offer_price = " + text_offer);
	}

	/** 监控箭头的方向 */
	private void setArrowsUp(ImageView iv) {
		for (Iterator<ImageView> iterator = arrowslist.iterator(); iterator.hasNext();) {
			ImageView next = iterator.next();
			next.setImageResource(R.drawable.pic_down);
		}
		iv.setImageResource(R.drawable.pic_up);
	}

	/** 监控底部显示隐藏 */
	private void setBelowVISIBLE(LinearLayout below) {
		for (Iterator<LinearLayout> iterator = belowlist.iterator(); iterator
				.hasNext();) {
			LinearLayout next = iterator.next();
			next.setVisibility(View.GONE);
		}
		below.setVisibility(View.VISIBLE);
	}

	@Override
	protected void setupView() {
		super.setupView();
		top_title = (TextView) findViewById(R.id.top_title);
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundResource(R.color.transparent);
		btn_addhealth.setText("免费微聊");
		top_title.setText(doctor_name);
		rtb_service_goods = (ColoredRatingBar) findViewById(R.id.rtb_service_goods);
		rtb_service_goods.setNumStars(5);
		rtb_service_goods.setRating(5);
		if (!TextUtils.isEmpty(entry.getDOCTOR_DSRS())&& !"0".equals(entry.getDOCTOR_DSRS())) {
			rtb_service_goods.setRating(Integer.valueOf(entry.getDOCTOR_DSRS()) / 2);
		}
		
		tv_attention_num = (TextView) findViewById(R.id.follow_tx);
		ll_attention = (LinearLayout) findViewById(R.id.follow_layout);
		ll_attention.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)){
					Intent i = new Intent(DoctorClininActivity.this, LoginActivity.class);
					i.putExtra("origin", DoctorClininActivity.class.getSimpleName());
					startActivity(i);
				} else {
					if("0".equals(memberInfo.getData().getDOCTOR_FANS().getIS_MY_FANS())){
						setFans();
					} else {
						cancelFans();
					}
				}
			}
		});
		
		tv_all_shuoshuo = (TextView) findViewById(R.id.tv_all_shuoshuo);
		tv_all_shuoshuo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(DoctorClininActivity.this, SayActivity.class);
				intent.putExtra("doctor_id", memberInfo.getData().getMEMBERID());    //XXX 测试   107936/**/
				intent.putExtra("doctor_name", memberInfo.getData().getREALNAME());
				startActivityForResult(intent, REQUEST_SAYS_LIST);
			}
		});

		iv_text_arrows = (ImageView) findViewById(R.id.iv_text_consult_arrows);
		iv_phone_arrows = (ImageView) findViewById(R.id.iv_phone_arrows);
//		iv_shangmen_arrows = (ImageView) findViewById(R.id.iv_shangmen_arrows);
		iv_jiuyi_arrows = (ImageView) findViewById(R.id.iv_jiuyi_arrows);
		iv_zhuyuan_arrows = (ImageView) findViewById(R.id.iv_zhuyuan_arrows);
		iv_private_doctor_arrows = (ImageView) findViewById(R.id.iv_private_doctor_arrows);
		iv_custom_arrows = (ImageView) findViewById(R.id.iv_custom_arrows);
		arrowslist.add(iv_text_arrows);
		arrowslist.add(iv_phone_arrows);
//		arrowslist.add(iv_shangmen_arrows);
		arrowslist.add(iv_jiuyi_arrows);
		arrowslist.add(iv_zhuyuan_arrows);
		arrowslist.add(iv_private_doctor_arrows);
		arrowslist.add(iv_custom_arrows);

		tv_doctor_name = (TextView) findViewById(R.id.tv_doctor_name);
		tv_doctor_alias = (TextView) findViewById(R.id.tv_doctor_alias);
		tv_doctor_quanke = (TextView) findViewById(R.id.tv_doctor_quanke);
		tv_doctor_zhuanke = (TextView) findViewById(R.id.tv_doctor_zhuanke);
		tv_hos_address = (TextView) findViewById(R.id.tv_hos_name);
		doctor_head_portrait = (ImageView) findViewById(R.id.doctor_head_portrait);
		tv_search_speciality = (TextView) findViewById(R.id.tv_search_speciality);
		ll_special = (LinearLayout) findViewById(R.id.ll_special);

		btn_consult = (Button) findViewById(R.id.btn_consult);
		btn_phone_consult = (Button) findViewById(R.id.btn_phone_consult);
		btn_private_doctor = (Button) findViewById(R.id.btn_private_doctor);
		btn_shangmen = (Button) findViewById(R.id.btn_shangmen);
		btn_jiuyi = (Button) findViewById(R.id.btn_jiuyi);
		btn_custom = (Button) findViewById(R.id.btn_custom);
		btn_zhuyuan = (Button) findViewById(R.id.btn_zhuyuan);

		tv_phone_consult_price = (TextView) findViewById(R.id.tv_phone_consult_price);
		tv_private_doctor_price = (TextView) findViewById(R.id.tv_private_doctor_price);

		lt_text_consult_below = (LinearLayout) findViewById(R.id.lt_text_consult_below);
		lt_phone_consult_below = (LinearLayout) findViewById(R.id.lt_phone_consult_below);
		lt_jiuyi_below = (LinearLayout) findViewById(R.id.lt_jiuyi_below);
		lt_shangmen_below = (LinearLayout) findViewById(R.id.lt_shangmen_below);
		lt_private_doctor_below = (LinearLayout) findViewById(R.id.lt_private_doctor_below);
		lt_zhuyuan_below = (LinearLayout) findViewById(R.id.lt_zhuyuan_below);
		lt_custom_below = (LinearLayout) findViewById(R.id.lt_custom_below);
		say_list = (ListViewForScrollView) findViewById(R.id.say_list);
		adapter = new MyAdapter();
		say_list.setAdapter(adapter);
//		setHeight();
		say_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				BaikeSaysInfo info = (BaikeSaysInfo)parent.getAdapter().getItem(position);
				Intent intent = new Intent(DoctorClininActivity.this, SayDetailsActivity.class);
				intent.putExtra("says_id", info.getSAYSID());
				startActivityForResult(intent, REQUEST_SAYS_DETAIL);
			}
		});
		doctor_scroll_view = (ScrollView) findViewById(R.id.doctor_scroll_view);
		lt_all_text_consult = (LinearLayout) findViewById(R.id.lt_all_text_consult);
		lt_all_phone_consult = (LinearLayout) findViewById(R.id.lt_all_phone_consult);
		lt_all_shangmen_consult = (LinearLayout) findViewById(R.id.lt_all_shangmen_consult);
		lt_all_jiuyi_consult = (LinearLayout) findViewById(R.id.lt_all_jiuyi_consult);
		lt_all_zhuyuan_consult = (LinearLayout) findViewById(R.id.lt_all_zhuyuan_consult);
		lt_all_private_doctor_consult = (LinearLayout) findViewById(R.id.lt_all_private_doctor_consult);
		lt_all_custom = (LinearLayout) findViewById(R.id.lt_all_custom);

		lt_all_text_consult.setVisibility(View.GONE);
		lt_all_phone_consult.setVisibility(View.GONE);
		lt_all_shangmen_consult.setVisibility(View.GONE);
		lt_all_jiuyi_consult.setVisibility(View.GONE);
		lt_all_zhuyuan_consult.setVisibility(View.GONE);
		lt_all_private_doctor_consult.setVisibility(View.GONE);
		lt_all_custom.setVisibility(View.GONE);
		
		tv_attention = (TextView) findViewById(R.id.tv_attention);
		tv_fans = (TextView) findViewById(R.id.tv_fans);
		tv_noshuoshuo = (TextView) findViewById(R.id.tv_noshuoshuo);

		if (!TextUtils.isEmpty(entry.getALLOWFREECONSULT())) {
			if (entry.getALLOWFREECONSULT().equals("1")) {
				btn_consult.setText(getString(R.string.consult_service));
				tv_doctor_quanke.setVisibility(View.VISIBLE);
				tv_doctor_zhuanke.setVisibility(View.GONE);
			} else if (entry.getALLOWFREECONSULT().equals("0")) {
				tv_doctor_quanke.setVisibility(View.GONE);
				tv_doctor_zhuanke.setVisibility(View.VISIBLE);
			}
		}

		if (TextUtils.isEmpty(entry.getSPECIALITY())) {
			ll_special.setVisibility(View.GONE);
		} else {
			tv_search_speciality.setText(entry.getSPECIALITY());
		}
		tv_doctor_name.setText(TextUtils.isEmpty(entry.getREALNAME()) ? ""
				: entry.getREALNAME());
		tv_doctor_alias.setText(TextUtils.isEmpty(entry.getPROFESSIONAL()) ? ""
				: entry.getPROFESSIONAL());
		tv_hos_address.setText(TextUtils.isEmpty(entry.getHOSPITALNAME()) ? ""
				: entry.getHOSPITALNAME());
		if (!TextUtils.isEmpty(entry.getAVATAR())) {
			imageLoader.displayImage(entry.getAVATAR(), doctor_head_portrait,
					Util.getOptions_pic());
		}
		tv_consult_price = (TextView) findViewById(R.id.tv_consult_price);

		
		requestMemberInfo();
	}
	
	private void requestMemberInfo() {
		if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)){
			getMemberInfo2();
		}else{
			tv_all_shuoshuo.setVisibility(View.VISIBLE);
			getMemberInfo();
		}
	}

	public void doClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.btn_addhealth:// 免费微聊
			
			
			if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))
					|| !SharePrefUtil.getBoolean(Conast.LOGIN)) {
				Intent intent = new Intent(DoctorClininActivity.this,
						LoginActivity.class);
				intent.putExtra("current_service", "-1");
				intent.putExtra("entry", entry);
				intent.putExtra("origin",
						DoctorClininActivity.class.getSimpleName());
				startActivity(intent);
			}else{
				Intent microIntent = new Intent(this, MicroChatActivity.class);
				microIntent.putExtra("tochat_userId", memberInfo.getData().getMEMBERID());
				microIntent.putExtra("doctor_avatar", memberInfo.getData().getAVATAR());
				microIntent.putExtra("doctorName", memberInfo.getData().getREALNAME());
				microIntent.putExtra("memberInfo", memberInfo);
				startActivity(microIntent);
			}
			
			
			break;
		case R.id.doctor_details://
			Intent intent_details = new Intent(this, DoctorInfoActivity.class);
			if (null != entry) {
				Bundle bundle = new Bundle();
				bundle.putSerializable("entry", entry);
				intent_details.putExtras(bundle);
				intent_details.putExtra("doctor_name", doctor_name);
				if (doctorServiceEntry != null) 
					intent_details.putExtra("hasService", doctorServiceEntry.hasAnyService());
				startActivityForResult(intent_details, REQUEST_DOCTOR_INFO);
			}
			break;
		case R.id.lt_text_consult:// 图文资讯
			if (lt_text_consult_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_text_consult_below);
				setArrowsUp(iv_text_arrows);
			} else {
				lt_text_consult_below.setVisibility(View.GONE);
				iv_text_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_phone_consult:// 电话资讯
			if (lt_phone_consult_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_phone_consult_below);
				setArrowsUp(iv_phone_arrows);
			} else {
				lt_phone_consult_below.setVisibility(View.GONE);
				iv_phone_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
//		case R.id.lt_shangmen:// 上门会诊
//			if (lt_shangmen_below.getVisibility() == View.GONE) {
//				setBelowVISIBLE(lt_shangmen_below);
//				setArrowsUp(iv_shangmen_arrows);
//			} else {
//				lt_shangmen_below.setVisibility(View.GONE);
//				iv_shangmen_arrows.setImageResource(R.drawable.pic_down);
//			}
//			break;
		case R.id.lt_jiuyi:// 预约加号
			if (lt_jiuyi_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_jiuyi_below);
				setArrowsUp(iv_jiuyi_arrows);
			} else {
				lt_jiuyi_below.setVisibility(View.GONE);
				iv_jiuyi_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_zhuyuan:// 住院直通车
			if (lt_zhuyuan_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_zhuyuan_below);
				setArrowsUp(iv_zhuyuan_arrows);
			} else {
				lt_zhuyuan_below.setVisibility(View.GONE);
				iv_zhuyuan_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_private_doctor:// 签约私人医生
			if (lt_private_doctor_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_private_doctor_below);
				setArrowsUp(iv_private_doctor_arrows);
			} else {
				lt_private_doctor_below.setVisibility(View.GONE);
				iv_private_doctor_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_custom:// 其他服务

			if (lt_custom_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_custom_below);
				setArrowsUp(iv_custom_arrows);
				handler.post(new Runnable() {
					@Override
					public void run() {
						doctor_scroll_view.fullScroll(ScrollView.FOCUS_DOWN);
					}
				});
			} else {
				lt_custom_below.setVisibility(View.GONE);
				iv_custom_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.btn_phone_consult:
		case R.id.btn_shangmen:
		case R.id.btn_zhuyuan:
		case R.id.btn_jiuyi:
		case R.id.btn_consult:
		case R.id.btn_private_doctor:
		case R.id.btn_custom:
			if (NetWorkUtils.detect(this)) {
				// if (null == EMChatManager.getInstance() ||
				// !EMChatManager.getInstance().isConnected()) {
				// showToast(getString(R.string.connect_chat_failed));
				// } else {
				go2BuyServiceAct(id);
				// }
			} else {
				handler.sendEmptyMessage(IResult.NET_ERROR);
			}
			break;
		case R.id.btn_phone_consult_desc:
		case R.id.btn_shangmen_consult_desc:
		case R.id.btn_zhuyuan_desc:
		case R.id.btn_jiuyi_desc:
		case R.id.btn_consult_desc:
		case R.id.btn_private_doctor_desc:
		case R.id.btn_custom_desc:
			goDescribe(id);
			break;
		case R.id.contact_image:
			// Intent intent = new Intent(this,DcotorInfoActivity.class);
			// intent.putExtra("doctor_name", doctor_name);
			// intent.putExtra("entry", entry);
			// startActivity(intent);

			Intent intent = new Intent(this, ImageActivity.class);
			intent.putExtra("avatar", entry.getAVATAR());
			startActivity(intent);
			break;
		default:
			break;
		}

	}
	int bntid;
	DescribeServiceDialog serviceDialog;
	private void goDescribe(int id) {
		
		String service_buy = null;
		int service_type = 0;
		
		switch (id) {
		case R.id.btn_consult_desc:
			service_type = 1;
			service_buy = btn_consult.getText().toString();
			bntid = R.id.btn_consult;
			break;
		case R.id.btn_phone_consult_desc:
			service_type = 2;
			service_buy = btn_phone_consult.getText().toString();
			bntid = R.id.btn_phone_consult;
			break;
		case R.id.btn_jiuyi_desc:
			service_type = 3;
			service_buy = btn_jiuyi.getText().toString();
			bntid = R.id.btn_jiuyi;
			break;
		case R.id.btn_shangmen_consult_desc:
			service_type = 4;
			service_buy = btn_shangmen.getText().toString();
			bntid = R.id.btn_shangmen;
			break;
		case R.id.btn_zhuyuan_desc:
			service_type = 14;
			service_buy = btn_zhuyuan.getText().toString();
			bntid = R.id.btn_zhuyuan;
			break;
		case R.id.btn_private_doctor_desc:
			service_type = 15;
			service_buy = btn_private_doctor.getText().toString();
			bntid = R.id.btn_private_doctor;
			break;
		case R.id.btn_custom_desc:
			service_type = 16;
			service_buy = btn_consult.getText().toString();
			bntid = R.id.btn_consult;
			break;

		default:
			break;
		}
		
		serviceDialog = new DescribeServiceDialog(this, R.style.wheelDialog);
		serviceDialog.show();
		serviceDialog.setShowText(service_type, service_buy, this);
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_DOCTOR_INFO) {
			switch (resultCode) {
			case DoctorInfoActivity.RESULT_FINISH:
				finish();
				break;

			default:
				break;
			}
		} else if(requestCode == REQUEST_SAYS_DETAIL || requestCode == REQUEST_SAYS_LIST){
			requestMemberInfo();
	    } 
		
	}

	/** 用于处理用户是否登录或者个人资料是否完善 */
	private void go2BuyServiceAct(final int bt_id) {
		if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID))
				|| !SharePrefUtil.getBoolean(Conast.LOGIN)) {// 没登录
			Intent intent = new Intent(DoctorClininActivity.this,
					LoginActivity.class);
			intent.putExtra("origin",
					DoctorClininActivity.class.getSimpleName());
			DoctorServiceEntry doctorServiceEntry = null;
			if (bt_id == R.id.btn_consult) {
				current_service = "1";
				doctorServiceEntry = new DoctorServiceEntry();
				OfferServiceEntry2 entry2 = new OfferServiceEntry2();
				entry2.setOFFER_PRICE(entry.getTEXT_OFFER_PRICE());
				doctorServiceEntry.setOFFER_TEXT(entry2);
			} else if (bt_id == R.id.btn_phone_consult) {
				current_service = "2";

				doctorServiceEntry = new DoctorServiceEntry();
				OfferServiceEntry entry2 = new OfferServiceEntry();
				entry2.setOFFER_PRICE(Float.parseFloat(entry
						.getCALL_OFFER_PRICE()));
				doctorServiceEntry.setOFFER_CALL(entry2);
			} else if (bt_id == R.id.btn_jiuyi) {
				current_service = "3";
			} else if (bt_id == R.id.btn_shangmen) {
				current_service = "4";
			} else if (bt_id == R.id.btn_zhuyuan) {
				current_service = "14";
			} else if (bt_id == R.id.btn_private_doctor) {
				current_service = "15";
				doctorServiceEntry = new DoctorServiceEntry();
				OfferServicePacketEntry packetEntry = new OfferServicePacketEntry();
				packetEntry.setPACKET_ID(entry.getPACKET_ID());
				packetEntry.setPACKET_PERIOD_LIST(entry.getPERIOD_LIST());
				doctorServiceEntry.setOFFER_PACKET(packetEntry);

			} else if (bt_id == R.id.btn_custom) {
				current_service = "16";
			}
			intent.putExtra("current_service", current_service);
			entry.setOFFER_SERVICE(doctorServiceEntry);
			intent.putExtra("entry", entry);
			startActivity(intent);
		} else if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName))
				|| TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_SEX))
				|| TextUtils.isEmpty(SharePrefUtil
						.getString(Conast.USER_BIRTHDAY))
				|| TextUtils.isEmpty(SharePrefUtil
						.getString(Conast.USER_MOBILE))) {
			// 资料没完善
			MenuDialog.Builder alert = new MenuDialog.Builder(
					DoctorClininActivity.this);
			MenuDialog dialog = alert
					.setTitle(R.string.system_info)
					.setMessage(R.string.you_should_improve_data)
					.setPositiveButton(getString(R.string.confirm_yes),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
									Intent intent = new Intent(
											DoctorClininActivity.this,
											PersonInfoActivity.class);
									intent.putExtra("origin",
											DoctorClininActivity.class
													.getSimpleName());
									if (bt_id == R.id.btn_consult) {
										current_service = "1";
									} else if (bt_id == R.id.btn_private_doctor) {
										current_service = "15";
									} else if (bt_id == R.id.btn_jiuyi) {
										current_service = "3";
									} else if (bt_id == R.id.btn_zhuyuan) {
										current_service = "14";
									} else if (bt_id == R.id.btn_shangmen) {
										current_service = "4";
									} else if (bt_id == R.id.btn_phone_consult) {
										current_service = "2";
									} else if (bt_id == R.id.btn_custom) {
										current_service = "16";
									}
									intent.putExtra("current_service",
											current_service);
									intent.putExtra("entry", entry);
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
		} else {// 跳转到 聊天、订单、简介 容器 界面
			Intent intent = null;
			if (bt_id == R.id.btn_consult) {
				if (isOpenPrivateDoctor) {
					intent = new Intent(this, DoctorDetailInfoActivity.class);
					intent.putExtra("doctor_name", doctor_name);
					intent.putExtra("doctor_avatar", entry.getAVATAR());
					intent.putExtra("tochat_userId", entry.getDOCTORID());
				} else {
					intent = new Intent(this, BuyTextConsultActivity.class);
					DoctorServiceEntry doctorServiceEntry = new DoctorServiceEntry();
					OfferServiceEntry2 entry2 = new OfferServiceEntry2();
					entry2.setOFFER_PRICE(entry.getTEXT_OFFER_PRICE());
					doctorServiceEntry.setOFFER_TEXT(entry2);
					intent.putExtra("doctorServiceEntry", doctorServiceEntry);
				}
			} else if (bt_id == R.id.btn_private_doctor) {
				if (isOpenPrivateDoctor) {
					intent = new Intent(this, JiuyiConsultDetailsActivity.class);
					intent.putExtra("ORDERID", PrivateDoctor_Order);
					intent.putExtra("source", 1);
				} else {
					intent = new Intent(this, PrivateDoctorActivity.class);
					DoctorServiceEntry doctorServiceEntry = new DoctorServiceEntry();
					OfferServicePacketEntry packetEntry = new OfferServicePacketEntry();
					packetEntry.setPACKET_ID(entry.getPACKET_ID());
					packetEntry.setPACKET_PERIOD_LIST(entry.getPERIOD_LIST());
					doctorServiceEntry.setOFFER_PACKET(packetEntry);
					intent.putExtra("doctorServiceEntry", doctorServiceEntry);

				}
			} else if (bt_id == R.id.btn_jiuyi) {
				intent = new Intent(this, BuyJiuyiActivity.class);
			} else if (bt_id == R.id.btn_zhuyuan) {
				intent = new Intent(this, BuyZhuyuanActivity.class);
			} else if (bt_id == R.id.btn_shangmen) {
				intent = new Intent(this, BuyShangmenActivity.class);
			} else if (bt_id == R.id.btn_phone_consult) {
				if (isOpenPrivateDoctor) {
					goChat();
					return;
				} else {
					intent = new Intent(this, BuyPhoneConsultActivity.class);
					DoctorServiceEntry doctorServiceEntry = new DoctorServiceEntry();
					OfferServiceEntry entry2 = new OfferServiceEntry();
					entry2.setOFFER_PRICE(Float.parseFloat(entry
							.getCALL_OFFER_PRICE()));
					doctorServiceEntry.setOFFER_CALL(entry2);
					intent.putExtra("doctorServiceEntry", doctorServiceEntry);
				}
			} else if (bt_id == R.id.btn_custom) {
				intent = new Intent(this, BuyCustomActivity.class);
			}
			intent.putExtra("doctorid", entry.getDOCTORID());
			intent.putExtra("doctor_name", doctor_name);
			startActivity(intent);
		}
	}

	public void goChat() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("系统提示").setMessage("请与医生沟通通话时间，并提醒医生在订单页设置通话时间")
				.setPositiveButton("取消", null)
				.setNegativeButton("确定", new AlertDialog.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(DoctorClininActivity.this,
								DoctorDetailInfoActivity.class);
						intent.putExtra("doctor_name", doctor_name);
						intent.putExtra("doctor_avatar", entry.getAVATAR());
						intent.putExtra("tochat_userId", entry.getDOCTORID());
						startActivity(intent);
					}
				}).show();
	}

	// 处理签约私人医生是否购买 ->如果购买图文资讯 预约通话直接调转聊天界面
	private boolean isOpenPrivateDoctor = false;
	private String PrivateDoctor_Order;
	private Button btn_consult;
	private Button btn_phone_consult;
	private Button btn_private_doctor;
	private TextView tv_phone_consult_price;
	private TextView tv_private_doctor_price;
	private MemberInfo memberInfo;
	private Button btn_shangmen;
	private Button btn_jiuyi;
	private Button btn_custom;
	private Button btn_zhuyuan;
	private DoctorServiceEntry doctorServiceEntry;

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		switch (msg.what) {
		case IResult.MEMBER_INFO:
			memberInfo = (MemberInfo) msg.obj;
			if (memberInfo != null && memberInfo.getData() != null) {
				
				//从该界面进入其他界面时传入了entry参数，而从医生的粉丝，关注列表进入该页面时传入的entry参数不全，次数需不全，方能不动其他地方的代码，权宜之计！
				dealEntry();
				
				
				doctorId = memberInfo.getData().getMEMBERID();
				doctorServiceEntry = memberInfo.getData().getOFFER_SERVICE();
				List<OrderListEntry> open_ORDER = memberInfo.getData().getOPEN_ORDER();
				if (open_ORDER != null && open_ORDER.size() > 0) {
					for (Iterator iterator = open_ORDER.iterator(); iterator.hasNext();) {
						OrderListEntry orderListEntry = (OrderListEntry) iterator.next();
						if ("15".equals(orderListEntry.getORDERTYPE())) {
							isOpenPrivateDoctor = true;
							PrivateDoctor_Order = orderListEntry.getORDERID();
							break;
						}

					}
				}
				if (isOpenPrivateDoctor) {
					btn_consult.setText("咨询");
					btn_phone_consult.setText("咨询");
					btn_private_doctor.setText("查看");
					tv_phone_consult_price.setVisibility(View.GONE);
					tv_consult_price.setVisibility(View.GONE);
					tv_private_doctor_price.setVisibility(View.GONE);
				}
				
				
				getIntData();
				
//				if(!(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN))){
					List<BaikeSaysInfo> tempList = memberInfo.getData().getSAYS_LIST();
					if(tempList != null && tempList.size() > 0){
						tv_noshuoshuo.setVisibility(View.GONE);
						shuoshuoList.clear();
						shuoshuoList.addAll(tempList);
						adapter.notifyDataSetChanged();
					} else {
						tv_noshuoshuo.setVisibility(View.VISIBLE);
					}
//				}
			}
			break;
		case IResult.BAIKE_ADD_ATTENTION:
			destroyDialog();
			BaikeFansEntry entry = (BaikeFansEntry) msg.obj;
			if(1 == entry.getSuccess()){
				showToast("添加关注成功");
				memberInfo.getData().getDOCTOR_FANS().setIS_MY_FANS("1");
				memberInfo.getData().getDOCTOR_FANS().setFANS_NUM(entry.getData().getDoctor_fans_num());
				tv_attention_num.setText("已关注");
				tv_fans.setText("粉丝：" + memberInfo.getData().getDOCTOR_FANS().getFANS_NUM());
			} else {
				showToast("添加关注失败，请稍后再试");
			}
			break;
		case IResult.BAIKE_CANCEL_ATTENTION:
			destroyDialog();
			BaikeFansEntry entry2 = (BaikeFansEntry) msg.obj;
			if(1 == entry2.getSuccess()){
				showToast("取消关注成功");
				memberInfo.getData().getDOCTOR_FANS().setIS_MY_FANS("0");
				memberInfo.getData().getDOCTOR_FANS().setFANS_NUM(entry2.getData().getDoctor_fans_num());
				tv_attention_num.setText("关注");
				tv_fans.setText("粉丝：" + memberInfo.getData().getDOCTOR_FANS().getFANS_NUM());
			} else {
				showToast("取消关注失败，请稍后再试");
			}
			break;
		case IResult.GIVE_PRAISE:
			destroyDialog();
			CommonResponseEntity response2 = (CommonResponseEntity)msg.obj;
			if (null != response2) {
				if (1 == response2.getSuccess()) {
					UserPreferenceWrapper.addMyPraiseCount();
					if(praise_position >= 0){
						int num = Integer.parseInt(shuoshuoList.get(praise_position).getPRAISE_NUM());
						shuoshuoList.get(praise_position).setPRAISE_NUM((num+1)+"");
						shuoshuoList.get(praise_position).setIS_PRAISED(true);
						adapter.notifyDataSetChanged();
					}
				} else {
					showToast(response2.getErrormsg());
				}
			}
			praise_position = -1;
			break;
		case IResult.DATA_ERROR:
			showToast(IMessage.DATA_ERROR);
			break;
		case IResult.NET_ERROR:
			showToast(IMessage.NET_ERROR);
			break;

		default:
			break;
		}
	}

	private void dealEntry() {
		MemberInfoEntity temp = memberInfo.getData();
		entry.setADDRESS(temp.getADDRESS());
		entry.setALLOWFREECONSULT(temp.getALLOWFREECONSULT());
		entry.setAUDITSTATUS(temp.getAUDITSTATUS());
		entry.setAVATAR(temp.getAVATAR());
//				entry.setCALL_OFFER(temp.getCA);
//				entry.setCALL_OFFER_PRICE(cALL_OFFER_PRICE);
//				entry.setCUSTOM_OFFER(tem);
		entry.setDETAILINFO(temp.getDETAILINFO());
		entry.setDOCTOR_DSRS(temp.getDOCTOR_DSRS());
		entry.setDOCTORID(temp.getMEMBERID());
		entry.setFans_doctor_total_num(temp.getFans_doctor_total_num());
		entry.setFans_total_num(temp.getFans_total_num());
		entry.setFans_user_total_num(temp.getFans_user_total_num());
		entry.setHOSPITAL_INFO(temp.getHOSPITAL_INFO());
		entry.setHOSPITALNAME(temp.getHOSPITALNAME());
//				entry.setJIAHAO_OFFER(jIAHAO_OFFER);
//				entry.setJIAHAO_OFFER_PRICE(jIAHAO_OFFER_PRICE);
		entry.setMOBILE(temp.getMOBILE());
		entry.setOFFER_SERVICE(temp.getOFFER_SERVICE());
//				entry.setPACKET_ID(temp.getP);
//				entry.setPACKET_OFFER(pACKET_OFFER);
//				entry.setPERCENT(temp.getP);
//				entry.setPERIOD_LIST(temp.getP);
		entry.setPROFESSIONAL(temp.getPROFESSIONAL());
		entry.setREALNAME(temp.getREALNAME());
		entry.setROOMNAME(temp.getROOMNAME());
//				entry.setSERVICE_CONTENT(temp.getS);
//				entry.setSEXNAME(temp.getS);
//				entry.setSHANGMEN_OFFER(sHANGMEN_OFFER);
		entry.setSPECIALITY(temp.getSPECIALITY());
//				entry.setTEXT_OFFER(tEXT_OFFER);
//				entry.setTEXT_OFFER_PRICE(tEXT_OFFER_PRICE);
//				entry.setUPDATETIME(temp.getU);
//				entry.setZHUYUAN_OFFER(zHUYUAN_OFFER);
//				entry.setZHUYUAN_OFFER_PRICE(zHUYUAN_OFFER_PRICE);
		entry.setDOCTOR_FANS(temp.getDOCTOR_FANS());
	}

//	private void getIntDataUnlogin() {
//		TextView tv_phone_consult_price = (TextView) findViewById(R.id.tv_phone_consult_price);
//		TextView tv_shangmen_price = (TextView) findViewById(R.id.tv_shangmen_price);
//		TextView tv_jiuyi_price = (TextView) findViewById(R.id.tv_jiuyi_price);
//		TextView tv_zhuyuan_price = (TextView) findViewById(R.id.tv_zhuyuan_price);
//		TextView tv_private_doctor_price = (TextView) findViewById(R.id.tv_private_doctor_price);
//
//		if (text_offer.equals("1")) {
//			lt_all_text_consult.setVisibility(View.VISIBLE);
//			tv_consult_price.setText(Util.PriceFormat(Util
//					.DecZore(text_offer_price)));
//		} else {
//			lt_all_text_consult.setVisibility(View.GONE);
//		}
//
//		if (call_offer.equals("1")) {
//			lt_all_phone_consult.setVisibility(View.VISIBLE);
//			tv_phone_consult_price.setText(Util.PriceFormat(Util
//					.DecZore(call_offer_price)));
//		} else {
//			lt_all_phone_consult.setVisibility(View.GONE);
//		}
//
//		if (shangmen_offer.equals("1")) {
//			lt_all_shangmen_consult.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_shangmen_consult.setVisibility(View.GONE);
//		}
//
//		if (jiahao_offer.equals("1")) {
//			lt_all_jiuyi_consult.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_jiuyi_consult.setVisibility(View.GONE);
//		}
//
//		if (zhuyuan_offer.equals("1")) {
//			lt_all_zhuyuan_consult.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_zhuyuan_consult.setVisibility(View.GONE);
//		}
//
//		if (custom_offer.equals("1")) {
//			lt_all_custom.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_custom.setVisibility(View.GONE);
//		}
//
//		if (null != packet_offer && "1".equals(packet_offer)) {
//			lt_all_private_doctor_consult.setVisibility(View.VISIBLE);
//			if (entry.getPERIOD_LIST() != null&&entry.getPERIOD_LIST().size()>0) {
//				tv_private_doctor_price.setText(Util.PriceFormat(Util
//						.DecZore(entry.getPERIOD_LIST().get(0)
//								.getPacket_period_price())));
//			}
//		} else {
//			lt_all_private_doctor_consult.setVisibility(View.GONE);
//		}
//
//		belowlist.add(lt_text_consult_below);
//		belowlist.add(lt_phone_consult_below);
//		belowlist.add(lt_jiuyi_below);
//		belowlist.add(lt_shangmen_below);
//		belowlist.add(lt_private_doctor_below);
//		belowlist.add(lt_zhuyuan_below);
//		belowlist.add(lt_custom_below);
//
//		if (current_service.equals("1")) {
//			setBelowVISIBLE(lt_text_consult_below);
//			setArrowsUp(iv_text_arrows);
//		} else if (current_service.equals("2")) {
//			setBelowVISIBLE(lt_phone_consult_below);
//			setArrowsUp(iv_phone_arrows);
//		} else if (current_service.equals("3")) {
//			setBelowVISIBLE(lt_jiuyi_below);
//			setArrowsUp(iv_jiuyi_arrows);
//		} else if (current_service.equals("4")) {
//			setBelowVISIBLE(lt_shangmen_below);
//			setArrowsUp(iv_shangmen_arrows);
//		} else if (current_service.equals("14")) {
//			setBelowVISIBLE(lt_zhuyuan_below);
//			setArrowsUp(iv_zhuyuan_arrows);
//		} else if (current_service.equals("15")) {
//			setBelowVISIBLE(lt_private_doctor_below);
//			setArrowsUp(iv_private_doctor_arrows);
//		} else if (current_service.equals("16")) {
//			setBelowVISIBLE(lt_custom_below);
//			setArrowsUp(iv_custom_arrows);
//		}
//
//		tv_shangmen_price.setVisibility(View.GONE);
//		tv_jiuyi_price.setVisibility(View.GONE);
//		tv_zhuyuan_price.setVisibility(View.GONE);
//	}
//	private void getIntDataUnlogin() {
//		TextView tv_phone_consult_price = (TextView) findViewById(R.id.tv_phone_consult_price);
////		TextView tv_shangmen_price = (TextView) findViewById(R.id.tv_shangmen_price);
//		TextView tv_jiuyi_price = (TextView) findViewById(R.id.tv_jiuyi_price);
//		TextView tv_zhuyuan_price = (TextView) findViewById(R.id.tv_zhuyuan_price);
//		TextView tv_private_doctor_price = (TextView) findViewById(R.id.tv_private_doctor_price);
//
//		if (text_offer.equals("1")) {
//			lt_all_text_consult.setVisibility(View.VISIBLE);
//			tv_consult_price.setText(Util.PriceFormat(Util
//					.DecZore(text_offer_price)));
//		} else {
//			lt_all_text_consult.setVisibility(View.GONE);
//		}
//
//		if (call_offer.equals("1")) {
//			lt_all_phone_consult.setVisibility(View.VISIBLE);
//			tv_phone_consult_price.setText(Util.PriceFormat(Util
//					.DecZore(call_offer_price)));
//		} else {
//			lt_all_phone_consult.setVisibility(View.GONE);
//		}
//
//		if (shangmen_offer.equals("1")) {
//			lt_all_shangmen_consult.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_shangmen_consult.setVisibility(View.GONE);
//		}
//
//		if (jiahao_offer.equals("1")) {
//			lt_all_jiuyi_consult.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_jiuyi_consult.setVisibility(View.GONE);
//		}
//
//		if (zhuyuan_offer.equals("1")) {
//			lt_all_zhuyuan_consult.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_zhuyuan_consult.setVisibility(View.GONE);
//		}
//
//		if (custom_offer.equals("1")) {
//			lt_all_custom.setVisibility(View.VISIBLE);
//		} else {
//			lt_all_custom.setVisibility(View.GONE);
//		}
//
//		if (null != packet_offer && "1".equals(packet_offer)) {
//			lt_all_private_doctor_consult.setVisibility(View.VISIBLE);
//			if (entry.getPERIOD_LIST() != null&&entry.getPERIOD_LIST().size()>0) {
//				tv_private_doctor_price.setText(Util.PriceFormat(Util
//						.DecZore(entry.getPERIOD_LIST().get(0)
//								.getPacket_period_price())));
//			}
//		} else {
//			lt_all_private_doctor_consult.setVisibility(View.GONE);
//		}
//
//		belowlist.add(lt_text_consult_below);
//		belowlist.add(lt_phone_consult_below);
//		belowlist.add(lt_jiuyi_below);
//		belowlist.add(lt_shangmen_below);
//		belowlist.add(lt_private_doctor_below);
//		belowlist.add(lt_zhuyuan_below);
//		belowlist.add(lt_custom_below);
//
//		if (current_service.equals("1")) {
//			setBelowVISIBLE(lt_text_consult_below);
//			setArrowsUp(iv_text_arrows);
//		} else if (current_service.equals("2")) {
//			setBelowVISIBLE(lt_phone_consult_below);
//			setArrowsUp(iv_phone_arrows);
//		} else if (current_service.equals("3")) {
//			setBelowVISIBLE(lt_jiuyi_below);
//			setArrowsUp(iv_jiuyi_arrows);
//		} 
////		else if (current_service.equals("4")) {
////			setBelowVISIBLE(lt_shangmen_below);
////			setArrowsUp(iv_shangmen_arrows);
////		}
//		else if (current_service.equals("14")) {
//			setBelowVISIBLE(lt_zhuyuan_below);
//			setArrowsUp(iv_zhuyuan_arrows);
//		} else if (current_service.equals("15")) {
//			setBelowVISIBLE(lt_private_doctor_below);
//			setArrowsUp(iv_private_doctor_arrows);
//		} else if (current_service.equals("16")) {
//			setBelowVISIBLE(lt_custom_below);
//			setArrowsUp(iv_custom_arrows);
//		}
//
////		tv_shangmen_price.setVisibility(View.GONE);
//		tv_jiuyi_price.setVisibility(View.GONE);
//		tv_zhuyuan_price.setVisibility(View.GONE);
//	}
	
	
	private void getIntData() {
		MemberInfoEntity memberInfoEntry = memberInfo.getData();
		
		TextView tv_phone_consult_price = (TextView) findViewById(R.id.tv_phone_consult_price);
//		TextView tv_shangmen_price = (TextView) findViewById(R.id.tv_shangmen_price);
		TextView tv_jiuyi_price = (TextView) findViewById(R.id.tv_jiuyi_price);
		TextView tv_zhuyuan_price = (TextView) findViewById(R.id.tv_zhuyuan_price);
		TextView tv_private_doctor_price = (TextView) findViewById(R.id.tv_private_doctor_price);

		if (null != doctorServiceEntry) {

			if (null != doctorServiceEntry.getOFFER_TEXT() && doctorServiceEntry.getOFFER_TEXT().getIS_OFFER() == 1) {
				lt_all_text_consult.setVisibility(View.VISIBLE);
				tv_consult_price.setText(Util.PriceFormat(Util.DecZore(Float.valueOf(doctorServiceEntry.getOFFER_TEXT().getOFFER_PRICE()))));
				
			} else {
				lt_all_text_consult.setVisibility(View.GONE);
			}

			if (null != doctorServiceEntry.getOFFER_CALL() && doctorServiceEntry.getOFFER_CALL().getIS_OFFER() == 1) {
				lt_all_phone_consult.setVisibility(View.VISIBLE);
				tv_phone_consult_price.setText(Util.PriceFormat(Util.DecZore(doctorServiceEntry.getOFFER_CALL().getOFFER_PRICE())));
				
			} else {
				lt_all_phone_consult.setVisibility(View.GONE);
			}

			if (null != doctorServiceEntry.getOFFER_SHANGMEN() && doctorServiceEntry.getOFFER_SHANGMEN().getIS_OFFER() == 1) {
//				lt_all_shangmen_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_shangmen_consult.setVisibility(View.GONE);
			}

			if (null != doctorServiceEntry.getOFFER_JIAHAO() && doctorServiceEntry.getOFFER_JIAHAO().getIS_OFFER() == 1) {
				lt_all_jiuyi_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_jiuyi_consult.setVisibility(View.GONE);
			}
			if (null != doctorServiceEntry.getOFFER_ZHUYUAN() && doctorServiceEntry.getOFFER_ZHUYUAN().getIS_OFFER() == 1) {
				lt_all_zhuyuan_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_zhuyuan_consult.setVisibility(View.GONE);
			}

			if (null != doctorServiceEntry.getOFFER_PACKET() && "1".equals(doctorServiceEntry.getOFFER_PACKET().getIS_OFFER())) {
				lt_all_private_doctor_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_private_doctor_consult.setVisibility(View.GONE);
			}
			if (null != doctorServiceEntry.getOFFER_PACKET() && doctorServiceEntry.getOFFER_PACKET().getPACKET_PERIOD_LIST() != null && doctorServiceEntry.getOFFER_PACKET().getPACKET_PERIOD_LIST().size() > 0) {
				// lt_all_private_doctor_consult.setVisibility(View.VISIBLE);
				List<PacketPeriod> packet_PERIOD_LIST = doctorServiceEntry.getOFFER_PACKET().getPACKET_PERIOD_LIST();
				if (packet_PERIOD_LIST.size() == 1) {
					tv_private_doctor_price.setText(Util.PriceFormat(Util.DecZore(packet_PERIOD_LIST.get(0).getPacket_period_price())));
				} else if (packet_PERIOD_LIST.size() > 1) {
					List<Double> list = new ArrayList<Double>();
					for (Iterator iterator = packet_PERIOD_LIST.iterator(); iterator.hasNext();) {
						PacketPeriod packetPeriod = (PacketPeriod) iterator.next();
						String packet_period_price = packetPeriod.getPacket_period_price();
						if (!TextUtils.isEmpty(packet_period_price)) {
							try {
								if (!list.contains(Double.parseDouble(packet_period_price)))
									list.add(Double.parseDouble(packet_period_price));
							} catch (NumberFormatException e) {
							}
						}
					}
					if (list.size() == 1) {
						tv_private_doctor_price.setText(Util.PriceFormat(Util.DecZore(list.get(0))));
					} else if (list.size() > 1) {
						Collections.sort(list);
						tv_private_doctor_price.setText("￥" + Util.DecZore(list.get(0)) + "-" + Util.DecZore(list.get(list.size() - 1)));
					} else {
						tv_private_doctor_price.setVisibility(View.GONE);
						// lt_all_private_doctor_consult.setVisibility(View.GONE);
					}
				}

			} else {
				lt_all_private_doctor_consult.setVisibility(View.GONE);
			}
		} else {
			lt_all_text_consult.setVisibility(View.GONE);
			lt_all_phone_consult.setVisibility(View.GONE);
			lt_all_shangmen_consult.setVisibility(View.GONE);
			lt_all_jiuyi_consult.setVisibility(View.GONE);
			lt_all_zhuyuan_consult.setVisibility(View.GONE);
			lt_all_private_doctor_consult.setVisibility(View.GONE);
		}
		
		belowlist.add(lt_text_consult_below);
		belowlist.add(lt_phone_consult_below);
		belowlist.add(lt_jiuyi_below);
		belowlist.add(lt_shangmen_below);
		belowlist.add(lt_private_doctor_below);
		belowlist.add(lt_zhuyuan_below);
		belowlist.add(lt_custom_below);

		if (current_service.equals("1")) {
			setBelowVISIBLE(lt_text_consult_below);
			setArrowsUp(iv_text_arrows);
		} else if (current_service.equals("2")) {
			setBelowVISIBLE(lt_phone_consult_below);
			setArrowsUp(iv_phone_arrows);
		} else if (current_service.equals("3")) {
			setBelowVISIBLE(lt_jiuyi_below);
			setArrowsUp(iv_jiuyi_arrows);
		} 
//		else if (current_service.equals("4")) {
//			setBelowVISIBLE(lt_shangmen_below);
//			setArrowsUp(iv_shangmen_arrows);
//		}
		else if (current_service.equals("5")) {
			setBelowVISIBLE(lt_zhuyuan_below);
			setArrowsUp(iv_zhuyuan_arrows);
		} else if (current_service.equals("15")) {
			setBelowVISIBLE(lt_private_doctor_below);
			setArrowsUp(iv_private_doctor_arrows);
		} else if (current_service.equals("16")) {
			setBelowVISIBLE(lt_custom_below);
			setArrowsUp(iv_custom_arrows);
		}

//		tv_shangmen_price.setVisibility(View.GONE);
		tv_jiuyi_price.setVisibility(View.GONE);
		tv_zhuyuan_price.setVisibility(View.GONE);
		
		//已关注图标
		if(null != memberInfo.getData().getDOCTOR_FANS()){
			DoctorFansInfo info = memberInfo.getData().getDOCTOR_FANS();
			if("1".equals(info.getIS_MY_FANS())){
				tv_attention_num.setText("已关注");
			} else {
				tv_attention_num.setText("关注");
			}
			
			tv_attention.setText("关注：" + info.getAttention_total_num());
			tv_fans.setText("粉丝：" + info.getFANS_NUM());
		}
		
		//认证图标
		CheckBox follow = (CheckBox) findViewById(R.id.follow);
		if(!TextUtils.isEmpty(memberInfo.getData().getAUDITSTATUS()) && "1".equals(memberInfo.getData().getAUDITSTATUS())){
			follow.setChecked(true);
		} else {
			follow.setChecked(false);
		}
	}
	

	/**已登录   获取用户资料和open订单 */
	public void getMemberInfo() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			/*
			 * token token加密码 userid 用户ID any_user_id 目标对象ID
			 */
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("any_user_id", entry.getDOCTORID()); 
			
			System.out.println("get_member_info 医生信息参数= "+params);
			
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_member_info, MemberInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.MEMBER_INFO;
							handler.sendMessage(message);

							MemberInfo memberInfo = (MemberInfo) response;
							if (memberInfo != null)
								EventBus.getDefault().postSticky(memberInfo);
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
	
	/**未登录  获取用户资料和open订单 */
	public void getMemberInfo2() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("any_user_id", entry.getDOCTORID()); 
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_member_info2, MemberInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.MEMBER_INFO;
							handler.sendMessage(message);

							MemberInfo memberInfo = (MemberInfo) response;
							if (memberInfo != null)
								EventBus.getDefault().postSticky(memberInfo);
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
	
	
	
	public void setHeight(){
		int listViewHeight = 0;
		int adaptCount = adapter.getCount();
		for(int i=0;i<adaptCount;i++){
			View temp = adapter.getView(i,null,say_list);
			temp.measure(0,0);
			listViewHeight += temp.getMeasuredHeight();
		}
		LinearLayout.LayoutParams layoutParams = (LayoutParams) say_list.getLayoutParams();
		layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
		layoutParams.height = listViewHeight;
		say_list.setLayoutParams(layoutParams);
	}

	class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			if (null == shuoshuoList) {
				return 0;
			} else if (shuoshuoList.size() > 3) {
				return 3;
			} else {
				return shuoshuoList.size();
			}
		}

		@Override
		public Object getItem(int position) {
			return shuoshuoList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(DoctorClininActivity.this).inflate(R.layout.say_item, null);

				holder.tv_read_num = (TextView) convertView.findViewById(R.id.tv_read_num);
				holder.tv_praise_num = (TextView) convertView.findViewById(R.id.tv_praise_num);
				holder.tv_comment_num = (TextView) convertView.findViewById(R.id.tv_comment_num);
				holder.tv_share_num = (TextView) convertView.findViewById(R.id.tv_share_num);
				holder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
				holder.tv_context = (TextView) convertView.findViewById(R.id.tv_context);
				holder.tv_new = (TextView) convertView.findViewById(R.id.tv_new);
				holder.iv_praise = (ImageView) convertView.findViewById(R.id.iv_praise);
				holder.iv_red_point = (ImageView) convertView.findViewById(R.id.iv_red_point);
				holder.ll_praise_layout = (LinearLayout) convertView.findViewById(R.id.ll_praise_layout);
				holder.ll_comment_layout = (LinearLayout) convertView.findViewById(R.id.ll_comment_layout);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			BaikeSaysInfo info = shuoshuoList.get(position);
			holder.tv_context.setText(info.getCONTENT_SHOW());
			holder.tv_read_num.setText(info.getHITS());
			holder.tv_praise_num.setText(info.getPRAISE_NUM());
			holder.tv_comment_num.setText(info.getCOMMENT_NUM());
			//TODO 待添加
			holder.tv_share_num.setText(info.getSHARE_COUNT());
			holder.tv_time.setText(info.getCREATE_TIME());
			if ("1".equals(info.getIS_NEW())) {
				holder.tv_new.setVisibility(View.VISIBLE);
			} else {
				holder.tv_new.setVisibility(View.GONE);
			}
			if(info.getIS_PRAISED()){
				holder.iv_praise.setBackgroundResource(R.drawable.praise_ioc_p);
			} else {
				holder.iv_praise.setBackgroundResource(R.drawable.praise_ioc_n);
			}
			if (info.getUNREAD_COMMENT_COUNT() > 0) {
				holder.iv_red_point.setVisibility(View.VISIBLE);
			} else {
				holder.iv_red_point.setVisibility(View.GONE);
			}
			
			final int pos = position; 
			final String says_id = info.getSAYSID();
			holder.ll_praise_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					praise_position = pos;
					givePraise(says_id);
				}
			});
			
			final BaikeSaysInfo info2 = info;
			holder.ll_comment_layout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(DoctorClininActivity.this, SayDetailsActivity.class);
					intent.putExtra("says_id", info2.getSAYSID());
					intent.putExtra("need_adjust", true);
					startActivityForResult(intent, REQUEST_SAYS_DETAIL);
				}
			});
			return convertView;
		}

		class ViewHolder {
			TextView tv_context;
			TextView tv_read_num;
			TextView tv_praise_num;
			TextView tv_comment_num;
			TextView tv_share_num;
			TextView tv_time;
			TextView tv_new;
			LinearLayout ll_comment_layout;
			LinearLayout ll_praise_layout;
			ImageView iv_red_point;
			ImageView iv_praise;
		}
	}
	
	private void givePraise(String says_id){
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("saysid", says_id);
		
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_praise, CommonResponseEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GIVE_PRAISE;
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
	
	/** 添加关注*/
	public void setFans() {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", doctorId);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_fans, BaikeFansEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;

							message.what = IResult.BAIKE_ADD_ATTENTION;
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
	
	/** 取消关注*/
	public void cancelFans() {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", doctorId);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.cancel_fans, BaikeFansEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;

							message.what = IResult.BAIKE_CANCEL_ATTENTION;
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
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_cancel:
			serviceDialog.dismiss();
			break;
		case R.id.btn_confirm:
			go2BuyServiceAct(bntid);
			serviceDialog.dismiss();
			break;
		default:
			break;
		}
	}
	
	
}
