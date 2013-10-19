package com.walrus.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.walrus.framework.Game;
import com.walrus.framework.Graphics;
import com.walrus.framework.Input.TouchEvent;
import com.walrus.framework.Screen;
import com.walrus.gui.ArrowSelector;
import com.walrus.gui.Button;
import com.walrus.gui.GridSpatiator;
import com.walrus.gui.SlidingBackground;
import com.walrus.gui.TextArea;

public class MainMenuScreen extends Screen {
	
	/*
	 * (C) Copyright Alex Cristian & Walrus Studios, 2012
	 * All rights reserved.
	 *  
	 */
	
	private SlidingBackground back1 = new SlidingBackground(0,0,1,Assets.background), back2 = new SlidingBackground(0,480,1,Assets.background), clouds1 = new SlidingBackground(0,0,2,Assets.clouds), clouds2 = new SlidingBackground(0,480,2,Assets.clouds);
	private ArrayList<SlidingBackground> updateableItems = new ArrayList<SlidingBackground> ();
	private ArrayList<Button> clickableItems = new ArrayList<Button> ();
	private Button newGame; private Button[] diffBtn = new Button[3];
	private TextArea title, diffTitle; private TextArea[] diffText = new TextArea[3]; 
	private Paint paint;
	private boolean setDificulty=false;
	private int currentDif = 0;
	private ArrowSelector next, prev;
	
	public MainMenuScreen(Game game) {
		super(game);
		instantiate();
	}
	
	private void instantiate(){
		updateableItems.add(back1);
		updateableItems.add(back2);
		updateableItems.add(clouds1);
		updateableItems.add(clouds2);
		paint = new Paint();
		paint.setTypeface(Assets.font);
		GridSpatiator menuGrid = new GridSpatiator(Assets.screenWidth/2, 100);
		paint.setTextSize(60); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(51,181,229));
		title = new TextArea(menuGrid.getPosX(), menuGrid.getPosY(), "SnowLand", paint);
		menuGrid.nextElement(); menuGrid.nextElement();
		paint = new Paint();
		paint.setTextSize(30); paint.setAntiAlias(true);
		paint.setTypeface(Assets.font);
		newGame = new Button(menuGrid.getPosX()-Assets.button.getWidth()/2, menuGrid.getPosY()-Assets.button.getHeight()/2, "New game", paint);
		menuGrid.nextElement();
		
