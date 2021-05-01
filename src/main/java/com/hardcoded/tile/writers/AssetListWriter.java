package com.hardcoded.tile.writers;

import java.util.List;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.Asset;
import com.hardcoded.tile.HeaderPart;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This writer is incomplete!</b>
 * 
 * @author HardCoded
 */
public class AssetListWriter implements TileWriterImpl {
	
	@Override
	public void write(HeaderPart header, Memory memory, TilePart part) {
		for(int i = 0; i < 4; i++) {
			List<Asset> list = part.assets[i];
			
			if(list.isEmpty()) {
				header.assetListCompressedSize[i] = 0;
				header.assetListSize[i] = 0;
				header.assetListIndex[i] = 0;
				header.assetListDefined[i] = 0;
			} else {
				byte[] data = write(list, part);
				byte[] compressed = TileUtils.compress_data(data);
				header.assetListCompressedSize[i] = compressed.length;
				header.assetListSize[i] = data.length;
				header.assetListIndex[i] = memory.index();
				header.assetListDefined[i] = list.size();
				memory.NextWriteBytes(compressed);
				
//				TileUtils.log("%d", header.assetListCompressedSize[i]);
//				TileUtils.log("%d", header.assetListSize[i]);
//				TileUtils.log("%d", header.assetListIndex[i]);
//				TileUtils.log("%d", header.assetListDefined[i]);
			}
		}
	}
	
	public byte[] write(List<Asset> asset_list, TilePart part) {
		Memory memory = new Memory(160 * asset_list.size());
		
		for(Asset asset : asset_list) {
			memory.NextWriteFloats(asset.getPosition().toArray());
			memory.NextWriteFloats(asset.getRotation().toArray());
			memory.NextWriteFloats(asset.getSize().toArray());
			memory.NextWriteUUID(asset.getUUID());
			
			List<String> list = asset.getMaterials();
			memory.NextWriteByte(list.size());
			for(String str : list) {
				memory.NextWriteByte(str.length());
				memory.NextWriteString(str);
				
				// TODO: I do not understand this value?
				memory.NextWriteInt(0);
			}
		}
		
		memory.NextWriteByte(0);
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
