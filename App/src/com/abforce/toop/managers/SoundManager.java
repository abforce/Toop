package com.abforce.toop.managers;


public class SoundManager {
	public static State STATE = new State();

	public static void restorePreferences() {
		STATE.isMute = PreferenceHelper.isMute();
	}

	public static void setMute(boolean mute) {
		STATE.isMute = mute;
		PreferenceHelper.setMute(mute);
		
		if(mute){
			RM.mMusic.pause();
		} else {
			RM.mMusic.resume();
		}
	}

	public static void onMenuSceneCreate() {
		if (STATE.isMute) {
			return;
		}
		RM.mMusic.resume();
	}

	public static void onActivityPause() {
		if(!SM.STATE.gameLoaded){
			return;
		}
		if(RM.mMusic.isPlaying()){
			RM.mMusic.pause();
		}
	}

	public static void onActivityResume() {
		if(!SM.STATE.gameLoaded){
			return;
		}
		if(!STATE.isMute){
			try{
				RM.mMusic.resume();
			} catch (Exception ex){}
		}
	}

	public static void onBallHit() {
		if (STATE.isMute) {
			return;
		}
		RM.sBallHit.play();
	}

	public static void onBirdEscapse() {
		if (STATE.isMute) {
			return;
		}
		RM.sBirdEscape.play();
	}

	public static void onBirdHit() {
		if (STATE.isMute) {
			return;
		}
		RM.sBirdHit.play();
	}

	public static void onCoinPick() {
		if (STATE.isMute) {
			return;
		}
		RM.sCoinPick.play();
	}

	public static void onCoinAppear() {
		if (STATE.isMute) {
			return;
		}
		RM.sCoinAppear.play();
	}

	public static void onGamePause() {
		if (STATE.isMute) {
			return;
		}
		RM.sGamePause.play();
	}

	public static void onGameResume() {
		if (STATE.isMute) {
			return;
		}
		RM.sGameResume.play();
	}

	public static void onGameOver() {
		if (STATE.isMute) {
			return;
		}
		RM.sGameOver.play();
	}

	public static void onNewRecored() {
		if (STATE.isMute) {
			return;
		}
		RM.sNewRecord.play();
	}

	public static class State {
		public boolean isMute;
	}
}