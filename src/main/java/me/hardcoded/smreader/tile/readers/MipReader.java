package me.hardcoded.smreader.tile.readers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.utils.TileUtils;

/**
 * @author HardCoded
 */
public class MipReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader header, Memory memory, TilePart part) {
		read(read(header, memory), part);
	}
	
	public byte[] read(CellHeader h, Memory reader) {
		return read(h, 0, reader);
	}
	
	public byte[] read(CellHeader h, int mipOrLevel, Memory reader) {
		TileUtils.log("  Mip              : %d, %d", h.mipCompressedSize[mipOrLevel], h.mipSize[mipOrLevel]);
		
		byte[] compressed = reader.set(h.mipIndex[mipOrLevel]).Bytes(h.mipCompressedSize[mipOrLevel]);
		byte[] bytes = new byte[h.mipSize[mipOrLevel]];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.mipSize[mipOrLevel]);
		if (debugSize != h.mipCompressedSize[mipOrLevel]) {
			TileUtils.error("debugSize != h.mipCompressedSize[%d]: %d != %d", mipOrLevel, debugSize, h.mipCompressedSize[mipOrLevel]);
		}
		
		return bytes;
	}
	
	public void read(byte[] bytes, TilePart part) {
		int w = 0x21, h = 0x21;
		Memory memory = new Memory(bytes);
		
		float[] height = new float[w * h];
		int[] color = new int[w * h];
		
		for (int i = 0; i < w * h; i++) {
			height[i] = memory.Float(i * 8, false);
			color[i] = memory.Int(i * 8 + 4, false);
		}
		
		memory.set(w * h * 8);
		
		w = 0x41;
		h = 0x41;
		long[] ground = new long[0x41 * 0x41];
		ground = memory.Longs(w * h);
//		for (int i = 0; i < w * h; i++) {
//			ground[i] = memory.nextReadLong();
//		}
		
		part.test = bytes;
		part.setVertexColor(color);
		part.setVertexHeight(height);
		part.setGroundMaterials(ground);
	}
}
