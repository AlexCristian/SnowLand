package com.walrus.game;

public class TileMatrixFactory {

	public static Tile[][] transformToTiles(int[][] level){
		Tile[][] tiles = new Tile[level.length][level[0].length];		
		
		for(int i=0; i<level.length; i++){
			for(int j=0; j<level[i].length; j++){
				tiles[i][j] = new Tile(Assets.tiles[level[i][j]]);
				if(level[i][j]>10)
					tiles[i][j].setHeight(tiles[i][j].getHeight()-10);
			}
		}
		
		return tiles;
	}
}
