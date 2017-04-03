package ktn.boommatrix;

import java.util.List;

import org.andengine.entity.sprite.AnimatedSprite;
import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

// lop nhan vat
public class Characters extends AnimatedSprite {

	private StateCharacters stateCharacters;
	private StateCharacters preState;
	private ColorTitle colorCharacters;
	private float velocity;
	private int posX;
	private int posY;
	private float oldPosX;
	private float oldPosY;
	private final int MaxDistance = 64;
	private float marginX;
	private float marginY;
	private float titleWidth;
	private float titleHeight;
	private float poWidth;
	private float poHeight;
	private boolean isDie;

	public Characters(int pX, int pY, float pWidth, float pHeight,
			float tWidth, float tHeight, int mX, int mY,
			ITiledTextureRegion pTiledTextureRegion,
			VertexBufferObjectManager pVertexBufferObjectManager) {
		super(mX + (tWidth * pX) + (tWidth - pWidth) / 2, mY + (tHeight * pY)
				+ (tHeight - pHeight) / 2, pWidth, pHeight,
				pTiledTextureRegion, pVertexBufferObjectManager);
		this.setStateCharacters(StateCharacters.stop);
		this.velocity = 3;
		this.setPosX(pX);
		this.setPosY(pY);
		this.marginX = mX;
		this.marginY = mY;
		this.titleWidth = tWidth;
		this.titleHeight = tHeight;
		this.poWidth = pWidth;
		this.poHeight = pHeight;
		this.oldPosX = this.getX();
		this.oldPosY = this.getY();
		setColorCharacters(ColorTitle.YELLOW);
		this.isDie = false;
	}

	/*
	 * check character move or activity bomb
	 */
	private boolean CheckTouchCharacters(float x, float y) {
		int tamX;
		int tamY;
		tamX = (int) (x - this.marginX);
		tamY = (int) (y - this.marginY);
		tamX = (int) (tamX/titleWidth);
		tamY = (int) (tamY/titleHeight);
		if (tamX == this.getPosX() && tamY == getPosY()) {
			return true;
		}
		return false;
	}

	/*
	 * Slide
	 */
	public void Slide() {
		// move
		switch (preState) {
		case stop:
			break;
		case up:
			this.setStateCharacters(StateCharacters.up);
			this.animate(new long[] { 30, 30, 30, 30, 30 }, 10, 14, true);
			preState = StateCharacters.up;
			setPosY(getPosY() - 1);
			break;
		case left:
			this.setStateCharacters(StateCharacters.left);
			this.animate(new long[] { 30, 30, 30, 30, 30 }, 15, 19, true);
			preState = StateCharacters.left;
			setPosX(getPosX() + 1);
			break;
		case right:
			this.setStateCharacters(StateCharacters.right);
			this.animate(new long[] { 30, 30, 30, 30, 30 }, 5, 9, true);
			preState = StateCharacters.right;
			setPosX(getPosX() - 1);
			break;
		case down:
			this.setStateCharacters(StateCharacters.down);
			this.animate(new long[] { 30, 30, 30, 30, 30 }, 0, 4, true);
			preState = StateCharacters.down;
			setPosY(getPosY() + 1);
			break;
		default:
			break;
		}

	}

