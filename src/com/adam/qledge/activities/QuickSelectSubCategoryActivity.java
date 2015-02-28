/**
 * 
 */
package com.adam.qledge.activities;

import java.io.IOException;
import java.util.Arrays;

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
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.adam.qledge.R;
import com.adam.qledge.constants.Constants;
import com.adam.qledge.constants.Messages;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

/**
 * @author Arindam
 *
 */
public class QuickSelectSubCategoryActivity extends SimpleBaseGameActivity {

	private static final String TAG = "QuickSelectSubCategoryActivity";
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;
	
	private ITexture ravieFontTexture;
	private Font mRavieFont;
	private ITexture ravieFontTextureChoice;
	private Font mRavieFontChoice;
	
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	
	private boolean[] areChecked;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTileBackground;
	private TiledTextureRegion mTileNext;
	
	private BitmapTextureAtlas mBitmapTextureAtlasSub0;
	private BitmapTextureAtlas mBitmapTextureAtlasSub1;
	private BitmapTextureAtlas mBitmapTextureAtlasSub2;
	private BitmapTextureAtlas mBitmapTextureAtlasSub3;
	private BitmapTextureAtlas mBitmapTextureAtlasSub4;
	private TiledTextureRegion mTileCheckedSub0;
	private TiledTextureRegion mTileCheckedSub1;
	private TiledTextureRegion mTileCheckedSub2;
	private TiledTextureRegion mTileCheckedSub3;
	private TiledTextureRegion mTileCheckedSub4;
	
	private Sound mClickSound;
	private AdView adView;
	
