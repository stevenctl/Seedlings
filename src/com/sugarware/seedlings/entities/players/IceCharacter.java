package com.sugarware.seedlings.entities.players;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.entities.platforms.FadingPlatform;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;


public class IceCharacter extends Player {

	
	//Animation Indicies
	public static final int IDLE = 0;
	public static final int RUN = 1;
	public static final int JUMP = 2;
	public static final int FALL = 3;
	//states
	
	
	public IceCharacter(PlayGameState ps, float x, float y) {
		super(ps, x, y);
		animation = new Animation();
		currentAction = IDLE;
		
		GameStateManager.rm.loadImages(Resources.Images.ICE_SEED);
		frames = GameStateManager.rm.getImages(Resources.Images.ICE_SEED);
		animation.setFrames(frames.get(currentAction));
	}
	
	public IceCharacter(PlayGameState ps, Player pl){
		super(ps, pl);
		GameStateManager.rm.loadImages(Resources.Images.ICE_SEED);
		frames = GameStateManager.rm.getImages(Resources.Images.ICE_SEED);
		animation.setFrames(frames.get(currentAction));
		
	}
	
	public void update(){
		super.update();
		if(inWater || ridingPlatform){
			decel = 0.025f;
			accel = 0.025f;
		}else{
			decel = defaccel;
			accel = defaccel;
		}
		
		
		
		
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
		if(currentAction == JUMP /*|| otherstate*/){
			if(!animation.hasPlayedOnce()){
				animation.update();
			}else{
			
				animation.setFrame(animation.getNumFrames() - 1);
			}
			
		}else{ 
		animation.update();
		
		}
		
	}
	
	public void draw(SpriteBatch g) {
	
		super.draw(g);
		g.setColor(1f, 1.0f, 1.0f, alpha);
		if (facingRight) {	
			if(animation.getImage() != null)g.draw(animation.getImage(), x -cwidth /4 ,y + height , width, -height);	
		} else {
			if(animation.getImage() != null)g.draw(animation.getImage(),  x + width -cwidth / 4 , y + height ,-width, -height);
		}
		
		g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	
		
	}
	
	public void keyPressed(int k){
		super.keyPressed(k);
		if(k == Keys.J)add(new FadingPlatform(gs,x - 10,y-33,48,32));
		
	}
	
	public void keyReleased(int k){
		super.keyReleased(k);
		
		
	}
	
	public void onTouchDown(float x, float y){
		super.onTouchDown(x, y);
	}

}
