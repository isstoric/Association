package com.example.association;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;

public class ViewGame extends Activity implements View.OnClickListener {

	SharedPreferences settings;
	private static Random rand = new Random();
	ArrayList<String> answers;// массив ответов
	ArrayList<ImageView> leftViews;
	ArrayList<ImageView> rightViews;
	ArrayList<String> allExtras;
	ArrayList<ArrayList<String>> rightImages;// массив из всех наборов правых
												// картинок
	ArrayList<Boolean> flagForAnswers;
	DataBaseLogic dbLogic;
	int helpPlace;// номер пары,на которой была вызвана подсказка
	Chronometer mchronometer;
	ImageView ButtonMusic;
	long tenSeconds = 10000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.view_game);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		leftViews = new ArrayList<ImageView>();
		rightViews = new ArrayList<ImageView>();

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

		ButtonMusic = (ImageView) findViewById(R.id.ButtonMusik);
		ButtonMusic.setOnClickListener(this);

		Button ButtonHelp = (Button) findViewById(R.id.ButtonHelp);
		ButtonHelp.setOnClickListener(this);

		settings = getSharedPreferences(Config.APP_PREFERENCES,
				Context.MODE_PRIVATE);
		setMusicImage();
		rightImages = new ArrayList<ArrayList<String>>();
		helpPlace = -1;

		setFlags();

		dbLogic = new DataBaseLogic(this);
		Boolean currentState = dbLogic.getCurrentState();

		answers = new ArrayList<String>();
		for (int i = 0; i < Config.countOfPair; i++) {
			ArrayList<String> onePair = dbLogic.getMainPictures(currentState,
					i + 1);
			Drawable leftImg = getImage(onePair.get(0));
			leftViews.get(i).setImageDrawable(leftImg);
			answers.add(onePair.get(1));
		}

		int countOfRightImg = settings.getInt(
				Config.APP_PREFERENCES_COMPLEXITY, 4);
		allExtras = dbLogic.getExtraPictures(currentState, countOfRightImg);
		int j = 0;
		for (int i = 0; i < Config.countOfPair; i++) {
			ArrayList<String> rights = new ArrayList<String>();
			for (int k = 0; k < countOfRightImg - 1; k++) {
				rights.add(allExtras.get(j));
				j++;
			}
			ArrayList<String> rand = randomizeImg(answers.get(i), rights);
			rightImages.add(rand);
		}

		mchronometer = (Chronometer) findViewById(R.id.timer);
		mchronometer.start();

	}

	public void setMusicImage() {
		if (settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)) {
			ButtonMusic.setImageResource(R.drawable.sound1);
		} else {
			ButtonMusic.setImageResource(R.drawable.sound2);
		}
	}

	public void setFlags() {
		flagForAnswers = new ArrayList<Boolean>();
		for (int i = 0; i < Config.countOfPair; i++) {
			flagForAnswers.add(false);
		}
	}

	public Drawable getImage(String name) {
		return getResources().getDrawable(
				getResources()
						.getIdentifier(name, "drawable", getPackageName()));
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)) {
			Music.play(this, R.raw.dendy);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		Music.stop();
	}

	public ArrayList<String> randomizeImg(String mainImage,
			ArrayList<String> extraImages) {
		ArrayList<String> result = new ArrayList<String>();
		int place = rand.nextInt(extraImages.size());
		result.add(mainImage);
		result.addAll(extraImages);
		String imageOnRandPl = result.get(place);
		result.set(place, mainImage);
		result.set(0, imageOnRandPl);
		return result;
	}

	public void changeImage(int numberOfPair) {
		ArrayList<String> extra = rightImages.get(numberOfPair);
		String imgName = extra.get(0);
		Drawable img = getImage(imgName);
		rightViews.get(numberOfPair).setImageDrawable(img);
		if (imgName.equals(answers.get(numberOfPair))) {
			flagForAnswers.set(numberOfPair, true);
		} else {
			flagForAnswers.set(numberOfPair, false);
		}
		extra.remove(0);
		extra.add(imgName);
		rightImages.set(numberOfPair, extra);
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

	public void statisticTime(long millis) {
		int complexity = settings.getInt(Config.APP_PREFERENCES_COMPLEXITY, 1);
		switch (complexity) {
		case Config.EASY:
			if (settings.contains(Config.APP_PREFERENCES_EASY_TIME)) {
				long easy_time = settings.getLong(
						Config.APP_PREFERENCES_EASY_TIME, 0);
				if (easy_time > millis) {
					Editor editor = settings.edit();
					editor.putLong(Config.APP_PREFERENCES_EASY_TIME, millis);
					editor.apply();
				}
			} else {
				Editor editor = settings.edit();
				editor.putLong(Config.APP_PREFERENCES_EASY_TIME, millis);
				editor.apply();
			}
			break;
		case Config.MEDIUM:
			if (settings.contains(Config.APP_PREFERENCES_MEDIUM_TIME)) {
				long mediumTime = settings.getLong(
						Config.APP_PREFERENCES_MEDIUM_TIME, 0);
				if (mediumTime > millis) {
					Editor editor = settings.edit();
					editor.putLong(Config.APP_PREFERENCES_MEDIUM_TIME, millis);
					editor.apply();
				}
			} else {
				Editor editor = settings.edit();
				editor.putLong(Config.APP_PREFERENCES_MEDIUM_TIME, millis);
				editor.apply();
			}
			break;
		case Config.HARD:
			if (settings.contains(Config.APP_PREFERENCES_HARD_TIME)) {
				long hardTime = settings.getLong(
						Config.APP_PREFERENCES_HARD_TIME, 0);
				if (hardTime > millis) {
					Editor editor = settings.edit();
					editor.putLong(Config.APP_PREFERENCES_HARD_TIME, millis);
					editor.apply();
				}
			} else {
				Editor editor = settings.edit();
				editor.putLong(Config.APP_PREFERENCES_HARD_TIME, millis);
				editor.apply();
			}
			break;
		default:
			break;
		}

	}

	public void addTime() {
		mchronometer.setBase(mchronometer.getBase() - tenSeconds);
	}

	public void checkAnswers() {
		int countOfRightAnswers = 0;
		for (int i = 0; i < Config.countOfPair; i++) {
			if (flagForAnswers.get(i)) {
				countOfRightAnswers++;
			}
		}

		if (countOfRightAnswers == Config.countOfPair) {
			mchronometer.stop();
			long elapsedMillis = SystemClock.elapsedRealtime()
					- mchronometer.getBase();
			statisticTime(elapsedMillis);
			mchronometer.setBase(SystemClock.elapsedRealtime());

			AlertDialog.Builder builder = new AlertDialog.Builder(ViewGame.this);
			String finishTime = outputTime(elapsedMillis);

			builder.setTitle(getString(R.string.LevelIsPassed))
					.setMessage(getString(R.string.YouTimeIs) + finishTime)
					.setCancelable(false)
					.setNegativeButton(getString(R.string.NextLevel),
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									dbLogic.clearCurrentState();
									// проверка,достаточно ли картинок для
									// формирования следующего уровня
									dbLogic.checkCountPictures(settings
											.getInt(Config.APP_PREFERENCES_COUNT_OF_PICTURES,
													0));
									int currentLevel = settings.getInt(
											Config.APP_PREFERENCES_LEVEL, 1);
									Editor editor = settings.edit();
									editor.putInt(Config.APP_PREFERENCES_LEVEL,
											currentLevel++);
									editor.apply();
									Intent intent = new Intent();
									intent.setClass(ViewGame.this,
											ViewGame.class);
									startActivity(intent);
								}
							});
			AlertDialog alert = builder.create();
			alert.show();
		}

	}

	public void giveRandomHelp() {
		if (helpPlace == -1) {
			// увеличиваем время на 10 сек
			addTime();
			ArrayList<Integer> wrongPair = new ArrayList<Integer>();
			for (int i = 0; i < Config.countOfPair; i++) {
				if (!flagForAnswers.get(i)) {
					wrongPair.add(i);
				}
			}
			int place = rand.nextInt(wrongPair.size());
			rightViews.get(wrongPair.get(place)).setImageResource(
					R.drawable.help);
			helpPlace = wrongPair.get(place);
		}
	}

	public void checkHelpPlace(int viewPlace) {
		if (viewPlace == helpPlace) {
			helpPlace = -1;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		// обработка нажатия кнопок

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
			giveRandomHelp();
			break;

		case R.id.ButtonMusik:
			// обработка кнопки музыка
			if (settings.getBoolean(Config.APP_PREFERENCES_SOUND, true)) {
				Editor editor = settings.edit();
				editor.putBoolean(Config.APP_PREFERENCES_SOUND,
						Config.SOUND_OFF);
				editor.apply();
				Music.stop();
				ButtonMusic.setImageResource(R.drawable.sound2);
			} else {
				Editor editor = settings.edit();
				editor.putBoolean(Config.APP_PREFERENCES_SOUND, Config.SOUND_ON);
				editor.apply();
				ButtonMusic.setImageResource(R.drawable.sound1);
				Music.play(this, R.raw.dendy);
			}
			break;

		}

	}

}
