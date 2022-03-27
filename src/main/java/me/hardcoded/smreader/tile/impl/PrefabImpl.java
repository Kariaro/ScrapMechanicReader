package me.hardcoded.smreader.tile.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import me.hardcoded.smreader.error.TileException;
import com.hardcoded.tile.object.*;
import me.hardcoded.smreader.prefab.readers.PrefabFileReader;
import me.hardcoded.smreader.tile.object.*;
import me.hardcoded.tile.object.*;

/**
 * A prefab implementation.
 * 
 * @author HardCoded
 * @since v0.2
 */
public class PrefabImpl extends TileEntityImpl implements Prefab {
	protected String flag = "";
	protected String path = "";
	
	// Blueprints
	protected final List<Blueprint> blueprints;
	
	// Prefabs
	protected final List<Prefab> prefabs;
	
	// Nodes
	protected final List<Node> nodes;
	
	// Assets
	protected final List<Asset> assets;
	
	
	public final List<String> blueprint_paths;
	public final List<String> prefabs_paths;
	
	/**
	 * If this value is {@code false} then it means that this object has not
	 * been loaded from the {@link PrefabFileReader}.
	 */
	private final boolean loaded;
	
	public PrefabImpl() {
		this(false);
	}
	
	public PrefabImpl(boolean loaded) {
		blueprints = new ArrayList<>();
		prefabs = new ArrayList<>();
		nodes = new ArrayList<>();
		assets = new ArrayList<>();
		
		blueprint_paths = new ArrayList<>();
		prefabs_paths = new ArrayList<>();
		
		this.loaded = loaded;
	}
	
	
	@Override
	public boolean isLoaded() {
		return loaded;
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
		if (flag == null) return;
		this.flag = flag;
	}
	
	private void checkModification() {
		if (!loaded) {
			throw new TileException("You cannot modify the entity contents of an unloaded prefab");
		}
	}
	
	public void addBlueprint(Blueprint blueprint) {
		checkModification();
		blueprints.add(Objects.requireNonNull(blueprint, "Prefab blueprint must not be null"));
	}
	
	public void addPrefab(Prefab prefab) {
		checkModification();
		prefabs.add(Objects.requireNonNull(prefab, "Prefab prefab must not be null"));
	}
	
	public void addNode(Node node) {
		checkModification();
		nodes.add(Objects.requireNonNull(node, "Prefab node must not be null"));
	}
	
	public void addAsset(Asset prefab) {
		checkModification();
		assets.add(Objects.requireNonNull(prefab, "Prefab assets must not be null"));
	}
	
	@Override
	public List<Blueprint> getBlueprints() {
		return blueprints;
	}
	
	@Override
	public List<Prefab> getPrefabs() {
		return prefabs;
	}
	
	@Override
	public List<Node> getNodes() {
		return nodes;
	}
	
	@Override
	public List<Harvestable> getHarvestables() {
		return List.of();
	}
	
	@Override
	public List<Asset> getAssets() {
		return assets;
	}
}
