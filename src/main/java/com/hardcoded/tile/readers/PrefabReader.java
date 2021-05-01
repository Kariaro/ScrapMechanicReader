package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.TileHeader.Header;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class PrefabReader implements TileReaderImpl {
	
	@Override
	public void read(Header header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(Header h, Memory reader) {
		if((h.bytes_c4 == 0) || (h.bytes_c8 == 0)) return null;
		reader.set(h.bytes_c8);
		
		TileUtils.log("  Prefab           : %d / %d", h.prefabSize, h.prefabCompressedSize);
		
		byte[] compressed = reader.Bytes(h.prefabCompressedSize);
		byte[] bytes = new byte[h.prefabSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.prefabSize);
		if(debugSize != h.prefabCompressedSize) {
			TileUtils.error("debugSize != h.prefabCompressedSize"); // 277
		}
		
		System.out.println(new String(bytes));
		// Assert(debugSize == h.prefabSize, "debugSize == h.prefabSize", 279);
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		
	}
}
