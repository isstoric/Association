package com.example.association;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;


public class ViewGame extends Activity implements View.OnClickListener, OnCompletionListener {
	
	MediaPlayer mediaPlayer;
	AudioManager am;
	SharedPreferences settings;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_game);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);		
		Button ButtonWindow10 = (Button) findViewById(R.id.Window10);
		ButtonWindow10.setOnClickListener(this);

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

		Button ButtonMusik = (Button) findViewById(R.id.ButtonMusik);
		ButtonMusik.setOnClickListener(this);

		
		Button ButtonTimer = (Button) findViewById(R.id.ButtonTimer);
		ButtonTimer.setOnClickListener(this);
		
		settings = getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
		am = (AudioManager) getSystemService(AUDIO_SERVICE);  
	    mediaPlayer = MediaPlayer.create(this, R.raw.dendy);

		if(settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)){
        mediaPlayer.start();
        }
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnCompletionListener(this);
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
		switch (v.getId()) {
		
		//обработка нажатия кнопок
		
		case R.id.ButtonRegulations:
			Intent intentRegulations=new Intent();
			intentRegulations.setClass(this,Regulations.class);
			startActivity(intentRegulations);
			break;
		case R.id.ButtonMusik:
			//обработка кнопки музыка
			if(settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)){
				Editor editor = settings.edit();
				editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_OFF);
				editor.apply();
				mediaPlayer.pause();
				//картиночку поменять надо бы
				}
			else{
				Editor editor = settings.edit();
				editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_ON);
				editor.apply();
				mediaPlayer.start();				
			}
			break;

		}
		
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		releaseMP();
		super.onBackPressed();
	}
	
	  private void releaseMP() {
		    if (mediaPlayer != null) {
		      try {
		        mediaPlayer.release();
		        mediaPlayer=null;
		      } catch (Exception e) {
		        e.printStackTrace();
		      }
		    }
		  }
	@Override
	public void onCompletion(MediaPlayer arg0) {
		// TODO Auto-generated method stub
		System.out.println("onComplection");
	}
}
