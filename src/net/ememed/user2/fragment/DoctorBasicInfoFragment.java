package net.ememed.user2.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.BuyCustomActivity;
import net.ememed.user2.activity.BuyJiuyiActivity;
import net.ememed.user2.activity.BuyPhoneConsultActivity;
import net.ememed.user2.activity.BuyShangmenActivity;
import net.ememed.user2.activity.BuyTextConsultActivity;
import net.ememed.user2.activity.BuyZhuyuanActivity;
import net.ememed.user2.activity.DescribeServiceActivity;
import net.ememed.user2.activity.DoctorDetailInfoActivity;
import net.ememed.user2.activity.DoctorInfoActivity;
import net.ememed.user2.activity.ImageActivity;
import net.ememed.user2.activity.JiuyiConsultDetailsActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.activity.PersonInfoActivity;
import net.ememed.user2.activity.PrivateDoctorActivity;
import net.ememed.user2.baike.SayActivity;
import net.ememed.user2.baike.SayDetailsActivity;
import net.ememed.user2.baike.entity.BaikeFansEntry;
import net.ememed.user2.baike.entity.BaikeSaysInfo;
import net.ememed.user2.entity.CommonResponseEntity;
import net.ememed.user2.entity.DoctorFansInfo;
import net.ememed.user2.entity.DoctorServiceEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MemberInfo;
import net.ememed.user2.entity.MemberInfoEntity;
import net.ememed.user2.entity.OrderListEntry;
import net.ememed.user2.entity.PacketPeriod;
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
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.easemob.chat.EMChatManager;

import de.greenrobot.event.EventBus;

public class DoctorBasicInfoFragment extends Fragment implements Handler.Callback, OnClickListener, OnRefreshListener {
	private static final String TAG = MyDoctorFragment.class.getSimpleName();
	private final int REQUEST_SAYS_DETAIL = 2;
	
	private String fragmentKey = "";
	private DoctorDetailInfoActivity activity = null;
	private Handler mHandler;
	// private PullToRefreshLayout mPullToRefreshLayout;
	private LinearLayout ll_view_content;
	private EditText et_name, et_phone, et_sex;
	private EditText et_identity_card, et_medical_num, et_address;
	private TextView et_birthday;
	private String tochat_userId;
	private String doctor_name;
	private boolean init = false;
	public MemberInfoEntity data;
	private RelativeLayout doctor_details;
	private LinearLayout lt_text_consult_below;
	private LinearLayout lt_phone_consult_below;
	private LinearLayout lt_jiuyi_below;
//	private LinearLayout lt_shangmen_below;
	private LinearLayout lt_private_doctor_below;
	private LinearLayout lt_zhuyuan_below;
	private LinearLayout lt_custom_below;

	private LinearLayout lt_text_consult;
	private LinearLayout lt_phone_consult;
//	private LinearLayout lt_shangmen;
	private LinearLayout lt_jiuyi;
	private LinearLayout lt_private_doctor;
	private LinearLayout lt_zhuyuan;
	private LinearLayout lt_custom;
	
	private LinearLayout ll_attention_Layout;
	private TextView tv_attention_num;

	private Button btn_phone_consult;
	private Button btn_shangmen;
	private Button btn_zhuyuan;
	private Button btn_jiuyi;
	private Button btn_private_doctor;
	private Button btn_consult;
	private Button btn_custom;
	private List<ImageView> arrowslist = new ArrayList<ImageView>();
	private List<LinearLayout> belowlist = new ArrayList<LinearLayout>();
	private ImageView iv_text_arrows;
	private ImageView iv_phone_arrows;
	private ImageView iv_shangmen_arrows;
	private ImageView iv_jiuyi_arrows;
	private ImageView iv_zhuyuan_arrows;
	private ImageView iv_private_doctor_arrows;
	private ImageView iv_custom_arrows;
	private ScrollView doctor_scroll_view;
	private ImageView doctor_head_portrait;

	private ListViewForScrollView say_list;
	private TextView tv_all_shuoshuo;
	private TextView tv_attention;
	private TextView tv_fans;

	// private DotorListener dotorListener;
	private MyAdapter adapter;
	private OrderListEntry orderData;
	private boolean isHavePhone = false;
	private int praise_position = -1;
	private boolean isNeedLoadMsg = true;
	private TextView tv_noshuoshuo;

