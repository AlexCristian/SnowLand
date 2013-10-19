package com.walrus.core;

import com.walrus.game.Entity;
import com.walrus.game.Entity.Orientation;

public class Move {

	private Entity entity;
	private Orientation orientation;
	private int positionI, positionJ;
	public Move(Entity subject, Orientation where){
		entity = subject;
		orientation = where;
		positionI = entity.getPosi();
		positionJ = entity.getPosj();
	}
	public Entity getEntity() {
		return entity;
	}
	public void setEntity(Entity entity) {
		this.entity = entity;
	}
	public Orientation getOrientation() {
		return orientation;
	}
	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}
	public int getPositionI() {
		return positionI;
	}
	public void setPositionI(int positionI) {
		this.positionI = positionI;
	}
	public int getPositionJ() {
		return positionJ;
	}
	public void setPositionJ(int positionJ) {
		this.positionJ = positionJ;
	}
	
}
