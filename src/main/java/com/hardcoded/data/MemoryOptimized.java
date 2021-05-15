package com.hardcoded.data;

import java.util.UUID;

/**
 * A class designed to make it easier to read serialized data.
 * 
 * @author HardCoded
 * @since v0.2
 * 
 * FIXME: This class gives invalid results for certain inputs. Debug!
 */
class MemoryOptimized {
	private byte[] bytes;
	private boolean defaultEndian;
	private int highest_written_index;
	private int index;
	
	public MemoryOptimized(int capacity) {
		this(null, capacity);
	}
	
	public MemoryOptimized(byte[] bytes) {
		this(bytes, bytes.length);
	}
	
	public MemoryOptimized(byte[] bytes, int capacity) {
		byte[] array = new byte[capacity];
		
		if(bytes != null) 
			System.arraycopy(bytes, 0, array, 0, Math.min(bytes.length, capacity));
	
		this.bytes = array;
	}
	
	public MemoryOptimized(byte[] bytes, int offset, int size) {
		byte[] array = new byte[size];
		
		if(bytes != null) 
			System.arraycopy(bytes, offset, array, 0, Math.min(bytes.length - offset, size));
	
		this.bytes = array;
	}
	
	// ======================= //
	
	public MemoryOptimized setDefaultBigEndian(boolean b) {
		defaultEndian = b;
		return this;
	}
	
	public MemoryOptimized set(int i) {
		index = i;
		return this;
	}
	
	public MemoryOptimized move(int i) {
		index += i;
		return this;
	}
	
	public byte[] data() {
		return bytes;
	}
	
	public int index() {
		return index;
	}
	
	public int getHighestWrittenIndex() {
		return highest_written_index;
	}
	
	public void expand(int size) {
		if(bytes.length >= size) return;
		
		byte[] array = new byte[size];
		
		if(bytes != null) 
			System.arraycopy(bytes, 0, array, 0, bytes.length);
		
		this.bytes = array;
	}
	// ======================= //
	
	private long readValue(int offset, int length, boolean bigEndian) {
		long result = 0;
		
		for(int i = 0; i < length; i++) {
			long val = Byte.toUnsignedLong(bytes[index + offset + i]);
			long shr = (bigEndian ? (length - 1 - i):i) * 8L;
			result |= (val << shr);
		}
		
		return result;
	}
	
	private long nextReadValue(int length, boolean bigEndian) {
		long result = 0;
		
		for(int i = 0; i < length; i++) {
			long val = Byte.toUnsignedLong(bytes[index + i]);
			long shr = (bigEndian ? (length - 1 - i):i) * 8L;
			result |= (val << shr);
		}
		
		index += length;
		
		return result;
	}
	
	private MemoryOptimized writeValue(long number, int offset, int length, boolean bigEndian) {
		// Clamp number
		number &= (~((-1L) << (length)));
//		if(number instanceof Byte) value = Byte.toUnsignedLong(number.byteValue());
//		if(number instanceof Short) value = Short.toUnsignedLong(number.shortValue());
//		if(number instanceof Integer) value = Integer.toUnsignedLong(number.intValue());
//		if(number instanceof Long) value = number.longValue();
//		if(number instanceof Float) value = Integer.toUnsignedLong(Float.floatToIntBits(number.floatValue()));
//		if(number instanceof Double) value = Double.doubleToRawLongBits(number.doubleValue());
		
		for(int i = 0; i < length; i++) {
			long shr = (bigEndian ? (length - 1 - i):i) * 8L;
			byte val = (byte)((number >>> shr) & 0xff);
			bytes[index + offset + i] = val;
		}
		
		int idx = index + length + offset;
		if(idx > highest_written_index) highest_written_index = idx;
		
		return this;
	}
	
	private MemoryOptimized nextWriteValue(long number, int length, boolean bigEndian) {
		// Clamp number
		number &= (~((-1L) << (length)));
		for(int i = 0; i < length; i++) {
			long shr = (bigEndian ? (length - 1 - i):i) * 8L;
			byte val = (byte)((number >>> shr) & 0xff);
			bytes[index + i] = val;
		}
		
		index += length;
		if(index > highest_written_index) highest_written_index = index;
		
		return this;
	}
	
	// ======================= //
	
	public byte readByte() {
		return bytes[index];
	}
	
	public byte readByte(int offset) {
		return bytes[index + offset];
	}
	
