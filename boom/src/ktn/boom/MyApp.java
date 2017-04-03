package ktn.boom;

import ktn.util.UtilActivity;
import android.app.Activity;
import android.os.Bundle;


public class MyApp extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		UtilActivity.requestWindowFeature(this);
	}
}
