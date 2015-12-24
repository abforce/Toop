package com.abforce.toop.entities;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.scene.Scene;

import com.abforce.toop.managers.RM;

public class Clouds {

	SSprite mSprite;
	
	public void attachToScene(Scene scene){
		float x = - RM.txClouds.getWidth() * RM.SX;
		float y = 40 * RM.SY;
		
		mSprite = new SSprite(x, y, RM.txClouds);
		MoveXModifier modifier = new MoveXModifier(25, x, RM.STATE.engine.getCamera().getWidth()){
			@Override
			protected void onModifierFinished(IEntity pItem) {
				RM.STATE.engine.runOnUpdateThread(new Runnable() {
					
					@Override
					public void run() {
						mSprite.detachSelf();
						mSprite.dispose();
					}
				});
			}
		};
		mSprite.registerEntityModifier(modifier);
		scene.attachChild(mSprite);
	}
}