	public byte nextReadByte() {
		return bytes[index++];
	}
	
	public byte nextReadByte(int offset) {
		return bytes[(index++) + offset];
	}
	
	public int readUnsignedByte() {
		return ((int)bytes[index]) & 0xff;
	}
	
	public int readUnsignedByte(int offset) {
		return ((int)bytes[index + offset]) & 0xff;
	}
	
	public int nextReadUnsignedByte() {
		return ((int)bytes[(index++)]) & 0xff;
	}
	
	public int nextReadUnsignedByte(int offset) {
		return ((int)bytes[(index++) + offset]) & 0xff;
	}
	
	public void writeByte(int value) {
		bytes[index] = (byte)value;
	}
	
	public void writeByte(int value, int offset) {
		bytes[index + offset] = (byte)value;
	}
	
	public void nextWriteByte(int value) {
		bytes[(index++)] = (byte)value;
	}
	
	public void nextWriteByte(int value, int offset) {
		bytes[(index++) + offset] = (byte)value;
	}
	
	// ======================= //
	
	public short readShort() {
		return readShort(0, defaultEndian);
	}
	
	public short readShort(int offset) {
		return readShort(offset, defaultEndian);
	}
	
	public short readShort(boolean bigEndian) {
		return readShort(0, bigEndian);
	}
	
	public short readShort(int offset, boolean bigEndian) {
		return (short)readValue(offset, 2, bigEndian);
	}
	
	public short nextReadShort() {
		return (short)nextReadValue(2, defaultEndian);
	}
	
	public short nextReadShort(boolean bigEndian) {
		return (short)nextReadValue(2, bigEndian);
	}
	
	public int readUnsignedShort() {
		return readUnsignedShort(0, defaultEndian);
	}
	
	public int readUnsignedShort(int offset) {
		return readUnsignedShort(offset, defaultEndian);
	}
	
	public int readUnsignedShort(boolean bigEndian) {
		return readUnsignedShort(0, bigEndian);
	}
	
	public int readUnsignedShort(int offset, boolean bigEndian) {
		return ((int)readValue(offset, 2, bigEndian)) & 0xffff;
	}
	
	public int nextReadUnsignedShort() {
		return ((int)nextReadValue(2, defaultEndian)) & 0xffff;
	}
	
	public int nextReadUnsignedShort(boolean bigEndian) {
		return ((int)nextReadValue(2, bigEndian)) & 0xffff;
	}
	
	public void writeShort(long value) {
		writeValue(value, 0, 2, defaultEndian);
	}
	
	public void writeShort(long value, int offset) {
		writeValue(value, offset, 2, defaultEndian);
	}
	
	public void writeShort(long value, boolean bigEndian) {
		writeValue(value, 0, 2, bigEndian);
	}
	
	public void writeShort(long value, int offset, boolean bigEndian) {
		writeValue(value, offset, 2, bigEndian);
	}
	
	public void nextWriteShort(long value) {
		nextWriteValue(value, 2, defaultEndian);
	}
	
	public void nextWriteShort(long value, boolean bigEndian) {
		nextWriteValue(value, 2, bigEndian);
	}
	
	// ======================= //
	
	public int readInt() {
		return readInt(0, defaultEndian);
	}
	
	public int readInt(int offset) {
		return readInt(offset, defaultEndian);
	}
	
	public int readInt(boolean bigEndian) {
		return readInt(0, bigEndian);
	}
	
	public int readInt(int offset, boolean bigEndian) {
		return (int)readValue(offset, 4, bigEndian);
	}
	
	public int nextReadInt() {
		return (int)nextReadValue(4, defaultEndian);
	}
	
	public int nextReadInt(boolean bigEndian) {
		return (int)nextReadValue(4, bigEndian);
	}
	
	public long readUnsignedInt() {
		return readUnsignedInt(0, defaultEndian);
	}
	
	public long readUnsignedInt(int offset) {
		return readUnsignedInt(offset, defaultEndian);
	}
	
	public long readUnsignedInt(boolean bigEndian) {
		return readUnsignedInt(0, bigEndian);
	}
	
	public long readUnsignedInt(int offset, boolean bigEndian) {
		return ((long)readValue(offset, 4, bigEndian)) & 0xffffffffL;
	}
	
	public long nextReadUnsignedInt() {
		return ((long)nextReadValue(2, defaultEndian)) & 0xffffffffL;
	}
	
