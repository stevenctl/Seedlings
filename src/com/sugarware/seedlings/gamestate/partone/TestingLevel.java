package com.sugarware.seedlings.gamestate.partone;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.sugarware.lone.LoneBad;
import com.sugarware.seedlings.Resources;
import com.sugarware.seedlings.ScrollSelector;
import com.sugarware.seedlings.entities.Block;
import com.sugarware.seedlings.entities.LeafCoin;
import com.sugarware.seedlings.entities.players.VanillaCharacter;
import com.sugarware.seedlings.gamestate.GameStateManager;
import com.sugarware.seedlings.gamestate.PlayGameState;

public class TestingLevel extends PlayGameState{
	
	

	
	
	

	
	
	public TestingLevel( GameStateManager gsm) {
		super( gsm, "tilemaps/test.tmx");
		inited = false;
		
	
		
		GameStateManager.rm.loadWaterImages();
		ArrayList<TextureRegion> icons = new ArrayList<TextureRegion>();
		
		ss = new ScrollSelector(icons, 32,24,24,this);
	
	}
		
	
	public void init(){
		dark = false;
		gsm.setNextState(GameStateManager.B1);
		inited = false;
		GameStateManager.rm.playSong(Resources.Sounds.Songs.MEADOW2);
		
		GameStateManager.rm.loadSoundPack(Resources.Sounds.GENERAL);
		for(Sound s :  GameStateManager.rm.getSoundPack(Resources.Sounds.GENERAL)){
			s.stop();
		}
		GameStateManager.rm.getSound(Resources.Sounds.GENERAL, Resources.Sounds.General.CRICKETS).loop();
	
		super.init();
		p = new VanillaCharacter(this, 50, h - 20);
		for(int i = 0; i < 100; i++)p.camUpdate();
		entities.clear();
		
		loadMapPlatforms();
		
	
		
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
			
			if(p.y < 0)init();
			if(p.x > w + 10)gsm.nextState();
			
			
			
			 	
		}
	
		
	}

	@Override
	protected void cursorMoved(Vector3 coords, int pointer) {}

	@Override
	protected void touchDown(Vector3 coords, int pointer) {
		p.onTouchDown(coords.x, coords.y);
		entities.add(new LeafCoin(this,coords.x, coords.y));
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
