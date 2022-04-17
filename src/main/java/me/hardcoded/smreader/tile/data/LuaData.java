package me.hardcoded.smreader.tile.data;

/**
 * This class contains serialized lua data
 *
 * @author HardCoded
 */
public class LuaData {
	private final byte[] data;
	
	public LuaData(byte[] data) {
		this.data = data;
	}
	
	public byte[] getData() {
		return data.clone();
	}
}