	public long nextReadUnsignedInt(boolean bigEndian) {
		return ((long)nextReadValue(4, bigEndian)) & 0xffffffffL;
	}
	
	public void writeInt(long value) {
		writeValue(value, 0, 4, defaultEndian);
	}
	
	public void writeInt(long value, int offset) {
		writeValue(value, offset, 4, defaultEndian);
	}
	
	public void writeInt(long value, boolean bigEndian) {
		writeValue(value, 0, 4, bigEndian);
	}
	
	public void writeInt(long value, int offset, boolean bigEndian) {
		writeValue(value, offset, 4, bigEndian);
	}
	
	public void nextWriteInt(long value) {
		nextWriteValue(value, 4, defaultEndian);
	}
	
	public void nextWriteInt(long value, boolean bigEndian) {
		nextWriteValue(value, 4, bigEndian);
	}
	
	// ======================= //
	
	public long readLong() {
		return readLong(0, defaultEndian);
	}
	
	public long readLong(int offset) {
		return readLong(offset, defaultEndian);
	}
	
	public long readLong(boolean bigEndian) {
		return readLong(0, bigEndian);
	}
	
	public long readLong(int offset, boolean bigEndian) {
		return readValue(offset, 8, bigEndian);
	}
	
	public long nextReadLong() {
		return nextReadValue(8, defaultEndian);
	}
	
	public long nextReadLong(boolean bigEndian) {
		return nextReadValue(8, bigEndian);
	}
	
	public void writeLong(long value) {
		writeValue(value, 0, 8, defaultEndian);
	}
	
	public void writeLong(long value, int offset) {
		writeValue(value, offset, 8, defaultEndian);
	}
	
	public void writeLong(long value, boolean bigEndian) {
		writeValue(value, 0, 8, bigEndian);
	}
	
	public void writeLong(long value, int offset, boolean bigEndian) {
		writeValue(value, offset, 8, bigEndian);
	}
	
	public void nextWriteLong(long value) {
		nextWriteValue(value, 8, defaultEndian);
	}
	
	public void nextWriteLong(long value, boolean bigEndian) {
		nextWriteValue(value, 8, bigEndian);
	}
	
	// ======================= //
	
	public float readFloat() {
		return readFloat(0, defaultEndian);
	}
	
	public float readFloat(int offset) {
		return readFloat(offset, defaultEndian);
	}
	
	public float readFloat(boolean bigEndian) {
		return readFloat(0, bigEndian);
	}
	
	public float readFloat(int offset, boolean bigEndian) {
		return Float.intBitsToFloat((int)readValue(offset, 4, bigEndian));
	}
	
	public float nextReadFloat() {
		return Float.intBitsToFloat((int)nextReadValue(4, defaultEndian));
	}
	
	public float nextReadFloat(boolean bigEndian) {
		return Float.intBitsToFloat((int)nextReadValue(4, bigEndian));
	}
	
	public void writeFloat(float value) {
		writeValue(Float.floatToRawIntBits(value), 0, 4, defaultEndian);
	}
	
	public void writeFloat(float value, int offset) {
		writeValue(Float.floatToRawIntBits(value), offset, 4, defaultEndian);
	}
	
	public void writeFloat(float value, boolean bigEndian) {
		writeValue(Float.floatToRawIntBits(value), 0, 4, bigEndian);
	}
	
	public void writeFloat(float value, int offset, boolean bigEndian) {
		writeValue(Float.floatToRawIntBits(value), offset, 4, bigEndian);
	}
	
	public void nextWriteFloat(float value) {
		nextWriteValue(Float.floatToRawIntBits(value), 4, defaultEndian);
	}
	
	public void nextWriteFloat(float value, boolean bigEndian) {
		nextWriteValue(Float.floatToRawIntBits(value), 4, bigEndian);
	}
	
	// ======================= //
	
	public double readDouble() {
		return readDouble(0, defaultEndian);
	}
	
	public double readDouble(int offset) {
		return readDouble(offset, defaultEndian);
	}
	
	public double readDouble(boolean bigEndian) {
		return readDouble(0, bigEndian);
	}
	
	public double readDouble(int offset, boolean bigEndian) {
		return Double.longBitsToDouble(readValue(offset, 8, bigEndian));
	}
	
	public double nextReadDouble() {
		return Double.longBitsToDouble(nextReadValue(8, defaultEndian));
	}
	
