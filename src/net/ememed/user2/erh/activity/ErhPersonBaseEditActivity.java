package net.ememed.user2.erh.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.dialog.Effectstype;
import net.ememed.user2.dialog.NiftyDialogBuilder;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.erh.bean.ErhBasePerson;
import net.ememed.user2.erh.bean.ErhBasePersonBean;
import net.ememed.user2.erh.util.SingleDmAdapter;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;

public class ErhPersonBaseEditActivity extends BasicActivity {
	private TextView top_title;
	private TextView birthday;
	private TextView nation;
	private TextView married;
	private TextView culture;
	private TextView workPlace;
	private TextView censused;
	private TextView address;
	private TextView phone;
	private TextView person_name;
	private TextView person_phone;
	private TextView bloodTypeTxt;
	private TextView rhsTxt;
	private TextView pay_txt;
	private TextView ids;
	private TextView gender;
	private ArrayList<String> reList = new ArrayList<String>();
	private ArrayList<String> reList2 = new ArrayList<String>();
	private static final int RETURN_PERSON_NAME = 0x12; // 姓名
	private static final int RETURN_PERSON_GENDER = 0x13; // 工作
	private int year;
	private int month;
	private int day;
	private NiftyDialogBuilder dialogBuilder;
	private Effectstype effect;
	private TextView nameTxt;
	private String name;
	private TextView jobTxt;
	private String job;
	private Button btn_addhealth;
	private int genderTxt = 1;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.erh_edit_person_base);
		initWeight();
		dialogBuilder = new NiftyDialogBuilder(ErhPersonBaseEditActivity.this,
				R.style.dialog_untran);
		getInitData();
	}

	private void getInitData() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_base_person,
					ErhBasePerson.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.USER_INFO;
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

	private void initWeight() {
		nameTxt = (TextView) findViewById(R.id.name_txt);
		jobTxt = (TextView) findViewById(R.id.job);
		birthday = (TextView) findViewById(R.id.birthday);
		nation = (TextView) findViewById(R.id.nation);
		married = (TextView) findViewById(R.id.married);
		culture = (TextView) findViewById(R.id.culture);
		workPlace = (TextView) findViewById(R.id.work);
		censused = (TextView) findViewById(R.id.huji);
		address = (TextView) findViewById(R.id.address);
		phone = (TextView) findViewById(R.id.phone);
		person_name = (TextView) findViewById(R.id.person_name2);
		person_phone = (TextView) findViewById(R.id.person_phone);
		bloodTypeTxt = (TextView) findViewById(R.id.xue);
		rhsTxt = (TextView) findViewById(R.id.rh);
		pay_txt = (TextView) findViewById(R.id.pay_txt);
		ids = (TextView) findViewById(R.id.ids);
		gender = (TextView) findViewById(R.id.gender);
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("个人基本资料");

		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("保存");
		super.setupView();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.pay) {
			
			final String[] txts = new String[] { "城镇职工基本医疗保险", "城镇居民基本医疗保险",
					"新型农村合作医疗", "贫困救助", "商业医疗保险", "全公费", "全自费", "其他" };
			View view2 = LayoutInflater.from(ErhPersonBaseEditActivity.this)
					.inflate(R.layout.custom_view, null);
			effect = Effectstype.Slideleft;
			String defaultStr = pay_txt.getText().toString().trim();

			dialogBuilder
					.withTitle("医疗费用支付方式")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).show();

			ListView listView = (ListView) view2.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(this,
					txts, defaultStr);
			listView.setAdapter(singleBaseAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter.setName(txts[arg2]);
					singleBaseAdapter.notifyDataSetChanged();
					dialogBuilder.dismiss();
					pay_txt.setText(txts[arg2]);
				}
			});

		} else if (view.getId() == R.id.person_name) {
			effect = Effectstype.Flipv;
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.erh_person_ids, null);
			final EditText idsExit = (EditText) view2.findViewById(R.id.name);
			idsExit.setText(nameTxt.getText().toString().trim());
			idsExit.setSelection(nameTxt.getText().length());
			dialogBuilder
					.withTitle("姓名")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).withButton1Text("确定") // def
																		// gone
					.withButton2Text("取消") // def gone
					// .setCustomView(R.layout.custom_view, MainMeActivity.this)
					// // .setCustomView(View
					// or
					// ResId,context)
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							
							String idStr = idsExit.getText().toString().trim();
							if (idStr == null || idStr.equals("")) {
								showToast("请填写姓名");
								return;
							}					
							nameTxt.setText(idStr);
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
		} else if (view.getId() == R.id.person_gender) {
			
			final String[] txts = new String[] { "男", "女" };
			View view2 = LayoutInflater.from(ErhPersonBaseEditActivity.this)
					.inflate(R.layout.custom_view, null);
			effect = Effectstype.Slideleft;
			String defaultStr = gender.getText().toString().trim();

			dialogBuilder
					.withTitle("性别")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).show();

			ListView listView = (ListView) view2.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(this,
					txts, defaultStr);
			listView.setAdapter(singleBaseAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter.setName(txts[arg2]);
					singleBaseAdapter.notifyDataSetChanged();
					gender.setText(txts[arg2]);
					if (arg2 == 0) {
						genderTxt = 1;
					} else {
						genderTxt = 0;
					}
					dialogBuilder.dismiss();
				}
			});
		} else if (view.getId() == R.id.person_culture) {
			
			String defaultStr = culture.getText().toString().trim();
			final String[] txts = new String[] { "文盲及半文盲", "小学", "初中",
					"高中/技校/中专", "大学本科及以上", "不详" };
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.custom_view, null);
			effect = Effectstype.Slideleft;
			dialogBuilder
					.withTitle("文化程度")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).show();

			ListView listView = (ListView) view2.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(this,
					txts, defaultStr);
			listView.setAdapter(singleBaseAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter.setName(txts[arg2]);
					singleBaseAdapter.notifyDataSetChanged();
					dialogBuilder.dismiss();
					culture.setText(txts[arg2]);
				}
			});

		} else if (view.getId() == R.id.person_work) {
			job = jobTxt.getText().toString().trim();
			Bundle data = new Bundle();
			data.putString("job", job);
			Utils.startActivityForResult(ErhPersonBaseEditActivity.this,
					ErhPayAndWorkActivity.class, data, RETURN_PERSON_GENDER);

		} else if (view.getId() == R.id.person_birthday) {
			
			// 初始化Calendar日历对象
			Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
			Date mydate = new Date(); // 获取当前日期Date对象
			mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
			year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
			month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
			day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
			DatePickerDialog datePicker = new DatePickerDialog(
					ErhPersonBaseEditActivity.this, new OnDateSetListener() {

						@Override
						public void onDateSet(DatePicker view, int myyear,
								int monthOfYear, int dayOfMonth) {
							// TODO Auto-generated method stub
							year = myyear;
							month = monthOfYear;
							day = dayOfMonth;
							// 更新日期
							updateDate();
						}

						private void updateDate() {
							String monthT = "";
							if ((month + 1) < 10) {
								monthT = "0" + (month + 1);
							} else {
								monthT = (month + 1) + "";
							}
							birthday.setText(year + "-" + monthT + "-"
									+ day);
							
						}
					}, year, month, day);
			datePicker.setIcon(R.drawable.medical_history_ememed2);
			datePicker.setTitle("选择时间");
			datePicker.show();
		} else if (view.getId() == R.id.person_id) {
			
			effect = Effectstype.Flipv;
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.erh_person_ids, null);
			final EditText idsExit = (EditText) view2.findViewById(R.id.name);
			idsExit.setText(ids.getText().toString().trim());
			idsExit.setSelection(ids.getText().length());
			dialogBuilder
					.withTitle("身份证")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).withButton1Text("确定") // def
																		// gone
					.withButton2Text("取消") // def gone
					// .setCustomView(R.layout.custom_view, MainMeActivity.this)
					// // .setCustomView(View
					// or
					// ResId,context)
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							
							String idStr = idsExit.getText().toString().trim();
							if (idStr == null || idStr.equals("")) {
								showToast("身份证号不能为空");
								return;
							}
							if (!Utils.IDNumIsValid(idStr)) {
								showToast("身份证输入错误");
								return;
							}
							ids.setText(idStr);
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
		} else if (view.getId() == R.id.rl_nation) {
			
			String defaultStr = nation.getText().toString().trim();
			final String[] nations = new String[] { "汉族", "少数民族" };
			View view3 = LayoutInflater.from(this).inflate(
					R.layout.custom_view, null);
			effect = Effectstype.Slideleft;
			dialogBuilder.withTitle("民族")
			// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					// .withIcon(getResources().getDrawable(R.drawable.dialog_logo))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view3, this).show();

			ListView listView2 = (ListView) view3.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter2 = new SingleDmAdapter(
					this, nations, defaultStr);
			listView2.setAdapter(singleBaseAdapter2);
			listView2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter2.setName(nations[arg2]);
					singleBaseAdapter2.notifyDataSetChanged();
					dialogBuilder.dismiss();
					nation.setText(nations[arg2]);
				}
			});
		} else if (view.getId() == R.id.rl_married) {
			
			String defaultStr = married.getText().toString().trim();
			final String[] marry = new String[] { "未婚", "已婚", "丧偶", "离婚",
					"未说明婚姻状况" };
			View view5 = LayoutInflater.from(this).inflate(
					R.layout.custom_view, null);
			effect = Effectstype.Slideleft;
			dialogBuilder.withTitle("婚姻状况")
			// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					// .withIcon(getResources().getDrawable(R.drawable.dialog_logo))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view5, this).show();

			ListView listView4 = (ListView) view5.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter4 = new SingleDmAdapter(
					this, marry, defaultStr);
			listView4.setAdapter(singleBaseAdapter4);
			listView4.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter4.setName(marry[arg2]);
					singleBaseAdapter4.notifyDataSetChanged();
					dialogBuilder.dismiss();
					married.setText(marry[arg2]);
				}
			});
		} else if (view.getId() == R.id.rl_work) {
			
			effect = Effectstype.Flipv;
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.erh_person_ids, null);
			final EditText idsExit = (EditText) view2.findViewById(R.id.name);
			idsExit.setText(workPlace.getText().toString().trim());
			idsExit.setSelection(workPlace.getText().length());
			dialogBuilder
					.withTitle("工作单位")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).withButton1Text("确定") // def
																		// gone
					.withButton2Text("取消") // def gone
					// .setCustomView(R.layout.custom_view, MainMeActivity.this)
					// // .setCustomView(View
					// or
					// ResId,context)
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							
							String idStr = idsExit.getText().toString().trim();
							if (idStr == null || idStr.equals("")) {
								showToast("工作单位没有填写");
								return;
							}
							workPlace.setText(idStr);
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
		} else if (view.getId() == R.id.rl_huji) {
			
			String defaultStr = censused.getText().toString().trim();

			final String[] census = new String[] { "户籍", "非户籍", "不详" };
			effect = Effectstype.Slideleft;
			View view5 = LayoutInflater.from(this).inflate(
					R.layout.custom_view, null);
			dialogBuilder.withTitle("户籍类型")
			// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					// .withIcon(getResources().getDrawable(R.drawable.dialog_logo))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view5, this).show();

			ListView listView4 = (ListView) view5.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter4 = new SingleDmAdapter(
					this, census, defaultStr);
			listView4.setAdapter(singleBaseAdapter4);
			listView4.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter4.setName(census[arg2]);
					singleBaseAdapter4.notifyDataSetChanged();
					dialogBuilder.dismiss();
					censused.setText(census[arg2]);
				}
			});
		} else if (view.getId() == R.id.rl_address) {
			
			effect = Effectstype.Flipv;
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.erh_person_ids, null);
			final EditText idsExit = (EditText) view2.findViewById(R.id.name);
			idsExit.setText(address.getText().toString().trim());
			idsExit.setSelection(address.getText().length());
			dialogBuilder
					.withTitle("户籍地址")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).withButton1Text("确定") // def
																		// gone
					.withButton2Text("取消") // def gone
					// .setCustomView(R.layout.custom_view, MainMeActivity.this)
					// // .setCustomView(View
					// or
					// ResId,context)
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							
							String idStr = idsExit.getText().toString().trim();
							if (idStr == null || idStr.equals("")) {
								showToast("户籍地址没有填写");
								return;
							}
							address.setText(idStr);
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
		} else if (view.getId() == R.id.rl_phone) {
			
			effect = Effectstype.Flipv;
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.erh_person_ids, null);
			final EditText idsExit = (EditText) view2.findViewById(R.id.name);
			idsExit.setInputType(InputType.TYPE_CLASS_PHONE);// 电话
			idsExit.setText(phone.getText().toString().trim());
			idsExit.setSelection(phone.getText().length());
			dialogBuilder
					.withTitle("本人联系电话")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).withButton1Text("确定") // def
																		// gone
					.withButton2Text("取消") // def gone
					// .setCustomView(R.layout.custom_view, MainMeActivity.this)
					// // .setCustomView(View
					// or
					// ResId,context)
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String idStr = idsExit.getText().toString().trim();
							if (idStr == null || idStr.equals("")) {
								showToast("本人联系电话没有填写");
								return;
							}
							phone.setText(idStr);
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
		} else if (view.getId() == R.id.rl_person_name) {
			
			effect = Effectstype.Flipv;
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.erh_person_ids, null);
			final EditText idsExit = (EditText) view2.findViewById(R.id.name);
			idsExit.setText(person_name.getText().toString().trim());
			idsExit.setSelection(person_name.getText().length());
			dialogBuilder
					.withTitle("联系人姓名")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).withButton1Text("确定") // def
																		// gone
					.withButton2Text("取消") // def gone
					// .setCustomView(R.layout.custom_view, MainMeActivity.this)
					// // .setCustomView(View
					// or
					// ResId,context)
					.setButton1Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							String idStr = idsExit.getText().toString().trim();
							if (idStr == null || idStr.equals("")) {
								showToast("联系人姓名没有填写");
								return;
							}
							person_name.setText(idStr);
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
		} else if (view.getId() == R.id.rl_person_phone) {
			
			effect = Effectstype.Flipv;
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.erh_person_ids, null);
			final EditText idsExit = (EditText) view2.findViewById(R.id.name);
			idsExit.setInputType(InputType.TYPE_CLASS_PHONE);// 电话
			idsExit.setText(person_phone.getText().toString().trim());
			idsExit.setSelection(person_phone.getText().length());
			dialogBuilder
					.withTitle("联系人电话")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					// .withDividerColor("#11000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
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
								showToast("本人联系电话没有填写");
								return;
							}
							person_phone.setText(idStr);
							dialogBuilder.dismiss();
						}
					}).setButton2Click(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							dialogBuilder.dismiss();
						}
					}).show();
		} else if (view.getId() == R.id.rl_xue) {
			
			String defaultStr = bloodTypeTxt.getText().toString().trim();
			effect = Effectstype.Slideleft;
			final String[] bloodType = new String[] { "A型", "B型", "O型", "AB型",
					"不详" };
			View view2 = LayoutInflater.from(this).inflate(
					R.layout.custom_view, null);
			dialogBuilder.withTitle("血型")
					// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					.withIcon(
							getResources().getDrawable(
									R.drawable.medical_history_ememed2))
					// def
					// .withIcon(getResources().getDrawable(R.drawable.dialog_logo))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view2, this).show();

			ListView listView = (ListView) view2.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(this,
					bloodType, defaultStr);
			listView.setAdapter(singleBaseAdapter);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter.setName(bloodType[arg2]);
					singleBaseAdapter.notifyDataSetChanged();
					dialogBuilder.dismiss();
					bloodTypeTxt.setText(bloodType[arg2]);
				}
			});
		} else if (view.getId() == R.id.rl_rh) {
			
			String defaultStr = rhsTxt.getText().toString().trim();
			final String[] rhs = new String[] { "否", "是", "不详" };
			effect = Effectstype.Slideleft;
			View view3 = LayoutInflater.from(this).inflate(
					R.layout.custom_view, null);
			effect = Effectstype.Flipv;
			dialogBuilder.withTitle("rh")
			// .withTitle(null) no title
					.withTitleColor("#000000")
					// def
					.withMessage(null)
					// .withMessage(null) no Msg
					.withMessageColor("#000000")
					// def
					// .withIcon(getResources().getDrawable(R.drawable.dialog_logo))
					.isCancelableOnTouchOutside(true) // def |
														// isCancelable(true)
					.withDuration(700) // def
					.withEffect(effect) // def Effectstype.Slidetop
					.setCustomView(view3, this).show();

			ListView listView2 = (ListView) view3.findViewById(R.id.list);
			final SingleDmAdapter singleBaseAdapter2 = new SingleDmAdapter(
					this, rhs, defaultStr);
			listView2.setAdapter(singleBaseAdapter2);
			listView2.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					singleBaseAdapter2.setName(rhs[arg2]);
					singleBaseAdapter2.notifyDataSetChanged();
					dialogBuilder.dismiss();
					rhsTxt.setText(rhs[arg2]);
				}
			});
		} else if (view.getId() == R.id.btn_addhealth){
			String name = nameTxt.getText().toString().trim();
			String birthTxt = birthday.getText().toString().trim();
			String nationTxt = nation.getText().toString().trim();
			String marry = married.getText().toString().trim();
			String cultures = culture.getText().toString().trim();
			String work = workPlace.getText().toString().trim();
			String idsTxt = ids.getText().toString().trim();
			String hujiStyle = censused.getText().toString().trim();
			String add = address.getText().toString().trim();
			String person_name_txt = person_name.getText().toString().trim();
			String person_phone_txt = person_phone.getText().toString().trim();
			String bloodType = bloodTypeTxt.getText().toString().trim();
			String rhs = rhsTxt.getText().toString().trim();
			String payTxt = pay_txt.getText().toString().trim();
			if (NetWorkUtils.detect(this)) {
				loading(null);
				HashMap<String, String> params = new HashMap<String, String>();
				params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
				params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
				if(!TextUtils.isEmpty(name)){
					params.put("realname", name);
				}
				params.put("sex", genderTxt + "");
				if(!TextUtils.isEmpty(birthTxt)){
					params.put("birthday", birthTxt);
				}
				if(!TextUtils.isEmpty(nationTxt)){
					params.put("nation", nationTxt);
				}
				if(!TextUtils.isEmpty(marry)){
					params.put("marital", marry);
				}
				if(!TextUtils.isEmpty(cultures)){
					params.put("culturallevel", cultures);
				}
				if(!TextUtils.isEmpty(work)){
					params.put("working_company", work);
				}
				if(!TextUtils.isEmpty(job)){
					params.put("occupation", job);
				}
				if(!TextUtils.isEmpty(hujiStyle)){
					params.put("residency_type", hujiStyle);
				}
				if(!TextUtils.isEmpty(add)){
					params.put("residence_address", add);
				}
				if(!TextUtils.isEmpty(person_name_txt)){
					params.put("contact_name", person_name_txt);
				}
				if(!TextUtils.isEmpty(person_phone_txt)){
					params.put("contact_phone", person_phone_txt);
				}
				if(!TextUtils.isEmpty(bloodType)){
					params.put("bloodtype", bloodType);
				}
				if(!TextUtils.isEmpty(rhs)){
					params.put("rh", rhs);
				}
				if(!TextUtils.isEmpty(payTxt)){
					params.put("medical_payment", payTxt);
				}
				if(!TextUtils.isEmpty(idsTxt)){
					params.put("cardid", idsTxt);
				}
				Log.e("YY", params.toString());
				MyApplication.volleyHttpClient.postWithParams(
						HttpUtil.post_base_person, SetProfile.class, params,
						new Response.Listener() {
							@Override
							public void onResponse(Object response) {
								Message message = new Message();
								message.obj = response;
								message.what = 0x31;
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
	}

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		try {
			switch (msg.what) {
			case IResult.USER_INFO:
				Log.e("YY", "www" +  "开始");
				final ErhBasePerson response = (ErhBasePerson) msg.obj;
				Log.e("YY", "www" + response.getSuccess() + "");
					if (response.getSuccess() == 1) {
						Log.e("YY", "www" +  "1111");
						ErhBasePersonBean basePersonBean = response.getData();
						Log.e("YY", "www" +  "2222");
						if (!TextUtils.isEmpty(basePersonBean.getREALNAME())) {
							Log.e("YY", "www" +  "3333");
							nameTxt.setText(basePersonBean.getREALNAME());
						}
						if (!TextUtils.isEmpty(basePersonBean.getBIRTHDAY())) {
							birthday.setText(basePersonBean.getBIRTHDAY());
						}
						if (!TextUtils.isEmpty(basePersonBean.getBLOODTYPE())) {
							bloodTypeTxt.setText(basePersonBean.getBLOODTYPE());
						}
						if (!TextUtils.isEmpty(basePersonBean.getCONTACT_NAME())) {
							person_name.setText(basePersonBean.getCONTACT_NAME());
						}
						if (!TextUtils.isEmpty(basePersonBean.getCONTACT_PHONE())) {
							person_phone.setText(basePersonBean.getCONTACT_PHONE());
						}
						if (!TextUtils.isEmpty(basePersonBean.getCULTURALLEVEL())) {
							culture.setText(basePersonBean.getCULTURALLEVEL());
						}
						if (!TextUtils.isEmpty(basePersonBean.getMARITAL())) {
							married.setText(basePersonBean.getMARITAL());
						}
						if (!TextUtils.isEmpty(basePersonBean.getMEDICAL_PAYMENT())) {
							pay_txt.setText(basePersonBean.getMEDICAL_PAYMENT());
						}
						if (!TextUtils.isEmpty(basePersonBean.getNATION())) {
							nation.setText(basePersonBean.getNATION());
						}
						if (!TextUtils.isEmpty(basePersonBean.getOCCUPATION())) {
							jobTxt.setText(basePersonBean.getOCCUPATION());
						}
						
						if (!TextUtils.isEmpty(basePersonBean.getRESIDENCE_ADDRESS())) {
							address.setText(basePersonBean.getRESIDENCE_ADDRESS());
						}
						if (!TextUtils.isEmpty(basePersonBean.getRESIDENCY_TYPE())) {
							censused.setText(basePersonBean.getRESIDENCY_TYPE());
						}
						if (!TextUtils.isEmpty(basePersonBean.getWORKING_COMPANY())) {
							workPlace.setText(basePersonBean.getWORKING_COMPANY());
						}
						if (!TextUtils.isEmpty(basePersonBean.getRH())) {
							rhsTxt.setText(basePersonBean.getRH());
						}
						if (!TextUtils.isEmpty(basePersonBean.getRH())) {
							rhsTxt.setText(basePersonBean.getRH());
						}
						if (!TextUtils.isEmpty(basePersonBean.getCARDID())) {
							ids.setText(basePersonBean.getCARDID());
						}
						if (!TextUtils.isEmpty(basePersonBean.getSEX())) {
							if (Integer.valueOf(basePersonBean.getSEX()) == 1) {
								genderTxt = 1;
								gender.setText("男");
							} else {
								genderTxt = 0;
								gender.setText("女");
							}
						}
					} else {
						showToast("获取用户信息失败");
					}
				

				break;
			case 0x31:
				SetProfile sp = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp.getSuccess() + "");
				Log.e("YY", "www" + sp.getErrormsg() + "");
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						showToast("保存成功");
						SharePrefUtil.putString(Conast.realName, nameTxt.getText().toString().trim());
						SharePrefUtil.commit();
					} else {
						showToast("保存失败");
					}
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == RETURN_PERSON_NAME && resultCode == RESULT_OK) {
			name = data.getStringExtra("name");
			nameTxt.setText(name);
		} else if (requestCode == RETURN_PERSON_GENDER
				&& resultCode == RESULT_OK) {
			job = data.getStringExtra("job");
			jobTxt.setText(job);
		}
	}

}
