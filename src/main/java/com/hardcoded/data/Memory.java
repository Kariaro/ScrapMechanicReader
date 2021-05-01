package com.hardcoded.data;

import java.util.UUID;

/**
 * A class designed to make it easier to read serialized data.
 * 
 * @author HardCoded
 */
public class Memory {
	private final byte[] bytes;
	private boolean defaultEndian;
	private int highest_written_index;
	private int index;
	
	public Memory(int capacity) {
		this(null, capacity);
	}
	
	public Memory(byte[] bytes) {
		this(bytes, bytes.length);
	}
	
	public Memory(byte[] bytes, int capacity) {
		byte[] array = new byte[capacity];
		
		if(bytes != null) 
			System.arraycopy(bytes, 0, array, 0, Math.min(bytes.length, capacity));
	
		this.bytes = array;
	}
	
	// ======================= //
	
	public Memory setDefaultBigEndian(boolean b) {
		defaultEndian = b;
		return this;
	}
	
	public Memory set(int i) {
		index = i;
		return this;
	}
	
	public Memory move(int i) {
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
	
	private Memory writeValue(Number number, int offset, int length, boolean bigEndian) {
		long value = 0;
		
		if(number instanceof Byte) value = Byte.toUnsignedLong(number.byteValue());
		if(number instanceof Short) value = Short.toUnsignedLong(number.shortValue());
		if(number instanceof Integer) value = Integer.toUnsignedLong(number.intValue());
		if(number instanceof Long) value = number.longValue();
		if(number instanceof Float) value = Integer.toUnsignedLong(Float.floatToIntBits(number.floatValue()));
		if(number instanceof Double) value = Double.doubleToRawLongBits(number.doubleValue());
		
		for(int i = 0; i < length; i++) {
			long shr = (bigEndian ? (length - 1 - i):i) * 8L;
			byte val = (byte)((value >>> shr) & 0xff);
			int idx = index + offset + i;
			bytes[idx] = val;
		}
		
		int idx = index + length + offset;
		if(idx > highest_written_index) highest_written_index = idx;
		
		return this;
	}
	
	// ======================= //
	
	public byte Byte() { return Byte(0); }
	public byte Byte(int offset) { return bytes[index + offset]; }
	public int UnsignedByte() { return UnsignedByte(0); }
	public int UnsignedByte(int offset) { return Byte.toUnsignedInt(bytes[index + offset]); }
	public Memory WriteByte(int value) { return WriteByte(value, 0); }
	public Memory WriteByte(int value, int offset) {
		int idx = index + offset;
		bytes[idx] = (byte)value;
		if(idx > highest_written_index) highest_written_index = idx;
		
		return this;
	}
	
	public byte NextByte() {
		byte result = Byte();
		index += 1;
		return result;
	}
	
	public int NextUnsignedByte() {
		int result = UnsignedByte();
		index += 1;
		return result;
	}
	
	public Memory NextWriteByte(int value) {
		WriteByte(value);
		index += 1;
		return this;
	}
	
	// ======================= //
	
	public short Short() { return Short(0, defaultEndian); }
	public short Short(int offset) { return Short(offset, defaultEndian); }
	public short Short(boolean bigEndian) { return Short(0, bigEndian); }
	public short Short(int offset, boolean bigEndian) { return (short)readValue(offset, 2, bigEndian); }
	public int UnsignedShort() { return UnsignedShort(0, defaultEndian); }
	public int UnsignedShort(int offset) { return UnsignedShort(offset, defaultEndian); }
	public int UnsignedShort(boolean bigEndian) { return UnsignedShort(0, bigEndian); }
	public int UnsignedShort(int offset, boolean bigEndian) { return Short.toUnsignedInt(Short(offset, bigEndian)); }
	public Memory WriteShort(int value) { return WriteShort(value, 0, defaultEndian); }
	public Memory WriteShort(int value, int offset) { return WriteShort(value, offset, defaultEndian); }
	public Memory WriteShort(int value, boolean bigEndian) { return WriteShort(value, 0, bigEndian); }
	public Memory WriteShort(int value, int offset, boolean bigEndian) { return writeValue((short)value, offset, 2, bigEndian); }
	
	public short NextShort() { return NextShort(defaultEndian); }
	public short NextShort(boolean bigEndian) {
		short result = Short(0, bigEndian);
		index += 2;
		return result;
	}
	
	public int NextUnsignedShort() { return NextUnsignedShort(defaultEndian); }
	public int NextUnsignedShort(boolean bigEndian) {
		int result = UnsignedShort(0, bigEndian);
		index += 2;
		return result;
	}
	
	public Memory NextWriteShort(int value) { return NextWriteShort(value, defaultEndian); }
	public Memory NextWriteShort(int value, boolean bigEndian) {
		WriteShort(value, 0, bigEndian);
		index += 2;
		return this;
	}
	
	// ======================= //
	
	public int Int() { return Int(0, defaultEndian); }
	public int Int(int offset) { return Int(offset, defaultEndian); }
	public int Int(boolean bigEndian) { return Int(0, bigEndian); }
	public int Int(int offset, boolean bigEndian) { return (int)readValue(offset, 4, bigEndian); }
	public long UnsignedInt() { return UnsignedInt(0, defaultEndian); }
	public long UnsignedInt(int offset) { return UnsignedInt(offset, defaultEndian); }
	public long UnsignedInt(boolean bigEndian) { return UnsignedInt(0, bigEndian); }
	public long UnsignedInt(int offset, boolean bigEndian) { return Integer.toUnsignedLong(Int(offset, bigEndian)); }
	public Memory WriteInt(int value) { return WriteInt(value, 0, defaultEndian); }
	public Memory WriteInt(int value, int offset) { return WriteInt(value, offset, defaultEndian); }
	public Memory WriteInt(int value, boolean bigEndian) { return WriteInt(value, 0, bigEndian); }
	public Memory WriteInt(int value, int offset, boolean bigEndian) { return writeValue(value, offset, 4, bigEndian); }
	
	public int NextInt() { return NextInt(defaultEndian); }
	public int NextInt(boolean bigEndian) {
		int result = Int(0, bigEndian);
		index += 4;
		return result;
	}
	
	public long NextUnsignedInt() { return NextUnsignedInt(defaultEndian); }
	public long NextUnsignedInt(boolean bigEndian) {
		long result = UnsignedInt(0, bigEndian);
		index += 4;
		return result;
	}
	
	public Memory NextWriteInt(int value) { return NextWriteInt(value, defaultEndian); }
	public Memory NextWriteInt(int value, boolean bigEndian) {
		WriteInt(value, 0, bigEndian);
		index += 4;
		return this;
	}
	
	// ======================= //
	
	public long Long() { return Long(0, defaultEndian); }
	public long Long(int offset) { return Long(offset, defaultEndian); }
	public long Long(boolean bigEndian) { return Long(0, bigEndian); }
	public long Long(int offset, boolean bigEndian) { return readValue(offset, 8, bigEndian); }
	public Memory WriteLong(long value) { return WriteLong(value, 0, defaultEndian); }
	public Memory WriteLong(long value, int offset) { return WriteLong(value, offset, defaultEndian); }
	public Memory WriteLong(long value, boolean bigEndian) { return WriteLong(value, 0, bigEndian); }
	public Memory WriteLong(long value, int offset, boolean bigEndian) { return writeValue(value, offset, 8, bigEndian); }
	
	public long NextLong() { return NextLong(defaultEndian); }
	public long NextLong(boolean bigEndian) {
		long result = Long(0, bigEndian);
		index += 8;
		return result;
	}
	
	public Memory NextWriteLong(long value) { return NextWriteLong(value, defaultEndian); }
	public Memory NextWriteLong(long value, boolean bigEndian) {
		WriteLong(value, 0, bigEndian);
		index += 8;
		return this;
	}
	
	// ======================= //
	
	public float Float() { return Float(0, defaultEndian); }
	public float Float(int offset) { return Float(offset, defaultEndian); }
	public float Float(boolean bigEndian) { return Float(0, bigEndian); }
	public float Float(int offset, boolean bigEndian) { return Float.intBitsToFloat(Int(offset, bigEndian)); }
	public Memory WriteFloat(float value) { return WriteFloat(value, 0, defaultEndian); }
	public Memory WriteFloat(float value, int offset) { return WriteFloat(value, offset, defaultEndian); }
	public Memory WriteFloat(float value, boolean bigEndian) { return WriteFloat(value, 0, bigEndian); }
	public Memory WriteFloat(float value, int offset, boolean bigEndian) { return WriteInt(Float.floatToIntBits(value), offset, bigEndian); }
	public float NextFloat() { return NextFloat(defaultEndian); }
	public float NextFloat(boolean bigEndian) { return Float.intBitsToFloat(NextInt(bigEndian)); }
	public Memory NextWriteFloat(float value) { return NextWriteFloat(value, defaultEndian); }
	public Memory NextWriteFloat(float value, boolean bigEndian) { return NextWriteInt(Float.floatToIntBits(value), bigEndian); }
	
	// ======================= //
	
	public double Double() { return Double(0, defaultEndian); }
	public double Double(int offset) { return Double(offset, defaultEndian); }
	public double Double(boolean bigEndian) { return Double(0, bigEndian); }
	public double Double(int offset, boolean bigEndian) { return Double.longBitsToDouble(Long(offset, bigEndian)); }
	public Memory WriteDouble(double value) { return WriteDouble(value, 0, defaultEndian); }
	public Memory WriteDouble(double value, int offset) { return WriteDouble(value, offset, defaultEndian); }
	public Memory WriteDouble(double value, boolean bigEndian) { return WriteDouble(value, 0, bigEndian); }
	public Memory WriteDouble(double value, int offset, boolean bigEndian) { return WriteLong(Double.doubleToLongBits(value), offset, bigEndian); }
	public double NextDouble() { return NextDouble(defaultEndian); }
	public double NextDouble(boolean bigEndian) { return Double.longBitsToDouble(NextLong(bigEndian)); }
	public Memory NextWriteDouble(double value) { return NextWriteDouble(value, defaultEndian); }
	public Memory NextWriteDouble(double value, boolean bigEndian) { return NextWriteLong(Double.doubleToLongBits(value), bigEndian); }
	
	// ======================= //
	
	public String String(int length) { return String(length, 0, false); }
	public String String(int length, int offset) { return String(length, offset, false); }
	public String String(int length, boolean reverse) { return String(length, 0, reverse); }
	public String String(int length, int offset, boolean reverse) { return new String(Bytes(length, offset, reverse)); }
	public Memory WriteString(String value) { return WriteString(value, value.length(), 0, false); }
	public Memory WriteString(String value, int length) { return WriteString(value, length, 0, false); }
	public Memory WriteString(String value, int length, int offset) { return WriteString(value, length, offset, false); }
	public Memory WriteString(String value, int length, boolean reverse) { return WriteString(value, length, 0, reverse); }
	public Memory WriteString(String value, int length, int offset, boolean reverse) { return WriteBytes(value.getBytes(), length, offset, reverse); }
	public String NextString(int length) { return NextString(length, false); }
	public String NextString(int length, boolean reverse) { return new String(NextBytes(length, reverse)); }
	public Memory NextWriteString(String value) { return NextWriteString(value, value.length(), false); }
	public Memory NextWriteString(String value, int length) { return NextWriteString(value, length, false); }
	public Memory NextWriteString(String value, int length, boolean reverse) { return NextWriteBytes(value.getBytes(), length, reverse); }
	
	// ======================= //
	
	public byte[] Bytes(int length) { return Bytes(length, 0, false); }
	public byte[] Bytes(int length, int offset) { return Bytes(length, offset, false); }
	public byte[] Bytes(int length, boolean reverse) { return Bytes(length, 0, reverse); }
	public byte[] Bytes(int length, int offset, boolean reverse) {
		byte[] result = new byte[length];
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			result[idx] = bytes[index + offset + i];
		}
		return result;
	}
	
