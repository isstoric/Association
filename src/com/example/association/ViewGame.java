package com.example.association;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.drm.DrmStore.RightsStatus;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.util.Log;
//import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class ViewGame extends Activity implements View.OnClickListener {
	
	SharedPreferences settings;
	private static Random rand=new Random();
	ArrayList<String> answers;//массив ответов
	ArrayList<ImageView> leftViews;
	ArrayList<ImageView> rightViews;
	ArrayList<String> allExtras;
	ArrayList<ArrayList<String>> rightImages;//массив из всех наборов правых картинок
	ArrayList<Boolean> flagForAnswers;
	DataBaseLogic dbLogic;
	int helpPlace;//номер пары,на которой была вызвана подсказка
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d("ViewGame","onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_game);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);	
		
		leftViews=new ArrayList<ImageView>();
		rightViews=new ArrayList<ImageView>();
		
		ImageView ButtonWindow10 = (ImageView) findViewById(R.id.Window10);
		ButtonWindow10.setOnClickListener(this);
		leftViews.add(ButtonWindow10);

		ImageView ButtonWindow11 = (ImageView) findViewById(R.id.Window11);
		ButtonWindow11.setOnClickListener(this);
		rightViews.add(ButtonWindow11);
		
		ImageView ButtonWindow20 = (ImageView) findViewById(R.id.Window20);
		ButtonWindow20.setOnClickListener(this);
		leftViews.add(ButtonWindow20);
		
		ImageView ButtonWindow21 = (ImageView) findViewById(R.id.Window21);
		ButtonWindow21.setOnClickListener(this);
		rightViews.add(ButtonWindow21);
		
		ImageView ButtonWindow30 = (ImageView) findViewById(R.id.Window30);
		ButtonWindow30.setOnClickListener(this);
		leftViews.add(ButtonWindow30);
		
		ImageView ButtonWindow31 = (ImageView) findViewById(R.id.Window31);
		ButtonWindow31.setOnClickListener(this);
		rightViews.add(ButtonWindow31);
		
		ImageView ButtonWindow40 = (ImageView) findViewById(R.id.Window40);
		ButtonWindow40.setOnClickListener(this);
		leftViews.add(ButtonWindow40);
		
		ImageView ButtonWindow41 = (ImageView) findViewById(R.id.Window41);
		ButtonWindow41.setOnClickListener(this);
		rightViews.add(ButtonWindow41);
		

		Button ButtonMusik = (Button) findViewById(R.id.ButtonMusik);
		ButtonMusik.setOnClickListener(this);
		
		Button ButtonHelp = (Button) findViewById(R.id.ButtonHelp);
		ButtonHelp.setOnClickListener(this);

		
		Button ButtonTimer = (Button) findViewById(R.id.ButtonTimer);
		ButtonTimer.setOnClickListener(this);
		
		settings = getSharedPreferences(Config.APP_PREFERENCES, Context.MODE_PRIVATE);
		rightImages=new ArrayList<ArrayList<String>>();
		helpPlace=-1; 
		
		setFlags();
		
		Log.d("ViewGame","before dbLogic");
		dbLogic=new DataBaseLogic(this);
		Boolean currentState=dbLogic.getCurrentState();
		
		
		answers=new ArrayList<String>();
		for (int i=0;i<Config.countOfPair;i++){
			ArrayList<String> onePair=dbLogic.getMainPictures(currentState, i+1);
			Log.d("ViewGame","onePair "+onePair);
			Drawable leftImg=getImage(onePair.get(0));
			leftViews.get(i).setImageDrawable(leftImg);
			answers.add(onePair.get(1));			
		}
		
		//setDrawableForLeft();
		//setAnswers();

		int countOfRightImg=settings.getInt(Config.APP_PREFERENCES_COMPLEXITY, 4);
		Log.d("ViewGame","count="+countOfRightImg);
		
		//setExtra();
		allExtras=dbLogic.getExtraPictures(currentState, countOfRightImg);
		int j=0;
		for(int i=0;i<Config.countOfPair;i++){
			ArrayList<String> rights=new ArrayList<String>();
			for(int k=0;k<countOfRightImg-1;k++){
				rights.add(allExtras.get(j));
				j++;
			}
			Log.d("ViewGame","rights "+i+":"+rights);
			ArrayList<String> rand=randomizeImg(answers.get(i), rights);
			Log.d("ViewGame","rand "+i+":"+rand);
			rightImages.add(rand);			
		}
		Log.d("ViewGame","rightsImages "+rightImages);
		
	
		
		}
	public void setFlags(){
		flagForAnswers=new ArrayList<Boolean>();
		for(int i=0;i<Config.countOfPair;i++){
		flagForAnswers.add(false);}
		Log.d("ViewGame","setFlag"+flagForAnswers);
	}
	public void setExtra(){
		allExtras=new ArrayList<String>();
		allExtras.add("img_001_002");
		allExtras.add("img_005_003");
		allExtras.add("img_004_003");
		
		allExtras.add("img_019_003");
		allExtras.add("img_018_003");
		allExtras.add("img_019_001");
		
		allExtras.add("img_017_001");
		allExtras.add("img_016_004");
		allExtras.add("img_015_002");
		
		allExtras.add("img_014_001");
		allExtras.add("img_011_004");
		allExtras.add("img_018_002");
		Log.d("ViewGame","extras="+allExtras);
	}
	
	public void setAnswers(){
		answers.add("img_003_003");
		answers.add("img_013_003");
		answers.add("img_005_004");
		answers.add("img_009_003");

		Log.d("ViewGame","answers "+answers);
	}
	public void setDrawableForLeft(){
		Drawable leftImg1=getImage("img_003_001");
		leftViews.get(0).setImageDrawable(leftImg1);
		
		Drawable leftImg2=getImage("img_013_002");
		leftViews.get(1).setImageDrawable(leftImg2);
		
		Drawable leftImg3=getImage("img_005_002");
		leftViews.get(2).setImageDrawable(leftImg3);
		
		Drawable leftImg4=getImage("img_009_001");
		leftViews.get(3).setImageDrawable(leftImg4);
	}
	
	public Drawable getImage(String name) {
	    return getResources().getDrawable(
	            getResources().getIdentifier(name, "drawable",
	                    getPackageName()));
	}
	   @Override
	   protected void onResume() {
	      super.onResume();
	      if(settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)){
	      Music.play(this, R.raw.dendy);
	      }
	   }

	   @Override
	   protected void onPause() {
	      super.onPause();
	      Music.stop();
	   }
	   

	
		public ArrayList<String> randomizeImg(String mainImage,ArrayList<String> extraImages){
			ArrayList<String> result=new ArrayList<String>();
			int place=rand.nextInt(extraImages.size());
			result.add(mainImage);
			result.addAll(extraImages);
			String imageOnRandPl=result.get(place);
			result.set(place, mainImage);
			result.set(0, imageOnRandPl);
			return result;
		}
