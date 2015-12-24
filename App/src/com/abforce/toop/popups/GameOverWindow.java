package com.abforce.toop.popups;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBounceOut;

import android.text.format.Time;

import com.abforce.toop.entities.SSprite;
import com.abforce.toop.managers.PreferenceHelper;
import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SM;
import com.abforce.toop.managers.SoundManager;
import com.abforce.toop.managers.SM.SceneType;
import com.abforce.toop.models.HighScorePack;
import com.abforce.toop.scenehandlers.MainSceneHandler;

public class GameOverWindow {
	Scene mChildScene;
	Scene mParentScene;
	
	Sprite mFrame;
	
	public void attachToScene(Scene scene){
		mChildScene = new Scene();
		mChildScene.setBackgroundEnabled(false);
		
		final float frmWidth = RM.txFrame.getWidth();
		final float frmHeight = RM.txFrame.getHeight();
		
		// Background dimming
		Rectangle rect = new Rectangle(0, 0, RM.CW, RM.CH, RM.VBO);
		rect.setColor(0, 0, 0, 0.6f);
	
		// Frame
		mFrame = new SSprite(RM.CW / 2 - RM.SX * frmWidth / 2, RM.CH / 2 - RM.SY * frmHeight / 2, RM.txFrame);
		
		// Frame animation
		MoveYModifier modifier = new MoveYModifier(0.5f, - frmHeight, mFrame.getY(), EaseBounceOut.getInstance());
		mFrame.registerEntityModifier(modifier);
		
		// Main Menu
		Sprite btnMainMenu = new Sprite(50, frmHeight - 110, RM.txBtnMainMenu, RM.VBO){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					setScale(1.2f);
				}
				if(pSceneTouchEvent.isActionUp()){
					setScale(1.0f);
					SM.setCurrentScene(SceneType.MENU, false);
				}
				return true;
			};
		};
		mFrame.attachChild(btnMainMenu);
		
		// Restart
		Sprite btnRestart = new Sprite(frmWidth - 110, frmHeight - 110, RM.txBtnRestart, RM.VBO){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					setScale(1.2f);
				}
				if(pSceneTouchEvent.isActionUp()){
					setScale(1.0f);
					SM.setCurrentScene(SceneType.MAINGAME, true);
				}
				return true;
			};
		};
		mFrame.attachChild(btnRestart);
		
		// Game over title
		float textWidth = RM.txGamePaused.getWidth();
		Sprite spGameOver = new Sprite(frmWidth / 2 - textWidth / 2, 25, RM.txGameOver, RM.VBO);
		mFrame.attachChild(spGameOver);
		
		// Score
		final int score = MainSceneHandler.STATE.score.mScore;
		Text textscore = new Text(0, 0, RM.fnScore, String.valueOf(score), RM.VBO){
			boolean init = false;
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if(!init){
					setX(frmWidth / 2 - getWidth() / 2);
					setY(frmHeight / 2 - getHeight() / 2 - 15);
					init = true;
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
		textscore.setColor(0.12f, 0.43f, 0.92f);
		mFrame.attachChild(textscore);
		
		// Checking for beating the global record
		HighScorePack pack = PreferenceHelper.getGlobalHighScorePack();
		if(pack != null && score > pack.score){
			float tw = RM.txRecord.getWidth();
			Sprite record = new Sprite(frmWidth - 3 * tw / 4 - 5, 20, RM.txRecord, RM.VBO);
			record.setRotation(40);
			record.setAlpha(0);
			ParallelEntityModifier modifier2 = new ParallelEntityModifier(new AlphaModifier(0.3f, 0, 1), new ScaleModifier(0.3f, 2.5f, 1));
			SequenceEntityModifier modifier3 = new SequenceEntityModifier(new DelayModifier(0.6f), modifier2);
			record.registerEntityModifier(modifier3);
			mFrame.attachChild(record);
			
			RM.STATE.engine.registerUpdateHandler(new TimerHandler(0.6f, new ITimerCallback() {
				
				@Override
				public void onTimePassed(TimerHandler pTimerHandler) {
					RM.STATE.engine.unregisterUpdateHandler(pTimerHandler);
					SoundManager.onNewRecored();
				}
			}));
			
			float btnWidth = RM.txBtnSubmit.getWidth();
			ButtonSprite btnSubmit = new ButtonSprite(frmWidth / 2 - btnWidth / 2, frmHeight - 90, RM.txBtnSubmit, RM.VBO){
				@Override
				public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
						float pTouchAreaLocalX, float pTouchAreaLocalY) {
					if(pSceneTouchEvent.isActionUp()){
						SM.setCurrentScene(SceneType.MENU, false);
						SM.openSubmitScorePopup(score);
						return true;
					}
					return super
							.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
				}
			};
			mFrame.attachChild(btnSubmit);
			mChildScene.registerTouchArea(btnSubmit);
		}
		
		// Checking local high score
		HighScorePack packLocal = PreferenceHelper.getLocalHighScorePack();
		if(score > packLocal.score){
			Time time = new Time();
			time.setToNow();
			HighScorePack newScore = new HighScorePack(null, score, time.toMillis(false));
			PreferenceHelper.setLocalHighScorePack(newScore);
		}
		
		mChildScene.registerTouchArea(btnMainMenu);
		mChildScene.registerTouchArea(btnRestart);
		mChildScene.attachChild(rect);
		mChildScene.attachChild(mFrame);
		scene.setChildScene(mChildScene, false, true, true);
		mParentScene = scene;
	}
	
	public void dismiss(){
		MainSceneHandler.STATE.gamePaused = false;
		float frmHeight = RM.txFrame.getHeight() * RM.SY;
		MoveYModifier modifier = new MoveYModifier(0.3f, mFrame.getY(), - frmHeight){
			@Override
			protected void onModifierFinished(IEntity pItem) {
				mParentScene.clearChildScene();
			}
		};
		mFrame.registerEntityModifier(modifier);
	}
}
