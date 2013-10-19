package com.walrus.game;

import com.walrus.framework.Image;

public class Tile {

	private int height, width;
	private Image tileImage;
	
	public Tile(Image tile){
		tileImage = tile;
		height = tile.getHeight();
		width = tile.getWidth();
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Image getTileImage() {
		return tileImage;
	}

	public void setTileImage(Image tileImage) {
		this.tileImage = tileImage;
	}
}
