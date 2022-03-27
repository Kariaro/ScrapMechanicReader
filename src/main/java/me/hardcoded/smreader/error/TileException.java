package me.hardcoded.smreader.error;

/**
 * A tile exception class.
 * 
 * @author HardCoded
 */
@SuppressWarnings("serial")
public class TileException extends RuntimeException {
	public TileException() {
		
	}
	
	public TileException(String format, Object... args) {
		super(String.format(format, args));
	}
}
