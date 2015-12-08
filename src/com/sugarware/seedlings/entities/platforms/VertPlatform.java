package com.sugarware.seedlings.entities.platforms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class VertPlatform extends Platform{

	
	
	
	public VertPlatform(PlayGameState gs, float x, float y, int w ) {
		super(gs);
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = 2;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void draw(SpriteBatch g) {
		// TODO Auto-generated method stub
		
	}

}
