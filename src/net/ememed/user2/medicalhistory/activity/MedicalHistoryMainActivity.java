package net.ememed.user2.medicalhistory.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.qq.wx.voice.recognizer.VoiceRecognizer;
import com.qq.wx.voice.recognizer.VoiceRecognizerListener;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult;
import com.qq.wx.voice.recognizer.VoiceRecognizerResult.Word;
import com.qq.wx.voice.recognizer.VoiceRecordState;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.SetProfile;
import net.ememed.user2.medicalhistory.Utils;
import net.ememed.user2.medicalhistory.adapter.MedicalHistoryAdapter;
import net.ememed.user2.medicalhistory.bean.ImageUrlBean;
import net.ememed.user2.medicalhistory.bean.MedicalHistory;
import net.ememed.user2.medicalhistory.bean.MedicalHistoryBean;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import net.ememed.user2.widget.MenuDialog;
import net.ememed.user2.widget.RefreshListView;
import net.ememed.user2.widget.RefreshListView.IOnLoadMoreListener;
import net.ememed.user2.widget.RefreshListView.IOnRefreshListener;

public class MedicalHistoryMainActivity extends BasicActivity implements OnRefreshListener, VoiceRecognizerListener {
	private TextView top_title;
	private RefreshListView lvCustomEvas;
	private MedicalHistoryAdapter adapter;
//	private TextView addTextView;
	private ImageView addImageView;
	private EditText searchEdit;
	private LinearLayout layout;
	private boolean refresh = true;
	private int totalpages = 1;
	private int page = 1;
	public MenuDialog alertDialog;
	private Button btn_addhealth;
	private ArrayList<String> ids = new ArrayList<String>();
	private List<String> picture_ids = new ArrayList<String>();
	private List<Integer> picture_ids_list = new ArrayList<Integer>();
	private String keyword;
	private boolean keyBool = false;
	private LinearLayout ll_empty;
	private Button voice;
	//定时器
	private Timer frameTimer;
	private TimerTask frameTask;
	//表示目前所处的状态 0:空闲状态，可进行识别； 1：正在进行录音; 2：者处于语音识别; 3：处于取消状态
	private int mRecoState = 0;
	int mInitSucc = 0;
	private PopupWindow mPop;
	private View layout1;
	private Button goMailBtn = null;
	private String mRecognizerResult;
	private final int mMicNum = 8;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.medical_history_book);
		initView();
		initGetData();
	}

	private void initView() {
		lvCustomEvas = (RefreshListView) findViewById(R.id.lv_contact_class);
		searchEdit = (EditText) findViewById(R.id.et_disease_keyword);
		ll_empty = (LinearLayout) findViewById(R.id.ll_empty);
		voice = (Button) findViewById(R.id.complete);
		
		searchEdit.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_ENTER) {// 修改回车键功能
					// 先隐藏键盘
					((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(MedicalHistoryMainActivity.this.getCurrentFocus().getWindowToken(),
							InputMethodManager.HIDE_NOT_ALWAYS);
					keyword = searchEdit.getText().toString().trim();
					keyBool = true;
					refresh = true;
					page = 1;
					initGetData();
				}
				return false;
			}
		});
