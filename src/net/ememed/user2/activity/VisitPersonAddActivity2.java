package net.ememed.user2.activity;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.AddCurdcardInfo;
import net.ememed.user2.entity.AddCuredmember;
import net.ememed.user2.entity.CurdcardListEntry;
import net.ememed.user2.entity.IMessage;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.StringUtil;
import net.ememed.user2.widget.ListViewForScrollView;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.joanzapata.android.BaseAdapterHelper;
import com.joanzapata.android.QuickAdapter;
import com.mobsandgeeks.saripaar.Rule;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.Validator.ValidationListener;
import com.mobsandgeeks.saripaar.annotation.Regex;
import com.mobsandgeeks.saripaar.annotation.Required;
import com.mobsandgeeks.saripaar.annotation.TextRule;

import net.ememed.user2.request.MediaCardRequestQueue;
import net.ememed.user2.request.MediaCardRequestQueue.OnRequestQueueListener;

public class VisitPersonAddActivity2 extends BasicActivity implements ValidationListener {

	public static final int STATE_ADD = 1;
	public static final int STATE_EDIT = 2;
	private static final String TITLE = "我的就诊人";

	@Required(order = 1, message = "请输入姓名")
	@Regex(order = 2, trim = true, pattern = StringUtil.NAME, message = "请输入正确的姓名")
	private EditText et_name;
	private RadioGroup rg_sex;
	@Required(order = 4, message = "请输入身份证号")
	@TextRule(order = 5, minLength = 15, maxLength = 18, message = "请输入15或18位身份证号")
	@Regex(order = 6, trim = true, pattern = StringUtil.IDENTITY_REG_EXPRESSION, message = "请输入正确的身份证号")
	private EditText et_identity;
	@Required(order = 7, message = "请选择出生日期")
	private TextView tv_birthday;
	@Required(order = 8, message = "请输入11位手机号")
	@Regex(order = 9, pattern = StringUtil.MOBILE_REG_EXPRESSION, message = "请输入正确的手机号")
	private EditText et_mobile;
	private EditText et_address;

