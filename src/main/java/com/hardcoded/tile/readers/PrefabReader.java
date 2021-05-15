package com.hardcoded.tile.readers;

import java.io.File;
import java.io.IOException;

import com.hardcoded.data.Memory;
import com.hardcoded.game.GameContext;
import com.hardcoded.math.Quat;
import com.hardcoded.math.Vec3;
import com.hardcoded.prefab.readers.PrefabFileReader;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.PrefabImpl;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class PrefabReader implements TileReaderImpl {
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		if((h.prefabCount == 0) || (h.prefabIndex == 0)) return;
		reader.set(h.prefabIndex);
		
		TileUtils.log("  Prefab           : %d / %d", h.prefabSize, h.prefabCompressedSize);
		
		byte[] compressed = reader.Bytes(h.prefabCompressedSize);
		byte[] bytes = new byte[h.prefabSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.prefabSize);
		if(debugSize != h.prefabCompressedSize) {
			TileUtils.error("debugSize != h.prefabCompressedSize: %d != %d", debugSize, h.prefabCompressedSize);
		}
		
		debugSize = read(bytes, h.prefabCount, part);
		if(debugSize != h.prefabSize) {
			TileUtils.error("debugSize != h.prefabSize: %d != %d", debugSize, h.prefabSize);
		}
		
		return;
	}
	
	public int read(byte[] bytes, int prefabCount, TilePart part) {
		int index = 0;
		Memory memory = new Memory(bytes);
		int version = part.getParent().getVersion();
		
		//GameContext context = part.getParent().getContext();
		
		for(int i = 0; i < prefabCount; i++) {
			float[] f_pos = memory.Floats(3, index);
			float[] f_quat = memory.Floats(4, index + 0xc);
			float[] f_size;
			
			if(version < 9) {
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
			
			{
				System.out.printf("Prefab:\n");
				System.out.printf("  path : %s\n", path);
				System.out.printf("  flag : %s\n", flag);
				System.out.printf("  pos  : %s\n", new Vec3(f_pos));
				System.out.printf("  rot  : %s\n", new Quat(f_quat));
				System.out.printf("  size : %s\n", new Vec3(f_size));
				System.out.println();
			}
			
			GameContext context = part.getParent().getContext();
			if(!context.isValid()) {
				TileUtils.warn("GameContext was not valid so prefab file cannot be parsed");
			} else {
				File prefab_file = context.resolve(path);
				try {
					PrefabFileReader.readPrefab(prefab_file.getAbsolutePath());
				} catch(IOException e) {
					TileUtils.error("Failed to read the prefab file '%s'", prefab_file);
				}
			}
			
			part.addPrefab(prefab);
		}
		
		return index;
	}
}
