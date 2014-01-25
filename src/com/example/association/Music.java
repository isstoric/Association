package com.example.association;

import android.content.Context;
import android.media.MediaPlayer;

public class Music {

	private static MediaPlayer mp = null;
	

	   /** Stop old song and start new one */
	public static void play(Context context, int resource) {
	      stop();
	         mp = MediaPlayer.create(context, resource);
	         mp.setLooping(true);
	         mp.start();
	   }
	   

	   /** Stop the music */
	   public static void stop() { 
	      if (mp != null) {
	         mp.stop();
	         mp.release(); 
	         mp = null;
	       
	      }
	   }


}
