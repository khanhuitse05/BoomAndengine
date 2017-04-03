package ktn.boom;

import java.util.ArrayList;
import java.util.List;
   
import org.andengine.engine.camera.Camera;
import org.andengine.engine.options.EngineOptions;
import org.andengine.engine.options.ScreenOrientation;
import org.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.andengine.entity.primitive.Rectangle;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.RepeatingSpriteBackground;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.util.FPSLogger;
import org.andengine.input.touch.TouchEvent;
import org.andengine.input.touch.detector.ClickDetector;
import org.andengine.input.touch.detector.ClickDetector.IClickDetectorListener;
import org.andengine.input.touch.detector.ScrollDetector;
import org.andengine.input.touch.detector.ScrollDetector.IScrollDetectorListener;
import org.andengine.input.touch.detector.SurfaceScrollDetector;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.source.AssetBitmapTextureAtlasSource;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.texture.region.TextureRegion;
import org.andengine.ui.activity.SimpleBaseGameActivity;
import android.content.Intent;
import android.widget.Toast;


public class MenuScrollerActivity extends SimpleBaseGameActivity implements IScrollDetectorListener, IOnSceneTouchListener, IClickDetectorListener {
       
        // ===========================================================
        // Constants
        // ===========================================================
        protected static int CAMERA_WIDTH = 480;
        protected static int CAMERA_HEIGHT = 800;
 
        protected static int FONT_SIZE = 24;
        protected static int PADDING = 50;
        
        protected static int MENUITEMS = 7;
        
 
        // ===========================================================
        // Fields
        // ===========================================================
        private Scene mScene;
        private Camera mCamera;
         
        private BitmapTextureAtlas mMenuTextureAtlas;        
        private ITextureRegion mMenuLeftTextureRegion;
        private ITextureRegion mMenuRightTextureRegion;

    	private RepeatingSpriteBackground mGridBackground;
        
        private Sprite menuleft;
        private Sprite menuright;
 
        // Scrolling
        private SurfaceScrollDetector mScrollDetector;
        private ClickDetector mClickDetector;
 
        private float mMinX = 0;
        private float mMaxX = 0;
        private float mCurrentX = 0;
        private int iItemClicked = -1;
        
