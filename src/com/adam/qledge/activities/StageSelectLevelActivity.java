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
import org.andengine.entity.primitive.Line;
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
public class StageSelectLevelActivity extends SimpleBaseGameActivity {
	
	private static final String TAG = "StageSelectLevelActivity";
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;
	
	private BitmapTextureAtlas mBackgroundTexture;
	private ITextureRegion mBackground;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mLevel1;
	private TiledTextureRegion mLevel2;
	private TiledTextureRegion mLevel3;
	private TiledTextureRegion mLevel4;
	private TiledTextureRegion mLevel5;
	private TiledTextureRegion mLevel6;
	private TiledTextureRegion mLevel7;
	private TiledTextureRegion mLevel8;
	private TiledTextureRegion mLevel9;
	private TiledTextureRegion mLevel10;
	
	private BitmapTextureAtlas mBitmapTextureAtlasLevel1;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel2;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel3;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel4;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel5;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel6;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel7;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel8;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel9;
	private BitmapTextureAtlas mBitmapTextureAtlasLevel10;
	
	private TiledTextureRegion mStar1;
	private TiledTextureRegion mStar2;
	private TiledTextureRegion mStar3;
	private TiledTextureRegion mStar4;
	private TiledTextureRegion mStar5;
	private TiledTextureRegion mStar6;
	private TiledTextureRegion mStar7;
	private TiledTextureRegion mStar8;
	private TiledTextureRegion mStar9;
	private TiledTextureRegion mStar10;
	private TiledTextureRegion mStar11;
	private TiledTextureRegion mStar12;
	private TiledTextureRegion mStar13;
	private TiledTextureRegion mStar14;
	private TiledTextureRegion mStar15;
	private TiledTextureRegion mStar16;
	private TiledTextureRegion mStar17;
	private TiledTextureRegion mStar18;
	private TiledTextureRegion mStar19;
	private TiledTextureRegion mStar20;
	private TiledTextureRegion mStar21;
	private TiledTextureRegion mStar22;
	private TiledTextureRegion mStar23;
	private TiledTextureRegion mStar24;
	private TiledTextureRegion mStar25;
	private TiledTextureRegion mStar26;
	private TiledTextureRegion mStar27;
	private TiledTextureRegion mStar28;
	private TiledTextureRegion mStar29;
	private TiledTextureRegion mStar30;
	
	private ITexture ravieFontTextureHeading;
	private ITexture ravieFontTexture;
	private Font mRavieFontHeading;
	private Font mRavieFont;
	
	private AdView adView;
	private Sound mClickSound;
	
	private int currentlLevel;
	private int currentlStage;
	
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

