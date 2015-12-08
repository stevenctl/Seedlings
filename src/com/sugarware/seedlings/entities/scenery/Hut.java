package com.sugarware.seedlings.entities.scenery;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.entities.ColEntity;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Hut extends ColEntity{
	float width = 128;
	float height = 128;
	TextureRegion[] frames;
	int HUT = Resources.Images.HUT;
	public boolean demolished = false;
	Animation animation;
	public Hut(PlayGameState gs, float x, float y) {
		super(gs);
		GameStateManager.rm.loadLongImage(HUT);
		animation = new Animation();
		frames = GameStateManager.rm.getImages(HUT).get(0);
		animation.setFrames(frames);
		animation.setDelay(50);
		this.x = x;
		this.y = y;
	}

	@Override
	public void update() {
		if(demolished && !animation.hasPlayedOnce()){
			animation.update();
		}
		
	}

	@Override
	public void draw(SpriteBatch g) {
		
			g.draw(animation.getImage(), x, y + height, width, -height);
		
		
	}

}
