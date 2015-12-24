package com.abforce.toop.entities;

import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.abforce.toop.managers.RM;

public class SSprite extends Sprite{

	public SSprite(float pX, float pY, ITextureRegion pTextureRegion) {
		super(pX, pY, pTextureRegion, RM.VBO);
		setScaleCenter(0, 0);
		setRotationCenter(RM.SX * pTextureRegion.getWidth() / 2, RM.SY * pTextureRegion.getHeight() / 2);
		setScale(RM.SX, RM.SY);
	}
	
	@Override
	public void setScale(float pScale) {
		float factor = (RM.SX + RM.SY) / 2;
		super.setScale(pScale * factor);
	}
	
	
	
//	@Override
//	public float getWidth() {
//		return super.getWidth() * RM.SX;
//	}
//
//	@Override
//	public float getHeight() {
//		return super.getHeight() * RM.SY;
//	}
}