//		addTextView = (TextView) findViewById(R.id.medical_history_add_text);
		addImageView = (ImageView) findViewById(R.id.medical_history_add_image);
		layout = (LinearLayout) findViewById(R.id.medical_history_add);

		// 添加按钮
		View searchButton = super.findViewById(R.id.toptitle_img_right);
		searchButton.setVisibility(View.VISIBLE);

		adapter = new MedicalHistoryAdapter(MedicalHistoryMainActivity.this, null);
		lvCustomEvas.setAdapter(adapter);

		lvCustomEvas.setOnRefreshListener(new IOnRefreshListener() {

			@Override
			public void OnRefresh() {
				refresh();
			}
		});
		lvCustomEvas.setOnLoadMoreListener(new IOnLoadMoreListener() {

			@Override
			public void OnLoadMore() {
				loadMore();
			}
		});

		lvCustomEvas.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
				final Context dialogContext = new ContextThemeWrapper(MedicalHistoryMainActivity.this, android.R.style.Theme_Light);
				String[] choices = new String[2];
				choices[0] = "删除";
				choices[1] = "取消";

				MenuDialog.Builder builder = new MenuDialog.Builder(dialogContext);
				alertDialog = builder.setTitle("删除病历").setItems(choices, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switch (which) {
						case 0:
							canelCase(position);
							break;
						case 1:

							break;
						}
					}
				}).create();
				alertDialog.setCanceledOnTouchOutside(true);
				alertDialog.show();
				return false;
			}
		});

		lvCustomEvas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				MedicalHistory medicalHistory = (MedicalHistory) adapter.getItem(position - lvCustomEvas.getHeaderViewsCount());
				Bundle bundle = new Bundle();
				Log.e("ID", "ID" + medicalHistory.getID());
				if (!TextUtils.isEmpty(medicalHistory.getID())) {
					bundle.putString("ID", medicalHistory.getID());
				} else {
					bundle.putString("ID", "");
				}
				String page1 = null;
				if ((position - lvCustomEvas.getHeaderViewsCount() + 1) % 20 != 0) {
					page1 = (position / 20 + 1) + "";
				} else {
					page1 = (position / 20) + "";
				}

				bundle.putString("page", page1);
				if (!TextUtils.isEmpty(medicalHistory.getTIME())) {
					bundle.putString("time", medicalHistory.getTIME());
				} else {
					bundle.putString("time", "");
				}
				if (!TextUtils.isEmpty(medicalHistory.getMECHANISM())) {
					bundle.putString("hospital", medicalHistory.getMECHANISM());
				} else {
					bundle.putString("hospital", "");
				}
				if (!TextUtils.isEmpty(medicalHistory.getNAME())) {
					bundle.putString("keshi", medicalHistory.getNAME());
				} else {
					bundle.putString("keshi", "");
				}
				if (!TextUtils.isEmpty(medicalHistory.getCONTENT())) {
					bundle.putString("zhenzhuang", medicalHistory.getCONTENT());
				} else {
					bundle.putString("zhenzhuang", "");
				}
				List<ImageUrlBean> imageUrlBean = ((MedicalHistory) adapter.getItem(position - lvCustomEvas.getHeaderViewsCount())).getATTACHMENTS();
				Log.e("XX:", "imageUrlBean.size():" + imageUrlBean.size());
				List<String> pictures = new ArrayList<String>();
				List<String> pictures2 = new ArrayList<String>();
				List<String> pictures3 = new ArrayList<String>();
				List<String> contents = new ArrayList<String>();
				List<String> times = new ArrayList<String>();
				if (imageUrlBean.size() != 0) {
					for (int i = 0; i < imageUrlBean.size(); i++) {

						String ATTACHMENT_ID = null;

						ATTACHMENT_ID = imageUrlBean.get(i).getATTACHMENT_ID();
						ids.add(ATTACHMENT_ID);

					}

				}

				bundle.putStringArrayList("ids", (ArrayList<String>) ids);

				Utils.startActivity(MedicalHistoryMainActivity.this, MedicalHistoryYuLan.class, bundle);
			}
		});
	}

	public String unicodeToGB(String s) {
		StringBuffer sb = new StringBuffer();
		StringTokenizer st = new StringTokenizer(s, "\\u");
		while (st.hasMoreTokens()) {
			sb.append((char) Integer.parseInt(st.nextToken(), 16));
		}
		return sb.toString();
	}

	private void canelCase(int position) {
		MedicalHistory medicalHistory = (MedicalHistory) adapter.getItem(position - lvCustomEvas.getHeaderViewsCount());
		String id = medicalHistory.getID();
		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("id", id);
			Log.e("id:", params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.cancel_bingli, SetProfile.class, params, new Response.Listener() {
				@Override
				public void onResponse(Object response) {
					Message message = new Message();
					message.obj = response;
					message.what = 0x28;
					handler.sendMessage(message);
				}
			}, new Response.ErrorListener() {
				@Override
				public void onErrorResponse(VolleyError error) {
					Message message = new Message();
					message.obj = error.getMessage();
					message.what = 0x29;
					handler.sendMessage(message);
				}
			});
		} else {
			handler.sendEmptyMessage(IResult.NET_ERROR);
		}
	}

	protected void loadMore() {
		page++;
		initGetData();
	}

	private void refresh() {
		refresh = false;
		initGetData();
	}

	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("病历夹");

		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.GONE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("添加");
		super.setupView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		layout.setBackgroundColor(getResources().getColor(R.color.ground_light));
