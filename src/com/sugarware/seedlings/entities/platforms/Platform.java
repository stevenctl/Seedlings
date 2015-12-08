package com.sugarware.seedlings.entities.platforms;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.entities.ColEntity;
import com.sugarware.seedlings.gamestate.PlayGameState;

public abstract class Platform extends ColEntity {

	public int width;
	public int height;
	
	protected Animation animation;
	protected ArrayList<TextureRegion[]> frames;
	
	public Platform(PlayGameState gs) {
		super(gs);
		animation = new Animation();
		
	}

	

}
