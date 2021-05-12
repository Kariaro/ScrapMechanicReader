package com.hardcoded.lua;

import java.util.*;

import com.hardcoded.data.BitStream;
import com.hardcoded.data.Memory;
import com.hardcoded.utils.TileUtils;

/**
 * A implementation of a lua deserializer
 * @author HardCoded
 * @since v0.1
 */
public class LuaDeserializer {
	//private static final Log LOGGER = Log.getLogger();
	public static final int VERSION = 1;
	
	public static enum LuaSaveDataType {
		Unknown(0x0),
		Nil(0x1),
		Bool(0x2),
		Number(0x3),
		String(0x4),
		Table(0x5),
		Int32(0x6),
		Int16(0x7),
		Int8(0x8),
		Json(0x9),
		Userdata(0x64),
		Unknown_0x65(0x65),
		;
		
		private final int id;
		private LuaSaveDataType(int id) {
			this.id = id;
		}
		
		public static LuaSaveDataType getType(int id) {
			for(LuaSaveDataType type : values()) {
				if(type.id == id) return type;
			}
			
			return LuaSaveDataType.Unknown;
		}
	}
	
	public static Object Deserialize(Memory param_2, int size) {
		BitStream stream;
		{
			byte[] output_test = new byte[0x400000];
			byte[] inputs = param_2.Bytes(size);
			int output_size = TileUtils.safe_decompress_data(inputs, output_test);
			stream = new BitStream(new Memory(output_test, output_size));
		}
		
		char[] magic = stream.readChars(3);
		if((magic[0] != 'L') || (magic[1] != 'U') || (magic[2] != 'A')) {
			throw new RuntimeException("Failed Assertion: tag[i] == LUA_MAGIC_TAG[i]");
		}
		
		int version = stream.readInt();
		if(version != 1) {
			throw new RuntimeException("Failed Assertion: Outdated serialized data: version='" + version + "'");
		}
		
		boolean keepReading = true;
		do {
			Object object = DeserializeLua(stream);
			
			return object;
		} while(keepReading);
	}
	
	private static Object DeserializeLua(BitStream stream) {
		Object output;
		
		int read_id = stream.readByte();
		LuaSaveDataType type = LuaSaveDataType.getType(read_id);
		
		switch(type) {
			case Nil: {
				output = null;
				break;
			}
			case Bool: {
				boolean result = stream.readBool();
				output = result;
				break;
			}
			case Number: {
				float result = Float.intBitsToFloat(stream.readInt());
				output = result;
				break;
			}
			case String: {
				int length = stream.readInt();
				String result = new String(stream.readBytes(length));
				output = result;
				break;
			}
			case Table: {
				Map<Object, Object> map = new HashMap<>();
				
				int length = stream.readInt();
				boolean isArray = stream.readBool();
				
				if(!isArray) {
					for(int i = 0; i < length; i++) {
						Object key = DeserializeLua(stream);
						Object val = DeserializeLua(stream);
						map.put(key, val);
					}
				} else {
					int offset = stream.readInt();
					
					for(int i = 0; i < length; i++) {
						Object val = DeserializeLua(stream);
						map.put(offset + i, val);
					}
				}
				
				output = map;
				break;
			}
			case Int32: {
				int result = stream.readInt();
				output = (int)result;
				break;
			}
			case Int16: {
				int result = stream.readShort();
				output = (short)result;
				break;
			}
			case Int8: {
				int result = stream.readByte();
				output = (byte)result;
				break;
			}
			case Json: {
				//LOGGER.info("%s(Json), ????", space);
				int length = stream.readInt();
				String result = new String(stream.readBytes(length));
				output = "<Json> (" + result + ")";
				break;
			}
			case Userdata: {
				//LOGGER.info("%s(Userdata), ????", space);
				
				output = "<Userdata>";
				break;
			}
			case Unknown_0x65: {
				//LOGGER.info("%s(Unknown_0x65), ????", space);
				
				output = "<Unknown_0x65>";
				break;
			}
			default: {
				throw new RuntimeException("Invalid lua type: '" + type + "'");
			}
		}
		
		return output;
	}
	
	private static void dump(String str, Memory data, int offset) {
		int len = data.data().length - data.index() - offset;
		if(!str.isEmpty()) str = " [" + str + "] ";
		
		if(len > 255) len = 255;
		
		System.out.println("#########" + str + "###########: len=" + len + ", idx=" + offset);
		for(int i = 0; i < len; i++) System.out.printf("%02x ", data.UnsignedByte(i + offset));
		System.out.println();
		for(int i = 0; i < len; i++) {
			char c = (char)data.UnsignedByte(i + offset);
			System.out.printf("%3s", (Character.isWhitespace(c) || Character.isISOControl(c) ? "":c) + " ");
		}
		System.out.println();
		System.out.println("#########" + str + "###########");
	}
	
	private static void dump(String str, Memory data) {
		dump(str, data, 0);
	}
	
	private static void dump(Memory data) {
		dump("", data, 0);
	}
}
