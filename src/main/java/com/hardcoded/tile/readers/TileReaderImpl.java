package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.CellHeader;
import com.hardcoded.tile.impl.TilePart;

/**
 * A tile reader interface.
 * 
 * @author HardCoded
 */
public interface TileReaderImpl {
	void read(CellHeader header, Memory memory, TilePart part);
}
