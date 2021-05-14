package com.hardcoded.tile.writers;

import java.util.List;
import java.util.Map;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.tile.object.Asset;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This writer is incomplete!</b>
 * 
 * @author HardCoded
 */
public class AssetListWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		for(int i = 0; i < 4; i++) {
			List<Asset> list = part.assets[i];
			
			if(list.isEmpty()) {
				header.assetListCount[i] = 0;
				header.assetListIndex[i] = 0;
				header.assetListCompressedSize[i] = 0;
				header.assetListSize[i] = 0;
			} else {
				byte[] data = write(list, part);
				byte[] compressed = TileUtils.compress_data(data);
				header.assetListCompressedSize[i] = compressed.length;
				header.assetListSize[i] = data.length;
				header.assetListIndex[i] = memory.index();
				header.assetListCount[i] = list.size();
				memory.NextWriteBytes(compressed);
			}
		}
	}
	
	public byte[] write(List<Asset> asset_list, TilePart part) {
		Memory memory = new Memory(160 * asset_list.size());
		
		for(Asset asset : asset_list) {
			memory.NextWriteFloats(asset.getPosition().toArray());
			memory.NextWriteFloats(asset.getRotation().toArray());
			memory.NextWriteFloats(asset.getSize().toArray());
			memory.NextWriteUUID(asset.getUuid(), true);
			
			Map<String, Integer> map = asset.getMaterials();
			memory.NextWriteByte(map.size());
			for(String key : map.keySet()) {
				memory.NextWriteByte(key.length());
				memory.NextWriteString(key);
				memory.NextWriteInt(map.get(key));
			}
		}
		
		memory.NextWriteByte(0);
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
