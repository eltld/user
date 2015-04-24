package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.entity.CouponEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

import org.json.JSONException;

import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.umeng.analytics.MobclickAgent;
/***
 * 礼品卡界面
 * @author chyaohui@gmail.com
 *
 */
//public class CouponActivity extends BasicActivity {
//	
//	private TextView tvtext;
//	private RefreshListView lvCoupon;
//	private int page = 1;
//	private boolean refresh = true;
//	private Button btn_add_coupon;
//	private CouponAdapter adapter;
//	private String orderId;
//	private LinearLayout layout_empty;
//	private String fromActivity;
//	private LinearLayout ll_net_unavailable;
//	@Override
//	protected void onBeforeCreate(Bundle savedInstanceState) {
//	   super.onBeforeCreate(savedInstanceState);
//	   orderId = getIntent().getStringExtra("orderId");
//	   fromActivity = getIntent().getStringExtra("fromActivity");
//	}
//
//	@Override
//	protected void onCreateContent(Bundle savedInstanceState) {
//
//		super.onCreateContent(savedInstanceState);
//		setContentView(R.layout.u_coupon_list);
//	}
//
//	@Override
//	protected void setupView() {
//		ll_net_unavailable = (LinearLayout)findViewById(R.id.ll_net_unavailable);
//		tvtext = (TextView) findViewById(R.id.top_title);
//		tvtext.setText(CouponActivity.this.getString(R.string.coupon_title));
//		lvCoupon = (RefreshListView) findViewById(R.id.lv_jht);
//		layout_empty = (LinearLayout) findViewById(R.id.layout_empty);
//		adapter = new CouponAdapter(this, null,orderId);
//		lvCoupon.setAdapter(adapter);
//		super.setupView();
//	}
//	
//	
//	
//	private EditText et_account = null;
//	private EditText et_pwd = null;
//	private String couponNameStr = null;
//	
//	public void doClick(View view) {
//		try {
//			if (view.getId() == R.id.imgbtn_add) {
//				Builder builder = new Builder(this);
//				View inputView = LayoutInflater.from(CouponActivity.this).inflate(R.layout.dialog_input_coupon, null);
//				et_account = (EditText) inputView.findViewById(R.id.et_account);
//				et_pwd = (EditText) inputView.findViewById(R.id.et_pwd);
//				builder.setTitle(CouponActivity.this.getString(R.string.coupon_add))
//						.setView(inputView)
//						.setPositiveButton(
//								getString(R.string.add_health_notice_confirm),
//								new OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										String account = et_account.getText().toString();
//										String pwd = et_pwd.getText().toString();
//			   						    System.out.println("account:"+account + " pwd:"+pwd);
//			   						    if (TextUtils.isEmpty(account)) {
//			   						    	et_account.setError(CouponActivity.this.getString(R.string.account_none));
//											return;
//										} else if (TextUtils.isEmpty(pwd)) {
//											et_pwd.setError(CouponActivity.this.getString(R.string.password_none));
//											return;
//										} 
//			   						    addCoupon(account, pwd);
//			   							dialog.dismiss();
//									}
//								})
//						.setNegativeButton(getString(R.string.about_us_quxiao), null)
//						.show();
//			} else if (view.getId() == R.id.imgbtn_back){
//				finish();
//			} else if (view.getId() == R.id.btn_add) {
//				Builder builder = new Builder(this);
//				View inputView = LayoutInflater.from(CouponActivity.this).inflate(R.layout.dialog_input_coupon, null);
//				et_account = (EditText) inputView.findViewById(R.id.et_account);
//				et_pwd = (EditText) inputView.findViewById(R.id.et_pwd);
//				builder.setTitle(CouponActivity.this.getString(R.string.coupon_add))
//						.setView(inputView)
//						.setPositiveButton(
//								getString(R.string.add_health_notice_confirm),
//								new OnClickListener() {
//
//									@Override
//									public void onClick(DialogInterface dialog,
//											int which) {
//										String account = et_account.getText().toString();
//										String pwd = et_pwd.getText().toString();
//			   						    System.out.println("account:"+account + " pwd:"+pwd);
//			   						    if (TextUtils.isEmpty(account)) {
//			   						    	et_account.setError(CouponActivity.this.getString(R.string.account_none));
//											return;
//										} else if (TextUtils.isEmpty(pwd)) {
//											et_pwd.setError(CouponActivity.this.getString(R.string.password_none));
//											return;
//										}
//			   						    addCoupon(account, pwd);
//			   							dialog.dismiss();
//									}
//								})
//						.setNegativeButton(getString(R.string.about_us_quxiao), null)
//						.show();
//			} else if (view.getId() == R.id.iv_net_failed) {
//				
//				if (Utils.netConnect(CouponActivity.this)) {
//					showProgressDialog(IMessage.LOADING);
//					refresh();
//				} else {
//					handler.sendEmptyMessage(IResult.NET_ERROR);
//				}
//				
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
//	
//	@Override
//	protected void addListener() {
//		lvCoupon.setOnItemClickListener(new OnItemClickListener() {
//
//			@Override
//			public void onItemClick(AdapterView<?> parent, View view,
//					int position, long id) {
//				try {
//					CouponEntry couponEntry = (CouponEntry) adapter.getItem(position);
////					if (couponEntry.getTYPEID().equals("2")) {
////						Intent intent = new Intent(CouponActivity.this,NoticeInfoActivity.class);
////						intent.putExtra("title", "优惠信息");
////						intent.putExtra("type", IKey.NOTICE_COUPON_E);
////						startActivity(intent);
////					}
//					
//					if (null!=couponEntry && !TextUtils.isEmpty(couponEntry.getURL_DESCRIPTION())) {
//						Intent intent = new Intent(CouponActivity.this,AboutBrowserActivity.class);
//						intent.putExtra("url",couponEntry.getURL_DESCRIPTION());
//						intent.putExtra("title", "优惠信息");
//						startActivity(intent);
//					}
//			
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//		
//			}
//		});
//		lvCoupon.setOnRefreshListener(new IOnRefreshListener() {
//
//			@Override
//			public void OnRefresh() {
//				refresh();
//
//			}
//		});
//		lvCoupon.setOnLoadMoreListener(new IOnLoadMoreListener() {
//
//			@Override
//			public void OnLoadMore() {
//				loadMore();
//			}
//		});
//
//		super.addListener();
//	}
//
//	private void refresh() {
//		page = 1;
//		refresh = true;
//		getCoupon();
//	}
//
//	private void loadMore() {
//		page++;
//		refresh = false;
//		getCoupon();
//	}
//
//	private void getCoupon() {
//		new Thread() {
//			public void run() {
//				try {
//					CouponInfo map = ClientContent.get_coupon_list(share.getString(IKey.USERID, ""));
//					sendMessage(IResult.RESULT, map);
//				} catch (IOException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(IResult.NET_ERROR);
//				} catch (JSONException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(IResult.DATA_ERROR);
//				}
//			};
//		}.start();
//	}
//	
//	private void addCoupon(final String CouponNum,final String CouponPwd) {
//		showProgressDialog(IMessage.LOADING);
//		new Thread() {
//			public void run() {
//				try {
//					HashMap<String, Object> map = ClientContent
//							.coupon_active(share.getString(IKey.USERID, ""),CouponNum,CouponPwd);
//					sendMessage(IResult.ADD, map);
//				} catch (IOException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(IResult.NET_ERROR);
//				} catch (JSONException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(IResult.DATA_ERROR);
//				}
//			};
//		}.start();
//		
//	}
//	
//	private void useCoupon(final String CouponNum,final String CouponPwd,final String orderId) {
//		showProgressDialog(IMessage.LOADING);
//		new Thread() {
//			public void run() {
//				try {
//					CouponUsedInfo map = ClientContent.coupon_use(share.getString(IKey.USERID, ""),CouponNum,CouponPwd,orderId);
//					sendMessage(IResult.END, map);
//				} catch (IOException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(IResult.NET_ERROR);
//				} catch (JSONException e) {
//					e.printStackTrace();
//					handler.sendEmptyMessage(IResult.DATA_ERROR);
//				}
//			};
//		}.start();
//	}
//	
//	@Override
//	protected void onResult(Message msg) {
//		try {
//			destroyDialog();
//			lvCoupon.onRefreshComplete();
//			HashMap<String, Object> map;
//			int success;
//			switch (msg.what) {
//				case IResult.RESULT:
//					ll_net_unavailable.setVisibility(View.GONE);
//					CouponInfo couponInfo = (CouponInfo) msg.obj;
//					if (couponInfo.getSuccess() == 0) {
//						showMessage(couponInfo.getErrormsg());
//					} else {
//						List<CouponEntry> times = couponInfo.getData();
//						if (times != null && times.size() > 0) {
//							layout_empty.setVisibility(View.GONE);
//							lvCoupon.setVisibility(View.VISIBLE);
//						} else {
//							lvCoupon.setVisibility(View.GONE);
//							layout_empty.setVisibility(View.VISIBLE);
//						}
//						if (refresh) {
//							adapter.change(times);
//						} else {
//							adapter.add(times);
//						}
//					}
//					break;
//				case IResult.NET_ERROR:
//					ll_net_unavailable.setVisibility(View.VISIBLE);
//					layout_empty.setVisibility(View.GONE);
//					lvCoupon.setVisibility(View.GONE);
//					break;
//				case IResult.DATA_ERROR:
//					showMessage(IMessage.DATA_ERROR);
//					break;
//				case IResult.ADD:
//					map = (HashMap<String, Object>) msg.obj;
//					success = (Integer) map.get("success");
//					if (success == 0) {
//						if (null != map.get("errormsg") && !TextUtils.isEmpty((String)map.get("errormsg"))) {
//							String errormsg = (String) map.get("errormsg");
//							if(errormsg.equals("卡已经激活，无法重复激活")){//如果该卡激活过，则继续调用使用接口，代表当前使用者是受赠人。
//								String account = et_account.getText().toString();
//								String pwd = et_pwd.getText().toString();
//								useCoupon(account, pwd, orderId);	
//							} else {
//								showMessage((String) map.get("errormsg"));
//							}
//						} 
//					} else if (success == 1) {
//						if (null != map.get("errormsg") && !TextUtils.isEmpty((String)map.get("errormsg"))) {
//							showToast((String)map.get("errormsg"));	
//						}
//						showProgressDialog(IMessage.LOADING);
//					    refresh();
//					}
//					break;
//				case IResult.END:
//					CouponUsedInfo mCouponUsedInfo = (CouponUsedInfo) msg.obj;
//					if (null!=mCouponUsedInfo) {
//						if (mCouponUsedInfo.getSuccess() == 1) {
//							Intent intetn = new Intent(CouponActivity.this,PayingActivity.class);
//							intetn.putExtra("OrderAmount",mCouponUsedInfo.getData().getOrderAmount());
//							intetn.putExtra("Discount",mCouponUsedInfo.getData().getDiscount());
//							intetn.putExtra("GoodsAmount",mCouponUsedInfo.getData().getGoodsAmount());
//							intetn.putExtra("CouponNum",mCouponUsedInfo.getData().getCouponNum());
//							intetn.putExtra("CouponName",couponNameStr);
//							setResult(RESULT_OK,intetn);
//							finish();			
//						} else {
//							showMessage(mCouponUsedInfo.getErrormsg());
//						}
//					}
//					break;
//					
//				default:
//					break;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		super.onResult(msg);
//	}
//	
//	@Override
//	public boolean onKeyUp(int keyCode, KeyEvent event) {
//		return super.onKeyUp(keyCode, event);
//	}
//
//	@Override
//	protected void getData() {
//		if (Utils.netConnect(CouponActivity.this)) {
//			showProgressDialog(IMessage.LOADING);
//			getCoupon();
//		} else {
//			handler.sendEmptyMessage(IResult.NET_ERROR);
//		}
//		super.getData();
//	}
//
//	@Override
//	protected void onResume() {
//		super.onResume();
//		MobclickAgent.onResume(this);
//	}
//	@Override
//	protected void onPause() {
//		super.onPause();
//		MobclickAgent.onPause(this);
//	}
//	
//	@Override
//	protected void onDestroy() {
//		super.onDestroy();
//	}
//	public class CouponAdapter extends BaseAdapter{
//		private LayoutInflater inflater;
//		private List<CouponEntry> couponList;
//		private String orderId;
//		public CouponAdapter() {
//			super();
//		}
//
//		public CouponAdapter(Context context, List<CouponEntry> couponList,String orderId) {
//			this.inflater = LayoutInflater.from(context);
//			this.couponList = couponList;
//			this.orderId = orderId;
//		}
//
//		@Override
//		public int getCount() {
//			if(couponList==null){
//				return 0;
//			}
//			return couponList.size();
//		}
//
//		@Override
//		public Object getItem(int position) {
//			return couponList.get(position-1);
//		}
//
//		@Override
//		public long getItemId(int position) {
//			return 0;
//		}
//
//		@Override
//		public View getView(int position, View convertView, ViewGroup parent) {
//			ViewHolder holder = null;
//			if(convertView==null){
//				holder = new ViewHolder();
//				convertView = inflater.inflate(R.layout.u_coupon_list_item, null);
//				holder.tvCouponTitle = (TextView) convertView.findViewById(R.id.tv_coupon_title);
//				holder.tvCouponTime = (TextView) convertView.findViewById(R.id.tv_coupon_time);
//				holder.btn_use_coupon = (Button) convertView.findViewById(R.id.btn_use_coupon);
//				holder.btn_share = (Button) convertView.findViewById(R.id.btn_share);
//				convertView.setTag(holder);
//			} else {
//				holder = (ViewHolder) convertView.getTag();
//			}
//			final CouponEntry time = couponList.get(position);
//			if (null != time) {
//				holder.tvCouponTitle.setText(time.getTYPENAME());
//				//DateUtil.parseDate(time.getENDTIME())
//				holder.tvCouponTime.setText(CouponActivity.this.getString(R.string.coupon_deadline) + (TextUtils.isEmpty(time.getENDTIME())?"":time.getENDTIME()));
////				holder.btn_use_coupon.setOnClickListener(new View.OnClickListener() {
////					
////					@Override
////					public void onClick(View v) {
////						if (fromActivity.equals(PayingActivity.class.getSimpleName())) {
////							Builder builder = new Builder(CouponActivity.this);
////							builder.setTitle(CouponActivity.this.getString(R.string.coupon_use))
////									.setPositiveButton(
////											getString(R.string.add_health_notice_confirm),
////											new OnClickListener() {
////
////												@Override
////												public void onClick(DialogInterface dialog,
////														int which) {
////													String account = time.getCARDNUM();
////													String pwd = time.getCARDPWD();
////													couponNameStr  = time.getTYPENAME();
////													useCoupon(account, pwd, orderId);	
////						   							dialog.dismiss();
////												}
////											})
////									.setNegativeButton(getString(R.string.about_us_quxiao), null)
////									.show();
////					
////						} else if (fromActivity.equals(MySpaceActivity.class.getSimpleName())){
////							Intent intent = new Intent(CouponActivity.this, SearchDoctorActivity.class);
////							startActivity(intent);
////						}
////					}
////				});
//				
//				if (TextUtils.isEmpty(time.getSHARETEXT())) {
//					holder.btn_share.setVisibility(View.GONE);	
//				} else {
//					holder.btn_share.setVisibility(View.VISIBLE);
//					holder.btn_share.setOnClickListener(new View.OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							String account = time.getCARDNUM();
//							String pwd = time.getCARDPWD();
//							share(account,pwd,time.getSHARETEXT());
//						}
//					});
//				}
//			
//				
//			}
//			return convertView;
//		}
//		class ViewHolder{
//			public Button btn_share;
//			public Button btn_use_coupon;
//			TextView tvCouponTitle;
//			TextView tvCouponTime;
//		}
//		public void change(List<CouponEntry> couponList) {
//			this.couponList = couponList;
//			notifyDataSetChanged();
//		}
//
//		public void add(List<CouponEntry> couponList) {
//			this.couponList.addAll(couponList);
//			notifyDataSetChanged();
//		}
//	}
//	
//	// 分享
//	private void share(String account,String pwd,final String shareText) {
//		Builder builder = new Builder(this);
//		String[] titles = new String[] { getString(R.string.about_us_emile),getString(R.string.about_us_phone)};
//		
//		int[] icons = new int[] { R.drawable.message, R.drawable.email};
//		
//		String[] from = new String[] { "title", "icon" };
//		
//		int[] to = new int[] { R.id.tv_title, R.id.iv_icon };
//		
//		ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();
//		for (int i = 0; i < titles.length; i++) {
//			HashMap<String, Object> map = new HashMap<String, Object>();
//			map.put("title", titles[i]);
//			map.put("icon", icons[i]);
//			list.add(map);
//		}
//
//		SimpleAdapter adapter = new SimpleAdapter(this, list , R.layout.u_share_item, from, to);
//		builder.setTitle(R.string.about_us_share)
//				.setSingleChoiceItems(adapter, -1,new DialogInterface.OnClickListener() {
//
//							@Override
//							public void onClick(DialogInterface dialog,int which) {
//								switch (which) {
//									case 0:
//										shareByMsg(shareText);
//										break;
//									case 1:
//										shareByEmail(shareText);
//										break;
//								}
//							}
//						}).show();
//	}
//	
//	// 短信分享
//	private void shareByMsg(String shareText) {
//		try {
//			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(""));
//			intent.putExtra("sms_body",shareText + getResources().getString(R.string.download_link));
//			intent.setType("vnd.android-dir/mms-sms");
//			startActivity(intent);	
//		} catch (Exception e) {
//			e.printStackTrace();
//			showToast("您的短信应用出现故障.");
//		}
//	}
//	
//	// 邮件分享
//	private void shareByEmail(String shareText) {
//		try {
//			Intent intent = new Intent(Intent.ACTION_SEND);
//			intent.setType("plain/test");
//			intent.putExtra(Intent.EXTRA_SUBJECT,getString(R.string.share_coupon_title));
//			intent.putExtra(Intent.EXTRA_TEXT,shareText + getResources().getString(R.string.download_link));
//			startActivity(intent);
//		} catch (Exception e) {
//			e.printStackTrace();
//			showToast("您的邮箱应用出现故障.");
//		}
//	}
//
//	
//}
