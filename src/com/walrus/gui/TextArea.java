package com.walrus.gui;

import com.walrus.game.Assets;

import android.graphics.Paint;


public class TextArea {
	
	private int txtX, txtY, origX;
	private String text;
	private Paint paint;
	public TextArea(int x, int y, String txt, Paint p){
		paint = p;
		txtY = y;
		text=txt;
		origX=x;
		txtX =(int) (x-paint.measureText(txt)/2);
	}
	
	

	public String getText() {
		return text;
	}



	public void setText(String text) {
		this.text = text;
	}




	public int getTxtX() {
		txtX =(int) (origX-paint.measureText(text)/2);
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


	
	
	
}
