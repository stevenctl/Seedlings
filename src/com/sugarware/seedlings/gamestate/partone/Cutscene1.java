package com.sugarware.seedlings.gamestate.partone;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Bulldozer;
import com.sugarware.seedlings.entities.Entity;
import com.sugarware.seedlings.entities.players.FlowerCharacter;
import com.sugarware.seedlings.entities.players.Player;
import com.sugarware.seedlings.entities.players.VanillaCharacter;
import com.sugarware.seedlings.entities.scenery.Hut;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlaybackState;

public class Cutscene1 extends PlaybackState{
	
	

	
	boolean reset;
	int hutn = 3;
	
	Player p2,p3,p4,p5;
	
	
	public Cutscene1( GameStateManager gsm) {
		super( gsm, "tilemaps/cutscene1.tmx","scripts/cut1.sav");
		inited = false;
		
	
		
		GameStateManager.rm.loadWaterImages();
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		
		
		ss = new ScrollSelector(icons, 32,24,24,this);
		
	}
		
	
	public void init(){
		drawGUI = false;
		dark = false;
		gsm.setNextState(GameStateManager.B1);
		inited = false;
		GameStateManager.rm.playSong(Resources.Sounds.Songs.PERIWINKLE);
		
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		for(Sound s :  GameStateManager.rm.getSoundPack(Resources.Sounds.GENERAL)){
			s.stop();
		}
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.CRICKETS).loop();
	
		super.init();
		p = new VanillaCharacter(this, -32, 400);
		for(int i = 0; i < 100; i++)p.camUpdate();
		entities.clear();
		loadMapPlatforms();
		entities.add(new Hut(this, 332, 50));
		entities.add(new Hut(this, 232, 50));
		entities.add(new Hut(this, 132, 50));
		p2 = new VanillaCharacter(this, 342,50);
		p3 = new FlowerCharacter(this, 150, 50);
		p4 = new VanillaCharacter(this, 190, 50);
		p4.width = 32;
		p4.height = 32;
		p4.jumpspeed = p4.jumpspeed * 0.7f;
		
		p4.maxspeed = 1;
		p4.keyPressed(Keys.D);
		p5 = new VanillaCharacter(this, 268,50);
		p2.camera = false; p3.camera = false; p4.camera  =false; p5.camera = false;
		entities.add(p2);
		entities.add(p3);
		entities.add(p4);
		entities.add(p5);
		
		//p.add(new VertPlatform(this, 349, 738, 48));
		//p.add(new VertPlatform(this, 272, 670, 48));
		
		
		
