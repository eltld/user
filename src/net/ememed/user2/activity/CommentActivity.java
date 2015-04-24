package net.ememed.user2.activity;

import net.ememed.user2.R;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CommentActivity extends BasicActivity{
	private TextView top_title;
	private Button btn_addhealth;
	protected void onCreateContent(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.comment_layout);
		initView();
	}
	private void initView(){
		
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("服务评价");
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setText("提交");
		btn_addhealth.setBackgroundResource(R.color.transparent);
		
	}
	
	public void doClick(View v){
		
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_addhealth://提交评论
			break;

		default:
			break;
		}
		
	}

}
