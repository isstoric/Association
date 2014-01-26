package com.example.association;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends Activity implements View.OnClickListener {

	SharedPreferences settings;
	Button ButtonContinue;
	ImageView ButtonMusic;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		Button ButtonNewGame = (Button) findViewById(R.id.ButtonNewGame);
	    ButtonNewGame.setOnClickListener(this);
	    
	    ButtonContinue = (Button) findViewById(R.id.ButtonContinue);
	    ButtonContinue.setOnClickListener(this);
	    
	    Button ButtonRegulations = (Button) findViewById(R.id.ButtonRegulations);
	    ButtonRegulations.setOnClickListener(this);
	    
	    Button ButtonStatistics = (Button) findViewById(R.id.ButtonStatistics);
	    ButtonStatistics.setOnClickListener(this);
	    
	    ButtonMusic = (ImageView) findViewById(R.id.ButtonMusic);
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
	    setMusicImage();
	    
	    
	    if(!settings.contains(Config.APP_PREFERENCES_COUNT_OF_PICTURES)){
	    DataBaseLogic dbLogic=new DataBaseLogic(this);
		int countOfPictures=dbLogic.countAllPictures();
		Editor editor = settings.edit();
		editor.putInt(Config.APP_PREFERENCES_COUNT_OF_PICTURES, countOfPictures);
		editor.apply();
		}
	    
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();		
		if(settings.contains(Config.APP_PREFERENCES_LEVEL)){
	    	ButtonContinue.setEnabled(true);
	    }
	}
	
public void  setMusicImage(){
	if(settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)){
		ButtonMusic.setImageResource(R.drawable.sound1);
	}else{
		ButtonMusic.setImageResource(R.drawable.sound2);
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
			
			case R.id.ButtonStatistics:
				Intent intent4 = new Intent();
				intent4.setClass(this, StatisticsActivity.class);
				startActivity(intent4);
				break;
			case R.id.ButtonMusic:
				if(settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)){
					Editor editor = settings.edit();
					editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_OFF);
					editor.apply();
					ButtonMusic.setImageResource(R.drawable.sound2);
					}
				else{
					Editor editor = settings.edit();
					editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_ON);
					editor.apply();
					ButtonMusic.setImageResource(R.drawable.sound1);
				}
				break;
			
		}
	}
	
	
}
