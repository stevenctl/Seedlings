package com.sugarware.seedlings.entities.platforms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class VertPlatform extends Platform{

	
	//Platforms that you can jump up through, but not fall down through. Represented by a polyline named "plat" on 
	//the Tiled map
	
	public VertPlatform(PlayGameState gs, float x, float y, int w ) {
		super(gs);
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = 2;
	}

	@Override
	public void update() {
	
	}

	@Override
	public void draw(SpriteBatch g) {

	}

}
