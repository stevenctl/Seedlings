package com.sugarware.seedlings.entities;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Rectangle;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Bulldozer extends Boss {

	float dx = 0;
	float dy = 0;
	public float speed = 1.3f;
	boolean bl = false;
	boolean br = false;
	boolean tl = false;
	boolean tr = false;
	float width = 256;
	float height = 256;
	int muscountdown;
	
	ArrayList<TextureRegion[]> frames;
	Animation animation;
	
	public Bulldozer(PlayGameState gs, float x, float y) {
		super(gs);
		this.x = x;
		this.y= y;
		cwidth =192;
		cheight = 128;
		animation = new Animation();
		GameStateManager.rm.loadImages(Resources.Images.BULLDOZER);
		animation.setFrames(GameStateManager.rm.getImages(Resources.Images.BULLDOZER).get(0));
		animation.setDelay(100);
		muscountdown = 70;
	}

	
	
	
	@Override
	public void update() {
		
		if(muscountdown == 0){
			GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.BULLDOZER_LOOP).loop();
			muscountdown = -1;
		}else if(muscountdown  > 0){
			muscountdown--;
		}
		updatePosition();
		animation.update();
	}

	void updatePosition(){
		//Rectangular
		dx = speed;
		for(int i = 0; i < gs.tilemap.getLayers().get("Collisions").getObjects().getCount(); i++){
	
	
		
		
	
	
		float d = calcCorners(x, y + dy, i);
		
		if(dy < 0 && (br || bl)){dy  = d;}
		if(dy > 0 && (tl || tr) ){dy = 0; }
		
		}
	
		y = y + dy;
		x = x + dx;
	

	
	
	}
	
	
private float calcCorners(float ax, float ay, int i){
		
		MapObjects obs = gs.tilemap.getLayers().get("Collisions").getObjects();
		
			if (obs.get(i) instanceof RectangleMapObject) {
				Rectangle r1 = ((RectangleMapObject) obs.get(i)).getRectangle();
				
				
			
			
					bl = r1.contains(ax + (width - cwidth) / 4 , ay);
					br = r1.contains(ax + cwidth + (width - cwidth) / 4, ay);
					tr = r1.contains(ax + cwidth + (width - cwidth) / 4, ay + cheight);
					tl = r1.contains(ax + (width - cwidth) / 4 , ay + cheight);
					
					
						
					if(bl || br){
						return 0  ;
					}
				
				
			
			}
			
			
		
			return 0;
		
		
		
		
		
	}
	
	
	@Override
	public void draw(SpriteBatch g) {
		// TODO Auto-generated method stub
		if(animation.getImage() != null)g.draw(animation.getImage(), x -cwidth /4 ,y + height , width, -height);	
	}

}
