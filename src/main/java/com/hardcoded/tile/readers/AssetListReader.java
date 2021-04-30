package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.TileHeader.Header;
import com.hardcoded.utils.TileUtils;
import com.hardcoded.tile.TilePart;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class AssetListReader implements TileReaderImpl {
	
	@Override
	public void read(Header header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[][] read(Header h, Memory reader) {
		byte[][] bytes = new byte[4][];
		for(int i = 0; i < 4; i++) {
			int assetListCompressedSize = h.assetListCompressedSize[i];
			int assetListSize = h.assetListSize[i];
			
			TileUtils.log("    Asset[%d]       : %d / %d", i, assetListSize, assetListCompressedSize);
			
			if(h.assetListDefined[i]) {
				reader.set(h.assetListIndex[i]);
				
				byte[] compressed = reader.Bytes(assetListCompressedSize);
				bytes[i] = new byte[assetListSize];
				
				int debugSize = TileUtils.decompress_data(compressed, bytes[i], assetListSize);
				if(debugSize != h.assetListCompressedSize[i]) {
					TileUtils.error("debugSize != h.assetListCompressedSize[%d]", i); // 254
				}
				
				// FUN_00b8cf70((int *)&local_1a4, (int)_pbVar9, (int *)&local_124, &local_18c, ((uint *)puVar18)[-8], tileVersion);
				// Assert(debugSize == h.assetListSize[i], "debugSize == h.assetListSize[" + i + "]", 256);
			}
		}
		
		return bytes;
	}

	public void read(byte[][] bytes, TilePart part) {
		
	}
}
