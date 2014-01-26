package com.example.association;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.*;
import android.widget.Button;

public class ComplexityGames extends Activity implements View.OnClickListener {

	SharedPreferences settings;
	
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
		settings = getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
		Log.d("ComplexityGames","onCreate");
		
		//обнуляем таблицы 
		DataBaseLogic dbLogic=new DataBaseLogic(this);
		dbLogic.clearGame();
		}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Editor editor = settings.edit();
		editor.putInt(Config.APP_PREFERENCES_LEVEL, Config.FIRST_LEVEL);
		editor.apply();
		switch(v.getId()){
		case R.id.ButtonEasy:
			//при нажатии формируем легкую игру
			editor.putInt(Config.APP_PREFERENCES_COMPLEXITY, Config.EASY);
			editor.apply();
			Intent intentEasy=new Intent();
			intentEasy.setClass(this, ViewGame.class);
			startActivity(intentEasy);
		break;
		
		case R.id.ButtonMedium:
			//при нжатии формируем среднюю игру
			editor.putInt(Config.APP_PREFERENCES_COMPLEXITY, Config.MEDIUM);
			editor.apply();
			Intent intentMedium=new Intent();
			intentMedium.setClass(this, ViewGame.class);
			startActivity(intentMedium);
		break;
		
		case R.id.ButtonHard:
			//при нажатии формируем сложную игру
			editor.putInt(Config.APP_PREFERENCES_COMPLEXITY, Config.HARD);
			editor.apply();
			Intent intentHard=new Intent();
			intentHard.setClass(this, ViewGame.class);
			startActivity(intentHard);
		break;
		}
	}
}
