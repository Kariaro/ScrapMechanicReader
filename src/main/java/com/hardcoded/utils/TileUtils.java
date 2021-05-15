package com.hardcoded.utils;

import com.hardcoded.data.BitStream;
import com.hardcoded.data.Memory;
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
	
	public static void warn(String format, Object... args) {
		LOGGER.log(Level.WARNING, 0, format, args);
	}
	
	public static boolean isDev() {
		return "true".equalsIgnoreCase(System.getProperty("com.hardcoded.dev"));
	}
	
	
	public static void debugPrint(String name, Memory memory, int offset) {
		debugPrint(name, memory.data(), offset + memory.index());
	}
	
	// Debug Print
	public static void debugPrint(String name, byte[] data, int offset) {
		int len = data.length - offset;
		StringBuilder sb = new StringBuilder();
		
		if(name.isEmpty()) {
			sb.append("######################");
		} else {
			sb.append("########### [").append(name).append("] ###########");
		}
		
		
		if(len > 255) len = 255;
		
		sb.append(": len=").append(len).append(", idx=").append(offset).append("\n");
		
		for(int i = 0; i < len; i++) sb.append(String.format("%02x ", ((int)data[i + offset]) & 0xff));
		sb.append("\n");
		for(int i = 0; i < len; i++) {
			char c = (char)(((int)data[i + offset]) & 0xff);
			sb.append(String.format("%2s ", (Character.isWhitespace(c) || Character.isISOControl(c) ? ".":c)));
		}
		sb.append("\n");
		if(name.isEmpty()) {
			sb.append("######################");
		} else {
			sb.append("########### [").append(name).append("] ###########");
		}
		
		System.out.println(sb.toString());
	}
	
	public static void debugPrint(String str, BitStream stream) {
		debugPrint(str, stream.data(), stream.index() >> 3);
	}
	
	public static void debugPrint(String str, byte[] data) {
		debugPrint(str, data, 0);
	}
	
	public static void debugPrint(byte[] data) {
		debugPrint("", data, 0);
	}
}
