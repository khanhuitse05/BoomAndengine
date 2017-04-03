package ktn.dialog;

import com.example.boom.R;

import ktn.boom.Menu;
import ktn.util.Util;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

public class DialogExit extends Dialog implements
		android.view.View.OnClickListener {

	Button button_yes, button_no;
	RelativeLayout dialog_exit;

	Activity mActivity;

	public DialogExit(Context context) {
		super(context);
		this.mActivity = (Activity) context;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.drawable.bg_null);

		this.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;

		setContentView(R.layout.dialog_exit);

		dialog_exit = (RelativeLayout) findViewById(R.id.dialog_exit);
		Util.resizeDialog(dialog_exit, this.mActivity);

		button_yes = (Button) findViewById(R.id.button_yes);
		button_yes.setOnClickListener(this);

		button_no = (Button) findViewById(R.id.button_no);
		button_no.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		try {
			if (Menu.mSound != null)
				Menu.mSound.playHarp();

			switch (view.getId()) {
				case R.id.button_yes:
					mActivity.finish();
					this.dismiss();
					break;
	
				case R.id.button_no:
					this.dismiss();
					break;
	
				default:
					break;
			}
		} catch (Exception e) {
		}
	}
}