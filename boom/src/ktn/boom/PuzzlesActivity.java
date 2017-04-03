package ktn.boom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.RatioResolutionPolicy;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.Sprite;
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
import org.andengine.opengl.texture.region.TiledTextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import org.andengine.util.HorizontalAlign;
import org.andengine.util.debug.Debug;

import android.content.Intent;
import android.graphics.Typeface;

@SuppressWarnings("unused")
public class PuzzlesActivity extends SimpleBaseGameActivity {
	// ===========================================================
	// Constants
	// ===========================================================

	private static final int CAMERA_WIDTH = 480;
	private static final int CAMERA_HEIGHT = 800;

	// ===========================================================
	// Fields
	// ===========================================================

	private BuildableBitmapTextureAtlas mBitmapTextureAtlas;

	private TiledTextureRegion starTextureRegion;
	private TiledTextureRegion lockTextureRegion;
	private Font mFont;
	private RepeatingSpriteBackground mGridBackground;
	int expansion = 1;
	MySharedPreferences mySharedPreferences;
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
		final Camera camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);

		return new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED,
				new RatioResolutionPolicy(CAMERA_WIDTH, CAMERA_HEIGHT), camera);
	}

	@Override
	public void onCreateResources() {
		this.mFont = FontFactory.create(this.getFontManager(),
				this.getTextureManager(), 256, 256,
				Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 32);
		this.mFont.load();

		BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");

		this.mBitmapTextureAtlas = new BuildableBitmapTextureAtlas(this.getTextureManager(), 1024, 1024, TextureOptions.NEAREST);
		this.starTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "bg_star.png", 1, 1);
		this.lockTextureRegion = BitmapTextureAtlasTextureRegionFactory.createTiledFromAsset(this.mBitmapTextureAtlas, this, "bg_star_lock.png", 1, 1);
		 this.mGridBackground = new RepeatingSpriteBackground(CAMERA_WIDTH,
 				CAMERA_HEIGHT, this.getTextureManager(),
 				AssetBitmapTextureAtlasSource.create(this.getAssets(),
 						"gfx/grid.png"), this.getVertexBufferObjectManager());
		try {
			this.mBitmapTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 0, 1));
			this.mBitmapTextureAtlas.load();
		} catch (TextureAtlasBuilderException e) {
			Debug.e(e);
		}
	}

	@Override
	public Scene onCreateScene() {
		this.expansion = this.getIntent().getIntExtra("expansion", 1);
		this.mEngine.registerUpdateHandler(new FPSLogger());

		final Scene scene = new Scene();
		scene.setBackground(this.mGridBackground);
		mySharedPreferences = new MySharedPreferences(this);
		 if ( mySharedPreferences.getLevel() == 0)
	        {
	        	mySharedPreferences.setLevel(1);
	        }
		final int maxlv = mySharedPreferences.getLevel();
		
		for (int j = 0; j < 4; j++) {
			for (int i = 0; i < 3; i++) {
				final int lv = (i + 1) + j * 3 + (expansion - 1) * 12;
				final Sprite face;
				if (lv <= maxlv) {
					face = new Sprite(140 * i + 60, 140 * j + 240,
							this.starTextureRegion,
							this.getVertexBufferObjectManager()) {
						@Override
						public boolean onAreaTouched(TouchEvent pEvent,
								float pX, float pY) {
							if (pEvent.isActionDown()) {
								Intent mIntent = new Intent(PuzzlesActivity.this, TouchDragExample.class);
					        	mIntent.putExtra("level", lv);
					        	PuzzlesActivity.this.startActivity(mIntent);
								return true;
							} else {
								return false;
							}
						}
					};

					scene.registerTouchArea(face);
					scene.attachChild(face);	
					final Text textlv = new Text(140 * i + 60 + 30, 140 * j + 240 + 30,
							this.mFont, String.valueOf(lv), new TextOptions(
									HorizontalAlign.RIGHT),
							this.getVertexBufferObjectManager());
					scene.attachChild(textlv);

				} else {
					face = new Sprite(140 * i + 60, 140 * j + 240,
							this.lockTextureRegion,
							this.getVertexBufferObjectManager());
					scene.attachChild(face);	
				}
				
			}
		}

		return scene;
	}

	// ===========================================================
	// Methods
	// ===========================================================
	
	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================
}
