package net.ememed.user2.activity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddCurdcardInfo;
import net.ememed.user2.entity.AddCuredmember;
import net.ememed.user2.entity.CurdcardList;
import net.ememed.user2.entity.CurdcardListEntry;
import net.ememed.user2.entity.CuredmemeberListEntry;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.GuahaoSuccessEvent;
import net.ememed.user2.entity.GuahaoSuccessInfo;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.ScheduleDetailEntry;
import net.ememed.user2.entity.UserGuahao;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.ColoredRatingBar;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.VisitPersonAddEvent;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReserveInfoActivity extends BasicActivity {
	private CuredmemeberListEntry data;
	private GuahaoDoctotItem guahao_doctot;
	private TextView et_name, et_sex, et_phone, et_shenfenzheng, et_curdcard, et_reserve_hospital, et_reserve_kesi, et_reserve_doctor_name, et_reserve_time, et_money;
	private ScheduleDetailEntry schedule_detail;
	private boolean needcard = false;
	private TextView tv_hosp_name;
	private EditText et_card;
	private String cardId;
	private EditText et_diseases_desc;
	private EditText et_hospital_num;
	private boolean isGuahao = false;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_reserve_info);
		data = (CuredmemeberListEntry) getIntent().getSerializableExtra("data");
		guahao_doctot = (GuahaoDoctotItem) getIntent().getSerializableExtra("guahao_doctot");
		schedule_detail = (ScheduleDetailEntry) getIntent().getSerializableExtra("schedule_detail");
	}

	@Override
	protected void setupView() {
		TextView top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("预约信息");
		et_name = (TextView) findViewById(R.id.et_name);
		et_sex = (TextView) findViewById(R.id.et_sex);
		et_phone = (TextView) findViewById(R.id.et_phone);
		et_shenfenzheng = (TextView) findViewById(R.id.et_shenfenzheng);
		et_curdcard = (TextView) findViewById(R.id.et_curdcard);
		et_reserve_hospital = (TextView) findViewById(R.id.et_reserve_hospital);
		et_reserve_kesi = (TextView) findViewById(R.id.et_reserve_kesi);
		et_reserve_doctor_name = (TextView) findViewById(R.id.et_reserve_doctor_name);
		et_reserve_time = (TextView) findViewById(R.id.et_reserve_time);
		et_diseases_desc = (EditText) findViewById(R.id.et_diseases_desc);
		et_hospital_num = (EditText) findViewById(R.id.et_hospital_num);
		et_money = (TextView) findViewById(R.id.et_money);

		et_name.setText(data.getREALNAME());
		et_sex.setText(data.getSEX().equals("1") ? "男" : "女");
		et_phone.setText(data.getMOBILE());
		et_shenfenzheng.setText(data.getCARDID());
		// et_curdcard.setText(data.getCurdcard_list().get(data.getCurdcard_list().size()
		// - 1).getCURDCARDID());
		// for (int i = 0; i < data.getCurdcard_list().size(); i++) {
		// CurdcardListEntry curdcardListEntry = data.getCurdcard_list().get(i);
		// }
		if ("1".equals(guahao_doctot.getNEEDCARD())) {
			needcard = true;
		}
		List<CurdcardListEntry> curdcard_list = data.getCurdcard_list();
		if (curdcard_list != null && curdcard_list.size() > 0)
			for (CurdcardListEntry curdcardListEntry : curdcard_list) {
				String hospital = curdcardListEntry.getHOSPITAL();
				if (guahao_doctot.getHOSPITALNAME().equals(hospital)) {
					et_curdcard.setText(curdcardListEntry.getCURDCARDID());
				}
			}
		cardId = et_curdcard.getText().toString();
		et_reserve_hospital.setText(guahao_doctot.getHOSPITALNAME());
		et_reserve_kesi.setText(guahao_doctot.getROOMNAME());
		et_reserve_doctor_name.setText(guahao_doctot.getDOCTORNAME());
		et_reserve_time.setText(schedule_detail.getSchedule_Date() +" "+schedule_detail.getAppoint_Period());
		et_money.setText(schedule_detail.getFee() + "元");
		super.setupView();
	}

	@Override
	protected void getData() {
		super.getData();
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.btn_reservation:
				reservaInfo();
			break;
		case R.id.ll_manxing:
			isGuahao = true;
			multiChoice();
			break;
		case R.id.ll_curdcard:
			isGuahao = false;
			addCard();
			break;
		default:
			break;
		}
	}

	// 提交挂号
	private void reservaInfo() {
		if (needcard) {// 需要诊疗卡
			addCard();
		} else {// 不需要诊疗卡
			userGuahao();
		}
//		View mModeView = getLayoutInflater().inflate(R.layout.dialog_reserva_info, null);
		// AlertDialog.Builder builder = new AlertDialog.Builder(this);
		// builder.setView(mModeView).setNegativeButton("取消",
		// null).setPositiveButton("确定", new OnClickListener() {
		//
		// @Override
		// public void onClick(DialogInterface dialog, int which) {
		// userGuahao();
		// }
		// }).show();
	}

	private void userGuahao() {
		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("doctorid", guahao_doctot.getDOCTORID());
			params.put("resource_code", schedule_detail.getResource_Code());
			params.put("date", schedule_detail.getSchedule_Date());
			int date = 0;
			if ("上午".equals(schedule_detail.getAm_Pm())) {
				date = 1;
			} else if ("下午".equals(schedule_detail.getAm_Pm())) {
				date = 2;
			} else {
				date = 3;
			}

			params.put("am_pm", date + "");
			params.put("patientname", data.getREALNAME());
			params.put("curedmember_id", data.getID());
			params.put("cardid", data.getCARDID());
			params.put("mobile", data.getMOBILE());
			params.put("hospitalname", guahao_doctot.getHOSPITALNAME());
			params.put("roomname", guahao_doctot.getROOMNAME());
			params.put("birthday", data.getBIRTHDAY());
			params.put("sex", data.getSEX());
			String card = "";
			if(needcard){
				card = cardId;
			}
			params.put("cardno", card);
			params.put("pay_fee", schedule_detail.getFee());
			params.put("diseases_desc", et_diseases_desc.getText().toString());
			params.put("chronic_no", et_hospital_num.getText().toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.user_guahao, GuahaoSuccessInfo.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = handler.obtainMessage();
					message.obj = response;
					message.what = IResult.USER_GUAHAO;
					handler.sendMessage(message);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Message message = handler.obtainMessage();
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
		switch (msg.what) {
		case IResult.USER_GUAHAO:
			GuahaoSuccessInfo userGuahao = (GuahaoSuccessInfo) msg.obj;
			if (null != userGuahao) {
				if (userGuahao.getSuccess() == 1) {
					showToast("预约成功");
					Intent i = new Intent(this, GuahaoSuccessActivity.class);
					i.putExtra("data", userGuahao.getData());
					i.putExtra("time", et_reserve_time.getText().toString());
					startActivity(i);
					EventBus.getDefault().postSticky(new GuahaoSuccessEvent());
					finish();
				} else {
					if(userGuahao.getErrormsg().contains("卫生局")){		//屏蔽卫生局敏感词
						Toast.makeText(this, "预约失败", 1).show();
					} else {
						Toast.makeText(this, userGuahao.getErrormsg(), 1).show();
					}
				}
			} else {
				showToast(IMessage.SERVICE_ERROR);
			}
			break;

		case IResult.NET_ERROR:
			showToast(IMessage.NET_ERROR);
			break;
		case IResult.MEDICAL_CARD_ADD:
			AddCurdcardInfo addCurdcard = (AddCurdcardInfo) msg.obj;
			if (null != addCurdcard) {
				switch (addCurdcard.getSuccess()) {
				case 1:
					if(isGuahao)
						userGuahao();
					else
						et_curdcard.setText(cardId);
					break;

				default:
					Toast.makeText(this, addCurdcard.getErrormsg(), 1).show();
					break;
				}
			} else {
				showToast(IMessage.SERVICE_ERROR);
			}
			break;
		case IResult.DATA_ERROR:
			showToast(IMessage.SERVICE_ERROR);
			break;

		default:
			break;
		}

	}

	private void addCard() {
		// 定义对话框的构建器
		Builder builder = new Builder(this);
		// AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.view_card, null);
		final AlertDialog dialog = builder.create();
		tv_hosp_name = (TextView) view.findViewById(R.id.tv_hosp_name);
		tv_hosp_name.setText(guahao_doctot.getHOSPITALNAME());
		et_card = (EditText) view.findViewById(R.id.et_card);
		Button bt_ok = (Button) view.findViewById(R.id.btn_ok);
		bt_ok.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String trim = et_card.getText().toString().trim();
				if (TextUtils.isEmpty(trim))
					showToast("诊疗卡号不能为空");
				else {
					AddCurdcard(trim);
					dialog.dismiss();
				}

			}
		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private void AddCurdcard(String curdcardid) {
		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("hospital", guahao_doctot.getHOSPITALNAME());
			params.put("curdcardid", curdcardid);
			this.cardId = curdcardid;
			params.put("curedmember_id", data.getID());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.MEDICAL_CARD_ADD, AddCurdcardInfo.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = handler.obtainMessage();
					message.obj = response;
					message.what = IResult.MEDICAL_CARD_ADD;
					handler.sendMessage(message);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Message message = handler.obtainMessage();
					message.obj = error.getMessage();
					message.what = IResult.DATA_ERROR;
					handler.sendMessage(message);
				}
			});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	public void multiChoice() {
		final String[] items = { "心血管内科", "心脏外科", "神经内科", "内分泌科", "风湿内科", "儿风湿内科", "消化内科", "肾内科", "儿科肾专科", "呼吸内科" };

		OnClickListener listener = new OnClickListener() {
			private int selected;

			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					et_diseases_desc.setText(items[selected]);
					// Toast.makeText(getApplicationContext(), items[selected],
					// Toast.LENGTH_SHORT).show();
					break;
				default:
					selected = which;
				}
			}
		};

		new Builder(this).setTitle("请选择慢性病类型").setSingleChoiceItems(items, -1, listener).setPositiveButton("确定", listener).show();
	}

}
