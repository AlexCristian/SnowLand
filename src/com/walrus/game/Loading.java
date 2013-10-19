package com.walrus.game;

import android.graphics.Color;
import android.graphics.Paint;

import com.walrus.core.LevelGenerator;
import com.walrus.framework.Game;
import com.walrus.framework.Graphics;
import com.walrus.framework.Screen;
import com.walrus.gui.TextArea;

public class Loading extends Screen{
	private Thread lvlGen;
	private Paint paint; 
	private TextArea title;
	private int oper=5, k=2;
	private float size=40, szinc=(float) 0.3;
	private int[] color = new int[3];
	public Loading(Game game, int size, int maxMoves){
		super(game);
		paint = new Paint(); 
		paint.setTextSize(50); paint.setAntiAlias(true);
		color[0]=51; color[1]=181; color[2]=229;
		paint.setColor(Color.rgb(51,181,229));
		paint.setTypeface(Assets.font);
		title = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2, "Generating map...", paint);
		lvlGen = new LevelGenerator(25, size, size, maxMoves);
		lvlGen.start();
	}   

	@Override
	public void update(float deltaTime) {
		// TODO Auto-generated method stub
		color[k]+=oper;
		if(color[k]>=255){
			color[k]=255; oper*=-1; 
		}else if(color[k]<=0){
			color[k]=0; oper*=-1; 
		}
		size+=szinc;
		if(size>=50) szinc*=-1;
		if(size<=30) szinc*=-1;
		title.getPaint().setColor(Color.rgb(color[0],color[1],color[2]));
		//title.getPaint().setTextSize(size);
		paint(deltaTime);
		if(((LevelGenerator) lvlGen).isGenerationComplete()){
			game.setScreen(new GameScreen(game, (LevelGenerator) lvlGen));
			lvlGen=null;
		}
	}
	

	@Override
	public void paint(float deltaTime) {
		// TODO Auto-generated method stub
		Graphics g = game.getGraphics();
		g.clearScreen(0);
		g.drawString(title.getText(), title.getTxtX(), title.getTxtY(), title.getPaint());
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void backButton() {
		// TODO Auto-generated method stub
		
	}
}
