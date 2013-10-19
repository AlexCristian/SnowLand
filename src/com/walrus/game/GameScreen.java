package com.walrus.game;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.graphics.Paint;

import com.walrus.core.LevelGenerator;
import com.walrus.core.LevelGenerator.Position;
import com.walrus.core.LevelGenerator.Step;
import com.walrus.core.Move;
import com.walrus.framework.Game;
import com.walrus.framework.Graphics;
import com.walrus.framework.Input.TouchEvent;
import com.walrus.framework.Screen;
import com.walrus.game.Entity.Action;
import com.walrus.game.Entity.Orientation;
import com.walrus.gui.Button;
import com.walrus.gui.GridSpatiator;
import com.walrus.gui.SlidingBackground;
import com.walrus.gui.TextArea;

public class GameScreen extends Screen {
	enum GameState {
		Ready, Running, Paused, GameOver
	}
	GameState state = GameState.Ready;
	
	private LevelGenerator lvlGen;
	private SlidingBackground back1 = new SlidingBackground(0,0,1,Assets.background), back2 = new SlidingBackground(0,480,1,Assets.background), clouds1 = new SlidingBackground(0,0,2,Assets.clouds), clouds2 = new SlidingBackground(0,480,2,Assets.clouds);
	private ArrayList<SlidingBackground> updateableItems = new ArrayList<SlidingBackground> ();
	private Tile[][] tiles;
	private int startX=Assets.screenWidth/3, startY=200, incX=0, incY=0, touchLastX=-1, touchLastY=-1;
	private Entity player, robots[], selected=null, copy=null;
	private ArrayList <Entity> clickable = new ArrayList<Entity>();
	private int[][] numericLevelMap;
	private int gestureX, gestureY, panelX, panelY, avatarOffsetX, avatarOffsetY, arrowBounce=0, moves=0;
	private boolean bouncingUp = true, win=false, isPaused=false, firstMove=false, usedSolver=false;
	private Action status = Action.Resting;
	private Paint paint, scorePaint;
	private TextArea title, stitle, sstitle, pausedTitle;
	private GridSpatiator menuGrid;
	private Button backToGame, backToMenu, undo, redo, solve; private Position[] winPosition;
	private ArrayList<Move> moveHistory = new ArrayList<Move>(), moveFuture = new ArrayList<Move>();
	private long lastTime=System.currentTimeMillis();
	
	public GameScreen(Game game, LevelGenerator lvl) {
		super(game);
		 lvlGen = lvl;
		 numericLevelMap=lvlGen.getLevelMap().clone();
		 tiles = TileMatrixFactory.transformToTiles(lvlGen.getLevelMap());
		 instantiate();
		 spawnEntities();
	}
	
	private void spawnEntities(){
		player = new Entity(Assets.princess, Assets.princessAvatar, lvlGen.getPrincessI(),lvlGen.getPrincessJ(),0,20);
		clickable.add(player);
		robots = new Entity[2];
		robots[0] = new Entity(Assets.robot, Assets.robotAvatar, lvlGen.getRobot1I(), lvlGen.getRobot1J(),0,20);
		clickable.add(robots[0]);
		robots[1] = new Entity(Assets.robot, Assets.robotAvatar, lvlGen.getRobot2I(),lvlGen.getRobot2J(),0,20);
		clickable.add(robots[1]);
	}
	
