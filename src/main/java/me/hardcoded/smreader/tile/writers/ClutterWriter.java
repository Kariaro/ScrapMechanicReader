package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.data.ClutterData;
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
		Memory memory = new Memory(128 * 128 + 1 + 1000);
		
		{
			if (part.clutterTest.isEmpty()) {
				memory.NextWriteByte(0); // no uuid's
			} else {
				memory.NextWriteByte(part.clutterTest.size());
				memory.NextWriteByte(0); // TODO: Flags?????
				
				for (ClutterData test : part.clutterTest) {
					memory.NextWriteBytes(test.bytes);
				}
			}
			// TODO: Figure out how to write clutter ????
		}
		byte[] temp = new byte[128 * 128];
		System.arraycopy(part.clutter, 0, temp, 0, part.clutter.length);
//		for (int i = 0; i < temp.length; i++) {
//			temp[i] = (byte)0xff;
//		}
		
		memory.NextWriteBytes(temp); // Empty clutter array
//		byte[] next = new byte[128 * 128];
//		{
//			int offset = 0;
//			
//			for (int i = 0; i < 128 * 128; i++) {
//				byte read = memory.Byte(1 + i + offset);
//				next[i] = (byte)read;
//			}
//		}
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
