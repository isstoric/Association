package com.example.association;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class ViewGame extends Activity implements View.OnClickListener{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("tut","tut");
		setContentView(R.layout.view_game);
		Log.d("tut1","tut1");
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		Log.d("tut3","tut3");
		
		Button ButtonWindow10 = (Button) findViewById(R.id.Window10);
		Log.d("tut4","tut4");
		ButtonWindow10.setOnClickListener(this);
		Log.d("tut5","tut5");

		Button ButtonWindow11 = (Button) findViewById(R.id.Window11);
		ButtonWindow11.setOnClickListener(this);
		
		Button ButtonWindow20 = (Button) findViewById(R.id.Window20);
		ButtonWindow20.setOnClickListener(this);
		
		Button ButtonWindow21 = (Button) findViewById(R.id.Window21);
		ButtonWindow21.setOnClickListener(this);
		
		Button ButtonWindow30 = (Button) findViewById(R.id.Window30);
		ButtonWindow30.setOnClickListener(this);
		
		Button ButtonWindow31 = (Button) findViewById(R.id.Window31);
		ButtonWindow31.setOnClickListener(this);
		
		Button ButtonWindow40 = (Button) findViewById(R.id.Window40);
		ButtonWindow40.setOnClickListener(this);
		
		Button ButtonWindow41 = (Button) findViewById(R.id.Window41);
		ButtonWindow41.setOnClickListener(this);
		
		Button ButtonRegulations = (Button) findViewById(R.id.ButtonRegulations);
		ButtonRegulations.setOnClickListener(this);
		Log.d("tut6","tut6");
		Button ButtonMusik = (Button) findViewById(R.id.ButtonMusik);
		ButtonMusik.setOnClickListener(this);
		Log.d("tut7","tut7");
		
		Button ButtonTimer = (Button) findViewById(R.id.ButtonTimer);
		ButtonTimer.setOnClickListener(this);
		Log.d("tut8","tut8");
		}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		Log.d("menu tut","menu tut");
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		Log.d("click","1");
		switch (v.getId()) {
		
		//обработка нажатия кнопок
		
		case R.id.ButtonRegulations:
			Log.d("click2","2");
			Intent intentRegulations=new Intent();
			intentRegulations.setClass(this,Regulations.class);
			startActivity(intentRegulations);
			break;
		case R.id.ButtonMusik:
			//обработка кнопки мызыка
			break;

		}
		
	}


}
