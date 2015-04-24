package net.ememed.user2.activity;

import java.util.HashMap;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import de.greenrobot.event.EventBus;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.entity.IResult;
import net.ememed.user2.entity.MemberInfo;
import net.ememed.user2.fragment.DoctorBasicInfoFragment.DotorListener;
import net.ememed.user2.fragment.DoctorBasicInfoFragment1;
import net.ememed.user2.network.HttpUtil;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.NetWorkUtils;
import net.ememed.user2.util.SharePrefUtil;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DoctorBasicInfoActivity extends BasicActivity implements DotorListener{
	
	private DoctorBasicInfoFragment1 basicInfoFragment;
	
	private Button btn_addhealth;
	
	private String DOCTORID;
	public TextView top_title;
	private String from;
	
	public TextView getTop_title() {
		return top_title;
	}

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.doctor_basic_info);
		from = getIntent().getExtras().getString("from");
		initView();
		basicInfoFragment = new DoctorBasicInfoFragment1();
		Bundle bundle = new Bundle();
		bundle.putString("fragmentKey",
				getString(R.string.frag_title_introduce));
		
		DOCTORID = getIntent().getExtras().getString("tochat_userId");
		
		bundle.putString("tochat_userId", DOCTORID);
		bundle.putString("doctor_name", getIntent().getExtras().getString("doctor_name"));
		basicInfoFragment.setArguments(bundle);
		getSupportFragmentManager().beginTransaction()
				.replace(R.id.layout_fragment, basicInfoFragment).commit();
	}
	
	private void initView(){
		
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(getIntent().getExtras().getString("doctor_name"));
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundResource(R.color.transparent);
		btn_addhealth.setText("免费微聊");
		
	}
	
	public void doClick(View view){
		
		switch (view.getId()) {
		case R.id.btn_back:
			
			if(from!=null){
				if(from.equals(SplashActivity.class.getSimpleName())){
					startActivity(new Intent(this, MainActivity.class));
					DoctorBasicInfoActivity.this.finish();
				}
			}else{
				finish();
			}
			break;
		case R.id.btn_addhealth:// 免费微聊
			Intent microIntent = new Intent(this, MicroChatActivity.class);
			
			if(basicInfoFragment.memberInfo!=null){
				microIntent.putExtra("tochat_userId", basicInfoFragment.memberInfo.getData().getMEMBERID());
				microIntent.putExtra("doctor_avatar", basicInfoFragment.memberInfo.getData().getAVATAR());
				microIntent.putExtra("doctorName", basicInfoFragment.memberInfo.getData().getREALNAME());
				microIntent.putExtra("memberInfo", basicInfoFragment.memberInfo);
				startActivity(microIntent);
			}else{
				showToast("网络错误");
			}
			break;

		default:
			break;
		}
		
	}

	@Override
	public void onNumber(int number) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(from!=null){
				if(from.equals(SplashActivity.class.getSimpleName())){
					startActivity(new Intent(this, MainActivity.class));
					DoctorBasicInfoActivity.this.finish();
				}
			}else{
				finish();
			}
			
			break;

		default:
			break;
		}
		
		return super.onKeyDown(keyCode, event);
	}

}