		//entities.add(new Water(this, 400, 0, 258, 6));
		//entities.add(new Powerup(this, 638,624, PowerType.FLOWER));
		//entities.add(new Powerup(this, 1382,401, PowerType.FIRE));
		
		
		
		
	}
	
	@Override
	protected void draw(SpriteBatch g) {
	
		
		//actual drawing
		if(inited)super.draw(g);
		
	
		
		
	}
	public void unload(){
		super.unload();
		GameStateManager.rm.unloadImages(Resources.Images.FIRE_SEED);
		GameStateManager.rm.unloadImages(Resources.Images.FLOWER_SEED);
		GameStateManager.rm.unloadImages(Resources.Images.ROBOT);
		GameStateManager.rm.unloadSoundPack(Resources.Sounds.GENERAL);
		GameStateManager.rm.unloadImages(Resources.Images.FIREBALL);
		GameStateManager.rm.unloadSoundPack(Resources.Images.EXPLOSION_1);
		GameStateManager.rm.unloadImages(Resources.Images.EXPLOSION_2);
		GameStateManager.rm.stopSong();
	}
	int intensecountdown = 360;
	boolean serene = true;
	boolean approach = false;
	boolean bad = false;
	
	int t1 = 120;
	@Override
	public void update() {
		if(!GameStateManager.rm.getMusic().isPlaying() ){intensecountdown--;
		if(intensecountdown == 0) GameStateManager.rm.playSong(Resources.Sounds.Songs.INTENSE);	
		
		}
		super.update();	
		if(inited){
			t1--;
			if(serene){
				if(p4.dy  == 0){
					p4.keyPressed(Keys.SPACE);
					p4.keyReleased(Keys.SPACE);
					if(p4.dx > 0){p4.keyReleased(Keys.D); p4.keyPressed(Keys.A);}else{p4.keyReleased(Keys.A); p4.keyPressed(Keys.D);}
				}
				
				
				if(t1 % 40 == 0){
					if(p2.dx == 0){
						if(p2.facingRight){
							p2.keyPressed(Keys.A);
						}else{
							p2.keyPressed(Keys.D);
						}
					}
					
					}
				if(t1 % 18 == 0){
						if(p2.dx != 0){if(p2.facingRight){
							p2.keyReleased(Keys.D);
						}else{
							p2.keyReleased(Keys.A);
						}
						}
					}
				
//
				if(t1 % 60 == 0){
					if(p5.dx == 0){
						if(p5.facingRight){
							p5.keyPressed(Keys.A);
						}else{
							p5.keyPressed(Keys.D);
						}
					}
					
					}
				if(t1 % 24 == 0){
						if(p5.dx != 0){if(p5.facingRight){
							p5.keyReleased(Keys.D);
						}else{
							p5.keyReleased(Keys.A);
						}
						}
					}
				//
				
				
				
				
				
				if(t1 <= 0)t1 = 120;
			}else if(approach){
				p2.LEFT = false;
				p3.LEFT = false;
				p4.LEFT = false;
				p5.LEFT = false;
				p2.RIGHT = false;
				p3.RIGHT = false;
				p4.RIGHT = false;
				p5.RIGHT = false;
				p2.facingRight = false;
				p3.facingRight = false;
				p5.facingRight = false;
				p4.facingRight = false;
				
					
					if(p2.dy == 0 && t1 % 30 == 0){p2.keyPressed(Keys.SPACE);p2.keyReleased(Keys.SPACE);}
					if(p3.dy == 0 && t1 % 20 == 0){p3.keyPressed(Keys.SPACE);p3.keyReleased(Keys.SPACE);}
					if(p4.dy == 0 && t1 % 40 == 0){p4.keyPressed(Keys.SPACE);p4.keyReleased(Keys.SPACE);}
					if(p5.dy == 0 && t1 % 10 == 0){p5.keyPressed(Keys.SPACE);p5.keyReleased(Keys.SPACE);}
					if(t1 == 0)t1 = 60;
				
			}else if(bad){
				if(!p2.RIGHT)p2.keyPressed(Keys.D);
				if(!p3.RIGHT)p3.keyPressed(Keys.D);
				if(!p4.RIGHT)p4.keyPressed(Keys.D);
				if(!p5.RIGHT)p5.keyPressed(Keys.D);
				for(Entity e : entities){
					if(e instanceof Bulldozer){
						gsm.setNextState(GameStateManager.B1);
						if(e.x > w - 80){
							System.out.println("218");
							gsm.nextState();
						}
					}
				}
			}
			
			
			
			 	
		}
	
		
	}

	@Override
	protected void cursorMoved(Vector3 coords, int pointer) {}

	@Override
	protected void touchDown(Vector3 coords, int pointer) {
		p.onTouchDown(coords.x, coords.y);
	}

	@Override
	protected void touchUp(Vector3 coords, int pointer) {}

	@Override
	protected void keyPressed(int k) {
		super.keyPressed(k);
		p.keyPressed(k);
		if(k == Keys.NUM_0){serene = false; approach = true;entities.add(new Bulldozer(this, -732,48));
		GameStateManager.rm.stopSong();GameStateManager.rm.playSound(Resources.Sounds.GENERAL, Resources.Sounds.General.TREE_FALL);
		
		for(Entity e : entities)if(e instanceof Player)((Player) e).unhappy = true;
		p.unhappy = true;
		}
		if(k == Keys.NUM_2){approach = false; bad = true;}
		
		if(k == Keys.NUM_1){
			int c = hutn;
			hutn--;
			for(Entity e : entities){
				if(e instanceof Hut){
					GameStateManager.rm.playSound(Resources.Sounds.GENERAL, Resources.Sounds.General.CRUNCH);
					c--;
					if(c == 0)
					((Hut) e).demolished = true;
				}
			}
		}
	
	
	}

	@Override
	protected void keyReleased(int k) {
	//super.keyReleased(k);
		p.keyReleased(k);
	
	}

	public void gatherResources(){
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		
		
		
	}
	
	
}
