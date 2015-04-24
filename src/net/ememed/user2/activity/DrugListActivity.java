package net.ememed.user2.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.DrugAdapter;
import net.ememed.user2.entity.Drug;
import net.ememed.user2.entity.DrugBiz;
import net.ememed.user2.entity.DrugClass;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.UICore;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;

import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

//药品列表
public class DrugListActivity extends BasicActivity implements BasicUIEvent{
	private TextView tvTilte;
	private RefreshListView lvDrugs;
	private DrugAdapter adapter;
	private int curPage = 1;
	private int pages = 1;
	private boolean refresh = true;
	private DrugClass drugClass;

	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		drugClass = (DrugClass) getIntent().getSerializableExtra("drugClass");
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.u_drug_list);
	}

	@Override
	protected void setupView() {
		tvTilte = (TextView) findViewById(R.id.top_title);
		tvTilte.setText(getString(R.string.drag_consult_title));
		lvDrugs = (RefreshListView) findViewById(R.id.lv_drug);
		adapter = new DrugAdapter(DrugListActivity.this, null, lvDrugs);
		lvDrugs.setAdapter(adapter);
		if (drugClass != null) {
			tvTilte.setText(drugClass.getTitle());
		}
		super.setupView();
	}
	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		}
	}
	@Override
	protected void getData() {
		super.getData();
		UICore.eventTask(this, this, IResult.RESULT, "", null);
	}
	
	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		switch (mes) {
		case IResult.RESULT:
			getDrugList();
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void addListener() {
		lvDrugs.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {

				Intent intent = new Intent(DrugListActivity.this,
						DrugInfoActivity.class);
				intent.putExtra("id",
						((Drug) adapter.getItem(position)).getId());
				startActivity(intent);

			}
		});
		lvDrugs.setOnRefreshListener(new IOnRefreshListener() {

			@Override
			public void OnRefresh() {
				refresh();

			}
		});
		lvDrugs.setOnLoadMoreListener(new IOnLoadMoreListener() {

			@Override
			public void OnLoadMore() {
				loadMore();

			}
		});
		super.addListener();
	}

	private void getDrugList() {
		try {
			HashMap<String, Object> map = Get_Drug_List(drugClass.getScId(), curPage);
			sendMessage(IResult.RESULT, map);
		} catch (IOException e) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		} catch (ParserConfigurationException e) {
			handler.sendEmptyMessage(IResult.DATA_ERROR);
		} catch (SAXException e) {
			handler.sendEmptyMessage(IResult.DATA_ERROR);
		}
	}
	// 获取药品列表
	public static HashMap<String, Object> Get_Drug_List(String classid, int page)
			throws IOException, ParserConfigurationException, SAXException {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("SCID", classid));
		params.add(new BasicNameValuePair("CurrentPage", page + ""));

		InputStream in = HttpUtil.getInputStream("http://121.8.142.242:6677/android/Service.asmx/GetProductsByClass",
						params, HttpUtil.POST);
		HashMap<String, Object> map = DrugBiz.getDrugs(in);
		if (in != null) {
			in.close();
		}
		return map;
	}
	private void refresh() {
		refresh = true;
		curPage = 1;
		UICore.eventTask(this, this, IResult.RESULT, null, null);
	}

	private void loadMore() {
		refresh = false;
		curPage++;
		UICore.eventTask(this, this, IResult.RESULT, null, null);
	}

	@Override
	protected void onResult(Message msg) {
		lvDrugs.onRefreshComplete();
		try {
			switch (msg.what) {
			case IResult.RESULT:
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				if (null != map) {
					int success = (Integer) map.get("success");
					if (success == 1) {

						List<Drug> drugs = (List<Drug>) map.get("drugs");
						pages = (Integer) map.get("pages");
						if (refresh) {
							adapter.change(drugs);
						} else {
							adapter.add(drugs);
						}
						if (curPage < pages) {
							lvDrugs.onLoadMoreComplete(false);
						} else {
							lvDrugs.onLoadMoreComplete(true);
						}
					} else {
						showToast((String) map.get("errormsg"));
					}
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
			e.printStackTrace();
		}

		super.onResult(msg);
	}
}
