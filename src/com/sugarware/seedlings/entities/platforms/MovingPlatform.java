package com.sugarware.seedlings.entities.platforms;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.ColEntity;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class MovingPlatform extends Platform {
	public boolean UP;
	public boolean RIGHT;
	public float vspeed;
	public float hspeed;
	public float dx;
	public float dy;
	/***
	 * Make sure this goes lower,upper
	 */
	public Vector2 vbounds;
	public Vector2 hbounds;
	
	public MovingPlatform(PlayGameState gs,float x, float y, int width, int height,Vector2 hbounds,Vector2 vbounds,boolean u, boolean r,float hspeed, float vspeed){
		super(gs);
		GameStateManager.rm.loadImages(Resources.Images.PLATFORM_1);
		frames = GameStateManager.rm.getImages(Resources.Images.PLATFORM_1);
		animation.setFrames(frames.get(0));
		UP = u;
		RIGHT = r;
		this.vspeed = vspeed;
		this.hspeed = hspeed;
		this.hbounds = hbounds;
		this.vbounds = vbounds;
		this.width = width;
		this.height = height;
		this.x = x;
		this.y = y;
		dx = 0;
		dy = 0;
		colRect = new Rectangle(x,y,width,height);
	}

	@Override
	public void update() {
		
		
		if(UP){
			dy = vspeed;
		}else{
			dy = -vspeed;
		}
		
		if(RIGHT){
			dx = hspeed;
		}else{
			dx = -hspeed;
		}
		
		if(x+dx + width > hbounds.y){
			RIGHT = false;
			dx = 0;
		}else if(x+dx < hbounds.x){
			RIGHT = true;
			dx = 0;
		}
		
		if(y+dy + height > vbounds.y){
			UP = false;
			dy = 0;
		}else if(y+dy < vbounds.x){
			UP = true;
			dy = 0;
		}
		
		y+=dy;
		x+=dx;
		
		
		colRect.x = x + dx;
		colRect.y = y + dy;
		colRect.width = width;
		colRect.height = height;
		
	}

	@Override
	public void draw(SpriteBatch g) {
		
		g.draw(animation.getImage(), x,y + height + 2,width,-height);
		
	}

	@Override
	public void collide(ArrayList<ColEntity> e) {
		// TODO Auto-generated method stub
		
	}
}
