package com.walrus.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import com.walrus.game.Assets;

public class LevelGenerator extends Thread{
	private int[][] levelMap;
	private int difficultyLevel, height, width, minimumSteps=-1;
	private int MAXIMUM_DEPTH;
	private final int posi[]={-1,0,1,0}, posj[]={0,1,0,-1};
	private HashMap<String, Integer> checkedSolutions;
	private boolean isDone = false;
	private ArrayList<ArrayList <Position>> toVisit, nextVisit;
	private Position[] finalPosition = new Position[3];
	
	public class Step{
		public byte i,j,id;
		public Step(byte ipos, byte jpos, byte ID){
			i=ipos;
			j=jpos;
			id=ID;
		}
	}
	public class Position{
		public byte i,j;
		public ArrayList<Step> stepHistory = new ArrayList<Step>();
		public Position(byte positionI, byte positionX){
			i=positionI;
			j=positionX;
		}
		public ArrayList<Step> getStepHistory() {
			return stepHistory;
		}
		public void setStepHistory(ArrayList<Step> stepHistory) {
			this.stepHistory = stepHistory;
		}
	}
	
	public Position robot1, robot2, princess, goal;
	
	public LevelGenerator(int dif, int hsize, int wsize, int maxMoves){
		difficultyLevel = dif;
		MAXIMUM_DEPTH = maxMoves;
		levelMap = new int[hsize][wsize];
		height = hsize;
		width = wsize;
		
	}
	
	public void run(){
		for(int i=0; i<levelMap[0].length; i++){
			levelMap[0][i]=11; levelMap[levelMap.length-1][i]=11;
		}
		for(int i=1; i<levelMap.length-1; i++){
			levelMap[i][0]=11; levelMap[i][levelMap[i].length-1]=11;
		}
		
		do{
			generateLevel();
		}while(!isSolvable());
		
		isDone=true;
	}
	
	private boolean isSolvable(){
		minimumSteps=-1; checkedSolutions = new HashMap<String, Integer>();
		int currentDepth=0;
		toVisit = new ArrayList<ArrayList<Position>>(); nextVisit = new ArrayList<ArrayList<Position>>();
		toVisit.add(new ArrayList<Position>());
		toVisit.get(0).add(copyPosition(princess, 0));
		toVisit.get(0).add(copyPosition(robot1, 1));
		toVisit.get(0).add(copyPosition(robot2, 2));
		while(minimumSteps==-1 && currentDepth <= MAXIMUM_DEPTH){
			for(ArrayList<Position> currentVisiting : toVisit){
				findSolution(currentVisiting, currentDepth);
			}
			toVisit = nextVisit;
			nextVisit = new ArrayList<ArrayList<Position>>();
			currentDepth++;
			System.gc();
		}
		toVisit=null; nextVisit=null; System.gc();
		if(minimumSteps==-1 || minimumSteps < MAXIMUM_DEPTH-2)
			return false;
		return true;
	}
	
	private Position copyPosition(Position x, int ID){
		Position n = new Position(x.i, x.j);
		n.setStepHistory((ArrayList<Step>) x.getStepHistory().clone());
		n.getStepHistory().add(new Step(x.i, x.j, (byte)ID));
		return n;
	}
	
