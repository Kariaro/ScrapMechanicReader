package com.hardcoded.utils;

public class TileUtils {
	private static final DecompressTestingVersion6 func = new DecompressTestingVersion6();
	
	public static int decompress_data(byte[] compressed, byte[] output, int size) {
		int result = func.decompress(compressed, output, size);
		return (int)result;
	}
	
	public static void error(String format, Object... args) {
		System.out.printf("[error]: %s\n", String.format(format, args));
	}
	
	public static void log(String format, Object... args) {
		System.out.printf("[log]: %s\n", String.format(format, args));
	}
}
