package com.abforce.toop.entities;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.particle.BatchedSpriteParticleSystem;
import org.andengine.entity.particle.emitter.RectangleParticleEmitter;
import org.andengine.entity.particle.initializer.AccelerationParticleInitializer;
import org.andengine.entity.particle.initializer.ScaleParticleInitializer;
import org.andengine.entity.particle.modifier.AlphaParticleModifier;
import org.andengine.entity.particle.modifier.ExpireParticleInitializer;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.entity.sprite.UncoloredSprite;
import org.andengine.input.touch.TouchEvent;

import com.abforce.toop.interfaces.OnHitListener;
import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SoundManager;
import com.abforce.toop.scenehandlers.MainSceneHandler;

public class Bird extends AnimatedSprite {

	private float fromX;
	private float toX;
	private float y;
	private float pxPs;
	boolean init = false;
	float width;
	float height;
	boolean dead = false;
	boolean escaping = false;

	OnHitListener mListener;
	
	public Bird(OnHitListener listener) {
		super(0, 0, RM.txBird1, RM.VBO);
		setScaleCenter(0, 0);
		
		mListener = listener;

		width = RM.txBird1.getWidth() * RM.SX;
		height = RM.txBird1.getHeight() * RM.SY;
		float scale = (float) (Math.random() * 0.5f + 0.7f);
		setScale(scale * ((RM.SX + RM.SY) / 2));
		RM.STATE.engine.getScene().registerTouchArea(this);
		
		this.fromX = 0;
		this.toX = RM.STATE.engine.getCamera().getWidth();
		this.pxPs = 80 * RM.SX;
		
		this.animate(80, true);
	}

	public void setYRanges(float from, float to) {
		to -= height;
		y = (float) (Math.random() * (to - from) + from);
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		if (!init) {
			setX(fromX - width);
			setY(y);
			init = true;
		}

		for(Ball ball : MainSceneHandler.STATE.ballPool.balls){
			if (collidesWith(ball) && !dead && !escaping) {
				dead = true;
				impulse();
			}
		}

		if (!dead) {
			setX(getX() + pxPs * pSecondsElapsed);
			if (getX() >= toX) {
				RM.STATE.engine.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						detachSelf();
					}
				});
				return;
			}
		} else {
			setY(getY() + 5 * pxPs * pSecondsElapsed);
			if (getY() >= RM.STATE.engine.getCamera().getHeight()) {
				RM.STATE.engine.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						detachSelf();
					}
				});
				return;
			}
		}

		super.onManagedUpdate(pSecondsElapsed);
	}

	private void impulse() {
		stopAnimation();
		setRotation(-90);
		SSprite blood = new SSprite(getX(), getY(), RM.txBlood);
		RM.STATE.engine.getScene().attachChild(blood);
		bloodifyScreen();
		mListener.onHit();
	}

	private void bloodifyScreen() {
		float camWidth = RM.STATE.engine.getCamera().getWidth();
		float camHeight = RM.STATE.engine.getCamera().getHeight();

		float minRate = 50;
		float maxRate = 150;
		int count = 100;

		RectangleParticleEmitter emitter = new RectangleParticleEmitter(
				camWidth / 2, camHeight / 2, camWidth, camHeight);

		final BatchedSpriteParticleSystem particleSystem = new BatchedSpriteParticleSystem(
				emitter, minRate, maxRate, count, RM.txBloodDrop, RM.VBO);

		particleSystem.addParticleInitializer(new AccelerationParticleInitializer<UncoloredSprite>(0, 0f, 50f, 100f));
		particleSystem.addParticleInitializer(new ExpireParticleInitializer<UncoloredSprite>(3));
		float fac = (RM.SX + RM.SY) / 2;
		particleSystem.addParticleInitializer(new ScaleParticleInitializer<UncoloredSprite>(0.3f * fac, 1f * fac));
		particleSystem.addParticleModifier(new AlphaParticleModifier<UncoloredSprite>(0f, 3f, 1f, 0f));

		final Scene scene = RM.STATE.engine.getScene();
		scene.attachChild(particleSystem);
		scene.registerUpdateHandler(new IUpdateHandler(){

			float elapsed = 0;
			boolean spawingDisabled = false;
			
			@Override
			public void onUpdate(float pSecondsElapsed) {
				elapsed += pSecondsElapsed;
				if(elapsed >= 0.8f && !spawingDisabled){
					particleSystem.setParticlesSpawnEnabled(false);
					spawingDisabled = true;
				}
				if(elapsed >= 4){
					scene.unregisterUpdateHandler(this);
					RM.STATE.engine.runOnUpdateThread(new Runnable() {
						
						@Override
						public void run() {
							particleSystem.detachSelf();
							particleSystem.dispose();
						}
					});
				}
			}

			@Override
			public void reset() {}
			
		});
		
	}

	@Override
	public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
			float pTouchAreaLocalX, float pTouchAreaLocalY) {

		if (pSceneTouchEvent.isActionDown()) {
			escaping = true;
			pxPs *= 5;
			stopAnimation();
			this.animate(40, true);
			MainSceneHandler.STATE.score.addScore(5);
			SoundManager.onBirdEscapse();
			return true;
		}
		return false;
	}

}
