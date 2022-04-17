package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.DecalImpl;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.tile.object.Decal;
import me.hardcoded.smreader.tile.object.Prefab;
import me.hardcoded.smreader.utils.TileUtils;

import java.util.List;
import java.util.UUID;

/**
 * <b>This writer is incomplete!</b>
 * 
 * @author HardCoded
 */
public class DecalWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		List<Decal> decals = part.decals;
		if (decals.isEmpty()) {
			header.decalCount = 0;
			return;
		}
		
		header.decalCount = decals.size();
		
		byte[] data = write(decals, part);
		byte[] compressed = TileUtils.compress_data(data);
		header.decalCompressedSize = compressed.length;
		header.decalSize = data.length;
		header.decalIndex = memory.index();
		memory.NextWriteBytes(compressed);
	}
	
	public byte[] write(List<Decal> decals, TilePart part) {
		final int tileVersion = part.getParent().getVersion();
		Memory memory = new Memory(0x10000);
		
		for (Decal decal : decals) {
			memory.NextWriteFloats(decal.getPosition().toArray());
			memory.NextWriteFloats(decal.getRotation().toArray());
			memory.NextWriteFloats(decal.getSize().toArray());
			memory.NextWriteUuid(decal.getUuid(), true);
			
			// TODO: Unknown bytes (color)
			memory.NextWriteBytes(new byte[8]);
		}
		
		memory.NextWriteByte(0);
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
