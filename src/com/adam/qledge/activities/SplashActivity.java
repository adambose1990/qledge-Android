/**
 * 
 */
package com.adam.qledge.activities;

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

import android.content.Intent;

import com.adam.qledge.constants.Constants;


/**
 * @author Arindam
 *
 */

public class SplashActivity extends BaseGameActivity {

	@SuppressWarnings("unused")
	private static final String TAG = "SplashActivity";
	private Scene splashScene;
	private BitmapTextureAtlas splashTextureAtlas;
	private ITextureRegion splashTextureRegion;
	private Sprite splash;

	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
		return engineOptions;
	}

	@Override
	public void onCreateResources(OnCreateResourcesCallback pOnCreateResourcesCallback) throws Exception {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.splashTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 800, 500, TextureOptions.DEFAULT);
		this.splashTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, this, "splashscreen.png", 0, 0);
		if (null != this.splashTextureAtlas) {
			this.splashTextureAtlas.load();
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
		this.mEngine.registerUpdateHandler(new TimerHandler(2f, new ITimerCallback() {
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
		startActivity(new Intent(this, MenuActivity.class));
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
}
