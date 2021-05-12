package com.hardcoded.utils;

import com.hardcoded.error.TileException;
import com.hardcoded.logger.Log;
import com.hardcoded.logger.Log.Level;

import net.jpountz.lz4.*;

public class TileUtils {
	private static final Log LOGGER = Log.getLogger();
	
	private static final LZ4Factory lz4_factory;
	private static final LZ4FastDecompressor fast_decomp;
	private static final LZ4SafeDecompressor safe_decomp;
	private static final LZ4Compressor compression;
	
	static {
		lz4_factory = LZ4Factory.fastestInstance();
		fast_decomp = lz4_factory.fastDecompressor();
		safe_decomp = lz4_factory.safeDecompressor();
		compression = lz4_factory.fastCompressor();
	}
	
	public static int decompress_data(byte[] input, byte[] output, int size) {
		int result = fast_decomp.decompress(input, 0, output, 0, size);
		return (int)result;
	}
	
	public static int safe_decompress_data(byte[] input, byte[] output) {
		int result = safe_decomp.decompress(input, output);
		return (int)result;
	}
	
	public static byte[] compress_data(byte[] data) {
		return compression.compress(data);
		//return func_comp.compress(data);
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
