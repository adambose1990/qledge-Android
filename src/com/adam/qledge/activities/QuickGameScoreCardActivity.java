/**
 * 
 */
package com.adam.qledge.activities;

import java.io.File;
import java.io.IOException;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.modifier.AlphaModifier;
import org.andengine.entity.modifier.ParallelEntityModifier;
import org.andengine.entity.modifier.ScaleModifier;
import org.andengine.entity.modifier.SequenceEntityModifier;
import org.andengine.entity.primitive.Line;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.AnimatedSprite;
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
import android.opengl.GLES20;
import android.os.Bundle;
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
public class QuickGameScoreCardActivity extends GBaseGameActivity {

	private static final String TAG = "QuickGameScoreCardActivity";
	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTileBackground;
	private TiledTextureRegion mTileHome;
	private TiledTextureRegion mTileNewGame;
	private TiledTextureRegion mTileHighScore;
	
	private ITexture ravieFontTextureHeading;
	private ITexture ravieFontTextureNormal;
	private ITexture ravieFontTextureBigger;
	
	private Font mRavieFontHeading;
	private Font mRavieFontNormal;
	private Font mRavieFontBigger;
	
	private Text tTotalPoint;
	
	private int countCorrectAnswer;
	private int countWrongAnswer;
	private int countLife;
	private int countTime;
	private int totalPoint;
	private int pointCounter, tempCounter;
	private int highScore;
	
	private boolean firstGamePlayed;
	private int totalGameWon;
	private int totalGamePlayed;
	
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
	
	private AdView adView;
	
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	private ScreenCapture screenCapture;
	
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
		
		final Bundle extras = getIntent().getExtras();
		if (extras != null) {
			this.countCorrectAnswer = extras.getInt("COUNTCORRECT");
			this.countWrongAnswer = extras.getInt("COUNTWRONG");
			this.countTime = extras.getInt("COUNTTIME");
			this.countLife = extras.getInt("COUNTLIFE");
		}
		