	private int choiceCategory;
	
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
		this.ravieFontTextureChoice = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFontChoice = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureChoice, this.getAssets(), "ravie.ttf", Constants.SMALLFONTSIZE, true, Color.rgb(57, 41, 17));
		if (null != this.mRavieFontChoice) {
			this.mRavieFontChoice.load();
		}
		
		this.areChecked = new boolean[5];
		Arrays.fill(areChecked, Boolean.TRUE);
		
		final Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.choiceCategory = extras.getInt("CATEGORY");
		}
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 540, 435, TextureOptions.BILINEAR);
		this.mTileBackground = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "tile_background.png", 0, 0, 1, 1);
		this.mTileNext =  BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "arrow_right.png", 0, 360, 1, 1);
		if (null != this.mBitmapTextureAtlas) {
			this.mBitmapTextureAtlas.load();
		}
		
		this.mBitmapTextureAtlasSub0 = new BitmapTextureAtlas(this.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		this.mTileCheckedSub0 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasSub0, this, "checkbox_checked.png", 0, 0, 1, 1);
		this.mBitmapTextureAtlasSub1 = new BitmapTextureAtlas(this.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		this.mTileCheckedSub1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasSub1, this, "checkbox_checked.png", 0, 0, 1, 1);
		this.mBitmapTextureAtlasSub2 = new BitmapTextureAtlas(this.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		this.mTileCheckedSub2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasSub2, this, "checkbox_checked.png", 0, 0, 1, 1);
		this.mBitmapTextureAtlasSub3 = new BitmapTextureAtlas(this.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		this.mTileCheckedSub3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasSub3, this, "checkbox_checked.png", 0, 0, 1, 1);
		this.mBitmapTextureAtlasSub4 = new BitmapTextureAtlas(this.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		this.mTileCheckedSub4 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasSub4, this, "checkbox_checked.png", 0, 0, 1, 1);
		if (null != this.mBitmapTextureAtlasSub0) {
			this.mBitmapTextureAtlasSub0.load();
		}if (null != this.mBitmapTextureAtlasSub1) {
			this.mBitmapTextureAtlasSub1.load();
		}if (null != this.mBitmapTextureAtlasSub2) {
			this.mBitmapTextureAtlasSub2.load();
		}if (null != this.mBitmapTextureAtlasSub3) {
			this.mBitmapTextureAtlasSub3.load();
		}if (null != this.mBitmapTextureAtlasSub4) {
			this.mBitmapTextureAtlasSub4.load();
		}
		
		try {
			this.mClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tick.mp3");
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
		
		final SharedPreferences sharedPreferences = QuickSelectSubCategoryActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
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
		
		final Text tHeading = new Text(0, 0, this.mRavieFont, "Select Sub-Category", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		tHeading.setPosition((Constants.CAMERA_WIDTH - tHeading.getWidth()) / 2, 40);
		this.scene.attachChild(tHeading);
		
		final Sprite sTileBackground = new Sprite(0, 0, this.mTileBackground, this.vertexBufferObjectManager);
		sTileBackground.setHeight(this.mTileBackground.getHeight()*0.8f);
		sTileBackground.setWidth(this.mTileBackground.getWidth()*1.0f);
		sTileBackground.setPosition((Constants.CAMERA_WIDTH - sTileBackground.getWidth()) / 2, 100);
		this.scene.attachChild(sTileBackground);
		
		String[] putChoice = new String[5];
		switch(this.choiceCategory) {
			case 0:
				putChoice = Messages.SUBCATEGORIES[0];
				break;
			case 1:
				putChoice = Messages.SUBCATEGORIES[1];
				break;
			case 2:
				putChoice = Messages.SUBCATEGORIES[2];
				break;
		}
		
		final Sprite sTileSubCatChoice0 = new Sprite(25 , 20, this.mTileCheckedSub0, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					mBitmapTextureAtlasSub0.clearTextureAtlasSources();
					areChecked[0] = !areChecked[0];
					BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasSub0, QuickSelectSubCategoryActivity.this, areChecked[0] ? "checkbox_checked.png" : "checkbox_unchecked.png", 0, 0, 1, 1);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					
				}
				return true;
			}
		};
		
		final Sprite sTileSubCatChoice1 = new Sprite(25 , 70, this.mTileCheckedSub1, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					mBitmapTextureAtlasSub1.clearTextureAtlasSources();
					areChecked[1] = !areChecked[1];
					BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasSub1, QuickSelectSubCategoryActivity.this, areChecked[1] ? "checkbox_checked.png" : "checkbox_unchecked.png", 0, 0, 1, 1);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					
				}
				return true;
			}
		};
		
		final Sprite sTileSubCatChoice2 = new Sprite(25 , 120, this.mTileCheckedSub2, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					mBitmapTextureAtlasSub2.clearTextureAtlasSources();
					areChecked[2] = !areChecked[2];
					BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasSub2, QuickSelectSubCategoryActivity.this, areChecked[2] ? "checkbox_checked.png" : "checkbox_unchecked.png", 0, 0, 1, 1);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					
				}
				return true;
			}
		};
		
		final Sprite sTileSubCatChoice3 = new Sprite(25 , 170, this.mTileCheckedSub3, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					mBitmapTextureAtlasSub3.clearTextureAtlasSources();
					areChecked[3] = !areChecked[3];
					BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasSub3, QuickSelectSubCategoryActivity.this, areChecked[3] ? "checkbox_checked.png" : "checkbox_unchecked.png", 0, 0, 1, 1);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					
				}
				return true;
			}
		};
		
		final Sprite sTileSubCatChoice4 = new Sprite(25 , 220, this.mTileCheckedSub4, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					mBitmapTextureAtlasSub4.clearTextureAtlasSources();
					areChecked[4] = !areChecked[4];
					BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasSub4, QuickSelectSubCategoryActivity.this, areChecked[4] ? "checkbox_checked.png" : "checkbox_unchecked.png", 0, 0, 1, 1);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					
				}
				return true;
			}
		};
		
		final Text tTextSubCatChoice0 = new Text(50, 12, this.mRavieFontChoice, putChoice[0], new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tTextSubCatChoice1 = new Text(50, 12, this.mRavieFontChoice, putChoice[1], new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tTextSubCatChoice2 = new Text(50, 12, this.mRavieFontChoice, putChoice[2], new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tTextSubCatChoice3 = new Text(50, 12, this.mRavieFontChoice, putChoice[3], new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tTextSubCatChoice4 = new Text(50, 12, this.mRavieFontChoice, putChoice[4], new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		
		final Sprite sTileNext = new Sprite(Constants.CAMERA_WIDTH - 100 , Constants.CAMERA_HEIGHT - 80, this.mTileNext, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					boolean anyChecked = false;
					for (int i=0; i<areChecked.length; i++) {
						if (areChecked[i] == Boolean.TRUE) {
							anyChecked = true;
							break;
						}
					}
					if (!anyChecked) {
						gameToast(Messages.SELECTCHIOSE);
					} else {
						startNextActivity();
					}
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}

		};
		this.scene.attachChild(sTileNext);
		
		sTileSubCatChoice0.attachChild(tTextSubCatChoice0);
		sTileSubCatChoice1.attachChild(tTextSubCatChoice1);
		sTileSubCatChoice2.attachChild(tTextSubCatChoice2);
		sTileSubCatChoice3.attachChild(tTextSubCatChoice3);
		sTileSubCatChoice4.attachChild(tTextSubCatChoice4);
		
		sTileBackground.attachChild(sTileSubCatChoice0);
		sTileBackground.attachChild(sTileSubCatChoice1);
		sTileBackground.attachChild(sTileSubCatChoice2);
		sTileBackground.attachChild(sTileSubCatChoice3);
		sTileBackground.attachChild(sTileSubCatChoice4);
		
		this.scene.registerTouchArea(sTileSubCatChoice0);
		this.scene.registerTouchArea(sTileSubCatChoice1);
		this.scene.registerTouchArea(sTileSubCatChoice2);
		this.scene.registerTouchArea(sTileSubCatChoice3);
		this.scene.registerTouchArea(sTileSubCatChoice4);
		this.scene.registerTouchArea(sTileNext);
		
		return this.scene;
	}
	
	public void gameToast(final String msg) {
	    this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	           Toast.makeText(QuickSelectSubCategoryActivity.this, msg, Toast.LENGTH_LONG).show();
	        }
	    });
	}
	
	private void startNextActivity() {
		Intent intent = new Intent(QuickSelectSubCategoryActivity.this, QuickGameActivity.class);
		intent.putExtra("CATEGORY", choiceCategory);
		intent.putExtra("SUBCATEGORY", areChecked);
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
			if (null != mClickSound) {
				mClickSound.play();
			}
			startActivity(new Intent(QuickSelectSubCategoryActivity.this, QuickSelectCategoryActivity.class));
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
        this.adView.setAdUnitId(getString(R.string.bottom_banner_quickselectsubcategoryactivity));
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
