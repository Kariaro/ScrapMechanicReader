package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.TileHeader.Header;
import com.hardcoded.tile.impl.TilePart;

/**
 * A tile reader interface.
 * 
 * @author HardCoded
 */
public interface TileReaderImpl {
	void read(Header header, Memory memory, TilePart part);
}
