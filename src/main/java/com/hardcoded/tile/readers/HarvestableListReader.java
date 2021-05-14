package com.hardcoded.tile.readers;

import java.util.UUID;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.HarvestableImpl;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 * @since v0.2
 */
public class HarvestableListReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		byte[][] bytes = new byte[4][];
		
		for(int i = 0; i < 4; i++) {
			int harvestableListCompressedSize = h.harvestableListCompressedSize[i];
			int harvestableListSize = h.harvestableListSize[i];
			
			TileUtils.log("    Harvestable[%d] : %d / %d", i, harvestableListSize, harvestableListCompressedSize);
			
			if(h.harvestableListCount[i] != 0) {
				reader.set(h.harvestableListIndex[i]);
				
				byte[] compressed = reader.Bytes(harvestableListCompressedSize);
				bytes[i] = new byte[harvestableListSize];
				
				int debugSize = TileUtils.decompress_data(compressed, bytes[i], h.harvestableListSize[i]);
				if(debugSize != h.harvestableListCompressedSize[i]) {
					TileUtils.error("debugSize != h.harvestableListCompressedSize[%d]", i); // 314
				}
				
				debugSize = read(bytes[i], i, h.harvestableListCount[i], part.getParent().getVersion(), part);
				if(debugSize != h.harvestableListSize[i]) {
					TileUtils.error("debugSize != h.harvestableListSize[%d]: %d != %d", i, debugSize, h.harvestableListSize[i]);
				}
			}
		}
	}

	public int read(byte[] bytes, int harvestable_index, int len, int version, TilePart part) {
		Memory memory = new Memory(bytes);
		
		int index = 0;
		for(int i = 0; i < len; i++) {
			float[] f_pos = memory.Floats(3, index);
			float[] f_quat = memory.Floats(4, index + 0xc);
			float[] f_size = memory.Floats(3, index + 0x1c);
			UUID uuid = memory.Uuid(index + 0x28, true);
			@SuppressWarnings("unused")
			int uVar12 = memory.Int(index + 0x38);
			index += 0x3c;
			
			HarvestableImpl harvestable = new HarvestableImpl();
			harvestable.setPosition(f_pos);
			harvestable.setRotation(f_quat);
			harvestable.setSize(f_size);
			harvestable.setUuid(uuid);
			
			part.addHarvestable(harvestable, harvestable_index);
		}
		
		return index;
	}
}
