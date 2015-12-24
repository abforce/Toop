package com.abforce.toop.scenehandlers;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.DelayModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.opengl.util.GLState;

import com.abforce.toop.entities.SSprite;
import com.abforce.toop.managers.RM;

public class SplashSceneHandler {
	public static final State STATE = new State();
	
	public static void create(){
		STATE.scene = new Scene();
		STATE.scene.setBackground(new Background(0, 0, 0));
		float splashWidth = RM.txSplash.getWidth() * RM.SX;
		float splashHeight = RM.txSplash.getHeight() * RM.SY;
		SSprite splash = new SSprite((RM.CW - splashWidth) * 0.5f, (RM.CH - splashHeight) * 0.5f, RM.txSplash) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		
		splash.setAlpha(0);
		STATE.scene.attachChild(splash);
		splash.registerEntityModifier(new SequenceEntityModifier(new DelayModifier(1), new AlphaModifier(2, 0, 1)));
	}
	
	public static class State{
		public Scene scene;
	}
}
