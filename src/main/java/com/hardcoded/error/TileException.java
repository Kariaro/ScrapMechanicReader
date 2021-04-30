package com.hardcoded.error;

@SuppressWarnings("serial")
public class TileException extends RuntimeException {
	public TileException() {
		
	}
	
	public TileException(String format, Object... args) {
		super(String.format(format, args));
	}
}
