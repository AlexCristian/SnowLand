package com.walrus.gui;

import com.walrus.framework.Image;
import com.walrus.game.Assets;


public class SlidingBackground {
	
	private int bgX, bgY, speedY, reset;
	private Image back;
	
	public SlidingBackground(int x, int y, int sY, Image background){
		bgX = x;
		bgY = y;
		speedY = sY;
		back=background;
		reset=back.getHeight()*2;
	}
	
	public void update() {
		bgY -= speedY;
		
		if (bgY <= -back.getHeight()){
			bgY += reset;
		}
	}

	public int getBgX() {
		return bgX;
	}

	public int getBgY() {
		return bgY;
	}

	public int getSpeedY() {
		return speedY;
	}

	public void setBgX(int bgX) {
		this.bgX = bgX;
	}

	public void setBgY(int bgY) {
		this.bgY = bgY;
	}

	public void setSpeedY(int speedY) {
		this.speedY = speedY;
	}

	public Image getBack() {
		return back;
	}

	public void setBack(Image back) {
		this.back = back;
	}

	
	
	
}