        private Rectangle scrollBar;        
        private List<TextureRegion> columns = new ArrayList<TextureRegion>();

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
		protected void onCreateResources() {
                // Paths
                FontFactory.setAssetBasePath("font/");
                BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("gfx/");               
                
                //Images for the menu
                for (int i = 0; i < MENUITEMS; i++) {				
                	BitmapTextureAtlas mMenuBitmapTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 512,512, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
            		ITextureRegion mMenuTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuBitmapTextureAtlas, this, "menu"+i+".png", 0, 0);
                	
                	this.mEngine.getTextureManager().loadTexture(mMenuBitmapTextureAtlas);
                	columns.add((TextureRegion) mMenuTextureRegion);
                	
                	
                }
                //Textures for menu arrows
                this.mMenuTextureAtlas = new BitmapTextureAtlas(this.getTextureManager(), 128,128, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
                this.mMenuLeftTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuTextureAtlas, this, "menu_left.png", 0, 0);
                this.mMenuRightTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mMenuTextureAtlas, this, "menu_right.png",32, 0);
                this.mEngine.getTextureManager().loadTexture(mMenuTextureAtlas); 
          
                this.mGridBackground = new RepeatingSpriteBackground(CAMERA_WIDTH,
        				CAMERA_HEIGHT, this.getTextureManager(),
        				AssetBitmapTextureAtlasSource.create(this.getAssets(),
        						"gfx/grid.png"), this.getVertexBufferObjectManager());
        		
        }
 
        @Override
		public EngineOptions onCreateEngineOptions() {
                this.mCamera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
    			final EngineOptions engineOptions = new EngineOptions(true, ScreenOrientation.PORTRAIT_FIXED, new FillResolutionPolicy(), this.mCamera);
    			engineOptions.getTouchOptions().setNeedsMultiTouch(true);
    			
    			return engineOptions;
        }
 
        @Override
		protected Scene onCreateScene() {
                this.mEngine.registerUpdateHandler(new FPSLogger());
 
                this.mScene = new Scene();
                this.mScene.setBackground(this.mGridBackground);
               
                this.mScrollDetector = new SurfaceScrollDetector(this);
                this.mClickDetector = new ClickDetector(this);
 
                this.mScene.setOnSceneTouchListener(this);
                this.mScene.setTouchAreaBindingOnActionDownEnabled(true);
                this.mScene.setTouchAreaBindingOnActionMoveEnabled(true);
                this.mScene.setOnSceneTouchListenerBindingOnActionDownEnabled(true);
 
                CreateMenuBoxes();
 
                return this.mScene;
 
        }
 
        @Override
        public boolean onSceneTouchEvent(final Scene pScene, final TouchEvent pSceneTouchEvent) {
                this.mClickDetector.onTouchEvent(pSceneTouchEvent);
                this.mScrollDetector.onTouchEvent(pSceneTouchEvent);
                return true;
        }
 
        @Override
		public void onScroll(ScrollDetector pScollDetector, int pPointerID, float pDistanceX, float pDistanceY) {

        		//Disable the menu arrows left and right (15px padding)
	        	if(mCamera.getXMin()<=15)
	             	menuleft.setVisible(false);
	             else
	             	menuleft.setVisible(true);
	        	 
	        	 if(mCamera.getXMin()>mMaxX-15)
		             menuright.setVisible(false);
		         else
		        	 menuright.setVisible(true);
	             	
                //Return if ends are reached
                if ( ((mCurrentX - pDistanceX) < mMinX)  ){                	
                    return;
                }else if((mCurrentX - pDistanceX) > mMaxX){
                	
                	return;
                }
                
                //Center camera to the current point
                this.mCamera.offsetCenter(-pDistanceX,0 );
                mCurrentX -= pDistanceX;
                	
               
                //Set the scrollbar with the camera
                float tempX =mCamera.getCenterX()-CAMERA_WIDTH/2;
                // add the % part to the position
                tempX+= (tempX/(mMaxX+CAMERA_WIDTH))*CAMERA_WIDTH;      
                //set the position
                scrollBar.setPosition(tempX, scrollBar.getY());
                
                //set the arrows for left and right
                menuright.setPosition(mCamera.getCenterX()+CAMERA_WIDTH/2-menuright.getWidth(),menuright.getY());
                menuleft.setPosition(mCamera.getCenterX()-CAMERA_WIDTH/2,menuleft.getY());
                
              
                
                //Because Camera can have negativ X values, so set to 0
            	if(this.mCamera.getXMin()<0){
            		this.mCamera.offsetCenter(0,0 );
            		mCurrentX=0;
            	}
            	
 
        }
 
        @Override
		public void onClick(ClickDetector pClickDetector, int pPointerID, float pSceneX, float pSceneY) {
                loadLevel(iItemClicked);
        };
 
        // ===========================================================
        // Methods
        // ===========================================================
        
        private void CreateMenuBoxes() {
        	
             int spriteX = PADDING;
        	 int spriteY = 300;
        	 
        	 //current item counter
             int iItem = 1;

        	 for (int x = 0; x < columns.size(); x++) {
        		 
        		 //On Touch, save the clicked item in case it's a click and not a scroll.
                 final int itemToLoad = iItem;
        		 
        		 Sprite sprite = new Sprite(spriteX,spriteY,(ITextureRegion)columns.get(x), this.getVertexBufferObjectManager()){
        			 
        			 public boolean onAreaTouched(final TouchEvent pSceneTouchEvent, final float pTouchAreaLocalX, final float pTouchAreaLocalY) {
                         iItemClicked = itemToLoad;
                         return false;
        			 }        			 
        		 };        		 
        		 iItem++;
        		
        		 
        		 this.mScene.attachChild(sprite);        		 
        		 this.mScene.registerTouchArea(sprite);        		 
   
        		 spriteX += 20 + PADDING+sprite.getWidth();
			}
        	
        	 mMaxX = spriteX - CAMERA_WIDTH;
        	 
        	 //set the size of the scrollbar
        	 float scrollbarsize = CAMERA_WIDTH/((mMaxX+CAMERA_WIDTH)/CAMERA_WIDTH);
        	 scrollBar = new Rectangle(0,CAMERA_HEIGHT-20,scrollbarsize, 20, this.getVertexBufferObjectManager());
        	 scrollBar.setColor(1,0,0);
        	 this.mScene.attachChild(scrollBar);
        	 
        	 menuleft = new Sprite(0,CAMERA_HEIGHT/2-mMenuLeftTextureRegion.getHeight()/2,mMenuLeftTextureRegion, this.getVertexBufferObjectManager());
        	 menuright = new Sprite(CAMERA_WIDTH-mMenuRightTextureRegion.getWidth(),CAMERA_HEIGHT/2-mMenuRightTextureRegion.getHeight()/2,mMenuRightTextureRegion, this.getVertexBufferObjectManager());
        	 this.mScene.attachChild(menuright);
        	 menuleft.setVisible(false);
        	 this.mScene.attachChild(menuleft);
        }
        
        
 
        //Here is where you call the item load.
        private void loadLevel(final int iLevel) {
                if (iLevel != -1) {
                	if (iLevel == 1 || iLevel == 2) {
                		Intent mIntent2 = new Intent(this, PuzzlesActivity.class);
                    	mIntent2.putExtra("expansion", iLevel);
                    	this.startActivity(mIntent2);
					}else{
                        runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                			
                                        Toast.makeText(MenuScrollerActivity.this, "<-Not Available->", Toast.LENGTH_SHORT).show();
                                        iItemClicked = -1;
                                }
                        });
					}
                }
        }


		@Override
		public void onScrollStarted(ScrollDetector pScollDetector,
				int pPointerID, float pDistanceX, float pDistanceY) {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void onScrollFinished(ScrollDetector pScollDetector,
				int pPointerID, float pDistanceX, float pDistanceY) {
			// TODO Auto-generated method stub
			
		}


}
 