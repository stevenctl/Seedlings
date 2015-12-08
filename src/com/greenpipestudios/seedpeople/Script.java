package com.greenpipestudios.seedpeople;

import java.io.Serializable;
import java.util.HashMap;

public class Script implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 335302745292977379L;
	public 	HashMap<Long, Integer> keyUp;
	public HashMap<Long, Integer> keyDown;
	public Script(HashMap<Long, Integer> keyUp,
	HashMap<Long, Integer> keyDown){
		this.keyDown = keyDown;
		this.keyUp = keyUp;
	}
}
