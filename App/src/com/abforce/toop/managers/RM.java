package com.abforce.toop.managers;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.graphics.Color;

public class RM {

	public static final float DESIGN_WINDOW_WIDTH = 800f;
	public static final float DESIGN_WINDOW_HEIGHT = 444f;
	
	public static final State STATE = new State();
	
	// Camera width
	public static float CW;
	// Camera height
	public static float CH;
	// X scale factor
	public static float SX;
	// Y scale factor
	public static float SY;
	public static VertexBufferObjectManager VBO;
	// ============================================
	// Splash
	// ============================================
	public static ITextureRegion txSplash;

	// ============================================
	// Game Scene
	// ============================================
	public static ITextureRegion txBall;
	public static ITextureRegion txPlatfromH;
	public static ITextureRegion txPlatfromV;
	public static ITextureRegion txBox;
	public static ITextureRegion txSky;
	public static ITextureRegion txCloud;
	public static TiledTextureRegion txBird1;
	public static ITextureRegion txCoin;
	public static ITextureRegion txDuplicate;
	public static ITextureRegion txSlow;
	public static ITextureRegion txBloodDrop;
	public static ITextureRegion txBlood;
	public static ITextureRegion txGetReady;

	public static ITextureRegion txFrame;
	public static ITextureRegion txBtnMainMenu;
	public static ITextureRegion txBtnRestart;
	public static ITextureRegion txBtnResume;
	public static ITextureRegion txGamePaused;
	public static ITextureRegion txGameOver;
	public static ITextureRegion txRecord;
	public static TiledTextureRegion txBtnSubmit;
	
	public static Font fnScore;

	// ============================================
	// Menu Scene
	// ============================================
	public static TiledTextureRegion txBtnStart;
	public static TiledTextureRegion txBtnScores;
	public static TiledTextureRegion txBtnMute;
	public static ITextureRegion txSun;
	public static ITextureRegion txGreenHill;
	public static ITextureRegion txClouds;
	public static ITextureRegion txLight;
	public static TiledTextureRegion txBird2;
	public static ITextureRegion txHighScore;
	
	public static Font fnHoma;
	
	// ============================================
	// Sounds and Musics
	// ============================================
	public static Music mMusic;
	public static Sound sBallHit;
	public static Sound sCoinPick;
	public static Sound sCoinAppear;
	public static Sound sNewRecord;
	public static Sound sBirdHit;
	public static Sound sBirdEscape;
	public static Sound sGamePause;
	public static Sound sGameResume;
	public static Sound sGameOver;
	
	public static void setup(BaseGameActivity activity, Engine engine) {
		STATE.activity = activity;
		STATE.engine = engine;
		CW = engine.getCamera().getWidth();
		CH = engine.getCamera().getHeight();
		SX = CW / DESIGN_WINDOW_WIDTH;
		SY = CH / DESIGN_WINDOW_HEIGHT;
		
		SX = (SX + SY) / 2;
		SY = SX;
		
		VBO = engine.getVertexBufferObjectManager();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("graphics/");
		MusicFactory.setAssetBasePath("sounds/");
		SoundFactory.setAssetBasePath("sounds/");
	}

