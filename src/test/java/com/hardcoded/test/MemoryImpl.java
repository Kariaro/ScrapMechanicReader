package com.hardcoded.test;

import java.util.UUID;

import com.hardcoded.data.Memory;

interface MemoryImpl {
	public Memory setDefaultBigEndian(boolean b);
	public Memory set(int i);
	public Memory move(int i);
	public byte[] data();
	public int index();
	
	// ======================= //
	
	// private long readValue(int offset, int length, boolean bigEndian);
	// private Memory writeValue(Number number, int offset, int length, boolean bigEndian);
	
	// ======================= //
	
	public byte Byte();
	public byte Byte(int offset);
	public int UnsignedByte();
	public int UnsignedByte(int offset);
	public Memory WriteByte(int value);
	public Memory WriteByte(int value, int offset);
	public byte NextByte();
	public int NextUnsignedByte();
	public Memory NextWriteByte(int value);
	
	// ======================= //
	
	public short Short();
	public short Short(int offset);
	public short Short(boolean bigEndian);
	public short Short(int offset, boolean bigEndian);
	public int UnsignedShort();
	public int UnsignedShort(int offset);
	public int UnsignedShort(boolean bigEndian);
	public int UnsignedShort(int offset, boolean bigEndian);
	public Memory WriteShort(int value);
	public Memory WriteShort(int value, int offset);
	public Memory WriteShort(int value, boolean bigEndian);
	public Memory WriteShort(int value, int offset, boolean bigEndian);
	public short NextShort();
	public short NextShort(boolean bigEndian);
	public int NextUnsignedShort();
	public int NextUnsignedShort(boolean bigEndian);
	public Memory NextWriteShort(int value);
	public Memory NextWriteShort(int value, boolean bigEndian);
	
	// ======================= //
	
	public int Int();
	public int Int(int offset);
	public int Int(boolean bigEndian);
	public int Int(int offset, boolean bigEndian);
	public long UnsignedInt();
	public long UnsignedInt(int offset);
	public long UnsignedInt(boolean bigEndian);
	public long UnsignedInt(int offset, boolean bigEndian);
	public Memory WriteInt(int value);
	public Memory WriteInt(int value, int offset);
	public Memory WriteInt(int value, boolean bigEndian);
	public Memory WriteInt(int value, int offset, boolean bigEndian);
	public int NextInt();
	public int NextInt(boolean bigEndian);
	public long NextUnsignedInt();
	public long NextUnsignedInt(boolean bigEndian);
	public Memory NextWriteInt(int value);
	public Memory NextWriteInt(int value, boolean bigEndian);
	
	// ======================= //
	
	public long Long();
	public long Long(int offset);
	public long Long(boolean bigEndian);
	public long Long(int offset, boolean bigEndian);
	public Memory WriteLong(long value);
	public Memory WriteLong(long value, int offset);
	public Memory WriteLong(long value, boolean bigEndian);
	public Memory WriteLong(long value, int offset, boolean bigEndian);
	public long NextLong();
	public long NextLong(boolean bigEndian);
	public Memory NextWriteLong(long value);
	public Memory NextWriteLong(long value, boolean bigEndian);
	
	// ======================= //
	
	public float Float();
	public float Float(int offset);
	public float Float(boolean bigEndian);
	public float Float(int offset, boolean bigEndian);
	public Memory WriteFloat(float value);
	public Memory WriteFloat(float value, int offset);
	public Memory WriteFloat(float value, boolean bigEndian);
	public Memory WriteFloat(float value, int offset, boolean bigEndian);
	public float NextFloat();
	public float NextFloat(boolean bigEndian);
	public Memory NextWriteFloat(float value);
	public Memory NextWriteFloat(float value, boolean bigEndian);
	
	// ======================= //
	
	public double Double();
	public double Double(int offset);
	public double Double(boolean bigEndian);
	public double Double(int offset, boolean bigEndian);
	public Memory WriteDouble(double value);
	public Memory WriteDouble(double value, int offset);
	public Memory WriteDouble(double value, boolean bigEndian);
	public Memory WriteDouble(double value, int offset, boolean bigEndian);
	public double NextDouble();
	public double NextDouble(boolean bigEndian);
	public Memory NextWriteDouble(double value);
	public Memory NextWriteDouble(double value, boolean bigEndian);
	