	private List<BaikeSaysInfo> shuoshuoList = new ArrayList<BaikeSaysInfo>();

	public interface DotorListener {

		public void onNumber(int number);
	}

	@Override
	public void setUserVisibleHint(boolean isVisibleToUser) {
		super.setUserVisibleHint(isVisibleToUser);
		
		
		if (isVisibleToUser) {
			
			if(isNeedLoadMsg){
				
				if(activity.memberInfo!=null){
					showDoctorInfo(activity.memberInfo);
				}else{
					getMemberInfo();
				}
				isNeedLoadMsg = false;
			}
		}
		
		
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		this.activity = (DoctorDetailInfoActivity) activity;
		// dotorListener = (DotorListener) activity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mHandler = new Handler(this);
		activity = (DoctorDetailInfoActivity) getActivity();
		fragmentKey = getArguments().getString("fragmentKey");
		tochat_userId = getArguments().getString("tochat_userId");
		doctor_name = getArguments().getString("doctor_name");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.root_layout, null);
		FrameLayout mContentView = (FrameLayout) view.findViewById(R.id.mainView);

		// mPullToRefreshLayout = (PullToRefreshLayout)
		// view.findViewById(R.id.ptr_layout);
		// ActionBarPullToRefresh.from(activity).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);
		ll_view_content = (LinearLayout) LayoutInflater.from(activity).inflate(R.layout.doctor_clinic, null);
		doctor_scroll_view = (ScrollView) ll_view_content.findViewById(R.id.doctor_scroll_view);
		doctor_scroll_view.setVisibility(View.GONE);
		FrameLayout tp_layout = (FrameLayout) ll_view_content.findViewById(R.id.fl_top_title);
		et_name = (EditText) ll_view_content.findViewById(R.id.et_name);
		say_list = (ListViewForScrollView) ll_view_content.findViewById(R.id.say_list);
		adapter = new MyAdapter();
		say_list.setAdapter(adapter);

		say_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				/*Intent intent = new Intent(getActivity(), SayActivity.class);
				intent.putExtra("doctor_id", data.getMEMBERID());
				intent.putExtra("doctor_name", data.getREALNAME());
				startActivity(intent);*/
				BaikeSaysInfo info = (BaikeSaysInfo)parent.getAdapter().getItem(position);
				Intent intent = new Intent(activity, SayDetailsActivity.class);
				intent.putExtra("says_id", info.getSAYSID());
				startActivityForResult(intent, REQUEST_SAYS_DETAIL);
			}
		});
		// setHeight();
		
		tv_all_shuoshuo = (TextView) ll_view_content.findViewById(R.id.tv_all_shuoshuo);
		tv_all_shuoshuo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getActivity(), SayActivity.class);
				intent.putExtra("doctor_id", data.getMEMBERID());
				intent.putExtra("doctor_name", data.getREALNAME());
				startActivity(intent);
			}
		});
		
		tv_attention = (TextView) ll_view_content.findViewById(R.id.tv_attention);
		tv_fans = (TextView) ll_view_content.findViewById(R.id.tv_fans);
		
		tv_noshuoshuo = (TextView) ll_view_content.findViewById(R.id.tv_noshuoshuo);
		
		addListener();
		tp_layout.setVisibility(View.GONE);
		mContentView.addView(ll_view_content);
		return view;
	}

	private void addListener() {
		doctor_head_portrait = (ImageView) ll_view_content.findViewById(R.id.doctor_head_portrait);
		lt_text_consult_below = (LinearLayout) ll_view_content.findViewById(R.id.lt_text_consult_below);
		lt_phone_consult_below = (LinearLayout) ll_view_content.findViewById(R.id.lt_phone_consult_below);
		lt_jiuyi_below = (LinearLayout) ll_view_content.findViewById(R.id.lt_jiuyi_below);
//		lt_shangmen_below = (LinearLayout) ll_view_content.findViewById(R.id.lt_shangmen_below);
		lt_private_doctor_below = (LinearLayout) ll_view_content.findViewById(R.id.lt_private_doctor_below);
		lt_zhuyuan_below = (LinearLayout) ll_view_content.findViewById(R.id.lt_zhuyuan_below);
		lt_custom_below = (LinearLayout) ll_view_content.findViewById(R.id.lt_custom_below);
		belowlist.add(lt_text_consult_below);
		belowlist.add(lt_phone_consult_below);
		belowlist.add(lt_jiuyi_below);
//		belowlist.add(lt_shangmen_below);
		belowlist.add(lt_private_doctor_below);
		belowlist.add(lt_zhuyuan_below);
		belowlist.add(lt_custom_below);
		doctor_details = (RelativeLayout) ll_view_content.findViewById(R.id.doctor_details);
		iv_text_arrows = (ImageView) ll_view_content.findViewById(R.id.iv_text_consult_arrows);
		iv_phone_arrows = (ImageView) ll_view_content.findViewById(R.id.iv_phone_arrows);
//		iv_shangmen_arrows = (ImageView) ll_view_content.findViewById(R.id.iv_shangmen_arrows);
		iv_jiuyi_arrows = (ImageView) ll_view_content.findViewById(R.id.iv_jiuyi_arrows);
		iv_zhuyuan_arrows = (ImageView) ll_view_content.findViewById(R.id.iv_zhuyuan_arrows);
		iv_private_doctor_arrows = (ImageView) ll_view_content.findViewById(R.id.iv_private_doctor_arrows);
		iv_custom_arrows = (ImageView) ll_view_content.findViewById(R.id.iv_custom_arrows);
		arrowslist.add(iv_text_arrows);
		arrowslist.add(iv_phone_arrows);
//		arrowslist.add(iv_shangmen_arrows);
		arrowslist.add(iv_jiuyi_arrows);
		arrowslist.add(iv_zhuyuan_arrows);
		arrowslist.add(iv_private_doctor_arrows);
		arrowslist.add(iv_custom_arrows);

		lt_text_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_text_consult);
		lt_phone_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_phone_consult);
