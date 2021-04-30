package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.TilePart;
import com.hardcoded.tile.TileHeader.Header;
import com.hardcoded.utils.TileUtils;

/**
 * @author HardCoded
 */
public class MipReader implements TileReaderImpl {
	
	@Override
	public void read(Header header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(Header h, Memory reader) {
		TileUtils.log("  Mip              : %d, %d", h.mipCompressedSize, h.mipSize);
		
		byte[] compressed = reader.set(h.mipIndex).Bytes(h.mipCompressedSize);
		byte[] bytes = new byte[h.mipSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.mipSize);
		if(debugSize != h.mipCompressedSize) {
			TileUtils.error("debugSize != h.mipCompressedSize[0]"); // 235
		}
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		int w = 0x21, h = 0x21;
		Memory memory = new Memory(bytes);
		
		float[] height = new float[w * h];
		int[] color = new int[w * h];
		for(int i = 0; i < w * h; i++) {
			height[i] = memory.Float(i * 8, false);
			color[i] = memory.Int(i * 8 + 4, false);
		}
		
		memory.set(w * h * 8);
		
		w = 0x41;
		h = 0x41;
		long[] ground = new long[0x41 * 0x41];
		for(int i = 0; i < w * h; i++) {
			ground[i] = memory.NextLong();
		}
		
		part.setVertexColor(color);
		part.setVertexHeight(height);
		part.setGroundMaterials(ground);
	}
}
