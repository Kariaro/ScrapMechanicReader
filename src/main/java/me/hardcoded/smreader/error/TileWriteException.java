package me.hardcoded.smreader.error;

/**
 * A tile exception class.
 * 
 * @author HardCoded
 */
@SuppressWarnings("serial")
public class TileWriteException extends RuntimeException {
	public TileWriteException() {
	
	}
	
	public TileWriteException(String format, Object... args) {
		super(String.format(format, args));
	}
}
