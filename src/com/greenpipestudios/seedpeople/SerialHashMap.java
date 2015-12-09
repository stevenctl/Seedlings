package com.greenpipestudios.seedpeople;

import java.io.Serializable;
import java.util.HashMap;

public class SerialHashMap extends HashMap<Long, Integer> implements Serializable {

	/**
	 * Serialized to store to file
	 */
	private static final long serialVersionUID = 1645731413285970348L;

}
