
package com.sugarware.seedlings;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;


public class GdxGame implements ApplicationListener , InputProcessor{
	
	public SpriteBatch batch;
	static boolean rec;
	static int c = 0;
	static int f = 0;
	public static int WIDTH;
	public static int HEIGHT;
	public static boolean test = false;
	public GameStateManager gsm;
	
	public boolean LONE = false;
	public TouchController tc;
    public long st;
	
    public GdxGame() {
		LONE = false;
	}

    
    public GdxGame(boolean b) {
		LONE = b;
	}




	@Override
	public void create() {		
	
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		
			float scale = 225 /((float)HEIGHT);
			WIDTH = (int) (WIDTH  * scale);
			HEIGHT = (int) (HEIGHT  * scale);
			System.out.println("Cam is " + WIDTH + " x " + HEIGHT + " " + scale);
			batch = new SpriteBatch();
	if(LONE){
		gsm = new GameStateManager(this, GameStateManager.LONE);
	}else if(!test){	gsm = new GameStateManager(this, GameStateManager.L1);
	}
	else{
		
		gsm = new GameStateManager(this, GameStateManager.D1);
	}	
	
	
		if(Gdx.app.getType() == ApplicationType.Android)tc = new TouchController(this);
		
		else	Gdx.input.setInputProcessor(this);
	}
	
		

	
	GdxGame testmode(){
		test = true;
		return this;
	}
	@Override
	public void dispose() {
		batch.dispose();
	
	}

	@Override
	public void render() {	
		Gdx.graphics.setTitle("Seedlings " + Gdx.graphics.getFramesPerSecond());;
		
		if(System.currentTimeMillis() - st >= 1000l / 64){
				
		gsm.update();
		st = System.currentTimeMillis();
		
	}
			
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(gsm.currentState.cam.combined);
		batch.begin();
		
		gsm.draw(batch);
		if(gsm.currentState instanceof PlayGameState && tc != null )if(((PlayGameState)gsm.currentState).drawGUI)tc.draw(batch);
		batch.end(); 
		
		
	}

	@Override
	public void resize(int width, int height) {
		WIDTH = Gdx.graphics.getWidth();
		HEIGHT = Gdx.graphics.getHeight();
		if(HEIGHT  >  225){
			float scale = 225 /((float)HEIGHT);
			WIDTH = (int) (WIDTH  * scale);
			HEIGHT = (int) (HEIGHT  * scale);
			System.out.println("Cam is " + WIDTH + " x " + HEIGHT + " " + scale);
		}
		gsm.resize(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}
	
	@Override
	public boolean keyDown(int k) {
		
		
		
		if(k == Keys.HOME){
			rec = !rec;
		}
				
		
		gsm.keyPressed(k);
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		gsm.keyReleased(keycode);
		return false;
	}
	
	
	
	    
	  
	 

Vector3 coords;
	
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		System.out.println("Screen: " + screenX +","+screenY);
	if(coords == null)coords = new Vector3();
	coords.set(screenX, screenY, 0);
	gsm.currentState.cam.unproject(coords);
	System.out.println("World: " + coords.x +","+coords.y);
	
	gsm.touchDown(coords, pointer);
	gsm.touchUp(coords, pointer);
	return false;}
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}
	public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}
	public boolean keyTyped(char character) {return false;}
	public boolean mouseMoved(int screenX, int screenY) {return false;}
	public boolean scrolled(int amount) {return false;}

	
}
