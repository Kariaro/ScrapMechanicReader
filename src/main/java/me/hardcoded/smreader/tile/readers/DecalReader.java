package me.hardcoded.smreader.tile.readers;

import java.util.UUID;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.utils.TileUtils;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.DecalImpl;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class DecalReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		if ((h.decalCount == 0) || (h.decalIndex == 0)) return;
		reader.set(h.decalIndex);
		
		TileUtils.log("  Decal            : %d / %d", h.decalSize, h.decalCompressedSize);
		
		byte[] compressed = reader.Bytes(h.decalCompressedSize);
		byte[] bytes = new byte[h.decalSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.decalSize);
		if (debugSize != h.decalCompressedSize) {
			TileUtils.error("debugSize != h.decalCompressedSize: %d != %d", debugSize, h.decalCompressedSize);
		}
		
		debugSize = read(bytes, h.decalCount, part);
		if (debugSize != h.decalSize) {
			TileUtils.error("debugSize != h.decalSize: %d != %d", debugSize, h.decalSize);
		}
	}
	
	public int read(byte[] bytes, int decalCount, TilePart part) {
		Memory memory = new Memory(bytes);
		int index = 0;
		
		for (int i = 0; i < decalCount; i++) {
			float[] f_pos = memory.Floats(3, index);
			float[] f_quat = memory.Floats(4, index + 0xc);
			float[] f_size = memory.Floats(3, index + 0x1c);
			UUID uuid = memory.Uuid(index + 0x28, true);
			
			DecalImpl decal = new DecalImpl();
			decal.setPosition(f_pos);
			decal.setRotation(f_quat);
			decal.setSize(f_size);
			decal.setUuid(uuid);
			part.addDecal(decal);
			
			// Unknown 2 bytes
//			float[] test_f = memory.Floats(2, index + 0x38);
//			int[] test_i = memory.Ints(2, index + 0x38);
//			System.out.printf("  test float: %s\n", Arrays.toString(test_f));
//			System.out.printf("  test int  : %s\n", Arrays.toString(test_i));
			
			//index += 0x38;
			index += 0x40;
		}
		
		return index;
	}
}
