package com.sugarware.seedlings.gamestate;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import com.greenpipestudios.seedpeople.Script;

public abstract class RecordState extends PlayGameState {

	long ticks;
	HashMap<Long, Integer> keyUp;
	HashMap<Long, Integer> keyDown;
	
	public RecordState(GameStateManager gsm, String map_path) {
		super(gsm, map_path);
		ticks = 0;
		keyUp = new HashMap<Long, Integer>();
		keyDown = new HashMap<Long, Integer>();
	}
	
	public void update(){
		super.update();
		ticks++;
	}
	
	protected void keyPressed(int k){
		super.keyPressed(k);
		keyDown.put(ticks, k);
	}
	
	protected void keyReleased(int k){
		
		keyUp.put(ticks, k);
	}
	
	public void unload(){
		super.unload();
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	protected void write() throws IOException{
		FileOutputStream saveFile = new FileOutputStream("script.sav");
		ObjectOutputStream save = new ObjectOutputStream(saveFile);
		for(Long l : keyDown.keySet()){
			System.out.println(l + " " + keyDown.get(l));
		}
		Script script = new Script(keyUp, keyDown);
		save.writeObject(script);
		save.close();
		
	}

}
