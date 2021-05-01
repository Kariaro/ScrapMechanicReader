package com.hardcoded.tile.writers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.TileUtils;

/**
 * @author HardCoded
 */
public class ClutterWriter implements TileWriterImpl {
	
	@Override
	public void write(HeaderPart header, Memory memory, TilePart part) {
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