	private void findSolution(ArrayList<Position> a, int depth){
		Position prin, r1, r2;
		prin=a.get(0);
		r1=a.get(1);
		r2=a.get(2);
		if(prin.i == goal.i && prin.j == goal.j){
			if(minimumSteps>depth||minimumSteps==-1) {minimumSteps=depth; finalPosition[0] = copyPosition(prin, 0);finalPosition[1] = copyPosition(r1, 1);finalPosition[2] = copyPosition(r2, 2);}return;
		}
		String s = prin.i+" "+prin.j+" "+r1.i+" "+r1.j+" "+r2.i+" "+r2.j;
		if(depth > MAXIMUM_DEPTH)
			return;
		if(checkedSolutions.containsKey(s))
			if(!(checkedSolutions.get(s)>depth))
				return;
		checkedSolutions.remove(s);
		checkedSolutions.put(s, depth);
		Position nprin=copyPosition(prin,0), nr1=copyPosition(r1,1), nr2=copyPosition(r2,2), current;
		//case 1, princess can reach answer
		for(int i=0; i<4; i++){
			current = copyPosition(prin,0);
			if(!(levelMap[current.i+posi[i]][current.j+posj[i]]<=10 && (current.j+posj[i] != nr1.j || current.i+posi[i] != nr1.i) && (current.j+posj[i] != nr2.j || current.i+posi[i] != nr2.i)))
				continue;
			current.i+=posi[i]; current.j+=posj[i];
			while(levelMap[current.i+posi[i]][current.j+posj[i]]<=10 && (current.j+posj[i] != nr1.j || current.i+posi[i] != nr1.i) && (current.j+posj[i] != nr2.j || current.i+posi[i] != nr2.i)){
				current.i+=posi[i]; current.j+=posj[i];
			}
			if(levelMap[current.i][current.j]<=10 && (current.j != nr1.j || current.i != nr1.i) && (current.j != nr2.j || current.i != nr2.i)){
				ArrayList<Position> toQueue = new ArrayList<Position>();
				toQueue.add(current);
				toQueue.add(copyPosition(r1,1));
				toQueue.add(copyPosition(r2,2));
				nextVisit.add(toQueue);
			}
		}
		
		//if(minimumSteps != -1) return;
		//case 2, r1 can help reach answer
		for(int i=0; i<4; i++){
			current = copyPosition(r1,1);
			if(!(levelMap[current.i+posi[i]][current.j+posj[i]]<=10 && (current.j+posj[i] != nprin.j || current.i+posi[i] != nprin.i) && (current.j+posj[i] != nr2.j || current.i+posi[i] != nr2.i)))
				continue;
			current.i+=posi[i]; current.j+=posj[i];
			while(levelMap[current.i+posi[i]][current.j+posj[i]]<=10 && (current.j+posj[i] != nprin.j || current.i+posi[i] != nprin.i) && (current.j+posj[i] != nr2.j || current.i+posi[i] != nr2.i)){
				current.i+=posi[i]; current.j+=posj[i];
			}
			if(levelMap[current.i][current.j]<=10 && (current.j != nprin.j || current.i != nprin.i) && (current.j != nr2.j || current.i != nr2.i)){
				ArrayList<Position> toQueue = new ArrayList<Position>();
				toQueue.add(copyPosition(prin,0));
				toQueue.add(current);
				toQueue.add(copyPosition(r2,2));
				nextVisit.add(toQueue);
			}
		}
		
		//if(minimumSteps != -1) return;
		//case 3, r2 can help reach answer
		for(int i=0; i<4; i++){
			current = copyPosition(r2,2);
			if(!(levelMap[current.i+posi[i]][current.j+posj[i]]<=10 && (current.j+posj[i] != nprin.j || current.i+posi[i] != nprin.i) && (current.j+posj[i] != nr1.j || current.i+posi[i] != nr1.i)))
				continue;
			current.i+=posi[i]; current.j+=posj[i];
			while(levelMap[current.i+posi[i]][current.j+posj[i]]<=10 && (current.j+posj[i] != nprin.j || current.i+posi[i] != nprin.i) && (current.j+posj[i] != nr1.j || current.i+posi[i] != nr1.i)){
				current.i+=posi[i]; current.j+=posj[i];
			}
			if(levelMap[current.i][current.j]<=10 && (current.j != nprin.j || current.i != nprin.i) && (current.j != nr1.j || current.i != nr1.i)){
				ArrayList<Position> toQueue = new ArrayList<Position>();
				toQueue.add(copyPosition(prin,0));
				toQueue.add(copyPosition(r1,1));
				toQueue.add(current);
				nextVisit.add(toQueue);
			}
		}	
		return;
	}
	
	private void generateLevel(){
		Random rnd = new Random();
		for(int i=1; i<levelMap.length-1; i++){
			for(int j=1; j<levelMap[i].length-1; j++){
				if(rnd.nextInt(100)<=difficultyLevel){
					//wall
					if(rnd.nextInt(100)>=30){
							levelMap[i][j]=11;
					}else{
						levelMap[i][j]=Assets.numberFloor+rnd.nextInt(Assets.numberWall-1)+2;
					}
				}else{
					//floor
					levelMap[i][j]=rnd.nextInt(Assets.numberFloor)+1;
				}
			}
		}
		robot1 = new Position((byte) rnd.nextInt(height), (byte) rnd.nextInt(width));
		robot2 = new Position((byte) rnd.nextInt(height),(byte)  rnd.nextInt(width));
		princess = new Position((byte)rnd.nextInt(height),(byte)  rnd.nextInt(width));
		goal = new Position((byte) rnd.nextInt(height),(byte)  rnd.nextInt(width));
		while(levelMap[robot1.i][robot1.j]>10){
			robot1 = new Position((byte) rnd.nextInt(height),(byte)  rnd.nextInt(width));
		}
		while(levelMap[robot2.i][robot2.j]>10 || (robot2.i==robot1.i && robot2.j==robot1.j)){
			robot2 = new Position((byte) rnd.nextInt(height),(byte) rnd.nextInt(width));
		}
		while(levelMap[princess.i][princess.j]>10 || (princess.i==robot1.i && princess.j==robot1.j)  || (robot2.i==princess.i && robot2.j==princess.j)){
			princess = new Position((byte) rnd.nextInt(height),(byte)  rnd.nextInt(width));
		}
		while(levelMap[goal.i][goal.j]>10 || (princess.i==goal.i && princess.j==goal.j)){
			goal = new Position((byte) rnd.nextInt(height),(byte)  rnd.nextInt(width));
		}
	}
	public int[][] getLevelMap() {
		return levelMap;
	}

	public void setLevelMap(int[][] levelMap) {
		this.levelMap = levelMap;
	}

	public int getRobot1I() {
		return robot1.i;
	}
	public int getRobot1J() {
		return robot1.j;
	}

	public void setRobot1(Position robot1) {
		this.robot1 = robot1;
	}

	public int getRobot2I() {
		return robot2.i;
	}
	public int getRobot2J() {
		return robot2.j;
	}

	public void setRobot2(Position robot2) {
		this.robot2 = robot2;
	}

	public int getPrincessI() {
		return princess.i;
	}
	public int getPrincessJ() {
		return princess.j;
	}

	public int getGoalI() {
		return goal.i;
	}
	public int getGoalJ() {
		return goal.j;
	}
	
	public int getMoves(){
		return minimumSteps;
	}
	public void setPrincess(Position princess) {
		this.princess = princess;
	}
	public boolean isGenerationComplete(){
		return isDone;
	}

	public Position[] getFinalPosition() {
		return finalPosition;
	}
}
