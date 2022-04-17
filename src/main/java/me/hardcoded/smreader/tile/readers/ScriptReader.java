package me.hardcoded.smreader.tile.readers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.utils.TileUtils;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class ScriptReader implements TileReaderImpl {
	
	@Override
	public void read(CellHeader h, Memory reader, TilePart part) {
		if ((h.scriptCount == 0) || (h.scriptIndex == 0)) return;
		reader.set(h.scriptIndex);
		
		TileUtils.log("  Script            : %d / %d", h.scriptSize, h.scriptCompressedSize);
		
		byte[] compressed = reader.Bytes(h.scriptCompressedSize);
		byte[] bytes = new byte[h.scriptSize];
		
		int debugSize = TileUtils.decompress_data(compressed, bytes, h.scriptSize);
		if (debugSize != h.scriptCompressedSize) {
			TileUtils.error("debugSize != h.scriptCompressedSize: %d != %d", debugSize, h.scriptCompressedSize);
		}
		
//		debugSize = read(bytes, part);
//		if (debugSize != h.scriptSize) {
//			TileUtils.error("debugSize != h.scriptSize: %d != %d", debugSize, h.scriptSize);
//		}
	}
	
	public int read(byte[] bytes, TilePart part) {
		return 0;
	}
}
