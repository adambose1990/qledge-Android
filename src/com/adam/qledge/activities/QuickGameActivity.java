/**
 * 
 */
package com.adam.qledge.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.scene.CameraScene;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.AutoParallaxBackground;
import org.andengine.entity.scene.background.ParallaxBackground.ParallaxEntity;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.AutoWrap;
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
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.adam.qledge.R;
import com.adam.qledge.constants.Constants;
import com.adam.qledge.constants.Messages;
import com.adam.qledge.data.QuestionData;

/**
 * @author Arindam
 *
 */
public class QuickGameActivity extends SimpleBaseGameActivity {
	
	private static final String TAG = "QuickGameActivity";
	private Camera camera;

	private BitmapTextureAtlas mAutoParallaxBackgroundTexture;
	private ITextureRegion mParallaxLayerBack;
	private ITextureRegion mParallaxLayerMid;
	private ITextureRegion mParallaxLayerFront;
	
	private ITexture ravieFontTextureInstruction;
	private ITexture ravieFontTextureQuestion;
	private ITexture ravieFontTextureAnswer;
	private ITexture ravieFontTexturePrompt;
	private ITexture ravieFontTextureClock;
	
	private Font mRavieFontInstruction;
	private Font mRavieFontQuestion;
	private Font mRavieFontAnswer;
	private Font mRavieFontPrompt;
	private Font mRavieFontClock;
	
	private Text tInstruction;
	private Text tQuestionNo;
	private Text tQuestion;
	private Text tPrompt;
	private Text tClock;
	private Sprite sTileNext;
	private Sprite sTileBackground;
	
	private BitmapTextureAtlas mBitmapTextureAtlas;
	private TiledTextureRegion mTileBackground;
	private TiledTextureRegion mTileAnswer1, mTileAnswer2, mTileAnswer3, mTileAnswer4;
	private TiledTextureRegion mTileNext;
	private BitmapTextureAtlas mBitmapTextureAtlasLife1,mBitmapTextureAtlasLife2,mBitmapTextureAtlasLife3;
	private TiledTextureRegion mTileLife1, mTileLife2, mTileLife3;
	private TiledTextureRegion mTilePause;
	private BitmapTextureAtlas mBitmapTextureAtlasPause;
	private ITextureRegion mPausedTextureRegion;
	
	private Sound mClickSound;
	private Sound mRightAnswerSound;
	private Sound mLifeGoneSound;
	private Sound mEmergencySound;
	
	private int choiceCategory;
	private boolean[] areChecked;
	private int currentQuestion = 0;
	private int countCorrectAnswer = 0;
	private int countWrongAnswer = 0;
	private int countLife = 3;
	private int countTime = Constants.MAXTIMEQUICKGAME;

	private boolean isAnswered = false;
	private boolean isTimedOut = false;
	private boolean isNextPageClicked = false;
	private boolean wasPaused = false;
	
	private Map<String, QuestionData> mapQuestionBank;
	private ArrayList<String> listQuestions;
	
	private VertexBufferObjectManager vertexBufferObjectManager;
	private Scene scene;
	private CameraScene mPauseScene;
	
	@Override
	public EngineOptions onCreateEngineOptions() {
		this.camera = new Camera(0, 0, Constants.CAMERA_WIDTH, Constants.CAMERA_HEIGHT);
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
			this.choiceCategory = extras.getInt("CATEGORY");
			this.areChecked = extras.getBooleanArray("SUBCATEGORY");
		}
		
