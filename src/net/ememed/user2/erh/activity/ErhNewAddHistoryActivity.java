package net.ememed.user2.erh.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
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
import net.ememed.user2.erh.util.SingleDmAdapter;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;

public class ErhNewAddHistoryActivity extends BasicActivity {
	private static final int SIX = 0x25;
	private TextView top_title;
	private TextView disease_style_txt;
	private TextView disease_style_txt2;
	private TextView timeTxt;
	private TextView timeTxt2;
	private Button btn_addhealth;
	private int year;
	private int month;
	private int day;
	private String name;
	private String time;
	private int shuzi;
	private NiftyDialogBuilder dialogBuilder;
	private Effectstype effect;
	private List<String> family = new ArrayList<String>();
	private List<String> familyHistory = new ArrayList<String>();

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.erh_person_history_new_add);
		dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
		Bundle data = getIntent().getExtras();
		if (data != null) {
			shuzi = data.getInt("shuzi");
			family = data.getStringArrayList("family");
			familyHistory = data.getStringArrayList("familyHistory");
		}
		initView();
	}

	private void initView() {
		disease_style_txt = (TextView) findViewById(R.id.diseasr_style);
		disease_style_txt2 = (TextView) findViewById(R.id.tv_disease_style);
		timeTxt = (TextView) findViewById(R.id.person_time);
		timeTxt2 = (TextView) findViewById(R.id.tv_title);
		if (shuzi == 2) {
			disease_style_txt2.setText("疾病类型");
			disease_style_txt.setText("点击选择类型");
		} else if (shuzi == 3) {
			disease_style_txt2.setText("手术名称");
			disease_style_txt.setText("填写手术名称");
		} else if (shuzi == 4) {
			disease_style_txt2.setText("外伤名称");
			disease_style_txt.setText("填写外伤名称");
		} else if (shuzi == 5) {
			disease_style_txt2.setText("输血原因");
			disease_style_txt.setText("填写输血原因");
		} else if (shuzi == 6) {
			disease_style_txt2.setText("家族");
			disease_style_txt.setText("点击选择家族成员");
			timeTxt2.setText("疾病类型");
			timeTxt.setText("点击选择疾病类型");
		}

	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("新增过往疾病史");

		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("确定");
		super.setupView();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.ll_history_order) {
			if (shuzi == 2) {
				final String[] txts = new String[] { "高血压", "糖尿病", "冠心病",
						"慢性阻塞性肺疾病", "恶性肿瘤", "脑卒中", "重性精神疾病", "结核病", "肝炎",
						"先天畸形", "其他" };
				View view2 = LayoutInflater.from(ErhNewAddHistoryActivity.this)
						.inflate(R.layout.custom_view, null);
				effect = Effectstype.Slideleft;

				dialogBuilder
						.withTitle("过往疾病史")
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
				final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(
						this, txts, "高血压");
				listView.setAdapter(singleBaseAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						singleBaseAdapter.setName(txts[arg2]);
						singleBaseAdapter.notifyDataSetChanged();
						dialogBuilder.dismiss();
						if (arg2 == 10) {
							final NiftyDialogBuilder dialogBuilder2 = new NiftyDialogBuilder(ErhNewAddHistoryActivity.this, R.style.dialog_untran);
							effect = Effectstype.Flipv;
							View view2 = LayoutInflater.from(ErhNewAddHistoryActivity.this).inflate(
									R.layout.erh_person_ids, null);
							final EditText idsExit = (EditText) view2
									.findViewById(R.id.name);
							dialogBuilder2
									.withTitle("过往疾病史")
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
									.setCustomView(view2, ErhNewAddHistoryActivity.this).withButton1Text("确定") // def
																						// gone
									.withButton2Text("取消") // def gone
									// .setCustomView(R.layout.custom_view,
									// MainMeActivity.this)
									// // .setCustomView(View
									// or
									// ResId,context)
									.setButton1Click(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											String idStr = idsExit.getText().toString()
													.trim();
											if (idStr == null || idStr.equals("")) {
												showToast("请填写内容");
												return;
											}
											dialogBuilder2.dismiss();
											disease_style_txt.setText(idStr);
											name = idStr;
										}
									}).setButton2Click(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialogBuilder2.dismiss();
										}
									}).show();
						}else{
							disease_style_txt.setText(txts[arg2]);
							name = txts[arg2];
						}
						
					}
				});
			} else if (shuzi == 3) {
				effect = Effectstype.Flipv;
				View view2 = LayoutInflater.from(this).inflate(
						R.layout.erh_person_ids, null);
				final EditText idsExit = (EditText) view2
						.findViewById(R.id.name);
				dialogBuilder
						.withTitle("手术名称")
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
						// .setCustomView(R.layout.custom_view,
						// MainMeActivity.this)
						// // .setCustomView(View
						// or
						// ResId,context)
						.setButton1Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String idStr = idsExit.getText().toString()
										.trim();
								if (idStr == null || idStr.equals("")) {
									showToast("请填写内容");
									return;
								}

								disease_style_txt.setText(idStr);
								name = idStr;
								dialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).show();
			} else if (shuzi == 4) {
				effect = Effectstype.Flipv;
				View view2 = LayoutInflater.from(this).inflate(
						R.layout.erh_person_ids, null);
				final EditText idsExit = (EditText) view2
						.findViewById(R.id.name);
				dialogBuilder
						.withTitle("外伤名称")
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
						// .setCustomView(R.layout.custom_view,
						// MainMeActivity.this)
						// // .setCustomView(View
						// or
						// ResId,context)
						.setButton1Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String idStr = idsExit.getText().toString()
										.trim();
								if (idStr == null || idStr.equals("")) {
									showToast("请填写内容");
									return;
								}
								disease_style_txt.setText(idStr);
								name = idStr;
								dialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).show();
			} else if (shuzi == 5) {
				effect = Effectstype.Flipv;
				View view2 = LayoutInflater.from(this).inflate(
						R.layout.erh_person_ids, null);
				final EditText idsExit = (EditText) view2
						.findViewById(R.id.name);
				dialogBuilder
						.withTitle("输血原因")
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
						// .setCustomView(R.layout.custom_view,
						// MainMeActivity.this)
						// // .setCustomView(View
						// or
						// ResId,context)
						.setButton1Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								String idStr = idsExit.getText().toString()
										.trim();
								if (idStr == null || idStr.equals("")) {
									showToast("请填写内容");
									return;
								}
								disease_style_txt.setText(idStr);
								name = idStr;
								dialogBuilder.dismiss();
							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).show();
			} else if (shuzi == 6) {
				final String[] txts = new String[] { "父亲", "母亲", "子女", "兄弟姐妹"};
				final NiftyDialogBuilder dialogBuilder2 = new NiftyDialogBuilder(
						this, R.style.dialog_untran);
				View view3 = LayoutInflater.from(this).inflate(
						R.layout.custom_view, null);
				effect = Effectstype.Slideleft;

				dialogBuilder2
						.withTitle("家族成员")
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
						.setCustomView(view3, this).show();

				ListView listView = (ListView) view3.findViewById(R.id.list);
				final SingleDmAdapter singleBaseAdapter = new SingleDmAdapter(
						this, txts, "父亲");
				listView.setAdapter(singleBaseAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						singleBaseAdapter.setName(txts[arg2]);
						singleBaseAdapter.notifyDataSetChanged();
						dialogBuilder2.dismiss();
						disease_style_txt.setText(txts[arg2]);
						if (arg2 == 0) {
							name = "a";
						} else if (arg2 == 1){
							name = "b";
						} else if (arg2 == 2){
							name = "c";
						} else if (arg2 == 3){
							name = "d";
						}
					}
				});
			}

		} else if (view.getId() == R.id.ll_history_guahao) {
			if (shuzi == 6) {
				Bundle data = new Bundle();
				data.putInt("shuzi", 6);
				Utils.startActivityForResult(this,
						ErhMultiChoiceActivity.class, data, SIX);
			} else {
				// 初始化Calendar日历对象
				Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
				Date mydate = new Date(); // 获取当前日期Date对象
				mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
				year = mycalendar.get(Calendar.YEAR); // 获取Calendar对象中的年
				month = mycalendar.get(Calendar.MONTH);// 获取Calendar对象中的月
				day = mycalendar.get(Calendar.DAY_OF_MONTH);// 获取这个月的第几天
				DatePickerDialog datePicker = new DatePickerDialog(
						ErhNewAddHistoryActivity.this, new OnDateSetListener() {

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
								timeTxt.setText(year + "/" + (month + 1) + "/"
										+ day);
								time = year + "/" + (month + 1) + "/" + day;
							}
						}, year, month, day);
				datePicker.setIcon(R.drawable.medical_history_ememed2);
				datePicker.setTitle("选择时间");
				datePicker.show();
			}

		} else if (view.getId() == R.id.btn_addhealth) {
			isNull();
			if (shuzi == 6) {
				postEditData2();
			} else {
				postEditData();
			}
			
		}
	}

	private void isNull() {
		
		if (shuzi == 6) {
			if (name == null || name.equals("")) {
				this.showToast("请输入家族成员！");
				return;
			}
			if (time == null || time.equals("")) {
				this.showToast("请输入疾病类型！");
				return;
			}
		} else {
			if (name == null || name.equals("")) {
				this.showToast("请输入疾病类型！");
				return;
			}
			if (time == null || time.equals("")) {
				this.showToast("请输入时间！");
				return;
			}
		}
		
	}

	private void postEditData() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", (shuzi - 1) + "");
			params.put("name", name);
			params.put("eventdate", time);
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.add_medical_history, SetProfile.class, params,
					new Response.Listener() {
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
	
	private void postEditData2() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", (shuzi - 1) + "");
			String name2 = "";
			for (int i = 0; i < family.size(); i++) {
				name2 = name2 + "\"" + family.get(i) + "\"" + ":" + "\"" + familyHistory.get(i) + "\"" + ",";
			}
			String name1 = "{" + name2 + "\"" + name + "\"" + ":" + "\"" + time + "\"" + "}";
			params.put("name", name1);
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.add_yaowu_history, SetProfile.class, params,
					new Response.Listener() {
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

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		try {
			switch (msg.what) {
			case IResult.USER_INFO:
				SetProfile sp = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp.getSuccess() + "");
				Log.e("YY", "www" + sp.getErrormsg() + "");
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						showToast("保存成功");
						setResult(RESULT_OK);
						finish();
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
		if (requestCode == SIX && resultCode == RESULT_OK) {
			ArrayList<String> reList = new ArrayList<String>();
			reList = data.getStringArrayListExtra("reList");
			String str = "";
			for (int i = 0; i < reList.size(); i++) {
				str = str + reList.get(i) + ",";
			}
			str = str.substring(0, str.length() - 1);
			timeTxt.setText(str);
			time = str;
		}
	}
	
	

}
