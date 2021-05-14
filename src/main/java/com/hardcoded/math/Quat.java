package com.hardcoded.math;

/**
 * A quaternion object.
 * 
 * @author HardCoded
 * @since v0.1
 */
public class Quat extends FloatVec<Quat> {
	public Quat() {
		super(4);
	}
	
	public Quat(float x, float y, float z, float w) {
		super(4);
		set(x, y, z, w);
	}
	
	public Quat(float... array) {
		super(4);
		set(array);
	}
	
	public float getX() {
		return array[0];
	}
	
	public float getY() {
		return array[1];
	}
	
	public float getZ() {
		return array[2];
	}
	
	public float getW() {
		return array[3];
	}
	
	public static Quat identity() {
		return new Quat(0, 0, 0, 1);
	}
}