		this.ravieFontTextureInstruction = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.ravieFontTextureQuestion = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.ravieFontTextureAnswer = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.ravieFontTexturePrompt = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.ravieFontTextureClock = new BitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.BILINEAR);
		this.mRavieFontInstruction = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureInstruction, this.getAssets(), "ravie.ttf", Constants.VERYSMALLFONTSIZE, true, Color.rgb(0, 65, 105));
		this.mRavieFontQuestion = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureQuestion, this.getAssets(), "ravie.ttf", Constants.VERYSMALLFONTSIZE, true, Color.rgb(150, 0, 0));
		this.mRavieFontAnswer = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureAnswer, this.getAssets(), "ravie.ttf", Constants.MEDIUMFONTSIZE, true, Color.rgb(57, 41, 17));
		this.mRavieFontPrompt = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTexturePrompt, this.getAssets(), "ravie.ttf", Constants.VERYSMALLFONTSIZE, true, Color.rgb(57, 41, 17));
		this.mRavieFontClock = FontFactory.createFromAsset(this.getFontManager(), this.ravieFontTextureClock, this.getAssets(), "ravie.ttf", Constants.SMALLFONTSIZE, true, Color.rgb(57, 41, 17));
		if (null != this.mRavieFontInstruction) {
			this.mRavieFontInstruction.load();
		}
		if (null != this.mRavieFontQuestion) {
			this.mRavieFontQuestion.load();
		}
		if (null != this.mRavieFontAnswer) {
			this.mRavieFontAnswer.load();
		}
		if (null != this.mRavieFontPrompt) {
			this.mRavieFontPrompt.load();
		}
		if (null != this.mRavieFontClock) {
			this.mRavieFontClock.load();
		}

		InputStream stream = null;
		BufferedReader reader = null;
		this.mapQuestionBank = new HashMap<String, QuestionData>();
		try {
			switch (this.choiceCategory) {
				case 0:
					stream = getAssets().open("csv/questions_apti.csv");
					break;
				case 1:
					stream = getAssets().open("csv/questions_logical.csv");
					break;
				case 2:
					stream = getAssets().open("csv/questions_verbal.csv");
					break;
			}
        } catch (IOException e) {
        	Log.d(TAG, e.getMessage());
        }
		
		reader = new BufferedReader(new InputStreamReader(stream, Charset.forName("UTF-8")));
        String line = "";
        StringTokenizer st = null;
        try {
            while ((line = reader.readLine()) != null) {
                st = new StringTokenizer(line, ",");
                QuestionData quesData = new QuestionData();
                quesData.setQuestionOID(st.nextToken());
                quesData.setCategory(Integer.parseInt(st.nextToken()));
                quesData.setSubCategory(Integer.parseInt(st.nextToken()));
                quesData.setQuestion(st.nextToken());
                quesData.setAnswer1(st.nextToken());
                quesData.setAnswer2(st.nextToken());
                quesData.setAnswer3(st.nextToken());
                quesData.setAnswer4(st.nextToken());
                this.mapQuestionBank.put(quesData.getQuestionOID(), quesData);
            }
        } catch (IOException e) {
        	Log.d(TAG, e.getMessage());
        } finally {
        	try {
        		reader.close();
				stream.close();
			} catch (IOException e) {
				Log.d(TAG, e.getMessage());
			}
        }
        
        ArrayList<Integer> listSubCategory = new ArrayList<Integer>();
        for (int i = 0; i < this.areChecked.length; i++) {
			if (this.areChecked[i] == Boolean.TRUE)
				listSubCategory.add(i);
		}
        
        this.listQuestions = new ArrayList<String>();
        Iterator<String> iterator = this.mapQuestionBank.keySet().iterator();
        while(iterator.hasNext()) {
        	QuestionData questionData = this.mapQuestionBank.get(iterator.next());
        	if(listSubCategory.contains(questionData.getSubCategory()))
        		this.listQuestions.add(questionData.getQuestionOID());
        }
        Collections.shuffle(this.listQuestions);
        
        this.mBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 540, 803, TextureOptions.BILINEAR);
		this.mTileAnswer1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_a.png", 0, 0, 1, 1);
		this.mTileAnswer2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_b.png", 0, 80, 1, 1);
		this.mTileAnswer3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_c.png", 0, 160, 1, 1);
		this.mTileAnswer4 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "menu_d.png", 0, 240, 1, 1);
		this.mTileNext = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "arrow_right.png", 0, 320, 1, 1);
		this.mTileBackground = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "tile_background.png", 0, 395, 1, 1);
		this.mTilePause = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "pause_icon.png", 0, 755, 1, 1);
		if (null != this.mBitmapTextureAtlas) {
			this.mBitmapTextureAtlas.load();
		}

		this.mBitmapTextureAtlasLife1 = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mBitmapTextureAtlasLife2 = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mBitmapTextureAtlasLife3 = new BitmapTextureAtlas(this.getTextureManager(), 32, 32, TextureOptions.BILINEAR);
		this.mTileLife1 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLife1, this, "heart_full.png", 0, 0, 1, 1);
		this.mTileLife2 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLife2, this, "heart_full.png", 0, 0, 1, 1);
		this.mTileLife3 = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLife3, this, "heart_full.png", 0, 0, 1, 1);
		if (null != this.mBitmapTextureAtlasLife1) {
			this.mBitmapTextureAtlasLife1.load();
		}
		if (null != this.mBitmapTextureAtlasLife2) {
			this.mBitmapTextureAtlasLife2.load();
		}
		if (null != this.mBitmapTextureAtlasLife3) {
			this.mBitmapTextureAtlasLife3.load();
		}

		this.mBitmapTextureAtlasPause = new BitmapTextureAtlas(this.getTextureManager(), 300, 300, TextureOptions.BILINEAR);
		this.mPausedTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(this.mBitmapTextureAtlasPause, this, "resume.png", 0, 0);
		if (null != this.mBitmapTextureAtlasPause) {
			this.mBitmapTextureAtlasPause.load();
		}

		try {
			this.mClickSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "tick.mp3");
			this.mRightAnswerSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "whoosh.mp3");
			this.mLifeGoneSound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "glassbreaking.mp3");
			this.mEmergencySound = SoundFactory.createSoundFromAsset(this.mEngine.getSoundManager(), this, "emergency.mp3");
			this.mEmergencySound.setLooping(true);
		} catch (final IOException e) {
			Log.d(TAG, e.getMessage());
		}
		
		final SharedPreferences sharedPreferences = QuickGameActivity.this.getSharedPreferences("savedPreferences", Context.MODE_PRIVATE);
		final int volume = sharedPreferences.getInt("VOLUME",10);
		final boolean isVolumeOn = sharedPreferences.getBoolean("ISVOLUMEON", true);
		if (isVolumeOn) {
			this.mClickSound.setVolume(volume * 0.1f);
			this.mRightAnswerSound.setVolume(volume * 0.1f);
			this.mLifeGoneSound.setVolume(volume * 0.1f);
			this.mEmergencySound.setVolume(volume * 0.1f);
		} else {
			this.mClickSound.setVolume(0 * 0.1f);
			this.mRightAnswerSound.setVolume(0 * 0.1f);
			this.mLifeGoneSound.setVolume(0 * 0.1f);
			this.mEmergencySound.setVolume(0 * 0.1f);
		}
	}

	@Override
	protected Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.scene = new Scene();
		this.mPauseScene = new CameraScene(this.camera);
		this.vertexBufferObjectManager = this.getVertexBufferObjectManager();
		
		onCreateSceneBackground();
		
		final Sprite sPausedSprite = new Sprite((Constants.CAMERA_WIDTH - this.mPausedTextureRegion.getWidth()) / 2, 
				(Constants.CAMERA_HEIGHT - this.mPausedTextureRegion.getHeight()) / 2,
				this.mPausedTextureRegion, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					scene.clearChildScene();
					scene.setIgnoreUpdate(false);
				}
				return true;
			}
		};
		this.mPauseScene.attachChild(sPausedSprite);
		this.mPauseScene.setBackgroundEnabled(true);
		
		this.mPauseScene.registerTouchArea(sPausedSprite);
		
		this.sTileBackground = new Sprite(0, 0, this.mTileBackground, this.vertexBufferObjectManager);
		this.sTileBackground.setHeight(this.mTileBackground.getHeight()*0.7f);
		this.sTileBackground.setWidth(this.mTileBackground.getWidth()*1.2f);
		this.sTileBackground.setPosition((Constants.CAMERA_WIDTH - this.sTileBackground.getWidth()) / 2, 160);
		this.scene.attachChild(this.sTileBackground);
		
		this.tInstruction = new Text(10, 45, this.mRavieFontInstruction, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		this.tQuestionNo = new Text(10, 5, this.mRavieFontInstruction, "AAAAAAA", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		this.tQuestion = new Text(10, 80, this.mRavieFontQuestion, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
				new TextOptions(AutoWrap.WORDS, Constants.CAMERA_WIDTH-50, HorizontalAlign.LEFT), this.vertexBufferObjectManager);
		this.tQuestion.setWidth(600);
		this.tPrompt = new Text(10, Constants.CAMERA_HEIGHT - 50, this.mRavieFontPrompt, "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA", 
				new TextOptions(AutoWrap.WORDS, Constants.CAMERA_WIDTH-200, HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		this.tClock = new Text(5, 5, this.mRavieFontClock, "10:00", new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		this.tClock.setPosition((Constants.CAMERA_WIDTH - tClock.getWidth()) / 2, 5);
		
		this.sTileNext = new Sprite(Constants.CAMERA_WIDTH - 100, Constants.CAMERA_HEIGHT - 80,
				this.mTileNext, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					this.setAlpha(0.5f);
					if (currentQuestion < Constants.QUICKGAMENOOFQUESTIONS && countLife > 0 && !isTimedOut){
						nextQuestion();
					} else {
						isNextPageClicked = true;
						startNextActivity();
					}
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		this.scene.registerUpdateHandler(new TimerHandler(1f, true, new ITimerCallback() {
	        @Override
	        public void onTimePassed(TimerHandler pTimerHandler) {
        		if (!isAnswered){
        			countTime--;
        			tClock.setText((countTime/60) + ":" + ((countTime%60) < 10 ? "0" + (countTime%60) : countTime%60));
        			if(countTime == 10) {
        				if (null != mEmergencySound) {
        					mEmergencySound.play();
    					}
        			}
        			if (wasPaused && countTime <= 10) {
        				if (null != mEmergencySound) {
        					mEmergencySound.resume();
    					}
        				wasPaused = false;
        			}
                    if(countTime == 0){
                    	scene.unregisterUpdateHandler(pTimerHandler);
                    	tPrompt.setVisible(true);
                    	tPrompt.setText("Sorry! Time out!!");
                    	if (null != mEmergencySound) {
                    		mEmergencySound.stop();
    					}
                    	tPrompt.setPosition((Constants.CAMERA_WIDTH - tPrompt.getWidth()) / 2, Constants.CAMERA_HEIGHT - 50);
                    	isAnswered = true;
                    	isTimedOut = true;
                		sTileNext.setVisible(true);
                     }
        		}
        		pTimerHandler.reset();
	        }
		}));
		
		Sprite sTilePause = new Sprite(Constants.CAMERA_WIDTH - 80, 5, this.mTilePause, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN) {
					if (null != mClickSound) {
						mClickSound.play();
					}
					scene.setChildScene(mPauseScene, false, true, true);
					if (null != mEmergencySound) {
						mEmergencySound.pause();
					}
					scene.setIgnoreUpdate(true);
					wasPaused = true;
				}
				return true;
			}
		};
		
		nextQuestion();

		this.scene.attachChild(this.tInstruction);
		this.scene.attachChild(this.tQuestionNo);
		this.scene.attachChild(this.tQuestion);
		this.scene.attachChild(this.sTileNext);
		this.scene.attachChild(this.tPrompt);
		this.scene.attachChild(this.tClock);
		this.scene.attachChild(sTilePause);
		
		this.scene.registerTouchArea(this.sTileNext);
		this.scene.registerTouchArea(sTilePause);
		
		return this.scene;
	}
	
	private void nextQuestion() {
		this.isAnswered = false;
		this.tPrompt.setVisible(false);
		this.sTileNext.setVisible(false);
		this.sTileBackground.detachChildren();
		
		QuestionData quesdata = new QuestionData();
		quesdata = this.mapQuestionBank.get(this.listQuestions.get(this.currentQuestion));
		final String instruction = Messages.INSTRUCTIONS[quesdata.getCategory()][quesdata.getSubCategory()];
		final String question = quesdata.getQuestion();
		final String answer1 = quesdata.getAnswer1();
		final String answer2 = quesdata.getAnswer2();
		final String answer3 = quesdata.getAnswer3();
		final String answer4 = quesdata.getAnswer4();
		final String correctAnswer = answer1;
		
		final ArrayList<String> listAnswer = new ArrayList<String>();
		listAnswer.add(answer1);
		listAnswer.add(answer2);
		listAnswer.add(answer3);
		listAnswer.add(answer4);
		Collections.shuffle(listAnswer);
		
		setTileLife();
		
		this.tInstruction.setText(instruction);
		this.tQuestionNo.setText(++this.currentQuestion + "/10");
		this.tQuestion.setText(question);
		
		final Sprite sTileAnswer1 = new Sprite(7, 10, this.mTileAnswer1, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && isAnswered == false) {
					this.setAlpha(0.7f);
					setPromtText(listAnswer.get(0), correctAnswer);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP && isAnswered == false) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Sprite sTileAnswer2 = new Sprite(7, 60, this.mTileAnswer2, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && isAnswered == false) {
					this.setAlpha(0.7f);
					setPromtText(listAnswer.get(1), correctAnswer);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP && isAnswered == false) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Sprite sTileAnswer3 = new Sprite(7, 110, this.mTileAnswer3, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && isAnswered == false) {
					this.setAlpha(0.7f);
					setPromtText(listAnswer.get(2), correctAnswer);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP && isAnswered == false) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Sprite sTileAnswer4 = new Sprite(7, 160, this.mTileAnswer4, this.vertexBufferObjectManager) {
			@Override
			public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_DOWN && isAnswered == false) {
					this.setAlpha(0.7f);
					setPromtText(listAnswer.get(3), correctAnswer);
				}
				if(pSceneTouchEvent.getAction() == MotionEvent.ACTION_UP && isAnswered == false) {
					this.setAlpha(1.0f);
				}
				return true;
			}
		};
		
		final Text tAnswer1 = new Text(150, 25, this.mRavieFontAnswer, listAnswer.get(0), new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer2 = new Text(150, 25, this.mRavieFontAnswer, listAnswer.get(1), new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer3 = new Text(150, 25, this.mRavieFontAnswer, listAnswer.get(2), new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		final Text tAnswer4 = new Text(150, 25, this.mRavieFontAnswer, listAnswer.get(3), new TextOptions(HorizontalAlign.CENTER), this.vertexBufferObjectManager);
		
		sTileAnswer1.setScale(Constants.SCALEFACTORHALF);
		sTileAnswer2.setScale(Constants.SCALEFACTORHALF);
		sTileAnswer3.setScale(Constants.SCALEFACTORHALF);
		sTileAnswer4.setScale(Constants.SCALEFACTORHALF);
		
		sTileAnswer1.attachChild(tAnswer1);
		sTileAnswer2.attachChild(tAnswer2);
		sTileAnswer3.attachChild(tAnswer3);
		sTileAnswer4.attachChild(tAnswer4);
		
		this.sTileBackground.attachChild(sTileAnswer1);
		this.sTileBackground.attachChild(sTileAnswer2);
		this.sTileBackground.attachChild(sTileAnswer3);
		this.sTileBackground.attachChild(sTileAnswer4);

		this.scene.registerTouchArea(sTileAnswer1);
		this.scene.registerTouchArea(sTileAnswer2);
		this.scene.registerTouchArea(sTileAnswer3);
		this.scene.registerTouchArea(sTileAnswer4);
	}
	
	private void setTileLife() {
		final Sprite sTileLife1 = new Sprite(Constants.CAMERA_WIDTH - 230, 5, this.mTileLife1, this.vertexBufferObjectManager);
		final Sprite sTileLife2 = new Sprite(Constants.CAMERA_WIDTH - 190, 5, this.mTileLife2, this.vertexBufferObjectManager);
		final Sprite sTileLife3 = new Sprite(Constants.CAMERA_WIDTH - 150, 5, this.mTileLife3, this.vertexBufferObjectManager);
		
		BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLife1, QuickGameActivity.this, this.countLife >= 3 ? "heart_full.png" : "heart_broken.png", 0, 0, 1, 1);
		BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLife2, QuickGameActivity.this, this.countLife >= 2 ? "heart_full.png" : "heart_broken.png", 0, 0, 1, 1);
		BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlasLife3, QuickGameActivity.this, this.countLife >= 1 ? "heart_full.png" : "heart_broken.png", 0, 0, 1, 1);
		
		this.scene.attachChild(sTileLife1);
		this.scene.attachChild(sTileLife2);
		this.scene.attachChild(sTileLife3);		
	}

	private void setPromtText(String answer, String correctAnswer) {
		CharSequence cs = "";
		this.tPrompt.setVisible(true);
		if (answer.equals(correctAnswer)) {
			cs = "Wow! Nice Hit!!";
			this.countCorrectAnswer++;
			if (null != this.mRightAnswerSound) {
				this.mRightAnswerSound.play();
			}
		} else {
			cs = "OOPS! Wrong!! Correct ans is: " + correctAnswer;
			this.countWrongAnswer++;
			this.countLife--;
			if (null != this.mLifeGoneSound) {
				this.mLifeGoneSound.play();
			}
			setTileLife();
		}
		this.tPrompt.setText(cs);
		this.tPrompt.setPosition((Constants.CAMERA_WIDTH-tPrompt.getWidth()) / 2, Constants.CAMERA_HEIGHT - 50);
		this.sTileNext.setVisible(true);
		this.isAnswered = true;
	}
	
	private void startNextActivity() {
		Intent intent = new Intent(QuickGameActivity.this, QuickGameScoreCardActivity.class);
		intent.putExtra("COUNTCORRECT", this.countCorrectAnswer);
		intent.putExtra("COUNTWRONG", this.countWrongAnswer);
		intent.putExtra("COUNTTIME", this.countTime);
		intent.putExtra("COUNTLIFE", this.countLife);
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
			new AlertDialog.Builder(QuickGameActivity.this)
				.setIcon(android.R.drawable.ic_dialog_alert)
				.setTitle(Messages.QUITTITLE)
				.setMessage(Messages.QUITMESSAGE)
				.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
		            public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(QuickGameActivity.this, QuickSelectCategoryActivity.class);
						startActivity(intent);
						finish();
						overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);   
		            }
		        })
		        .setNegativeButton("No", null)
		        .show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if(!this.isNextPageClicked) {
			this.scene.setChildScene(this.mPauseScene, false, true, true);
			this.scene.setIgnoreUpdate(true);
			if (null != this.mEmergencySound) {
				this.mEmergencySound.pause();
			}
			this.wasPaused = true;
		}
	}
	
	@Override
	public synchronized void onResumeGame() {
	    if (null != this.mEngine)
	        super.onResumeGame();
	}
}
