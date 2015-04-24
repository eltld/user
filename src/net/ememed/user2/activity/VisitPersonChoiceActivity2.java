package net.ememed.user2.activity;

import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.CuredmemeberList;
import net.ememed.user2.entity.CuredmemeberListEntry;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.ScheduleDetailEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.Util;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.VisitPersonAddEvent;

/**
 * 选择就诊人
 * 
 * @author pch
 * 
 */
public class VisitPersonChoiceActivity2 extends BasicActivity {

	private static final String FORMAT_SEX = "性    别：%s";
	private static final String FORMAT_IDENTITY = "身份证：%s";
	private static final String FORMAT_MOBILE = "手机号：%s";

	private ListView mListView;
	private QuickAdapter<CuredmemeberListEntry> mAdapter;

	private GuahaoDoctotItem guahao_doctot;
	private ScheduleDetailEntry scheduleDetailEntry;
	private View mAddView;
	private boolean isflag;

	public static final int REQUEST_ADD = 1;
	public static final int REQUEST_EDIT = 2;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_visit_person_choice2);
		EventBus.getDefault().registerSticky(this, VisitPersonAddEvent.class);
		scheduleDetailEntry = (ScheduleDetailEntry) getIntent().getSerializableExtra(
				"schedule_detail");
		guahao_doctot = (GuahaoDoctotItem) getIntent().getSerializableExtra("guahao_doctot");
		isflag = getIntent().getBooleanExtra("isflag", true);
		EventBus.getDefault().register(this, GuahaoSuccessEvent.class);
	}

	public void onEvent(GuahaoSuccessEvent event) {
		finish();
	}

	@Override
	protected void getData() {
		queryVisitPerson();
		super.getData();
	}

	@Override
	protected void setupView() {
		mAddView = findViewById(R.id.btn_add_person);
		mAddView.setClickable(false);

		mListView = (ListView) findViewById(R.id.listView);
		mAdapter = new QuickAdapter<CuredmemeberListEntry>(this, R.layout.list_item_visit_person) {

			@Override
			protected void convert(final BaseAdapterHelper helper, CuredmemeberListEntry item) {
				helper.setText(R.id.tv_name, item.getREALNAME())
						.setText(R.id.tv_sex, String.format(FORMAT_SEX, item.getSex()))
						.setText(R.id.tv_identity, String.format(FORMAT_IDENTITY, item.getCARDID()))
						.setText(R.id.tv_mobile, String.format(FORMAT_MOBILE, item.getMOBILE()));
				helper.getView(R.id.iv_edit).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						editVisitPerson(helper.getPosition());
					}
				});
			}
		};
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if (!isflag)
					return;
				Intent intent = new Intent(VisitPersonChoiceActivity2.this,
						ReserveInfoActivity.class);
				intent.putExtra("data", mAdapter.getItem(position));
				intent.putExtra("guahao_doctot", guahao_doctot);
				intent.putExtra("schedule_detail", scheduleDetailEntry);
				startActivity(intent);
			}
		});
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.btn_add_person:
			addVisitPerson();
			break;
		default:
			break;
		}
	}

	/**
	 * 添加就诊人
	 */
	private void addVisitPerson() {
		if (mAdapter.getCount() >= 5) {
			showToast("仅支持添加5个就诊人信息");
			return;
		}
		Intent intent = new Intent(this, VisitPersonAddActivity2.class);
		intent.putExtra("state", VisitPersonAddActivity2.STATE_ADD);
		intent.putExtra("guahao_doctot", guahao_doctot);
		intent.putExtra("schedule_detail", scheduleDetailEntry);
		startActivityForResult(intent, REQUEST_ADD);
	}

	/**
	 * 编辑就诊人
	 * 
	 * @param position
	 */
	private void editVisitPerson(int position) {
		Intent intent = new Intent(VisitPersonChoiceActivity2.this, VisitPersonEditActivity.class);
		intent.putExtra("data", mAdapter.getItem(position));
		intent.putExtra("guahao_doctot", guahao_doctot);
		intent.putExtra("schedule_detail", scheduleDetailEntry);
		startActivity(intent);
	}

	/**
	 * 查询就诊人
	 */
	private void queryVisitPerson() {
		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));

			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_curedmember_lists,
					CuredmemeberList.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.PATIENT_LIST;
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
		destroyDialog();
		try {
			switch (msg.what) {
			case IResult.PATIENT_LIST:
				CuredmemeberList curedmemeberList = (CuredmemeberList) msg.obj;
				if (null != curedmemeberList) {
					if (curedmemeberList.getSuccess() == 1) {
						List<CuredmemeberListEntry> data = curedmemeberList.getData();
						if (null != data) {
							mAdapter.clear();
							mAdapter.addAll(data);
							Util.setListViewHeightBasedOnChildren(mListView);
						}
						mAddView.setClickable(true);
					} else {
						showToast(curedmemeberList.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				break;
			default:
				break;
			}

		} catch (Exception e) {
		}
		super.onResult(msg);
	}

	public void onEvent(VisitPersonAddEvent testEvent) {
		queryVisitPerson();
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(VisitPersonAddEvent.class);
	}

	@Override
	protected void onDestroy() {
		EventBus.getDefault().removeStickyEvent(GuahaoSuccessEvent.class);
		super.onDestroy();
		MyApplication.volleyHttpClient.cancelRequest(HttpUtil.get_curedmember_lists);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		switch (requestCode) {
		case REQUEST_ADD:
			queryVisitPerson();
			break;

		default:
			break;
		}
	}
}
