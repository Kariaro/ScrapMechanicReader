package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class PrefabReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(CellHeader h, Memory reader) {
		if((h.prefabCount == 0) || (h.prefabIndex == 0)) return null;
		reader.set(h.prefabIndex);
		
		TileUtils.log("  Prefab           : %d / %d", h.prefabSize, h.prefabCompressedSize);
		
		byte[] compressed = reader.Bytes(h.prefabCompressedSize);
		byte[] bytes = new byte[h.prefabSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.prefabSize);
		if(debugSize != h.prefabCompressedSize) {
			TileUtils.error("debugSize != h.prefabCompressedSize");
		}
		
		// System.out.println(new String(bytes));
		// Assert(debugSize == h.prefabSize, "debugSize == h.prefabSize", 279);
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		
	}
}
