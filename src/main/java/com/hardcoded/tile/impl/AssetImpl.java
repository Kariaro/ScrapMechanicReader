package com.hardcoded.tile.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.hardcoded.tile.Asset;

/**
 * A asset implementation.
 * 
 * @author HardCoded
 */
public class AssetImpl extends TileEntityImpl implements Asset {
	public UUID uuid = DEFAULT_UUID;
	
	public final List<String> materials = new ArrayList<>();
	
	@Override
	public UUID getUUID() {
		return uuid;
	}
	
	@Override
	public List<String> getMaterials() {
		return materials;
	}
}
