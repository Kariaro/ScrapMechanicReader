package com.hardcoded.tile;

import java.util.List;
import java.util.UUID;

import com.hardcoded.tile.object.TileEntity;

/**
 * A asset interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public interface Asset extends TileEntity {
	UUID getUUID();
	
	List<String> getMaterials();
}
