package net.ememed.user2.widget;

import net.ememed.user2.R;
import net.ememed.user2.util.Conast;
import net.ememed.user2.util.SharePrefUtil;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class DescribeServiceDialog extends Dialog{
	
	
	private TextView top_title;
	private TextView tv_presentation;
//	private int service_type;
//	private String service_buy;

	LinearLayout ll_text ;
	LinearLayout ll_phone ;
	LinearLayout ll_private_doctor ;
	LinearLayout ll_xianxia ;
	Context context;
	
	private Button btn_cancel;
	private Button btn_confirm;
	
	public DescribeServiceDialog(Context context) {
		super(context);
		this.context = context;
	}
	public DescribeServiceDialog(Context context,int theme) {
		super(context,theme);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_describe_service);
		
		
		
		top_title = (TextView) findViewById(R.id.top_title);
		tv_presentation = (TextView) findViewById(R.id.tv_presentation);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		ll_text = (LinearLayout) findViewById(R.id.ll_text);
		ll_phone = (LinearLayout) findViewById(R.id.ll_phone);
		ll_private_doctor = (LinearLayout) findViewById(R.id.ll_private_doctor);
		ll_xianxia = (LinearLayout) findViewById(R.id.ll_xianxia);
		
		
		
	}
	
	public void setContent(String str){
		String presentation = "        ";
		String[] text=str.split(",");
		top_title.setText(text[0]);
		presentation += Html.fromHtml(text[1]);
		tv_presentation.setText(presentation);
	}
	
	public void setShowText( int service_type, String service_buy,android.view.View.OnClickListener clickListener){
		btn_cancel.setOnClickListener(clickListener);
		btn_confirm.setOnClickListener(clickListener);
		switch (service_type) {
		case 1:
			setContent(SharePrefUtil.getString(Conast.TELETEXT_CONSULT));
			System.out.println(SharePrefUtil.getString(Conast.TELETEXT_CONSULT));
			break;
		case 2:
//			top_title.setText("预约通话");
//			presentation += context.getResources().getString(R.string.phone_presentation);
//			ll_phone.setVisibility(View.VISIBLE);
			setContent(SharePrefUtil.getString(Conast.ORDER_CALL));
			break;
		case 3:
//			top_title.setText("预约加号");
//			presentation += context.getResources().getString(R.string.jiuyi_presentation);
//			ll_xianxia.setVisibility(View.VISIBLE);
			setContent(SharePrefUtil.getString(Conast.ORDER_ADD_NULBER));
			break;
		case 4:
			top_title.setText("上门会诊");
//			presentation += context.getResources().getString(R.string.shangmen_presentation);
//			ll_xianxia.setVisibility(View.VISIBLE);
			break;
		case 14:
//			top_title.setText("预约住院");
//			presentation += context.getResources().getString(R.string.zhuyuan_presentation);
//			ll_xianxia.setVisibility(View.VISIBLE);
			setContent(SharePrefUtil.getString(Conast.ORDER_HOSPITAL));
			break;
		case 15:
//			top_title.setText("签约私人医生");
//			presentation += context.getResources().getString(R.string.private_doctor_presentation);
//			ll_private_doctor.setVisibility(View.VISIBLE);
			setContent(SharePrefUtil.getString(Conast.PEOPLE_DOCTOR));
			break;
		case 16:
			top_title.setText("其他服务");
//			presentation += context.getResources().getString(R.string.other_presentation);
//			ll_xianxia.setVisibility(View.VISIBLE);
			
			break;

		default:
			break;
		}
		if(!TextUtils.isEmpty(service_buy)){
			btn_confirm.setText(service_buy);
		}
	}

}