	public static void loadSplashSceneResources() {
		BitmapTextureAtlas splashTextureAtlas = new BitmapTextureAtlas(
				STATE.activity.getTextureManager(), 227, 355,
				TextureOptions.BILINEAR);
		txSplash = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				splashTextureAtlas, STATE.activity, "splash.png", 0, 0);
		splashTextureAtlas.load();
	}

	public static void loadGameSceneResources() {
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
				STATE.engine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);

		txBall = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "ball.png");
		txPlatfromH = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "platform.png");
		txPlatfromV = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "platformV.png");
		txBox = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "box.jpg");
		txCloud = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "cloud.png");
		txBird1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				atlas, STATE.activity.getAssets(), "bird1.png", 3, 1);
		txCoin = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "coin.png");
		txDuplicate = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "duplicate.png");
		txSlow = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "slow.png");
		txBlood = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "blood.png");
		txBloodDrop = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "blood_drop.png");
		txGetReady = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "get_ready.png");
		txFrame = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "frame.png");
		txBtnMainMenu = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "btn_main_menu.png");
		txBtnRestart = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "btn_restart.png");
		txBtnResume = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "btn_resume.png");
		txGamePaused = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "game_paused.png");
		txGameOver = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "game_over.png");
		txRecord = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "record.png");
		txBtnSubmit = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				atlas, STATE.activity.getAssets(), "submit.png", 2, 1);
		
		try {
			atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
					0, 1, 1));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		atlas.load();

		BitmapTextureAtlas repeatingBitmap = new BitmapTextureAtlas(
				STATE.engine.getTextureManager(), 512, 512,
				TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		txSky = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				repeatingBitmap, STATE.activity.getAssets(), "sky.jpg", 0, 0);
		repeatingBitmap.load();

		fnScore = FontFactory.createFromAsset(STATE.activity.getFontManager(),
				 STATE.engine.getTextureManager(), 256, 256, STATE.activity.getAssets(),
				 "fonts/homa.ttf", 55f, true, Color.WHITE); 

		fnScore.load();
		fnScore.prepareLetters("0123456789".toCharArray());
	}

	public static void loadMenuSceneResources() {
		BuildableBitmapTextureAtlas atlas = new BuildableBitmapTextureAtlas(
				STATE.engine.getTextureManager(), 1024, 1024,
				TextureOptions.BILINEAR);

		txBtnStart = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(atlas, STATE.activity.getAssets(),
						"btn_start.png", 2, 1);
		txBtnScores = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(atlas, STATE.activity.getAssets(),
						"btn_scores.png", 2, 1);
		txBtnMute = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(atlas, STATE.activity.getAssets(),
						"btn_mute.png", 2, 1);
		txSun = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "sun.png");
		txClouds = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				atlas, STATE.activity.getAssets(), "clouds.png");
		txLight = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "light.png");
		txBird2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(
				atlas, STATE.activity.getAssets(), "bird2.png", 6, 1);
		txHighScore = BitmapTextureAtlasTextureRegionFactory.createFromAsset(atlas,
				STATE.activity.getAssets(), "high_score.png");
		
		try {
			atlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(
					0, 1, 1));
		} catch (TextureAtlasBuilderException e) {
			e.printStackTrace();
		}
		atlas.load();

		BitmapTextureAtlas bgAtlas = new BitmapTextureAtlas(
				STATE.engine.getTextureManager(), 1024, 693,
				TextureOptions.BILINEAR);
		txGreenHill = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				bgAtlas, STATE.activity.getAssets(), "bg.png", 0, 0);
		bgAtlas.load();

		fnHoma = FontFactory.createFromAsset(STATE.activity.getFontManager(),
				 STATE.engine.getTextureManager(), 256, 256, STATE.activity.getAssets(),
				 "fonts/homa.ttf", 24, true, Color.BLUE);
        fnHoma.load();
	}

	public static void loadSoundsAndMusics(){
		Context context = STATE.activity.getApplicationContext();
		try {
			mMusic = MusicFactory.createMusicFromAsset(STATE.engine.getMusicManager(), context, "music.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		mMusic.setLooping(true);
		
		try {
			sBallHit = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "ball_hit.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sCoinPick = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "coin_pick.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sCoinAppear = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "coin_appear.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sNewRecord = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "new_record.ogg");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sBirdHit = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "bird_hit.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sBirdEscape = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "bird_escape.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sGamePause = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "game_pause.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sGameResume = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "game_resume.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			sGameOver = SoundFactory.createSoundFromAsset(STATE.engine.getSoundManager(), context, "game_over.mp3");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static class State {
		public BaseGameActivity activity;
		public Engine engine;
	}

}
