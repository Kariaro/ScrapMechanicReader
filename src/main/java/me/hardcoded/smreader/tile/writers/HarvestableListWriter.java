package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.HarvestableImpl;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.tile.object.Asset;
import me.hardcoded.smreader.tile.object.Harvestable;
import me.hardcoded.smreader.utils.TileUtils;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * <b>This writer is incomplete!</b>
 * 
 * @author HardCoded
 */
public class HarvestableListWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		for (int i = 0; i < 4; i++) {
			List<Harvestable> list = part.harvestables[i];
			
			if (list.isEmpty()) {
				header.harvestableListCount[i] = 0;
				header.harvestableListIndex[i] = 0;
				header.harvestableListCompressedSize[i] = 0;
				header.harvestableListSize[i] = 0;
			} else {
				byte[] data = write(list, part);
				byte[] compressed = TileUtils.compress_data(data);
				header.harvestableListCompressedSize[i] = compressed.length;
				header.harvestableListSize[i] = data.length;
				header.harvestableListIndex[i] = memory.index();
				header.harvestableListCount[i] = list.size();
				memory.NextWriteBytes(compressed);
			}
		}
	}
	
	public byte[] write(List<Harvestable> harvestableList, TilePart part) {
		Memory memory = new Memory(160 * harvestableList.size());
		
		for (Harvestable harvestable : harvestableList) {
			memory.NextWriteFloats(harvestable.getPosition().toArray());
			memory.NextWriteFloats(harvestable.getRotation().toArray());
			memory.NextWriteFloats(harvestable.getSize().toArray());
			memory.NextWriteUuid(harvestable.getUuid(), true);
			memory.NextWriteInt(harvestable.getColor(), true);
		}
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