		this.mBackgroundTexture = new BitmapTextureAtlas(this.getTextureManager(), 600, 360);
		this.mBackground = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBackgroundTexture, this,"map_background.png", 0, 0);
		if (null != this.mBackgroundTexture) {
			this.mBackgroundTexture.load();
		}
		
		this.ravieFontTextureHeading = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFontHeading = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureHeading, this.getAssets(), "ravie.ttf", Constants.MEDIUMFONTSIZE, true, Color.rgb(0, 65, 105));
		if (null != this.mRavieFontHeading) {
			this.mRavieFontHeading.load();
		}
		
		this.ravieFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFont = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTexture, this.getAssets(), "ravie.ttf", Constants.VERYSMALLFONTSIZE, true, Color.rgb(57, 41, 17));
		if (null != this.mRavieFont) {
			this.mRavieFont.load();
		}
		
		final SharedPreferences sharedPreferences = this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
		this.currentlLevel = sharedPreferences.getInt("CURRENTLEVEL", 1);
		this.currentlStage = sharedPreferences.getInt("CURRENTSTAGE", 1);
		final int volume = sharedPreferences.getInt("VOLUME",10);
		final boolean isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
		
		String[] stageImageNames = new String[10];
		for (int i = 0; i < this.currentlLevel - 1; i++) {
			stageImageNames[i] = "hex.png";
		}
		stageImageNames[this.currentlLevel - 1] = "hex_current.png";
		for (int i = this.currentlLevel; i < 10; i++) {
			stageImageNames[i] = "hex_locked.png";
		}
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 48, 480, TextureOptions.BILINEAR);
		this.mLevel1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[0], 0, 0, 1, 1);
		this.mLevel2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[1], 0, 48, 1, 1);
		this.mLevel3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[2], 0, 96, 1, 1);
		this.mLevel4 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[3], 0, 144, 1, 1);
		this.mLevel5 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[4], 0, 192, 1, 1);
		this.mLevel6 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[5], 0, 240, 1, 1);
		this.mLevel7 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[6], 0, 288, 1, 1);
		this.mLevel8 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[7], 0, 336, 1, 1);
		this.mLevel9 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[8], 0, 384, 1, 1);
		this.mLevel10 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, stageImageNames[9], 0, 432, 1, 1);
		if (null != this.mBitmapTextureAtlas) {
			this.mBitmapTextureAtlas.load();
		}
		
		String[] levelImageNames = new String[30];
		for (int i = 0; i < this.currentlStage - 1; i++) {
			levelImageNames[i] = "filled_star.png";
		}
		levelImageNames[this.currentlStage - 1] = "current_star.png";
		for (int i = this.currentlStage; i < 30; i++) {
			levelImageNames[i] = "hollow_star.png";
		}
		
		this.mBitmapTextureAtlasLevel1 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel1, this, levelImageNames[0], 0, 0, 1, 1);
		this.mStar2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel1, this, levelImageNames[1], 0, 20, 1, 1);
		this.mStar3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel1, this, levelImageNames[2], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel1) {
			this.mBitmapTextureAtlasLevel1.load();
		}
		this.mBitmapTextureAtlasLevel2 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar4 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel2, this, levelImageNames[3], 0, 0, 1, 1);
		this.mStar5 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel2, this, levelImageNames[4], 0, 20, 1, 1);
		this.mStar6 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel2, this, levelImageNames[5], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel2) {
			this.mBitmapTextureAtlasLevel2.load();
		}
		this.mBitmapTextureAtlasLevel3 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar7 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel3, this, levelImageNames[6], 0, 0, 1, 1);
		this.mStar8 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel3, this, levelImageNames[7], 0, 20, 1, 1);
		this.mStar9 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel3, this, levelImageNames[8], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel3) {
			this.mBitmapTextureAtlasLevel3.load();
		}
		this.mBitmapTextureAtlasLevel4 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar10 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel4, this, levelImageNames[9], 0, 0, 1, 1);
		this.mStar11 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel4, this, levelImageNames[10], 0, 20, 1, 1);
		this.mStar12 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel4, this, levelImageNames[11], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel4) {
			this.mBitmapTextureAtlasLevel4.load();
		}
		this.mBitmapTextureAtlasLevel5 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar13 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel5, this, levelImageNames[12], 0, 0, 1, 1);
		this.mStar14 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel5, this, levelImageNames[13], 0, 20, 1, 1);
		this.mStar15 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel5, this, levelImageNames[14], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel5) {
			this.mBitmapTextureAtlasLevel5.load();
		}
		this.mBitmapTextureAtlasLevel6 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar16 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel6, this, levelImageNames[15], 0, 0, 1, 1);
		this.mStar17 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel6, this, levelImageNames[16], 0, 20, 1, 1);
		this.mStar18 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel6, this, levelImageNames[17], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel6) {
			this.mBitmapTextureAtlasLevel6.load();
		}
		this.mBitmapTextureAtlasLevel7 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar19 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel7, this, levelImageNames[18], 0, 0, 1, 1);
		this.mStar20 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel7, this, levelImageNames[19], 0, 20, 1, 1);
		this.mStar21 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel7, this, levelImageNames[20], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel7) {
			this.mBitmapTextureAtlasLevel7.load();
		}
		this.mBitmapTextureAtlasLevel8 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar22 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel8, this, levelImageNames[21], 0, 0, 1, 1);
		this.mStar23 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel8, this, levelImageNames[22], 0, 20, 1, 1);
		this.mStar24 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel8, this, levelImageNames[23], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel8) {
			this.mBitmapTextureAtlasLevel8.load();
		}
		this.mBitmapTextureAtlasLevel9 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar25 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel9, this, levelImageNames[24], 0, 0, 1, 1);
		this.mStar26 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel9, this, levelImageNames[25], 0, 20, 1, 1);
		this.mStar27 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel9, this, levelImageNames[26], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel9) {
			this.mBitmapTextureAtlasLevel9.load();
		}
		this.mBitmapTextureAtlasLevel10 = new BitmapTextureAtlas(this.getTextureManager(), 20, 60, TextureOptions.BILINEAR);
		this.mStar28 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel10, this, levelImageNames[27], 0, 0, 1, 1);
		this.mStar29 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel10, this, levelImageNames[28], 0, 20, 1, 1);
		this.mStar30 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLevel10, this, levelImageNames[29], 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLevel10) {
			this.mBitmapTextureAtlasLevel10.load();
		}
		
		try {
			this.mClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tick.mp3");
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
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
	    
		final Text tHeading = new Text(0, 0, this.mRavieFontHeading, "Select Level", new TextOptions(HorizontalAlign.CENTER), vertexBufferObjectManager);
		tHeading.setPosition((Constants.CAMERA_WIDTH - tHeading.getWidth()) / 2, 40);
		this.scene.attachChild(tHeading);
		
		final Sprite sBackground = new Sprite(0, 0, this.mBackground, this.vertexBufferObjectManager);
		sBackground.setPosition((Constants.CAMERA_WIDTH - sBackground.getWidth()) / 2, 100);
		this.scene.attachChild(sBackground);
		
		final Sprite sLevel1 = new Sprite(87, 105, this.mLevel1, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(1);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x1 = sLevel1.getX() + sLevel1.getWidth() / 2;
		final float y1 = sLevel1.getY() + sLevel1.getHeight() / 2;
		final Sprite sStar1 = new Sprite(-5, 45, this.mStar1, this.vertexBufferObjectManager);
		final Sprite sStar2 = new Sprite(15, 45, this.mStar2, this.vertexBufferObjectManager);
		final Sprite sStar3 = new Sprite(35, 45, this.mStar3, this.vertexBufferObjectManager);
		sLevel1.attachChild(sStar1);
		sLevel1.attachChild(sStar2);
		sLevel1.attachChild(sStar3);
		
		final Sprite sLevel2 = new Sprite(18, 160, this.mLevel2, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(2);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x2 = sLevel2.getX() + sLevel2.getWidth() / 2;
		final float y2 = sLevel2.getY() + sLevel2.getHeight() / 2;
		final Sprite sStar4 = new Sprite(-5, 45, this.mStar4, this.vertexBufferObjectManager);
		final Sprite sStar5 = new Sprite(15, 45, this.mStar5, this.vertexBufferObjectManager);
		final Sprite sStar6 = new Sprite(35, 45, this.mStar6, this.vertexBufferObjectManager);
		sLevel2.attachChild(sStar4);
		sLevel2.attachChild(sStar5);
		sLevel2.attachChild(sStar6);
		
		final Sprite sLevel3 = new Sprite(22, 256, this.mLevel3, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(3);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x3 = sLevel3.getX() + sLevel3.getWidth() / 2;
		final float y3 = sLevel3.getY() + sLevel3.getHeight() / 2;
		final Sprite sStar7 = new Sprite(-5, 45, this.mStar7, this.vertexBufferObjectManager);
		final Sprite sStar8 = new Sprite(15, 45, this.mStar8, this.vertexBufferObjectManager);
		final Sprite sStar9 = new Sprite(35, 45, this.mStar9, this.vertexBufferObjectManager);
		sLevel3.attachChild(sStar7);
		sLevel3.attachChild(sStar8);
		sLevel3.attachChild(sStar9);
		
		final Sprite sLevel4 = new Sprite(107, 299, this.mLevel4, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(4);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x4 = sLevel4.getX() + sLevel4.getWidth() / 2;
		final float y4 = sLevel4.getY() + sLevel4.getHeight() / 2;
		final Sprite sStar10 = new Sprite(-5, 45, this.mStar10, this.vertexBufferObjectManager);
		final Sprite sStar11 = new Sprite(15, 45, this.mStar11, this.vertexBufferObjectManager);
		final Sprite sStar12 = new Sprite(35, 45, this.mStar12, this.vertexBufferObjectManager);
		sLevel4.attachChild(sStar10);
		sLevel4.attachChild(sStar11);
		sLevel4.attachChild(sStar12);
		
		final Sprite sLevel5 = new Sprite(153, 227, this.mLevel5, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(5);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x5 = sLevel5.getX() + sLevel5.getWidth() / 2;
		final float y5 = sLevel5.getY() + sLevel5.getHeight() / 2;
		final Sprite sStar13 = new Sprite(-5, 45, this.mStar13, this.vertexBufferObjectManager);
		final Sprite sStar14 = new Sprite(15, 45, this.mStar14, this.vertexBufferObjectManager);
		final Sprite sStar15 = new Sprite(35, 45, this.mStar15, this.vertexBufferObjectManager);
		sLevel5.attachChild(sStar13);
		sLevel5.attachChild(sStar14);
		sLevel5.attachChild(sStar15);
		
		final Sprite sLevel6 = new Sprite(224, 157, this.mLevel6, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(6);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x6 = sLevel6.getX() + sLevel6.getWidth() / 2;
		final float y6 = sLevel6.getY() + sLevel6.getHeight() / 2;
		final Sprite sStar16 = new Sprite(-5, 45, this.mStar16, this.vertexBufferObjectManager);
		final Sprite sStar17 = new Sprite(15, 45, this.mStar17, this.vertexBufferObjectManager);
		final Sprite sStar18 = new Sprite(35, 45, this.mStar18, this.vertexBufferObjectManager);
		sLevel6.attachChild(sStar16);
		sLevel6.attachChild(sStar17);
		sLevel6.attachChild(sStar18);
		
		final Sprite sLevel7 = new Sprite(345 , 116, this.mLevel7, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(7);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x7 = sLevel7.getX() + sLevel7.getWidth() / 2;
		final float y7 = sLevel7.getY() + sLevel7.getHeight() / 2;
		final Sprite sStar19 = new Sprite(-5, 45, this.mStar19, this.vertexBufferObjectManager);
		final Sprite sStar20 = new Sprite(15, 45, this.mStar20, this.vertexBufferObjectManager);
		final Sprite sStar21 = new Sprite(35, 45, this.mStar21, this.vertexBufferObjectManager);
		sLevel7.attachChild(sStar19);
		sLevel7.attachChild(sStar20);
		sLevel7.attachChild(sStar21);
		
		final Sprite sLevel8 = new Sprite(360, 246, this.mLevel8, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(8);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x8 = sLevel8.getX() + sLevel8.getWidth() / 2;
		final float y8 = sLevel8.getY() + sLevel8.getHeight() / 2;
		final Sprite sStar22 = new Sprite(-5, 45, this.mStar22, this.vertexBufferObjectManager);
		final Sprite sStar23 = new Sprite(15, 45, this.mStar23, this.vertexBufferObjectManager);
		final Sprite sStar24 = new Sprite(35, 45, this.mStar24, this.vertexBufferObjectManager);
		sLevel8.attachChild(sStar22);
		sLevel8.attachChild(sStar23);
		sLevel8.attachChild(sStar24);
		
		final Sprite sLevel9 = new Sprite(432, 178, this.mLevel9, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(9);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x9 = sLevel9.getX() + sLevel9.getWidth() / 2;
		final float y9 = sLevel9.getY() + sLevel9.getHeight() / 2;
		final Sprite sStar25 = new Sprite(-5, 45, this.mStar25, this.vertexBufferObjectManager);
		final Sprite sStar26 = new Sprite(15, 45, this.mStar26, this.vertexBufferObjectManager);
		final Sprite sStar27 = new Sprite(35, 45, this.mStar27, this.vertexBufferObjectManager);
		sLevel9.attachChild(sStar25);
		sLevel9.attachChild(sStar26);
		sLevel9.attachChild(sStar27);
		
		final Sprite sLevel10 = new Sprite(529, 155, this.mLevel10, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startNextActivity(10);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final float x10 = sLevel10.getX() + sLevel10.getWidth() / 2;
		final float y10 = sLevel10.getY() + sLevel10.getHeight() / 2;
		final Sprite sStar28 = new Sprite(-5, 45, this.mStar28, this.vertexBufferObjectManager);
		final Sprite sStar29 = new Sprite(15, 45, this.mStar29, this.vertexBufferObjectManager);
		final Sprite sStar30 = new Sprite(35, 45, this.mStar30, this.vertexBufferObjectManager);
		sLevel10.attachChild(sStar28);
		sLevel10.attachChild(sStar29);
		sLevel10.attachChild(sStar30);
		
		final Line sline1 = new Line(x1, y1, x2, y2, 15, vertexBufferObjectManager);
		final Line sline2 = new Line(x2, y2, x3, y3, 15, vertexBufferObjectManager);
		final Line sline3 = new Line(x3, y3, x4, y4, 15, vertexBufferObjectManager);
		final Line sline4 = new Line(x4, y4, x5, y5, 15, vertexBufferObjectManager);
		final Line sline5 = new Line(x5, y5, x6, y6, 15, vertexBufferObjectManager);
		final Line sline6 = new Line(x6, y6, x7, y7, 15, vertexBufferObjectManager);
		final Line sline7 = new Line(x7, y7, x8, y8, 15, vertexBufferObjectManager);
		final Line sline8 = new Line(x8, y8, x9, y9, 15, vertexBufferObjectManager);
		final Line sline9 = new Line(x9, y9, x10, y10, 15, vertexBufferObjectManager);
		
		sline1.setColor(0.22f, 0.16f, 0.07f);
		sline2.setColor(0.22f, 0.16f, 0.07f);
		sline3.setColor(0.22f, 0.16f, 0.07f);
		sline4.setColor(0.22f, 0.16f, 0.07f);
		sline5.setColor(0.22f, 0.16f, 0.07f);
		sline6.setColor(0.22f, 0.16f, 0.07f);
		sline7.setColor(0.22f, 0.16f, 0.07f);
		sline8.setColor(0.22f, 0.16f, 0.07f);
		sline9.setColor(0.22f, 0.16f, 0.07f);
		
		final Text tAnswer1 = new Text(0, 0, this.mRavieFont, "1", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer2 = new Text(0, 0, this.mRavieFont, "2", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer3 = new Text(0, 0, this.mRavieFont, "3", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer4 = new Text(0, 0, this.mRavieFont, "4", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer5 = new Text(0, 0, this.mRavieFont, "5", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer6 = new Text(0, 0, this.mRavieFont, "6", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer7 = new Text(0, 0, this.mRavieFont, "7", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer8 = new Text(0, 0, this.mRavieFont, "8", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer9 = new Text(0, 0, this.mRavieFont, "9", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer10 = new Text(0, 0, this.mRavieFont, "10", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		
		tAnswer1.setPosition((sLevel1.getWidth() - tAnswer1.getWidth()) / 2, (sLevel1.getHeight() - tAnswer1.getHeight()) / 2);
		tAnswer2.setPosition((sLevel2.getWidth() - tAnswer2.getWidth()) / 2, (sLevel2.getHeight() - tAnswer2.getHeight()) / 2);
		tAnswer3.setPosition((sLevel3.getWidth() - tAnswer3.getWidth()) / 2, (sLevel3.getHeight() - tAnswer3.getHeight()) / 2);
		tAnswer4.setPosition((sLevel4.getWidth() - tAnswer4.getWidth()) / 2, (sLevel4.getHeight() - tAnswer4.getHeight()) / 2);
		tAnswer5.setPosition((sLevel5.getWidth() - tAnswer5.getWidth()) / 2, (sLevel5.getHeight() - tAnswer5.getHeight()) / 2);
		tAnswer6.setPosition((sLevel6.getWidth() - tAnswer6.getWidth()) / 2, (sLevel6.getHeight() - tAnswer6.getHeight()) / 2);
		tAnswer7.setPosition((sLevel7.getWidth() - tAnswer7.getWidth()) / 2, (sLevel7.getHeight() - tAnswer7.getHeight()) / 2);
		tAnswer8.setPosition((sLevel8.getWidth() - tAnswer8.getWidth()) / 2, (sLevel8.getHeight() - tAnswer8.getHeight()) / 2);
		tAnswer9.setPosition((sLevel9.getWidth() - tAnswer9.getWidth()) / 2, (sLevel9.getHeight() - tAnswer9.getHeight()) / 2);
		tAnswer10.setPosition((sLevel10.getWidth() - tAnswer10.getWidth()) / 2, (sLevel10.getHeight() - tAnswer10.getHeight()) / 2);
		
		sBackground.attachChild(sline1);
		sBackground.attachChild(sline2);
		sBackground.attachChild(sline3);
		sBackground.attachChild(sline4);
		sBackground.attachChild(sline5);
		sBackground.attachChild(sline6);
		sBackground.attachChild(sline7);
		sBackground.attachChild(sline8);
		sBackground.attachChild(sline9);
		
		sLevel1.attachChild(tAnswer1);
		sLevel2.attachChild(tAnswer2);
		sLevel3.attachChild(tAnswer3);
		sLevel4.attachChild(tAnswer4);
		sLevel5.attachChild(tAnswer5);
		sLevel6.attachChild(tAnswer6);
		sLevel7.attachChild(tAnswer7);
		sLevel8.attachChild(tAnswer8);
		sLevel9.attachChild(tAnswer9);
		sLevel10.attachChild(tAnswer10);
		
		sBackground.attachChild(sLevel1);
		sBackground.attachChild(sLevel2);
		sBackground.attachChild(sLevel3);
		sBackground.attachChild(sLevel4);
		sBackground.attachChild(sLevel5);
		sBackground.attachChild(sLevel6);
		sBackground.attachChild(sLevel7);
		sBackground.attachChild(sLevel8);
		sBackground.attachChild(sLevel9);
		sBackground.attachChild(sLevel10);
		
		switch (this.currentlLevel) {
		case 10:
			this.scene.registerTouchArea(sLevel10);
		case 9:
			this.scene.registerTouchArea(sLevel9);
		case 8:
			this.scene.registerTouchArea(sLevel8);
		case 7:
			this.scene.registerTouchArea(sLevel7);
		case 6:
			this.scene.registerTouchArea(sLevel6);
		case 5:
			this.scene.registerTouchArea(sLevel5);
		case 4:
			this.scene.registerTouchArea(sLevel4);
		case 3:
			this.scene.registerTouchArea(sLevel3);
		case 2:
			this.scene.registerTouchArea(sLevel2);
		case 1:
			this.scene.registerTouchArea(sLevel1);
		}

		return this.scene;
	}
	
	protected void startNextActivity(int levelClicked) {
		Intent intent = new Intent(StageSelectLevelActivity.this, StageSelectStageActivity.class);
		intent.putExtra("LEVELCLICKED", levelClicked);
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
	protected void onSetContentView() {
        final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.FILL);
        final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        this.adView = new AdView(this);
        this.adView.setAdUnitId(getString(R.string.bottom_banner_stageselectlevelactivity));
        this.adView.setAdSize(AdSize.BANNER);
        this.adView.setVisibility(AdView.VISIBLE);
        this.adView.refreshDrawableState();

        final AdRequest request = new AdRequest.Builder().build();
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if (null != adView) {
					adView.loadAd(request);
				}
			}
		});

        this.mRenderSurfaceView = new RenderSurfaceView(this);
        this.mRenderSurfaceView.setRenderer(mEngine, this);

        final FrameLayout.LayoutParams surfaceViewLayoutParams = new FrameLayout.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT, android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        surfaceViewLayoutParams.gravity = Gravity.CENTER ;

        frameLayout.addView(this.mRenderSurfaceView, surfaceViewLayoutParams);
        frameLayout.addView(this.adView, adViewLayoutParams);
        this.setContentView(frameLayout, frameLayoutLayoutParams);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (null != this.mClickSound) {
				this.mClickSound.play();
			}
			startActivity(new Intent(StageSelectLevelActivity.this, MenuActivity.class));
			finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public synchronized void onResumeGame() {
	    if (null != this.mEngine)
	        super.onResumeGame();
	}
}
