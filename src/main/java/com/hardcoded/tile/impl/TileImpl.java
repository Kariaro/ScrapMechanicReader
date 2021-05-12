package com.hardcoded.tile.impl;

import java.util.UUID;

import com.hardcoded.error.TileException;
import com.hardcoded.tile.Tile;

/**
 * A tile implementation.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public class TileImpl implements Tile {
	private UUID uuid = DEFAULT_UUID;
	private int version;
	
	/**
	 * <pre>
	 * 0: Terrain
	 * 1: Dungeon
	 * </pre>
	 */
	private int type;
	private long creatorId;
	
	private int width;
	private int height;
	private TilePart[] tiles;
	
	public TileImpl(int width, int height) {
		this.width = width;
		this.height = height;
		this.tiles = new TilePart[width * height];
		for(int i = 0; i < tiles.length; i++) {
			tiles[i] = new TilePart(this);
		}
	}
	
	@Override
	public int getWidth() {
		return width;
	}
	
	@Override
	public int getHeight() {
		return height;
	}
	
	@Override
	public int getVersion() {
		return version;
	}
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public int getTileType() {
		return type;
	}
	
	@Override
	public long getCreatorId() {
		return creatorId;
	}
	
	
	@Override
	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	public void setUUID(UUID uuid) {
		if(uuid == null) throw new NullPointerException("The uuid of a tile cannot be null");
		this.uuid = uuid;
	}
	
	@Override
	public void setTileType(int type) {
		this.type = type;
	}
	
	@Override
	public void setCreatorId(long creatorId) {
		this.creatorId = creatorId;
	}
	
	@Override
	public void resize(int width, int height) {
		if(width < 1 || height < 1) throw new TileException("The length of a tile must not be less than one");
		
		TilePart[] array = new TilePart[width * height];
		
		int min_height = Math.min(height, this.height);
		int min_width = Math.min(width, this.width);
		for(int y = 0; y < min_height; y++) {
			for(int x = 0; x < min_width; x++) {
				array[x + y * width] = getPart(x, y);
			}
		}
		
		for(int i = 0; i < array.length; i++) {
			if(array[i] == null) array[i] = new TilePart(this);
		}
		
		this.width = width;
		this.height = height;
		this.tiles = array;
	}
	
	@Override
	public float[] getVertexHeight() {
		int w = width * 32 + 1;
		int h = height * 32 + 1;
		float[] result = new float[w * h];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				TilePart part = getPart(x, y);
				
				float[] array = part.vertexHeight;
				int idx = x * 32 + y * 32 * h;
				for(int i = 0; i < 32; i++) {
					System.arraycopy(array, i * 33, result, idx + i * w , 32);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public int[] getVertexColor() {
		int w = width * 32 + 1;
		int h = height * 32 + 1;
		int[] result = new int[w * h];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				TilePart part = getPart(x, y);
				
				int[] array = part.vertexColor;
				int idx = x * 32 + y * 32 * h;
				for(int i = 0; i < 32; i++) {
					System.arraycopy(array, i * 33, result, idx + i * w , 32);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public byte[] getClutter() {
		int w = width * 128;
		int h = height * 128;
		byte[] result = new byte[w * h];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				TilePart part = getPart(x, y);
				
				byte[] array = part.clutter;
				int idx = x * 128 + y * 128 * h;
				for(int i = 0; i < 128; i++) {
					System.arraycopy(array, i * 128, result, idx + i * w , 128);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public long[] getGround() {
		int w = width * 64 + 1;
		int h = height * 64 + 1;
		long[] result = new long[w * h];
		
		for(int y = 0; y < height; y++) {
			for(int x = 0; x < width; x++) {
				TilePart part = getPart(x, y);
				
				long[] array = part.ground;
				int idx = x * 64 + y * 64 * h;
				for(int i = 0; i < 64; i++) {
					System.arraycopy(array, i * 65, result, idx + i * w, 64);
				}
			}
		}
		
		return result;
	}
	
	@Override
	public TilePart getPart(int x, int y) {
		return tiles[x + y * width];
	}
	
	public void setPart(int x, int y, TilePart part) {
		if(part == null) throw new NullPointerException("TilePart must not be null");
		tiles[x + y * width] = part;
	}
}
