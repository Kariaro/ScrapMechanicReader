package sm.hardcoded.tile;

public class TestFunction2 {
	
	private static final int[] INT_00e6cab8 = { 0x0, 0x1, 0x2, 0x1, 0x0, 0x4, 0x4, 0x4, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7, 0x1000b, 0x10 };
	private static final int[] INT_00e6cbe0 = { 0x0, 0x0, 0x0, 0xffffffff, 0xfffffffc, 0x1, 0x2, 0x3, 0x6, 0xd, 0x0, 0x0, 0x0, 0x0, 0x0, 0x1, 0x1, 0x2, 0x0, 0x3, 0x1, 0x3, 0x1, 0x4, 0x2, 0x7, 0x0, 0x2, 0x3, 0x6, 0x1, 0x5, 0x3, 0x5, 0x1, 0x3, 0x4, 0x4, 0x2, 0x5, 0x6, 0x7, 0x7, 0x0, 0x1, 0x2, 0x3, 0x3, 0x4, 0x6, 0x2, 0x6, 0x5, 0x5, 0x3, 0x4, 0x5, 0x6, 0x7, 0x1, 0x2, 0x4, 0x6, 0x4, 0x4, 0x5, 0x7, 0x2, 0x6, 0x5, 0x7, 0x6, 0x7, 0x7 };
	
