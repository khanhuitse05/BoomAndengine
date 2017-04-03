package ktn.boom;

import com.example.boom.R;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import ktn.config.MyConfig;
import ktn.control.ValueControl;
import ktn.dialog.DialogExit;
import ktn.sound.MusicBackground;
import ktn.sound.Sound;

public class Menu extends MyApp implements OnClickListener {
 
    public static MusicBackground mMusicBackground;
    public static Sound mSound;
    Button play, puzzle, help;
    ImageView imageView_sound, imageView_music, imageView_rate;
    Animation mAnimation_in_left, mAnimation_in_right, mAnimation_out_left, mAnimation_out_right;
 
    MySharedPreferences mySharedPreferences;
   
   
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyConfig.getDisplayScreen(this);
        setContentView(R.layout.menu);
        
        mSound = null;
        mSound = new Sound();
        mMusicBackground = null;
        mMusicBackground = new MusicBackground();
        mySharedPreferences = new MySharedPreferences(this);
 
        mySharedPreferences.getIsMusic();
        mySharedPreferences.getIsSound();
        if ( mySharedPreferences.getLevel() == 0)
        {
        	mySharedPreferences.setLevel(5);
        }
       
 
        mSound.loadSound(this);
        mMusicBackground.loadMusic(this);
 
        play = (Button) findViewById(R.id.play);
        play.setOnClickListener(this);
 
        puzzle = (Button) findViewById(R.id.puzzles);
        puzzle.setOnClickListener(this);
 
        help = (Button) findViewById(R.id.help);
        help.setOnClickListener(this);
       
        imageView_sound = (ImageView) findViewById(R.id.imageView_sound);
        imageView_sound.setOnClickListener(this);
 
        imageView_music = (ImageView) findViewById(R.id.imageView_music);
        imageView_music.setOnClickListener(this);
       
        imageView_rate = (ImageView) findViewById(R.id.imageView_rate);
        imageView_rate.setOnClickListener(this);
 
        changIconMusicSound(true);
       
    }
 
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSound = null;
        mMusicBackground.release();
    }
 
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(mSound != null)
                mSound.playHarp();
            // back
            DialogExit dialog = new DialogExit(this);
            dialog.show();
        }
        return false;
    }
 
    @Override
    protected void onResume() {
        super.onResume();
 
        mAnimation_in_left = AnimationUtils.loadAnimation(this,R.anim.slide_in_left);
        mAnimation_in_right = AnimationUtils.loadAnimation(this,R.anim.slide_in_right);
        mAnimation_out_left = AnimationUtils.loadAnimation(this,R.anim.slide_out_left);
        mAnimation_out_right = AnimationUtils.loadAnimation(this,R.anim.slide_out_right);
 
        play.startAnimation(mAnimation_in_left);
        puzzle.startAnimation(mAnimation_in_right);
        help.startAnimation(mAnimation_in_left);
 
        changIconMusicSound(false);
    }
   
    public void animationOut() {
        play.startAnimation(mAnimation_out_left);
        puzzle.startAnimation(mAnimation_out_right);
        help.startAnimation(mAnimation_out_left);
 
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250);
                    nextSelectLevel();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
 // hàm chuyển icon bật tắt âm thanh trong game menu
    public void changIconMusicSound(boolean check) {
        if (ValueControl.isSound == true) {
            imageView_sound.setImageResource(R.drawable.soundon);
        } else {
            imageView_sound.setImageResource(R.drawable.soundoff);
        }
 
        if (ValueControl.isMusic == true) {
            if (check)
                mMusicBackground.play();
            imageView_music.setImageResource(R.drawable.music_on);
        } else {
            imageView_music.setImageResource(R.drawable.music_off);
        }
    }
 
    public void nextSelectLevel() {
    	Intent mIntent2 = new Intent(this, MenuScrollerActivity.class);
    	this.startActivity(mIntent2);
    }
 
    // hàm bắt sự kiện click
    @Override
    public void onClick(View v) {
        if(Menu.mSound != null)
            mSound.playHarp();
 
        switch (v.getId()) {
        case R.id.play:
        	Intent mIntent = new Intent(this, TouchDragExample.class);
        	mIntent.putExtra("level", mySharedPreferences.getLevel());
        	this.startActivity(mIntent);
            break;
        case R.id.puzzles:
        	animationOut();
            break;
        case R.id.help:  
        	Intent mIntent3 = new Intent(this, HelpActivity.class);
        	this.startActivity(mIntent3);
            break;
        case R.id.imageView_rate:
        	// info  
        	break;
        case R.id.imageView_sound:
            updateSoundIcon();
            break;
 
        case R.id.imageView_music:
            updateMusicIcon();
            break;
           
   
        default:
            break;
        }
 
    }
 
    public void showDialogExit() {
        DialogExit mDialogExit = new DialogExit(this);       
        mDialogExit.show();
    }
    // ------------------------------------------------------
  
 
    public void updateMusicIcon() {
        if (ValueControl.isMusic == true) {
            mySharedPreferences.updateIsMusic(false);
            imageView_music.setImageResource(R.drawable.music_off);
            mMusicBackground.pause();
        } else {
            mySharedPreferences.updateIsMusic(true);
            imageView_music.setImageResource(R.drawable.music_on);
            mMusicBackground.resume();
        }
    }
 
    public void updateSoundIcon() {
        if (ValueControl.isSound == true) {
            mySharedPreferences.updateIsSound(false);
            imageView_sound.setImageResource(R.drawable.soundoff);
        } else {
            mySharedPreferences.updateIsSound(true);
            imageView_sound.setImageResource(R.drawable.soundon);
        }
    }
 
}