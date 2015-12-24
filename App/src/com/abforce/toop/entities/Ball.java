package com.abforce.toop.entities;

import org.andengine.entity.IEntity;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;

import com.abforce.toop.managers.RM;
import com.abforce.toop.scenehandlers.MainSceneHandler;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class Ball extends SSprite {

	final FixtureDef BALL_FIX = PhysicsFactory.createFixtureDef(10, 0.7f, 0.3f);

	public Body mBody;
	PhysicsConnector mConnector;
	boolean exploded = false;

	float bTop;
	float bLeft;
	float bRight;
	float bBottom;
	
	public Ball(float vx, float vy) {
		super(RM.CW / 2, RM.CH / 2, RM.txBall);
		mBody = PhysicsFactory.createCircleBody(MainSceneHandler.STATE.physicsWorld, this,
				BodyType.DynamicBody, BALL_FIX);
		mConnector = new PhysicsConnector(this, mBody, true, true);
		MainSceneHandler.STATE.physicsWorld.registerPhysicsConnector(mConnector);
		mBody.setLinearDamping(0);
		mBody.setLinearVelocity(vx, vy);
		
		bTop = 0;
		bLeft = 0;
		bRight = RM.CW;
		bBottom = RM.CH;
	}

	public void slowDown(){
		elapsed /= 5;
	}
	
	float elapsed = 0;
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		elapsed += pSecondsElapsed;
		
		Vector2 vel = mBody.getLinearVelocity();
		if (vel.x > 0) {
			vel.x += elapsed * 0.001;
		} else {
			vel.x -= elapsed * 0.001;
		}
		if (vel.y > 0) {
			vel.y += elapsed * 0.001;
		} else {
			vel.y -= elapsed * 0.001;
		}
		mBody.setLinearVelocity(vel);

		if (!exploded) {
			if (getY() <= bTop) {
				explode(DIR_TOP);
			}
			if (getY() + getHeight() >= bBottom) {
				explode(DIR_BOTTOM);
			}
			if (getX() <= bLeft) {
				explode(DIR_LEFT);
			}
			if (getX() + getWidth() >= bRight) {
				explode(DIR_RIGHT);
			}
		}

		super.onManagedUpdate(pSecondsElapsed);
	}

	private void explode(int direction) {
		exploded = true;
		MainSceneHandler.STATE.physicsWorld.unregisterPhysicsConnector(mConnector);
		MainSceneHandler.STATE.physicsWorld.destroyBody(mBody);
		ParallelEntityModifier modifier = new ParallelEntityModifier(
				new ScaleModifier(0.4f, 1, 1.5f), new AlphaModifier(0.4f, 1, 0)) {
			@Override
			protected void onModifierFinished(IEntity pItem) {
				RM.STATE.engine.runOnUpdateThread(new Runnable() {

					@Override
					public void run() {
						detachSelf();
						dispose();
						mListener.onLeft();
					}
				});
				super.onModifierFinished(pItem);
			}
		};
		registerEntityModifier(modifier);
		//firePaticles();
	}
	
	OnBallLeaveListener mListener;

	public void setOnBallLeaveListener(OnBallLeaveListener listener) {
		mListener = listener;
	}

	public static final int DIR_TOP = 0;
	public static final int DIR_LEFT = 1;
	public static final int DIR_RIGHT = 2;
	public static final int DIR_BOTTOM = 3;

	public interface OnBallLeaveListener {
		public void onLeft();
	}
}
