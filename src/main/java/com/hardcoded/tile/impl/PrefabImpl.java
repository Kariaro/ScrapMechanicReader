package com.hardcoded.tile.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.hardcoded.tile.object.Blueprint;
import com.hardcoded.tile.object.Node;
import com.hardcoded.tile.object.TilePrefab;

/**
 * A prefab implementation.
 * 
 * @author HardCoded
 * @since v0.2
 */
public class PrefabImpl extends TileEntityImpl implements TilePrefab {
	protected String flag = "";
	protected String path = "";
	
	// Blueprints
	protected final List<Blueprint> blueprints;
	
	// Prefabs
	protected final List<TilePrefab> prefabs;
	
	// Nodes
	protected final List<Node> nodes;
	
	
	public final List<String> blueprint_paths;
	public final List<String> prefabs_paths;
	
	/**
	 * If this value is {@code false} then it means that this object has not
	 * been loaded from the {@link com.hardcoded.prefab.readers.PrefabFileReader}.
	 */
	public final boolean is_loaded;
	
	public PrefabImpl() {
		this(false);
	}
	
	public PrefabImpl(boolean loaded) {
		blueprints = new ArrayList<>();
		prefabs = new ArrayList<>();
		nodes = new ArrayList<>();
		
		blueprint_paths = new ArrayList<>();
		prefabs_paths = new ArrayList<>();
		
		this.is_loaded = loaded;
	}
	
	@Override
	public String getPath() {
		return path;
	}
	
	@Override
	public String getFlag() {
		return flag;
	}
	
	@Override
	public void setPath(String path) {
		if(path == null) return;
		this.path = path;
	}
	
	@Override
	public void setFlag(String flag) {
		if(flag == null) return;
		this.flag = flag;
	}
	
	
	public void addBlueprint(Blueprint blueprint) {
		blueprints.add(Objects.requireNonNull(blueprint, "Prefab blueprint must not be null"));
	}
	
	public void addPrefab(TilePrefab prefab) {
		prefabs.add(Objects.requireNonNull(prefab, "Prefab prefab must not be null"));
	}
	
	public void addNode(Node node) {
		nodes.add(Objects.requireNonNull(node, "Prefab node must not be null"));
	}
}
