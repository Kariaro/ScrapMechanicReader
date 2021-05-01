package com.hardcoded.tile.writers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * @author HardCoded
 */
public class MipWriter implements TileWriterImpl {
	
	@Override
	public void write(HeaderPart header, Memory memory, TilePart part) {
		byte[] data = write(part);
		
		byte[] compressed = TileUtils.compress_data(data);
		header.mipCompressedSize = compressed.length;
		header.mipSize = data.length;
		header.mipIndex = memory.index();
		memory.NextWriteBytes(compressed);
	}
	
	public byte[] write(TilePart part) {
		int w = 0x21, h = 0x21;
		
		float[] height = part.vertexHeight;
		int[] color = part.vertexColor;
		long[] ground = part.ground;
		
		Memory memory = new Memory((height.length + color.length) * 4 + (ground.length) * 8);
		for(int i = 0; i < w * h; i++) {
			memory.WriteFloat(height[i], i * 8, false);
			memory.WriteInt(color[i], i * 8 + 4, false);
		}
		
		memory.set(w * h * 8);
		
		for(int i = 0; i < w * h; i++) {
			memory.NextWriteLong(ground[i]);
		}
		
		return memory.data();
	}
}
