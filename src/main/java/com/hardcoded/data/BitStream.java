package com.hardcoded.data;

public class BitStream {
	private Memory memory;
	private int index;
	
	public BitStream(Memory memory) {
		this.memory = memory;
		
//		for(int i = 0; i < 7; i++) {
//			TileUtils.debugPrint(memory);
//			
//			int tempv = (int)readNBytes(4);
//			int value = (int)readInt();
//			move(-31);
//			
//			System.out.printf("tempv '%08x'\n", tempv);
//			System.out.printf("value '%08x'\n", value);
//			System.out.println("---------------");
//		}
	}
	
	private long readNBytes(int count) {
		if(length() < index + (count << 3)) return 0;
		
		byte[] bytes = memory.data();
		int mem_off = memory.index() + (index >> 3);
		int offset = (index & 7);
		
		long result = 0;
		index += count * 8;
		if(offset == 0) {
			for(int i = 0; i < count; i++) {
				result |= (((int)bytes[mem_off + i]) & 0xffl) << ((count - i - 1L) << 3L);
			}
			
			return result;
		}
		
		for(int i = 0; i < count; i++) {
			int a = (((int)bytes[mem_off + i    ] << (    offset))) & 0xff;
			int b = (((int)bytes[mem_off + i + 1] >> (8 - offset))) & 0xff;
			long c = (a | b) & 0xff;
			result |= (c << ((count - i - 1L) << 3L));
		}
		
		return result;
	}
	
//	public long readLong() {
//		int a = readByte();
//		int b = readByte();
//		int c = readByte();
//		int d = readByte();
//		int e = readByte();
//		int f = readByte();
//		int g = readByte();
//		int h = readByte();
//		
//		return (a << 56L) | (b << 48L) | (c << 40L) | (d << 32L) | (e << 24L) | (f << 16L) | (g << 8L) | (h << 0L);
//	}
	
	public float readFloat() {
		return Float.intBitsToFloat((int)readNBytes(4));
	}
	
	public int readInt() {
//		int a = readOldByte(0);
//		int b = readOldByte(8);
//		int c = readOldByte(16);
//		int d = readOldByte(24);
//		int r = (a << 24) | (b << 16) | (c << 8) | (d << 0);
		int n = (int)readNBytes(4);
		
//		if(r != n) {
//			move(-32);
//			TileUtils.debugPrint("BAD", memory, index >> 3);
//			TileUtils.log("int '%08x'", r);
//			TileUtils.log("new '%08x'", n);
//			throw new RuntimeException();
//		}
		
		return n;
	}
	
	public int readShort() {
//		int a = readOldByte(0);
//		int b = readOldByte(8);
//		int r = (a << 8) | (b << 0);
		int n = (int)readNBytes(2);
		
//		if(r != n) {
//			move(-16);
//			TileUtils.debugPrint("BAD", memory, index >> 3);
//			TileUtils.log("short '%04x'", r);
//			TileUtils.log("new   '%04x'", n);
//			throw new RuntimeException();
//		}
		
		return n;
	}
	
	public int readByte() {
		return (int)readNBytes(1);
//		if(length() < (index >> 3) + 8) {
//			// Bad
//			return 0;
//		}
//		
//		int offset = index & 7;
//		if(offset == 0) {
//			int result = memory.UnsignedByte((index >> 3));
//			index += 8;
//			return result;
//		}
//		
//		
//		//memory.WriteByte(0b10101101, (index >> 3) + 0);
//		//memory.WriteByte(0b11111110, (index >> 3) + 1);
//		
//		int a;
//		int b;
//		
//		if(endian) {
//			a = memory.UnsignedByte((index >> 3) + 1) << (offset);
//			b = memory.UnsignedByte((index >> 3) + 0) >> ((8 - offset) & 0x1f);
//		} else {
//			a = memory.UnsignedByte((index >> 3) + 0) << (offset);
//			b = memory.UnsignedByte((index >> 3) + 1) >> ((8 - offset) & 0x1f);
//		}
//		
////		int av = memory.UnsignedByte((index >> 3) + 0);
////		int bv = memory.UnsignedByte((index >> 3) + 1);
////		System.out.printf("a: %d\n", offset);
////		System.out.printf("a: %8s, %02x\n", String.format("%8s", Integer.toBinaryString(av)).replace(' ', '0'), av);
////		System.out.printf("b: %8s, %02x\n", String.format("%8s", Integer.toBinaryString(bv)).replace(' ', '0'), bv);
////		int c = (a | b) & 0xff;
////		System.out.printf("c: %8s, %02x\n", String.format("%8s", Integer.toBinaryString(c)).replace(' ', '0'), c);
//		index += 8;
//		return (a | b) & 0xff;
	}
	
//	public int readOldByte(int ofx) {
//		int index = this.index + ofx;
//		if(length() < index + 8) {
//			return 0;
//		}
//		
//		int offset = index & 7;
//		if(offset == 0) {
//			return memory.UnsignedByte((index >> 3));
//		}
//		
//		int a = memory.UnsignedByte((index >> 3) + 0) << (offset);
//		int b = memory.UnsignedByte((index >> 3) + 1) >> ((8 - offset) & 0x1f);
//		return (a | b) & 0xff;
//	}
	
	public boolean readBool() {
		int value = memory.UnsignedByte(index >> 3);
		boolean result = (value & (0x80 >> (index & 7))) != 0;
		index ++;
		return result;
	}
	
	public String readString(int length) {
		align();
		return new String(readBytes(length));
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
		return (memory.data().length - memory.index()) * 8;
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
