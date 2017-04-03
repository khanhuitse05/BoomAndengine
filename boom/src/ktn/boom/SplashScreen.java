package ktn.boom;

import com.example.boom.R;

import ktn.config.MyConfig;
import ktn.util.UtilActivity;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class SplashScreen extends Activity {

	int timeDelay = 2000;// 2s
	boolean isFinish = false;

	Animation mAnimation_in_left, mAnimation_out_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UtilActivity.requestWindowFeature(this);

		setContentView(R.layout.splashscreen);
		MyConfig.getDisplayScreen(this);

		Handler mHandler = new Handler();
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (!isFinish)
					nextMenu();
			}
		}, timeDelay);

		mAnimation_in_left = AnimationUtils.loadAnimation(this,
				R.anim.slide_in_left);
		mAnimation_out_left = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_left);
	}

	public void nextMenu() {
		Intent mIntent = new Intent(this, Menu.class);
		this.startActivity(mIntent);
		this.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		isFinish = true;
	}
}