package com.hardcoded.tile.object;

import java.util.Map;
import java.util.UUID;

import com.hardcoded.utils.NotNull;

/**
 * A asset interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.1
 */
public interface Asset extends TileEntity {
	/**
	 * Returns the uuid of this asset.
	 * @return the uuid of this asset
	 */
	@NotNull
	UUID getUuid();
	
	/**
	 * Set the uuid of this asset.
	 * @param uuid the new uuid
	 */
	void setUuid(UUID uuid);
	
	/**
	 * Returns a map that contains the material colors for this asset.
	 * @return a map that contains the material colors for this asset
	 */
	@NotNull
	Map<String, Integer> getMaterials();
}
