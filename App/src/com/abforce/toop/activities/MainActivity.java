package com.abforce.toop.activities;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.util.DisplayMetrics;
import android.widget.Toast;

import com.abforce.toop.managers.NetworkHelper;
import com.abforce.toop.managers.PreferenceHelper;
import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SM;
import com.abforce.toop.managers.SoundManager;
import com.abforce.toop.managers.NetworkHelper.OnTaskFinishedListener;
import com.abforce.toop.managers.SM.SceneType;
import com.abforce.toop.models.HighScorePack;
import com.abforce.toop.scenehandlers.MainSceneHandler;
import com.abforce.toop.scenehandlers.MenuSceneHandler;
import com.abforce.toop.scenehandlers.SplashSceneHandler;

public class MainActivity extends BaseGameActivity {

	@Override
	public EngineOptions onCreateEngineOptions() {
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		int CAMERA_WIDTH = metrics.widthPixels;
		int CAMERA_HEIGHT = metrics.heightPixels;
		
		Camera camera = new Camera(0.0f, 0.0f, CAMERA_WIDTH, CAMERA_HEIGHT);
		
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_FIXED,
				new FillResolutionPolicy(), camera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		engineOptions.getRenderOptions().setDithering(true);
		engineOptions.getRenderOptions().setMultiSampling(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(
			OnCreateResourcesCallback pOnCreateResourcesCallback)
			throws Exception {
		RM.setup(this, mEngine);
		PreferenceHelper.setup();
		SoundManager.restorePreferences();
		RM.loadSplashSceneResources();
		pOnCreateResourcesCallback.onCreateResourcesFinished();
	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback)
			throws Exception {
		SplashSceneHandler.create();
		pOnCreateSceneCallback.onCreateSceneFinished(SplashSceneHandler.STATE.scene);
	}

	@Override
	public void onPopulateScene(Scene pScene,
			OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {

		mEngine.registerUpdateHandler(new TimerHandler(3f,
				new ITimerCallback() {
					public void onTimePassed(final TimerHandler pTimerHandler) {
						mEngine.unregisterUpdateHandler(pTimerHandler);
						RM.loadMenuSceneResources();
						RM.loadGameSceneResources();
						RM.loadSoundsAndMusics();
						SM.STATE.gameLoaded = true;
						SM.setCurrentScene(SceneType.MENU, true);
						if(NetworkHelper.isNetworkAvailable()){
							NetworkHelper.asyncGetHighScore(new OnTaskFinishedListener<HighScorePack>() {
								
								@Override
								public void onFinished(HighScorePack pack) {
									if(pack == null){
										return;
									}
									PreferenceHelper.setGlobalHighScorePack(pack);
									MenuSceneHandler.redrawHighScore();
									final Context context = RM.STATE.activity.getApplicationContext();
									RM.STATE.activity.runOnUiThread(new Runnable() {

										@Override
										public void run() {
											Toast.makeText(
													context,
													"بالاترین امتیاز بروز آوری شد",
													Toast.LENGTH_SHORT).show();
										}
									});
								}
							});
						} else {
							MenuSceneHandler.notifyNetworkUnavailable();
						}
					}
				}));
		pOnPopulateSceneCallback.onPopulateSceneFinished();
	}
	
	@Override
	public void onBackPressed() {
		if(SM.STATE.currentScene == SceneType.MAINGAME){
			if(!MainSceneHandler.STATE.gameStarted){
				SM.setCurrentScene(SceneType.MENU, false);
				return;
			}
			if(MainSceneHandler.STATE.gamePaused){
				MainSceneHandler.doResumeGame();
			} else {
				MainSceneHandler.doPauseGame();
			}
			return;
		}
		super.onBackPressed();
	}

	@Override
	public synchronized void onResumeGame() {
		super.onResumeGame();
		if(SM.STATE.currentScene == SceneType.MAINGAME){
			MainSceneHandler.onResumeGame();
		}
		SoundManager.onActivityResume();
	}

	@Override
	public synchronized void onPauseGame() {
		super.onPauseGame();
		if(SM.STATE.currentScene == SceneType.MAINGAME){
			MainSceneHandler.onPauseGame();
		}
		SoundManager.onActivityPause();
	}
}
