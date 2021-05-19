package com.hardcoded.data;

import java.util.UUID;

/**
 * A bit stream implementation.
 * 
 * @author HardCoded
 * @since v0.2
 */
public class BitStream {
	private final byte[] data;
	private int index;
	
	public BitStream(Memory memory) {
		data = new byte[memory.data().length - memory.index()];
		System.arraycopy(memory.data(), memory.index(), data, 0, data.length);
	}
	
//	public int readOldByte(int ofs) {
//		int index = this.index + ofs;
//		if(length() < (index >> 3) + 1) {
//			return 0;
//		}
//		
//		int offset = index & 7;
//		if(offset == 0) {
//			int result = memory.UnsignedByte(index >> 3);
//			return result;
//		}
//		
//		int a = memory.UnsignedByte((index >> 3) + 0) << (offset);
//		int b = memory.UnsignedByte((index >> 3) + 1) >> (8 - offset);
//		
//		return (a | b) & 0xff;
//	}
	
	private long readNBytes(int count) {
		if(length() < (index >> 3) + count) return 0;
		
		int mem_off = (index >> 3);
		int offset = (index & 7);
		
		long result = 0;
		index += count * 8;
		if(offset == 0) {
			for(int i = 0; i < count; i++) {
				result |= (((long)data[mem_off + i]) & 0xff) << ((count - i - 1L) << 3L);
			}
			
			return result;
		}
		
		for(int i = 0; i < count; i++) {
			int a = ((((int)data[mem_off + i    ] & 0xff) << (    offset))) & 0xff;
			int b = ((((int)data[mem_off + i + 1] & 0xff) >> (8 - offset))) & 0xff;
			long c = (a | b) & 0xff;
			result |= (c << ((count - i - 1L) << 3L));
		}
		
		return result;
	}
	
	public float readFloat() {
		return Float.intBitsToFloat((int)readNBytes(4));
	}
	
	public UUID readUuid() {
		return new UUID(readNBytes(8), readNBytes(8));
	}
	
	public int readInt() {
//		int a = readOldByte(0);
//		int b = readOldByte(8);
//		int c = readOldByte(16);
//		int d = readOldByte(24);
//		int r = (a << 24) | (b << 16) | (c << 8) | (d << 0);
//		int n = (int)readNBytes(4);
//		
//		if(r != n) {
//			System.out.printf("int   : '%08x' (%d)\n", r, r);
//			System.out.printf("new   : '%08x' (%d)\n", n, n);
//			throw new RuntimeException();
//		}
//		
//		return n;
		return (int)readNBytes(4);
	}
	
	public int readShort() {
//		int a = readOldByte(0);
//		int b = readOldByte(8);
//		int r = (a << 8) | (b << 0);
//		int n = (int)readNBytes(2);
//		
//		if(r != n) {
//			System.out.printf("short : '%08x' (%d)\n", r, r);
//			System.out.printf("new   : '%08x' (%d)\n", n, n);
//			throw new RuntimeException();
//		}
//		
//		return n;
		return (int)readNBytes(2);
	}
	
	public int readByte() {
//		int a = readOldByte(0);
//		int r = a;
//		int n = (int)readNBytes(1);
//		
//		if(r != n) {
//			System.out.printf("byte  : '%08x' (%d)\n", r, r);
//			System.out.printf("new   : '%08x' (%d)\n", n, n);
//			throw new RuntimeException();
//		}
//		
//		return n;
		return (int)readNBytes(1);
	}
	
	public boolean readBool() {
		int value = ((int)data[index >> 3]) & 0xff;// memory.UnsignedByte(index >> 3);
		boolean result = (value & (0x80 >> (index & 7))) != 0;
		index ++;
		return result;
	}
	
	public byte[] readBytes(int length) {
		byte[] result = new byte[length];
		align();
		System.arraycopy(data, (index >> 3), result, 0, length);
		index += length * 8;
		return result;
	}
	
	public String readString(int length) {
		return new String(readBytes(length));
	}
	
	public int index() {
		return index;
	}
	
	public int length() {
		return data.length;
		//memory.data().length - memory.index();
	}
	
	public void align() {
		index += (7 - ((index - 1) & 7));
	}
	
	public void move(int step) {
		index += step;
	}
	
	public byte[] data() {
		return data;
	}
}
