package com.abforce.toop.entities;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;

import com.abforce.toop.interfaces.OnHitListener;
import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SoundManager;
import com.abforce.toop.scenehandlers.MainSceneHandler;

public class DuplicateBall extends SSprite {
	float x;
	float fromY;
	float toY;
	int round = 0;
	boolean init = false;
	boolean upward;
	boolean gone = false;
	
	public DuplicateBall() {
		super(-100 * RM.SX, -100 * RM.SY, RM.txDuplicate);
		if (Math.random() >= 0.5f) {
			upward = true;
		} else {
			upward = false;
		}
		registerEntityModifier(new ScaleModifier(0.5f, 0, (RM.SX + RM.SY) / 2));
		SoundManager.onCoinAppear();
	}

	public void setXRanges(float from, float to) {
		x = (float) (Math.random() * (to - from) + from);
	}

	public void setYRanges(float from, float to) {
		fromY = from;
		toY = to;
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {

		if (!init) {
			setX(x);
			setY((fromY + toY) / 2);
			init = true;
		}

		for(Ball ball : MainSceneHandler.STATE.ballPool.balls){
			if (collidesWith(ball) && !gone) {
				explode();
				generateBall(ball);
				return;
			}
		}

		if (getY() >= toY || getY() <= fromY) {
			round += 1;
			upward = !upward;
		}

		if (upward) {
			setY(getY() - pSecondsElapsed * 30);
		} else {
			setY(getY() + pSecondsElapsed * 30);
		}

		if (round == 3 && !gone) {
			hide();
			return;
		}

		super.onManagedUpdate(pSecondsElapsed);
	}

	private void generateBall(Ball ball){
		float vx = ball.mBody.getLinearVelocity().x;
		float vy = ball.mBody.getLinearVelocity().y;
		MainSceneHandler.STATE.ballPool.generateBall(vx, vy);
		SoundManager.onCoinPick();
	}
	
	private void hide() {
		gone = true;
		ScaleModifier modifier = new ScaleModifier(0.5f, 1, 0) {
			protected void onModifierFinished(org.andengine.entity.IEntity pItem) {
				RM.STATE.engine.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						detachSelf();
						dispose();
					}
				});
			};
		};
		registerEntityModifier(modifier);
	}

	private void explode() {
		gone = true;
		ParallelEntityModifier modifier = new ParallelEntityModifier(
				new ScaleModifier(0.3f, 1, 1.5f), new AlphaModifier(0.3f, 1, 0)) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				RM.STATE.engine.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						detachSelf();
						dispose();
					}
				});
			}
		};
		registerEntityModifier(modifier);
	}

	
}
