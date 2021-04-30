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
public class HarvestableListReader implements TileReaderImpl {
	
	@Override
	public void read(Header header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[][] read(Header h, Memory reader) {
		byte[][] bytes = new byte[4][];
		
		for(int i = 0; i < 4; i++) {
			int harvestableListCompressedSize = h.harvestableListCompressedSize[i];
			int harvestableListSize = h.harvestableListSize[i];
			
			TileUtils.log("    Harvestable[%d] : %d / %d", i, harvestableListSize, harvestableListCompressedSize);
			
			if(h.harvestableListDefined[i]) {
				reader.set(h.harvestableListIndex[i]);
				
				byte[] compressed = reader.Bytes(harvestableListCompressedSize);
				bytes[i] = new byte[harvestableListSize];
				
				int debugSize = TileUtils.decompress_data(compressed, bytes[i], h.harvestableListSize[i]);
				if(debugSize != h.harvestableListCompressedSize[i]) {
					TileUtils.error("debugSize != h.harvestableListCompressedSize[%d]", i); // 314
				}
				
				// Assert(debugSize == h.harvestableListSize[i], "debugSize == h.harvestableListSize[" + i + "]", 316);
			}
		}
		
		return bytes;
	}

	public void read(byte[][] bytes, TilePart part) {
		
	}
}
