package com.example.association;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.view.Menu;

public class MainActivity extends Activity implements View.OnClickListener {

	SharedPreferences settings;
	Button ButtonContinue;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		Button ButtonNewGame = (Button) findViewById(R.id.ButtonNewGame);
	    ButtonNewGame.setOnClickListener(this);
	    
	    ButtonContinue = (Button) findViewById(R.id.ButtonContinue);
	    ButtonContinue.setOnClickListener(this);
	    
	    Button ButtonRegulations = (Button) findViewById(R.id.ButtonRegulations);
	    ButtonRegulations.setOnClickListener(this);
	    
	    Button ButtonInformation = (Button) findViewById(R.id.ButtonInformation);
	    ButtonInformation.setOnClickListener(this);
	    
	    Button ButtonStatistics = (Button) findViewById(R.id.ButtonStatistics);
	    ButtonStatistics.setOnClickListener(this);
	    
	    Button ButtonMusic = (Button) findViewById(R.id.ButtonMusic);
	    ButtonMusic.setOnClickListener(this);
	    
	    settings = getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
	    if(!settings.contains(Config.APP_PREFERENCES_LEVEL)){
	    	ButtonContinue.setEnabled(false);
	    }	
	    if(!settings.contains(Config.APP_PREFERENCES_SOUND)){
	    	Editor editor = settings.edit();
			editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_ON);
			editor.apply();
	    }	 
	    
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		Log.d("MainActivity","level:"+settings.getAll());
		
		if(settings.contains(Config.APP_PREFERENCES_LEVEL)){
	    	ButtonContinue.setEnabled(true);
	    }
	}
		
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
			//при нажатии переход а стрнаицу выборая сложности новой игры
			case R.id.ButtonNewGame:
				Intent intent=new Intent();
				intent.setClass(this, ComplexityGames.class);
				startActivity(intent);
				break;
				
				//при нажатии запуск прерваной игры
			case R.id.ButtonContinue:
				Intent intent2 = new Intent();
				intent2.setClass(this, ViewGame.class);
				startActivity(intent2);
				break;
				
			case R.id.ButtonRegulations:
				Intent intent3 = new Intent();
				intent3.setClass(this, Regulations.class);
				startActivity(intent3);
				
				break;
			case R.id.ButtonInformation:
				//запуск страницы с информацией о программе
				Intent intent4 = new Intent();
				intent4.setClass(this, Information.class);
				startActivity(intent4);
				;
				break;
			case R.id.ButtonStatistics:
				//статистика
				break;
			case R.id.ButtonMusic:
				if(settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)){
					Editor editor = settings.edit();
					editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_OFF);
					editor.apply();
					//картиночку поменять надо бы
					}
				else{
					Editor editor = settings.edit();
					editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_ON);
					editor.apply();
				}
				break;
			
		}
	}
	
	
}
