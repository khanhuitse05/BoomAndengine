package ktn.boom;


import java.util.List;

import ktn.boommatrix.CColor;
import ktn.boommatrix.Characters;
import ktn.boommatrix.ColorTitle;
import ktn.boommatrix.MapColor;
import ktn.boommatrix.StateCharacters;
import ktn.dialog.DialogCompleted;
import ktn.dialog.DialogGameOver;
import ktn.dialog.DialogPause;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.handler.IUpdateHandler;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import android.content.Intent;
import android.graphics.Typeface;

/**
 * (c) 2010 Nicolas Gramlich (c) 2011 Zynga
 * 
 * @author Nicolas Gramlich
 * @since 15:13:46 - 15.06.2010
 */
public class TouchDragExample extends SimpleBaseGameActivity implements IOnMenuItemClickListener {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;
	private Camera camera;
	private Scene scene; 
	private MenuScene menuScene;
	// ===========================================================
	// Fields
	// ===========================================================

	private ITextureRegion texHome;

	private RepeatingSpriteBackground mGridBackground;
	private BuildableBitmapTextureAtlas bitmapTextureAtlas;
	private TiledTextureRegion texPlayer;
	private ITiledTextureRegion texColor;
	private MapColor map;
	private int level;
	private Characters face;
	private Font mFont;
	MySharedPreferences mySharedPreferences;
	Boolean isGameOver = false;
	// ===========================================================
	// Constructors
	// ===========================================================

	// ===========================================================
	// Getter & Setter
	// ===========================================================

	// ===========================================================
	// Methods for/from SuperClass/Interfaces
	// ===========================================================

	
	@Override
	public EngineOptions onCreateEngineOptions() {
		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}


