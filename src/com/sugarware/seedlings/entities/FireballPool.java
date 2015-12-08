package com.sugarware.seedlings.entities;
import com.badlogic.gdx.utils.Pool;
import com.sugarware.seedlings.gamestate.PlayGameState;
public class FireballPool extends Pool<Fireball> {

	PlayGameState gs;
	
	public FireballPool(PlayGameState ps){
		gs = ps;
	}
	
	@Override
	protected Fireball newObject() {
		// TODO Auto-generated method stub
		return new Fireball(gs);
	}

}
