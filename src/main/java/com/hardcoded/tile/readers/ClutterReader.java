package com.hardcoded.tile.readers;

import java.util.UUID;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class ClutterReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(CellHeader h, Memory reader) {
		TileUtils.log("  Clutter          : %d, %d", h.clutterCompressedSize, h.clutterSize);
		
		byte[] compressed = reader.set(h.clutterIndex).Bytes(h.clutterCompressedSize);
		byte[] bytes = new byte[h.clutterSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.clutterSize);
		if(debugSize != h.clutterCompressedSize) {
			TileUtils.error("debugSize != h.clutterCompressedSize");
		}
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		Memory memory = new Memory(bytes);
		
		if(memory.Byte() != 0) {
			int length = memory.UnsignedByte();
			int offset = 0;
			
			memory.move(2);
			for(int i = 0; i < length; i++) {
				int uVar7 = 0;
				for(int j = 0; j < 0x10; j++) {
					int read = memory.Byte(offset + j);
					// uVar7 = (uVar7 ^ read) + 0x9e3779b9 + (uVar7 * 0x40) + (uVar7 >> 2);
					
					int a = (uVar7 ^ read);
					int b = 0x9e3779b9;
					int c = (uVar7 << 6);
					int d = (uVar7 >> 2);
					
					uVar7 = a + b + c + d;
				}
				UUID uuid = memory.Uuid(offset, true);
				
				TileUtils.log("    Clutter: %s    %08x", uuid, uVar7);
				//TileUtils.log("%08x\n", uVar7);
				
				int iVar8 = 0;
				if(iVar8 != 1) {
					//Log("TerrainGrass", "false && \"Uuid dosen't exist in the clutter manager, defaulting to 0.\"", 149);
				} else {
					// FUN_009b8120(&local_64,local_3c,(byte *)((int)&bytes + 2));
				}
				
				offset += 0x11;
			}
			
			memory.set(offset);
		}
		
		byte[] next = new byte[128 * 128];
		{
			int offset = 0;
			
			for(int i = 0; i < 0x4000; i++) {
				byte read = memory.Byte(1 + i + offset);
				next[i] = (byte)read;
			}
			
			part.setClutter(next);
		}
		
		// TODO - This is not complete...
	}
}
