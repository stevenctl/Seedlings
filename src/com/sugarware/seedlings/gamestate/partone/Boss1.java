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
import com.sugarware.seedlings.entities.players.VanillaCharacter;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Boss1 extends PlayGameState {
	
	
	boolean playerControll = true;
	boolean inited;
	boolean reset;
	float cammin;
	float camspeed  = 1.3f;
	
	
	public Boss1( GameStateManager gsm) {
		super( gsm, "tilemaps/boss1.tmx");
		inited = false;
	
		
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		
		ss = new ScrollSelector(icons, 32,24,24,this);
		
	}
		
	
	public void init(){
	GameStateManager.rm.playSong(Resources.Sounds.Songs.BOSS1);
	for(Sound s :  GameStateManager.rm.getSoundPack(Resources.Sounds.GENERAL)){
		s.stop();
	}
		gsm.setNextState(GameStateManager.D1);
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.TREE_FALL).play();
		inited = true;
		super.init();
		playerControll = true;
		p = new VanillaCharacter(this, 48,h - 10);
		p.unhappy = true;
		entities.clear();
		Bulldozer b = new Bulldozer(this, -500, 50);
		b.speed = camspeed;
		entities.add(b);
		loadMapPlatforms();
		//p.add(new VertPlatform(this, 349, 738, 48));
		//p.add(new VertPlatform(this, 272, 670, 48));
		
		//entities.add(new Rope(this, 375,385,100));
		//entities.add(new Powerup(this, 638,624, PowerType.FLOWER));
		//entities.add(new Powerup(this, 1382,401, PowerType.FIRE));
		//entities.add(new Robot(this, 670,290));
		cam.position.set(p.x, p.y, 0);
		//this doesn't matter
		inited =true;
		reset = true;
		cammin =  - 700;
	}
	
	@Override
	protected void draw(SpriteBatch g) {
	
		
		//actual drawing
		if(inited)super.draw(g);
		if(reset){
			reset = false;
		}
	
		
		
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

	@Override
	public void update(){
			
		if(inited){
			super.update();
			
			if(p.x < cam.position.x - cam.viewportWidth / 2 - p.width * 2.5){
				p.dead = true;
			}
			
			if(p.y < 0){
				if(p.x > w - 100){
					System.out.println("Beat level!");
				for(Sound s : GameStateManager.rm.getSoundPack(Resources.Sounds.GENERAL)){
					if(s!=null){
						try{
							s.stop();
						}catch(Exception e){}
					}
				
			}
				gsm.nextState(); 
				}
			else init();}
			}
			cammin += camspeed;
			
			float camx = p.x;
			
					while(!(camx +cam.viewportWidth / 2 <= w)){
						if(camx + cam.viewportWidth / 2 > w){camx-=0.01;}
					}
					while(!(camx -cam.viewportWidth / 2 >= cammin && camx -  cam.viewportWidth / 2 >= 0 )){
						camx+=0.01;
						if(camx + cam.viewportWidth / 2 >w){
							camx -= 0.01;
							break;
						}
					}
					cam.position.x = camx;
			
			
					
					
			cam.update();
			if(p.x > w - 100){playerControll = false;
			p.LEFT = false;
			p.RIGHT = false;
			p.UP = false;
			p.DOWN = false;
			p.trueUP = false;
			p.keyPressed(Keys.D);
			
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
		if(playerControll)	p.keyPressed(k);
		if(k == Keys.NUM_0){System.out.println(p.x + ", " + p.y);
	}
		
	}

	@Override
	protected void keyReleased(int k) {
		
		if(playerControll)	p.keyReleased(k);
		
	}

	public void gatherResources(){
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW);
		GameStateManager.rm.loadImages(Resources.Images.FIRE_SEED);
		GameStateManager.rm.loadImages(Resources.Images.FLOWER_SEED);
		GameStateManager.rm.loadImages(Resources.Images.ROBOT);
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		
		GameStateManager.rm.loadImages(Resources.Images.FIREBALL);
		GameStateManager.rm.loadWaterImages();

	}
	
	
}
