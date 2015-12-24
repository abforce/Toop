package com.abforce.toop.managers;

import com.abforce.toop.popups.HighScoresPopup;
import com.abforce.toop.popups.ScoreSubmitPopup;
import com.abforce.toop.scenehandlers.MainSceneHandler;
import com.abforce.toop.scenehandlers.MenuSceneHandler;

public class SM {

	public static final State STATE = new State();

	public enum SceneType {
		SPLASH, MENU, MAINGAME
	}

	public static void openSubmitScorePopup(final int score){
		RM.STATE.activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				ScoreSubmitPopup popup = new ScoreSubmitPopup(RM.STATE.activity);
				popup.show(score);
			}
		});
	}
	
	public static void openHighScoresPopup(){
		RM.STATE.activity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				HighScoresPopup popup = new HighScoresPopup(RM.STATE.activity);
				popup.show();
			}
		});
	}
	
	public static void setCurrentScene(SceneType scene, boolean reset) {
		STATE.currentScene = scene;
		switch (scene) {
		case SPLASH:
			break;
		case MENU:
			if (reset) {
				MenuSceneHandler.create();
			}
			RM.STATE.engine.setScene(MenuSceneHandler.STATE.scene);
			break;
		case MAINGAME:
			if (reset) {
				MainSceneHandler.create();
			}
			RM.STATE.engine.setScene(MainSceneHandler.STATE.scene);
			break;
		}
	}

	public static class State {
		public SceneType currentScene;
		public boolean gameLoaded;
	}
}
