package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

//Controlls image cycling for animatons.

public class Animation {
	
	
	private TextureRegion[] frames;
	private int curFrame;
	
	private long startTime;
	private long delay;
	
	private boolean playOnce;
	
	public Animation() {
		playOnce = false;
	}
	
	public void setFrames(TextureRegion[] frames) {
		
		 this.frames = frames;
		 curFrame = 0;
		 startTime = System.nanoTime();
		 playOnce = false;
	}
	
	public void setFrames(TextureRegion[] frames, int f){
		 this.frames = frames;
		 //playOnce = false;
	}
	
	public void setDelay(long d) { delay = d;}
	public void setFrame(int i) { curFrame = i;}
	
	public void update(){
		if(delay == -1 ) return; //NO ANIMATION
		
		long elapsed = (System.nanoTime() - startTime) / 1000000;
		if (elapsed > delay) {
			curFrame++;
			
			startTime = System.nanoTime();
		}
		if(frames != null){
		if(curFrame == frames.length - 1){
			playOnce = true;
		}
		if(curFrame == frames.length){
			curFrame = 0;
			
			
		}
		}
		
	}
	
	public TextureRegion[] getFrames(){return frames;}
	public int getFrame() {return curFrame;}
	public int getNumFrames(){return frames.length;}
	public TextureRegion getImage() {if(frames != null) {return frames[curFrame];}else{System.out.println("NO IMAGE!");return null;}}
	public boolean hasPlayedOnce(){return playOnce;}
	public void setPlayedOnce(boolean b){playOnce = b;}
	public long getDelay() {return delay;}
	
}
