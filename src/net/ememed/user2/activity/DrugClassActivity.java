package net.ememed.user2.activity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.DrugClassAdapter;
import net.ememed.user2.entity.DrugClass;
import net.ememed.user2.entity.DrugClassBiz;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.BasicUIEvent;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.UICore;
import net.ememed.user2.widget.RefreshListView;

import org.apache.http.message.BasicNameValuePair;
import org.xml.sax.SAXException;

import com.umeng.analytics.MobclickAgent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

//药品分类
public class DrugClassActivity extends BasicActivity implements BasicUIEvent{
	private DrugClass drugClass;
	private TextView tvTitle;
	private RefreshListView lvDrugClass;
	private DrugClassAdapter adapter;
	private LinearLayout ll_net_unavailable;
	private LinearLayout ll_empty;
	private TextView tvTilte;
	@Override
	protected void onBeforeCreate(Bundle savedInstanceState) {
		super.onBeforeCreate(savedInstanceState);
		drugClass = (DrugClass) getIntent().getSerializableExtra("drugClass");
	}

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.u_drug_class);
	}

	@Override
	protected void setupView() {
		tvTilte = (TextView) findViewById(R.id.top_title);
		tvTilte.setText(getString(R.string.drag_consult_info));
		ll_net_unavailable = (LinearLayout)findViewById(R.id.ll_net_unavailable);
		ll_empty = (LinearLayout)findViewById(R.id.ll_empty);
		lvDrugClass = (RefreshListView) findViewById(R.id.lv_drug_class);
		adapter = new DrugClassAdapter(this, null);
		lvDrugClass.setAdapter(adapter);
		super.setupView();
	}

	@Override
	protected void getData() {
		if (NetWorkUtils.detect(DrugClassActivity.this)) {
			
			UICore.eventTask(this, this, IResult.RESULT, "", null);
		
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
		super.getData();
	}

	@Override
	public void execute(int mes, Object obj) {
		super.execute(mes, obj);
		switch (mes) {
		case IResult.RESULT:
			getDrugClass();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void addListener() {
		try {
			lvDrugClass.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					DrugClass drugClass = (DrugClass) adapter.getItem(position);
					Intent intent = null;
					if (drugClass.getExistSub() == 0) {
						intent = new Intent(DrugClassActivity.this,DrugListActivity.class);
					} else {
						intent = new Intent(DrugClassActivity.this,DrugClassActivity.class);
					}
					if (intent != null) {
						intent.putExtra("drugClass", drugClass);
						startActivity(intent);
					}

				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		super.addListener();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.iv_net_failed) {
			getDrugClass();
		}
	}
	private void getDrugClass() {
		try {
			HashMap<String, Object> map = Get_drug_class(drugClass == null ? "0" : drugClass.getScId());
			sendMessage(IResult.RESULT, map);
		} catch (IOException e) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		} catch (ParserConfigurationException e) {
			handler.sendEmptyMessage(IResult.DATA_ERROR);
		} catch (SAXException e) {
			handler.sendEmptyMessage(IResult.DATA_ERROR);
		} catch (Exception e) {
			handler.sendEmptyMessage(IResult.DATA_ERROR);
		}
	}

	// 获取药品分类
	public static HashMap<String, Object> Get_drug_class(String classid)
			throws IOException, ParserConfigurationException, SAXException {
		ArrayList<BasicNameValuePair> params = new ArrayList<BasicNameValuePair>();
		params.add(new BasicNameValuePair("ParentID", classid));

		InputStream in = HttpUtil.getInputStream("http://121.8.142.242:6677/android/Service.asmx/GetProductsClass",
						params, HttpUtil.POST);
		HashMap<String, Object> map = DrugClassBiz.getDrugClass(in);
		if (in != null) {
			in.close();
		}
		return map;
	}

	@Override
	protected void onResult(Message msg) {
		try {
			switch (msg.what) {
			case IResult.RESULT:
				HashMap<String, Object> map = (HashMap<String, Object>) msg.obj;
				if (null != map) {
					int success = (Integer) map.get("success");
					if (success == 1) {
						ll_net_unavailable.setVisibility(View.GONE);
						lvDrugClass.setVisibility(View.VISIBLE);
						List<DrugClass> drugClasses = (List<DrugClass>) map.get("drugClasses");
						if (null != drugClasses && drugClasses.size() > 0) {
							adapter.change(drugClasses);
						} else {
							ll_empty.setVisibility(View.VISIBLE);
							lvDrugClass.setVisibility(View.GONE);
						}
						
					} else {
						showToast((String) map.get("errormsg"));
					}
				}
				break;
			case IResult.NET_ERROR:
				ll_net_unavailable.setVisibility(View.VISIBLE);
				ll_empty.setVisibility(View.GONE);
				lvDrugClass.setVisibility(View.GONE);
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