	public double nextReadDouble(boolean bigEndian) {
		return Double.longBitsToDouble(nextReadValue(8, bigEndian));
	}
	
	public void writeDouble(double value) {
		writeValue(Double.doubleToRawLongBits(value), 0, 8, defaultEndian);
	}
	
	public void writeDouble(double value, int offset) {
		writeValue(Double.doubleToRawLongBits(value), offset, 8, defaultEndian);
	}
	
	public void writeDouble(double value, boolean bigEndian) {
		writeValue(Double.doubleToRawLongBits(value), 0, 8, bigEndian);
	}
	
	public void writeDouble(double value, int offset, boolean bigEndian) {
		writeValue(Double.doubleToRawLongBits(value), offset, 8, bigEndian);
	}
	
	public void nextWriteDouble(double value) {
		nextWriteValue(Double.doubleToRawLongBits(value), 8, defaultEndian);
	}
	
	public void nextWriteDouble(double value, boolean bigEndian) {
		nextWriteValue(Double.doubleToRawLongBits(value), 8, bigEndian);
	}
	
	// ======================= //
	
	public byte[] readBytes(int length) {
		return readBytes(length, 0, false);
	}
	
	public byte[] readBytes(int length, int offset) {
		return readBytes(length, offset, false);
	}
	
	public byte[] readBytes(int length, boolean reverse) {
		return readBytes(length, 0, reverse);
	}
	
	public byte[] readBytes(int length, int offset, boolean reverse) {
		byte[] result = new byte[length];
		
		if(!reverse) {
			System.arraycopy(this.bytes, index, result, 0, length);
			return result;
		}
		
		int ofs = index + offset + length - 1;
		for(int i = 0; i < length; i++) {
			result[i] = bytes[ofs - i];
		}
		
		return result;
	}
	
	public byte[] nextReadBytes(int length) {
		return nextReadBytes(length, false);
	}
	
	public byte[] nextReadBytes(int length, boolean reverse) {
		byte[] result = new byte[length];
		
		if(!reverse) {
			System.arraycopy(this.bytes, index, result, 0, length);
			index += length;
			return result;
		}
		
		index += length;
		int ofs = index - 1;
		for(int i = 0; i < length; i++) {
			result[i] = bytes[ofs - i];
		}
		
		return result;
	}
	
	public void writeBytes(byte[] bytes) {
		writeBytes(bytes, bytes.length, 0, false);
	}
	
	public void writeBytes(byte[] bytes, int length) {
		writeBytes(bytes, length, 0, false);
	}
	
	public void writeBytes(byte[] bytes, int length, int offset) {
		writeBytes(bytes, length, offset, false);
	}
	
	public void writeBytes(byte[] bytes, int length, boolean reverse) {
		writeBytes(bytes, length, 0, reverse);
	}
	
	public void writeBytes(byte[] bytes, int length, int offset, boolean reverse) {
		if(!reverse) {
			System.arraycopy(bytes, 0, this.bytes, index + offset, length);

			int idx = index + offset + length;
			if(idx > highest_written_index) highest_written_index = idx;
			return;
		}
		
		int ofs = index + offset + length - 1;
		for(int i = 0; i < length; i++) {
			this.bytes[ofs - i] = bytes[i];
		}
		
		int idx = index + offset + length;
		if(idx > highest_written_index) highest_written_index = idx;
	}
	
	public void nextWriteBytes(byte[] bytes) {
		nextWriteBytes(bytes, bytes.length, false);
	}
	
	public void nextWriteBytes(byte[] bytes, int length) {
		nextWriteBytes(bytes, length, false);
	}
	
	public void nextWriteBytes(byte[] bytes, int length, boolean reverse) {
		if(!reverse) {
			System.arraycopy(bytes, 0, this.bytes, index, length);
			index += length;
			if(index > highest_written_index) highest_written_index = index;
			return;
		}
		
		index += length;
		if(index > highest_written_index) highest_written_index = index;
		int ofs = index - 1;
		for(int i = 0; i < length; i++) {
			this.bytes[ofs - i] = bytes[i];
		}
	}
	
	// ======================= //
	
	public String readString(int length) {
		return new String(readBytes(length, 0, false));
	}
	
	public String readString(int length, int offset) {
		return new String(readBytes(length, offset, false));
	}
	
	public String readString(int length, boolean reverse) {
		return new String(readBytes(length, 0, reverse));
	}
	
