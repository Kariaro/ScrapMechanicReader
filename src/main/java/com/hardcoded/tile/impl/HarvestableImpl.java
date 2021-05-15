package com.hardcoded.tile.impl;

import java.util.Objects;
import java.util.UUID;

import com.hardcoded.tile.object.Harvestable;

/**
 * A harvestable implementation.
 * 
 * @author HardCoded
 * @since v0.2
 */
public class HarvestableImpl extends TileEntityImpl implements Harvestable {
	public UUID uuid = DEFAULT_UUID;
	public int color;
	
	@Override
	public UUID getUuid() {
		return uuid;
	}
	
	@Override
	public void setUuid(UUID uuid) {
		this.uuid = Objects.requireNonNull(uuid, "A harvestables uuid must not be null");
	}
	
	@Override
	public int getColor() {
		return color;
	}
	
	@Override
	public void setColor(int color) {
		this.color = color;
	}
}
