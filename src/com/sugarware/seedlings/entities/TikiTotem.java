package com.sugarware.seedlings.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.gamestate.PlayGameState;

//Will shoot fireballs. Not yet implemented.

public class TikiTotem extends ColEntity {

	public boolean facingRight;
	public int maxTime;
	public int timer;
	public boolean shooting;
	private Animation animation;
	private ArrayList<TextureRegion[]> frames;
	public TikiTotem(PlayGameState gs, float x, float y, boolean r, int maxT) {
		super(gs);
		this.x = x;
		this.y = y;
		facingRight = r;
		animation = new Animation();
		animation.setFrames(frames.get(0));
		animation.setDelay(-1);
		timer = maxT;
		maxTime = maxT;
	}

	@Override
	public void update() {
		timer--;
		if(timer < 0){
			shoot();
			timer = maxTime;
		}
	}
	
	void shoot(){
		shooting  = true;
	}

	@Override
	public void draw(SpriteBatch g) {
		// TODO Auto-generated method stub
		
	}

}
