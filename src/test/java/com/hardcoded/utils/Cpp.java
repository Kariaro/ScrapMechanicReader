package com.hardcoded.utils;

public class Cpp {
	public static void memcpy(char[] src, int srcPos, char[] dst, int dstPos, int size) {
		for(int i = 0; i < size; i++) {
			dst[srcPos + i] = src[dstPos + i];
		}
	}
}