	private void instantiate(){
		paint = new Paint();
		paint.setTextSize(30); paint.setAntiAlias(true);
		paint.setColor(Color.rgb(51,181,229));
		paint.setTypeface(Assets.font);
		scorePaint = new Paint();
		scorePaint.setTextSize(13); scorePaint.setAntiAlias(true);
		scorePaint.setTypeface(Assets.font);
		updateableItems.add(back1);
		updateableItems.add(back2);
		updateableItems.add(clouds1);
		updateableItems.add(clouds2);
		panelX = Assets.screenWidth/2-Assets.panel.getWidth()/2;
		panelY = Assets.screenHeight-Assets.panel.getHeight();
		avatarOffsetX = 42; avatarOffsetY = 45;
		undo = new Button(Assets.screenWidth/2-Assets.panel.getWidth()/2+350, Assets.screenHeight-Assets.panel.getHeight()+65);
		undo.setButton(Assets.undo);
		redo = new Button(Assets.screenWidth/2-Assets.panel.getWidth()/2+410, Assets.screenHeight-Assets.panel.getHeight()+65);
		redo.setButton(Assets.redo);
		solve = new Button(Assets.screenWidth/2-Assets.panel.getWidth()/2+370, Assets.screenHeight-Assets.panel.getHeight()+30);
		solve.setButton(Assets.solve);
		undo.setButtonDown(Assets.undoDown);
		redo.setButtonDown(Assets.redoDown);
		solve.setButtonDown(Assets.solveDown);
		
		menuGrid = new GridSpatiator(Assets.screenWidth/2, 170);
		pausedTitle = new TextArea(menuGrid.getPosX(), menuGrid.getPosY(), "Paused", paint);
		menuGrid.setPosY(menuGrid.getPosY()+30);
		backToGame = new Button(menuGrid.getPosX()-Assets.button.getWidth()/2, menuGrid.getPosY(), "Resume", paint);
		menuGrid.nextElement(); menuGrid.setPosY(menuGrid.getPosY()+10);
		backToMenu = new Button(menuGrid.getPosX()-Assets.button.getWidth()/2, menuGrid.getPosY(), "Main menu", paint);
	}
	@Override
	public void update(float deltaTime) {
		List<TouchEvent> touchEvents = game.getInput().getTouchEvents();
		if(player.getStatus() == Action.Resting && player.getPosi() == lvlGen.getGoalI() && player.getPosj() == lvlGen.getGoalJ()){
			win=true;
			if(usedSolver){
				title = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2, "Solving complete", paint);
				stitle = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2 + 40, "Map solved in " + lvlGen.getMoves() + " moves!", paint);
				sstitle = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2 + 80, "", paint);
			}else{ 
			if(moves==lvlGen.getMoves())
				title = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2, "Extraordinary!", paint);
			else
				title = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2, "Congratulations!", paint);
			stitle = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2 + 40, "You won in " + moves + " moves, with an", paint);
			sstitle = new TextArea(Assets.screenWidth/2, Assets.screenHeight/2 + 80, "optimal number of " + lvlGen.getMoves() + " moves!", paint);
			}
		}
		paint(deltaTime);
		for(SlidingBackground item : updateableItems)
			item.update();
		if(usedSolver&&!isPaused&&!win&&status!=Action.Sliding&&lastTime+1000<=System.currentTimeMillis()){
			simulateSolver(lvlGen.getFinalPosition()[0].getStepHistory());
			simulateSolver(lvlGen.getFinalPosition()[1].getStepHistory());
			simulateSolver(lvlGen.getFinalPosition()[2].getStepHistory());
			lastTime=System.currentTimeMillis();
		}
		slide();
		if(!win&&!isPaused&&!usedSolver){
			if(selected==null){
				for(TouchEvent event : touchEvents){
					for(Entity character : clickable){
						if(inBounds(event, character.getRealX(), character.getRealY(), character.getWidth(), character.getHeight(), 15)){
							copy=character;
						}
					}
					if(touchLastX!=-1){
						incX += event.x-touchLastX;
						incY += event.y-touchLastY;
					}
					touchLastX = event.x; touchLastY = event.y;
					if(event.type == TouchEvent.TOUCH_UP){
						touchLastX=-1; selected=copy; copy=null;
					}
					if(event.type == TouchEvent.TOUCH_UP){
						solve.setTouchedDown(false);
						undo.setTouchedDown(false);
						redo.setTouchedDown(false);
					}
					if(inBounds(event, undo.getImgX(), undo.getImgY(), undo.getButton().getWidth(), undo.getButton().getHeight(), 5)){
						if(event.type == TouchEvent.TOUCH_DOWN)
							undo.setTouchedDown(true);
						else
						undoMove();
					}else if(inBounds(event, redo.getImgX(), redo.getImgY(), redo.getButton().getWidth(), redo.getButton().getHeight(), 5)){
						if(event.type == TouchEvent.TOUCH_DOWN)
							redo.setTouchedDown(true);
						else
							redoMove();
					} else if(inBounds(event, solve.getImgX(), solve.getImgY(), solve.getButton().getWidth(), solve.getButton().getHeight(), 5) && !usedSolver){
						if(event.type == TouchEvent.TOUCH_DOWN)
							solve.setTouchedDown(true);
						else{
							solve.setTouchedDown(false);
							solve();
						}
					}
				}
			}else{
				for(TouchEvent event : touchEvents){
					if(event.type == TouchEvent.TOUCH_DOWN){
						gestureX = event.x;
						gestureY = event.y;
					}
					if(event.type == TouchEvent.TOUCH_UP){
						if(inBounds(event, gestureX, gestureY, 0, 0, 20)){
							selected=null;
							for(Entity character : clickable){
								if(inBounds(event, character.getRealX(), character.getRealY(), character.getWidth(), character.getHeight(), 15)){
									selected=character;
								}
							}
							if(selected==null)
								touchLastX=-1;
						}else if(status!=Action.Sliding){
							selected.setStatus(Action.Sliding);
							moveFuture.clear();
							status = Action.Sliding;
							firstMove = true;
							if(Math.abs(event.x-gestureX) > Math.abs(event.y-gestureY)){
								if(event.x<gestureX){
									selected.setFacing(Orientation.West);
								}else{
									selected.setFacing(Orientation.East);
								}
							}else{
								if(event.y>gestureY){
									selected.setFacing(Orientation.South);
								}else{
									selected.setFacing(Orientation.North);
								}
							}
						}
					}
				}
			}
		}else if(isPaused){
			for(TouchEvent event : touchEvents){
				if(event.type == TouchEvent.TOUCH_UP){
					backToGame.setTouchedDown(false);
					backToMenu.setTouchedDown(false);
				}
				if(inBounds(event, backToGame.getImgX(), backToGame.getImgY(), backToGame.getButton().getWidth(), backToGame.getButton().getHeight())){
					if(event.type == TouchEvent.TOUCH_DOWN){
						backToGame.setTouchedDown(true);
					}else if(event.type == TouchEvent.TOUCH_UP){
						isPaused=false;
					}
				}else if(inBounds(event, backToMenu.getImgX(), backToMenu.getImgY(), backToMenu.getButton().getWidth(), backToMenu.getButton().getHeight())){
					if(event.type == TouchEvent.TOUCH_DOWN){
						backToMenu.setTouchedDown(true);
					}else if(event.type == TouchEvent.TOUCH_UP){
						game.setScreen(new MainMenuScreen(game));
						nullify();
					}
				}
			}
		}
		
	}

	private boolean inBounds(TouchEvent event, int x, int y, int width, int height) {
		if (event.x > x && event.x < x + width - 1 && event.y > y
				&& event.y < y + height - 1)
			return true;
		else
			return false;
	}
	private boolean inBounds(TouchEvent event, int x, int y, int width, int height, int radius) {
		if (event.x > x-radius && event.x < x+radius + width - 1 && event.y > y-radius
				&& event.y < y+radius + height - 1)
			return true;
		else
			return false;
	}
	
	private void simulateSolver(ArrayList<Step> y){
		Step x = y.get(0);
		if(status==Action.Resting && y.size()>0){
			status=Action.Sliding;
			Entity current = null;
			if(x.id==0){
				current=player;
			}else if(x.id==1){
				current=robots[0];
			}else if(x.id==2){
				current=robots[1];
			}
			current.setTargetI(x.i);
			current.setTargetJ(x.j);
			current.setStatus(Action.Sliding);
			if(x.i>current.getPosi()){
				current.setFacing(Orientation.North);
			}else if(x.i<current.getPosi()){
				current.setFacing(Orientation.South);
			}else if(x.j>current.getPosj()){
				current.setFacing(Orientation.East);
			}else if(x.j<current.getPosj()){
				current.setFacing(Orientation.West);
			}
			if(x.i==current.getPosi()&&x.j==current.getPosj()){
				current.setStatus(Action.Resting);
				status = Action.Resting;
			}else moves++;
		}
		y.remove(0);
		
	}
	
	private void undoMove(){
		if(status==Action.Resting && moveHistory.size()>0){
			status = Action.Sliding;
			Move current;
			current = moveHistory.get(moveHistory.size()-1);
			if(current.getOrientation() == Orientation.North){
				current.getEntity().setFacing(Orientation.South);
			}
			if(current.getOrientation() == Orientation.South){
				current.getEntity().setFacing(Orientation.North);
			}
			if(current.getOrientation() == Orientation.East){
				current.getEntity().setFacing(Orientation.West);
			}
			if(current.getOrientation() == Orientation.West){
				current.getEntity().setFacing(Orientation.East);
			}
			moveFuture.add(new Move(current.getEntity(), current.getOrientation()));
			moveHistory.remove(moveHistory.size()-1);
			current.getEntity().setTargetI(current.getPositionI());
			current.getEntity().setTargetJ(current.getPositionJ());
			current.getEntity().setStatus(Action.Sliding);
			moves--;
		}
	}
	private void redoMove(){
		if(status==Action.Resting && moveFuture.size()>0){
			status = Action.Sliding;
			Move current;
			current = moveFuture.get(moveFuture.size()-1);
			moveHistory.add(new Move(current.getEntity(), current.getOrientation()));
			moveFuture.remove(moveFuture.size()-1);
			current.getEntity().setFacing(current.getOrientation());
			current.getEntity().setTargetI(current.getPositionI());
			current.getEntity().setTargetJ(current.getPositionJ());
			current.getEntity().setStatus(Action.Sliding);
			moves++;
		}
	}
	private void solve(){
		usedSolver=true; moves=0;
		numericLevelMap = lvlGen.getLevelMap().clone();
		player = new Entity(Assets.princess, Assets.princessAvatar, lvlGen.getPrincessI(),lvlGen.getPrincessJ(),0,20);
		clickable.clear();
		clickable.add(player);
		robots = new Entity[2];
		robots[0] = new Entity(Assets.robot, Assets.robotAvatar, lvlGen.getRobot1I(), lvlGen.getRobot1J(),0,20);
		clickable.add(robots[0]);
		robots[1] = new Entity(Assets.robot, Assets.robotAvatar, lvlGen.getRobot2I(),lvlGen.getRobot2J(),0,20);
		clickable.add(robots[1]);
		winPosition = lvlGen.getFinalPosition();
		winPosition[0].stepHistory.remove(0);
		winPosition[1].stepHistory.remove(0);
		winPosition[2].stepHistory.remove(0);
	}
	private void slide(){
		boolean didSlide=false;
		for(Entity character : clickable){
			if(character.getStatus()==Action.Sliding){
				int ci=character.getPosi()+character.getIncrementI(), cj=character.getPosj()+character.getIncrementJ();
				if(numericLevelMap[ci][cj]<=10&&numericLevelMap[ci][cj]>=0){
					didSlide=true;
					if(firstMove){
						firstMove=false;
						moves++;
						moveHistory.add(new Move(character, character.getFacing()));
					}
					character.setPosi(ci); character.setPosj(cj);
					if(ci == character.getTargetI() && cj == character.getTargetJ()){
						character.setTargetI(-1);
						character.setTargetJ(-1);
						character.setStatus(Action.Resting);
					}
				}else{
					character.setStatus(Action.Resting);
				}
			}
		}
		if(!didSlide){
			status=Action.Resting;
		}
	}
	
	@Override
	public void paint(float deltaTime) {
		Graphics g = game.getGraphics();
		
		for(SlidingBackground item : updateableItems)
			g.drawImage(item.getBack(), item.getBgX(), item.getBgY());
		//end compositing background
		
		paintTiles(g);
		//paint menu
		
		paintPanel(g);
		
		if(win){
			g.drawImage(Assets.transparentBlack, 0, 0);
			g.drawString(title.getText(), title.getTxtX(), title.getTxtY(), title.getPaint());
			g.drawString(stitle.getText(), stitle.getTxtX(), stitle.getTxtY(), stitle.getPaint());
			g.drawString(sstitle.getText(), sstitle.getTxtX(), sstitle.getTxtY(), sstitle.getPaint());
		}
		if(isPaused){
			g.drawImage(Assets.pauseScreen, 0, 0);
			g.drawString(pausedTitle.getText(), pausedTitle.getTxtX(), pausedTitle.getTxtY(), pausedTitle.getPaint());
			g.drawImage(backToGame.getButton(), backToGame.getImgX(), backToGame.getImgY());
			g.drawString(backToGame.getText(), backToGame.getTxtX(), backToGame.getTxtY(), backToGame.getPaint());
			g.drawImage(backToMenu.getButton(), backToMenu.getImgX(), backToMenu.getImgY());
			g.drawString(backToMenu.getText(), backToMenu.getTxtX(), backToMenu.getTxtY(), backToMenu.getPaint());
			
		}
	}
	
	private void paintPanel(Graphics g){
		if(selected==null){
			g.drawImage(Assets.panel, panelX, panelY);
		}else{
			g.drawImage(Assets.panelSelected, panelX, panelY);
			g.drawImage(selected.getAvatar(), panelX+avatarOffsetX, panelY+avatarOffsetY);			
		}
		g.drawString(moves + " moves so far", panelX+avatarOffsetX+80, panelY+avatarOffsetY+15, scorePaint);
		g.drawString(lvlGen.getMoves() + " computed moves", panelX+avatarOffsetX+80, panelY+avatarOffsetY+35, scorePaint);
		g.drawImage(undo.getButton(), undo.getImgX(), undo.getImgY());
		g.drawImage(redo.getButton(), redo.getImgX(), redo.getImgY());
		g.drawImage(solve.getButton(), solve.getImgX(), solve.getImgY());
		
		
	}
	
	private void paintTiles(Graphics g) {
		int currentX=startX, currentY=startY, perspectiveOffset=0; boolean isOccupied;
		
		for(int i=tiles.length-2; i>0; i--){
			currentX=startX-perspectiveOffset;
			for(int j=1; j<tiles[i].length-1; j++){
				if(i==lvlGen.getGoalI() && j==lvlGen.getGoalJ()){
					g.drawImage(Assets.goal, currentX + incX, currentY + incY - (Assets.goal.getHeight()));
				}else{
					g.drawImage(tiles[i][j].getTileImage(), currentX + incX, currentY + incY - (tiles[i][j].getHeight()));
				}
				isOccupied=false;
				for(Entity character : clickable){
					if(character.getPosi()==i && character.getPosj() == j){
						g.drawImage(character.getCharacterImage(), currentX + incX + (tiles[i][j].getWidth()/2 - character.getWidth()/2), currentY + incY - (character.getHeight() + character.getOffsetY()));
						character.setRealX(currentX + incX);
						character.setRealY(currentY + incY - (character.getHeight()));
						isOccupied=true;
					}
				}
				if((isOccupied&&numericLevelMap[i][j]>-1)||(!isOccupied&&numericLevelMap[i][j]<0))
					numericLevelMap[i][j]*=(-1);
				if(i==lvlGen.getGoalI() && j==lvlGen.getGoalJ()){
					g.drawImage(Assets.arrow, currentX + incX + Assets.arrow.getWidth()/2, currentY + incY - (arrowBounce+70));
					if(bouncingUp){
						arrowBounce++;
						if(arrowBounce==30)
							bouncingUp=false;
					}else{
						arrowBounce--;
						if(arrowBounce==0)
							bouncingUp=true;
					}
				}
				currentX += 44; //width of a tile
			}
			currentY += 18; //height of a tile
			perspectiveOffset+=17;
		}
		
	}


	private void nullify() {

		// Set all variables to null. You will be recreating them in the
		// constructor.
		// Call garbage collector to clean up memory.
		lvlGen=null;
		back1 = null; back2 = null; clouds1 = null; clouds2 = null;
		updateableItems = null;
		tiles = null;
		player=null; robots=null; selected=null; copy=null;
		clickable = null;
		numericLevelMap = null;
		status = null;
		paint = null;
		title = null; stitle = null; sstitle = null; pausedTitle = null;
		menuGrid = null;
		backToGame = null; backToMenu = null;
		
		System.gc();

	}

	@Override
	public void pause() {
		isPaused=true;

	}

	@Override
	public void resume() {
		if (state == GameState.Paused)
			state = GameState.Running;
	}

	@Override
	public void dispose() {

	}

	@Override
	public void backButton() {
		if(isPaused)
			isPaused=false;
		else isPaused=true;
	}

	private void goToMenu() {
		// TODO Auto-generated method stub
		game.setScreen(new MainMenuScreen(game));

	}
}