package me.hardcoded.smreader.tile.readers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.utils.TileUtils;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.PrefabImpl;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class PrefabReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		if ((h.prefabCount == 0) || (h.prefabIndex == 0)) return;
		reader.set(h.prefabIndex);
		
		TileUtils.log("  Prefab           : %d / %d", h.prefabSize, h.prefabCompressedSize);
		
		byte[] compressed = reader.Bytes(h.prefabCompressedSize);
		byte[] bytes = new byte[h.prefabSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.prefabSize);
		if (debugSize != h.prefabCompressedSize) {
			TileUtils.error("debugSize != h.prefabCompressedSize: %d != %d", debugSize, h.prefabCompressedSize);
		}
		
		debugSize = read(bytes, h.prefabCount, part);
		if (debugSize != h.prefabSize) {
			TileUtils.error("debugSize != h.prefabSize: %d != %d", debugSize, h.prefabSize);
		}
		
		return;
	}
	
	public int read(byte[] bytes, int prefabCount, TilePart part) {
		int index = 0;
		Memory memory = new Memory(bytes);
		int version = part.getParent().getVersion();
		
		for (int i = 0; i < prefabCount; i++) {
			float[] f_pos = memory.Floats(3, index);
			float[] f_quat = memory.Floats(4, index + 0xc);
			float[] f_size;
			
			if (version < 9) {
				f_size = new float[] { 1.0f, 1.0f, 1.0f };
				index += 0x1c;
			} else {
				f_size = memory.Floats(3, index + 0x1c);
				index += 0x28;
			}
			
			int string_length = memory.Int(index);
			index += 4;
			String path = memory.String(string_length, index);
			index += string_length;
			
			int bVar2 = memory.UnsignedByte(index);
			index += 1;
			String flag = memory.String(bVar2, index);
			index += bVar2;
			index += 4;
			
			PrefabImpl prefab = new PrefabImpl();
			prefab.setPosition(f_pos);
			prefab.setRotation(f_quat);
			prefab.setSize(f_size);
			prefab.setPath(path);
			prefab.setFlag(flag);
			
			part.addPrefab(prefab);
		}
		
		return index;
	}
}
