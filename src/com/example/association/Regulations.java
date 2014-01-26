package com.example.association;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
//import android.view.View;
import android.view.Window;

public class Regulations extends Activity{

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_regulations);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		
	}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

}
