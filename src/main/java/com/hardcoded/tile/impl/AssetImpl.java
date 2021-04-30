package com.hardcoded.tile.impl;

import java.util.ArrayList;
import java.util.List;

import com.hardcoded.tile.Asset;

/**
 * A asset implementation
 * 
 * @author HardCoded
 */
public class AssetImpl implements Asset {
	public final float[] pos = new float[3];
	public final float[] quat = new float[4];
	public final float[] size = new float[3];
	public final List<String> materials = new ArrayList<>();
	
	@Override
	public float[] getPosition() {
		return pos;
	}
	
	@Override
	public float[] getQuat() {
		return quat;
	}
	
	@Override
	public float[] getSize() {
		return size;
	}
	
	@Override
	public List<String> getMaterials() {
		return materials;
	}
}
