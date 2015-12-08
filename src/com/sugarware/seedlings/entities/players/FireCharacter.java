package com.sugarware.seedlings.entities.players;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.entities.Animation;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;


public class FireCharacter extends Player {

	
	//Animation Indices
	public static final int IDLE = 0;
	public static final int RUN = 1;
	public static final int JUMP = 2;
	public static final int FALL = 3;
	public static final int SHOOT = 4;
	public static final int JUMPSHOOT = 5;
	public static final int FALLSHOOT = 6;
	public static final int RUNSHOOT = 7;
	//states
	public boolean shooting;
	int shootcooldown;
	boolean canshoot;
	public boolean shootball;
	
	public FireCharacter(PlayGameState ps, float x, float y) {
		super(ps, x, y);
		animation = new Animation();
		currentAction = IDLE;
		
		
		frames = GameStateManager.rm.getImages(Resources.Images.FIRE_SEED);
		animation.setFrames(frames.get(currentAction));
		transition = new Animation();
		GameStateManager.rm.loadLongImage(Resources.Images.FIRETRANSITION);
		transframes = GameStateManager.rm.getImages(Resources.Images.FIRETRANSITION).get(0);
		transition.setFrames(transframes, 11);
		transition.setDelay(13);
	}
	
	public FireCharacter(PlayGameState ps, Player pl){
		super(ps, pl);
		GameStateManager.rm.loadImages(Resources.Images.FIRE_SEED);
		frames = GameStateManager.rm.getImages(Resources.Images.FIRE_SEED);
		animation.setFrames(frames.get(currentAction));
		transition = new Animation();
		GameStateManager.rm.loadLongImage(Resources.Images.FIRETRANSITION);
		transition.setFrames(GameStateManager.rm.getImages(Resources.Images.FIRETRANSITION).get(0));
		transition.setDelay(13);
	}
	
	public void update(){
		

		super.update();
		
		if(inWater){dead = true;
		alpha = 1;}
		if(!canshoot){
			shootcooldown--;
			if(shootcooldown < 0)canshoot = true;		
		}
		
		
		if(dx == 0 && dy == 0 && !slopeadj && !wasslope && !jumping){
			
			if(shooting){
				if(currentAction != SHOOT){
					boolean k = (currentAction == RUNSHOOT) || (currentAction == FALLSHOOT) || (currentAction == SHOOT);
					int f = animation.getFrame();
					currentAction = SHOOT;
					animation.setFrames(frames.get(currentAction));
					if(k)animation.setFrame(f);
					animation.setDelay(15); 
					}
				}else if(currentAction != IDLE){
					
					boolean k = currentAction == SHOOT;
					int f = animation.getFrame();
					currentAction = IDLE;
					animation.setFrames(frames.get(currentAction));
					if(k)animation.setFrame(f);
					animation.setDelay(35);
					
				}
			
		}else if(dx != 0 && ( dy == 0|| ridingPlatform || slopeadj || wasslope) &&  (LEFT || RIGHT)&& !jumping){
			if(shooting){
				if(currentAction != RUNSHOOT){
					boolean k =  (currentAction == FALLSHOOT) || (currentAction == SHOOT);
					int f = animation.getFrame();
					currentAction = RUNSHOOT;
					animation.setFrames(frames.get(currentAction));
					if(k)animation.setFrame(f);
					animation.setDelay(16); 
				}
			}else if(currentAction != RUN){
				boolean k = (currentAction == RUNSHOOT);
				int f = animation.getFrame();
				currentAction = RUN;
				animation.setFrames(frames.get(currentAction));
				if(k)animation.setFrame(f);
				animation.setDelay(16);
			}
		}else if(jumping){
			if(shooting){
				if(currentAction != JUMPSHOOT){
					boolean k = (currentAction == RUNSHOOT) || (currentAction == SHOOT);
					int f = animation.getFrame();
					currentAction = JUMPSHOOT;
					animation.setFrames(frames.get(currentAction));
					if(k)animation.setFrame(f);
					animation.setDelay(5); 
				}
			}else if(currentAction != JUMP){
				boolean k = (currentAction == JUMPSHOOT);
				int f = animation.getFrame();
				currentAction = JUMP;
				animation.setFrames(frames.get(JUMP));
				if(k)animation.setFrame(f);
				animation.setDelay(10);
			}
		}else if(dy < 0 && !ridingPlatform && !wasslope && !slopeadj ){
			if(shooting){
				if(currentAction != FALLSHOOT){
					boolean k = currentAction == JUMPSHOOT || currentAction == RUNSHOOT || currentAction == SHOOT || currentAction == FALL;
					int f = animation.getFrame();
					currentAction = FALLSHOOT;
					animation.setFrames(frames.get(currentAction));
					if(k)animation.setFrame(f);
					animation.setDelay(5); 
				}
			}else if(currentAction != FALL){
				boolean k = (currentAction == FALLSHOOT);
				int f = animation.getFrame();
				currentAction = FALL;
				animation.setFrames(frames.get(FALL));
				if(k)animation.setFrame(f);
				animation.setDelay(35);
			}
		}else{
			
			if(shooting){
				if(currentAction != SHOOT){
					boolean k = (currentAction == RUNSHOOT) || (currentAction == FALLSHOOT) || (currentAction == SHOOT);
					int f = animation.getFrame();
					currentAction = SHOOT;
					animation.setFrames(frames.get(currentAction));
					if(k)animation.setFrame(f);
					animation.setDelay(15); 
					}
				}else if(currentAction != IDLE){
					
					boolean k = currentAction == SHOOT;
					int f = animation.getFrame();
					currentAction = IDLE;
					animation.setFrames(frames.get(currentAction));
					if(k)animation.setFrame(f);
					animation.setDelay(35);
					
				}
		}
		
		if((animation.hasPlayedOnce() || animation.getFrame() > 9) && shooting){
			shooting = false;
		}
		
		//all the things that stop on last one
		if(currentAction == JUMP /*|| otherstate*/){
			if(!animation.hasPlayedOnce()){
				animation.update();
			}else{
			
				jumping = false;
			}
			
		}else{ 
		animation.update();
		
		}
		
		
		
		
	}
		
	
	
	public void draw(SpriteBatch g) {
		
		super.draw(g);
		g.setColor(1.0f, 1.0f, 1.0f, alpha);
		if (facingRight) {	
			if(animation.getImage() != null)g.draw(animation.getImage(), x -cwidth /4 ,y + height , width, -height);	
		} else {
			if(animation.getImage() != null)g.draw(animation.getImage(),  x + width -cwidth / 4 , y + height ,-width, -height);
		}
		g.setColor(1.0f, 1.0f, 1.0f, 1.0f);
		
		
			if(transition!=null){
			
				if (facingRight) {	
					g.draw(transition.getImage(), x - 24 - cwidth / 4, y  - 3 + 96 ,96,-96 );
				} else {
					g.draw(transition.getImage(), x - 24 - cwidth / 4  + 96, y  - 3 + 96 ,-96,-96 );
				}
			
		}
		
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
		if(k == Keys.J && canshoot){
			shooting = true;
			canshoot = false;
			shootcooldown = 25;
			shootball = true;
			GameStateManager.rm.playSound(Resources.Sounds.GENERAL, Resources.Sounds.General.WHOOSH1, 0.07f);
		}
	}
	
	public void keyReleased(int k){
		super.keyReleased(k);
		
		
	}
	
	public void onTouchDown(float x, float y){
		super.onTouchDown(x, y);
	}

}
