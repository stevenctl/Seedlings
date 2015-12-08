package com.sugarware.seedlings.entities;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class LeafCoin extends ColEntity {

	final int size = 20;
	TextureRegion img;
	
	public LeafCoin(PlayGameState gs, float x, float y) {
		super(gs);
		this.x = x;
		this.y = y;
		cwidth = cheight = size;
		this.colRect.height = this.colRect.width = size;
		GameStateManager.rm.loadImages(Resources.Images.LEAF2);
		img = GameStateManager.rm.getImages(Resources.Images.LEAF2).get(0)[0];
	}

	
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		this.colRect.setPosition(x, y);
		if(this.colRect.overlaps(gs.p.colRect)){
			gs.score++;
			killme = true;
		}
	}

	@Override
	public void draw(SpriteBatch g) {
		g.draw(img, x,y +size,size,- size);
	}

}
