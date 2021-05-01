package com.hardcoded.utils;

import com.hardcoded.error.TileException;
import com.hardcoded.logger.Log;
import com.hardcoded.logger.Log.Level;

public class TileUtils {
	private static final Log LOGGER = Log.getLogger();
	private static final DecompressVersion6 func = new DecompressVersion6();
	private static final CompressVersion1 func_comp = new CompressVersion1();
	
	public static int decompress_data(byte[] compressed, byte[] output, int size) {
		int result = func.decompress(compressed, output, size);
		return (int)result;
	}
	
	public static byte[] compress_data(byte[] data) {
		return func_comp.compress(data);
	}
	
	public static void error(String format, Object... args) {
		if(isDev()) {
			LOGGER.log(Level.ERROR, 0, format, args);
		}
		
		// ???
		throw new TileException(format, args);
	}
	
	public static void log(String format, Object... args) {
		if(isDev()) {
			LOGGER.log(Level.INFO, 0, format, args);
		}
	}
	
	public static boolean isDev() {
		return "true".equalsIgnoreCase(System.getProperty("com.hardcoded.dev"));
	}
}
