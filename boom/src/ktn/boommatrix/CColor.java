package ktn.boommatrix;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

public class CColor extends AnimatedSprite{

	
	private ColorTitle colorTitle;
	private int posX;
	private int posY;
	private boolean vanish;
	private int idColor;
	public CColor(int id, ColorTitle c,
					int pX, int pY, 
					float pWidth, float pHeight, 
					int marginX, int marginY,
					ITiledTextureRegion pTextureRegion,
					VertexBufferObjectManager pVertexBufferObjectManager) {
		
		super(marginX + (pWidth * pX), marginY + (pHeight * pY), 
				pWidth, pHeight, pTextureRegion, pVertexBufferObjectManager);
		setColorTitle(c);
		setCurrentTileIndex(c.value);
		setPosX(pX);
		setPosY(pY);
		this.setVanish(false);
		this.idColor = id;
	}
	
	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub
		if (this.getColorTitle() == ColorTitle.WHITE && 
				this.getCurrentTileIndex() == 17) {
//			this.setColorTitle(ColorTitle.WHITE);
			this.setCurrentTileIndex(this.getColorTitle().value);
		}
		if (this.getColorTitle() == ColorTitle.WHITE && 
				this.getCurrentTileIndex() == 11) {
//			this.setColorTitle(ColorTitle.WHITE);
			this.setCurrentTileIndex(this.getColorTitle().value);
		}
		super.onManagedUpdate(pSecondsElapsed);
	}
	
	public int getPosX() {
		return posX;
	}
	public void setPosX(int posX) {
		this.posX = posX;
	}
	public int getPosY() {
		return posY;
	}
	public void setPosY(int posY) {
		this.posY = posY;
	}

	public ColorTitle getColorTitle() {
		return colorTitle;
	}

	public void setColorTitle(ColorTitle colorTitle) {
		this.colorTitle = colorTitle;
	}
	
	public void setColorTitleIndex(ColorTitle colorTitle) {
		this.colorTitle = colorTitle;
		setCurrentTileIndex(this.colorTitle.value);
		this.setVanish(false);
		this.stopAnimation();
	}
	
	public boolean isVanish() {
		return vanish;
	}

	public void setVanish(boolean vanish) {
		this.vanish = vanish;
	}

	public int getIdColor() {
		return idColor;
	}

	public void setIdColor(int idColor) {
		this.idColor = idColor;
	}


	
}
