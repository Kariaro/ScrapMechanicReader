package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.TileHeader.Header;
import com.hardcoded.tile.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class DecalReader implements TileReaderImpl {
	
	@Override
	public void read(Header header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(Header h, Memory reader) {
		if((h.bytes_d4 == 0) || (h.bytes_d8 == 0)) return null;
		reader.set(h.bytes_d8);
		
		TileUtils.log("  Decal            : %d / %d", h.decalSize, h.decalCompressedSize);
		
		byte[] compressed = reader.Bytes(h.decalCompressedSize);
		byte[] bytes = new byte[h.decalSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.decalSize);
		if(debugSize != h.decalCompressedSize) {
			TileUtils.error("debugSize != h.decalCompressedSize"); // 301
		}
		
		// Assert(debugSize == h.decalSize, "debugSize == h.decalSize", 303);
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		
	}
}
