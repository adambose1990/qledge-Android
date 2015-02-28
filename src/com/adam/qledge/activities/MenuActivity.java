package com.adam.qledge.activities;

import java.io.File;
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
import org.andengine.entity.util.ScreenCapture;
import org.andengine.entity.util.ScreenCapture.IScreenCaptureCallback;
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
import org.andengine.util.FileUtils;
import org.andengine.util.HorizontalAlign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.adam.qledge.R;
import com.adam.qledge.constants.Constants;
import com.adam.qledge.constants.Messages;
import com.adam.qledge.util.GBaseGameActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.games.Games;

/**
 * @author Arindam
 * 
 */

public class MenuActivity extends GBaseGameActivity {

	private static final String TAG = "MenuActivity";
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;
	
	private ITexture ravieFontTexture;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mMenuStageMode;
	private TiledTextureRegion mMenuQuickPlay;
	private TiledTextureRegion mMenuOptions;
	private TiledTextureRegion mMenuExit;
	
	private BitmapTextureAtlas mBitmapTextureAtlasMenu;
	private TiledTextureRegion mShare;
	private TiledTextureRegion mAchievements;
	private TiledTextureRegion mLeaderBoard;
	private TiledTextureRegion mStar;
	
	private BitmapTextureAtlas mBitmapTextureAtlasLoginout;
	private TiledTextureRegion mLogout;
	private TiledTextureRegion mLogin;
	
	private Sprite sLogout;
	private Sprite sLogin;
	
	private Sound mClickSound;
	
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	private ScreenCapture screenCapture;
	private Font mRavieFont;
	
	private AdView adView;

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
		
