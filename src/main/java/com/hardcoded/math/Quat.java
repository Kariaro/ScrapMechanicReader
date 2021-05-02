package com.hardcoded.math;

import java.util.Locale;

/**
 * A quaternion object.
 * 
 * @author HardCoded
 */
public class Quat {
	public float x;
	public float y;
	public float z;
	public float w;
	
	public Quat() {
		
	}
	
	public Quat(float x, float y, float z, float w) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public void set(float[] array) {
		this.x = array[0];
		this.y = array[1];
		this.z = array[2];
		this.w = array[3];
	}
	
	public float[] toArray() {
		return new float[] { x, y, z, w };
	}
	
	public static Quat identity() {
		return new Quat(0, 0, 0, 1);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "{ %.6f, %.6f, %.6f, %.6f }", x, y, z, w);
	}
}