//		addTextView.setTextColor(getResources().getColor(R.color.text_color));
	}

	public void doClick(View view) {
		if (view.getId() == R.id.btn_back) {
			finish();
		} else if (view.getId() == R.id.toptitle_img_right) {
			Utils.startActivityForResult(this, MedicalHistoryEditFileActivity.class, 11);
		} else if (view.getId() == R.id.bt_keyword_search) {
			voice.setVisibility(View.VISIBLE);
			lvCustomEvas.setVisibility(View.GONE);
			ll_empty.setVisibility(View.GONE);
			preInitVoiceRecognizer();
			if (0 == startRecognizer()) {
				//int ret = VoiceRecognizer.shareInstance().cancel();
				//Log.d(TAG, "cancel ret = " + ret);

				mRecoState = 1;
			}
//			keyword = searchEdit.getText().toString().trim();
//			keyBool = true;
//			refresh = true;
//			page = 1;
//			initGetData();
		}
	}
	
	private void preInitVoiceRecognizer() {
		//setSilentTime参数单位为微秒：1秒=1000毫秒
		VoiceRecognizer.shareInstance().setSilentTime(1000);
		VoiceRecognizer.shareInstance().setListener(this);
		//VoiceRecognizer.shareInstance().setDomain("113.108.82.81", 8080);
		//VoiceRecognizer.shareInstance().setDomain("newsso.map.qq.com", 8080, "newsso.map.qq.com:8080");
		//VoiceRecognizer.shareInstance().setUri("/voice/fetchinfo");
		//VoiceRecognizer.shareInstance().setGetPureRes(true);
		
		mInitSucc = VoiceRecognizer.shareInstance().init(this, "248b63f1ceca9158ca88516bcb338e82a482ecd802cbca12");
		if (mInitSucc != 0) {
			Toast.makeText(this, "初始化失败",
					Toast.LENGTH_SHORT).show();
		}
	}
	
	private int startRecognizer() {
		if (0 == VoiceRecognizer.shareInstance().start()) {
			searchEdit.setText("语音已开启，请说话…");
			return 0;
		}
		searchEdit.setText("启动失败");
		voice.setEnabled(true);
		return -1;
		
	}

	private void initGetData() {

		if (NetWorkUtils.detect(this)) {
			loading(null);
			HashMap<String, String> params = new HashMap<String, String>();
			params.put("token", SharePrefUtil.getString(Conast.ACCESS_TOKEN));
			params.put("userid", SharePrefUtil.getString(Conast.MEMBER_ID));
			params.put("page", page + "");
			if (!TextUtils.isEmpty(keyword)) {
				params.put("key", keyword);
			}
			Log.e("YY", params.toString());
			MyApplication.volleyHttpClient.postWithParams(HttpUtil.get_bingli_list, MedicalHistoryBean.class, params, new Response.Listener() {
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
			showToast("网络连接异常，请检查网络！");
			return;
		}
	}

	@Override
	protected void onResult(Message msg) {
		destroyDialog();
		lvCustomEvas.onRefreshComplete();
		try {
			switch (msg.what) {
			case IResult.USER_INFO:
				final MedicalHistoryBean response = (MedicalHistoryBean) msg.obj;
				Log.e("YY", "www" + response.getSuccess() + "");
				Log.e("YY", "www" + response.getErrormsg() + "");
				Log.e("YY", "www" + response.getCount() + "");
				if (null != response) {
					if (response.getSuccess() == 1) {
						totalpages = response.getCount() / 20 + 1;
						if (refresh) {
							if (response.getData() == null || response.getData().size() == 0) {
								picture_ids.clear();
								picture_ids_list.clear();
								adapter.clear();
								ll_empty.setVisibility(View.VISIBLE);
								lvCustomEvas.setVisibility(View.GONE);
							} else {
								ll_empty.setVisibility(View.GONE);
								lvCustomEvas.setVisibility(View.VISIBLE);
								Log.e("YY", "搜索之前1111111111111111111");
								if (keyBool) {
									picture_ids.clear();
									picture_ids_list.clear();
									adapter.clear();
									Log.e("YY", "搜索1111111111111111111");
									keyBool = false;
								}
								sortList(response.getData());
								adapter.add(response.getData(), picture_ids_list);
							}
						} else {

						}
						if (page < totalpages) {
							lvCustomEvas.onLoadMoreComplete(false);
							refresh = true;
						} else {
							lvCustomEvas.onLoadMoreComplete(true);
							refresh = false;
						}
					}
				}
				// if (null != sp) {
				// if (sp.getSuccess() == 1) {
				// showToast("保存成功");
				// }
				// }
				break;
			case 0x28:
				SetProfile sp = (SetProfile) msg.obj;
				Log.e("YY", "www" + sp.getSuccess() + "");
				Log.e("YY", "www" + sp.getErrormsg() + "");
				if (null != sp) {
					if (sp.getSuccess() == 1) {
						showToast("删除成功");
						Utils.startActivity(MedicalHistoryMainActivity.this, MedicalHistoryMainActivity.class);
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

	private void sortList(List<MedicalHistory> data) {
		for (int j = 0; j < data.size(); j++) {
			sort(data.get(j).getATTACHMENTS());
		}

	}

	// 分组
	private void sort(List<ImageUrlBean> datas) {
		List<ImageUrlBean> fileList = datas;
		if (fileList.size() == 0) {
			picture_ids.clear();
			picture_ids_list.add(0);
			// return;
		} else {
			for (int i = 0; i < fileList.size(); i++) {
				ImageUrlBean imageUrlBean = fileList.get(i);
				if (imageUrlBean.getATTACHMENT() != null && !imageUrlBean.getATTACHMENT().equals("")) {
					picture_ids.add(imageUrlBean.getATTACHMENT());
				}
			}
			picture_ids_list.add(picture_ids.size());
			picture_ids.clear();

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 11 && resultCode == RESULT_OK) {
			page = 1;
			Log.d("XX", "");
			initGetData();
		}
	}

	@Override
	public void onRefreshStarted(View view) {

	}

	@Override
	public void onGetError(int errorCode) {
		if (2 == mRecoState) {
			cancelTask();
		}
		
		if (-202 == errorCode) {
			initPopWindow();
			findViewById(R.id.et_disease_keyword).post(new Runnable() {
				public void run() {
				  mPop.showAtLocation(findViewById(R.id.et_disease_keyword), Gravity.CENTER, 0, 0);
				}
			});
			return;
		}
		searchEdit.setText("");
		setStartBtn(true);
		mRecoState = 0;
		keyword = searchEdit.getText().toString().trim();
		keyBool = true;
		refresh = true;
		page = 1;
		voice.setVisibility(View.GONE);
		lvCustomEvas.setVisibility(View.VISIBLE);
		initGetData();
//		setCancelBtn(false);
//		mRecoState = 0;
//		setStartBtn(true);
	}
	
	@SuppressLint("InlinedApi")
	private void initPopWindow() {
//		if (mPop == null) {
//			goMailBtn = (Button) layout1.findViewById(R.id.hint3);
//			goMailBtn.setOnClickListener(new View.OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					Intent data=new Intent(Intent.ACTION_SENDTO);    
//					data.setData(Uri.parse("mailto:prteam@tencent.com"));    
//					startActivity(data); 
//				}
//			});
//			mPop = new PopupWindow(layout1,
//					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
//		}
//		if (mPop.isShowing()) {
//			mPop.dismiss();
//		}
	}

	@SuppressLint("ShowToast")
	@Override
	public void onGetResult(VoiceRecognizerResult result) {
		cancelTask();

		mRecognizerResult = "";

		//if (1 == result.type) {
		//	Log.d(TAG, "result = " + new String(result.httpRes));
		//}

		if (result != null && result.words != null) {
			int wordSize = result.words.size();
			StringBuilder results = new StringBuilder();
			for (int i = 0; i<wordSize; ++i) {
				Word word = (Word) result.words.get(i);
				if (word != null && word.text != null) {
					results.append("\r\n");
					results.append(word.text.replace(" ", ""));
				}
			}
			results.append("\r\n");
			mRecognizerResult = results.toString();
		}
		String[] arrs = mRecognizerResult.split("。");
		searchEdit.setText(arrs[0]);
		searchEdit.setSelection(arrs[0].length());
//		setCancelBtn(false);
		setStartBtn(true);
		mRecoState = 0;
		keyword = searchEdit.getText().toString().trim();
		keyBool = true;
		refresh = true;
		page = 1;
		voice.setVisibility(View.GONE);
		lvCustomEvas.setVisibility(View.VISIBLE);
		initGetData();
	}

	@Override
	public void onGetVoiceRecordState(VoiceRecordState state) {
		if (state == VoiceRecordState.Start) {
			searchEdit.setText("语音已开启，请说话…");
		} else if (state == VoiceRecordState.Complete) {
			voice.setEnabled(false);

			searchEdit.setText("识别中...");
			mRecoState = 2;
			startTask();
		} else if (state == VoiceRecordState.Canceling) {
			mRecoState = 3;
			setStartBtn(false);
			searchEdit.setText("正在取消");
		} else if (state == VoiceRecordState.Canceled) {
			cancelTask();
//			searchEdit.setText("点击开始说话");
			mRecoState = 0;
//			setCancelBtn(false);
			setStartBtn(true);
		}
	}

	@Override
	public void onVolumeChanged(int volume) {

		int index = volume;
		if (index < 0) {
			index = 0;
		} else if (index >= mMicNum) {
			index = mMicNum - 1;
		}
		if (voice != null && 1 == mRecoState) {
			switch (index) {
			case 0:
				voice.setBackgroundResource(R.drawable.recog001);
				break;
			case 1:
				voice.setBackgroundResource(R.drawable.recog002);
				break;
			case 2:
				voice.setBackgroundResource(R.drawable.recog003);
				break;
			case 3:
				voice.setBackgroundResource(R.drawable.recog004);
				break;
			case 4:
				voice.setBackgroundResource(R.drawable.recog005);
				break;
			case 5:
				voice.setBackgroundResource(R.drawable.recog006);
				break;
			case 6:
				voice.setBackgroundResource(R.drawable.recog007);
				break;
			case 7:
				voice.setBackgroundResource(R.drawable.recog008);
				break;
			default:
				voice.setBackgroundResource(R.drawable.recog001);
			}
		}
	}
	
	private void setStartBtn(boolean enabled) {
		if (null != voice) {
			if (true == enabled) {
				voice.setEnabled(enabled);
				voice.setBackgroundResource(R.drawable.recog001);
			}
			else {
				voice.setEnabled(enabled);
				voice.setBackgroundResource(R.drawable.recoggray);
			}
		}
	}
	
	public void startTask() {
		frameTimer = new Timer(false);
		frameTask = new TimerTask() {
			int btnIndex = 0;
			@Override
			public void run() {
				int index = (btnIndex++) % 8;
				Message message = new Message();
				message.what = index;
				handler.sendMessage(message);
			}
		};
		frameTimer.schedule(frameTask, 200, 100);
	}
	
	public void cancelTask() {
		if (null != frameTask) {
			frameTask.cancel();
		}
		if (null != frameTimer) {
			frameTimer.cancel();
		}
	}

}
