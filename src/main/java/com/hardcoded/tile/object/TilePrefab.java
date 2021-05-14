package com.hardcoded.tile.object;

import com.hardcoded.utils.NotNull;

/**
 * A prefab interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.2
 */
public interface TilePrefab extends TileEntity {
	/**
	 * Returns the path of this prefab.
	 * @return the path of this prefab
	 */
	@NotNull
	String getPath();
	
	/**
	 * Returns the flag of this prefab.
	 * @return the falg of this prefab
	 */
	@NotNull
	String getFlag();
	
	/**
	 * Set the path of this prefab.
	 * @param path the new path
	 */
	void setPath(String path);
	
	/**
	 * Set the flag of this prefab.
	 * @param flag the new flag
	 */
	void setFlag(String flag);
}
