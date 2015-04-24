package net.ememed.user2.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.ProblemAdapter;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.Problem;
import net.ememed.user2.entity.ProblemInfo;
import net.ememed.user2.entity.ResultInfo;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

import org.json.JSONException;

import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class ProblemActivity extends BasicActivity implements OnRefreshListener{
	private RefreshListView lvProblem;
	private ProblemAdapter adapter;
	private static int currentPage = 1;
	private int type = 0;
	private int totalPage;
	private boolean refresh = true;
	private PullToRefreshLayout mPullToRefreshLayout;
	private int page = 1;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;
	
	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.problem_list);
	}

	@Override
	protected void setupView() {
		mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.ptr_layout);
		ActionBarPullToRefresh.from(this).allChildrenArePullable().listener(this)
				.setup(mPullToRefreshLayout);
		
		ll_net_unavailable = (LinearLayout)findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout)findViewById(R.id.ll_empty);
		
		TextView tvtitle = (TextView) findViewById(R.id.top_title);
		tvtitle.setText(getString(R.string.lt_problem));
		lvProblem = (RefreshListView) findViewById(R.id.lv_problem);
		adapter = new ProblemAdapter(this, null);
		lvProblem.setAdapter(adapter);
		getProblemList(page);
	}
	
	public void getProblemList(int page){
		mPullToRefreshLayout.setRefreshing(true);
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("page", page+"");
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_faq_list,
				ProblemInfo.class,params, new Response.Listener() {
					@Override
					public void onResponse(Object response) {

						Message message = new Message();
						message.obj = response;
						message.what = IResult.PROBLEM_LIST;
						handler.sendMessage(message);
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {

						Message message = new Message();
						message.obj = error.getMessage();
						message.what = IResult.NET_ERROR;
						handler.sendMessage(message);
					}
				});
	}

	@Override
	protected void addListener() {
		lvProblem.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Intent intent = new Intent(ProblemActivity.this,ProblemInfoActivity.class);
				Problem problem = (Problem) adapter.getItem(position);
				intent.putExtra("problem", problem);
				startActivity(intent);
				
			}
		});
		lvProblem.setOnRefreshListener(new IOnRefreshListener() {
			
			@Override
			public void OnRefresh() {
				refresh();
				
			}
		});
		lvProblem.setOnLoadMoreListener(new IOnLoadMoreListener() {
			
			@Override
			public void OnLoadMore() {
				loadMore();
			}
		});
	}
	
	
	private void refresh(){
		refresh = true;
		currentPage=1;
		getProblemList(currentPage);
	}
	
	private void loadMore(){
		refresh = false;
		currentPage++;
		getProblemList(currentPage);
	}
	
	@Override
	public void onRefreshStarted(View view) {
		refresh();
	}
	@Override
	protected void onResult(Message msg) {
		mPullToRefreshLayout.setRefreshComplete();
		lvProblem.onRefreshComplete();
		if(currentPage<totalPage){
			lvProblem.onLoadMoreComplete(false);
		} else {
			lvProblem.onLoadMoreComplete(true);
		}
		try {
			switch (msg.what) {
			case IResult.PROBLEM_LIST:
				ProblemInfo problems = (ProblemInfo) msg.obj;
				if (problems.getSuccess() == 1) {
					if(refresh){
						if (null != problems.getData() && problems.getData().size()>0) {
							ll_empty.setVisibility(View.GONE);
							lvProblem.setVisibility(View.VISIBLE);
							adapter.change(problems.getData());
						} else {
							ll_empty.setVisibility(View.VISIBLE);
							lvProblem.setVisibility(View.GONE);			
						}
					} else {
						adapter.add(problems.getData());
					}	
				} else {
					ll_empty.setVisibility(View.VISIBLE);
					lvProblem.setVisibility(View.GONE);
				}
				
				break;
			case IResult.PAGE_COUNT:
				totalPage = msg.arg1;
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}
	public void doClick(View view){
		if (view.getId() == R.id.btn_back) {
			finish();
		}
	}
}
