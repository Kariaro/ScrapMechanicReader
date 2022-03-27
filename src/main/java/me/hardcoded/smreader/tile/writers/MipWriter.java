package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.utils.TileUtils;

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
		int fs = (0x20 >> mipLevel) + 1;
		int ls = (0x40 >> mipLevel) + 1;
		
		float[] height = part.vertexHeight;
		int[] color = part.vertexColor;
		long[] ground = part.ground;
		
		Memory memory = new Memory((fs * fs * 8) + (ls * ls * 8));
		for(int i = 0; i < fs * fs; i++) {
			int x = (i % fs) << mipLevel;
			int y = (i / fs) << mipLevel;
			int idx = x + y * 0x21;
			
			memory.WriteFloat(height[idx], i * 8, false);
			memory.WriteInt(color[idx], i * 8 + 4, false);
		}
		
		memory.set(fs * fs * 8);
		
		for(int i = 0; i < ls * ls; i++) {
			int x = (i % ls) << mipLevel;
			int y = (i / ls) << mipLevel;
			int idx = x + y * 0x41;
			memory.NextWriteLong(ground[idx]);
		}
		
		return memory.data();
	}
}
