package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.PrefabImpl;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.tile.object.Node;
import me.hardcoded.smreader.tile.object.Prefab;
import me.hardcoded.smreader.tile.readers.TileReaderImpl;
import me.hardcoded.smreader.utils.TileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * <b>This writer is incomplete!</b>
 * 
 * @author HardCoded
 */
public class PrefabWriter implements TileWriterImpl {
	
	@Override
	public void write(CellHeader header, Memory memory, TilePart part) {
		List<Prefab> prefabs = part.prefabs;
		if (prefabs.isEmpty()) {
			header.prefabCount = 0;
			return;
		}
		
		header.prefabCount = prefabs.size();
		
		byte[] data = write(prefabs, part);
		byte[] compressed = TileUtils.compress_data(data);
		header.prefabCompressedSize = compressed.length;
		header.prefabSize = data.length;
		header.prefabIndex = memory.index();
		memory.NextWriteBytes(compressed);
	}
	
	public byte[] write(List<Prefab> prefabs, TilePart part) {
		final int tileVersion = part.getParent().getVersion();
		Memory memory = new Memory(0x10000);
		
		for (Prefab prefab : prefabs) {
			memory.NextWriteFloats(prefab.getPosition().toArray());
			memory.NextWriteFloats(prefab.getRotation().toArray());
			
			if (tileVersion < 9) {
				memory.NextWriteFloats(new float[] { 1.0f, 1.0f, 1.0f });
			} else {
				memory.NextWriteFloats(prefab.getSize().toArray());
			}
			
			// TODO: What encoding should we use for writing strings???
			String prefabPath = prefab.getPath();
			memory.NextWriteInt(prefabPath.length());
			memory.NextWriteBytes(prefabPath.getBytes());
			
			String prefabFlag = prefab.getFlag();
			memory.NextWriteByte(prefabFlag.length());
			memory.NextWriteBytes(prefabFlag.getBytes());
			
			// Mystery bytes....
			memory.NextWriteInt(0);
		}
		
		byte[] bytes = new byte[memory.getHighestWrittenIndex()];
		System.arraycopy(memory.data(), 0, bytes, 0, bytes.length);
		return bytes;
	}
}
