package me.hardcoded.smreader.lua;

import java.util.LinkedHashMap;
import java.util.Map;

import me.hardcoded.smreader.data.BitStream;
import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.logger.Log;
import me.hardcoded.smreader.utils.TileUtils;

/**
 * A implementation of a lua deserializer
 * @author HardCoded
 * @since v0.1
 */
public class LuaDeserializer {
	private static final Log LOGGER = Log.getLogger(LuaDeserializer.class);
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
		byte[] output = new byte[0x400000];
		int output_size = TileUtils.safe_decompress_data(param_2.Bytes(size), output);
		
		return DeserializePure(output, 0, output_size);
	}
	
	public static Object DeserializePure(byte[] param_2, int offset, int size) {
		return DeserializePure(new Memory(param_2, offset, size));
	}
	
	public static Object DeserializePure(Memory memory) {
		// Quick fix to make sure we have enough data to read
		memory.expand(memory.data().length + 10);
		BitStream stream = new BitStream(memory);
		
		String magic = stream.readString(3);
		//System.out.println("music: '" + magic + "'");
		if(!"LUA".equals(magic)) {
			throw new RuntimeException("Failed Assertion: tag[i] == LUA_MAGIC_TAG[i]");
		}
		
		int version = stream.readInt();
		if(version != VERSION) {
			throw new RuntimeException("Failed Assertion: Outdated serialized data: version='" + version + "'");
		}
		
		try {
			boolean[] keepReading = { true };
			do {
				Object object = DeserializeLua(stream, keepReading);
				
				return object;
			} while(keepReading[0]);
		} catch(Exception e) {
			LOGGER.throwing(e);
			
			// In case there was an error return an empty map
			return Map.of();
		}
	}
	
	private static Object DeserializeLua(BitStream stream, boolean[] keepReading) {
		Object output;
		
		int read_id = stream.readByte();
		if(read_id == 0) {
			keepReading[0] = false;
			return null;
		}
		
		LuaSaveDataType type = LuaSaveDataType.getType(read_id);
		
//		if(type != LuaSaveDataType.Table) {
//			System.out.printf("ReadingType: (type)=%-10s, (id)   =%-5d, (value)=", type, read_id);
//		}
		
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
				Map<Object, Object> map = new LinkedHashMap<>();
				
				int length = stream.readInt();
				boolean isArray = stream.readBool();
				
				//System.out.printf("ReadingType: (type)=%-10s, (array)=%-5s, (length)=%d\n", type, isArray, length);
				if(!isArray) {
					for(int i = 0; i < length; i++) {
						Object key = DeserializeLua(stream, keepReading);
						Object val = DeserializeLua(stream, keepReading);
						map.put(key, val);
					}
				} else {
					int offset = stream.readInt();
					
					for(int i = 0; i < length; i++) {
						Object val = DeserializeLua(stream, keepReading);
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
				throw new RuntimeException("Invalid lua type: '" + type + "' id='" + read_id + "'");
			}
		}
		
//		if(type != LuaSaveDataType.Table) {
//			System.out.println(output);
//		}
		
		return output;
	}
	
}
