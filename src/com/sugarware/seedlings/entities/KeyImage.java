package com.sugarware.seedlings.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class KeyImage extends ColEntity {

	TextureRegion img;
	
	public KeyImage(PlayGameState gs, int x, int y, int w, int h, String c) {
		super(gs);
		this.x = x; this.cwidth = w; this.cheight = h; this.y = y;
		img = new TextureRegion(
				new Texture(Gdx.files.internal(c + "key.png"))
				);
		
	}

	@Override
	public void update() {
         
	}

	@Override
	public void draw(SpriteBatch g) {
		g.draw(img, x, y,cwidth, cheight);
	}

}
