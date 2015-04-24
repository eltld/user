package net.ememed.user2.baike;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.activity.LoginActivity;
import net.ememed.user2.baike.adapter.CommentAdapter;
import net.ememed.user2.baike.adapter.PutImageAdapter;
import net.ememed.user2.baike.entity.BaikeSaysInfo;
import net.ememed.user2.baike.entity.SaysCommentInfo;
import net.ememed.user2.baike.entity.SaysDetailEntry;
import net.ememed.user2.entity.CommonResponseEntity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.request.UserPreferenceWrapper;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.PublicUtil;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.ShareSdkUtil;
import net.ememed.user2.widget.MyGridView;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.ShareEnd;

public class SayDetailsActivity extends BasicActivity {

	private ListView lv_comment;
	private String says_id;
	private Boolean need_adjust = false;
	private TextView tv_title;
	private TextView tv_time;
	private TextView tv_author;
	private TextView tv_read_num;
	private TextView tv_praise_num;
	private TextView tv_share_num;
	private EditText et_comment;
	private LinearLayout ll_pic_area;
	private MyGridView gridview;
	private Button btn_send;
	private Button btn_praise;
	private String refer_commentid = null;
	private CommentAdapter adapter;
	private PutImageAdapter adapter2;
	private List<SaysCommentInfo> commentList;
	private String commentid = null;
	private TextView tv_comment_num;
	private RelativeLayout rl_content_area;
	private BaikeSaysInfo detailInfo = null;
	private List<String> pics = null;
	private List<String> pics_thumb = null;
	private ScrollView scrollView1;
	private LinearLayout ll_all_area;
	// private boolean isFirstEntry = true;
	private FrameLayout fl_topLayout;
	private CheckBox cb_annoymous;
	private ShareSdkUtil share = null;
	private InnerReceiver receiver;
	private IntentFilter filter;
	private TextView tv_shuoshuo;

