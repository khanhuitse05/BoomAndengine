package ktn.boom;

import com.example.boom.R;

import ktn.util.UtilActivity;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class HelpActivity extends Activity {


	Animation mAnimation_in_left, mAnimation_out_left;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UtilActivity.requestWindowFeature(this);

		setContentView(R.layout.helpscreen);

		
		mAnimation_in_left = AnimationUtils.loadAnimation(this,
				R.anim.slide_in_left);
		mAnimation_out_left = AnimationUtils.loadAnimation(this,
				R.anim.slide_out_left);
	}

}