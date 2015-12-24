package com.abforce.toop.scenehandlers;

import java.util.ArrayList;
import java.util.List;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.shape.IAreaShape;
import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.extension.physics.box2d.util.Vector2Pool;
import org.andengine.input.sensor.acceleration.AccelerationData;
import org.andengine.input.sensor.acceleration.IAccelerationListener;

import com.abforce.toop.entities.Ball;
import com.abforce.toop.entities.BallPool;
import com.abforce.toop.entities.Bird;
import com.abforce.toop.entities.Coin;
import com.abforce.toop.entities.DuplicateBall;
import com.abforce.toop.entities.Platform;
import com.abforce.toop.entities.SSprite;
import com.abforce.toop.entities.ScoreText;
import com.abforce.toop.entities.SlowDown;
import com.abforce.toop.entities.Ball.OnBallLeaveListener;
import com.abforce.toop.interfaces.OnHitListener;
import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SoundManager;
import com.abforce.toop.popups.GameOverWindow;
import com.abforce.toop.popups.GetReady;
import com.abforce.toop.popups.PauseWindow;
import com.abforce.toop.popups.GetReady.OnDisposeListener;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;

public class MainSceneHandler {
	public static final State STATE = new State();
	private static final FixtureDef WALL_FIX = PhysicsFactory.createFixtureDef(
			0.0f, 0.0f, 0.4f);

	static IAccelerationListener accelerationListener = new IAccelerationListener() {

		@Override
		public void onAccelerationChanged(AccelerationData pAccelerationData) {
			if (!STATE.sceneReady) {
				return;
			}
			float vx = pAccelerationData.getX() * 13;
			float vy = pAccelerationData.getY() * 13;

			if(!STATE.baseSet){
				STATE.baseSet = true;
				STATE.platformT.setBaseVelocity(vx);
				STATE.platformL.setBaseVelocity(vy);
				STATE.platformB.setBaseVelocity(vx);
				STATE.platformR.setBaseVelocity(vy);
			}
			
			STATE.platformT.setLinearVelocity(vx);
			STATE.platformL.setLinearVelocity(vy);
			STATE.platformB.setLinearVelocity(vx);
			STATE.platformR.setLinearVelocity(vy);
		}

		@Override
		public void onAccelerationAccuracyChanged(
				AccelerationData pAccelerationData) {
		}
	};

