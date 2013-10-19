package com.walrus.game;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

import com.walrus.framework.Image;
import com.walrus.framework.Music;
import com.walrus.framework.Sound;

public class Assets {
	
	public static Image splash, background, princessAvatar, robotAvatar, arrow, transparentBlack, pauseScreen, goal;
	public static Image button, buttonDown, clouds, panel, panelSelected, nextArrow, previousArrow, redo, undo, solve, redoDown, undoDown, solveDown, logo;
	public static Image[] tiles, princess, robot;
	public static Sound click;
	public static Context context;
	//public static Music theme;
	public static Typeface font;
	public static int screenHeight, screenWidth, numberFloor, numberWall;
	
	public static void load(GameBoot sampleGame) {
		// TODO Auto-generated method stub
		/*theme = sampleGame.getAudio().createMusic("menutheme.mp3");
		theme.setLooping(true);
		theme.setVolume(0.85f);
		theme.play();*/
	}
	
	
	
}
