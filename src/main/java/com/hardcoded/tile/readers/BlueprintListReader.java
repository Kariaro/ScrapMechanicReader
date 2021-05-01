package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.HeaderPart;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class BlueprintListReader implements TileReaderImpl {
	
	@Override
	public void read(HeaderPart header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(HeaderPart h, Memory reader) {
		if((h.bytes_94 == 0) || (h.bytes_98 == 0)) return null;
		reader.set(h.bytes_98);
		
		TileUtils.log("  BlueprintList    : %d / %d", h.blueprintListSize, h.blueprintListCompressedSize);
		
		byte[] compressed = reader.Bytes(h.blueprintListCompressedSize);
		byte[] bytes = new byte[h.blueprintListSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.blueprintListSize);
		if(debugSize != h.blueprintListCompressedSize) {
			TileUtils.error("debugSize != h.blueprintListCompressedSize"); // 290
		}
		
		// Assert(debugSize == h.blueprintListSize, "debugSize == h.blueprintListSize", 292);
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		
	}
}
