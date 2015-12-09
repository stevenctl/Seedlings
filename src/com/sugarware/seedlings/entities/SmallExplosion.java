package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class SmallExplosion extends ColEntity {

	
	//TODO Combine with regular explosion class, change the image and width/height
	
	private Animation animation;
	int width = 32;
	int height = 32;
	
	public SmallExplosion(PlayGameState gs, float x, float y) {
		super(gs);
		this.x = x;
		this.y = y;
		animation = new Animation();
		GameStateManager.rm.loadImages(Resources.Images.EXPLOSION_2);
		animation.setFrames(GameStateManager.rm.getImages(Resources.Images.EXPLOSION_2).get(0));
		animation.setDelay(20);
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		killme = animation.hasPlayedOnce();
		
		if(!killme)animation.update();
	}

	@Override
	public void draw(SpriteBatch g) {
		g.draw(animation.getImage(), x, y + height, width, -height);
		
	}
	
	

}