public void changeImage(int numberOfPair){
	ArrayList<String> extra=rightImages.get(numberOfPair);
	String imgName=extra.get(0);			
	Drawable img=getImage(imgName);
	rightViews.get(numberOfPair).setImageDrawable(img);
	if(imgName.equals(answers.get(numberOfPair))){
		flagForAnswers.set(numberOfPair, true);
	}else{
		flagForAnswers.set(numberOfPair, false);
	}
	Log.d("ViewGame","flags="+flagForAnswers);
	extra.remove(0);
	extra.add(imgName);
	rightImages.set(numberOfPair, extra);
}
public void checkAnswers(){
	int countOfRightAnswers=0;
	for(int i=0;i<Config.countOfPair;i++){
		if(flagForAnswers.get(i)){
			countOfRightAnswers++;
		}
	}
	if(countOfRightAnswers==Config.countOfPair){
		Log.d("ViewGame","OK!!!");		
		AlertDialog.Builder builder = new AlertDialog.Builder(ViewGame.this);
		//TODO запихать в strings
		builder.setTitle("Уровень пройден!")
				.setMessage("Ваше время: ололо")
				.setCancelable(false)
				.setNegativeButton("Следующий уровень",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dbLogic.clearCurrentState();
								//проверка,достаточно ли картинок для формирования следующего уровня
								dbLogic.checkCountPictures(settings.getInt(Config.APP_PREFERENCES_COUNT_OF_PICTURES, 0));
								int currentLevel=settings.getInt(Config.APP_PREFERENCES_LEVEL, 1);
								Editor editor = settings.edit();
								editor.putInt(Config.APP_PREFERENCES_LEVEL, currentLevel++);
								editor.apply();
								Intent intent=new Intent();
								intent.setClass(ViewGame.this,ViewGame.class);
								startActivity(intent);
							}
						});
		AlertDialog alert = builder.create();
		alert.show();
	}
	
}

public void giveRandomHelp(){
	if(helpPlace==-1){
	ArrayList<Integer> wrongPair=new ArrayList<Integer>();
	for(int i=0;i<Config.countOfPair;i++){
		if(!flagForAnswers.get(i)){
			wrongPair.add(i);
		}
	}
	int place=rand.nextInt(wrongPair.size());
	rightViews.get(wrongPair.get(place)).setImageResource(R.drawable.help);
	helpPlace=wrongPair.get(place);
	}
}

public void checkHelpPlace(int viewPlace){
	if(viewPlace==helpPlace){
		helpPlace=-1;
	}
}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		//обработка нажатия кнопок
		
		case R.id.Window11:
			checkHelpPlace(0);
			changeImage(0);		
			checkAnswers();
			break;			
		case R.id.Window21:
			checkHelpPlace(1);
			changeImage(1);	
			checkAnswers();
			break;
		case R.id.Window31:
			checkHelpPlace(2);
			changeImage(2);	
			checkAnswers();
			break;
		case R.id.Window41:
			checkHelpPlace(3);
			changeImage(3);	
			checkAnswers();
			break;
			
		case R.id.ButtonHelp:
			//TODO время увеличить
			giveRandomHelp();
			break;
				
		
		case R.id.ButtonMusik:
			//обработка кнопки музыка
			if(settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)){
				Editor editor = settings.edit();
				editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_OFF);
				editor.apply();
				Music.stop();
				//картиночку поменять надо бы
				}
			else{
				Editor editor = settings.edit();
				editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_ON);
				editor.apply();
				Music.play(this, R.raw.dendy);	
			}
			break;

		}
		
	}
	

}
