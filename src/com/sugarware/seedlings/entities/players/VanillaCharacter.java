package com.sugarware.seedlings.entities.players;
import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;


public class VanillaCharacter extends Player {

	
	//Animation Indicies
	public static final int IDLE = 0;
	public static final int RUN = 1;
	public static final int JUMP = 2;
	public static final int FALL = 3;
	public static final int SWING = 4;
	//states
	
	
	public VanillaCharacter(PlayGameState ps, float x, float y) {
		super(ps, x, y);
		animation = new Animation();
		currentAction = IDLE;
		
		GameStateManager.rm.loadImages(Resources.Images.VANILLA_SEED);
		frames = GameStateManager.rm.getImages(Resources.Images.VANILLA_SEED);
		animation.setFrames(frames.get(currentAction));
		/*transition = new Animation();
		gs.gsm.rm.loadLongImage(Resources.Images.VANILLA_TRANSITION);
		transframes = gs.gsm.rm.getImages(Resources.Images.VANILLA_TRANSITION).get(0);
		transition.setFrames(transframes, 11);
		transition.setDelay(18);*/
	}
	
	public VanillaCharacter(PlayGameState ps, Player pl){
		super(ps, pl);
		GameStateManager.rm.loadImages(Resources.Images.VANILLA_SEED);
		frames = GameStateManager.rm.getImages(Resources.Images.VANILLA_SEED);
		animation.setFrames(frames.get(currentAction));
		transition = new Animation();
		GameStateManager.rm.loadLongImage(Resources.Images.VANILLA_TRANSITION);
		transition.setFrames(GameStateManager.rm.getImages(Resources.Images.VANILLA_TRANSITION).get(0));
		transition.setDelay(18);
		
	}
	
	
	@Override
	public void alphaUpdate(){
		if(oldanim !=null ){
			
			if(oldalpha>0)if(transition != null)if(transition.getFrame() == 34)oldalpha = 0;
			if(oldalpha<=0)oldanim = null;
			
		}
		if(transition != null){
			
			if(transition.hasPlayedOnce())transition = null;
			else transition.update();
		}
		if(alpha<1 && !dead)if(transition!=null)if(transition.getFrame() == 34)alpha = 1;
		if(alpha > 1)alpha = 1;
	}
	
	public void update(){

		super.update();
		
		
		
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
		}else if(ropeSwing){
			if(currentAction != SWING){
				currentAction = SWING;
				animation.setFrames(frames.get(SWING));
				animation.setDelay(-1);
			}else{
				if(myrope.r ){
					animation.setFrame(facingRight ?  1 : 0);
				}else{
					animation.setFrame(facingRight ? 0 : 1);
				}
			}
		}else if(jumping){
			if(currentAction != JUMP){
				currentAction = JUMP;
				animation.setFrames(frames.get(JUMP));
				animation.setDelay(5); 
			}else{
				
			}
		}else if(dy < 0 && !ridingPlatform && !wasslope && !slopeadj){
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
	
	ArrayList<TextureRegion[]> unhappyFrames;
	public void draw(SpriteBatch g) {
	
		super.draw(g);
		g.setColor(1.0f, 1.0f, 1.0f, alpha);
		
		if(unhappy){
			if(unhappyFrames == null){
				GameStateManager.rm.loadImages(Resources.Images.VANILLA_UNHAPPY);
				unhappyFrames = GameStateManager.rm.getImages(Resources.Images.VANILLA_UNHAPPY);
			}
			
			animation.setFrames(unhappyFrames.get(currentAction), animation.getFrame());
		}else{
			animation.setFrames(frames.get(currentAction), animation.getFrame());
		}
		
		if (facingRight) {	
			if(animation.getImage() != null)g.draw(animation.getImage(), x -cwidth /4 ,y + height , width, -height);	
		} else {
			if(animation.getImage() != null)g.draw(animation.getImage(),  x + width -cwidth / 4 , y + height ,-width, -height);
		}
		
		g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
	
		
			if(transition!=null){
			
				if (facingRight) {	
					g.draw(transition.getImage(), x - 24 - cwidth / 4 - 12, y  - 1 + 106 ,96 + 24,-106 );
				} else {
					g.draw(transition.getImage(), x - 24 - cwidth / 4  + 96 + 8, y  - 1 + 106 ,-96 - 16,-106 );
					System.out.println("???");
				}
			}
		
	}
	
	public void keyPressed(int k){
		super.keyPressed(k);
	}
	
	public void keyReleased(int k){
		super.keyReleased(k);
		
		
	}
	
	public void onTouchDown(float x, float y){
		super.onTouchDown(x, y);
	}

}
