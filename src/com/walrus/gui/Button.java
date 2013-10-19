package com.walrus.gui;

import android.graphics.Paint;

import com.walrus.framework.Image;
import com.walrus.game.Assets;


public class Button {
	
	private int imgX, imgY, txtX, txtY;
	private Image button = Assets.button, buttonDown = Assets.buttonDown;
	private String text;
	private Paint paint;
	private boolean touchedDown=false;

	public Button(int x, int y, String txt, Paint p){
		paint = p;
		imgX = x;
		imgY = y;
		text=txt;
		txtY = imgY + button.getHeight()-20;
		txtX = (int) (imgX + (button.getWidth()/2) - paint.measureText(txt)/2);
		
	}
	
	public Button(int x, int y){
		imgX = x;
		imgY = y;
	}
	
	public void update() {
		
	}



	public Image getButton() {
		if(touchedDown) 
			return buttonDown;
		return button;
	}

	public void setButton(Image button) {
		this.button = button;
	}



	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}



	public int getImgX() {
		return imgX;
	}



	public void setImgX(int imgX) {
		this.imgX = imgX;
	}



	public int getImgY() {
		return imgY;
	}



	public void setImgY(int imgY) {
		this.imgY = imgY;
	}



	public int getTxtX() {
		return txtX;
	}



	public void setTxtX(int txtX) {
		this.txtX = txtX;
	}



	public int getTxtY() {
		return txtY;
	}



	public void setTxtY(int txtY) {
		this.txtY = txtY;
	}



	public Paint getPaint() {
		return paint;
	}



	public void setPaint(Paint paint) {
		this.paint = paint;
	}

	public boolean isTouchedDown() {
		return touchedDown;
	}

	public void setTouchedDown(boolean touchedDown) {
		this.touchedDown = touchedDown;
	}

	public void setButtonDown(Image buttonDown) {
		this.buttonDown = buttonDown;
	}


	
	
	
}