		this.ravieFontTextureHeading = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.ravieFontTextureNormal = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.ravieFontTextureBigger = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFontHeading = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureHeading, this.getAssets(), "ravie.ttf", Constants.MEDIUMFONTSIZE, true, Color.rgb(0, 65, 105));
		this.mRavieFontNormal = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureNormal, this.getAssets(), "ravie.ttf", Constants.MEDIUMSMALLFONTSIZE, true, Color.rgb(57, 41, 17));
		this.mRavieFontBigger = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureBigger, this.getAssets(), "ravie.ttf", Constants.HEADINGFONTSIZE, true, Color.rgb(57, 41, 17));
		if (null != this.mRavieFontHeading) {
			this.mRavieFontHeading.load();
		}
		if (null != this.mRavieFontNormal) {
			this.mRavieFontNormal.load();
		}
		if (null != this.mRavieFontBigger) {
			this.mRavieFontBigger.load();
		}

		this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 650, 494, TextureOptions.BILINEAR);
		this.mTileBackground = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "scroll_background.png", 0, 0, 1, 1);	
		this.mTileNewGame = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "restart.png", 0, 278, 1, 1);
		this.mTileHighScore = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "highscore.png", 0, 334, 1, 1);
		if (null != this.mBitmapTextureAtlas) {
			this.mBitmapTextureAtlas.load();
		}
		
		this.mBitmapTextureAtlasMenu = new BitmapTextureAtlas(this.getTextureManager(), 48, 224, TextureOptions.BILINEAR);
		this.mTileHome = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "blue_home.png", 0, 0, 1, 1);
		this.mShare = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "share.png", 0, 48, 1, 1);
		this.mAchievements = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "ic_play_games_badge_achievements_green.png", 0, 88, 1, 1);
		this.mLeaderBoard = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "ic_play_games_badge_leaderboards_green.png", 0, 136, 1, 1);
		this.mStar = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasMenu, this, "star_icon.png", 0, 184, 1, 1);
		if (null != this.mBitmapTextureAtlasMenu) {
			this.mBitmapTextureAtlasMenu.load();
		}
		
		this.mBitmapTextureAtlasLoginout = new BitmapTextureAtlas(this.getTextureManager(), 40, 80, TextureOptions.BILINEAR);
		this.mLogout = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasLoginout, this, "logout.png", 0, 0, 1, 1);
		this.mLogin = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(mBitmapTextureAtlasLoginout, this, "google_plus.png", 0, 40, 1, 1);
		if (null != this.mBitmapTextureAtlasLoginout) {
			this.mBitmapTextureAtlasLoginout.load();
		}
		
		try {
			this.mClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tick.mp3");
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
		
		final SharedPreferences sharedPreferences = QuickGameScoreCardActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
		final int volume = sharedPreferences.getInt("VOLUME",10);
		final boolean isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
		if (isVolumeOn) {
			this.mClickSound.setVolume(volume * 0.1f);
		} else {
			this.mClickSound.setVolume(0 * 0.1f);
		}
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.scene = new Scene();
		this.screenCapture = new ScreenCapture();
		this.vertexBufferObjectManager = this.getVertexBufferObjectManager();
		onCreateSceneBackground();
		
		final Sprite sTileBackground = new Sprite(0, 0, this.mTileBackground, this.vertexBufferObjectManager);
		sTileBackground.setPosition((Constants.CAMERA_WIDTH - sTileBackground.getWidth()) / 2, (Constants.CAMERA_HEIGHT - sTileBackground.getHeight()) / 2);
		this.scene.attachChild(sTileBackground);
		
		int correctBonusPoint = this.countCorrectAnswer * Constants.CORRECTPOINT;
		int wrongPenaltyPoint = this.countWrongAnswer * Constants.WRONGPOINT;
		int timeBonusPoint = this.countTime * Constants.TIMEPOINT;
		int lifeBonusPoint = this.countLife * Constants.LIFEPOINT;
		this.totalPoint = correctBonusPoint - wrongPenaltyPoint + timeBonusPoint + lifeBonusPoint;
		
		final Text tHeading = new Text(0, 0, this.mRavieFontHeading, "Q-Ledge Score", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		tHeading.setPosition((Constants.CAMERA_WIDTH - tHeading.getWidth()) / 2, 30);
		
		this.scene.attachChild(tHeading);
		
		final Sprite sTileHome = new Sprite(Constants.CAMERA_WIDTH - 50, 20, this.mTileHome, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					this.setAlpha(0.5f);
					startNextActivity("HOME");
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Sprite sShare = new Sprite(Constants.CAMERA_WIDTH - 50 , 70, this.mShare, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					final String pFileName = FileUtils.getAbsolutePathOnExternalStorage(QuickGameScoreCardActivity.this, "qledge_screen.png");
					captureImage(pFileName);
					shareImage(pFileName);
				}
				return true;
			}
		};
		
		final Sprite sAchievements = new Sprite(Constants.CAMERA_WIDTH - 54 , 120, this.mAchievements, this.vertexBufferObjectManager) {
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
		
		final Sprite sLeaderBoard = new Sprite(Constants.CAMERA_WIDTH - 54 , 178, this.mLeaderBoard, this.vertexBufferObjectManager) {
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
		
		final Sprite sStar = new Sprite(Constants.CAMERA_WIDTH - 50 , 226, 
				this.mStar, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setData(Uri.parse(Messages.GAMELINK));
		            startActivity(intent);
				}
				return true;
			}
		};
		
		this.sLogin = new Sprite(Constants.CAMERA_WIDTH - 50 , 276, 
				this.mLogin, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					if (!getApiClient().isConnected()) {
						beginUserInitiatedSignIn();
						sLogin.setVisible(false);
						sLogout.setVisible(true);
					} else {
						signOut();
						sLogin.setVisible(true);
						sLogout.setVisible(false);
					}
				}
				return true;
			}
		};
		
		this.sLogout = new Sprite(Constants.CAMERA_WIDTH - 50 , 276, this.mLogout, this.vertexBufferObjectManager);
		
		final Sprite sTileNextGame = new Sprite(Constants.CAMERA_WIDTH - 100, Constants.CAMERA_HEIGHT - 80, 
				this.mTileNewGame, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					this.setAlpha(0.5f);
					startNextActivity("NEXTGAME");
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		if (getApiClient().isConnected()) {
			this.sLogin.setVisible(false);
			this.sLogout.setVisible(true);
		} else {
			this.sLogin.setVisible(true);
			this.sLogout.setVisible(false);
		}
		
		final AnimatedSprite sHighScore = new AnimatedSprite(310, 130, this.mTileHighScore, this.vertexBufferObjectManager);
		sHighScore.registerEntityModifier(new SequenceEntityModifier(
				new AlphaModifier(2f, 0.0f, 0.0f),
				new ParallelEntityModifier(
					new ScaleModifier(1, 1.0f, 0.8f),
					new AlphaModifier(1, 0.5f, 1.0f)
				)
			)
		);
		sHighScore.setVisible(false);
		
		this.highScore = loadScore(this);
		
		sTileBackground.attachChild(new Text(55, 50, this.mRavieFontNormal, "Correct Bonus:", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(310, 50, this.mRavieFontNormal, countCorrectAnswer + "", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(390, 50, this.mRavieFontNormal, "x", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(415, 50, this.mRavieFontNormal, Constants.CORRECTPOINT + "", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(470, 50, this.mRavieFontNormal, "=", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(500, 50, this.mRavieFontNormal, correctBonusPoint + "", this.vertexBufferObjectManager));
		
		sTileBackground.attachChild(new Text(55, 80, this.mRavieFontNormal, "Wrong Penalty:", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(310, 80, this.mRavieFontNormal, countWrongAnswer + "", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(390, 80, this.mRavieFontNormal, "x", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(415, 80, this.mRavieFontNormal, "-" + Constants.WRONGPOINT, this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(470, 80, this.mRavieFontNormal, "=", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(500, 80, this.mRavieFontNormal, "-" + wrongPenaltyPoint, this.vertexBufferObjectManager));
		
		sTileBackground.attachChild(new Text(55, 110, this.mRavieFontNormal, "Time Bonus:", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(310, 110, this.mRavieFontNormal, countTime + "", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(390, 110, this.mRavieFontNormal, "x", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(415, 110, this.mRavieFontNormal, Constants.TIMEPOINT + "", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(470, 110, this.mRavieFontNormal, "=", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(500, 110, this.mRavieFontNormal, timeBonusPoint + "", this.vertexBufferObjectManager));
		
		sTileBackground.attachChild(new Text(55, 140, this.mRavieFontNormal, "Life Bonus:", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(310, 140, this.mRavieFontNormal, countLife + "", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(390, 140, this.mRavieFontNormal, "x", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(415, 140, this.mRavieFontNormal, Constants.LIFEPOINT + "", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(470, 140, this.mRavieFontNormal, "=", this.vertexBufferObjectManager));
		sTileBackground.attachChild(new Text(500, 140, this.mRavieFontNormal, lifeBonusPoint + "",this. vertexBufferObjectManager));
		
		final Line line = new Line(50, 170, 565, 170, 5, this.vertexBufferObjectManager);
		line.setColor(0.22f, 0.16f, 0.06f);
		sTileBackground.attachChild(line);
		
		sTileBackground.attachChild(new Text(55, 180, this.mRavieFontNormal, "Total", this.vertexBufferObjectManager));
		
		this.tTotalPoint = new Text(380, 180, this.mRavieFontBigger, "   0", this.vertexBufferObjectManager);
		sTileBackground.attachChild(this.tTotalPoint);
		
		this.pointCounter = 0;
		this.tempCounter = (int) Math.log10(totalPoint);
		this.scene.registerUpdateHandler(new TimerHandler(0.02f, true, new ITimerCallback() {
	        @Override
	        public void onTimePassed(TimerHandler pTimerHandler) {
        		if (pointCounter <= totalPoint){
        			if((pointCounter < (totalPoint / Math.pow(10, tempCounter)) * Math.pow(10, tempCounter)) && (pointCounter + Math.pow(10, tempCounter) <= totalPoint)) {
        				pointCounter += Math.pow(10, tempCounter);
        			} else 
        				tempCounter--;
        			tTotalPoint.setText(pointCounter + "");
        			pTimerHandler.reset(); 
        		} else {
                    	scene.unregisterUpdateHandler(pTimerHandler);
                }
        	}
	    }));
		
		if (this.highScore < this.totalPoint) {
			saveScore(this, this.totalPoint);
			sTileBackground.attachChild(sHighScore);
			sHighScore.setVisible(true);
			sHighScore.setBlendFunction(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
		}
		
		savePreferences(this);
		
		final SharedPreferences sharedPreferences = this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
	    int ratingReply = sharedPreferences.getInt("RATINGREPLY", 0); // 0: Okay, 1: Later, 2: Never
	    if (this.totalGamePlayed == 2 || (this.totalGamePlayed % 10 == 0 && ratingReply == 1)) {
	    	rateAlertDialog();
	    }
	    
		this.scene.attachChild(sTileHome);
		this.scene.attachChild(sTileNextGame);
		this.scene.attachChild(sShare);
		this.scene.attachChild(sAchievements);
		this.scene.attachChild(sLeaderBoard);
		this.scene.attachChild(sStar);
		this.scene.attachChild(this.sLogin);
		this.scene.attachChild(this.sLogout);
		this.scene.attachChild(this.screenCapture);
		
		this.scene.registerTouchArea(sTileHome);
		this.scene.registerTouchArea(sTileNextGame);
		this.scene.registerTouchArea(sShare);
		this.scene.registerTouchArea(sAchievements);
		this.scene.registerTouchArea(sLeaderBoard);
		this.scene.registerTouchArea(sStar);
		this.scene.registerTouchArea(this.sLogin);
		//this.scene.registerTouchArea(sLogout);
		
		return this.scene;
	}
	
	private void captureImage(String pFileName) {
		final int viewWidth = this.mRenderSurfaceView.getWidth();
        final int viewHeight = this.mRenderSurfaceView.getHeight();
        this.screenCapture.capture(viewWidth, viewHeight, pFileName, new IScreenCaptureCallback() {
            @Override
            public void onScreenCaptured(final String pFilePath) {
            	
            }

            @Override
            public void onScreenCaptureFailed(final String pFilePath, final Exception pException) {
            	
            }
        });
	}
	
	private void shareImage(String pFileName) {
		 Intent shareIntent = new Intent();
		 shareIntent.setAction(Intent.ACTION_SEND);
		 shareIntent.setType("image/png");
		 shareIntent.putExtra(Intent.EXTRA_TEXT, Messages.PROMOTESCOREMESSAGEP1 + totalPoint + Messages.PROMOTESCOREMESSAGEP2);
		 Uri imageUri = Uri.fromFile(new File(pFileName));
		 shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
		 shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
		 shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		 startActivity(Intent.createChooser(shareIntent, "Share via"));
	}
	
	private void gameAlertDialog() {
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	new AlertDialog.Builder(QuickGameScoreCardActivity.this)
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
	
	private void rateAlertDialog() {
		this.runOnUiThread(new Runnable() {
	        @Override
	        public void run() {
	        	new AlertDialog.Builder(QuickGameScoreCardActivity.this)
				.setIcon(android.R.drawable.ic_dialog_info)
				.setTitle(Messages.RATETITLE)
				.setMessage(Messages.RATEMESSAGE)
				.setPositiveButton("Sure", new DialogInterface.OnClickListener() {
					@Override
		            public void onClick(DialogInterface dialog, int which) {
						if (null != mClickSound) {
							mClickSound.play();
						}
						Intent intent = new Intent(Intent.ACTION_VIEW);
						intent.setData(Uri.parse(Messages.GAMELINK));
			            startActivity(intent);
		            }
		        })
		        .setNeutralButton("Later", new DialogInterface.OnClickListener() {
					@Override
		            public void onClick(DialogInterface dialog, int which) {
						if (null != mClickSound) {
							mClickSound.play();
						}
						final SharedPreferences sharedPreferences = QuickGameScoreCardActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
					    SharedPreferences.Editor editor = sharedPreferences.edit();
					    editor.putInt("RATINGREPLY", 1);
					    editor.commit(); 
		            }
		        })
		        .setNegativeButton("Never", new DialogInterface.OnClickListener() {
					@Override
		            public void onClick(DialogInterface dialog, int which) {
						if (null != mClickSound) {
							mClickSound.play();
						}
						final SharedPreferences sharedPreferences = QuickGameScoreCardActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
					    SharedPreferences.Editor editor = sharedPreferences.edit();
					    editor.putInt("RATINGREPLY", 2);
					    editor.commit(); 
		            }
		        })
		        .show();
	        }
	    });
	}
	
	public int loadScore(Context context){
	    final SharedPreferences sharedPreferences = context.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
	    int score = sharedPreferences.getInt("HIGHSCORE", 0);
	    this.firstGamePlayed = sharedPreferences.getBoolean("FIRSTGAMEPLAYED", true);
	    this.totalGamePlayed = sharedPreferences.getInt("TOTALQUICKGAMEPLAYED", 0);
	    this.totalGameWon = sharedPreferences.getInt("TOTALGAMEWON", 0);
	    return score;
	}
	
	public void saveScore(Context context, int score){
	    final SharedPreferences sharedPreferences = context.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putInt("HIGHSCORE", score);
	    editor.commit();
	}

	public void savePreferences(Context context) {
		final SharedPreferences sharedPreferences = context.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
	    SharedPreferences.Editor editor = sharedPreferences.edit();
	    editor.putBoolean("FIRSTGAMEPLAYED", false);
	    editor.putInt("TOTALQUICKGAMEPLAYED", ++this.totalGamePlayed);
	    if (this.countLife == 3) {
	    	editor.putInt("TOTALGAMEWON", ++this.totalGameWon);
	    }
	    editor.commit();
	}
	
	private void startNextActivity(String choice) {
		Intent intent = null;
		if (choice.equals("HOME")) {
			intent = new Intent(QuickGameScoreCardActivity.this, MenuActivity.class);
		} else if (choice.equals("NEXTGAME")) {
			intent = new Intent(QuickGameScoreCardActivity.this, QuickSelectCategoryActivity.class);
		}
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
			Intent intent = new Intent(QuickGameScoreCardActivity.this, QuickSelectCategoryActivity.class);
			startActivity(intent);
			finish();
			overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onSignInFailed() {
		if (this.sLogin != null && this.sLogout != null) {
			this.sLogin.setVisible(true);
			this.sLogout.setVisible(false);
		}
	}

	@Override
	public void onSignInSucceeded() {
		if (this.sLogin != null && this.sLogout != null) {
			this.sLogin.setVisible(false);
			this.sLogout.setVisible(true);
		}
		Games.Leaderboards.submitScore(getApiClient(), getString(R.string.leaderboard_quickplay_leaderboard), totalPoint);
		if (this.firstGamePlayed) {
			Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_newbie));
		}
		if (this.totalPoint >= 1000) {
			Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_genius));
		}
		if (this.totalGameWon == 1) {
			Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_survivor));
		} else if (this.totalGameWon == 5) {
			Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_master));
		} else if (this.totalGameWon == 10) {
			Games.Achievements.unlock(getApiClient(), getString(R.string.achievement_supreme));
		}		
	}
	
	@Override
	protected void onSetContentView() {
        final FrameLayout frameLayout = new FrameLayout(this);
        final FrameLayout.LayoutParams frameLayoutLayoutParams = new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT, Gravity.FILL);
        final FrameLayout.LayoutParams adViewLayoutParams = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);

        this.adView = new AdView(this);
        this.adView.setAdUnitId(getString(R.string.bottom_banner_quickgamescorecardactivity));
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
