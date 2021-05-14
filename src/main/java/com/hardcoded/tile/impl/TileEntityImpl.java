package com.hardcoded.tile.impl;

import com.hardcoded.math.Quat;
import com.hardcoded.math.Vec3;
import com.hardcoded.tile.object.TileEntity;

/**
 * A tile entity implementation.
 * 
 * @author HardCoded
 * @since v0.1
 */
public abstract class TileEntityImpl implements TileEntity {
	public final Vec3 pos = new Vec3();
	public final Quat rot = Quat.identity();
	public final Vec3 size = new Vec3();
	
	@Override
	public Vec3 getPosition() {
		return pos;
	}
	
	@Override
	public Quat getRotation() {
		return rot;
	}
	
	@Override
	public Vec3 getSize() {
		return size;
	}
	
	@Override
	public void setPosition(float x, float y, float z) {
		pos.set(x, y, z);
	}
	
	@Override
	public void setPosition(float... array) {
		pos.set(array);
	}
	
	@Override
	public void setPosition(Vec3 vec) {
		pos.set(vec);
	}
	
	@Override
	public void setRotation(float x, float y, float z, float w) {
		rot.set(x, y, z, w);
	}
	
	@Override
	public void setRotation(float... array) {
		rot.set(array);
	}
	
	@Override
	public void setRotation(Quat quat) {
		rot.set(quat);
	}
	
	@Override
	public void setSize(float x, float y, float z) {
		size.set(x, y, z);
	}
	
	@Override
	public void setSize(float... array) {
		size.set(array);
	}
	
	@Override
	public void setSize(Vec3 vec) {
		size.set(vec);
	}
}
