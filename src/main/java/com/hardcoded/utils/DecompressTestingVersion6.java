 package com.hardcoded.utils;

public class DecompressTestingVersion6 {
	public int decompress(byte[] compressed, byte[] output, int uncompressed_size) {
		if(compressed.length > 1000000) {
			// This is more than what the game allows?
		}
		
		if(output.length < uncompressed_size) {
			// throw new Exception("output array is too small for the requested size uncompressed_size");
			return -1;
		}
		
		/*
		{
			for(int highByte = 0; highByte < 8; highByte++) {
				byte[] param_2 = { 1, 2, 3, 4, 5, 6, 7, 0, 0, 0, 0, 0, 0, 0, 0 };
				
				int ESI = 7;
				param_2[ESI] = 0;
				param_2[ESI + 1] = 0;
				param_2[ESI + 2] = 0;
				param_2[ESI + 3] = 0;
				
				int EDI = ESI - highByte;
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[highByte];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI += INT_00e6cbe0[highByte];
				
				System.out.printf("%x: %s, EDI += %d\n", highByte, Arrays.toString(param_2), INT_00e6cab8[highByte] + INT_00e6cbe0[highByte]);
			}
		}
		
		if(true) throw new NullPointerException();
		*/
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
	

	private static final int[] INT_OFSTART = { 0, 1, 2, 1, 0, 4, 4, 4 };
	private static final int[] INT_OFFSETS = { 0, 1, 2, 3, 4, 3, 2, 1 };
	
	private int DecompressJava(final byte[] input, final byte[] param_2, final int size) {
		if(size == 0) {
			return (input[0] == 0) ? -1:1;
		}
		
		int index = 0;
		int ESI = 0; // Never goes backwards
		int EDI = 0; // Copy index ?
		
		do {
			final int readByte = input[index++] & 255;
			int highByte = readByte >> 0x4;
			int lowByte = readByte & 0xf;
			
			if((highByte < 0x9) && (ESI <= size - 26)) {
				// Only need to read [highByte] bytes but 8 works too
				memcpy(param_2, ESI, input, index, 8);
				
				/* [BLOCK 0] */
				ESI += highByte;
				index += highByte;
				highByte = (input[index++] & 255) | ((input[index++] & 255) << 8);
				
				if((lowByte != 0xf) && (highByte > 0x7)) {
					memcpy(param_2, ESI, param_2, ESI - highByte, 0x12);
					ESI += lowByte + 0x4;
					continue;
				}
			} else {
				/* [BLOCK 1] */
				if(highByte == 0xf) {
					int value = 0;
					int read = 0;
					
					do {
						read = input[index++] & 255;
						value += read;
					} while(read == 0xff);
					
					highByte = value + 0xf;
				}
				
				if(ESI + highByte > size - 8) {
					if(ESI + highByte != size) return -(index + 1);
					memmove(param_2, ESI, input, index, highByte);
					return highByte + index;
				}
				
				memcpy(param_2, ESI, input, index, ((highByte + 7) / 8) * 8);
				
				/* [BLOCK 0] */
				ESI += highByte;
				index += highByte;
				highByte = (input[index++] & 255) | ((input[index++] & 255) << 8);
			}
			
			/* [BLOCK 1] */
			if(lowByte == 0xf) {
				int value = 0;
				int read = 0;
				
				do {
					read = input[index++] & 255;
					value += read;
				} while(read == 0xff);
				
				lowByte = value + 0xf;
			}
			
			// Both branches [EDI += 8]
			if(highByte < 8) {
				param_2[ESI] = 0;
				param_2[ESI + 1] = 0;
				param_2[ESI + 2] = 0;
				param_2[ESI + 3] = 0;
				
				/* [ Possible outcomes for a stream]
				
				0: [1, 2, 3, 4, 5, 6, 7, |                       ], EDI += 0
				1: [1, 2, 3, 4, 5, 6, 7, | 7,                    ], EDI += 1
				2: [1, 2, 3, 4, 5, 6, 7, | 6, 7,                 ], EDI += 2
				3: [1, 2, 3, 4, 5, 6, 7, | 5, 6,                 ], EDI += 2
				4: [1, 2, 3, 4, 5, 6, 7, | 4, 5, 6, 7,           ], EDI += 4
				5: [1, 2, 3, 4, 5, 6, 7, | 3, 4, 5,              ], EDI += 3
				6: [1, 2, 3, 4, 5, 6, 7, | 2, 3,                 ], EDI += 2
				7: [1, 2, 3, 4, 5, 6, 7, | 1,                    ], EDI += 1
				*/
				
				// INT_OFSTART = { 0, 1, 2, 1, 0, 4, 4, 4 };
				// INT_OFFSETS = { 0, 1, 2, 3, 4, 3, 2, 1 };
				
				
				
				// We copy from the previous bytes and fill the next 4 bytes.
				// *(param_2[ESI    ]) = *(param_2[ESI - highByte]);
				// *(param_2[ESI + 4]) = *(param_2[ESI - highByte + INT_OFSTART[highByte]]);
				
				// OFF_1 = { 0, 1, 2, 3, 4, 5, 6, 7 }
				// OFF_2 = { 0, 0, 0, 2, 4, 1, 2, 3 };
				
				memcpy(param_2, ESI + 0x0, param_2, ESI - highByte, 4);
				memcpy(param_2, ESI + 0x4, param_2, ESI - highByte + INT_OFSTART[highByte], 4);
				EDI = ESI - highByte + INT_OFFSETS[highByte];
				ESI += 0x8;
			} else {
				memcpy(param_2, ESI, param_2, ESI - highByte, 8);
				EDI = ESI - highByte + 0x8;
				ESI += 0x8;
			}
			
			final int EBP_0x1c = ESI + lowByte - 0x4;
			
			if(ESI + lowByte > size - 8) { // Only runs if we are at the end of the stream
				if(ESI + lowByte >= size) return - (index + 1);
				
				if(ESI < size - 7) {
					memcpy(param_2, ESI, param_2, EDI, ((size - ESI) / 8) * 8);
					
					int tmp = size - 7 - ESI;
					ESI = size - 7;
					EDI += tmp;
				}
				
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				}
			} else {
				memcpy(param_2, ESI, param_2, EDI, 8); // Placement of this line does not matter
				
				if(lowByte > 12) {
					memcpy(param_2, ESI + 8, param_2, EDI + 8, ((lowByte + 7) / 8) * 8);
				}
			}
			
			ESI = EBP_0x1c;
		} while(true);
	}
}