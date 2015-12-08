package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.gamestate.PlayGameState;

public abstract class Entity {

	public float x;
	public float y;
	public PlayGameState gs;
	
	public Entity(PlayGameState gs) {
		this.gs = gs;
	}
	
	public abstract void update();
	public abstract void draw(SpriteBatch g);

}