	WebView wv_oauth;

	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);

		setContentView(R.layout.activity_say_details);
		says_id = getIntent().getStringExtra("says_id");
		need_adjust = getIntent().getBooleanExtra("need_adjust", false);

		receiver = new InnerReceiver();
		filter = new IntentFilter();
		filter.addAction(MyApplication.WECHAT_SHARE_SUCCESS);
		registerReceiver(receiver, filter);

		initView();
	}

	public void onEvent(ShareEnd event) {
		destroyDialog();
	}

	public void initView() {

		fl_topLayout = (FrameLayout) findViewById(R.id.fl_top_title);
		((TextView) findViewById(R.id.top_title)).setText("说说详情");
		((ImageView) findViewById(R.id.iv_right_fun))
				.setVisibility(View.VISIBLE);

		rl_content_area = (RelativeLayout) findViewById(R.id.rl_content_area);
		rl_content_area.setVisibility(View.GONE);

		cb_annoymous = (CheckBox) findViewById(R.id.cb_annoymous);

		btn_praise = (Button) findViewById(R.id.btn_praise);
		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_time = (TextView) findViewById(R.id.tv_time);
		tv_author = (TextView) findViewById(R.id.tv_author);
		tv_read_num = (TextView) findViewById(R.id.tv_read_num);
		tv_praise_num = (TextView) findViewById(R.id.tv_praise_num);
		tv_share_num = (TextView) findViewById(R.id.tv_share_num);
		tv_comment_num = (TextView) findViewById(R.id.tv_comment_num);

		lv_comment = (ListView) findViewById(R.id.lv_comment); 
		adapter = new CommentAdapter(null, this, handler);
		lv_comment.setAdapter(adapter);

		ll_pic_area = (LinearLayout) findViewById(R.id.ll_pic_area);
		gridview = (MyGridView) findViewById(R.id.gridview);
		// gridview.setColumnWidth(Util.dip2px(this, 70));

		btn_send = (Button) findViewById(R.id.btn_send);
		et_comment = (EditText) findViewById(R.id.et_comment);
		et_comment.setOnFocusChangeListener(new OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					btn_send.setVisibility(View.VISIBLE);
				} else {
					btn_send.setVisibility(View.GONE);
				}
			}
		});
		scrollView1 = (ScrollView) findViewById(R.id.scrollView1);
		ll_all_area = (LinearLayout) findViewById(R.id.ll_all_area);
		tv_shuoshuo = (TextView) findViewById(R.id.tv_shuoshuo);
		wv_oauth = (WebView) findViewById(R.id.wv_oauth);

		getSayDetail();
	}

	private void getSayDetail() {
		if (NetWorkUtils.detect(SayDetailsActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("saysid", says_id);
			params.put("app_version", PublicUtil.getVersionName(this));

			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.get_says_detail, SaysDetailEntry.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.GET_SAYS_DETAIL;
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

//	private void setShare() {
	
	private void setShare(int channel_type){
		//如果未登录，则不统计说说的分享情况
		if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)){
			return;
		} 
		
		if (NetWorkUtils.detect(this)) {
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("other_doctorid", detailInfo.getDOCTORID());
			params.put("saysid", detailInfo.getSAYSID());
			params.put("channel_type", ""+channel_type);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_share,
					CommonResponseEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.SET_SHARE;
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

	private void givePraise() {
		if (NetWorkUtils.detect(SayDetailsActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("saysid", says_id);

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_praise,
					CommonResponseEntity.class, params,
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

	private void commentShuoshuo(String refer_commentid, String annoymous) {
		if (NetWorkUtils.detect(SayDetailsActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("saysid", says_id);
			params.put("content", et_comment.getText().toString().trim());
			// XXX 匿名
			params.put("anonymous", annoymous);
			if (null != refer_commentid) {
				params.put("refer_commentid", refer_commentid);
			}

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.set_comment,
					CommonResponseEntity.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.COMMENT_SHUOSHUO;
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

	private void delelteComment(String commentid) {
		if (NetWorkUtils.detect(SayDetailsActivity.this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("commentid", commentid);

			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.delete_comment, CommonResponseEntity.class,
					params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.DELETE_COMMENT;
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

			switch (msg.what) {
			case IResult.GET_SAYS_DETAIL:
				destroyDialog();
				SaysDetailEntry response = (SaysDetailEntry) msg.obj;
				if (null != response) {
					if (1 == response.getSuccess()) {

						rl_content_area.setVisibility(View.VISIBLE);

						// tv_shuoshuo.setMovementMethod(LinkMovementMethod.getInstance());//加这句才能让里面的超链接生效
						// URLImageGetter ReviewImgGetter = new
						// URLImageGetter(SayDetailsActivity.this,
						// tv_shuoshuo);//实例化URLImageGetter类
						// tv_shuoshuo.setText(Html.fromHtml(response.getData().getCONTENT(),
						// ReviewImgGetter, null));

						wv_oauth.loadData(response.getData().getCONTENT(),
								"text/html; charset=UTF-8", null);

						/*
						 * if(true == need_adjust){
						 * wb.addOnLayoutChangeListener(new
						 * OnLayoutChangeListener() {
						 * 
						 * @Override public void onLayoutChange(View v, int
						 * left, int top, int right, int bottom, int oldLeft,
						 * int oldTop, int oldRight, int oldBottom) {
						 * scrollToBottom(scrollView1, ll_all_area); } }); }
						 */

						commentList = response.getData().getCOMMENT_LIST()
								.getList();
						if (commentList != null && commentList.size() > 0) {
							adapter.change(commentList);
						}

						pics = response.getData().getPICS();
						pics_thumb = response.getData().getPICS_THUMB();
						if (pics != null && pics.size() > 0) {
							ll_pic_area.setVisibility(View.VISIBLE);
							List<Map<String, String>> picMapList = new ArrayList<Map<String, String>>();
							for (int i = 0; i < pics.size(); i++) {
								HashMap<String, String> tempMap = new HashMap<String, String>();
								tempMap.put("url", pics_thumb.get(i));
								tempMap.put("url_big", pics.get(i));
								picMapList.add(tempMap);
							}
							adapter2 = new PutImageAdapter(picMapList, this, false);
							gridview.setAdapter(adapter2);
						} else {
							ll_pic_area.setVisibility(View.GONE);
						}
						detailInfo = response.getData();
						updateView(response.getData());
					} else {
						showToast(response.getErrormsg());
					}
				} else {
					showToast("获取说说详情失败");
				}

				break;
			case IResult.GIVE_PRAISE:
				destroyDialog();
				CommonResponseEntity response2 = (CommonResponseEntity) msg.obj;
				if (null != response2) {
					if (1 == response2.getSuccess()) {
						detailInfo.setPRAISE_NUM((Integer.parseInt(detailInfo.getPRAISE_NUM())+1)+"");
						tv_praise_num.setText(detailInfo.getPRAISE_NUM());
						UserPreferenceWrapper.addMyPraiseCount();
						if (!detailInfo.getIS_PRAISED()) {
							detailInfo.setIS_PRAISED(true);
							btn_praise.setBackgroundResource(R.drawable.btn_already_praise);
						}
						setResult(RESULT_OK);
					} else {
						showToast(response2.getErrormsg());
					}
				}
				break;
			case IResult.COMMENT_SHUOSHUO:
				CommonResponseEntity response3 = (CommonResponseEntity) msg.obj;
				if (null != response3) {
					if (1 == response3.getSuccess()) {
						showToast("发表评论成功");
						detailInfo.setCOMMENT_NUM((Integer.parseInt(detailInfo.getCOMMENT_NUM())+1)+"");
						tv_comment_num.setText(detailInfo.getCOMMENT_NUM());
						UserPreferenceWrapper.addMyCommentCount();
						setResult(RESULT_OK);
						// XXX 需更新评论列表
						getSayDetail();
					} else {
						showToast(response3.getErrormsg());
					}
				}
				break;
			case IResult.COMMENT_OTHER:
				destroyDialog();
				// 弹出软键盘
				InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
				String name = (String) msg.obj;
				et_comment.requestFocus();
				et_comment.setHint("@" + name);
				refer_commentid = msg.arg1 + "";
				// commentShuoshuo(msg.arg1+"");
				break;
			case IResult.DELETE_COMMENT_INNER:
				commentid = msg.arg1 + "";
				delelteComment(commentid);
				break;
			case IResult.DELETE_COMMENT:
				destroyDialog();
				CommonResponseEntity response4 = (CommonResponseEntity) msg.obj;
				if (null != response4) {
					if (1 == response4.getSuccess()) {
						showToast("删除评论成功");
						setResult(RESULT_OK);
						UserPreferenceWrapper.subMyCommentCount();
						// 需更新评论列表
						updateCommnetList();
						detailInfo.setCOMMENT_NUM((Integer.parseInt(detailInfo.getCOMMENT_NUM())-1)+"");
					} else {
						showToast(response4.getErrormsg());
					}
				}
				break;
			case IResult.NET_ERROR:
				destroyDialog();
				showToast(getString(R.string.net_error));
				break;
			case IResult.DATA_ERROR:
				destroyDialog();
				showToast("数据出错！");
				break;
			case ShareSdkUtil.onError:
				destroyDialog();
				showToast("分享失败");
				break;
			case ShareSdkUtil.onComplete:
				destroyDialog();
				showToast("分享成功");
				setShare(msg.arg1);
				break;
			case ShareSdkUtil.onCancel:
				destroyDialog();
				showToast("取消分享");
				break;
			case IResult.SET_SHARE:
				destroyDialog();
				CommonResponseEntity result = (CommonResponseEntity)msg.obj;
				if(1 == result.getSuccess()){
//					String num = tv_share_num.getText().toString();
					detailInfo.setSHARE_COUNT((Integer.parseInt(detailInfo.getSHARE_COUNT())+1)+"");
					tv_share_num.setText(detailInfo.getSHARE_COUNT());
					UserPreferenceWrapper.addMyShareCount();
				}
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void scrollToBottom(final View scroll, final View inner) {

		handler.post(new Runnable() {
			public void run() {
				if (scroll == null || inner == null) {
					return;
				}

				int offset = inner.getMeasuredHeight() - scroll.getHeight();
				if (offset < 0) {
					offset = 0;
				}

				scroll.scrollTo(0, offset);
			}
		});
	}

	/**
	 * 更新评论列表（删除评论后，本地更新列表，不用重新请求服务器）
	 */
	private void updateCommnetList() {

		for (int i = 0; i < commentList.size(); i++) {
			if (commentList.get(i).getCOMMENTID().equals(commentid)) {
				commentList.remove(i);
				break;
			}
		}
		adapter.change(commentList);

		if (commentid.equals(refer_commentid)) {
			refer_commentid = null;

			if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
				et_comment.setHint("请输入要发表评论的内容");
			}
		}
		commentid = null;
	}

	private void updateView(BaikeSaysInfo info) {

		if (!TextUtils.isEmpty(info.getTITLE())) {
			tv_title.setVisibility(View.VISIBLE);
			tv_title.setText(info.getTITLE());
		} else {
			tv_title.setVisibility(View.GONE);
		}

		tv_time.setText(info.getCREATE_TIME());
		tv_author.setText(info.getREALNAME());
		tv_read_num.setText(info.getHITS());
		tv_praise_num.setText(info.getPRAISE_NUM());
		tv_share_num.setText(info.getSHARE_COUNT());
		tv_comment_num.setText("共" + info.getCOMMENT_NUM() + "条评论");

		if(info.getIS_PRAISED()){
			btn_praise.setBackgroundResource(R.drawable.btn_already_praise);
		} else {
			btn_praise.setBackgroundResource(R.drawable.btn_praise);
		}
	}

	public void doClick(View view) {
		Intent intent = null;
		switch (view.getId()) {
		case R.id.btn_back:
	        intent = new Intent();
	        intent.putExtra("result", detailInfo);
	        setResult(RESULT_OK, intent);
			finish();
			break;
		case R.id.btn_praise:
			if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)){
				intent = new Intent(this, LoginActivity.class);
				intent.putExtra("origin", SayDetailsActivity.class.getSimpleName());
				startActivity(intent);
			} else {
				givePraise();
			}
			break;
		case R.id.btn_give:
			if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)){
				intent = new Intent(this, LoginActivity.class);
				intent.putExtra("origin", SayDetailsActivity.class.getSimpleName());
			} else {
				intent = new Intent(this, RewardActivity.class);
				intent.putExtra("avatar", detailInfo.getAVATAR());
				intent.putExtra("real_name", detailInfo.getREALNAME());
				intent.putExtra("professional", detailInfo.getPROFESSIONAL());
				intent.putExtra("doctor_id", detailInfo.getDOCTORID());
				intent.putExtra("says_id", says_id);
				intent.putExtra("bounty_num", detailInfo.getTOTAL_BOUNTY_NUM());
			}
			startActivity(intent);
			break;
		case R.id.btn_send:
			if(TextUtils.isEmpty(SharePrefUtil.getString(Conast.MEMBER_ID)) || !SharePrefUtil.getBoolean(Conast.LOGIN)){
				intent = new Intent(this, LoginActivity.class);
				intent.putExtra("origin", SayDetailsActivity.class.getSimpleName());
				startActivity(intent);
			} else {
				if (TextUtils.isEmpty(et_comment.getText().toString().trim())) {
					showToast("请输入要发表评论的内容");
				} else {
					if (cb_annoymous.isChecked()) {
						commentShuoshuo(refer_commentid, "1");
					} else {
						commentShuoshuo(refer_commentid, "0");
					}
					refer_commentid = null;
					InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
					imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					et_comment.setText("");
					et_comment.setHint("请输入要发表评论的内容");
					tv_time.requestFocus();
				}
			}
			break;
		case R.id.iv_right_fun:
			if (detailInfo != null) {
				String name = detailInfo.getREALNAME();
				String text = null;
				String pic = null;
				String title = "薏米网-" + name + "的说说";
				if (!TextUtils.isEmpty(detailInfo.getTITLE())) {
					text = "来自薏米网" + name + "医生的原创--" + detailInfo.getTITLE();
				} else {
					if (detailInfo.getCONTENT().length() > 10) {
						text = "来自薏米网" + name + "医生的原创--" + detailInfo.getCONTENT().substring(0, 10);
					} else {
						text = "来自薏米网" + name + "医生的原创--" + detailInfo.getCONTENT();
					}
				}

				if (pics_thumb != null && pics_thumb.size() > 0) {
					pic = pics_thumb.get(0);
				}

				if (null == share) {
					share = new ShareSdkUtil(this, handler);
				}
				share.initModePopupWindow(fl_topLayout, title, text, detailInfo.getSHARE_URL(), pic);
			}
			break;
		default:
			break;
		}
	}

	private class InnerReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(MyApplication.WECHAT_SHARE_SUCCESS)) {
				int type = getIntent().getIntExtra("share_type", ShareSdkUtil.SHARE_TO_WECHAT);
				setShare(type);
			}
		}
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.set_comment);
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.set_praise);
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_says_detail);
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.delete_comment);
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.set_share);
		
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
	        Intent intent = new Intent();
	        intent.putExtra("result", detailInfo);
	        setResult(RESULT_OK, intent);
	        finish();
			break;
		default:
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
