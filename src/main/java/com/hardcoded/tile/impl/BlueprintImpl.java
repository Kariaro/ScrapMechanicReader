package com.hardcoded.tile.impl;

import com.hardcoded.math.Vec3;
import com.hardcoded.tile.object.Blueprint;

/**
 * A blueprint implementation.
 * 
 * @author HardCoded
 * @since v0.2
 */
public class BlueprintImpl extends TileEntityImpl implements Blueprint {
	private final boolean loaded;
	protected String value = "";
	
	public BlueprintImpl(boolean loaded) {
		this.loaded = loaded;
	}
	
	/**
	 * @deprecated This method is deprecated because blueprints are only containers and cannot be scaled
	 */
	@Override
	public Vec3 getSize() {
		return new Vec3(1, 1, 1);
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	@Override
	public String getValue() {
		return value;
	}
	
	@Override
	public void setValue(String value) {
		if(value == null) return;
		this.value = value;
	}
}
