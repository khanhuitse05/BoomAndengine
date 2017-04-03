package ktn.boom;

import ktn.control.ValueControl;
import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPreferences {
	
	SharedPreferences.Editor editor;

	SharedPreferences my_share;
	public final String PREFS_NAME = "MyPrefs";
	public final String NameLevel = "level";

	public MySharedPreferences(Context mContext) {
		my_share = mContext.getSharedPreferences(PREFS_NAME, 0);
		editor = my_share.edit();
	}

	/**
	 * quản lý nhạc nền
	 */
	public void getIsMusic() {
		ValueControl.isMusic = my_share.getBoolean("isMusic", true);
	}

	/**
	 * Quản lý âm thanh
	 */
	public void getIsSound() {
		ValueControl.isSound = my_share.getBoolean("isSound", true);
	}

	/**
	 * Level hiện tại
	 */
	public int getLevel() {
		return  my_share.getInt(NameLevel, 0 );
	}

	

	/**
	 * Cập nhật trạng thái bật tắt nhạc nền
	 * 
	 * @param isMusic
	 */
	public void updateIsMusic(boolean isMusic) {
		ValueControl.isMusic = isMusic;
		editor.putBoolean("isMusic", isMusic);
		editor.commit();
	}

	/**
	 * cập nhật trạng thái bật tắt âm thanh
	 * 
	 * @param isSound
	 */
	public void updateIsSound(boolean isSound) {
		ValueControl.isSound = isSound;
		editor.putBoolean("isSound", isSound);
		editor.commit();
	}

	public void setLevel(int index)
	{
		editor.putInt(NameLevel, index);
		editor.commit();
	}

	

}