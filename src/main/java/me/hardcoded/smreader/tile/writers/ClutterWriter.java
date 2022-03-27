package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.utils.TileUtils;

/**
 * <b>This writer is incomplete!</b>
 * 
 * @author HardCoded
 */
public class ClutterWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		byte[] data = write(part);
		
		byte[] compressed = TileUtils.compress_data(data);
		header.clutterCompressedSize = compressed.length;
		header.clutterSize = data.length;
		header.clutterIndex = memory.index();
		memory.NextWriteBytes(compressed);
	}
	
	public byte[] write(TilePart part) {
		Memory memory = new Memory(128 * 128 + 1);
		memory.NextWriteByte(0); // no uuid's
		
		byte[] temp = new byte[128 * 128];
		for(int i = 0; i < temp.length; i++) {
			temp[i] = (byte)0xff;
		}
		
		memory.NextWriteBytes(temp); // Empty clutter array
//		byte[] next = new byte[128 * 128];
//		{
//			int offset = 0;
//			
//			for(int i = 0; i < 128 * 128; i++) {
//				byte read = memory.Byte(1 + i + offset);
//				next[i] = (byte)read;
//			}
//		}
		
		return memory.data();
	}
}
