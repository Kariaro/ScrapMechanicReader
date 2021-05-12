package com.hardcoded.data;

public class BitStream {
	private Memory memory;
	private int index;
	
	public BitStream(Memory memory) {
		this.memory = memory;
	}
	
	public int readByte() {
		return readByte(false);
	}
	
	public int readInt() {
		int a = readByte();
		int b = readByte();
		int c = readByte();
		int d = readByte();
		
		return (a << 24) | (b << 16) | (c << 8) | (d << 0);
	}
	
	public int readShort() {
		int a = readByte();
		int b = readByte();
		
		return (a << 8) | (b << 0);
	}
	
	public int readByte(boolean endian) {
		int offset = index & 7;
		if(offset == 0) {
			int result = memory.UnsignedByte((index >> 3));
			index += 8;
			return result;
		}
		
		//memory.WriteByte(0b10101101, (index >> 3) + 0);
		//memory.WriteByte(0b11111110, (index >> 3) + 1);
		
		int a;
		int b;
		
		if(endian) {
			a = memory.UnsignedByte((index >> 3) + 1) << (offset);
			b = memory.UnsignedByte((index >> 3) + 0) >> ((8 - offset) & 0x1f);
		} else {
			a = memory.UnsignedByte((index >> 3) + 0) << (offset);
			b = memory.UnsignedByte((index >> 3) + 1) >> ((8 - offset) & 0x1f);
		}
		
//		int av = memory.UnsignedByte((index >> 3) + 0);
//		int bv = memory.UnsignedByte((index >> 3) + 1);
//		System.out.printf("a: %d\n", offset);
//		System.out.printf("a: %8s, %02x\n", String.format("%8s", Integer.toBinaryString(av)).replace(' ', '0'), av);
//		System.out.printf("b: %8s, %02x\n", String.format("%8s", Integer.toBinaryString(bv)).replace(' ', '0'), bv);
//		int c = (a | b) & 0xff;
//		System.out.printf("c: %8s, %02x\n", String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'), c);
		index += 8;
		return (a | b) & 0xff;
	}
	
	public boolean readBool() {
		int value = readByte() & 1;
		index -= 7;
		return (value != 0);
	}
	
	public byte[] readBytes(int length) {
		byte[] result = new byte[length];
		align();
		
		for(int i = 0; i < length; i++) {
			result[i] = (byte)readByte();
		}
		
		return result;
	}
	
	public char[] readChars(int length) {
		char[] result = new char[length];
		
		for(int i = 0; i < length; i++) {
			result[i] = (char)readByte();
		}
		
		return result;
	}
	
	public int index() {
		return index;
	}
	
	public int length() {
		return memory.data().length - memory.index();
	}
	
	public void align() {
		index += 7 - ((index - 1) & 7);
	}
	
	public void move(int step) {
		index += step;
	}

	public Memory memory() {
		return memory;
	}
}
