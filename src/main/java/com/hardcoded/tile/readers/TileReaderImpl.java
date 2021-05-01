package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.HeaderPart;
import com.hardcoded.tile.impl.TilePart;

/**
 * A tile reader interface.
 * 
 * @author HardCoded
 */
public interface TileReaderImpl {
	void read(HeaderPart header, Memory memory, TilePart part);
}