	// ======================= //
	
	public String String(int length);
	public String String(int length, int offset);
	public String String(int length, boolean reverse);
	public String String(int length, int offset, boolean reverse);
	public Memory WriteString(String value);
	public Memory WriteString(String value, int length);
	public Memory WriteString(String value, int length, int offset);
	public Memory WriteString(String value, int length, boolean reverse);
	public Memory WriteString(String value, int length, int offset, boolean reverse);
	public String NextString(int length);
	public String NextString(int length, boolean reverse);
	public Memory NextWriteString(String value);
	public Memory NextWriteString(String value, int length);
	public Memory NextWriteString(String value, int length, boolean reverse);
	
	// ======================= //
	
	public byte[] Bytes(int length);
	public byte[] Bytes(int length, int offset);
	public byte[] Bytes(int length, boolean reverse);
	public byte[] Bytes(int length, int offset, boolean reverse);
	
	public Memory WriteBytes(byte[] value);
	public Memory WriteBytes(byte[] value, int length);
	public Memory WriteBytes(byte[] value, int length, int offset);
	public Memory WriteBytes(byte[] value, int length, boolean reverse);
	public Memory WriteBytes(byte[] value, int length, int offset, boolean reverse);
	
	public byte[] NextBytes(int length);
	public byte[] NextBytes(int length, boolean reverse);
	
	public Memory NextWriteBytes(byte[] value);
	public Memory NextWriteBytes(byte[] value, int length);
	public Memory NextWriteBytes(byte[] value, int length, boolean reverse);
	
	// ======================= //
	
	public short[] Shorts(int length);
	public short[] Shorts(int length, int offset);
	public short[] Shorts(int length, boolean bigEndian);
	public short[] Shorts(int length, int offset, boolean bigEndian);
	public short[] Shorts(int length, int offset, boolean bigEndian, boolean reverse);
	public Memory WriteShorts(short[] value);
	public Memory WriteShorts(short[] value, int length);
	public Memory WriteShorts(short[] value, int length, int offset);
	public Memory WriteShorts(short[] value, int length, boolean bigEndian);
	public Memory WriteShorts(short[] value, int length, int offset, boolean bigEndian);
	public Memory WriteShorts(short[] value, int length, int offset, boolean bigEndian, boolean reverse);
	public short[] NextShorts(int length);
	public short[] NextShorts(int length, boolean bigEndian);
	public short[] NextShorts(int length, boolean bigEndian, boolean reverse);
	public Memory NextWriteShorts(short[] value);
	public Memory NextWriteShorts(short[] value, int length);
	public Memory NextWriteShorts(short[] value, int length, boolean bigEndian);
	public Memory NextWriteShorts(short[] value, int length, boolean bigEndian, boolean reverse);
	
	// ======================= //
	
	public int[] Ints(int length);
	public int[] Ints(int length, int offset);
	public int[] Ints(int length, boolean bigEndian);
	public int[] Ints(int length, int offset, boolean bigEndian);
	public int[] Ints(int length, int offset, boolean bigEndian, boolean reverse);
	public Memory WriteInts(int[] value);
	public Memory WriteInts(int[] value, int length);
	public Memory WriteInts(int[] value, int length, int offset);
	public Memory WriteInts(int[] value, int length, boolean bigEndian);
	public Memory WriteInts(int[] value, int length, int offset, boolean bigEndian);
	public Memory WriteInts(int[] value, int length, int offset, boolean bigEndian, boolean reverse);
	public int[] NextInts(int length);
	public int[] NextInts(int length, boolean bigEndian);
	public int[] NextInts(int length, boolean bigEndian, boolean reverse);
	public Memory NextWriteInts(int[] value);
	public Memory NextWriteInts(int[] value, int length);
	public Memory NextWriteInts(int[] value, int length, boolean bigEndian);
	public Memory NextWriteInts(int[] value, int length, boolean bigEndian, boolean reverse);
	