	public static void create() {
		STATE.sceneReady = false;
		STATE.gameStarted = false;
		STATE.gamePaused = false;
		STATE.baseSet = false;
		STATE.ballPool = new BallPool();
		STATE.scene = new Scene();
		STATE.physicsWorld = new PhysicsWorld(new Vector2(0, 0), false);
		STATE.physicsWorld.setContactListener(new ContactListener() {

			@Override
			public void preSolve(Contact contact, Manifold oldManifold) {
			}

			@Override
			public void postSolve(Contact contact, ContactImpulse impulse) {
			}

			@Override
			public void endContact(Contact contact) {
			}

			@Override
			public void beginContact(Contact contact) {
				if (isBallHit(contact)) {
					STATE.score.addScore(10);
					SoundManager.onBallHit();
				}
			}
		});
		STATE.scene.registerUpdateHandler(STATE.physicsWorld);
		applyBackground();
		STATE.scene.registerUpdateHandler(new IUpdateHandler() {
			float elapsed = 0;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				elapsed += pSecondsElapsed;
				if (elapsed >= 12) {
					elapsed = 0;
					double rand = Math.random(); 
					if(rand >= 0.5){
						Coin coin = new Coin(new OnHitListener() {
	
							@Override
							public void onHit() {
								STATE.score.addScore(20);
								SoundManager.onCoinPick();
							}
						});
						coin.setXRanges(STATE.boxTL.getWidthScaled() + 50 * RM.SX,
								STATE.boxTR.getX() - 50 * RM.SX);
						coin.setYRanges(STATE.boxTL.getHeightScaled() + 50 * RM.SY,
								STATE.boxBL.getY() - 50 * RM.SY);
						STATE.scene.attachChild(coin);
					} else if (rand >= 0.25) {
						DuplicateBall duplicateBall = new DuplicateBall();
						duplicateBall.setXRanges(STATE.boxTL.getWidthScaled() + 50 * RM.SX,
								STATE.boxTR.getX() - 50 * RM.SX);
						duplicateBall.setYRanges(STATE.boxTL.getHeightScaled() + 50 * RM.SY,
								STATE.boxBL.getY() - 50 * RM.SY);
						STATE.scene.attachChild(duplicateBall);
					} else {
						SlowDown slowDown = new SlowDown(new OnHitListener() {
							
							@Override
							public void onHit() {
								STATE.ballPool.slowDown();
								SoundManager.onCoinPick();
							}
						});
						slowDown.setXRanges(STATE.boxTL.getWidthScaled() + 50 * RM.SX,
								STATE.boxTR.getX() - 50 * RM.SX);
						slowDown.setYRanges(STATE.boxTL.getHeightScaled() + 50 * RM.SY,
								STATE.boxBL.getY() - 50 * RM.SY);
						STATE.scene.attachChild(slowDown);
					}
				}
			}

			@Override
			public void reset() {
			}
		});
		STATE.scene.registerUpdateHandler(new IUpdateHandler() {
			float elapsed = 0;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				elapsed += pSecondsElapsed;
				if (elapsed >= 1) {
					elapsed = 0;
					STATE.score.addScore(1);
				}
			}

			@Override
			public void reset() {
			}
		});
		STATE.score = new ScoreText();
		STATE.score.attachToScene(STATE.scene);

		GetReady getReady = new GetReady(new OnDisposeListener() {

			@Override
			public void onDisposed() {
				STATE.ballPool.setVisible(true);
				STATE.gameStarted = true;
			}
		});
		getReady.attachToScene(STATE.scene);

		// Ball
		STATE.ballPool.generateBall();

		// Boxes
		STATE.boxTL = new SSprite(0, 0, RM.txBox);
		STATE.boxTR = new SSprite(RM.CW - RM.txBox.getWidth() * RM.SX, 0,
				RM.txBox);
		STATE.boxBL = new SSprite(0, RM.CH - RM.txBox.getHeight() * RM.SY,
				RM.txBox);
		STATE.boxBR = new SSprite(RM.CW - RM.txBox.getWidth() * RM.SX, RM.CH
				- RM.txBox.getHeight() * RM.SY, RM.txBox);

		PhysicsFactory.createBoxBody(STATE.physicsWorld, STATE.boxTL,
				BodyType.StaticBody, WALL_FIX);
		PhysicsFactory.createBoxBody(STATE.physicsWorld, STATE.boxTR,
				BodyType.StaticBody, WALL_FIX);
		PhysicsFactory.createBoxBody(STATE.physicsWorld, STATE.boxBL,
				BodyType.StaticBody, WALL_FIX);
		PhysicsFactory.createBoxBody(STATE.physicsWorld, STATE.boxBR,
				BodyType.StaticBody, WALL_FIX);

		STATE.scene.attachChild(STATE.boxTL);
		STATE.scene.attachChild(STATE.boxTR);
		STATE.scene.attachChild(STATE.boxBL);
		STATE.scene.attachChild(STATE.boxBR);

		STATE.platformT = new Platform(Platform.TOP, STATE.boxTL.getHeightScaled(), STATE.boxTL.getWidthScaled(), STATE.boxTR.getX());
		STATE.platformL = new Platform(Platform.LEFT, STATE.boxTL.getWidthScaled(), STATE.boxTL.getHeightScaled(), STATE.boxBL.getY());
		STATE.platformB = new Platform(Platform.BOTTOM, STATE.boxBL.getY(), STATE.boxBL.getWidthScaled(), STATE.boxBR.getX());
		STATE.platformR = new Platform(Platform.RIGHT, STATE.boxTR.getX(), STATE.boxTR.getHeightScaled(), STATE.boxBR.getY());
		
		STATE.platformT.attachToScene(STATE.physicsWorld, STATE.scene);
		STATE.platformL.attachToScene(STATE.physicsWorld, STATE.scene);
		STATE.platformB.attachToScene(STATE.physicsWorld, STATE.scene);
		STATE.platformR.attachToScene(STATE.physicsWorld, STATE.scene);
		
		STATE.ballPool.setVisible(false);
		STATE.sceneReady = true;
		RM.STATE.engine.enableAccelerationSensor(RM.STATE.activity,
				accelerationListener);
	}

	private static boolean isBallHit(Contact contact) {
		Body a = contact.getFixtureA().getBody();
		Body b = contact.getFixtureB().getBody();

		for(Ball ball : STATE.ballPool.balls){
			if (a.equals(ball.mBody)) {
				if (b.equals(STATE.platformT.mBody) || b.equals(STATE.platformL.mBody)
						|| b.equals(STATE.platformB.mBody)
						|| b.equals(STATE.platformR.mBody)) {
					return true;
				}
			}
	
			if (b.equals(ball.mBody)) {
				if (a.equals(STATE.platformT.mBody) || a.equals(STATE.platformL.mBody)
						|| a.equals(STATE.platformB.mBody)
						|| a.equals(STATE.platformR.mBody)) {
					return true;
				}
			}
		}

		return false;
	}

	private static void applyBackground() {
		IAreaShape cloudbox1 = new Rectangle(0, RM.CH / 2, RM.SX
				* RM.txCloud.getWidth() * 3, RM.SY * RM.txCloud.getHeight(),
				RM.VBO);
		cloudbox1.setAlpha(0);
		cloudbox1.attachChild(new SSprite(0, 0, RM.txCloud));

		IAreaShape cloudbox2 = new Rectangle(0, RM.CH / 4, RM.SX
				* RM.txCloud.getWidth() * 3,
				RM.SY * RM.txCloud.getHeight() / 2, RM.VBO);
		cloudbox2.setAlpha(0);
		Sprite cloud2 = new SSprite(0, 0, RM.txCloud);
		cloud2.setScale(0.5f);
		cloudbox2.attachChild(cloud2);

		AutoParallaxBackground auto = new AutoParallaxBackground(0, 0, 0, 5);
		auto.attachParallaxEntity(new ParallaxEntity(0, new Sprite(0, 0, RM.CW,
				RM.CH, RM.txSky, RM.VBO)));
		auto.attachParallaxEntity(new ParallaxEntity(-3.0f, cloudbox1));
		auto.attachParallaxEntity(new ParallaxEntity(2.3f, cloudbox2));
		STATE.scene.setBackground(auto);

		// Birds
		STATE.scene.registerUpdateHandler(new IUpdateHandler() {

			float elapsed = 0;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				elapsed += pSecondsElapsed;
				if (elapsed > 15) {

					Bird b = new Bird(new OnHitListener() {

						@Override
						public void onHit() {
							STATE.score.addScore(-10);
							SoundManager.onBirdHit();
						}
					});
					b.setYRanges(
							STATE.boxTL.getY() + STATE.boxTL.getHeightScaled(),
							STATE.boxBL.getY());
					STATE.scene.attachChild(b);
					elapsed = 0;
				}
			}

			@Override
			public void reset() {
			}

		});
	}

	public static void doPauseGame() {
		STATE.gamePaused = true;
		STATE.pauseWindow = new PauseWindow();
		STATE.pauseWindow.attachToScene(STATE.scene);
		SoundManager.onGamePause();
	}

	public static void doResumeGame() {
		STATE.gamePaused = false;
		STATE.pauseWindow.dismiss();
		SoundManager.onGameResume();
	}

	public static synchronized void onResumeGame() {
		RM.STATE.engine.enableAccelerationSensor(RM.STATE.activity,
				accelerationListener);
	}

	public static synchronized void onPauseGame() {
		RM.STATE.engine.disableAccelerationSensor(RM.STATE.activity);
		doPauseGame();
	}

	public static class State {
		public Scene scene;
		public PhysicsWorld physicsWorld;
		public ScoreText score;
		public boolean gamePaused;
		public PauseWindow pauseWindow;
		public BallPool ballPool;

		public SSprite boxTL;
		public SSprite boxTR;
		public SSprite boxBL;
		public SSprite boxBR;

		public Platform platformT;
		public Platform platformL;
		public Platform platformB;
		public Platform platformR;
		
		public boolean sceneReady = false;
		public boolean gameStarted;
		public boolean baseSet = false;
	}

}