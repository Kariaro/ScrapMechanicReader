package com.hardcoded.lua;

import com.hardcoded.data.Memory;

class LuaDecomp {
	public static void main(String[] args) {
		LuaDecomp comp = new LuaDecomp();
		//byte[] bytes = hex("f0154c55410000000105000000050200000009006c69676874496e74657261637461626c65731d00f3000680000000b2000013938000000532090014061200140712001407120014081200fe0308820000000c8064697370656e736572626f5e0010007b00ed0c806e6f74657465726d696e616c82001801820010043100ff000d0073686970776f726b62656e636832000b10038700a003006163746976650200");
		byte[] bytes = hex("0000000100010b030000004bf11e3b8d6a5e003024535552564956414c5f444154412f536372697074732f67616d652f776f726c64732f4f7665720b00652e6c756100090f00f00000056e756c6c0a0000000000000000");
		//byte[] bytes = hex("4c55410000000105000000050200000009006c69676874496e74657261637461626c65731d00f3000680000000b2000013938000000532090014061200140712001407120014081200fe0308820000000c8064697370656e736572626f5e0010007b00ed0c806e6f74657465726d696e616c82001801820010043100ff000d0073686970776f726b62656e636832000b10038700a003006163746976650200");
		//bytes = hex("000004fe00012e030000006dd04c5541000000010500000004020500f016007175616e74697479080104000000107469636b734c656674496e576f726c64060001f2e31a00f00b047575696464000027115ed92dfd4df4259c524c07e68c120ffb1e00f0050e6c6173745469636b5570646174650600026c54");
		
		
		DecompressLua lua = new DecompressLua();
		
		byte[] outps = new byte[5616];
		int decp = lua.decompress(bytes, outps, 0x20, 0x200);
		// int decp = func.run(bytes, bytes, 0x200, 0x200);
		
		System.out.println("decp: " + decp);
		System.out.println("decp:\n" + getHexString(outps, 512, 64));
		int iOriginalSize = 159;
		Memory mem = new Memory(bytes);
		Memory type = new Memory(10000);
		
		Input inp = new Input();
		
		// BitStream_???_00c751c0(local_23c, SERIALIZED_BUFFER, (uint)iOriginalSize, 0);
		// (int* test, byte* input, int size, char param_4)
		inp._0 = iOriginalSize * 8;
		inp._1 = iOriginalSize * 8;
		inp._2 = 0;
		inp._3 = mem;
		
		
		System.out.println("hex: " + getHexString(inp._3, 64, bytes.length));
		
		for(int i = 0; i < 100; i++) {
			int result = comp.DECOMP_00c75540(inp, type, 0x20, 2);
			
			if(result == 0) break;
			
			System.out.println("result: " + result);
			System.out.println("hex: " + getHexString(type, 16, bytes.length));
			System.out.println("str: '" + new String(type.data(), 0, 16) + "'");
		}
	}
	
	public static String getHexString(Memory bytes, int maxLength, int lineLength) {
		return getHexString(bytes.data(), maxLength, lineLength);
	}
	
	public static String getHexString(byte[] bytes, int maxLength, int lineLength) {
		StringBuilder sb = new StringBuilder();
		int a = 1;
		for(int i = 0; i < Math.min(bytes.length, maxLength); i++) {
			sb.append(String.format("%02x", bytes[i]));
			if((a ++) % lineLength == 0) sb.append('\n');
		}
		
		return sb.toString();
	}
	
	private static byte[] hex(String string) {
		byte[] result = new byte[string.length() / 2];
		for(int i = 0; i < string.length(); i += 2) {
			String h = string.substring(i, i + 2);
			
			result[i / 2] = (byte)(Integer.valueOf(h, 16).intValue());
		}
		return result;
	}
	
	private void memcpy(Memory src, int srcPos, Memory dst, int dstPos, int size) {
		byte[] s = src.data();
		byte[] d = dst.data();
		for(int i = 0; i < size; i++) {
			s[srcPos + i] = d[dstPos + i];
		}
	}
	
	private void memset(Memory src, int value, int size) {
		src.WriteBytes(new byte[size], size);
	}
	
	private int DECOMP_00c75540(Input input, Memory output, int size, int param_4) {
		int uVar1;
		int uVar2;
		int readByte;
		
		// int input_0 = 0; // ???
		// int input_2 = 0; // Offset
		// int input_3 = 0; // Pointer
		
		// uVar1 = input[2];
		uVar1 = input._2;
		
		// if ((size == 0) || (*input <= uVar1 + size && uVar1 + size != *input)) {
		// if ((size == 0) || (*input <= (uVar1 + size) && *input != (uVar1 + size))) {
		if ((size == 0) || (input._0 < (uVar1 + size))) {
			return 0;
		}
		
		uVar2 = uVar1 & 7;
		
		if((uVar2 == 0) && ((size & 7) == 0)) {
			memcpy(output, output.index(), input._3, (uVar1 >> 3), size >> 3);
			
			//input[2] = input[2] + size;
			input._2 = input._2 + size;
			return 1;
		}
		
		memset(output, 0, (size + 7) >> 3);
		
		do {
			// readByte = *(char *)((input[2] >> 3) + input[3]) << (sbyte)uVar2 | *output;
			// readByte = *(char *)(input._3 + (input._2 >> 3)) << (uVar2 | output.UnsignedByte());
			// readByte = (*(byte *)(input._3 + (input._2 >> 3) + 0) << uVar2) | output.UnsignedByte();
			readByte = (input._3.UnsignedByte((input._2 >> 3) + 0) << uVar2) | output.UnsignedByte();
			
			// *output = readByte;
			output.WriteByte(readByte);
			
			if((uVar2 != 0) && (8 - uVar2 < size)) {
				// output._0_1_ = (byte)(8 - uVar2);
				// *output = *(byte *)((input[2] >> 3) + 1 + input[3]) >> ((byte)output & 0x1f) | readByte;
				// *output = *(byte *)(input._3 + (input._2 >> 3) + 1) >> ((byte)output & 0x1f) | readByte;
				// *output = (*(byte *)(input._3 + (input._2 >> 3) + 1) >> ((byte)output & 0x1f)) | readByte;
				
				int read = input._3.UnsignedByte((input._2 >> 3) + 1);
				read = (read >> ((8 - uVar2) & 0x1f)) | readByte;
				output.WriteByte(read);
			}
			
			if(size < 8) {
				/*
				if((int)(size - 8) < 0) {
					if(param_4 != '\0') {
						*output = *output >> (-(char)(size - 8) & 0x1fU);
					}
					input[2] = input[2] + size;
				} else {
					input[2] = input[2] + 8;
				}
				*/
				
				if(param_4 != 0) {
					// *output = *output >> (-(char)(size - 8) & 0x1fU);
					output.WriteByte(output.UnsignedByte() >> ((8 - size) & 0x1f));
				}
				
				// input[2] = input[2] + size;
				input._2 += size;
				size = 0;
			} else {
				//size = size - 8;
				size -= 8;
				
				// input[2] = input[2] + 8;
				input._2 += 8;
			}
			
			//output = output + 1;
			output.move(1);
		} while(size != 0);
		
		return 1;
	}
	
	static class Input {
		public int _0;
		public int _1;
		public int _2;
		public Memory _3;
	}
	
}
