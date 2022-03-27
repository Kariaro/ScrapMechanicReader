package me.hardcoded.smreader.tile.readers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.utils.TileUtils;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.BlueprintImpl;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class BlueprintListReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		if ((h.blueprintListCount == 0) || (h.blueprintListIndex == 0)) return;
		reader.set(h.blueprintListIndex);
		
		TileUtils.log("  BlueprintList    : %d / %d", h.blueprintListSize, h.blueprintListCompressedSize);
		
		byte[] compressed = reader.Bytes(h.blueprintListCompressedSize);
		byte[] bytes = new byte[h.blueprintListSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.blueprintListSize);
		if (debugSize != h.blueprintListCompressedSize) {
			TileUtils.error("debugSize != h.blueprintListCompressedSize"); // 290
		}
		
		debugSize = read(bytes, h.blueprintListCount, part);
		if (debugSize != h.blueprintListSize) {
			TileUtils.error("debugSize != h.blueprintListSize: %d != %d", debugSize, h.blueprintListSize);
		}
	}
	
	public int read(byte[] bytes, final int count, TilePart part) {
		final int tileVersion = part.getParent().getVersion();
		
		Memory memory = new Memory(bytes);
		
		int index = 0;
		for (int i = 0; i < count; i++) {
			float[] f_pos = memory.Floats(3, index);
			float[] f_quat = memory.Floats(4, index + 0xc);
			index += 0x1c;
			
			String value = "";
			if (tileVersion < 7) {
				int len = memory.UnsignedByte(index);
				index++;
				
				value = memory.String(len, index);
				index += len;
			} else {
				int len = memory.Int(index);
				index += 4;
				
				value = memory.String(len, index);
				index += len;
			}
			
			boolean isLoaded = value.startsWith("?JB:");
			BlueprintImpl blueprint = new BlueprintImpl(isLoaded);
			blueprint.setPosition(f_pos);
			blueprint.setRotation(f_quat);
			
//			boolean bVar10;
//			if(tileVersion == 6) {
//				bVar10 = true;
//			} else {
//				bVar10 = false;
//			}
			
			part.addBlueprint(blueprint);
		}
		
		return index;
	}
}
