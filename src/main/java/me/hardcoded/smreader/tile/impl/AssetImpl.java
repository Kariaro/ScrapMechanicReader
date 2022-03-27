package me.hardcoded.smreader.tile.impl;

import java.util.*;

import me.hardcoded.smreader.tile.object.Asset;

/**
 * A asset implementation.
 * 
 * @author HardCoded
 * @since v0.1
 */
public class AssetImpl extends TileEntityImpl implements Asset {
	public final Map<String, Integer> materials = new HashMap<>();
	public UUID uuid = DEFAULT_UUID;
	
	@Override
	public UUID getUuid() {
		return uuid;
	}
	
	@Override
	public void setUuid(UUID uuid) {
		this.uuid = Objects.requireNonNull(uuid, "An assets uuid must not be null");
	}
	
	@Override
	public Map<String, Integer> getMaterials() {
		return materials;
	}
}
