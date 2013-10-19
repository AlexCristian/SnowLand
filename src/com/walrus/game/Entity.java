package com.walrus.game;

import com.walrus.framework.Image;

public class Entity {
	private int posi, posj, realX, realY;
	private Image[] characterImage; 
	private Image avatar;
	private int targetI=-1, targetJ=-1;
	public enum Orientation{
		North, South, East, West
	}
	public enum Action{
		Resting, Sliding
	}
	private int inci[]={1,-1,0,0}, incj[]={0,0,1,-1};
	private Orientation facing = Orientation.South;
	private Action status = Action.Resting;
	private int offsetX, offsetY;
	public Entity(Image[] im, Image avt, int i, int j, int offstX, int offstY){
		characterImage = im;
		avatar=avt;
		posi=i;
		posj=j;
		offsetX=offstX;
		offsetY=offstY;
	}
	public int getIncrementI(){
		return inci[facing.ordinal()];
	}
	public int getIncrementJ(){
		return incj[facing.ordinal()];
	}
	public int getPosi() {
		return posi;
	}
	public void setPosi(int posi) {
		this.posi = posi;
	}
	public int getPosj() {
		return posj;
	}
	public void setPosj(int posj) {
		this.posj = posj;
	}
	public Image getCharacterImage() {
		return characterImage[facing.ordinal()+1];
	}
	public void setCharacterImage(Image[] characterImage) {
		this.characterImage = characterImage;
	}
	public Orientation getFacing() {
		return facing;
	}
	public void setFacing(Orientation facing) {
		this.facing = facing;
	}
	public int getRealX() {
		return realX;
	}
	public void setRealX(int realX) {
		this.realX = realX;
	}
	public int getRealY() {
		return realY;
	}
	public void setRealY(int realY) {
		this.realY = realY;
	}
	public int getHeight() {
		return characterImage[facing.ordinal()+1].getHeight();
	}
	public int getWidth() {
		return characterImage[facing.ordinal()+1].getWidth();
	}
	public Action getStatus() {
		return status;
	}
	public void setStatus(Action status) {
		this.status = status;
	}
	public int getOffsetX() {
		return offsetX;
	}
	public void setOffsetX(int offsetX) {
		this.offsetX = offsetX;
	}
	public int getOffsetY() {
		return offsetY;
	}
	public void setOffsetY(int offsetY) {
		this.offsetY = offsetY;
	}
	public Image getAvatar() {
		return avatar;
	}
	public void setAvatar(Image avatar) {
		this.avatar = avatar;
	}
	public int getTargetI() {
		return targetI;
	}
	public void setTargetI(int targetI) {
		this.targetI = targetI;
	}
	public int getTargetJ() {
		return targetJ;
	}
	public void setTargetJ(int targetJ) {
		this.targetJ = targetJ;
	}
}
