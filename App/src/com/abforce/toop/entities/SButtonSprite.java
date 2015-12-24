package com.abforce.toop.entities;

import org.andengine.entity.sprite.ButtonSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITiledTextureRegion;

import com.abforce.toop.managers.RM;

public class SButtonSprite extends ButtonSprite{

	public SButtonSprite(float pX, float pY,
			ITiledTextureRegion pTiledTextureRegion) {
		super(pX, pY, pTiledTextureRegion, RM.VBO);
		setScaleCenter(0, 0);
		setScale(RM.SX, RM.SY);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {
		
		if(pSceneTouchEvent.isActionDown()){
			setCurrentTileIndex(1);
		}
		if(pSceneTouchEvent.isActionUp()){
			setCurrentTileIndex(0);
		}
		
		return super
				.onAreaTouched(pSceneTouchEvent, pTouchAreaLocalX, pTouchAreaLocalY);
	}
}
