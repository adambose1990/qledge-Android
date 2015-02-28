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
import org.andengine.entity.slider.Slider;
import org.andengine.entity.slider.Slider.OnSliderValueChangeListener;
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

public class OptionActivity extends SimpleBaseGameActivity implements OnSliderValueChangeListener {

	private static final String TAG = "OptionActivity";
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;

	private Sound mClickSound;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTileBackground;
	
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	
	private ITexture ravieFontTextureHeading;
	private ITexture ravieFontTextureText;
	private Font mRavieFontHeading;
	private Font mRavieFontText;
	private Text tVolumeMessure;
	
	private AdView adView;
	
	private BitmapTextureAtlas mBitmapSliderTextureAtlas;
	private TiledTextureRegion mTileSlider;
	private TiledTextureRegion mTileThumb;
	
	private BitmapTextureAtlas mBitmapTextureAtlasCheck;
	private TiledTextureRegion mTileChecked;
	
	private int volume;
	private boolean isVolumeOn;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		final Camera camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
		EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.LANDSCAPE_SENSOR, new FillResolutionPolicy(), camera);
		engineOptions.getAudioOptions().setNeedsSound(true);
		return engineOptions;
	}

	@Override
	public void onCreateResources() {
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		FontFactory.setAssetBasePath("font/");
		SoundFactory.setAssetBasePath("mfx/");
		onCreateResourceBackground();
		
		final SharedPreferences sharedPreferences = this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
		this.volume = sharedPreferences.getInt("VOLUME", 10);
		this.isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
		
		this.ravieFontTextureHeading = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFontHeading = FontFactory.createFromAsset(this.getFontManager(), ravieFontTextureHeading, this.getAssets(), "ravie.ttf", Constants.HEADINGFONTSIZE, true, Color.rgb(0, 65, 105));
		if (null != this.mRavieFontHeading) {
			this.mRavieFontHeading.load();
		}
		
		this.ravieFontTextureText = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFontText = FontFactory.createFromAsset(this.getFontManager(), ravieFontTextureText, this.getAssets(), "ravie.ttf", Constants.SMALLFONTSIZE, true, Color.rgb(57, 41, 17));
		if (null != this.mRavieFontText) {
			this.mRavieFontText.load();
		}
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mTileBackground = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "tile_background.png", 0, 0, 1, 1);
		if (null != this.mBitmapTextureAtlas) {
			this.mBitmapTextureAtlas.load();
		}
		
		this.mBitmapSliderTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(),248, 48, TextureOptions.BILINEAR);
		this.mTileSlider = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapSliderTextureAtlas, this, "slider.png", 0, 0, 1, 1);
		this.mTileThumb = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapSliderTextureAtlas, this, "sliderthumb.png", 0, 18, 1, 1);
		if (null != this.mBitmapSliderTextureAtlas) {
			this.mBitmapSliderTextureAtlas.load();
		}
		
		this.mBitmapTextureAtlasCheck = new BitmapTextureAtlas(this.getTextureManager(), 48, 48, TextureOptions.BILINEAR);
		this.mTileChecked = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasCheck, this, isVolumeOn ? "checkbox_checked.png" : "checkbox_unchecked.png", 0, 0, 1, 1);				
		if (null != this.mBitmapTextureAtlasCheck) {
			this.mBitmapTextureAtlasCheck.load();
		}
		
		try {
			this.mClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tick.mp3");
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.scene = new Scene();
		this.vertexBufferObjectManager = this.getVertexBufferObjectManager();
		onCreateSceneBackground();
		
		final Text tHeading = new Text(0, 0, this.mRavieFontHeading, "Options", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		tHeading.setPosition((Constants.CAMERA_WIDTH - tHeading.getWidth()) / 2, 40);
		
		final Sprite sTileBackground = new Sprite(0, 0, this.mTileBackground, this.vertexBufferObjectManager);
		sTileBackground.setWidth(sTileBackground.getWidth() * 0.8f);
		sTileBackground.setHeight(sTileBackground.getHeight() * 0.8f);
		sTileBackground.setPosition((Constants.CAMERA_WIDTH - sTileBackground.getWidth()) / 2, 130);
		
		final Slider slider = new Slider(this.mTileSlider, this.mTileThumb, this.vertexBufferObjectManager);
		slider.setPosition(sTileBackground.getX() + 50, sTileBackground.getY() + sTileBackground.getHeight() / 2);
		slider.setValue((float) (this.volume * 10));
		slider.setOnSliderValueChangeListener(this);

		final Text tVolumeText = new Text(50, 50, this.mRavieFontText, "Volume", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		tVolumeMessure = new Text(350, sTileBackground.getHeight() / 2 - 5, this.mRavieFontText, volume+" ", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		
		if(null != this.tVolumeMessure) {
			if(this.isVolumeOn) {
				slider.setVisible(true);
				this.tVolumeMessure.setVisible(true);
			} else {
				slider.setVisible(false);
				this.tVolumeMessure.setVisible(false);
			}
		}
		
		final Sprite sTileVolumeChoice = new Sprite(350 , 40, this.mTileChecked, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					final SharedPreferences sharedPreferences = OptionActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
					volume = sharedPreferences.getInt("VOLUME",10);
					isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
					if (null != mClickSound) { 
						if (isVolumeOn) {
							mClickSound.setVolume(volume * 0.1f);
						} else {
							mClickSound.setVolume(0 * 0.1f);
						}
						mClickSound.play();
					}
					mBitmapTextureAtlasCheck.clearTextureAtlasSources();
					isVolumeOn = !isVolumeOn;
					BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasCheck, OptionActivity.this, isVolumeOn ? "checkbox_checked.png" : "checkbox_unchecked.png", 0, 0, 1, 1);
					if(isVolumeOn) {
						slider.setVisible(true);
						tVolumeMessure.setVisible(true);
					} else {
						slider.setVisible(false);
						tVolumeMessure.setVisible(false);
					}
					SharedPreferences.Editor editor = sharedPreferences.edit();
					editor.putBoolean("ISVOLUMEON", isVolumeOn);
					editor.commit();
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					
				}
				return true;
			}
		};
		
		sTileBackground.attachChild(tVolumeText);
		sTileBackground.attachChild(this.tVolumeMessure);
		sTileBackground.attachChild(sTileVolumeChoice);
		
		this.scene.attachChild(tHeading);
		this.scene.attachChild(sTileBackground);
		this.scene.attachChild(slider);
		
		this.scene.registerTouchArea(sTileVolumeChoice);
		this.scene.registerTouchArea(slider.getmThumb());
		this.scene.setTouchAreaBindingOnActionDownEnabled(true);

		return this.scene;
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
			final SharedPreferences sharedPreferences = OptionActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
			this.volume = sharedPreferences.getInt("VOLUME",10);
			this.isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
			if (null != mClickSound) { 
				if (isVolumeOn) {
					mClickSound.setVolume(volume * 0.1f);
				} else {
					mClickSound.setVolume(0 * 0.1f);
				}
				mClickSound.play();
			}
			startActivity(new Intent(OptionActivity.this, MenuActivity.class));
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
        this.adView.setAdUnitId(getString(R.string.bottom_banner_optionactivity));
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
	public void onSliderValueChanged(float value) {
		final int volume = ((Integer) Math.round(value / 10));
		this.tVolumeMessure.setText(Integer.toString(volume));
		final SharedPreferences sharedPreferences = OptionActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt("VOLUME", volume);
		editor.commit();
	}
	
	@Override
	public synchronized void onResumeGame() {
	    if (null != this.mEngine)
	        super.onResumeGame();
	}
}
