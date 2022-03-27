package me.hardcoded.smreader.tile.readers;

import me.hardcoded.smreader.data.Memory;
import me.hardcoded.smreader.tile.CellHeader;
import me.hardcoded.smreader.tile.impl.TilePart;

/**
 * A tile reader interface.
 * 
 * @author HardCoded
 */
public interface TileReaderImpl {
	void read(CellHeader header, Memory memory, TilePart part);
}
