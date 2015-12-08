package com.sugarware.seedlings.gamestate.partone;

import java.util.ArrayList;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Robot;
import com.sugarware.seedlings.entities.Water;
import com.sugarware.seedlings.entities.platforms.MovingPlatform;
import com.sugarware.seedlings.entities.players.VanillaCharacter;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class DemoLevel extends PlayGameState {
	
	

	

	
	
	
	public DemoLevel( GameStateManager gsm) {
		super( gsm, "tilemaps/demo.tmx");
		inited = false;
		
		
		
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		icons.addAll(GameStateManager.rm.icons);
	
		ss = new ScrollSelector(icons, 32,24,24,this);
	
	}
		
	
	public void init(){
		gsm.setNextState(GameStateManager.L1);
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW);
		
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.CRICKETS).stop();GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.CRICKETS).loop();
		inited = false; 
		super.init();
		p = new VanillaCharacter(this, 96, 800);
		p.camUpdate();
		entities.clear();
		loadMapPlatforms();
		p.add( new MovingPlatform(this, 368,h - 304,96,48,new Vector2(368, 592),new Vector2(592, h - 304),true, false, 1, 1 ));
		//p.add(new VertPlatform(this, 349, 738, 48));
		//p.add(new VertPlatform(this, 272, 670, 48));
		//
		//entities.add(new Powerup(this, 638,624, PowerType.FLOWER));
		//entities.add(new Powerup(this, 1382,401, PowerType.FIRE));
		cam.position.set(p.x, p.y, 0);
		entities.add(new  Robot(this, 1440, 373, true));
		entities.add(new  Robot(this, 1538, 373, false));
		entities.add(new Water(this, 0, 480, 258, 36));
		entities.add(new Water(this, 0, 0, w + 32, 245));
		gsm.setNextState(GameStateManager.L1);
		
		
	}
	
	@Override
	protected void draw(SpriteBatch g) {
	
		
		//actual drawing
		if(inited){
			super.draw(g);
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
	int endcd = 45;
	@Override
	public void update() {
		super.update();
		if(inited){
		if(p.y < 0){
			init();
		}
		//if at the right corner of the level
		if(p.x > w - p.width * 3)endcd--;}
		if(endcd < 0){
			gsm.setNextState(GameStateManager.L1);
			gsm.nextState();
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
		GameStateManager.rm.loadImages(Resources.Images.FIRE_SEED);
		GameStateManager.rm.loadImages(Resources.Images.FLOWER_SEED);
		GameStateManager.rm.loadImages(Resources.Images.ROBOT);
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		
		GameStateManager.rm.loadImages(Resources.Images.FIREBALL);
		GameStateManager.rm.loadWaterImages();
	}
	
	
}