//		lt_shangmen = (LinearLayout) ll_view_content.findViewById(R.id.lt_shangmen);
		lt_jiuyi = (LinearLayout) ll_view_content.findViewById(R.id.lt_jiuyi);
		lt_private_doctor = (LinearLayout) ll_view_content.findViewById(R.id.lt_private_doctor);
		lt_zhuyuan = (LinearLayout) ll_view_content.findViewById(R.id.lt_zhuyuan);
		lt_custom = (LinearLayout) ll_view_content.findViewById(R.id.lt_custom);
		
		lt_custom.setVisibility(View.GONE);
		
		btn_phone_consult = (Button) ll_view_content.findViewById(R.id.btn_phone_consult);
		btn_shangmen = (Button) ll_view_content.findViewById(R.id.btn_shangmen);
		btn_zhuyuan = (Button) ll_view_content.findViewById(R.id.btn_zhuyuan);
		btn_jiuyi = (Button) ll_view_content.findViewById(R.id.btn_jiuyi);
		btn_private_doctor = (Button) ll_view_content.findViewById(R.id.btn_private_doctor);
		btn_consult = (Button) ll_view_content.findViewById(R.id.btn_consult);
		btn_custom = (Button) ll_view_content.findViewById(R.id.btn_custom);

		Button btn_phone_consult_desc = (Button) ll_view_content.findViewById(R.id.btn_phone_consult_desc);
		Button btn_shangmen_consult_desc = (Button) ll_view_content.findViewById(R.id.btn_shangmen_consult_desc);
		Button btn_zhuyuan_desc = (Button) ll_view_content.findViewById(R.id.btn_zhuyuan_desc);
		Button btn_jiuyi_desc = (Button) ll_view_content.findViewById(R.id.btn_jiuyi_desc);
		Button btn_consult_desc = (Button) ll_view_content.findViewById(R.id.btn_consult_desc);
		Button btn_private_doctor_desc = (Button) ll_view_content.findViewById(R.id.btn_private_doctor_desc);
		Button btn_custom_desc = (Button) ll_view_content.findViewById(R.id.btn_custom_desc);
		btn_phone_consult_desc.setOnClickListener(this);
		btn_shangmen_consult_desc.setOnClickListener(this);
		btn_zhuyuan_desc.setOnClickListener(this);
		btn_jiuyi_desc.setOnClickListener(this);
		btn_consult_desc.setOnClickListener(this);
		btn_private_doctor_desc.setOnClickListener(this);
		btn_custom_desc.setOnClickListener(this);

		ll_view_content.findViewById(R.id.contact_image).setOnClickListener(this);

		lt_text_consult.setOnClickListener(this);
		lt_phone_consult.setOnClickListener(this);
