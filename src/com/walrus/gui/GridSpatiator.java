package com.walrus.gui;

public class GridSpatiator {

	int posX, posY;
	public GridSpatiator(int x, int y){
		posX = x;
		posY = y;
		
		
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
	public void nextElement(){
		posY+=70;
	}
}
