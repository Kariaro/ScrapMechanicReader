package com.hardcoded.tile.writers;

import com.hardcoded.data.Memory;
import com.hardcoded.tile.impl.TilePart;

/**
 * A tile writer interface.
 * 
 * @author HardCoded
 */
public interface TileWriterImpl {
	void write(HeaderPart header, Memory memory, TilePart part);
}
