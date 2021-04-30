package com.hardcoded.tile;

/**
 * A implementation of a tile in ScrapMechanic.
 * 
 * @author HardCoded <https://github.com/Kariaro>
 */
public class TilePart {
	// Mip
	public final float[] vertexHeight;
	public final int[] vertexColor;
	public final long[] ground;
	
	// Clutter
	public final byte[] clutter;
	
	// AssetList
	
	// Node
	
	// Prefab
	
	// BlueprintList
	
	// Decal
	
	// HarvestableList
	
	public TilePart() {
		vertexColor = new int[33 * 33];
		vertexHeight = new float[33 * 33];
		ground = new long[65 * 65];
		clutter = new byte[128 * 128];
	}

	public void setVertexColor(int[] array) {
		System.arraycopy(array, 0, this.vertexColor, 0, Math.min(array.length, this.vertexColor.length));
	}
	
	public void setVertexHeight(float[] array) {
		System.arraycopy(array, 0, this.vertexHeight, 0, Math.min(array.length, this.vertexHeight.length));
	}
	
	public void setGroundMaterials(long[] array) {
		System.arraycopy(array, 0, this.ground, 0, Math.min(array.length, this.ground.length));
	}
	
	public void setClutter(byte[] array) {
		System.arraycopy(array, 0, this.clutter, 0, Math.min(array.length, this.clutter.length));
	}
}