	public String readString(int length, int offset, boolean reverse) {
		return new String(readBytes(length, offset, reverse));
	}
	
	public String nextReadString(int length) {
		return new String(nextReadBytes(length, false));
	}
	
	public String nextReadString(int length, boolean reverse) {
		return new String(nextReadBytes(length, reverse));
	}
	
	public void writeString(String string) {
		byte[] bytes = string.getBytes();
		writeBytes(bytes, bytes.length, 0, false);
	}
	
	public void writeString(String string, int length) {
		writeBytes(string.getBytes(), length, 0, false);
	}
	
	public void writeString(String string, int length, int offset) {
		writeBytes(string.getBytes(), length, offset, false);
	}
	
	public void writeString(String string, int length, boolean reverse) {
		writeBytes(string.getBytes(), length, 0, reverse);
	}
	
	public void writeString(String string, int length, int offset, boolean reverse) {
		writeBytes(string.getBytes(), length, offset, reverse);
	}
	
	public void nextWriteString(String string) {
		byte[] bytes = string.getBytes();
		nextWriteBytes(bytes, bytes.length, false);
	}
	
	public void nextWriteString(String string, int length) {
		nextWriteBytes(string.getBytes(), length, false);
	}
	
	public void nextWriteString(String string, int length, boolean reverse) {
		nextWriteBytes(string.getBytes(), length, reverse);
	}
	
	// ======================= //
	
	public short[] readShorts(int length) {
		return readShorts(length, 0, defaultEndian, false);
	}
	
	public short[] readShorts(int length, int offset) {
		return readShorts(length, offset, defaultEndian, false);
	}
	
	public short[] readShorts(int length, boolean bigEndian) {
		return readShorts(length, 0, bigEndian, false);
	}
	
	public short[] readShorts(int length, int offset, boolean bigEndian, boolean reverse) {
		short[] result = new short[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readShort(offset + i * 2, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readShort(offset + i * 2, bigEndian);
			}
		}
		
		return result;
	}
	
	public short[] nextReadShorts(int length) {
		return nextReadShorts(length, defaultEndian, false);
	}
	
	public short[] nextReadShorts(int length, boolean bigEndian) {
		return nextReadShorts(length, bigEndian, false);
	}
	
	public short[] nextReadShorts(int length, boolean bigEndian, boolean reverse) {
		short[] result = new short[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readShort(i * 2, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readShort(i * 2, bigEndian);
			}
		}
		
		return result;
	}
	
	public void writeShorts(short[] shorts) {
		writeShorts(shorts, shorts.length, 0, defaultEndian, false);
	}
	
	public void writeShorts(short[] shorts, int length) {
		writeShorts(shorts, length, 0, defaultEndian, false);
	}
	
	public void writeShorts(short[] shorts, int length, int offset) {
		writeShorts(shorts, length, offset, defaultEndian, false);
	}
	
	public void writeShorts(short[] shorts, int length, boolean reverse) {
		writeShorts(shorts, length, 0, defaultEndian, reverse);
	}
	