	public int decompress(byte[] bytes, byte[] input, int uncompressed_size) {
		// System.out.printf("Result: '%08x'\n", result);
		
		Pointer __input = new Pointer(1000000).WriteBytes(bytes);
		Pointer __param_2 = new Pointer(1000000);
		int __result = DecompressJava(__input, __param_2, uncompressed_size);
		
		__param_2.Bytes(input, 0, uncompressed_size, true);
		System.out.println("Jva: '" + __result + "'");
		
		// System.out.println("Asm: '" + result + "'");
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
	
	private int DecompressJava(Pointer input, Pointer param_2, int size) {
		int EAX = 0;
		int ECX = 0;
		int EDX = 0;
		int EBX = 0;
		int ESI = 0;
		int EDI = 0;
		
		int EBP_0x4 = 0;
		int EBP_0x8 = 0;
		int EBP_0x1c = 0;
		
		// int __fastcall CalculateCompressedSize_007039c0(byte* input, int* param_2, int size)
		//
		// __fastcall
		// EAX:4			int		<RETURN>
		// ECX:4			byte*	input
		// EDX:4			int*	param_2
		// Stack[0x4]:4		int		size

		// NOTE - Jump table
		// dst = src    ZF = 1    CF = 0
		// dst < src    ZF = 0    CF = 1
		// dst > src    ZF = 0    CF = 0
		
		
		// JNZ - Jump short if not zero (ZF=0)
		// Log("TEST EAX,EAX");
		// Log("JNZ LAB_00703a07");
		if(size == 0) {
			return (input.Byte() == 0) ? -1:1;
		}
		
		EAX = size;
		
		for(;;) { // LAB_00703a07:
			// NOTE - EDI is always 'SIZE - 0x1a' here
			EDI = size - 0x1a;
			
			int readByte = input.UnsignedByte(EBX++);
			ECX = readByte;
			
			EBP_0x8 = readByte;
			EBP_0x4 = readByte >> 0x4;
			EDX = EBP_0x4;
			
			boolean jump_00703a9b = true;
			
			// JA - Jump short if above (CF=0 and ZF=0)
			// Log("CMP EDX,0x8");
			// Log("JA LAB_00703a81");
			if(EBP_0x4 > 0x8) {	// LAB_00703a81: (Siplified)
				// JNZ - Jump short if not zero (ZF=0)
				// Log("CMP EDX,0xf");
				// Log("JNZ LAB_00703a9b");
				if(EBP_0x4 == 0xf) {
					int value = 0;
					int read = 0;
					
					do { // LAB_00703a88:
						read = input.UnsignedByte(EBX++);
						value += read;
					} while(read == 0xff);
					
					EBP_0x4 = value + 0xf;
					EDX = EBP_0x4;
					ECX = value;
				}
				
				// JA - Jump short if above (CF=0 and ZF=0)
				// Log("CMP ESI,EDI");
				// Log("JA LAB_00703a9b");
			} else if(!(ESI > size - 0x1a)) {
				memcpy(param_2, ESI, input, EBX, 8);
				
				ESI += EBP_0x4;
				EBX += EBP_0x4;
				
				EBP_0x4 = input.UnsignedShort(EBX);
				EBP_0x8 = readByte & 0xf;
				
				
				// JZ - Jump short if zero (ZF = 1)
				// Log("CMP ECX,0xf");					// 00703a4d
				// Log("JZ LAB_00703a7c");				// 00703a50
				
				// JC - Jump short if carry (CF = 1)
				// Log("CMP EAX,0x8");					// 00703a52
				// Log("JC LAB_00703a7c");				// 00703a55
				if((EBP_0x8 == 0xf) || (EBP_0x4 < 0x8)) { // LAB_00703a7c
					EAX = EBX;
					
					EDI = ESI - EBP_0x4;
					EBX += 0x2;
					ECX &= 0xf;
					
					jump_00703a9b = false;
				} else {
					memcpy(param_2, ESI, param_2, ESI - EBP_0x4, 0x12);
					
					ESI += EBP_0x8 + 0x4;
					EBX += 0x2;
					
					// EDI = SIZE - 0x1a;
					continue;
				}
			}
			
			if(jump_00703a9b) { // LAB_00703a9b:
				// JA - Jump short if above (CF=0 and ZF=0)
				// Log("CMP EDI,EAX");					// 00703aa4
				// Log("JA LAB_00703bfb");				// 00703aa6
				if(EDX + ESI > size - 0x8) { // LAB_00703bfb:
					// JNZ - Jump short if not zero (ZF=0)
					// Log("CMP EDI,ECX");					// 00703bfb
					// Log("JNZ LAB_00703c19");				// 00703bfd
					if(EDX + ESI != size) break; // Jump to end of function
					
					// param_2.WriteBytes(input.Bytes(EDX, EBX), EDX, ESI);
					memmove(param_2, ESI, input, EBX, EDX);
					
					// return EBP_0x4 - EBP_0x14 + EBX;
					return EBP_0x4 + EBX;
				}
				
				// EAX = EBP_0xc - 0x8;
				EDI = EDX + ESI;
				ECX = EBX - ESI;
				
				// TODO - Replace this with memcpy
				do { // LAB_00703ab0:
					memcpy(param_2, ESI, input, ECX + ESI, 8);
					ESI += 0x8;
					
					// JC - Jump short if carry (CF = 1)
					// Log("CMP ESI,EDI");						// 00703abf
					// Log("JC LAB_00703ab0");					// 00703ac1
				} while(ESI < EDI);
				
				EBX += EDX;
				
				EBP_0x4 = input.UnsignedShort(EBX);
				
				ESI = EDI;
				EDI -= EBP_0x4;
				
				ECX = EBP_0x8 & 0xf;
				EAX = EBX;
			}
			
			// LAB_00703ade:
			EBP_0x8 = EAX + 0x2;
			EBX = EBP_0x8;
			
			// JNZ - Jump short if not zero (ZF=0)
			// Log("CMP ECX,0xf");						// 00703ae4
			// Log("JNZ LAB_00703b03");					// 00703ae7
			if(ECX == 0xf) {
				int value = 0;
				int read = 0;
				
				do { // LAB_00703af0:
					read = input.UnsignedByte(EBX++);
					value += read;
				} while(read == 0xff);
				
				EBP_0x8 = EBX;
				ECX = value + 0xf;
			}
			
			// LAB_00703b03:
			// JNC - Jump short if not carry (CF=0)
			// Log("CMP dword ptr [EBP + -0x4],0x8");	// 00703b06
			// Log("JNC LAB_00703b4d");					// 00703b10
			if(EBP_0x4 < 0x8) {
				param_2.WriteInt(0, ESI);
				memcpy(param_2, ESI + 0x0, param_2, EDI, 4);
				EDI += INT_00e6cab8[EBP_0x4];
				
				memcpy(param_2, ESI + 0x4, param_2, EDI, 4);
				EDI -= INT_00e6cbe0[EBP_0x4];
			} else { // LAB_00703b4d:
				memcpy(param_2, ESI, param_2, EDI, 8);
				EDI += 0x8;
			}
			
			// LAB_00703b5a:
			// EAX = EBP_0xc - 0xc;
			EBP_0x1c = ECX + ESI + 0x4;
			ECX += 0x4;
			ESI += 0x8;
			
			// JBE - Jump short if below or equal (CF=1 or ZF=1)
			// Log("CMP EDX,EAX");						// 00703b63
			// Log("JBE LAB_00703bc8");					// 00703b65
			if(!(EBP_0x1c <= size - 0xc)) {
				// JA - Jump short if above (CF=0 and ZF=0)
				// Log("CMP EDX,EAX");							// 00703b73
				// Log("JA LAB_00703c19");						// 00703b75
				if(EBP_0x1c > size - 0x5) break;
				
				// ECX = size - 0x7;
				EAX = size - 0x5;
				
				// JNC - Jump short if not carry (CF=0)
				// Log("CMP ESI,ECX");						// 00703b7b
				// Log("JNC LAB_00703bb1");					// 00703b7d
				if(ESI < size - 0x7) { // JC branch
					int start = ESI;
					EDX = EDI - ESI;
					
					// TODO - Replace this with memcpy
					do { // LAB_00703b90:
						memcpy(param_2, start, input, EDX + start, 8);
						start += 0x8;
						
						// JC - Jump short if carry (CF = 1)
						// Log("CMP ECX,EBX");							// 00703b9f
						// Log("JC LAB_00703b90");						// 00703ba1
					} while(start < size - 0x7);
					
					EAX = size - 0x7 - ESI;
					EDI += EAX;
					
					ESI = size - 0x7;
					EBX = EBP_0x8;
				}
				
				// LAB_00703bb1:
				// JNC - Jump short if not carry (CF=0)
				// Log("CMP ESI,EDX");						// 00703bb1
				// Log("JNC LAB_00703bf4");					// 00703bb3
				if(ESI < EBP_0x1c) {
					memcpy(param_2, ESI, param_2, EDI, EBP_0x1c - ESI);
				} else { // LAB_00703bf4:
					
				}
				
				ESI = EBP_0x1c;
				continue;
			}
			
			// LAB_00703bc8:
			memcpy(param_2, ESI, param_2, EDI, 8);
			
			// JBE - Jump short if below or equal (CF=1 or ZF=1)
			// Log("CMP ECX,0x10");					// 00703bd2
			// Log("JBE LAB_00703bf4");				// 00703bd5
			if(!(ECX <= 0x10)) { // JA branch
				ESI += 0x8;
				EDI -= ESI;

				// TODO - Replace this with memcpy
				do { // LAB_00703be0:
					memcpy(param_2, ESI, param_2, EDI + ESI + 0x8, 8);
					ESI += 0x8;
					
					// JC - Jump short if carry (CF = 1)
					// Log("CMP ESI,EDX");							// 00703bf0
					// Log("JC LAB_00703be0");						// 00703bf2
				} while(ESI < EBP_0x1c);
			}
			
			ESI = EBP_0x1c;
			continue;
		}
		
		// LAB_00703c19:
		return - (EBX + 1);
	}
}
