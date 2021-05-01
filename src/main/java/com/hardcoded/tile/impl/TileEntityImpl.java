package com.hardcoded.tile.impl;

import com.hardcoded.math.Quat;
import com.hardcoded.math.Vec3;
import com.hardcoded.tile.object.TileEntity;

public class TileEntityImpl implements TileEntity {
	public final Vec3 pos = new Vec3();
	public final Quat rot = new Quat();
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
		pos.x = x;
		pos.y = y;
		pos.z = z;
	}
	
	@Override
	public void setPosition(Vec3 vec) {
		pos.x = vec.x;
		pos.y = vec.y;
		pos.z = vec.z;
	}
	
	@Override
	public void setRotation(float x, float y, float z, float w) {
		rot.x = x;
		rot.y = y;
		rot.z = z;
		rot.w = w;
	}
	
	@Override
	public void setRotation(Quat quat) {
		rot.x = quat.x;
		rot.y = quat.y;
		rot.z = quat.z;
		rot.w = quat.w;
	}
	
	@Override
	public void setSize(float x, float y, float z) {
		size.x = x;
		size.y = y;
		size.z = z;
	}
	
	@Override
	public void setSize(Vec3 vec) {
		size.x = vec.x;
		size.y = vec.y;
		size.z = vec.z;
	}
}
