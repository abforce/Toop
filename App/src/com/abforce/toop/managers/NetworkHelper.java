package com.abforce.toop.managers;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.regex.Pattern;

import com.abforce.toop.models.HighScorePack;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

public class NetworkHelper {
	private static final String SERVICE_ADDRESS = "http://abforce.ir/toop";

	public static final int UNKNOWN_ERROR = 0;
	public static final int OKAY = 1;
	public static final int INVALID_HIGH_SCORE = 2;
	
	public static boolean isNetworkAvailable() {
		Context context = RM.STATE.activity.getApplicationContext();
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null && activeNetworkInfo.isConnected();
	}

	public static HighScorePack getHighScorePack() {
		String line = null;
		try {
			String s = SERVICE_ADDRESS + "?type=get";
			URL url = new URL(s);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.connect();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return null;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			line = reader.readLine();
		} catch (Exception ex) {
			return null;
		}
		String[] parts = line.split(Pattern.quote("|"));
		if(parts.length != 3){
			return null;
		}
		String name = parts[0];
		String score = parts[1];
		String millis = parts[2];
		HighScorePack pack = new HighScorePack(name, Integer.parseInt(score), Long.parseLong(millis));
		pack.normalize();
		return pack;
	}

	public static void asyncGetHighScore(final OnTaskFinishedListener<HighScorePack> listener){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				HighScorePack pack = getHighScorePack();
				listener.onFinished(pack);
			}
		}).start();
	}
	
	public static void asyncSetHighScorePack(final HighScorePack pack, final OnTaskFinishedListener<Integer> listener){
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				final int result = setHighScorePack(pack);
				handler.post(new Runnable() {
					
					@Override
					public void run() {
						listener.onFinished(result);
					}
				});
			}
		}).start();
	}
	
	public static int setHighScorePack(HighScorePack pack){
		String line = null;
		try {
			String name = pack.name;
			
			String query = String.format("?type=set&name=%s&score=%d",
					URLEncoder.encode(name), pack.score);
			
			String s = SERVICE_ADDRESS + query;
			URL url = new URL(s);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.connect();
			if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
				return UNKNOWN_ERROR;
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "utf-8"));
			line = reader.readLine();
		} catch (Exception ex) {
			return UNKNOWN_ERROR;
		}
		
		if(line.equals("ok")){
			return OKAY;
		}
		if(line.equals("invalid")){
			return INVALID_HIGH_SCORE;
		}
		return UNKNOWN_ERROR;
	}
	
	public interface OnTaskFinishedListener<T>{
		public void onFinished(T result);
	}
}
