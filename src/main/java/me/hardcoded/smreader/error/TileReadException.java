package me.hardcoded.smreader.error;

/**
 * A tile exception class.
 * 
 * @author HardCoded
 */
@SuppressWarnings("serial")
public class TileReadException extends RuntimeException {
	public TileReadException() {
	
	}
	
	public TileReadException(String format, Object... args) {
		super(String.format(format, args));
	}
}
