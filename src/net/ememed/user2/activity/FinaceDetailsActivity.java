package net.ememed.user2.activity;

import java.util.HashMap;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.entity.UserBalance;
import net.ememed.user2.entity.UserBalanceEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class FinaceDetailsActivity extends BasicActivity implements OnRefreshListener{
	private TextView top_title;
	private TextView my_money;
	
	private PullToRefreshLayout mPullToRefreshLayout;
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.finance_details);
	}

	@Override
	protected void setupView() {
		top_title = (TextView)findViewById(R.id.top_title);
		top_title.setText(getString(R.string.finance_details));
		my_money = (TextView)findViewById(R.id.my_money);
		
		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this).setup(mPullToRefreshLayout);
		getUserBalance();
		
		super.setupView();
		
	}

	private void getUserBalance() {
		if (NetWorkUtils.detect(this)) {
			mPullToRefreshLayout.setRefreshing(true);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_user_balance,
					UserBalance.class ,params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.USER_BALANCE;
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
	
	
	public void doClick(View view) {
		if(view.getId() == R.id.btn_back){
			finish();
		}
//		else if (view.getId() == R.id.btn_tijiao){
//			Intent intent = new Intent(FinaceDetailsActivity.this,MoneyApplyActivity.class);
//			startActivity(intent);
//		}
	}
	
	@Override
	protected void onResult(Message msg) {
		try {
			switch (msg.what) {
			case IResult.USER_BALANCE:
				mPullToRefreshLayout.setRefreshComplete();
				UserBalance ub = (UserBalance) msg.obj;
				if(null != ub){
					if(ub.getSuccess() == 1){
						UserBalanceEntry data = ub.getData();
//						SharePrefUtil.putString(Conast.MONEY, data.getMoney() + "");
//						SharePrefUtil.commit();
						my_money.setText("￥"  + data.getMoney());
					}else {
						showToast(ub.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				showToast(IMessage.DATA_ERROR);
				break;
			default:
				break;
			}
		} catch (Exception e) {

		}
		super.onResult(msg);
	}

	@Override
	public void onRefreshStarted(View view) {
		// TODO Auto-generated method stub
		
	}
}
