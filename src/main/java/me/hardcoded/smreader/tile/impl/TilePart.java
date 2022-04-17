package me.hardcoded.smreader.tile.impl;

import java.util.ArrayList;
import java.util.List;

import me.hardcoded.smreader.tile.data.ClutterData;
import me.hardcoded.smreader.tile.object.*;
import me.hardcoded.smreader.tile.Tile;

/**
 * An implementation of a tile.
 * 
 * <p>A tile contains multiple parts:
 * <pre>
 * * Mip
 * * Clutter
 * * AssetList
 * * Node
 * * Prefab
 * * BlueprintList
 * * Decal
 * * HarvestableList
 * </pre>
 * 
 * <p>These parts
 *  
 * @author HardCoded <https://github.com/Kariaro>
 * @since v0.1
 */
public class TilePart {
	private final Tile parent;
	
	// Mip
	public final float[] vertexHeight;
	public final int[] vertexColor;
	public final long[] ground;
	
	// Clutter
	public final byte[] clutter;
	// TODO: This is only used to correctly write UUID when writing tile files.
	//       It's currently not possible to update what clutters are used.
	public final List<ClutterData> clutterTest;
	
	// AssetList
	public final List<Asset>[] assets;
	
	// Node
	public final List<Node> nodes;
	
	public byte[] test;
	
	// Prefab
	public final List<Prefab> prefabs;
	
	// BlueprintList
	public final List<Blueprint> blueprints;
	
	// Decal
	public final List<Decal> decals;
	
	// HarvestableList
	public final List<Harvestable>[] harvestables;
	
	
	
	@SuppressWarnings("unchecked")
	protected TilePart(Tile parent) {
		this.parent = parent;
		
		vertexColor = new int[33 * 33];
		vertexHeight = new float[33 * 33];
		ground = new long[65 * 65];
		clutter = new byte[128 * 128];
		clutterTest = new ArrayList<>();
		
		assets = new List[] {
			new ArrayList<Asset>(),
			new ArrayList<Asset>(),
			new ArrayList<Asset>(),
			new ArrayList<Asset>()
		};
		
		nodes = new ArrayList<>();
		prefabs = new ArrayList<>();
		decals = new ArrayList<>();
		blueprints = new ArrayList<>();
		
		harvestables = new List[] {
			new ArrayList<Harvestable>(),
			new ArrayList<Harvestable>(),
			new ArrayList<Harvestable>(),
			new ArrayList<Harvestable>()
		};
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

	public void addAsset(Asset asset, int index) {
		if (asset == null) throw new NullPointerException("A tile cannot contain null assets");
		if (index < 0 || index > 3) throw new ArrayIndexOutOfBoundsException("Invalid asset index. The index must be one of [ 0, 1, 2, 3 ]");
		assets[index].add(asset);
	}
	
	public void addHarvestable(Harvestable harvestable, int index) {
		if (harvestable == null) throw new NullPointerException("A tile cannot contain null harvestables");
		if (index < 0 || index > 3) throw new ArrayIndexOutOfBoundsException("Invalid harvestables index. The index must be one of [ 0, 1, 2, 3 ]");
		harvestables[index].add(harvestable);
	}
	
	public void addNode(Node node) {
		if (node == null) throw new NullPointerException("A tile cannot contain null nodes");
		nodes.add(node);
	}
	
	public void addPrefab(Prefab prefab) {
		if (prefab == null) throw new NullPointerException("A tile cannot contain null prefabs");
		prefabs.add(prefab);
	}
	
	public void addBlueprint(Blueprint blueprint) {
		if (blueprint == null) throw new NullPointerException("A tile cannot contain null blueprints");
		blueprints.add(blueprint);
	}
	
	public void addDecal(Decal decal) {
		if (decal == null) throw new NullPointerException("A tile cannot contain null decals");
		decals.add(decal);
	}
	
	public Tile getParent() {
		return parent;
	}
}
