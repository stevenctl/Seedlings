package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Light extends ColEntity {

	
	public boolean flicker = false;
	
	public Light(PlayGameState gs, float x, float y) {
		super(gs);
		this.x =x; this.y = y;
		System.out.println("L: " + x +", " + y);
	}

	@Override
	public void update() {}

	@Override
	public void draw(SpriteBatch g) {}

}
