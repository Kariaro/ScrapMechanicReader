package com.hardcoded.tile.object;

import java.util.UUID;

import com.hardcoded.utils.NotNull;

/**
 * A harvestable interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.2
 */
public interface Harvestable extends TileEntity {
	/**
	 * Returns the uuid of this harvestable.
	 * @return the uuid of this harvestable
	 */
	@NotNull
	UUID getUuid();
	
	/**
	 * Set the uuid of this harvestable.
	 * @param uuid the new uuid
	 */
	void setUuid(UUID uuid);
}