	@Override
	public void onCreateResources() {
        mySharedPreferences = new MySharedPreferences(this);
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();
		
		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");
		this.mGridBackground = new RepeatingSpriteBackground(CAMERA_WIDTH,
				CAMERA_HEIGHT, this.getTextureManager(),
				AssetBitmapTextureAtlasSource.create(this.getAssets(),
						"gfx/grid.png"), this.getVertexBufferObjectManager());

		this.bitmapTextureAtlas = new BuildableBitmapTextureAtlas(
				this.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
		this.texPlayer = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.bitmapTextureAtlas, this,
						"bomber.png", 10, 2);
		this.texColor = BitmapTextureAtlasTextureRegionFactory
				.createTiledFromAsset(this.bitmapTextureAtlas, this,
						"color.png", 6, 3);

		texHome = BitmapTextureAtlasTextureRegionFactory.createFromAsset(
				bitmapTextureAtlas, getAssets(), "home.png");
		try {
			this.bitmapTextureAtlas
					.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource,
							BitmapTextureAtlas>( 0, 0, 0));
			this.bitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}

	}

	@Override
	public Scene onCreateScene() {
		this.mEngine.registerUpdateHandler(new FPSLogger());

		scene = new Scene();
		scene.setBackground(this.mGridBackground);
		///////////////// menu
		menuScene = new MenuScene(camera);
		final IMenuItem btnhome = new ScaleMenuItemDecorator(
				new SpriteMenuItem(1, 64, 64, texHome,
						getVertexBufferObjectManager()), 1.2f, 1);
		btnhome.setPosition(208, 680);
		menuScene.addMenuItem(btnhome);
		menuScene.setOnMenuItemClickListener(this);
		menuScene.setBackgroundEnabled(false);

		/////////////////
		map = new MapColor(this, texColor, this.getVertexBufferObjectManager());
		this.level = this.getIntent().getIntExtra("level", 1);
		map.setMap(level);

		for (CColor item : map.listColor) {
			if (item.getColorTitle() != ColorTitle.WHITE) {
				scene.attachChild(item);
			}
		}
		/* Quickly twinkling face. */
		face = new Characters(map.getPosPX(), map.getPosPY(),
				32, 32, 64, 64, 16, 208, this.texPlayer,
				this.getVertexBufferObjectManager());
		scene.attachChild(face);
		scene.setTouchAreaBindingOnActionDownEnabled(true);

		// draw level
		final Text textlv = new Text(84, 116,
				this.mFont, "LEVEL " + level, new TextOptions(
						HorizontalAlign.RIGHT),
				this.getVertexBufferObjectManager());
		scene.attachChild(textlv);
		// screen
		scene.registerUpdateHandler(new IUpdateHandler() {                    
            public void reset() {        
            }
            // loop game
            public void onUpdate(float pSecondsElapsed) {
        		ColorTitle titleTam = GetColorCharacters(face, map.listColor);
            	// check win
            	if (CheckWin() == true
            			&& (titleTam != ColorTitle.WHITE && titleTam != ColorTitle.BLUE )) {            		
            		// write file
            		mEngine.stop();
					runOnUiThread(new Runnable() {
				        public void run() {
				        	DialogCompleted dialog = new DialogCompleted(TouchDragExample.this);
				        	dialog.show();
				        }
				    });
				}            	
            	// check move character
            	if (face.getStateCharacters() == StateCharacters.stop) {
            		switch (titleTam) {
					case WHITE:
						mEngine.stop();
						runOnUiThread(new Runnable() {
					        public void run() {
					        	DialogGameOver dialog = new DialogGameOver(TouchDragExample.this);
					        	dialog.show();
					        }
					    });
						break;
						
					case BLUE:
						face.Slide();
						break;
					case PINK:
						for (CColor item : map.listColor) {
							if (item.getPosX() == face.getPosX() &&
									item.getPosY() == face.getPosY()) {
								if (item.getColorTitle() == ColorTitle.PINK) {
									item.setVanish(true);
								}
							}
						}
						break;

					default:
						break;
					}
				}
            	
            }
        });
		scene.setOnSceneTouchListener(new IOnSceneTouchListener() {

			@Override
			public boolean onSceneTouchEvent(Scene pScene,
					final TouchEvent pSceneTouchEvent) {
				runOnUpdateThread(new Runnable() {
					@Override
					public void run() {
						if (pSceneTouchEvent.getAction() == TouchEvent.ACTION_DOWN) {
							if (face.getStateCharacters() == StateCharacters.stop) {
								if (face.CheckMove(pSceneTouchEvent.getX(), pSceneTouchEvent.getY()) == false) {
									map.ActivityBomb(face.getPosX(), face.getPosY());									
								}else {
									map.Explosion();
									map.Vanish();
								}
							}
						}
					}
				});
				
				return false;
			}
		});

		scene.setChildScene(menuScene);
		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	private boolean CheckWin(){
		for (CColor item : map.listColor) {
			if (item.getColorTitle() == ColorTitle.BOMB ||
					item.getColorTitle() == ColorTitle.BOOM) {
				return false;
			}
		}
		if (face.getStateCharacters() == StateCharacters.stop) {
			if ( mySharedPreferences.getLevel() == level)
	        {
	        	mySharedPreferences.setLevel(level+1);
	        }
			return true;			
		}
		return false;
	}
	
	private ColorTitle GetColorCharacters(Characters characters, List<CColor> listColor){
		for (CColor item : listColor) {
			if (item.getPosX() == characters.getPosX() &&
					item.getPosY() == characters.getPosY()) {
				return item.getColorTitle();
			}
		}
		return ColorTitle.WHITE;
	}
	
	public void resume()
	{
		mEngine.start();
	}
	public void resetGame()
	{
		MapColor mapTam = new MapColor(this, texColor, this.getVertexBufferObjectManager());
		mapTam.setMap(level);
		face.setAllPosX(mapTam.getPosPX());
		face.setAllPosY(mapTam.getPosPY());
		for (CColor item : map.listColor) {
			item.setColorTitleIndex(mapTam.getColor(item.getIdColor()));
		}
	}
	public void nextLevel()
	{
		this.finish();
		Intent mIntent = new Intent(this, TouchDragExample.class);
    	mIntent.putExtra("level", level + 1);    	
    	this.startActivity(mIntent);
	}
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================


	@Override
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem,
			float pMenuItemLocalX, float pMenuItemLocalY) {
		// TODO Auto-generated method stub
		switch (pMenuItem.getID()) {
		case 1:
			if (Menu.mSound != null)
				Menu.mSound.playHarp();
			runOnUiThread(new Runnable() {
		        public void run() {
		        	TouchDragExample.this.mEngine.stop();
		        	DialogPause dialog = new DialogPause(TouchDragExample.this);
		        	dialog.show();
		        }
		    });			

		default:
			break;
		}
		return false;
	}
}
