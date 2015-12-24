package com.abforce.toop.scenehandlers;

import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.modifier.LoopEntityModifier;
import org.andengine.entity.modifier.MoveXModifier;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.RotationModifier;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.sprite.TiledSprite;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.modifier.ease.EaseBounceOut;
import org.andengine.util.modifier.ease.EaseQuadOut;

import android.content.Context;
import android.widget.Toast;

import com.abforce.toop.entities.Bird2;
import com.abforce.toop.entities.Clouds;
import com.abforce.toop.entities.SButtonSprite;
import com.abforce.toop.entities.SSprite;
import com.abforce.toop.managers.PreferenceHelper;
import com.abforce.toop.managers.RM;
import com.abforce.toop.managers.SM;
import com.abforce.toop.managers.SoundManager;
import com.abforce.toop.managers.SM.SceneType;
import com.abforce.toop.models.HighScorePack;
import com.abforce.toop.utils.PersianFontTextureGenerater;
import com.abforce.toop.utils.SolarCalendar;

public class MenuSceneHandler {
	public static final State STATE = new State();

	public static void create() {
		STATE.scene = new Scene();
		applyBackground();
		createMenu();
		redrawHighScore();
		SoundManager.onMenuSceneCreate();
	}

	private static void createMenu() {
		final float btnWidth = RM.txBtnStart.getWidth() * RM.SX;
		float btnHeight = RM.txBtnStart.getHeight() * RM.SY;

		// Start game
		final SButtonSprite btnStart = new SButtonSprite(RM.CW / 2 - btnWidth / 2, 2 * RM.CH / 3 - btnHeight / 2, RM.txBtnStart) {
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					SM.setCurrentScene(SceneType.MAINGAME, true);
					return true;
				}
				return false;
			};
		};

		// Scores
		float y = btnStart.getY() + 3 * btnHeight / 2;
		final SButtonSprite btnScores = new SButtonSprite(RM.CW / 2 - btnWidth / 2, y, RM.txBtnScores) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if(pSceneTouchEvent.isActionUp()){
					SM.openHighScoresPopup();
					return true;
				}
				return false;
			}
		};

		// Mute button
		float mute_x = RM.CW - RM.txBtnMute.getWidth() * RM.SX - 20 * RM.SX;
		float mute_y = RM.CH - RM.txBtnMute.getHeight() * RM.SY - 10 * RM.SY;
		TiledSprite btnMute = new TiledSprite(mute_x, mute_y, RM.txBtnMute, RM.VBO) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if (SoundManager.STATE.isMute) {
						setCurrentTileIndex(0);
						SoundManager.setMute(false);
					} else {
						setCurrentTileIndex(1);
						SoundManager.setMute(true);
					}
					return true;
				}
				return false;
			}
		};
		btnMute.setScaleCenter(0, 0);
		btnMute.setScale(RM.SX, RM.SY);
		if (SoundManager.STATE.isMute) {
			btnMute.setCurrentTileIndex(1);
		} else {
			btnMute.setCurrentTileIndex(0);
		}

		STATE.scene.registerTouchArea(btnStart);
		STATE.scene.registerTouchArea(btnScores);
		STATE.scene.registerTouchArea(btnMute);
		STATE.scene.attachChild(btnScores);
		STATE.scene.attachChild(btnStart);
		STATE.scene.attachChild(btnMute);

		btnStart.setVisible(false);
		btnScores.setVisible(false);

		STATE.scene.registerUpdateHandler(new IUpdateHandler() {
			float elapsed = 0;
			@Override
			public void onUpdate(float pSecondsElapsed) {
				elapsed += pSecondsElapsed;
				if (elapsed >= 2f) {
					STATE.scene.unregisterUpdateHandler(this);
					btnStart.setVisible(true);
					btnScores.setVisible(true);

					btnStart.registerEntityModifier(new MoveXModifier(0.8f,
							-btnWidth, btnStart.getX(), EaseBounceOut
									.getInstance()));
					btnScores.registerEntityModifier(new MoveXModifier(0.8f,
							RM.CW + btnWidth, btnScores.getX(),
							EaseBounceOut.getInstance()));
				}
			}

			@Override
			public void reset() {}

		});
	}

	private static void applyBackground() {
		float lw = RM.txLight.getWidth() * RM.SX;

		// Radial light
		SSprite spLight = new SSprite(RM.CW / 2 - lw / 2, 0, RM.txLight);
		LoopEntityModifier modifier2 = new LoopEntityModifier(
				new RotationModifier(20, 0, 359));
		spLight.registerEntityModifier(modifier2);
		STATE.scene.attachChild(spLight);

		// Sun
		SSprite spSun = new SSprite(10 * RM.SX, 10 * RM.SY, RM.txSun);
		LoopEntityModifier modifier = new LoopEntityModifier(
				new RotationModifier(10, 0, 359));
		spSun.registerEntityModifier(modifier);
		STATE.scene.attachChild(spSun);

		// Green hill
		Sprite spBg = new Sprite(0, 0, RM.CW, RM.CH, RM.txGreenHill, RM.VBO);
		STATE.scene.attachChild(spBg);

		// Background color
		STATE.scene.setBackground(new Background(0.35f, 0.76f, 0.92f));

		STATE.scene.registerUpdateHandler(new IUpdateHandler() {

			float elapsed = 10;

			@Override
			public void onUpdate(float pSecondsElapsed) {
				elapsed += pSecondsElapsed;
				if (elapsed > 8) {
					elapsed = 0;
					Clouds c = new Clouds();
					c.attachToScene(STATE.scene);

					Bird2 b = new Bird2();
					b.setYRanges(5 * RM.SY, RM.CH / 2);
					b.animate();
					STATE.scene.attachChild(b);
				}
			}

			@Override
			public void reset() {
			}

		});

		STATE.scene.registerEntityModifier(new MoveYModifier(2.5f, RM.CH, 0, EaseQuadOut.getInstance()));

		// High score label
		float textureHieght = RM.txHighScore.getHeight() * RM.SY;
		float textureWidth = RM.txHighScore.getWidth() * RM.SX;
		SSprite spHighScore = new SSprite(RM.CW - 5 * RM.SX - textureWidth, RM.CH / 2
				- textureHieght / 2 + 20 * RM.SY, RM.txHighScore);
		STATE.scene.attachChild(spHighScore);

		STATE.highScoreY = RM.CH / 2 + textureHieght / 2 + 45 * RM.SY;
	}

	public static void notifyNetworkUnavailable() {
		final Context context = RM.STATE.activity.getApplicationContext();
		RM.STATE.engine.registerUpdateHandler(new TimerHandler(3,
				new ITimerCallback() {

					@Override
					public void onTimePassed(TimerHandler pTimerHandler) {
						RM.STATE.engine.unregisterUpdateHandler(pTimerHandler);
						RM.STATE.activity.runOnUiThread(new Runnable() {

							@Override
							public void run() {
								Toast.makeText(
										context,
										"اینترنت در دسترس نیست!\nبرای نمایش بالاترین امتیاز به اینترنت نیاز است",
										Toast.LENGTH_LONG).show();
							}
						});

					}
				}));
	}

	public static void redrawHighScore() {
		final float x = RM.CW - (PersianFontTextureGenerater.WIDTH - 10) * RM.SX;

		if (STATE.highScore != null) {
			RM.STATE.engine.runOnUpdateThread(new Runnable() {

				@Override
				public void run() {
					STATE.highScore.detachSelf();
					STATE.highScore.dispose();
					STATE.highScore = new SSprite(x, STATE.highScoreY, getLatestHighScoreTexture());
					STATE.scene.attachChild(STATE.highScore);
				}
			});
		} else {
			STATE.highScore = new SSprite(x, STATE.highScoreY, getLatestHighScoreTexture());
			STATE.scene.attachChild(STATE.highScore);
		}
	}

	public static ITextureRegion getLatestHighScoreTexture() {
		HighScorePack pack = PreferenceHelper.getGlobalHighScorePack();
		String text = null;
		if (pack.isValid) {
			String name = pack.name;
			String score = String.valueOf(pack.score);
			String time = SolarCalendar.getShamsiDate(pack.millis);
			text = score + "\n" + name + "\n" + time;
		} else {
			text = "اطلاعاتی موجود نیست!";
		}
		return PersianFontTextureGenerater.getTextureFor(text);
	}

	public static class State {
		public Scene scene;
		public float highScoreY;
		public SSprite highScore;
	}

}
