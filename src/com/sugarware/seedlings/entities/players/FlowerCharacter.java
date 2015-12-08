package com.sugarware.seedlings.entities.players;
import java.util.ArrayList;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;


public class FlowerCharacter extends Player {

	
	//Animation Indicies
	public static final int IDLE = 0;
	public static final int RUN = 1;
	public static final int JUMP = 2;
	public static final int FALL = 3;
	public static final int SLOWFALL = 4;
	//states
	boolean FLOAT;
	
	//
	float defFallspeed;
	float adjFallspeed = 1f;
	
	public FlowerCharacter(PlayGameState ps, float x, float y) {
		super(ps, x, y);
		animation = new Animation();
		currentAction = IDLE;
		FLOAT = false;
		GameStateManager.rm.loadImages(Resources.Images.FLOWER_SEED);
		frames = GameStateManager.rm.getImages(Resources.Images.FLOWER_SEED);
		
		animation.setFrames(frames.get(currentAction));
		defFallspeed = maxfallspeed;
		
	}
	
	
	public FlowerCharacter(PlayGameState ps, Player pl) {
		super(ps,pl);
		defFallspeed = maxfallspeed;
		GameStateManager.rm.loadImages(Resources.Images.FLOWER_SEED);
		frames = GameStateManager.rm.getImages(Resources.Images.FLOWER_SEED);
		animation.setFrames(frames.get(currentAction));
		
	}
	
	public void update(){
		super.update();
		
		if(FLOAT && dy < 0)maxfallspeed = adjFallspeed;
		else maxfallspeed = defFallspeed;
		
		if(dx == 0 && dy == 0 && !slopeadj && !wasslope && !jumping){
			
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(frames.get(currentAction));
				animation.setDelay(-1);
			}
		}else if(dx != 0 && ( dy == 0|| ridingPlatform || slopeadj || wasslope) &&  (LEFT || RIGHT)&& !jumping){
			if(currentAction != RUN){
				currentAction = RUN;
				animation.setFrames(frames.get(currentAction));
				animation.setDelay(16);
			}
		}else if(jumping){
			if(currentAction != JUMP){
				currentAction = JUMP;
				animation.setFrames(frames.get(JUMP));
				animation.setDelay(5); 
			}else{
				
			}
		}else if(dy < 0 && !ridingPlatform && maxfallspeed == adjFallspeed){
			if(currentAction != SLOWFALL){
				currentAction = SLOWFALL;
				animation.setFrames(frames.get(SLOWFALL));
				animation.setDelay(40);
			}
		}else if(dy < 0 && !ridingPlatform && !wasslope && !slopeadj ){
			if(currentAction != FALL){
				currentAction = FALL;
				animation.setFrames(frames.get(FALL));
				animation.setDelay(-1);
			}
		}else{
			
			if(currentAction != IDLE){
				currentAction = IDLE;
				animation.setFrames(frames.get(currentAction));
				animation.setDelay(-1);
			}
		}
		
		
		
		//all the things that stop on last one
		if(currentAction == JUMP || currentAction == SLOWFALL){
			if(!animation.hasPlayedOnce()){
				animation.update();
			}else{
			
				animation.setFrame(animation.getNumFrames() - 1);
			}
			
		}else{ 
		animation.update();
		}

	}
	
	ArrayList<TextureRegion[]> unhappyFrames;
	public void draw(SpriteBatch g) {
	
		super.draw(g);
		g.setColor(1.0f, 1.0f, 1.0f, alpha);
		
		if(unhappy){
			if(unhappyFrames == null){
				GameStateManager.rm.loadImages(Resources.Images.FLOWER_UNHAPPY);
				unhappyFrames = GameStateManager.rm.getImages(Resources.Images.FLOWER_UNHAPPY);
			}
			
			animation.setFrames(unhappyFrames.get(currentAction), animation.getFrame());
		}else{
			animation.setFrames(frames.get(currentAction), animation.getFrame());
		}
		
		g.setColor(1.0f, 1.0f, 1.0f, alpha);
		if (facingRight) {	
			if(animation.getImage() != null)g.draw(animation.getImage(), x -cwidth /4 ,y + height , width, -height);	
		} else {
			if(animation.getImage() != null)g.draw(animation.getImage(),  x + width -cwidth / 4 , y + height ,-width, -height);
		}
		g.setColor(1.0f, 1.0f, 1.0f, 1);
	/*	g.end();
		s.setProjectionMatrix(gs.cam.combined);
		s.setColor(Color.RED);
		s.begin(ShapeType.Line);
		s.line(x, y + cheight, x + cwidth,y + cheight);
		s.line(x, y, x + cwidth,y);
		s.line(x, y, x, y + cheight);
		s.line(x + cwidth, y, x + cwidth,y + cheight);
		s.end();
		g.begin();*/
			
		
	}
	
	public void keyPressed(int k){
		super.keyPressed(k);
		if(Gdx.app.getType() == ApplicationType.Android)FLOAT = gs.gsm.game.tc.isDown(Keys.SPACE) ||gs.gsm.game.tc.isDown(Keys.J);
		
		else FLOAT = Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.J);
	}
	
	public void keyReleased(int k){
		super.keyReleased(k);
		if(Gdx.app.getType() == ApplicationType.Android)FLOAT = gs.gsm.game.tc.isDown(Keys.SPACE) ||gs.gsm.game.tc.isDown(Keys.J);
		else FLOAT = Gdx.input.isKeyPressed(Keys.SPACE) || Gdx.input.isKeyPressed(Keys.J);
	}
	
	public void onTouchDown(float x, float y){
		super.onTouchDown(x, y);
	}

}
