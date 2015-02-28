/**
 * 
 */
package com.adam.qledge.activities;

import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.opengl.view.RenderSurfaceView;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.adam.qledge.R;
import com.adam.qledge.constants.Constants;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * @author Arindam
 *
 */
public class QuickSelectCategoryActivity extends SimpleBaseGameActivity {

	private static final String TAG = "QuickSelectCategoryActivity";
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;
	
	private ITexture ravieFontTexture;
	private Font mRavieFont;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mMenuApti;
	private TiledTextureRegion mMenuLogic;
	private TiledTextureRegion mMenuVerbal;
	
	private Sound mClickSound;
	private AdView adView;
	
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	@Override
	protected void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");
		SoundFactory.setAssetBasePath("mfx/");
		onCreateResourceBackground();
		
		this.ravieFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFont = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTexture, this.getAssets(), "ravie.ttf", Constants.MEDIUMFONTSIZE, true, Color.rgb(0, 65, 105));
		if (null != this.mRavieFont) {
			this.mRavieFont.load();
		}
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 312, 310, TextureOptions.BILINEAR);
		this.mMenuApti = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_cat_a.png", 0, 0, 1, 1);
		this.mMenuLogic = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_cat_lr.png", 0, 80, 1, 1);
		this.mMenuVerbal = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_cat_va.png", 0, 195, 1, 1);
		if (null != this.mBitmapTextureAtlas) {
			this.mBitmapTextureAtlas.load();
		}
		
		try {
			this.mClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tick.mp3");
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
		final SharedPreferences sharedPreferences = QuickSelectCategoryActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
		final int volume = sharedPreferences.getInt("VOLUME",10);
		final boolean isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
		if (null != this.mClickSound) {
			if (isVolumeOn) {
				this.mClickSound.setVolume(volume * 0.1f);
			} else {
				this.mClickSound.setVolume(0 * 0.1f);
			}
		}
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.scene = new Scene();
		this.vertexBufferObjectManager = this.getVertexBufferObjectManager();
		onCreateSceneBackground();
		
		final Text tHeading = new Text(0, 0, this.mRavieFont, "Select Category", new TextOptions(HorizontalAlign.CENTER), vertexBufferObjectManager);
		tHeading.setPosition((Constants.CAMERA_WIDTH - tHeading.getWidth()) / 2, 40);
		this.scene.attachChild(tHeading);
		
		final Sprite sMenuApti = new Sprite((Constants.CAMERA_WIDTH - this.mMenuApti.getWidth()) /2 , 140, 
				this.mMenuApti, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					startNextActivity(0);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Sprite sMenuLogic = new Sprite(100, 240, this.mMenuLogic, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					startNextActivity(1);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Sprite sMenuVerbal = new Sprite(100, 240, this.mMenuVerbal, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					startNextActivity(2);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		sMenuVerbal.setPosition(Constants.CAMERA_WIDTH - sMenuVerbal.getWidth() - 100, 240);
		
		sMenuApti.setScale(Constants.SCALEFACTOR);
		sMenuLogic.setScale(Constants.SCALEFACTOR);
		sMenuVerbal.setScale(Constants.SCALEFACTOR);
		
		this.scene.attachChild(sMenuApti);
		this.scene.attachChild(sMenuLogic);
		this.scene.attachChild(sMenuVerbal);
		
		this.scene.registerTouchArea(sMenuApti);
		this.scene.registerTouchArea(sMenuLogic);
		this.scene.registerTouchArea(sMenuVerbal);
		
		return this.scene;
	}

	protected void startNextActivity(int choiceCategory) {
		if (null != this.mClickSound) {
			this.mClickSound.play();
		}
		Intent intent = new Intent(QuickSelectCategoryActivity.this, QuickSelectSubCategoryActivity.class);
		intent.putExtra("CATEGORY", choiceCategory);
		startActivity(intent);
		finish();
		this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
		
	}

	private void onCreateResourceBackground() {
		this.mAutoParallaxBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024);
		this.mParallaxLayerFront = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this,"parallax_layer_front.png", 0, 0);
		this.mParallaxLayerBack = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this,"parallax_layer_back.png", 0, 188);
		this.mParallaxLayerMid = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mAutoParallaxBackgroundTexture, this,"parallax_layer_mid.png", 0, 669);
		if (null != this.mAutoParallaxBackgroundTexture) {
			this.mAutoParallaxBackgroundTexture.load();
		}
	}
	
	private void onCreateSceneBackground() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		final AutoParallaxBackground autoParallaxBackground = new AutoParallaxBackground(0, 0, 0, 5);
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-1.0f, new Sprite(0, Constants.CAMERA_HEIGHT - this.mParallaxLayerBack.getHeight(), this.mParallaxLayerBack, this.vertexBufferObjectManager)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-5.0f, new Sprite(0, Constants.CAMERA_HEIGHT - this.mParallaxLayerMid.getHeight(), this.mParallaxLayerMid, this.vertexBufferObjectManager)));
		autoParallaxBackground.attachParallaxEntity(new ParallaxEntity(-10.0f, new Sprite(0, Constants.CAMERA_HEIGHT - this.mParallaxLayerFront.getHeight(), this.mParallaxLayerFront, this.vertexBufferObjectManager)));
		this.scene.setBackground(autoParallaxBackground);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (null != this.mClickSound) {
				this.mClickSound.play();
			}
			startActivity(new Intent(QuickSelectCategoryActivity.this, MenuActivity.class));
			finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	protected void onSetContentView() {
        final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.FILL);
        final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        this.adView = new AdView(this);
        this.adView.setAdUnitId(getString(R.string.bottom_banner_quickselectcategoryactivity));
        this.adView.setAdSize(AdSize.BANNER);
        this.adView.setVisibility(AdView.VISIBLE);
        this.adView.refreshDrawableState();

        final AdRequest request = new AdRequest.Builder().build();
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				adView.loadAd(request);
			}
		});

        this.mRenderSurfaceView = new RenderSurfaceView(this);
        this.mRenderSurfaceView.setRenderer(this.mEngine, this);

        final FrameLayout.LayoutParams surfaceViewLayoutParams = new FrameLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        surfaceViewLayoutParams.gravity = Gravity.CENTER ;

        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        frameLayout.addView(this.adView, adViewLayoutParams);
        this.setContentView(frameLayout, frameLayoutLayoutParams);
	}
	
	@Override
	public synchronized void onResumeGame() {
	    if (null != this.mEngine)
	        super.onResumeGame();
	}
}
