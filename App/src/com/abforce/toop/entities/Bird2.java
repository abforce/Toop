package com.abforce.toop.entities;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.input.touch.TouchEvent;

import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SoundManager;

public class Bird2 extends AnimatedSprite {

	private float y;
	private float pxPs;
	boolean init = false;
	float width;
	float height;
	
	public Bird2() {
		super(0, 0, RM.txBird2, RM.VBO);
		width = RM.txBird2.getWidth() * RM.SX;
		height = RM.txBird2.getHeight() * RM.SY;
		float scale = (float) (Math.random() * 0.5f + 0.5f);
		setScaleCenter(0, 0);
		setScale(scale * ((RM.SX + RM.SY) / 2));
		RM.STATE.engine.getScene().registerTouchArea(this);
		this.pxPs = 60 * RM.SX;
	}
	
	public void setYRanges(float from, float to) {
		to -= height;
		y = (float) (Math.random() * (to - from) + from);
	}

	public void animate() {
		this.animate(80, true);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		if (!init) {
			setX(RM.CW);
			setY(y);
			init = true;
		}
		
		setX(getX() - pxPs * pSecondsElapsed);
		if (getX() <= - width) {
			RM.STATE.engine.runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					detachSelf();
				}
			});
			return;
		}

		super.onManagedUpdate(pSecondsElapsed);
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionDown()) {
			pxPs *= 5;
			stopAnimation();
			this.animate(40, true);
			SoundManager.onBirdEscapse();
			return true;
		}
		return false;
	}
}
