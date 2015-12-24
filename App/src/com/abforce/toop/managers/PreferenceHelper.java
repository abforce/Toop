package com.abforce.toop.managers;

import com.abforce.toop.models.HighScorePack;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

public class PreferenceHelper {
	private static final State STATE = new State();
	private static final String KEY_MUTE = "KEY_MUTE";
	private static final String KEY_GLOBAL_HIGHSCORE_NAME = "KEY_GLOBAL_HIGHSCORE_NAME";
	private static final String KEY_GLOBAL_HIGHSCORE_SCORE = "KEY_GLOBAL_HIGHSCORE_SCORE";
	private static final String KEY_GLOBAL_HIGHSCORE_MILLIS = "KEY_GLOBAL_HIGHSCORE_MILLIS";
	private static final String KEY_LOCAL_HIGHSCORE_NAME = "KEY_LOCAL_HIGHSCORE_NAME";
	private static final String KEY_LOCAL_HIGHSCORE_SCORE = "KEY_LOCAL_HIGHSCORE_SCORE";
	private static final String KEY_LOCAL_HIGHSCORE_MILLIS = "KEY_LOCAL_HIGHSCORE_MILLIS";
	
	public static void setup(){
		Context context = RM.STATE.activity.getApplicationContext();
		STATE.preferences = PreferenceManager.getDefaultSharedPreferences(context);
	}
	
	public static boolean isMute(){
		return STATE.preferences.getBoolean(KEY_MUTE, false);
	}
	
	public static void setMute(boolean mute){
		STATE.preferences.edit().putBoolean(KEY_MUTE, mute).commit();
	}
	
	public static HighScorePack getGlobalHighScorePack(){
		String name = STATE.preferences.getString(KEY_GLOBAL_HIGHSCORE_NAME, null);
		int score = STATE.preferences.getInt(KEY_GLOBAL_HIGHSCORE_SCORE, 0);
		long millis = STATE.preferences.getLong(KEY_GLOBAL_HIGHSCORE_MILLIS, 0);
		
		HighScorePack pack = new HighScorePack(name, score, millis);
		pack.normalize();
		return pack;
	}
	
	public static void setGlobalHighScorePack(HighScorePack pack){
		Editor editor = STATE.preferences.edit();
		editor.putString(KEY_GLOBAL_HIGHSCORE_NAME, pack.name);
		editor.putInt(KEY_GLOBAL_HIGHSCORE_SCORE, pack.score);
		editor.putLong(KEY_GLOBAL_HIGHSCORE_MILLIS, pack.millis);
		editor.commit();
	}
	
	public static HighScorePack getLocalHighScorePack(){
		String name = STATE.preferences.getString(KEY_LOCAL_HIGHSCORE_NAME, null);
		int score = STATE.preferences.getInt(KEY_LOCAL_HIGHSCORE_SCORE, 0);
		long millis = STATE.preferences.getLong(KEY_LOCAL_HIGHSCORE_MILLIS, 0);
		
		HighScorePack pack = new HighScorePack(name, score, millis);
		pack.normalize();
		return pack;
	}
	
	public static void setLocalHighScorePack(HighScorePack pack){
		Editor editor = STATE.preferences.edit();
		editor.putString(KEY_LOCAL_HIGHSCORE_NAME, pack.name);
		editor.putInt(KEY_LOCAL_HIGHSCORE_SCORE, pack.score);
		editor.putLong(KEY_LOCAL_HIGHSCORE_MILLIS, pack.millis);
		editor.commit();
	}
	
	private static class State{
		SharedPreferences preferences;
	}
}
