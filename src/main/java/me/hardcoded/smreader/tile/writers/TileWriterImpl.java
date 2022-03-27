package me.hardcoded.smreader.tile.writers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * A tile writer interface.
 * 
 * @author HardCoded
 */
public interface TileWriterImpl {
	void write(CellHeader header, Memory memory, TilePart part);
}
