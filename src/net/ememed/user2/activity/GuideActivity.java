package net.ememed.user2.activity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import net.ememed.user2.R;
import net.ememed.user2.activity.adapter.ViewPagerAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

public class GuideActivity extends FragmentActivity implements OnClickListener,
		OnPageChangeListener {

	private ViewPager vp;
	private ViewPagerAdapter vpAdapter;
	private List<View> views;
	private ImageView[] dots; // 底部小圆点
	private static final String SHAREDPREFERENCES_NAME = "first_pref";
	// 记录当前选中位置
	private int currentIndex;
	Button guide_tixian;
	LinearLayout ll;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_guide_first_install);

		// 初始化页面
		initViews();

		// Button guide_zhuce = (Button) findViewById(R.id.guide_zhuce);
		// guide_zhuce.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		// // 设置已经引导
		// setGuided();
		// gologin();
		//
		// }
		// });

		guide_tixian = (Button) findViewById(R.id.guide_tixian);
		
		guide_tixian.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setGuided();
				goHome();

			}

		});
	}

	private void initViews() {
		LayoutInflater inflater = LayoutInflater.from(this);

		views = new ArrayList<View>();
		// 初始化引导图片列表
		views.add(inflater.inflate(R.layout.guider_1, null));
		views.add(inflater.inflate(R.layout.guider_2, null));
		views.add(inflater.inflate(R.layout.guider_3, null));
		// views.add(inflater.inflate(R.layout.guider_4, null));
		// views.add(inflater.inflate(R.layout.guider_5, null));

		// ImageView imageView = new ImageView(this);
		// imageView.setImageResource(R.drawable.guider_10);
		// // imageView.setScaleType(ScaleType.FIT_XY);
		//
		// ImageView imageView2 = new ImageView(this);
		// imageView2.setImageResource(R.drawable.guider_20);
		// // imageView2.setScaleType(ScaleType.FIT_XY);
		//
		// ImageView imageView3 = new ImageView(this);
		// imageView3.setImageResource(R.drawable.guider_30);
		// // imageView3.setScaleType(ScaleType.FIT_XY);
		//
		// ImageView imageView4 = new ImageView(this);
		// imageView4.setImageResource(R.drawable.guider_40);
		// // imageView4.setScaleType(ScaleType.FIT_XY);
		//
		// ImageView imageView5 = new ImageView(this);
		// imageView5.setImageResource(R.drawable.guider_50);
		// // imageView5.setScaleType(ScaleType.FIT_XY);
		// views.add(imageView);
		// views.add(imageView2);
		// views.add(imageView3);
		// views.add(imageView4);
		// views.add(imageView5);

		// 初始化Adapter
		vpAdapter = new ViewPagerAdapter(views, this);

		vp = (ViewPager) findViewById(R.id.viewpager);
		vp.setAdapter(vpAdapter);
		// 绑定回调
		vp.setOnPageChangeListener(this);
		initDots();
	}

	private void initDots() {
		ll = (LinearLayout) findViewById(R.id.ll);

		dots = new ImageView[views.size()];

		// 循环取得小点图片
		for (int i = 0; i < views.size(); i++) {
			dots[i] = (ImageView) ll.getChildAt(i);
			dots[i].setEnabled(true);// 都设为灰色
			dots[i].setOnClickListener(this);
			dots[i].setTag(i);// 设置位置tag，方便取出与当前位置对应
		}

		currentIndex = 0;
		dots[currentIndex].setEnabled(false);// 设置为白色，即选中状态
	}

	/**
	 * *设置当前的引导页 .
	 */
	private void setCurView(int position) {
		if (position < 0 || position >= views.size()) {
			return;
		}

		vp.setCurrentItem(position);
	}

	/**
	 * 这只当前引导小点的选中
	 */
	private void setCurDot(int positon) {
		if (positon < 0 || positon > views.size() - 1
				|| currentIndex == positon) {
			return;
		}

		dots[positon].setEnabled(false);
		dots[currentIndex].setEnabled(true);

		currentIndex = positon;
	}

	// 当滑动状态改变时调用
	@Override
	public void onPageScrollStateChanged(int arg0) {
	}

	// 当当前页面被滑动时调用
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO Auto-generated method stub
		setCurDot(arg0);
		if(arg0==views.size()-1){
			guide_tixian.setVisibility(View.VISIBLE);
			ll.setVisibility(View.GONE);
		}else{
			guide_tixian.setVisibility(View.GONE);
			ll.setVisibility(View.VISIBLE);
		}
	}

	private void goHome() {
		// 跳转
		Intent intent = new Intent(this, MainActivity.class);
		this.startActivity(intent);
		this.finish();

	}

	private void gologin() {
		// 跳转
		Intent intent = new Intent(this, RegisterByMobileActivity.class);
		this.startActivity(intent);
		this.finish();

	}

	private void setGuided() {
		SharedPreferences preferences = this.getSharedPreferences(
				SHAREDPREFERENCES_NAME, Context.MODE_PRIVATE);
		Editor editor = preferences.edit();
		// 存入数据
		editor.putBoolean("isFirstIn", false);
		// 提交修改
		editor.commit();
	}

	public Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
		int options = 90;
		int longs = baos.toByteArray().length;
		while (baos.toByteArray().length / 1024 > 50) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
			baos.reset();// 重置baos即清空baos
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
			longs = baos.toByteArray().length;
			options -= 10;// 每次都减少10
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
		Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
		return bitmap;
	}

	public void compressResourcesBitmap() {

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int position = (Integer) v.getTag();
		setCurView(position);
		setCurDot(position);
		
	}

}