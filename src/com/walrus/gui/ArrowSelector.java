package com.walrus.gui;

import com.walrus.framework.Image;

public class ArrowSelector {

	private Image arrow;
	private int arrowX, arrowY, clickedOffset=0, inc;
	
	public ArrowSelector(Image arrowImage, int x, int y, int incrementation){
		arrow=arrowImage;
		arrowX=x;
		arrowY=y;
		inc=incrementation;
	}

	public Image getArrow() {
		return arrow;
	}

	public void setArrow(Image arrow) {
		this.arrow = arrow;
	}

	public int getArrowX() {
		if(clickedOffset>0)
			clickedOffset--;
		return arrowX+clickedOffset*inc;
	}

	public void setArrowX(int arrowX) {
		this.arrowX = arrowX;
	}

	public int getArrowY() {
		return arrowY;
	}

	public void setArrowY(int arrowY) {
		this.arrowY = arrowY;
	}

	public int getClickedOffset() {
		return clickedOffset;
	}

	public void setClickedOffset(int clickedOffset) {
		this.clickedOffset = clickedOffset;
	}
}