	private Validator mValidator;
	private QuickAdapter<CurdcardListEntry> mAdapter;
	private ListViewForScrollView mListView;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_visit_person_add2);
		((TextView) findViewById(R.id.tv_top_title)).setText(TITLE);
		findViewById(R.id.tv_top_right).setVisibility(View.VISIBLE);

		et_name = (EditText) findViewById(R.id.et_name);
		rg_sex = (RadioGroup) findViewById(R.id.rg_sex);
		et_identity = (EditText) findViewById(R.id.et_identity);
		tv_birthday = (TextView) findViewById(R.id.tv_birday2);
		et_mobile = (EditText) findViewById(R.id.et_mobile);
		et_address = (EditText) findViewById(R.id.et_address);

		mValidator = new Validator(this);
		mValidator.setValidationListener(this);

		mAdapter = new QuickAdapter<CurdcardListEntry>(this, R.layout.list_item_media_card) {

			@Override
			protected void convert(BaseAdapterHelper helper, CurdcardListEntry item) {
				helper.setText(R.id.tv_hospital, item.getHOSPITAL()).setText(R.id.tv_card,
						item.getCURDCARDID());
			}
		};
		mListView = (ListViewForScrollView) findViewById(R.id.lv_card);
		mListView.setAdapter(mAdapter);

		et_identity.addTextChangedListener(mWatcher);
	}

	private TextWatcher mWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			String identity = String.valueOf(s);
			if (!StringUtil.isIdentity(identity)){
				tv_birthday.setText("");
				return;
			}
			String year = "";
			String month = "";
			String day = "";
			if (identity.length() == 15) {
				year = identity.substring(6, 8);
				month = identity.substring(8, 10);
				day = identity.substring(10, 12);
				tv_birthday.setText(String.format("19%s-%s-%s", year, month, day));
			}
			if (identity.length() == 18) {
				year = identity.substring(6, 10);
				month = identity.substring(10, 12);
				day = identity.substring(12, 14);
				tv_birthday.setText(String.format("%s-%s-%s", year, month, day));
			}
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void afterTextChanged(Editable s) {
		}
	};

	public void doClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.tv_top_right:
			mValidator.validate();
			break;
		case R.id.tv_birday2:
			setBirthday();
			break;
		case R.id.tv_media_card:
			addMediaCard();
			break;
		default:
			break;
		}
	}

	private void setBirthday() {
		Calendar calendar = Calendar.getInstance(Locale.CHINA);
		calendar.setTime(new Date());
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH);
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		new DatePickerDialog(this, new OnDateSetListener() {

			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				tv_birthday.setText(year + "-" + String.format("%02d", (monthOfYear + 1)) + "-"
						+ String.format("%02d", dayOfMonth));
			}
		}, year, month, day).show();
	}

	@Override
	public void onValidationFailed(View failedView, Rule<?> failedRule) {
		String message = failedRule.getFailureMessage();
		if (failedView instanceof EditText) {
			failedView.requestFocus();
			((EditText) failedView).setError(message);
		} else {
			Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void onValidationSucceeded() {
		requestPatientAdd();
	}

	private void addMediaCard() {
		final AlertDialog dialog = new Builder(this).create();
		View view = View.inflate(this, R.layout.view_card, null);
		dialog.setCanceledOnTouchOutside(true);
		TextView tv_hosptalName = (TextView) view.findViewById(R.id.tv_hosp_name);
		tv_hosptalName.setVisibility(View.GONE);
		final EditText et_hosptalName = (EditText) view.findViewById(R.id.et_hosp_name);
		et_hosptalName.setVisibility(View.VISIBLE);
		final EditText et_card = (EditText) view.findViewById(R.id.et_card);
		Button btn_ok = (Button) view.findViewById(R.id.btn_ok);
		btn_ok.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String hosptalName = et_hosptalName.getText().toString().trim();
				String card = et_card.getText().toString().trim();
				if (TextUtils.isEmpty(hosptalName)) {
					showToast("医院名称不能为空");
					return;
				}

				if (TextUtils.isEmpty(card)) {
					showToast("诊疗卡号不能为空");
					return;
				}
				CurdcardListEntry mediaCard = new CurdcardListEntry();
				mediaCard.setHOSPITAL(hosptalName);
				mediaCard.setCURDCARDID(card);
				mAdapter.add(mediaCard);
				dialog.dismiss();
			}
		});
		dialog.setView(view);
		dialog.show();
	}

	/**
	 * 保存就诊人信息
	 */
	private void requestPatientAdd() {
		if (!NetWorkUtils.detect(this)) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
			return;
		}
		String name = et_name.getText().toString().trim();
		String sex = rg_sex.getCheckedRadioButtonId() == R.id.rb_man ? "1" : "0";
		String identitiy = et_identity.getText().toString().trim();
		String birthday = tv_birthday.getText().toString();
		String mobile = et_mobile.getText().toString();
		String address = et_address.getText().toString();

		loading("正在保存，请稍后...");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("realname", name);
		params.put("sex", sex);
		params.put("cardid", identitiy);
		params.put("birthday", birthday);
		params.put("mobile", mobile);
		params.put("address", address);
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.PATIENT_ADD, AddCuredmember.class,
				params, new Response.Listener() {
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
	}

	/**
	 * 保存诊疗卡
	 * 
	 * @param curedmember_id
	 */
	private void requestMedicalCardAdd(String curedmember_id, String hospital, String card) {
		if (NetWorkUtils.detect(this)) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
			return;
		}
		// loading("努力加载中...");
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("memberid", SharePrefUtil.getString(Conast.MEMBER_ID));
		params.put("curedmember_id", curedmember_id);
		params.put("hospital", hospital);
		params.put("curdcardid", card);
		MyApplication.volleyHttpClient.postWithParams(HttpUtil.MEDICAL_CARD_ADD,
				AddCurdcardInfo.class, params, new Response.Listener() {
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
	}

	@Override
	protected void onResult(Message msg) {
		try {
			destroyDialog();
			switch (msg.what) {
			case IResult.PATIENT_ADD:
				AddCuredmember addCuredmember = (AddCuredmember) msg.obj;
				if (addCuredmember == null) {
					showToast(IMessage.DATA_ERROR);
					return;
				}
				if (!addCuredmember.isSuccess()) {
					showToast(addCuredmember.getErrorMessage());
					return;
				}
				showToast(addCuredmember.getErrorMessage());
				if (!mAdapter.isEmpty()) {
					new MediaCardRequestQueue(this, mAdapter.getList(), addCuredmember.getData()
							.getCUREDMEMBER_ID(), mOnRequestQueueListener).start();
				} else {
					setResult(RESULT_OK);
					finish();
				}
				break;
			case IResult.MEDICAL_CARD_ADD:
				AddCurdcardInfo addCurdcard = (AddCurdcardInfo) msg.obj;
				if (null != addCurdcard) {
					if (addCurdcard.getSuccess() == 1) {
						showToast("添加就诊卡成功");
					} else {
						showToast(addCurdcard.getErrormsg());
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
		}
		super.onResult(msg);
	}

	private OnRequestQueueListener mOnRequestQueueListener = new OnRequestQueueListener() {

		@Override
		public void onRequestQueueSuccess() {
			destroyDialog();
			showToast("诊疗卡添加成功");
			setResult(RESULT_OK);
			finish();
		}

		@Override
		public void onRequestQueueStart() {
			showLoadingDialog();
		}

		@Override
		public void onRequestQueueFail() {
			destroyDialog();
			showToast("诊疗卡添加失败");
			setResult(RESULT_CANCELED);
			finish();
		}
	};

}
