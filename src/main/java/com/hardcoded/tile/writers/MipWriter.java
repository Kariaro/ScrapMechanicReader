package com.hardcoded.tile.writers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * @author HardCoded
 */
public class MipWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		for(int i = 0; i < 6; i++) {
			write(header, memory, i, part);
		}
	}
	
	public void write(CellHeader header, Memory memory, int mipOrLevel, TilePart part) {
		byte[] data = write(mipOrLevel, part);
		byte[] compressed = TileUtils.compress_data(data);
		header.mipCompressedSize[mipOrLevel] = compressed.length;
		header.mipSize[mipOrLevel] = data.length;
		header.mipIndex[mipOrLevel] = memory.index();
		memory.NextWriteBytes(compressed);
	}
	
	public byte[] write(int mipLevel, TilePart part) {
		int w = (0x20 >> mipLevel) + 1;
		int h = w;
		
		int ls = (0x40 >> mipLevel) + 1;
		
		float[] height = part.vertexHeight;
		int[] color = part.vertexColor;
		long[] ground = part.ground;
		
		Memory memory = new Memory((w * h * 8) + (ls * ls * 8));
		for(int i = 0; i < w * h; i++) {
			int x = (i % w) << mipLevel;
			int y = (i / w) << mipLevel;
			int idx = x + y * 0x21;
			
			memory.WriteFloat(height[idx], i * 8, false);
			memory.WriteInt(color[idx], i * 8 + 4, false);
		}
		
		memory.set(w * h * 8);
		
		for(int i = 0; i < w * h; i++) {
			int x = (i % w) << mipLevel;
			int y = (i / w) << mipLevel;
			int idx = x + y * 0x41;
			memory.NextWriteLong(ground[idx]);
		}
		
		return memory.data();
	}
}
