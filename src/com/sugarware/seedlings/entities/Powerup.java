
package com.sugarware.seedlings.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Powerup extends ColEntity {
	public static enum PowerType{
		VANILLA, FLOWER, FIRE
	}
	
	
	public PowerType type;
	
	private Animation animation;
	private ArrayList<TextureRegion[]> frames;
	
	public int width;
	public int height;
	
	
	public Powerup(PlayGameState gs, float x, float y, PowerType type){
		super(gs);
		this.x = x;
		this.y = y;
		this.type = type;
		animation = new Animation();
		
		switch(type){
		case VANILLA:
			GameStateManager.rm.loadImages(Resources.Images.TESTSEED);
			frames = GameStateManager.rm.getImages(Resources.Images.TESTSEED);
			
			break;
			
		case FLOWER:
			GameStateManager.rm.loadImages(Resources.Images.TESTSEED);
			frames = GameStateManager.rm.getImages(Resources.Images.TESTSEED);
			
			break;
		case FIRE:
			GameStateManager.rm.loadImages(Resources.Images.TESTSEED);
			frames = GameStateManager.rm.getImages(Resources.Images.TESTSEED);
		default:
			break;
		}
		
		colRect = new Rectangle();
		colRect.x = x;
		colRect.y = y;
		animation.setFrames(frames.get(0));
		animation.update();
		width = frames.get(0)[0].getRegionWidth();
		height = frames.get(0)[0].getRegionHeight();
		colRect.width = width;
		colRect.height = height;
	}



	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void draw(SpriteBatch g) {
		g.draw(animation.getImage(), x, y+height,width, -height);
		
	}



	@Override
	public void collide(ArrayList<ColEntity> e) {
		// TODO Auto-generated method stub
		
	}}
