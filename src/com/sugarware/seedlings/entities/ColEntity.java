package com.sugarware.seedlings.entities;

import java.util.ArrayList;

import com.badlogic.gdx.math.Rectangle;
import com.sugarware.seedlings.gamestate.PlayGameState;

public abstract class ColEntity extends Entity {

	
	public int cwidth;
	public int cheight;
	public Rectangle colRect;
	public boolean killme = false;
	public ColEntity(PlayGameState gs) {
		super(gs);
		colRect = new Rectangle();
	}
	
	public  void collide(ArrayList<ColEntity> e){
		colRect.x = x;
		colRect.y = y;
		colRect.width = cwidth;
		colRect.height = cheight;
	}
	
	

}
