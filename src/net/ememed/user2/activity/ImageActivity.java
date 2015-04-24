package net.ememed.user2.activity;

import net.ememed.user2.R;
import net.ememed.user2.util.Util;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;

public class ImageActivity extends BasicActivity {

	private ImageView icon;
	private String avatar;

	@Override
	protected void onCreateContent(Bundle savedInstanceState) {
		super.onCreateContent(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_image);
		icon = (ImageView) findViewById(R.id.iv);

		avatar = getIntent().getStringExtra("avatar");
		if (TextUtils.isEmpty(avatar))
			finish();
		imageLoader.displayImage(avatar, icon, Util.getOptions_pic());
		icon.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

}
