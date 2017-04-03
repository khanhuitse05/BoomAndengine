package ktn.dialog;

import com.example.boom.R;

import ktn.boom.Menu;
import ktn.boom.MySharedPreferences;
import ktn.boom.TouchDragExample;
import ktn.util.Util;
import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

public class DialogCompleted extends Dialog implements
		android.view.View.OnClickListener {
	Button button_next, button_replay, button_exit;
	RelativeLayout dialog_completed;
	TouchDragExample mainGame;
	MySharedPreferences mySharedPreferences;

	public DialogCompleted(Context context) {
		super(context);
		this.mainGame = (TouchDragExample) context;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setBackgroundDrawableResource(R.drawable.bg_null);
		this.getWindow().getAttributes().windowAnimations = R.style.Animations_SmileWindow;

		setContentView(R.layout.dialog_completed);

		mySharedPreferences = new MySharedPreferences(context);

		dialog_completed = (RelativeLayout) findViewById(R.id.dialog_completed);
		Util.resizeDialog(dialog_completed, this.mainGame);
		this.setCancelable(false);

		button_next = (Button) findViewById(R.id.button_next);
		button_next.setOnClickListener(this);

		

		button_replay = (Button) findViewById(R.id.button_replay);
		button_replay.setOnClickListener(this);

		button_exit = (Button) findViewById(R.id.button_exit);
		button_exit.setOnClickListener(this);
	}

	@Override
	public void onClick(View view) {
		if (Menu.mSound != null)
			Menu.mSound.playHarp();
		switch (view.getId()) {

		case R.id.button_next:
			mainGame.nextLevel();
			this.dismiss();
			break;

		case R.id.button_replay:
			mainGame.resetGame();
			mainGame.resume();
			this.dismiss();			
			break;

		case R.id.button_exit:
			mainGame.finish();
			this.dismiss();
			break;
		default:
			break;
		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return false;
		}
		return false;
	}

}