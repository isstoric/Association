package com.example.association;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class StatisticsActivity extends Activity{
	SharedPreferences settings;
	TextView easyTime;
	TextView mediumTime;
	TextView hardTime;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.statistics);
		
		easyTime=(TextView) findViewById(R.id.Easy);
		mediumTime=(TextView) findViewById(R.id.Medium);
		hardTime=(TextView) findViewById(R.id.Hard);
		settings = getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
		setStatistics();
	
	}
	
	private void setStatistics(){
		if(settings.contains(Config.APP_PREFERENCES_EASY_TIME)){
			long easyTimeMillis=settings.getLong(Config.APP_PREFERENCES_EASY_TIME, 0);
			easyTime.setText(outputTime(easyTimeMillis));
		}
		if(settings.contains(Config.APP_PREFERENCES_MEDIUM_TIME)){
			long mediumTimeMillis=settings.getLong(Config.APP_PREFERENCES_MEDIUM_TIME, 0);
			mediumTime.setText(outputTime(mediumTimeMillis));
		}
		if(settings.contains(Config.APP_PREFERENCES_HARD_TIME)){
			long hardTimeMillis=settings.getLong(Config.APP_PREFERENCES_HARD_TIME, 0);
			hardTime.setText(outputTime(hardTimeMillis));
		}
	}
	
	public String outputTime(long millis) {
		long minutes = millis / 1000 / 60;
		long seconds = (millis / 1000) - (minutes * 60);
		String time;
		if (minutes > 0) {
			time = " " + minutes + " мин " + seconds + " сек";
		} else {
			time = " " + seconds + " сек";
		}
		return time;
	}

}
