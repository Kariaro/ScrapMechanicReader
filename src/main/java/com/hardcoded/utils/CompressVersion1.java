package com.hardcoded.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * This class should only be used internally
 * 
 * @author HardCoded
 */
class CompressVersion1 {
	
	public byte[] compress(byte[] data) {
		ByteArrayOutputStream bs = new ByteArrayOutputStream();
		
		if(data.length > 14) {
			int len = data.length - 15;
			
			bs.write(0xf0);
			do {
				if(len >= 0xff) {
					bs.write(0xff);
					len -= 255;
				} else {
					bs.write(len);
					break;
				}
			} while(len > 0);
			
			try {
				bs.write(data);
			} catch(IOException e) {
				e.printStackTrace();
			}
			
			return bs.toByteArray();
		}
		
		// Create mask
		bs.write(data.length << 4);
		
		try {
			bs.write(data);
		} catch(IOException e) {
			e.printStackTrace();
		}
		
		return bs.toByteArray();
	}
}
