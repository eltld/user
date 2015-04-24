package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.dialog.Effectstype;
import net.ememed.user2.dialog.NiftyDialogBuilder;
import net.ememed.user2.entity.AddCurdcardInfo;
import net.ememed.user2.entity.AddCuredmember;
import net.ememed.user2.entity.CurdcardList;
import net.ememed.user2.entity.CurdcardListEntry;
import net.ememed.user2.entity.CuredmemeberListEntry;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.IDCard;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.widget.RefreshListView;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;
import de.greenrobot.event.VisitPersonAddEvent;

public class VisitPersonAddActivity extends BasicActivity {

	private static final String UPDATE = "1";
	private static final String ADD = "2";
	private TextView et_birthday;
	private EditText et_shenfenzheng;
	private EditText et_phone;
	private EditText et_name;
	private EditText et_hospital;
	private EditText et_curdcard;
	private EditText et_address;
	private RadioButton rbtn_man;
	private RadioButton rbtn_woman;
	private RadioGroup rg_sex;
	private Button btn_addhealth;
	private LinearLayout ll_reservation_info, ll_reservation_list;
	private CuredmemeberListEntry data;
	private String update;
	private String add;
	private ReservationAdapter new_adapter;
	private RefreshListView lv_revervation_list;
	private GuahaoDoctotItem guahao_doctot;
	private LinearLayout ll_add_reservation_card;
	private NiftyDialogBuilder dialogBuilder;
	private Effectstype effect;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_visit_person_add);
		dialogBuilder = new NiftyDialogBuilder(this,
				R.style.dialog_untran);
		data = (CuredmemeberListEntry) getIntent().getSerializableExtra("data");
		update = getIntent().getStringExtra("update");
		add = getIntent().getStringExtra("add");
		guahao_doctot = (GuahaoDoctotItem) getIntent().getSerializableExtra("guahao_doctot");
	}

	@Override
	protected void setupView() {
		ll_add_reservation_card = (LinearLayout) findViewById(R.id.ll_add_reservation_card);

		TextView title = (TextView) findViewById(R.id.top_title);
		et_birthday = (TextView) findViewById(R.id.et_birthday);
		et_name = (EditText) findViewById(R.id.et_name);
		et_shenfenzheng = (EditText) findViewById(R.id.et_shenfenzheng);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_hospital = (EditText) findViewById(R.id.et_hospital);
		et_curdcard = (EditText) findViewById(R.id.et_curdcard);
		et_address = (EditText) findViewById(R.id.et_address);
		rbtn_man = (RadioButton) findViewById(R.id.rbtn_man);
		rbtn_woman = (RadioButton) findViewById(R.id.rbtn_woman);
		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		if (!TextUtils.isEmpty(add)) {
			if (add.equals(ADD)) {
				title.setText("添加就诊人");
				ll_reservation_info = (LinearLayout) findViewById(R.id.ll_reservation_info);
				ll_reservation_info.setVisibility(View.VISIBLE);
				if (guahao_doctot != null){
					et_hospital.setFocusable(false);
					et_hospital.setClickable(false);
					et_hospital.setEnabled(false);
					et_hospital.setText(guahao_doctot.getHOSPITALNAME());
				}
				btn_addhealth.setText(getString(R.string.public_save));
				btn_addhealth.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						addCuredmember();
					}
				});
			}

		}
		if (!TextUtils.isEmpty(update)) {
			if (update.equals(UPDATE)) {
				ll_reservation_list = (LinearLayout) findViewById(R.id.ll_reservation_list);
				ll_reservation_list.setVisibility(View.VISIBLE);
				lv_revervation_list = (RefreshListView) findViewById(R.id.lv_revervation_list);
				List<CurdcardListEntry> curdcard_list = data.getCurdcard_list();
				new_adapter = new ReservationAdapter(curdcard_list);
				lv_revervation_list.setAdapter(new_adapter);
				// getCurdcardLists();
				btn_addhealth.setText("确认修改");
				btn_addhealth.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						addCuredmember();
					}
				});
				title.setText("就诊人信息");
				et_birthday.setText(data.getBIRTHDAY());
				et_name.setText(data.getREALNAME());
				et_shenfenzheng.setText(data.getCARDID());
				et_phone.setText(data.getMOBILE());
				et_birthday.setText(data.getBIRTHDAY());
				et_name.setText(data.getREALNAME());
				if ("1".equals(data.getSEX())) {
					rbtn_man.setChecked(true);
					rbtn_woman.setChecked(false);
				} else {
					rbtn_man.setChecked(false);
					rbtn_woman.setChecked(true);
				}
				et_address.setText(data.getADDRESS());
			}
		}
	}

	// 设置生日
	private void setBirthday() {
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
		int year = mycalendar.get(Calendar.YEAR);
		int month = mycalendar.get(Calendar.MONTH);
		int day = mycalendar.get(Calendar.DAY_OF_MONTH);
		DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				et_birthday.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth));
			}
		}, year, month, day);
		dialog.show();
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.btn_addhealth:
			// addCuredmember();
			// addCurdcard();
			break;
		case R.id.ll_birthday:
			setBirthday();
			break;
		case R.id.et_name:
			getEditText("姓名", et_name, et_name.getText().toString().trim());
			break;
		case R.id.et_shenfenzheng:
			getEditText("身份证", et_shenfenzheng, et_shenfenzheng.getText().toString().trim());
			break;
		case R.id.et_phone:
			getEditText("手机号码", et_phone, et_phone.getText().toString());
			break;
		case R.id.et_address:
			getEditText("地址", et_address, et_address.getText().toString());
			break;
		default:
			break;
		}
	}
	
	private void getEditText(final String title, final EditText et, String str){
		effect = Effectstype.Flipv;
		View view2 = LayoutInflater.from(this).inflate(
				R.layout.erh_person_ids, null);
		final EditText idsExit = (EditText) view2.findViewById(R.id.name);
		idsExit.setText(str);
		idsExit.setSelection(et.getText().length());
		
		dialogBuilder
				.withTitle(title)
				.withTitleColor("#000000")
				.withMessage(null)
				.withMessageColor("#000000")
				.withIcon(getResources().getDrawable(R.drawable.medical_history_ememed2))
				.isCancelableOnTouchOutside(true) // def |
				.withDuration(700) // def
				.withEffect(effect) // def Effectstype.Slidetop
				.setCustomView(view2, this).withButton1Text("确定") // def
																	// gone
				.withButton2Text("取消") // def gone
			
				.setButton1Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						
						String idStr = idsExit.getText().toString().trim();
						if (idStr == null || idStr.equals("")) {
							showToast("请填写内容");
							return;
						}
						if (title.equals("身份证")) {
							if (!Utils.IDNumIsValid(idStr)) {
								showToast("身份证号码输入错误");
								return;
							}
							et_birthday.setText(Utils.getBirthday(idStr));
						}
						et.setText(idStr);
						dialogBuilder.dismiss();
					}
				}).setButton2Click(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						dialogBuilder.dismiss();
					}
				}).show();
	}

	private void getCurdcardLists() {
		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_curdcard_lists, CurdcardList.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = handler.obtainMessage();
					message.obj = response;
					message.what = IResult.MEDICAL_CARD_LIST;
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

	private String sex = "1";
	private String etHospital;
	private String etCurdcard;

	private void addCuredmember() {
		String etBirthday = et_birthday.getText().toString().trim();
		String etName = et_name.getText().toString().trim();
		String etShenfenzheng = et_shenfenzheng.getText().toString().trim();
		String etPhone = et_phone.getText().toString().trim();
		String etAddress = et_address.getText().toString().trim();
		etHospital = et_hospital.getText().toString().trim();
		etCurdcard = et_curdcard.getText().toString().trim();
		if (TextUtils.isEmpty(etName) && etName.length() == 0) {
			showToast("姓名必填哦");
			return;
		}
		if (TextUtil.isNotName(etName)) {
			showToast("请输入正确的姓名");
			return;
		}
		if (TextUtils.isEmpty(etShenfenzheng) && etShenfenzheng.length() == 0) {
			showToast("身份证必填哦");
			return;
		}
		// 处理是身份证号的情况
		String hint = IDCard.IDCardValidate(etShenfenzheng);
		if (!hint.equals("YES")) {
			showToast(hint);
			return;
		}
		if (TextUtils.isEmpty(etBirthday)) {
			showToast("生日必填哦");
			return;
		}
		if (TextUtils.isEmpty(etPhone)) {
			showToast("电话号码必填哦");
			return;
		}
		/*if (TextUtils.isEmpty(etAddress)) {
			showToast("地址必填哦");
			return;
		}*/
		if (TextUtils.isEmpty(etHospital)) {
			showToast("所属医院必填哦");
			return;
		}
		// if (TextUtils.isEmpty(etCurdcard)) {
		// showToast("诊疗卡必填哦");
		// return;
		// }
		rg_sex.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if (checkedId == R.id.rbtn_man) {
					sex = "1";
				} else if (checkedId == R.id.rbtn_woman) {
					sex = "0";
				}
			}
		});

		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("realname", etName);
			params.put("sex", sex);
			params.put("cardid", etShenfenzheng);
			params.put("birthday", etBirthday);
			params.put("mobile", etPhone);
			params.put("address", etAddress);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.PATIENT_ADD, AddCuredmember.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = handler.obtainMessage();
					message.obj = response;
					message.what = IResult.PATIENT_ADD;
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

	private void addCurdcard(String curedmember_id) {
		if (NetWorkUtils.detect(this)) {
			// loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("hospital", etHospital);
			params.put("curdcardid", etCurdcard);
			params.put("curedmember_id", curedmember_id);
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

	@Override
	protected void onResult(Message msg) {
		try {
			destroyDialog();
			switch (msg.what) {
			case IResult.PATIENT_ADD:
				AddCuredmember addCuredmember = (AddCuredmember) msg.obj;
				if (null != addCuredmember) {
					if (addCuredmember.getSuccess() == 1) {
						if (!TextUtils.isEmpty(etCurdcard))
							addCurdcard(addCuredmember.getData().getCUREDMEMBER_ID());
						else {
							EventBus.getDefault().postSticky(new VisitPersonAddEvent());
							finish();
						}
					} else {
						showToast(addCuredmember.getErrorMessage());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.MEDICAL_CARD_ADD:
				
				AddCurdcardInfo addCurdcard = (AddCurdcardInfo) msg.obj;
				if (null != addCurdcard) {
					if (addCurdcard.getSuccess() == 1) {
						showToast("添加就诊卡成功");
						EventBus.getDefault().postSticky(new VisitPersonAddEvent());
						finish();
					} else {
						showToast(addCurdcard.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.MEDICAL_CARD_LIST:
				destroyDialog();
				CurdcardList curdcardList = (CurdcardList) msg.obj;
				if (null != curdcardList) {
					if (curdcardList.getSuccess() == 1) {
						new_adapter.change(curdcardList.getData());
					} else {
						showToast(curdcardList.getErrormsg());
					}
				} else {
					showToast(IMessage.DATA_ERROR);
				}
				break;
			case IResult.NET_ERROR:
				destroyDialog();
				showToast(IMessage.NET_ERROR);
				break;
			case IResult.DATA_ERROR:
				destroyDialog();
				break;
			default:
				break;
			}

		} catch (Exception e) {
			// TODO: handle exception
		}
		super.onResult(msg);
	}

	private class ReservationAdapter extends BaseAdapter {
		private List<CurdcardListEntry> listItems;

		public ReservationAdapter(List<CurdcardListEntry> listItems) {
			if (listItems == null) {
				listItems = new ArrayList<CurdcardListEntry>();
			}
			this.listItems = listItems;
		}

		@Override
		public int getCount() {
			return listItems.size();
		}

		@Override
		public Object getItem(int position) {
			return (listItems == null || listItems.size() == 0) ? null : listItems.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if (convertView == null) {
				holder = new ViewHolder();
				convertView = LayoutInflater.from(VisitPersonAddActivity.this).inflate(R.layout.revervation_info_item, null);

				holder.tv_hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
				holder.tv_reserve_card = (TextView) convertView.findViewById(R.id.tv_reserve_card);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_hospital.setText("医院：" + listItems.get(position).getHOSPITAL());
			holder.tv_reserve_card.setText("诊疗卡：" + listItems.get(position).getCURDCARDID());
			if (guahao_doctot != null && listItems.get(position).getHOSPITAL().equals(guahao_doctot.getHOSPITALNAME())) {
				ll_add_reservation_card.setVisibility(View.GONE);
			}
			return convertView;
		}

		public void change(List<CurdcardListEntry> lists) {
			if (lists == null) {
				lists = new ArrayList<CurdcardListEntry>();
			}
			this.listItems = lists;
			notifyDataSetChanged();
		}

		public void add(List<CurdcardListEntry> list) {
			this.listItems.addAll(list);
			notifyDataSetChanged();
		}
	}

	class ViewHolder {
		TextView tv_hospital;
		TextView tv_reserve_card;
	}

	@Override
	protected void onStop() {
		super.onStop();
		EventBus.getDefault().removeStickyEvent(VisitPersonAddEvent.class);
	}
}
