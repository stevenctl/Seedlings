package com.sugarware.lone;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Block;
import com.sugarware.seedlings.entities.Light;
import com.sugarware.seedlings.entities.platforms.MovingPlatform;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class LoneLevel extends PlayGameState{
	
	

	
	boolean reset;
	int hutn = 3;
	

	
	int cd = 700;
	public LoneLevel( GameStateManager gsm) {
		super( gsm, "ltilemaps/level1.tmx");
		inited = false;
		
	
		
		GameStateManager.rm.loadWaterImages();
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		
		
		ss = new ScrollSelector(icons, 32,24,24,this);
		cam.viewportHeight = h;
		cam.viewportWidth =((float) ((float)Gdx.graphics.getWidth() / (float)Gdx.graphics.getHeight())) * h;
	}
		
	
	public void init(){
		dark = true;
		gsm.setNextState(GameStateManager.B1);
		inited = false;
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW2);
		
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		for(Sound s :  GameStateManager.rm.getSoundPack(Resources.Sounds.GENERAL)){
			s.stop();
		}
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.CRICKETS).loop();
	
		super.init();
		p = new LonePlayer(this, 400, 98);
		for(int i = 0; i < 100; i++)p.camUpdate();
		entities.clear();
		
		loadMapPlatforms();
		
		entities.add(new Block(this,835, 335, 48,48));
		//p.add(new VertPlatform(this, 349, 738, 48));
		//p.add(new VertPlatform(this, 272, 670, 48));
		p.add( new MovingPlatform(this,915,320 - 48,48,16,new Vector2(912, 912 + 256),new Vector2(320, 320),true, false, 1, 1 ));
		
		
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
	
	
	
	@Override
	public void update() {
		
		super.update();	
		if(inited){
			
		cd--;
		if(cd % 350 == 0)entities.add(new Light(this, (float)Math.random() * w,(float) Math.random() * h));
		if(cd < 0){
			entities.add(new Light(this, (float)Math.random() * w,(float) Math.random() * h));
			keyPressed(Keys.NUM_5);
			cd = 700;
		}
			
			
			
			 	
		}
	
		
	}

	@Override
	protected void cursorMoved(Vector3 coords, int pointer) {}

	@Override
	protected void touchDown(Vector3 coords, int pointer) {
		p.onTouchDown(coords.x, coords.y);
		entities.add(new Light(this,coords.x, coords.y));
	}

	@Override
	protected void touchUp(Vector3 coords, int pointer) {}

	@Override
	protected void keyPressed(int k) {
		super.keyPressed(k);
		p.keyPressed(k);
		if(k == Keys.NUM_0)System.out.println(p.x + ", " + p.y);
		if(k == Keys.NUM_5){
			Color col = new Color((float)Math.random(), (float)Math.random(),(float) Math.random(),1);
			entities.add(new LoneBad(this,23, 275, col));
		}
	if(k == Keys.R)entities.add(new Block(this,835, 335, 48,48));
	}

	@Override
	protected void keyReleased(int k) {
		
		p.keyReleased(k);
	
	}

	public void gatherResources(){
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW);
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		
	}
	
	
}
