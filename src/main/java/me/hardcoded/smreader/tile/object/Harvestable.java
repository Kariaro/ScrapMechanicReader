package me.hardcoded.smreader.tile.object;

import java.util.UUID;

import me.hardcoded.smreader.utils.NotNull;

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
	
	/**
	 * Returns the color of this harvestable.
	 * @return the color of this harvestable
	 */
	int getColor();
	
	/**
	 * Set the color of this harvestable.
	 * @param color the new color
	 */
	void setColor(int color);
	
}