		GridSpatiator diffGrid = new GridSpatiator(Assets.screenWidth/2, 150);
		paint = new Paint();
		paint.setTextSize(30); paint.setAntiAlias(true);
		paint.setTypeface(Assets.font);
		paint.setColor(Color.rgb(51,181,229));
		diffTitle = new TextArea(diffGrid.getPosX(), diffGrid.getPosY(), "Select level difficulty", paint);
		diffGrid.nextElement();//diffGrid.setPosY(diffGrid.getPosY()+30);
		paint = new Paint();
		paint.setTypeface(Assets.font);
		paint.setTextSize(30); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(102,153,0));
		diffBtn[0]=new Button(diffGrid.getPosX()-Assets.button.getWidth()/2, diffGrid.getPosY(), "Easy", paint);
		paint = new Paint();
		paint.setTypeface(Assets.font);
		paint.setTextSize(30); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(255,136,0));
		diffBtn[1]=new Button(diffGrid.getPosX()-Assets.button.getWidth()/2, diffGrid.getPosY(), "Normal", paint);
		paint = new Paint();
		paint.setTypeface(Assets.font);
		paint.setTextSize(30); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(204,0,0));
		diffBtn[2]=new Button(diffGrid.getPosX()-Assets.button.getWidth()/2, diffGrid.getPosY(), "Hard", paint);
		diffGrid.nextElement();diffGrid.nextElement();
		paint = new Paint();
		paint.setTypeface(Assets.font);
		paint.setTextSize(25); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(102,153,0));
		diffText[0]=new TextArea(diffGrid.getPosX(), diffGrid.getPosY(), "A 4x4 map, with 6 maximum moves", paint);
		paint = new Paint();
		paint.setTypeface(Assets.font);
		paint.setTextSize(25); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(255,136,0));
		diffText[1]=new TextArea(diffGrid.getPosX(), diffGrid.getPosY(), "A 6x6 map, with 8 maximum moves", paint);
		paint = new Paint();
		paint.setTypeface(Assets.font);
		paint.setTextSize(25); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(204,0,0));
		diffText[2]=new TextArea(diffGrid.getPosX(), diffGrid.getPosY(), "A 10x10 map, with 8 maximum moves", paint);
		
		clickableItems.add(newGame);
		
		next = new ArrowSelector(Assets.nextArrow, diffBtn[0].getImgX()+diffBtn[0].getButton().getWidth()/2+210, diffBtn[0].getImgY()+10, 1);
		prev = new ArrowSelector(Assets.previousArrow, diffBtn[0].getImgX()-diffBtn[0].getButton().getWidth()/2+120, diffBtn[0].getImgY()+10, -1);
	}
	
	@Override
	public void update(float deltaTime) {
		Graphics g = game.getGraphics();
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();

		paint(deltaTime);
		for(SlidingBackground item : updateableItems)
			item.update();
		int len = touchEvents.size();
		if(!setDificulty){
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				
				if (event.type == TouchEvent.TOUCH_DOWN) {
					for(Button button : clickableItems){
						if (inBounds(event, button.getImgX(), button.getImgY(), button.getButton().getWidth(), button.getButton().getHeight())) {
							button.setTouchedDown(true);
						}
					}
				}else if(event.type == TouchEvent.TOUCH_UP){
					for(Button button : clickableItems){
						button.setTouchedDown(false);
						if (inBounds(event, button.getImgX(), button.getImgY(), button.getButton().getWidth(), button.getButton().getHeight())) {
							setDificulty=true;
						}
					}
				}
			}
		}else{
			for (int i = 0; i < len; i++) {
				TouchEvent event = touchEvents.get(i);
				
				if (event.type == TouchEvent.TOUCH_DOWN) {
					if (inBounds(event, diffBtn[0].getImgX(), diffBtn[0].getImgY(), diffBtn[0].getButton().getWidth(), diffBtn[0].getButton().getHeight())) {
						diffBtn[currentDif].setTouchedDown(true);
					}
				}else if(event.type == TouchEvent.TOUCH_UP){
					diffBtn[currentDif].setTouchedDown(false);
					if (inBounds(event, diffBtn[0].getImgX(), diffBtn[0].getImgY(), diffBtn[0].getButton().getWidth(), diffBtn[0].getButton().getHeight())) {
						if(currentDif==0){
							game.setScreen(new Loading(game, 6, 6));
						}else if(currentDif==1){
							game.setScreen(new Loading(game, 8, 8));
						}else if(currentDif==2){
							game.setScreen(new Loading(game, 12, 8));
						}
					}else if(inBounds(event, prev.getArrowX(), prev.getArrowY(), prev.getArrow().getWidth(), prev.getArrow().getHeight(), 20)){
						prev.setClickedOffset(30);
						currentDif--;
						if(currentDif<0) currentDif=2;
					}else if(inBounds(event, next.getArrowX(), next.getArrowY(), next.getArrow().getWidth(), next.getArrow().getHeight(), 20)){
						next.setClickedOffset(30);
						currentDif++;
						if(currentDif>2) currentDif=0;
					}
				}
			}
		}
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width,
			int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}

	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		
		for(SlidingBackground item : updateableItems)
			g.drawImage(item.getBack(), item.getBgX(), item.getBgY());
		//end compositing background
		
		g.drawString(title.getText(), title.getTxtX(), title.getTxtY(), title.getPaint());
		g.drawImage(newGame.getButton(), newGame.getImgX(), newGame.getImgY());
		g.drawString(newGame.getText(), newGame.getTxtX(), newGame.getTxtY(), newGame.getPaint());
		
		//TESTING STRING
		/*
		Paint testPaint = new Paint();
		testPaint.setTextSize(30); testPaint.setAntiAlias(true);
		g.drawString("(c) Walrus Studios - internal BETA", 10, Assets.screenHeight-20, testPaint);
		*/
		g.drawImage(Assets.logo, 0, Assets.screenHeight-100);
		if(setDificulty)
			paintDiffSelector(g);
	}

	private void paintDiffSelector(Graphics g){
		g.drawImage(Assets.transparentBlack, 0, 0);
		g.drawString(diffTitle.getText(), diffTitle.getTxtX(), diffTitle.getTxtY(), diffTitle.getPaint());
		g.drawImage(diffBtn[currentDif].getButton(), diffBtn[currentDif].getImgX(), diffBtn[currentDif].getImgY());
		g.drawString(diffBtn[currentDif].getText(), diffBtn[currentDif].getTxtX(), diffBtn[currentDif].getTxtY(), diffBtn[currentDif].getPaint());
		g.drawImage(prev.getArrow(), prev.getArrowX(), prev.getArrowY());
		g.drawImage(next.getArrow(), next.getArrowX(), next.getArrowY());
		g.drawString(diffText[currentDif].getText(), diffText[currentDif].getTxtX(), diffText[currentDif].getTxtY(), diffText[currentDif].getPaint());
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

	private boolean inBounds(TouchEvent event, int x, int y, int width, int height, int radius) {
		if (event.x > x-radius && event.x < x+radius + width - 1 && event.y > y-radius
				&& event.y < y+radius + height - 1)
			return true;
		else
			return false;
	}
	
	@Override
	public void backButton() {
		if(setDificulty)
			setDificulty=false;
		else
			android.os.Process.killProcess(android.os.Process.myPid());

	}
}
