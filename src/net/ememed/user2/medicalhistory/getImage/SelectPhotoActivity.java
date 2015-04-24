package net.ememed.user2.medicalhistory.getImage;

import java.util.ArrayList;
import java.util.List;
import net.ememed.user2.MyApplication;
import net.ememed.user2.R;
import net.ememed.user2.activity.BasicActivity;
import net.ememed.user2.medicalhistory.getImage.PhotoFolderFragment.OnPageLodingClickListener;
import net.ememed.user2.medicalhistory.getImage.PhotoFragment.OnPhotoSelectClickListener;
import net.ememed.user2.medicalhistory.getImage.bean.PhotoInfo;
import net.ememed.user2.medicalhistory.getImage.bean.PhotoSerializable;
import net.ememed.user2.medicalhistory.getImage.util.CheckImageLoaderConfiguration;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**    
 * @title SelectPhotoActivity.java
 * @package com.centaline.mhc.activity.friendGroupActivity
 * @author guilin   
 * @date 2013-8-6 上午10:42:15  
 */
public class SelectPhotoActivity extends BasicActivity implements OnPageLodingClickListener
				, OnPhotoSelectClickListener{

	private PhotoFolderFragment photoFolderFragment;
	private TextView top_title;
//	private Button btnback,btnright;
	
//	private TextView title;
	
	private List<PhotoInfo> hasList;
	
	private ArrayList<String> imagePaths;
	
	private FragmentManager manager;
	private int backInt = 0;
	private Button btn_addhealth;
//	private TopBar topbar;
	
	/**
	 * 已选择图片数量
	 */
	private int count;

	@Override
	public void onCreateContent(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreateContent(savedInstanceState);
		setContentView(R.layout.activity_selectphoto);
//		topbar = new TopBar(this);
//		topbar.setTitle("请选择相册");
//		topbar.setIssueListener("完成", this);
//		topbar.setIssueBackListener(this);
		getWindowManager().getDefaultDisplay().getMetrics(MyApplication.getDisplayMetrics());   
		
		count = getIntent().getIntExtra("count", 0);
		
		manager = getSupportFragmentManager();
		
		hasList = new ArrayList<PhotoInfo>();
		
		imagePaths = new ArrayList<String>();
		
//		btnback = (Button)findViewById(R.id.btnback);
//		btnright = (Button)findViewById(R.id.btnright);
//		title = (TextView)findViewById(R.id.title);
//		findViewById(R.id.iv_topbar_back)
//		.setVisibility(View.VISIBLE);
//		findViewById(R.id.iv_topbar_back)
//		.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View paramView) {
//				if(backInt==0){
//					finish();
//				}else if(backInt==1){
//					backInt--;
//					hasList.clear();
//					imagePaths.clear();
//					topbar.bindData("请选择相册");
////					title.setText("请选择相册");
//					FragmentTransaction transaction = manager.beginTransaction();
//					transaction.show(photoFolderFragment).commit();
//					manager.popBackStack(0, 0);
//				}
//			}
//		});
//		btnback.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(backInt==0){
//					finish();
//				}else if(backInt==1){
//					backInt--;
//					hasList.clear();
//					imagePaths.clear();
//					topbar.bindData("请选择相册");
////					title.setText("请选择相册");
//					FragmentTransaction transaction = manager.beginTransaction();
//					transaction.show(photoFolderFragment).commit();
//					manager.popBackStack(0, 0);
//				}
//			}
//		});
//		btnright.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				if(hasList.size()>0){
////					Bundle bundle = new Bundle();
////					bundle.putStringArrayList("imagePaths", imagePaths);
//					Intent intent = getIntent();
//					intent.putStringArrayListExtra("imagePaths", imagePaths);
//					setResult(11, intent);
//					finish();
//				}else{
//					Toast.makeText(SelectPhotoActivity.this, "至少选择一张图片", Toast.LENGTH_SHORT).show();
//				}
//			}
//		});
//		title.setText("请选择相册");
//		topbar.setTitle("请选择相册");
		photoFolderFragment = new PhotoFolderFragment();

		FragmentTransaction transaction = manager.beginTransaction();
		transaction.add(R.id.body,photoFolderFragment);  
		transaction.addToBackStack(null);
		// Commit the transaction  
		transaction.commit(); 
	}
	
	@Override
	protected void setupView() {
		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText("请选择相册");
		
		btn_addhealth = (Button) findViewById(R.id.btn_addhealth);
		btn_addhealth.setVisibility(View.VISIBLE);
		btn_addhealth.setBackgroundDrawable(null);
		btn_addhealth.setText("确认");
		super.setupView();
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		CheckImageLoaderConfiguration.checkImageLoaderConfiguration(this);
	}

	@Override
	public void onPageLodingClickListener(List<PhotoInfo> list) {
		// TODO Auto-generated method stub
//		topbar.setTitle("已选择0张");
		top_title.setText("已选择0张");
		FragmentTransaction transaction = manager.beginTransaction();
		PhotoFragment photoFragment = new PhotoFragment();
		Bundle args = new Bundle();
		PhotoSerializable photoSerializable = new PhotoSerializable();
		for (PhotoInfo photoInfoBean : list) {
			photoInfoBean.setChoose(false);
		}
		photoSerializable.setList(list);
		args.putInt("count", count);
		args.putSerializable("list", photoSerializable);
		photoFragment.setArguments(args);
		transaction = manager.beginTransaction();
		transaction.hide(photoFolderFragment).commit();
		transaction = manager.beginTransaction();
		transaction.add(R.id.body,photoFragment);  
		transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		transaction.addToBackStack(null);
		// Commit the transaction  
		transaction.commit(); 
		backInt++;
	}

	@Override
	public void onPhotoSelectClickListener(List<PhotoInfo> list) {
		// TODO Auto-generated method stub
		hasList.clear();
		imagePaths.clear();
		for (PhotoInfo photoInfoBean : list) {
			if(photoInfoBean.isChoose()){
				hasList.add(photoInfoBean);
				imagePaths.add(photoInfoBean.getPath_absolute());
			}
		}
		top_title.setText("已选择"+hasList.size()+"张");
//		title.setText("已选择"+hasList.size()+"张");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==0){
			finish();
		}else if(keyCode == KeyEvent.KEYCODE_BACK&&backInt==1){
			backInt--;
			hasList.clear();
			imagePaths.clear();
//			topbar.setTitle("请选择相册");
			top_title.setText("请选择相册");
			FragmentTransaction transaction = manager.beginTransaction();
			transaction.show(photoFolderFragment).commit();
			manager.popBackStack(0, 0);
		}
		return false;
	}
	
	public void doClick(View view) {
		if(view.getId() == R.id.btn_back){
			if(backInt==0){
				finish();
			}else if(backInt==1){
				backInt--;
				hasList.clear();
				imagePaths.clear();
//				topbar.setTitle("请选择相册");
				top_title.setText("请选择相册");
				FragmentTransaction transaction = manager.beginTransaction();
				transaction.show(photoFolderFragment).commit();
				manager.popBackStack(0, 0);
			}
		} else if(view.getId() == R.id.btn_addhealth){
//			Utils.startActivity(this, MedicalHistoryEditFile.class);
			postImage();
		}
	}

	public void postImage() {
		if(hasList.size()>0){
//			Bundle bundle = new Bundle();
//			bundle.putStringArrayList("imagePaths", imagePaths);
			Intent intent = getIntent();
			intent.putStringArrayListExtra("imagePaths", imagePaths);
			setResult(11, intent);
			finish();
		}else{
			Toast.makeText(SelectPhotoActivity.this, "至少选择一张图片", Toast.LENGTH_SHORT).show();
		}
	}
//
//	@Override
//	public void back() {
//		if(backInt==0){
//			finish();
//		}else if(backInt==1){
//			backInt--;
//			hasList.clear();
//			imagePaths.clear();
//			topbar.setTitle("请选择相册");
////			title.setText("请选择相册");
//			FragmentTransaction transaction = manager.beginTransaction();
//			transaction.show(photoFolderFragment).commit();
//			manager.popBackStack(0, 0);
//		}
//	}
}
