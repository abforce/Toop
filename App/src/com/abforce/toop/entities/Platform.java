package com.abforce.toop.entities;

import org.andengine.entity.scene.Scene;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.constants.PhysicsConstants;
import org.andengine.opengl.texture.region.ITextureRegion;

import com.abforce.toop.managers.RM;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Platform {
	private static final FixtureDef PLATFORM_FIX = PhysicsFactory.createFixtureDef(0.0f, 0.0f, 0.5f);
	
	public static final int TOP = 0;
	public static final int LEFT = 1;
	public static final int BOTTOM = 2;
	public static final int RIGHT = 3;
	
	int mType;
	float mBase;
	float mLow;
	float mHigh;
	float mBaseVelocity = 0f;
	
	public SSprite mSprite;
	public Body mBody;
	
	public Platform(int type, float base, float low, float high){
		mType = type;
		mBase = base;
		mLow = low;
		mHigh = high;
		
		mSprite = new SSprite(getInitialX(), getInitialY(), getTexture()){
			@Override
			protected void onManagedUpdate(float pSecondsElapsed) {
				if(isCollidedWithLowerBound()){
					resetPositionToLowerBound();
				}
				if(isCollidedWithUpperBound()){
					resetPositionToUpperBound();
				}
				super.onManagedUpdate(pSecondsElapsed);
			}
		};
	}
	
	public void attachToScene(PhysicsWorld physicsWorld, Scene scene){
		mBody = PhysicsFactory.createBoxBody(physicsWorld, mSprite, BodyType.KinematicBody, PLATFORM_FIX);
		scene.attachChild(mSprite);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(mSprite, mBody));
	}
	
	public void setBaseVelocity(float velocity){
		mBaseVelocity = velocity;
	}
	
	public void setLinearVelocity(float velocity){
		if(isCollidedWithLowerBound() && (velocity <= mBaseVelocity)){
			mBody.setLinearVelocity(0, 0);
			return;
		}
		if(isCollidedWithUpperBound() && (velocity >= mBaseVelocity)){
			mBody.setLinearVelocity(0, 0);
			return;
		}
		
		if(mType == TOP || mType == BOTTOM){
			mBody.setLinearVelocity(velocity - mBaseVelocity, 0);
		} else {
			mBody.setLinearVelocity(0, velocity - mBaseVelocity);
		}
	}
	
	private boolean isCollidedWithLowerBound(){
		if(mType == TOP || mType == BOTTOM){
			return mSprite.getX() <= mLow;
		} else {
			return mSprite.getY() <= mLow;
		}
	}
	
	private boolean isCollidedWithUpperBound(){
		if(mType == TOP || mType == BOTTOM){
			return mSprite.getX() + mSprite.getWidthScaled() >= mHigh;
		} else {
			return mSprite.getY() + mSprite.getHeightScaled() >= mHigh;
		}
	}
	
	private void resetPositionToLowerBound(){
		if(mType == TOP || mType == BOTTOM){
			setPosition(mLow, getInitialY());
		} else {
			setPosition(getInitialX(), mLow);
		}
	}
	
	private void resetPositionToUpperBound(){
		if(mType == TOP || mType == BOTTOM){
			setPosition(mHigh - mSprite.getWidthScaled(), getInitialY());
		} else {
			setPosition(getInitialX(), mHigh - mSprite.getHeightScaled());
		}
	}
	
	private void setPosition(float x, float y){
		final float widthD2 = mSprite.getWidth() / 2;
		final float heightD2 = mSprite.getHeight() / 2;
		final float angle = mBody.getAngle();
		float ax = (x + widthD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		float ay = (y + heightD2) / PhysicsConstants.PIXEL_TO_METER_RATIO_DEFAULT;
		mBody.setTransform(ax, ay, angle);
	}
	
	private ITextureRegion getTexture(){
		if(mType == TOP || mType == BOTTOM){
			return RM.txPlatfromH;
		} else {
			return RM.txPlatfromV;
		}
	}
	
	private float getInitialX(){
		if(mType == TOP || mType == BOTTOM){
			return (mLow + mHigh) / 2 - RM.SX * RM.txPlatfromH.getWidth() / 2;
		}
		if(mType == LEFT){
			return mBase - RM.SX * RM.txPlatfromV.getWidth();
		}
		if(mType == RIGHT){
			return mBase;
		}
		return 0;
	}
	
	private float getInitialY(){
		if(mType == RIGHT || mType == LEFT){
			return (mLow + mHigh) / 2 - RM.SY * RM.txPlatfromV.getHeight() / 2;
		}
		if(mType == TOP){
			return mBase - RM.SY * RM.txPlatfromH.getHeight();
		}
		if(mType == BOTTOM){
			return mBase;
		}
		return 0;
	}
}