//		lt_shangmen.setOnClickListener(this);
		lt_jiuyi.setOnClickListener(this);
		lt_private_doctor.setOnClickListener(this);
		lt_zhuyuan.setOnClickListener(this);
		lt_custom.setOnClickListener(this);
		doctor_details.setOnClickListener(this);

		btn_phone_consult.setOnClickListener(this);
		btn_shangmen.setOnClickListener(this);
		btn_zhuyuan.setOnClickListener(this);
		btn_jiuyi.setOnClickListener(this);
		btn_private_doctor.setOnClickListener(this);
		btn_consult.setOnClickListener(this);
		btn_custom.setOnClickListener(this);
		
		ll_attention_Layout = (LinearLayout) ll_view_content.findViewById(R.id.follow_layout);
		tv_attention_num = (TextView) ll_view_content.findViewById(R.id.follow_tx);
		ll_attention_Layout.setOnClickListener(this);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		// System.out.println("TestFragment onSaveInstanceState fragmentKey : "+fragmentKey);
	}

	/** 获取用户资料和open订单 */
	public void getMemberInfo() {

		if (NetWorkUtils.detect(activity)) {
			if(null == memberInfo){
				activity.loading(null);
			}
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("any_user_id", tochat_userId);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_member_info, MemberInfo.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.MEMBER_INFO;
							mHandler.sendMessage(message);

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
							mHandler.sendMessage(message);
						}
					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}
	
	/** 添加关注*/
	public void setFans() {

		if (NetWorkUtils.detect(activity)) {
			activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", tochat_userId);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_fans, BaikeFansEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;

							message.what = IResult.BAIKE_ADD_ATTENTION;
							mHandler.sendMessage(message);

						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}
	
	/** 取消关注*/
	public void cancelFans() {

		if (NetWorkUtils.detect(activity)) {
			activity.loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", tochat_userId);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.cancel_fans, BaikeFansEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;

							message.what = IResult.BAIKE_CANCEL_ATTENTION;
							mHandler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	private void givePraise(String says_id){
		if (NetWorkUtils.detect(activity)) {
			activity.loading(null);
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
							mHandler.sendMessage(message);
						}
					}, new Response.ErrorListener() {
						@Override
						public void onErrorResponse(VolleyError error) {
							Message message = new Message();
							message.obj = error.getMessage();
							message.what = IResult.DATA_ERROR;
							mHandler.sendMessage(message);
						}
					});
		} else {
			mHandler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}
	
	@Override
	public boolean handleMessage(Message msg) {
		try {
			switch (msg.what) {
			case IResult.MEMBER_INFO:
				activity.destroyDialog();
				doctor_scroll_view.setVisibility(View.VISIBLE);
				memberInfo = (MemberInfo) msg.obj;
				if (null != memberInfo) {
					showDoctorInfo(memberInfo);
				} else {
					activity.showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.BAIKE_ADD_ATTENTION:
				activity.destroyDialog();
				BaikeFansEntry entry = (BaikeFansEntry) msg.obj;
				if(1 == entry.getSuccess()){
					activity.showToast("添加关注成功");
					data.getDOCTOR_FANS().setIS_MY_FANS("1");
					//XXX data.getDOCTOR_FANS().setFANS_NUM(entry.getData().getDoctor_fans_num());
					tv_attention_num.setText("已关注");
					data.getDOCTOR_FANS().setFANS_NUM(data.getDOCTOR_FANS().getFANS_NUM()+1);
					data.setFans_user_total_num((Integer.parseInt(data.getFans_user_total_num())+1)+"");
					tv_fans.setText("粉丝：" + data.getDOCTOR_FANS().getFANS_NUM());
				} else {
					activity.showToast("添加关注失败，请稍后再试");
				}
				break;
			case IResult.BAIKE_CANCEL_ATTENTION:
				activity.destroyDialog();
				BaikeFansEntry entry2 = (BaikeFansEntry) msg.obj;
				if(1 == entry2.getSuccess()){
					activity.showToast("取消关注成功");
					data.getDOCTOR_FANS().setIS_MY_FANS("0");
					//XXX data.getDOCTOR_FANS().setFANS_NUM(entry2.getData().getDoctor_fans_num());
					tv_attention_num.setText("关注");
					data.getDOCTOR_FANS().setFANS_NUM(data.getDOCTOR_FANS().getFANS_NUM()-1);
					data.setFans_user_total_num((Integer.parseInt(data.getFans_user_total_num())-1)+"");
					tv_fans.setText("粉丝：" + data.getDOCTOR_FANS().getFANS_NUM());
				} else {
					activity.showToast("取消关注失败，请稍后再试");
				}
				break;
			case IResult.GIVE_PRAISE:
				activity.destroyDialog();
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
						activity.showToast(response2.getErrormsg());
					}
				}
				praise_position = -1;
				break;
			case IResult.NET_ERROR:
				activity.destroyDialog();
				activity.showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				activity.destroyDialog();
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private boolean isOpenPrivateDoctor = false;
	private String PrivateDoctor_Order;
	private MemberInfo memberInfo;

	private void setBasicInfo(MemberInfoEntity data) {
		TextView tv_private_doctor_price = (TextView) ll_view_content.findViewById(R.id.tv_private_doctor_price);
		TextView tv_doctor_name = (TextView) ll_view_content.findViewById(R.id.tv_doctor_name);
		TextView tv_doctor_alias = (TextView) ll_view_content.findViewById(R.id.tv_doctor_alias);
		TextView tv_search_speciality = (TextView) ll_view_content.findViewById(R.id.tv_search_speciality);
		TextView tv_hos_name = (TextView) ll_view_content.findViewById(R.id.tv_hos_name);
		LinearLayout ll_special = (LinearLayout) ll_view_content.findViewById(R.id.ll_special);
		
		ImageView doctor_head_portrait = (ImageView) ll_view_content.findViewById(R.id.doctor_head_portrait);
		if (!TextUtils.isEmpty(data.getAVATAR())) {
			activity.imageLoader.displayImage(data.getAVATAR(), doctor_head_portrait, Util.getOptions_big_avatar());
		}
		List<OrderListEntry> open_ORDER = data.getOPEN_ORDER();
		for (Iterator iterator = open_ORDER.iterator(); iterator.hasNext();) {
			OrderListEntry orderListEntry = (OrderListEntry) iterator.next();
			if ("15".equals(orderListEntry.getORDERTYPE())) {
				isOpenPrivateDoctor = true;
				PrivateDoctor_Order = orderListEntry.getORDERID();
				isHavePhone = true;
				orderData = orderListEntry;
				btn_private_doctor.setText("查看");
				tv_private_doctor_price.setVisibility(View.GONE);
			}
		}

		if (!TextUtils.isEmpty(data.getAVATAR())) {
			activity.imageLoader.displayImage(data.getAVATAR(), doctor_head_portrait, Util.getOptions_pic());
		}
		tv_doctor_name.setText(data.getREALNAME());
		tv_doctor_alias.setText(data.getPROFESSIONAL());
		if (!TextUtils.isEmpty(data.getSPECIALITY())) {
			tv_search_speciality.setText(data.getSPECIALITY());
		} else {
			ll_special.setVisibility(View.GONE);
		}
		
		if(null != data.getDOCTOR_FANS()){
			DoctorFansInfo info = data.getDOCTOR_FANS();
			if("1".equals(info.getIS_MY_FANS())){
				tv_attention_num.setText("已关注");
			} else {
				tv_attention_num.setText("关注");
			}
			tv_attention.setText("关注："+ info.getAttention_total_num());
			tv_fans.setText("粉丝：" + info.getFANS_NUM());
		}

		tv_hos_name.setText(data.getHOSPITALNAME());
		TextView tv_doctor_quanke = (TextView) ll_view_content.findViewById(R.id.tv_doctor_quanke);
		TextView tv_doctor_zhuanke = (TextView) ll_view_content.findViewById(R.id.tv_doctor_zhuanke);

		if (data.getALLOWFREECONSULT().equals("1")) {
			tv_doctor_quanke.setVisibility(View.VISIBLE);
			tv_doctor_zhuanke.setVisibility(View.GONE);
		} else if (data.getALLOWFREECONSULT().equals("0")) {
			tv_doctor_quanke.setVisibility(View.GONE);
			tv_doctor_zhuanke.setVisibility(View.VISIBLE);
		}
		ColoredRatingBar rtb_service_goods = (ColoredRatingBar) ll_view_content.findViewById(R.id.rtb_service_goods);
		rtb_service_goods.setNumStars(5);
		rtb_service_goods.setRating(5);
		if (!TextUtils.isEmpty(data.getDOCTOR_DSRS()) && !"0".equals(data.getDOCTOR_DSRS())) {
			rtb_service_goods.setRating(Integer.valueOf(data.getDOCTOR_DSRS()) / 2);
		}

		TextView tv_consult_price = (TextView) ll_view_content.findViewById(R.id.tv_consult_price);
		TextView tv_phone_consult_price = (TextView) ll_view_content.findViewById(R.id.tv_phone_consult_price);

		LinearLayout lt_all_text_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_all_text_consult);
		LinearLayout lt_all_phone_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_all_phone_consult);
		LinearLayout lt_all_shangmen_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_all_shangmen_consult);
		LinearLayout lt_all_jiuyi_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_all_jiuyi_consult);
		LinearLayout lt_all_zhuyuan_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_all_zhuyuan_consult);
		LinearLayout lt_all_private_doctor_consult = (LinearLayout) ll_view_content.findViewById(R.id.lt_all_private_doctor_consult);

		CheckBox follow = (CheckBox) ll_view_content.findViewById(R.id.follow);
		if(!TextUtils.isEmpty(data.getAUDITSTATUS()) && "1".equals(data.getAUDITSTATUS())){
			follow.setChecked(true);
		} else {
			follow.setChecked(false);
		}
		
		DoctorServiceEntry server_off = data.getOFFER_SERVICE();

		if (null != server_off) {

			if (null != server_off.getOFFER_TEXT() && server_off.getOFFER_TEXT().getIS_OFFER() == 1) {
				lt_all_text_consult.setVisibility(View.VISIBLE);
				tv_consult_price.setText(Util.PriceFormat(Util.DecZore(server_off.getOFFER_TEXT().getOFFER_PRICE())));

			} else {
				lt_all_text_consult.setVisibility(View.GONE);
			}

			if (null != server_off.getOFFER_CALL() && server_off.getOFFER_CALL().getIS_OFFER() == 1) {
				lt_all_phone_consult.setVisibility(View.VISIBLE);
				tv_phone_consult_price.setText(Util.PriceFormat(Util.DecZore(server_off.getOFFER_CALL().getOFFER_PRICE())));
			} else {
				lt_all_phone_consult.setVisibility(View.GONE);
			}

			if (null != server_off.getOFFER_SHANGMEN() && server_off.getOFFER_SHANGMEN().getIS_OFFER() == 1) {
//				lt_all_shangmen_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_shangmen_consult.setVisibility(View.GONE);
			}

			if (null != server_off.getOFFER_JIAHAO() && server_off.getOFFER_JIAHAO().getIS_OFFER() == 1) {
				lt_all_jiuyi_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_jiuyi_consult.setVisibility(View.GONE);
			}
			if (null != server_off.getOFFER_ZHUYUAN() && server_off.getOFFER_ZHUYUAN().getIS_OFFER() == 1) {
				lt_all_zhuyuan_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_zhuyuan_consult.setVisibility(View.GONE);
			}
			if (null != server_off.getOFFER_PACKET() && server_off.getOFFER_PACKET().getIS_OFFER().equals("1")) {
				lt_all_private_doctor_consult.setVisibility(View.VISIBLE);
			} else {
				lt_all_private_doctor_consult.setVisibility(View.GONE);
			}
			if (null != server_off.getOFFER_PACKET() && server_off.getOFFER_PACKET().getPACKET_PERIOD_LIST() != null
					&& server_off.getOFFER_PACKET().getPACKET_PERIOD_LIST().size() > 0) {

				// lt_all_private_doctor_consult.setVisibility(View.VISIBLE);
				List<PacketPeriod> packet_PERIOD_LIST = server_off.getOFFER_PACKET().getPACKET_PERIOD_LIST();
				if (packet_PERIOD_LIST.size() == 1) {
					tv_private_doctor_price.setText(Util.PriceFormat(Util.DecZore(packet_PERIOD_LIST.get(0)
							.getPacket_period_price())));
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
						tv_private_doctor_price.setText("￥" + Util.DecZore(list.get(0)) + "-"
								+ Util.DecZore(list.get(list.size() - 1)));
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
	}

	@Override
	public void onClick(View view) {
		int id = view.getId();
		switch (id) {
		case R.id.doctor_details:
			Intent intent_details = new Intent(getActivity(), DoctorInfoActivity.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("data", data);
			intent_details.putExtras(bundle);
			intent_details.putExtra("src", this.getClass().getSimpleName());
			intent_details.putExtra("doctor_name", doctor_name);

			this.startActivity(intent_details);
			break;
		case R.id.lt_text_consult:
			if (lt_text_consult_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_text_consult_below);
				setArrowsUp(iv_text_arrows);
			} else {
				lt_text_consult_below.setVisibility(View.GONE);
				iv_text_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_phone_consult:
			if (lt_phone_consult_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_phone_consult_below);
				setArrowsUp(iv_phone_arrows);
			} else {
				lt_phone_consult_below.setVisibility(View.GONE);
				iv_phone_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
//		case R.id.lt_shangmen:
//			if (lt_shangmen_below.getVisibility() == View.GONE) {
//				setBelowVISIBLE(lt_shangmen_below);
//				setArrowsUp(iv_shangmen_arrows);
//			} else {
//				lt_shangmen_below.setVisibility(View.GONE);
//				iv_shangmen_arrows.setImageResource(R.drawable.pic_down);
//			}
//			break;
		case R.id.lt_jiuyi:
			if (lt_jiuyi_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_jiuyi_below);
				setArrowsUp(iv_jiuyi_arrows);
			} else {
				lt_jiuyi_below.setVisibility(View.GONE);
				iv_jiuyi_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_zhuyuan:
			if (lt_zhuyuan_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_zhuyuan_below);
				setArrowsUp(iv_zhuyuan_arrows);
			} else {
				lt_zhuyuan_below.setVisibility(View.GONE);
				iv_zhuyuan_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_private_doctor:
			if (lt_private_doctor_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_private_doctor_below);
				setArrowsUp(iv_private_doctor_arrows);
			} else {
				lt_private_doctor_below.setVisibility(View.GONE);
				iv_private_doctor_arrows.setImageResource(R.drawable.pic_down);
			}
			break;
		case R.id.lt_custom:

			if (lt_custom_below.getVisibility() == View.GONE) {
				setBelowVISIBLE(lt_custom_below);
				setArrowsUp(iv_custom_arrows);
				Handler handler = new Handler();
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
			if (NetWorkUtils.detect(activity)) {
				if (null == EMChatManager.getInstance() || !EMChatManager.getInstance().isConnected()) {
					activity.showToast(getString(R.string.connect_chat_failed));
				} else {
					go2BuyServiceAct(id);
				}
			} else {
				mHandler.sendEmptyMessage(IResult.NET_ERROR);
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
			if (data != null && !TextUtils.isEmpty(data.getAVATAR())) {
				Intent intent = new Intent(activity, ImageActivity.class);
				intent.putExtra("avatar", data.getAVATAR());
				startActivity(intent);
			}
		case R.id.follow_layout:
			if("0".equals(data.getDOCTOR_FANS().getIS_MY_FANS())){
				setFans();
			} else {
				cancelFans();
			}
			break;
			
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
	
	int bntid;
	private void goDescribe(int id) {
//		Intent intent = new Intent(getActivity(), DescribeServiceActivity.class);
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
//		intent.putExtra("service_type", service_type);
//		intent.putExtra("service_buy", service_buy);
		
		serviceDialog = new DescribeServiceDialog(getActivity(), R.style.wheelDialog);
		serviceDialog.show();
		serviceDialog.setShowText(service_type, service_buy, this);
		
		
//		intent.putExtra("doctor_name", doctor_name);
//		intent.putExtra("memberInfo", memberInfo);
//		intent.putExtra("PrivateDoctor_Order", PrivateDoctor_Order);
//		startActivity(intent);
	}
	DescribeServiceDialog serviceDialog;
	private void go2BuyServiceAct(int bt_id) {
		if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)) {// 没登录
			Intent intent = new Intent(activity, LoginActivity.class);
			startActivity(intent);
		} else if (TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_SEX))
				|| TextUtils.isEmpty(SharePrefUtil.getString(Conast.realName))
				|| TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_BIRTHDAY))
				|| TextUtils.isEmpty(SharePrefUtil.getString(Conast.USER_MOBILE))) {
			// 资料没完善
			MenuDialog.Builder alert = new MenuDialog.Builder(activity);
			MenuDialog dialog = alert.setTitle(R.string.system_info).setMessage(R.string.you_should_improve_data)
					.setPositiveButton(getString(R.string.confirm_yes), new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							Intent intent = new Intent(activity, PersonInfoActivity.class);
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
				intent = new Intent(activity, BuyTextConsultActivity.class);
			} else if (bt_id == R.id.btn_private_doctor) {
				if (isHavePhone) {
					intent = new Intent(activity, JiuyiConsultDetailsActivity.class);
					intent.putExtra("item", orderData);
					intent.putExtra("doctor_name", data.getREALNAME());
					intent.putExtra("tochat_userId", data.getMEMBERID());
					intent.putExtra("doctor_avatar", data.getAVATAR());
					startActivity(intent);
					return;
				} else {
					intent = new Intent(activity, PrivateDoctorActivity.class);
				}
			} else if (bt_id == R.id.btn_jiuyi) {
				intent = new Intent(activity, BuyJiuyiActivity.class);
			} else if (bt_id == R.id.btn_zhuyuan) {
				intent = new Intent(activity, BuyZhuyuanActivity.class);
			} else if (bt_id == R.id.btn_shangmen) {
				intent = new Intent(activity, BuyShangmenActivity.class);
			} else if (bt_id == R.id.btn_phone_consult) {

				intent = new Intent(activity, BuyPhoneConsultActivity.class);
			} else if (bt_id == R.id.btn_custom) {
				intent = new Intent(activity, BuyCustomActivity.class);
			}
			if (data == null) {
				Toast.makeText(activity, "数据加载加载中请稍后", 0).show();
				return;
			}
			
			intent.putExtra("doctorid", data.getMEMBERID());
			intent.putExtra("doctorServiceEntry", data.getOFFER_SERVICE());
			intent.putExtra("doctor_name", doctor_name);
			startActivity(intent);
		}
	}
	
	
	

	@Override
	public void onRefreshStarted(View view) {
		//getMemberInfo();
	}

	private void setArrowsUp(ImageView iv) {
		for (Iterator<ImageView> iterator = arrowslist.iterator(); iterator.hasNext();) {
			ImageView next = iterator.next();
			next.setImageResource(R.drawable.pic_down);
		}
		iv.setImageResource(R.drawable.pic_up);
	}

	private void setArrowsDown(ImageView iv) {
		for (Iterator<ImageView> iterator = arrowslist.iterator(); iterator.hasNext();) {
			ImageView next = iterator.next();
			next.setImageResource(R.drawable.pic_up);
		}
		iv.setImageResource(R.drawable.pic_down);
	}

	private void setBelowVISIBLE(LinearLayout below) {
		for (Iterator<LinearLayout> iterator = belowlist.iterator(); iterator.hasNext();) {
			LinearLayout next = iterator.next();
			next.setVisibility(View.GONE);
		}
		below.setVisibility(View.VISIBLE);
	}

	public void setHeight() {
		int listViewHeight = 0;
		int adaptCount = adapter.getCount();
		for (int i = 0; i < adaptCount; i++) {
			View temp = adapter.getView(i, null, say_list);
			temp.measure(0, 0);
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
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.say_item, null);

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
			holder.tv_share_num.setText(info.getSHARE_COUNT());
			
			holder.tv_time.setText(info.getCREATE_TIME());
			if (!TextUtils.isEmpty(info.getIS_NEW()) && "1".equals(info.getIS_NEW())) {
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
					Intent intent = new Intent(activity, SayDetailsActivity.class);
					intent.putExtra("says_id", info2.getSAYSID());
					intent.putExtra("need_adjust", true);
					DoctorBasicInfoFragment.this.startActivityForResult(intent, REQUEST_SAYS_DETAIL);
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
	
	
	public void showDoctorInfo(MemberInfo memberInfo){
		doctor_scroll_view.setVisibility(View.VISIBLE);
		if (memberInfo.getSuccess() == 1) {
			data = memberInfo.getData();
			init = true;
			setBasicInfo(data);
			//XXX
			if (data != null && data.getSAYS_LIST() != null) {
				tv_noshuoshuo.setVisibility(View.GONE);
				shuoshuoList.clear();
				shuoshuoList.addAll(data.getSAYS_LIST());
				adapter.notifyDataSetChanged();
			} else {
				tv_noshuoshuo.setVisibility(View.VISIBLE);
			}
		} else {
			activity.showToast(memberInfo.getErrormsg());
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		if(REQUEST_SAYS_DETAIL == requestCode){
			getMemberInfo();
	    }
	}
}
