package com.sugarware.seedlings.gamestate;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.lone.LoneLevel;
import com.sugarware.seedlings.GdxGame;
import com.sugarware.seedlings.ResourceManager;
import com.sugarware.seedlings.gamestate.partone.Boss1;
import com.sugarware.seedlings.gamestate.partone.Cutscene1;
import com.sugarware.seedlings.gamestate.partone.DemoLevel;
import com.sugarware.seedlings.gamestate.partone.Level1;
import com.sugarware.seedlings.gamestate.partone.Level2;
import com.sugarware.seedlings.gamestate.partone.Level3;
import com.sugarware.seedlings.gamestate.partone.TestingLevel;

public class GameStateManager {

	
	public boolean paused;
	
	public GameState currentState;
	public int currentStateIndex;
	public GdxGame game;
	BitmapFont f;
	public static ResourceManager rm;
	OrthographicCamera cam;
	public GameState nextState;
	public int nextStateIndex;
	
	public static final int L1 = 1;//normal levels
	public static final int L2 = 2;
	public static final int L3 = 3;
	public static final int C1 = 5;//cutscene
	public static final int B1 = 6;//boss level
	public static final int TEST = 99;
	public static final int D1 = 4;//demo level
	//Lone
	public static final int LONE = -1;
	public GameStateManager(GdxGame game,int index) {
		this.game = game;
		rm = new ResourceManager();
		currentStateIndex = index;
		cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2,0);
		cam.update();
		f = new BitmapFont();
		setState(index);
		
	
	}
		
		
	
	public void setState(int index){
		
		
		try{
		currentStateIndex = index;
		if(currentState!=null)currentState.unload();
		currentState = stateLookup(index);
		currentState.gatherResources();
		currentState.init();
		
		} catch (Exception e) {
			e.printStackTrace();
			Gdx.app.exit();
		}
	}
	
	public void draw(SpriteBatch g){
		currentState.draw(g);
		if(currentState instanceof PlayGameState)if(!((PlayGameState) currentState).inited){
			g.setProjectionMatrix(cam.combined);
			if(!g.isDrawing())g.begin();
			g.draw(rm.loadbg,0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
			
		}
		if(paused)f.draw(g, "PAUSED", currentState.cam.position.x - 10, currentState.cam.position.y);
		f.draw(g, "FPS: " + String.valueOf(Gdx.graphics.getFramesPerSecond()),  currentState.cam.position.x -  currentState.cam.viewportWidth / 2 + 4, currentState.cam.position.y -  currentState.cam.viewportHeight / 2 + 16);
	
	}
	
	
	public void keyPressed(int k){
		if(k == Keys.ESCAPE)paused = !paused;
		if(!paused && !(currentState instanceof PlaybackState))currentState.keyPressed(k);
		
	}
	
	public void keyReleased(int k){
		if(!(currentState instanceof PlaybackState))currentState.keyReleased(k);
	}
	
	public void touchDown(Vector3 coords, int p){
		currentState.touchDown(coords, p);
	}
	
	public void touchUp(Vector3 coords, int p){
		currentState.touchUp(coords, p);
	}
	
	public void update(){
		if(!paused)currentState.update();
	}


	public void resize(int width, int height) {
		if(currentState != null)currentState.resize(width, height);
		
	}
	
	public void nextState(){
		nextState = stateLookup(nextStateIndex);
		currentStateIndex = nextStateIndex;
		nextStateIndex = -1;
		currentState.unload();
	
		currentState = nextState;
		currentState.gatherResources();
		currentState.init();
		nextState = null;
		System.out.println((currentState instanceof DemoLevel) + "?");
	}
	
	public void setNextState(int i){
		
		nextStateIndex = i;
		
	}
	
	public GameState stateLookup(int index){
		switch(index){
		case L1:
			return new Level1(this);
			
		case B1: 
			return new Boss1(this);
			
		case L2: 
			
			return new Level2(this);
			
		case L3:
			return new Level3(this);
		case D1:
			return new DemoLevel(this);
		case C1:
			return new Cutscene1(this);
		case LONE:
			return new LoneLevel(this);
		case TEST: 
			return new TestingLevel(this);
		default:
			currentState = null; System.out.println("Invalid state index.");
			return null;
		}
	}
	

}