		this.ravieFontTexture = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFont = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTexture, this.getAssets(), "ravie.ttf", Constants.HEADINGFONTSIZE, true, Color.rgb(0, 65, 105));
		if (null != this.mRavieFont) {
			this.mRavieFont.load();
		}
		
		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 328, 360, TextureOptions.BILINEAR);
		this.mMenuStageMode = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_stagemode.png", 0, 0, 1, 1);
		this.mMenuQuickPlay = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_quickplay.png", 0, 80, 1, 1);
		this.mMenuOptions = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_options.png", 0, 160, 1, 1);
		this.mMenuExit = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_exit.png", 0, 240, 1, 1);
		if (null != this.mBitmapTextureAtlas) {
			this.mBitmapTextureAtlas.load();
		}
		
		this.mBitmapTextureAtlasMenu = new BitmapTextureAtlas(this.getTextureManager(), 48, 176, TextureOptions.BILINEAR);
		this.mShare = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "share.png", 0, 0, 1, 1);
		this.mAchievements = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "ic_play_games_badge_achievements_green.png", 0, 40, 1, 1);
		this.mLeaderBoard = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "ic_play_games_badge_leaderboards_green.png", 0, 88, 1, 1);
		this.mStar = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "star_icon.png", 0, 136, 1, 1);
		if (null != this.mBitmapTextureAtlasMenu) {
			this.mBitmapTextureAtlasMenu.load();
		}
		
		this.mBitmapTextureAtlasLoginout = new BitmapTextureAtlas(this.getTextureManager(), 40, 80, TextureOptions.BILINEAR);
		this.mLogout = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLoginout, MenuActivity.this, "logout.png", 0, 0, 1, 1);
		this.mLogin = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLoginout, MenuActivity.this, "google_plus.png", 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLoginout) {
			this.mBitmapTextureAtlasLoginout.load();
		}
		
		try {
			this.mClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tick.mp3");
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
		final SharedPreferences sharedPreferences = MenuActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
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
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.scene = new Scene();
		this.screenCapture = new ScreenCapture();
		this.vertexBufferObjectManager = this.getVertexBufferObjectManager();
		onCreateSceneBackground();
		
		final Text tHeading = new Text(0, 0, this.mRavieFont, "Q-Ledge", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		tHeading.setPosition((Constants.CAMERA_WIDTH - tHeading.getWidth()) / 2, 40);
		this.scene.attachChild(tHeading);
		
		final Sprite sMenuStageMode = new Sprite((Constants.CAMERA_WIDTH - this.mMenuStageMode.getWidth()) /2 , 120, 
				this.mMenuStageMode, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startActivity(new Intent(MenuActivity.this, StageSelectLevelActivity.class));
					finish();
					MenuActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final Sprite sMenuRandomPlay = new Sprite((Constants.CAMERA_WIDTH - this.mMenuQuickPlay.getWidth()) /2 , 190, 
				this.mMenuQuickPlay, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startActivity(new Intent(MenuActivity.this, QuickSelectCategoryActivity.class));
					finish();
					MenuActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final Sprite sMenuOptions = new Sprite((Constants.CAMERA_WIDTH - this.mMenuOptions.getWidth()) /2 , 260, 
				this.mMenuOptions, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					startActivity(new Intent(MenuActivity.this, OptionActivity.class));
					finish();
					MenuActivity.this.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		final Sprite sMenuExit = new Sprite((Constants.CAMERA_WIDTH - this.mMenuExit.getWidth()) /2 , 330, 
				this.mMenuExit, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					this.setAlpha(0.7f);
					if (null != mClickSound) {
						mClickSound.play();
					}
					MenuActivity.this.runOnUiThread(new Runnable() {
				        @Override
				        public void run() {
				        	exitAlert();  
				        }
				     });
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Sprite sShare = new Sprite(Constants.CAMERA_WIDTH - 50 , 20, 
				this.mShare, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					final String pFileName = FileUtils.getAbsolutePathOnExternalStorage(MenuActivity.this, "qledge_screen.png");
					captureImage(pFileName);
					shareImage(pFileName);
				}
				return true;
			}
		};
		
		final Sprite sAchievements = new Sprite(Constants.CAMERA_WIDTH - 54 , 70, 
				this.mAchievements, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					if (!getApiClient().isConnected()) {
						gameAlertDialog();
					} else {
						startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), 1);
					}
				}
				return true;
			}
		};
		
		final Sprite sLeaderBoard = new Sprite(Constants.CAMERA_WIDTH - 54 , 128, 
				this.mLeaderBoard, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					if (!getApiClient().isConnected()) {
						gameAlertDialog();
					} else {
						startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(getApiClient()), 2);
					}
				}
				return true;
			}
		};
		
		final Sprite sStar = new Sprite(Constants.CAMERA_WIDTH - 50 , 186, 
				this.mStar, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.adam.qledge"));
		            startActivity(intent);
				}
				return true;
			}
		};
		
		this.sLogin = new Sprite(Constants.CAMERA_WIDTH - 50 , 236, 
				this.mLogin, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					if (!getApiClient().isConnected()) {
						beginUserInitiatedSignIn();
						if (null != sLogin && null != sLogout) {
							sLogin.setVisible(false);
							sLogout.setVisible(true);
						}
					} else {
						signOut();
						if (null != sLogin && null != sLogout) {
							sLogin.setVisible(true);
							sLogout.setVisible(false);
						}
					}
				}
				return true;
			}
		};
		
		this.sLogout = new Sprite(Constants.CAMERA_WIDTH - 50 , 236, this.mLogout, this.vertexBufferObjectManager);
		
		if (null != sLogin && null != sLogout) {
			if (getApiClient().isConnected()) {
				sLogin.setVisible(false);
				sLogout.setVisible(true);
			} else {
				sLogin.setVisible(true);
				sLogout.setVisible(false);
			}
		}
		
		sMenuStageMode.setScale(Constants.SCALEFACTOR);
		sMenuRandomPlay.setScale(Constants.SCALEFACTOR);
		sMenuOptions.setScale(Constants.SCALEFACTOR);
		sMenuExit.setScale(Constants.SCALEFACTOR);
		
		this.scene.attachChild(sMenuStageMode);
		this.scene.attachChild(sMenuRandomPlay);
		this.scene.attachChild(sMenuOptions);
		this.scene.attachChild(sMenuExit);
		this.scene.attachChild(sShare);
		this.scene.attachChild(sAchievements);
		this.scene.attachChild(sLeaderBoard);
		this.scene.attachChild(sStar);
		this.scene.attachChild(this.sLogin);
		this.scene.attachChild(this.sLogout);
		this.scene.attachChild(this.screenCapture);
		
		this.scene.registerTouchArea(sMenuStageMode);
		this.scene.registerTouchArea(sMenuRandomPlay);
		this.scene.registerTouchArea(sMenuOptions);
		this.scene.registerTouchArea(sMenuExit);
		this.scene.registerTouchArea(sShare);
		this.scene.registerTouchArea(sAchievements);
		this.scene.registerTouchArea(sLeaderBoard);
		this.scene.registerTouchArea(sStar);
		this.scene.registerTouchArea(this.sLogin);
		//this.scene.registerTouchArea(sLogout);
		/*if (getApiClient().isConnected()) {
			this.interstitial = new InterstitialAd(MenuActivity.this);
	        this.interstitial.setAdUnitId(getString(R.string.interstitial_menuActivity));
	        final AdRequest adRequest = new AdRequest.Builder().build();
	        this.interstitial.setAdListener(new AdListener() {
	            @Override
	            public void onAdLoaded() {
	            	Toast.makeText(MenuActivity.this,
	                        "The interstitial is loaded", Toast.LENGTH_SHORT).show();
	            }
	
	            @Override
	            public void onAdClosed() {
	            }
	        });
			this.runOnUiThread(new Runnable() {
			      public void run() {
			    	  interstitial.loadAd(adRequest);
			    	  
			      }
			});
		}*/
		
		return this.scene;
	}
	
	private void captureImage(String pFileName) {
		final int viewWidth = MenuActivity.this.mRenderSurfaceView.getWidth();
        final int viewHeight = MenuActivity.this.mRenderSurfaceView.getHeight();
	    if (null != this.screenCapture) {
	    	this.screenCapture.capture(viewWidth, viewHeight, pFileName, new IScreenCaptureCallback() {
	            @Override
	            public void onScreenCaptured(final String pFilePath) {
	            	
	            }
	
	            @Override
	            public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
	            	
	            }
	        });
        }
	}
	
	private void shareImage(String pFileName) {
		final Uri imageUri = Uri.fromFile(new File(pFileName));
		final Intent shareIntent = new Intent();
		shareIntent.setAction(Intent.ACTION_SEND);
		shareIntent.setType("image/png");
		shareIntent.putExtra(Intent.EXTRA_TEXT, Messages.PROMOTEMESSAGE);
		shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		startActivity(Intent.createChooser(shareIntent, "Share via"));
	}
	
	private void gameAlertDialog() {
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	new AlertDialog.Builder(MenuActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(Messages.LOGINTITLE)
				.setMessage(Messages.LOGINMESSAGE)
				.setPositiveButton("Sign in", new DialogInterface.OnClickListener() {
					@Override
		            public void onClick(DialogInterface dialog, int which) {
						if (null != mClickSound) {
							mClickSound.play();
						}
						beginUserInitiatedSignIn();  
		            }
		        })
		        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
		            public void onClick(DialogInterface dialog, int which) {
						if (null != mClickSound) {
							mClickSound.play();
						}
		            }
		        })
		        .show();
	        }
	    });
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
	public void onSignInFailed() {
		if (null != this.sLogin && null != this.sLogout) {
			this.sLogin.setVisible(true);
			this.sLogout.setVisible(false);
		}
	}

	@Override
	public void onSignInSucceeded() {
		if (null != this.sLogin && null != this.sLogout) {
			this.sLogin.setVisible(false);
			this.sLogout.setVisible(true);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			if (null != this.mClickSound) {
				this.mClickSound.play();
			}
			exitAlert();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
	
	protected void exitAlert() {
		new AlertDialog.Builder(MenuActivity.this)
		.setIcon(android.R.drawable.ic_dialog_alert)
		.setTitle(Messages.QUITTITLE)
		.setMessage(Messages.EXITMESSAGE)
		.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				if (null != mClickSound) {
					mClickSound.play();
				}
				finish();
				overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
				System.exit(0);
            }
        })
        .setNegativeButton("No", new DialogInterface.OnClickListener() {
			@Override
            public void onClick(DialogInterface dialog, int which) {
				if (null != mClickSound) {
					mClickSound.play();
				}
            }
        })
        .show();
	}
	
	@Override
	protected void onSetContentView() {
        final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.FILL);
        final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        this.adView = new AdView(this);
        this.adView.setAdUnitId(getString(R.string.bottom_banner_menuactivity));
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
	public synchronized void onResumeGame() {
	    if (null != this.mEngine)
	        super.onResumeGame();
	}
}


