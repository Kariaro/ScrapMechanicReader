package com.hardcoded.tile.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.hardcoded.tile.object.Asset;

/**
 * A asset implementation.
 * 
 * @author HardCoded
 */
public class AssetImpl extends TileEntityImpl implements Asset {
	public final Map<String, Integer> materials = new HashMap<>();
	public UUID uuid = DEFAULT_UUID;
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public Map<String, Integer> getMaterials() {
		return materials;
	}
}
