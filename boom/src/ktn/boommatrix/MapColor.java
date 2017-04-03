package ktn.boommatrix;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import ktn.boom.Menu;

import org.andengine.opengl.texture.region.ITiledTextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import android.content.Context;

public class MapColor {
	private static MapColor INSTANCE = null;
	private Context context;
	
	private int posPX;
	private int posPY;
	private int level;
	public List<CColor> listColor;
	ITiledTextureRegion pTextureRegion;
	VertexBufferObjectManager pVertexBufferObjectManager;
	
//	public static Map getInstance(Context context , ITextureRegion textureRegion,
//			ISpriteVertexBufferObject spriteVertexBufferObject) {
//		if (INSTANCE == null) {
//			INSTANCE = new Map(context, textureRegion, spriteVertexBufferObject);
//			return INSTANCE;
//		} else {
//			return INSTANCE;
//		}
//	}
	
	public MapColor(Context context, ITiledTextureRegion textureRegion,
			VertexBufferObjectManager vertexBufferObjectManager) {
		this.context = context;
		this.pTextureRegion = textureRegion;
		this.pVertexBufferObjectManager = vertexBufferObjectManager;
	}
	//tao map game
	public boolean setMap(int lv){
		listColor = new ArrayList<CColor>();
		String path = "tmx/map_"+lv;
		int[] aTam = getMap(path).clone();
		try {
			for (int i = 0; i < aTam.length; i++) {
				if (aTam[i] == ColorTitle.PLAYER.value) {
					CColor colorTam = new CColor(i, ColorTitle.YELLOW, 
													i%7, i/7,
													64, 64, 
													16, 208,
													pTextureRegion, pVertexBufferObjectManager);
					listColor.add(colorTam);
					setPosPX(i%7);
					setPosPY(i/7);
				}else {
					CColor colorTam = new CColor(i, ColorTitle.values()[aTam[i]],
													i%7, i/7,
													64, 64, 
													16, 208, 
													pTextureRegion, pVertexBufferObjectManager);
					listColor.add(colorTam);
				}
			}
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	private int[] getMap(String fileName) {
		return getMap(fileName, ",");
	}
	// Get map
	private int[] getMap(String fileName, String splitToken) {
		//tao mang kieu String chua cac gia tri cua map
		String[] sMapData = getData(fileName).split(splitToken);
		// tao mang resul co so phan tu la chieu dai mang sMapData
		int[] result = new int[sMapData.length];
		// Chuyen mang tu kieu String sang kieu Int luu vao mang result  
		for (int i = 0; i < sMapData.length; i++) {
			result[i] = Integer.parseInt(sMapData[i]);
		}
		
		return result;
	}
	// read file and wirte to a String
	private String getData(String fileName) {
		String result = null;
		
		try {
			InputStream in = context.getAssets().open(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			
			StringBuilder temp = new StringBuilder();
			String s;
			while ((s = br.readLine()) != null) {
				temp.append(s);
			}
			
			result = temp.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	public int getPosPX() {
		return posPX;
	}
	public void setPosPX(int posPX) {
		this.posPX = posPX;
	}
	public int getPosPY() {
		return posPY;
	}
	public void setPosPY(int posPY) {
		this.posPY = posPY;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public ColorTitle getColor(int id) {
		for (CColor item : listColor) {
			if (item.getIdColor() == id) {
				return item.getColorTitle();
			}
		}
		return null;
	}
	//kiem tra bommer dang dung tren o nao
	public ColorTitle getColor(int x, int y) {
		for (CColor item : listColor) {
			if (item.getPosX() == x && item.getPosY() == y) {
				return item.getColorTitle();
			}
		}
		return null;
	}
	// ham nay de active bom
	public void ActivityBomb(int x, int y){
		for (CColor item : listColor) {
			if (item.getPosX() == x && item.getPosY() == y) {
				if (item.getColorTitle() == ColorTitle.BOMB) {
					if (Menu.mSound != null)
						Menu.mSound.playImpactmic();
					item.setColorTitle(ColorTitle.BOOM);
					item.animate(new long[] {160, 160 }, 4, 5, true);
					ActivityBomb(x-1, y);
					ActivityBomb(x+1, y);
					ActivityBomb(x, y-1);
					ActivityBomb(x, y+1);
				}
			}
		}
	}
	// no bom chuyen sang o mau trang
	public void Explosion(){
		for (CColor item : listColor) {
			if (item.getColorTitle() == ColorTitle.BOOM) {
				if (Menu.mSound != null)
					Menu.mSound.playBomb();
					item.setColorTitle(ColorTitle.WHITE);
					item.animate(new long[] {260, 260, 260, 260, 260, 260 }, 12, 17, false);
					
			}
		}
	}
	// vanish 
	public void Vanish (){
		for (CColor item : listColor) {
			if (item.isVanish() == true &&
					item.getColorTitle() == ColorTitle.PINK) {
					if (Menu.mSound != null)
						Menu.mSound.playThunder();
					item.setColorTitle(ColorTitle.WHITE);
					item.animate(new long[] {260, 260, 260, 260, 260, 260 }, 6, 11, false);
			}
		}
	}
}