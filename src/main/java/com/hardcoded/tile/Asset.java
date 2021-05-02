package com.hardcoded.tile;

import java.util.Map;
import java.util.UUID;

import com.hardcoded.tile.object.TileEntity;
import com.hardcoded.utils.NotNull;

/**
 * A asset interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public interface Asset extends TileEntity {
	/**
	 * Returns the uuid of this asset.
	 * @return the uuid of this asset
	 */
	@NotNull
	UUID getUUID();
	
	/**
	 * Returns a map that contains the material colors for this asset.
	 * @return a map that contains the material colors for this asset
	 */
	@NotNull
	Map<String, Integer> getMaterials();
}