	public Memory WriteBytes(byte[] value) { return WriteBytes(value, value.length, 0, false); }
	public Memory WriteBytes(byte[] value, int length) { return WriteBytes(value, length, 0, false); }
	public Memory WriteBytes(byte[] value, int length, int offset) { return WriteBytes(value, length, offset, false); }
	public Memory WriteBytes(byte[] value, int length, boolean reverse) { return WriteBytes(value, length, 0, reverse); }
	public Memory WriteBytes(byte[] value, int length, int offset, boolean reverse) {
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			
			int byte_idx = index + offset + i;
			bytes[byte_idx] = value[idx];
			
			if(byte_idx > highest_written_index) highest_written_index = byte_idx;
		}
		return this;
	}
	
	public byte[] NextBytes(int length) { return NextBytes(length, false); }
	public byte[] NextBytes(int length, boolean reverse) {
		byte[] result = Bytes(length, 0, reverse);
		index += length;
		return result;
	}
	
	public Memory NextWriteBytes(byte[] value) { return NextWriteBytes(value, value.length, false); }
	public Memory NextWriteBytes(byte[] value, int length) { return NextWriteBytes(value, length, false); }
	public Memory NextWriteBytes(byte[] value, int length, boolean reverse) {
		WriteBytes(value, length, 0, reverse);
		index += length;
		return this;
	}
	
	// ======================= //
	
	public short[] Shorts(int length) { return Shorts(length, 0, defaultEndian, false); }
	public short[] Shorts(int length, int offset) { return Shorts(length, offset, defaultEndian, false); }
	public short[] Shorts(int length, boolean bigEndian) { return Shorts(length, 0, bigEndian, false); }
	public short[] Shorts(int length, int offset, boolean bigEndian) { return Shorts(length, offset, bigEndian, false); }
	public short[] Shorts(int length, int offset, boolean bigEndian, boolean reverse) {
		short[] result = new short[length];
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			result[idx] = Short(offset + i * 2, bigEndian);
		}
		return result;
	}
	
	public Memory WriteShorts(short[] value) { return WriteShorts(value, value.length, 0, defaultEndian, false); }
	public Memory WriteShorts(short[] value, int length) { return WriteShorts(value, length, 0, defaultEndian, false); }
	public Memory WriteShorts(short[] value, int length, int offset) { return WriteShorts(value, length, offset, defaultEndian, false); }
	public Memory WriteShorts(short[] value, int length, boolean bigEndian) { return WriteShorts(value, length, 0, bigEndian, false); }
	public Memory WriteShorts(short[] value, int length, int offset, boolean bigEndian) { return WriteShorts(value, length, offset, bigEndian, false); }
	public Memory WriteShorts(short[] value, int length, int offset, boolean bigEndian, boolean reverse) {
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			WriteShort(value[idx], offset + i * 2, bigEndian);
		}
		return this;
	}
	
	public short[] NextShorts(int length) { return NextShorts(length, defaultEndian, false); }
	public short[] NextShorts(int length, boolean bigEndian) { return NextShorts(length, bigEndian, false); }
	public short[] NextShorts(int length, boolean bigEndian, boolean reverse) {
		short[] result = Shorts(length, 0, reverse);
		index += length * 2;
		return result;
	}
	
	public Memory NextWriteShorts(short[] value) { return NextWriteShorts(value, value.length, defaultEndian, false); }
	public Memory NextWriteShorts(short[] value, int length) { return NextWriteShorts(value, length, defaultEndian, false); }
	public Memory NextWriteShorts(short[] value, int length, boolean bigEndian) { return NextWriteShorts(value, length, bigEndian, false); }
	public Memory NextWriteShorts(short[] value, int length, boolean bigEndian, boolean reverse) {
		WriteShorts(value, length, 0, bigEndian, reverse);
		index += length * 2;
		return this;
	}
	
	// ======================= //
	
	public int[] Ints(int length) { return Ints(length, 0, defaultEndian, false); }
	public int[] Ints(int length, int offset) { return Ints(length, offset, defaultEndian, false); }
	public int[] Ints(int length, boolean bigEndian) { return Ints(length, 0, bigEndian, false); }
	public int[] Ints(int length, int offset, boolean bigEndian) { return Ints(length, offset, bigEndian, false); }
	public int[] Ints(int length, int offset, boolean bigEndian, boolean reverse) {
		int[] result = new int[length];
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			result[idx] = Int(offset + i * 4, bigEndian);
		}
		return result;
	}
	
	public Memory WriteInts(int[] value) { return WriteInts(value, value.length, 0, defaultEndian, false); }
	public Memory WriteInts(int[] value, int length) { return WriteInts(value, length, 0, defaultEndian, false); }
	public Memory WriteInts(int[] value, int length, int offset) { return WriteInts(value, length, offset, defaultEndian, false); }
	public Memory WriteInts(int[] value, int length, boolean bigEndian) { return WriteInts(value, length, 0, bigEndian, false); }
	public Memory WriteInts(int[] value, int length, int offset, boolean bigEndian) { return WriteInts(value, length, offset, bigEndian, false); }
	public Memory WriteInts(int[] value, int length, int offset, boolean bigEndian, boolean reverse) {
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			WriteInt(value[idx], offset + i * 4, bigEndian);
		}
		return this;
	}
	
	public int[] NextInts(int length) { return NextInts(length, defaultEndian, false); }
	public int[] NextInts(int length, boolean bigEndian) { return NextInts(length, bigEndian, false); }
	public int[] NextInts(int length, boolean bigEndian, boolean reverse) {
		int[] result = Ints(length, 0, reverse);
		index += length * 4;
		return result;
	}
	
	public Memory NextWriteInts(int[] value) { return NextWriteInts(value, value.length, defaultEndian, false); }
	public Memory NextWriteInts(int[] value, int length) { return NextWriteInts(value, length, defaultEndian, false); }
	public Memory NextWriteInts(int[] value, int length, boolean bigEndian) { return NextWriteInts(value, length, bigEndian, false); }
	public Memory NextWriteInts(int[] value, int length, boolean bigEndian, boolean reverse) {
		WriteInts(value, length, 0, bigEndian, reverse);
		index += length * 4;
		return this;
	}
	
	// ======================= //
	
	public long[] Longs(int length) { return Longs(length, 0, defaultEndian, false); }
	public long[] Longs(int length, int offset) { return Longs(length, offset, defaultEndian, false); }
	public long[] Longs(int length, boolean bigEndian) { return Longs(length, 0, bigEndian, false); }
	public long[] Longs(int length, int offset, boolean bigEndian) { return Longs(length, offset, bigEndian, false); }
	public long[] Longs(int length, int offset, boolean bigEndian, boolean reverse) {
		long[] result = new long[length];
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			result[idx] = Long(offset + i * 8, bigEndian);
		}
		return result;
	}
	
	public Memory WriteLongs(long[] value) { return WriteLongs(value, value.length, 0, defaultEndian, false); }
	public Memory WriteLongs(long[] value, int length) { return WriteLongs(value, length, 0, defaultEndian, false); }
	public Memory WriteLongs(long[] value, int length, int offset) { return WriteLongs(value, length, offset, defaultEndian, false); }
	public Memory WriteLongs(long[] value, int length, boolean bigEndian) { return WriteLongs(value, length, 0, bigEndian, false); }
	public Memory WriteLongs(long[] value, int length, int offset, boolean bigEndian) { return WriteLongs(value, length, offset, bigEndian, false); }
	public Memory WriteLongs(long[] value, int length, int offset, boolean bigEndian, boolean reverse) {
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			WriteLong(value[idx], offset + i * 8, bigEndian);
		}
		return this;
	}
	
	public long[] NextLongs(int length) { return NextLongs(length, defaultEndian, false); }
	public long[] NextLongs(int length, boolean bigEndian) { return NextLongs(length, bigEndian, false); }
	public long[] NextLongs(int length, boolean bigEndian, boolean reverse) {
		long[] result = Longs(length, 0, reverse);
		index += length * 8;
		return result;
	}
	
	public Memory NextWriteLongs(long[] value) { return NextWriteLongs(value, value.length, defaultEndian, false); }
	public Memory NextWriteLongs(long[] value, int length) { return NextWriteLongs(value, length, defaultEndian, false); }
	public Memory NextWriteLongs(long[] value, int length, boolean bigEndian) { return NextWriteLongs(value, length, bigEndian, false); }
	public Memory NextWriteLongs(long[] value, int length, boolean bigEndian, boolean reverse) {
		WriteLongs(value, length, 0, bigEndian, reverse);
		index += length * 8;
		return this;
	}
	
	// ======================= //
	
	public float[] Floats(int length) { return Floats(length, 0, defaultEndian, false); }
	public float[] Floats(int length, int offset) { return Floats(length, offset, defaultEndian, false); }
	public float[] Floats(int length, boolean bigEndian) { return Floats(length, 0, bigEndian, false); }
	public float[] Floats(int length, int offset, boolean bigEndian) { return Floats(length, offset, bigEndian, false); }
	public float[] Floats(int length, int offset, boolean bigEndian, boolean reverse) {
		float[] result = new float[length];
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			result[idx] = Float(offset + i * 4, bigEndian);
		}
		return result;
	}
	
	public Memory WriteFloats(float[] value) { return WriteFloats(value, value.length, 0, defaultEndian, false); }
	public Memory WriteFloats(float[] value, int length) { return WriteFloats(value, length, 0, defaultEndian, false); }
	public Memory WriteFloats(float[] value, int length, int offset) { return WriteFloats(value, length, offset, defaultEndian, false); }
	public Memory WriteFloats(float[] value, int length, boolean bigEndian) { return WriteFloats(value, length, 0, bigEndian, false); }
	public Memory WriteFloats(float[] value, int length, int offset, boolean bigEndian) { return WriteFloats(value, length, offset, bigEndian, false); }
	public Memory WriteFloats(float[] value, int length, int offset, boolean bigEndian, boolean reverse) {
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			WriteFloat(value[idx], offset + i * 4, bigEndian);
		}
		return this;
	}
	
	public float[] NextFloats(int length) { return NextFloats(length, defaultEndian, false); }
	public float[] NextFloats(int length, boolean bigEndian) { return NextFloats(length, bigEndian, false); }
	public float[] NextFloats(int length, boolean bigEndian, boolean reverse) {
		float[] result = Floats(length, 0, reverse);
		index += length * 4;
		return result;
	}
	
	public Memory NextWriteFloats(float[] value) { return NextWriteFloats(value, value.length, defaultEndian, false); }
	public Memory NextWriteFloats(float[] value, int length) { return NextWriteFloats(value, length, defaultEndian, false); }
	public Memory NextWriteFloats(float[] value, int length, boolean bigEndian) { return NextWriteFloats(value, length, bigEndian, false); }
	public Memory NextWriteFloats(float[] value, int length, boolean bigEndian, boolean reverse) {
		WriteFloats(value, length, 0, bigEndian, reverse);
		index += length * 4;
		return this;
	}
	
	// ======================= //
	
	public double[] Doubles(int length) { return Doubles(length, 0, defaultEndian, false); }
	public double[] Doubles(int length, int offset) { return Doubles(length, offset, defaultEndian, false); }
	public double[] Doubles(int length, boolean bigEndian) { return Doubles(length, 0, bigEndian, false); }
	public double[] Doubles(int length, int offset, boolean bigEndian) { return Doubles(length, offset, bigEndian, false); }
	public double[] Doubles(int length, int offset, boolean bigEndian, boolean reverse) {
		double[] result = new double[length];
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			result[idx] = Double(offset + i * 8, bigEndian);
		}
		return result;
	}
	
	public Memory WriteDoubles(double[] value) { return WriteDoubles(value, value.length, 0, defaultEndian, false); }
	public Memory WriteDoubles(double[] value, int length) { return WriteDoubles(value, length, 0, defaultEndian, false); }
	public Memory WriteDoubles(double[] value, int length, int offset) { return WriteDoubles(value, length, offset, defaultEndian, false); }
	public Memory WriteDoubles(double[] value, int length, boolean bigEndian) { return WriteDoubles(value, length, 0, bigEndian, false); }
	public Memory WriteDoubles(double[] value, int length, int offset, boolean bigEndian) { return WriteDoubles(value, length, offset, bigEndian, false); }
	public Memory WriteDoubles(double[] value, int length, int offset, boolean bigEndian, boolean reverse) {
		for(int i = 0; i < length; i++) {
			int idx = reverse ? (length - 1 - i):i;
			WriteDouble(value[idx], offset + i * 8, bigEndian);
		}
		return this;
	}
	
	public double[] NextDoubles(int length) { return NextDoubles(length, defaultEndian, false); }
	public double[] NextDoubles(int length, boolean bigEndian) { return NextDoubles(length, bigEndian, false); }
	public double[] NextDoubles(int length, boolean bigEndian, boolean reverse) {
		double[] result = Doubles(length, 0, reverse);
		index += length * 8;
		return result;
	}
	
	public Memory NextWriteDoubles(double[] value) { return NextWriteDoubles(value, value.length, defaultEndian, false); }
	public Memory NextWriteDoubles(double[] value, int length) { return NextWriteDoubles(value, length, defaultEndian, false); }
	public Memory NextWriteDoubles(double[] value, int length, boolean bigEndian) { return NextWriteDoubles(value, length, bigEndian, false); }
	public Memory NextWriteDoubles(double[] value, int length, boolean bigEndian, boolean reverse) {
		WriteDoubles(value, length, 0, bigEndian, reverse);
		index += length * 8;
		return this;
	}
	
	// ======================= //
	
	public UUID Uuid() { return Uuid(0, defaultEndian); }
	public UUID Uuid(int offset) { return Uuid(offset, defaultEndian); }
	public UUID Uuid(boolean bigEndian) { return Uuid(0, bigEndian); }
	public UUID Uuid(int offset, boolean bigEndian) {
		long a = Long(offset, bigEndian);
		long b = Long(offset + 8, bigEndian);
		if(bigEndian) return new UUID(a, b);
		return new UUID(b, a);
	}
	
	public UUID NextUuid() { return NextUuid(defaultEndian); }
	public UUID NextUuid(boolean bigEndian) {
		UUID result = Uuid(bigEndian);
		index += 16;
		return result;
	}
	

	public Memory WriteUUID(UUID uuid) { return WriteUUID(uuid, 0, defaultEndian); }
	public Memory WriteUUID(UUID uuid, int offset) { return WriteUUID(uuid, offset, defaultEndian); }
	public Memory WriteUUID(UUID uuid, boolean bigEndian) { return WriteUUID(uuid, 0, bigEndian); }
	public Memory WriteUUID(UUID uuid, int offset, boolean bigEndian) {
		if(bigEndian) {
			WriteLong(uuid.getMostSignificantBits(), offset, bigEndian);
			WriteLong(uuid.getLeastSignificantBits(), offset + 8, bigEndian);
		} else {
			WriteLong(uuid.getLeastSignificantBits(), offset, bigEndian);
			WriteLong(uuid.getMostSignificantBits(), offset + 8, bigEndian);
		}
		return this;
	}
	public Memory NextWriteUUID(UUID uuid) { return NextWriteUUID(uuid, defaultEndian); }
	public Memory NextWriteUUID(UUID uuid, boolean bigEndian) {
		WriteUUID(uuid, 0, bigEndian);
		index += 16;
		return this;
	}
}
