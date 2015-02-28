/**
 * 
 */
package com.adam.qledge.activities;

import java.io.IOException;

import org.andengine.audio.music.Music;
import org.andengine.audio.music.MusicFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.util.GLState;
import org.andengine.ui.activity.BaseGameActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.adam.qledge.constants.Constants;


/**
 * @author Arindam
 *
 */

public class StageGameEndSplashActivity extends BaseGameActivity {

	private static final String TAG = "StageGameEndSplashActivity";
	private Scene splashScene;
	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	private Sprite splash;
	private Music mCompleteSound;

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
		engineOptions.getAudioOptions().setNeedsMusic(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		MusicFactory.setAssetBasePath("mfx/");
		this.splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 800, 500, TextureOptions.DEFAULT);
		this.splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, this, "game_completed.png", 0, 0);
		if (null != this.splashTextureAtlas) {
			this.splashTextureAtlas.load();
		}
		
		try {
			this.mCompleteSound = MusicFactory.createMusicFromAsset(this.mEngine.getMusicManager(), this, "highscore.mp3");
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
		
		final SharedPreferences sharedPreferences = StageGameEndSplashActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
		final int volume = sharedPreferences.getInt("VOLUME",10);
		final boolean isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
		if (isVolumeOn) {
			this.mCompleteSound.setVolume(volume * 0.1f);
		} else {
			this.mCompleteSound.setVolume(0 * 0.1f);
		}

		pOnCreateResourcesCallback.onCreateResourcesFinished();

	}

	@Override
	public void onCreateScene(OnCreateSceneCallback pOnCreateSceneCallback) throws Exception {
		initSplashScene();
		pOnCreateSceneCallback.onCreateSceneFinished(this.splashScene);
	}

	@Override
	public void onPopulateScene(Scene pScene, OnPopulateSceneCallback pOnPopulateSceneCallback) throws Exception {
		if (null != this.mCompleteSound) {
			this.mCompleteSound.play();
		}
		this.mEngine.registerUpdateHandler(new TimerHandler(3f, new ITimerCallback() {
			public void onTimePassed(final TimerHandler pTimerHandler) {
				mEngine.unregisterUpdateHandler(pTimerHandler);
				loadMenu();
				if(null != splash) {
					splash.detachSelf();
				}
			}
		}));

		pOnPopulateSceneCallback.onPopulateSceneFinished();

	}

	private void loadMenu() {
		startActivity(new Intent(this, StageSelectLevelActivity.class));
		finish();
	}

	private void initSplashScene() {
		this.splashScene = new Scene();
		this.splash = new Sprite(0, 0, this.splashTextureRegion, this.mEngine.getVertexBufferObjectManager()) {
			@Override
			protected void preDraw(GLState pGLState, Camera pCamera) {
				super.preDraw(pGLState, pCamera);
				pGLState.enableDither();
			}
		};
		
		if(null != this.splash) {
			this.splash.setPosition((Constants.CAMERA_WIDTH - this.splash.getWidth()) * 0.5f, (Constants.CAMERA_HEIGHT - this.splash.getHeight()) * 0.5f);
		}
		this.splashScene.attachChild(this.splash);
	}
	
	@Override
	public synchronized void onResumeGame() {
	    if (null != this.mEngine)
	        super.onResumeGame();
	}
	
	@Override
	protected void onPause() {
	super.onPause();
		if (null != this.mCompleteSound) {
			this.mCompleteSound.pause();
		}
	}
}