	public void writeShorts(short[] shorts, int length, int offset, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeShort(shorts[i], i * 2, bigEndian);
			}
		} else {
			int ofs = (offset + length - 1) * 2;
			for(int i = 0; i < length; i++) {
				writeShort(shorts[i], ofs - i * 2, bigEndian);
			}
		}
	}
	
	public void nextWriteShorts(short[] shorts) {
		nextWriteShorts(shorts, shorts.length, defaultEndian, false);
	}
	
	public void nextWriteShorts(short[] shorts, int length) {
		nextWriteShorts(shorts, length, defaultEndian, false);
	}
	
	public void nextWriteShorts(short[] shorts, int length, boolean bigEndian) {
		nextWriteShorts(shorts, length, bigEndian, false);
	}
	
	public void nextWriteShorts(short[] shorts, int length, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeShort(shorts[i], i * 2, bigEndian);
			}
		} else {
			int ofs = (length - 1) * 2;
			for(int i = 0; i < length; i++) {
				writeShort(shorts[i], ofs - i * 2, bigEndian);
			}
		}
		
		index += length * 2;
	}
	
	// ======================= //
	
	public int[] readInts(int length) {
		return readInts(length, 0, defaultEndian, false);
	}
	
	public int[] readInts(int length, int offset) {
		return readInts(length, offset, defaultEndian, false);
	}
	
	public int[] readInts(int length, boolean bigEndian) {
		return readInts(length, 0, bigEndian, false);
	}
	
	public int[] readInts(int length, int offset, boolean bigEndian, boolean reverse) {
		int[] result = new int[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readInt(offset + i * 4, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readInt(offset + i * 4, bigEndian);
			}
		}
		
		return result;
	}
	
	public int[] nextReadInts(int length) {
		return nextReadInts(length, defaultEndian, false);
	}
	
	public int[] nextReadInts(int length, boolean bigEndian) {
		return nextReadInts(length, bigEndian, false);
	}
	
	public int[] nextReadInts(int length, boolean bigEndian, boolean reverse) {
		int[] result = new int[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readInt(i * 4, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readInt(i * 4, bigEndian);
			}
		}
		
		return result;
	}
	
	public void writeInts(int[] ints) {
		writeInts(ints, ints.length, 0, defaultEndian, false);
	}
	
	public void writeInts(int[] ints, int length) {
		writeInts(ints, length, 0, defaultEndian, false);
	}
	
	public void writeInts(int[] ints, int length, int offset) {
		writeInts(ints, length, offset, defaultEndian, false);
	}
	
	public void writeInts(int[] ints, int length, boolean reverse) {
		writeInts(ints, length, 0, defaultEndian, reverse);
	}
	
	public void writeInts(int[] ints, int length, int offset, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeInt(ints[i], i * 4, bigEndian);
			}
		} else {
			int ofs = (offset + length - 1) * 4;
			for(int i = 0; i < length; i++) {
				writeInt(ints[i], ofs - i * 4, bigEndian);
			}
		}
	}
	
	public void nextWriteInts(int[] ints) {
		nextWriteInts(ints, ints.length, defaultEndian, false);
	}
	
	public void nextWriteInts(int[] ints, int length) {
		nextWriteInts(ints, length, defaultEndian, false);
	}
	
	public void nextWriteInts(int[] ints, int length, boolean bigEndian) {
		nextWriteInts(ints, length, bigEndian, false);
	}
	
	public void nextWriteInts(int[] ints, int length, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeInt(ints[i], i * 4, bigEndian);
			}
		} else {
			int ofs = (length - 1) * 4;
			for(int i = 0; i < length; i++) {
				writeInt(ints[i], ofs - i * 4, bigEndian);
			}
		}
		
		index += length * 4;
	}
	
	// ======================= //
	
	public long[] readLongs(int length) {
		return readLongs(length, 0, defaultEndian, false);
	}
	
	public long[] readLongs(int length, int offset) {
		return readLongs(length, offset, defaultEndian, false);
	}
	
	public long[] readLongs(int length, boolean bigEndian) {
		return readLongs(length, 0, bigEndian, false);
	}
	
	public long[] readLongs(int length, int offset, boolean bigEndian, boolean reverse) {
		long[] result = new long[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readLong(offset + i * 8, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readLong(offset + i * 8, bigEndian);
			}
		}
		
		return result;
	}
	
	public long[] nextReadLongs(int length) {
		return nextReadLongs(length, defaultEndian, false);
	}
	
	public long[] nextReadLongs(int length, boolean bigEndian) {
		return nextReadLongs(length, bigEndian, false);
	}
	
	public long[] nextReadLongs(int length, boolean bigEndian, boolean reverse) {
		long[] result = new long[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readLong(i * 8, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readLong(i * 8, bigEndian);
			}
		}
		
		return result;
	}
	
	public void writeLongs(long[] longs) {
		writeLongs(longs, longs.length, 0, defaultEndian, false);
	}
	
	public void writeLongs(long[] longs, int length) {
		writeLongs(longs, length, 0, defaultEndian, false);
	}
	
	public void writeLongs(long[] longs, int length, int offset) {
		writeLongs(longs, length, offset, defaultEndian, false);
	}
	
	public void writeLongs(long[] longs, int length, boolean reverse) {
		writeLongs(longs, length, 0, defaultEndian, reverse);
	}
	
	public void writeLongs(long[] longs, int length, int offset, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeLong(longs[i], i * 8, bigEndian);
			}
		} else {
			int ofs = (offset + length - 1) * 8;
			for(int i = 0; i < length; i++) {
				writeLong(longs[i], ofs - i * 8, bigEndian);
			}
		}
	}
	
	public void nextWriteLongs(long[] longs) {
		nextWriteLongs(longs, longs.length, defaultEndian, false);
	}
	
	public void nextWriteLongs(long[] longs, int length) {
		nextWriteLongs(longs, length, defaultEndian, false);
	}
	
	public void nextWriteLongs(long[] longs, int length, boolean bigEndian) {
		nextWriteLongs(longs, length, bigEndian, false);
	}
	
	public void nextWriteLongs(long[] longs, int length, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeLong(longs[i], i * 8, bigEndian);
			}
		} else {
			int ofs = (length - 1) * 8;
			for(int i = 0; i < length; i++) {
				writeLong(longs[i], ofs - i * 8, bigEndian);
			}
		}
		
		index += length * 8;
	}
	
	// ======================= //
	
	public float[] readFloats(int length) {
		return readFloats(length, 0, defaultEndian, false);
	}
	
	public float[] readFloats(int length, int offset) {
		return readFloats(length, offset, defaultEndian, false);
	}
	
	public float[] readFloats(int length, boolean bigEndian) {
		return readFloats(length, 0, bigEndian, false);
	}
	
	public float[] readFloats(int length, int offset, boolean bigEndian, boolean reverse) {
		float[] result = new float[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readFloat(offset + i * 4, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readFloat(offset + i * 4, bigEndian);
			}
		}
		
		return result;
	}
	
	public float[] nextReadFloats(int length) {
		return nextReadFloats(length, defaultEndian, false);
	}
	
	public float[] nextReadFloats(int length, boolean bigEndian) {
		return nextReadFloats(length, bigEndian, false);
	}
	
	public float[] nextReadFloats(int length, boolean bigEndian, boolean reverse) {
		float[] result = new float[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readFloat(i * 4, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readFloat(i * 4, bigEndian);
			}
		}
		
		return result;
	}
	
	public void writeFloats(float[] floats) {
		writeFloats(floats, floats.length, 0, defaultEndian, false);
	}
	
	public void writeFloats(float[] floats, int length) {
		writeFloats(floats, length, 0, defaultEndian, false);
	}
	
	public void writeFloats(float[] floats, int length, int offset) {
		writeFloats(floats, length, offset, defaultEndian, false);
	}
	
	public void writeFloats(float[] floats, int length, boolean reverse) {
		writeFloats(floats, length, 0, defaultEndian, reverse);
	}
	
	public void writeFloats(float[] floats, int length, int offset, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeFloat(floats[i], i * 4, bigEndian);
			}
		} else {
			int ofs = (offset + length - 1) * 4;
			for(int i = 0; i < length; i++) {
				writeFloat(floats[i], ofs - i * 4, bigEndian);
			}
		}
	}
	
	public void nextWriteFloats(float[] floats) {
		nextWriteFloats(floats, floats.length, defaultEndian, false);
	}
	
	public void nextWriteFloats(float[] floats, int length) {
		nextWriteFloats(floats, length, defaultEndian, false);
	}
	
	public void nextWriteFloats(float[] floats, int length, boolean bigEndian) {
		nextWriteFloats(floats, length, bigEndian, false);
	}
	
	public void nextWriteFloats(float[] floats, int length, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeFloat(floats[i], i * 4, bigEndian);
			}
		} else {
			int ofs = (length - 1) * 4;
			for(int i = 0; i < length; i++) {
				writeFloat(floats[i], ofs - i * 4, bigEndian);
			}
		}
		
		index += length * 4;
	}
	
	// ======================= //
	
	public double[] readDoubles(int length) {
		return readDoubles(length, 0, defaultEndian, false);
	}
	
	public double[] readDoubles(int length, int offset) {
		return readDoubles(length, offset, defaultEndian, false);
	}
	
	public double[] readDoubles(int length, boolean bigEndian) {
		return readDoubles(length, 0, bigEndian, false);
	}
	
	public double[] readDoubles(int length, int offset, boolean bigEndian, boolean reverse) {
		double[] result = new double[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readDouble(offset + i * 8, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readDouble(offset + i * 8, bigEndian);
			}
		}
		
		return result;
	}
	
	public double[] nextReadDoubles(int length) {
		return nextReadDoubles(length, defaultEndian, false);
	}
	
	public double[] nextReadDoubles(int length, boolean bigEndian) {
		return nextReadDoubles(length, bigEndian, false);
	}
	
	public double[] nextReadDoubles(int length, boolean bigEndian, boolean reverse) {
		double[] result = new double[length];
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				result[i] = readDouble(i * 8, bigEndian);
			}
		} else {
			int ofs = length - 1;
			for(int i = 0; i < length; i++) {
				result[ofs - i] = readDouble(i * 8, bigEndian);
			}
		}
		
		return result;
	}
	
	public void writeDoubles(double[] doubles) {
		writeDoubles(doubles, doubles.length, 0, defaultEndian, false);
	}
	
	public void writeDoubles(double[] doubles, int length) {
		writeDoubles(doubles, length, 0, defaultEndian, false);
	}
	
	public void writeDoubles(double[] doubles, int length, int offset) {
		writeDoubles(doubles, length, offset, defaultEndian, false);
	}
	
	public void writeDoubles(double[] doubles, int length, boolean reverse) {
		writeDoubles(doubles, length, 0, defaultEndian, reverse);
	}
	
	public void writeDoubles(double[] doubles, int length, int offset, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeDouble(doubles[i], i * 8, bigEndian);
			}
		} else {
			int ofs = (offset + length - 1) * 8;
			for(int i = 0; i < length; i++) {
				writeDouble(doubles[i], ofs - i * 8, bigEndian);
			}
		}
	}
	
	public void nextWriteDoubles(double[] doubles) {
		nextWriteDoubles(doubles, doubles.length, defaultEndian, false);
	}
	
	public void nextWriteDoubles(double[] doubles, int length) {
		nextWriteDoubles(doubles, length, defaultEndian, false);
	}
	
	public void nextWriteDoubles(double[] doubles, int length, boolean bigEndian) {
		nextWriteDoubles(doubles, length, bigEndian, false);
	}
	
	public void nextWriteDoubles(double[] doubles, int length, boolean bigEndian, boolean reverse) {
		if(!reverse) {
			for(int i = 0; i < length; i++) {
				writeDouble(doubles[i], i * 8, bigEndian);
			}
		} else {
			int ofs = (length - 1) * 8;
			for(int i = 0; i < length; i++) {
				writeDouble(doubles[i], ofs - i * 8, bigEndian);
			}
		}
		
		index += length * 8;
	}
	
	// ======================= //
	
	public UUID readUuid() {
		return readUuid(0, defaultEndian);
	}
	
	public UUID readUuid(int offset) {
		return readUuid(offset, defaultEndian);
	}
	
	public UUID readUuid(boolean bigEndian) {
		return readUuid(0, bigEndian);
	}
	
	public UUID readUuid(int offset, boolean bigEndian) {
		long a = readLong(offset, bigEndian);
		long b = readLong(offset + 8, bigEndian);
		if(bigEndian) return new UUID(a, b);
		return new UUID(b, a);
	}
	
	public UUID nextReadUuid() {
		return nextReadUuid(defaultEndian);
	}
	
	public UUID nextReadUuid(boolean bigEndian) {
		long a = readLong(0, bigEndian);
		long b = readLong(8, bigEndian);
		index += 16;
		if(bigEndian) return new UUID(a, b);
		return new UUID(b, a);
	}
	
	public void writeUuid(UUID uuid) {
		writeUuid(uuid, 0, defaultEndian);
	}
	
	public void writeUuid(UUID uuid, int offset) {
		writeUuid(uuid, offset, defaultEndian);
	}
	
	public void writeUuid(UUID uuid, boolean bigEndian) {
		writeUuid(uuid, 0, bigEndian);
	}
	
	public void writeUuid(UUID uuid, int offset, boolean bigEndian) {
		if(bigEndian) {
			writeLong(uuid.getMostSignificantBits(), offset, bigEndian);
			writeLong(uuid.getLeastSignificantBits(), offset + 8, bigEndian);
		} else {
			writeLong(uuid.getLeastSignificantBits(), offset, bigEndian);
			writeLong(uuid.getMostSignificantBits(), offset + 8, bigEndian);
		}
	}
	
	public void nextWriteUuid(UUID uuid) {
		writeUuid(uuid, defaultEndian);
	}
	
	public void nextWriteUuid(UUID uuid, boolean bigEndian) {
		if(bigEndian) {
			writeLong(uuid.getMostSignificantBits(), 0, bigEndian);
			writeLong(uuid.getLeastSignificantBits(), 8, bigEndian);
		} else {
			writeLong(uuid.getLeastSignificantBits(), 0, bigEndian);
			writeLong(uuid.getMostSignificantBits(), 8, bigEndian);
		}
		
		index += 16;
	}
}
