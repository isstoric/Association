package com.example.association;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.*;
import android.widget.Button;

public class ComplexityGames extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.complexity_game);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		Button ButtonEasy = (Button) findViewById(R.id.ButtonEasy);
		ButtonEasy.setOnClickListener(this);
		
		Button ButtonMedium = (Button) findViewById(R.id.ButtonMedium);
		ButtonMedium.setOnClickListener(this);
		
		Button ButtonHard = (Button) findViewById(R.id.ButtonHard);
		ButtonHard.setOnClickListener(this);
		
		}
	
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.ButtonEasy:
			//при нажатии формируем легкую игру
			Intent intentEasy=new Intent();
			intentEasy.setClass(this, ViewGame.class);
			startActivity(intentEasy);
		break;
		
		case R.id.ButtonMedium:
			//при нжатии формируем среднюю игру
			Intent intentMedium=new Intent();
			intentMedium.setClass(this, ViewGame.class);
			startActivity(intentMedium);;
		break;
		
		case R.id.ButtonHard:
			//при нажатии формируем сложную игру
			Intent intentHard=new Intent();
			intentHard.setClass(this, ViewGame.class);
			startActivity(intentHard);;
		break;
		}
	}
}
