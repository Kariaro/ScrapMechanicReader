package me.hardcoded.smreader.math;

/**
 * A three dimensional vector class.
 * 
 * @author HardCoded
 * @since v0.1
 */
public class Vec3 extends FloatVec<Vec3> {
	public Vec3() {
		super(3);
	}
	
	public Vec3(float... array) {
		super(3);
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
}
