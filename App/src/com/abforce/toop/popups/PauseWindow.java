package com.abforce.toop.popups;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.input.touch.TouchEvent;
import org.andengine.util.modifier.ease.EaseBounceOut;

import com.abforce.toop.entities.SSprite;
import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SM;
import com.abforce.toop.managers.SM.SceneType;
import com.abforce.toop.scenehandlers.MainSceneHandler;

public class PauseWindow {

	Scene mChildScene;
	Scene mParentScene;
	
	SSprite mFrame;
	
	public void attachToScene(Scene scene){
		mChildScene = new Scene();
		mChildScene.setBackgroundEnabled(false);
		
		final float frmWidth = RM.txFrame.getWidth();
		final float frmHeight = RM.txFrame.getHeight();
		
		// Background dimming
		Rectangle rect = new Rectangle(0, 0, RM.CW, RM.CH, RM.VBO);
		rect.setColor(0, 0, 0, 0.6f);
	
		mFrame = new SSprite(RM.CW / 2 - RM.SX * frmWidth / 2,
				RM.CH / 2 - RM.SY * frmHeight / 2, RM.txFrame);
		
		MoveYModifier modifier = new MoveYModifier(0.5f, - frmHeight, mFrame.getY(), EaseBounceOut.getInstance());
		mFrame.registerEntityModifier(modifier);
		
		// Main Menu
		Sprite btnMainMenu = new Sprite(50, frmHeight - 110, RM.txBtnMainMenu, RM.STATE.engine.getVertexBufferObjectManager()){
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
		float btnWidth = RM.txBtnRestart.getWidth();
		Sprite btnRestart = new Sprite(frmWidth / 2 - btnWidth / 2 , frmHeight - 110, RM.txBtnRestart, RM.STATE.engine.getVertexBufferObjectManager()){
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
		
		// Resume
		Sprite btnResume = new Sprite(frmWidth - 110, frmHeight - 110, RM.txBtnResume, RM.STATE.engine.getVertexBufferObjectManager()){
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent, float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionDown()){
					setScale(1.2f);
				}
				if(pSceneTouchEvent.isActionUp()){
					setScale(1.0f);
					dismiss();
				}
				return true;
			};
		};
		mFrame.attachChild(btnResume);
		
		float textWidth = RM.txGamePaused.getWidth();
		Sprite spGamePaused = new Sprite(frmWidth / 2 - textWidth / 2, 25, RM.txGamePaused, RM.STATE.engine.getVertexBufferObjectManager());
		mFrame.attachChild(spGamePaused);
		
		int score = MainSceneHandler.STATE.score.mScore;
		Text textscore = new Text(0, 0, RM.fnScore, String.valueOf(score), RM.STATE.engine.getVertexBufferObjectManager()){
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
		
		mChildScene.registerTouchArea(btnMainMenu);
		mChildScene.registerTouchArea(btnRestart);
		mChildScene.registerTouchArea(btnResume);
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
