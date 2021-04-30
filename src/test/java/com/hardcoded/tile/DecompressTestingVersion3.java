 package com.hardcoded.tile;

public class DecompressTestingVersion3 {
	private static final int[] INT_00e6cab8 = { 0x0, 0x1, 0x2, 0x1, 0x0, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7, 0x1000b, 0x10 };
	private static final int[] INT_00e6cbe0 = { 0x0, 0x0, 0x0, 0xffffffff, 0xfffffffc, 0x1, 0x2, 0x3, 0x6, 0xd, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7 };
	
	public int decompress(byte[] compressed, byte[] output, int uncompressed_size) {
		if(compressed.length > 1000000) {
			// This is more than what the game allows?
		}
		
		if(output.length < uncompressed_size) {
			// throw new Exception("output array is too small for the requested size uncompressed_size");
			return -1;
		}
		
		// Create buffers
		byte[] input = new byte[1000000];
		byte[] param_2 = new byte[1000000];
		
		// Write the compressed bytes to the input buffer
		System.arraycopy(compressed, 0, input, 0, compressed.length);
		
		// Run the decompresser
		int result = DecompressJava(input, param_2, uncompressed_size);
		
		// Write the decompressed bytes to the output buffer
		System.arraycopy(param_2, 0, output, 0, uncompressed_size);
		
		return result;
	}
	
	private static final void memcpy(byte[] dst, int dstPos, byte[] src, int srcPos, int len) {
		for(int i = 0; i < len; i++) {
			dst[i + dstPos] = src[i + srcPos];
		}
	}
	
	private static final void memmove(byte[] dst, int dstPos, byte[] src, int srcPos, int len) {
		for(int i = len - 1; i >= 0; i--) {
			dst[i + dstPos] = src[i + srcPos];
		}
	}
	
	private int DecompressJava(byte[] input, byte[] param_2, int size) {
		int EAX = 0;
		int index = 0;
		int ESI = 0;
		int EDI = 0;
		
		int EBP_0x1c = 0;
		
		if(size == 0) {
			return (input[0] == 0) ? -1:1;
		}
		
		do {
			int readByte = input[index++] & 255;
			int highByte = readByte >> 0x4;
			int lowByte = readByte & 0xf;
			
			if((highByte < 0x9) && (ESI <= size - 26)) {
				memcpy(param_2, ESI, input, index, 8);
				
				ESI += highByte;
				index += highByte;
				
				highByte = (input[index] & 255) | ((input[index + 1] & 255) << 8);
				
				if((lowByte == 0xf) || (highByte < 0x8)) {
					EAX = index;
					index += 0x2;
					
					EDI = ESI - highByte;
				} else {
					memcpy(param_2, ESI, param_2, ESI - highByte, 0x12);
					
					ESI += lowByte + 0x4;
					index += 0x2;
					continue;
				}
			} else {
				if(highByte == 0xf) {
					int value = 0;
					int read = 0;
					
					do {
						read = input[index++] & 0xff;
						value += read;
					} while(read == 0xff);
					
					highByte = value + 0xf;
				}
				
				int someOffset = highByte + ESI;
				if(someOffset > size - 8) {
					if(someOffset != size) break;
					memmove(param_2, ESI, input, index, highByte);
					return highByte + index;
				}
				
				memcpy(param_2, ESI, input, index, 8 * ((highByte + 7) / 8));
				
				index += highByte;
				highByte = (input[index] & 255) | ((input[index + 1] & 255) << 8);
				
				ESI = someOffset;
				EDI = someOffset - highByte;
				
				EAX = index;
			}
			
			// EDI = x - highByte
			index = EAX + 0x2;
			
			if(lowByte == 0xf) {
				int value = 0;
				int read = 0;
				
				do {
					read = input[index++] & 255;
					value += read;
				} while(read == 0xff);
				
				lowByte = value + 0xf;
			}
			
			if(highByte < 8) {
				param_2[ESI] = 0;
				param_2[ESI + 1] = 0;
				param_2[ESI + 2] = 0;
				param_2[ESI + 3] = 0;
				
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[highByte];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI -= INT_00e6cbe0[highByte];
			} else {
				memcpy(param_2, ESI, param_2, EDI, 8);
				EDI += 0x8;
			}
			
			EBP_0x1c = lowByte + ESI + 0x4;
			ESI += 0x8;
			
			if(EBP_0x1c > size - 12) {
				if(EBP_0x1c > size - 5) break;
				
				if(ESI < size - 0x7) {
					memcpy(param_2, ESI, param_2, EDI, 8 * ((size - ESI) / 8));
					
					EAX = size - 7 - ESI;
					ESI = size - 7;
					EDI += EAX;
				}
				
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				}
				
				ESI = EBP_0x1c;
				continue;
			}
			
			memcpy(param_2, ESI, param_2, EDI, 8);
			
			if(lowByte > 12) {
				memcpy(param_2, ESI + 8, param_2, EDI + 8, 8 * ((lowByte + 7) / 8));
			}
			
			ESI = EBP_0x1c;
		} while(true);
		
		return - (index + 1);
	}
}