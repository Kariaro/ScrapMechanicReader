package me.hardcoded.smreader.tile;

import java.util.UUID;

import me.hardcoded.smreader.game.GameContext;
import me.hardcoded.smreader.tile.impl.TilePart;
import me.hardcoded.smreader.utils.NotNull;

/**
 * A tile interface.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public interface Tile {
	static final UUID DEFAULT_UUID = UUID.fromString("00000000-0000-0000-0000-000000000000");
	
	/**
	 * Returns the current game context of this tile.
	 * @return the current game context
	 */
	@NotNull
	GameContext getContext();
	
	/**
	 * Set the current game context of this tile.
	 * <p>The game context is only used when reading.
	 * 
	 * @param context the new context
	 * @since v0.2
	 */
	void setContext(GameContext context);
	
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
	 * Returns the uuid of this tile.
	 * @return the uuid of this tile
	 */
	@NotNull
	UUID getUuid();
	
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
	void setUuid(UUID uuid);
	
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
