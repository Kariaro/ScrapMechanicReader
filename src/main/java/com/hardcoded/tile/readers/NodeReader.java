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
public class NodeReader implements TileReaderImpl {
	
	@Override
	public void read(Header header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(Header h, Memory reader) {
		if((h.bytes_a4 == 0) || (h.bytes_a8 == 0)) return null;
		reader.set(h.bytes_a8);
		
		TileUtils.log("  Node             : %d / %d", h.nodeSize, h.nodeCompressedSize);
		
		byte[] compressed = reader.Bytes(h.nodeCompressedSize);
		byte[] bytes = new byte[h.nodeSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.nodeSize);
		if(debugSize != h.nodeCompressedSize) {
			TileUtils.error("debugSize != h.nodeCompressedSize"); // 266
		}
		
		// Assert(debugSize == h.nodeSize, "debugSize == h.nodeSize", 268);
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		
	}
}
