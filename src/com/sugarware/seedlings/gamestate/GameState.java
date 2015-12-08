package com.sugarware.seedlings.gamestate;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.GdxGame;

public abstract class GameState {
	public final GameStateManager gsm;
	public OrthographicCamera cam;
	public int w;
	public int h;
	private static Random random;
	
	public GameState(int w, int h, GameStateManager gsm) {
		this.w = w;
		this.h = h;
		this.gsm = gsm;
		cam = new OrthographicCamera( GdxGame.WIDTH,  GdxGame.HEIGHT);
		
	    cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
	    cam.update();
	}
	
	public GameState( GameStateManager gsm) {
		
		this.gsm = gsm;
		cam = new OrthographicCamera(  GdxGame.WIDTH, GdxGame.HEIGHT);
	    cam.position.set(cam.viewportWidth / 2, cam.viewportHeight / 2, 0);
	    cam.update();
	}
	
	protected Vector2 randomMarginalCoordinate(double margin){
		int location =((random == null ? new Random() : random).nextInt(4));
		double x = 100000000;
		double y = 100000000;
		switch (location){
		case 0: //left
			while(!(0 > x && x >= -margin)){
				x =  ((random == null ? new Random() : random).nextDouble() * ((w + margin) +margin)) -margin;
			}
			
			 y = (h / 2 ) + margin  * Math.sin((h / 2) * x) + (h / 2);
			return new Vector2((float)x, (float) y);
		case 1: //top
			while(!(w > x && x >= 0)){
				x =  ((random == null ? new Random() : random).nextDouble() * ((w + margin) +margin)) -margin;
			}
			 y = (margin / 2) * Math.sin(x) + (margin / 2) + h;
			return new Vector2((float)x, (float) y);
		case 2: //bottom
			while(!(w > x && x >= 0)){
				x =  ((random == null ? new Random() : random).nextDouble() * ((w + margin) +margin)) -margin;
			}
			 y = -((margin / 2) * Math.sin(x) + (margin / 2));
				return new Vector2((float)x, (float) y);
		case 3: //right
			
			while(!(w + margin > x && x >= w)){
				x =  ((random == null ? new Random() : random).nextDouble() * ((w + margin) +margin)) -margin;
			}
			 y = (h / 2 ) + margin  * Math.sin((h / 2) * x) + (h / 2);
			return new Vector2((float)x, (float) y);

		}
		return null;
	}
	
	protected abstract void init();
	protected abstract void draw(SpriteBatch g);
	protected abstract void update();
	protected abstract void unload();
	protected abstract void cursorMoved(Vector3 coords, int pointer);
	protected abstract void touchDown(Vector3 coords, int pointer);
	protected abstract void touchUp(Vector3 coords, int pointer);
	protected abstract void keyPressed(int k);
	protected abstract void keyReleased(int k);

	protected void resize(int width, int height){
		cam = new OrthographicCamera( GdxGame.WIDTH,  GdxGame.HEIGHT);
		
	   
	    cam.update();
	}
	
	public abstract void gatherResources();
	
	

}
