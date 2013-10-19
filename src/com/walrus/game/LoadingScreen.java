package com.walrus.game;

import android.graphics.Typeface;

import com.walrus.framework.Game;
import com.walrus.framework.Graphics;
import com.walrus.framework.Graphics.ImageFormat;
import com.walrus.framework.Image;
import com.walrus.framework.Screen;

public class LoadingScreen extends Screen {
	public LoadingScreen(Game game) {
		
		super(game);
	}

	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		Assets.background = g.newImage("back1.png", ImageFormat.RGB565);
		Assets.clouds = g.newImage("back2.png", ImageFormat.ARGB4444);
		
		Assets.panel = g.newImage("panel.png", ImageFormat.ARGB4444);
		Assets.panelSelected = g.newImage("panelselected.png", ImageFormat.ARGB4444);
		Assets.nextArrow = g.newImage("next.png", ImageFormat.ARGB4444);
		Assets.redo = g.newImage("redo.png", ImageFormat.ARGB4444);
		Assets.undo = g.newImage("undo.png", ImageFormat.ARGB4444);
		Assets.solve = g.newImage("solve.png", ImageFormat.ARGB4444);
		Assets.redoDown = g.newImage("redodown.png", ImageFormat.ARGB4444);
		Assets.undoDown = g.newImage("undodown.png", ImageFormat.ARGB4444);
		Assets.solveDown = g.newImage("solvedown.png", ImageFormat.ARGB4444);
		Assets.logo = g.newImage("logo.png", ImageFormat.ARGB4444);
		Assets.previousArrow = g.newImage("previous.png", ImageFormat.ARGB4444);

		Assets.numberFloor=10;
		Assets.numberWall=6;
		Assets.context = ((GameBoot) game).getApplicationContext();
		
		Assets.screenWidth = 800;
		Assets.screenHeight = 480;		
		
		Assets.arrow = g.newImage("arrow.png", ImageFormat.ARGB4444);
		Assets.pauseScreen = g.newImage("pause.png", ImageFormat.ARGB4444);
		Assets.transparentBlack = g.newImage("transparentblack.png", ImageFormat.ARGB4444);
		Assets.font = Typeface.createFromAsset(Assets.context.getAssets(), "Fipps-Regular.otf");;
		
		Assets.button = g.newImage("button.png", ImageFormat.ARGB4444);
		Assets.buttonDown = g.newImage("buttondown.png", ImageFormat.ARGB4444);
		
		Assets.tiles= new Image[Assets.numberFloor + Assets.numberWall + 1];
		for(int i=1; i<Assets.tiles.length; i++){
			Assets.tiles[i] = g.newImage("tiles/tile" + i + ".png", ImageFormat.ARGB4444);
		}
		
		Assets.princess= new Image[5];
		for(int i=1; i<Assets.princess.length; i++){
			Assets.princess[i] = g.newImage("characters/princess/" + i + ".png", ImageFormat.ARGB4444);
		}
		Assets.princessAvatar = g.newImage("characters/princess/avatar.png", ImageFormat.RGB565);
		Assets.goal = g.newImage("goal.png", ImageFormat.ARGB4444);
		
		Assets.robot= new Image[5];
		for(int i=1; i<Assets.robot.length; i++){
			Assets.robot[i] = g.newImage("characters/robot/" + i + ".png", ImageFormat.ARGB4444);
		}
		Assets.robotAvatar = g.newImage("characters/robot/avatar.png", ImageFormat.RGB565);

		//This is how you would load a sound if you had one.
		//Assets.click = game.getAudio().createSound("explode.ogg");

		
		game.setScreen(new MainMenuScreen(game));

	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		g.drawImage(Assets.splash, 0, 0);
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {

	}
}