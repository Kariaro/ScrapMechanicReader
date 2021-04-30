package com.hardcoded.tile.readers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.TilePart;
import com.hardcoded.tile.TileHeader.Header;

/**
 * A tile reader interface.
 * 
 * @author HardCoded
 */
public interface TileReaderImpl {
	void read(Header header, Memory memory, TilePart part);
}