	// ======================= //
	
	public long[] Longs(int length);
	public long[] Longs(int length, int offset);
	public long[] Longs(int length, boolean bigEndian);
	public long[] Longs(int length, int offset, boolean bigEndian);
	public long[] Longs(int length, int offset, boolean bigEndian, boolean reverse);
	public Memory WriteLongs(long[] value);
	public Memory WriteLongs(long[] value, int length);
	public Memory WriteLongs(long[] value, int length, int offset);
	public Memory WriteLongs(long[] value, int length, boolean bigEndian);
	public Memory WriteLongs(long[] value, int length, int offset, boolean bigEndian);
	public Memory WriteLongs(long[] value, int length, int offset, boolean bigEndian, boolean reverse);
	public long[] NextLongs(int length);
	public long[] NextLongs(int length, boolean bigEndian);
	public long[] NextLongs(int length, boolean bigEndian, boolean reverse);
	public Memory NextWriteLongs(long[] value);
	public Memory NextWriteLongs(long[] value, int length);
	public Memory NextWriteLongs(long[] value, int length, boolean bigEndian);
	public Memory NextWriteLongs(long[] value, int length, boolean bigEndian, boolean reverse);
	
	// ======================= //
	
	public float[] Floats(int length);
	public float[] Floats(int length, int offset);
	public float[] Floats(int length, boolean bigEndian);
	public float[] Floats(int length, int offset, boolean bigEndian);
	public float[] Floats(int length, int offset, boolean bigEndian, boolean reverse);
	public Memory WriteFloats(float[] value);
	public Memory WriteFloats(float[] value, int length);
	public Memory WriteFloats(float[] value, int length, int offset);
	public Memory WriteFloats(float[] value, int length, boolean bigEndian);
	public Memory WriteFloats(float[] value, int length, int offset, boolean bigEndian);
	public Memory WriteFloats(float[] value, int length, int offset, boolean bigEndian, boolean reverse);
	public float[] NextFloats(int length);
	public float[] NextFloats(int length, boolean bigEndian);
	public float[] NextFloats(int length, boolean bigEndian, boolean reverse);
	public Memory NextWriteFloats(float[] value);
	public Memory NextWriteFloats(float[] value, int length);
	public Memory NextWriteFloats(float[] value, int length, boolean bigEndian);
	public Memory NextWriteFloats(float[] value, int length, boolean bigEndian, boolean reverse);
	
	// ======================= //
	
	public double[] Doubles(int length);
	public double[] Doubles(int length, int offset);
	public double[] Doubles(int length, boolean bigEndian);
	public double[] Doubles(int length, int offset, boolean bigEndian);
	public double[] Doubles(int length, int offset, boolean bigEndian, boolean reverse);
	public Memory WriteDoubles(double[] value);
	public Memory WriteDoubles(double[] value, int length);
	public Memory WriteDoubles(double[] value, int length, int offset);
	public Memory WriteDoubles(double[] value, int length, boolean bigEndian);
	public Memory WriteDoubles(double[] value, int length, int offset, boolean bigEndian);
	public Memory WriteDoubles(double[] value, int length, int offset, boolean bigEndian, boolean reverse);
	public double[] NextDoubles(int length);
	public double[] NextDoubles(int length, boolean bigEndian);
	public double[] NextDoubles(int length, boolean bigEndian, boolean reverse);
	public Memory NextWriteDoubles(double[] value);
	public Memory NextWriteDoubles(double[] value, int length);
	public Memory NextWriteDoubles(double[] value, int length, boolean bigEndian);
	public Memory NextWriteDoubles(double[] value, int length, boolean bigEndian, boolean reverse);
	
	// ======================= //
	
	public UUID Uuid();
	public UUID Uuid(int offset);
	public UUID Uuid(boolean bigEndian);
	public UUID Uuid(int offset, boolean bigEndian);
	public UUID NextUuid();
	public UUID NextUuid(boolean bigEndian);
}
