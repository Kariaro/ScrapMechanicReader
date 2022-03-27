package me.hardcoded.smreader.tile.impl;

import java.util.*;

import me.hardcoded.smreader.tile.object.Decal;

/**
 * A decal implementation.
 * 
 * @author HardCoded
 * @since v0.1
 */
public class DecalImpl extends TileEntityImpl implements Decal {
	public final Map<String, Integer> materials = new HashMap<>();
	public UUID uuid = DEFAULT_UUID;
	
	@Override
	public UUID getUuid() {
		return uuid;
	}
	
	@Override
	public void setUuid(UUID uuid) {
		this.uuid = Objects.requireNonNull(uuid, "A decal uuid must not be null");
	}
}
