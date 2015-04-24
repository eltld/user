package net.ememed.user2.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddCurdcardInfo;
import net.ememed.user2.entity.CurdcardListEntry;
import net.ememed.user2.entity.CuredmemeberList;
import net.ememed.user2.entity.CuredmemeberListEntry;
import net.ememed.user2.entity.EditCurdcardInfo;
import net.ememed.user2.entity.EditCuredmemberInfo;
import net.ememed.user2.entity.GuahaoDoctotItem;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.ScheduleDetailEntry;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.IDCard;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;
import net.ememed.user2.widget.RefreshListView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
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

public class VisitPersonEditActivity2 extends BasicActivity {

	private TextView et_birthday;
	private EditText et_shenfenzheng;
	private EditText et_phone;
	private EditText et_name;
	private TextView et_hospital;
	private EditText et_curdcard;
	private boolean isEdit = false;
	private EditText et_address;
	private RadioButton rbtn_man;
	private RadioButton rbtn_woman;
	private RadioGroup rg_sex;
	private Button btn_addhealth;
	private LinearLayout ll_reservation_info, ll_reservation_list;
	private CuredmemeberListEntry data;
	private ReservationAdapter new_adapter;
	private String update;
	private String add;
	// private ReservationAdapter new_adapter;
	private RefreshListView lv_revervation_list;
	private GuahaoDoctotItem guahao_doctot;
	private ScheduleDetailEntry schedule_detail;
	private LinearLayout ll_add_reservation_card;
	private String sex = "1";
	private EditText et_card;
	private TextView tv_hosp_name;
	private List<CurdcardListEntry> curdcard_list;
	private EditText et_hosp_name;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_visit_person_edit);
		data = (CuredmemeberListEntry) getIntent().getSerializableExtra("data");
		guahao_doctot = (GuahaoDoctotItem) getIntent().getSerializableExtra("guahao_doctot");
		schedule_detail = (ScheduleDetailEntry) getIntent().getSerializableExtra("schedule_detail");
	}

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:// 返回键
			finish();
			break;
		case R.id.ll_birthday:
			setBirthday();
			break;
		case R.id.ll_add_reservation_card:
			addCard();
			break;
		default:
			break;
		}
	}

	// 设置生日
	private void setBirthday() {
		DatePickerDialog dialog = new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				et_birthday.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
						+ String.format("%02d", dayOfMonth));
			}
		}, 2013, 0, 1);
		dialog.show();
	}

	@Override
	protected void setupView() {

		ll_add_reservation_card = (LinearLayout) findViewById(R.id.ll_add_reservation_card);

		TextView title = (TextView) findViewById(R.id.top_title);
		ll_reservation_list = (LinearLayout) findViewById(R.id.ll_reservation_list);
		ll_reservation_list.setVisibility(View.VISIBLE);
		lv_revervation_list = (RefreshListView) findViewById(R.id.lv_revervation_list);
		curdcard_list = data.getCurdcard_list();
		new_adapter = new ReservationAdapter(curdcard_list);
		lv_revervation_list.setAdapter(new_adapter);

		et_birthday = (TextView) findViewById(R.id.et_birthday);
		et_name = (EditText) findViewById(R.id.et_name);
		et_shenfenzheng = (EditText) findViewById(R.id.et_shenfenzheng);
		et_phone = (EditText) findViewById(R.id.et_phone);
		et_hospital = (TextView) findViewById(R.id.et_hospital);
		et_curdcard = (EditText) findViewById(R.id.et_curdcard);
		et_address = (EditText) findViewById(R.id.et_address);
		rbtn_man = (RadioButton) findViewById(R.id.rbtn_man);
		rbtn_woman = (RadioButton) findViewById(R.id.rbtn_woman);
		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		btn_addhealth.setText("确认修改");
		btn_addhealth.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EditCuredmember();
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

	@Override
	protected void addListener() {
		lv_revervation_list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				CurdcardListEntry curdcardListEntry = curdcard_list.get(position - 1);
				EditCard(curdcardListEntry);

			}
		});
	}

	private void EditCuredmember() {
		String etBirthday = et_birthday.getText().toString().trim();
		String etName = et_name.getText().toString().trim();
		String etShenfenzheng = et_shenfenzheng.getText().toString().trim();
		String etPhone = et_phone.getText().toString().trim();
		String etAddress = et_address.getText().toString().trim();
		// etHospital = et_hospital.getText().toString().trim();
		// etCurdcard = et_curdcard.getText().toString().trim();
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
		if (TextUtils.isEmpty(etAddress)) {
			showToast("地址必填哦");
			return;
		}
		// if (TextUtils.isEmpty(etHospital)) {
		// showToast("所属医院必填哦");
		// return;
		// }
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
			params.put("id", data.getID());
			params.put("address", etAddress);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.edit_curedmember,
					EditCuredmemberInfo.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							isEdit = true;
							Message message = handler.obtainMessage();
							message.obj = response;
							message.what = IResult.EDIT_CUREDMEMEBER;
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

	private void EditCard(final CurdcardListEntry Entry) {
		// 定义对话框的构建器
		Builder builder = new Builder(this);
		// AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.view_card, null);
		final AlertDialog dialog = builder.create();
		tv_hosp_name = (TextView) view.findViewById(R.id.tv_hosp_name);
		tv_hosp_name.setText(Entry.getHOSPITAL());
		et_card = (EditText) view.findViewById(R.id.et_card);
		et_card.setText(Entry.getCURDCARDID());
		Button bt_ok = (Button) view.findViewById(R.id.btn_ok);
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String trim = et_card.getText().toString().trim();
				if (TextUtils.isEmpty(trim))
					showToast("诊疗卡号不能为空");
				else {
					EditCurdcard(Entry, trim);
					dialog.dismiss();
				}

			}
		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private void addCard() {
		// 定义对话框的构建器
		Builder builder = new Builder(this);
		// AlertDialog.Builder builder = new Builder(this);
		View view = View.inflate(this, R.layout.view_card, null);
		final AlertDialog dialog = builder.create();
		tv_hosp_name = (TextView) view.findViewById(R.id.tv_hosp_name);
		et_hosp_name = (EditText) view.findViewById(R.id.et_hosp_name);
		String hosp_name = "";
		if (guahao_doctot == null || TextUtils.isEmpty(guahao_doctot.getHOSPITALNAME())) {
			et_hosp_name.setVisibility(View.VISIBLE);
			tv_hosp_name.setVisibility(View.GONE);
		} else {
			hosp_name = guahao_doctot.getHOSPITALNAME();
		}
		tv_hosp_name.setText(hosp_name);
		et_card = (EditText) view.findViewById(R.id.et_card);
		Button bt_ok = (Button) view.findViewById(R.id.btn_ok);
		bt_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String trim = et_card.getText().toString().trim();
				String hosp_name = null;
				if (guahao_doctot == null) {
					hosp_name = et_hosp_name.getText().toString().trim();
				} else {
					hosp_name = tv_hosp_name.getText().toString().trim();
				}
				if (TextUtils.isEmpty(hosp_name)) {
					showToast("医院名称不能为空");
					return;
				}

				if (TextUtils.isEmpty(trim)) {
					showToast("诊疗卡号不能为空");
					return;
				}

				AddCurdcard(hosp_name, trim);
				dialog.dismiss();
			}

		});
		dialog.setView(view, 0, 0, 0, 0);
		dialog.show();
	}

	private void EditCurdcard(CurdcardListEntry Entry, String curdcardid) {
		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("id", Entry.getID());
			params.put("hospital", Entry.getHOSPITAL());
			params.put("curdcardid", curdcardid);
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.edit_curdcard,
					EditCurdcardInfo.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							isEdit = true;
							Message message = handler.obtainMessage();
							message.obj = response;
							message.what = IResult.EDIT_CURDCARD;
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

	private void AddCurdcard(String hospital, String curdcardid) {
		if (NetWorkUtils.detect(this)) {
			loading("努力加载中...");
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("hospital", hospital);
			params.put("curdcardid", curdcardid);
			params.put("curedmember_id", data.getID());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.MEDICAL_CARD_ADD,
					AddCurdcardInfo.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							isEdit = true;
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
		destroyDialog();
		switch (msg.what) {
		case IResult.EDIT_CUREDMEMEBER:
			EditCuredmemberInfo info1 = (EditCuredmemberInfo) msg.obj;
			if (info1 != null) {
				switch (info1.getSuccess()) {
				case 1:
					showToast("修改就诊人信息成功");
					finish();
					break;
				case 0:
					showToast(info1.getErrormsg());
					break;
				default:
					showToast(info1.getErrormsg());
					break;
				}
			} else {
				showToast(IMessage.SERVICE_ERROR);
			}
			break;
		case IResult.MEDICAL_CARD_ADD:
			AddCurdcardInfo addCurdcard = (AddCurdcardInfo) msg.obj;
			if (null != addCurdcard) {
				if (addCurdcard.getSuccess() == 1) {
					showToast("添加就诊卡成功");
					getCuredmemeberList();
				} else {
					showToast(addCurdcard.getErrormsg());
				}
			} else {
				showToast(IMessage.SERVICE_ERROR);
			}

			break;
		case IResult.EDIT_CURDCARD:
			EditCurdcardInfo info = (EditCurdcardInfo) msg.obj;
			if (info != null) {
				switch (info.getSuccess()) {
				case 1:
					showToast("修改就诊卡成功");
					getCuredmemeberList();
					break;

				default:
					showToast(info.getErrormsg());
					break;
				}
			} else {
				showToast(IMessage.SERVICE_ERROR);
			}
			break;

		case IResult.PATIENT_LIST:

			CuredmemeberList curedmemeberList = (CuredmemeberList) msg.obj;
			if (null != curedmemeberList) {
				if (curedmemeberList.getSuccess() == 1) {
					List<CuredmemeberListEntry> list = curedmemeberList.getData();
					if (null != list && list.size() > 0) {
						for (int i = 0; i < list.size() && i < 5; i++) {
							CuredmemeberListEntry entry = list.get(i);
							if (data.getID().equals(entry.getID())) {
								data = entry;
								new_adapter.change(entry.getCurdcard_list());
							}

						}

					}
				} else {
					showToast(curedmemeberList.getErrormsg());
				}
			} else {
				showToast(IMessage.SERVICE_ERROR);
			}
			break;

		default:
			break;
		}
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
				convertView = LayoutInflater.from(VisitPersonEditActivity2.this).inflate(
						R.layout.revervation_info_item, null);

				holder.tv_hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
				holder.tv_reserve_card = (TextView) convertView.findViewById(R.id.tv_reserve_card);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.tv_hospital.setText(listItems.get(position).getHOSPITAL());
			holder.tv_reserve_card.setText(listItems.get(position).getCURDCARDID());

			if (guahao_doctot != null
					&& listItems.get(position).getHOSPITAL()
							.equals(guahao_doctot.getHOSPITALNAME())) {
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

	private void getCuredmemeberList() {
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
	protected void onDestroy() {
		if (isEdit)
			EventBus.getDefault().postSticky(new VisitPersonAddEvent());
		super.onDestroy();
	}
}
