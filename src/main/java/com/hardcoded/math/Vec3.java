package com.hardcoded.math;

import java.util.Locale;

/**
 * A vec3 class.
 * 
 * @author HardCoded
 */
public class Vec3 {
	public float x;
	public float y;
	public float z;
	
	public Vec3() {
		
	}
	
	public Vec3(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public void set(Vec3 vec) {
		this.x = vec.x;
		this.y = vec.y;
		this.z = vec.z;
	}
	
	public void set(float[] array) {
		this.x = array[0];
		this.y = array[1];
		this.z = array[2];
	}
	
	public float[] toArray() {
		return new float[] { x, y, z };
	}
	
	public float getLength() {
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
	
	@Override
	public String toString() {
		return String.format(Locale.US, "{ %.6f, %.6f, %.6f }", x, y, z);
	}
}