	/*
	 * handling click character move or activity bomb
	 */
	public boolean CheckMove(float x, float y) {
		if (this.getStateCharacters() == StateCharacters.stop) {
			float tX = x - (this.getX() + (getWidth() / 2));
			float tY = y - (this.getY() + (getHeight() / 2));
			if (CheckTouchCharacters(x, y) == true) {
				return false;
			} else {
				if (Math.abs(tY) < Math.abs(tX)) {
					// move left
					if (tX > 0) {
						this.setStateCharacters(StateCharacters.left);
						this.animate(new long[] { 30, 30, 30, 30, 30 }, 15, 19,
								true);
						preState = StateCharacters.left;
						setPosX(getPosX() + 1);
					}
					// move right
					else {
						this.setStateCharacters(StateCharacters.right);
						this.animate(new long[] { 30, 30, 30, 30, 30 }, 5, 9,
								true);
						preState = StateCharacters.right;
						setPosX(getPosX() - 1);
					}
				} else {
					// move down
					if (tY > 0) {
						this.setStateCharacters(StateCharacters.down);
						this.animate(new long[] { 30, 30, 30, 30, 30 }, 0, 4,
								true);
						preState = StateCharacters.down;
						setPosY(getPosY() + 1);
					}
					// move up
					else {
						this.setStateCharacters(StateCharacters.up);
						this.animate(new long[] { 30, 30, 30, 30, 30 }, 10, 14,
								true);
						preState = StateCharacters.up;
						setPosY(getPosY() - 1);
					}
				}
				return true;
			}
		}
		return true;
	}

	private void Move() {
		// move
		switch (getStateCharacters()) {
		case stop:
			break;
		case up:
			this.setPosition(this.getX(), this.getY() - velocity);
			if (this.getY() <= oldPosY - MaxDistance) {
				this.setPosition(this.getX(), oldPosY - MaxDistance);
				setStateCharacters(StateCharacters.stop);
				this.stopAnimation(10);
				oldPosX = this.getX();
				oldPosY = this.getY();
			}
			break;
		case left:
			this.setPosition(this.getX() + velocity, this.getY());
			if (this.getX() >= oldPosX + MaxDistance) {
				this.setPosition(oldPosX + MaxDistance, this.getY());
				setStateCharacters(StateCharacters.stop);
				this.stopAnimation(15);
				oldPosX = this.getX();
				oldPosY = this.getY();
			}
			break;
		case right:
			this.setPosition(this.getX() - velocity, this.getY());
			if (this.getX() <= oldPosX - MaxDistance) {
				this.setPosition(oldPosX - MaxDistance, this.getY());
				setStateCharacters(StateCharacters.stop);
				this.stopAnimation(5);
				oldPosX = this.getX();
				oldPosY = this.getY();
			}
			break;
		case down:
			this.setPosition(this.getX(), this.getY() + velocity);
			if (this.getY() >= oldPosY + MaxDistance) {
				this.setPosition(this.getX(), oldPosY + MaxDistance);
				setStateCharacters(StateCharacters.stop);
				this.stopAnimation(0);
				oldPosX = this.getX();
				oldPosY = this.getY();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onManagedUpdate(float pSecondsElapsed) {
		// TODO Auto-generated method stub

		Move();
		super.onManagedUpdate(pSecondsElapsed);
	}

	public ColorTitle WhatColor(List<CColor> list) {
		for (CColor item : list) {
			if (item.getPosX() == this.getPosX()
					&& item.getPosY() == this.getPosY()) {
				return item.getColorTitle();
			}
		}
		return null;
	}

	public StateCharacters getStateCharacters() {
		return stateCharacters;
	}

	public void setStateCharacters(StateCharacters stateCharacters) {
		this.stateCharacters = stateCharacters;
	}

	public boolean isDie() {
		return isDie;
	}

	public void setDie(boolean isDie) {
		this.isDie = isDie;
	}

	public StateCharacters getPreState() {
		return preState;
	}

	public void setPreState(StateCharacters preState) {
		this.preState = preState;
	}

	public ColorTitle getColorCharacters() {
		return colorCharacters;
	}

	public void setColorCharacters(ColorTitle colorCharacters) {
		this.colorCharacters = colorCharacters;
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

	public void setAllPosX(int posX) {
		this.posX = posX;
		this.setX(marginX + (titleWidth * posX) + (titleWidth - poWidth) / 2);
		oldPosX = this.getX();
	}
	public void setAllPosY(int posY) {
		this.posY = posY;
		this.setY( marginY + (titleHeight * posY) + (titleHeight - poHeight) / 2);
		oldPosY = this.getY();
	}
}
