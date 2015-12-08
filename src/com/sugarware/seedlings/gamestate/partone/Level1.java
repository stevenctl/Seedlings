package com.sugarware.seedlings.gamestate.partone;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.KeyImage;
import com.sugarware.seedlings.entities.Robot;
import com.sugarware.seedlings.entities.Water;
import com.sugarware.seedlings.entities.players.VanillaCharacter;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class Level1 extends PlayGameState {
	
	

	
	boolean reset;

	
	
	
	public Level1( GameStateManager gsm) {
		super( gsm, "tilemaps/level1.tmx");
		inited = false;
		
	
		
		GameStateManager.rm.loadWaterImages();
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		//no changes on this level
		
		ss = new ScrollSelector(icons, 32,24,24,this);
		
	}
		
	
	public void init(){
		gsm.setNextState(GameStateManager.L2);
		inited = false;
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW);
		
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.CRICKETS).loop();
	
		super.init();
		p = new VanillaCharacter(this, 96, 400);
		for(int i = 0; i < 100; i++)p.camUpdate();
		entities.clear();
		entities.add(new KeyImage(this, 150, 300,20,20,  "a"));
		entities.add(new KeyImage(this, 175, 300,20,20,  "d"));
		entities.add(new KeyImage(this, 150, 275 ,45,20,  "space"));
		
		loadMapPlatforms();
		
		//p.add(new VertPlatform(this, 349, 738, 48));
		//p.add(new VertPlatform(this, 272, 670, 48));
		
		entities.add(new Water(this, 0, 0, 150, 6));
		entities.add(new Water(this, 150, 0, 150, 6));
		entities.add(new Water(this, 300, 0, 150, 6));
		
		
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
			 	
		if(p.y < 0){
			init();
		}
		if(p.x > w + 10)gsm.nextState();
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
		if(k == Keys.NUM_0){System.out.println(p.x + ", " + p.y);
	}
		
	}

	@Override
	protected void keyReleased(int k) {
		p.keyReleased(k);
		if(k == Keys.NUM_1){entities.add( new Robot(this, 356,291,false));
		entities.add( new Robot(this, 356,291,true));}
	}

	public void gatherResources(){
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW);
		//GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		
	}
	
	
}
