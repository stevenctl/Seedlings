package com.sugarware.seedlings.entities.platforms;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class FadingPlatform extends MovingPlatform{

	float alpha;
	
	public FadingPlatform(PlayGameState gs, float x, float y, int width,int height) {
		super(gs, x, y, width, height, new Vector2(0,0), new Vector2(0,0), false, false, 0, 0);
		alpha = 1;
		GameStateManager.rm.loadImages(Resources.Images.ICE_PLAT);
		frames = GameStateManager.rm.getImages(Resources.Images.ICE_PLAT);
		animation.setFrames(frames.get(0));
	}
	
	public void update(){
		
		super.update();
		if(alpha > 0)alpha -= 0.0092f;
		else{ killme = true;if(alpha < 0) alpha = 0;}
	}
	
	public void draw(SpriteBatch g){
		g.setColor(1,1,1, alpha);
		g.draw(animation.getImage(), x,y + height,width,-height);
		g.setColor(1,1,1, 1);
	}

}
