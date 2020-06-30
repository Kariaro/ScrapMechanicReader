 package sm.hardcoded.tile;

class DecompressTestingVersion3 {
	private static final int[] INT_00e6cab8 = { 0x0, 0x1, 0x2, 0x1, 0x0, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7, 0x1000b, 0x10 };
	private static final int[] INT_00e6cbe0 = { 0x0, 0x0, 0x0, 0xffffffff, 0xfffffffc, 0x1, 0x2, 0x3, 0x6, 0xd, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7 };
	
	public int decompress(byte[] bytes, byte[] input, int uncompressed_size) {
		Pointer __input = new Pointer(1000000).WriteBytes(bytes);
		Pointer __param_2 = new Pointer(1000000);
		int __result = DecompressJava(__input, __param_2, uncompressed_size);
		
		__param_2.Bytes(input, 0, uncompressed_size, true);
		
		return __result;
	}
	
	private static final void memcpy(Pointer dst, int dstPos, Pointer src, int srcPos, int len) {
		byte[] d = dst.data();
		byte[] s = src.data();
		for(int i = 0; i < len; i++) {
			d[i + dstPos] = s[i + srcPos];
		}
	}
	
	private static final void memmove(Pointer dst, int dstPos, Pointer src, int srcPos, int len) {
		byte[] d = dst.data();
		byte[] s = src.data();
		for(int i = len - 1; i >= 0; i--) {
			d[i + dstPos] = s[i + srcPos];
		}
	}
	
	// TODO: Use only byte arrays
	private int DecompressJava(Pointer input, Pointer param_2, int size) {
		int EAX = 0;
		int index = 0;
		int ESI = 0;
		int EDI = 0;
		
		int EBP_0x1c = 0;
		
		if(size == 0) {
			return (input.Byte() == 0) ? -1:1;
		}
		
		do {
			int readByte = input.UnsignedByte(index++);
			int highByte = readByte >> 0x4;
			int lowByte = readByte & 0xf;
			
			if((highByte < 0x9) && (ESI <= size - 26)) {
				memcpy(param_2, ESI, input, index, 8);
				
				ESI += highByte;
				index += highByte;
				
				highByte = input.UnsignedShort(index);
				
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
						read = input.UnsignedByte(index++);
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
				highByte = input.UnsignedShort(index);
				
				ESI = someOffset;
				EDI = someOffset - highByte;
				
				// NOTE - EAX and EBX can probably be combined
				EAX = index;
			}
			
			// EDI = x - highByte
			index = EAX + 0x2;
			
			if(lowByte == 0xf) {
				int value = 0;
				int read = 0;
				
				do {
					read = input.UnsignedByte(index++);
					value += read;
				} while(read == 0xff);
				
				lowByte = value + 0xf;
			}
			
			if(highByte < 8) {
				param_2.WriteInt(0, ESI);
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
