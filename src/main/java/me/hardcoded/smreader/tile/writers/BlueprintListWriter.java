package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.error.TileException;
import me.hardcoded.smreader.error.TileWriteException;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.BlueprintImpl;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.tile.object.Blueprint;
import me.hardcoded.smreader.tile.object.Node;
import me.hardcoded.smreader.tile.readers.TileReaderImpl;
import me.hardcoded.smreader.utils.TileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b>This reader is incomplete!</b>
 * 
 * @author HardCoded
 */
public class BlueprintListWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		List<Blueprint> blueprints = part.blueprints;
		if (blueprints.isEmpty()) {
			header.blueprintListCount = 0;
			return;
		}
		
		header.blueprintListCount = blueprints.size();
		
		byte[] data = write(blueprints, part);
		byte[] compressed = TileUtils.compress_data(data);
		header.blueprintListCompressedSize = compressed.length;
		header.blueprintListSize = data.length;
		header.blueprintListIndex = memory.index();
		memory.NextWriteBytes(compressed);
	}
	
	public byte[] write(List<Blueprint> blueprints, TilePart part) {
		final int tileVersion = part.getParent().getVersion();
		Memory memory = new Memory(0x10000);
		
		for (Blueprint blueprint : blueprints) {
			memory.NextWriteFloats(blueprint.getPosition().toArray());
			memory.NextWriteFloats(blueprint.getRotation().toArray());
			
			String blueprintValue = blueprint.getValue();
			if(tileVersion < 7) {
				if(blueprintValue.length() > 255) {
					throw new TileWriteException("Tile version 7 only supports blueprints with a length less than 256");
				}
				
				memory.NextWriteByte(blueprintValue.length());
				
				// TODO: Make sure this is the same encoding as what the game uses
				memory.NextWriteBytes(blueprintValue.getBytes());
			} else {
				memory.NextWriteInt(blueprintValue.length());
				memory.NextWriteBytes(blueprintValue.getBytes());
			}
		}
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
