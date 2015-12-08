package com.sugarware.seedlings.gamestate;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.greenpipestudios.seedpeople.Script;

public abstract class PlaybackState extends PlayGameState {

	long ticks;
	HashMap<Long, Integer> keyUp;
	HashMap<Long, Integer> keyDown;
	
	public PlaybackState(GameStateManager gsm, String map_path,String scriptPath) {
		super(gsm, map_path);
		ticks = 0;
		try {
			InputStream scriptFile = Gdx.files.internal(scriptPath).read();
			ObjectInputStream reader = new ObjectInputStream(scriptFile);
			Object obj = reader.readObject();
			Script scr = (Script) obj;
			keyUp = scr.keyUp;
			keyDown = scr.keyDown;
			reader.close();
		} catch (Exception e) { 
			e.printStackTrace();
		}
	}
	
	public void update(){
		super.update();
		
		ticks++;
		try{
			if(keyDown.keySet().contains(ticks))keyPressed(keyDown.get(ticks));
			if(keyUp.keySet().contains(ticks))keyReleased(keyUp.get(ticks));
			
		}catch(Exception e){/*do nothing*/};
	}
	
	

}
