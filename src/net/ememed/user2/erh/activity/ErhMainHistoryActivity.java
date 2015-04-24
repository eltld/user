package net.ememed.user2.erh.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.dialog.Effectstype;
import net.ememed.user2.dialog.NiftyDialogBuilder;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.erh.adapter.ErhHistoryAdapter;
import net.ememed.user2.erh.adapter.ErhHistoryYaoWuAdapter;
import net.ememed.user2.erh.bean.ErhDiseaseHistory;
import net.ememed.user2.erh.bean.ErhHistoryBean;
import net.ememed.user2.erh.bean.ErhHistoryBean2;
import net.ememed.user2.erh.bean.ErhHistoryBean3;
import net.ememed.user2.erh.ui.MyViewPagerAdapter;
import net.ememed.user2.erh.ui.SyncHorizontalScrollView;
import net.ememed.user2.erh.util.SingleDmAdapter;
import net.ememed.user2.erh.util.Tools;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.util.TextUtil;

/**
 * 病史主界面
 * 
 */
public class ErhMainHistoryActivity extends BasicActivity implements
		OnClickListener {
	private static final int TWO = 0x22;
	private static final int THREE = 0x23;
	private static final int THOUR = 0x24;
	private static final int FIVE = 0x25;
	private static final int SIX = 0x26;
	private static final int SEVEN = 0x31;
	private TextView top_title;
	private SyncHorizontalScrollView mSyncHorizontalScrollView;
	private RelativeLayout rlRadio;
	private RadioGroup rgTabhost;
	private ImageView ivCursor;
	private ViewPager viewPager;
	private ArrayList<View> listViews = new ArrayList<View>();
	private ArrayList<RadioButton> radioButttons = new ArrayList<RadioButton>();
	private int cursorWidth;
	private LayoutInflater inflater;
	private int mCurrentCheckedRadioLeft;
	private int pageCount = 0; // 所在pager
	private ListView lvCustomEvas;
	private ErhHistoryAdapter adapterErhHistory1;
	private ErhHistoryAdapter adapterErhHistory2;
	private ErhHistoryAdapter adapterErhHistory3;
	private ErhHistoryAdapter adapterErhHistory4;
	private ErhHistoryAdapter adapterErhHistory5;
	private ErhHistoryYaoWuAdapter erhHistoryYaoWuAdapter;
	private ErhHistoryYaoWuAdapter erhHistoryYaoWuAdapter1;
	private ErhHistoryYaoWuAdapter erhHistoryYaoWuAdapter2;
	private ErhHistoryYaoWuAdapter erhHistoryYaoWuAdapter4;
	private String[] hys = new String[] { "青霉素", "磺胺", "链霉素", "其它" };
	private String[] hys2 = new String[] { "化学品", "毒物", "射线" };
	private String[] hys3 = new String[] { "视力残疾", "听力残疾", "言语残疾", "肢体残疾",
			"智力残疾", "精神残疾" };
	private String urlPath;
	private String urlDeletePath;
	private String type = 1 + "";
	private NiftyDialogBuilder dialogBuilder;
	private Effectstype effect;
	private List<String> medicalList1 = new ArrayList<String>();
	private List<String> medicalList2 = new ArrayList<String>();
	private List<String> medicalList3 = new ArrayList<String>();
	private List<String> medicalList4 = new ArrayList<String>();
	private int year;
	private int month;
	private int day;
	private RadioButton radioButton;
	private List<String> family = new ArrayList<String>();
	private List<String> familyHistory = new ArrayList<String>();
	private List<ErhDiseaseHistory> idsMedicalHistory = new ArrayList<ErhDiseaseHistory>();
	private List<ErhDiseaseHistory> idsOperation = new ArrayList<ErhDiseaseHistory>();
	private List<ErhDiseaseHistory> idsInjury = new ArrayList<ErhDiseaseHistory>();
	private List<ErhDiseaseHistory> idsTransfuse = new ArrayList<ErhDiseaseHistory>();
	private boolean isDele = false;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.erh_main_history);
		dialogBuilder = new NiftyDialogBuilder(this, R.style.dialog_untran);
		initView();
		initTabhost();
		initViewPager();
		initFirstPage();
	}

	private void initFirstPage() {
		pageCount = 0;
		radioButttons.get(0).setChecked(true);
		moveCursor(0);
		setType(0);
		getListView(0);
		getPage(0);
	}

	private void initView() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		mSyncHorizontalScrollView = (SyncHorizontalScrollView) findViewById(R.id.shsv);
		rlRadio = (RelativeLayout) findViewById(R.id.rl_radio);
		rgTabhost = (RadioGroup) findViewById(R.id.rg_tabhost);
		ivCursor = (ImageView) findViewById(R.id.iv_cursor);
		inflater = LayoutInflater.from(this);
		// 添加按钮
		View searchButton = super.findViewById(R.id.toptitle_img_right);
		searchButton.setVisibility(View.VISIBLE);
	}

	/**
	 * 标签栏
	 */
	private void initTabhost() {
		listViews.clear();
		radioButttons.clear();
		rgTabhost.removeAllViews();

		cursorWidth = MyApplication.getScreenWidth()
				/ Math.min(mSyncHorizontalScrollView.tabSize, 8);

		LayoutParams cursor_Params = ivCursor.getLayoutParams();
		cursor_Params.width = cursorWidth;
		ivCursor.setLayoutParams(cursor_Params);
		mSyncHorizontalScrollView.setSomeParam(rlRadio, this);

		for (int i = 0; i < 9; i++) {
			radioButton = (RadioButton) inflater.inflate(
					R.layout.erh_information_tabhost_item, null);
			radioButton.setId(i);
			setRadioText(i);

			radioButton.setLayoutParams(new LayoutParams(cursorWidth,
					LayoutParams.FILL_PARENT));
			radioButton.setOnClickListener(this);
			radioButttons.add(radioButton);
			rgTabhost.addView(radioButton);

			listViews.add(inflater.inflate(
					R.layout.layout_listview_pull_refresh, null));
		}

		mSyncHorizontalScrollView.showOrHideArrow();
	}

	private void setRadioText(int i) {
		if (i == 0) {
			radioButton.setText("药物过敏史");
		} else if (i == 1) {
			radioButton.setText("暴露史");
		} else if (i == 2) {
			radioButton.setText("过往疾病史");
		} else if (i == 3) {
			radioButton.setText("过往手术史");
		} else if (i == 4) {
			radioButton.setText("过往外伤史");
		} else if (i == 5) {
			radioButton.setText("过往输血史");
		} else if (i == 6) {
			radioButton.setText("家族史");
		} else if (i == 7) {
			radioButton.setText("遗传病史");
		} else if (i == 8) {
			radioButton.setText("障碍史");
		}
	}

	private LayoutAnimationController getListAnim() {
		AnimationSet set = new AnimationSet(true);
		Animation animation = new AlphaAnimation(0.0f, 1.0f);
		animation.setDuration(300);
		set.addAnimation(animation);

		animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
				-1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(500);
		set.addAnimation(animation);
		LayoutAnimationController controller = new LayoutAnimationController(
				set, 0.5f);
		return controller;
	}

	/**
	 * 初化底部ViewPager
	 */
	private void initViewPager() {
		MyViewPagerAdapter vpa = new MyViewPagerAdapter(listViews);
		viewPager.setAdapter(vpa);
		viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			public void onPageScrolled(int i, float f, int i1) {
			}

			public void onPageScrollStateChanged(int i) {
			}

			public void onPageSelected(int i) {
				pageCount = i;
				radioButttons.get(i).setChecked(true);
				moveCursor(i);
				setType(i);
				getListView(i);
				getPage(i);

			}
		});
	}

	protected void getListView(int i) {
		lvCustomEvas = (ListView) listViews.get(i);
	}

	protected void getPage(int i) {
		if (i == 2 && adapterErhHistory1 == null) {
			getInformationByType(i);
		} else if (i == 3 && adapterErhHistory2 == null) {
			getInformationByType(i);
		} else if (i == 4 && adapterErhHistory3 == null) {
			getInformationByType(i);
		} else if (i == 5 && adapterErhHistory4 == null) {
			getInformationByType(i);
		} else if (i == 0 && erhHistoryYaoWuAdapter == null) {
			getInformationByType(i);
		} else if (i == 1 && erhHistoryYaoWuAdapter1 == null) {
			getInformationByType(i);
		} else if (i == 7 && erhHistoryYaoWuAdapter4 == null) {
			getInformationByType(i);
		} else if (i == 6 && adapterErhHistory5 == null) {
			getInformationByType(i);
		} else if (i == 8 && erhHistoryYaoWuAdapter2 == null) {
			getInformationByType(i);
		}
	}

	protected void setType(int i) {
		if (i == 2) {
			type = 1 + "";
		} else if (i == 3) {
			type = 2 + "";
		} else if (i == 4) {
			type = 3 + "";
		} else if (i == 5) {
			type = 4 + "";
		} else if (i == 0) {
			type = 1 + "";
		} else if (i == 1) {
			type = 4 + "";
		} else if (i == 7) {
			type = 2 + "";
		} else if (i == 6) {
			type = 5 + "";
		} else if (i == 8) {
			type = 3 + "";
		}
	}

	protected void getInformationByType(int index) {		
		if (index == 2) {
			adapterErhHistory1 = new ErhHistoryAdapter(
					ErhMainHistoryActivity.this, null, 2);
			lvCustomEvas.setAdapter(adapterErhHistory1);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();
									String idStr = idsMedicalHistory.get(position).getID();
									urlDeletePath = HttpUtil.delete_history_medical;
									deleteDisease(urlDeletePath, type, idStr);									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
					
				}
			});
			urlPath = HttpUtil.get_medical_history;
			getServiceData(urlPath, type);

		} else if (index == 0) {
			erhHistoryYaoWuAdapter = new ErhHistoryYaoWuAdapter(
					ErhMainHistoryActivity.this, null, 0);
			lvCustomEvas.setAdapter(erhHistoryYaoWuAdapter);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			urlPath = HttpUtil.get_yaowu_history;
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();									
									String postData = "";
									if (medicalList1 != null
											&& medicalList1
													.size() != 0) {
										medicalList1.remove(position);
										showToast("删除成功");
										isDele = true;
										for (int i = 0; i < medicalList1
												.size(); i++) {
											postData = postData
													+ medicalList1
															.get(i)
													+ "|";
										}
										postData = postData.substring(0, postData.length() - 1);
										postServiceData(postData);
									} 
									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
				}
			});
			getServiceData2(urlPath, type);

		} else if (index == 1) {
			erhHistoryYaoWuAdapter1 = new ErhHistoryYaoWuAdapter(
					ErhMainHistoryActivity.this, null, 1);
			lvCustomEvas.setAdapter(erhHistoryYaoWuAdapter1);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			urlPath = HttpUtil.get_yaowu_history;
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();									
									String postData = "";
									if (medicalList2 != null
											&& medicalList2
													.size() != 0) {
										medicalList2.remove(position);
										showToast("删除成功");
										isDele = true;
										for (int i = 0; i < medicalList2
												.size(); i++) {
											postData = postData
													+ medicalList2
															.get(i)
													+ "|";
										}
										postData = postData.substring(0, postData.length() - 1);
										postServiceData(postData);
									} 
									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
				}
			});
			getServiceData2(urlPath, type);

		} else if (index == 3) {
			adapterErhHistory2 = new ErhHistoryAdapter(
					ErhMainHistoryActivity.this, null, 3);
			lvCustomEvas.setAdapter(adapterErhHistory2);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			urlPath = HttpUtil.get_medical_history;
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();
									String idStr = idsOperation.get(position).getID();
									urlDeletePath = HttpUtil.delete_history_medical;
									deleteDisease(urlDeletePath, type, idStr);									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
					
				}
			});
			getServiceData(urlPath, type);

		} else if (index == 4) {
			adapterErhHistory3 = new ErhHistoryAdapter(
					ErhMainHistoryActivity.this, null, 4);
			lvCustomEvas.setAdapter(adapterErhHistory3);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			urlPath = HttpUtil.get_medical_history;
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();
									String idStr = idsInjury.get(position).getID();
									urlDeletePath = HttpUtil.delete_history_medical;
									deleteDisease(urlDeletePath, type, idStr);									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
					
				}
			});
			getServiceData(urlPath, type);

		} else if (index == 5) {
			adapterErhHistory4 = new ErhHistoryAdapter(
					ErhMainHistoryActivity.this, null, 5);
			lvCustomEvas.setAdapter(adapterErhHistory4);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			urlPath = HttpUtil.get_medical_history;
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();
									String idStr = idsTransfuse.get(position).getID();
									urlDeletePath = HttpUtil.delete_history_medical;
									deleteDisease(urlDeletePath, type, idStr);									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
					
				}
			});
			getServiceData(urlPath, type);

		} else if (index == 7) {
			erhHistoryYaoWuAdapter4 = new ErhHistoryYaoWuAdapter(
					ErhMainHistoryActivity.this, null, 4);
			lvCustomEvas.setAdapter(erhHistoryYaoWuAdapter4);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			urlPath = HttpUtil.get_yaowu_history;
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();									
									String postData = "";
									if (medicalList4 != null
											&& medicalList4
													.size() != 0) {
										medicalList4.remove(position);
										showToast("删除成功");
										isDele = true;
										for (int i = 0; i < medicalList4
												.size(); i++) {
											postData = postData
													+ medicalList4
															.get(i)
													+ "|";
										}
										postData = postData.substring(0, postData.length() - 1);
										postServiceData(postData);
									} 
									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
				}
			});
			getServiceData2(urlPath, type);

		} else if (index == 6) {
			adapterErhHistory5 = new ErhHistoryAdapter(
					ErhMainHistoryActivity.this, null, 6);
			lvCustomEvas.setAdapter(adapterErhHistory5);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();									
									String postData = "";
									
									family.remove(position);
									familyHistory.remove(position);
									postEditData2();
									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
				}
			});
			urlPath = HttpUtil.get_yaowu_history;
			getServiceData2(urlPath, type);

		} else if (index == 8) {
			erhHistoryYaoWuAdapter2 = new ErhHistoryYaoWuAdapter(
					ErhMainHistoryActivity.this, null, 2);
			lvCustomEvas.setAdapter(erhHistoryYaoWuAdapter2);
			lvCustomEvas.setLayoutAnimation(getListAnim());
			urlPath = HttpUtil.get_yaowu_history;
			lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

				@Override
				public boolean onItemLongClick(AdapterView<?> parent, View view,
						final int position, long id) {
					effect = Effectstype.Slidetop;

					dialogBuilder
							.withTitle("删除")
							// .withTitle(null) no title
							.withTitleColor("#000000")
							// def
							// .withDividerColor("#11000000")
							// def
							.withMessage("是否删除")
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
							.withButton1Text("删除")
							.withButton2Text("取消")
							.setButton1Click(new OnClickListener() {
								
								@Override
								public void onClick(View view) {
									dialogBuilder.dismiss();									
									String postData = "";
									if (medicalList3 != null
											&& medicalList3
													.size() != 0) {
										medicalList3.remove(position);
										showToast("删除成功");
										isDele = true;
										for (int i = 0; i < medicalList3
												.size(); i++) {
											postData = postData
													+ medicalList3
															.get(i)
													+ "|";
										}
										postData = postData.substring(0, postData.length() - 1);
										postServiceData(postData);
									} 
									
								}
							})
							.setButton2Click(new OnClickListener() {
								
								@Override
								public void onClick(View arg0) {
									dialogBuilder.dismiss();
								}
							}).show();
					return false;
				}
			});
			getServiceData2(urlPath, type);

		}

	}
	
	protected void deleteDisease(String urlPath, String type, String idStr) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", type);
			params.put("id", idStr);
			
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(urlPath,
					SetProfile.class, params, new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = IResult.DELETE_DATA;
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

	private void getServiceData(String urlPath, String type) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", type);

			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(urlPath,
					ErhHistoryBean.class, params, new Response.Listener() {
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

	private void getServiceData2(String urlPath, String type) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", type);

			Log.e("YY", params.toString());
			if (pageCount == 6) {
				MyApplication.volleyHttpClient.postWithParams(urlPath,
						ErhHistoryBean3.class, params, new Response.Listener() {
							@Override
							public void onResponse(Object response) {
								Message message = new Message();
								message.obj = response;
								message.what = 0x33;
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
				MyApplication.volleyHttpClient.postWithParams(urlPath,
						ErhHistoryBean2.class, params, new Response.Listener() {
							@Override
							public void onResponse(Object response) {
								Message message = new Message();
								message.obj = response;
								message.what = 0x32;
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
			}

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
				final ErhHistoryBean response = (ErhHistoryBean) msg.obj;
				Log.e("YY", "www" + response.getSuccess() + "");
				Log.e("YY", "www" + response.getErrormsg() + "");
				if (response.getErrormsg().equals("暂无该病史信息")) {
					if (pageCount == 2) {
						adapterErhHistory1.change(null);
						idsMedicalHistory.clear();
					} else if(pageCount == 3){
						adapterErhHistory2.change(null);
						idsOperation.clear();
					} else if (pageCount == 4) {
						adapterErhHistory3.change(response.getData());
						idsInjury.clear();
					} else if (pageCount == 5) {
						adapterErhHistory4.change(response.getData());
						idsTransfuse.clear();
					}
					
				}
				if (null != response) {
					if (response.getSuccess() == 1) {
						if (pageCount == 2) {
							adapterErhHistory1.change(response.getData());
							idsMedicalHistory.clear();
							idsMedicalHistory = response.getData();
						} else if (pageCount == 3) {
							adapterErhHistory2.change(response.getData());
							idsOperation.clear();
							idsOperation = response.getData(); 
						} else if (pageCount == 4) {
							adapterErhHistory3.change(response.getData());
							idsInjury.clear();
							idsInjury = response.getData();
						} else if (pageCount == 5) {
							adapterErhHistory4.change(response.getData());
							idsTransfuse.clear();
							idsTransfuse = response.getData();
						}

					}
				}

				break;
			case SEVEN:
				SetProfile sp = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp.getSuccess() + "");
				Log.e("YY", "www" + sp.getErrormsg() + "");
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						if (!isDele) {
							showToast("保存成功");
							isDele = false;
						}					
						urlPath = HttpUtil.get_yaowu_history;
						getServiceData2(urlPath, type);
					} else {
						if (!isDele) {
							showToast("保存失败");
							isDele = false;
						}							
					}
				}
				break;
			case 0x32:
				final ErhHistoryBean2 response2 = (ErhHistoryBean2) msg.obj;
				Log.e("YY", "www" + response2.getSuccess() + "");
				Log.e("YY", "www" + response2.getErrormsg() + "");
				if (null != response2) {
					if (response2.getSuccess() == 1) {
						if (pageCount == 0) {
							if (response2.getData().getNAME() != null
									&& response2.getData().getNAME().length() != 0) {
								String[] strs = response2.getData().getNAME()
										.split("\\|");
								medicalList1.clear();
								for (int i = 0; i < strs.length; i++) {
									medicalList1.add(strs[i]);
								}
								erhHistoryYaoWuAdapter.change(medicalList1);
							}
						} else if (pageCount == 1) {
							if (response2.getData().getNAME() != null
									&& response2.getData().getNAME().length() != 0) {
								String[] strs = response2.getData().getNAME()
										.split("\\|");
								medicalList2.clear();
								for (int i = 0; i < strs.length; i++) {
									medicalList2.add(strs[i]);
								}
								erhHistoryYaoWuAdapter1.change(medicalList2);
							}
						} else if (pageCount == 7) {
							if (response2.getData().getNAME() != null
									&& response2.getData().getNAME().length() != 0) {
								String[] strs = response2.getData().getNAME()
										.split("\\|");
								medicalList4.clear();
								for (int i = 0; i < strs.length; i++) {
									medicalList4.add(strs[i]);
								}
								erhHistoryYaoWuAdapter4.change(medicalList4);
							}
						} else if (pageCount == 8) {
							Log.e("XX", "来过这里1111");
							if (response2.getData().getNAME() != null
									&& response2.getData().getNAME().length() != 0) {
								Log.e("XX", "来过这里2222");
								String[] strs = response2.getData().getNAME()
										.split("\\|");
								Log.e("XX", "来过这里3333");
								medicalList3.clear();
								for (int i = 0; i < strs.length; i++) {
									medicalList3.add(strs[i]);
								}
								erhHistoryYaoWuAdapter2.change(medicalList3);
							}
						}
					}

				}
				break;
			case 0x33:
				final ErhHistoryBean3 response3 = (ErhHistoryBean3) msg.obj;
				Log.e("YY", "www" + response3.getSuccess() + "");
				Log.e("YY", "www" + response3.getErrormsg() + "");
				if (null != response3) {
					if (response3.getSuccess() == 1) {
						Log.e("XX", response3.getData().getID());
						if (family.size() == 0) {
							adapterErhHistory5.change(null);
						}
						if (response3.getData().getNAME() != null) {
							List<ErhDiseaseHistory> listItems = new ArrayList<ErhDiseaseHistory>();
							family.clear();
							familyHistory.clear();
							if (response3.getData().getNAME().getA() != null) {
								ErhDiseaseHistory diseaseHistory = new ErhDiseaseHistory();
								diseaseHistory.setNAME("父亲");
								diseaseHistory.setEVENTDATE(response3.getData()
										.getNAME().getA());
								family.add("a");
								familyHistory.add(response3.getData().getNAME()
										.getA());
								listItems.add(diseaseHistory);
							}
							if (response3.getData().getNAME().getB() != null) {
								ErhDiseaseHistory diseaseHistory = new ErhDiseaseHistory();
								diseaseHistory.setNAME("母亲");
								diseaseHistory.setEVENTDATE(response3.getData()
										.getNAME().getB());
								family.add("b");
								familyHistory.add(response3.getData().getNAME()
										.getB());
								listItems.add(diseaseHistory);
							}
							if (response3.getData().getNAME().getC() != null) {
								ErhDiseaseHistory diseaseHistory = new ErhDiseaseHistory();
								diseaseHistory.setNAME("子女");
								diseaseHistory.setEVENTDATE(response3.getData()
										.getNAME().getC());
								family.add("c");
								familyHistory.add(response3.getData().getNAME()
										.getC());
								listItems.add(diseaseHistory);
							}
							if (response3.getData().getNAME().getD() != null) {
								ErhDiseaseHistory diseaseHistory = new ErhDiseaseHistory();
								diseaseHistory.setNAME("兄弟姐妹");
								diseaseHistory.setEVENTDATE(response3.getData()
										.getNAME().getD());
								family.add("d");
								familyHistory.add(response3.getData().getNAME()
										.getD());
								listItems.add(diseaseHistory);
							}
							adapterErhHistory5.change(listItems);
						}
					}
				}
				break;
			case IResult.DELETE_DATA:
				SetProfile sp2 = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp2.getSuccess() + "");
				Log.e("YY", "www" + sp2.getErrormsg() + "");
				if (null != sp2) {
					if (sp2.getSuccess() == 1) {
						Log.e("YY", "www" + "是否删除");
						showToast("删除成功");
//						if (pageCount == 2) {
//							Log.e("YY", "www" + "请求数据");
							urlPath = HttpUtil.get_medical_history;
							Log.e("YY", "www" + "请求数据2");
							getServiceData(urlPath, type);
//						} else if (pageCount == 3){
//							
//						}
						
					} else {
						showToast("删除失败");
					}
				}
				break;
			case 0x24:
				SetProfile sp3 = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp3.getSuccess() + "");
				Log.e("YY", "www" + sp3.getErrormsg() + "");
				if (null != sp3) {
					if (sp3.getSuccess() == 1) {
						showToast("删除成功");
						urlPath = HttpUtil.get_yaowu_history;
						getServiceData2(urlPath, type);
					} else {
						showToast("删除失败");
					}
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		super.onResult(msg);
	}

	/**
	 * 获取二级栏目的当前左边位置
	 * 
	 * @param index
	 * @return
	 */
	private int getTabhostChildLeft(int index) {
		int left = 0;
		while (index-- > 0) {
			left += cursorWidth;
		}
		return left;
	}

	/**
	 * 移动下划光标
	 * 
	 * @param checkedId
	 */
	private void moveCursor(int checkedId) {
		try {
			if (rgTabhost != null && rgTabhost.getChildCount() > 0
					&& rgTabhost.getChildAt(checkedId) != null) {
				int left = getTabhostChildLeft(checkedId);
				TranslateAnimation animation = new TranslateAnimation(
						mCurrentCheckedRadioLeft, left, 0f, 0f);
				animation.setInterpolator(new LinearInterpolator());
				animation.setDuration(100);
				animation.setFillAfter(true);
				ivCursor.startAnimation(animation);
				mCurrentCheckedRadioLeft = left;
				smoothScrollTo(checkedId);
			}
		} catch (Exception e) {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	/**
	 * 二级栏目SyncHorizontalScrollView滑动相应位置
	 * 
	 * @param index
	 */
	private void smoothScrollTo(int index) {
		index = index - 3;
		int left = 0;
		while (index-- > 0) {
			left += cursorWidth;
		}
		if (left > 0)
			left += Tools.dip2px(5);
		final int scrollX = left;
		Utils.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				mSyncHorizontalScrollView.smoothScrollTo(scrollX, 0);
			}
		}, 500);
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("个人病史资料");
		super.setupView();
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.toptitle_img_right) {

			if (pageCount == 0) {

				View view2 = LayoutInflater.from(ErhMainHistoryActivity.this)
						.inflate(R.layout.custom_view, null);
				effect = Effectstype.Slideleft;

				dialogBuilder
						.withTitle("药物过敏")
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
						this, hys, "青霉素");
				listView.setAdapter(singleBaseAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						singleBaseAdapter.setName(hys[arg2]);
						singleBaseAdapter.notifyDataSetChanged();
						dialogBuilder.dismiss();
						if (arg2 == 3) {
							final NiftyDialogBuilder dialogBuilder2 = new NiftyDialogBuilder(
									ErhMainHistoryActivity.this,
									R.style.dialog_untran);
							effect = Effectstype.Flipv;
							View view2 = LayoutInflater.from(
									ErhMainHistoryActivity.this).inflate(
									R.layout.erh_person_ids, null);
							final EditText idsExit = (EditText) view2
									.findViewById(R.id.name);
							dialogBuilder2
									.withTitle("过敏药物")
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
											getResources()
													.getDrawable(
															R.drawable.medical_history_ememed2))
									.isCancelableOnTouchOutside(true)
									// def |
									// isCancelable(true)
									.withDuration(700)
									// def
									.withEffect(effect)
									// def Effectstype.Slidetop
									.setCustomView(view2,
											ErhMainHistoryActivity.this)
									.withButton1Text("确定")
									// def
									// gone
									.withButton2Text("取消")
									// def gone
									// .setCustomView(R.layout.custom_view,
									// MainMeActivity.this)
									// // .setCustomView(View
									// or
									// ResId,context)
									.setButton1Click(
											new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													String idStr = idsExit
															.getText()
															.toString().trim();
													if (idStr == null
															|| idStr.equals("")) {
														showToast("请填写内容");
														return;
													}
													dialogBuilder2.dismiss();
													String postData = "";
													if (medicalList1 != null
															&& medicalList1
																	.size() != 0) {
														for (int i = 0; i < medicalList1
																.size(); i++) {
															postData = postData
																	+ medicalList1
																			.get(i)
																	+ "|";
														}
														postData = postData
																+ idStr;
													} else {
														postData = idStr;
													}

													postServiceData(postData);
												}
											})
									.setButton2Click(
											new View.OnClickListener() {
												@Override
												public void onClick(View v) {
													dialogBuilder2.dismiss();
												}
											}).show();
						} else {

							String postData = "";
							if (medicalList1 != null
									&& medicalList1.size() != 0) {
								for (int i = 0; i < medicalList1.size(); i++) {
									postData = postData + medicalList1.get(i)
											+ "|";
								}
								postData = postData + hys[arg2];
							} else {
								postData = hys[arg2];
							}

							postServiceData(postData);
						}

					}
				});
			} else if (pageCount == 1) {
				View view2 = LayoutInflater.from(ErhMainHistoryActivity.this)
						.inflate(R.layout.custom_view, null);
				effect = Effectstype.Slideleft;

				dialogBuilder
						.withTitle("暴露史")
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
						this, hys2, "化学品");
				listView.setAdapter(singleBaseAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						singleBaseAdapter.setName(hys2[arg2]);
						singleBaseAdapter.notifyDataSetChanged();
						dialogBuilder.dismiss();
						String postData = "";
						if (medicalList2 != null && medicalList2.size() != 0) {
							for (int i = 0; i < medicalList2.size(); i++) {
								postData = postData + medicalList2.get(i) + "|";
							}
							postData = postData + hys2[arg2];
						} else {
							postData = hys2[arg2];
						}
						postServiceData(postData);
					}
				});
			} else if (pageCount == 2) { // 过往疾病史
				Bundle data = new Bundle();
				data.putInt("shuzi", 2);
				Utils.startActivityForResult(this,
						ErhNewAddHistoryActivity.class, data, TWO);
			} else if (pageCount == 3) {
				Bundle data = new Bundle();
				data.putInt("shuzi", 3);
				Utils.startActivityForResult(this,
						ErhNewAddHistoryActivity.class, data, THREE);
			} else if (pageCount == 4) {
				Bundle data = new Bundle();
				data.putInt("shuzi", 4);
				Utils.startActivityForResult(this,
						ErhNewAddHistoryActivity.class, data, THOUR);
			} else if (pageCount == 5) {
				Bundle data = new Bundle();
				data.putInt("shuzi", 5);
				Utils.startActivityForResult(this,
						ErhNewAddHistoryActivity.class, data, FIVE);
			} else if (pageCount == 6) {
				Bundle data = new Bundle();
				data.putInt("shuzi", 6);
				data.putStringArrayList("family", (ArrayList<String>) family);
				data.putStringArrayList("familyHistory",
						(ArrayList<String>) familyHistory);
				Utils.startActivityForResult(this,
						ErhNewAddHistoryActivity.class, data, SIX);
			} else if (pageCount == 7) {
				effect = Effectstype.Flipv;
				View view2 = LayoutInflater.from(this).inflate(
						R.layout.erh_person_ids, null);
				final EditText idsExit = (EditText) view2
						.findViewById(R.id.name);
				dialogBuilder
						.withTitle("遗传病史")
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
								dialogBuilder.dismiss();
								String postData = "";
								if (medicalList4 != null
										&& medicalList4.size() != 0) {
									for (int i = 0; i < medicalList4.size(); i++) {
										postData = postData
												+ medicalList4.get(i) + "|";
									}
									postData = postData + idStr;
								} else {
									postData = idStr;
								}

								postServiceData(postData);

							}
						}).setButton2Click(new View.OnClickListener() {
							@Override
							public void onClick(View v) {
								dialogBuilder.dismiss();
							}
						}).show();
			} else if (pageCount == 8) {
				View view2 = LayoutInflater.from(ErhMainHistoryActivity.this)
						.inflate(R.layout.custom_view, null);
				effect = Effectstype.Slideleft;

				dialogBuilder
						.withTitle("障碍史")
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
						this, hys3, "视力残疾");
				listView.setAdapter(singleBaseAdapter);
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						singleBaseAdapter.setName(hys3[arg2]);
						singleBaseAdapter.notifyDataSetChanged();
						dialogBuilder.dismiss();
						String postData = "";
						if (medicalList3 != null && medicalList3.size() != 0) {
							for (int i = 0; i < medicalList3.size(); i++) {
								postData = postData + medicalList3.get(i) + "|";
							}
							postData = postData + hys3[arg2];
						} else {
							postData = hys3[arg2];
						}

						postServiceData(postData);
					}
				});
			}
		}
	}

	/**
	 * 上传数据
	 * 
	 * @param name
	 *            上传字符串
	 */
	private void postServiceData(String name) {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", type + "");
			params.put("name", name);
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.add_yaowu_history, SetProfile.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = SEVEN;
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
	public void onClick(View v) {
		int indexOf = radioButttons.indexOf(v);
		if (indexOf != -1) {
			viewPager.setCurrentItem(indexOf);
		}
	}
	
	private void postEditData2() {
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("type", 5 + "");
			String name2 = "";
			String name1 = "";
			if (family.size() != 0) {
				for (int i = 0; i < family.size(); i++) {
					name2 = name2 + "\"" + family.get(i) + "\"" + ":" + "\""
							+ familyHistory.get(i) + "\"" + ",";
				}
			}
			if (!TextUtils.isEmpty(name2)) {
				name2 = name2.substring(0, name2.length() - 1);
				name1 = "{" + name2 + "}";
			} else {
				name1 = "{" + null + "}";
			}
			params.put("name", name1);
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(
					HttpUtil.add_yaowu_history, SetProfile.class, params,
					new Response.Listener() {
						@Override
						public void onResponse(Object response) {
							Message message = new Message();
							message.obj = response;
							message.what = 0x24;
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
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == TWO && resultCode == RESULT_OK) {
			urlPath = HttpUtil.get_medical_history;
			type = 1 + "";
			getServiceData(urlPath, type);
		} else if (requestCode == THREE && resultCode == RESULT_OK) {
			urlPath = HttpUtil.get_medical_history;
			type = 2 + "";
			getServiceData(urlPath, type);
		} else if (requestCode == THOUR && resultCode == RESULT_OK) {
			urlPath = HttpUtil.get_medical_history;
			type = 3 + "";
			getServiceData(urlPath, type);
		} else if (requestCode == FIVE && resultCode == RESULT_OK) {
			urlPath = HttpUtil.get_medical_history;
			type = 4 + "";
			getServiceData(urlPath, type);
		} else if (requestCode == SIX && resultCode == RESULT_OK) {
			type = 5 + "";
			urlPath = HttpUtil.get_yaowu_history;
			getServiceData2(urlPath, type);
		}
	}

}
