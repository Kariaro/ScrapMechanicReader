package com.hardcoded.tile;

import java.util.UUID;

import com.hardcoded.tile.impl.TilePart;
import com.hardcoded.utils.NotNull;

/**
 * A tile interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public interface Tile {
	static final UUID DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	
	/**
	 * Returns the width of this tile.
	 * @return the width of this tile
	 */
	int getWidth();
	
	/**
	 * Returns the height of this tile.
	 * @return the height of this tile
	 */
	int getHeight();
	
	/**
	 * Returns the version of this tile.
	 * @return the version of this tile
	 */
	int getVersion();
	
	/**
	 * Returns the UUID of this tile.
	 * @return the UUID of this tile
	 */
	@NotNull
	UUID getUUID();
	
	/**
	 * Returns the type of this tile.
	 * @return the type of this tile
	 */
	int getTileType();
	
	/**
	 * Returns the creatorId of this tile.
	 * @return the creatorId of this tile
	 */
	long getCreatorId();
	
	/**
	 * Set the version of this tile.
	 * @param version the new version
	 */
	void setVersion(int version);
	
	/**
	 * Set the uuid of this tile.
	 * @param uuid the new uuid
	 */
	void setUUID(UUID uuid);
	
	/**
	 * Set the type of this tile.
	 * @param type the new type
	 */
	void setTileType(int type);
	
	/**
	 * Set the creatorId of this tile.
	 * @param creatorId the new creatorId
	 */
	void setCreatorId(long creatorId);
	
	/**
	 * Resize this tile.
	 * 
	 * @param width the new width of the tile
	 * @param height the new height of the tile
	 */
	void resize(int width, int height);
	
	/**
	 * Returns a combined array of all height data in this tile.
	 * 
	 * <p>The width of this array is:<br>
	 * {@code 0x20 * tile.getWidth() + 1}
	 * 
	 * @return a combined array of all height data
	 */
	@NotNull
	float[] getVertexHeight();
	
	/**
	 * Returns a combined array of all color data in this tile.
	 * 
	 * <p>The width of this array is:<br>
	 * {@code 0x20 * tile.getWidth() + 1}
	 * 
	 * @return a combined array of all color data
	 */
	@NotNull
	int[] getVertexColor();
	
	/**
	 * Returns a combined array of all clutter data in this tile.
	 * 
	 * <p>The width of this array is:<br>
	 * {@code 0x80 * tile.getWidth() + 1}
	 * 
	 * @return a combined array of all color data
	 */
	@NotNull
	byte[] getClutter();
	
	/**
	 * Returns a combined array of all ground material data in this tile.
	 * 
	 * <p>The width of this array is:<br>
	 * {@code 0x40 * tile.getWidth() + 1}
	 * 
	 * @return a combined array of all ground material data
	 */
	@NotNull
	long[] getGround();
	
	/**
	 * Returns the sub tile at the specified position in this tile.
	 * @param x the x coordinate
	 * @param y the y coordinate
	 * @return a sub tile at the specified position
	 */
	@NotNull
	TilePart getPart(int x, int y);
}
