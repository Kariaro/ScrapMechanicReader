package me.hardcoded.smreader.tile.object;

import java.util.UUID;

import me.hardcoded.smreader.utils.NotNull;

/**
 * A decal interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.2
 */
public interface Decal extends TileEntity {
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
}
